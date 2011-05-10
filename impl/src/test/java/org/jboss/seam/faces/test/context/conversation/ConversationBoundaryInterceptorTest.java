package org.jboss.seam.faces.test.context.conversation;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.context.conversation.ConversationBoundaryInterceptor;
import org.jboss.seam.faces.test.MockLogger;
import org.jboss.seam.faces.test.MockLoggerProducer;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class ConversationBoundaryInterceptorTest {
    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap
                .create(JavaArchive.class)
                .addClasses(ConversationBoundaryInterceptor.class, ConversationalBean.class, MockLogger.class, MockLoggerProducer.class)
                .addManifestResource(
                        ConversationBoundaryInterceptorTest.class.getPackage().getName().replaceAll("\\.", "/")
                                + "/ConversationBoundaryInterceptorTest-beans.xml", ArchivePaths.create("beans.xml"));
    }

    @Inject
    Conversation conversation;

    @Inject
    private ConversationalBean interceptedBean;

    @Test
    public void testConversationStarted() {
        assertTrue(conversation.isTransient());
        assertFalse(interceptedBean.isConversationLongRunningInsideMethodCall());

        interceptedBean.beginConversation();

        assertFalse(conversation.isTransient());
        assertTrue(interceptedBean.isConversationLongRunningInsideMethodCall());
    }

    // @Test
    public void testConversationStartedWithTimeout() {
        assertTrue(conversation.isTransient());
        assertFalse(interceptedBean.isConversationLongRunningInsideMethodCall());

        interceptedBean.beginConversation();

        assertEquals(1000, conversation.getTimeout());
        assertFalse(conversation.isTransient());
        assertTrue(interceptedBean.isConversationLongRunningInsideMethodCall());
    }

    // @Test
    public void testConversationBeginsAndEnds() {
        assertTrue(conversation.isTransient());
        assertFalse(interceptedBean.isConversationLongRunningDuringInvocation2());

        interceptedBean.beginAndEndConversation();

        assertTrue(conversation.isTransient());
        assertTrue(interceptedBean.isConversationLongRunningDuringInvocation2());
    }

    // @Test
    public void testConversationAbortsBeginOnFatalException() {
        assertTrue(conversation.isTransient());
        assertFalse(interceptedBean.isConversationLongRunningDuringInvocation3());

        try {
            interceptedBean.beginAndThrowFatalException();
        } catch (Exception e) {
            // expected
        }

        assertTrue(conversation.isTransient());
        assertTrue(interceptedBean.isConversationLongRunningDuringInvocation3());
    }

    // @Test
    public void testConversationBeginsOnPermittedException() {
        assertTrue(conversation.isTransient());
        assertFalse(interceptedBean.isConversationLongRunningDuringInvocation4());

        try {
            interceptedBean.beginAndThrowPermittedException();
        } catch (Exception e) {
            // expected
        }

        assertFalse(conversation.isTransient());
        assertTrue(interceptedBean.isConversationLongRunningDuringInvocation4());
    }

    // @Test
    public void testConversationAbortsEndOnFatalException() {
        assertTrue(conversation.isTransient());
        assertFalse(interceptedBean.isConversationLongRunningDuringInvocation5());

        try {
            interceptedBean.begin();
            interceptedBean.endAndThrowFatalException();
        } catch (Exception e) {
            // expected
        }

        assertFalse(conversation.isTransient());
        assertTrue(interceptedBean.isConversationLongRunningDuringInvocation5());
    }

    // @Test
    public void testConversationEndsOnPermittedException() {
        assertTrue(conversation.isTransient());
        assertFalse(interceptedBean.isConversationLongRunningDuringInvocation6());

        try {
            interceptedBean.begin();
            interceptedBean.endAndThrowPermittedException();
        } catch (Exception e) {
            // expected
        }

        assertTrue(conversation.isTransient());
        assertTrue(interceptedBean.isConversationLongRunningDuringInvocation6());
    }
}
