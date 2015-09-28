app.service("httpService", function( $http, $q ) {
	
	var baseUrl = 'http://localhost:9080'
	var headers = {'Authorization': 'Basic dXNlcjpwYXNzd29yZA=='}
	
	// public API
	return({
		getData: getData,
		postData: postData
	})
	
	function getData(url) {
		//Authorization : Basic dXNlcjpwYXNzd29yZA==
		
		var request = $http({ method: "GET", url: baseUrl + url, params: {}, data: {}, headers:headers })
		return( request.then( handleSuccess, handleError ) )
	}
	
	function postData(url, data){
		var request = $http({ method: "POST", url: baseUrl + url, params: {}, data: data, headers:headers })
		return( request.then( handleSuccess, handleError ) )
	}
	
	// ---
	// PRIVATE METHODS.
	// ---
	// I transform the error response, unwrapping the application dta from
	// the API response payload.
	function handleError( response ) {
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
	
	// Transform the successful response, unwrapping the application data
	// from the API response payload.
	function handleSuccess( response ) {
		return( response.data )
	}
})