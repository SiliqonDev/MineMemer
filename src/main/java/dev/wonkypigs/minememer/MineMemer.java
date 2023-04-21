package dev.wonkypigs.minememer;

import co.aikar.commands.BukkitCommandManager;
import dev.wonkypigs.minememer.commands.adminCommands.*;
import dev.wonkypigs.minememer.commands.generalUtils.InventoryCommand;
import dev.wonkypigs.minememer.commands.playerCommands.economy.banking.*;
import dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.begging.BegCommand;
import dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.digging.DigCommand;
import dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.fishing.FishCommand;
import dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.hunting.HuntCommand;
import dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.mining.MineCommand;
import dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.postingMemes.PostMemeCommand;
import dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.searching.SearchCommand;
import dev.wonkypigs.minememer.commands.playerCommands.economy.store.StoreCommand;
import dev.wonkypigs.minememer.listeners.menuListeners.*;
import dev.wonkypigs.minememer.listeners.PlayerJoinListener;
import dev.wonkypigs.minememer.mobs.DogeSpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dev.wonkypigs.minememer.helpers.InventoryUtils.*;
import static dev.wonkypigs.minememer.commands.playerCommands.economy.makingMoney.fishing.FishCommandUtils.*;

public final class MineMemer extends JavaPlugin {
    private static MineMemer instance;{ instance = this; }
    public String db_type, host, database, username, password; public int port;
    private Connection connection;
    private File langFile; public FileConfiguration lang;
    private File economyFile; public FileConfiguration economy;
    private File itemsFile; public FileConfiguration items;
    public Material menubg, menubg2;
    public String currencyName;
    private BukkitCommandManager commandManager;
    public String buyButtonKeyName = "buy_button",
            sellButtonKeyName = "sell_button",
            backButtonKeyName = "back_button",
            validItemKeyName = "valid_item",
            fishItemKeyName = "fish",
            animalItemKeyName = "animal",
            searchMenuItemKeyName = "search_option",
            dogeKeyName = "doge";
    public final UUID HEADS_RANDOM_UUID = UUID.fromString("37234774-51c5-4c3b-9039-435c9927d1b4");

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Starting up...");
        // all configs loading up
        saveDefaultConfig();
        getConfigValues();
        createLangFile();
        createEconomyFile();
        loadItemsResource();
        // database init
        getDatabaseInfo();
        mysqlSetup();
        // register imp stuff
        registerCommands();
        registerCommandCompletion();
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
        commandManager = new BukkitCommandManager(this);
        // banking
        commandManager.registerCommand(new BalanceCommand());
        commandManager.registerCommand(new DepositCommand());
        commandManager.registerCommand(new WithdrawCommand());
        // makin bank
        commandManager.registerCommand(new BegCommand());
        commandManager.registerCommand(new SearchCommand());
        commandManager.registerCommand(new FishCommand());
        commandManager.registerCommand(new PostMemeCommand());
        commandManager.registerCommand(new DigCommand());
        commandManager.registerCommand(new MineCommand());
        commandManager.registerCommand(new HuntCommand());
        // admin
        commandManager.registerCommand(new GiveEcoCommand());
        commandManager.registerCommand(new TakeEcoCommand());
        commandManager.registerCommand(new GiveItemCommand());
        commandManager.registerCommand(new TakeItemCommand());
        // misc
        commandManager.registerCommand(new InventoryCommand());
        commandManager.registerCommand(new StoreCommand());
    }
    public void registerCommandCompletion() {
        // command completions
        // --- all offline players "@AllPlayers"
        commandManager.getCommandCompletions().registerCompletion("AllPlayers", context -> {
            List<String> nameList = new ArrayList<>();
            for (OfflinePlayer player: Bukkit.getOfflinePlayers()) {
                nameList.add(player.getName());
            }
            return nameList;
        });
        // --- all items "@AllItems"
        commandManager.getCommandCompletions().registerCompletion("AllItems", context -> getValidItemList());
        // --- all fish "@AllFish"
        commandManager.getCommandCompletions().registerCompletion("AllFish", context -> getValidFishList());
    }
    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new SearchMenuListener(), this);
        getServer().getPluginManager().registerEvents(new BankMenuListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryMenuListener(), this);
        getServer().getPluginManager().registerEvents(new FishingMenuListener(), this);
        getServer().getPluginManager().registerEvents(new StoreMenuListener(), this);
        getServer().getPluginManager().registerEvents(new PostmemeMenuListener(), this);
        getServer().getPluginManager().registerEvents(new HuntingMenuListener(), this);

        getServer().getPluginManager().registerEvents(new DogeSpawnManager(), this); // <--- very experimental
    }

    public void getConfigValues() {
        menubg = Material.valueOf(getConfig().getString("menu-background-item"));
        menubg2 = Material.valueOf(getConfig().getString("menu-background-item-2"));
    }
    private void createLangFile() {
        langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            langFile.getParentFile().mkdirs();
            saveResource("lang.yml", false);
        }

        lang = YamlConfiguration.loadConfiguration(langFile);
    }
    private void createEconomyFile() {
        economyFile = new File(getDataFolder(), "economy.yml");
        if (!economyFile.exists()) {
            economyFile.getParentFile().mkdirs();
            saveResource("economy.yml", false);
        }

        economy = YamlConfiguration.loadConfiguration(economyFile);
        currencyName = economy.getString("currency-name");
    }
    private void loadItemsResource() {
        InputStream is = getResource("items.yml");
        InputStreamReader isr = new InputStreamReader(is);
        items = YamlConfiguration.loadConfiguration(isr);
    }

    public void getDatabaseInfo() {
        try {
            db_type = getConfig().getString("db_type");
            host = getConfig().getString("db_host");
            port = getConfig().getInt("db_port");
            database = getConfig().getString("db_database");
            username = getConfig().getString("db_username");
            password = getConfig().getString("db_password");
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
                    getLogger().severe("Invalid database type in config.yml!\nPlease use either 'mysql' or 'sqlite'.");
                    getLogger().severe("------------------------");
                    Bukkit.getPluginManager().disablePlugin(this);
                }

                getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS mm_pdata (uuid TEXT, name TEXT, purse int, bankStored int, bankLimit int)").executeUpdate();
                getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS mm_inventory (uuid TEXT, item TEXT, amount int)").executeUpdate();
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
    public static void reloadPlugin() {
        instance.saveDefaultConfig();
        instance.getConfigValues();
        instance.createLangFile();
        instance.createEconomyFile();
        instance.loadItemsResource();
    }
}
