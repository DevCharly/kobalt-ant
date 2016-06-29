import com.beust.kobalt.*
import com.beust.kobalt.plugin.packaging.*

val repos = repos("https://dl.bintray.com/devcharly/maven/")

val plugin = project {
	name = "kobalt-ant"
	group = "com.devcharly.kobalt"
	artifactId = name
	version = "0.1"

	dependencies {
		provided("com.beust:kobalt-plugin-api:")
		compile("com.devcharly:kotlin-ant-dsl:0.2")
		compile("org.apache.ant:ant:1.9.7")
	}

	assemble {
		// Kobalt plugin (includes ant.jar)
		mavenJars {
			jar {
				fatJar = true

				exclude("**/kotlin-stdlib-*.jar")
				exclude("**/kotlin-runtime-*.jar")
			}
		}
	}


/*
	bintray {
		publish = true
	}
*/
}

val examples = project(plugin) {
	name = "kobalt-ant-examples"
	directory = "examples"
	sourceDirectories { path("src") }
}
