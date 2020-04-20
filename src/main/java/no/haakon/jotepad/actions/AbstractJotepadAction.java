package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.ApplicationFrame;

import javax.swing.*;
import java.util.UUID;

public abstract class AbstractJotepadAction extends AbstractAction {

    protected final ApplicationFrame frame;
    protected final String command;

    /**
     * This constructor is protected because Streams are not meant to be used as arguments in general.
     * However, I consider this to be fine, as a Stream is a fine lowest-common-demoninator: You can have an array, collection or a singular object represented in a stream.
     * This makes things easier.
     *
     * This makes inheriting from the abstract class a bit simpler, since you can specify all sorts of constructors yourself, depending on need.
     *
     * @param commandRoot The root of the command. Typically something like "NEW_FILE", or "SAVE",etc.
     *                    The actual command will have a UUID (v4) appended to it to make it unique.
     *                    That is, the first piece of the id is for information, the second piece is for uniqueification.
     *                    You can manually mess this up, but automatically, not.
     * @param frame       The frame serves as a sort of root node of wisdom: It can tell you things like which buffer is currently shown, and more.
     */
    protected AbstractJotepadAction(String commandRoot, ApplicationFrame frame) {
        command = String.format("%s-%s", commandRoot, UUID.randomUUID());
        this.frame = frame;
    }

    /**
     * In order to deal with keyboard combinations and similar, a command-object is needed.
     * This object acts as a key for lookup tables. Here, a String is used, because it's easy to understand in
     * debuggers and stacktraces, and because of it's immutability it's well suited for use as a key in maps.
     *
     * @return the string representing the command bound to this action.
     */
    public String getCommand() {
        return command;
    }
}
