package com.turkcell;

// Entrypoint
public class Main {
    public static void main(String[] args) {
        System.out.println("Hellö Wörld! Tamer'in Java Eğitimine Hoşgeldiniz!");

        // Programlama konseptleri

        // Scope Kavramı -> {} kapsama alanı

        /* Değişkenler (Variables)

        Kodun akışında değer tutan isimli verilerdir 

         */

        System.out.println(10);
        int X = 15;
        System.out.println(X);
        // Tanımlandıktan itibaren değişebilir, erişilebilir ve kullanılabilirler
        X = 20; 
        System.out.println(X);

        // Değişken tipleri => int, double, boolean, char, String
        String name = "Tamer";
        String age = "24"; 
        boolean isStudent = true; 
        char grade = 'A'; 

    }
} // Main classının kapsama alanı (sınır)