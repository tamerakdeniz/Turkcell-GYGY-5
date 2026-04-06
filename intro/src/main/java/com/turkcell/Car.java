package com.turkcell;

// Car isminde bir type oluşturmak.
public class Car extends Vehicle { // Car, Vehicle sınıfından türetilmiş bir sınıftır.
    /*
    erişim belirleyiciler => access modifiers - public, private, protected, default
    kimlerin erişebileceğini belirler
    public => herkes erişebilir
    private => sadece sınıfın içinden erişilebilir
    protected => sadece aynı paket içindeki sınıflardan ve alt (türetilen) sınıflardan erişilebilir
    default => sadece aynı paket içindeki sınıflardan erişilebilir
    Gerçek hayattaki her şeyi değil, programda kullanacağımız özelliklerini tanımlayacağız.
    */

    /* Constructor => Yapıcı method, yazmasanız bile bir tane vardır, default constructor denir, parametresizdir, gövdesi boştur. Car() şeklindedir.
    Eğer bir constructor yazarsanız, default constructor ortadan kalkar, artık Car() şeklinde bir constructor olmaz, parametreli constructor'lar yazmanız gerekir.
    */

    // Subclass - superclass

    public Car(boolean hasSunroof, String brand) {
        System.out.println("Car object created.");
        this.hasSunroof = hasSunroof;
        super.setBrand(brand); // Vehicle sınıfındaki setBrand methodunu çağırarak brand'ı "Car" olarak ayarlıyoruz.
    }

    public Car() {
    }
    
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
