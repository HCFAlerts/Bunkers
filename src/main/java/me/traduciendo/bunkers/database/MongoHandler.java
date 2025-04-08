/*package me.traduciendo.bunkers.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.traduciendo.bunkers.Bunkers;
import me.traduciendo.bunkers.utils.file.ConfigCursor;
import org.bson.Document;

import java.util.Collections;

public class MongoHandler {

    private final Bunkers plugin;

    private MongoClient client;
    private MongoDatabase mongoDatabase;

    @Getter
    private final MongoCollection<Document> players;
    @Getter private final MongoCollection<Document> matchHistory;

    public MongoHandler(Bunkers instance) {
        this.plugin = instance;
        ConfigCursor mongoConfig = new ConfigCursor(instance.getSettingsConfig(), "MONGO");

        try {
            instance.getLogger().info("Connecting to MongoDB...");

            if (!mongoConfig.getBoolean("URI-MODE")) {
                String host = mongoConfig.getString("NORMAL.HOST");
                int port = mongoConfig.getInt("NORMAL.PORT");
                if (mongoConfig.getBoolean("NORMAL.AUTH.ENABLED")) {
                    this.client = new MongoClient(new ServerAddress(host, port), Collections.singletonList(MongoCredential.createCredential(
                            mongoConfig.getString("NORMAL.AUTH.USERNAME"),
                            mongoConfig.getString("NORMAL.AUTH.AUTH-DATABASE"),
                            mongoConfig.getString("NORMAL.AUTH.PASSWORD").toCharArray())
                    ));
                } else {
                    this.client = new MongoClient(new ServerAddress(host, port));
                }
                this.mongoDatabase = this.client.getDatabase(mongoConfig.getString("NORMAL.DATABASE"));
                instance.getLogger().finest("Successfully connected to MongoDB.");
            } else {
                MongoClientURI mongoClientURI = new MongoClientURI(mongoConfig.getString("URI.CONNECTION-STRING"));
                try (MongoClient mongoClient = new MongoClient(mongoClientURI)) {
                    this.mongoDatabase = mongoClient.getDatabase(mongoConfig.getString("URI.DATABASE-NAME"));
                }
                instance.getLogger().finest("Successfully connected to MongoDB (URI).");
            }
        } catch (Exception e) {
            instance.getServer().getPluginManager().disablePlugin(this.plugin);
            instance.getLogger().info("Disabling Bunkers Core because an error occurred while trying to connect to MongoDB.");
        }

        this.players = this.mongoDatabase.getCollection("players");
        this.matchHistory = this.mongoDatabase.getCollection("matchHistory");
    }

    public void disconnect() {
        if (this.client != null) {
            this.client.close();
            plugin.getLogger().info("[MongoDB] MongoDB has been disconnected.");
        }
    }
}
*/