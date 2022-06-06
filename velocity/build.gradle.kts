dependencies {
    api(projects.common)
    implementation(projects.common)
    compileOnly(libs.tab)
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}
