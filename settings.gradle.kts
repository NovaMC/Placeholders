@file:Suppress("UnstableApiUsage")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://repo.kryptonmc.org/releases")
        maven("https://repo.opencollab.dev/maven-snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
        mavenLocal()
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "NovaPlaceholders"

sequenceOf(
    "common",
    "velocity",
    "paper"
).forEach {
    include(":$it")
}
