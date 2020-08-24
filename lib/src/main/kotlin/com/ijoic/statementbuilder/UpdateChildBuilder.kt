/*
 *
 *  Copyright(c) 2020 VerstSiu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.ijoic.statementbuilder

import com.ijoic.statementbuilder.entity.BuildOptions
import com.ijoic.statementbuilder.entity.Field
import com.ijoic.statementbuilder.entity.FieldSearch

/**
 * Update child builder
 *
 * ",column2=value2.. WHERE condition .."
 *
 * @author verstsiu created at 2020-08-24 16:08
 */
class UpdateChildBuilder internal constructor(
  private val options: BuildOptions
) : WhereBuilder(options) {

  fun <T> set(field: Field<T>, value: T): UpdateChildBuilder {
    options.sqlBuilder.append(",`${field.name}`=?")
    options.params.add(FieldSearch(field, value))
    return this
  }

}