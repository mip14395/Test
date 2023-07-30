package TemperatureConverter;

public interface Publisher {

    void attach(Subscriber observer);

    void detach(Subscriber observer);

    void notifyObservers();
}
