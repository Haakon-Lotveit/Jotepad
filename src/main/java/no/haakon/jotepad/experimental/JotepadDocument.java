package no.haakon.jotepad.experimental;

import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JotepadDocument implements Document {

    Set<DocumentListener> documentListeners = new HashSet<>();
    Set<UndoableEditListener> undoableEditListeners = new HashSet<>();
    Map<Object, Object> properties = new HashMap<>();

    private StringBuilder content = new StringBuilder(4096);

    @Override
    public int getLength() {
        return content.length();
    }

    @Override
    public void addDocumentListener(DocumentListener listener) {
        documentListeners.add(listener);
    }

    @Override
    public void removeDocumentListener(DocumentListener listener) {
        documentListeners.remove(listener);
    }

    @Override
    public void addUndoableEditListener(UndoableEditListener listener) {
        undoableEditListeners.add(listener);
    }

    @Override
    public void removeUndoableEditListener(UndoableEditListener listener) {
        undoableEditListeners.remove(listener);
    }

    @Override
    public Object getProperty(Object key) {
        return properties.get(key);
    }

    @Override
    public void putProperty(Object key, Object value) {
        properties.put(key, value);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        try {
            content.delete(offs, len);
        } catch(StringIndexOutOfBoundsException sioobe) {
            throw new BadLocationException(sioobe.getMessage(), offs);
        }
    }

    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        content.insert(offset, str);

    }

    @Override
    public String getText(int offset, int length) throws BadLocationException {
        return null;
    }

    @Override
    public void getText(int offset, int length, Segment txt) throws BadLocationException {

    }

    @Override
    public Position getStartPosition() {
        return null;
    }

    @Override
    public Position getEndPosition() {
        return null;
    }

    @Override
    public Position createPosition(int offs) throws BadLocationException {
        return null;
    }

    @Override
    public Element[] getRootElements() {
        return new Element[0];
    }

    @Override
    public Element getDefaultRootElement() {
        return null;
    }

    @Override
    public void render(Runnable r) {

    }
}
