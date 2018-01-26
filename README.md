# [Ant] plug-in for [Kobalt]

[![Build Status](https://travis-ci.org/DevCharly/kobalt-ant.svg?branch=master)](https://travis-ci.org/DevCharly/kobalt-ant)
[![Download](https://api.bintray.com/packages/devcharly/maven/kobalt-ant/images/download.svg) ](https://bintray.com/devcharly/maven/kobalt-ant/_latestVersion)

Supports definition of per-project Kobalt tasks (similar to Ant targets)
and execution of [Ant tasks].

The primary goal is support migration of Ant builds to Kobalt.
You can use [built-it Ant tasks](https://github.com/DevCharly/kotlin-ant-dsl#supported-ant-features) in Kobalt.
Or [define functions](https://github.com/DevCharly/kotlin-ant-dsl#kotlin-ant-dsl)
to use your custom Ant tasks.

```kotlin
val bs = buildScript {
    plugins("com.devcharly:kobalt-ant:")
}

val project = project {
    antTask("hello") {
        echo("Hello World")
    }

    antTask("copy-dir1-to-dir2") {
        mkdir("dir2")
        copy(todir = "dir2") {
            fileset(dir = "dir1")
            include(name = "**/*.txt")
        }
    }
}
```

Kobalt's incremental tasks are supported:

```kotlin
val bs = buildScript {
    plugins("com.devcharly:kobalt-ant:")
}

val project = project {
    antTask("copy-dir1-to-dir2",
            inputFiles = arrayOf("dir1"),
            outputFiles = arrayOf("dir2")
    ) {
        mkdir("dir2")
        copy(todir = "dir2") {
            fileset(dir = "dir1")
            include(name = "**/*.txt")
        }
    }
}
```

[Examples](examples/kobalt/src/Build.kt)


## Supported Ant features

See [Kotlin Ant DSL](https://github.com/DevCharly/kotlin-ant-dsl#supported-ant-features)


## Requirements

Kobalt 1.0.100 or later


[Kobalt]: http://beust.com/kobalt
[Ant]: http://ant.apache.org/
[Ant tasks]: http://ant.apache.org/manual/tasksoverview.html