package com.turkcell;

// Car isminde bir type oluşturmak.
public class Car {

    // erişim belirleyiciler => access modifiers - public, private, protected, default
    // kimlerin erişebileceğini belirler
    // public => herkes erişebilir
    // private => sadece sınıfın içinden erişilebilir
    // protected => sadece aynı paket içindeki sınıflardan ve alt (türetilen) sınıflardan erişilebilir
    // default => sadece aynı paket içindeki sınıflardan erişilebilir

    // Gerçek hayattaki her şeyi değil, programda kullanacağımız özelliklerini tanımlayacağız.

    public String brand;
    public String model;
    public int year;

    // ENCAPSULATION => veriyi koruma, gizleme

    private double pricePerDay;

    // setter methods => setBrand, setModel, setYear, setPricePerDay
    public void setPricePerDay(double pricePerDay) {
        // this => bu sınıfın kendisi, yani Car sınıfı
        // this.pricePerDay => Car sınıfının pricePerDay özelliği
        // pricePerDay => setPricePerDay metodunun parametresi
        if (pricePerDay < 0) {
            System.out.println("Fiyat negatif olamaz.");
            pricePerDay = 0.0; // Negatif fiyatı sıfır yap  
            return;
        }
        this.pricePerDay = pricePerDay;
    }

    // getter methods => getBrand, getModel, getYear, getPricePerDay
    public double getPricePerDay() {
        return this.pricePerDay;
    }


}
