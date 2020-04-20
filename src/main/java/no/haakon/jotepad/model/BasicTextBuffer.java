package no.haakon.jotepad.model;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.File;
import java.nio.charset.Charset;

// dette sammensuriumet skal inneholde:
// - JTextArea eller lignende som skal kunne vises
// - modellen under som vet om ting.
// - ++?
// Det er meningen at Editor-klassen skal inneholde en buffer, skrolling, osv. som så skal gis til ApplicationFrame.
// AF skal egentlig bare få disse tingene, og så skal andre ting snakke med ting via denne.
// Når denne virker kan vi abstrahere ut interfjes og slikt.
public class BasicTextBuffer extends AbstractTextBuffer {

    JTextArea tekstvindu;

    public BasicTextBuffer(File fil, Charset charset) {
        super(fil, charset);
    }

    @Override
    public String getTypeNavn() {
        return "Tekst";
    }

    @Override
    public String getBufferNavn() {
        return String.format("%s: %s", getTypeNavn(), getFil().getAbsolutePath());
    }

    @Override
    String getTekst() {
        return tekstvindu.getText();
    }

    @Override
    void setTekst(String tekst) {
        tekstvindu.setText(tekst);
    }

    @Override
    public JTextComponent getComponent() {
        return tekstvindu;
    }
}