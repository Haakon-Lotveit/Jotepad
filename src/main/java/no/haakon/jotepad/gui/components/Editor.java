package no.haakon.jotepad.gui.components;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Editor extends JTextArea {

    private File editingFile = null;
    private final Map<String, String> state;

    public Editor() {
        super();
        this.setPreferredSize(new Dimension(1024, 768));
        this.state = new HashMap<>();
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

    public Editor setFile(File file) {
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

    public void newFile() {
        this.editingFile = null;
        this.setText("");
    }

    /**
     * Lager en popup med angitt tittel og melding.
     * Parent-container blir brukt til å angi hvor den skal poppe opp.
     * @param title Tittelen på meldingen
     * @param message Den faktiske meldingen.
     */
    public void popupError(String title, String message) {
        JOptionPane.showMessageDialog(getParent(), message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void popupInfo(String title, String message) {
        JOptionPane.showMessageDialog(getParent(), message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Popper opp en vanlig inputdialog. Du oppgir tittel og melding som skal vises.
     * messageType er satt til JOptionPane.QUESTION_MESSAGE.
     * Dersom strengen som er returnert er null eller "", blir Optional.empty() returnert.
     * Ellers blir en Optional med strengen returnert.
     * @param title Tittelen på boksen.
     * @param message Meldingen som skal stå foran.
     * @return Optional.empty() dersom boksen er tom, eller bruker trykket avbryt (da blir strengen null). Ellers blir en Optional med strengen returnert.
     */
    public Optional<String> inputBox(String title, String message) {
        final String out = JOptionPane.showInputDialog(getParent(), message, title, JOptionPane.QUESTION_MESSAGE);
        if(out == null) {
            System.err.println("Bruker avbrøt input.");
            return Optional.empty();
        }
        if(out.isEmpty()) {
            System.err.println("Bruker skrev ikke inn input");
            return Optional.empty();
        }

        return Optional.of(out);
    }

    public Editor setValue(String key, String value) {
        state.put(key, value);
        return this;
    }

    public String getValue(String key) {
        return state.get(key);
    }

}