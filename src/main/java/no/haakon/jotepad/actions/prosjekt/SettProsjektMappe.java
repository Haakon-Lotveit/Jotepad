package no.haakon.jotepad.actions.prosjekt;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.FileChooserOption;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Optional;

/**
 * Setter hvilken mappe som skal indekseres for å finne filer.
 */
public class SettProsjektMappe extends AbstractProsjektAction {

    public static final String COMMAND_ROOT = "SETT_FILINDEKS";

    public SettProsjektMappe(ApplicationFrame editor) {
        super(COMMAND_ROOT, editor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File nåværendeMappe = frame.synligBuffer().getFile()
                .map(File::getParent)
                .map(File::new)
                .orElse(new File(System.getProperty("user.home"))); // Hvis ikke det finnes en valgt fil, tar vi utgangspunkt i hjemmemappen til brukeren.
        JFileChooser velgMappe = new JFileChooser();
        velgMappe.setCurrentDirectory(nåværendeMappe);
        velgMappe.setDialogTitle("Velg mappe som skal indekseres");
        velgMappe.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        velgMappe.setAcceptAllFileFilterUsed(false);

        // For at dette skal bli ordentlig bør JFileChooser wrappes kjærlig i en annen klasse.
        Optional<File> valgresultat = null;
        switch (FileChooserOption.from(velgMappe.showOpenDialog(frame.synligBuffer()))) {
            case APPROVE:
                File valgtFil = velgMappe.getSelectedFile();
                System.out.println("Har valgt fil: " + valgtFil.getAbsolutePath());
                valgresultat = Optional.of(valgtFil);
                break;
            case CANCEL:
                System.out.println("Brukeren avbrøt");
                valgresultat = Optional.empty();
                break;
            case ERROR:
                System.out.println("Noe gikk galt!");
                valgresultat = Optional.empty();
                break;
            default:
                System.err.println("Feil i valg av fil til å lagre: Fil ble ikke valgt, ingen feilmelding returnert, og brukeren avbrøt ikke.");
                valgresultat = Optional.empty();
                break;
        }

        if(valgresultat.isEmpty()) {
            return; // ingenting å gjøre, ingenting ble valgt...
        }

        frame.setValue(NØKKEL_INDEKSERT_MAPPE, valgresultat.get().getAbsolutePath());

        System.out.println("Satt mappe til: " + frame.getValue(NØKKEL_INDEKSERT_MAPPE));
        oppdaterFilIndeks();
    }
}
