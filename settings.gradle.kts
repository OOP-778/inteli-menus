rootProject.name = "Inteli-menus"

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.oop") {
                useModule("com.oop:minecraftservers:${requested.version}")
            }
        }
    }
    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
        gradlePluginPortal()
    }
}
