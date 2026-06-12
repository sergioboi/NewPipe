/*
 * SPDX-FileCopyrightText: 2025 NewPipe e.V. <https://newpipe-ev.de>
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "NewPipe"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://repo.clojars.org")
    }
}
include(":app") // androidApp
include(":desktopApp")
include("shared")

buildCache {
    local {
        // Disable local buildcache to maximize use of BuildFetch remote cache.
        isEnabled = false
    }

    remote<HttpBuildCache> {
        // On CI it's easiest to provide Env Vars
        // On local macOS it's easier to provide ~/.gradle/gradle.properties for consistency between Terminal & IDE
        val remoteUrl: String? = "NEWPIPE_GRADLE_REMOTE_CACHE_URL"
            .let { System.getenv(it) ?: providers.gradleProperty(it).orNull }

        val user: String? = "NEWPIPE_GRADLE_REMOTE_CACHE_USER"
            .let { System.getenv(it) ?: providers.gradleProperty(it).orNull }

        val token: String? = "NEWPIPE_GRADLE_REMOTE_CACHE_TOKEN"
            .let { System.getenv(it) ?: providers.gradleProperty(it).orNull }

        if (remoteUrl != null && user != null && token != null) {
            isEnabled = true

            url = uri(remoteUrl.trim())

            credentials {
                username = user.trim()
                password = token.trim()
            }

            isPush = true
        } else {
            isEnabled = false
        }
    }
}

// Use a local copy of NewPipe Extractor by uncommenting the lines below.
// We assume, that NewPipe and NewPipe Extractor have the same parent directory.
// If this is not the case, please change the path in includeBuild().

//    includeBuild("../NewPipeExtractor") {
//        dependencySubstitution {
//            substitute(module("com.github.TeamNewPipe:NewPipeExtractor"))
//                .using(project(":extractor"))
//        }
//    }
