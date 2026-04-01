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

        // Döngüler (Loops)
        // X işlemini birden fazla kez çalıştırmak istediğimizde kullanırız
        // i (iteration = yenileme) genellikle 0'dan başlar ve belirli bir koşula kadar devam eder

        for (int i = 0; i < 5; i++) {
            System.out.println("Döngü sayısı: " + i);
        }

        String[] students = {"Tamer", "Ahmet", "Ayşe"};
        for (int i = 0; i < students.length; i++) {
            System.out.println("Öğrenci: " + students[i]);
        }

        for (String student : students) {
            System.out.println("Öğrenci: " + student);
        }

        // iterasyon => Koşul
        int whileCounter = 0;
        while (whileCounter < 5) {
            System.out.println("While döngüsü sayısı: " + whileCounter);
            whileCounter++; 
        }

        String name2 = "Tamer";
        System.out.println(name2);
        name2 = "Halit";
        System.out.println(name2);
        String name3 = name2.concat("abc"); 
        // String immutable (değiştirilemez) bir yapıya sahiptir. 
        // concat() gibi metotlar yeni bir String oluşturur, mevcut String'i değiştirmez.
        System.out.println(name3);

    }
} // Main classının kapsama alanı (sınır)