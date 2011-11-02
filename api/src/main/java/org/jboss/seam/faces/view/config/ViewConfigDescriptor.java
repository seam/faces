package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
        //add to metaDataByAnnotation
        metaDataByAnnotation.put(metaData.annotationType(), metaData);
        log.debugf("Putting new annotation (type: %s) for viewId: %s", metaData.annotationType().getName(), getViewId());
        //add to metaDataByQualifier
        Annotation[] annotations = metaData.annotationType().getAnnotations();
        for (Annotation qualifier : annotations) {
            if (qualifier.annotationType().getName().startsWith("java.")) {
                log.debugf("Disregarding java.* package %s", qualifier.annotationType().getName());
                continue;
            }
            List<Annotation> qualifiedAnnotations = new ArrayList<Annotation>();
            List<? extends Annotation> exisitngQualifiedAnnotations = metaDataByQualifier.get(qualifier
                  .annotationType());
            if (exisitngQualifiedAnnotations != null && !exisitngQualifiedAnnotations.isEmpty()) {
                qualifiedAnnotations.addAll(exisitngQualifiedAnnotations);
            }
            qualifiedAnnotations.add(metaData);
            log.debugf("Adding new annotation (type: %s) for Qualifier %s", metaData.annotationType().getName(), qualifier.annotationType().getName());
            metaDataByQualifier.put(qualifier.annotationType(), qualifiedAnnotations);
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
        return metaData!=null ? Collections.unmodifiableList(metaData) : Collections.EMPTY_LIST; 
    }
}
