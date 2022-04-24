package org.browsit.mcmmoquests;

import me.blackvein.quests.Quests;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.AbstractMap;
import java.util.Map;

public class mcMMOModule extends JavaPlugin {
    private static final Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");
    private static final String moduleName = "mcMMO Overhaul Quests Module";
    private static final Map.Entry<String, Short> moduleItem = new AbstractMap.SimpleEntry<>("DIAMOND_AXE", (short)1562);

    public static Quests getQuests() {
        return quests;
    }

    public static String getModuleName() {
        return moduleName;
    }

    public static Map.Entry<String, Short> getModuleItem() {
        return moduleItem;
    }

    @Override
    public void onEnable() {
        getLogger().severe(ChatColor.RED + "Move this jar to your " + File.separatorChar + "Quests" + File.separatorChar
                + "modules folder!");
        getServer().getPluginManager().disablePlugin(this);
        setEnabled(false);
    }

    @Override
    public void onDisable() {
    }
}
