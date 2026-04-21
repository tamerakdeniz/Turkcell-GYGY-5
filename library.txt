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
