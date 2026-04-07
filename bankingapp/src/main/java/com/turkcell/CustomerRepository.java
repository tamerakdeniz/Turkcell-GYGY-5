package com.turkcell;

import java.util.List;

public interface CustomerRepository {
    
    void add(Customer customer);

    List<Customer> getAll();
}
