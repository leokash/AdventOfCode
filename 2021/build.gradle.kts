
plugins {
    kotlin("jvm")
}

group = Configurations.App.GROUP + ".2021"
version = Configurations.App.VERSION

tasks {
    sourceSets {
        val main by getting {
            dependencies {
                api(project(":utils"))
            }
        }
    }
}
