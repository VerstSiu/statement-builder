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
package com.ijoic.statementbuilder.condition

import com.ijoic.statementbuilder.entity.BuildOptions
import com.ijoic.statementbuilder.entity.Field
import com.ijoic.statementbuilder.entity.FieldSearch

/**
 * Condition builder
 *
 * @author verstsiu created at 2020-08-24 15:45
 */
class ConditionBuilder internal constructor(
  private val options: BuildOptions
) {

  fun <T> and(field: Field<T>, value: T): ConditionBuilder {
    options.sqlBuilder.append(" AND `${field.name}`=?")
    options.params.add(FieldSearch(field, value))
    return this
  }

  fun and(builder: ConditionBuilder): ConditionBuilder {
    options.sqlBuilder.append(" AND (")
    builder.fill(options)
    options.sqlBuilder.append(')')
    return this
  }

  fun <T> or(field: Field<T>, value: T): ConditionBuilder {
    options.sqlBuilder.append(" OR `${field.name}`=?")
    options.params.add(FieldSearch(field, value))
    return this
  }

  fun or(builder: ConditionBuilder): ConditionBuilder {
    options.sqlBuilder.append(" OR (")
    builder.fill(options)
    options.sqlBuilder.append(')')
    return this
  }

  /**
   * Fill build [options]
   */
  internal fun fill(options: BuildOptions) {
    options.sqlBuilder.append(this.options.sqlBuilder.toString())
    options.params.addAll(this.options.params)
  }

  companion object {
    fun <T> field(field: Field<T>, value: T): ConditionBuilder {
      val options = BuildOptions()
      options.sqlBuilder.append("`${field.name}`=?")
      options.params.add(FieldSearch(field, value))
      return ConditionBuilder(options)
    }
  }
}