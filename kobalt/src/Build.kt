import com.beust.kobalt.*
import com.beust.kobalt.plugin.packaging.*
import com.beust.kobalt.plugin.publish.bintray

object Versions {
	val kobalt = "1.0.100"
	val kotlinAntDSL = "0.6"
	val ant = "1.10.1"
}

val plugin = project {
	name = "kobalt-ant"
	group = "com.devcharly"
	artifactId = name
	version = "0.4"

	dependencies {
		provided("com.beust:kobalt-plugin-api:${Versions.kobalt}")
		compile("com.devcharly:kotlin-ant-dsl:${Versions.kotlinAntDSL}")
		compile("org.apache.ant:ant:${Versions.ant}")
	}

	assemble {
		// Kobalt plugin (includes kotlin-ant-dsl.jar and ant.jar)
		mavenJars {
			jar {
				fatJar = true

				exclude("**/kotlin-stdlib-*.jar")
				exclude("**/kotlin-runtime-*.jar")
			}
		}

		jar {
			name = "${project.name}-core-$version.jar"
		}
	}

	bintray {
		publish = true
	}
}

val examples = project(plugin) {
	name = "examples"
	directory = "examples"
	sourceDirectories { path("kobalt/src") }

	dependencies {
		provided("com.beust:kobalt-plugin-api:${Versions.kobalt}")
	}
}
