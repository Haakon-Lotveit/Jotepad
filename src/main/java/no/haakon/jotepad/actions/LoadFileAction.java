package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.Editor;

import java.awt.event.ActionEvent;
import java.io.File;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LoadFileAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "ÅPNE";

    public LoadFileAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Editor editor = frame.synligBuffer();
        editor.filDialogÅpne().ifPresent(this::åpneMedUtf8);
    }

    private void åpneMedUtf8(File fil) {
        frame.nyEditorForFil(fil, UTF_8);
    }
}
