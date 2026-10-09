package dev.morphia.aggregation.expressions.impls;

import dev.morphia.Datastore;
import dev.morphia.utils.CollectionUtil;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static dev.morphia.aggregation.codecs.ExpressionHelper.array;
import static dev.morphia.aggregation.codecs.ExpressionHelper.value;
import static java.util.Collections.emptyList;

public class ArrayLiteral extends ArrayExpression {

    private Object[] objects;

    public ArrayLiteral(Expression... values) {
        super("unused", CollectionUtil.asList(values));
    }

    public ArrayLiteral(Object... objects) {
        super("unused", emptyList());
        this.objects = objects;
    }

    @Override
    public void encode(Datastore datastore, BsonWriter writer, EncoderContext encoderContext) {
        if (objects == null) {
            Expression value = getValue();
            if (value != null) {
                value.encode(datastore, writer, encoderContext);
            }
        } else {
            array(writer, () -> {
                for (Object object : objects) {
                    value(datastore, writer, object, encoderContext);
                }
            });
        }
    }
}
