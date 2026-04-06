package com.turkcell;

public class Bike extends Vehicle { // Bike, Vehicle sınıfından türetilmiş bir sınıftır.

    private boolean hasBasket; // Bisikletin sepeti olup olmadığı

    public boolean isHasBasket() {
        return hasBasket;
    }

    public void setHasBasket(boolean hasBasket) {
        this.hasBasket = hasBasket;
    }

    // Inheritance => kalıtım
    // Bir nesnenin tüm özelliklerini ve extra kendi özelliklerini taşıması
    // Car ve Bike birbirinden farklı nesneler, ama ikisi de birer araç

    // extends (genişletmek) => miras alma, kalıtım


}
