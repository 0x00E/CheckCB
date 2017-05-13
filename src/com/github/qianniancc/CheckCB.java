package com.github.qianniancc;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CheckCB
  extends JavaPlugin
  implements Listener
{
  public void onEnable()
  {
    if (!getDataFolder().exists()) {
      getDataFolder().mkdir();
    }
    File file = new File(getDataFolder(), "config.yml");
    if (!file.exists()) {
      saveDefaultConfig();
    }
    getLogger().info("启动成功！");
    reloadConfig();
    getServer().getPluginManager().registerEvents(this, this);
  }
  
  @SuppressWarnings("deprecation")
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (!(sender instanceof Player))
    {
      getLogger().info("请在游戏中执行指令！");
      return false;
    }
    Player p = (Player)sender;
    if (cmd.getName().equalsIgnoreCase("checkcb")) {
      if ((p.hasPermission("checkcb.yes")) || (p.isOp()) || (p.hasPermission("checkcb.*")))
      {
        World world = p.getWorld();
        int px = (int)p.getLocation().getX();
        int py = (int)p.getLocation().getY();
        int pz = (int)p.getLocation().getZ();
        int isHas = 0;
        int rx = getConfig().getInt("x");
        int ry = getConfig().getInt("y");
        int rz = getConfig().getInt("z");
        for (int x = px - rx; x <= px + rx; x++) {
          for (int y = py - ry; y <= py + ry; y++) {
            for (int z = pz - rz; z <= pz + rz; z++) {
              if ((world.getBlockAt(x, y, z).getTypeId() == 137) || (world.getBlockAt(x, y, z).getTypeId() == 210) || (world.getBlockAt(x, y, z).getTypeId() == 211))
              {
                CommandBlock cb = (CommandBlock)world.getBlockAt(x, y, z).getState();
                if ((cb.getCommand().contains("/setblock")) || (cb.getCommand().contains("setblock")))
                {
                  world.getBlockAt(x, y, z).setType(Material.AIR);
                  p.sendMessage("§a§l检测到坐标" + x + "," + y + "," + z + "含有放置方块的指令，即清除！");
                  isHas++;
                }
              }
            }
          }
        }
        if (isHas == 0) {
          p.sendMessage("§e§l没有找到放置方块的CB。");
        } else {
          p.sendMessage("§e§l已清除" + isHas + "个命令方块");
        }
      }
      else
      {
        p.sendMessage("§c§l你没有权限执行这个指令。");
      }
    }
    if (cmd.getName().equalsIgnoreCase("ccbreload")) {
      if ((p.hasPermission("checkcb.reload")) || (p.isOp()) || (p.hasPermission("checkcb.*")))
      {
        reloadConfig();
        p.sendMessage("§a§l你成功重置了插件！");
      }
      else
      {
        p.sendMessage("§c§l你没有权限执行这个指令。");
      }
    }
    return false;
  }
}
