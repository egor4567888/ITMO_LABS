plugins {
    id("java")
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application{
    mainClass = "com.egor456788.Main"
}
group = "com.egor456788"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("com.thoughtworks.xstream:xstream:1.4.20")
    implementation ("org.apache.logging.log4j:log4j-api:2.14.1")
    implementation ("org.apache.logging.log4j:log4j-core:2.14.1")
}



tasks.test {
    useJUnitPlatform()
}
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
tasks.shadowJar {
    archiveBaseName.set("client")
    archiveVersion.set("1.0.0")
    manifest {
        attributes["Main-Class"] = "com.egor456788.Main"
    }
}