package no.haakon.jotepad.old.actions;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.function.Consumer;

final public class SaveFileAction extends AbstractSaveAction {

    public static final String COMMAND_ROOT = "LAGRE";

    public SaveFileAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    /**
     * Denne metoden er egentlig veldig enkel, men kan virke litt sær om du ikke er vant til Optional:
     * Dersom det finnes en fil assosiert med editoren, blir denne valgt til å lagre med.
     * Men dersom det ikke finnes en, blir selectNewFile kjørt, og den vil i så fall lagre.
     * @param actionEvent blir ignorert.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Consumer<File> lagre = (ignored) -> frame.synligBuffer().getEditor().lagre();
        frame.synligBuffer().getFil().ifPresentOrElse(lagre, this::selectNewFile);
    }
}
