package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.FileChooserOption;
import no.haakon.jotepad.gui.components.TextEditorPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class LoadFileAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "ÅPNE";

    public LoadFileAction(TextEditorPane editor, KeyStroke snarvei) {
        super(COMMAND_ROOT, editor, snarvei);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(editor);

        switch (FileChooserOption.from(returnVal)) {
            case APPROVE:
                File valgtFil = fileChooser.getSelectedFile();
                System.out.println("Har valgt fil: " + valgtFil.getAbsolutePath());
                editor.loadFile(valgtFil, StandardCharsets.UTF_8);
                break;
            case CANCEL:
                System.out.println("Brukeren avbrøt");
                break;
            case ERROR:
                System.out.println("Noe gikk galt!");
                break;
        }

    }
}
