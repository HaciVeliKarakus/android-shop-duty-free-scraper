buildscript {
//    val kotlinVersion by extra("1.5.20")

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.11.0")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

//tasks.register<Delete>("clean") {
//    delete(rootProject.buildDir)
//}
