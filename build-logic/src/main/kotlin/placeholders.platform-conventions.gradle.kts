plugins {
    id("placeholders.base-conventions")
    id("com.github.johnrengelman.shadow")
}

tasks {
    shadowJar {
        archiveFileName.set("placeholders-${project.name}-${project.version}.jar")

        // Exclude unneeded Configurate libraries
        exclude("com/google/**")
        exclude("javax/**")
        exclude("org/aopalliance/**")
        exclude("org/checkerframework/**")
        exclude("org/codehaus/**")
        exclude("org/yaml/**")

        if (this.project.equals(findProject(":paper"))) {
            relocate("ninja.leaping.configurate", "xyz.novaserver.placeholders.libs.ninja.leaping.configurate")
        }
    }
    build {
        dependsOn(shadowJar)
    }
}
