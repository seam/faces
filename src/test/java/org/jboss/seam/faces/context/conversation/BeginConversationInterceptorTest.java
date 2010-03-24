/**
 * 
 */
package org.jboss.seam.faces.context.conversation;

import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.seam.faces.MockConversation;
import org.jboss.seam.faces.MockLogger;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.experimental.categories.Category;
import org.junit.internal.runners.statements.Fail;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
// @RunWith(Arquillian.class)
public class BeginConversationInterceptorTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class).addClasses(BeginConversationInterceptor.class, BeginConversationBean.class, MockLogger.class, MockConversation.class).addManifestResource(BeginConversationInterceptorTest.class.getPackage().getName().replaceAll("\\.", "/") + "/BeginConversationInterceptorTest-beans.xml", ArchivePaths.create("beans.xml"));
   }

   @Inject
   private BeginConversationBean bean;

   // @Test
   @Category(Fail.class)
   public void testConversationStarted()
   {
      bean.beginConversation();
      assertTrue(bean.conversationStarted);
   }
}
