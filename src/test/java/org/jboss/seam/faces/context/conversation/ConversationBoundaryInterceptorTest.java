/**
 * 
 */
package org.jboss.seam.faces.context.conversation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.MockConversation;
import org.jboss.seam.faces.MockLogger;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@RunWith(Arquillian.class)
public class ConversationBoundaryInterceptorTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class).addClasses(ConversationBoundaryInterceptor.class, ConversationalBean.class, MockLogger.class, MockConversation.class).addManifestResource(ConversationBoundaryInterceptorTest.class.getPackage().getName().replaceAll("\\.", "/") + "/ConversationBoundaryInterceptorTest-beans.xml", ArchivePaths.create("beans.xml"));
   }

   @Inject
   Conversation conversation;

   @Inject
   private ConversationalBean interceptedBean;

   @Test
   public void testConversationStarted()
   {
      assertTrue(conversation.isTransient());
      assertFalse(interceptedBean.isConversationLongRunningInsideMethodCall());

      interceptedBean.beginConversation();

      assertFalse(conversation.isTransient());
      assertTrue(interceptedBean.isConversationLongRunningInsideMethodCall());
   }

   @Test
   public void testConversationBeginsAndEnds()
   {
      assertTrue(conversation.isTransient());
      assertFalse(interceptedBean.isConversationLongRunningDuringInvocation2());

      interceptedBean.beginAndEndConversation();

      assertTrue(conversation.isTransient());
      assertTrue(interceptedBean.isConversationLongRunningDuringInvocation2());
   }
}
