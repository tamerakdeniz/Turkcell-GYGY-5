package com.turkcell;

public interface CustomerRepository {

    void add(Customer customer);

    Customer[] getAll();
}
