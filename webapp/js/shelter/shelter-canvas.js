
function mkCanvas(id) {
	var canvas = {id:id, loaders:[], images:{}}
	
	canvas.loaders.push(loadSprite(canvas, 'images/textures32.png', 'textures'));
	canvas.loaders.push(loadSprite(canvas, 'images/sprites/sprite_sacha.png', 'sacha'));
	
	canvas.ctx = document.getElementById(id).getContext("2d");
	
	return canvas
}

function drawAll(canvas) {
	
	$.when.apply(null, canvas.loaders).done(function() {
		
		var canvasXsize = $("#" + canvas.id).width()
		var canvasYsize = $("#" + canvas.id).height()
		
		for (var x=0; x<canvasXsize; x+=32) {
			for (var y=0; y<canvasYsize; y+=32) {
				var rand = Math.random()
				if (rand < 0.3) {
					drawGrass(canvas, x, y)
				} else if (rand < 0.6) {
					drawWater(canvas, x, y)
				} else {
					drawRock(canvas, x, y)
				}
			}
		}
	});
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
	drawTexture(canvas, 0, 0, x, y)
}

function drawWater (canvas, x, y) {
	drawTexture(canvas, 32, 0, x, y)
}

function drawRock (canvas, x, y) {
	drawTexture(canvas, 64, 0, x, y)
}

function drawCity (canvas, x, y) {
	drawTexture(canvas, 96, 0, x, y)
}

function drawOff (canvas, x, y) {
	drawTexture(canvas, 128, 0, x, y)
}

function drawDweller (canvas, x, y) {
	drawTexture(canvas, 160, 0, x, y)
}

//scX, scY : start clipping => the part of the image to use
//x, y : pos on the canvas => where to display the part
function drawTexture (canvas, scX, scY, x, y) {
	// 32 width/height clipping => the size of the part
	// 32 for width/height : size of the part on the canvas => strech if necessary, ie different of wc / hc
	canvas.ctx.drawImage(canvas.images.textures, scX, scY, 32, 32, x, y, 32, 32);
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