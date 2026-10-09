package dev.morphia.mapping.experimental;

import com.mongodb.DBRef;
import com.mongodb.lang.Nullable;
import dev.morphia.Datastore;
import dev.morphia.annotations.internal.MorphiaInternal;
import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.MappingException;
import dev.morphia.mapping.codec.pojo.EntityModel;
import dev.morphia.mapping.codec.pojo.PropertyModel;
import dev.morphia.mapping.lazy.proxy.ReferenceException;
import dev.morphia.query.Query;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static dev.morphia.query.filters.Filters.eq;

/**
 * @param <T>
 * @morphia.internal
 * @hidden
 */
@MorphiaInternal
@SuppressWarnings("unchecked")
@Deprecated
public class SingleReference<T> extends MorphiaReference<T> {
    private EntityModel entityModel;
    private Object id;
    private T value;

    /**
     * @param datastore
     * @param entityModel the entity's mapped class
     * @param id          the ID value
     * @morphia.internal
     */
    @MorphiaInternal
    public SingleReference(Datastore datastore, EntityModel entityModel, Object id) {
        super(datastore);
        this.entityModel = entityModel;
        this.id = id;
        if (entityModel.getType().isInstance(id)) {
            value = (T) id;
            PropertyModel idProperty = entityModel.getIdProperty();
            if (idProperty != null) {
                this.id = idProperty.getValue(value);
                resolve();
            } else {
                throw new MappingException("No field is annotated with @Id on " + entityModel.getType().getName() + " but it is required");
            }
        }
    }

    SingleReference(Datastore datastore, T value) {
        super(datastore);
        this.value = value;
    }

    /**
     * Decodes a document in to an entity
     *
     * @param datastore   the datastore
     * @param mapper      the mapper
     * @param mappedField the MappedField
     * @param paramType   the type of the underlying entity
     * @param document    the Document to decode
     * @return the entity
     */
    public static MorphiaReference<?> decode(Datastore datastore,
                                             Mapper mapper,
                                             PropertyModel mappedField,
                                             Class<?> paramType, Document document) {
        final EntityModel entityModel = mapper.getEntityModel(paramType);
        Object id = document.get(mappedField.getMappedName());

        return new SingleReference<>(datastore, entityModel, id);
    }

    @Override
    public T get() {
        if (!isResolved() && value == null && id != null) {
            value = (T) buildQuery().iterator().tryNext();
            if (value == null && !ignoreMissing()) {
                throw new ReferenceException("Referenced '" + entityModel.getType().getSimpleName() + "' entity could not be found during a fetch.");
            }
            resolve();
        }
        return value;
    }

    @Override
    public List<Object> getIds() {
        ArrayList<Object> result = new ArrayList<>();
        result.add(getId());
        return result;
    }

    @Override
    public Class<T> getType() {
        return (Class<T>) entityModel.getType();
    }

    @Override
    Object getId(Mapper mapper, EntityModel fieldClass) {
        if (id == null) {
            EntityModel entityModel = getEntityModel(mapper);
            if (entityModel != null && entityModel.getIdProperty() != null) {
                id = entityModel.getIdProperty().getValue(get());
                if (!entityModel.equals(fieldClass)) {
                    id = new DBRef(entityModel.getCollectionName(), id);
                }
            }
        }
        if (id == null) {
            throw new ReferenceException("No ID found for referenced entity.  Ensure referenced entities are saved first.");
        }
        return id;
    }

    private Object getId() {
        return id instanceof DBRef ? ((DBRef) id).getId() : id;
    }

    Query<?> buildQuery() {
        final Query<?> query;
        if (id instanceof DBRef) {
            query = getDatastore().find(getDatastore()
                    .getMapper()
                    .getClassFromCollection(((DBRef) this.id).getCollectionName()));
        } else {
            query = getDatastore().find(entityModel.getType());
        }
        return query.filter(eq("_id", getId()));
    }

    @Nullable
    EntityModel getEntityModel(Mapper mapper) {
        if (entityModel == null) {
            T t = get();
            if (t != null) {
                entityModel = mapper.getEntityModel(t.getClass());
            }
        }

        return entityModel;
    }

}
