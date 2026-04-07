package com.turkcell;

public class Customer {
    private int id;
    private String name;
    private double balance;
    private String password;
    private double monthlyIncome;

    public Customer() {
    }

    public Customer(int id, String name, double balance, String password, double monthlyIncome) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.password = password;
        this.monthlyIncome = monthlyIncome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }
}
