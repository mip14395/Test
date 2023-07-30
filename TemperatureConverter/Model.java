package TemperatureConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model implements Publisher, Serializable {
    private double cel, fah;
    List<View> observers = new ArrayList<>();

    public double getCel() {
        return cel;
    }

    public void setCel(double cel) {
        this.cel = cel;
    }

    public double getFah() {
        return fah;
    }

    public void setFah(double fah) {
        this.fah = fah;
    }

    public double c2f() {
        this.fah = cel * 9.0 / 5.0 + 32.0;
        this.fah = Math.round(fah * 10000000000000.0) / 10000000000000.0;
        notifyObservers();
        return this.fah;
    }

    public double f2c() {
        this.cel = (fah - 32.0) / 9.0 * 5.0;
        this.cel = Math.round(cel * 10000000000000.0) / 10000000000000.0;
        notifyObservers();
        return this.cel;
    }

    @Override
    public void attach(Subscriber observer) {
        if (!this.observers.contains(observer)) {
            observers.add((View) observer);
        }
    }

    @Override
    public void detach(Subscriber observer) {
        if (this.observers.contains(observer)) {
            observers.remove(observers.indexOf(observer));
        }
    }

    @Override
    public void notifyObservers() {
        for (Subscriber observer : observers) {
            observer.update(this.cel, this.fah);
        }
    }

    public void getFromMemento(Memento memento) {
        this.cel = memento.getCel();
        this.fah = memento.getFah();
    }

    public Memento saveToMemento() {
        return new Memento(this);
    }
}
