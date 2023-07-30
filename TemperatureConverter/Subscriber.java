package TemperatureConverter;

public interface Subscriber {
    void setSubject(Publisher subject);

    void update(double cel, double fah);
}
