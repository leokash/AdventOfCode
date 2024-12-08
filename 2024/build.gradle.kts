
plugins {
    kotlin("jvm")
}

group = Configurations.App.GROUP + ".2024"
version = Configurations.App.VERSION

tasks {
    sourceSets {
        val main by getting {
            dependencies {
                api(project(":utils"))
                implementation(Deps.COROUTINES)
                implementation(Deps.COMBINATORICS)
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
