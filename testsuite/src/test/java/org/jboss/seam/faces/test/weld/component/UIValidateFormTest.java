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
package org.jboss.seam.faces.test.weld.component;

import javax.faces.component.UIViewRoot;

import org.jboss.seam.faces.component.UIValidateForm;
import org.jboss.test.faces.mock.application.MockApplication;
import org.jboss.test.faces.mock.context.MockFacesContext;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
public class UIValidateFormTest {

    private final MockFacesContext facesContext = new MockFacesContext();
    private final MockApplication application = new MockApplication();
    private static final UIViewRoot uiViewRoot = new UIViewRoot();

    static {
        uiViewRoot.setViewId("foo.xhtml");
    }

    @Test
    public void testCanLocateChildComponents() throws Exception {
        UIValidateForm vf = new UIValidateForm();
    }
}
