plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
//    id("org.sonarqube") version "4.4.1.3373"
}

//sonarqube {
//    properties {
//        property("sonar.projectKey", "BookingService")
//        property("sonar.projectName", "BookingService Android")
//        property("sonar.host.url", System.getenv("SONAR_HOST"))
//        // Use sonar.token instead of sonar.login
//        property("sonar.token", System.getenv("SONAR_TOKEN"))
//
//        // Explicitly define source and test directories to avoid overlap
//        property("sonar.sources", "app/src/main")
//        property("sonar.tests", "app/src/test, app/src/androidTest")
//
//        // Define exclusions for generated code and tests from the main source set
//        property("sonar.exclusions", "**/*Test*.*, **/*androidTest*.*, **/build/**")
//
//        // Point to the JaCoCo report
//        property("sonar.coverage.jacoco.xmlReportPaths",
//            "app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
//
//        // Point to compiled class files
//        property("sonar.java.binaries", "app/build/intermediates/javac/debug/classes,app/build/tmp/kotlin-classes/debug")
//    }
//}