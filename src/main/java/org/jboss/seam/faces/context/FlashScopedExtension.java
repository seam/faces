/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package org.jboss.seam.faces.context;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.faces.bean.FlashScoped;

/**
 * An extension to provide @FlashScoped CDI / JSF 2 integration.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FlashScopedExtension implements Extension
{

   public void addScope(@Observes final BeforeBeanDiscovery event)
   {
      event.addScope(FlashScoped.class, true, true);
   }

   public void registerContext(@Observes final AfterBeanDiscovery event)
   {
      event.addContext(new FlashScopedContext());
   }

}
