plugins {
    id("java")
}

group = "org.yakupsayin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.squareup.moshi:moshi:1.12.0")
}

tasks.test {
    useJUnitPlatform()
}