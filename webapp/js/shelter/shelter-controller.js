var app = angular.module( "shelterModule", ['datatables', 'ngResource'] )
		
app.controller("shelterController", function( $scope, $rootScope, $interval, httpService ) {

	$scope.updateDwellers = function() {
		httpService.getData("/dweller/list").then(function(data){
			$scope.dwellers = data
		})
	}

	$scope.actionRoom = function(floor, room) {
		$scope.showDisplayRoom = false
		$scope.showConstructRoom = false
		if(room.id > 0){
			$scope.showDisplayRoom = true
			httpService.getData("/room/" + room.id).then(function(data){
				$scope.displayedRoom = data
			})
		} else {
			$scope.showConstructRoom = true
			httpService.getData("/room/types").then(function(data){
				$scope.roomtype = data
				$scope.constructFloor = floor.number
				$scope.constructCell = room.cells[0]
			})
		}
	}

	$scope.upgradeRoom = function(room) {
		httpService.postData("/room/upgrade/"+room.id, {}).then(function(data){
			$scope.updateShelter()
			$scope.showDisplayRoom = false
			$scope.showConstructRoom = false
		})
	}

	$scope.construct = function(floor, cell, type) {
		httpService.postData("/room/construct", {floor:floor, cell:cell, type:type}).then(function(data){
			$scope.updateShelter()
		})
	}

	$scope.updateShelter = function() {
		httpService.getData("/player/get").then(function(player){
			var floors = player.shelter.floors
			fillEmptySpace(floors)
			$scope.floors = floors
			$scope.money = player.money
			$scope.food = player.shelter.food
			$scope.water = player.shelter.water
		})
	}

	// add "empty room" between reals rooms,
	// use to interact with floor and construct room
	function fillEmptySpace(floors) {
		Object.keys(floors).forEach(function(key){
			var floor = floors[key]
			var cellList = Array.apply(null, {length: floor.size}).map(Number.call, Number)
			var used = floor.rooms.map(function(v, i) {
				return v.cells
			})
			.reduce(function(previousValue, currentValue, index, array) {
				return previousValue.concat(currentValue)
			})
			cellList.forEach(function(e) {
				if($.inArray(e, used) == - 1){
					var emptySpace = {id:-1, 'size':1, floor:key, 'cells':[e], 'roomType':{'name':'vide'}}
					floor.rooms.push(emptySpace)
				}
			})
		})
		return floors
	}

	$rootScope.$on('dropEvent', function(evt, dweller, room) {
		httpService.postData("/room/" + room + "/assign/" + dweller, {}).then(function(data){
			$scope.updateDwellers()
		})
    });

	// function use to order room in floor
	$scope.roomOrder = function(room){
	    return room.cells[0]
	}

	// call when app is loaded
	angular.element(document).ready(function () {
		$scope.updateShelter()
		$scope.updateDwellers()

		//update view with 5s interval
		$interval(function(){
			$scope.updateShelter()
		}, 5000);
    });
})