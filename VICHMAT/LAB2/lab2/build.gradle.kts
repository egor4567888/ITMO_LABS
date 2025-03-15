plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.jzy3d.org/releases")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.springframework:spring-aspects:5.2.8.RELEASE")
    implementation("org.jfree:jfreechart:1.5.3")
    implementation("org.jzy3d:jzy3d-api:1.0.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}