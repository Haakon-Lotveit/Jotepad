package no.haakon.jotepad.model.editor;

import no.haakon.jotepad.actions.undo.TekstUndoer;
import no.haakon.jotepad.model.buffer.AbstractTekstBuffer;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Optional;
import java.util.OptionalInt;

public class TekstEditor extends Editor {

    private final TekstUndoer undoer;
    private final AbstractTekstBuffer buffer;

    public TekstEditor(AbstractTekstBuffer buffer) {
        // TODO: Må oppdateres...
        undoer = new TekstUndoer(null);
        this.buffer = buffer;
    }
    @Override
    public void lagre() {
        throw new UnsupportedOperationException("ikke implementert");
    }

    @Override
    public void undo() {

    }

    public int getMarkørPosisjon() {
        return -1;
    }

    public String getValgTekst() {
        return "";
    }

    public OptionalInt getCaretPosition() {
        JComponent comp = null; //getBuffer().getComponent();
        if(comp instanceof JTextComponent) {
            return OptionalInt.of(((JTextComponent) comp).getCaretPosition());
        }
        return OptionalInt.empty();
    }

    public Optional<String> getSelectedText() {
        if(null /*getBuffer()*/ instanceof AbstractTekstBuffer) {
            AbstractTekstBuffer tekstBuffer = (AbstractTekstBuffer) null; //getBuffer();
            return Optional.of(tekstBuffer.getComponent().getSelectedText());
        }
        return Optional.empty();
    }

    // TODO: Hva skal egentlig inn i Editor-klassene?
    public String getTekst() {
        return buffer.getComponent().getText();
    }

    public void setTekst(String tekst) {
        buffer.getComponent().setText(tekst);
    }
}
