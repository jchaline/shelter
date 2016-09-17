app.service("worldService", function( $q ) {
	
	// public API
	return({
		updateCenter: updateCenter,
		updateMapWithWorld: updateMapWithWorld,
		drawMap: drawMap,
		drawDwellers: drawDwellers,
		updateTeamsDuty: updateTeamsDuty
	})
	
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
	function drawDwellers(worldMap, dwellers, canvas) {
		var nbCeilHeight = canvas.height / canvas.cellSize // nombre de cellules à afficher sur la hauteur
		var nbCeilWidth = canvas.width / canvas.cellSize // nombre de cellules à afficher sur la largeur
		
		var xLeft = worldMap.center.x - Math.round(nbCeilWidth / 2)
		var yUp = worldMap.center.y - Math.round(nbCeilHeight / 2)
		
		canvas.ctx.clearRect(0, 0, canvas.width, canvas.height)
		
		dwellers.forEach(function(d) {
			var cellId = d.mapCell.id
			var x = d.mapCell.xaxis - xLeft
			var y = d.mapCell.yaxis - yUp
			
			draw(canvas, x * canvas.cellSize, y * canvas.cellSize, "dweller")
		})
	}
	
	// display the map
	function drawMap(worldMap, canvas) {
		$.when.apply(null, canvas.loaders).done(function() {
			var nbCeilHeight = canvas.height / canvas.cellSize // nombre de cellules à afficher sur la hauteur
			var nbCeilWidth = canvas.width / canvas.cellSize // nombre de cellules à afficher sur la largeur

			var xLeft = worldMap.center.x - Math.round(nbCeilWidth / 2)
			var yUp = worldMap.center.y - Math.round(nbCeilHeight / 2)
			
			canvas.ctx.clearRect(0, 0, canvas.width, canvas.height)
			
			for (var x=0; x<nbCeilWidth; x++) {
				for (var y=0; y<nbCeilHeight; y++) {
					
					var cellX = x + xLeft
					var cellY = y + yUp
					try {
						var cell = worldMap.cells[cellX][cellY]
						var cellType = cell.occupant.type.toLowerCase()
						draw(canvas, x * canvas.cellSize, y * canvas.cellSize, cellType)
						
					} catch (e) {
						draw(canvas, x * canvas.cellSize, y * canvas.cellSize, "off")
					}
				}
			}
		})

		$(rootDiv).html("")

		//param/constante
		var mapHeightPx = 600
		var mapWidthPx = 600
		var cellHeightPx = 30 // taille hauteur en px d'une cell
		var cellWidthPx = 30 // taille largeur en px d'une cell
		
		var nbCeilHeight = mapHeightPx / cellHeightPx // nombre de cellules à afficher sur la hauteur
		var nbCeilWidth = mapWidthPx / cellWidthPx // nombre de cellules à afficher sur la largeur
		
		var nbCeilHeightTotal = worldMap.height // nombre total de cellule sur la hauteur
		var nbCeilWidthTotal = worldMap.width // nombre total de cellule sur la largeur
		
		//determination des bornes de la map en fonction du point
		var xLeft = worldMap.center.x - Math.round(nbCeilWidth / 2)
		var yUp = worldMap.center.y - Math.round(nbCeilHeight / 2)
		
		//cellules à afficher
		for (y = 0; y < nbCeilHeight; y++) {
			for (x = 0; x < nbCeilWidth; x++) {
				//détermination des identifiants des cellules affichés
				try {
					var cellX = x + xLeft
					var cellY = y + yUp
					var cell = worldMap.cells[cellX][cellY]
					var id = cell.xaxis + '_' + cell.yaxis
					var cellType = cell.occupant.type.toLowerCase()
					var div = '<div id="' + id + '" data-xaxis="' + cell.xaxis + '" data-yaxis="' + cell.yaxis + '" class="cell texture-' + cellType + '" data-cell-id="' + cell.id + '" title="' + id
					div += '" ondrop="drop(event)" ondragover="allowDrop(event)">'
					div += '</div>'
					$(rootDiv).append(div)
				} catch (e) {
					//console.log("try to display cell at (" + x + "," + y + ") , cannot find")
					var div = '<div class="cell void"></div>'
					$(rootDiv).append(div)
				}
			}
		}
	}
})

