buildscript {
    allprojects {
        extra.apply {
            set("compose_version", "1.4.0-alpha01")
            set("hilt_version", "2.42")
            set("coroutines_version", "1.6.4")
            set("room_version","2.4.3")
        }
    }

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.42")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}