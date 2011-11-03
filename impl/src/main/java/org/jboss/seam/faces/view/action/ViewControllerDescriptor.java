package org.jboss.seam.faces.view.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.MethodExpression;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.solder.logging.Logger;
import org.jboss.solder.reflection.annotated.InjectableMethod;

/**
 * Information about a particular controller.
 * 
 * A Controller is managed bean which is associated to a given view (a given {@link ViewConfig}).
 * 
 * @author Adriàn Gonzalez
 */
public class ViewControllerDescriptor {

    private transient final Logger log = Logger.getLogger(ViewControllerDescriptor.class);

    private String viewId;
    private Class<?> viewControllerClass;
    private BeanManager beanManager;
    private Map<PhaseInstant, List<ViewActionStrategy>> phaseMethods = new HashMap<PhaseInstant, List<ViewActionStrategy>>();

    /**
     * Creates descriptor.
     * 
     * Lifecycle callback registration is up to the caller.
     * 
     * Note : beanManager parameter is horrible, should be a way to send beanManager more elegantly
     */
    public ViewControllerDescriptor(String viewId, BeanManager beanManager) {
        this.viewId = viewId;
        this.beanManager = beanManager;
        log.debugf("Created viewController #0", this);
    }

    /**
     * Creates descriptor by reading controllerViewClass methods.
     * 
     * Register controllerViewClass lifecycle callback methods.
     * 
     * Note : beanManager parameter is horrible, should be a way to send beanManager more elegantly
     */
    public ViewControllerDescriptor(String viewId, Class<?> viewControllerClass, BeanManager beanManager) {
        this.viewId = viewId;
        this.viewControllerClass = viewControllerClass;
        this.beanManager = beanManager;
        registerCallbacks();
        log.debugf("Created viewController #0", this);
    }

    /**
     * Register any lifecycle methods declared in this viewController class (or inherited).
     */
    private void registerCallbacks() {
        Class<?> current = viewControllerClass;
        while (current != Object.class) {
            for (Method method : current.getDeclaredMethods()) {
                // if (method.isAnnotationPresent(BeforeRenderView.class)) {
                // beforeRenderViewMethods.add(new MethodInvoker(method, beanManager));
                // }
                // if (method.isAnnotationPresent(AfterRenderView.class)) {
                // afterRenderViewMethods.add(new MethodInvoker(method, beanManager));
                // }
                PhaseInstant phaseInstant = ViewActionUtils.getPhaseInstant(Arrays.asList(method.getAnnotations()), method);
                if (phaseInstant != null) {
                    addMethod(phaseInstant, new MethodInvoker(method, beanManager));
                }
            }
            current = current.getSuperclass();
        }
    }

    public void executeBeforePhase(PhaseId phaseId) {
        List<ViewActionStrategy> actions = phaseMethods.get(new PhaseInstant(phaseId, true));
        if (actions != null) {
            for (ViewActionStrategy action : actions) {
                action.execute();
            }
        }
    }

    public void executeAfterPhase(PhaseId phaseId) {
        List<ViewActionStrategy> actions = phaseMethods.get(new PhaseInstant(phaseId, false));
        if (actions != null) {
            for (ViewActionStrategy action : actions) {
                action.execute();
            }
        }
    }

    public void addMethod(PhaseInstant phaseInstant, ViewActionStrategy method) {
        List<ViewActionStrategy> methods = phaseMethods.get(phaseInstant);
        if (methods == null) {
            methods = new ArrayList<ViewActionStrategy>();
            phaseMethods.put(phaseInstant, methods);
        }
        methods.add(method);
    }

    public Map<PhaseInstant, List<ViewActionStrategy>> getPhaseMethods() {
        return phaseMethods;
    }

    public void setPhaseMethods(Map<PhaseInstant, List<ViewActionStrategy>> phaseMethods) {
        this.phaseMethods = phaseMethods;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public Class<?> getViewControllerClass() {
        return viewControllerClass;
    }

    public void setViewControllerClass(Class<?> viewControllerClass) {
        this.viewControllerClass = viewControllerClass;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        builder.append("{viewId: ").append(getViewId()).append(", viewControllerClass: ").append(getViewControllerClass())
                .append("}, phaseMethods: {").append(getPhaseMethods()).append("}}");
        return builder.toString();
    }

    /**
     * Invokes method on a CDI bean.
     * 
     * Note : copy from Seam Security's SecurityExtension class. Should be extracted into common utility.
     */
    public static class AnnotatedMethodInvoker implements ViewActionStrategy {
        private Bean<?> targetBean;
        private BeanManager beanManager;
        private InjectableMethod<?> injectableMethod;
        private AnnotatedMethod<?> annotatedMethod;

        public AnnotatedMethodInvoker(AnnotatedMethod<?> annotatedMethod, BeanManager beanManager) {
            this.beanManager = beanManager;
            this.annotatedMethod = annotatedMethod;
        }

        public AnnotatedMethod<?> getAnnotatedMethod() {
            return annotatedMethod;
        }

        public void setAnnotatedMethod(AnnotatedMethod<?> annotatedMethod) {
            this.annotatedMethod = annotatedMethod;
        }

        public BeanManager getBeanManager() {
            return beanManager;
        }

        public Object execute() {
            if (targetBean == null) {
                lookupTargetBean();
            }
            CreationalContext<?> cc = beanManager.createCreationalContext(targetBean);
            Object reference = beanManager.getReference(targetBean, getAnnotatedMethod().getJavaMember().getDeclaringClass(),
                    cc);
            return injectableMethod.invoke(reference, cc, null);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private synchronized void lookupTargetBean() {
            if (targetBean == null) {
                AnnotatedMethod annotatedMethod = getAnnotatedMethod();
                Method method = annotatedMethod.getJavaMember();
                Set<Bean<?>> beans = beanManager.getBeans(method.getDeclaringClass());
                if (beans.size() == 1) {
                    targetBean = beans.iterator().next();
                } else if (beans.isEmpty()) {
                    throw new IllegalStateException("Exception looking up method bean - " + "no beans found for method ["
                            + method.getDeclaringClass() + "." + method.getName() + "]");
                } else if (beans.size() > 1) {
                    throw new IllegalStateException("Exception looking up method bean - " + "multiple beans found for method ["
                            + method.getDeclaringClass().getName() + "." + method.getName() + "]");
                }
                injectableMethod = new InjectableMethod(annotatedMethod, targetBean, beanManager);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
            builder.append("{method: ").append(getAnnotatedMethod()).append("}");
            return builder.toString();
        }
    }

    /**
     * Invokes method on a CDI bean.
     * 
     * Note : copy from Seam Security's SecurityExtension class. Should be extracted into common utility.
     */
    public static class MethodInvoker extends AnnotatedMethodInvoker {

        public MethodInvoker(Method method, BeanManager beanManager) {
            super(null, beanManager);
            setAnnotatedMethod(convert(method));

        }

        private AnnotatedMethod<?> convert(Method method) {
            AnnotatedType<?> annotatedType = getBeanManager().createAnnotatedType(method.getDeclaringClass());
            AnnotatedMethod<?> annotatedMethod = null;
            for (AnnotatedMethod<?> current : annotatedType.getMethods()) {
                if (current.getJavaMember().equals(method)) {
                    annotatedMethod = current;
                }
            }
            if (annotatedMethod == null) {
                throw new IllegalStateException("No matching annotated method found for method : " + method);
            }
            return annotatedMethod;
        }
    }

    /**
     * Invokes a method expression.
     */
    public static class MethodExpressionInvoker implements ViewActionStrategy {
        private MethodExpression methodExpression;
        private String methodExpressionAsString;

        public MethodExpressionInvoker(String methodExpressionAsString) {
            this.methodExpressionAsString = methodExpressionAsString;
        }

        public String getMethodExpressionString() {
            return methodExpressionAsString;
        }

        @Override
        public Object execute() {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (methodExpression == null) {
                methodExpression = facesContext.getApplication().getExpressionFactory()
                        .createMethodExpression(facesContext.getELContext(), methodExpressionAsString, null, new Class[] {});
            }
            return methodExpression.invoke(FacesContext.getCurrentInstance().getELContext(), null);
        }
    }
}
