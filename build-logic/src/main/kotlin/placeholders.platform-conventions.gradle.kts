plugins {
    id("placeholders.base-conventions")
    id("com.github.johnrengelman.shadow")
}

tasks {
    shadowJar {
        archiveFileName.set("${rootProject.name}-${project.name}-${project.version}.jar")

        // Exclude unneeded Configurate libraries
        exclude("com/google/**")
        exclude("javax/**")
        exclude("org/aopalliance/**")
        exclude("org/checkerframework/**")
        exclude("org/codehaus/**")
        exclude("org/yaml/**")
        // Exclude unneeded Reflections libraries
        exclude("org/slf4j/**")
        exclude("javassist/util/**")
        exclude("javassist/tools/**")
        exclude("javassist/scopedpool/**")
        exclude("javassist/runtime/**")
        exclude("javassist/expr/**")
        exclude("javassist/convert/**")
        exclude("javassist/compiler/**")

        if (this.project.equals(findProject(":paper"))) {
            relocate("ninja.leaping.configurate", "xyz.novaserver.placeholders.libs.ninja.leaping.configurate")
        }
        relocate("javassist", "xyz.novaserver.placeholders.libs.javassist")
        relocate("org.reflections", "xyz.novaserver.placeholders.libs.org.reflections")
    }
    build {
        dependsOn(shadowJar)
    }
}
