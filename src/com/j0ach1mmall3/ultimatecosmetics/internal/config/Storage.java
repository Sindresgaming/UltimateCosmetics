package com.j0ach1mmall3.ultimatecosmetics.internal.config;

import com.j0ach1mmall3.jlib.methods.General;
import com.j0ach1mmall3.jlib.storage.DataType;
import com.j0ach1mmall3.jlib.storage.file.yaml.ConfigLoader;
import com.j0ach1mmall3.ultimatecosmetics.Main;
import org.bukkit.ChatColor;

/**
 * Created by j0ach1mmall3 on 22:34 29/08/2015 using IntelliJ IDEA.
 */
public final class Storage extends ConfigLoader {
    private DataType type;
    private final String database_Host;
    private final int database_Port;
    private final String database_Database;
    private final String database_User;
    private final String database_Password;
    private final String database_Prefix;

    public Storage(Main plugin) {
        super("storage.yml", plugin);
        try {
            this.type = DataType.valueOf(this.config.getString("Type"));
            if (plugin.getBabies().getLoggingLevel() >= 2) General.sendColoredMessage(plugin, "Using " + this.type + " for Storage!", ChatColor.GREEN);
        } catch(IllegalArgumentException e) {
            this.type = DataType.FILE;
            if (plugin.getBabies().getLoggingLevel() >= 2) General.sendColoredMessage(plugin, "Unknown StorageType! Switching over to FILE!", ChatColor.GOLD);
        }
        this.database_Host = this.config.getString("Database.Host");
        this.database_Port = this.config.getInt("Database.Port");
        this.database_Database = this.config.getString("Database.Database");
        this.database_User = this.config.getString("Database.User");
        this.database_Password = this.config.getString("Database.Password");
        this.database_Prefix = this.config.getString("Database.Prefix");
    }

    public DataType getType() {
        return this.type;
    }

    public String getDatabaseHost() {
        return this.database_Host;
    }

    public int getDatabasePort() {
        return this.database_Port;
    }

    public String getDatabaseDatabase() {
        return this.database_Database;
    }

    public String getDatabaseUser() {
        return this.database_User;
    }

    public String getDatabasePassword() {
        return this.database_Password;
    }

    public String getDatabasePrefix() {
        return this.database_Prefix;
    }
}
