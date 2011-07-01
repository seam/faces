Seam Faces short.ly example
===========================

Deploying to JBoss AS 6
-----------------------
export JBOSS_HOME=/path/to/jboss
mvn clean package jboss:hard-deploy -Pjboss6

Deploying to JBoss AS 7
-----------------------
mvn clean package -Pjboss7
$JBOSS_HOME/bin/jboss-admin.sh --connect
deploy target/faces-shortly.war

Deploying to GlassFish 3.1.1
----------------------------
mvn clean package -Pglassfish
$GLASSFISH_HOME/bin/asadmin start-database
$GLASSFISH_HOME/bin/asadmin start-domain domain1
$GLASSFISH_HOME/bin/asadmin deploy target/faces-shortly.war

Note that a bug prevents the example to successfully deploy on Glassfish versions prior to 3.1.1 

see

 http://java.net/jira/browse/GLASSFISH-15721

for a workaround for 3.1

Functional tests
----------------
To run the functional tests, start the application server, deploy the example using one of the methods described above and run the following command:

 mvn verify -Pftest


