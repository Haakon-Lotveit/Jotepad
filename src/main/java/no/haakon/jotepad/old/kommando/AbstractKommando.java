package no.haakon.jotepad.old.kommando;

public abstract class AbstractKommando implements Kommando {

    @Override
    public String toString() {
        return navn();
    }
}
