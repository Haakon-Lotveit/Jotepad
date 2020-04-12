package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public abstract class AbstractJotepadAction extends AbstractAction {

    protected final Editor editor;
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
     * @param editor      The Editor that this action is supposed to be bound to. Note that it might <i>also</i>
     *                    be bound to the menubar. But that's more about the frame having actions injected.
     * @param shortcuts   The keyboard shortcuts you want to use. Note that there is ZERO attempt at having these be
     *                    unique. It's up to the programmer to make sure that they're unique, and that should not be a
     *                    Herculean task.
     */
    protected AbstractJotepadAction(String commandRoot, Editor editor, Stream<KeyStroke> shortcuts) {
        command = String.format("%s-%s", commandRoot, UUID.randomUUID());
        this.editor = editor;
        // While this does not actually add any shortcuts, it does tell the editor about this action, and what the command is called.
        editor.getActionMap().put(command, this);
        shortcuts.forEach(this::addShortcut);
    }

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
     * @param editor      The Editor that this action is supposed to be bound to. Note that it might <i>also</i>
     *                    be bound to the menubar. But that's more about the frame having actions injected.
     */
    protected AbstractJotepadAction(String commandRoot, Editor editor) {
        this(commandRoot, editor, Stream.empty()); // This is just a convenience for actions without direct shortcuts.
    }


    /**
     * Adds a shortcut to the editor.
     */
    private void addShortcut(KeyStroke shortcut) {
        editor.getInputMap().put(shortcut, command);
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
