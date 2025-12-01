
plugins {
    kotlin("jvm")
}

group = Configurations.App.GROUP + ".2025"
version = Configurations.App.VERSION

tasks {
    sourceSets {
        val main by getting {
            dependencies {
                api(project(":utils"))
                implementation(Dependencies.COROUTINES)
                implementation(Dependencies.COMBINATORICS)
            }
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xwhen-guards")
        freeCompilerArgs.add("-Xnon-local-break-continue")
        freeCompilerArgs.add("-Xmulti-dollar-interpolation")
    }
}
