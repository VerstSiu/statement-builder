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
package com.ijoic.statementbuilder.entity

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import java.text.SimpleDateFormat

/**
 * Field accessors
 *
 * @author verstsiu created at 2020-08-24 11:24
 */
object FieldAccessors {
  val LONG = FieldAccessor(
    read = { rs, field -> rs.getLong(field) },
    write = { statement, index, value ->
      statement.setLong(index, value)
    }
  )
  val OPTIONAL_LONG = optionalAccessor(
    read = { rs, field -> rs.getString(field)?.toLongOrNull() },
    write = { statement, index, value ->
      statement.setLong(index, value)
    }
  )

  val BOOLEAN = FieldAccessor(
    read = { rs, field -> rs.getBoolean(field) },
    write = { statement, index, value ->
      statement.setBoolean(index, value)
    }
  )
  val OPTIONAL_BOOLEAN = optionalAccessor(
    read = { rs, field ->
      when(rs.getString(field)?.toIntOrNull()) {
        null -> null
        0 -> false
        1 -> true
        else -> null
      }
    },
    write = { statement, index, value ->
      statement.setBoolean(index, value)
    }
  )

  val INT = FieldAccessor(
    read = { rs, field -> rs.getInt(field) },
    write = { statement, index, value ->
      statement.setInt(index, value)
    }
  )
  val OPTIONAL_INT = optionalAccessor(
    read = { rs, field -> rs.getString(field)?.toIntOrNull() },
    write = { statement, index, value ->
      statement.setInt(index, value)
    }
  )

  val STRING = FieldAccessor<String>(
    read = { rs, field -> rs.getString(field) },
    write = { statement, index, value ->
      statement.setString(index, value)
    }
  )
  val OPTIONAL_STRING = optionalAccessor(
    read = { rs, field -> rs.getString(field) },
    write = { statement, index, value ->
      statement.setString(index, value)
    }
  )

  val DOUBLE = FieldAccessor(
    read = { rs, field -> rs.getDouble(field) },
    write = { statement, index, value ->
      statement.setDouble(index, value)
    }
  )
  val OPTIONAL_DOUBLE = optionalAccessor(
    read = { rs, field -> rs.getString(field)?.toDoubleOrNull() },
    write = { statement, index, value ->
      statement.setDouble(index, value)
    }
  )

  val DOUBLE_AS_DECIMAL = FieldAccessor(
    read = { rs, field -> rs.getString(field).toDouble() },
    write = { statement, index, value ->
      statement.setBigDecimal(index, value.toBigDecimal())
    }
  )
  val OPTIONAL_DOUBLE_AS_DECIMAL = optionalAccessor(
    read = { rs, field -> rs.getString(field)?.toDoubleOrNull() },
    write = { statement, index, value ->
      statement.setBigDecimal(index, value.toBigDecimal())
    }
  )

  val FLOAT = FieldAccessor(
    read = { rs, field -> rs.getFloat(field) },
    write = { statement, index, value ->
      statement.setFloat(index, value)
    }
  )
  val FLOAT_DOUBLE = optionalAccessor(
    read = { rs, field -> rs.getString(field)?.toFloatOrNull() },
    write = { statement, index, value ->
      statement.setFloat(index, value)
    }
  )

  val DATETIME_SECONDS = optionalAccessor(
    read = { rs, field -> rs.getString(field)
      ?.let { toDatetimeOrNull(it) }
      ?.let { it / 1000L }
    },
    write = { statement, index, value ->
      statement.setString(index, toDatetimeText(value * 1000L))
    }
  )

  private fun <T> optionalAccessor(
    read: (rs: ResultSet, field: String) -> T?,
    write: (statement: PreparedStatement, index: Int, value: T) -> Unit
  ): FieldAccessor<T?> {

    return FieldAccessor(
      read = read,
      write = { statement, index, value ->
        when(value) {
          null -> statement.setNull(index, Types.NULL)
          else -> write.invoke(statement, index, value)
        }
      }
    )
  }

  /* Datetime */

  private val datetimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  private val responseFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

  private fun toDatetimeOrNull(text: String): Long? {
    return this.runCatching { toDatetime(text) }.getOrNull()
  }

  @Throws(Exception::class)
  private fun toDatetime(text: String): Long? {
    if (text.contains('.')) {
      return responseFormat.parse(text)?.time
    }
    return datetimeFormat.parse(text)?.time
  }

  private fun toDatetimeText(timestamp: Long): String {
    return datetimeFormat.format(timestamp)
  }

  /* Datetime :END */
}