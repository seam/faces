package org.jboss.seam.faces.view.action;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

import org.jboss.seam.faces.view.action.NonContextual.Instance;
import org.jboss.seam.faces.view.config.ViewConfigDescriptor;
import org.jboss.seam.faces.view.config.ViewConfigStore;
import org.jboss.solder.logging.Logger;
import org.jboss.solder.reflection.AnnotationInspector;

/**
 * Retrieves all annotations qualified by ViewAction, and add their corresponding ViewActionHandler as view actions.
 * 
 * @author Adrian Gonzalez
 */
public class ViewActionExtension implements Extension {
    private final Logger log = Logger.getLogger(ViewActionExtension.class);
    private BeanManager beanManager;
    private ViewConfigStore viewConfigStore;
    private List<Instance<ViewActionHandlerProvider<?>>> nonContextualObjects = new ArrayList<Instance<ViewActionHandlerProvider<?>>>();

    public void setup(@Observes AfterDeploymentValidation event, javax.enterprise.inject.Instance<ViewConfigStore> viewConfigStore, BeanManager beanManager) {
        this.beanManager = beanManager;
        if (viewConfigStore.isUnsatisfied()) {
            // extension disabled : surely because we're in come UT context (i.e. ProjectStageExtensionTest)
            // not adding ViewConfigStore in ShrinkWrap archive
            log.warn("ViewConfigStore dependency missing - ViewActionExtension disabled");
            return;
        }
        this.viewConfigStore = viewConfigStore.get();
        registerViewActions();
    }

    @PreDestroy
    public void destroy() {
        for (Instance<ViewActionHandlerProvider<?>> instance : nonContextualObjects) {
            instance.preDestroy();
            instance.dispose();
        }
    }
    
    private void registerViewActions() {
        List<ViewConfigDescriptor> viewConfigDescriptors = viewConfigStore.getViewConfigDescriptors();
        for (ViewConfigDescriptor viewConfigDescriptor : viewConfigDescriptors) {
            for (Annotation metaData : viewConfigDescriptor.getMetaData()) {
                ViewAction viewAction = AnnotationInspector.getAnnotation(metaData.annotationType(), ViewAction.class,
                        beanManager);
                if (viewAction != null) {
                    Class<? extends ViewActionHandlerProvider<? extends Annotation>> providerClazz = viewAction.value();
                    ViewActionHandlerProvider<? extends Annotation> provider = newViewActionProvider(metaData, providerClazz);
                    for (ViewActionHandler viewActionHandler : provider.getActionHandlers()) {
                        viewConfigDescriptor.addViewActionHandler(viewActionHandler);
                    }
                }
            }
        }
    }

    /**
     * Creates a viewActionProvider which can be injected using CDI annotations. 
     * 
     * @param annotation
     * @param providerClazz
     * @return
     */
    @SuppressWarnings("unchecked")
    private <E extends Annotation> ViewActionHandlerProvider<E> newViewActionProvider(Annotation annotation,
            Class<? extends ViewActionHandlerProvider<? extends Annotation>> providerClazz) {
        NonContextual<ViewActionHandlerProvider<?>> nonContextual = new NonContextual<ViewActionHandlerProvider<?>>(beanManager,
                (Class<ViewActionHandlerProvider<?>>) providerClazz);
        Instance<ViewActionHandlerProvider<?>> instance = nonContextual.newInstance();
        instance.produce();
        instance.inject();
        instance.postConstruct();
        nonContextualObjects.add(instance);
        ViewActionHandlerProvider<E> provider = (ViewActionHandlerProvider<E>) instance.get();
        provider.initialize((E) annotation);
        return provider;
    }

}
