# [Ant] plug-in for [Kobalt]

[![Build Status](https://travis-ci.org/DevCharly/kobalt-ant.svg?branch=master)](https://travis-ci.org/DevCharly/kobalt-ant)
[![Download](https://api.bintray.com/packages/devcharly/maven/kobalt-ant/images/download.svg) ](https://bintray.com/devcharly/maven/kobalt-ant/_latestVersion)

Supports definition of per-project Kobalt tasks (similar to Ant targets)
and execution of [Ant tasks].

```kotlin
val plugins = plugins("com.devcharly:kobalt-ant:")

val project = project {
    antTask("hello") {
        echo("Hello World")
    }

    antTask("copy-dir1") {
        mkdir("dir1")
        copy(todir = "dir1") {
            fileset(dir = "dir2")
                include(name = "**/*.txt")
            }
        }
    }
}
```

The primary goal is support migration of Ant builds to Kobalt.
You can use your custom Ant tasks in Kobalt. Or use built-it Ant tasks.

[Examples](examples/kobalt/src/Build.kt)


## Supported Ant features

See [Kotlin Ant DSL](https://github.com/DevCharly/kotlin-ant-dsl#supported-ant-features)


## Requirements

Kobalt 0.834 or later


[Kobalt]: http://beust.com/kobalt
[Ant]: http://ant.apache.org/
[Ant tasks]: http://ant.apache.org/manual/tasksoverview.html