# [Ant] plug-in for [Kobalt]

[![Build Status](https://travis-ci.org/DevCharly/kobalt-ant.svg?branch=master)](https://travis-ci.org/DevCharly/kobalt-ant)
[![Download](https://api.bintray.com/packages/devcharly/maven/kobalt-ant/images/download.svg) ](https://bintray.com/devcharly/maven/kobalt-ant/_latestVersion)

Supports definition of per-project Kobalt tasks (similar to Ant targets)
and execution of [Ant tasks].

```kotlin
val project = project {
    antTask("hello") {
       echo("Hello World")
    }
}
```

The primary goal is support migration of Ant builds to Kobalt.
You can use your custom Ant tasks in Kobalt. Or use built-it Ant tasks.

[Examples](examples/kobalt/src/Build.kt)


## Supported Ant features

See [Kotlin Ant DSL](https://github.com/DevCharly/kotlin-ant-dsl#supported-ant-features)


## Download

[![Download](https://api.bintray.com/packages/devcharly/maven/kobalt-ant/images/download.svg) ](https://bintray.com/devcharly/maven/kobalt-ant/_latestVersion)

For Maven, Gradle or Kobalt use:

    Repository: https://dl.bintray.com/devcharly/maven/ 
    Group:      com.devcharly
    Artifact:   kobalt-ant
    Version:    (latest)


[Kobalt]: http://beust.com/kobalt
[Ant]: http://ant.apache.org/
[Ant tasks]: http://ant.apache.org/manual/tasksoverview.html