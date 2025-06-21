rootProject.name = "cheolhyun-onboarding"

pluginManagement {
    repositories {
        gradlePluginPortal()   // ✅ 가장 중요!
        mavenCentral()
        google()               // Android가 있다면
    }
}

include("survey-app")
include("survey-domain")
include("survey-infrastructure")
