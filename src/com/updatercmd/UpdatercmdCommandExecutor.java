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
		
		 if(oldcmd.equals("") || oldcmd.equals(" "))
			 	return false;
		
		String newcmd = oldcmd;
		
		newcmd = updateClearGive(newcmd);
        newcmd = updateEffect(newcmd);
        newcmd = updateScoreboardPlayerTest(newcmd);
        newcmd = updateSelectorRadius(newcmd);
        newcmd = updateSelectorScore(newcmd);
        newcmd = updateNameLore(newcmd);
        newcmd = updateExecute(newcmd);
		
		newcmd = newcmd.replaceAll("r=","distance=..");
		newcmd = newcmd.replaceAll("type=Player","type=player");
		newcmd = newcmd.replaceAll("c=", "limit=");
		
		
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
	        newcmd = newcmd.replaceAll("fence", "oak_fence");
	            
	        newcmd = newcmd.replaceAll("_button 0", "_button[face=ceiling]");
	        newcmd = newcmd.replaceAll("_button 1", "_button[facing=east]");
	        newcmd = newcmd.replaceAll("_button 2", "_button[facing=west]");
	        newcmd = newcmd.replaceAll("_button 3", "_button[facing=south]");
	        newcmd = newcmd.replaceAll("_button 4", "_button[face=floor]");
			
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

			newcmd = newcmd.replaceAll("Player","player");
	        newcmd = newcmd.replaceAll("Villager", "villager");
	        newcmd = newcmd.replaceAll("Zombie", "zombie");
	        newcmd = newcmd.replaceAll("Skeleton", "skeleton");
	        newcmd = newcmd.replaceAll("Endermen", "enderman");
	        newcmd = newcmd.replaceAll("small_Fireball", "small_fireball");
	        newcmd = newcmd.replaceAll("fireworksSpark", "firework");
	        newcmd = newcmd.replaceAll("fireworks_rocket", "firework_rocket");
	        newcmd = newcmd.replaceAll("evocation_fangs", "evoker_fangs");
	        newcmd = newcmd.replaceAll("vindication_illager", "vindicator");
	        newcmd = newcmd.replaceAll("irongolem", "iron_golem");
	        newcmd = newcmd.replaceAll("endRod", "end_rod");
	        newcmd = newcmd.replaceAll("magicCrit ", "enchanted_hit");
	        newcmd = newcmd.replaceAll("witchMagic", "witch");
	        newcmd = newcmd.replaceAll("happyVillager", "happy_villager");
	        newcmd = newcmd.replaceAll("dripLava", "dripping_lava");
	        newcmd = newcmd.replaceAll("dripWater", "dripping_water");
	        newcmd = newcmd.replaceAll("dragonbreath", "dragon_breath");
	        newcmd = newcmd.replaceAll("enchantmenttable", "enchant");
	        newcmd = newcmd.replaceAll("instantSpell", "instant_effect");
	        newcmd = newcmd.replaceAll("note", "note_block");
	        newcmd = newcmd.replaceAll("enderdragon", "ender_dragon");
	        newcmd = newcmd.replaceAll("evocation_illager", "evoker");
			
			newcmd = newcmd.replaceAll("minecraft:stone 0", "minecraft:stone");
			newcmd = newcmd.replaceAll("minecraft:stone 1", "minecraft:granite");
			newcmd = newcmd.replaceAll("minecraft:stone 2", "minecraft:polished_granite");
			newcmd = newcmd.replaceAll("minecraft:stone 3", "minecraft:diorite");
			newcmd = newcmd.replaceAll("minecraft:stone 4", "minecraft:polished_diorite");
			newcmd = newcmd.replaceAll("minecraft:stone 5", "minecraft:andesite");
			newcmd = newcmd.replaceAll("minecraft:stone 6", "minecraft:polished_andesite");
			newcmd = newcmd.replaceAll("minecraft:air 0 replace", "minecraft:air replace");
			
			newcmd = newcmd.replaceAll("stone 0", "stone");
	        newcmd = newcmd.replaceAll("stone 1", "granite");
	        newcmd = newcmd.replaceAll("stone 2", "polished_granite");
	        newcmd = newcmd.replaceAll("stone 3", "diorite");
	        newcmd = newcmd.replaceAll("stone 4", "polished_diorite");
	        newcmd = newcmd.replaceAll("stone 5", "andesite");
	        newcmd = newcmd.replaceAll("stone 6", "polished_andesite");

	        newcmd = newcmd.replaceAll("air 0", "air");
	        newcmd = newcmd.replaceAll("Air", "air");
			
			newcmd = newcmd.replaceAll("stonebrick", "stone_bricks");	
			newcmd = newcmd.replaceAll("double_stone_slab", "smooth_stone_slab[type=double]");
	        newcmd = newcmd.replaceAll("dirt 0", "dirt");
			
	        newcmd = newcmd.replaceAll("stained_glass 10", "purple_stained_glass");
	        newcmd = newcmd.replaceAll("stained_glass 11", "blue_stained_glass");
	        newcmd = newcmd.replaceAll("stained_glass 12", "brown_stained_glass");
	        newcmd = newcmd.replaceAll("stained_glass 13", "green_stained_glass");
	        newcmd = newcmd.replaceAll("stained_glass 14", "red_stained_glass");
	        newcmd = newcmd.replaceAll("stained_glass 15", "black_stained_glass");
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
			
			newcmd = newcmd.replaceAll("double_stone_slab", "smooth_stone_slab[type=double]");
			
			newcmd = newcmd.replaceAll("stained_hardened_clay 10", "purple_terracotta");
	        newcmd = newcmd.replaceAll("stained_hardened_clay 11", "blue_terracotta");
	        newcmd = newcmd.replaceAll("stained_hardened_clay 12", "brown_terracotta");
	        newcmd = newcmd.replaceAll("stained_hardened_clay 13", "green_terracotta");
	        newcmd = newcmd.replaceAll("stained_hardened_clay 14", "red_terracotta");
	        newcmd = newcmd.replaceAll("stained_hardened_clay 15", "black_terracotta");
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
			
			newcmd = newcmd.replaceAll("dirt 0 replace", "dirt replace");
			
			newcmd = newcmd.replaceAll("wool 10", "purple_wool");
	        newcmd = newcmd.replaceAll("wool 11", "blue_wool");
	        newcmd = newcmd.replaceAll("wool 12", "brown_wool");
	        newcmd = newcmd.replaceAll("wool 13", "green_wool");
	        newcmd = newcmd.replaceAll("wool 14", "red_wool");
	        newcmd = newcmd.replaceAll("wool 15", "black_wool");
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
			Location loc = cmdblock.getLocation();
			
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
	
	public static String updateEffect(String str) {
        /*  Isole le /effect du reste de la commande (si /execute par ex)
        *  effect @a[SELECTOR] id sec ampl [particle]
        *  ->
        *  effect give/clear @a[SELECTOR] id sec ampl [particle] */
        if(!str.contains("effect ") ||
            (str.contains("effect give") ||
             str.contains("effect clear")))
            return str;
        String[] parts = str.split("/");
        String res = "";
        for(int i = 0; i < parts.length; i++) {
            res += updateEffect2(parts[i]);
            if(i != parts.length-1)
                res += "/";
        }
        return res;
    }

    public static String updateEffect2(String str) {
        /* Gère un /effect isolé. Utilisé dans UpdateEffect() */
        if(!str.contains("effect "))
            return str;
        String[] parts = str.split(" ");
        String[] res = {"", "", "", "", "", ""};
        int i = 0;
        for(String s : parts) {
            res[i++] = s;
        }
        String new_str = res[0];
        if(res[3].equals("0"))
            new_str += " clear " + res[1] + " " + res[2];
        else {
            new_str += " give " + res[1] + " " + res[2] + " " + res[3];
            if(!res[4].equals("")) {
                new_str += " " + res[4];
                if(!res[5].equals(""))
                    new_str += " " + res[5];
            }
        }
        return new_str;

    }

    public static String updateExecute(String str) {
    	  /** Isole les execute d'une commande pour la maj
         * execute @e[SELETOR] ~ ~ ~ /[command]
         * ->
         * execute at @e[SELETOR] as @e[SELETOR] positioned ~ ~ ~ run [command]
         * Si possible, faire cette commande en dernier par rapport aux autres updates */
         if(!str.contains("execute"))
             return str;
         if((str.substring(0, str.indexOf("@")).contains("at")) ||
            (str.substring(0, str.indexOf("@")).contains("as")) ||
            (str.contains("execute if ")))
             return str;

         if(str.charAt(0) == '/')
             str = str.substring(1, str.length());
         String[] parts = str.split("/");
         String res = "";
         for(int i = 0; i < parts.length; i++) {
             res += updateExecute2(parts[i]);
             
         }
         return res;
    }

    public static String updateExecute2(String str) {
        /* Gère un execute séparé du reste de la commande
        *  Utilisé dans updateExecute() */
        if(!str.contains("execute"))
            return str;
        String[] parts = str.split(" ");
        String res = parts[0] + " at " + parts[1]
                              + " as " + parts[1]
                              + " positioned " + parts[2]
                              + " " + parts[3]
                              + " " + parts[4]
                              + " run ";
        return res;
    }

    public static String updateClearGive(String str) {
        /** give @a[selector] minecraft:name count data {NBT}
        *   ->
        *   give @a[selector] minecraft:name{NBT} count
        *
        * - Attention, si le data est différent de 0
        *     il faut le mettre à jour APRES cette commande.
        * - Il pourrait y avoir un uniquement :
        *      give @a[selector] name
        *     qui ne donne qu'un item.
        * - Peut générer un souc avec des datablock comme :
        *     /give @p wool 1 1
        * - DOIT être lancé AVANT updateExecute() */
        if(!(str.contains("give") || str.contains("clear")))
            return str;

        int i = str.indexOf("give ");
        if(str.substring(i, str.indexOf("}", i)).split(" ")[2].contains("{"))
            return str;
        
        String[] parts = str.split("/");
        String res = "";
        for(i = 0; i < parts.length; i++) {
            res += updateClearGive2(parts[i]);
            if(i != parts.length-1)
                res += "/";

        }
        return res;
    }

    public static String updateClearGive2(String str) {
        /** utilisé pour updateClearGive()
        * Version simplifiée sans de /execute */
        if(!(str.contains("give") || str.contains("clear")))
            return str;

        // check si ça a déjà été mis à jour ET gère le cas give @a[selector] minecraft:name
        String[] parts = str.split(" ", 3);
        if(!parts[2].contains(" "))
            return str;
        parts[2] += " ";
        if(parts[2].indexOf("{") < parts[2].indexOf(" ")) {
            String res = parts[0] + " " + parts[1] + " ";
            String[] parts2 = parts[2].split(" ");
            if(parts2.length == 2)
                return str;
            else if(parts2[2].equals("0")){
                res += parts2[0] + " " + parts2[1];
            }
            return res;
        }

        // début de la MAJ
        parts = str.split(" ", 6);
    
        String res = parts[0] + " " + parts[1] + " " + parts[2];

        if(!parts[4].equals("0")) {
            res += " " + parts[4];
        }
        return res + parts[5] + " " + parts[3];
    }

    public static String updateSelectorRadius(String str) {
        /** Selecteur : agence correctement les r=max et rm=min
        *    -> distance=min..max
        *   Aucune condition */
        if(!str.contains("rm=") &&
           !str.contains("r="))
            return str;
        String[] parts = str.split("@");
        String res = "";
        for(String s : parts) {
            res += updateSelectorRadius2("@" + s);
        }
        return res.substring(1, res.length());
    }

    public static String updateSelectorRadius2(String str) {
        /** Selecteur : agence correctement les r=max et rm=min
        *    -> distance=min..max
        *   Aucune condition */
        if(!str.contains("rm=") &&
           !str.contains("r="))
            return str;

        int j = str.indexOf("]") + 1;

        String old_selector = str.substring(0, j-1) + ",]";
        // virgule à la fin pour éviter les sous-cas car la fin est ]

        int i_rm_start = old_selector.indexOf("rm=");
        String rm = "";
        String r = "";
        int i_rm_end=-2; int i_r_start=-2; int i_r_end=-2;

        if(i_rm_start < 0) {
            // rm pas présent
            i_r_start = old_selector.indexOf("r=");
            if(i_r_start < 0) {
                // rm et r pas présent
                return str;
            } else {
                // rm pas présent et r= présent
                i_r_end = old_selector.indexOf(",", i_r_start+1);
                i_rm_start = i_r_start;
                i_rm_end = i_r_end;
                r = old_selector.substring(i_r_start+2, i_r_end);
            }
        } else {
            // rm présent
            i_rm_end = old_selector.indexOf(",", i_rm_start+1);
            rm = old_selector.substring(i_rm_start+3, i_rm_end);

            i_r_start = old_selector.indexOf("r=");
            if(i_r_start > 0) {
                // rm= et r= présents
                i_r_end = old_selector.indexOf(",", i_r_start+1);
                r = old_selector.substring(i_r_start+2, i_r_end);
            } else {
                // rm= présent et r= pas présent
                i_r_start = i_rm_start;
                i_r_end = i_rm_end;
            }
        }
        int first_occur = Math.min(i_rm_start, i_r_start);
        int first_occur_end = Math.min(i_rm_end, i_r_end);
        int second_occur = Math.max(i_rm_start, i_r_start);
        int second_occur_end = Math.max(i_rm_end, i_r_end);

        String new_selector = old_selector.substring(0, first_occur);

        if(!(r.equals("") || rm.equals("")))
            new_selector += old_selector.substring(first_occur_end, second_occur);

        new_selector += old_selector.substring(second_occur_end, old_selector.length()-1)
                     + "distance=" + rm + ".." + r + "]";

        new_selector = selectorComma(new_selector);

        String new_str = new_selector + str.substring(j, str.length());

        return new_str;
    }

    public static String selectorComma(String str) {
        /* retire l'excès de virgules (dans un sélecteur) */
        String new_str = "";
        for(int i = 0; i < str.length(); i++) {
            new_str += str.charAt(i);
            while((str.charAt(i) == ',' && str.charAt(i+1) == ',') ||
                  (str.charAt(i) == '[' && str.charAt(i+1) == ',') ||
                  (str.charAt(i) == ',' && str.charAt(i+1) == ']'))
                i++;
        }
        return new_str;
    }

    public static String updateScoreboardPlayerTest(String str) {
        /** scoreboard players test @e[selecteur] tag 8 9 
        *   ->
        *   execute if entity @e[selecteur,scores={tag=8..9}]
        * NB: le dernier paramètre est optionnel.
        * Souci s'il y a un espace dans le sélecteur. */
        if(!str.contains("scoreboard players test "))
            return str;
        String[] parts = str.split("/");
        String res = "";
        for(int i = 0; i < parts.length; i++) {
            res += updateScoreboardPlayerTest2(parts[i]);
            if(i != parts.length-1)
                res += "/";
        }
        return res;
    }

    public static String updateScoreboardPlayerTest2(String str) {
        if(!str.contains("scoreboard players test "))
            return str;
        String[] tab = str.split(" ");
        String new_command = "execute if entity "
                            + tab[3].substring(0, tab[3].length()-1)
                            + ",scores={" + tab[4] + "=" + tab[5] + "..";
        if(tab.length == 7)
            new_command += tab[6] + "}]";
        else
            new_command += "}]";

        return new_command;
    }

    public static String updateSelectorScore(String str) {
        /** Remplace toutes les occurences de la forme
        *   score_tag_min=30,score_tag=50
        * en
        *   scores={tag=30..50}
        *  CONDITION :
        *    Il faut D'ABORD donner le
        *    score_tag_min= PUIS le score_tag= */
        if(!str.contains("score_"))
            return str;
        String[] parts = str.split("@");
        String res = "";
        for(String s : parts) {
            res += updateSelectorScore2("@" + s);
        }
        return res.substring(1, res.length());
    }

    public static String updateSelectorScore2(String str) {
        /** Gère les scores du sélecteur isolé.
        * Utilisé dans updateSelectorScore() */
        if(!str.contains("score_"))
            return str;

        int j = str.indexOf("]") + 1;
        String old_selector = str.substring(0, j-1) + ",]";
        // virgule à la fin pour retirer le cas où ça termine par ] (éviter les sous-cas)

        int i_scorename_start = old_selector.indexOf("score_") + 6;
        if(i_scorename_start-6 < 0) // si pas d'élément score dedans
            return str;

        int i_scoremin_start = old_selector.indexOf("=", i_scorename_start+1) +1;
        int i_scoremin_end = old_selector.indexOf(",", i_scorename_start+1);
        // /!\ Si min n'existe pas, ça encadre la valeur de max

        int is_min;
        int i_scorename_end;
        if((is_min=old_selector.indexOf("_min=", i_scorename_start)) > 0) {
            i_scorename_end = is_min;
        } else {
            i_scorename_end = old_selector.indexOf("=", i_scorename_start);
        }

        String scorename = old_selector.substring(i_scorename_start, i_scorename_end);
        String min = old_selector.substring(i_scoremin_start, i_scoremin_end);
        // min = max s'il n'existe pas de min dans la commande.

        String max;
        int i_scoremax_end = -1; // sinon danger avec max du new_selector
        if(is_min < 0) {
            // Il n'y a pas de min
            max = min;
            min = "";
        } else {
            // il y a un min
            //min = min;
            if(old_selector.indexOf(scorename, i_scoremin_end) < 0) {
                // il n'y a pas de max
                max = "";
            } else {
                // il y a un max
                int i_scoremax_start = old_selector.indexOf("=", i_scoremin_end+1) +1;
                i_scoremax_end = old_selector.indexOf(",", i_scoremin_end+1);
                max = old_selector.substring(i_scoremax_start, i_scoremax_end);
            }
        }

        String new_selector = old_selector.substring(0, i_scorename_start-6)
                             + "scores={" + scorename + "=" + min+".."+max + "}"
                             + old_selector.substring(
                                old_selector.indexOf(",", Math.max(i_scoremin_end, i_scoremax_end)),
                                old_selector.length()-2)
                             + "]"
                             + str.substring(j, str.length());

        return new_selector;
    }

    public static String updateNameLore(String str) {
        /**  remplace toutes les occurences dans str de :
        *    display:{Name:"oui",Lore:["non1","non2"]}
        *  en
        *    display:{Name:'{"text":"oui"}',Lore:['{"text":"non1"}','{"text":"non2"}']}
        *
        *  SINON retourne la chaine à l'identique.
        *
        *  Condition :
        *   1. D'abord le name PUIS le lore, dans la commande
        *      C'est accepté par Minecraft mais ça survient dans peu de cas. */
        int i=str.indexOf("display:{");
        if(i < 0 || str.contains("\"text\":\""))
            return str;
        int j = str.indexOf("}", i) + 1;

        String old_seq = str.substring(i, j);

        String new_seq = "display:{Name:'{\"text\":"
                        + old_seq.substring(14, old_seq.indexOf("\"", 15))
                        + "\"}',Lore:['{\"text\":\"";

        String[] parts = old_seq.substring(old_seq.indexOf("\"", 15)+1).split("\"");

        for(int k = 1; k < parts.length; k++) {
            if(parts[k].equals(","))
                new_seq += "\"}','{\"text\":\"";
            else if(parts[k].equals("]}"))
                new_seq += "\"}']}";
            else
                new_seq += parts[k];
        }
        String new_str = str.substring(0, i)
                        + new_seq
                        + updateNameLore(str.substring(j, str.length()));

        return new_str;
    }

}
