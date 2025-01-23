package plugins

import configuration.KtorAdminConfiguration
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import models.forms.UserForm
import modules.configureRouting
import modules.configureSessions
import modules.configureTemplating
import repository.AdminTableRepository

class KtorAdmin {
    companion object Plugin : BaseApplicationPlugin<Application, KtorAdminConfiguration, KtorAdmin> {
        override val key: AttributeKey<KtorAdmin>
            get() = AttributeKey("Ktor Admin")

        override fun install(pipeline: Application, configure: KtorAdminConfiguration.() -> Unit): KtorAdmin {
            val tables = AdminTableRepository.getAll()
            val configuration = KtorAdminConfiguration().apply(configure)
            val authenticateName = configuration.authenticateName
            pipeline.configureTemplating()
            pipeline.configureRouting(authenticateName, tables)
            pipeline.configureSessions()
            pipeline.monitor.subscribe(ApplicationStopPreparing) { configuration.closeDatabase() }
            return KtorAdmin()
        }
    }
}