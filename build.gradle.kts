plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("org.jetbrains.kotlin.jupyter.api") version "0.11.0-1"
    `maven-publish`
}

val ggdslVersion = "0.2.1-dev-0.2"
val ggdslEChartsVersion = "0.2.1-dev-0.2"

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
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("com.github.AndreiKingsley:ggdsl:$ggdslVersion")

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