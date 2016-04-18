var app = angular.module( 'worldModule', [] )

var rootDiv = "#world-map"

app.run(function ($rootScope) { $rootScope._ = _; });

app.controller('worldController', function( $scope, $interval, httpService, worldService ) {
	
	$scope.cancelDuty = function(teamId) {
		httpService.postData("/team/cancelDuty", {teamId:teamId}).then(function(team) {
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
	
	function updateTeams() {
		httpService.getData("/team/list").then(function(teams) {
			$scope.teams = teams
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
	
	angular.element(document).ready(function () {
		_init()
		updateWorld()
		updateMessages()

		//update view
		$interval(function() {
			updateWorld()
		}, 10 * 1000);

		$interval(function() {
			updateMessages()
		}, 20 * 1000);
    });
})