package no.haakon.jotepad.old.model.buffer.tekst;

import no.haakon.jotepad.old.model.buffer.Buffer;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class LoggBuffer extends Buffer {
    public LoggBuffer(Map<String, Object> args) {
        super(null, args);
        this.komponent = new JTextArea();
        this.editor = new LoggEditor(this);// Du skal ikke redigere loggen!
        komponent.setEditable(false);
    }

    @Override
    public String getTypeNavn() {
        return "Logg";
    }

    @Override
    public void die() {
        // Denne bufferen skal aldri dø...
    }

    @Override
    public String getBufferNavn() {
        return "Loggen!";
    }

    @Override
    public JComponent getComponent() {
        return komponent;
    }

    @Override
    public LoggEditor getEditor() {
        return editor;
    }

    @Override
    public byte[] innholdSomBytes() {
        try {
            return komponent.getDocument().getText(0, komponent.getDocument().getLength()).getBytes(StandardCharsets.UTF_8);
        } catch(BadLocationException ble) {
            editor.melding("Feil med LoggBuffer. Kunne ikke hente ut teksten, returnerer tomt innhold");
            return new byte[0];
        }
    }

    public final JTextArea getKomponent() {
        return komponent;
    }

    private final JTextArea komponent;
    private final LoggEditor editor;
}
