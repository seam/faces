package org.jboss.seam.faces.international;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.util.List;

import javax.enterprise.inject.AnnotationLiteral;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.jboss.seam.el.Expressions;
import org.jboss.seam.el.ExpressionsProducer;
import org.jboss.seam.faces.Faces;
import org.jboss.seam.international.Interpolator;
import org.jboss.seam.international.LocaleProducer;
import org.jboss.seam.international.LocaleResolver;
import org.jboss.seam.international.LocaleResolverProducer;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.mock.faces.MockApplication;
import org.jboss.seam.mock.faces.MockFacesContext;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.webbeans.context.ConversationContext;
import org.jboss.webbeans.log.LoggerProducer;
import org.jboss.webbeans.test.AbstractWebBeansTest;
import org.jboss.weld.context.api.helpers.ConcurrentHashMapBeanStore;
import org.testng.annotations.Test;

/**
 * First and foremost, ensure that FacesMessage is configured properly to wrap
 * the StatusMessages component. Once loaded, verify that FacesMessages adds the
 * appropriate JSF-specific functionality to the conversation-scoped
 * StatusMessages repository.
 * 
 * @author Dan Allen
 * 
 * @see FacesMessages
 * @see StatusMessages
 */
@Artifact(addCurrentPackage = false)
@Classes(
{
   LoggerProducer.class, FacesMessages.class, StatusMessages.class, Interpolator.class,
   Expressions.class, ExpressionsProducer.class, LocaleProducer.class, LocaleResolver.class, LocaleResolverProducer.class
})
public class FacesMessagesTest extends AbstractWebBeansTest
{
   @Override
   public void beforeMethod()
   {
      super.beforeMethod();
      activateConversationContext();
      installMockFacesContext();
   }

   /**
    * Test that the StatusMessage objects properly tranfer to FacesMessage objects
    * upon the call to FacesMessages#onBeforeRender().
    */
   // FIXME broken
   //@Test
   public void testGlobalStatusMessagesGetTransferedToFacesMessages()
   {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      facesContext.setCurrentPhaseId(PhaseId.INVOKE_APPLICATION);
      StatusMessages statusMessages = getStatusMessagesInstance();

      statusMessages.add("You've booked a night at {0}. Bon chance!", "Mandalay Bay");

      assertEquals(statusMessages.getGlobalMessages().size(), 1);
      assertEquals(facesContext.getMessageList().size(), 0);

      facesContext.setCurrentPhaseId(PhaseId.RENDER_RESPONSE);
      statusMessages.onBeforeRender();

      assertEquals(statusMessages.getGlobalMessages().size(), 0);
      assertEquals(facesContext.getMessageList().size(), 1);
      assertEquals(facesContext.getMessageList(null).size(), 1);
      FacesMessage facesMessage = facesContext.getMessageList().get(0);
      assertEquals(facesMessage.getSeverity(), FacesMessage.SEVERITY_INFO);
      assertEquals(facesMessage.getSummary(), "You've booked a night at Mandalay Bay. Bon chance!");
      // NOTE this assignment happens inside the FacesMessage implementation
      assertEquals(facesMessage.getSummary(), facesMessage.getDetail());
   }

   /**
    * Verify that a message can be added to a control based on either it's absolute client id
    * or it's local id. Verify that if the component cannot be found, a global message is created.
    */
   // FIXME broken
   //@Test
   public void testStatusMessagesForControlGetTransferedToFacesMessages()
   {
      FacesContext facesContext = FacesContext.getCurrentInstance();

      facesContext.setCurrentPhaseId(PhaseId.RESTORE_VIEW);
      UIViewRoot viewRoot = new UIViewRoot();
      UIComponent form = new UINamingContainer();
      form.setId("form");
      UIComponent input = new UIInput();
      input.setId("input");
      form.getChildren().add(input);
      viewRoot.getChildren().add(form);
      facesContext.setViewRoot(viewRoot);

      facesContext.setCurrentPhaseId(PhaseId.INVOKE_APPLICATION);
      StatusMessages statusMessages = getStatusMessagesInstance();
      statusMessages.addToControl("input", StatusMessage.Severity.WARN, "First validation message for input");
      statusMessages.addToControl("form:input", StatusMessage.Severity.WARN, "Second validation message for input");
      statusMessages.addToControl("NO_SUCH_ID", StatusMessage.Severity.WARN, "Validation message that becomes global");

      assertEquals(statusMessages.getKeyedMessages().size(), 3);
      assertEquals(facesContext.getMessageList().size(), 0);

      facesContext.setCurrentPhaseId(PhaseId.RENDER_RESPONSE);
      statusMessages.onBeforeRender();

      assertEquals(statusMessages.getGlobalMessages().size(), 0);
      assertEquals(facesContext.getMessageList().size(), 3);
      assertEquals(facesContext.getMessageList(null).size(), 1);
      FacesMessage globalMessage = facesContext.getMessageList(null).get(0);
      assertSame(globalMessage.getSeverity(), FacesMessage.SEVERITY_WARN);
      assertEquals(globalMessage.getSummary(), "Validation message that becomes global");
      List<FacesMessage> messagesForInput = facesContext.getMessageList("form:input");
      assertEquals(messagesForInput.size(), 2);
      assertEquals(messagesForInput.get(0).getSummary(), "First validation message for input");
      assertEquals(messagesForInput.get(1).getSummary(), "Second validation message for input");
   }

   // TODO either test TransferStatusMessagesListener here or create a dedicated test for it; to test lookup of StatusMessages from listener

   private void installMockFacesContext()
   {
      new MockFacesContext(new MockApplication(), true).setCurrent();
   }

   private void activateConversationContext()
   {
      // TODO fix
      //ConversationContext.instance().setBeanStore(new ConcurrentHashMapBeanStore());
      //ConversationContext.instance().setActive(true);
   }

   /**
    * Retrieve the StatusMessage instance, which is expected to be the type FacesMessages.
    */
   private StatusMessages getStatusMessagesInstance()
   {
      StatusMessages statusMessages = getCurrentManager().getInstanceByType(StatusMessages.class, new AnnotationLiteral<Faces>() {});
      assert statusMessages instanceof FacesMessages;
      return statusMessages;
   }
}
