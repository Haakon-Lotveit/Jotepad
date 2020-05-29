package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.FileChooserOption;
import no.haakon.jotepad.model.buffer.Buffer;

import javax.swing.*;
import java.io.File;

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
        Buffer buffer = frame.synligBuffer();
        JFileChooser chooser = new JFileChooser();
        FileChooserOption valg = FileChooserOption.from(chooser.showOpenDialog(frame));
        switch (valg) {
            case APPROVE:
                File fil = chooser.getSelectedFile();
                buffer.setFil(fil);
                buffer.getEditor().lagre();
            case ERROR:

                System.err.println("Noe gikk galt, men jeg vet ikke hvordan jeg får tak i feilmeldingen enda.");
            case CANCEL:
            default:
                return;
        }
    }
}
