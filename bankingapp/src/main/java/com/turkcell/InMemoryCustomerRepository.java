package com.turkcell;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCustomerRepository implements CustomerRepository {

    // Boyutu baştan belli olmayan, dinamik büyüyebilen ArrayList yapısı kullanıldı
    private List<Customer> customers = new ArrayList<>();

    public void add(Customer customer) {
        customers.add(customer);
        System.out.println(customer.getName() + " isimli müşteri sisteme başarıyla eklendi!");
    }

    public List<Customer> getAll() {
        // Doğrudan listeyi dönüyoruz, boş dizileri temizlemeye vs. gerek kalmadı
        return customers;
    }
}
