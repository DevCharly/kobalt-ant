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
import org.apache.tools.ant.types.FileSet

class AntCopyNested(val task: Copy) {
	fun fileset(dir: String, nested: (AntFileSet.() -> Unit)? = null) {
		val fileset = FileSet()
		fileset.dir = fileOrNull(dir)
		if (nested != null)
			nested(AntFileSet(fileset))
		task.addFileset(fileset)
	}
}

class AntFileSet(val fileset: FileSet) {
	fun include(name: String) {
		fileset.createInclude().name = name
	}

	fun exclude(name: String) {
		fileset.createExclude().name = name
	}
}