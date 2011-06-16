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

import org.jboss.seam.faces.examples.viewconfig.model.Current;
import org.jboss.seam.faces.examples.viewconfig.model.Item;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.Secures;

/**
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
public class SecurityRules {
    public
    @Secures
    @Owner
    boolean ownerChecker(Identity identity, @Current Item item) {
        if (item == null || identity.getUser() == null) {
            return false;
        } else {
            return item.getOwner().equals(identity.getUser().getId());
        }
    }

    public
    @Secures
    @Admin
    boolean adminChecker(Identity identity) {
        if (identity.getUser() == null) {
            return false;
        } else {
            return "admin".equals(identity.getUser().getId());
        }
    }
}
