package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.model.buffer.Buffer;

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
