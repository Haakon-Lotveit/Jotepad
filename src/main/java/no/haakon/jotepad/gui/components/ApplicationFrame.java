package no.haakon.jotepad.gui.components;

import no.haakon.jotepad.gui.menubar.JotepadMenubar;
import no.haakon.jotepad.model.buffer.Buffer;
import no.haakon.jotepad.model.buffer.BufferFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a simple frame that sets up no.haakon.jotepad.start.Jotepad.
 */
public class ApplicationFrame extends JFrame {

    public static final String TITLE = "Jotepad";
    private Buffer currentBuffer = null;
    private final CardLayout layout;
    private final JPanel panel;
    private Map<String, String> state = new HashMap<>();
    private Map<String, Map<Class<?>, Object>> typedState = new HashMap<>();
    private BufferFactory bufferFactory;

    public ApplicationFrame() {
        super();
        layout = new CardLayout();
        panel = new JPanel(layout);
        this.add(panel);

        this.bufferFactory = new BufferFactory(this);
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

    public void addBuffer(Buffer buffer) {
        panel.add(buffer.getSkroller(), buffer.getUnikId());
        visBuffer(buffer);
    }

    public void visBuffer(Buffer buffer) {
        System.out.printf("Prøver å vise buffer '%s', med unik id '%s'%n", buffer.getBufferNavn(), buffer.getUnikId());
        layout.show(panel, buffer.getUnikId()); // Merk at id her deler navn med JSCrollPane sitt navn...
        oppdaterTittel(buffer);
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

    public Collection<Buffer> getBuffere() {
        return Arrays.stream(panel.getComponents())
                .filter(c -> c instanceof BufferSkroller)
                .map(BufferSkroller.class::cast)
                .map(BufferSkroller::getBuffer)
                .collect(Collectors.toList());
        // Ugh.
    }

    public void åpneFil(File fil, Charset charset) {
        bufferFactory.createBuffer(fil, Map.of("charset", charset))
                .ifPresentOrElse(this::addBuffer,
                                 () -> System.err.println("Kunne ikke åpne fil " + fil.getAbsolutePath()));
    }

    private void oppdaterTittel(Buffer buffer) {
         oppdaterTittel(buffer.getBufferNavn());
    }

    private void oppdaterTittel() {
        String tittel = synligBuffer().getBufferNavn();
        oppdaterTittel(tittel);
    }

    public Buffer synligBuffer() {
        return Arrays.stream(panel.getComponents())
                .filter(Component::isVisible)
                .filter(c -> c instanceof BufferSkroller)
                .map(BufferSkroller.class::cast)
                .map(BufferSkroller::getBuffer).findAny()
                .get(); // Det skal ALLTID være en og bare en som dette er gyldig for.
    }

    private void oppdaterTittel(final String suffix) {
        setTitle(String.format("%s - %s", TITLE, suffix));
    }

    public void lukkBuffer(Buffer buffer) {
        if(getBuffere().size() <= 1) {
            // det er siste, så vi må være litt varsomme...
            nyTomBuffer();
        }
        else {
            visNesteBuffer();
        }
            panel.remove(buffer.getSkroller());
    }

    public void lukkSynligBuffer() {
        lukkBuffer(synligBuffer());
    }

    public void nyTomBuffer() {
        addBuffer(bufferFactory.tomBuffer());
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
