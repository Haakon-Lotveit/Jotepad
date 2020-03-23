package no.haakon.jotepad.start;

import no.haakon.jotepad.actions.*;
import no.haakon.jotepad.actions.search.*;
import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.Editor;
import no.haakon.jotepad.gui.menubar.JotepadMenubar;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * no.haakon.jotepad.start.Jotepad er en Notepad-klon skrevet i Java.
 * Dette er ikke et komplisert program, og det har ikke noen spesielle egenskaper.
 * I beste fall er det en illustrasjon av hvordan en kan lage et enkelt program med Swing i Java.
 */
public class Jotepad {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {
            System.err.println("Kunne ikke bruke systemets innebygde look and feel");
        }
        System.out.println(UIManager.getSystemLookAndFeelClassName());
        JFrame applicationFrame = new ApplicationFrame();

        Editor editor = new Editor();

//        editingArea.getInputMap().put(aKeyStroke, aCommand);
//        editingArea.getActionMap().put(aCommmand, anAction);

        JotepadMenubar menuBar = new JotepadMenubar();
        JMenu fil = new JMenu("Fil");

        JMenuItem nyFil = new JMenuItem();
        NewFileAction newFileAction = new NewFileAction(editor, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        nyFil.setAction(newFileAction);
        nyFil.setText("Ny...");
        fil.add(nyFil);

        JMenuItem åpne = new JMenuItem();
        åpne.setAction(new LoadFileAction(editor, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)));
        åpne.setText("Åpne fil");
        fil.add(åpne);

        JMenuItem lagre = new JMenuItem();
        lagre.setAction(new SaveFileAction(editor, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)));
        lagre.setText("Lagre");
        fil.add(lagre);

        JMenuItem lagreSom = new JMenuItem();
        lagreSom.setAction(new SaveAsAction(editor, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)));
        lagreSom.setText("Lagre som");
        fil.add(lagreSom);

        JMenuItem avslutt = new JMenuItem();
        avslutt.setAction(new ExitAction(editor, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)));
        avslutt.setText("Avslutt");
        fil.add(avslutt);

        menuBar.add(fil);

        JMenu rediger = new JMenu("Rediger");

        JMenuItem enkeltSøk = new JMenuItem();
        enkeltSøk.setAction(new FindTextAction(editor, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)));
        enkeltSøk.setText("Søk");
        rediger.add(enkeltSøk);

        menuBar.add(rediger);

        JMenu søking = new JMenu("Søking");

        JMenuItem setSøketerm = new JMenuItem();
        setSøketerm.setAction(new SetSearchTermAction(editor, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)));
        setSøketerm.setText("Sett søketerm [enkelt søk]");
        søking.add(setSøketerm);

        JMenuItem visSøketerm = new JMenuItem();
        visSøketerm.setAction(new ShowSearchTermAction(editor, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK)));
        visSøketerm.setText("Vis søketerm");
        søking.add(visSøketerm);

        JMenuItem søkForrige = new JMenuItem();
        søkForrige.setAction(new FindPreviousAction(editor, KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK)));
        søkForrige.setText("Finn forrige");
        søking.add(søkForrige);

        JMenuItem søkNeste = new JMenuItem();
        søkNeste.setAction(new FindNextAction(editor, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK)));
        søkNeste.setText("Finn neste");
        søking.add(søkNeste);

        menuBar.add(søking);

        applicationFrame.setJMenuBar(menuBar);
        applicationFrame.getContentPane().add(editor.getInScrollPane());
        applicationFrame.pack();
        applicationFrame.setVisible(true);


    }
}
