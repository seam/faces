/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.faces.test;

/**
 * Provide a completely empty logger implementation for Unit Tests. This entire class is a no-op; it does <i>nothing</i>.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
public class MockLogger extends org.jboss.seam.solder.logging.internal.Logger {

    protected MockLogger(String name) {
        super(name);
    }

    @Override
    public boolean isEnabled(Level level) {
        return false;
    }

    @Override
    protected void doLog(Level arg0, String arg1, Object arg2, Object[] arg3, Throwable arg4) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void doLogf(Level arg0, String arg1, String arg2, Object[] arg3, Throwable arg4) {
        // TODO Auto-generated method stub
        
    }

}
