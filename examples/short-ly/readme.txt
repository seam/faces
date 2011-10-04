Seam Faces short.ly example
===========================

Deploying to JBoss AS 6
-----------------------
export JBOSS_HOME=/path/to/jboss
mvn clean package arquillian:run -Darquillian=jbossas-managed-6


Deploying to JBoss AS 7
-----------------------
export JBOSS_HOME=/path/to/jboss
mvn clean package arquillian:run -Darquillian=jbossas-managed-7


Deploying to GlassFish 3.1.1
----------------------------
$GLASSFISH_HOME/bin/asadmin start-database
$GLASSFISH_HOME/bin/asadmin start-domain
mvn clean package arquillian:run -Darquillian=glassfish-remote-3.1

Note that a bug prevents the example to successfully deploy on Glassfish versions prior to 3.1.1 

see

 http://java.net/jira/browse/GLASSFISH-15721

for a workaround for 3.1

Functional tests
----------------
You can run functional test using following configurations:

mvn clean verify -Darquillian=jbossas-managed-7 
mvn clean verify -Darquillian=jbossas-remote-7 -Pjbossas7

mvn clean verify -Darquillian=jbossas-managed-6
mvn clean verify -Darquillian=jbossas-remote-6  -Pjbossas6 

mvn clean verify -Pglassfish -Darquillian=glassfish-remote-3.1 
 



