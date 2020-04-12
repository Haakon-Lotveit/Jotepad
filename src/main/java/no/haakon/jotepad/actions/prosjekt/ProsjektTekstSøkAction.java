package no.haakon.jotepad.actions.prosjekt;

import no.haakon.jotepad.gui.components.Editor;
import no.haakon.jotepad.gui.components.ProsjektSøkTekstVindu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;

public class ProsjektTekstSøkAction extends AbstractProsjektAction {

    public static final String COMMAND_ROOT = "PROSJEKT_SØK_TEKST";

    /**
     * This constructor is protected because Streams are not meant to be used as arguments in general.
     * However, I consider this to be fine, as a Stream is a fine lowest-common-demoninator: You can have an array, collection or a singular object represented in a stream.
     * This makes things easier.
     * <p>
     * This makes inheriting from the abstract class a bit simpler, since you can specify all sorts of constructors yourself, depending on need.
     *
     * @param editor    The Editor that this action is supposed to be bound to. Note that it might <i>also</i>
     *                  be bound to the menubar. But that's more about the frame having actions injected.
     * @param shortcuts The keyboard shortcuts you want to use. Note that there is ZERO attempt at having these be
     */
    public ProsjektTekstSøkAction(Editor editor, Stream<KeyStroke> shortcuts) {
        super(COMMAND_ROOT, editor, shortcuts);
    }

    /**
     * This constructor is protected because Streams are not meant to be used as arguments in general.
     * However, I consider this to be fine, as a Stream is a fine lowest-common-demoninator: You can have an array, collection or a singular object represented in a stream.
     * This makes things easier.
     * <p>
     * This makes inheriting from the abstract class a bit simpler, since you can specify all sorts of constructors yourself, depending on need.
     *
     * @param editor The Editor that this action is supposed to be bound to. Note that it might <i>also</i>
     *               be bound to the menubar. But that's more about the frame having actions injected.
     */
    public ProsjektTekstSøkAction(Editor editor) {
        this(editor, Stream.empty());
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (!mappeHarBlittSatt()) {
            return;
        }

        SwingUtilities.invokeLater(() -> new ProsjektSøkTekstVindu(getIndeksertInnhold(), editor));
    }

}
