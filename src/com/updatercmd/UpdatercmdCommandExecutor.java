package com.updatercmd;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
	
public class UpdatercmdCommandExecutor implements CommandExecutor {

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Commande joueur uniquement !");
		} else {
			Player player = (Player) sender;
			if(!player.isOp()){
				player.sendMessage(ChatColor.RED + "Commande opérateur uniquement !");
			} else {
				ExecuteCommand(player);
				return true;
			}
		}
		
		
		return false;
	}
	
	
	public void ExecuteCommand(Player player) {
		int radius = 20;
		Block playerBlock = player.getLocation().getBlock();
		int cmdBlocks = 0;
		
		for (int x = -(radius); x <= radius; x++){
			
			for (int y = -(radius); y <= radius; y++){
			  
				for (int z = -(radius); z <= radius; z++){
			    	
			    	// Pour chaque bloc dans le rayon
					Block block = playerBlock.getRelative(x, y, z);
					
					if(block.getType().toString().toUpperCase().contains("COMMAND_BLOCK")){
						if(UpdateCommandBlock(player, (CommandBlock) block.getState())) {
							cmdBlocks++;
						}
						
					}
				}
		    
			}
		  
		}
		
		player.sendMessage(ChatColor.YELLOW + "Vous avez mis à jour " + cmdBlocks + " command blocks.");
		Plugin.LOG.info(cmdBlocks + " command blocks ont été modifiés.");
		
	}
	
	public boolean UpdateCommandBlock(Player player, CommandBlock cmdblock){
		String oldcmd = cmdblock.getCommand();
		String newcmd = oldcmd;
		
		if(oldcmd.startsWith("execute") || oldcmd.startsWith("/execute")) {
			newcmd = oldcmd.replaceAll("execute @", "execute as @");
			newcmd = newcmd.replaceAll("r=","distance=");
			newcmd = newcmd.replaceAll("~ ~ ~ /","run ");
		}
		
		if(oldcmd.contains("setblock")){
			newcmd = newcmd.replaceAll("_button 0", "_button[face=ceiling]");
			newcmd = newcmd.replaceAll("_button 1", "_button[facing=east]");
			newcmd = newcmd.replaceAll("_button 2", "_button[facing=west]");
			newcmd = newcmd.replaceAll("_button 3", "_button[facing=south]");
			newcmd = newcmd.replaceAll("_button 4", "_button[face=floor]");
		}
		
		if(!oldcmd.equals(newcmd)) {
			cmdblock.setCommand(newcmd);
			cmdblock.update();
			String alert = ChatColor.GOLD + "Cmd block modifié de : " + ChatColor.WHITE + oldcmd + ChatColor.GOLD + " à : " + ChatColor.WHITE + newcmd;
			player.sendMessage(alert);
			Plugin.LOG.info("Cmd block modifié de : " + oldcmd + " à : " + newcmd);
			return true;
		} else {
			return false;
		}
		
	}

}
