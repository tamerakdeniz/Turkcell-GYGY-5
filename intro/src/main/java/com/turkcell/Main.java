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
        int X = 15; // X değişkeni oluşturuldu ve değeri atandı
        System.out.println(X); // X değişkeninin değeri ekrana yazdırıldı
        // Tanımlandıktan itibaren değişebilir, erişilebilir ve kullanılabilirler
        X = 20; // X değişkeninin değeri güncellendi
        System.out.println(X); // X değişkeninin güncellenmiş değeri ekrana yazdırıldı

    }
} // Main classının kapsama alanı (sınır)