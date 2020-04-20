package no.haakon.jotepad.gui.components;

import javax.swing.*;

public enum JaNeiValg {
    JA(true), NEI(false), ANNET(false);


    private final boolean valgteJa;

    JaNeiValg(boolean valgteJa) {
        this.valgteJa = valgteJa;
    }

    public static JaNeiValg fraInt(int valg) {
        switch (valg) {
            case JOptionPane.YES_OPTION:
                return JA;
            case JOptionPane.NO_OPTION:
                return NEI;
            default:
                return ANNET;
        }
    }

    public boolean valgteJa() {
        return valgteJa;
    }
}
