package annotations.info


/**
 * You can also specify an SQL query as the default value.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class ColumnInfo(
    val columnName: String,
    val defaultValue: String = "",
    val showInPanel: Boolean = true,
    val limits: String = ""
)