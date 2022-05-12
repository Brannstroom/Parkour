package me.brannstrom;

import com.google.common.collect.Lists;
import me.brannstrom.Commands.ParkourCommand;
import me.brannstrom.Handlers.ParkourHandler;
import me.brannstrom.Listeners.ParkourListener;
import me.brannstrom.Model.Parkour;
import me.brannstrom.Service.ParkourService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.TimeZone;

public final class ParkourPlugin extends JavaPlugin {

    public static ParkourPlugin instance;

    public FileConfiguration config;

    public void onEnable() {
        instance = this;
        loadListeners();
        loadCommands();

        getDataFolder().mkdir();
        loadConfig();
        saveConfig();

        TimeZone timezoneDefault = TimeZone.getTimeZone("Europe/Oslo");
        TimeZone.setDefault(timezoneDefault);

        List<Parkour> parkours = ParkourService.getParkours();
        if(parkours != null) {
            for(Parkour parkour : parkours) {
                ParkourHandler.createHologram(parkour);
            }
        }
    }

    public void onDisable() {

    }

    public void loadConfig() {
        config = getConfig();
        config.options().copyDefaults(true);

        // API
        config.addDefault("api.url", "127.0.0.1:8088");
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new ParkourListener(), this);
    }

    private void loadCommands() {
        registerCommand("parkour", new ParkourCommand());
    }

    public void registerCommand(String name, CommandExecutor executor, String... aliases) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand command = constructor.newInstance(name, this);

            command.setExecutor(executor);
            command.setAliases(Lists.newArrayList(aliases));
            if (executor instanceof TabCompleter) {
                command.setTabCompleter((TabCompleter) executor);
            }
            this.getCommandMap().register("parkour", command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CommandMap getCommandMap() {
        try {
            org.bukkit.Server server = Bukkit.getServer();
            Field commandMap = server.getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            return (CommandMap) commandMap.get(server);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}