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

        // Diziler (Arrays)

        String[] names = {"Tamer", "Ahmet", "Ayşe"};
        System.out.println(names[0]); // Tamer

        // Primitive (ilkel) tipler -> int, double, boolean, char
        // Non-primitive (nesne) tipler -> Referans Tipler -> String, Arrays, Objects
        int[] c = {1, 2, 3, 4};
        int[] d = c;
        d[3] = 100;
        System.out.println(c[3]);
        System.out.println(d[3]);

        String str1 = "Hello";
        String str2 = "Hello";
        System.out.println(str1 == str2); // true (String havuzu (pool) nedeniyle)
        // Aynı metinleri bir havuzda tutar ve aynı referansı paylaşır

        System.out.println(str1.equals(str2)); // true (değer karşılaştırması)

        String str3 = "Turkcell";
        String str4 = str3.intern(); // Havuzda aynı değere sahip bir String varsa onu kullanır
        System.out.println(str3 == str4); // true (intern() ile havuzdaki referansı kullanır)

    }
} // Main classının kapsama alanı (sınır)