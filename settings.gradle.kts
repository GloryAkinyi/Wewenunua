pluginManagement {
    repositories {
        gradlePluginPortal()
        google()   // 👈 required for AGP
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Wewenunua"
include(":app")
