package no.haakon.jotepad.old.actions;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;

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
        frame.synligBuffer().guiHelpers().filDialogÅpne().ifPresent(this::åpneMedUtf8);
    }

    private void åpneMedUtf8(File fil) {
        frame.åpneFil(fil, UTF_8);
    }
}
