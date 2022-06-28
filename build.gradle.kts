plugins {
    id("placeholders.parent")
}

val platforms = setOf(
    projects.paper,
    projects.velocity
).map { it.dependencyProject }

subprojects {
    when (this) {
        in platforms -> plugins.apply("placeholders.platform-conventions")
        else -> plugins.apply("placeholders.base-conventions")
    }
}