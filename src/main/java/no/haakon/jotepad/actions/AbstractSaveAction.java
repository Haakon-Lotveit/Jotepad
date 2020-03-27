package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.Editor;
import no.haakon.jotepad.gui.components.FileChooserOption;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractSaveAction extends AbstractJotepadAction {

    public AbstractSaveAction(String commandRoot, Editor editor, KeyStroke shortcut) {
        super(commandRoot, editor, shortcut);
    }

    public AbstractSaveAction(String commandRoot, Editor editor) {
        super(commandRoot, editor);
    }

    /**
     * Denne metoden vil finne en fil, og bruke filen brukeren velger til å lagre til.
     * Dersom editoren ikke har en fil assosiert med seg, må enten denne eller tilsvarende gjøres av Lagre.
     * Lagre som bør nok bruke denne uansett.
     */
    protected void selectNewFile() {
        JFileChooser chooser = new JFileChooser();
        FileChooserOption valg = FileChooserOption.from(chooser.showOpenDialog(editor));
        switch (valg) {
            case APPROVE:
                File fil = chooser.getSelectedFile();
                editor.setFile(fil);
                save(fil);
            case ERROR:
                System.err.println("Noe gikk galt, men jeg vet ikke hvordan jeg får tak i feilmeldingen enda.");
            case CANCEL:
            default:
                return;
        }
    }

    /**
     * Denne metoden er ansvarlig for å lagre filen.
     * Den sjekker ikke om filen finnes, det er ansvaret til metodene som kaller denne.
     * selectNewFile sniker seg unna ved å kalle editor.setFile først, slik at en nullær fil kaster en feilmelding.
     * Det er som alltid mye som kan gå galt som må meldes fra om til brukeren, så det er en del try-catch her.
     * Denne metoden er IKKE ferdig. Det er mye som mangler på kommunikasjon til brukeren.
     *
     * @param fil filen som skal lagres.
     */
    protected void save(File fil) {
        if (!fil.canWrite()) {
            System.err.println("Har ikke skriverettigheter til " + fil.getAbsolutePath());
            String feilmelding = String.format("Har ikke skriverettigheter til fil %s. Kan ikke lagre.", fil.getAbsolutePath());
            editor.popupError("Kan ikke lagre", feilmelding);
        }
        try (FileOutputStream fos = new FileOutputStream(fil)) {
            String tekst = editor.getText();
            byte[] data = tekst.getBytes(StandardCharsets.UTF_8);
            try {
                fos.write(data);
            } catch (IOException ioe) {
                System.err.println("Kan ikke skrive data:\n" + ioe);
                editor.popupError("Feil under lagring", "Kan ikke skrive data.");
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("Finner ikke filen.");
            editor.popupError("Finner ikke filen", String.format("Fil %s finnes ikke", fil.getAbsolutePath()));
        } catch (IOException ioe) {
            System.err.println("En generisk io-feil oppstod:\n" + ioe);
            editor.popupError("Feil under lagring", "En ukjent feil oppstod under lagring");
        }
    }
}
