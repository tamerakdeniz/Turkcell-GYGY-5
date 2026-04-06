package com.turkcell;

public interface Interfaces {
    public static void main(String[] args) {
        CarRepository carRepository = new PgCarRepository(); // Sol taraf CarRepository kurallarına uyan somut bir class, sağ taraf ise bu kurallara uyan bir nesne.

        carRepository.add(new Car(true, "BMW"));
        carRepository.add(new Car(false, "Mercedes"));
        carRepository.add(new Car(true, "Audi"));

    }
}
