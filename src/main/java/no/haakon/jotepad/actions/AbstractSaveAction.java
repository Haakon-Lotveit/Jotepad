package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.Editor;
import no.haakon.jotepad.gui.components.FileChooserOption;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractSaveAction extends AbstractJotepadAction {

    /**
     * This constructor is protected because Streams are not meant to be used as arguments in general.
     * However, I consider this to be fine, as a Stream is a fine lowest-common-demoninator: You can have an array, collection or a singular object represented in a stream.
     * This makes things easier.
     * <p>
     * This makes inheriting from the abstract class a bit simpler, since you can specify all sorts of constructors yourself, depending on need.
     *
     * @param commandRoot The root of the command. Typically something like "NEW_FILE", or "SAVE",etc.
     *                    The actual command will have a UUID (v4) appended to it to make it unique.
     *                    That is, the first piece of the id is for information, the second piece is for uniqueification.
     *                    You can manually mess this up, but automatically, not.
     * @param frame       The frame serves as a sort of root node of wisdom: It can tell you things like which buffer is currently shown, and more.
     */
    protected AbstractSaveAction(String commandRoot, ApplicationFrame frame) {
        super(commandRoot, frame);
    }

    /**
     * Denne metoden vil finne en fil, og bruke filen brukeren velger til å lagre til.
     * Dersom editoren ikke har en fil assosiert med seg, må enten denne eller tilsvarende gjøres av Lagre.
     * Lagre som bør nok bruke denne uansett.
     */
    protected void selectNewFile() {
        Editor editor = frame.synligBuffer();
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
        Editor editor = frame.synligBuffer();
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
