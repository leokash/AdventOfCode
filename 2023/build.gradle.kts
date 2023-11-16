
plugins {
    kotlin("jvm")
}

group = Configurations.App.group + ".2023"
version = Configurations.App.version

tasks {
    sourceSets {
        val main by getting {
            dependencies {
                api(project(":utils"))
            }
        }
    }
}
