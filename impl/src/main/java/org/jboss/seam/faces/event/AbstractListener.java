package org.jboss.seam.faces.event;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.seam.solder.beanManager.BeanManagerAware;

/**
 * Superclass for event listeners
 * 
 * @author Nicklas Karlsson
 *
 * @param <T> Listener class
 */
public class AbstractListener<T> extends BeanManagerAware
{
   @SuppressWarnings("unchecked")
   protected List<T> getListeners(Class<? extends T>... classes)
   {
      List<T> listeners = new ArrayList<T>();
      for (Class<? extends T> clazz : classes) {
         Bean<? extends T> bean = (Bean<? extends T>) getBeanManager().getBeans(clazz).iterator().next();
         CreationalContext<? extends T> context = getBeanManager().createCreationalContext(bean);
         T listener = (T) getBeanManager().getReference(bean, clazz, context);
         listeners.add(listener);
      }
      return listeners;
   }
   
}
