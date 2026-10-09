package dev.morphia.mapping.codec.writer;

import com.mongodb.lang.Nullable;
import org.bson.Document;

import java.util.List;
import java.util.StringJoiner;

abstract class WriteState {
    private final DocumentWriter writer;
    private final WriteState previous;

    WriteState() {
        writer = null;
        previous = null;
    }

    WriteState(DocumentWriter writer, @Nullable WriteState previous) {
        this.writer = writer;
        this.previous = previous;
        writer.state(this);
    }

    protected abstract String state();

    protected String toString(Object value) {
        if (value instanceof Document) {
            StringJoiner joiner = new StringJoiner(", ", "{ ", " }");
            ((Document) value).entrySet().stream()
                    .map(e -> e.getKey() + ": " + toString(e.getValue()))
                    .forEach(joiner::add);

            return joiner.toString();
        } else if (value instanceof List) {
            StringJoiner joiner = new StringJoiner(", ", "[ ", " ]");
            ((List<?>) value).stream()
                    .map(this::toString)
                    .forEach(joiner::add);

            return joiner.toString();
        } else {
            return String.valueOf(value);
        }
    }

    WriteState array() {
        throw new IllegalStateException("Can not start a new array while in state: " + state() + ". writer: " + getWriter());
    }

    WriteState document() {
        throw new IllegalStateException("Can not start a new document while in state: " + state() + ". writer: " + getWriter());
    }

    void done() {
    }

    void end() {
        getWriter().state(previous);
        if (previous != null) {
            previous.done();
        }
    }

    DocumentWriter getWriter() {
        return writer;
    }

    WriteState name(String name) {
        throw new IllegalStateException("Was not expecting a name while in the " + state() + " state." + "  writer:  " + getWriter());
    }

    void value(Object value) {
        throw new IllegalStateException("Was not expecting a value while in the " + state() + " state." + "  writer:  " + getWriter());
    }
}
