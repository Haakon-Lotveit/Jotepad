package no.haakon.jotepad.old.gui.components;

import no.haakon.jotepad.old.model.buffer.Buffer;

import javax.swing.*;

public class BufferSkroller extends JScrollPane {

    private final Buffer buffer;

    public BufferSkroller(Buffer buffer) {
        super(buffer.getComponent(),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.buffer = buffer;
        this.setName(buffer.getUnikId());
    }

    @Override
    public String getName() {
        // Trengs i linuxversjoner av programmet.
        return buffer == null ? "" : buffer.getUnikId();
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