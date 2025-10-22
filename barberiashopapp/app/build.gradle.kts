plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.example.barberiashop_app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.barberiashop_app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        viewBinding = true
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
    implementation(libs.legacy.support.v4)
    val room_version = "2.8.1"

    implementation("androidx.room:room-runtime:$room_version")

    annotationProcessor("androidx.room:room-compiler:$room_version")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("com.google.android.material:material:1.12.0")
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    //implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}