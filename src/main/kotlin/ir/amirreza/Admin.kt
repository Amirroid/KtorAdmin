package ir.amirreza

import annotations.value_mapper.ValueMapper
import io.ktor.server.application.*
import ir.amirreza.action.MyCustomAction
import ir.amirreza.dashboard.CustomDashboard
import ir.amirreza.listeners.AdminListener
import ir.amirreza.previews.ImagePreview
import ir.amirreza.previews.VideoPreview
import mapper.KtorAdminValueMapper
import models.JDBCDrivers
import models.UploadTarget
import models.forms.LoginFiled
import mongo.MongoCredential
import mongo.MongoServerAddress
import org.jetbrains.exposed.sql.Database
import plugins.KtorAdmin
import providers.StorageProvider
import tiny.TinyMCEConfig
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

const val MEDIA_ROOT = "files"
const val MEDIA_PATH = "uploads"

fun Application.configureAdmin(database: Database) {
    install(KtorAdmin) {
        jdbc(
            key = null,
            url = "jdbc:postgresql://localhost:5432/postgres",
            username = "amirreza",
            password = "your_password",
            driver = JDBCDrivers.POSTGRES
        )
        mongo(
            "0@localhost",
            MongoServerAddress("localhost", 27017),
            MongoCredential(
                "amirreza", "admin", "your_password"
            ),
        )
        mediaPath = MEDIA_PATH
        mediaRoot = MEDIA_ROOT
        adminDashboard = CustomDashboard()
        defaultAwsS3Bucket = "school-data"
        s3SignatureDuration = 1.minutes.toJavaDuration()
        authenticateName = "admin"
        loginFields = adminLoginFields
        csrfTokenExpirationTime = 1000 * 60
        registerCustomAdminActionForAll(MyCustomAction())
        registerEventListener(AdminListener(database))
        canDownloadDataAsCsv = true
        canDownloadDataAsPdf = true
        tinyMCEConfig = TinyMCEConfig.Professional.copy(uploadTarget = UploadTarget.LocalFile(path = null))
        registerValueMapper(
            CustomValueMapper
        )
        registerPreview(VideoPreview())
        registerPreview(ImagePreview())
        registerValueMapper(
            CustomValueMapper2
        )
    }
}

object CustomValueMapper : KtorAdminValueMapper {
    override fun map(value: Any?): Any? {
        return when (value) {
            is Int -> value.times(2)
            else -> null
        }
    }

    override fun restore(value: Any?): Any? {
        return when (value) {
            is Int -> value.div(2)
            else -> null
        }
    }

    override val key: String
        get() = "timesTo2"
}

object CustomValueMapper2 : KtorAdminValueMapper {
    override fun map(value: Any?): Any? {
        return (value as? String)?.plus(" Test")
    }

    override fun restore(value: Any?): Any? {
        return (value as? String)?.replace(" Test", "")
    }

    override val key: String
        get() = "test"
}

private val adminLoginFields = listOf(
    LoginFiled(
        name = "Username",
        key = "username",
        autoComplete = "username"
    ),
    LoginFiled(
        name = "Password",
        key = "password",
        autoComplete = "current-password",
        type = "password"
    )
)