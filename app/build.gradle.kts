plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.jg.moviesearch"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jg.moviesearch"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // API Keys from local.properties
        val localPropertiesFile = rootProject.file("local.properties")
        var kobisApiKey = ""
        var tmdbApiKey = ""
        
        if (localPropertiesFile.exists()) {
            val properties = localPropertiesFile.readText()
            kobisApiKey = properties.lines()
                .find { it.startsWith("KOBIS_API_KEY=") }
                ?.substringAfter("=") ?: ""
            tmdbApiKey = properties.lines()
                .find { it.startsWith("TMDB_API_KEY=") }
                ?.substringAfter("=") ?: ""
        }
        
        buildConfigField("String", "KOBIS_API_KEY", "\"$kobisApiKey\"")
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
        
        // Network Base URLs
        buildConfigField("String", "KOBIS_BASE_URL", "\"https://www.kobis.or.kr/kobisopenapi/webservice/rest/\"")
        buildConfigField("String", "TMDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
        buildConfigField("String", "TMDB_IMAGE_BASE_URL", "\"https://image.tmdb.org/t/p/w500\"")
        buildConfigField("String", "TMDB_BACKDROP_BASE_URL", "\"https://image.tmdb.org/t/p/w780\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
    
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
}

// Room 스키마 설정
room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // AppCompat & Fragment
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Retrofit & Network
    implementation(libs.retrofit)
    implementation(libs.retrofitKotlinxSerialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Image Loading
    implementation(libs.coil.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}