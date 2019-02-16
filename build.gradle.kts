plugins {
    scala
}

group = "fi.jarimatti"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("org.erlang.otp", "jinterface", "1.6.1")
    implementation("io.netty", "netty-all", "4.1.33.Final")
    implementation("org.apache.logging.log4j", "log4j-api", "2.11.1")
    implementation("org.apache.logging.log4j", "log4j-api-scala_2.12", "11.0")
    
    implementation("org.apache.logging.log4j", "log4j-core", "2.11.1")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.9.+")
    implementation("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", "2.9.+")

    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}