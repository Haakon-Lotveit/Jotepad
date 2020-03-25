package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.Editor;

public class RegexSearcher implements Searcher {

    private final Editor editor;

    public RegexSearcher(Editor editor) {
        this.editor = editor;
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
