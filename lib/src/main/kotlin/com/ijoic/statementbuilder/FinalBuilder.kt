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
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement

/**
 * Final builder
 *
 * @author verstsiu created at 2020-08-24 11:42
 */
open class FinalBuilder internal constructor(
  private val options: BuildOptions
) : StatementBuilder {

  /**
   * Returns generated sql for unit test
   */
  internal fun generateSql(): String {
    onPrepareBuild()
    return options.sqlBuilder.toString()
  }

  override fun build(connection: Connection, requireGenerateKeys: Boolean): PreparedStatement {
    onPrepareBuild()
    val sql = options.sqlBuilder.toString()
    val statement = when(requireGenerateKeys) {
      true -> connection.prepareStatement(
        sql,
        Statement.RETURN_GENERATED_KEYS
      )
      else -> connection.prepareStatement(sql)
    }
    var paramIndex = 1

    for (param in options.params) {
      param.write(statement, paramIndex)
      ++paramIndex
    }
    return statement
  }

  protected open fun onPrepareBuild() {}
}