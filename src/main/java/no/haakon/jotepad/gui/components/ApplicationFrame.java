package no.haakon.jotepad.gui.components;

import no.haakon.jotepad.gui.menubar.JotepadMenubar;
import no.haakon.jotepad.model.Buffer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a simple frame that sets up no.haakon.jotepad.start.Jotepad.
 */
public class ApplicationFrame extends JFrame {

    public static final String TITLE = "Jotepad";
    private Map<String, Buffer> buffere = new TreeMap<>();
    private Buffer currentBuffer = null;
    private final CardLayout layout;
    private final JPanel panel;
    private Map<String, String> state = new HashMap<>();
    private Map<String, Map<Class<?>, Object>> typedState = new HashMap<>();

    public ApplicationFrame() {
        super();
        layout = new CardLayout();
        panel = new JPanel(layout);
        this.add(panel);

        nyTomBuffer();

        setJMenuBar(new JotepadMenubar(this));

        oppdaterTittel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1366, 740));
        this.pack();
    }

    public void sentrerPåSkjerm() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    public void addEditor(Editor editor) {
        panel.add(editor.getInScrollPane(), editor.getId());
        visEditor(editor);
    }

    public void visEditor(Editor editor) {
        System.out.printf("Prøver å vise editor '%s', med unik id '%s'%n", editor.getVennligNavn(), editor.getId());
        layout.show(panel, editor.getId()); // Merk at id her deler navn med JSCrollPane sitt navn...
        oppdaterTittel(editor);
    }

    public void visForrigeBuffer() {
        layout.previous(panel);
        oppdaterTittel();
    }

    public void visNesteBuffer() {
        layout.next(panel);
        oppdaterTittel();
    }

    public void visFørsteBuffer() {
        layout.first(panel);
        oppdaterTittel();
    }

    public void visSisteBuffer() {
        layout.last(panel);
        oppdaterTittel();
    }

    public void visBufferMedNavn(final String navn) {
        layout.show(panel, navn);
    }

    public Collection<Editor> getBuffere() {
        return Arrays.stream(panel.getComponents())
                .filter(c -> c instanceof EditorScrollPane)
                .map(EditorScrollPane.class::cast)
                .map(EditorScrollPane::getEditor)
                .collect(Collectors.toList());
        // Ugh.
    }

    public void nyEditorForFil(File fil, Charset charset) {
        Editor editor = new Editor(this);
        editor.loadFile(fil, charset);
        addEditor(editor);
    }

    private void oppdaterTittel(Editor editor) {
         oppdaterTittel(editor.getVennligNavn());
    }

    private void oppdaterTittel() {
        String tittel = synligBuffer().getVennligNavn();
        oppdaterTittel(tittel);
    }

    public Editor synligBuffer() {
        return Arrays.stream(panel.getComponents())
                .filter(Component::isVisible)
                .filter(c -> c instanceof EditorScrollPane)
                .map(EditorScrollPane.class::cast)
                .map(EditorScrollPane::getEditor).findAny()
                .get(); // Det skal ALLTID være en og bare en som dette er gyldig for.
    }

    private void oppdaterTittel(final String suffix) {
        setTitle(String.format("%s - %s", TITLE, suffix));
    }

    public void lukkBuffer(Editor editor) {
        if(getBuffere().size() <= 1) {
            // det er siste, så vi må være litt varsomme...
            nyTomBuffer();
        }
        else {
            visNesteBuffer();
        }
            panel.remove(editor.getInScrollPane());
    }

    public void lukkSynligBuffer() {
        lukkBuffer(synligBuffer());
    }

    public void nyTomBuffer() {
        Editor tom = new Editor(this);
        this.addEditor(tom);
    }

    // Håndterer state som ymse kommandoer og lignende trenger å sette...


    public ApplicationFrame setValue(String key, String value) {
        state.put(key, value);
        return this;
    }


    public String getValue(String key) {
        return state.get(key);
    }

    // Dette er gjort i Effective Java et sted. Jeg trenger bare et sted å dumpe noe global state før Alex Jones ser det.
    // WHUH? GLOBALISM?! AAAAARG! - Alex Jones
    public <T> ApplicationFrame setTypedObject(Class<T> klasse, String navn, T objekt) {
//        private final Map<String, Map<Class<?>, Object>> objekter;
        typedState.computeIfAbsent(navn, ignored -> new HashMap<>()).put(klasse, objekt);
        return this;
    }
    // Dette er gjort i Effective Java et sted. Jeg trenger bare et sted å dumpe noe global state før Alex Jones ser det.
    // WHUH? GLOBALISM?! AAAAARG! - Alex Jones
    public <T> T getTypedObject(Class<T> klasse, String navn) {
        // garantert å være av type T gitt at eneste skriving skjer via setTypedObject.
        return (T) typedState.getOrDefault(navn, new HashMap<>()).get(klasse);
    }

}
