package com.turkcell;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // In-memory repository'i ArrayList ile başlat
        CustomerRepository repository = new InMemoryCustomerRepository();

        // Kullanıcı girdisi için Scanner
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("=== Turkcell Bank Uygulamasına Hoşgeldiniz ===");

        // Arayüz döngüsü
        while (isRunning) {
            System.out.println("\n-------------------------------------------");
            System.out.println("Lütfen Yapmak İstediğiniz İşlemi Seçiniz:");
            System.out.println("1 - Yeni Müşteri Ol (Müşteri Ekleme)");
            System.out.println("2 - Tüm Müşterileri ve Veritabanını Gör");
            System.out.println("3 - Hesap Özeti Gör (Müşteri Ara)");
            System.out.println("0 - Çıkış Yap");
            System.out.print("Seçiminiz: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                // 1. Özellik: Müşteri Ekle
                System.out.print("Müşteri ID'si giriniz: ");
                int id = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Müşteri Ad Soyad giriniz: ");
                String name = scanner.nextLine();

                System.out.print("Başlangıç Bakiyesini giriniz: ");
                double balance = scanner.nextDouble();

                Customer newCustomer = new Customer(id, name, balance);

                repository.add(newCustomer);

            } else if (choice == 2) {
                // 2. Özellik: Veritabanını Göster (Dinamik ArrayList'i çağırıyoruz)
                System.out.println("\n--- Veritabanındaki Tüm Müşteriler ---");
                List<Customer> allCustomers = repository.getAll();

                if (allCustomers.isEmpty()) {
                    System.out.println("Sistemde henüz kayıtlı müşteri bulunmamaktadır.");
                } else {
                    for (Customer c : allCustomers) {
                        System.out.println("ID: " + c.getId() + " - İsim: " + c.getName() + " | Bakiye: "
                                + c.getBalance() + " TL");
                    }
                }

            } else if (choice == 3) {
                // 3. Özellik: Hesap Özeti Görüntüleme (Seçili Müşteri Arama)
                System.out.print("\nSorgulamak istediğiniz Müşteri ID'sini giriniz: ");
                int searchId = scanner.nextInt();

                List<Customer> allCustomers = repository.getAll();
                boolean isFound = false;

                for (Customer c : allCustomers) {
                    if (c.getId() == searchId) {
                        System.out.println("\n--- Hesap Özeti ---");
                        System.out.println("Müşteri No   : " + c.getId());
                        System.out.println("Ad Soyad     : " + c.getName());
                        System.out.println("Güncel Bakiye: " + c.getBalance() + " TL");
                        isFound = true;
                        break;
                    }
                }

                if (!isFound) {
                    System.out.println("Hata: Bu ID'ye ait bir müşteri bulunamadı.");
                }

            } else if (choice == 0) {
                System.out.println("\nSistemden çıkılıyor. Bizi tercih ettiğiniz için teşekkür ederiz!");
                isRunning = false;
            } else {
                System.out.println("\nHatalı bir seçim yaptınız, lütfen tekrar deneyiniz.");
            }
        }

        scanner.close();
    }
}