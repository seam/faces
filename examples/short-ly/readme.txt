TODO:
  - Purpose of the example - for example, to demonstrate feature X by illustrate how that API can be used to perform A, B and C.
  - Prerequisites - The AS has been installed and the appropriate properties files have been updated
  - Step by Step setup for the example -
  - Step by Step execution of the example - "ant deploy" or "mvn clean install" or "run -c server1" + "run -c server2" or "using your browser hit: http://localhost:8080/MyNewApp"
  - Expected results - you should see "Hello World" on the console or you can now navigate the web-based application
  - the readme.txt should include the maintainer of the example (along with contact info). This may simply point to the examples' POM.
  
JBoss AS 6.0
------------
To deploy the example to jbossas 6:

 export JBOSS_HOME=/path/to/jboss
 mvn clean package jboss:hard-deploy -Pjbossas



GlassFish 3.1
-------------
To run the example on GlassFish 3.1:

 $GLASSFISH_HOME/bin/asadmin start-database
 $GLASSFISH_HOME/bin/asadmin start-domain domain1

 mvn clean package -Pglassfish
 
 $GLASSFISH_HOME/bin/asadmin deploy target/short.ly.war

TODO: remove the note if the glassfish bug is fixed in the final version of Glassfish3.1

Note that there is a bug in the current Glassfish3.1 build (b43) that prevents successful deployment of the example on Glassfish

see

 http://java.net/jira/browse/GLASSFISH-15721

for a workaround


