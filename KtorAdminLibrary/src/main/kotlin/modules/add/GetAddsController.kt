package modules.add

import utils.badRequest
import utils.notFound
import getters.getReferencesItems
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.velocity.*
import panels.AdminJdbcTable
import panels.AdminPanel
import panels.getAllAllowToShowColumns
import utils.Constants

internal suspend fun ApplicationCall.handleAddNewItem(tables: List<AdminPanel>) {
    val pluralName = parameters["pluralName"]
    val panel = tables.find { it.getPluralName() == pluralName }
    if (panel == null) {
        notFound("No table found with plural name: $pluralName")
    } else {
        when (panel) {
            is AdminJdbcTable -> handleJdbcAddView(table = panel, tables = tables)
        }
    }
}

private suspend fun ApplicationCall.handleJdbcAddView(
    table: AdminJdbcTable,
    tables: List<AdminPanel>,
) {
    runCatching {
        val columns = table.getAllAllowToShowColumns()
        val referencesItems = getReferencesItems(tables.filterIsInstance<AdminJdbcTable>(), columns)
        respond(
            VelocityContent(
                "${Constants.TEMPLATES_PREFIX_PATH}/upsert_admin.vm", model = mapOf(
                    "columns" to columns,
                    "tableName" to table.getTableName(),
                    "method" to "post",
                    "singularTableName" to table.getSingularName().replaceFirstChar { it.uppercaseChar() },
                    "references" to referencesItems
                )
            )
        )
    }.onFailure {
        badRequest("Error: ${it.message}")
    }
}