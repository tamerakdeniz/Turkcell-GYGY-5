package com.turkcell;

// Sistemimde araba veritabanı olarak çalışmak isteyen her nesne bu arayüzü implement etmek zorunda, bu sayede araba ekleme işlemi için addCar methodunu kullanabilirler.

public interface CarRepository {
    // Bir car repository'si nasıl davranmalı? Net kalıp ve kurallar ile tanımla.
    // Soyut => içi boş,  yalnızca imza (signature) içerir, implementasyon içermez.

    void add(Car car); // Araba ekleme

}

// Böylelikle PostgreSQLCarRepository, MySQLCarRepository, MongoDBCarRepository gibi farklı veritabanları için farklı implementasyonlar yapabiliriz.
// Bu sayede kodumuz daha esnek ve genişletilebilir olur, yeni bir veritabanı eklemek istediğimizde sadece yeni bir sınıf oluşturup CarRepository arayüzünü implement etmemiz yeterli olur.
