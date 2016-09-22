//ref : http://simonsarris.com/blog/510-making-html5-canvas-useful
//		http://www.ibm.com/developerworks/library/wa-canvashtml5layering/

//TODO : une méthode draw par objet affiché ?
var myState = {'dragging':false}

function mkCanvas(id) {
	var canvas = {id:id, loaders:[], images:{}, cellSize:32, width:32*28, height:32*18}
	
	canvas.loaders.push(loadSprite(canvas, 'images/sprites/textures32.png', 'textures'));
	canvas.loaders.push(loadSprite(canvas, 'images/sprites/pnj.png', 'pnj'));
	canvas.loaders.push(loadSprite(canvas, 'images/sprites/sprite_sacha.png', 'sacha'));
	
	canvas.ctx = document.getElementById(id).getContext("2d");
	
	
	//var s = new CanvasState(document.getElementById('game-layer'));
	
	return canvas
}

function draw(canvas, x, y, name) {
	switch (name) {
	case 'empty': drawGrass(canvas, x, y);
	break;
	case 'water': drawWater(canvas, x, y);
	break;
	case 'rock': drawRock(canvas, x, y);
	break;
	case 'city': drawCity(canvas, x, y);
	break;
	case 'off': drawOff(canvas, x, y);
	break;
	case 'dweller': drawDweller(canvas, x, y);
	break;
	default:console.log("error draw " + name)
	}
}

function drawGrass (canvas, x, y) {
	drawTexture(canvas.ctx, canvas.images.textures, 0, 0, x, y)
}

function drawWater (canvas, x, y) {
	drawTexture(canvas.ctx, canvas.images.textures, 32, 0, x, y)
}

function drawRock (canvas, x, y) {
	drawTexture(canvas.ctx, canvas.images.textures, 64, 0, x, y)
}

function drawCity (canvas, x, y) {
	drawTexture(canvas.ctx, canvas.images.textures, 96, 0, x, y)
}

function drawOff (canvas, x, y) {
	drawTexture(canvas.ctx, canvas.images.textures, 128, 0, x, y)
}

function drawDweller (canvas, x, y) {
	drawTexture(canvas.ctx, canvas.images.pnj, 0, 0, x, y)
}

//scX, scY : start clipping => the part of the image to use
//x, y : pos on the canvas => where to display the part
function drawTexture (ctx, image, scX, scY, x, y) {
	// 32 width/height clipping => the size of the part
	// 32 for width/height : size of the part on the canvas => strech if necessary, ie different of wc / hc
	ctx.drawImage(image, scX, scY, 32, 32, x, y, 32, 32);
}

function loadSprite(canvas, src, name) {
	var deferred = $.Deferred();
	var sprite = new Image();
	sprite.onload = function() {
		deferred.resolve();
	};
	sprite.src = src;
	canvas.images[name] = sprite
	return deferred.promise();
}


var sprites = {}

function Pnj(x, y, w, h, sprite) {
	this.x = x || 0;
	this.y = y || 0;
	this.w = w || 32;
	this.h = h || 32;
	this.sprite = sprite || sprites.pnj;
}

// Draws this pnj to a given context
Pnj.prototype.draw = function(ctx, dragging) {
	var pnj = this
	ctx.save();
	if (dragging) {
		ctx.globalAlpha = 0.5;
	}
	ctx.drawImage(pnj.sprite, 0, 0, pnj.w, pnj.h, pnj.x, pnj.y, pnj.w, pnj.h);
	ctx.restore()
}

// Determine if a point is inside the shape's bounds
Pnj.prototype.contains = function(mx, my) {
	// All we have to do is make sure the Mouse X,Y fall in the area between
	// the shape's X and (X + Height) and its Y and (Y + Height)
	return	(this.x <= mx) && (this.x + this.w >= mx) &&
					(this.y <= my) && (this.y + this.h >= my);
}

function CanvasState(canvas) {
	
	this.loaders = []
	this.sprites = {}
	this.cellSize = 32
	this.width = 32*28
	this.height = 32*18
	
	this.loadSprite('images/sprites/pnj.png', 'pnj')
	
	// **** First some setup! ****
	
	this.canvas = canvas;
	this.width = canvas.width;
	this.height = canvas.height;
	this.ctx = canvas.getContext('2d');
	
	// This complicates things a little but but fixes mouse co-ordinate problems
	// when there's a border or padding. See getMouse for more detail
	var stylePaddingLeft, stylePaddingTop, styleBorderLeft, styleBorderTop;
	if (document.defaultView && document.defaultView.getComputedStyle) {
		this.stylePaddingLeft = parseInt(document.defaultView.getComputedStyle(canvas, null)['paddingLeft'], 10)			|| 0;
		this.stylePaddingTop	= parseInt(document.defaultView.getComputedStyle(canvas, null)['paddingTop'], 10)			 || 0;
		this.styleBorderLeft	= parseInt(document.defaultView.getComputedStyle(canvas, null)['borderLeftWidth'], 10)	|| 0;
		this.styleBorderTop	 = parseInt(document.defaultView.getComputedStyle(canvas, null)['borderTopWidth'], 10)	 || 0;
	}
	// Some pages have fixed-position bars (like the stumbleupon bar) at the top or left of the page
	// They will mess up mouse coordinates and this fixes that
	var html = document.body.parentNode;
	this.htmlTop = html.offsetTop;
	this.htmlLeft = html.offsetLeft;

	// **** Keep track of state! ****
	
	this.valid = false; // when set to false, the canvas will redraw everything
	this.pnjs = [];	// the collection of pnj to be drawn
	this.dragging = false; // Keep track of when we are dragging
	// the current selected object. In the future we could turn this into an array for multiple selection
	this.selection = null;
	this.dragoffx = 0; // See mousedown and mousemove events for explanation
	this.dragoffy = 0;
	
	// **** Then events! ****
	
	// This is an example of a closure!
	// Right here "this" means the CanvasState. But we are making events on the Canvas itself,
	// and when the events are fired on the canvas the variable "this" is going to mean the canvas!
	// Since we still want to use this particular CanvasState in the events we have to save a reference to it.
	// This is our reference!
	var myState = this;
	
	//fixes a problem where double clicking causes text to get selected on the canvas
	canvas.addEventListener('selectstart', function(e) { e.preventDefault(); return false; }, false);
	
	// Up, down, and move are for dragging
	canvas.addEventListener('mousedown', function(e) {
		var mouse = myState.getMouse(e);
		var mx = mouse.x;
		var my = mouse.y;
		var pnjs = myState.pnjs; //concat with other things to draw
		var l = pnjs.length;
		for (var i = l-1; i >= 0; i--) {
			if (pnjs[i].contains(mx, my)) {
				var mySel = pnjs[i];
				// Keep track of where in the object we clicked
				// so we can move it smoothly (see mousemove)
				myState.dragoffx = mx - mySel.x;
				myState.dragoffy = my - mySel.y;
				myState.dragging = true;
				myState.selection = mySel;
				myState.valid = false;
				return;
			}
		}
		// havent returned means we have failed to select anything.
		// If there was an object selected, we deselect it
		if (myState.selection) {
			myState.selection = null;
			myState.valid = false; // Need to clear the old selection border
		}
	}, true);
	
	canvas.addEventListener('mousemove', function(e) {
		if (myState.dragging){
			var mouse = myState.getMouse(e);
			// We don't want to drag the object by its top-left corner, we want to drag it
			// from where we clicked. Thats why we saved the offset and use it here
			myState.selection.x = mouse.x - myState.dragoffx;
			myState.selection.y = mouse.y - myState.dragoffy;	 
			myState.valid = false; // Something's dragging so we must redraw
		}
	}, true);
	
	canvas.addEventListener('mouseup', function(e) {
		myState.dragging = false;
	}, true);
	
	// double click for making new shapes
	canvas.addEventListener('dblclick', function(e) {
		var mouse = myState.getMouse(e);
		myState.addPnj(new Pnj(mouse.x - 10, mouse.y - 10, 32, 32, myState.sprites.pnj));
	}, true);
	
	// **** Options! ****
	this.selectionColor = '#CC0000';
	this.selectionWidth = 2;	
	this.interval = 30;
	setInterval(function() { myState.draw(); }, myState.interval);
}

CanvasState.prototype.loadSprite = function (src, name) {
	var deferred = $.Deferred();
	var sprite = new Image();
	sprite.onload = function() {
		deferred.resolve();
	};
	sprite.src = src;
	this.sprites[name] = sprite
	this.loaders.push(deferred.promise());
}

CanvasState.prototype.addPnj = function(pnj) {
	this.pnjs.push(pnj);
	this.valid = false;
}

CanvasState.prototype.clear = function() {
	this.ctx.clearRect(0, 0, this.width, this.height);
}

// While draw is called as often as the INTERVAL variable demands,
// It only ever does something if the canvas gets invalidated by our code
CanvasState.prototype.draw = function() {
	// if our state is invalid, redraw and validate!
	if (!this.valid) {
		var ctx = this.ctx;
		var pnjs = this.pnjs; // concat with other thing to draw
		this.clear();
		
		// ** Add stuff you want drawn in the background all the time here **
		
		// draw all shapes
		var l = pnjs.length;
		for (var i = 0; i < l; i++) {
			var pnj = pnjs[i];
			// We can skip the drawing of elements that have moved off the screen:
		var thisPnj = false || this.selection != null && this.selection == pnj;
			if (thisPnj || pnj.x > this.width || pnj.y > this.height ||
					pnj.x + pnj.w < 0 || pnj.y + pnj.h < 0) continue;
			pnjs[i].draw(ctx, false);
		}
		
		// draw selection
		if (this.selection != null) {
			this.selection.draw(ctx, true);
		}
		
		// ** Add stuff you want drawn on top all the time here **
		
		this.valid = true;
	}
}


// Creates an object with x and y defined, set to the mouse position relative to the state's canvas
// If you wanna be super-correct this can be tricky, we have to worry about padding and borders
CanvasState.prototype.getMouse = function(e) {
	var element = this.canvas, offsetX = 0, offsetY = 0, mx, my;
	
	// Compute the total offset
	if (element.offsetParent !== undefined) {
		do {
			offsetX += element.offsetLeft;
			offsetY += element.offsetTop;
		} while ((element = element.offsetParent));
	}

	// Add padding and border style widths to offset
	// Also add the <html> offsets in case there's a position:fixed bar
	offsetX += this.stylePaddingLeft + this.styleBorderLeft + this.htmlLeft;
	offsetY += this.stylePaddingTop + this.styleBorderTop + this.htmlTop;

	mx = e.pageX - offsetX;
	my = e.pageY - offsetY;
	
	// We return a simple javascript object (a hash) with x and y defined
	return {x: mx, y: my};
}

// If you dont want to use <body onLoad='init()'>
// You could uncomment this init() reference and place the script reference inside the body tag
//init();

function init() {
	var s = new CanvasState(document.getElementById('game-layer'));
}

$(document).ready(init)

