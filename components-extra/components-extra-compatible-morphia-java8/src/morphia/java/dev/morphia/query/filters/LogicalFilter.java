package dev.morphia.query.filters;

import dev.morphia.Datastore;
import dev.morphia.utils.CollectionUtil;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.List;

import static dev.morphia.aggregation.codecs.ExpressionHelper.document;
import static java.lang.String.format;

public class LogicalFilter extends Filter {
    private final List<Filter> filters;

    LogicalFilter(String name, Filter... filters) {
        super(name);
        this.filters = CollectionUtil.asList(filters);
    }

    /**
     * Adds a new filter to this LogicalFilter.
     *
     * @param filter the new filter
     * @return this
     * @since 2.3
     */
    public LogicalFilter add(Filter filter) {
        filters.add(filter);
        return this;
    }

    @Override
    public void encode(Datastore datastore, BsonWriter writer, EncoderContext context) {
        writer.writeStartArray(getName());
        for (Filter filter : filters) {
            document(writer, () -> filter.encode(datastore, writer, context));
        }
        writer.writeEndArray();
    }

    @Override
    public Filter entityType(Class<?> type) {
        super.entityType(type);
        for (Filter filter : filters) {
            filter.entityType(type);
        }
        return this;
    }

    @Override
    public Filter isValidating(boolean validate) {
        super.isValidating(validate);
        for (Filter filter : filters) {
            filter.isValidating(validate);
        }
        return this;
    }

    @Override
    public String toString() {
        return format("%s: %s", getName(), filters);
    }
}
