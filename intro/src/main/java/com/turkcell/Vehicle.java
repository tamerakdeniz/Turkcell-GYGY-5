package com.turkcell;

// Vehicle isminde bir type oluşturmak.
// Araç katorisine giren tüm nesnelerin ortak özelliklerini ve davranışlarını tanımlayacağımız bir sınıf

public class Vehicle {

    private String brand;
    private String model;
    private int year;
    private double pricePerDay;

    public void setPricePerDay(double pricePerDay) {
        // this => bu sınıfın kendisi, yani Car sınıfı
        // this.pricePerDay => Car sınıfının pricePerDay özelliği
        // pricePerDay => setPricePerDay metodunun parametresi
        if (pricePerDay < 0) {
            System.out.println("Fiyat negatif olamaz.");
            pricePerDay = 0.0; // Negatif fiyatı sıfır yaparak düzeltme
        }
        this.pricePerDay = pricePerDay;
    }

        public double getPricePerDay() {
        return this.pricePerDay;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }


}
