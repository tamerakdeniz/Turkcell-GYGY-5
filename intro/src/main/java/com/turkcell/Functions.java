package com.turkcell;

public class Functions {
    public static void main(String[] args) {
        String message = "Merhaba";
        sayWelcome(message);
        System.out.println("Ana methoddaki mesaj: " + message); // Merhaba (String immutable olduğu için orijinal değer değişmez)

        int[] numbers = {1, 2, 3, 4, 5};
        sum(numbers);
        System.out.println("Ana methoddaki ilk sayı: " + numbers[0]); //
    }

    // Pass by Value (Değer ile Geçiş) => Primitive tipler (int, double, boolean, char) ve String gibi immutable tipler için geçerlidir. 
    // Methodlara argüman olarak verilen değerlerin kopyası oluşturulur ve method içinde yapılan değişiklikler orijinal değeri etkilemez
    // Referans tipler (Arrays, Objects) için geçerli değildir. Methodlara argüman olarak verilen referansların kopyası oluşturulur ancak bu referanslar aynı nesneyi işaret eder. 
    // Bu nedenle method içinde yapılan değişiklikler orijinal nesneyi etkiler.

    public static void sayWelcome(String message) {
        message = "Java Eğitimi";
        System.out.println("Mesaj: " + message);
    }

    public static void sum(int[] numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        System.out.println("Toplam: " + total);
        numbers[0] = 100; // Diziler mutable (değiştirilebilir) olduğu için orijinal dizi değişir
    }
}
