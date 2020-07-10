plugins {
    java
}

group = "com.oop.intelimenus"
version = "1.0"

repositories {
    jcenter()
    maven(url="https://repo.codemc.org/repository/nms/")
}

dependencies {
    implementation("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
    implementation("org.projectlombok:lombok:1.18.12")
    annotationProcessor("org.projectlombok:lombok:1.18.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}