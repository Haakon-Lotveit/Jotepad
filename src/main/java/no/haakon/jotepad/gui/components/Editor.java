package no.haakon.jotepad.gui.components;

import no.haakon.jotepad.actions.undo.TestUndoer;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.ToIntBiFunction;

/**
 * Dette er per nå en skikkelig roteklasse.
 * Den gjør flere ting, og det er ikke bra:
 * <ul>
 * <li>Den tilbyr funksjoner som å ta imot input, lage popups, filvelger, osv.</li>
 * <li>Den Holder på state i en og bare en buffer, så du kan ikke ha flere filer åpne eller lignende</li>
 * <li>Den tilbyr global lagringsplass for variabler, og i teorien per-buffer, men siden vi bare hare en...</li>
 * </ul>
 */
public class Editor extends JTextArea {

    private final JScrollPane scrollPane;
    private File editingFile = null;
    private final Map<String, String> state;
    private final Map<String, Map<Class<?>, Object>> objekter;
    private final TestUndoer undoer;
    private final ApplicationFrame parentFrame;
    private String unikId = UUID.randomUUID().toString();
    private String vennligNavn = "Navnløs";

    public Editor(ApplicationFrame parentFrame) {
        super();
        this.scrollPane = new EditorScrollPane(this);
        this.state = new HashMap<>();
        this.objekter = new HashMap<>();
        undoer = new TestUndoer(this);
        this.getDocument().addUndoableEditListener(undoer);
        this.parentFrame = parentFrame;
        parentFrame.addEditor(this);
    }

    /**
     * @return the frame that contains the editor.
     */
    public ApplicationFrame getParentFrame() {
        return parentFrame;
    }
    /**
     * @return the UndoManager associated with this editor's Document.
     */
    public TestUndoer getUndoer() {
        return undoer;
    }

    /**
     * Returns a scollpane with this component as scrollable.
     *
     * @return a JScrollPane with this component set. Will be reused
     */
    public JScrollPane getInScrollPane() {
        return scrollPane;
    }

    public Optional<File> getFile() {
        return Optional.ofNullable(editingFile);
    }

    public Editor setFile(File file) {
        Objects.requireNonNull(file, "Cannot set the currently editing file to null.");
        this.editingFile = file;
        this.vennligNavn = file.getAbsolutePath();
        return this;
    }

    /**
     * Forsøker å laste inn en fil til å vises som tekst.
     * Langt ifra ferdig!
     *
     * @param file
     * @param charset
     */
    public void loadFile(File file, Charset charset) {
        Objects.requireNonNull(file, "Du må spesifisere en reell ikke-nullær fil");
        Objects.requireNonNull(charset, "Du kan ikke bruke null som charset");

        try (FileInputStream fis = new FileInputStream(file)) {
            String contents = this.getText();
            try {
                contents = new String(fis.readAllBytes(), charset);
            } catch (IOException ioe) {
                // Dette går i "loggen" slik som den er. Greit for debugging og slikt.
                System.err.println("Noe gikk galt under lesing av filen: " + ioe);
                // Dette vises til brukeren, slik at de vet at det ikke gikk.
                String message = String.format("Feil under åpning av fil '%s', på sti '%s'. Kan ikke bruke innholdet", file.getName(), file.getAbsolutePath());
                JOptionPane.showMessageDialog(this, message, "Feil under lesing", JOptionPane.ERROR_MESSAGE);
            }
            this.setText(contents);
            this.setFile(file); // Vi kan ikke sette filen før nå, fordi det er først nå vi vet at alt har gått bra...
        } catch (FileNotFoundException fnfe) {
            System.err.println("fant ikke filen"); // TODO: Dette må opplagt håndteres på en bedre måte...
        } catch (IOException ioe) {
            System.err.println("noe gikk galt: " + ioe);
        }
        requestFocus();
        setCaretPosition(0);
        undoer.nullstill();
    }

    public void newFile() {
        this.editingFile = null;
        this.setText("");
    }

    /**
     * Lager en popup med angitt tittel og melding.
     * Parent-container blir brukt til å angi hvor den skal poppe opp.
     *
     * @param title   Tittelen på meldingen
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
     *
     * @param title   Tittelen på boksen.
     * @param message Meldingen som skal stå foran.
     * @return Optional.empty() dersom boksen er tom, eller bruker trykket avbryt (da blir strengen null). Ellers blir en Optional med strengen returnert.
     */
    public Optional<String> inputBox(String title, String message) {
        final String out = JOptionPane.showInputDialog(getParent(), message, title, JOptionPane.QUESTION_MESSAGE);
        if (out == null) {
            System.err.println("Bruker avbrøt input.");
            return Optional.empty();
        }
        if (out.isEmpty()) {
            System.err.println("Bruker skrev ikke inn input");
            return Optional.empty();
        }

        return Optional.of(out);
    }

    public Optional<File> filDialogÅpne() {
        return jFileChooserHelper((editor, jFileChooser) -> jFileChooser.showOpenDialog(this),
                                  new JFileChooser());
    }

    public Optional<File> filDialogLagre() {
        return jFileChooserHelper((editor, fileChooser) -> fileChooser.showSaveDialog(editor),
                                  new JFileChooser());
    }

    /**
     * Abstraherer vekk mye av seremonien rundt det å åpne en JFileChooser for å åpne eller lagre en fil.
     * Den tar inn en JFileChooser så du kan sette den opp på forhånd.
     *
     * @param handling    handlingen du vil kjøre for å vise fildialogen.
     * @param fileChooser en JFileChooser som har blitt satt opp med filtre og lignende så den er klar til å brukes.
     * @return en Optional<File> som inneholder en fil hvis og bare hvis en fil klarte å bli valgt.
     */
    private Optional<File> jFileChooserHelper(final ToIntBiFunction<Editor, JFileChooser> handling, final JFileChooser fileChooser) {
        switch (FileChooserOption.from(handling.applyAsInt(this, fileChooser))) {
            case APPROVE:
                File valgtFil = fileChooser.getSelectedFile();
                System.out.println("Har valgt fil: " + valgtFil.getAbsolutePath());
                return Optional.of(valgtFil);
            case CANCEL:
                System.out.println("Brukeren avbrøt");
                return Optional.empty();
            case ERROR:
                System.out.println("Noe gikk galt!");
                return Optional.empty();
            default:
                System.err.println("Feil i valg av fil til å lagre: Fil ble ikke valgt, ingen feilmelding returnert, og brukeren avbrøt ikke.");
                return Optional.empty();
        }
    }

    public String getId() {
        return unikId;
    }

    public String getVennligNavn() {
        return vennligNavn;
    }

    public JaNeiValg jaNeiPopup(String tittel, String melding) {
        int choice = JOptionPane.showConfirmDialog(this, melding, tittel, JOptionPane.YES_NO_OPTION);
        return JaNeiValg.fraInt(choice);
    }
}
