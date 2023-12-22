import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val testContainerVersion: String by project
val kotlinLoggingVersion: String by project
val awaitilityVersion: String by project
val mockserverClientVersion: String by project

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.springdoc.openapi-gradle-plugin") version "1.6.0"
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
}

group = "com.gft.sample"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	maven { url = uri("https://mvnrepository.com") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.postgresql:postgresql:42.5.4")
	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")
	implementation("io.projectreactor:reactor-core:3.6.1")
	implementation("org.testcontainers:testcontainers:$testContainerVersion")
	implementation("org.testcontainers:junit-jupiter:$testContainerVersion")
	implementation("org.testcontainers:mockserver:$testContainerVersion")
	implementation("org.testcontainers:kafka:$testContainerVersion")
	implementation("org.testcontainers:postgresql:$testContainerVersion")
	implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
	implementation("org.awaitility:awaitility-kotlin:$awaitilityVersion")
	implementation("org.mock-server:mockserver-client-java:$mockserverClientVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.rest-assured:spring-mock-mvc-kotlin-extensions")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
