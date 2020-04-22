package no.haakon.jotepad.gui.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import static java.lang.Integer.max;

public abstract class NarrowingInputList<T> {
    protected final Collection<T> listen;
    private final String opprinneligTittel;
    protected final ApplicationFrame frame;
    protected JList<ListRepresentation<T>> nåværendeUtvalg;
    protected JFrame vindu;

    // Lys, Kamera, ACTION!
    private final ActionListener reagerPåEnter = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            gjørValg();
        }
    };

    private final DocumentListener reagerPåEndringer = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            react(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            react(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            react(e);
        }

        private void react(DocumentEvent e) {
            try {
                String tekst = e.getDocument().getText(0, e.getDocument().getLength());
                oppdaterUtvalg(tekst);
            } catch (BadLocationException ble) {
                System.err.println("Kunne ikke hente ut teksten fra søkefeltet, noe som aldri burde skje.");
            }
        }
    };

    // håndterer at vi klikker på listen.
    private final MouseListener håndterLister = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() > 1) { // Hvis du dobbeltklikker eller mer. Merk at hva som er et dobbeltklikk er opp til OS-et.
                gjørValg();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            return;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            return;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            return;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            return;
        }
    };

    public NarrowingInputList(String tittel, Collection<T> elementerTilListen, ApplicationFrame frame) {
        this.listen = elementerTilListen;
        this.opprinneligTittel = tittel;
        this.frame = frame;
        init(frame);
    }

    /**
     * Når denne metoden blir kalt er en ferdig med å bruke listen. Et valg har blitt tatt, og basert på valget, skal noe skje.
     * Hva som skal skje er opp til implementasjonen.
     */
    protected abstract void gjørValg();

    /**
     * Sjekker om et element av type T matcher mot et regulært uttrykk.
     * Dvs. at en implementasjon av denne metoden vil motta et regulært uttrykk, og skal sjekke om
     * elementet matcher dette. Elementet må derfor mappes om til en stringverdi først.
     * Hvilken stringverdi som skal brukes, og hvordan denne skal genereres må avgjøres av implementasjonen.
     * @param mønster et regulært uttrykk. Vil bli generert basert på brukerens inndata. Vil alltid være et gyldig regulært uttrykk, og aldri null.
     * @param element elementet som skal testes mot. Elementet vil alltid være medlem av den opprinnelige samlingen som ble oppgitt til denne klassen.
     * @return true hviss elementets strengrepresentasjon matches av mønsteret, og false ellers.
     */
    protected abstract boolean matchMotMønster(Pattern mønster, T element);

    /**
     * Strengen her er den som blir vist i listen per element.
     * Dersom toString er greit nok, har du lov til å bvare returnere elementets toString-metode.
     * Dersom du ikke gir inn null-verdier i den initielle samlingen er dette garantert aldri null.
     * @param element elementet som skal ha en strengrepresentasjon til listen.
     * @return en brukervennlig tekstsnutt som representerer dette objektet. (Stien til en fil til dømes).
     */
    protected abstract String stringRepresentasjon(T element);


    protected void lukkVindu() {
        vindu.dispose();
    }

    public void oppdaterUtvalg(String tekst) {
        if(tekst.length() < 3) { // TODO: Dette tallet er helt tilfeldig valgt. Det virker som en litt dårlig ide å sette det fast her, men det får vente til en annen gang...
            return;
        }

        Pattern mønster;
        try {
            mønster = Pattern.compile(tekst);
        } catch(PatternSyntaxException ignored) {
            System.err.println("Syntaksfeil i regex, oppdaterer ikke liste for tekst '" + tekst + "'");
            return;
        }

        DefaultListModel<ListRepresentation<T>> nyListModel = new DefaultListModel<>();
        listen.stream()
                .filter(element -> tekst.isEmpty() || matchMotMønster(mønster, element))
                .map(this::listRepresentation)
                .sorted()
                .forEach(nyListModel::addElement);

        nåværendeUtvalg.setModel(nyListModel);
    }


    protected void init(JFrame hovedVindu) {
        vindu = new JFrame(opprinneligTittel);
        JPanel panel = new JPanel();
        vindu.add(panel);
        BoxLayout manager = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(manager);

        JTextField søkefelt = new JTextField(40);


        DefaultListModel<ListRepresentation<T>> listModel = new DefaultListModel<>();
        listModel.addAll(listen.stream().map(this::listRepresentation).collect(Collectors.toList()));
        nåværendeUtvalg = new JList<>(listModel);
        JScrollPane skroller = new JScrollPane();
        skroller.setViewportView(nåværendeUtvalg);
        nåværendeUtvalg.setLayoutOrientation(JList.VERTICAL);

        // setter felles bredde
        final int commonWidth = max(søkefelt.getPreferredSize().width, nåværendeUtvalg.getPreferredSize().width);
        Dimension søkefeltStørrelse = søkefelt.getPreferredSize();
        søkefeltStørrelse.width = commonWidth;
        søkefelt.setMinimumSize(søkefeltStørrelse);

        Dimension størrelse = nåværendeUtvalg.getPreferredSize();
        størrelse.width = commonWidth;
        nåværendeUtvalg.setMinimumSize(størrelse);
        nåværendeUtvalg.setFixedCellWidth(commonWidth);

        panel.add(søkefelt);
        panel.add(skroller);

        panel.setSize(new Dimension(commonWidth, søkefeltStørrelse.height + størrelse.height));
        vindu.pack();
        vindu.setVisible(true);
        vindu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        søkefelt.getDocument().addDocumentListener(reagerPåEndringer);
        søkefelt.addActionListener(reagerPåEnter);
        nåværendeUtvalg.addMouseListener(håndterLister);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        nåværendeUtvalg.getInputMap().put(enter, "ENTER");
        nåværendeUtvalg.getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gjørValg();
            }
        });

        søkefelt.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "FOKUS_LISTE");
        søkefelt.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "FOKUS_LISTE");
        søkefelt.getActionMap().put("FOKUS_LISTE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Gir fokus til listen");
                if(nåværendeUtvalg.getSelectedIndex() < 0) {
                    nåværendeUtvalg.setSelectedIndex(0);
                }
                nåværendeUtvalg.requestFocus();
            }
        });


        // putter dette vinduet i sentrum av det andre vinduet.
        int xPosisjon = ((hovedVindu.getWidth() / 2) - (vindu.getWidth() / 2)) + hovedVindu.getLocationOnScreen().x; // Velkommen til gamle dager, da dette her med get og set var litt sånn nytt og vakkert...
        int yPosisjon = ((hovedVindu.getHeight() / 2) - (vindu.getHeight() / 2)) + hovedVindu.getLocationOnScreen().y;

        vindu.setLocation(xPosisjon, yPosisjon);
    }


    private ListRepresentation<T> listRepresentation(T element) {
        return new ListRepresentation<>(element, stringRepresentasjon(element));
    }

    public class ListRepresentation<T> implements Comparable<ListRepresentation<T>> {
        public ListRepresentation(T element, String representasjon) {
            this.element = element;
            this.representasjon = representasjon;
        }

        public final T element;
        public final String representasjon;

        @Override
        public String toString() {
            return representasjon;
        }

        public T getElement() {
            return element;
        }

        @Override
        public int compareTo(ListRepresentation<T> o) {
            return this.toString().compareTo(o.toString());
        }
    }

}
