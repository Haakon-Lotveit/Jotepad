package no.haakon.jotepad.model.editor;

/**
 * En editor er en klasse som tilbyr forskjellige kommandoer som kan utføres på et dokument det er assosiert med.
 * Forskjellige editorer tilbyr forskjellige sett med kommandoer. En editor for en tekstfil vil la deg søke gjennom tekst. en editor for et bilde vil ikke kunne gjøre dette.
 * Hele denne klassen er ikke ferdig designet enda.
 */
public abstract class Editor {

    public abstract void lagre();

    public abstract void undo();
}
