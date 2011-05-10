package org.jboss.seam.faces;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class SeamFacesException extends RuntimeException {
    private static final long serialVersionUID = -610838646516706170L;

    public SeamFacesException() {
    }

    public SeamFacesException(final String message) {
        super(message);
    }

    public SeamFacesException(final String message, final Exception e) {
        super(message, e);
    }

}
