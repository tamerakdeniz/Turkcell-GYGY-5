package com.turkcell.spring_cqrs.core.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

// JWT ile ilgili konfigürasyonları application.properties veya application.yml dosyasından okumak için kullanılan bir sınıf
// Bu sınıf, JWT'nin gizli anahtarı, geçerlilik süresi ve issuer gibi bilgileri içerir
// @ConfigurationProperties anotasyonu, bu sınıfın belirli bir prefix ile başlayan konfigürasyonları okuyacağını belirtir
// Örneğin, application.properties dosyasında security.jwt.secret=your_secret_key gibi bir konfigürasyon varsa, bu değer secret alanına atanır

// Neden Record değil de normal bir sınıf? 
// Çünkü bu sınıfın alanları değiştirilebilir (setter'lar var) ve default değerler atanmış durumda. 
// Record'lar ise immutable (değiştirilemez) yapılar olduğu için setter'lara izin vermezler ve tüm alanların constructor ile atanması gerekir. 
// Bu nedenle, bu tür konfigürasyon sınıfları genellikle normal sınıflar olarak tanımlanır.

@ConfigurationProperties(prefix="security.jwt")
public class JwtProperties {
    private String secret;
    private long expirationInSeconds = 360000;
    private String issuer = "spring-cqrs";


    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public long getExpirationInSeconds() {
        return expirationInSeconds;
    }
    public void setExpirationInSeconds(long expirationInSeconds) {
        this.expirationInSeconds = expirationInSeconds;
    }
    public String getIssuer() {
        return issuer;
    }
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    
}