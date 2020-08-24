package com.ijoic.statementbuilder

import com.ijoic.statementbuilder.entity.BuildOptions
import com.ijoic.statementbuilder.entity.Field
import com.ijoic.statementbuilder.entity.FieldSearch

/**
 * Insert builder
 *
 * "(column1, column2..) VALUES (value1, value2..)"
 *
 * @author verstsiu created at 2020-08-24 16:29
 */
class InsertBuilder internal constructor(
  private val options: BuildOptions
) : FinalBuilder(options) {

  private val insertFields = mutableListOf<FieldSearch<*>>()

  fun <T> set(field: Field<T>, value: T): InsertBuilder {
    insertFields.add(FieldSearch(field, value))
    return this
  }

  override fun onPrepareBuild() {
    if (insertFields.isEmpty()) {
      throw IllegalArgumentException("Insert field and values not found")
    }
    options.sqlBuilder
      .append('(')
      .append(insertFields.joinToString(",") { "`${it.field.name}`"})
      .append(") VALUES (")
      .append(insertFields.joinToString(",") { "?" })
      .append(')')
    options.params.addAll(insertFields)
  }
}