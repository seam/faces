package org.jboss.seam.faces.config;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Automatically registers the JSF FacesServlet if it's not already configured
 * by the application (WEB-INF/web.xml).
 * 
 * Only activates in a Servlet 3.0 environment. Most Java EE 6 application servers
 * will register FacesServlet automatically, but in some cases require certain
 * resources to be present in the application (i.e., a JSF component class or
 * WEB-INF/faces-config.xml). Since the application is using Seam Faces, we assume
 * intent to use JSF.
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public class FacesServletInitializer implements ServletContainerInitializer
{
   private static final Logger log = Logger.getLogger(FacesServletInitializer.class.getName());
   
   private static final String FACES_SERVLET_CLASS_NAME = "javax.faces.webapp.FacesServlet";

   private static final String FACES_SERVLET_NAME = "FacesServlet";

   private static final String[] FACES_SERVLET_MAPPINGS = new String[] { "/faces/*", "*.jsf", "*.faces" };

   public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException
   {
      if (!isFacesServletConfigured(ctx))
      {
         registerFacesServlet(ctx);
      }
   }
   
   private void registerFacesServlet(ServletContext ctx)
   {
      log.info("Auto-registering FacesServlet with mappings: " + Arrays.asList(FACES_SERVLET_MAPPINGS));
      ServletRegistration.Dynamic facesServlet = ctx.addServlet(FACES_SERVLET_NAME, FACES_SERVLET_CLASS_NAME);
      facesServlet.addMapping(FACES_SERVLET_MAPPINGS);
   }

   private boolean isFacesServletConfigured(ServletContext ctx)
   {
      Map<String, ? extends ServletRegistration> servletRegistrations = ctx.getServletRegistrations();
      for (ServletRegistration registration : servletRegistrations.values())
      {
         if (FACES_SERVLET_CLASS_NAME.equals(registration.getClassName()))
         {
            return true;
         }
      }

      return false;
   }
}
