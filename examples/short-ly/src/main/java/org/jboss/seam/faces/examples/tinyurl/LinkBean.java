/**
 * 
 */
package org.jboss.seam.faces.examples.tinyurl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.jboss.seam.faces.context.conversation.Begin;
import org.jboss.seam.faces.context.conversation.End;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named
@ConversationScoped
@Stateful
public class LinkBean implements Serializable
{
   private static final long serialVersionUID = -2209547152337410725L;

   @PersistenceContext
   private EntityManager em;

   @Resource(name = "java:app/Application_Level_DataSource")
   private DataSource ds;

   private TinyLink link = new TinyLink();

   @Begin
   @End
   public String createLink() throws SQLException
   {
      System.out.println("Created link: [ " + link.getName() + " => " + link.getTarget() + " ]");
      em.persist(link);
      // ds.getConnection();
      return "pretty:create";
   }

   @SuppressWarnings("unchecked")
   public TinyLink getByKey(final String key)
   {
      Query query = em.createQuery("from TinyLink t where t.name=:key", TinyLink.class);
      query.setParameter("key", key);
      List<TinyLink> resultList = query.getResultList();
      if (resultList.isEmpty())
      {
         return new TinyLink();
      }
      return resultList.get(0);
   }

   public String format(final String link)
   {
      if (link != null)
      {
         String result = link.trim();
         if (!result.matches("(http|ftp)://.*"))
         {
            result = "http://" + result;
         }
         return result;
      }
      return "";
   }

   public String deleteAll()
   {
      em.createQuery("delete from TinyLink").executeUpdate();
      return "pretty:";
   }

   public List<TinyLink> getLinks()
   {
      return em.createQuery("from TinyLink").getResultList();
   }

   public TinyLink getLink()
   {
      return link;
   }

   public void setLink(final TinyLink link)
   {
      this.link = link;
   }
}
