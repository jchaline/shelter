var app = angular.module( 'worldModule', [] )

var rootDiv = "#world-map"

app.run(function ($rootScope) { $rootScope._ = _; });

app.controller('worldController', function( $scope, $interval, httpService, worldService ) {
	
	$scope.cancelDuty = function(teamId) {
		httpService.postData("/team/cancelDuty", {teamId:teamId}).then(function(team) {
			updateTeams()
		})
	}
	$scope.disband = function(teamId) {
		httpService.postData("/team/disband", {teamId:teamId}).then(function(ok) {
			updateTeams()
		})
	}
	
	$scope.sendDuty = function(teamId) {
		var xaxis = $("#xaxis_" + teamId).val()
		var yaxis = $("#yaxis_" + teamId).val()
		var duty = $("#duty_" + teamId).val()
		
		var cellId = $scope.worldMap.cells[xaxis][yaxis].id
		
		httpService.postData("/team/sendDuty", {teamId:teamId, dutyId:duty, target:cellId}).then(function(team) {
			updateTeams()
		})
	}

	$scope.teamup = function() {
		var teamup = $('[name="dweller"]:checked').map(function(){return this.value}).get()
		
		httpService.postData("/team/teamup", {dwellersId: _.values(teamup)}).then(function(team) {
			updateTeams()
		})
	}

	//move the displayed map
	$scope.moveMap = function(vx, vy) {
		worldService.updateCenter($scope.worldMap, vx, vy)
		worldService.drawMap($scope.worldMap)
		worldService.drawDwellers($scope.dwellers)
	}
	
	$scope.showTeamDetail = function(teamId) {
		httpService.getData("/team/get", {teamId: teamId}).then(function(team) {
			$scope.teamDetail = team
		})
	}

	//add empty spot to city and display it
	function updateCity(cityId) {
		httpService.getData("/world/cell/" + cityId).then(function(city) {
			$scope.city = city
			$scope.cityDwellers = _.filter($scope.dwellers, function(o) { return o.mapCell.id==cityId; });
		})
	}

	//update the world map with server data
	function updateWorld() {
		httpService.getData("/world/get").then(function(world) {
			$scope.world = world
			
			$scope.worldMap = worldService.updateMapWithWorld($scope.world, $scope.worldMap)
			
			worldService.drawMap($scope.worldMap)
			
			updateDwellers()
			updateTeams()
		})
	}
	
	function updateDwellers() {
		httpService.getData("/dweller/list").then(function(dwellers) {
			$scope.dwellers = dwellers
			worldService.drawDwellers($scope.dwellers)
		})
	}
	
	//TODO : add args to force refresh ?
	//TODO : manage 'return' duty when refresh team with duty... with refresh force ?
	function updateTeams() {
		httpService.getData("/team/list").then(function(teams) {
			$scope.teams = teams
			
			if (!$scope.teamsWithoutDuty) {
				$scope.teamsWithoutDuty = []
			}
			if (!$scope.teamsWithDuty) {
				$scope.teamsWithDuty = []
			}
			
			var teamsWithoutDuty = _.filter(teams, {'target':null})
			var teamsWithDuty = _.differenceBy(teams, teamsWithoutDuty, 'id');

			var teamWithoutDutyRemove = _.differenceBy($scope.teamsWithoutDuty, teamsWithoutDuty, 'id');
			var teamWithoutDutyAdd = _.differenceBy(teamsWithoutDuty, $scope.teamsWithoutDuty, 'id');
			if (teamWithoutDutyRemove.length > 0 || teamWithoutDutyAdd.length > 0) {
				$scope.teamsWithoutDuty = teamsWithoutDuty
			}

			var teamWithDutyRemove = _.differenceBy($scope.teamsWithDuty, teamsWithDuty, 'id');
			var teamWithDutyAdd = _.differenceBy(teamsWithDuty, $scope.teamsWithDuty, 'id');
			if (teamWithDutyRemove.length > 0 || teamWithDutyAdd.length > 0) {
				$scope.teamsWithDuty = teamsWithDuty
			}
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
		
		$(rootDiv).on('click', '.city', function() {
			updateCity($(this).attr('data-cell-id'))
		})
	}
	
	//TODO : fix dev frequency, externalize & integrate with grunt ?
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
		}, 5 * 1000);

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