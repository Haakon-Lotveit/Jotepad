package no.haakon.jotepad.actions.prosjekt;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.ProsjektSøkTekstVindu;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ProsjektTekstSøkAction extends AbstractProsjektAction {

    public static final String COMMAND_ROOT = "PROSJEKT_SØK_TEKST";

    public ProsjektTekstSøkAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!mappeHarBlittSatt()) {
            return;
        }

        SwingUtilities.invokeLater(() -> new ProsjektSøkTekstVindu(getIndeksertInnhold(), frame));
    }

}
