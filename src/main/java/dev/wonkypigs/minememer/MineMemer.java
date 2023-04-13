package dev.wonkypigs.minememer;

import co.aikar.commands.BukkitCommandManager;
import dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.Banking.balanceCommand;
import dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.Banking.depositCommand;
import dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.Banking.withdrawCommand;
import dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.MakingMoney.begCommand;
import dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.MakingMoney.searchCommand;
import dev.wonkypigs.minememer.Listeners.MenuListeners.searchMenuListener;
import dev.wonkypigs.minememer.Listeners.playerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;

public final class MineMemer extends JavaPlugin {
    private static MineMemer instance;{ instance = this; }
    public String db_type, host, database, username, password; public int port;
    private Connection connection;
    public YamlConfiguration lang, economy, config;
    public String currencyName;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Starting up...");
        //
        mysqlSetup();
        getConf();
        getLang();
        getEconomy();
        registerCommands();
        registerListeners();
        //
        getLogger().info("Startup Successful.");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Shutting down...");
        //
        getLogger().info("Shutdown Successful.");
    }

    public void registerCommands() {
        BukkitCommandManager commandManager = new BukkitCommandManager(this);

        commandManager.registerCommand(new balanceCommand());
        commandManager.registerCommand(new depositCommand());
        commandManager.registerCommand(new withdrawCommand());
        commandManager.registerCommand(new begCommand());
        commandManager.registerCommand(new searchCommand());
    }
    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new playerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new searchMenuListener(), this);
    }

    public void getLang() {
        File file = new File(getDataFolder(), "lang.yml");
        lang = YamlConfiguration.loadConfiguration(file);
    }
    public void getEconomy() {
        File file = new File(getDataFolder(), "economy.yml");
        economy = YamlConfiguration.loadConfiguration(file);
        currencyName = economy.getString("currency-name");
    }
    public void getConf() {
        File file = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void getDatabaseInfo() {
        try {
            db_type = config.getString("db_type");
            host = config.getString("db_host");
            port = config.getInt("db_port");
            database = config.getString("db_database");
            username = config.getString("db_username");
            password = config.getString("db_password");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mysqlSetup() {
        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                if (db_type.equalsIgnoreCase("sqlite")) {
                    // create local database file and stuff
                    Class.forName("org.sqlite.JDBC");
                    File file = new File(getDataFolder(), "database.db");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    setConnection(DriverManager.getConnection("jdbc:sqlite:" + file));
                } else if (db_type.equalsIgnoreCase("mysql")) {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    // create database if not exists
                    setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "?autoReconnect=true&useSSL=false", username, password));
                    getConnection().createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);
                    setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
                } else {
                    // shut off plugin and send error
                    getLogger().severe("------------------------");
                    getLogger().severe("Invalid database type in config.yml!\n" +
                            "Please use either 'mysql' or 'sqlite'.");
                    getLogger().severe("------------------------");
                    Bukkit.getPluginManager().disablePlugin(this);
                }

                getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS mm_pdata (uuid TEXT, name TEXT, purse int, bankStored int, bankLimit int)").executeUpdate();
                getLogger().info("Successfully connected to the MySQL database");
            }
        } catch (Exception e) {
            getLogger().severe("Error connecting to the MySQL database");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public static MineMemer getInstance() {
        return instance;
    }
}
