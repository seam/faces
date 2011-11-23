package org.jboss.seam.faces.view.action;

import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.solder.reflection.annotated.InjectableMethod;

/**
 * Invokes method on a CDI bean.
 * 
 * Note : copy from Seam Security's SecurityExtension class. Should be extracted into common utility.
 */
public abstract class AnnotatedMethodViewActionHandler implements ViewActionHandler {
    private Bean<?> targetBean;
    private BeanManager beanManager;
    private InjectableMethod<?> injectableMethod;
    private AnnotatedMethod<?> annotatedMethod;

    public AnnotatedMethodViewActionHandler(AnnotatedMethod<?> annotatedMethod, BeanManager beanManager) {
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
