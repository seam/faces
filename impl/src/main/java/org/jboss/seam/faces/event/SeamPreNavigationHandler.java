package org.jboss.seam.faces.event;

import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import org.jboss.seam.solder.beanManager.BeanManagerLocator;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public class SeamPreNavigationHandler extends ConfigurableNavigationHandler
{
   private final ConfigurableNavigationHandler parent;

   public SeamPreNavigationHandler(final ConfigurableNavigationHandler parent)
   {
      this.parent = parent;
   }

   @Override
   public NavigationCase getNavigationCase(final FacesContext context, final String fromAction, final String outcome)
   {
      return parent.getNavigationCase(context, fromAction, outcome);
   }

   @Override
   public Map<String, Set<NavigationCase>> getNavigationCases()
   {
      return parent.getNavigationCases();
   }

   @Override
   public void handleNavigation(final FacesContext context, final String fromAction, final String outcome)
   {
      BeanManager manager = new BeanManagerLocator().getBeanManager();
      NavigationHandler navigationHandler = context.getApplication().getNavigationHandler();

      NavigationCase navigationCase;
      if (navigationHandler instanceof ConfigurableNavigationHandler)
      {
         navigationCase = ((ConfigurableNavigationHandler) navigationHandler).getNavigationCase(context, fromAction, outcome);
      }
      else
      {
         navigationCase = getNavigationCase(context, fromAction, outcome);
      }
      manager.fireEvent(new PreNavigateEvent(context, fromAction, outcome, navigationCase));
      parent.handleNavigation(context, fromAction, outcome);
   }

}
