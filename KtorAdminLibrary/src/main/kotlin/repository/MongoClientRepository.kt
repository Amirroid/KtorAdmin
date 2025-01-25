package repository

import com.mongodb.MongoClientSettings
import com.mongodb.ServerAddress
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import mongo.MongoCredential
import mongo.MongoServerAddress
import org.bson.Document
import panels.AdminMongoCollection

internal object MongoClientRepository {
    private var clients: MutableMap<String, MongoClient> = mutableMapOf()
    var defaultDatabaseName: String? = null

    fun registerNewClient(databaseName: String, address: MongoServerAddress, credential: MongoCredential? = null) {
        if (clients.isEmpty() && defaultDatabaseName == null) {
            defaultDatabaseName = databaseName
        }
        if (clients.containsKey(databaseName)) {
            throw IllegalArgumentException("Client with database name '$databaseName' already exists")
        }
        val settings = MongoClientSettings.builder()
            .let {
                if (credential != null) {
                    it.credential(
                        com.mongodb.MongoCredential.createCredential(
                            credential.username, credential.database, credential.password.toCharArray()
                        )
                    )
                } else it
            }
            .applyToClusterSettings { cluster -> cluster.hosts(listOf(ServerAddress(address.host, address.port))) }
            .build()
        clients[databaseName] = MongoClient.create(settings)
    }

    fun AdminMongoCollection.getCollection(): MongoCollection<Document> {
        val databaseKey = getDatabaseKey() ?: defaultDatabaseName
        ?: throw IllegalStateException("Both database key and default database name are null.")
        val client =
            clients[databaseKey] ?: throw IllegalArgumentException("Client for database name $databaseKey not found.")
        return client.getDatabase(databaseKey).getCollection(getCollectionName())
    }

    suspend fun insertData(
        values: Map<String, Any?>,
        table: AdminMongoCollection
    ) {
        val document = Document().apply {
        }

    }
}