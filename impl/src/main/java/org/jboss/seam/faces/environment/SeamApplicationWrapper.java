package org.jboss.seam.faces.environment;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ApplicationWrapper;
import javax.faces.convert.Converter;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.validator.Validator;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.jboss.seam.faces.util.BeanManagerUtils;

/**
 * Provides contextual lifecycle and @{link Inject} support for JSF artifacts such as {@link Converter}, {@link Validator}.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 */
@ApplicationScoped
public class SeamApplicationWrapper extends ApplicationWrapper {
    private transient final Logger log = Logger.getLogger(SeamApplicationWrapper.class);

    private Application parent;

    @Inject
    BeanManagerUtils managerUtils;

    @Override
    public Application getWrapped() {
        return parent;
    }

    public void installWrapper(@Observes final PostConstructApplicationEvent event) {
        log.debug("PostConstructApplicationEvent observed, installing wrapper");
        ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        parent = event.getApplication();
        factory.setApplication(this);
    }

    @Override
    public Converter createConverter(final Class<?> targetClass) {
        log.debugf("Creating converter for targetClass %s", targetClass.getName());
        Converter result = parent.createConverter(targetClass);
        result = attemptExtension(result);
        return result;
    }

    @Override
    public Converter createConverter(final String converterId) {
        log.debugf("Creating converter for converterId %s", converterId);
        Converter result = parent.createConverter(converterId);
        result = attemptExtension(result);
        return result;
    }

    @Override
    public Validator createValidator(final String validatorId) {
        log.debugf("Creating validator for validatorId %s", validatorId);
        Validator result = parent.createValidator(validatorId);
        result = attemptExtension(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> T attemptExtension(final T base) {
        T result = base;

        if (base == null) {
            log.warnf("Cannot attempt extension on null");
            return null;
        }

        log.debugf("Extending class: %s", base.getClass().getName());
        if (managerUtils.isDependentScoped(base.getClass())) {
            managerUtils.injectNonContextualInstance(result);
        } else {
            result = (T) managerUtils.getContextualInstance(base.getClass());
        }

        if (result == null) {
            if (!base.getClass().getName().startsWith("javax.")) {
                log.warnf("Using JSF provided instance, unable to find a BeanManaged instance for class %s", base.getClass()
                        .getName());
            }
            result = base;
        }

        return result;
    }
}
