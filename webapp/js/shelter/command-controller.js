var app = angular.module( 'commandModule', ['datatables'] )

app.run(function ($rootScope) { $rootScope._ = _; });

app.controller('commandController', function( $scope, $interval, httpService, commandService ) {
	
	//CONST
	var TEXT_AREA_ID = "#textarea"
	var SECOND_BEFORE_REFRESH = 3
	var DWELLERS_OFFSET = 10
	
	var lastOrder = "id"
	var lastDirection = "ASC"
		
	var lastPage = 1
	
	$scope.updateDwellers = function(p, o) {
		var page = p || lastPage
		var order = o || lastOrder
		var direction = lastDirection
		
		if (!o) {
			
		} else if (o && o == order) {
			direction = (direction == "ASC" ? "DESC" : "ASC")
		}
		
		lastPage = page
		lastOrder = order
		lastDirection = direction
		
		httpService.getData("/dweller/paginate/" + page + "/" + DWELLERS_OFFSET, {order:order, direction:direction}).then(function(dwellers) {
			$scope.dwellers = dwellers
			$scope.dwellers.pages = _.range(1, dwellers.totalPages + 1);
			var current = dwellers.number + 1
			if (current > 1) {
				$scope.dwellers.previous = current - 1
			}
			if (current < dwellers.totalPages) {
				$scope.dwellers.next = current + 1
			}
		})
	}
		
	function updateTeams() {
		httpService.getData("/team/list").then(function(teams) {
			$scope.teams = teams
		})
	}
	
	var updateFunc = {'dwellers':$scope.updateDwellers, 'teams':updateTeams}
	function updateTab(tab) {
		var func = updateFunc[tab] || function(){}
		func()
	}
	
	var tabActive = 'dwellers'
	angular.element(document).ready(function () {
		updateTab(tabActive)
		
		$interval(function() {
			updateTab(tabActive)
			updateMessages()
		}, SECOND_BEFORE_REFRESH * 1000);
	})
	
	function updateMessages() {
		httpService.getData("/message/last", {page:0, size:20}).then(function(messages) {
			$scope.messages = messages
		})
	}
	
	$scope.ask = function() {
		var command = $('#command').val()
		if ("clear" === command) {
			$(TEXT_AREA_ID).text("")
		} else {
			httpService.getData("/command/ask", {command: command}).then(function(messages) {
				messages.forEach(function(m){
					$(TEXT_AREA_ID).append(m.content)
					$(TEXT_AREA_ID).append('\n')
				})
				$(TEXT_AREA_ID).append('\n')
				$(TEXT_AREA_ID).scrollTop(10000)
			})
		}
	}
})

app.filter('formatTime', function() {
	return function(input) {
		return (!!input) ? (input < 10 ? '0' + input : input) : '';
	}
})
