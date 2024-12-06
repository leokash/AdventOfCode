
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(Build.Plugins.ClassPaths.KOTLIN_GRADLE)
    }
}

allprojects {
    group = Configurations.App.GROUP
    version = Configurations.App.VERSION

    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}
