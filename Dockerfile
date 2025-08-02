# 1. Aşama: Uygulamayı Maven ile build etmek (inşa etmek)
# Java 17 ve Maven'ın olduğu bir ortam kullanıyoruz
FROM maven:3.8-openjdk-17 AS build

# Kodumuzu kopyalamak için bir çalışma dizini oluşturuyoruz
WORKDIR /app

# Önce sadece pom.xml'i kopyalayıp bağımlılıkları indiriyoruz. Bu, her kod değişikliğinde kütüphanelerin tekrar indirilmesini engeller.
COPY pom.xml .
RUN mvn dependency:go-offline

# Şimdi projenin geri kalan kaynak kodunu kopyalıyoruz
COPY src ./src

# Projeyi testleri atlayarak paketliyoruz (.jar dosyası oluşturuyoruz)
RUN mvn clean package -DskipTests


# 2. Aşama: Sadece çalıştırılabilir uygulamayı içeren küçük imajı oluşturmak
# Sadece Java'yı çalıştırabilen (JRE) küçük bir ortam kullanıyoruz
FROM openjdk:17-jre-slim

# Çalışma dizini oluşturuyoruz
WORKDIR /app

# Bir önceki aşamada oluşturduğumuz .jar dosyasını bu yeni ortama kopyalıyoruz
COPY --from=build /app/target/utm-0.0.1-SNAPSHOT.jar ./app.jar

# Uygulamanın 8080 portunu dinleyeceğini belirtiyoruz
EXPOSE 8080

# Konteyner başladığında çalıştırılacak komut
CMD ["java", "-jar", "app.jar"]