package org.jboss.seam.faces.context;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Typed;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
@Typed()
public class RenderContextImpl implements RenderContext, Serializable {
    private static final long serialVersionUID = 7502050909452181348L;

    private Integer id = null;
    private final Map<String, Object> map = new ConcurrentHashMap<String, Object>();

    public Object get(final String key) {
        return map.get(key);
    }

    public Integer getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void put(final String key, final Object value) {
        map.put(key, value);
    }

}
