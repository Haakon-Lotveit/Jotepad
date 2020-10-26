package no.haakon.jotepad.old.model.buffer.tekst;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.io.File;
import java.util.Map;

// dette sammensuriumet skal inneholde:
// - JTextArea eller lignende som skal kunne vises
// - modellen under som vet om ting.
// - ++?
// Det er meningen at Editor-klassen skal inneholde en buffer, skrolling, osv. som så skal gis til ApplicationFrame.
// AF skal egentlig bare få disse tingene, og så skal andre ting snakke med ting via denne.
// Når denne virker kan vi abstrahere ut interfjes og slikt.
public class BasicTextBuffer extends AbstractTekstBuffer {

    JTextArea tekstvindu;
    public BasicTextBuffer(File fil, Map<String, Object> args) {
        super(fil, args);
    }
    @Override
    public void init() {
        tekstvindu = new JTextArea();
    }

    @Override
    public String getBufferNavn() {
        return String.format("%s: %s", getTypeNavn(), getFil().map(File::getAbsolutePath).orElse("Uten navn"));
    }

    @Override
    public JTextComponent getComponent() {
        return tekstvindu;
    }

    @Override
    public Document getDocument() {
        return tekstvindu.getDocument();
    }

}