package org.jboss.seam.faces.context;

/**
 * A context that lives from Restore View to the next Render Response.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
public interface RenderContext {

    /**
     * Returns true if the current {@link RenderContext} contains no data.
     */
    boolean isEmpty();

    /**
     * Return the current ID of this request's {@link RenderContext}. If the ID has not yet been set as part of a redirect, the
     * ID will be null.
     */
    Integer getId();

    /**
     * Get a key value pair from the {@link RenderContext}.
     */
    Object get(String key);

    /**
     * Put a key value pair into the {@link RenderContext}.
     */
    void put(String key, Object value);

}
