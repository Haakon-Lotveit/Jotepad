package no.haakon.jotepad.model;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.FileChooserOption;
import no.haakon.jotepad.gui.components.JaNeiValg;

import javax.swing.*;
import java.io.File;
import java.util.Optional;
import java.util.function.ToIntBiFunction;

/**
 * Tilbyr forskjellige hjelpemetoder for å gjøre ting som å vise filvelgere, popuper, mm.
 * Navnet er på en måte veldig korrekt, men også litt feil. Det det GJØR er å hjelpe til med GUI-funksjoner,
 * men intensjonen er å tilby kanoniske gode måter å samhandle med brukeren på. Popups er en dårlig måte, men det er enkelt å starte med.
 */
public class GuiHelpers {
    private final ApplicationFrame frame;

    public GuiHelpers(ApplicationFrame frame) {
        this.frame = frame;
    }

    public void popupError(String title, String message) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void popupInfo(String title, String message) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
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
        final String out = JOptionPane.showInputDialog(frame, message, title, JOptionPane.QUESTION_MESSAGE);
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

    // TODO: JFileChooser trenger mye kjærlighet for å være brukervennlighet.
    public Optional<File> filDialogÅpne() {
        return jFileChooserHelper((editor, jFileChooser) -> jFileChooser.showOpenDialog(frame),
                                  new JFileChooser());
    }

    public Optional<File> filDialogLagre() {
        return jFileChooserHelper((editor, fileChooser) -> fileChooser.showSaveDialog(frame),
                                  new JFileChooser());
    }


    public JaNeiValg jaNeiPopup(String tittel, String melding) {
        int choice = JOptionPane.showConfirmDialog(frame, melding, tittel, JOptionPane.YES_NO_OPTION);
        return JaNeiValg.fraInt(choice);
    }

    /**
     * Abstraherer vekk mye av seremonien rundt det å åpne en JFileChooser for å åpne eller lagre en fil.
     * Den tar inn en JFileChooser så du kan sette den opp på forhånd.
     *
     * @param handling    handlingen du vil kjøre for å vise fildialogen.
     * @param fileChooser en JFileChooser som har blitt satt opp med filtre og lignende så den er klar til å brukes.
     * @return en Optional<File> som inneholder en fil hvis og bare hvis en fil klarte å bli valgt.
     */
    private Optional<File> jFileChooserHelper(final ToIntBiFunction<ApplicationFrame, JFileChooser> handling, final JFileChooser fileChooser) {
        switch (FileChooserOption.from(handling.applyAsInt(frame, fileChooser))) {
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

}
