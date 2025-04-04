package ir.amirreza

import annotations.confirmation.Confirmation
import annotations.date.AutoNowDate
import annotations.exposed.ExposedTable
import annotations.info.ColumnInfo
import annotations.limit.Limits
import annotations.query.AdminQueries
import annotations.references.OneToOneReferences
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@ExposedTable(
    "token",
    "user_id",
    "token",
    "tokens",
    groupName = "profiles"
)
@AdminQueries(
    filters = ["date"]
)
object TokenTable : Table() {
    @ColumnInfo("user_id")
    @OneToOneReferences("users", "id")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    val token = text("token")

    @ColumnInfo("date")
    @Limits(
        minDateRelativeToNow = 0
    )
    @AutoNowDate(updateOnChange = true)
    val expiredAt = datetime("date")
    override val primaryKey = PrimaryKey(userId)
}