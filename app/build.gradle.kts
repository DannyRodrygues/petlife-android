import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")

    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { inputStream ->
            load(inputStream)
        }
    }
}

val supabaseUrl =
    localProperties.getProperty("SUPABASE_URL", "")

val supabasePublishableKey =
    localProperties.getProperty(
        "SUPABASE_PUBLISHABLE_KEY",
        "",
    )

android {
    namespace = "com.dannyrodrygues.petlife"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dannyrodrygues.petlife"

        minSdk = 26
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        /*
         * Supabase
         *
         * Os valores são lidos do local.properties.
         *
         * Não deixar URL/chave diretamente no código.
         */
        buildConfigField(
            "String",
            "SUPABASE_URL",
            "\"$supabaseUrl\"",
        )

        buildConfigField(
            "String",
            "SUPABASE_PUBLISHABLE_KEY",
            "\"$supabasePublishableKey\"",
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt",
                ),
                "proguard-rules.pro",
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

        /*
         * Necessário para acessarmos:
         *
         * BuildConfig.SUPABASE_URL
         * BuildConfig.SUPABASE_PUBLISHABLE_KEY
         */
        buildConfig = true
    }
}

dependencies {

    /*
     * Android
     */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    /*
     * Jetpack Compose
     */
    implementation(
        platform(libs.androidx.compose.bom),
    )

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    /*
     * Navigation
     */
    implementation(libs.androidx.navigation.compose)

    /*
     * Coil
     */
    implementation(libs.coil.compose)

    /*
     * Room
     */
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    ksp(libs.androidx.room.compiler)

    /*
     * Supabase
     */
    implementation(
        platform(libs.supabase.bom),
    )

    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.auth)
    implementation(libs.supabase.storage)

    /*
     * Ktor
     *
     * Engine HTTP utilizado pelo Supabase no Android.
     */
    implementation(libs.ktor.client.android)

    /*
     * Testes
     */
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(
        libs.androidx.espresso.core,
    )

    androidTestImplementation(
        platform(libs.androidx.compose.bom),
    )

    androidTestImplementation(
        libs.androidx.ui.test.junit4,
    )

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}