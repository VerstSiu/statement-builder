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
import java.sql.Connection
import java.sql.PreparedStatement

/**
 * Statement builder
 *
 * @author verstsiu created at 2020-08-24 11:04
 */
interface StatementBuilder {

  /**
   * Build state with [connection] and [requireGenerateKeys]
   */
  fun build(connection: Connection, requireGenerateKeys: Boolean = false): PreparedStatement

  companion object {
    /**
     * Select fields("SELECT column1, column2 ..")
     */
    @Throws(IllegalArgumentException::class)
    fun select(vararg field: Field<*>): FromBuilder {
      if (field.isEmpty()) {
        throw IllegalArgumentException("Select fields must not empty")
      }
      val options = BuildOptions()
      options.sqlBuilder
        .append("SELECT ")
        .append(field.joinToString(",") { "`${it.name}`" })
      return FromBuilder(options)
    }

    /**
     * Select all("SELECT * ..")
     */
    fun selectAll(): FromBuilder {
      val options = BuildOptions()
      options.sqlBuilder
        .append("SELECT *")
      return FromBuilder(options)
    }

    /**
     * Insert records to table("INSERT INTO table_name ..")
     */
    fun insert(tableName: String): InsertBuilder {
      val options = BuildOptions()
      options.sqlBuilder
        .append("INSERT INTO `$tableName`")
      return InsertBuilder(options)
    }

    /**
     * Update table("UPDATE table_name ..")
     */
    fun update(tableName: String): UpdateTopBuilder {
      val options = BuildOptions()
      options.sqlBuilder
        .append("UPDATE `$tableName`")
      return UpdateTopBuilder(options)
    }

    /**
     * Update table("DELETE FROM table_name ..")
     */
    fun delete(tableName: String): WhereBuilder {
      val options = BuildOptions()
      options.sqlBuilder
        .append("DELETE FROM `$tableName`")
      return WhereBuilder(options)
    }
  }
}