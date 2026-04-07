package com.turkcell;

import java.util.InputMismatchException;
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

            int mainChoice = getIntInput(scanner, "Seçiminiz: ");

            if (mainChoice == 1) {
                System.out.print("Müşteri Ad Soyad giriniz: ");
                String name = scanner.nextLine();

                System.out.print("Şifre belirleyiniz: ");
                String password = scanner.nextLine();

                double monthlyIncome = getDoubleInput(scanner, "Aylık gelirinizi giriniz: ");
                double balance = getDoubleInput(scanner, "Başlangıç Bakiyesini giriniz: ");

                Customer newCustomer = new Customer(0, name, balance, password, monthlyIncome);
                repository.add(newCustomer);

            } else if (mainChoice == 2) {
                int id = getIntInput(scanner, "\nMüşteri ID: ");

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

                        int cChoice = getIntInput(scanner, "İşlem: ");

                        if (cChoice == 1) {
                            double amount = getDoubleInput(scanner, "Çekilecek Tutar: ");
                            if (currentCustomer.getBalance() >= amount) {
                                currentCustomer.setBalance(currentCustomer.getBalance() - amount);
                                System.out.println("Para çekildi. Yeni Bakiye: " + currentCustomer.getBalance());
                            } else {
                                System.out.println("Yetersiz Bakiye!");
                            }
                        } else if (cChoice == 2) {
                            double amount = getDoubleInput(scanner, "Yatırılacak Tutar: ");
                            currentCustomer.setBalance(currentCustomer.getBalance() + amount);
                            System.out.println("Para yatırıldı. Yeni Bakiye: " + currentCustomer.getBalance());
                        } else if (cChoice == 3) {
                            System.out.println("Güncel Bakiyeniz: " + currentCustomer.getBalance() + " TL");
                        } else if (cChoice == 4) {
                            int taksit = getIntInput(scanner, "Kaç taksit ile kredi istiyorsunuz (Örn: 12): ");

                            double maxKredi = (currentCustomer.getMonthlyIncome() / 2) * taksit;
                            System.out.println(
                                    "Çekebileceğiniz Maksimum Kredi (Eksi Bakiye Limiti): " + maxKredi + " TL");

                            double miktar = getDoubleInput(scanner, "Çekmek istediğiniz miktar: ");

                            if (miktar <= maxKredi) {
                                double geriOdeme = miktar + (miktar * (2.8 * taksit) / 100);
                                currentCustomer.setBalance(currentCustomer.getBalance() - geriOdeme);

                                System.out.println("Kredi onaylandı! " + miktar
                                        + " TL krediniz tanımlandı ve toplam borç bakiyenize yansıtıldı.");
                                System.out.println("Yapılan hesaplama: " + miktar + " TL anapara, " + taksit
                                        + " ay vade, %2.8 faiz oranı.");
                                System.out
                                        .println("Toplam Geri Ödeme Tutarınız (Anapara + Faiz): " + geriOdeme + " TL");
                                System.out.println(
                                        "Yeni Bakiyeniz (Borç dahil): " + currentCustomer.getBalance() + " TL");
                            } else {
                                System.out.println("Gelirinize göre bu tutarda kredi alamazsınız!");
                            }
                        } else if (cChoice == 5) {
                            int taksit = getIntInput(scanner, "Kaç aylık limit tahmininizi görmek istersiniz?: ");
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

                        int aChoice = getIntInput(scanner, "İşlem: ");

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
                            int id = getIntInput(scanner, "Müşteri ID: ");
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
                            int id = getIntInput(scanner, "Silinecek Müşteri ID: ");
                            repository.delete(id);
                            System.out.println(id + " numaralı müşteri sistemden silindi.");
                        } else if (aChoice == 4) {
                            int id = getIntInput(scanner, "Şifresi yenilenecek Müşteri ID: ");
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

    // --- YARDIMCI FONKSİYONLAR ---

    public static int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("HATA: Lütfen sadece sayı formatında giriş yapınız!\n");
                scanner.nextLine();
            }
        }
    }

    public static double getDoubleInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("HATA: Lütfen geçerli bir tutar veya oran giriniz!\n");
                scanner.nextLine();
            }
        }
    }
}