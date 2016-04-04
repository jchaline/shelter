# shelter
Work in progress...

Tribute to the Fallout universe ! Play with your browser, build your shelter and conquer the world !

Use : JDK 8 - Maven - Spring Boot - Spring MVC - Hibernate - Mockito - Spring Test - HSQL(dev) - AngularJS - jQuery - Boostrap - Underscore.js

Rules : Multiplayer game. One shelter by account. Find or reproduce dwellers, train it. Go outside to find weapons, suits. Choose destination with map or explore to improve it. Beat other players (rob money).

Credits : Datatables for jQuery (https://www.datatables.net/) & angular-datatables (https://github.com/l-lin/angular-datatables), http://underscorejs.org/

# Installation
Currently, config example for browser cors, to include in your $HTTPD_HOME/conf/httpd.conf :
	
ProxyPass /shelter-srv/ http://localhost:9080/
ProxyPassReverse /shelter-srv/ http://localhost:9080/

Alias /shelter "C:/Users/JCHALINE/git/shelter/webapp"
<Directory "C:/Users/JCHALINE/git/shelter/webapp">
	Options Indexes FollowSymLinks MultiViews
	AllowOverride all
	Require local
</Directory>

You can also include httpd-shelter.conf from src/main/resources.
The port is configured by application.yml
You must change the file path to your project workspace

Required : LoadModule proxy_module modules/mod_proxy.so & LoadModule proxy_http_module modules/mod_proxy_http.so
	
Then go to http://localhost/shelter/index.html