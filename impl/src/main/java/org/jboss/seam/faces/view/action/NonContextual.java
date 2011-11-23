package org.jboss.seam.faces.view.action;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;

/**
 * This class handles non-contextual injection for a thirdparty framework.
 * 
 * <p>For each non-contextual type you want to provide CDI services (like injection, &#064;PostConstruct, &#064;PreDestroy) to, you need to
 * create an instance of this class. You'll need to pass in the BeanManager; assuming you are in a EE environment, the easiest
 * way to do this is to look it up in JNDI - java:comp/BeanManager, otherwise you'll have to resort to some non-portable method.
 * You then need to ensure to call nonContextual.newInstance() to create an instance. You can then use (the order here is
 * recommended) produce(), inject(), postConstruct(), preDestroy(), dispose() to create and cleanup the instance. Call get() to
 * get the instance.</p> 
 * 
 * See also http://seamframework.org/Documentation/HowDoIDoNoncontextualInjectionForAThirdpartyFramework.
 * 
 * @author Adriàn Gonzalez
 */
public class NonContextual<T> {

    // Store the injection target. The CDI spec doesn't require an implementation to cache it, so we do
    private final InjectionTarget<T> injectionTarget;

    // Store a reference to the CDI BeanManager
    private final BeanManager beanManager;

    /**
     * Create an injector for the given class
     */
    public NonContextual(BeanManager manager, Class<T> clazz) {
        this.beanManager = manager;

        // Generate an "Annotated Type"
        AnnotatedType<T> type = manager.createAnnotatedType(clazz);

        // Generate the InjectionTarget
        this.injectionTarget = manager.createInjectionTarget(type);
    }

    public Instance<T> newInstance() {
        return new Instance<T>(beanManager, injectionTarget);
    }

    /**
     * Represents a non-contextual instance
     */
    public static class Instance<T> {

        private final CreationalContext<T> ctx;
        private final InjectionTarget<T> injectionTarget;
        private T instance;
        private boolean disposed = false;

        private Instance(BeanManager beanManager, InjectionTarget<T> injectionTarget) {
            this.injectionTarget = injectionTarget;
            this.ctx = beanManager.createCreationalContext(null);
        }

        /**
         * Get the instance
         */
        public T get() {
            return instance;
        }

        /**
         * Create the instance
         */
        public Instance<T> produce() {
            if (this.instance != null) {
                throw new IllegalStateException("Trying to call produce() on already constructed instance");
            }
            if (disposed) {
                throw new IllegalStateException("Trying to call produce() on an already disposed instance");
            }
            this.instance = injectionTarget.produce(ctx);
            return this;
        }

        /**
         * Inject the instance
         */
        public Instance<T> inject() {
            if (this.instance == null) {
                throw new IllegalStateException("Trying to call inject() before produce() was called");
            }
            if (disposed) {
                throw new IllegalStateException("Trying to call inject() on already disposed instance");
            }
            injectionTarget.inject(instance, ctx);
            return this;
        }

        /**
         * Call the @PostConstruct callback
         */
        public Instance<T> postConstruct() {
            if (this.instance == null) {
                throw new IllegalStateException("Trying to call postConstruct() before produce() was called");
            }
            if (disposed) {
                throw new IllegalStateException("Trying to call preDestroy() on already disposed instance");
            }
            injectionTarget.postConstruct(instance);
            return this;
        }

        /**
         * Call the @PreDestroy callback
         */
        public Instance<T> preDestroy() {
            if (this.instance == null) {
                throw new IllegalStateException("Trying to call preDestroy() before produce() was called");
            }
            if (disposed) {
                throw new IllegalStateException("Trying to call preDestroy() on already disposed instance");
            }
            injectionTarget.preDestroy(instance);
            return this;
        }

        /**
         * Dispose of the instance, doing any necessary cleanup
         */
        public Instance<T> dispose() {
            if (this.instance == null) {
                throw new IllegalStateException("Trying to call dispose() before produce() was called");
            }
            if (disposed) {
                throw new IllegalStateException("Trying to call dispose() on already disposed instance");
            }
            injectionTarget.dispose(instance);
            ctx.release();
            return this;
        }

    }

}
