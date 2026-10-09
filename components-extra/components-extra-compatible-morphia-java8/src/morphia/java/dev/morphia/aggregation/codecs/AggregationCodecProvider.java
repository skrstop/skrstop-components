package dev.morphia.aggregation.codecs;

import com.mongodb.lang.Nullable;
import dev.morphia.Datastore;
import dev.morphia.aggregation.codecs.stages.*;
import dev.morphia.aggregation.expressions.impls.Expression;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AggregationCodecProvider implements CodecProvider {

    private final Codec expressionCodec;
    private Map<Class, StageCodec> codecs;
    private Datastore datastore;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public AggregationCodecProvider(Datastore datastore) {
        this.datastore = datastore;
        expressionCodec = new ExpressionCodec(datastore);
    }

    @Override
    @Nullable
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        Codec<T> codec = getCodecs().get(clazz);
        if (codec == null) {
            if (Expression.class.isAssignableFrom(clazz)) {
                codec = expressionCodec;
            }
        }
        return codec;
    }

    private Map<Class, StageCodec> getCodecs() {
        if (codecs == null) {
            codecs = new HashMap<>();

            // Stages
            addCodec(new AddFieldsCodec(datastore),
                    new AutoBucketCodec(datastore),
                    new BucketCodec(datastore),
                    new ChangeStreamCodec(datastore),
                    new CollectionStatsCodec(datastore),
                    new CountCodec(datastore),
                    new CurrentOpCodec(datastore),
                    new DensifyCodec(datastore),
                    new DocumentsCodec(datastore),
                    new FacetCodec(datastore),
                    new FillCodec(datastore),
                    new GeoNearCodec(datastore),
                    new GraphLookupCodec(datastore),
                    new GroupCodec(datastore),
                    new IndexStatsCodec(datastore),
                    new MergeCodec(datastore),
                    new PlanCacheStatsCodec(datastore),
                    new LimitCodec(datastore),
                    new LookupCodec(datastore),
                    new MatchCodec(datastore),
                    new OutCodec(datastore),
                    new ProjectionCodec(datastore),
                    new RedactCodec(datastore),
                    new ReplaceRootCodec(datastore),
                    new ReplaceWithCodec(datastore),
                    new SampleCodec(datastore),
                    new SetStageCodec(datastore),
                    new SetWindowFieldsCodec(datastore),
                    new SkipCodec(datastore),
                    new SortCodec(datastore),
                    new SortByCountCodec(datastore),
                    new UnionWithCodec(datastore),
                    new UnsetCodec(datastore),
                    new UnwindCodec(datastore));
        }
        return codecs;
    }

    @Nullable
    private void addCodec(StageCodec... stageCodecs) {
        for (StageCodec codec : stageCodecs) {
            codecs.put(codec.getEncoderClass(), codec);
        }
    }
}
