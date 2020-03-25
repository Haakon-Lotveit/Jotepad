package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.Editor;

public class NoneSearcher implements Searcher {

    public NoneSearcher(Editor ignored) {}

    @Override
    public void searchForward(String term) {
        return;
    }

    @Override
    public void searchBackwards(String term) {
        return;
    }
}
