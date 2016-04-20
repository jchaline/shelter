app.service("worldService", function( $q ) {
	
	// public API
	return({
		updateCenter: updateCenter,
		updateMapWithWorld: updateMapWithWorld,
		drawMap: drawMap,
		drawDwellers: drawDwellers
	})
	
	//update map center view
	function updateCenter(map, vx, vy) {
		map.center.x = map.center.x + vx
		map.center.y = map.center.y + vy
		console.log(map.center)
	}
	
	//Update the world Map, with new cells. Keep the center if exist, else, take the map absolute center
	function updateMapWithWorld(world, worldMap) {
		
		var map = {'height':world.height, 'width':world.with, 'cells':[], 'center':worldMap != undefined ? worldMap.center : {'x':Math.round(world.width / 2), 'y':Math.round(world.height / 2)}}
		
		//transform the object into array
		Object.keys(world.map).forEach(function(key) {
			var cell = world.map[key]
			map.cells[cell.xaxis] = map.cells[cell.xaxis] || []
			map.cells[cell.xaxis][cell.yaxis] = cell
		})
		
		return map
	}

	// display dwellers position on the map
	function drawDwellers(dwellers) {
		dwellers.forEach(function(d) {
			var cellId = d.mapCell.id
			$('[data-cell-id="' + cellId + '"]').html('<span class="glyphicon glyphicon-user"></span>')
		})
	}
	
	// display the map
	function drawMap(worldMap) {
		$(rootDiv).html("")
		
		//param/constante
		var mapHeightPx = 600
		var mapWidthPx = 600
		var cellHeightPx = 30 // taille hauteur en px d'une cell
		var cellWidthPx = 30 // taille largeur en px d'une cell

		var nbCeilHeight = mapHeightPx / cellHeightPx // nombre de cellules à afficher sur la hauteur
		var nbCeilWidth = mapWidthPx / cellWidthPx // nombre de cellules à afficher sur la largeur
		
		var nbCeilHeightTotal = worldMap.cells[0].length // nombre total de cellule sur la hauteur
		var nbCeilWidthTotal = worldMap.cells.length // nombre total de cellule sur la largeur
		
		//determination des bornes de la map en fonction du point
		var xLeft = worldMap.center.x - Math.round(nbCeilWidth / 2)
		var yUp = worldMap.center.y - Math.round(nbCeilHeight / 2)
		
		//cellules à afficher
		for (y = 0; y < nbCeilHeight; y++) {
			for (x = 0; x < nbCeilWidth; x++) {
				//détermination des identifiants des cellules affichés
				var cell = $($(worldMap.cells).get((x + xLeft) % nbCeilWidthTotal)).get((y + yUp) % nbCeilHeightTotal)
				var id = cell.xaxis + '_' + cell.yaxis
				var cellType = "empty"
				if (cell.occupant) {
					cellType = cell.occupant.type.toLowerCase()
				}
					
				var div = '<div id="' + id + '" data-xaxis="' + cell.xaxis + '" data-yaxis="' + cell.yaxis + '" class="cell ' + cellType + '" data-cell-id="' + cell.id + '" title="' + id + '" ondrop="drop(event)" ondragover="allowDrop(event)""></div>'
				$(rootDiv).append(div)
			}
		}
	}
})

function dragStart(event) {
	//document.getElementById("demo").innerHTML = "Started to drag the p element";
	event.dataTransfer.setData("teamId", $(event.target).attr('data-team-id'));
}

function allowDrop(event) {
    event.preventDefault();
}

function drop(event) {
    event.preventDefault();
    var teamId = event.dataTransfer.getData("teamId");
    var target = event.target
    $('#xaxis_' + teamId).val($(target).attr('data-xaxis'))
    $('#yaxis_' + teamId).val($(target).attr('data-yaxis'))
    //event.target.appendChild(document.getElementById(data));
    //document.getElementById("demo").innerHTML = "The p element was dropped";
}