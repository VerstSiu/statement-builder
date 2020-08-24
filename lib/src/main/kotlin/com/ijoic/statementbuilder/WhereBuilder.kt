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

import com.ijoic.statementbuilder.condition.ConditionBuilder
import com.ijoic.statementbuilder.entity.BuildOptions
import com.ijoic.statementbuilder.entity.Field
import com.ijoic.statementbuilder.entity.FieldSearch

/**
 * Where builder
 *
 * "WHERE condition .."
 *
 * @author verstsiu created at 2020-08-24 14:28
 */
open class WhereBuilder internal constructor(
  private val options: BuildOptions
) : FinalBuilder(options) {

  fun <T> where(field: Field<T>, value: T): FinalBuilder {
    options.sqlBuilder.append(" WHERE `${field.name}`=?")
    options.params.add(FieldSearch(field, value))
    return FinalBuilder(options)
  }

  fun where(builder: ConditionBuilder): FinalBuilder {
    options.sqlBuilder.append(" WHERE ")
    builder.fill(options)
    return FinalBuilder(options)
  }
}