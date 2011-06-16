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
package org.jboss.seam.faces.examples.viewconfig.security;

import javax.inject.Inject;

import org.jboss.seam.security.Authenticator;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;
import org.picketlink.idm.impl.api.PasswordCredential;
import org.picketlink.idm.impl.api.model.SimpleUser;

/**
 * This is the simplest possible example of a custom authenticator.
 *
 * @author Shane Bryzak
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class SimpleAuthenticator extends BaseAuthenticator implements Authenticator {

    @Inject
    Credentials credentials;

    @Override
    public void authenticate() {
        if ("demo".equals(credentials.getUsername())
                && credentials.getCredential() instanceof PasswordCredential
                && "demo".equals(((PasswordCredential) credentials.getCredential()).getValue())) {
            setStatus(AuthenticationStatus.SUCCESS);
            setUser(new SimpleUser("demo"));
        } else if ("admin".equals(credentials.getUsername())
                && credentials.getCredential() instanceof PasswordCredential
                && "admin".equals(((PasswordCredential) credentials.getCredential()).getValue())) {
            setStatus(AuthenticationStatus.SUCCESS);
            setUser(new SimpleUser("admin"));
        }
    }
}
