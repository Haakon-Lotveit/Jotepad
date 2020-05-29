package no.haakon.jotepad.model.editor;

import no.haakon.jotepad.model.buffer.Buffer;

/**
 * An editor for buffers who do not edit anything themselves.
 */
public class NoOpEditor extends Editor {
    private final Buffer buffer;

    public NoOpEditor(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    protected Buffer getBuffer() {
        return buffer;
    }

    @Override
    public void lagre() {
        return;
    }

    @Override
    public void undo() {
        return;
    }
}
