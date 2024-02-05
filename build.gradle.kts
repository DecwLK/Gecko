import net.ltgt.gradle.errorprone.errorprone

plugins {
    id("java")
    id("application")
    id("antlr")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("net.ltgt.errorprone") version "3.1.0"
    id("io.freefair.lombok") version "8.4"
}

group = "org.gecko"
version = "0.1"

javafx {
    version = "21.0.1"
    modules("javafx.controls")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.16.1")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    errorprone("com.google.errorprone:error_prone_core:2.23.0")

    implementation("org.antlr:antlr4-runtime:4.13.1")
    antlr("org.antlr:antlr4:4.13.1")
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
}

application {
    mainClass.set("org.gecko.application.Main")
}