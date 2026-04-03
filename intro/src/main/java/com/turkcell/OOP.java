package com.turkcell;

public class OOP {
    public static void main(String[] args) {
        // car1 => car instance'ı
        Car car1 = new Car(); // new => yeni bir instace oluşturma keyword'ü
        car1.year = 2020;
        car1.model = "Superb";
        car1.brand = "Skoda";
        car1.price = 400000; // set
        System.out.println(car1.price); // get
    }
}
