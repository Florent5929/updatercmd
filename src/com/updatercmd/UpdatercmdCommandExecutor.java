package com.updatercmd;

import org.bukkit.ChatColor;
import org.bukkit.Location;
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
				ExecuteCommand(player, args);
				return true;
			}
		}
		
		
		return false;
	}
	
	
	public void ExecuteCommand(Player player, String[] args) {
		int radius = 0;
		
		try {
			if(args.length != 0){
				radius = Integer.parseInt(args[0]);
			}
		} catch (Exception e) {
			
		} finally {
			if(radius <= 0) {
				radius = 20;
			}	
		}
		
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
		
		player.sendMessage(ChatColor.YELLOW + "Vous avez mis à jour " + cmdBlocks + " command blocks.\nVoir les logs pour plus de détails.");
		Plugin.LOG.info(cmdBlocks + " command blocks ont été modifiés.");
		
	}
	
	public boolean UpdateCommandBlock(Player player, CommandBlock cmdblock){
		String oldcmd = cmdblock.getCommand();
		String newcmd = oldcmd;
		

		newcmd = oldcmd.replaceAll("execute @", "execute as @");
		newcmd = newcmd.replaceAll("r=","distance=..");
		newcmd = newcmd.replaceAll("~ ~ ~ /","run ");
		newcmd = newcmd.replaceAll("type=Player","type=player");
		
		if(newcmd.matches(".+minecraft:[a-zA-Z_]+ [0-9] [0-9]$")){
			// Si contient un ou plusieurs caractères suivi de minecraft: suivi d'un ou plusieurs caractères alphabétiques (symbole _ accepté)
			// suivi d'un espace, suivi d'un chiffre, suivi d'un autre espace, suivi d'un autre chiffre
			// puis suivi de la fin de la chaîne
			// alors on supprime le dernier chiffre et le dernier espace
			
			newcmd = newcmd.substring(0, newcmd.length()-2);		
		}
		
		
		if(oldcmd.contains("setblock")){
			newcmd = newcmd.replaceAll("wooden_button 0", "oak_button[face=ceiling]");
			newcmd = newcmd.replaceAll("wooden_button 1", "oak_button[facing=east]");
			newcmd = newcmd.replaceAll("wooden_button 2", "oak_button[facing=west]");
			newcmd = newcmd.replaceAll("wooden_button 3", "oak_button[facing=south]");
			newcmd = newcmd.replaceAll("wooden_button 4", "oak_button[face=floor]");
			
			newcmd = newcmd.replaceAll("minecraft:stone 0", "minecraft:stone");
			newcmd = newcmd.replaceAll("minecraft:stone 1", "minecraft:granite");
			newcmd = newcmd.replaceAll("minecraft:stone 2", "minecraft:polished_granite");
			newcmd = newcmd.replaceAll("minecraft:stone 3", "minecraft:diorite");
			newcmd = newcmd.replaceAll("minecraft:stone 4", "minecraft:polished_diorite");
			newcmd = newcmd.replaceAll("minecraft:stone 5", "minecraft:andesite");
			newcmd = newcmd.replaceAll("minecraft:stone 6", "minecraft:polished_andesite");
		}
		
		if(oldcmd.contains("fill")){
			newcmd = newcmd.replaceAll("minecraft:air 0 replace", "minecraft:air replace");
		}
		
		//if(oldcmd.contains("particle")){
		//	newcmd = newcmd.replaceAll("particle blockcrack", "particle block oak_planks");
		//}
		
		if(!oldcmd.equals(newcmd)) {
			cmdblock.setCommand(newcmd);
			cmdblock.update();
			String alert = ChatColor.GOLD + "Cmd block modifié de : " + ChatColor.WHITE + oldcmd + ChatColor.GOLD + " à : " + ChatColor.WHITE + newcmd;
			player.sendMessage(alert);
			Plugin.LOG.info("Cmd block modifié de : " + oldcmd + " à : " + newcmd);
			Location loc = player.getLocation();
			Plugin.LOG.info("Location : x=" + loc.getX() + ", y=" + loc.getY() + ", z=" + loc.getZ());
			return true;
		} else {
			return false;
		}
		
	}

}
