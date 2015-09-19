var app = angular.module( "shelterModule", ['datatables', 'ngResource'] )
		
app.controller(
	"shelterController",
	function( $scope, $rootScope, shelterService ) {
		var bgColor = {elevator:'bg-gold', power:'bg-danger', water:'bg-info', food:'bg-success'};
		
		$scope.updateGame = function() {
			shelterService.getData("/game/get").then(thenDoUpdateGame)
			$scope.showDisplayRoom = false
			$scope.showConstructRoom = false
		}
		
		$scope.bgColor = function(type) {
			return bgColor[type.toLowerCase()]
		}

		$scope.updateDwellers = function() {
			shelterService.getData("/dweller/list").then(thenDoUpdateDwellers)
		}
		
		$scope.actionRoom = function(floor, room) {
			$scope.showDisplayRoom = false
			$scope.showConstructRoom = false
			if(room.id > 0){
				$scope.showDisplayRoom = true
				shelterService.getData("/room/"+room.id).then(function(data){
					$scope.displayedRoom = data
				})
			} else {
				$scope.showConstructRoom = true
				shelterService.getData("/room/types").then(function(data){
					$scope.roomtype = data
					$scope.constructFloor = floor.number
					$scope.constructCell = room.cells[0]
				})
			}
		}
		$scope.upgradeRoom = function(room) {
			shelterService.postData("/room/upgrade/"+room.id, {}).then(function(data){
				$scope.updateGame()
			})
		}
		
		$scope.construct = function(floor, cell, type) {
			console.log("construct"+floor+type+cell)
			shelterService.postData("/room/construct", {floor:floor, cell:cell, type:type}).then(function(data){
				$scope.updateGame()
			})
		}
		
		// when request is ok, processing server data
		function thenDoUpdateGame(game) {
			var floors = game.shelter.floors
			fillEmptySpace(floors)
			$scope.floors = floors
			$scope.money = game.shelter.money
			$scope.food = game.shelter.food
			$scope.water = game.shelter.water
		}
		
		// add "empty room" between real room,
		// use to interact with floor and construct room
		function fillEmptySpace(floors) {
			var keys = Object.keys(floors)
			for (var key in keys) {
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
			}
			return floors
		}
		
		// update the dwellers list
		function thenDoUpdateDwellers(data) {
			$scope.dwellers = data
		}
		
		$rootScope.$on('dropEvent', function(evt, dragged, dropped) {
			//TODO : see http://www.frangular.com/2013/02/drag-drop-avec-angularjs.html
	        var i, oldIndex1, oldIndex2;
			
	        $scope.$apply()
	    });
		
		// function use to order room in floor
		$scope.roomOrder = function(room){
		    return room.cells[0]
		}

		// call when app is loaded
		angular.element(document).ready(function () {
			$scope.updateGame()
			$scope.updateDwellers()
	    });
	}
)//controller end

app.directive("drag", ["$rootScope", function($rootScope) {
	  
	  function dragStart(evt, element) {
	    evt.originalEvent.dataTransfer.setData("id", evt.target.id)
	    evt.originalEvent.dataTransfer.effectAllowed = 'move'
	  }
	  
	  function dragEnd(evt, element) {
	  }
	  
	  return {
	    restrict: 'A',
	    link: function(scope, element, attrs) {
	      attrs.$set('draggable', 'true')
	      scope.dragData = scope[attrs["drag"]]
	      element.bind('dragstart', function(evt) {
	        $rootScope.draggedElement = scope.dragData
	        dragStart(evt, element)
	      })
	      
	      element.bind('dragend', function(evt) {
	        dragEnd(evt, element)
	      })
	    }
	  }
	}])

app.directive("drop", ['$rootScope', function($rootScope) {
	  
	  function dragEnter(evt, element) {
	    evt.preventDefault();
	  }
	  
	  function dragLeave(evt, element) {
	  }
	  
	  function dragOver(evt) {
	    evt.preventDefault();
	  };
	  
	  function drop(evt, element) {
	    evt.preventDefault();
	    console.log("drop")
	  };
	  
	  return {
	    restrict: 'A',
	    link: function(scope, element, attrs) {
	      scope.dropData = scope[attrs["drop"]];
	      element.bind('dragenter', function(evt) {
	        dragEnter(evt, element)
	      })
	      
	      element.bind('dragleave', function(evt) {
	        dragLeave(evt, element)
	      })
	      
	      element.bind('dragover', dragOver)
	      element.bind('drop', function(evt) {
	        drop(evt, element)
	        $rootScope.$broadcast('dropEvent', $rootScope.draggedElement, scope.dropData)
	      })
	    }
	  }
	}])

app.filter('firstLetter', function() {
    return function(input) {
      return (!!input) ? input.charAt(0).toUpperCase() : '';
    }
})

app.service(
	"shelterService",
	function( $http, $q ) {
		
		var baseUrl = "http://localhost:9080"
		
		// Return public API.
		return({
			getData: getData,
			postData: postData
		})
		
		// generic get data
		function getData(url) {
			var request = $http({ method: "GET", url: baseUrl + url, params: {}, data: {} })
			return( request.then( handleSuccess, handleError ) )
		}
		
		function postData(url, data){
			var request = $http({ method: "POST", url: baseUrl + url, params: {}, data: data })
			return( request.then( handleSuccess, handleError ) )
		}
		
		// ---
		// PRIVATE METHODS.
		// ---
		// I transform the error response, unwrapping the application dta from
		// the API response payload.
		function handleError( response ) {
			console.log("handle error !")
			// The API response from the server should be returned in a
			// normalized format. However, if the request was not handled by the
			// server (or what not handles properly - ex. server error), then we
			// may have to normalize it on our end, as best we can.
			if ( ! angular.isObject( response.data ) || !response.data.message ) {
				return( $q.reject( "An unknown error occurred." ) )
			}
			// Otherwise, use expected error message.
			else {
				return( $q.reject( response.data.message ) )
			}
		}
		
		// I transform the successful response, unwrapping the application data
		// from the API response payload.
		function handleSuccess( response ) {
			console.log("handle success !")
			return( response.data )
		}
	}
)//service end