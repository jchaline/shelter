var app = angular.module( 'commandModule', [] )

app.run(function ($rootScope) { $rootScope._ = _; });

app.controller('commandController', function( $scope, httpService, commandService ) {
	
	var textAreaId = "#textarea"
	var textAreaSize = 130
	
	$scope.messages = []
	
	$scope.ask = function() {
		var command = $('#command').val()
		if ("clear" === command) {
			$(textAreaId).text("")
		} else {
			httpService.getData("/command/ask", {command: command}).then(function(messages) {
				messages.forEach(function(m){
					$(textAreaId).append(m.content)
					$(textAreaId).append('\n')
				})
				$(textAreaId).append('\n')
				$(textAreaId).scrollTop(10000)
			})
		}
	}
})

