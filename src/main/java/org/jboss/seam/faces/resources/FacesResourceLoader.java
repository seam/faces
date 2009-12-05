package org.jboss.seam.faces.resources;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.faces.context.FacesContext;

import org.jboss.seam.beans.RuntimeSelected;
import org.jboss.seam.beans.RuntimeSelectedBean;
import org.jboss.seam.resources.DefaultResourceLoader;
import org.jboss.webbeans.log.Log;
import org.jboss.webbeans.log.Logger;

/**
 * Extend the {@link DefaultResourceLoader} in the JSF environment to first
 * attempt to locate the resource in the web root.
 * 
 * @author Dan Allen
 */
public
@RuntimeSelected
class FacesResourceLoader extends DefaultResourceLoader implements RuntimeSelectedBean
{
   @Logger Log log;
   @Inject FacesContext facesContext;
   @Inject Locale locale;
   
   public boolean isActive()
   {
      return facesContext != null && facesContext.getCurrentPhaseId() != null && facesContext.getExternalContext() != null;
   }

   @Override
   protected ResourceBundle loadBundleInternal(String bundleName)
   {
      return ResourceBundle.getBundle(bundleName, locale, Thread.currentThread().getContextClassLoader());
   }

   @Override
   protected URL getResourceInternal(String absolutePath, String relativePath)
   {
      URL url = null;
      try
      {
         url = facesContext.getExternalContext().getResource(absolutePath);
      }
      catch (MalformedURLException e) {}
      
      if (url != null)
      {
         log.debug("Loaded resource from servlet context: " + url);
         return url;
      }
      
      return super.getResourceInternal(absolutePath, relativePath);
   }
   
   @Override
   protected InputStream getResourceAsStreamInternal(String absolutePath, String relativePath)
   {
      InputStream stream = facesContext.getExternalContext().getResourceAsStream(absolutePath);
      if (stream != null)
      {
         log.debug("Loaded resource stream from servlet context: " + absolutePath);
      }
      
      return super.getResourceAsStreamInternal(absolutePath, relativePath);
   }

}
