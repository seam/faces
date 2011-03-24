package org.jboss.seam.faces.test;

import org.jboss.logging.Logger;

/**
 * Provide a completely empty logger implementation for Unit Tests. This entire class is a no-op; it does <i>nothing</i>.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class MockLogger extends Logger {

    protected MockLogger(String name) {
        super(name);
    }

    @Override
    public boolean isEnabled(Level level) {
        return false;
    }

    @Override
    protected void doLog(Level level, String className, Object message, Object[] params, Throwable e) {
    }

    @Override
    protected void doLogf(Level level, String className, String message, Object[] params, Throwable e) {
    }
}
