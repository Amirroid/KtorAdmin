package ir.amirreza

import annotations.display.TableDisplayFormat
import annotations.exposed.ExposedTable
import annotations.info.IgnoreColumn
import org.jetbrains.exposed.sql.Table

@ExposedTable(
    "users",
    "id",
    "user",
    "users",
    "profiles"
)
@TableDisplayFormat(
    format = "{id} - {username}"
)
object Users : Table() {
    @IgnoreColumn
    val id = integer("id").autoIncrement()
    val username = varchar("username", length = 100)
    val email = varchar("email", length = 150)
    val password = text("password")

    override val primaryKey = PrimaryKey(id)
}