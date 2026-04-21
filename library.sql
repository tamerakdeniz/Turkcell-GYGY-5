-- =============================================================
-- KÜTÜPHANE YÖNETİM SİSTEMİ - POSTGRESQL DDL + DML
-- =============================================================

-- BÖLÜM 0: TABLOLARI TEMİZLEME (ters bağımlılık sırasıyla)

DROP TABLE IF EXISTS rezervasyon    CASCADE;
DROP TABLE IF EXISTS ceza           CASCADE;
DROP TABLE IF EXISTS iade           CASCADE;
DROP TABLE IF EXISTS odunc_alma     CASCADE;
DROP TABLE IF EXISTS gorevli_yetki  CASCADE;
DROP TABLE IF EXISTS yetki          CASCADE;
DROP TABLE IF EXISTS gorevli        CASCADE;
DROP TABLE IF EXISTS ogrenci        CASCADE;
DROP TABLE IF EXISTS kitap_yazar    CASCADE;
DROP TABLE IF EXISTS yazar          CASCADE;
DROP TABLE IF EXISTS kitap_kategori CASCADE;
DROP TABLE IF EXISTS kitap_kopya    CASCADE;
DROP TABLE IF EXISTS kitap          CASCADE;
DROP TABLE IF EXISTS kategori       CASCADE;
DROP TABLE IF EXISTS yayinevi       CASCADE;


-- BÖLÜM 1: DDL - TABLO OLUŞTURMA (CREATE TABLE)

-- 1. YAYINEVI
CREATE TABLE yayinevi (
    yayinevi_id SERIAL PRIMARY KEY,
    ad          VARCHAR(150) NOT NULL,
    adres       VARCHAR(300),
    telefon     VARCHAR(20),
    email       VARCHAR(100),
    web_sitesi  VARCHAR(200)
);

-- 2. KATEGORI (self-reference: ust_kategori_id)
CREATE TABLE kategori (
    kategori_id     SERIAL PRIMARY KEY,
    ust_kategori_id INT,
    ad              VARCHAR(100),
    aciklama        TEXT,
    CONSTRAINT fk_kategori_ust
        FOREIGN KEY (ust_kategori_id)
        REFERENCES kategori(kategori_id)
        ON DELETE SET NULL
);

-- 3. KITAP
CREATE TABLE kitap (
    kitap_id     SERIAL PRIMARY KEY,
    isbn         VARCHAR(20) NOT NULL UNIQUE,
    kategori_id  INT NOT NULL,
    yayinevi_id  INT NOT NULL,
    baslik       VARCHAR(255) NOT NULL,
    yayin_yili   INT CHECK (yayin_yili > 1000 AND yayin_yili <= 2100),
    sayfa_sayisi INT CHECK (sayfa_sayisi > 0),
    dil          VARCHAR(30) DEFAULT 'Türkçe',
    baski_no     INT DEFAULT 1,
    aciklama     TEXT NOT NULL,
    CONSTRAINT fk_kitap_kategori
        FOREIGN KEY (kategori_id)
        REFERENCES kategori(kategori_id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_kitap_yayinevi
        FOREIGN KEY (yayinevi_id)
        REFERENCES yayinevi(yayinevi_id)
        ON DELETE RESTRICT
);

-- 4. KITAP_KOPYA
CREATE TABLE kitap_kopya (
    kopya_id    SERIAL PRIMARY KEY,
    kitap_id    INT NOT NULL,
    barkod      VARCHAR(20) NOT NULL UNIQUE,
    raf_no      VARCHAR(20),
    durum       VARCHAR(15) NOT NULL DEFAULT 'musait'
                CHECK (durum IN ('musait','oduncte','kayip','hasarli')),
    alim_tarihi DATE,
    fiyat       DECIMAL(10,2) CHECK (fiyat >= 0),
    CONSTRAINT fk_kopya_kitap
        FOREIGN KEY (kitap_id)
        REFERENCES kitap(kitap_id)
        ON DELETE CASCADE
);

-- 5. KITAP_KATEGORI (Bridge: Kitap M:N Kategori)
CREATE TABLE kitap_kategori (
    kitap_id    INT NOT NULL,
    kategori_id INT NOT NULL,
    PRIMARY KEY (kitap_id, kategori_id),
    CONSTRAINT fk_kk_kitap
        FOREIGN KEY (kitap_id)
        REFERENCES kitap(kitap_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_kk_kategori
        FOREIGN KEY (kategori_id)
        REFERENCES kategori(kategori_id)
        ON DELETE CASCADE
);

-- 6. YAZAR
CREATE TABLE yazar (
    yazar_id     SERIAL PRIMARY KEY,
    ad           VARCHAR(100) NOT NULL,
    soyad        VARCHAR(100) NOT NULL,
    dogum_tarihi DATE,
    olum_tarihi  DATE,
    uyruk        VARCHAR(50),
    biyografi    TEXT
);

-- 7. KITAP_YAZAR (Bridge: Kitap M:N Yazar)
CREATE TABLE kitap_yazar (
    kitap_id INT NOT NULL,
    yazar_id INT NOT NULL,
    PRIMARY KEY (kitap_id, yazar_id),
    CONSTRAINT fk_ky_kitap
        FOREIGN KEY (kitap_id)
        REFERENCES kitap(kitap_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_ky_yazar
        FOREIGN KEY (yazar_id)
        REFERENCES yazar(yazar_id)
        ON DELETE CASCADE
);

-- 8. OGRENCI
CREATE TABLE ogrenci (
    ogrenci_id   SERIAL PRIMARY KEY,
    ogrenci_no   VARCHAR(15) NOT NULL UNIQUE,
    tc_kimlik    CHAR(11)    NOT NULL UNIQUE,
    ad           VARCHAR(50) NOT NULL,
    soyad        VARCHAR(50) NOT NULL,
    email        VARCHAR(100) UNIQUE,
    telefon      VARCHAR(20) NOT NULL,
    adres        VARCHAR(300),
    dogum_tarihi DATE NOT NULL,
    cinsiyet     CHAR(1) CHECK (cinsiyet IN ('E','K')),
    kayit_tarihi DATE DEFAULT CURRENT_DATE,
    aktif_mi     BOOLEAN DEFAULT TRUE
);

-- 9. GOREVLI
CREATE TABLE gorevli (
    gorevli_id         SERIAL PRIMARY KEY,
    tc_kimlik          CHAR(11)    NOT NULL UNIQUE,
    kullanici_adi      VARCHAR(50) NOT NULL UNIQUE,
    ad                 VARCHAR(50) NOT NULL,
    soyad              VARCHAR(50) NOT NULL,
    email              VARCHAR(100) UNIQUE,
    telefon            VARCHAR(20) NOT NULL,
    pozisyon           VARCHAR(15) NOT NULL
                       CHECK (pozisyon IN ('yonetici','memur','stajyer')),
    ise_baslama_tarihi DATE NOT NULL,
    maas               DECIMAL(10,2) CHECK (maas >= 0),
    sifre_hash         VARCHAR(255) NOT NULL,
    aktif_mi           BOOLEAN DEFAULT TRUE
);

-- 10. YETKI
CREATE TABLE yetki (
    yetki_id SERIAL PRIMARY KEY,
    yetki_ad VARCHAR(50) NOT NULL UNIQUE,
    aciklama VARCHAR(200),
    modul    VARCHAR(30) NOT NULL
);

-- 11. GOREVLI_YETKI (Bridge: Gorevli M:N Yetki)
CREATE TABLE gorevli_yetki (
    gorevli_id       INT NOT NULL,
    yetki_id         INT NOT NULL,
    veren_gorevli_id INT,
    verilme_tarihi   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (gorevli_id, yetki_id),
    CONSTRAINT fk_gy_gorevli
        FOREIGN KEY (gorevli_id)
        REFERENCES gorevli(gorevli_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_gy_yetki
        FOREIGN KEY (yetki_id)
        REFERENCES yetki(yetki_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_gy_veren
        FOREIGN KEY (veren_gorevli_id)
        REFERENCES gorevli(gorevli_id)
        ON DELETE SET NULL
);

-- 12. ODUNC_ALMA
CREATE TABLE odunc_alma (
    odunc_id             SERIAL PRIMARY KEY,
    ogrenci_id           INT NOT NULL,
    kopya_id             INT NOT NULL,
    gorevli_id           INT NOT NULL,
    odunc_tarihi         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    iade_tarihi_beklenen DATE NOT NULL,
    durum                VARCHAR(15) NOT NULL DEFAULT 'aktif'
                         CHECK (durum IN ('aktif','iade_edildi','gecikmis','kayip')),
    CONSTRAINT fk_odunc_ogrenci
        FOREIGN KEY (ogrenci_id)
        REFERENCES ogrenci(ogrenci_id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_odunc_kopya
        FOREIGN KEY (kopya_id)
        REFERENCES kitap_kopya(kopya_id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_odunc_gorevli
        FOREIGN KEY (gorevli_id)
        REFERENCES gorevli(gorevli_id)
        ON DELETE RESTRICT,
    CONSTRAINT chk_odunc_tarih
        CHECK (iade_tarihi_beklenen > odunc_tarihi::DATE)
);

-- 13. IADE (1:1 -> Odunc_Alma)
CREATE TABLE iade (
    iade_id      SERIAL PRIMARY KEY,
    odunc_id     INT NOT NULL UNIQUE,
    gorevli_id   INT NOT NULL,
    iade_tarihi  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    kitap_durumu VARCHAR(15) NOT NULL
                 CHECK (kitap_durumu IN ('iyi','hasarli','kayip')),
    notlar       TEXT,
    CONSTRAINT fk_iade_odunc
        FOREIGN KEY (odunc_id)
        REFERENCES odunc_alma(odunc_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_iade_gorevli
        FOREIGN KEY (gorevli_id)
        REFERENCES gorevli(gorevli_id)
        ON DELETE RESTRICT
);

-- 14. CEZA
CREATE TABLE ceza (
    ceza_id      SERIAL PRIMARY KEY,
    ogrenci_id   INT NOT NULL,
    odunc_id     INT NOT NULL,
    ceza_miktari DECIMAL(8,2) NOT NULL CHECK (ceza_miktari >= 0),
    ceza_nedeni  VARCHAR(15)  NOT NULL
                 CHECK (ceza_nedeni IN ('gecikme','hasar','kayip')),
    ceza_tarihi  DATE DEFAULT CURRENT_DATE,
    odendi_mi    BOOLEAN DEFAULT FALSE,
    odeme_tarihi DATE,
    CONSTRAINT fk_ceza_ogrenci
        FOREIGN KEY (ogrenci_id)
        REFERENCES ogrenci(ogrenci_id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_ceza_odunc
        FOREIGN KEY (odunc_id)
        REFERENCES odunc_alma(odunc_id)
        ON DELETE CASCADE
);

-- 15. REZERVASYON
CREATE TABLE rezervasyon (
    rezervasyon_id        SERIAL PRIMARY KEY,
    ogrenci_id            INT NOT NULL,
    kitap_id              INT NOT NULL,
    rezervasyon_tarihi    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    son_gecerlilik_tarihi DATE NOT NULL,
    durum                 VARCHAR(15) NOT NULL DEFAULT 'bekliyor'
                          CHECK (durum IN ('bekliyor','tamamlandi','iptal')),
    CONSTRAINT fk_rezerv_ogrenci
        FOREIGN KEY (ogrenci_id)
        REFERENCES ogrenci(ogrenci_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_rezerv_kitap
        FOREIGN KEY (kitap_id)
        REFERENCES kitap(kitap_id)
        ON DELETE CASCADE
);


-- BÖLÜM 2: DML

-- 1. YAYINEVI
INSERT INTO yayinevi (ad, adres, telefon, email, web_sitesi) VALUES
('Can Yayınları',        'Beyoğlu, İstanbul',   '02122525959', 'info@canyayinlari.com',  'www.canyayinlari.com'),
('İş Bankası Kültür',    'Beyoğlu, İstanbul',   '02122521151', 'info@iskultur.com.tr',   'www.iskultur.com.tr'),
('Yapı Kredi Yayınları', 'Beyoğlu, İstanbul',   '02122520093', 'info@ykykultur.com.tr',  'www.ykykultur.com.tr'),
('İletişim Yayınları',   'Cağaloğlu, İstanbul', '02125166000', 'info@iletisim.com.tr',   'www.iletisim.com.tr'),
('Doğan Kitap',          'Şişli, İstanbul',     '02124781111', 'info@dogankitap.com.tr', 'www.dogankitap.com.tr');

-- 2. KATEGORI (hiyerarşili)
INSERT INTO kategori (ust_kategori_id, ad, aciklama) VALUES
(NULL, 'Edebiyat',     'Edebi eserler'),
(NULL, 'Bilim',        'Bilimsel yayınlar'),
(NULL, 'Tarih',        'Tarihsel eserler'),
(1,    'Roman',        'Edebiyat > Roman'),
(4,    'Klasik Roman', 'Roman > Klasik Roman'),
(4,    'Bilim Kurgu',  'Roman > Bilim Kurgu'),
(3,    'Biyografi',    'Tarih > Biyografi');

-- 3. KITAP
-- KITAP.kategori_id = ana kategori; ek kategoriler KITAP_KATEGORI tablosunda
INSERT INTO kitap (isbn, kategori_id, yayinevi_id, baslik, yayin_yili, sayfa_sayisi, dil, baski_no, aciklama) VALUES
('9789750726330', 4, 1, 'Suç ve Ceza',           1866, 687,  'Türkçe', 12, 'Dostoyevski klasiği'),
('9789944886918', 4, 2, 'Sefiller',              1862, 1463, 'Türkçe', 8,  'Victor Hugo başyapıtı'),
('9789755108438', 6, 3, '1984',                  1949, 352,  'Türkçe', 15, 'George Orwell distopyası'),
('9789750838446', 2, 1, 'Sapiens',               2011, 464,  'Türkçe', 20, 'Yuval Noah Harari'),
('9789753422444', 4, 4, 'Simyacı',               1988, 184,  'Türkçe', 30, 'Paulo Coelho'),
('9789944884983', 4, 2, 'Tutunamayanlar',        1972, 724,  'Türkçe', 25, 'Oğuz Atay romanı'),
('9789753421645', 4, 5, 'Kürk Mantolu Madonna',  1943, 160,  'Türkçe', 40, 'Sabahattin Ali novellası');

-- 4. KITAP_KOPYA 
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

-- 5. KITAP_KATEGORI (Bridge)
INSERT INTO kitap_kategori (kitap_id, kategori_id) VALUES
(1, 4),  -- Suç ve Ceza → Roman
(1, 5),  -- Suç ve Ceza → Klasik Roman
(2, 4),  -- Sefiller → Roman
(2, 5),  -- Sefiller → Klasik Roman
(3, 6),  -- 1984 → Bilim Kurgu
(4, 2),  -- Sapiens → Bilim
(4, 3),  -- Sapiens → Tarih
(5, 4),  -- Simyacı → Roman
(6, 4);  -- Tutunamayanlar → Roman

-- 6. YAZAR 
INSERT INTO yazar (ad, soyad, dogum_tarihi, olum_tarihi, uyruk, biyografi) VALUES
('Fyodor',     'Dostoyevski', '1821-11-11', '1881-02-09', 'Rus',       'Rus edebiyatının klasik isimlerinden'),
('Victor',     'Hugo',        '1802-02-26', '1885-05-22', 'Fransız',   'Fransız romantik yazarı'),
('George',     'Orwell',      '1903-06-25', '1950-01-21', 'İngiliz',   'Distopik romanlarıyla tanınır'),
('Yuval Noah', 'Harari',      '1976-02-24',  NULL,        'İsrailli',  'İsrailli tarihçi ve yazar'),
('Paulo',      'Coelho',      '1947-08-24',  NULL,        'Brezilyalı','Brezilyalı romancı'),
('Oğuz',       'Atay',        '1934-10-12', '1977-12-13', 'Türk',      'Modern Türk edebiyatının öncülerinden'),
('Sabahattin', 'Ali',         '1907-02-25', '1948-04-02', 'Türk',      'Türk edebiyatının önemli isimlerinden');

-- 7. KITAP_YAZAR 
INSERT INTO kitap_yazar (kitap_id, yazar_id) VALUES
(1, 1),  -- Suç ve Ceza → Dostoyevski
(2, 2),  -- Sefiller → Victor Hugo
(3, 3),  -- 1984 → Orwell
(4, 4),  -- Sapiens → Harari
(5, 5),  -- Simyacı → Coelho
(6, 6),  -- Tutunamayanlar → Oğuz Atay
(7, 7);  -- Kürk Mantolu Madonna → Sabahattin Ali


-- 8. OGRENCI 
INSERT INTO ogrenci (ogrenci_no, tc_kimlik, ad, soyad, email, telefon, adres, dogum_tarihi, cinsiyet, aktif_mi) VALUES
('20230001', '12345678901', 'Ahmet',   'Yılmaz',  'ahmet.yilmaz@ogr.edu.tr',   '05321112233', 'Kadıköy, İstanbul',  '2003-05-12', 'E', TRUE),
('20230002', '12345678902', 'Elif',    'Demir',   'elif.demir@ogr.edu.tr',     '05322223344', 'Şişli, İstanbul',    '2003-08-20', 'K', TRUE),
('20230003', '12345678903', 'Mehmet',  'Kaya',    'mehmet.kaya@ogr.edu.tr',    '05323334455', 'Beşiktaş, İstanbul', '2002-11-05', 'E', TRUE),
('20220004', '12345678904', 'Zeynep',  'Şahin',   'zeynep.sahin@ogr.edu.tr',   '05324445566', 'Üsküdar, İstanbul',  '2002-03-18', 'K', TRUE),
('20220005', '12345678905', 'Mustafa', 'Aydın',   'mustafa.aydin@ogr.edu.tr',  '05325556677', 'Beykoz, İstanbul',   '2001-07-22', 'E', TRUE),
('20210006', '12345678906', 'Ayşe',    'Çelik',   'ayse.celik@ogr.edu.tr',     '05326667788', 'Bakırköy, İstanbul', '2001-01-30', 'K', TRUE),
('20190007', '12345678907', 'Can',     'Öztürk',  'can.ozturk@ogr.edu.tr',     '05327778899', 'Maltepe, İstanbul',  '1999-09-14', 'E', FALSE);

-- 9. GOREVLI 
INSERT INTO gorevli (tc_kimlik, kullanici_adi, ad, soyad, email, telefon, pozisyon, ise_baslama_tarihi, maas, sifre_hash) VALUES
('98765432101', 'ali.k',   'Ali',   'Kütüphaneci', 'ali.kutuphaneci@lib.edu.tr', '05411112233', 'yonetici', '2015-09-01', 45000.00, '$2a$10$abcdef1234567890hashexample1'),
('98765432102', 'fatma.e', 'Fatma', 'Erdem',       'fatma.erdem@lib.edu.tr',     '05412223344', 'memur',    '2018-03-15', 28000.00, '$2a$10$abcdef1234567890hashexample2'),
('98765432103', 'hasan.d', 'Hasan', 'Doğan',       'hasan.dogan@lib.edu.tr',     '05413334455', 'memur',    '2020-06-10', 26000.00, '$2a$10$abcdef1234567890hashexample3'),
('98765432104', 'selin.a', 'Selin', 'Aksoy',       'selin.aksoy@lib.edu.tr',     '05414445566', 'stajyer',  '2025-02-01', 15000.00, '$2a$10$abcdef1234567890hashexample4'),
('98765432105', 'burak.g', 'Burak', 'Güneş',       'burak.gunes@lib.edu.tr',     '05415556677', 'stajyer',  '2025-09-01', 15000.00, '$2a$10$abcdef1234567890hashexample5');

-- 10. YETKI
INSERT INTO yetki (yetki_ad, aciklama, modul) VALUES
('kitap_ekle',      'Kitap ekleme yetkisi',     'kitap'),
('kitap_sil',       'Kitap silme yetkisi',      'kitap'),
('odunc_ver',       'Ödünç verme yetkisi',      'odunc'),
('iade_al',         'İade alma yetkisi',        'odunc'),
('ceza_olustur',    'Ceza oluşturma yetkisi',   'ceza'),
('ceza_sil',        'Ceza silme yetkisi',       'ceza'),
('rapor_al',        'Rapor görüntüleme',        'rapor'),
('kullanici_yonet', 'Kullanıcı yönetimi',       'sistem');

-- 11. GOREVLI_YETKI (Bridge)
-- Yönetici (id=1) tüm yetkilere sahip
INSERT INTO gorevli_yetki (gorevli_id, yetki_id, veren_gorevli_id) VALUES
(1, 1, NULL),
(1, 2, NULL),
(1, 3, NULL),
(1, 4, NULL),
(1, 5, NULL),
(1, 6, NULL),
(1, 7, NULL),
(1, 8, NULL),
-- Memurlar: odunc + iade + ceza
(2, 3, 1),
(2, 4, 1),
(2, 5, 1),
(3, 3, 1),
(3, 4, 1),
-- Stajyerler: sadece odunc + iade
(4, 3, 1),
(4, 4, 1),
(5, 3, 1);

-- 12. ODUNC_ALMA
INSERT INTO odunc_alma (ogrenci_id, kopya_id, gorevli_id, odunc_tarihi, iade_tarihi_beklenen, durum) VALUES
(1, 2,  2, '2026-03-20 10:30:00', '2026-04-03', 'iade_edildi'),
(3, 5,  3, '2026-04-01 14:15:00', '2026-04-15', 'iade_edildi'),
(2, 9,  4, '2026-04-05 09:00:00', '2026-04-19', 'gecikmis'),
(4, 7,  2, '2026-04-10 11:45:00', '2026-04-24', 'aktif'),
(5, 8,  5, '2026-04-15 13:20:00', '2026-04-29', 'aktif'),
(6, 10, 3, '2026-03-01 15:00:00', '2026-03-15', 'gecikmis');

-- iade
INSERT INTO odunc_alma (ogrenci_id, kopya_id, gorevli_id, odunc_tarihi, iade_tarihi_beklenen, durum) VALUES
(1, 8, 2, '2026-02-15 10:00:00', '2026-03-01', 'iade_edildi'),
(3, 1, 3, '2026-02-20 14:00:00', '2026-03-06', 'iade_edildi');

-- 13. IADE
INSERT INTO iade (odunc_id, gorevli_id, iade_tarihi, kitap_durumu, notlar) VALUES
(1, 2, '2026-04-02 16:45:00', 'iyi',     'Zamanında iade edildi'),
(2, 3, '2026-04-20 10:00:00', 'hasarli', 'Kapak hafif yıpranmış'),
(6, 3, '2026-04-18 14:30:00', 'iyi',     '3 gün gecikmeli iade'),
(7, 2, '2026-02-28 11:00:00', 'iyi',     'Temiz iade'),
(8, 3, '2026-03-05 15:30:00', 'hasarli', 'Sayfa kıvrılmış');

-- 14. CEZA
INSERT INTO ceza (ogrenci_id, odunc_id, ceza_miktari, ceza_nedeni, ceza_tarihi, odendi_mi, odeme_tarihi) VALUES
(3, 2, 25.00,  'hasar',   '2026-04-20', TRUE,  '2026-04-20'),
(2, 3, 10.00,  'gecikme', '2026-04-20', FALSE, NULL),
(6, 6, 175.00, 'gecikme', '2026-04-18', TRUE,  '2026-04-19'),
(6, 6, 30.00,  'hasar',   '2026-04-18', FALSE, NULL),
(3, 8, 15.00,  'hasar',   '2026-03-05', FALSE, NULL);

-- 15. REZERVASYON
INSERT INTO rezervasyon (ogrenci_id, kitap_id, rezervasyon_tarihi, son_gecerlilik_tarihi, durum) VALUES
(4, 3, '2026-04-10 10:00:00', '2026-04-17', 'tamamlandi'),
(5, 1, '2026-04-18 14:30:00', '2026-04-25', 'bekliyor'),
(2, 6, '2026-04-15 09:15:00', '2026-04-22', 'bekliyor'),
(6, 4, '2026-04-05 11:00:00', '2026-04-12', 'iptal'),
(1, 7, '2026-04-20 16:45:00', '2026-04-27', 'bekliyor');

-- BÖLÜM 3: EK DML SORGULARI (Derste İşlenen Konular + Türevleri)

-- 3.1 SELECT - Temel Sorgular
-- * ile tüm kolonlar (ALL)
SELECT * FROM yayinevi;
SELECT * FROM kitap;
SELECT * FROM ogrenci;
SELECT * FROM gorevli;
SELECT * FROM odunc_alma;
-- Belirli kolonları seçme
SELECT kitap_id, baslik FROM kitap;
SELECT ad, soyad, email FROM ogrenci;
SELECT baslik, yayin_yili, sayfa_sayisi FROM kitap;

-- Kolon birleştirme (concat) ve takma ad (AS)
SELECT ogrenci_no, ad || ' ' || soyad AS tam_ad FROM ogrenci;
SELECT baslik AS kitap_adi, yayin_yili AS yil FROM kitap;


-- 3.2 WHERE - Filtreleme
-- Eşitlik (=)
SELECT * FROM ogrenci WHERE cinsiyet = 'E';
SELECT * FROM ogrenci WHERE cinsiyet = 'K';
SELECT * FROM kitap_kopya WHERE durum = 'musait';
SELECT * FROM odunc_alma WHERE durum = 'gecikmis';
SELECT * FROM gorevli WHERE pozisyon = 'yonetici';
-- Karşılaştırma (<, >, <=, >=, !=)
SELECT * FROM kitap WHERE yayin_yili > 2000;
SELECT * FROM kitap WHERE yayin_yili < 1950;
SELECT * FROM kitap WHERE sayfa_sayisi >= 500;
SELECT * FROM gorevli WHERE maas <= 20000;
SELECT * FROM ogrenci WHERE cinsiyet != 'E';
-- Boolean
SELECT * FROM ogrenci WHERE aktif_mi = TRUE;
SELECT * FROM ogrenci WHERE aktif_mi = FALSE;
SELECT * FROM ceza WHERE odendi_mi = FALSE;
-- AND / OR / NOT
SELECT * FROM ogrenci WHERE cinsiyet = 'K' AND aktif_mi = TRUE;
SELECT * FROM kitap WHERE yayin_yili > 1950 AND sayfa_sayisi > 300;
SELECT * FROM gorevli WHERE pozisyon = 'memur' OR pozisyon = 'yonetici';
SELECT * FROM ogrenci WHERE NOT cinsiyet = 'E';
-- IS NULL / IS NOT NULL
SELECT * FROM yazar WHERE olum_tarihi IS NULL;        -- yaşayan yazarlar
SELECT * FROM yazar WHERE olum_tarihi IS NOT NULL;    -- vefat etmiş yazarlar
SELECT * FROM ceza WHERE odeme_tarihi IS NULL;        -- henüz ödenmemiş
SELECT * FROM kategori WHERE ust_kategori_id IS NULL; -- kök kategoriler
-- BETWEEN - Aralık sorgusu
SELECT * FROM kitap WHERE yayin_yili BETWEEN 1900 AND 2000;
SELECT * FROM gorevli WHERE maas BETWEEN 20000 AND 40000;
SELECT * FROM kitap_kopya WHERE fiyat BETWEEN 50 AND 100;
-- IN / NOT IN
SELECT * FROM gorevli WHERE pozisyon IN ('memur', 'stajyer');
SELECT * FROM kitap_kopya WHERE durum IN ('kayip', 'hasarli');
SELECT * FROM odunc_alma WHERE durum IN ('aktif', 'gecikmis');
SELECT * FROM ogrenci WHERE cinsiyet NOT IN ('E');

-- 3.3 ORDER BY - Sıralama (ASC / DESC)
-- ASC -> Ascending (küçükten büyüğe) - varsayılan
SELECT * FROM kitap ORDER BY yayin_yili ASC;
SELECT * FROM kitap ORDER BY baslik;            -- ASC yazmasak da varsayılan
SELECT * FROM ogrenci ORDER BY ad, soyad;
-- DESC -> Descending (büyükten küçüğe)
SELECT * FROM kitap ORDER BY yayin_yili DESC;
SELECT * FROM gorevli ORDER BY maas DESC;
SELECT * FROM ogrenci ORDER BY kayit_tarihi DESC;
SELECT * FROM kitap ORDER BY sayfa_sayisi DESC;
-- Birden çok kolona göre sıralama
SELECT * FROM ogrenci ORDER BY cinsiyet ASC, soyad DESC;
SELECT * FROM gorevli ORDER BY pozisyon, maas DESC;
-- WHERE + ORDER BY
SELECT * FROM kitap WHERE yayin_yili > 1950 ORDER BY sayfa_sayisi DESC;

-- 3.4 LIMIT / OFFSET - Kayıt Sınırlama & Sayfalama
-- İlk 5 kayıt
SELECT * FROM kitap LIMIT 5;
-- En pahalı 3 kopya
SELECT * FROM kitap_kopya ORDER BY fiyat DESC LIMIT 3
-- En yüksek maaşlı görevli
SELECT * FROM gorevli ORDER BY maas DESC LIMIT 1;
-- Sayfalama (2. sayfa - 5'erli)
SELECT * FROM kitap ORDER BY kitap_id LIMIT 5 OFFSET 5;

-- 3.5 AGGREGATE FONKSIYONLAR (COUNT, MIN, MAX, AVG, SUM)
-- COUNT -> Bir tablodaki veri sayısı
SELECT COUNT(*) AS toplam_kitap FROM kitap;
SELECT COUNT(*) AS toplam_ogrenci FROM ogrenci;
SELECT COUNT(*) AS aktif_ogrenci FROM ogrenci WHERE aktif_mi = TRUE;
SELECT COUNT(*) AS kadin_ogrenci FROM ogrenci WHERE cinsiyet = 'K';
SELECT COUNT(*) AS musait_kopya FROM kitap_kopya WHERE durum = 'musait';
SELECT COUNT(DISTINCT yayinevi_id) AS farkli_yayinevi FROM kitap;
-- MIN -> Bir kolondaki min değer
SELECT MIN(yayin_yili) AS en_eski_kitap_yili FROM kitap;
SELECT MIN(maas) AS en_dusuk_maas FROM gorevli;
SELECT MIN(sayfa_sayisi) AS en_kisa_kitap FROM kitap;
SELECT MIN(ceza_miktari) AS en_dusuk_ceza FROM ceza;
SELECT MIN(fiyat) AS en_ucuz_kopya FROM kitap_kopya;
-- MAX -> Bir kolondaki max değer
SELECT MAX(yayin_yili) AS en_yeni_kitap_yili FROM kitap;
SELECT MAX(maas) AS en_yuksek_maas FROM gorevli;
SELECT MAX(sayfa_sayisi) AS en_uzun_kitap FROM kitap;
SELECT MAX(ceza_miktari) AS en_yuksek_ceza FROM ceza;
SELECT MAX(fiyat) AS en_pahali_kopya FROM kitap_kopya;
-- AVG -> Bir kolondaki ortalama değer
SELECT AVG(sayfa_sayisi) AS ortalama_sayfa FROM kitap;
SELECT AVG(maas) AS ortalama_maas FROM gorevli;
SELECT AVG(fiyat) AS ortalama_fiyat FROM kitap_kopya;
SELECT AVG(ceza_miktari) AS ortalama_ceza FROM ceza;
-- SUM -> Bir kolondaki tüm değerleri topla
SELECT SUM(ceza_miktari) AS toplam_ceza_tutari FROM ceza;
SELECT SUM(ceza_miktari) AS odenmemis_toplam FROM ceza WHERE odendi_mi = FALSE;
SELECT SUM(fiyat) AS kutuphane_toplam_deger FROM kitap_kopya;
SELECT SUM(maas) AS aylik_personel_gider FROM gorevli WHERE aktif_mi = TRUE;
SELECT SUM(sayfa_sayisi) AS toplam_sayfa FROM kitap;
-- Tüm istatistikler
SELECT
    COUNT(*)   AS gorevli_sayisi,
    MIN(maas)  AS en_dusuk_maas,
    MAX(maas)  AS en_yuksek_maas,
    AVG(maas)  AS ortalama_maas,
    SUM(maas)  AS toplam_maas
FROM gorevli;

-- 3.6 LIKE / ILIKE - Pattern Matching (Kalıp Eşleme)
-- Sembol 1 -> % (herhangi sayıda karakter)
-- 'S' ile başlayan kitaplar
SELECT * FROM kitap WHERE baslik LIKE 'S%';
-- 'r' ile biten kitaplar
SELECT * FROM kitap WHERE baslik LIKE '%r';
-- İçinde 'ce' geçen kitaplar
SELECT * FROM kitap WHERE baslik LIKE '%ce%';
-- 'A' ile başlayan öğrenci adları
SELECT * FROM ogrenci WHERE ad LIKE 'A%';
-- İçinde 'e' olan öğrenci adları
SELECT * FROM ogrenci WHERE ad LIKE '%e%';
-- '@ogr.edu.tr' ile biten emailler
SELECT * FROM ogrenci WHERE email LIKE '%@ogr.edu.tr';
-- '2023' ile başlayan öğrenci numaraları (2023 girişliler)
SELECT * FROM ogrenci WHERE ogrenci_no LIKE '2023%';

-- Sembol 2 -> _ (tek karakter)
-- 2. harfi 'h' olan isimler (Ahmet, Zehra vb.)
SELECT * FROM ogrenci WHERE ad LIKE '_h%';
-- Sondan bir önceki harfi 'a' olan isimler
SELECT * FROM ogrenci WHERE ad LIKE '%a_';
-- 4 karakterli isimler
SELECT * FROM ogrenci WHERE ad LIKE '____';
-- ILIKE -> Büyük/küçük harf duyarsız (LOWER + LIKE ile aynı)
SELECT * FROM kitap WHERE baslik ILIKE '%CEZA%';
SELECT * FROM kitap WHERE baslik ILIKE '%sapiens%';
SELECT * FROM yazar WHERE uyruk ILIKE 'rus';
SELECT * FROM ogrenci WHERE ad ILIKE '%er%';
-- NOT LIKE - uygun olmayanlar
SELECT * FROM kitap WHERE baslik NOT LIKE '%ceza%';
SELECT * FROM ogrenci WHERE email NOT LIKE '%@ogr.edu.tr';

-- 3.7 DISTINCT - Tekil (Benzersiz) Değerler
SELECT DISTINCT dil       FROM kitap;
SELECT DISTINCT uyruk     FROM yazar;
SELECT DISTINCT pozisyon  FROM gorevli;
SELECT DISTINCT durum     FROM kitap_kopya;
SELECT DISTINCT cinsiyet  FROM ogrenci;
SELECT DISTINCT modul     FROM yetki;

-- 3.8 UPDATE - Veri Güncelleme
-- Tek alanı güncelleme
UPDATE ogrenci
SET telefon = '05551234567'
WHERE ogrenci_id = 1;
-- Birden çok alanı güncelleme
UPDATE ogrenci
SET telefon = '05559998877', adres = 'Yeni Adres, İstanbul'
WHERE ogrenci_id = 2;
-- Kayıp kitap kopyasının durumunu güncelle
UPDATE kitap_kopya
SET durum = 'kayip'
WHERE kopya_id = 7;
-- Tüm memurlara %10 zam
UPDATE gorevli
SET maas = maas * 1.10
WHERE pozisyon = 'memur';
-- Cezayı ödendi olarak işaretle
UPDATE ceza
SET odendi_mi = TRUE, odeme_tarihi = CURRENT_DATE
WHERE ceza_id = 2;
-- Gecikmiş ödünçlerin durumunu otomatik güncelle
UPDATE odunc_alma
SET durum = 'gecikmis'
WHERE iade_tarihi_beklenen < CURRENT_DATE
  AND durum = 'aktif';
-- Öğrenciyi pasif yap (mezun olan)
UPDATE ogrenci
SET aktif_mi = FALSE
WHERE ogrenci_id = 7;
-- Email'i toplu güncelleme - domain değişikliği
UPDATE ogrenci
SET email = REPLACE(email, '@ogr.edu.tr', '@ogrenci.uni.edu.tr')
WHERE email LIKE '%@ogr.edu.tr';

-- 3.9 DELETE - Veri Silme
-- İptal edilmiş rezervasyonları sil
DELETE FROM rezervasyon WHERE durum = 'iptal';
-- Geçmiş rezervasyonları sil
DELETE FROM rezervasyon WHERE son_gecerlilik_tarihi < CURRENT_DATE;
-- Çok düşük cezaları sil
DELETE FROM ceza WHERE ceza_miktari < 5 AND odendi_mi = TRUE;
-- Belirli bir ID'yi sil (bağımlı yoksa çalışır)
DELETE FROM rezervasyon WHERE rezervasyon_id = 4;
-- FK RESTRICT hatası örneği (çalışmaz)
DELETE FROM ogrenci WHERE ogrenci_id = 1;
-- -> HATA: ogrenci_id=1 kayıdına odunc_alma ve rezervasyon tabloları bağlı

-- 3.10 JOIN - Tablolar Arası Sorgu (BONUS)
-- INNER JOIN - Kitap + Yayınevi
SELECT k.baslik, y.ad AS yayinevi
FROM kitap k
INNER JOIN yayinevi y ON y.yayinevi_id = k.yayinevi_id;
-- Kitap + Yazar
SELECT k.baslik, y.ad || ' ' || y.soyad AS yazar
FROM kitap k
JOIN kitap_yazar ky ON ky.kitap_id = k.kitap_id
JOIN yazar y        ON y.yazar_id  = ky.yazar_id;
-- Kitap + Kategori (ana)
SELECT k.baslik, kat.ad AS ana_kategori
FROM kitap k
JOIN kategori kat ON kat.kategori_id = k.kategori_id;
-- Aktif ödünçler: Öğrenci + Kitap + Görevli
SELECT
    o.ad || ' ' || o.soyad AS ogrenci,
    k.baslik AS kitap,
    g.ad || ' ' || g.soyad AS odunc_veren_gorevli,
    oa.odunc_tarihi::DATE AS odunc_gun,
    oa.iade_tarihi_beklenen,
    oa.durum
FROM odunc_alma oa
JOIN ogrenci o      ON o.ogrenci_id = oa.ogrenci_id
JOIN kitap_kopya kk ON kk.kopya_id  = oa.kopya_id
JOIN kitap k        ON k.kitap_id   = kk.kitap_id
JOIN gorevli g      ON g.gorevli_id = oa.gorevli_id
WHERE oa.durum IN ('aktif', 'gecikmis');

-- LEFT JOIN - Tüm öğrenciler ve varsa ödünçleri
SELECT o.ad, o.soyad, COUNT(oa.odunc_id) AS odunc_sayisi
FROM ogrenci o
LEFT JOIN odunc_alma oa ON oa.ogrenci_id = o.ogrenci_id
GROUP BY o.ogrenci_id, o.ad, o.soyad;

-- 3.11 GROUP BY / HAVING - Gruplama
-- Cinsiyete göre öğrenci sayısı
SELECT cinsiyet, COUNT(*) AS sayi
FROM ogrenci
GROUP BY cinsiyet;
-- Pozisyona göre görevli ve ortalama maaş
SELECT pozisyon, COUNT(*) AS sayi, AVG(maas) AS ort_maas
FROM gorevli
GROUP BY pozisyon
ORDER BY ort_maas DESC;
-- Her yayınevinin kitap sayısı
SELECT y.ad AS yayinevi, COUNT(k.kitap_id) AS kitap_sayisi
FROM yayinevi y
LEFT JOIN kitap k ON k.yayinevi_id = y.yayinevi_id
GROUP BY y.yayinevi_id, y.ad
ORDER BY kitap_sayisi DESC;
-- Her öğrencinin toplam cezası
SELECT
    o.ad || ' ' || o.soyad AS ogrenci,
    SUM(c.ceza_miktari) AS toplam_ceza
FROM ogrenci o
JOIN ceza c ON c.ogrenci_id = o.ogrenci_id
GROUP BY o.ogrenci_id, o.ad, o.soyad
ORDER BY toplam_ceza DESC;
-- HAVING -> gruplanmış veride filtre
-- En az 2 kitabı olan yayınevleri
SELECT y.ad, COUNT(k.kitap_id) AS kitap_sayisi
FROM yayinevi y
JOIN kitap k ON k.yayinevi_id = y.yayinevi_id
GROUP BY y.yayinevi_id, y.ad
HAVING COUNT(k.kitap_id) >= 2;
-- Toplam cezası 50 TL üstü olan öğrenciler
SELECT
    o.ad || ' ' || o.soyad AS ogrenci,
    SUM(c.ceza_miktari) AS toplam_ceza
FROM ogrenci o
JOIN ceza c ON c.ogrenci_id = o.ogrenci_id
GROUP BY o.ogrenci_id, o.ad, o.soyad
HAVING SUM(c.ceza_miktari) > 50;
-- Durum bazlı kopya sayısı
SELECT durum, COUNT(*) AS adet
FROM kitap_kopya
GROUP BY durum
ORDER BY adet DESC;
-- Uyruk bazlı yazar sayısı
SELECT uyruk, COUNT(*) AS yazar_sayisi
FROM yazar
GROUP BY uyruk
ORDER BY yazar_sayisi DESC;

-- 3.12 EK INSERT ÖRNEKLERİ - Veri Zenginleştirme
-- Yeni yayınevi
INSERT INTO yayinevi (ad, adres, telefon, email, web_sitesi) VALUES
('Everest Yayınları', 'Mecidiyeköy, İstanbul', '02122127500', 'info@everestyayinlari.com', 'www.everestyayinlari.com'),
('Metis Yayınları',   'Beyoğlu, İstanbul',     '02122456688', 'info@metiskitap.com',       'www.metiskitap.com');
-- Yeni kategori
INSERT INTO kategori (ust_kategori_id, ad, aciklama) VALUES
(NULL, 'Felsefe',    'Felsefi eserler'),
(NULL, 'Psikoloji',  'Psikoloji kitapları'),
(1,    'Şiir',       'Edebiyat > Şiir');
-- Yeni yazar
INSERT INTO yazar (ad, soyad, dogum_tarihi, uyruk, biyografi) VALUES
('Albert',   'Camus',    '1913-11-07', 'Fransız', 'Fransız yazar ve filozof'),
('Nazım',    'Hikmet',   '1902-01-15', 'Türk',    'Türk şair ve oyun yazarı'),
('Orhan',    'Pamuk',    '1952-06-07', 'Türk',    'Nobel ödüllü Türk yazar');
-- Yeni öğrenci
INSERT INTO ogrenci (ogrenci_no, tc_kimlik, ad, soyad, email, telefon, adres, dogum_tarihi, cinsiyet) VALUES
('20240008', '12345678908', 'Deniz',  'Arslan', 'deniz.arslan@ogr.edu.tr', '05328889900', 'Ataşehir, İstanbul', '2004-04-10', 'K'),
('20240009', '12345678909', 'Emre',   'Yıldız', 'emre.yildiz@ogr.edu.tr',  '05329990011', 'Beykoz, İstanbul',   '2004-06-22', 'E');

-- BÖLÜM 4: KONTROL SORGUSU - Tüm Tabloların Kayıt Sayısı
-- =============================================================
SELECT 'yayinevi'       AS tablo, COUNT(*) AS kayit FROM yayinevi
UNION ALL SELECT 'kategori',       COUNT(*) FROM kategori
UNION ALL SELECT 'kitap',          COUNT(*) FROM kitap
UNION ALL SELECT 'kitap_kopya',    COUNT(*) FROM kitap_kopya
UNION ALL SELECT 'kitap_kategori', COUNT(*) FROM kitap_kategori
UNION ALL SELECT 'yazar',          COUNT(*) FROM yazar
UNION ALL SELECT 'kitap_yazar',    COUNT(*) FROM kitap_yazar
UNION ALL SELECT 'ogrenci',        COUNT(*) FROM ogrenci
UNION ALL SELECT 'gorevli',        COUNT(*) FROM gorevli
UNION ALL SELECT 'yetki',          COUNT(*) FROM yetki
UNION ALL SELECT 'gorevli_yetki',  COUNT(*) FROM gorevli_yetki
UNION ALL SELECT 'odunc_alma',     COUNT(*) FROM odunc_alma
UNION ALL SELECT 'iade',           COUNT(*) FROM iade
UNION ALL SELECT 'ceza',           COUNT(*) FROM ceza
UNION ALL SELECT 'rezervasyon',    COUNT(*) FROM rezervasyon;
