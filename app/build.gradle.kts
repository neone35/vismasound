plugins {
    // main
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    // firebase
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    namespace = "com.arturmaslov.vismasound"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arturmaslov.vismasound"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}


dependencies {
    // main
    implementation(Deps.coreKtx)
    implementation(Deps.lifecycleRuntimeKtx)
    api(Deps.kotlinxCoroutinesCore)
    api(Deps.kotlinxCoroutinesAndroid)
    implementation(Deps.activityKtx)
    // koin
    implementation(Deps.koinAndroid)
    // lifecycle
    implementation(Deps.lifecycleViewModelKtx)
    // compose
    implementation(Deps.activityCompose)
    implementation(Deps.composeBom)
    implementation(Deps.composeUi)
    implementation(Deps.composeUiGraphics)
    implementation(Deps.composeUiToolingPreview)
    implementation(Deps.composeMaterial3)
    implementation(Deps.landscapistGlide)
    implementation(Deps.landscapistPlaceholder)
    // network
    implementation(Deps.retrofit)
    implementation(Deps.retrofitConverterGson)
    implementation(Deps.okhttpLoggingInterceptor)
    implementation(Deps.stetho)
    implementation(Deps.stethoOkhttp3)
    // database
    implementation(Deps.roomKtx)
    ksp(Deps.roomCompiler)
    androidTestImplementation(Deps.Test.roomTesting)
    // Logging
    implementation(Deps.timber)
    // Firebase
    implementation(platform(Deps.firebaseBomPlatform))
    implementation(Deps.firebaseAnalyticsKtx)
    implementation(Deps.firebaseCrashlyticsKtx)
    implementation(Deps.firebasePerfKtx)

    // test
    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.testExtJunit)
    androidTestImplementation(Deps.Test.espressoCore)
    androidTestImplementation(Deps.Test.composeBomTest)
    androidTestImplementation(Deps.Test.composeUiTestJUnit4)
    debugImplementation(Deps.Test.composeUiTooling)
    debugImplementation(Deps.Test.composeUiTestManifest)
}

object Versions {
    const val coreKtx = "1.12.0"
    const val lifecycle = "2.7.0"
    const val coroutines = "1.7.3"
    const val activity = "1.8.2"
    const val koin = "3.3.2"
    const val compose = "2023.10.01"
    const val retrofit = "2.9.0"
    const val okhttp = "4.12.0"
    const val room = "2.6.1"
    const val timber = "5.0.1"
    const val firebaseBom = "32.7.1"
    const val landscapist = "2.2.10"
    const val stetho = "1.6.0"

    object Test {
        const val jUnit = "4.13.2"
        const val jUnitExt = "1.1.5"
        const val espressoCore = "1.1.5"
    }
}

object Deps {
    // main
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val kotlinxCoroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val kotlinxCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activity}"

    // koin
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"

    // lifecycle
    const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    // compose
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activity}"
    const val composeBom = "androidx.compose:compose-bom:${Versions.compose}"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeUiGraphics = "androidx.compose.ui:ui-graphics"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeMaterial3 = "androidx.compose.material3:material3"
    const val landscapistGlide = "com.github.skydoves:landscapist-glide:${Versions.landscapist}"
    const val landscapistPlaceholder =
        "com.github.skydoves:landscapist-placeholder:${Versions.landscapist}"

    // network
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitConverterGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val okhttpLoggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val stetho = "com.facebook.stetho:stetho:${Versions.stetho}"
    const val stethoOkhttp3 = "com.facebook.stetho:stetho-okhttp3:${Versions.stetho}"

    // database
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    // Logging
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    // Firebase
    const val firebaseBomPlatform = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
    const val firebaseAnalyticsKtx = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseCrashlyticsKtx = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebasePerfKtx = "com.google.firebase:firebase-perf-ktx"

    // test
    object Test {
        const val junit = "junit:junit:${Versions.Test.jUnit}"
        const val testExtJunit = "androidx.test.ext:junit:${Versions.Test.jUnitExt}"
        const val espressoCore =
            "androidx.test.espresso:espresso-core:${Versions.Test.espressoCore}"

        // compose
        const val composeBomTest = "androidx.compose:compose-bom:${Versions.compose}"
        const val composeUiTestJUnit4 = "androidx.compose.ui:ui-test-junit4"
        const val composeUiTooling = "androidx.compose.ui:ui-tooling"
        const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest"

        // database
        const val roomTesting = "androidx.room:room-testing:${Versions.room}"
    }
}