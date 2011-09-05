#Seam Faces Test Suite

##Running the testsuite on the default container (Embedded Weld)

    mvn clean verify 

##Running the testsuite on all containers
    
    mvn clean verify -DallTests

##Running the testsuite on JBoss AS 7 only

    mvn clean verify -Djbossas-managed-7

##Contents

common/ directory contains the source of tests common to all the containers. Sources of container-specific tests are located in the respective container modules.


