package modules

import annotations.errors.badRequest
import annotations.errors.notFound
import annotations.errors.serverError
import configuration.DynamicConfiguration
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.velocity.*
import models.*
import models.ReferenceItem
import models.PanelGroup
import models.toTableGroups
import modules.add.handleAddNewItem
import modules.list.handlePanelList
import modules.update.handleEditItem
import repository.FileRepository
import repository.JdbcQueriesRepository
import tables.AdminJdbcTable
import tables.AdminPanel
import utils.Constants
import tables.getAllAllowToShowColumns
import utils.withAuthenticate


internal fun Routing.configureGetRouting(tables: List<AdminPanel>, authenticateName: String? = null) {
    val panelGroups = tables.toTableGroups()
    withAuthenticate(authenticateName) {
        route("/admin") {
            get {
                call.renderAdminPanel(panelGroups)
            }
            route("/{pluralName}") {
                get {
                    call.handlePanelList(tables)
                }
                get("add") {
                    call.handleAddNewItem(tables)
                }
                get("/{primaryKey}") {
                    call.handleEditItem(tables)
                }
            }
        }
    }
}

private suspend fun ApplicationCall.renderAdminPanel(panelGroups: List<PanelGroup>) {
    respond(
        VelocityContent(
            "${Constants.TEMPLATES_PREFIX_PATH}/admin_panel.vm",
            model = mutableMapOf("tableGroups" to panelGroups)
        )
    )
}
