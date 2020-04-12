package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.Editor;
import no.haakon.jotepad.gui.components.FileChooserOption;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class LoadFileAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "ÅPNE";

    public LoadFileAction(Editor editor) {
        super(COMMAND_ROOT, editor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Consumer<File> åpneMedUtf8 = f -> editor.loadFile(f, StandardCharsets.UTF_8);
        editor.filDialogÅpne().ifPresent(åpneMedUtf8);
    }
}
