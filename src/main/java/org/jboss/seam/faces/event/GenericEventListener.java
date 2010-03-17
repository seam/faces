package org.jboss.seam.faces.event;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.weld.Container;

public class GenericEventListener
{
   @Inject BeanManager beanManager;
   
   protected BeanManager getBeanManager()
   {
      return beanManager != null ? beanManager : Container.instance().deploymentManager();
   }
}
