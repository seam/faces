package org.jboss.seam.faces.view.config;

import javax.enterprise.event.Observes;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.jboss.seam.faces.security.Restrict;
import org.jboss.seam.solder.el.Expressions;

/**
 *
 * @author bleathem
 */
public class ViewConfigSecurityEnforcer
{
   private static final Logger log = Logger.getLogger(ViewConfigSecurityEnforcer.class);

   @Inject
   private ViewConfigStore metaStore;
   @Inject
   private Expressions expressions;

   public void enforce (@Observes PreRenderViewEvent event)
   {
      log.info("PostConstructViewMapEvent");
      Restrict annotation = metaStore.getDataForCurrentViewId(Restrict.class);
      if (annotation == null)
      {
         log.info("Annotation is null");
         return;
      }
      log.info("Evaluating Annotation");
      String el = annotation.value();
      Boolean allowed = expressions.evaluateMethodExpression(el, Boolean.class);
      if (allowed)
      {
          log.info("Access allowed");
          return;
      }
      else
      {
          log.info("Access denied");
          throw new AbortProcessingException("Access denied");
      }
   }
}
