package no.haakon.jotepad.model.buffer;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.model.GuiHelpers;
import no.haakon.jotepad.model.editor.TekstEditor;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractTekstBuffer extends Buffer {

    private final File fil;
    private final TekstEditor editor;
    private final GuiHelpers guiHelpers;
    private final ApplicationFrame frame;
    private Charset charset;


    public AbstractTekstBuffer(File fil, Map<String, Object> args) {
        super();
        init();
        this.fil = fil;
        this.frame = konverter(args.get(Buffer.APPLICATION_FRAME), ApplicationFrame.class).orElseThrow(() -> new IllegalStateException("Ingen applikasjonsframe satt! Kan ikke lage buffer"));
        this.charset = konverter(args.get("charset"), Charset.class).orElse(Charset.defaultCharset());
        this.guiHelpers = new GuiHelpers(frame);
        this.editor = new TekstEditor(this);
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

        if(fil == null) {
            // Hvis det ikke er noen fil, setter vi opp et tomt dokument.
            editor.setTekst("");
            return;
        }

        try (FileInputStream fis = new FileInputStream(fil)) {
            String contents = editor.getTekst();
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
            editor.setTekst(contents);
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
    public Optional<File> getFil() {
        return Optional.ofNullable(fil);
    }

    /**
     * Ting som må gjøres for at denne superklassen skal kunne kalle inn til den.
     * Ting som å sette inn tekst osv.
     */
    public abstract void init();

    @Override
    public String getTypeNavn() {
        return "Tekst";
    }

    @Override
    public void die() {
        editor.setTekst("");
        getComponent().setEnabled(false);
    }

    @Override
    public void save() {
        if(fil == null) {
            System.err.println("Ingen fil satt, kan ikke lagre.");
            return;
        }

        if (!fil.canWrite()) {
            System.err.println("Har ikke skriverettigheter til " + fil.getAbsolutePath());
            String feilmelding = String.format("Har ikke skriverettigheter til fil %s. Kan ikke lagre.", fil.getAbsolutePath());
            guiHelpers.popupError("Kan ikke lagre", feilmelding);
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(fil)) {
            byte[] data = editor.getTekst().getBytes(StandardCharsets.UTF_8);
            try {
                fos.write(data);
            } catch (IOException ioe) {
                System.err.println("Kan ikke skrive data:\n" + ioe);
                guiHelpers.popupError("Feil under lagring", "Kan ikke skrive data.");
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("Finner ikke filen.");
            guiHelpers.popupError("Finner ikke filen", String.format("Fil %s finnes ikke", fil.getAbsolutePath()));
        } catch (IOException ioe) {
            System.err.println("En generisk io-feil oppstod:\n" + ioe);
            guiHelpers.popupError("Feil under lagring", "En ukjent feil oppstod under lagring");
        }
    }

    public GuiHelpers guiHelpers() {
        return guiHelpers;
    }

    public TekstEditor getEditor() {
        return editor;
    }
    public abstract JTextComponent getComponent();
    public abstract Document getDocument();
}
