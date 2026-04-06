package com.turkcell;

// Car isminde bir type oluşturmak.
public class Car extends Vehicle { // Car, Vehicle sınıfından türetilmiş bir sınıftır.

    // erişim belirleyiciler => access modifiers - public, private, protected, default
    // kimlerin erişebileceğini belirler
    // public => herkes erişebilir
    // private => sadece sınıfın içinden erişilebilir
    // protected => sadece aynı paket içindeki sınıflardan ve alt (türetilen) sınıflardan erişilebilir
    // default => sadece aynı paket içindeki sınıflardan erişilebilir

    // Gerçek hayattaki her şeyi değil, programda kullanacağımız özelliklerini tanımlayacağız.

    private boolean hasSunroof; // Arabanın cam tavanı olup olmadığı

    private String[] specs; // Arabanın özellikleri (cam tavan, bebek koltuğu, otonom sürüş gibi)

    // ENCAPSULATION => veriyi koruma, gizleme
    public String[] getSpecs() {
        return specs.clone();
    }
    public void setSpecs(String[] specs) {
        this.specs = specs.clone();
    }
    public boolean isHasSunroof() {
        return hasSunroof;
    }
    public void setHasSunroof(boolean hasSunroof) {
        this.hasSunroof = hasSunroof;
    }

    // Değerlerini al, referansı alma.


    // setter methods => setBrand, setModel, setYear, setPricePerDay


    // getter methods => getBrand, getModel, getYear, getPricePerDay


}
