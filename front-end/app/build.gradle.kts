plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.studytalk"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.studytalk"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,INDEX.LIST,io.netty.versions.properties}"
        }
    }
}

dependencies {
    val coroutineVersion = "1.7.3"
    val hiltVersion = "2.47"
    val roomVersion = "2.5.2"
    val ktorVersion = "2.3.3"

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //COMPOSE DEPENDENCIES
    // Add core of material icons
    implementation("androidx.compose.material:material-icons-core:1.4.3")
    // Add full set of material icons
    implementation("androidx.compose.material:material-icons-extended:1.4.3")
    // Add window size utils
    implementation("androidx.compose.material3:material3-window-size-class:1.1.1")
    // Integration with activities
    implementation("androidx.activity:activity-compose:1.7.2")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    // Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata:1.4.3")

    //COROUTINE
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")
    kapt("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

    //HILT
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    //ROOM
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")

    //FIREBASE
    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx")

    //KTOR
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktorVersion")
}

kapt {
    correctErrorTypes = true
}