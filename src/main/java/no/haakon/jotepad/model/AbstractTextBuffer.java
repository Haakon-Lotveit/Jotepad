package no.haakon.jotepad.model;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

public abstract class AbstractTextBuffer implements Buffer {

    private final File fil;

    public AbstractTextBuffer(File fil, Charset charset) {
        Objects.requireNonNull(fil, "Du må spesifisere en reell ikke-nullær fil");
        this.fil = fil;
        lastFilSomTekst(charset);
    }

    /**
     * Forsøker å laste inn en fil til å vises som tekst.
     * Langt ifra ferdig!
     *
     * @param charset tegnsettet som skal brukes
     */
    protected void lastFilSomTekst(Charset charset) {
        Objects.requireNonNull(charset, "Du kan ikke bruke null som charset");

        try (FileInputStream fis = new FileInputStream(fil)) {
            String contents = getTekst();
            try {
                contents = new String(fis.readAllBytes(), charset);
            } catch (IOException ioe) {
                // Dette går i "loggen" slik som den er. Greit for debugging og slikt.
                System.err.println("Noe gikk galt under lesing av filen: " + ioe);
                // Dette vises til brukeren, slik at de vet at det ikke gikk.
                String message = String.format("Feil under åpning av fil '%s', på sti '%s'. Kan ikke bruke innholdet", fil.getName(), fil.getAbsolutePath());
                // TODO: dette må over til buffer sine ting som den kan.
                JOptionPane.showMessageDialog(getComponent(), message, "Feil under lesing", JOptionPane.ERROR_MESSAGE);
            }
            this.setTekst(contents);
            getComponent().requestFocus();
            getComponent().setCaretPosition(0);
        } catch (FileNotFoundException fnfe) {
            System.err.println("fant ikke filen"); // TODO: Trenger en bedre feilmelding her...
            throw new IllegalStateException(fnfe);
        } catch (IOException ioe) {
            System.err.println("noe gikk galt: " + ioe);
            throw new IllegalStateException(ioe);
        }
    }

    @Override
    public File getFil() {
        return fil;
    }

    @Override
    public abstract String getTypeNavn();

    @Override
    public void die() {
        this.setTekst("");
        this.getComponent().setEnabled(false);
    }

    abstract String getTekst();
    abstract void setTekst(String tekst);
    public abstract JTextComponent getComponent();
}
