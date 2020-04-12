package no.haakon.jotepad.actions.files;

import no.haakon.jotepad.gui.components.Editor;
import no.haakon.jotepad.gui.components.ProsjektFinnFilVindu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Disse kommandoene setter en mappe til å være hjemmet til et prosjekt.
 * Dette lar editoren gjøre småkule ting, som å finne filer, søke gjennom dem alle, osv.
 */
public class ProsjektFilSøkAction extends AbstractProsjektAction {

    public static String COMMAND_ROOT = "PROSJEKT_FINN_FIL";

    /**
     * Oppretter en ProsjektFilSøkAction.
     * <p>
     * This makes inheriting from the abstract class a bit simpler, since you can specify all sorts of constructors yourself, depending on need.
     *
     * @param editor      The Editor that this action is supposed to be bound to. Note that it might <i>also</i>
     *                    be bound to the menubar. But that's more about the frame having actions injected.
     * @param shortcuts   The keyboard shortcuts you want to use. Note that there is ZERO attempt at having these be
     */
    public ProsjektFilSøkAction(Editor editor, Stream<KeyStroke> shortcuts) {
        super(COMMAND_ROOT, editor, shortcuts);
    }

    /**
     * <p>
     * This makes inheriting from the abstract class a bit simpler, since you can specify all sorts of constructors yourself, depending on need.
     *
     * @param editor      The Editor that this action is supposed to be bound to. Note that it might <i>also</i>
     *                    be bound to the menubar. But that's more about the frame having actions injected.
     */
    public ProsjektFilSøkAction(Editor editor) {
        this(editor, Stream.empty());
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(editor.getValue(NØKKEL_INDEKSERT_MAPPE) == null) {
            System.err.println("Ingen mappe indeksert, kan ikke gjøre noe enda.");
            editor.popupError("Ingen mappe satt", "Ingen mappe satt som prosjektmappe. Sett denne først.");
            return;
        }
        Set<File> gyldigeFiler = getSøkbareFiler();

        SwingUtilities.invokeLater(() -> new ProsjektFinnFilVindu(getSøkbareFiler(), editor));
    }
}
