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
package org.jboss.seam.faces.test.context.conversation;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.seam.faces.SeamFacesException;
import org.jboss.seam.faces.context.conversation.Begin;
import org.jboss.seam.faces.context.conversation.End;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RequestScoped
public class ConversationalBean {
    @Inject
    Conversation conversation;

    private boolean conversationLongRunningDuringInvocation;
    private boolean conversationLongRunningDuringInvocation2;
    private boolean conversationLongRunningDuringInvocation3;
    private boolean conversationLongRunningDuringInvocation4;
    private boolean conversationLongRunningDuringInvocation5;

    private boolean conversationLongRunningDuringInvocation6;

    @Begin
    public void begin() {
    }

    @Begin(timeout = 1000)
    public void beginConversation() {
        if (!conversation.isTransient()) {
            conversationLongRunningDuringInvocation = true;
        }
    }

    @Begin
    @End
    public void beginAndEndConversation() {
        if (!conversation.isTransient()) {
            conversationLongRunningDuringInvocation2 = true;
        }
    }

    @Begin(permit = {SeamFacesException.class})
    public void beginAndThrowFatalException() {
        if (!conversation.isTransient()) {
            conversationLongRunningDuringInvocation3 = true;
        }
        throw new RuntimeException("A vanilla exception.");
    }

    @Begin(permit = {SeamFacesException.class})
    public void beginAndThrowPermittedException() {
        if (!conversation.isTransient()) {
            conversationLongRunningDuringInvocation4 = true;
        }
        throw new SeamFacesException("Just so it's not a vanilla Exception.");
    }

    @End(permit = {SeamFacesException.class})
    public void endAndThrowFatalException() {
        if (!conversation.isTransient()) {
            conversationLongRunningDuringInvocation5 = true;
        }
        throw new RuntimeException("A vanilla exception.");
    }

    @End(permit = {SeamFacesException.class})
    public void endAndThrowPermittedException() {
        if (!conversation.isTransient()) {
            conversationLongRunningDuringInvocation6 = true;
        }
        throw new SeamFacesException("A vanilla exception.");
    }

    public boolean isConversationLongRunningInsideMethodCall() {
        return conversationLongRunningDuringInvocation;
    }

    public boolean isConversationLongRunningDuringInvocation2() {
        return conversationLongRunningDuringInvocation2;
    }

    public boolean isConversationLongRunningDuringInvocation3() {
        return conversationLongRunningDuringInvocation3;
    }

    public boolean isConversationLongRunningDuringInvocation4() {
        return conversationLongRunningDuringInvocation4;
    }

    public boolean isConversationLongRunningDuringInvocation5() {
        return conversationLongRunningDuringInvocation5;
    }

    public boolean isConversationLongRunningDuringInvocation6() {
        return conversationLongRunningDuringInvocation6;
    }

}
