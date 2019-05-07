package xyz.mukri.duels.file;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.mukri.duels.Core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitFile {

    File file;
    FileConfiguration config;

    public KitFile() {
        file = new File(Core.getInstance().getDataFolder(), "kits.yml");

        if (!isExists()) {
            Core.getInstance().saveResource("kits.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean isExists() {
        return file.exists();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    // Testing purpose
    public void giveKit(Player p) {
        List<String> list = config.getStringList("kits.default.items");

        for (String kits : list) {
            String[] kit = kits.split(":");

            ItemStack item = new ItemStack(Material.valueOf(kit[0]), Integer.valueOf(kit[1]), Byte.valueOf(kit[2]));
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(kit[3].replace("&", "§"));

            if (kit.length > 4) {
                for (int i = 4; i < kit.length; i++) {
                    String[] encha = kit[i].split("-");

                    meta.addEnchant(Enchantment.getByName(encha[0]), Integer.valueOf(encha[1]), false);
                }
            }

            item.setItemMeta(meta);

            p.getInventory().addItem(item);
        }
    }

    // Testing purpose (armor)
    public void giveArmor(Player p) {
        List<String> armors = config.getStringList("kits.default.armor");

        for (String armor : armors) {
            String[] items = armor.split(":");

            ItemStack item =  new ItemStack(Material.valueOf(items[0]), 1);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(items[1].replace("&", "§"));

            if (items.length > 2) {
                for (int i = 2; i < items.length; i++) {
                    String[] encha = items[i].split("-");

                    meta.addEnchant(Enchantment.getByName(encha[0]), Integer.valueOf(encha[1]), false);
                }
            }

            item.setItemMeta(meta);

            if (items[0].contains("HELMET")) {
                p.getInventory().setHelmet(item);
            }
            else if (items[0].contains("CHESTPLATE")) {
                p.getInventory().setChestplate(item);
            }
            else if (items[0].contains("LEGGINGS")) {
                p.getInventory().setLeggings(item);
            }
            else if (items[0].contains("BOOTS")) {
                p.getInventory().setBoots(item);
            }
        }
    }

    // Testing purpose
    public void createNewKit(Player p, String kitName) {
        List<String> kits = new ArrayList<>();

        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() != Material.AIR || item.getType() != null) {
                    p.sendMessage(item.getType().toString());
                }
            }
        }
    }
}
