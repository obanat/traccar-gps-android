plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.floatingball"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.floatingball"
        minSdk = 17
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.support.v4)
    implementation(libs.appcompat.v7)
    //implementation(libs.appcompat)
    //implementation(libs.material)
    //implementation(libs.activity)
    //implementation(libs.constraintlayout)
}