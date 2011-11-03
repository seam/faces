package org.jboss.seam.faces.view.action;

import static org.jboss.seam.faces.view.action.PhaseInstant.BEFORE_RENDER_RESPONSE;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.seam.faces.view.config.ViewConfigDescriptor;
import org.jboss.seam.faces.view.config.ViewConfigStore;

/**
 * Data store for view controllers.
 * 
 * @author Adriàn gonzalez
 */
public class ViewControllerStore {
    /** map containing view pattern / controller */
    private Map<String, List<ViewControllerDescriptor>> viewPatternControllerDescriptors = new HashMap<String, List<ViewControllerDescriptor>>();
    private ConcurrentHashMap<String, List<ViewControllerDescriptor>> viewControllerDescriptorsCache = new ConcurrentHashMap<String, List<ViewControllerDescriptor>>();

    /**
     * Initialization : Retrieves any ViewControllers associated to ViewConfig objects
     */
    @Inject
    public void setup(ViewControllerExtension viewControllerExtension, ViewConfigStore viewConfigStore, BeanManager beanManager) {
        registerViewControllers(viewConfigStore, beanManager);
        registerViewActions(viewConfigStore, beanManager);
        registerViewActionBindingTypes(viewControllerExtension, viewConfigStore, beanManager);
    }

    private void registerViewControllers(ViewConfigStore viewConfigStore, BeanManager beanManager) {
        Map<String, Annotation> views = viewConfigStore.getAllAnnotationViewMap(ViewController.class);
        for (Map.Entry<String, Annotation> entry : views.entrySet()) {
            ViewController annotation = (ViewController) entry.getValue();
            if (annotation.value() == null) {
                throw new IllegalArgumentException("Invalid ViewConfig for view '" + entry.getKey()
                        + "' : @ViewController must have a non null value.");
            }
            for (Class<?> viewControllerClass : annotation.value()) {
                ViewControllerDescriptor viewControllerDescriptor = createViewControllerDescriptor(entry.getKey(),
                        viewControllerClass, beanManager);
                addControllerDescriptor(viewControllerDescriptor);
            }
        }
    }

    private void registerViewActionBindingTypes(ViewControllerExtension viewControllerExtension,
            ViewConfigStore viewConfigStore, BeanManager beanManager) {
        List<ViewConfigDescriptor> viewConfigDescriptors = viewConfigStore.getAllViewConfigDescriptors();
        for (ViewConfigDescriptor viewConfigDescriptor : viewConfigDescriptors) {
            List<ViewActionBindingTypeDescriptor> viewActionBindingTypes = new ArrayList<ViewActionBindingTypeDescriptor>();
            for (Object value : viewConfigDescriptor.getValues()) {
                List<ViewActionBindingTypeDescriptor> current = viewControllerExtension.getViewActionBindingTypeDescriptors()
                        .get(value);
                if (current != null) {
                    viewActionBindingTypes.addAll(current);
                }
            }
            if (viewActionBindingTypes.size() > 0) {
                ViewControllerDescriptor viewControllerDescriptor = new ViewControllerDescriptor(
                        viewConfigDescriptor.getViewId(), beanManager);
                for (ViewActionBindingTypeDescriptor viewActionBindingTypeDescriptor : viewActionBindingTypes) {
                    viewControllerDescriptor.addMethod(
                            viewActionBindingTypeDescriptor.getPhaseInstant(),
                            new ViewControllerDescriptor.AnnotatedMethodInvoker(viewActionBindingTypeDescriptor
                                    .getAnnotatedMethod(), beanManager));
                }
                addControllerDescriptor(viewControllerDescriptor);
            }
        }
    }

    private void registerViewActions(ViewConfigStore viewConfigStore, BeanManager beanManager) {
        List<ViewConfigDescriptor> viewConfigDescriptors = viewConfigStore.getAllViewConfigDescriptors();
        for (ViewConfigDescriptor viewConfigDescriptor : viewConfigDescriptors) {
            ViewAction viewAction = viewConfigDescriptor.getMetaData(ViewAction.class);
            if (viewAction != null) {
                ViewControllerDescriptor viewControllerDescriptor = new ViewControllerDescriptor(
                        viewConfigDescriptor.getViewId(), beanManager);
                PhaseInstant phaseInstant = new PhaseInstant(ViewActionUtils.convert(viewAction.phase()), viewAction.before());
                viewControllerDescriptor.addMethod(phaseInstant, new ViewControllerDescriptor.MethodExpressionInvoker(
                        viewAction.value()));
                addControllerDescriptor(viewControllerDescriptor);
            }
        }
    }

    private ViewControllerDescriptor createViewControllerDescriptor(String viewId, Class<?> controllerViewClass,
            BeanManager beanManager) {
        return new ViewControllerDescriptor(viewId, controllerViewClass, beanManager);
    }

    public void addControllerDescriptor(ViewControllerDescriptor controllerDescriptor) {
        List<ViewControllerDescriptor> descriptors = viewPatternControllerDescriptors.get(controllerDescriptor.getViewId());
        if (descriptors == null) {
            descriptors = new ArrayList<ViewControllerDescriptor>();
        }
        descriptors.add(controllerDescriptor);
        viewPatternControllerDescriptors.put(controllerDescriptor.getViewId(), descriptors);
    }

    /**
     * Returns contollers matching a viewId.
     * 
     * Controllers are ordered from best matching viewId (longest) to least matching one.
     */
    public List<ViewControllerDescriptor> getControllerDescriptors(String viewId) {
        List<ViewControllerDescriptor> controllers = viewControllerDescriptorsCache.get(viewId);
        if (controllers == null) {
            controllers = new ArrayList<ViewControllerDescriptor>();
            List<String> viewPatterns = findViewsWithPatternsThatMatch(viewId, viewPatternControllerDescriptors.keySet());
            for (String viewPattern : viewPatterns) {
                List<ViewControllerDescriptor> viewPatternControllers = viewPatternControllerDescriptors.get(viewPattern);
                controllers.addAll(viewPatternControllers);
            }
            viewControllerDescriptorsCache.putIfAbsent(viewId, controllers);
        }
        return controllers;
    }

    // Copied from ViewConfigStoreImpl : extract into utility method //

    private List<String> findViewsWithPatternsThatMatch(String viewId, Set<String> viewPatterns) {
        List<String> resultingViews = new ArrayList<String>();
        for (String viewPattern : viewPatterns) {
            if (viewPattern.endsWith("*")) {
                String cutView = viewPattern.substring(0, viewPattern.length() - 1);
                if (viewId.startsWith(cutView)) {
                    resultingViews.add(viewPattern);
                }
            } else {
                if (viewPattern.equals(viewId)) {
                    resultingViews.add(viewPattern);
                }
            }
        }
        // sort the keys by length, longest is the most specific and so should go first
        Collections.sort(resultingViews, StringLengthComparator.INSTANCE);
        return resultingViews;
    }

    private static class StringLengthComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            if (o1.length() > o2.length()) {
                return -1;
            }
            if (o1.length() < o2.length()) {
                return 1;
            }
            return 0;
        }

        public static final StringLengthComparator INSTANCE = new StringLengthComparator();

    }
}
