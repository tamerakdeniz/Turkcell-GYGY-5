package com.turkcell;

public class OOP {
    public static void main(String[] args) {

        // new => yeni bir instace oluşturma keyword'ü
        Car car1 = new Car();
        car1.setBrand("Toyota");
        car1.setModel("Corolla");
        car1.setYear(2020);
        car1.setPricePerDay(-300.0);

        System.out.println(car1.getBrand()); // Get işlemi (değer okuma)
        System.out.println(car1.getModel());
        System.out.println(car1.getYear());
        System.out.println(car1.getPricePerDay());

    }
}
