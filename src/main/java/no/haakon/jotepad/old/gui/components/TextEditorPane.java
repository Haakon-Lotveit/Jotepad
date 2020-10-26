package no.haakon.jotepad.old.gui.components;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;

public class TextEditorPane extends JTextArea {

    private File editingFile = null;

    public TextEditorPane() {
        super();
        this.setPreferredSize(new Dimension(1024, 768));
    }

    /**
     * Creates a scollpane with this component as scrollable.
     * @return a new JScrollPane (every time) with this component set.
     */
    public JScrollPane getInScrollPane() {
        return new JScrollPane(this);
    }

    public Optional<File> getFile() {
        return Optional.ofNullable(editingFile);
    }

    public TextEditorPane setFile(File file) {
        Objects.requireNonNull(file, "Cannot set the currently editing file to null.");
        this.editingFile = file;
        return this;
    }

    /**
     * Forsøker å laste inn en fil til å vises som tekst.
     * Langt ifra ferdig!
     * @param file
     * @param charset
     */
    public void loadFile(File file, Charset charset) {
        Objects.requireNonNull(file, "Du må spesifisere en reell ikke-nullær fil");
        Objects.requireNonNull(charset, "Du kan ikke bruke null som charset");

        try(FileInputStream fis = new FileInputStream(file)) {
            String contents = this.getText();
            try {
                 contents = new String(fis.readAllBytes(), charset);
            }
            catch (IOException ioe) {
                // Dette går i "loggen" slik som den er. Greit for debugging og slikt.
                System.err.println("Noe gikk galt under lesing av filen: " + ioe);
                // Dette vises til brukeren, slik at de vet at det ikke gikk.
                String message = String.format("Feil under åpning av fil '%s', på sti '%s'. Kan ikke bruke innholdet", file.getName(), file.getAbsolutePath());
                JOptionPane.showMessageDialog(this, message, "Feil under lesing", JOptionPane.ERROR_MESSAGE);
            }
            this.setText(contents);
            this.setFile(file); // Vi kan ikke sette filen før nå, fordi det er først nå vi vet at alt har gått bra...
        }
        catch (FileNotFoundException fnfe) {
            System.err.println("fant ikke filen"); // TODO: Dette må opplagt håndteres på en bedre måte...
        }
        catch(IOException ioe) {
            System.err.println("noe gikk galt: " + ioe);
        }
    }

}
