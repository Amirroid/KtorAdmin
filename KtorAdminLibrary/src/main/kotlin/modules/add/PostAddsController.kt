package modules.add

import annotations.errors.badRequest
import configuration.DynamicConfiguration
import converters.toEvents
import converters.toTableValues
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.ColumnSet
import repository.JdbcQueriesRepository
import tables.AdminJdbcTable
import tables.AdminPanel
import tables.findWithPluralName
import tables.getAllAllowToShowColumns
import validators.validateParameters

private suspend fun onInsert(
    tableName: String,
    objectPrimaryKey: String,
    columnSets: List<ColumnSet>,
    parametersData: List<Pair<String, Any?>?>
) {
    DynamicConfiguration.currentEventListener?.onInsertData(
        tableName = tableName,
        objectPrimaryKey = objectPrimaryKey,
        events = columnSets.toEvents(parametersData.map { it?.second })
    )
}

internal suspend fun RoutingContext.handleAddRequest(tables: List<AdminPanel>) {
    val pluralName = call.parameters["pluralName"]
    val table = tables.findWithPluralName(pluralName)
    if (table == null) {
        call.respondText { "No table found with plural name: $pluralName" }
        return
    }

    when (table) {
        is AdminJdbcTable -> insertData(pluralName, table)
    }
}

private suspend fun RoutingContext.insertData(pluralName: String?, table: AdminJdbcTable) {
    val parametersData = call.receiveMultipart().toTableValues(table)
    val parameters = parametersData.map { it?.first }
    val columns = table.getAllAllowToShowColumns()

    // Validate parameters
    val isValidParameters = columns.validateParameters(parameters)
    if (isValidParameters) {
        kotlin.runCatching {
            val id = JdbcQueriesRepository.insertData(table, parameters)
            if (id != null) {
                onInsert(
                    tableName = table.getTableName(),
                    columnSets = columns,
                    objectPrimaryKey = id.toString(),
                    parametersData = parametersData
                )
            }
            call.respondRedirect("/admin/$pluralName")
        }.onFailure {
            call.badRequest("Failed to insert $pluralName\nReason: ${it.message}")
        }
    } else {
        call.badRequest("Invalid parameters for $pluralName: $parameters")
    }
}