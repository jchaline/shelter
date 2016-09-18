
function mkCanvas(id) {
	var canvas = {id:id, loaders:[], images:{}, cellSize:32, width:32*28, height:32*18}
	
	canvas.loaders.push(loadSprite(canvas, 'images/sprites/textures32.png', 'textures'));
	canvas.loaders.push(loadSprite(canvas, 'images/sprites/pnj.png', 'pnj'));
	canvas.loaders.push(loadSprite(canvas, 'images/sprites/sprite_sacha.png', 'sacha'));
	
	canvas.ctx = document.getElementById(id).getContext("2d");
	
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