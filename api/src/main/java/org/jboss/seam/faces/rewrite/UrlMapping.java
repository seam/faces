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
package org.jboss.seam.faces.rewrite;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping URLs to JSF Views.
 *
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Documented
public @interface UrlMapping {
    /**
     * <p>
     * Specify the pattern for which this URL will be matched. This element is
     * required.
     * </p>
     * <p>
     * Any EL expressions #{someBean.paramName} found within the pattern will be
     * processed as value injections. The URL will be parsed and the value found
     * at the location of the EL expression will be injected into the location
     * specified in that EL expression. Note: EL expressions will not match over
     * the ‘/’ character.
     * </p>
     * <p>
     * The pattern itself is compiled parsed as a regular expression, meaning
     * that the actual URL matching can be as simple or as complex as desired.
     * </p>
     */
    String pattern();

    /**
     * Enable or disable outbound URL rewriting for this mapping (default: 'true'
     * / enabled.) If enabled, any links matching the viewId specified will be
     * rewritten (if possible) using parameters mapping to named path parameters
     * specified in the pattern.
     */
    boolean outbound() default true;

    /**
     * <p>
     * Optional boolean (default true), if set to <code>false</code>, path
     * parameters will not be injected on form postbacks.
     * </p>
     */
    boolean onPostback() default true;

}

