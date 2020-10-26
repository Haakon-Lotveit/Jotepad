package no.haakon.jotepad.view;

import javax.swing.JComponent;
import java.io.File;
import java.util.Optional;

/**
 * This is the very basics of what a buffer have to offer. Things like editing, etc. are not part of this inteface.
 * There will be ohter interfaces expanding on this one, but this is very much a work in progress.
 */
public interface Buffer {

    /**
     * A name that is unique to this buffer. If it's not unique, it will replace the other buffer that has the same name.
     * Note that Jotepad follows some conventions stolen from GNU Emacs:
     * <p>
     *     System buffers, such as the message buffer has its name surrounded by *, such as *message-buffer*.
     *     Users are free to create such bufferes themselves, but certain names are holy, and cannot be
     *     created.
     * </p>
     * <p>
     *     In general if a new buffer is asked for with the exact same name, it replaces the old buffer.
     *     However, whereas Emacs lets you change modes after a buffer is created, Jotepad is less agile.
     *     Jotepad considers a buffer a view of content, and how to deal with the data is a part of that view.
     *     If you open a buffer that views a CSV-file as a table, that buffer will forever view it as a table.
     *     You can *also* open the file as text, and it would be two buffers, thinking about the same file.
     *     This is different from how emacs does things, and that's fine. This is not a clone of Emacs.
     *     It just steals a lot of inspiration from it.
     * </p>
     * <p>
     *     A buffer name is not final. However, if you do change the name and don't tell the controller,
     *     all hell can be expected to break lose, since it keeps a mapping of names, and is used by frames to
     *     get hold of buffers. Therefore, you are expected to tell the controller that you've changed names.
     * </p>
     * @return the name of the buffer. Non-null, non-empty string.
     */
    String name();

    // IO based stuff. Buffers don't open files, but they might write to them. Controller loads, buffers save.

    /**
     * A buffer may (but is not obligated to) attach itself to a file. A buffer is allowed to change the file it is
     * attached to, or to stop holding on to a file. A buffer may only attach itself to one file.
     * @return if no file is attached to this buffer, an empty optional is returned. Otherwise an optional of said file.
     */
    Optional<File> saveFile();

    /**
     * This requests that the buffer saves. If it is attached to a file, it is required that it attempts to save to this
     * file directly. If there is no such file to attach to, the buffer may do what makes sense to the buffer.
     * Typically, this would be something mundane, such as asking the user for a file input, refusing to save, while
     * signalling an error, or something similar. While wild things are possible, throwing exceptions is considered
     * breaking the contract.
     */
    void save();

    // gui-stuff

    /**
     * Every buffer is required to have a component that can be rendered. This is what shows up when you have the
     * buffer in focus. It can be anything from a JTextArea to a custom component. The JotepadFrame will handle
     * scrollbars and such.
     * @return The JComponent that renders the buffer.
     */
    JComponent getComponent();

    /**
     * This method gets called by a {@link JotepadFrame} that gives this buffer focus. The frame is given as
     * an argument.
     * @param focusedFrame the Jotepad frame that gave this buffer focus in its window.
     */
    void getFocus(JotepadFrame focusedFrame);

    /**
     * This method gets called by a {@link JotepadFrame} when it takes this buffer out of focus. The frame is given
     * as an argument.
     * @param focusedFrame the Jotepad frame that removed focus from this buffer in its window.
     */
    void loseFocus(JotepadFrame focusedFrame);
}
