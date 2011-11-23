package org.jboss.seam.faces.view.action.el;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;

import org.jboss.seam.faces.view.action.SimpleViewActionHandlerProvider;

public class BeginConversationHandlerProvider extends SimpleViewActionHandlerProvider<BeginConversation> {
    private boolean join;

    @Inject
    private Conversation conversation;

    @Override
    public void doInitialize(BeginConversation annotation) {
        join = annotation.join();
    }

    @Override
    public Object execute() {
        if (join && !conversation.isTransient()) {
            return null;
        }
        conversation.begin();
        return null;
    }
}
