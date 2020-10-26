package no.haakon.jotepad.old.actions.search;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;

public class ShowSearchTermAction extends AbstractSearchAction {

    public static String COMMAND_ROOT = "VIS-SØKETERM";

    public ShowSearchTermAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String searchTerm = getSearchTerm();
        if(searchTerm == null) {
            frame.synligBuffer().guiHelpers().popupInfo("Søketerm", "Ingen søketerm har blitt satt");
            return;
        }

        frame.synligBuffer().guiHelpers().popupInfo("Søketerm", "Søketerm: " + searchTerm);
    }
}
