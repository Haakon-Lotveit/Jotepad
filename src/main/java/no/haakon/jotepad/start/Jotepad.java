package no.haakon.jotepad.start;

import no.haakon.jotepad.actions.*;
import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.TextEditorPane;
import no.haakon.jotepad.gui.menubar.JotepadMenubar;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Optional;

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

        TextEditorPane editingArea = new TextEditorPane();

//        editingArea.getInputMap().put(aKeyStroke, aCommand);
//        editingArea.getActionMap().put(aCommmand, anAction);

        JotepadMenubar menuBar = new JotepadMenubar();
        JMenu fil = new JMenu("Fil");


        JMenuItem nyFil = new JMenuItem();
        NewFileAction newFileAction = new NewFileAction(editingArea, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        nyFil.setAction(newFileAction);
        nyFil.setText("Ny...");
        fil.add(nyFil);

        JMenuItem åpne = new JMenuItem();
        åpne.setAction(new LoadFileAction(editingArea, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)));
        åpne.setText("Åpne fil");
        fil.add(åpne);

        JMenuItem lagre = new JMenuItem();
        lagre.setAction(new SaveFileAction(editingArea, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)));
        lagre.setText("Lagre");
        fil.add(lagre);

        JMenuItem lagreSom = new JMenuItem();
        lagreSom.setAction(new SaveAsAction(editingArea, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)));
        lagreSom.setText("Lagre som");
        fil.add(lagreSom);

        JMenuItem avslutt = new JMenuItem();
        avslutt.setAction(new ExitAction(editingArea, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)));
        avslutt.setText("Avslutt");
        fil.add(avslutt);

        menuBar.add(fil);

        applicationFrame.setJMenuBar(menuBar);
        applicationFrame.getContentPane().add(editingArea.getInScrollPane());
        applicationFrame.pack();
        applicationFrame.setVisible(true);


    }
}
