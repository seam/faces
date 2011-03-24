package org.jboss.seam.faces.component;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.seam.faces.validation.InputField;
import org.jboss.seam.solder.reflection.annotated.AnnotatedTypeBuilder;

/**
 * Ensure that any field annotated with {@link InputField} is produced by the same producer method with output type
 * {@link Object}.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * 
 */
@ApplicationScoped
public class FormValidationTypeOverrideExtension implements Extension {
    private final Map<Class<?>, AnnotatedType<?>> typeOverrides = new HashMap<Class<?>, AnnotatedType<?>>();

    public <T> void processAnnotatedType(@Observes final ProcessAnnotatedType<T> event) {
        AnnotatedTypeBuilder<T> builder = new AnnotatedTypeBuilder<T>();
        builder.readFromType(event.getAnnotatedType());

        boolean modifiedType = false;

        for (AnnotatedField<?> f : event.getAnnotatedType().getFields()) {
            if (f.isAnnotationPresent(InputField.class)) {
                builder.overrideFieldType(f.getJavaMember(), Object.class);
                modifiedType = true;
            }
        }

        if (modifiedType) {
            AnnotatedType<T> replacement = builder.create();
            typeOverrides.put(replacement.getJavaClass(), replacement);
            event.setAnnotatedType(replacement);
        }
    }

    public boolean hasOverriddenType(final Class<?> clazz) {
        return typeOverrides.containsKey(clazz);
    }

    public AnnotatedType<?> getOverriddenType(final Class<?> clazz) {
        return typeOverrides.get(clazz);
    }
}
