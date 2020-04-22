package no.haakon.jotepad.actions.search;

public class NoneSearcher implements Searcher {

    public NoneSearcher(Object ignored) {}

    @Override
    public void searchForward(String term) {
        return;
    }

    @Override
    public void searchBackwards(String term) {
        return;
    }
}
