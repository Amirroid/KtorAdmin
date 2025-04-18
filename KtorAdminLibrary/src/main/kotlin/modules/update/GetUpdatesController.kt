package modules.update

import authentication.KtorAdminPrincipal
import configuration.DynamicConfiguration
import csrf.CsrfManager
import flash.getFlashDataAndClear
import flash.getRequestId
import utils.notFound
import utils.serverError
import getters.getReferencesItems
import getters.getSelectedReferencesItems
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.velocity.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import models.ColumnSet
import models.PanelGroup
import models.field.FieldSet
import models.types.ColumnType
import models.types.FieldType
import panels.*
import repository.FileRepository
import repository.JdbcQueriesRepository
import repository.MongoClientRepository
import response.toMap
import translator.KtorAdminTranslator
import translator.translator
import utils.Constants
import utils.addCommonModels
import utils.addCommonUpsertModels
import validators.checkHasRole
import kotlin.collections.toMap


internal suspend fun ApplicationCall.handleEditItem(
    panels: List<AdminPanel>,
    panelGroups: List<PanelGroup>
) {
    val pluralName = parameters["pluralName"]
    val primaryKey = parameters["primaryKey"]
    val panel = panels.find { it.getPluralName() == pluralName }
    when {
        panel == null || panel.isShowInAdminPanel()
            .not() -> respondText { "No table found with plural name: $pluralName" }

        primaryKey == null -> respondText { "No primary key found: $pluralName" }
        else -> {
            checkHasRole(panel) {
                when (panel) {
                    is AdminJdbcTable -> handleJdbcEditView(primaryKey, panel, panels, panelGroups = panelGroups)
                    is AdminMongoCollection -> handleNoSqlEditView(primaryKey, panel, panelGroups = panelGroups)
                }
            }
        }
    }
}

internal suspend fun ApplicationCall.handleJdbcEditView(
    primaryKey: String,
    table: AdminJdbcTable,
    panels: List<AdminPanel>,
    panelGroups: List<PanelGroup> = emptyList(),
) {
    val data = JdbcQueriesRepository.getData(table, primaryKey)
    if (data == null) {
        notFound("No data found with primary key: $primaryKey")
    } else {
        runCatching {
            val username = principal<KtorAdminPrincipal>()?.name
            val columns = table.getAllAllowToShowColumnsInUpsert()
            val panelColumnsList = table.getAllAllowToShowColumnsInUpsertView()
            val referencesItems = getReferencesItems(panels.filterIsInstance<AdminJdbcTable>(), panelColumnsList)
            val selectedReferences =
                getSelectedReferencesItems(table, panels.filterIsInstance<AdminJdbcTable>(), primaryKey)
            val requestId = getRequestId()
            val valuesWithErrors = getFlashDataAndClear(requestId)
            val errorValues = valuesWithErrors.first
            val errors = valuesWithErrors.second ?: emptyList()
            val values = errorValues?.takeIf { it.isNotEmpty() } ?: processColumnValues(
                columns, data, this
            )
            val singularTableName = table.getSingularName().replaceFirstChar { it.uppercaseChar() }
            respond(
                VelocityContent(
                    "${Constants.TEMPLATES_PREFIX_PATH}/admin_panel_upsert.vm", model = mapOf(
                        "columns" to panelColumnsList,
                        "tableName" to table.getTableName(),
                        "primaryKey" to primaryKey,
                        "canDownload" to DynamicConfiguration.canDownloadDataAsPdf,
                        "values" to values,
                        "singularTableName" to singularTableName,
                        "references" to referencesItems,
                        "selectedReferences" to selectedReferences,
                        "pluralNameBase" to table.getPluralName(),
                        "errors" to errors.toMap(),
                        "csrfToken" to CsrfManager.generateToken(),
                        "panelGroups" to panelGroups,
                        "currentPanel" to table.getPluralName(),
                        "isUpdate" to true,
                        "requestId" to requestId,
                        "hasAction" to table.hasEditAction,
                        "previews" to extractColumnsPreviews(
                            table.getTableName(),
                            columnsWithValues = values.mapKeys { item -> columns.first { column -> column.columnName == item.key } }
                        ),
                        "title" to translator.translate(
                            KtorAdminTranslator.Keys.UPDATE_ITEM,
                            mapOf("name" to singularTableName)
                        )
                    ).addCommonUpsertModels(table, username).toMutableMap()
                        .addCommonModels(table, panelGroups, applicationCall = this)
                )
            )
        }.onFailure {
            serverError("Error: ${it.message}", it)
        }
    }
}


private fun extractColumnsPreviews(
    tableName: String,
    columnsWithValues: Map<ColumnSet, Any?>
) = columnsWithValues.mapValues { (columnSet, value) ->
    columnSet.preview?.let { preview ->
        DynamicConfiguration.getPreview(preview).createPreview(tableName, columnSet.columnName, value)
    }
}


private fun extractFieldsPreviews(
    tableName: String,
    fieldsWithValues: Map<FieldSet, Any?>
) = fieldsWithValues.mapValues { (fieldSet, value) ->
    fieldSet.preview?.let { preview ->
        DynamicConfiguration.getPreview(preview).createPreview(tableName, fieldSet.fieldName.orEmpty(), value)
    }
}


internal suspend fun ApplicationCall.handleNoSqlEditView(
    primaryKey: String,
    panel: AdminMongoCollection,
    panelGroups: List<PanelGroup>
) {
    val data = MongoClientRepository.getData(panel, primaryKey)
    if (data == null) {
        notFound("No data found with primary key: $primaryKey")
    } else {
        runCatching {
            val username = principal<KtorAdminPrincipal>()?.name
            val fields = panel.getAllAllowToShowFieldsInUpsert()
            val requestId = getRequestId()
            val valuesWithErrors = getFlashDataAndClear(requestId)
            val errors = valuesWithErrors.second ?: emptyList()
            val errorValues = valuesWithErrors.first
            val values = errorValues?.takeIf { it.isNotEmpty() } ?: processFieldValues(fields, data, this)
            val singularName = panel.getSingularName().replaceFirstChar { it.uppercaseChar() }
            respond(
                VelocityContent(
                    "${Constants.TEMPLATES_PREFIX_PATH}/admin_panel_no_sql_upsert.vm", model = mutableMapOf(
                        "fields" to fields,
                        "values" to values,
                        "errors" to errors.toMap(),
                        "collectionName" to panel.getCollectionName(),
                        "singularName" to singularName,
                        "pluralName" to panel.getPluralName(),
                        "primaryKey" to primaryKey,
                        "csrfToken" to CsrfManager.generateToken(),
                        "panelGroups" to panelGroups,
                        "currentPanel" to panel.getPluralName(),
                        "isUpdate" to true,
                        "requestId" to requestId,
                        "hasAction" to panel.hasEditAction,
                        "canDownload" to DynamicConfiguration.canDownloadDataAsPdf,
                        "previews" to extractFieldsPreviews(
                            panel.getCollectionName(),
                            fieldsWithValues = values.mapKeys { item -> fields.first { field -> field.fieldName == item.key } }
                        ),
                        "title" to translator.translate(
                            KtorAdminTranslator.Keys.UPDATE_ITEM,
                            mapOf("name" to singularName)
                        )
                    ).addCommonUpsertModels(panel, username).toMutableMap()
                        .addCommonModels(panel, panelGroups, applicationCall = this)
                )
            )
        }.onFailure {
            serverError("Error: ${it.message}", it)
        }
    }
}

suspend fun handleColumnPreviewValue(columnSet: ColumnSet, value: String, call: ApplicationCall) =
    when (columnSet.type) {
        ColumnType.FILE -> FileRepository.generateMediaUrl(columnSet.uploadTarget!!, value, call)
        else -> value
    }

suspend fun handleFieldPreviewValue(fieldSet: FieldSet, value: String, call: ApplicationCall) = when (fieldSet.type) {
    FieldType.File -> FileRepository.generateMediaUrl(fieldSet.uploadTarget!!, value, call)
    else -> value
}

private suspend fun processColumnValues(
    columns: List<ColumnSet>,
    data: List<String?>,
    call: ApplicationCall
): Map<String, Any?> = coroutineScope {
    columns.mapIndexed { index, column ->
        async {
            column.columnName to data[index]?.let { item ->
                handleColumnPreviewValue(column, item, call)
            }
        }
    }.awaitAll().toMap()
}

private suspend fun processFieldValues(
    fields: List<FieldSet>,
    data: List<String?>,
    call: ApplicationCall
): Map<String, Any?> = coroutineScope {
    fields.mapIndexed { index, field ->
        async {
            field.fieldName.orEmpty() to data[index]?.let { item ->
                handleFieldPreviewValue(field, item, call)
            }
        }
    }.awaitAll().toMap()
}