package com.turkcell;

import java.util.List;

public interface CustomerRepository {
    
    void add(Customer customer);

    void delete(int id);
    
    Customer getById(int id);

    List<Customer> getAll();
}
