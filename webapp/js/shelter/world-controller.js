var app = angular.module( 'worldModule', [] )

app.run(function ($rootScope) { $rootScope._ = _; });

app.controller('worldController', function( $scope, $interval, httpService, worldService ) {
	
	var canvasGame = document.getElementById('game-layer');
	
	$scope.showDwellerDetails = function(dwellerId) {
		httpService.getData("/dweller/get", {dwellerId: dwellerId}).then(function(dweller) {
			$scope.dwellerDetail = dweller
			$('#dwellerInfoModal').modal('show')
		})
	}
	
	$scope.cancelDuty = function(teamId) {
		httpService.postData("/team/cancelDuty", {teamId:teamId}).then(function(team) {
			updateTeams(true)
		})
	}
	$scope.disband = function(teamId) {
		httpService.postData("/team/disband", {teamId:teamId}).then(function(ok) {
			updateTeams(true)
		})
	}
	
	$scope.sendDuty = function(teamId) {
		var xaxis = $("#xaxis_" + teamId).val()
		var yaxis = $("#yaxis_" + teamId).val()
		var duty = $("#duty_" + teamId).val()
		
		var cellId = $scope.worldMap.cells[xaxis][yaxis].id
		
		httpService.postData("/team/sendDuty", {teamId:teamId, dutyId:duty, target:cellId}).then(function(team) {
			updateTeams(true)
		})
	}

	$scope.teamup = function() {
		var teamup = $('[name="dweller"]:checked').map(function(){return this.value}).get()
		console.log("create team :")
		console.log(teamup)
		$('#ui-layer').hide()
		
//		httpService.postData("/team/teamup", {dwellersId: _.values(teamup)}).then(function(team) {
//			updateTeams(true)
//		})
	}

	//move the displayed map
	$scope.moveMap = function(vx, vy) {
		worldService.updateCenter($scope.worldMap, vx, vy)
		var thenDo = function(){worldService.drawMap($scope.worldMap); worldService.drawDwellers($scope.worldMap, $scope.dwellers)}
		updateWorld()
		thenDo()
	}
	canvasGame.addEventListener('moveMap', function (e) {
		var vx = -1 * Math.floor((e.detail.vx-e.detail.x) / 32)
		var vy = -1 * Math.floor((e.detail.vy-e.detail.y) / 32)
		$scope.moveMap(vx, vy);
	}, false);
	
	$scope.showTeamDetail = function(teamId) {
		httpService.getData("/team/get", {teamId: teamId}).then(function(team) {
			$scope.teamDetail = team
		})
	}

	//add empty spot to city and display it
	function updateCell(cellId) {
		httpService.getData("/world/cell/" + cellId).then(function(cell) {
			$scope.cell = cell
			$scope.cellDwellers = _.filter($scope.dwellers, function(o) { return o.mapCell.id==cellId; });
		})
	}

	//update the world map with server data, run thenDo after if function is given
	function updateWorld(thenDo) {
		var xcenter = $scope.worldMap ? $scope.worldMap.center.x : 25
		var ycenter = $scope.worldMap ? $scope.worldMap.center.y : 25
		httpService.getData("/world/get", {'xcenter': xcenter || 0, 'ycenter': ycenter}).then(function(world) {
			$scope.world = world
			
			$scope.worldMap = worldService.updateMapWithWorld($scope.world, $scope.worldMap)
			
			worldService.drawMap($scope.worldMap)
			
			updateDwellers()
			updateTeams(false)
			if (_.isFunction(thenDo)) {
				thenDo()
			}
		})
	}
	
	function updateDwellers() {
		httpService.getData("/dweller/list").then(function(dwellers) {
			$scope.dwellers = dwellers
			worldService.drawDwellers($scope.worldMap, $scope.dwellers)
		})
	}
	

	function movePnj(xPix, yPix, vx, vy) {
		$scope.movedDwellers = worldService.findDweller(xPix, yPix)
		//instruction for background color
		// set the div content, then show
		$('#ui-layer').show()
		//$('#ui-layer').css('background-color', 'rgba(125,125,125,0.8)')
	}
	canvasGame.addEventListener('movePnj', function (e) { movePnj(e.detail.x, e.detail.y, e.detail.vx, e.detail.vy) }, false);
	
	
	//TODO : add args to force refresh ?
	//TODO : manage 'return' duty when refresh team with duty... with refresh force ?
	function updateTeams(force) {
		httpService.getData("/team/list").then(function(teams) {
			$scope.teams = teams
			
			var teamsDuty = worldService.updateTeamsDuty(teams, $scope.teamsWithoutDuty || [], $scope.teamsWithDuty || [], force)
			
			$scope.teamsWithoutDuty = teamsDuty.teamsWithoutDuty || $scope.teamsWithoutDuty
			$scope.teamsWithDuty = teamsDuty.teamsWithDuty || $scope.teamsWithDuty
		})
	}
	
	function updateMessages() {
		httpService.getData("/message/last", {page:0, size:10}).then(function(messages) {
			$scope.messages = messages
		})
	}
	
	// function call once, init data for app
	function _init() {
		httpService.getData("/duty/listAction").then(function(duties) {
			$scope.duties = duties
		})
	}
	
	//TODO : fix dev frequency, externalize & integrate with gulp ?
	angular.element(document).ready(function () {
		_init()
		updateWorld()
		updateMessages()

		//update view
		$interval(function() {
			updateWorld()
			httpService.getData("/metrics").then(function(data) {
				console.log(data)
			})
		}, 60 * 1000);

		$interval(function() {
			updateMessages()
		}, 5 * 1000);
    });
})

app.filter('formatTime', function() {
	return function(input) {
		return (!!input) ? (input < 10 ? '0' + input : input) : '';
	}
})

//html standard drag'n'drop
function dragTeamStart(event) {
	event.dataTransfer.setData("teamId", $(event.target).attr('data-team-id'));
}

function allowDrop(event) {
	event.preventDefault();
}

function drop(event) {
	event.preventDefault();
	var teamId = event.dataTransfer.getData("teamId");
	var target = event.target
	var xaxis = $(target).attr('data-xaxis')
	var yaxis = $(target).attr('data-yaxis')
	if (teamId) {
		$('#xaxis_' + teamId).val(xaxis)
		$('#yaxis_' + teamId).val(yaxis)
	}
}