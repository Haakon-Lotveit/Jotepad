package no.haakon.jotepad.gui.components;

import javax.swing.*;

public  class EditorScrollPane extends JScrollPane {
    private final Editor editor;

    public EditorScrollPane(Editor editor) {
        super(editor);
        this.editor = editor;
        this.createHorizontalScrollBar();
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.createVerticalScrollBar();
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setName(editor.getId());
    }

    public Editor getEditor() {
        return editor;
    }

    @Override
    public String toString() {
        return String.format("%s for %s | %s", getClass().getName(), editor.getVennligNavn(), editor.getId());
    }
}