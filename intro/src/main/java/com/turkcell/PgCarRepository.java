package com.turkcell;


// Implement ediyorsan imza (signature) uymak zorundasın, yani addCar methodunu implement etmek zorundasın, aksi takdirde hata alırsın.
public class PgCarRepository implements CarRepository {
    public void add(Car car) {
        System.out.println("Saving car to PostgreSQL database");
    }

}
