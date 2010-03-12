package org.jboss.seam.faces.context;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.faces.bean.FlashScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * This class provides the contexts lifecycle for the new JSF2 Flash Context
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FlashScopedContext implements Context, PhaseListener
{
    private static final long serialVersionUID = -1580689204988513798L;

    private final static String COMPONENT_MAP_NAME = "org.jboss.seam.faces.flash.componentInstanceMap";
    private final static String CREATIONAL_MAP_NAME = "org.jboss.seam.faces.flash.creationalInstanceMap";
    private final ThreadLocal<Map<Contextual<?>, Object>> lastComponentInstanceMap = new ThreadLocal<Map<Contextual<?>, Object>>();
    private final ThreadLocal<Map<Contextual<?>, CreationalContext<?>>> lastCreationalContextMap = new ThreadLocal<Map<Contextual<?>, CreationalContext<?>>>();

    @SuppressWarnings("unchecked")
    public <T> T get(final Contextual<T> component)
    {
        assertActive();
        return (T) getComponentInstanceMap().get(component);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final Contextual<T> component, final CreationalContext<T> creationalContext)
    {
        assertActive();

        T instance = get(component);

        if (instance == null)
        {
            Map<Contextual<?>, CreationalContext<?>> creationalContextMap = getCreationalContextMap();
            Map<Contextual<?>, Object> componentInstanceMap = getComponentInstanceMap();

            synchronized (componentInstanceMap)
            {
                instance = (T) componentInstanceMap.get(component);
                if (instance == null)
                {
                    instance = component.create(creationalContext);

                    if (instance != null)
                    {
                        componentInstanceMap.put(component, instance);
                        creationalContextMap.put(component, creationalContext);
                    }
                }
            }
        }

        return instance;
    }

    public Class<? extends Annotation> getScope()
    {
        return FlashScoped.class;
    }

    public boolean isActive()
    {
        return getFlash() != null;
    }

    /**
     * This method should, **in theory**, catch the current instanceMap (which
     * is the previous lifecycle's next instanceMap.) These are the objects that
     * we want cleaned up at the end of the current render-response phase, so we
     * save them here until after the RENDER_RESPONSE phase, because otherwise
     * they would have been destroyed by the Flash, and we would no longer have
     * access to them.
     */
    public void beforePhase(final PhaseEvent event)
    {
        this.lastComponentInstanceMap.set(getComponentInstanceMap());
        this.lastCreationalContextMap.set(getCreationalContextMap());
    }

    /**
     * Do the object cleanup using our saved references.
     */
    @SuppressWarnings("unchecked")
    public void afterPhase(final PhaseEvent event)
    {
        // TODO verify that this is actually destroying the beans we want to be
        // destroyed... flash is confusing, tests will make sense of it
        Map<Contextual<?>, Object> componentInstanceMap = lastComponentInstanceMap.get();
        Map<Contextual<?>, CreationalContext<?>> creationalContextMap = lastCreationalContextMap.get();

        if (componentInstanceMap != null)
        {
            for (Entry<Contextual<?>, Object> componentEntry : componentInstanceMap.entrySet())
            {
                Contextual contextual = componentEntry.getKey();
                Object instance = componentEntry.getValue();
                CreationalContext creational = creationalContextMap.get(contextual);

                contextual.destroy(instance, creational);
            }
        }

        this.lastComponentInstanceMap.remove();
        this.lastCreationalContextMap.remove();
    }

    public PhaseId getPhaseId()
    {
        return PhaseId.RENDER_RESPONSE;
    }

    private Flash getFlash()
    {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (currentInstance != null)
        {
            ExternalContext externalContext = currentInstance.getExternalContext();
            return externalContext.getFlash();
        }
        return null;
    }

    private void assertActive()
    {
        if (!isActive())
        {
            throw new ContextNotActiveException(
                    "Seam context with scope annotation @FlashScoped is not active with respect to the current thread");
        }
    }

    @SuppressWarnings("unchecked")
    private Map<Contextual<?>, Object> getComponentInstanceMap()
    {
        Flash flash = getFlash();
        ConcurrentHashMap<Contextual<?>, Object> map = (ConcurrentHashMap<Contextual<?>, Object>) flash
                .get(COMPONENT_MAP_NAME);

        if (map == null)
        {
            map = new ConcurrentHashMap<Contextual<?>, Object>();
            flash.put(COMPONENT_MAP_NAME, map);
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    private Map<Contextual<?>, CreationalContext<?>> getCreationalContextMap()
    {
        Flash flash = getFlash();
        Map<Contextual<?>, CreationalContext<?>> map = (ConcurrentHashMap<Contextual<?>, CreationalContext<?>>) flash
                .get(CREATIONAL_MAP_NAME);

        if (map == null)
        {
            map = new ConcurrentHashMap<Contextual<?>, CreationalContext<?>>();
            flash.put(CREATIONAL_MAP_NAME, map);
        }

        return map;
    }

}
