plugins {
    java
    id("org.springframework.boot") version "3.3.5" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
}

val springBootVersion = "3.3.5"

group = "com.stackwork360"
version = "0.1.0-SNAPSHOT"

allprojects {
    group = rootProject.group
    version = rootProject.version
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter:5.10.3")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

configure(subprojects.filter { it.path.startsWith(":services:") }) {
    apply(plugin = "org.springframework.boot")

    dependencies {
        "implementation"(project(":libs:common"))
        "implementation"(project(":libs:events"))
        "implementation"(project(":libs:security"))
        "implementation"(project(":libs:web"))
        "implementation"("org.springframework.boot:spring-boot-starter-actuator")
        "implementation"("org.springframework.boot:spring-boot-starter-validation")
        "implementation"("org.springframework.boot:spring-boot-starter-web")
        "implementation"("org.springframework.kafka:spring-kafka")
        "runtimeOnly"("org.postgresql:postgresql")
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "testImplementation"("org.springframework.kafka:spring-kafka-test")
    }
}

configure(subprojects.filter { it.path.startsWith(":libs:") }) {
    dependencies {
        "implementation"("org.springframework.boot:spring-boot-starter")
        "implementation"("org.springframework.boot:spring-boot-starter-validation")
    }
}

project(":libs:web") {
    dependencies {
        "implementation"("org.springframework.boot:spring-boot-starter-web")
    }
}

project(":libs:web") {
    dependencies {
        "implementation"("org.springframework.boot:spring-boot-starter-web")
    }
}
