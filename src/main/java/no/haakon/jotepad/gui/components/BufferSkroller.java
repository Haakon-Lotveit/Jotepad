package no.haakon.jotepad.gui.components;

import no.haakon.jotepad.model.buffer.Buffer;

import javax.swing.*;

public class BufferSkroller extends JScrollPane {

    private final Buffer buffer;

    public BufferSkroller(Buffer buffer) {
        super(buffer.getComponent());
        this.buffer = buffer;
        this.createHorizontalScrollBar();
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.createVerticalScrollBar();
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setName(buffer.getUnikId());
    }

    @Override
    public String getName() {
        return buffer.getUnikId();
    }

    public Buffer getBuffer() {
        return buffer;
    }

    @Override
    public String toString() {
        return String.format("%s for %s | %s",
                             getClass().getName(),
                             buffer.getBufferNavn(),
                             buffer.getUnikId());
    }
}