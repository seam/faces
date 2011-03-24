package org.jboss.seam.faces.view.config;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.jboss.logging.Logger;

/**
 * Data store for view specific data.
 * 
 * @author Stuart Douglas
 * @author <a href="mailto:bleathem@gmail.com">Brian Leathem</a>
 * 
 */
@ApplicationScoped
public class ViewConfigStoreImpl implements ViewConfigStore {
    private transient final Logger log = Logger.getLogger(SecurityPhaseListener.class);
    /**
     * cache of viewId to a given data list
     */
    private final ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>> annotationCache = new ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>>();
    private final ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>> qualifierCache = new ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>>();

    private final ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, Annotation>> viewPatternDataByAnnotation = new ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, Annotation>>();
    private final ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>> viewPatternDataByQualifier = new ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>>();

    /**
     * setup the bean with the configuration from the extension
     * 
     * It would be better if the extension could do this, but the extension cannot resolve the bean until after all lifecycle
     * events have been processed
     * 
     */
    @Inject
    public void setup(ViewConfigExtension extension) {
        for (Entry<String, Set<Annotation>> e : extension.getData().entrySet()) {
            for (Annotation i : e.getValue()) {
                addAnnotationData(e.getKey(), i);
            }
        }
    }

    @Override
    public synchronized void addAnnotationData(String viewId, Annotation annotation) {
        ConcurrentHashMap<String, Annotation> annotationMap = viewPatternDataByAnnotation.get(annotation.annotationType());
        if (annotationMap == null) {
            annotationMap = new ConcurrentHashMap<String, Annotation>();
            viewPatternDataByAnnotation.put(annotation.annotationType(), annotationMap);
            log.debugf("Putting new annotation map for anotation type %s", annotation.annotationType().getName());
        }
        annotationMap.put(viewId, annotation);
        log.debugf("Putting new annotation (type: %s) for viewId: %s", annotation.annotationType().getName(), viewId);

        Annotation[] annotations = annotation.annotationType().getAnnotations();
        for (Annotation qualifier : annotations) {
            if (qualifier.annotationType().getName().startsWith("java.")) {
                log.debugf("Disregarding java.* package %s", qualifier.annotationType().getName());
                continue;
            }
            ConcurrentHashMap<String, List<? extends Annotation>> qualifierMap = viewPatternDataByQualifier.get(qualifier
                    .annotationType());
            if (qualifierMap == null) {
                qualifierMap = new ConcurrentHashMap<String, List<? extends Annotation>>();
                viewPatternDataByQualifier.put(qualifier.annotationType(), qualifierMap);
                log.debugf("Putting new qualifier map for qualifier type %s", qualifier.annotationType().getName());
            }
            List<Annotation> qualifiedAnnotations = new ArrayList<Annotation>();
            List<? extends Annotation> exisitngQualifiedAnnotations = qualifierMap.get(viewId);
            if (exisitngQualifiedAnnotations != null && !exisitngQualifiedAnnotations.isEmpty()) {
                qualifiedAnnotations.addAll(exisitngQualifiedAnnotations);
            }
            qualifiedAnnotations.add(annotation);
            qualifierMap.put(viewId, qualifiedAnnotations);
        }
    }

    @Override
    public <T extends Annotation> T getAnnotationData(String viewId, Class<T> type) {
        List<T> data = prepareAnnotationCache(viewId, type, annotationCache, viewPatternDataByAnnotation);
        if ((data != null) && (data.size() > 0)) {
            return data.get(0);
        }
        return null;
    }

    @Override
    public <T extends Annotation> List<T> getAllAnnotationData(String viewId, Class<T> type) {
        List<T> data = prepareAnnotationCache(viewId, type, annotationCache, viewPatternDataByAnnotation);
        if (data != null) {
            return Collections.unmodifiableList(data);
        }
        return null;
    }

    @Override
    public List<? extends Annotation> getAllQualifierData(String viewId, Class<? extends Annotation> qualifier) {
        List<? extends Annotation> data = prepareQualifierCache(viewId, qualifier, qualifierCache, viewPatternDataByQualifier);
        if (data != null) {
            return Collections.unmodifiableList(data);
        }
        return null;
    }

    private <T extends Annotation> List<T> prepareAnnotationCache(String viewId, Class<T> annotationType,
            ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>> cache,
            ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, Annotation>> viewPatternData) {
        // we need to synchronize to make sure that no threads see a half
        // completed list due to instruction re-ordering
        ConcurrentHashMap<String, List<? extends Annotation>> map = cache.get(annotationType);
        if (map == null) {
            ConcurrentHashMap<String, List<? extends Annotation>> newMap = new ConcurrentHashMap<String, List<? extends Annotation>>();
            map = cache.putIfAbsent(annotationType, newMap);
            if (map == null) {
                map = newMap;
            }
        }
        List<? extends Annotation> annotationData = map.get(viewId);
        if (annotationData == null) {
            List<Annotation> newList = new ArrayList<Annotation>();
            Map<String, Annotation> viewPatterns = viewPatternData.get(annotationType);
            if (viewPatterns != null) {
                List<String> resultingViews = findViewsWithPatternsThatMatch(viewId, viewPatterns.keySet());
                for (String i : resultingViews) {
                    newList.add(viewPatterns.get(i));
                }
            }

            annotationData = map.putIfAbsent(viewId, newList);
            if (annotationData == null) {
                annotationData = newList;
            }
        }
        return (List) annotationData;
    }

    private <T extends Annotation> List<T> prepareQualifierCache(
            String viewId,
            Class<T> annotationType,
            ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>> cache,
            ConcurrentHashMap<Class<? extends Annotation>, ConcurrentHashMap<String, List<? extends Annotation>>> viewPatternData) {
        // we need to synchronize to make sure that no threads see a half
        // completed list due to instruction re-ordering
        ConcurrentHashMap<String, List<? extends Annotation>> map = cache.get(annotationType);
        if (map == null) {
            ConcurrentHashMap<String, List<? extends Annotation>> newMap = new ConcurrentHashMap<String, List<? extends Annotation>>();
            map = cache.putIfAbsent(annotationType, newMap);
            if (map == null) {
                map = newMap;
            }
        }
        List<? extends Annotation> annotationData = map.get(viewId);
        if (annotationData == null) {
            List<Annotation> newList = new ArrayList<Annotation>();
            Map<String, List<? extends Annotation>> viewPatterns = viewPatternData.get(annotationType);
            if (viewPatterns != null) {
                List<String> resultingViews = findViewsWithPatternsThatMatch(viewId, viewPatterns.keySet());
                for (String i : resultingViews) {
                    newList.addAll(viewPatterns.get(i));
                }
            }
            Collections.sort(newList, new AnnotationNameComparator());
            annotationData = map.putIfAbsent(viewId, newList);
            if (annotationData == null) {
                annotationData = newList;
            }
        }
        return (List) annotationData;
    }

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

    private static class AnnotationNameComparator implements Comparator<Annotation> {

        @Override
        public int compare(Annotation o1, Annotation o2) {
            return o1.annotationType().getName().compareTo(o2.annotationType().getName());
        }

        public static final StringLengthComparator INSTANCE = new StringLengthComparator();

    }
}
