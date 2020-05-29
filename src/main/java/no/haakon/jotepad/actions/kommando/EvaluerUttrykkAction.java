package no.haakon.jotepad.actions.kommando;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import no.haakon.jotepad.actions.AbstractJotepadAction;
import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.model.GuiHelpers;

import java.awt.event.ActionEvent;
import java.util.Optional;

public class EvaluerUttrykkAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "EVAL";

    public EvaluerUttrykkAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final GuiHelpers guiHelpers = frame.synligBuffer().guiHelpers();
        Optional<String> input = guiHelpers.inputBox("Evaluer uttrykk", "Skriv inn uttrykket du vil evaluere");
        GroovyShell shell = frame.lagShell();
        String resultat = input.map(shell::evaluate)
                .map(Object::toString)
                .orElse("");

        guiHelpers.popupInfo("Resultat", resultat);

    }
}
