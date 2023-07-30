package TemperatureConverter;

import java.io.Serializable;

import javax.swing.JOptionPane;

public class F2CCommand implements Command, Serializable {

    private Model modelRemote;

    public F2CCommand(Model modelRemote) {
        this.modelRemote = modelRemote;
    }

    @Override
    public void execute() {
        String input = JOptionPane.showInputDialog(null, "Enter Fahrenheit: ", "Fahrenheit to Celcius",
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
            return;
        }
        modelRemote.setFah(i);
        modelRemote.setCel(modelRemote.f2c());
    }
}
