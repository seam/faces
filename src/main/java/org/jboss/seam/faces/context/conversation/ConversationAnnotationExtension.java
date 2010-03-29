/**
 * 
 */
package org.jboss.seam.faces.context.conversation;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

import org.jboss.weld.manager.BeanManagerImpl;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ConversationAnnotationExtension implements Extension
{
   public void beforeBeanDiscovery(@Observes final BeforeBeanDiscovery event, final BeanManager manager)
   {
      if (manager instanceof BeanManagerImpl)
      {
         BeanManagerImpl impl = (BeanManagerImpl) manager;

         List<Class<?>> list = new ArrayList<Class<?>>();
         list.addAll(impl.getEnabledInterceptorClasses());
         list.add(BeginConversationInterceptor.class);
         impl.setEnabledInterceptorClasses(list);
      }
   }
}
