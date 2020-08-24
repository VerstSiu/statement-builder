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

/**
 * From builder
 *
 * "FROM table_name .."
 *
 * @author verstsiu created at 2020-08-24 11:12
 */
class FromBuilder internal constructor(
  private val options: BuildOptions
) {

  fun from(tableName: String): WhereBuilder {
    options.sqlBuilder
      .append(" FROM `$tableName`")
    return WhereBuilder(options)
  }
}