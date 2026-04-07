package com.turkcell;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCustomerRepository implements CustomerRepository {

    private List<Customer> customers = new ArrayList<>();
    private int nextId = 1;

    public void add(Customer customer) {
        customer.setId(nextId++);
        customers.add(customer);
        System.out.println(customer.getName() + " isimli kayıt oluşturuldu! Lütfen not alınız, Müşteri ID Numaranız: "
                + customer.getId());
    }

    public void delete(int id) {
        customers.removeIf(c -> c.getId() == id);
    }

    public Customer getById(int id) {
        for (Customer c : customers) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public List<Customer> getAll() {
        return customers;
    }
}
