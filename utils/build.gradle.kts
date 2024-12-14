
plugins {
    kotlin("jvm")
}

group = Configurations.App.GROUP + ".utils"
version = Configurations.App.VERSION

tasks {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(Dependencies.DATETIME)
            }
        }
    }
}
