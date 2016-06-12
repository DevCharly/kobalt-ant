/*
 * Copyright 2016 the original author or authors.
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

package com.devcharly.kotlin.ant

import org.apache.tools.ant.taskdefs.*
import java.io.File

fun AntBuilder.copy(file: String? = null, tofile: String? = null, todir: String? = null,
					overwrite: Boolean = false,
					nested: (AntCopyNested.() -> Unit)? = null)
{
	Copy().execute("copy") { task ->
		task.setFile(fileOrNull(file))
		task.setTofile(fileOrNull(tofile))
		task.setTodir(fileOrNull(todir))
		task.setOverwrite(overwrite)
		if (nested != null)
			nested(AntCopyNested(task))
	}
}

fun AntBuilder.delete(file: String? = null, dir: String? = null) {
	Delete().execute("delete") { task ->
		task.setFile(fileOrNull(file))
		task.setDir(fileOrNull(dir))
	}
}

fun AntBuilder.mkdir(dir: String) {
	Mkdir().execute("mkdir") { task ->
		task.dir = File(dir)
	}
}

fun AntBuilder.touch(file: String) {
	Touch().execute("touch") { task ->
		task.setFile(File(file))
	}
}