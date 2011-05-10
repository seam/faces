package org.jboss.seam.faces.test.context;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.context.FacesAnnotationsAdapterExtension;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class FacesAnnotationsAdapterExtensionTest {

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class).addClasses(ImproperlyAnnotatedBean.class)
                .addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
    }

    @Test
    public void testImproperlyAnnotatedClassIsCaptured() {
        assertTrue(FacesAnnotationsAdapterExtension.getAliasedbeans().containsKey(ImproperlyAnnotatedBean.class));
    }
}
