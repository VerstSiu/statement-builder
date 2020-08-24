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
import com.ijoic.statementbuilder.entity.Field
import com.ijoic.statementbuilder.entity.FieldAccessors
import org.junit.Test

/**
 * Statement builder test
 *
 * @author verstsiu created at 2020-08-24 11:51
 */
internal class StatementBuilderTest {

  @Test
  fun testSelectFields() {
    val builder = StatementBuilder
      .select(Fields.name, Fields.age)
      .from(TEST_TABLE)

    assert(builder.generateSql() == "SELECT `$NAME`,`$AGE` FROM `$TEST_TABLE`")
  }

  @Test
  fun testSelectAll() {
    val builder = StatementBuilder
      .selectAll()
      .from(TEST_TABLE)

    assert(builder.generateSql() == "SELECT * FROM `$TEST_TABLE`")
  }

  @Test
  fun testWhereSimple() {
    val builder = StatementBuilder
      .selectAll()
      .from(TEST_TABLE)
      .where(
        ConditionBuilder
          .field(Fields.id, 1001)
          .and(
            ConditionBuilder
              .field(Fields.name, "Tom")
              .or(Fields.age, 10)
          )
      )

    assert(builder.generateSql() == "SELECT * FROM `$TEST_TABLE` WHERE `$ID`=? AND (`$NAME`=? OR `$AGE`=?)")
  }

  @Test
  fun testInsertSimple() {
    val builder = StatementBuilder
      .insert(TEST_TABLE)
      .set(Fields.name, "Tom")
      .set(Fields.age, 10)

    assert(builder.generateSql() == "INSERT INTO `$TEST_TABLE`(`$NAME`,`$AGE`) VALUES (?,?)")
  }

  @Test
  fun testUpdateSimple() {
    val builder = StatementBuilder
      .update(TEST_TABLE)
      .set(Fields.name, "Tom")
      .set(Fields.age, 10)
      .where(Fields.id, 1001)

    assert(builder.generateSql() == "UPDATE `$TEST_TABLE` SET `$NAME`=?,`$AGE`=? WHERE `$ID`=?")
  }

  @Test
  fun testDeleteSimple() {
    val builder = StatementBuilder
      .delete(TEST_TABLE)
      .where(Fields.id, 1001)

    assert(builder.generateSql() == "DELETE FROM `$TEST_TABLE` WHERE `$ID`=?")
  }

  private object Fields {
    val id = Field(name = ID, accessor = FieldAccessors.LONG)
    val name = Field(name = NAME, accessor = FieldAccessors.STRING)
    val age = Field(name = AGE, accessor = FieldAccessors.INT)
  }

  companion object {
    private const val TEST_TABLE = "person"

    private const val ID = "id"
    private const val NAME = "name"
    private const val AGE = "age"
  }
}