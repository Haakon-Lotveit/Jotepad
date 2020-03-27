package no.haakon.jotepad.actions.undo;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * This class represents a collection of edits, grouped by time.
 * It is a priority queue of timestamped edits, with links to the front and the back of the queue.
 */
public final class CompoundedEdit {
    PriorityQueue<TimestampedEdit> edits = new PriorityQueue<>(Comparator.comparingLong(TimestampedEdit::getTimestamp).reversed()); // Yngste først ut.

    private long lowestTime = Long.MAX_VALUE;
    private long highestTime = Long.MIN_VALUE;

    public void acceptEdit(TimestampedEdit edit) {
        lowestTime = Math.min(lowestTime, edit.getTimestamp());
        highestTime = Math.max(highestTime, edit.getTimestamp());
        edits.add(edit);
    }

    /**
     * Sier noe om hvor lang periode med editer som ligger i denne sammensetningen
     * @return en long med antall nanosekunder.
     */
    public long periode() {
        if(edits.size() == 0) {
            return 0L;
        }
        return highestTime - lowestTime;
    }

    /**
     * Undos everything here, in order.
     */
    public void undoAll() {
       while(!edits.isEmpty()) {
           UndoableEdit currentEdit = edits.poll().getEdit();
           if(currentEdit.canUndo()) {
               currentEdit.undo();
           }
       }
    }

    /**
     * Lager en sentinelverdi, dvs. en verdi som betyr "tomt objekt".
     * Da unngår vi et null-objekt.
     * @return en CompounedEdit med maksimal alder.
     */
    public static CompoundedEdit createSentinelCompoundedEdit() {
        /* I tilfelle en får tak i denne editen, så skal det ikke skje noe galt om en prøver å gjøre noe.
         * Prøver du å undo denne, går det fint. Redo? Null stress.
         * Legg til eller bytte ut? Det går ikke.
         */
        UndoableEdit tomEdit = new UndoableEdit() {
            @Override
            public void undo() throws CannotUndoException {
                return;
            }

            @Override
            public boolean canUndo() {
                return true;
            }

            @Override
            public void redo() throws CannotRedoException {
                return;
            }

            @Override
            public boolean canRedo() {
                return true;
            }

            @Override
            public void die() {
                return;
            }

            @Override
            public boolean addEdit(UndoableEdit anEdit) {
                return false;
            }

            @Override
            public boolean replaceEdit(UndoableEdit anEdit) {
                return false;
            }

            @Override
            public boolean isSignificant() {
                return false;
            }

            @Override
            public String getPresentationName() {
                return "EMPTY";
            }

            @Override
            public String getUndoPresentationName() {
                return "EMPTY";
            }

            @Override
            public String getRedoPresentationName() {
                return "EMPTY";
            }
        };

        TimestampedEdit ensligNullverdi = new TimestampedEdit(tomEdit);
        CompoundedEdit sentinel = new CompoundedEdit();
        sentinel.acceptEdit(ensligNullverdi);
        sentinel.highestTime = Long.MAX_VALUE;
        sentinel.lowestTime = 0L;

        return sentinel;
    }


}
