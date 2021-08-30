plugins {
    id("com.android.application")
//    id("kotlin-android")
}

android {
    compileSdk = 36
//    buildToolsVersion = "30.0.3"
    namespace = "com.hvk.antalyadutyfree"

    defaultConfig {
        applicationId = "com.hvk.antalyadutyfree"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

//    kotlinOptions {
//        jvmTarget = "17"
//    }
    buildToolsVersion = "36.0.0"
}

dependencies {

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("com.google.code.gson:gson:2.13.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("org.jsoup:jsoup:1.21.1")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.3-native-mt")
}
