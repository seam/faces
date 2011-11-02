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
package org.jboss.seam.faces.examples.viewconfig;

import org.jboss.seam.faces.event.qualifier.ApplyRequestValues;
import org.jboss.seam.faces.event.qualifier.Before;
import org.jboss.seam.faces.examples.viewconfig.security.Admin;
import org.jboss.seam.faces.examples.viewconfig.security.Owner;
import org.jboss.seam.faces.rewrite.FacesRedirect;
import org.jboss.seam.faces.rewrite.UrlMapping;
import org.jboss.seam.faces.security.AccessDeniedView;
import org.jboss.seam.faces.security.LoginView;
import org.jboss.seam.faces.view.action.ViewAction;
import org.jboss.seam.faces.view.action.ViewController;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;

/**
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@ViewConfig
public interface MyAppViewConfig {

    static enum Pages {

        @ViewPattern("/admin.xhtml")
        @Admin
        ADMIN,

        @UrlMapping(pattern = "/item/#{id}/")
        @ViewPattern("/item.xhtml")
        @ViewController(PageController.class)
        @Owner
        @ViewAction("#{pageController.viewAction(pageController.item)}")
        @Before @ApplyRequestValues
        ITEM,

        @ViewPattern("/viewcontroller.xhtml")
        @ViewController(org.jboss.seam.faces.examples.viewconfig.ViewController.class)
        VIEW_CONTROLLER,

        @ViewPattern("/viewactionbindingtype.xhtml")
        VIEW_ACTION_BINDING_TYPE,

        @ViewPattern("/viewaction.xhtml")
        @ViewAction("#{viewActionController.preRenderAction}")
        VIEW_ACTION,

        @FacesRedirect
        @ViewPattern("/*")
        @AccessDeniedView("/denied.xhtml")
        @LoginView("/login.xhtml")
        ALL;

    }
}
