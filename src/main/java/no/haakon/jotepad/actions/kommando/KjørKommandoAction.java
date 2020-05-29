package no.haakon.jotepad.actions.kommando;

import no.haakon.jotepad.actions.AbstractJotepadAction;
import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.inputlister.Kommandoliste;
import no.haakon.jotepad.kommando.Kommando;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;

public class KjørKommandoAction extends AbstractJotepadAction {
    public static final String COMMAND_ROOT = "kjør_kommando";

    public KjørKommandoAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<Kommando> kommandoer = frame.synligBuffer().lovligeKommandoer().collect(Collectors.toList());
        SwingUtilities.invokeLater(() -> new Kommandoliste(kommandoer, frame));

    }
}
