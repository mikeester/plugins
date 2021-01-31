import ProjectVersions.openosrsVersion

buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    java // enables annotationProcessor and implementation in dependencies
}

project.extra["GithubUrl"] = "https://github.com/mikeester/plugins"

apply<BootstrapPlugin>()
apply<VersionPlugin>()

allprojects {
    group = "com.openosrs.externals"
    apply<MavenPublishPlugin>()
}

allprojects {
    apply<MavenPublishPlugin>()

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}

subprojects {
    group = "com.openosrs.externals"

    project.extra["PluginProvider"] = "mikeester"
    project.extra["ProjectSupportUrl"] = "https://discord.gg/Y7ysNpSpY2"
    project.extra["PluginLicense"] = "3-Clause BSD License"

    repositories {
        jcenter {
            content {
                excludeGroupByRegex("com\\.openosrs.*")
            }
        }

        exclusiveContent {
            forRepository {
                mavenLocal()
            }
            filter {
                includeGroupByRegex("com\\.openosrs.*")
                includeGroupByRegex("com\\.owain.*")
            }
        }
    }

    apply<JavaPlugin>()

    dependencies {
        // compileOnly("com.yworks:yguard:2.10.0")

        annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.12")
        annotationProcessor(group = "org.pf4j", name = "pf4j", version = "3.4.1")

        // compileOnly(group = "com.openosrs", name = "http-api", version = openosrsVersion)
        // compileOnly(group = "com.openosrs", name = "runelite-api", version = openosrsVersion)
        // compileOnly(group = "com.openosrs", name = "runelite-client", version = openosrsVersion)
        compileOnly("com.openosrs:runelite-api:$openosrsVersion+")
        compileOnly("com.openosrs.rs:runescape-api:$openosrsVersion+")
        compileOnly("com.openosrs:runelite-client:$openosrsVersion+")
        compileOnly("com.openosrs:http-api:$openosrsVersion+")

        compileOnly(group = "org.apache.commons", name = "commons-text", version = "1.9")
        compileOnly(group = "com.google.guava", name = "guava", version = "29.0-jre")
        compileOnly(group = "com.google.inject", name = "guice", version = "4.2.3", classifier = "no_aop")
        compileOnly(group = "com.google.code.gson", name = "gson", version = "2.8.6")
        compileOnly(group = "net.sf.jopt-simple", name = "jopt-simple", version = "5.0.4")
        compileOnly(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
        compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.12")
        compileOnly(group = "com.squareup.okhttp3", name = "okhttp", version = "4.8.1")
        compileOnly(group = "org.pf4j", name = "pf4j", version = "3.4.1")
        compileOnly(group = "io.reactivex.rxjava3", name = "rxjava", version = "3.0.6")
        compileOnly(group = "org.pushing-pixels", name = "radiance-substance", version = "2.5.1")
    }


    configure<PublishingExtension> {
        repositories {
            maven {
                url = uri("$buildDir/repo")
            }
        }
        publications {
            register("mavenJava", MavenPublication::class) {
                from(components["java"])
            }
        }
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }



    tasks {

        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<Jar> {
            doLast {
                copy {
                    from("./build/libs/")
                    // into(System.getProperty("user.home") + "/.runelite/externalmanager")
                    into("$rootDir/release/")
                }
            }
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            dirMode = 493
            fileMode = 420
        }

        register<Copy>("copyDeps") {
            into("./build/deps/")
            from(configurations["runtimeClasspath"])
        }
    }
}

fun isNonStable(version: String): Boolean {
    return listOf("ALPHA", "BETA", "RC").any {
        version.toUpperCase().contains(it)
    }
}
