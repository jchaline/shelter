
// TODO : when move pnj ie dweller(s), open window to select wich dwellers to move to the target

app.service("worldService", function( $q ) {
	
	var canvasGame = document.getElementById('game-layer');
	
	var gameCanvas = new CanvasGame(canvasGame);
	var canvasBackground = new CanvasBackground(document.getElementById('background-layer'));
	var _dwellers = []
	var _worldMap = {}
	var xLeft
	var yUp
	
	// public API
	return({
		updateCenter: updateCenter,
		updateMapWithWorld: updateMapWithWorld,
		drawMap: drawMap,
		drawDwellers: drawDwellers,
		updateTeamsDuty: updateTeamsDuty,
		findDweller: findDweller
	})
	
	function findDweller(x, y) {
		//transcrire valeur px sur le canvas en indices x / y avec le map center
		var xPos = Math.floor(x / 32) + xLeft
		var yPos = Math.floor(y / 32) + yUp
		
		var res = _.filter(_dwellers, function(d){
			return d.mapCell.xaxis == xPos && d.mapCell.yaxis == yPos;
		})
		
		return res
	}
	
	function updateTeamsDuty(teams, actualWithoutDuty, actualWithDuty, force) {
		var res = {}
		var teamsWithoutDuty = _.filter(teams, {'target':null})
		var teamsWithDuty = _.differenceBy(teams, teamsWithoutDuty, 'id');
		
		if (force) {
			res['teamsWithoutDuty'] = teamsWithoutDuty
			res['teamsWithDuty'] = teamsWithDuty
		} else {
			var teamWithoutDutyRemove = _.differenceBy(actualWithoutDuty, teamsWithoutDuty, 'id');
			var teamWithoutDutyAdd = _.differenceBy(teamsWithoutDuty, actualWithoutDuty, 'id');
			
			if (teamWithoutDutyRemove.length > 0 || teamWithoutDutyAdd.length > 0) {
				res['teamsWithoutDuty'] = teamsWithoutDuty
			}
			
			var teamWithDutyRemove = _.differenceBy(actualWithDuty, teamsWithDuty, 'id');
			var teamWithDutyAdd = _.differenceBy(teamsWithDuty, actualWithDuty, 'id');
			if (teamWithDutyRemove.length > 0 || teamWithDutyAdd.length > 0) {
				res['teamsWithDuty'] = teamsWithDuty
			}
		}

		return res
	}
	
	//update map center view
	function updateCenter(map, vx, vy) {
		map.center.x = Math.min(Math.max(map.center.x + vx, 0), map.width - 1)
		map.center.y = Math.min(Math.max(map.center.y + vy, 0), map.height - 1)
		console.log("update center : " + map.center.x + "," + map.center.y)
	}
	
	//Update the world Map, with new cells. Keep the center if exist, else, take the map absolute center
	function updateMapWithWorld(world, worldMap) {
		
		var map = {'height':world.height, 'width':world.width, 'cells':[], 'center':worldMap != undefined ? worldMap.center : {'x':Math.round(world.width / 2), 'y':Math.round(world.height / 2)}}
		
		//transform the object into array
		Object.keys(world.map).forEach(function(key) {
			var cell = world.map[key]
			map.cells[cell.xaxis] = map.cells[cell.xaxis] || []
			map.cells[cell.xaxis][cell.yaxis] = cell
		})
		
		return map
	}

	// display dwellers position on the map
	function drawDwellers(worldMap, dwellers) {
		var canvas = gameCanvas
		
		_dwellers = dwellers
		var withoutDuplicate = _.uniqBy(dwellers, 'mapCell.id');
		
		var nbCeilHeight = canvas.height / canvas.cellSize // nombre de cellules à afficher sur la hauteur
		var nbCeilWidth = canvas.width / canvas.cellSize // nombre de cellules à afficher sur la largeur
		
		xLeft = worldMap.center.x - Math.round(nbCeilWidth / 2)
		yUp = worldMap.center.y - Math.round(nbCeilHeight / 2)
		
		canvas.drop()
		canvas.clear()

		//wait the sprites loader
		$.when.apply(null, canvas.loaders).done(function() {
			withoutDuplicate.forEach(function(d) {
				var cellId = d.mapCell.id
				var x = d.mapCell.xaxis - xLeft
				var y = d.mapCell.yaxis - yUp
				
				canvas.addPnj(new Pnj(x * canvas.cellSize, y * canvas.cellSize, 32, 32, canvas.sprites.pnj));
			})
		})
	}
	
	// display the map
	function drawMap(worldMap) {
		_worldMap = worldMap
		
		var canvas = canvasBackground
		
		var nbCeilHeight = canvas.height / canvas.cellSize // nombre de cellules à afficher sur la hauteur
		var nbCeilWidth = canvas.width / canvas.cellSize // nombre de cellules à afficher sur la largeur
		
		xLeft = worldMap.center.x - Math.round(nbCeilWidth / 2)
		yUp = worldMap.center.y - Math.round(nbCeilHeight / 2)
		
		canvas.ctx.clearRect(0, 0, canvas.width, canvas.height)

		//wait the sprites loader
		$.when.apply(null, canvas.loaders).done(function() {
			for (var x=0; x<nbCeilWidth; x++) {
				for (var y=0; y<nbCeilHeight; y++) {
					var cellX = x + xLeft
					var cellY = y + yUp
					try {
						var cell = worldMap.cells[cellX][cellY]
						var cellType = cell.occupant.type.toLowerCase()
						canvas.drawTexture(x * canvas.cellSize, y * canvas.cellSize, cellType)
					} catch (e) {
						canvas.drawTexture(x * canvas.cellSize, y * canvas.cellSize, "off")
					}
				}
			}
		})
	}
})

