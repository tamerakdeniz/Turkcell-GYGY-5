package com.turkcell;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CustomerRepository repository = new InMemoryCustomerRepository();
        Scanner scanner = new Scanner(System.in);
        boolean mainRunning = true;

        System.out.println("=== Turkcell Bank Uygulamasına Hoşgeldiniz ===");

        while (mainRunning) {
            System.out.println("\n----------------- ANA MENÜ -----------------");
            System.out.println("1 - Kapsamlı Müşteri Ol (Kayıt)");
            System.out.println("2 - Müşteri Paneline Giriş Yap");
            System.out.println("3 - Yönetici Paneline Giriş Yap");
            System.out.println("0 - Sistemi Kapat");
            System.out.print("Seçiminiz: ");

            int mainChoice = scanner.nextInt();
            scanner.nextLine();

            if (mainChoice == 1) {
                System.out.print("Müşteri ID'si giriniz: ");
                int id = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Müşteri Ad Soyad giriniz: ");
                String name = scanner.nextLine();

                System.out.print("Şifre belirleyiniz: ");
                String password = scanner.nextLine();

                System.out.print("Aylık gelirinizi giriniz: ");
                double monthlyIncome = scanner.nextDouble();

                System.out.print("Başlangıç Bakiyesini giriniz: ");
                double balance = scanner.nextDouble();

                Customer newCustomer = new Customer(id, name, balance, password, monthlyIncome);
                repository.add(newCustomer);

            } else if (mainChoice == 2) {
                System.out.print("\nMüşteri ID: ");
                int id = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Şifreniz: ");
                String password = scanner.nextLine();

                Customer currentCustomer = repository.getById(id);

                if (currentCustomer != null && currentCustomer.getPassword().equals(password)) {
                    System.out.println("\nHoşgeldiniz, " + currentCustomer.getName() + "!");
                    boolean customerLoop = true;

                    while (customerLoop) {
                        System.out.println("\n--- MÜŞTERİ PANELİ ---");
                        System.out.println("1 - Para Çek");
                        System.out.println("2 - Para Yatır");
                        System.out.println("3 - Bakiye Sorgula");
                        System.out.println("4 - Kredi Çek (Eksi Bakiye)");
                        System.out.println("5 - Çekilebilir Kredi Limitimi Sorgula");
                        System.out.println("6 - Şifre Değiştir");
                        System.out.println("0 - Ana Menüye Dön");
                        System.out.print("İşlem: ");
                        int cChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (cChoice == 1) {
                            System.out.print("Çekilecek Tutar: ");
                            double amount = scanner.nextDouble();
                            if (currentCustomer.getBalance() >= amount) {
                                currentCustomer.setBalance(currentCustomer.getBalance() - amount);
                                System.out.println("Para çekildi. Yeni Bakiye: " + currentCustomer.getBalance());
                            } else {
                                System.out.println("Yetersiz Bakiye!");
                            }
                        } else if (cChoice == 2) {
                            System.out.print("Yatırılacak Tutar: ");
                            double amount = scanner.nextDouble();
                            currentCustomer.setBalance(currentCustomer.getBalance() + amount);
                            System.out.println("Para yatırıldı. Yeni Bakiye: " + currentCustomer.getBalance());
                        } else if (cChoice == 3) {
                            System.out.println("Güncel Bakiyeniz: " + currentCustomer.getBalance() + " TL");
                        } else if (cChoice == 4) {
                            System.out.print("Kaç taksit ile kredi istiyorsunuz (Örn: 12): ");
                            int taksit = scanner.nextInt();

                            // Formül: (aylık gelirin yarısı) * taksit
                            double maxKredi = (currentCustomer.getMonthlyIncome() / 2) * taksit;
                            System.out.println(
                                    "Çekebileceğiniz Maksimum Kredi (Eksi Bakiye Limiti): " + maxKredi + " TL");
                            System.out.print("Çekmek istediğiniz miktar: ");
                            double miktar = scanner.nextDouble();

                            if (miktar <= maxKredi) {
                                // Bakiye eksiye düşecek
                                currentCustomer.setBalance(currentCustomer.getBalance() - miktar);
                                double geriOdeme = miktar + (miktar * (2.8 * taksit) / 100);
                                // Basit faiz hesabı örneği: Anapara + (Anapara * Faiz Orani * Taksit / 100)
                                System.out.println("Kredi onaylandı! " + miktar
                                        + " TL krediniz tanımlandı ve bakiyeniz eksiye düşürüldü.");
                                System.out.println("Yapılan hesaplama: " + miktar + " TL anapara, " + taksit
                                        + " ay vade, %2.8 faiz oranı.");
                                System.out.println("Toplam Geri Ödeme Tutarınız: " + geriOdeme + " TL");
                                System.out.println(
                                        "Yeni Bakiyeniz (Borç dahil): " + currentCustomer.getBalance() + " TL");
                            } else {
                                System.out.println("Gelirinize göre bu tutarda kredi alamazsınız!");
                            }
                        } else if (cChoice == 5) {
                            System.out.print("Kaç aylık limit tahmininizi görmek istersiniz?: ");
                            int taksit = scanner.nextInt();
                            double maxKredi = (currentCustomer.getMonthlyIncome() / 2) * taksit;
                            System.out.println("=> Yaklaşık Kredi Limitiniz: " + maxKredi + " TL");
                        } else if (cChoice == 6) {
                            System.out.print("Yeni Şifreniz: ");
                            String newPass = scanner.nextLine();
                            currentCustomer.setPassword(newPass);
                            System.out.println("Şifreniz başarıyla değiştirildi.");
                        } else if (cChoice == 0) {
                            customerLoop = false;
                        } else {
                            System.out.println("Geçersiz işlem!");
                        }
                    }

                } else {
                    System.out.println("HATA: Müşteri bilgisi bulunamadı veya Şifre yanlış!");
                }

            } else if (mainChoice == 3) {
                System.out.print("\nYönetici Şifresi (Admin123): ");
                String adminPass = scanner.nextLine();

                if (adminPass.equals("Admin123")) {
                    boolean adminLoop = true;
                    while (adminLoop) {
                        System.out.println("\n--- YÖNETİCİ PANELİ ---");
                        System.out.println("1 - Tüm Müşterileri Görüntüle");
                        System.out.println("2 - Müşteri Hesap Özeti ve Kredi Bakiyesi Sorgula");
                        System.out.println("3 - Müşteri Sil");
                        System.out.println("4 - Müşteri Şifresi Güncelle (Reset)");
                        System.out.println("0 - Ana Menüye Dön");
                        System.out.print("İşlem: ");
                        int aChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (aChoice == 1) {
                            System.out.println("\n-- KAYITLI MÜŞTERİLER --");
                            List<Customer> list = repository.getAll();
                            if (list.isEmpty())
                                System.out.println("Hiç müşteri yok.");
                            for (Customer c : list) {
                                System.out.println(c.getId() + " - " + c.getName() + " | Bakiye: " + c.getBalance()
                                        + " | Gelir: " + c.getMonthlyIncome());
                            }
                        } else if (aChoice == 2) {
                            System.out.print("Müşteri ID: ");
                            int id = scanner.nextInt();
                            Customer c = repository.getById(id);
                            if (c != null) {
                                System.out.println("Müşteri: " + c.getName());
                                System.out.println("Bakiye: " + c.getBalance() + " TL");
                                System.out.println("Aylık Gelir: " + c.getMonthlyIncome() + " TL");
                                System.out.println("Varsayılan İhtiyaç Kredisi Limiti (36 ay bazında): "
                                        + ((c.getMonthlyIncome() / 2) * 36) + " TL");
                            } else {
                                System.out.println("Müşteri bulunamadı.");
                            }
                        } else if (aChoice == 3) {
                            System.out.print("Silinecek Müşteri ID: ");
                            int id = scanner.nextInt();
                            repository.delete(id);
                            System.out.println(id + " numaralı müşteri sistemden silindi.");
                        } else if (aChoice == 4) {
                            System.out.print("Şifresi yenilenecek Müşteri ID: ");
                            int id = scanner.nextInt();
                            Customer c = repository.getById(id);
                            if (c != null) {
                                c.setPassword("123456");
                                System.out.println(c.getName() + " kullanıcısının şifresi '123456' olarak resetlendi.");
                            } else {
                                System.out.println("Müşteri bulunamadı.");
                            }
                        } else if (aChoice == 0) {
                            adminLoop = false;
                        } else {
                            System.out.println("Geçersiz işlem.");
                        }
                    }
                } else {
                    System.out.println("HATA: Yetkisiz giriş denemesi!");
                }

            } else if (mainChoice == 0) {
                System.out.println("\nSistem Kapatıldı.");
                mainRunning = false;
            } else {
                System.out.println("Geçersiz seçim!");
            }
        }
        scanner.close();
    }
}