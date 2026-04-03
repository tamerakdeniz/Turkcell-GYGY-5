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

        // Karar Blokları & Döngüler

        // Belirtli 1+ kapsamdaki kod bloklarının çalıştırılmasını sağlarlar
        // Karar bloğu = minimum 1, maximum n adet karara göre farklı kod bloklarının çalıştırabilir
        // Koşul: true/false döndüren bir ifade


        // Her koşul bloğu yalnızca 1 scope (kapsama alanı) çalıştırır.
        // Kodlar yukarıdan aşağıya doğru okunur ve çalıştırılır. Koşullar sırayla kontrol edilir ve ilk true olan bloğun kodu çalıştırılır, diğer bloklar atlanır.
        int age2 = 18;
        if(age2 >= 18) {
            System.out.println("Reşitsiniz.");
        }
        else if(age2 == 18) {
            System.out.println("Ehliyet alabilirsiniz.");
        }
        else {
            System.out.println("Reşit değilsiniz.");
        }

        // Karar Blokları bir scope çalıştırmak zorunda değildir.
        // String karşılaştırırken == operatörünü kullanmak referans karşılaştırması yapar, equals() metodu ise değer karşılaştırması yapar.
        String username = "admin";
        if(username.equals("Tamer")) {
            System.out.println("Hoşgeldiniz, admin!");
        }
        calculateGrade(50, "Tamer");
        calculateGrade(70, "Mehmet");
        calculateGrade(85);
        calculateGrade(63, "Fatma");
    }

    // Methodlar (Functions) => Belirli bir görevi yerine getiren kod bloklarıdır. Tekrar kullanılabilirler ve programın farklı yerlerinden çağrılabilirler.
    // Erişim belirticileri (Access Modifiers) => public, private, protected, default
    // Dönüş Tipleri (Return Types) => void, int, String, boolean, vs.
    // Method İsimleri => camelCase (örneğin: calculateGrade)
    // Parametreler (Parameters) => Methodun çalışması için gereken bilgileri sağlarlar. Methodun parantezleri içinde tanımlanırlar.
    public static void calculateGrade(int grade, String studentName) { // Required Parameters (Zorunlu Parametreler)
        if (grade >= 90) {
            System.out.println(studentName + " Notunuz: A");
        } else if (grade >= 80) {
            System.out.println(studentName + " Notunuz: B");
        } else if (grade >= 70) {
            System.out.println(studentName + " Notunuz: C");
        } else if (grade >= 60) {
            System.out.println(studentName + " Notunuz: D");
        } else {
            System.out.println(studentName + " Notunuz: F");
        }
    }

    // studentName parametresi olmadan da not hesaplamak isteyebiliriz, bu durumda studentName parametresini opsiyonel (isteğe bağlı) yapabiliriz.
    // Method Overloading (Aşırı Yükleme) => Aynı isimde ancak farklı parametre listesine sahip birden fazla method tanımlama tekniğidir. Bu sayede aynı işlemi farklı şekillerde gerçekleştirebiliriz.
    public static void calculateGrade(int grade) { // Overloading (Aşırı Yükleme)
        calculateGrade(grade, "Öğrenci");
    }

} // Main classının kapsama alanı (sınır)