package dev.wonkypigs.minememer;

import co.aikar.commands.BukkitCommandManager;
import dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.Banking.balanceCommand;
import dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.Banking.depositCommand;
import dev.wonkypigs.minememer.Commands.PlayerCommands.Economy.Banking.withdrawCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.List;

public final class MineMemer extends JavaPlugin {
    private static MineMemer instance;{ instance = this; }
    public String host, database, username, password;
    private Connection connection;
    public int port;
    public YamlConfiguration lang;
    public YamlConfiguration economy;
    public String currencyName;
    public static MineMemer getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Starting up...");
        //
        mysqlSetup();
        registerCommands();
        getLang();
        getEconomy();
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

    public void mysqlSetup() {
        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }

                Class.forName("com.mysql.cj.jdbc.Driver");
                // create database if not exists
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "?autoReconnect=true&useSSL=false", username, password));
                getConnection().createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));

                getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS pdata (uuid TEXT, name TEXT, purse int, bankStored int, bankLimit int)").executeUpdate();
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
}
