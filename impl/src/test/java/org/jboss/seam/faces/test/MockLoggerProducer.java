package org.jboss.seam.faces.test;

import javax.enterprise.inject.Produces;

import org.jboss.logging.Logger;

public class MockLoggerProducer {
    @Produces
    public Logger getLogger() {
        return new MockLogger("mock");
    }
}
