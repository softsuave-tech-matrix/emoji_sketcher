plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("maven-publish")
}

android {
    namespace = "softsuave.tech_matrix.emoji_sketcher"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}
/*publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "softsuave-tech-matrix"
            artifactId = "emoji-sketcher"
            version = "1.0.1"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}*/
afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("maven") {
                from(components["release"])
                groupId = "softsuave-tech-matrix"
                artifactId = "emoji_sketcher"
                version = "3.0.4"
            }
        }
        repositories {
            maven {
                url = uri("https://jitpack.io")
            }
        }
    }
}
dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation(project(mapOf("path" to ":app")))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}