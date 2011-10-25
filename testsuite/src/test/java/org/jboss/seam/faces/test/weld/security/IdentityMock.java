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
package org.jboss.seam.faces.test.weld.security;

import java.util.Collection;
import java.util.Set;

import org.jboss.seam.security.Authenticator;
import org.jboss.seam.security.Identity;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.Role;
import org.picketlink.idm.api.User;

/**
 * @author bleathem
 */
public class IdentityMock implements Identity {

    @Override
    public boolean addGroup(String name, String groupType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addRole(String role, String group, String groupType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void checkGroup(String group, String groupType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void checkPermission(Object resource, String permission) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void checkRestriction(String expr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void checkRole(String role, String group, String groupType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void filterByPermission(Collection<?> collection, String permission) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class<Authenticator> getAuthenticatorClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAuthenticatorName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Group> getGroups() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Role> getRoles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User getUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasPermission(Object resource, String permission) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasRole(String role, String group, String groupType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean inGroup(String name, String groupType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isLoggedIn() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isVerified() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String login() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void logout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void quietLogin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeGroup(String name, String groupType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeRole(String role, String group, String groupType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAuthenticatorClass(Class<? extends Authenticator> authenticatorClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAuthenticatorName(String authenticatorName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean tryLogin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
