 
app.service("commandService", function( $q ) {
	
	// public API
	return ({
		ping:ping
	})
	
	function ping() {
		return "pong"
	}
})

