-- Alias -> Takma ad
SELECT * FROM customers c where contact_name LIKE '%a%';

-- Joinler
SELECT * FROM orders o
inner join customers c
on o.customer_id = c.customer_id;
--

SELECT * FROM customers c;

INSERT INTO customers(customer_id, company_name, contact_name, contact_title, address, city, postal_code,country,phone,fax)
VALUES ('HALIT', 'Deneme', 'Halit Kalaycı', 'Abc','Abc','İstanbul','34788','Türkiye','+90', 'abc');

--

SELECT * FROM orders o
RIGHT JOIN employees e
ON o.employee_id = e.employee_id;

--

SELECT * FROM orders o
INNER JOIN customers c
on o.customer_id = c.customer_id
INNER join order_details od
on o.order_id = od.order_id
INNER JOIN products p
on od.product_id = p.product_id
WHERE od.quantity>10
order by c.contact_name;

--

-- GROUP BY
SELECT c.country, c.city, COUNT(*) 
FROM customers c
GROUP BY c.country, c.city;

SELECT c.country, c.city, COUNT(*) 
FROM customers c
GROUP BY c.country, c.city
ORDER BY COUNT(*) DESC;
--

SELECT * FROM customers;
--
SELECT s.company_name, COUNT(*) FROM shippers s
INNER JOIN orders o
on s.shipper_id = o.ship_via
GROUP BY s.shipper_id, s.company_name;

SELECT s.company_name, COUNT(o.order_id) FROM shippers s
LEFT JOIN orders o
on s.shipper_id = o.ship_via
GROUP BY s.shipper_id, s.company_name -- GROUP BY Kullanılıyorsa WHERE yerine HAVING kullanılır.
HAVING COUNT(o.order_id)>250
ORDER BY COUNT(o.order_id) DESC;
--

--

SELECT c.customer_id, COUNT(o.order_id) AS total_orders FROM customers c
INNER JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.contact_name
HAVING COUNT(o.order_id) > 10
ORDER BY total_orders DESC;

--

-- Toplam Cirosu 50k'dan büyük müşteriler

SELECT c.customer_id, c.company_name,
SUM(od.unit_price * od.quantity) AS total_revenue
FROM customers c
INNER JOIN orders o ON c.customer_id = o.customer_id
INNER JOIN order_details od ON o.order_id = od.order_id
GROUP BY c.customer_id, c.company_name
HAVING SUM(od.unit_price * od.quantity) > 50000
ORDER BY total_revenue DESC;

--

-- Her kategori için en az 5 farklı ürün satan kategoriler

SELECT c.category_id, c.category_name, COUNT(DISTINCT od.product_id) AS product_count
FROM categories c
INNER JOIN products p ON c.category_id = p.category_id
INNER JOIN order_details od ON p.product_id = od.product_id
GROUP BY c.category_id, c.category_name
HAVING COUNT(DISTINCT od.product_id) >= 5;

--

-- Çalışan bazlı toplam satış tutarı (birim fiyatı)

SELECT e.employee_id, e.first_name, e.last_name, SUM(od.unit_price * od.quantity) AS total_sales
FROM employees e
INNER JOIN orders o ON e.employee_id = o.employee_id
INNER JOIN order_details od ON o.order_id = od.order_id
GROUP BY e.employee_id, e.first_name, e.last_name
ORDER BY total_sales DESC;

--

-- Sayfalama

SELECT * FROM products p
LIMIT 10 OFFSET 5;

--

-- 1-Sayfa Boyutu?
-- 2-Aktif Sayfa?
-- 1.sayfa, sayfa başı 10 element

-- LIMIT {sayfa_basi_element} OFFSET {(aktif_Sayfa-1) * sayfa_basi_element}
SELECT * FROM products p 
LIMIT 10 OFFSET 0;

SELECT * FROM products p 
LIMIT 10 OFFSET 5;





