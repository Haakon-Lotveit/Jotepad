package no.haakon.jotepad.actions.undo;

import javax.swing.undo.UndoableEdit;
import java.util.Objects;

public final class TimestampedEdit {
    private final long timestamp;
    private final UndoableEdit edit;

    public TimestampedEdit(UndoableEdit edit) {
        Objects.requireNonNull(edit);
     this.timestamp = System.nanoTime();
     this.edit = edit;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UndoableEdit getEdit() {
        return edit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimestampedEdit that = (TimestampedEdit) o;
        return timestamp == that.timestamp &&
                edit.equals(that.edit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, edit);
    }

    @Override
    public String toString() {
        return "TimestampedEdit{" +
                "timestamp=" + timestamp +
                ", edit=" + edit +
                '}';
    }
}
