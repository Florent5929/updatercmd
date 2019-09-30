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
		
		newcmd = newcmd.replaceAll("r=","distance=..");
		newcmd = newcmd.replaceAll("type=Player","type=player");
		
		
		if(newcmd.matches("/{0,1}execute @.+ ~ ~ ~ /.+")){
			String parts[] = newcmd.split(" ");
			newcmd = oldcmd.replaceAll("execute @", "execute as @");
			newcmd = newcmd.replaceAll("~ ~ ~ /", new String("at " + parts[1] + " positioned ~ ~ ~ run "));
		}
		
		
		if(newcmd.matches(".+minecraft:[a-zA-Z_]+ [0-9] [0-9]$")){
			// Si contient un ou plusieurs caractères suivi de minecraft: suivi d'un ou plusieurs caractères alphabétiques (symbole _ accepté)
			// suivi d'un espace, suivi d'un chiffre, suivi d'un autre espace, suivi d'un autre chiffre
			// puis suivi de la fin de la chaîne
			// alors on supprime le dernier chiffre et le dernier espace
			
			newcmd = newcmd.substring(0, newcmd.length()-2);		
		}
		
		
		if(oldcmd.contains("setblock") || oldcmd.contains("fill")){
			
			newcmd = newcmd.replaceAll("wooden", "oak");
			
			newcmd = newcmd.replaceAll("wooden_button 0", "oak_button[face=ceiling]");
			newcmd = newcmd.replaceAll("wooden_button 1", "oak_button[facing=east]");
			newcmd = newcmd.replaceAll("wooden_button 2", "oak_button[facing=west]");
			newcmd = newcmd.replaceAll("wooden_button 3", "oak_button[facing=south]");
			newcmd = newcmd.replaceAll("wooden_button 4", "oak_button[face=floor]");
			
			newcmd = newcmd.replaceAll("chest 0", "chest[facing=north]");
			newcmd = newcmd.replaceAll("chest 1", "chest[facing=north]");
			newcmd = newcmd.replaceAll("chest 2", "chest[facing=north]");
			newcmd = newcmd.replaceAll("chest 3", "chest[facing=south]");
			newcmd = newcmd.replaceAll("chest 4", "chest[facing=west]");
			newcmd = newcmd.replaceAll("chest 5", "chest[facing=east]");
			
			///give @p[r=2] minecraft:paper 1 0 {NBT}  -> /give @p[distance=..2] minecraft:paper{NBT} 1
			
			// Aussi, jpense que ça pourra aider : 
			///setblock ~ ~ ~ minecraft:trapped_chest 1 replace <NBT à ne pas toucher>
			//->
			///setblock ~ ~ ~ minecraft:trapped_chest[facing=north]<NBT à ne pas toucher> replace
			
			if(newcmd.matches("/{0,1}setblock ~ ~ ~ minecraft:.*chest[facing=north] replace .+")){
				
				newcmd = newcmd.replaceAll("replace ", "");
				newcmd = new String(newcmd + " replace");
			
			}
			
			if(newcmd.matches("/{0,1}setblock ~ ~ ~ mob_spawner 0 replace .+")){
				
				newcmd = newcmd.replaceAll("mob_spawner 0 replace ", "spawner ");
				newcmd = new String(newcmd + " replace");
			
			}

			
			newcmd = newcmd.replaceAll("minecraft:stone 0", "minecraft:stone");
			newcmd = newcmd.replaceAll("minecraft:stone 1", "minecraft:granite");
			newcmd = newcmd.replaceAll("minecraft:stone 2", "minecraft:polished_granite");
			newcmd = newcmd.replaceAll("minecraft:stone 3", "minecraft:diorite");
			newcmd = newcmd.replaceAll("minecraft:stone 4", "minecraft:polished_diorite");
			newcmd = newcmd.replaceAll("minecraft:stone 5", "minecraft:andesite");
			newcmd = newcmd.replaceAll("minecraft:stone 6", "minecraft:polished_andesite");
			newcmd = newcmd.replaceAll("minecraft:air 0 replace", "minecraft:air replace");
			
			
			newcmd = newcmd.replaceAll("stonebrick", "stone_bricks");
			
			newcmd = newcmd.replaceAll("stained_glass 0", "white_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 1", "orange_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 2", "magenta_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 3", "light_blue_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 4", "yellow_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 5", "lime_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 6", "pink_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 7", "gray_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 8", "light_gray_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 9", "cyan_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 10", "purple_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 11", "blue_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 12", "brown_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 13", "green_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 14", "red_stained_glass");
			newcmd = newcmd.replaceAll("stained_glass 15", "black_stained_glass");
			
			newcmd = newcmd.replaceAll("double_stone_slab", "smooth_stone_slab[type=double]");
			
			newcmd = newcmd.replaceAll("stained_hardened_clay 0", "white_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 1", "orange_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 2", "magenta_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 3", "light_blue_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 4", "yellow_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 5", "lime_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 6", "pink_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 7", "gray_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 8", "light_gray_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 9", "cyan_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 10", "purple_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 11", "blue_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 12", "brown_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 13", "green_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 14", "red_terracotta");
			newcmd = newcmd.replaceAll("stained_hardened_clay 15", "black_terracotta");
			
			newcmd = newcmd.replaceAll("dirt 0 replace", "dirt replace");
			
			newcmd = newcmd.replaceAll("fence", "oak_fence");
			newcmd = newcmd.replaceAll("Air", "air");
			newcmd = newcmd.replaceAll("wool 0", "white_wool");
			newcmd = newcmd.replaceAll("wool 1", "orange_wool");
			newcmd = newcmd.replaceAll("wool 2", "magenta_wool");
			newcmd = newcmd.replaceAll("wool 3", "light_blue_wool");
			newcmd = newcmd.replaceAll("wool 4", "yellow_wool");
			newcmd = newcmd.replaceAll("wool 5", "lime_wool");
			newcmd = newcmd.replaceAll("wool 6", "pink_wool");
			newcmd = newcmd.replaceAll("wool 7", "gray_wool");
			newcmd = newcmd.replaceAll("wool 8", "light_gray_wool");
			newcmd = newcmd.replaceAll("wool 9", "cyan_wool");
			newcmd = newcmd.replaceAll("wool 10", "purple_wool");
			newcmd = newcmd.replaceAll("wool 11", "blue_wool");
			newcmd = newcmd.replaceAll("wool 12", "brown_wool");
			newcmd = newcmd.replaceAll("wool 13", "green_wool");
			newcmd = newcmd.replaceAll("wool 14", "red_wool");
			newcmd = newcmd.replaceAll("wool 15", "black_wool");
			newcmd = newcmd.replaceAll("c=", "limit=");
			
			//(De manière générale, le data d'un cube de couleur sera toujours la même couleur, donc 
				//	wool 10 = purple_wool
				//	carpet 10 = purple_carpet
				//  stained_glass 10 = purple_stained_glass
				//	stained_glass_pane 10 = purple_stained_glass_pane
				//	stained_hardened_clay 10 = purple_terracotta
		}
		
		if(oldcmd.contains("playsound") || oldcmd.contains("particle")){
			newcmd = newcmd.replaceAll("endRod", "end_rod");
			newcmd = newcmd.replaceAll("magicCrit ", "enchanted_hit");
			newcmd = newcmd.replaceAll("endRod", "end_rod");
			newcmd = newcmd.replaceAll("witchMagic", "witch");
			newcmd = newcmd.replaceAll("happyVillager", "happy_villager");
			newcmd = newcmd.replaceAll("dripLava", "dripping_lava");
			newcmd = newcmd.replaceAll("dripWater", "dripping_water");
			newcmd = newcmd.replaceAll("dragonbreath", "dragon_breath");
			newcmd = newcmd.replaceAll("enchantmenttable", "enchant");
			newcmd = newcmd.replaceAll("instantSpell", "instant_effect");
			
			
			///particle blockcrack <plein de chiffres etc à pas changer> 155 -> /particle block quartz_block <plein de chiffres etc à pas changer>
			if(newcmd.matches("/{0,1}particle blockcrack .+ 155")){
				
				newcmd = newcmd.replaceAll("blockcrack", "block quartz_block");
				newcmd = newcmd.substring(0, newcmd.length()-4); // suppression du 155 et du dernier espace
			}
			
			
			///particle blockcrack <truc> 101 -> /particle block iron_bars <truc>
			if(newcmd.matches("/{0,1}particle blockcrack .+ 101")){
				
				newcmd = newcmd.replaceAll("blockcrack", "block iron_bars");
				newcmd = newcmd.substring(0, newcmd.length()-4); // suppression du 101 et du dernier espace
			}
			
			//Dès que tu vois un /playsound ou un /particle avec dedans un @e, tu changes ça en @a
			if(newcmd.matches("/{0,1}(particle|playsound) .+ @e.*")){
				
				newcmd = newcmd.replaceAll("@e", "@a");
			}
			
			
			
		}
		
		if(oldcmd.contains("give") || oldcmd.contains("clear")){
			if(newcmd.contains("give @p iron_ingot 1 0 {skin")) {
				
				newcmd = newcmd.replaceAll("iron_ingot 1 0 ", "minecraft:iron_ingot");
				newcmd = new String(newcmd + " 1");
			}
		}
		
		System.out.println(newcmd);
		
		// Changement du Name:name en Name:{text:"name"}
		if(newcmd.contains("display:" + '{' + "Name:\"")){
			String parts[] = newcmd.split("Name:\"");
			String parts2[] = parts[1].split("\""); // parts2[0] = NOM
			newcmd = newcmd.replaceAll("Name" + ':' + "\"", "Name:\'{\"text\":\"");
			newcmd = newcmd.replaceAll(parts2[0] + "\"", parts2[0] + "\"" + '}' + "'");
			newcmd = newcmd.replaceAll(" 1 0", "");
		}
		
		

		// Changement de Lore ["","",""] en Lore [text:"", text:""]...
		if(newcmd.contains("Lore:" + '[')){ 
			String parts[] = newcmd.split("Lore:" + "\\[");
			String parts2[] = parts[1].split("\\]");
			String parts3[] = parts2[0].split("\\,"); // contient chaque phrase de lore quotes comprises
			
			for(int i = 0; i < parts3.length; i++) { // pour chaque phrase on rajoute ce qu'il manque
				newcmd = newcmd.replaceAll(parts3[i], "'" + '{' + "\"text\":" + parts3[i] + '}' + "'");
			}
			
		}
		
		newcmd = newcmd.replaceAll("Villager", "villager");
		newcmd = newcmd.replaceAll("testforblock", "execute if block");
		newcmd = newcmd.replaceAll("testforblocks", "execute if blocks");
		newcmd = newcmd.replaceAll("testfor @e", "execute if entity @e");
		
		
		//   /clear @p *id* 0 *n* {NBT}
		//-> /clear @p *id*{NBT} *n*
		
		// vérifier si marche
		if(newcmd.matches("/{0,1}clear @p .+ 0 .+ .+")){
			
			String parts[] = newcmd.split(" ");
			String partsNbt[] = newcmd.split("{");
			
			newcmd = new String(parts[0] + " " + parts[1] + " " + parts[2]); // clear @p id
			
			
			// Reconstitution du NBT
			newcmd = new String(newcmd + "{" + partsNbt[1] + " " + parts[4]);
			
		}
		
		
		

//Crilian mercredi dernier à 16:12
//Dans les sélecteurs :
//avant on avait : 
//@e[score_<ObjectiveName>_min=<Min>,score_<ObjectiveName>=<Max>]
//Maintenant on a :
//@e[scores={<ObjectiveName>=<Min>..<Max>}]
 
// gros, par exemple, si on veut que ça sélectionne l'entité quand invocTime est entre 250 et 500 :
//AVANT :
//@e[score_invocTime _min=250,score_invocTime =500]
//MAINTENANT :
//@e[scores={invocTime=250..500}]
//Normalement ça règlera tous les soucis dans les donjons utilisant ça

// >>> à faire

		newcmd = newcmd.replaceAll("small_Fireball", "small_fireball");
		newcmd = newcmd.replaceAll("fireworksSpark", "firework");
		newcmd = newcmd.replaceAll("fireworks_rocket", "firework_rocket");

		//Dès que tu vois un /effect @e[bidule] effect time power, tu rejoutes un "give", pour faire /effect give @e[bidule] effect time power
		//SAUF si le temps = 0, alors tu replaces par le tout par /effect clear @e[bidule] effect
		
		newcmd = newcmd.replaceAll("evocation_fangs", "evoker_fangs");
		newcmd = newcmd.replaceAll("vindication_illager", "vindicator");
		newcmd = newcmd.replaceAll("Zombie", "zombie");
		newcmd = newcmd.replaceAll("Skeleton", "skeleton");
		newcmd = newcmd.replaceAll("endermen", "enderman");
		newcmd = newcmd.replaceAll("irongolem", "iron_golem");
		
		//Si  tu vois un truc : scoreboard players test @a[r=400] donjonfm 1 3 (avec un TEST dedans), 
		// tu dois tout transformer en /execute if entity @a[r=400,scores={donjonfm=1..3}] 
		// (Le sélecteur reste identique, avec un argument scores= en plus, avec le min et max)
		
		//newcmd = newcmd.replaceAll("scoreboard players test @a", "execute if entity @a");
		
		
		
		
		if(!oldcmd.equals(newcmd)) {
			cmdblock.setCommand(newcmd);
			cmdblock.update();
			Location loc = player.getLocation();
			
			String alertCmdColor = ChatColor.GOLD + "Cmd block modifié de : " + ChatColor.WHITE + oldcmd + ChatColor.GOLD + " à : " + ChatColor.WHITE + newcmd;
			String alertCmd = "Cmd block modifié de : " + oldcmd + " à : " + newcmd;
			String alertLoc = "Location : x=" + loc.getX() + ", y=" + loc.getY() + ", z=" + loc.getZ() + ", w=" + loc.getWorld().getName();
			
			player.sendMessage(alertCmdColor);
			player.sendMessage(alertLoc);
			
			Plugin.LOG.info(alertCmd);
			Plugin.LOG.info(alertLoc);
			
			return true;
		} else {
			return false;
		}
		
	}

}
