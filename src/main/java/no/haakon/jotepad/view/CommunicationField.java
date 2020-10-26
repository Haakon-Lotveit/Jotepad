package no.haakon.jotepad.view;

import javax.swing.JTextField;

public class CommunicationField extends JTextField {
    public CommunicationField() {
        reset();
    }

    public void reset() {
        this.setEditable(false);
        this.setText("Communications field");
    }

    public void setMessage(String text) {
        this.setEditable(false);
        this.setText(text == null? "" : text);
    }
}
