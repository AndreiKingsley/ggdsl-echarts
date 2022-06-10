plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("org.jetbrains.dokka") version "1.6.21"
    id("org.jetbrains.kotlin.jupyter.api") version "0.11.0-89-1"
    `maven-publish`
}

val ggdslVersion = "0.6.5-3"
val ggdslEChartsVersion = "0.6.5-3"

group = "com.andreikingsley"
version = ggdslEChartsVersion

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.5")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    implementation("com.github.AndreiKingsley:ggdsl:$ggdslVersion")

    testImplementation(kotlin("test"))

    //todo
   // implementation("com.beust:klaxon:5.5")
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.andreikingsley"
            artifactId = "ggdsl-echarts"
            version = ggdslEChartsVersion

            from(components["java"])
        }
    }
}
