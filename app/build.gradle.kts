plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    jacoco
//    id ("org.sonarqube")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sonarqube)
}

android {
    namespace = "com.example.bookingservice"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bookingservice"
        minSdk = 31
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
        debug {
            enableUnitTestCoverage = true  // Ensure coverage is enabled
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
        testOptions.unitTests.isIncludeAndroidResources = true
    }
    namespace = "com.example.bookingservice"
}

jacoco {
    toolVersion = "0.8.10"
}

//tasks.named("test") {
//    useJUnitPlatform()
//    testLogging {
//        events("passed", "skipped", "failed")
//    }
//    finalizedBy(tasks.jacocoTestReport)
//}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    
    reports {
        xml.required.set(true) // Required for SonarQube
        html.required.set(true)
        csv.required.set(false)
    }

    // Configure class directories to analyze
    val debugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
        exclude(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "**/ui/theme/**",
            "**/ui/**",
            "android/**",
            "**/MainKt.class",
            "**/MainActivity.class",
            "**/MainActivityKt.class",
            "**/MainActivityKt*",
            "**/MainActivity*",
            "**/BookingViewModel*",
            "**/RoomViewModel*",
            "**/BuildingViewModel*",
            "**/BookingRepository*",
            "**/AuthViewModel.login**",
            "**/AuthViewModel.updatePassword**",
            "**/Create*",
            "**/Update*",
            "**/RoomRepository*",
            "**/UserRepository*",
            "**/BuildingRepository*",
            "**/model/**",
            // Add other excludes as needed
        )
    }
    val executionDataFiles = fileTree(buildDir) {
        include(
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            "outputs/code_coverage/debugAndroidTest/connected/*coverage.ec"
        )
    }
    
    val mainSrc = "${project.projectDir}/src/main/java"
    
    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(executionDataFiles)
}

tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
//    dependsOn("assembleUnitTest")

    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}

// Existing plugins and configurations...

// Add or modify JaCoCo configuration
tasks.withType<Test> {
    // Configure test task - ensure tests run before coverage analysis
    finalizedBy("jacocoTestReport")
}

tasks.withType<JacocoReport> {
    // Make sure we have proper dependencies
    dependsOn(
        "testDebugUnitTest"
//        "createDebugCoverageReport",
//        "checkDebugAndroidTestAarMetadata",
//        "generateDebugAndroidTestResValues",
//        "mergeDebugAndroidTestResources",
//        "processDebugAndroidTestResources",
//        "processDebugAndroidTestManifest"
    )
    
    reports {
        xml.required.set(true) // Required for SonarQube
        html.required.set(true) // Useful for local inspection
    }
    
    // Configure source sets and class files properly
    sourceDirectories.from(
        files(
            "$projectDir/src/main/java",
            "$projectDir/src/main/kotlin"
        )
    )
    
    classDirectories.from(
        fileTree("$buildDir/intermediates/javac/debug") {
            exclude(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/ui/**",
                "**/*Test*.*"
            )
        }
    )
    
    executionData.from(
        fileTree(project.buildDir) {
            include(
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "outputs/code_coverage/debugAndroidTest/connected/*coverage.ec"
            )
        }
    )
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}

sonarqube {
    properties {
        property("sonar.projectKey", "BookingService")
        property("sonar.projectName", "BookingService Android")
        property("sonar.host.url", "http://158.160.153.21:9000")
        property("sonar.login", "sqp_5ca386079f2fae032d4de8664d295bef4bbad6b5")

        // Skip compile to avoid dependency issues
        property("sonar.gradle.skipCompile", true)

        // Use mutable collections
        property("sonar.sources", arrayListOf(
            "src/main/java"
        ))

        property("sonar.tests", arrayListOf(
            "src/test/java"
        ))

        // JaCoCo report path
        property("sonar.coverage.jacoco.xmlReportPaths",
            "build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")

        // Java binaries
        property("sonar.java.binaries", arrayListOf(
            "build/intermediates/javac/debug/classes",
            "build/tmp/kotlin-classes/debug"
        ))
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.okhttp)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.legacy.support.v4)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.compose.ui.ui.test.junit42)
    testImplementation(libs.core.testing)
    testImplementation(libs.truth)
    testImplementation(libs.mockk.mockk)
    testImplementation(libs.ui.test.manifest)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test.v180)
    testImplementation(libs.androidx.compose.ui.ui.test.junit4)
    testImplementation(libs.androidx.ui.graphics.android)
    testImplementation("androidx.compose.ui:ui-test-junit4:1.5.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("androidx.test:runner:1.5.2")
    testImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.4")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(kotlin("test"))
}