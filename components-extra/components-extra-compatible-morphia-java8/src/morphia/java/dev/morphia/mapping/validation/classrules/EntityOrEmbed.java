package dev.morphia.mapping.validation.classrules;

import dev.morphia.annotations.ExternalEntity;
import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.codec.pojo.EntityModel;
import dev.morphia.mapping.validation.ClassConstraint;
import dev.morphia.mapping.validation.ConstraintViolation;
import dev.morphia.mapping.validation.ConstraintViolation.Level;

import java.util.Set;


/**
 * Checks that @Entity or @Embed are used on a type.
 */
public class EntityOrEmbed implements ClassConstraint {

    @Override
    public void check(Mapper mapper, EntityModel entityModel, Set<ConstraintViolation> ve) {
        if (entityModel.getEntityAnnotation() == null
                && entityModel.getAnnotation(ExternalEntity.class) == null
                && entityModel.getEmbeddedAnnotation() == null) {
            ve.add(new ConstraintViolation(Level.FATAL, entityModel, getClass(), entityModel.getType().getName() + " must be annotated with either @Entity, @ExternalEntity, or @Embedded."));
        }
    }
}
