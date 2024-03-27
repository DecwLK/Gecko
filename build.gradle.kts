import net.ltgt.gradle.errorprone.errorprone
import org.gradle.jvm.tasks.Jar

plugins {
    jacoco
    id("java")
    id("application")
    id("antlr")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("net.ltgt.errorprone") version "3.1.0"
    id("io.freefair.lombok") version "8.4"
    id("pmd")
}

group = "org.gecko"
version = "1.0.1"

javafx {
    version = "21.0.1"
    modules("javafx.controls")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.elk:org.eclipse.elk.core:0.8.1")
    implementation("org.eclipse.elk:org.eclipse.elk.alg.common:0.8.1")
    implementation("org.eclipse.elk:org.eclipse.elk.alg.force:0.8.1")
    implementation("org.eclipse.elk:org.eclipse.elk.alg.layered:0.8.1")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.16.1")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.testfx:testfx-junit5:4.0.18")
    testImplementation("org.hamcrest:hamcrest:2.2")

    errorprone("com.google.errorprone:error_prone_core:2.23.0")

    implementation("org.antlr:antlr4-runtime:4.13.1")
    antlr("org.antlr:antlr4:4.13.1")


    runtimeOnly("org.openjfx:javafx-graphics:$javafx.version")
//    runtimeOnly("org.openjfx:javafx-graphics:$javafx.version:win")
//    runtimeOnly("org.openjfx:javafx-graphics:$javafx.version:linux")
//    runtimeOnly("org.openjfx:javafx-graphics:$javafx.version:mac")
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone.disable("SameNameButDifferent")
    options.errorprone.disableWarningsInGeneratedCode.set(true)
}

val generateGrammarSource by tasks.existing(AntlrTask::class) {
    arguments.add("-visitor")
    arguments.add("-package")
    arguments.add("gecko.parser")
}

tasks.getByName("compileJava").dependsOn(generateGrammarSource)

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    // Exclude the package from the coverage report
    val excludes = listOf("gecko/parser/*") // Add other packages if needed
    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it).apply {
            excludes.forEach { ex ->
                exclude("**/$ex/**")
            }
        }
    }))
}

pmd {
    // Disabling build failure on rule violations
    isIgnoreFailures = true
}

application {
    mainClass.set("org.gecko.application.Main")
}

tasks.register<Jar>("uberJar") {
    archiveClassifier = "uber"

    from(sourceSets.main.get().output)
    manifest.attributes["Main-Class"] = "org.gecko.application.Main"

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
    exclude("META-INF/LICENSE")
}