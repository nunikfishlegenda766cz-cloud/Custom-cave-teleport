package com.example.cave;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CavePlugin extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        getCommand("cave").setExecutor(this);
        getLogger().info("CavePlugin byl úspěšně spuštěn!");
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tento příkaz mohou používat pouze hráči.");
            return true;
        }

        Player player = (Player) sender;
        Location loc = player.getLocation();
        int startX = loc.getBlockX();
        int startZ = loc.getBlockZ();
        int startY = loc.getBlockY() - 5; // Začneme hledat pod hráčem

        player.sendMessage(ChatColor.YELLOW + "🔍 Hledám nejbližší jeskyni...");

        // Prohledáváme dolů směrem k bedroku (až do Y = 0)
        boolean found = false;
        Location targetLoc = null;

        for (int y = startY; y > 0; y--) {
            Block block1 = loc.getWorld().getBlockAt(startX, y, startZ);
            Block block2 = loc.getWorld().getBlockAt(startX, y + 1, startZ);

            // Hledáme prostor, kde je pod nohama pevný blok a nad ním 2 bloky vzduchu (jeskyně / tunel)
            if (!block1.getType().isAir() && block1.getType() != Material.WATER && block1.getType() != Material.LAVA &&
                block2.getType().isAir() && loc.getWorld().getBlockAt(startX, y + 2, startZ).getType().isAir()) {
                
                targetLoc = new Location(loc.getWorld(), startX + 0.5, y + 1, startZ + 0.5, loc.getYaw(), loc.getPitch());
                found = true;
                break;
            }
        }

        if (found && targetLoc != null) {
            player.teleport(targetLoc);
            player.playSound(targetLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
            player.sendMessage(ChatColor.GREEN + "✨ Úspěšně teleportován do jeskyně!");
        } else {
            player.sendMessage(ChatColor.RED + "❌ Pod tebou nebyla nalezena žádná vhodná jeskyně, zkus to jinde!");
        }

        return true;
    }
}
