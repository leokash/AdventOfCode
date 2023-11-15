
@file:Suppress("UnusedPrivateMember")

plugins {
    kotlin("jvm")
}

group = Configurations.App.group + ".utils"
version = Configurations.App.version

tasks {
    sourceSets {
        val main by getting { }
    }
}
