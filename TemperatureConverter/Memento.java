package TemperatureConverter;

import java.io.Serializable;

public class Memento implements Serializable {
    private double cel, fah;

    public Memento(Model modelRemote) {
        this.cel = modelRemote.getCel();
        this.fah = modelRemote.getFah();
    }

    public double getCel() {
        return cel;
    }

    public double getFah() {
        return fah;
    }
}
