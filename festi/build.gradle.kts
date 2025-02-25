plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.gdg"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation ("org.apache.commons:commons-lang3:3.12.0")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")   // API 모듈
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")  // 구현체
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5") // JSON 처리 (Spring Boot 3+에서 필요)
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
	implementation ("javax.xml.bind:jaxb-api:2.3.1")
	implementation ("org.glassfish.jaxb:jaxb-runtime:2.3.1")}


tasks.withType<Test> {
	useJUnitPlatform()
}
