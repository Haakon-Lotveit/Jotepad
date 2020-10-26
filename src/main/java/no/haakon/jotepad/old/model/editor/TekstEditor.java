package no.haakon.jotepad.old.model.editor;

import no.haakon.jotepad.old.actions.undo.TekstUndoer;
import no.haakon.jotepad.old.model.buffer.tekst.AbstractTekstBuffer;

public class TekstEditor extends Editor {

    private final TekstUndoer undoer;
    private final AbstractTekstBuffer buffer;

    public TekstEditor(AbstractTekstBuffer buffer) {
        // TODO: Må oppdateres...
        undoer = new TekstUndoer(null);
        this.buffer = buffer;
    }


    @Override
    protected AbstractTekstBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void undo() {
        undoer.undo();
    }

    public int getMarkørPosisjon() {
        return buffer.getComponent().getCaretPosition();
    }

    public String getValgTekst() {
        return buffer.getComponent().getSelectedText();
    }


    // TODO: Hva skal egentlig inn i Editor-klassene?
    public String getTekst() {
        return buffer.getComponent().getText();
    }

    public void setTekst(String tekst) {
        buffer.getComponent().setText(tekst);
    }
}
