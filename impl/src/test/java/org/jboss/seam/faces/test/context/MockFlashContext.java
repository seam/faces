package org.jboss.seam.faces.test.context;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Typed;

import org.jboss.seam.faces.context.RenderContext;

/**
 * A mock {@link RenderContext} that can be injected into tests.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
@Typed(RenderContext.class)
public class MockFlashContext implements RenderContext, Serializable
{
   private static final long serialVersionUID = 7502050909452181348L;
   private Integer id = null;
   private final Map<String, Object> map = new ConcurrentHashMap<String, Object>();

   public Object get(final String key)
   {
      return map.get(key);
   }

   public Integer getId()
   {
      return id;
   }

   public void setId(final Integer id)
   {
      this.id = id;
   }

   public boolean isEmpty()
   {
      return map.isEmpty();
   }

   public void put(final String key, final Object value)
   {
      map.put(key, value);
   }

}
