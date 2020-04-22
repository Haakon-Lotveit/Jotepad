package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.model.buffer.Buffer;

public class RegexSearcher implements Searcher {

    private final Buffer buffer;

    public RegexSearcher(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void searchForward(String term) {
        throw new UnsupportedOperationException("unimplemented");
    }

    @Override
    public void searchBackwards(String term) {
        throw new UnsupportedOperationException("unimplemented");
    }
}
