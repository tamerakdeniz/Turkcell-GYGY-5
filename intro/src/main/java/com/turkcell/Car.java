package com.turkcell;

public class Car {

    public int year;
    public String model;
    public String brand;
    // erişim belirleyiciler => access modifiers - public, private, protected, default
    // kimlerin erişebileceğini belirler

    // public => herkes erişebilir
    // private => sadece sınıfın içinden erişilebilir
    // protected => sadece aynı paket içindeki sınıflardan ve alt (türetilen) sınıflardan erişilebilir
    // default => sadece aynı paket içindeki sınıflardan erişilebilir
    public double price;


}
