package org.jboss.seam.faces.facelets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.el.MethodExpression;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;

/**
 * An adaptor class for Facelets that allows the method expression attribute to
 * be accepted rather than interpreted as a value expression. This class should
 * be supplemented to support all such tags that require a method expression.
 * 
 * @author Dan Allen
 */
public class ActionSourceHandler extends ComponentHandler
{
   public static final String EXECUTE_ATTRIBUTE_NAME = "execute";

   public ActionSourceHandler(ComponentConfig config)
   {
      super(config);
   }

   @SuppressWarnings("unchecked")
   @Override
   protected MetaRuleset createMetaRuleset(Class type)
   {
      return super.createMetaRuleset(type).addRule(new ExecuteRule());
   }

   private class ExecuteRule extends MetaRule
   {
      @Override
      public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta)
      {
         if (EXECUTE_ATTRIBUTE_NAME.equals(name) && MethodExpression.class.equals(meta.getPropertyType(name)))
         {
            return new MethodExpressionMetadata(meta.getWriteMethod(name), attribute, Object.class, new Class[0]);
         }
         
         return null;
      }
   }

   private static class MethodExpressionMetadata extends Metadata
   {
      private final Method method;

      private final TagAttribute attribute;

      private Class returnType;

      private Class[] argumentTypes;

      public MethodExpressionMetadata(Method method, TagAttribute attribute, Class returnType, Class[] argumentTypes)
      {
         this.method = method;
         this.attribute = attribute;
         this.returnType = returnType;
         this.argumentTypes = argumentTypes;
      }

      public void applyMetadata(FaceletContext ctx, Object instance)
      {
         MethodExpression expr = attribute.getMethodExpression(ctx, returnType, argumentTypes);

         try
         {
            // this invocation is to assign the method expression to the property of the component
            method.invoke(instance, expr);
         }
         catch (InvocationTargetException e)
         {
            throw new TagAttributeException(attribute, e.getCause());
         }
         catch (Exception e)
         {
            throw new TagAttributeException(attribute, e);
         }
      }
   }
}
