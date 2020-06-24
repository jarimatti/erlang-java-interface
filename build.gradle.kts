plugins {
    java
}

group = "fi.jarimatti"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("org.erlang.otp", "jinterface", "1.6.1")
    implementation("io.netty", "netty-all", "4.1.50.Final")
    implementation("org.apache.logging.log4j", "log4j-api", "2.13.3")
    implementation("org.apache.logging.log4j", "log4j-core", "2.13.3")

    testImplementation("org.junit.jupiter", "junit-jupiter", "5.6.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
}
