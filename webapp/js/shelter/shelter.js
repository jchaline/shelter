window.console = window.console || {'log':function(){}}

//random int between min (included) and max (included)
function randomInt(min, max) {
	return Math.floor(Math.random() * (max - min + 1)) + min;
}

//get random element from array
Array.prototype.random = function () {
	return this[Math.random() * this.length - 1 | 0];
};

//
Array.prototype.subRandom = function () {
	var length = randomInt(0, this.length)
	var subArray = []
	for (i=0; i<length; i++) {
		subArray.push(this.random())
	}
	return _.uniq(subArray);
};



//unorder array
Array.prototype.shuffle = function () {
	for (var i = this.length; i > 0; --i)
		this.push(this.splice(Math.random() * i | 0, 1)[0]);
	return this;
};

//Add composition. see http://scott.sauyet.com/Javascript/Talk/Compose/2013-05-22/#slide-15
Function.prototype.compose = function(g) {
	var fn = this;
	return function() {
		return fn.call(this, g.apply(this, arguments));
	};
};



