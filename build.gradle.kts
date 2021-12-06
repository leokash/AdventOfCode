
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(Build.Plugins.kotlinGradle)
    }
}

allprojects {

    group = Configurations.App.group
    version = Configurations.App.version

    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}
