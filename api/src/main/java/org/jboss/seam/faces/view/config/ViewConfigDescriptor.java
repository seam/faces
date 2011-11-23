package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.event.PhaseId;

import org.jboss.seam.faces.view.action.OrderDefault;
import org.jboss.seam.faces.view.action.PhaseInstant;
import org.jboss.seam.faces.view.action.ViewActionHandler;
import org.jboss.solder.logging.Logger;

/**
 * Information about {@link ViewConfig} enum.
 * 
 * @author Adriàn Gonzalez
 */
public class ViewConfigDescriptor {
    private transient final Logger log = Logger.getLogger(ViewConfigDescriptor.class);

    private String viewId;
    private List<Object> values = new ArrayList<Object>();
    private List<Annotation> metaData = new ArrayList<Annotation>();
    private final ConcurrentHashMap<Class<? extends Annotation>, Annotation> metaDataByAnnotation = new ConcurrentHashMap<Class<? extends Annotation>, Annotation>();
    private ConcurrentHashMap<Class<? extends Annotation>, List<? extends Annotation>> metaDataByQualifier = new ConcurrentHashMap<Class<? extends Annotation>, List<? extends Annotation>>();
    private List<ViewActionHandler> viewActionHandlers = new ArrayList<ViewActionHandler>();
    private Map<PhaseInstant, List<ViewActionHandler>> viewActionHandlersPerPhaseInstant = new HashMap<PhaseInstant, List<ViewActionHandler>>();

    /**
     * ViewConfigDescriptor for view <code>viewId</code>
     */
    public ViewConfigDescriptor(String viewId, Object value) {
        this.viewId = viewId;
        this.values = new ArrayList<Object>();
        values.add(value);
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public void addValue(Object value) {
        if (!values.contains(value)) {
            values.add(value);
        }
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public void addMetaData(Annotation metaData) {
        this.metaData.add(metaData);
        // add to metaDataByAnnotation
        metaDataByAnnotation.put(metaData.annotationType(), metaData);
        log.debugf("Putting new annotation (type: %s) for viewId: %s", metaData.annotationType().getName(), getViewId());
        // add to metaDataByQualifier
        Annotation[] annotations = metaData.annotationType().getAnnotations();
        for (Annotation qualifier : annotations) {
            if (qualifier.annotationType().getName().startsWith("java.")) {
                log.debugf("Disregarding java.* package %s", qualifier.annotationType().getName());
                continue;
            }
            List<Annotation> qualifiedAnnotations = new ArrayList<Annotation>();
            List<? extends Annotation> exisitngQualifiedAnnotations = metaDataByQualifier.get(qualifier.annotationType());
            if (exisitngQualifiedAnnotations != null && !exisitngQualifiedAnnotations.isEmpty()) {
                qualifiedAnnotations.addAll(exisitngQualifiedAnnotations);
            }
            qualifiedAnnotations.add(metaData);
            log.debugf("Adding new annotation (type: %s) for Qualifier %s", metaData.annotationType().getName(), qualifier
                    .annotationType().getName());
            metaDataByQualifier.put(qualifier.annotationType(), qualifiedAnnotations);
        }
    }

    public List<ViewActionHandler> getViewActionHandlers() {
        return viewActionHandlers;
    }

    public void addViewActionHandler(ViewActionHandler viewActionHandler) {
        this.viewActionHandlers.add(viewActionHandler);
        Collections.sort(this.viewActionHandlers, ViewActionHandlerComparator.INSTANCE);
        for (PhaseId phaseId : PhaseId.VALUES) {
            addViewActionHandlerPerPhaseInstant(new PhaseInstant(phaseId, true), viewActionHandler);
            addViewActionHandlerPerPhaseInstant(new PhaseInstant(phaseId, false), viewActionHandler);
        }
    }

    private void addViewActionHandlerPerPhaseInstant(PhaseInstant phaseInstant, ViewActionHandler viewActionHandler) {
        if (viewActionHandler.handles(phaseInstant)) {
            List<ViewActionHandler> viewActionHandlers = viewActionHandlersPerPhaseInstant.get(phaseInstant);
            if (viewActionHandlers == null) {
                viewActionHandlers = new ArrayList<ViewActionHandler>();
                viewActionHandlersPerPhaseInstant.put(phaseInstant, viewActionHandlers);
            }
            viewActionHandlers.add(viewActionHandler);
        }
    }

    public void executeBeforePhase(PhaseId phaseId) {
        List<ViewActionHandler> viewActionHandlers = viewActionHandlersPerPhaseInstant.get(new PhaseInstant(phaseId, true));
        if (viewActionHandlers != null) {
            for (int i = viewActionHandlers.size(); --i >= 0;) {
                ViewActionHandler viewActionHandler = viewActionHandlers.get(i);
                viewActionHandler.execute();
            }
        }
    }

    public void executeAfterPhase(PhaseId phaseId) {
        List<ViewActionHandler> viewActionHandlers = viewActionHandlersPerPhaseInstant.get(new PhaseInstant(phaseId, false));
        if (viewActionHandlers != null) {
            for (ViewActionHandler viewActionHandler : viewActionHandlers) {
                viewActionHandler.execute();
            }
        }
    }

    /**
     * Returns read-only list.
     * 
     * Use {@link #addMetaData(Annotation)} to modify metaDatas.
     */
    public List<Annotation> getMetaData() {
        return Collections.unmodifiableList(metaData);
    }

    /**
     * returns all metaData of the corresponding type.
     */
    public <T extends Annotation> T getMetaData(Class<T> type) {
        return (T) metaDataByAnnotation.get(type);
    }

    /**
     * returns all qualified data from metadata annotations.
     * 
     * returns empty list if there's no metaData for the qualifier.
     */
    @SuppressWarnings("unchecked")
    public List<? extends Annotation> getAllQualifierData(Class<? extends Annotation> qualifier) {
        List<? extends Annotation> metaData = metaDataByQualifier.get(qualifier);
        return metaData != null ? Collections.unmodifiableList(metaData) : Collections.EMPTY_LIST;
    }

    public String toString() {
        return super.toString() + "{viewId=" + getViewId() + "}";
    }

    private static class ViewActionHandlerComparator implements Comparator<ViewActionHandler> {

        @Override
        public int compare(ViewActionHandler o1, ViewActionHandler o2) {
            Integer o1Order = o1.getOrder();
            Integer o2Order = o2.getOrder();
            if (o1Order == null) {
                o1Order = OrderDefault.DEFAULT;
            }
            if (o2Order == null) {
                o2Order = OrderDefault.DEFAULT;
            }
            if (o1Order == o2Order) {
                return 0;
            }
            if (o1Order > o2Order) {
                return 1;
            } else {
                return -1;
            }
        }

        public static final ViewActionHandlerComparator INSTANCE = new ViewActionHandlerComparator();

    }
}
