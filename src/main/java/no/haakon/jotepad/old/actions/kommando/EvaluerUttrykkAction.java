package no.haakon.jotepad.old.actions.kommando;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import no.haakon.jotepad.old.actions.AbstractJotepadAction;
import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.model.GuiHelpers;

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
        Optional<String> kanskjeInput = guiHelpers.inputBox("Evaluer uttrykk", "Skriv inn uttrykket du vil evaluere");
        if(kanskjeInput.isEmpty()) {
            return; // ingenting skrevet inn.
        }
        GroovyShell shell = frame.getShell();
        String input = kanskjeInput.get();

        String resultat;
        try {
            Object evaluering = shell.evaluate(input);
            resultat = evaluering == null? "[Ingen resultat/nullverdi]" : evaluering.toString();
        } catch (GroovyRuntimeException ex) {
            resultat = "Kunne ikke evaluere uttrykket:\n" + input + "\nDetaljer er i loggbufferen.";
            frame.logg(String.format("Feil under evaluering av:%n%s%nFeilmelding:%n%s", input, ex.toString()));
        }

        guiHelpers.popupInfo("Resultat", resultat);

    }
}
