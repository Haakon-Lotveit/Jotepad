package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.TextEditorPane;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public abstract class AbstractJotepadAction extends AbstractAction {

    protected final TextEditorPane editor;
    protected final String command;

    /**
     * This constructor is private because Streams are not meant to be used as arguments in general.
     * However, I consider this to be fine, since the Stream objects are in this case guaranteed to be created
     * just for this method, and therefore there's no insanity going on anywhere.
     * <p>
     * This is the big constructor that actually does the work, the others just delegate to this.
     *
     * @param commandRoot The root of the command. Typically something like "NEW_FILE", or "SAVE",etc.
     *                    The actual command will have a UUID (v4) appended to it to make it unique.
     *                    That is, the first piece of the id is for information, the second piece is for uniqueification.
     *                    You can manually mess this up, but automatically, not.
     * @param editor      The TextEditorPane that this action is supposed to be bound to. Note that it might <i>also</i>
     *                    be bound to the menubar. But that's more about the frame having actions injected.
     * @param shortcuts   The keyboard shortcuts you want to use. Note that there is ZERO attempt at having these be
     *                    unique. It's up to the programmer to make sure that they're unique, and that should not be a
     *                    Herculean task.
     */
    private AbstractJotepadAction(String commandRoot, TextEditorPane editor, Stream<KeyStroke> shortcuts) {
        command = String.format("%s-%s", commandRoot, UUID.randomUUID());
        this.editor = editor;
        // While this does not actually add any shortcuts, it does tell the editor about this action, and what the command is called.
        editor.getActionMap().put(command, this);
        shortcuts.forEach(this::addShortcut);
    }

    /**
     * Creates a new AbstractJotepadAction.
     * @param commandRoot The root of the command. Typically something like "NEW_FILE", or "SAVE",etc.
     *                    The actual command will have a UUID (v4) appended to it to make it unique.
     *                    That is, the first piece of the id is for information, the second piece is for uniqueification.
     *                    You can manually mess this up, but automatically, not.
     * @param editor      The TextEditorPane that this action is supposed to be bound to. Note that it might <i>also</i>
     *                    be bound to the menubar. But that's more about the frame having actions injected.
     */
    public AbstractJotepadAction(String commandRoot, TextEditorPane editor) {
        this(commandRoot, editor, Stream.empty());
    }

    /**
     * Creates a new AbstractJotepadAction
     * @param commandRoot The root of the command. Typically something like "NEW_FILE", or "SAVE",etc.
     *                    The actual command will have a UUID (v4) appended to it to make it unique.
     *                    That is, the first piece of the id is for information, the second piece is for uniqueification.
     *                    You can manually mess this up, but automatically, not.
     * @param editor      The TextEditorPane that this action is supposed to be bound to. Note that it might <i>also</i>
     *                    be bound to the menubar. But that's more about the frame having actions injected.
     * @param shortcuts   The keyboard shortcuts you want to use. Note that there is ZERO attempt at having these be
     *                    unique. It's up to the programmer to make sure that they're unique, and that should not be a
     *                    Herculean task.
     */
    public AbstractJotepadAction(String commandRoot, TextEditorPane editor, KeyStroke[] shortcuts) {
        this(commandRoot, editor, stream(shortcuts));
    }

    /**
     * Creates a new AbstractJotepadAction
     * @param commandRoot The root of the command. Typically something like "NEW_FILE", or "SAVE",etc.
     *                    The actual command will have a UUID (v4) appended to it to make it unique.
     *                    That is, the first piece of the id is for information, the second piece is for uniqueification.
     *                    You can manually mess this up, but automatically, not.
     * @param editor      The TextEditorPane that this action is supposed to be bound to. Note that it might <i>also</i>
     *                    be bound to the menubar. But that's more about the frame having actions injected.
     * @param shortcuts   The keyboard shortcuts you want to use. Note that there is ZERO attempt at having these be
     *                    unique. It's up to the programmer to make sure that they're unique, and that should not be a
     *                    Herculean task.
     */
    public AbstractJotepadAction(String commandRoot, TextEditorPane editor, Collection<KeyStroke> shortcuts) {
        this(commandRoot, editor, shortcuts.stream());
    }

    public AbstractJotepadAction(String commandRoot, TextEditorPane editor, KeyStroke shortcut) {
        this(commandRoot, editor, Stream.of(shortcut));
    }

    /**
     * Adds a shortcut to the editor.
     * @param shortcut
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
