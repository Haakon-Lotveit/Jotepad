package no.haakon.jotepad.old.actions.kommando;

import no.haakon.jotepad.old.actions.AbstractJotepadAction;
import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.gui.components.inputlister.Kommandoliste;
import no.haakon.jotepad.old.kommando.Kommando;

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
