val apiCode: Int by rootProject.extra
val verCode: Int by rootProject.extra
val verName: String by rootProject.extra
val coreVerCode: Int by rootProject.extra
val coreVerName: String by rootProject.extra
val androidSourceCompatibility: JavaVersion by rootProject.extra
val androidTargetCompatibility: JavaVersion by rootProject.extra

plugins {
    id("java-library")
}

java {
    sourceCompatibility = androidSourceCompatibility
    targetCompatibility = androidTargetCompatibility
}

val generateTask = task<Copy>("generateJava") {
    val template = mapOf(
        "apiCode" to apiCode,
        "verCode" to verCode,
        "verName" to verName,
        "coreVerCode" to coreVerCode,
        "coreVerName" to coreVerName
    )
    inputs.properties(template)
    from("src/template/java")
    into("$buildDir/generated/java")
    expand(template)
}

sourceSets["main"].java.srcDir("$buildDir/generated/java")
tasks["compileJava"].dependsOn(generateTask)

// 👇 允许 java-library 模块解析并提取 AAR 中的 classes.jar
dependencies {
    artifactTypes.register("aar") {
        attributes.attribute(ArtifactAttributes.ARTIFACT_FORMAT, "jar")
    }
    compileOnly(lspatch.libxposed.api)
}
