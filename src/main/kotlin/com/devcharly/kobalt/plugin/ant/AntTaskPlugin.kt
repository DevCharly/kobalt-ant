/*
 * Copyright 2016 Karl Tauber
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devcharly.kobalt.plugin.ant

import com.beust.kobalt.IncrementalTaskInfo
import com.beust.kobalt.TaskResult
import com.beust.kobalt.api.*
import com.beust.kobalt.api.annotation.AnnotationDefault
import com.beust.kobalt.api.annotation.Directive
import com.beust.kobalt.maven.Md5
import com.beust.kobalt.misc.KobaltLogger
import com.beust.kobalt.misc.error
import com.devcharly.kotlin.ant.Ant
import com.devcharly.kotlin.ant.AntContext
import com.devcharly.kotlin.ant.LogLevel
import org.apache.tools.ant.BuildException
import org.apache.tools.ant.Task
import java.io.File
import java.util.*

/**
 * antTask plugin for Kobalt
 *
 * Supports definition of per-project Kobalt tasks (similar to Ant targets)
 * and execution of Ant tasks.
 *
 * val project = project {
 *     antTask("hello") {
 *         echo("Hello World")
 *     }
 * }
 */
class AntTaskPlugin : BasePlugin(), ITaskContributor, IIncrementalTaskContributor {
	override val name = PLUGIN_NAME

	companion object {
		const val PLUGIN_NAME = "AntTask"
		const val ANT_TASKS = "antTasks"
	}

	override fun accept(project: Project): Boolean {
		// enable plugin only for project with ant tasks
		return project.projectProperties.get(ANT_TASKS) != null
	}

	// ITaskContributor
	override fun tasksFor(project: Project, context: KobaltContext): List<DynamicTask> {
		@Suppress("UNCHECKED_CAST")
		val antTasks = project.projectProperties.get(AntTaskPlugin.ANT_TASKS) as ArrayList<AntTask>?
			?: return emptyList()

		// add new tasks for project
		val dynamicTasks = ArrayList<DynamicTask>()
		antTasks.filter { !it.isIncremental() }.forEach {
			dynamicTasks.add(DynamicTask(this, it.taskName, it.description, it.group, project,
					it.dependsOn.toList(), it.reverseDependsOn.toList(),
					it.runBefore.toList(), it.runAfter.toList(), it.alwaysRunAfter.toList(),
					closure = { project ->
						it.executeTasks()
					}))
		}
		return dynamicTasks
	}

	// IIncrementalTaskContributor
	override fun incrementalTasksFor(project: Project, context: KobaltContext): List<IncrementalDynamicTask> {
		@Suppress("UNCHECKED_CAST")
		val antTasks = project.projectProperties.get(AntTaskPlugin.ANT_TASKS) as ArrayList<AntTask>?
				?: return emptyList()

		// add new tasks for project
		val dynamicTasks = ArrayList<IncrementalDynamicTask>()
		antTasks.filter { it.isIncremental() }.forEach {
			dynamicTasks.add(IncrementalDynamicTask(context, this, it.taskName, it.description, it.group, project,
					it.dependsOn.toList(), it.reverseDependsOn.toList(),
					it.runBefore.toList(), it.runAfter.toList(), it.alwaysRunAfter.toList(),
					incrementalClosure = { project ->
						IncrementalTaskInfo(
							inputChecksum = {
								if (it.inputChecksum != null)
									it.inputChecksum!!()
								else if (it.inputFiles != null)
									Md5.toMd5Directories(it.inputFiles.map { File(it) })
								else
									null
							},
							outputChecksum = {
								if (it.outputChecksum != null)
									it.outputChecksum!!()
								else if (it.outputFiles != null)
									Md5.toMd5Directories(it.outputFiles.map { File(it) })
								else
									null
							},
							task = { project ->
								it.executeTasks()
							},
							context = context
						)
					}))
		}
		return dynamicTasks
	}
}

class AntTask(val taskName: String,
		val description: String = "", val group: String = AnnotationDefault.GROUP,
		val dependsOn: Array<String> = arrayOf(), val reverseDependsOn: Array<String> = arrayOf(),
		val runBefore: Array<String> = arrayOf(), val runAfter: Array<String> = arrayOf(),
		val alwaysRunAfter: Array<String> = arrayOf(),
		val inputFiles: Array<String>? = null, val outputFiles: Array<String>? = null,
		val inputChecksum: (() -> String?)? = null, val outputChecksum: (() -> String?)? = null,
		basedir: String = "", logLevel: LogLevel? = null,
		context: AntContext? = null,
		tasks: AntTask.() -> Unit)
	: Ant(context ?: AntContext(basedir, logLevel ?: kobalt2antLogLevel()), execute = false, tasks = tasks as Ant.() -> Unit)
{
	fun isIncremental() = inputFiles != null || inputChecksum != null || outputFiles != null || outputChecksum != null

	fun executeTasks(): TaskResult {
		// create basedir
		if (context.basedir != "")
			File(context.basedir).mkdirs()

		// execute Ant tasks
		try {
			execute()
			return TaskResult()
		} catch (e: BuildException) {
			return TaskResult(false, e.message)
		}
	}

	override fun executeTask(task: Task) {
		try {
			super.executeTask(task)
		} catch (e: BuildException) {
			// always print stack trace
			// if running from an IDE, clicking on
			//     BuildKt$project.invoke(Build.kt:123)
			// in stack trace jump directly to the line in Build.kt
			e.printStackTrace()

			error("Ant task [${task.taskName}] failed: ${e.message}")
			throw e
		}
	}
}

// use Kobalt log level
private fun kobalt2antLogLevel() =
	when (KobaltLogger.LOG_LEVEL) {
		 0 -> LogLevel.WARN
		 1 -> LogLevel.INFO
		 2 -> LogLevel.VERBOSE
		 3 -> LogLevel.DEBUG
		 else -> LogLevel.INFO
	}

@Directive
fun Project.antTask(taskName: String,
		description: String = "", group: String = AnnotationDefault.GROUP,
		dependsOn: Array<String> = arrayOf(), reverseDependsOn: Array<String> = arrayOf(),
		runBefore: Array<String> = arrayOf(), runAfter: Array<String> = arrayOf(),
		alwaysRunAfter: Array<String> = arrayOf(),
		inputFiles: Array<String>? = null, outputFiles: Array<String>? = null,
		inputChecksum: (() -> String?)? = null, outputChecksum: (() -> String?)? = null,
		basedir: String = "", logLevel: LogLevel? = null,
		context: AntContext? = null,
		tasks: AntTask.() -> Unit)
= AntTask(taskName, description, group,
		dependsOn, reverseDependsOn, runBefore, runAfter, alwaysRunAfter,
		inputFiles, outputFiles,
		inputChecksum, outputChecksum,
		basedir, logLevel, context, tasks).apply {
	@Suppress("UNCHECKED_CAST")
	val antTasks = projectProperties.get(AntTaskPlugin.ANT_TASKS) as ArrayList<AntTask>?
		?: ArrayList<AntTask>().apply { projectProperties.put(AntTaskPlugin.ANT_TASKS, this) }
	antTasks.forEach {
		if( it.taskName == taskName )
			throw AssertionError("Duplicate antTask '$taskName' in project '$projectName'")
	}
	antTasks.add(this)
}
