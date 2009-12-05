package org.jboss.seam.faces.facelets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.el.ValueExpression;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;

public class DeferredValueExpressionHandler extends ComponentHandler
{
   public static final String REQUIRE_ATTRIBUTE_NAME = "require";
   
   public DeferredValueExpressionHandler(ComponentConfig config)
   {
      super(config);
   }

   @SuppressWarnings("unchecked")
   @Override
   protected MetaRuleset createMetaRuleset(Class type)
   {
      return super.createMetaRuleset(type).addRule(new RequiredRule());
   }
   
   private class RequiredRule extends MetaRule
   {
      @Override
      public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta)
      {
         if (REQUIRE_ATTRIBUTE_NAME.equals(name) && ValueExpression.class.equals(meta.getPropertyType(name)))
         {
            return new ValueExpressionMetadata(meta.getWriteMethod(name), attribute, Boolean.class);
         }
         
         return null;
      }
   }
   
   private static class ValueExpressionMetadata extends Metadata
   {
      private final Method method;

      private final TagAttribute attribute;

      private Class returnType;

      public ValueExpressionMetadata(Method method, TagAttribute attribute, Class returnType)
      {
         this.method = method;
         this.attribute = attribute;
         this.returnType = returnType;
      }

      public void applyMetadata(FaceletContext ctx, Object instance)
      {
         ValueExpression expr = attribute.getValueExpression(ctx, returnType);

         try
         {
            // this invocation is to assign the value expression to the property of the component
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
