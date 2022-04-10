enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://repo.essentialsx.net/releases/")
        maven("https://repo.kryptonmc.org/releases")
        maven("https://repo.kryptonmc.org/snapshots")
        maven("https://nexus.velocitypowered.com/repository/maven-public/")
        maven("https://repo.opencollab.dev/maven-snapshots/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.plo.su")
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "NovaPlaceholders"

include(":common")
include(":velocity")
include(":paper")
