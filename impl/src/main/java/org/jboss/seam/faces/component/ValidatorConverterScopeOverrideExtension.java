package org.jboss.seam.faces.component;

import javax.enterprise.context.NormalScope;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessManagedBean;
import javax.enterprise.inject.spi.ProcessProducerMethod;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;

import org.jboss.seam.solder.literal.RequestScopedLiteral;
import org.jboss.seam.solder.logging.internal.Logger;
import org.jboss.seam.solder.reflection.annotated.AnnotatedTypeBuilder;

/**
 * Performs validation of validators and converters to ensure they're normal scoped (i.e. not @Dependent scoped)
 * 
 * @author Shane Bryzak
 *
 */
public class ValidatorConverterScopeOverrideExtension implements Extension {
    
    private Logger log = Logger.getLogger(ValidatorConverterScopeOverrideExtension.class);
    
    public <T> void processManagedBean(@Observes final ProcessManagedBean<T> event) {
        if (isMatch(event.getBean())) {
            log.warn("The " + (event.getBean().getTypes().contains(Validator.class) ? "validator" : "converter") + 
                    " bean [" + event.getBean().getBeanClass().getName() + 
                    "] does not have a normal scope - overriding it with @RequestScoped");            
            
            AnnotatedTypeBuilder<T> builder = new AnnotatedTypeBuilder<T>().readFromType(event.getAnnotatedBeanClass());
            
            builder.removeFromClass(event.getBean().getScope());
            builder.addToClass(new RequestScopedLiteral());
        }
    }
    
    private <T> boolean isMatch(Bean<T> bean) {
        return ((bean.getTypes().contains(Validator.class) || bean.getTypes().contains(Converter.class)) && 
            !bean.getScope().isAnnotationPresent(NormalScope.class));          
    }
}
