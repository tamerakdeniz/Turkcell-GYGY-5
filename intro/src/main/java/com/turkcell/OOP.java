package com.turkcell;

public class OOP {
    public static void main(String[] args) {

        // new => yeni bir instace oluşturma keyword'ü
        Car car1 = new Car();
        car1.brand = "Toyota";
        car1.model = "Corolla";
        car1.year = 2020;
        car1.setPricePerDay(100.0);

        System.out.println(car1.brand); // Get işlemi (değer okuma)
        System.out.println(car1.model);
        System.out.println(car1.year);
        System.out.println(car1.getPricePerDay());

    }
}
