-- =============================================================
-- KÜTÜPHANE YÖNETİM SİSTEMİ - POSTGRESQL DDL + DML
-- =============================================================
-- Veritabanı: library
-- Ortam: Docker PostgreSQL
-- Yazar: Tamer Akdeniz
-- Tarih: 2026-04-21
-- =============================================================
-- Kullanım:
--   1. PostgreSQL'de 'library' database'i oluşturulmuş olmalı
--   2. Query tool'da bu dosyayı tek seferde çalıştır
--   3. Önce tüm DROP'lar, sonra CREATE'ler, sonra INSERT'ler çalışır
-- =============================================================


-- =============================================================
-- BÖLÜM 0: TABLOLARI TEMİZLEME (Re-runnability için)
-- =============================================================
-- FK bağımlılıkları nedeniyle TERS sırayla drop ediyoruz

DROP TABLE IF EXISTS rezervasyon CASCADE;
DROP TABLE IF EXISTS ceza CASCADE;
DROP TABLE IF EXISTS iade CASCADE;
DROP TABLE IF EXISTS odunc_alma CASCADE;
DROP TABLE IF EXISTS gorevli_yetki CASCADE;
DROP TABLE IF EXISTS yetki CASCADE;
DROP TABLE IF EXISTS gorevli CASCADE;
DROP TABLE IF EXISTS ogrenci CASCADE;
DROP TABLE IF EXISTS kitap_yazar CASCADE;
DROP TABLE IF EXISTS yazar CASCADE;
DROP TABLE IF EXISTS kitap_kategori CASCADE;
DROP TABLE IF EXISTS kitap_kopya CASCADE;
DROP TABLE IF EXISTS kitap CASCADE;
DROP TABLE IF EXISTS kategori CASCADE;
DROP TABLE IF EXISTS yayinevi CASCADE;


-- =============================================================
-- BÖLÜM 1: DDL - TABLO OLUŞTURMA (CREATE TABLE)
-- =============================================================


-- -----------------------------------------------------------
-- 1. YAYINEVI
-- -----------------------------------------------------------
CREATE TABLE yayinevi (
    yayinevi_id   SERIAL PRIMARY KEY,
    ad            VARCHAR(150) NOT NULL,
    adres         VARCHAR(300),
    telefon       VARCHAR(20),
    email         VARCHAR(100),
    web_sitesi    VARCHAR(200)
);


-- -----------------------------------------------------------
-- 2. KATEGORI (self-reference: ust_kategori_id)
-- -----------------------------------------------------------
CREATE TABLE kategori (
    kategori_id       SERIAL PRIMARY KEY,
    ad                VARCHAR(100) NOT NULL UNIQUE,
    ust_kategori_id   INT,
    aciklama          TEXT,
    CONSTRAINT fk_kategori_ust
        FOREIGN KEY (ust_kategori_id)
        REFERENCES kategori(kategori_id)
        ON DELETE SET NULL
);


-- -----------------------------------------------------------
-- 3. KITAP
-- -----------------------------------------------------------
CREATE TABLE kitap (
    kitap_id        SERIAL PRIMARY KEY,
    isbn            VARCHAR(20) NOT NULL UNIQUE,
    baslik          VARCHAR(255) NOT NULL,
    yayin_yili      SMALLINT CHECK (yayin_yili > 1000 AND yayin_yili <= 2100),
    sayfa_sayisi    INT CHECK (sayfa_sayisi > 0),
    dil             VARCHAR(30) DEFAULT 'Türkçe',
    baski_no        SMALLINT DEFAULT 1,
    aciklama        TEXT,
    yayinevi_id     INT NOT NULL,
    eklenme_tarihi  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_kitap_yayinevi
        FOREIGN KEY (yayinevi_id)
        REFERENCES yayinevi(yayinevi_id)
        ON DELETE RESTRICT
);


-- -----------------------------------------------------------
-- 4. KITAP_KOPYA
-- -----------------------------------------------------------
CREATE TABLE kitap_kopya (
    kopya_id       SERIAL PRIMARY KEY,
    kitap_id       INT NOT NULL,
    barkod         VARCHAR(20) NOT NULL UNIQUE,
    raf_no         VARCHAR(20),
    durum          VARCHAR(15) NOT NULL DEFAULT 'musait'
                   CHECK (durum IN ('musait','oduncte','kayip','hasarli')),
    alim_tarihi    DATE,
    fiyat          DECIMAL(10,2) CHECK (fiyat >= 0),
    CONSTRAINT fk_kopya_kitap
        FOREIGN KEY (kitap_id)
        REFERENCES kitap(kitap_id)
        ON DELETE CASCADE
);


-- -----------------------------------------------------------
-- 5. KITAP_KATEGORI (Bridge: Kitap M:N Kategori)
-- -----------------------------------------------------------
CREATE TABLE kitap_kategori (
    kitap_id         INT NOT NULL,
    kategori_id      INT NOT NULL,
    ana_mi           BOOLEAN DEFAULT FALSE,
    eklenme_tarihi   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (kitap_id, kategori_id),
    CONSTRAINT fk_kk_kitap
        FOREIGN KEY (kitap_id) REFERENCES kitap(kitap_id) ON DELETE CASCADE,
    CONSTRAINT fk_kk_kategori
        FOREIGN KEY (kategori_id) REFERENCES kategori(kategori_id) ON DELETE CASCADE
);


-- -----------------------------------------------------------
-- 6. YAZAR
-- -----------------------------------------------------------
CREATE TABLE yazar (
    yazar_id       SERIAL PRIMARY KEY,
    ad             VARCHAR(100) NOT NULL,
    soyad          VARCHAR(100) NOT NULL,
    dogum_tarihi   DATE,
    olum_tarihi    DATE,
    uyruk          VARCHAR(50),
    biyografi      TEXT,
    CONSTRAINT chk_yazar_tarih CHECK (olum_tarihi IS NULL OR olum_tarihi > dogum_tarihi)
);


-- -----------------------------------------------------------
-- 7. KITAP_YAZAR (Bridge: Kitap M:N Yazar)
-- -----------------------------------------------------------
CREATE TABLE kitap_yazar (
    kitap_id   INT NOT NULL,
    yazar_id   INT NOT NULL,
    PRIMARY KEY (kitap_id, yazar_id),
    CONSTRAINT fk_ky_kitap
        FOREIGN KEY (kitap_id) REFERENCES kitap(kitap_id) ON DELETE CASCADE,
    CONSTRAINT fk_ky_yazar
        FOREIGN KEY (yazar_id) REFERENCES yazar(yazar_id) ON DELETE CASCADE
);


-- -----------------------------------------------------------
-- 8. OGRENCI
-- -----------------------------------------------------------
CREATE TABLE ogrenci (
    ogrenci_id     SERIAL PRIMARY KEY,
    ogrenci_no     VARCHAR(15) NOT NULL UNIQUE,
    tc_kimlik      CHAR(11) NOT NULL UNIQUE,
    ad             VARCHAR(50) NOT NULL,
    soyad          VARCHAR(50) NOT NULL,
    email          VARCHAR(100) UNIQUE,
    telefon        VARCHAR(20),
    adres          VARCHAR(300),
    dogum_tarihi   DATE NOT NULL,
    cinsiyet       CHAR(1) CHECK (cinsiyet IN ('E','K','D')),
    kayit_tarihi   DATE DEFAULT CURRENT_DATE,
    aktif_mi       BOOLEAN DEFAULT TRUE
);


-- -----------------------------------------------------------
-- 9. GOREVLI
-- -----------------------------------------------------------
CREATE TABLE gorevli (
    gorevli_id            SERIAL PRIMARY KEY,
    tc_kimlik             CHAR(11) NOT NULL UNIQUE,
    ad                    VARCHAR(50) NOT NULL,
    soyad                 VARCHAR(50) NOT NULL,
    email                 VARCHAR(100) UNIQUE,
    telefon               VARCHAR(20),
    pozisyon              VARCHAR(15) NOT NULL
                          CHECK (pozisyon IN ('yonetici','memur','stajyer')),
    ise_baslama_tarihi    DATE NOT NULL,
    maas                  DECIMAL(10,2) CHECK (maas >= 0),
    kullanici_adi         VARCHAR(50) NOT NULL UNIQUE,
    sifre_hash            VARCHAR(255) NOT NULL,
    aktif_mi              BOOLEAN DEFAULT TRUE
);


-- -----------------------------------------------------------
-- 10. YETKI
-- -----------------------------------------------------------
CREATE TABLE yetki (
    yetki_id    SERIAL PRIMARY KEY,
    ad          VARCHAR(50) NOT NULL UNIQUE,
    aciklama    VARCHAR(200),
    modul       VARCHAR(30) NOT NULL
);


-- -----------------------------------------------------------
-- 12. GOREVLI_YETKI (Bridge)
-- -----------------------------------------------------------
CREATE TABLE gorevli_yetki (
    gorevli_id          INT NOT NULL,
    yetki_id            INT NOT NULL,
    verilme_tarihi      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    veren_gorevli_id    INT,
    PRIMARY KEY (gorevli_id, yetki_id),
    CONSTRAINT fk_gy_gorevli
        FOREIGN KEY (gorevli_id) REFERENCES gorevli(gorevli_id) ON DELETE CASCADE,
    CONSTRAINT fk_gy_yetki
        FOREIGN KEY (yetki_id) REFERENCES yetki(yetki_id) ON DELETE CASCADE,
    CONSTRAINT fk_gy_veren
        FOREIGN KEY (veren_gorevli_id) REFERENCES gorevli(gorevli_id) ON DELETE SET NULL
);


-- -----------------------------------------------------------
-- 13. ODUNC_ALMA
-- -----------------------------------------------------------
CREATE TABLE odunc_alma (
    odunc_id                SERIAL PRIMARY KEY,
    kopya_id                INT NOT NULL,
    ogrenci_id              INT NOT NULL,
    gorevli_id              INT NOT NULL,
    odunc_tarihi            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    iade_tarihi_beklenen    DATE NOT NULL,
    durum                   VARCHAR(15) NOT NULL DEFAULT 'aktif'
                            CHECK (durum IN ('aktif','iade_edildi','gecikmis','kayip')),
    CONSTRAINT fk_odunc_kopya
        FOREIGN KEY (kopya_id) REFERENCES kitap_kopya(kopya_id) ON DELETE RESTRICT,
    CONSTRAINT fk_odunc_ogrenci
        FOREIGN KEY (ogrenci_id) REFERENCES ogrenci(ogrenci_id) ON DELETE RESTRICT,
    CONSTRAINT fk_odunc_gorevli
        FOREIGN KEY (gorevli_id) REFERENCES gorevli(gorevli_id) ON DELETE RESTRICT,
    CONSTRAINT chk_odunc_tarih CHECK (iade_tarihi_beklenen > odunc_tarihi::DATE)
);


-- -----------------------------------------------------------
-- 14. IADE
-- -----------------------------------------------------------
CREATE TABLE iade (
    iade_id         SERIAL PRIMARY KEY,
    odunc_id        INT NOT NULL UNIQUE,
    gorevli_id      INT NOT NULL,
    iade_tarihi     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    kitap_durumu    VARCHAR(15) NOT NULL
                    CHECK (kitap_durumu IN ('iyi','hasarli','kayip')),
    notlar          TEXT,
    CONSTRAINT fk_iade_odunc
        FOREIGN KEY (odunc_id) REFERENCES odunc_alma(odunc_id) ON DELETE CASCADE,
    CONSTRAINT fk_iade_gorevli
        FOREIGN KEY (gorevli_id) REFERENCES gorevli(gorevli_id) ON DELETE RESTRICT
);


-- -----------------------------------------------------------
-- 15. CEZA
-- -----------------------------------------------------------
CREATE TABLE ceza (
    ceza_id         SERIAL PRIMARY KEY,
    odunc_id        INT NOT NULL,
    ogrenci_id      INT NOT NULL,
    ceza_miktari    DECIMAL(8,2) NOT NULL CHECK (ceza_miktari >= 0),
    ceza_nedeni     VARCHAR(15) NOT NULL
                    CHECK (ceza_nedeni IN ('gecikme','hasar','kayip')),
    ceza_tarihi     DATE DEFAULT CURRENT_DATE,
    odendi_mi       BOOLEAN DEFAULT FALSE,
    odeme_tarihi    DATE,
    CONSTRAINT fk_ceza_odunc
        FOREIGN KEY (odunc_id) REFERENCES odunc_alma(odunc_id) ON DELETE CASCADE,
    CONSTRAINT fk_ceza_ogrenci
        FOREIGN KEY (ogrenci_id) REFERENCES ogrenci(ogrenci_id) ON DELETE RESTRICT
);


-- -----------------------------------------------------------
-- 16. REZERVASYON
-- -----------------------------------------------------------
CREATE TABLE rezervasyon (
    rezervasyon_id          SERIAL PRIMARY KEY,
    kitap_id                INT NOT NULL,
    ogrenci_id              INT NOT NULL,
    rezervasyon_tarihi      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    son_gecerlilik_tarihi   DATE NOT NULL,
    durum                   VARCHAR(15) NOT NULL DEFAULT 'bekliyor'
                            CHECK (durum IN ('bekliyor','tamamlandi','iptal')),
    CONSTRAINT fk_rezerv_kitap
        FOREIGN KEY (kitap_id) REFERENCES kitap(kitap_id) ON DELETE CASCADE,
    CONSTRAINT fk_rezerv_ogrenci
        FOREIGN KEY (ogrenci_id) REFERENCES ogrenci(ogrenci_id) ON DELETE CASCADE
);


-- =============================================================
-- BÖLÜM 2: DML - ÖRNEK VERİ EKLEME (INSERT)
-- =============================================================


-- -----------------------------------------------------------
-- 1. YAYINEVI (5 kayıt)
-- -----------------------------------------------------------
INSERT INTO yayinevi (ad, adres, telefon, email, web_sitesi) VALUES
('Can Yayınları',       'Beyoğlu, İstanbul',  '02122525959', 'info@canyayinlari.com',  'www.canyayinlari.com'),
('İş Bankası Kültür',   'Beyoğlu, İstanbul',  '02122521151', 'info@iskultur.com.tr',   'www.iskultur.com.tr'),
('Yapı Kredi Yayınları','Beyoğlu, İstanbul',  '02122520093', 'info@ykykultur.com.tr',  'www.ykykultur.com.tr'),
('İletişim Yayınları',  'Cağaloğlu, İstanbul','02125166000', 'info@iletisim.com.tr',   'www.iletisim.com.tr'),
('Doğan Kitap',         'Şişli, İstanbul',    '02124781111', 'info@dogankitap.com.tr', 'www.dogankitap.com.tr');


-- -----------------------------------------------------------
-- 2. KATEGORI (7 kayıt, hiyerarşili)
-- -----------------------------------------------------------
INSERT INTO kategori (ad, ust_kategori_id, aciklama) VALUES
('Edebiyat',       NULL, 'Edebi eserler'),
('Bilim',          NULL, 'Bilimsel yayınlar'),
('Tarih',          NULL, 'Tarihsel eserler'),
('Roman',          1,    'Edebiyat > Roman'),
('Klasik Roman',   4,    'Roman > Klasik Roman'),
('Bilim Kurgu',    4,    'Roman > Bilim Kurgu'),
('Biyografi',      3,    'Tarih > Biyografi');


-- -----------------------------------------------------------
-- 3. KITAP (7 kayıt)
-- -----------------------------------------------------------
INSERT INTO kitap (isbn, baslik, yayin_yili, sayfa_sayisi, dil, baski_no, aciklama, yayinevi_id) VALUES
('9789750726330', 'Suç ve Ceza',         1866, 687, 'Türkçe', 12, 'Dostoyevski klasiği',       1),
('9789944886918', 'Sefiller',            1862, 1463,'Türkçe', 8,  'Victor Hugo başyapıtı',     2),
('9789755108438', '1984',                1949, 352, 'Türkçe', 15, 'George Orwell distopyası',  3),
('9789750838446', 'Sapiens',             2011, 464, 'Türkçe', 20, 'Yuval Noah Harari',         1),
('9789753422444', 'Simyacı',             1988, 184, 'Türkçe', 30, 'Paulo Coelho',              4),
('9789944884983', 'Tutunamayanlar',      1972, 724, 'Türkçe', 25, 'Oğuz Atay romanı',          2),
('9789753421645', 'Kürk Mantolu Madonna',1943, 160, 'Türkçe', 40, 'Sabahattin Ali novellası',  5);


-- -----------------------------------------------------------
-- 4. KITAP_KOPYA (10 kayıt - bazı kitapların çok kopyası var)
-- -----------------------------------------------------------
INSERT INTO kitap_kopya (kitap_id, barkod, raf_no, durum, alim_tarihi, fiyat) VALUES
(1, 'BRK-0001', 'A-01-05', 'musait',  '2023-01-15', 85.00),
(1, 'BRK-0002', 'A-01-06', 'oduncte', '2023-01-15', 85.00),
(2, 'BRK-0003', 'A-02-10', 'musait',  '2023-02-20', 120.00),
(3, 'BRK-0004', 'B-01-03', 'musait',  '2023-03-10', 65.00),
(3, 'BRK-0005', 'B-01-04', 'oduncte', '2023-03-10', 65.00),
(4, 'BRK-0006', 'C-05-12', 'musait',  '2024-01-05', 95.00),
(4, 'BRK-0007', 'C-05-13', 'hasarli', '2024-01-05', 95.00),
(5, 'BRK-0008', 'A-03-20', 'musait',  '2024-02-15', 55.00),
(6, 'BRK-0009', 'A-04-01', 'oduncte', '2023-11-01', 110.00),
(7, 'BRK-0010', 'A-05-15', 'musait',  '2023-09-10', 45.00);


-- -----------------------------------------------------------
-- 5. KITAP_KATEGORI (Bridge - 9 kayıt)
-- -----------------------------------------------------------
INSERT INTO kitap_kategori (kitap_id, kategori_id, ana_mi) VALUES
(1, 4, TRUE),   -- Suç ve Ceza → Roman (ana)
(1, 5, FALSE),  -- Suç ve Ceza → Klasik Roman
(2, 4, TRUE),   -- Sefiller → Roman (ana)
(2, 5, FALSE),  -- Sefiller → Klasik Roman
(3, 6, TRUE),   -- 1984 → Bilim Kurgu (ana)
(4, 2, TRUE),   -- Sapiens → Bilim (ana)
(4, 3, FALSE),  -- Sapiens → Tarih
(5, 4, TRUE),   -- Simyacı → Roman (ana)
(6, 4, TRUE);   -- Tutunamayanlar → Roman (ana)


-- -----------------------------------------------------------
-- 6. YAZAR (7 kayıt)
-- -----------------------------------------------------------
INSERT INTO yazar (ad, soyad, dogum_tarihi, olum_tarihi, uyruk, biyografi) VALUES
('Fyodor',     'Dostoyevski', '1821-11-11', '1881-02-09', 'Rus',      'Rus edebiyatının klasik isimlerinden'),
('Victor',     'Hugo',        '1802-02-26', '1885-05-22', 'Fransız',  'Fransız romantik yazarı'),
('George',     'Orwell',      '1903-06-25', '1950-01-21', 'İngiliz',  'Distopik romanlarıyla tanınır'),
('Yuval Noah', 'Harari',      '1976-02-24',  NULL,        'İsrailli', 'İsrailli tarihçi ve yazar'),
('Paulo',      'Coelho',      '1947-08-24',  NULL,        'Brezilyalı','Brezilyalı romancı'),
('Oğuz',       'Atay',        '1934-10-12', '1977-12-13', 'Türk',     'Modern Türk edebiyatının öncülerinden'),
('Sabahattin', 'Ali',         '1907-02-25', '1948-04-02', 'Türk',     'Türk edebiyatının önemli isimlerinden');


-- -----------------------------------------------------------
-- 7. KITAP_YAZAR (Bridge - 7 kayıt)
-- -----------------------------------------------------------
INSERT INTO kitap_yazar (kitap_id, yazar_id) VALUES
(1, 1),  -- Suç ve Ceza → Dostoyevski
(2, 2),  -- Sefiller → Victor Hugo
(3, 3),  -- 1984 → Orwell
(4, 4),  -- Sapiens → Harari
(5, 5),  -- Simyacı → Coelho
(6, 6),  -- Tutunamayanlar → Oğuz Atay
(7, 7);  -- Kürk Mantolu Madonna → Sabahattin Ali


-- -----------------------------------------------------------
-- 8. BOLUM (5 kayıt)
-- -----------------------------------------------------------
INSERT INTO bolum (ad, fakulte) VALUES
('Bilgisayar Mühendisliği',     'Mühendislik Fakültesi'),
('Yazılım Mühendisliği',        'Mühendislik Fakültesi'),
('Türk Dili ve Edebiyatı',      'Fen-Edebiyat Fakültesi'),
('Tarih',                       'Fen-Edebiyat Fakültesi'),
('İşletme',                     'İktisadi İdari Bilimler Fakültesi');


-- -----------------------------------------------------------
-- 9. OGRENCI (7 kayıt)
-- -----------------------------------------------------------
INSERT INTO ogrenci (ogrenci_no, tc_kimlik, ad, soyad, email, telefon, adres, dogum_tarihi, cinsiyet, bolum_id, aktif_mi) VALUES
('20230001', '12345678901', 'Ahmet',    'Yılmaz',   'ahmet.yilmaz@ogr.edu.tr',    '05321112233', 'Kadıköy, İstanbul',  '2003-05-12', 'E', 1, TRUE),
('20230002', '12345678902', 'Elif',     'Demir',    'elif.demir@ogr.edu.tr',      '05322223344', 'Şişli, İstanbul',    '2003-08-20', 'K', 1, TRUE),
('20230003', '12345678903', 'Mehmet',   'Kaya',     'mehmet.kaya@ogr.edu.tr',     '05323334455', 'Beşiktaş, İstanbul', '2002-11-05', 'E', 2, TRUE),
('20220004', '12345678904', 'Zeynep',   'Şahin',    'zeynep.sahin@ogr.edu.tr',    '05324445566', 'Üsküdar, İstanbul',  '2002-03-18', 'K', 3, TRUE),
('20220005', '12345678905', 'Mustafa',  'Aydın',    'mustafa.aydin@ogr.edu.tr',   '05325556677', 'Beykoz, İstanbul',   '2001-07-22', 'E', 4, TRUE),
('20210006', '12345678906', 'Ayşe',     'Çelik',    'ayse.celik@ogr.edu.tr',      '05326667788', 'Bakırköy, İstanbul', '2001-01-30', 'K', 5, TRUE),
('20190007', '12345678907', 'Can',      'Öztürk',   'can.ozturk@ogr.edu.tr',      '05327778899', 'Maltepe, İstanbul',  '1999-09-14', 'E', 2, FALSE);


-- -----------------------------------------------------------
-- 10. GOREVLI (5 kayıt)
-- -----------------------------------------------------------
INSERT INTO gorevli (tc_kimlik, ad, soyad, email, telefon, pozisyon, ise_baslama_tarihi, maas, kullanici_adi, sifre_hash) VALUES
('98765432101', 'Ali',      'Kütüphaneci', 'ali.kutuphaneci@lib.edu.tr',   '05411112233', 'yonetici', '2015-09-01', 45000.00, 'ali.k',      '$2a$10$abcdef1234567890hashexample1'),
('98765432102', 'Fatma',    'Erdem',       'fatma.erdem@lib.edu.tr',       '05412223344', 'memur',    '2018-03-15', 28000.00, 'fatma.e',    '$2a$10$abcdef1234567890hashexample2'),
('98765432103', 'Hasan',    'Doğan',       'hasan.dogan@lib.edu.tr',       '05413334455', 'memur',    '2020-06-10', 26000.00, 'hasan.d',    '$2a$10$abcdef1234567890hashexample3'),
('98765432104', 'Selin',    'Aksoy',       'selin.aksoy@lib.edu.tr',       '05414445566', 'stajyer',  '2025-02-01', 15000.00, 'selin.a',    '$2a$10$abcdef1234567890hashexample4'),
('98765432105', 'Burak',    'Güneş',       'burak.gunes@lib.edu.tr',       '05415556677', 'stajyer',  '2025-09-01', 15000.00, 'burak.g',    '$2a$10$abcdef1234567890hashexample5');


-- -----------------------------------------------------------
-- 11. YETKI (8 kayıt)
-- -----------------------------------------------------------
INSERT INTO yetki (ad, aciklama, modul) VALUES
('kitap_ekle',      'Kitap ekleme yetkisi',     'kitap'),
('kitap_sil',       'Kitap silme yetkisi',      'kitap'),
('odunc_ver',       'Ödünç verme yetkisi',      'odunc'),
('iade_al',         'İade alma yetkisi',        'odunc'),
('ceza_olustur',    'Ceza oluşturma yetkisi',   'ceza'),
('ceza_sil',        'Ceza silme yetkisi',       'ceza'),
('rapor_al',        'Rapor görüntüleme',        'rapor'),
('kullanici_yonet', 'Kullanıcı yönetimi',       'sistem');


-- -----------------------------------------------------------
-- 12. GOREVLI_YETKI (Bridge - 12 kayıt)
-- -----------------------------------------------------------
-- Yönetici (id=1) tüm yetkilere sahip
INSERT INTO gorevli_yetki (gorevli_id, yetki_id, veren_gorevli_id) VALUES
(1, 1, NULL),  -- Yönetici → kitap_ekle
(1, 2, NULL),
(1, 3, NULL),
(1, 4, NULL),
(1, 5, NULL),
(1, 6, NULL),
(1, 7, NULL),
(1, 8, NULL),
-- Memurlar odunc + iade + ceza yetkisine sahip
(2, 3, 1),
(2, 4, 1),
(2, 5, 1),
(3, 3, 1),
(3, 4, 1),
-- Stajyerler sadece odunc + iade
(4, 3, 1),
(4, 4, 1),
(5, 3, 1);


-- -----------------------------------------------------------
-- 13. ODUNC_ALMA (6 kayıt)
-- -----------------------------------------------------------
INSERT INTO odunc_alma (kopya_id, ogrenci_id, gorevli_id, odunc_tarihi, iade_tarihi_beklenen, durum) VALUES
(2, 1, 2, '2026-03-20 10:30:00', '2026-04-03', 'iade_edildi'),
(5, 3, 3, '2026-04-01 14:15:00', '2026-04-15', 'iade_edildi'),
(9, 2, 4, '2026-04-05 09:00:00', '2026-04-19', 'gecikmis'),
(7, 4, 2, '2026-04-10 11:45:00', '2026-04-24', 'aktif'),
(8, 5, 5, '2026-04-15 13:20:00', '2026-04-29', 'aktif'),
(10,6, 3, '2026-03-01 15:00:00', '2026-03-15', 'gecikmis');


-- -----------------------------------------------------------
-- 14. IADE (3 kayıt - sadece iade edilmiş olanlar için)
-- -----------------------------------------------------------
INSERT INTO iade (odunc_id, gorevli_id, iade_tarihi, kitap_durumu, notlar) VALUES
(1, 2, '2026-04-02 16:45:00', 'iyi',     'Zamanında iade edildi'),
(2, 3, '2026-04-20 10:00:00', 'hasarli', 'Kapak hafif yıpranmış'),
(6, 3, '2026-04-18 14:30:00', 'iyi',     '3 gün gecikmeli iade');


-- -----------------------------------------------------------
-- 15. CEZA (5 kayıt)
-- -----------------------------------------------------------
INSERT INTO ceza (odunc_id, ogrenci_id, ceza_miktari, ceza_nedeni, ceza_tarihi, odendi_mi, odeme_tarihi) VALUES
(2, 3, 25.00, 'hasar',    '2026-04-20', TRUE,  '2026-04-20'),
(3, 2, 10.00, 'gecikme',  '2026-04-20', FALSE, NULL),
(6, 6, 175.00,'gecikme',  '2026-04-18', TRUE,  '2026-04-19'),
(6, 6, 30.00, 'hasar',    '2026-04-18', FALSE, NULL),
(3, 2, 5.00,  'gecikme',  '2026-04-21', FALSE, NULL);


-- -----------------------------------------------------------
-- 16. REZERVASYON (5 kayıt)
-- -----------------------------------------------------------
INSERT INTO rezervasyon (kitap_id, ogrenci_id, rezervasyon_tarihi, son_gecerlilik_tarihi, durum) VALUES
(3, 4, '2026-04-10 10:00:00', '2026-04-17', 'tamamlandi'),
(1, 5, '2026-04-18 14:30:00', '2026-04-25', 'bekliyor'),
(6, 2, '2026-04-15 09:15:00', '2026-04-22', 'bekliyor'),
(4, 6, '2026-04-05 11:00:00', '2026-04-12', 'iptal'),
(7, 1, '2026-04-20 16:45:00', '2026-04-27', 'bekliyor');


-- =============================================================
-- BÖLÜM 3: KONTROL SORGULARI (TEST)
-- =============================================================
-- Çalıştırdıktan sonra veri gelişini kontrol et

-- SELECT 'yayinevi'       AS tablo, COUNT(*) AS kayit FROM yayinevi
-- UNION ALL SELECT 'kategori',       COUNT(*) FROM kategori
-- UNION ALL SELECT 'kitap',          COUNT(*) FROM kitap
-- UNION ALL SELECT 'kitap_kopya',    COUNT(*) FROM kitap_kopya
-- UNION ALL SELECT 'kitap_kategori', COUNT(*) FROM kitap_kategori
-- UNION ALL SELECT 'yazar',          COUNT(*) FROM yazar
-- UNION ALL SELECT 'kitap_yazar',    COUNT(*) FROM kitap_yazar
-- UNION ALL SELECT 'bolum',          COUNT(*) FROM bolum
-- UNION ALL SELECT 'ogrenci',        COUNT(*) FROM ogrenci
-- UNION ALL SELECT 'gorevli',        COUNT(*) FROM gorevli
-- UNION ALL SELECT 'yetki',          COUNT(*) FROM yetki
-- UNION ALL SELECT 'gorevli_yetki',  COUNT(*) FROM gorevli_yetki
-- UNION ALL SELECT 'odunc_alma',     COUNT(*) FROM odunc_alma
-- UNION ALL SELECT 'iade',           COUNT(*) FROM iade
-- UNION ALL SELECT 'ceza',           COUNT(*) FROM ceza
-- UNION ALL SELECT 'rezervasyon',    COUNT(*) FROM rezervasyon;


-- =============================================================
-- BÖLÜM 4: ÖRNEK JOIN SORGULARI (BONUS)
-- =============================================================

-- 1) Aktif ödünç alınan kitaplar ve öğrenci bilgileri
-- SELECT
--     o.ad || ' ' || o.soyad AS ogrenci,
--     k.baslik AS kitap,
--     oa.odunc_tarihi,
--     oa.iade_tarihi_beklenen,
--     oa.durum
-- FROM odunc_alma oa
-- JOIN ogrenci o       ON o.ogrenci_id = oa.ogrenci_id
-- JOIN kitap_kopya kk  ON kk.kopya_id  = oa.kopya_id
-- JOIN kitap k         ON k.kitap_id   = kk.kitap_id
-- WHERE oa.durum IN ('aktif', 'gecikmis')
-- ORDER BY oa.odunc_tarihi DESC;

-- 2) Toplam ödenmemiş ceza - öğrenci bazlı
-- SELECT
--     o.ad || ' ' || o.soyad AS ogrenci,
--     SUM(c.ceza_miktari) AS toplam_ceza
-- FROM ceza c
-- JOIN ogrenci o ON o.ogrenci_id = c.ogrenci_id
-- WHERE c.odendi_mi = FALSE
-- GROUP BY o.ogrenci_id, o.ad, o.soyad
-- ORDER BY toplam_ceza DESC;

-- 3) En çok ödünç alınan kitaplar (top 5)
-- SELECT
--     k.baslik,
--     COUNT(*) AS odunc_sayisi
-- FROM odunc_alma oa
-- JOIN kitap_kopya kk ON kk.kopya_id = oa.kopya_id
-- JOIN kitap k        ON k.kitap_id  = kk.kitap_id
-- GROUP BY k.kitap_id, k.baslik
-- ORDER BY odunc_sayisi DESC
-- LIMIT 5;

-- =============================================================
-- SON
-- =============================================================
