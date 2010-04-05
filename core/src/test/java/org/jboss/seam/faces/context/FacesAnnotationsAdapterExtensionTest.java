/**
 * 
 */
package org.jboss.seam.faces.context;

import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class FacesAnnotationsAdapterExtensionTest
{

   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class).addClasses(ImproperlyAnnotatedBean.class).addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }

   @Test
   public void testImproperlyAnnotatedClassIsCaptured()
   {
      assertTrue(FacesAnnotationsAdapterExtension.aliasedBeans.containsKey(ImproperlyAnnotatedBean.class));
   }
}
