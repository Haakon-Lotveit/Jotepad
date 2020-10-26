package no.haakon.jotepad.old.gui.components.inputlister;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Integer.max;

public abstract class SimpleNarrowingInputList<T> {
    protected final Collection<T> listen;
    private final String opprinneligTittel;
    protected final ApplicationFrame frame;
    protected JList<ListRepresentation<T>> nåværendeUtvalg;
    protected JFrame vindu;
    protected JTextField søkefelt;

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
            reagerPåInput(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            reagerPåInput(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            reagerPåInput(e);
        }
    };

    // håndterer at vi klikker på listen.
    private final MouseListener håndterLister = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 1) { // Hvis du dobbeltklikker eller mer. Merk at hva som er et dobbeltklikk er opp til OS-et.
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

    public SimpleNarrowingInputList(String tittel, Collection<T> elementerTilListen, ApplicationFrame frame) {
        this.listen = elementerTilListen;
        this.opprinneligTittel = tittel;
        this.frame = frame;
        init(frame);
    }

    /**
     * Når det blir skrevet i inputboksen, må en reagere.
     *
     * @param documentEvent hendelsen i inputboksen.
     */
    protected void reagerPåInput(DocumentEvent documentEvent) {
        if(søkefelt.getText().length() >= oppdateringGrenseverdi()) {
            oppdaterUtvalg(søkefelt.getText());
        }
    }

    /**
     * Når denne metoden blir kalt er en ferdig med å bruke listen. Et valg har blitt tatt, og basert på valget, skal noe skje.
     * Hva som skal skje er opp til implementasjonen.
     */
    protected abstract void gjørValg();

    /**
     * Strengen her er den som blir vist i listen per element.
     * Dersom toString er greit nok, har du lov til å bvare returnere elementets toString-metode.
     * Dersom du ikke gir inn null-verdier i den initielle samlingen er dette garantert aldri null.
     *
     * @param element elementet som skal ha en strengrepresentasjon til listen.
     * @return en brukervennlig tekstsnutt som representerer dette objektet. (Stien til en fil til dømes).
     */
    protected abstract String stringRepresentasjon(T element);


    protected void lukkVindu() {
        vindu.dispose();
    }

    protected abstract void oppdaterUtvalg(String tekst);


    /**
     * Hvor mange tegn må der være før en kjører en oppdatering? På små lister er det ikke et meningsfylt spørsmål.
     * På større lister derimot, slik som for tekstsøk i flere filer, gi dette mening å spesifisere.
     */
    protected abstract int oppdateringGrenseverdi();

    protected void init(JFrame hovedVindu) {
        vindu = new JFrame(opprinneligTittel);
        JPanel panel = new JPanel();
        vindu.add(panel);
        BoxLayout manager = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(manager);

        this.søkefelt = new JTextField(40);


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
                if (nåværendeUtvalg.getSelectedIndex() < 0) {
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

    /**
     * Dersom et element i listen er valgt, eller det bare er et element igjen i listen, vil dette elementet bli returnert i en Optional.
     * Ellers vil en tom optional bli returnert. Dette er en hjelpemetode for å gjøre ting lettere.
     *
     * @return en Optional, som hvis det finnes et logisk valgt element (enten eksplisitt, eller det bare er et element igjen) inneholder elementet.
     * Hvis ikke returneres en tom optional.
     */
    public Optional<T> getValgtElement() {
        // Hvis det bare er et element igjen.
        if (nåværendeUtvalg.getModel().getSize() == 1) {
            return Optional.of(nåværendeUtvalg.getModel().getElementAt(0).getElement());
        }

        // Hvis ikke noen elementer er valgt.
        if (nåværendeUtvalg.getSelectedIndex() < 0) {
            return Optional.empty();
        }

        return Optional.of(nåværendeUtvalg.getSelectedValue().getElement());
    }

    /**
     * Dette er bare støttekode for å gjøre det lettere å vise ting i en liste på et fornuftig vis.
     * Siden toString ikke alltid er en god måte å gjøre ting på, lager vi en ny metode som holder på et objekt, og bruker stringRepresentasjon-metoden,
     * for å generere en streng som representerer elementet. Det er fullstendig lov å definere strengrepresentasjon som
     * return e.toString(), men det er ikke alltid passende.
     */
    protected ListRepresentation<T> listRepresentation(T element) {
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
