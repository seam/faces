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
package org.jboss.seam.faces.test.weld.view.action.annotation;

import org.jboss.seam.faces.event.PhaseIdType;
import org.jboss.seam.faces.security.RestrictAtPhase;
import org.jboss.seam.faces.view.action.ViewAction;
import org.jboss.seam.faces.view.action.ViewController;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;

@ViewConfig
public interface ViewConfigEnum {
    static enum Pages {
        @ViewPattern("/*")
        DEFAULT,

        @ViewPattern("/client/*")
        @ViewAction("#{clientController.viewAction}")
        CLIENTS,

        @ViewPattern("/country/*")
        @ViewController(CountryController.class)
        COUNTRIES(),

        @ViewPattern("/client/done.xhtml")
        CLIENT_CONFIRMED(),

        @ViewPattern("/qualified/*")
        @RestrictAtPhase(PhaseIdType.INVOKE_APPLICATION)
        QUALIFIED,

        @ViewPattern("/qualified/yes.xhtml")
        QUALIFIED_YES;

    }
}
