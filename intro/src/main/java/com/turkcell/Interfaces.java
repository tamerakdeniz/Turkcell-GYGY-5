package com.turkcell;

public interface Interfaces {
    public static void main(String[] args) {
        CarRepository carRepository = new PgCarRepository(); // Sol taraf CarRepository kurallarına uyan somut bir class, sağ taraf ise bu kurallara uyan bir nesne.

        carRepository.add(new Car(true, "BMW"));
        carRepository.add(new Car(false, "Mercedes"));
        carRepository.add(new Car(true, "Audi"));

    }
}
/*
Yeni bir proje oluşturmak (Aynı klasör içinde olabilir)
isim: Bankingapplication

Banka uygulaması => İçerik tamamen size ait.
Min 3+ özellik. (Hesap özeti görme, veritabanı görme, müşteri olma vb.)
In-Memory Repository => Veriler RAM'de tutulabilir
Tek bir main classda simülasyon yeterli.
Console üzerinden çalışacak.

Çarşamba ders saatine kadar github teslimi.
*/