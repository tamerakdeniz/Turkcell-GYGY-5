package com.turkcell;

public class OOP {
    public static void main(String[] args) {

        // new => yeni bir instace oluşturma keyword'ü
        Car car1 = new Car(); // Car sınıfından bir nesne oluşturma, constructor'ı çağırma
        car1.setBrand("Toyota");
        car1.setModel("Corolla");
        car1.setYear(2020);
        car1.setPricePerDay(-300.0);

        String[] specs = {"Cam tavan", "Bebek koltuğu", "Otonom sürüş"};
        car1.setSpecs(specs);

        String[] x = car1.getSpecs();
        x[0] = "Güneş panelleri"; // specs dizisinin ilk elemanını değiştirme

        System.out.println(car1.getSpecs()[0]); // specs dizisinin ilk elemanını yazdırma
        System.out.println(car1.getBrand()); // Get işlemi (değer okuma)
        System.out.println(car1.getModel());
        System.out.println(car1.getYear());
        System.out.println(car1.getPricePerDay());

        Bike bike1 = new Bike();

        Car car2 = new Car(true, "Honda"); // Car sınıfından bir nesne oluşturma, constructor'ı çağırma
        System.out.println(car2.getBrand()); // Car sınıfının constructor'ında brand'i
    }
}
