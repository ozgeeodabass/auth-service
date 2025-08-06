# 🔐 Spring Boot JWT Authentication Service

Bu proje, Spring Boot kullanılarak geliştirilmiş, JWT tabanlı bir kimlik doğrulama servisidir. Kullanıcılar kayıt olabilir, giriş yapabilir ve giriş sonrası JWT token ile güvenli erişim sağlayabilir.

## 📦 Proje Yapısı

com.example.authservice
├── controllers
│ └── AuthController.java // Register ve login endpointleri
├── models
│ ├── User.java // User entity
│ ├── ERole.java // Kullanıcı rolleri enumu
│ └── requests / responses // DTO sınıfları
├── repositories
│ └── UserRepository.java
├── security
│ ├── jwt // JWT token üretimi, doğrulama ve filtreleme
│ ├── services // UserDetailsService implementasyonu
│ ├── SecurityConfig.java
│ └── PasswordEncoderConfig.java
├── services
│ └── AuthService.java // Giriş ve kayıt işlemleri

## 🔐 Güvenlik

- Kimlik doğrulama JWT (JSON Web Token) ile yapılmaktadır.
- Spring Security ile yetkilendirme sağlanır.
- Şifreler `BCryptPasswordEncoder` ile hash'lenir.
- Silinmiş kullanıcılar (`deleted=true`) giriş yapamaz.

## 🚀 Kullanılan Teknolojiler

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL (Docker ile)
- JWT
- Lombok

## 🔧 Nasıl Kurulur ve Çalıştırılır?

### 1. Repoyu Klonlayın

```bash
git clone https://github.com/kullaniciadi/auth-service.git
cd auth-service

src/main/resources/application.properties dosyasında aşağıdaki gibi MySQL bağlantısı sağlanmalıdır.
spring.datasource.url=jdbc:mysql://localhost:3306/authdb
spring.datasource.username=root
spring.datasource.password=*password*
spring.jpa.hibernate.ddl-auto=update

app.jwtSecret=YOUR_SECRET_KEY
app.jwtExpirationMs=86400000

Not: app.jwtSecret için rastgele Base64 bir string kullanmanız önerilir.

Proje ide ortamında açıldıktan ve gerekli yapılandırma yapıldıktan sonra sonra docker-compose up komutu ile docker containerların oluşması sağlanır.
./mvnw spring-boot:run komutu veya ide arayüzü ile proje çalıştırılır.

📬 API Endpointleri
🔹 Register
POST /api/auth/register

Request Body
{
  "username": "username",
  "email": "email@example.com",
  "password": "password",
  "gender": "gender",
  "name": "Name",
  "phoneNumber": "phoneNumber",
  "role": "ROLE_USER" or "ROLE_ADMIN"
}

Response
{
  "id": "uuid",
  "username": "username",
  "email": "email@example.com",
  "gender": "gender",
  "name": "Name",
  "phoneNumber": "phoneNumber",
  "role": "ROLE_USER"
}


🔹 Login (Giriş Yap)
POST /api/auth/signin

Request Body
{
  "username": "username",
  "password": "password"
}

Response
{
  "token": "JWT_TOKEN",
  "type": "Bearer",
  "id": "uuid",
  "username": "username",
  "email": "email@example.com",
  "gender": "gender",
  "name": "Name",
  "phoneNumber": "phoneNumber",
  "role": "ROLE_USER"
}
