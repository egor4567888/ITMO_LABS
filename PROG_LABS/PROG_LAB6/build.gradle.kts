plugins {
    id("java")
    application
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
}



tasks.test {
    useJUnitPlatform()
}
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
