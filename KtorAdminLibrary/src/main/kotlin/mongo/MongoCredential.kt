package mongo

data class MongoCredential(
    val username: String,
    val database: String,
    val password: String
)
