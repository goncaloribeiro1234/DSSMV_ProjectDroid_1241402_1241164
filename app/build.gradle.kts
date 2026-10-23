import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.lojadevideojogos"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.lojadevideojogos"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // LER AS CHAVES DO LOCAL.PROPERTIES PARA O CÓDIGO JAVA
        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(FileInputStream(localPropertiesFile))
        }
        buildConfigField("String", "RAWG_API_KEY", properties.getProperty("rawg.api.key", "\"\""))
        buildConfigField(
            "String",
            "RESTDB_API_KEY",
            properties.getProperty("restdb.api.key", "\"\"")
        )
        buildConfigField("String", "RAWG_API_KEY", properties.getProperty("rawg.api.key", "\"\""))
        buildConfigField("String", "RESTDB_API_KEY", properties.getProperty("restdb.api.key", "\"\""))
        buildConfigField("String", "RESTDB_BASE_URL", properties.getProperty("restdb.base.url", "\"\""))   // <- nova
        buildConfigField("String", "ADMIN_PIN", properties.getProperty("admin.pin", "\"1234\""))
    }

    // Ativar o ViewBinding e a geração do BuildConfig
    buildFeatures {
        viewBinding = true
        buildConfig = true
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // 1. Retrofit & Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // 2. Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // 3. Navigation Component
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    // 4. CameraX
    val cameraxVersion = "1.3.1"
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")

    // 5. ML Kit
    implementation("com.google.mlkit:text-recognition:16.0.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}