package annotations.references

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class References(
    val tableName: String,
    val targetColumn: String
)