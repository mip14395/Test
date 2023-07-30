package TemperatureConverter;

import java.io.Serializable;

import javax.swing.JOptionPane;

public class C2FCommand implements Command, Serializable {

    private Model modelRemote;

    public C2FCommand(Model modelRemote) {
        this.modelRemote = modelRemote;
    }

    @Override
    public void execute() {
        String input = JOptionPane.showInputDialog(null, "Enter Celcius: ", "Celcius to Fahrenheit",
                JOptionPane.PLAIN_MESSAGE);
        if (input == null) {
            return;
        }
        double i = 0;
        try {
            i = Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a decimal!", "ERROR",
                    JOptionPane.WARNING_MESSAGE);
            execute();
        }
        modelRemote.setCel(i);
        modelRemote.setFah(modelRemote.c2f());
    }
}
