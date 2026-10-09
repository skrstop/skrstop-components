package dev.morphia.mapping.conventions;

import com.mongodb.lang.NonNull;
import dev.morphia.annotations.internal.MorphiaInternal;
import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.MappingException;
import dev.morphia.mapping.codec.ArrayFieldAccessor;
import dev.morphia.mapping.codec.Conversions;
import dev.morphia.mapping.codec.FieldAccessor;
import dev.morphia.mapping.codec.pojo.EntityModelBuilder;
import dev.morphia.mapping.codec.pojo.TypeData;
import dev.morphia.utils.CollectionUtil;
import org.bson.codecs.pojo.PropertyAccessor;

import java.lang.reflect.Field;
import java.util.Set;

@MorphiaInternal
public class FieldDiscovery implements MorphiaConvention {

    @Override
    public void apply(Mapper mapper, EntityModelBuilder builder) {
        if (builder.propertyModels().isEmpty()) {
            Set<Class<?>> list = builder.classHierarchy();
            list.add(builder.type());

            for (Class<?> type : list) {
                for (Field field : type.getDeclaredFields()) {

                    TypeData<?> typeData = builder.getTypeData(type, TypeData.get(field), field.getGenericType());
                    try {
                        builder.addProperty()
                                .name(field.getName())
                                .typeData(typeData)
                                .annotations(CollectionUtil.asList(field.getDeclaredAnnotations()))
                                .accessor(getAccessor(getTargetField(builder, field), typeData, mapper.getConversions()))
                                .modifiers(field.getModifiers())
                                .discoverMappedName();
                    } catch (NoSuchFieldException e) {
                        throw new MappingException("Mapped field '" + field.getName() + "' on '" + builder.type().getName() + "' does not match any fields on '" + builder.targetType().getName() + "'.");
                    }
                }
            }
        }
    }

    @NonNull
    private Field getTargetField(EntityModelBuilder builder, @NonNull Field field) throws NoSuchFieldException {
        if (builder.type().equals(builder.targetType())) {
            return field;
        }
        return builder.targetType().getDeclaredField(field.getName());
    }

    private PropertyAccessor<? super Object> getAccessor(Field field, TypeData<?> typeData, Conversions conversions) {
        return field.getType().isArray() && !field.getType().getComponentType().equals(byte.class)
                ? new ArrayFieldAccessor(typeData, field, conversions)
                : new FieldAccessor(field);
    }
}
