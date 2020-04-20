package no.haakon.jotepad.actions.prosjekt;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.ProsjektFinnFilVindu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Set;

/**
 * Disse kommandoene setter en mappe til å være hjemmet til et prosjekt.
 * Dette lar editoren gjøre småkule ting, som å finne filer, søke gjennom dem alle, osv.
 */
public class ProsjektFilSøkAction extends AbstractProsjektAction {

    public static String COMMAND_ROOT = "PROSJEKT_FINN_FIL";

    public ProsjektFilSøkAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (!mappeHarBlittSatt()) {
            return;
        }
        Set<File> gyldigeFiler = getSøkbareFiler();

        SwingUtilities.invokeLater(() -> new ProsjektFinnFilVindu(getSøkbareFiler(), frame.synligBuffer()));
    }

}
