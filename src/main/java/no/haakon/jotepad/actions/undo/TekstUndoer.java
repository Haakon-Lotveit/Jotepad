package no.haakon.jotepad.actions.undo;

import no.haakon.jotepad.model.editor.TekstEditor;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

/**
 * Dette er toppnivået på undomanageren. Denne er den biten som en plugger inn steder.
 * Resten er ment til å være usynlig.
 *
 * Siden det ikke er noen ordentlige steder å dokumentere dette på kan de like gjerne dokumenteres her:
 *
 *  <p>Ideen er egentlig ganske enkel: Vi samler redigeringshendelsene (UndoableEdit-objekter) i tidsbolker, og en undo
 *  vil da si at vi kjører en undo på alle redigeringshendelsene i den bolken. En evt. redo vil være likedan.
 *
 *  Dette treffer ikke perfekt, siden den ikke splitter på ord eller lignende, men det funker.
 *
 *  For å håndtere disse bolkene med redigeringer har vi en CompoundedEditManager. Dens jobb er å ta imot redigeringshendelser og bygge en stable med bolker.
 *  Dens ansvar består utelukkende av å holde på hendelsene, og å ta imot meldinger om at undo og redo ønskes.
 *
 *  For å håndtere bolkene, bruker den en klasse kalt CompoundedEdit, som holder på redigeringer. Denne klassen vet om hendelsene, og om når de kom inn, relativt til hverandre.
 *  Dette trenger den for å kunne sortere slik at yngste/eldste hendelsen blir undoet/redoet først. Den vet noe om forskjellen på eldste og yngste, og den godtar alle den får inn.
 *
 *  For å håndtere dette med tid, har vi nok et objekt, som heter TimestampedEdit. Dette er en enkel klasse som tar imot en hendelse og putter på et timestamp. Enkelt og greit.
 */
public class TekstUndoer implements UndoableEditListener {

    TekstEditor editor;
    CompoundedEditManager editManager = new CompoundedEditManager();

    public TekstUndoer(TekstEditor editor) {
        super();
        this.editor = editor;
    }

    public void undo() {
        editManager.undo();
    }

    public void nullstill() {
        editManager = new CompoundedEditManager();
    }
    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        editManager.receiveUndoableEdit(e.getEdit());
    }
}
