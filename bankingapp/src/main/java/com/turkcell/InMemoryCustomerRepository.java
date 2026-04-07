package com.turkcell;

public class InMemoryCustomerRepository implements CustomerRepository {
    
    private Customer[] customers = new Customer[100];
    private int customerCount = 0;

    public void add(Customer customer) {
        if (customerCount < customers.length) {
            customers[customerCount] = customer;
            customerCount++;
            System.out.println(customer.getName() + " isimli müşteri sisteme başarıyla eklendi!");
        } else {
            System.out.println("Sistem kapasitesi tam dolu! Yeni müşteri eklenemez.");
        }
    }

    public Customer[] getAll() {
        Customer[] currentCustomers = new Customer[customerCount];
        for (int i = 0; i < customerCount; i++) {
            currentCustomers[i] = customers[i];
        }
        return currentCustomers;
    }
}
