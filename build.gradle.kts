plugins {
    java
    id("com.oop.minecraftservers") version "1.1"
    id("com.github.johnrengelman.shadow") version "4.0.4"
    `maven-publish`
}

group = "com.oop.intelimenus"
version = "1.2"

repositories {
    jcenter()
    mavenLocal()
    maven(url="https://repo.codemc.org/repository/nms/")
}

dependencies {
    compile("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
    implementation("org.projectlombok:lombok:1.18.12")
    implementation("com.oop.orangeengine:item:4.8")
    implementation("com.oop.orangeengine:engine:4.8")
    annotationProcessor("org.projectlombok:lombok:1.18.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveFileName.set("InteliMenusPlugin.jar")
        destinationDirectory.set(File("out"))
    }
}

val config = extensions.findByType(com.oop.minecraftservers.ServerRunnerConfiguration::class)!!
val shadowJar = tasks.findByName("shadowJar") as com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
config.jarPath = shadowJar.destinationDirectory.asFile.get().listFiles()!![0]
config.version = "1.12.2"
config.pluginName = "InteliMenusPlugin"

tasks {
    build {
        dependsOn(shadowJar)
        dependsOn(publish)
    }

    findByName("server-run")!!.dependsOn(shadowJar)
    findByName("publish")!!.dependsOn(shadowJar)
}

publishing {
    repositories {
        mavenLocal()
        if (project.hasProperty("mavenUsername")) {
            maven {
                credentials {
                    username = project.property("mavenUsername") as String
                    password = project.property("mavenPassword") as String
                }

                setUrl("https://repo.codemc.org/repository/maven-releases/")
            }
        }
    }

    publications {
        register("mavenJava", MavenPublication::class) {
            artifact(File("${project.projectDir}/out/").listFiles()!![0])
            groupId = "com.oop"
            artifactId = "intelimenus"
            version = "${project.version}"
        }
    }
}