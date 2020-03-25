package no.haakon.jotepad.actions.search;

public interface Searcher {

    public void searchForward(String term);
    public void searchBackwards(String term);

}
