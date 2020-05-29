package no.haakon.jotepad.model.editor;

import no.haakon.jotepad.actions.undo.TekstUndoer;
import no.haakon.jotepad.model.GuiHelpers;
import no.haakon.jotepad.model.buffer.Buffer;
import no.haakon.jotepad.model.buffer.tekst.AbstractTekstBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

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
