app.directive("drag", ["$rootScope", function($rootScope) {

	function dragStart(evt, element) {
		evt.originalEvent.dataTransfer.setData("id", evt.target.id)
		evt.originalEvent.dataTransfer.effectAllowed = 'move'
	}
	  
	function dragEnd(evt, element) {}
	  
	return {
		restrict: 'A',
		link: function(scope, element, attrs) {
			attrs.$set('draggable', 'true')
			scope.dragData = attrs["drag"]
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

	function dragLeave(evt, element) { }

	function dragOver(evt) {
		evt.preventDefault();
	}
  
	function drop(evt, element) {
		evt.preventDefault();
	}
	  
	return {
		restrict: 'A',
		link: function(scope, element, attrs) {
			element.bind('dragenter', function(evt) {
				dragEnter(evt, element)
			})
			element.bind('dragleave', function(evt) {
				dragLeave(evt, element)
			})
			element.bind('dragover', dragOver)
			element.bind('drop', function(evt) {
				scope.dropData = attrs["drop"];
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