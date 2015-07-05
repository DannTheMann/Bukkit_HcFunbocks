package aog.minigame.funbocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import aog.minigame.funbocks.events.FBWinnerEvents;
import aog.minigame.funbocks.instance.FBBoss;
import aog.minigame.funbocks.instance.FBGame;
import aog.minigame.funbocks.instance.FBHost;
import aog.minigame.funbocks.instance.FBMap;
import aog.minigame.funbocks.instance.FBPlayer;

public class Core {

	public static FBMap getMap(String mapName) {
		return Main.data.getMaps().get(mapName);
	}

	public static int generateRandomNumber(int x) {
		return new Random().nextInt(x)+1;
	}

	public static ItemStack getHostItem(int round) {
		
		switch(round){
		
		case 1: 
			return Monsters.getZombie();
		case 2: return Monsters.getSkeleton();
		case 3: return Monsters.getSpider();
		case 4: return Monsters.getCaveSpider();
		case 5: return Monsters.getBlaze();
		case 6: return Monsters.getWitch();
		case 7: return Monsters.getGiant();
		case 8: return Monsters.getMagmaCube();
		
		}
		return null;
	}

	public static ItemStack nameItemStack(ItemStack is, String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack nameItemStack(ItemStack is, char a) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.RESET + "" + a);
		is.setItemMeta(im);
		return is;		
	}

	public static ItemStack loreItemStack(ItemStack is, String line) {
		List<String> list = null;
		if(is.getItemMeta().getLore() == null)
			list = new ArrayList<String>();
		else
			list = is.getItemMeta().getLore();
		
		line = ChatColor.stripColor(line);
		
		if(line != null){
			
			for(int i = 0; i < line.split("\n").length; i++){
				
				list.add(ChatColor.RESET + "" + ChatColor.GRAY + line.split("\n")[i]);
				
			}

		}
		
		ItemMeta im = is.getItemMeta();
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	public static void loadItems() {
		
		List<ItemStack> item = FBGame.item;
		
		item.add(new ItemStack(Material.GOLDEN_APPLE));
		item.add(new ItemStack(Material.ARROW, 32));
		item.add(new ItemStack(Material.BREAD, 4));
		item.add(new ItemStack(Material.IRON_SWORD));
		item.add(new ItemStack(Material.GOLD_SWORD));
		item.add(new ItemStack(Material.DIAMOND_SWORD));
		item.add(new ItemStack(Material.COOKED_CHICKEN, 4));
		item.add(new ItemStack(Material.COOKED_BEEF, 4));
		item.add(new ItemStack(Material.GRILLED_PORK, 4));
		item.add(new ItemStack(Material.BOW));
		item.add(new ItemStack(Material.COOKIE, 12));
		//
		ItemStack RegenPotion = new ItemStack(Material.POTION);
		RegenPotion.setDurability((short)16369);
		item.add(RegenPotion);
		ItemStack InstantHealth = new ItemStack(Material.POTION);
		InstantHealth.setDurability((short)16373);
		item.add(InstantHealth);
		ItemStack Speed = new ItemStack(Material.POTION);
		Speed.setDurability((short)16338);
		item.add(Speed);
		ItemStack FireResis = new ItemStack(Material.POTION);
		FireResis.setDurability((short)16371);
		item.add(FireResis);
		ItemStack OPApple = new ItemStack(Material.APPLE);
		OPApple.setDurability((short)1);
		item.add(OPApple);
		//
		ItemStack BookProtectionI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack BookProtectionII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		ItemStack BookProtectionIII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemStack BookProtectionIV = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		ItemStack BookSharpnessI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_ALL, 1);
		ItemStack BookSharpnessII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_ALL, 2);
		ItemStack BookSharpnessIII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_ALL, 3);
		ItemStack BookSharpnessIV = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_ALL, 4);
		ItemStack BookSharpnessV = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_ALL, 5);
		ItemStack BookFireAspectI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.FIRE_ASPECT, 1);
		ItemStack BookFireAspectII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.FIRE_ASPECT, 2);
		ItemStack BookPunchI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.ARROW_KNOCKBACK, 1);
		ItemStack BookPunchII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.ARROW_KNOCKBACK, 2);
		ItemStack BookPunchIII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.ARROW_KNOCKBACK, 3);
		ItemStack BookInfinityI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.ARROW_INFINITE, 1);
		ItemStack BookFlameI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.ARROW_FIRE, 1);
		ItemStack BookUnbreakingI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DURABILITY, 1);
		ItemStack BookUnbreakingII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DURABILITY, 2);
		ItemStack BookUnbreakingIII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DURABILITY, 3);
		ItemStack BookSmiteI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_UNDEAD, 1);
		ItemStack BookSmiteII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_UNDEAD, 2);
		ItemStack BookSmiteIII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_UNDEAD, 3);
		ItemStack BookSmiteIV = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_UNDEAD, 4);
		ItemStack BookSmiteV = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_UNDEAD, 5);
		ItemStack BookBOAI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_ARTHROPODS, 1);
		ItemStack BookBOAII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_ARTHROPODS, 2);
		ItemStack BookBOAIII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_ARTHROPODS, 3);
		ItemStack BookBOAIV = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_ARTHROPODS, 4);
		ItemStack BookBOAV = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.DAMAGE_ARTHROPODS, 5);
		ItemStack BookKnockbackI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.KNOCKBACK, 1);
		ItemStack BookKnockbackII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.KNOCKBACK, 2);
		ItemStack BookLootingI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.LOOT_BONUS_MOBS, 1);
		ItemStack BookLootingII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.LOOT_BONUS_MOBS, 2);
		ItemStack BookLootingIII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.LOOT_BONUS_MOBS, 3);
		ItemStack BookThornsI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.THORNS, 1);
		ItemStack BookThornsII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.THORNS, 2);
		ItemStack BookThornsIII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.THORNS, 3);
		ItemStack BookPowerI = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.ARROW_DAMAGE, 1);
		ItemStack BookPowerII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.ARROW_DAMAGE, 2);
		ItemStack BookPowerIII = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.ARROW_DAMAGE, 3);
		ItemStack BookPowerIV = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.ARROW_DAMAGE, 4);
		ItemStack BookPowerV = setEnchantment(new ItemStack(Material.ENCHANTED_BOOK), Enchantment.ARROW_DAMAGE, 5);
		item.add(BookPowerV);
		item.add(BookPowerIV);
		item.add(BookPowerIII);
		item.add(BookPowerII);
		item.add(BookPowerI);
		item.add(BookSmiteV);
		item.add(BookSmiteIV);
		item.add(BookSmiteIII);
		item.add(BookSmiteII);
		item.add(BookSmiteI);
		item.add(BookUnbreakingIII);
		item.add(BookUnbreakingII);
		item.add(BookUnbreakingI);
		item.add(BookFlameI);
		item.add(BookFireAspectI);
		item.add(BookFireAspectII);
		item.add(BookSharpnessV);
		item.add(BookSharpnessIV);
		item.add(BookSharpnessIII);
		item.add(BookSharpnessII);
		item.add(BookSharpnessI);
		item.add(BookPunchI);
		item.add(BookPunchII);
		item.add(BookPunchIII);
		item.add(BookProtectionIV);
		item.add(BookProtectionIII);
		item.add(BookProtectionII);
		item.add(BookProtectionI);
		item.add(BookInfinityI);
		item.add(BookBOAI);
		item.add(BookBOAII);
		item.add(BookBOAIII);
		item.add(BookBOAIV);
		item.add(BookBOAV);
		item.add(BookKnockbackI);
		item.add(BookKnockbackII);
		item.add(BookLootingI);
		item.add(BookLootingII);
		item.add(BookLootingIII);
		item.add(BookThornsI);
		item.add(BookThornsII);
		item.add(BookThornsIII);




		
	}

	public static ItemStack setLore(ItemStack item, String lore) {
		ItemMeta im = item.getItemMeta();
				if(im.getLore() == null){
				List<String> l = new ArrayList<String>();
				l.add(lore);
				im.setLore(l);
				}else{
				im.getLore().add(lore);
				}
				item.setItemMeta(im);		
		return item;
		
	}
	
	public static ItemStack setEnchantment(ItemStack item, Enchantment enchant, int level){
		EnchantmentStorageMeta ebm = (EnchantmentStorageMeta) item.getItemMeta();
		ebm.addStoredEnchant(enchant, level, true);
		item.setItemMeta(ebm);
		return item;
		
	}

	@SuppressWarnings("serial")
	public static void loadBosses() {
		
		FBGame.bosses.add(new FBBoss(ChatColor.DARK_PURPLE + "Fran The Dominatrix", 1, EntityType.WITHER, false));
		FBGame.bosses.add(new FBBoss(ChatColor.RED + "Angered 12 Year Old", 50, EntityType.PIG_ZOMBIE, true));
		FBGame.bosses.add(new FBBoss(ChatColor.DARK_GRAY + "Aggressive Skeleton", 75, EntityType.SKELETON, SkeletonType.WITHER, true));
		FBGame.bosses.add(new FBBoss(ChatColor.BLUE + "Creepers!", 20, EntityType.CREEPER, true, false));
		FBGame.bosses.add(new FBBoss(ChatColor.BLUE + "Nasty Spiders!", 100, EntityType.SPIDER, new ArrayList<EntityType>(){{
			add(EntityType.CAVE_SPIDER);
		}}, false));
		
		
	}

	public static void showWinningMenu(FBPlayer fb) {
		
		FBWinnerEvents.winner = fb.getPlayer();
		
		Player p = fb.getPlayer();
		
		fb.restoreLife();
		
		Inventory inv = FBWinnerEvents.getWinningInventory(false);
		
		p.openInventory(inv);
		
	}
	

	public static void showWinningMenu(Player p) {
		FBWinnerEvents.winner = p;
		
		Inventory inv = FBWinnerEvents.getWinningInventory(true);
		
		p.openInventory(inv);
	}

	public static boolean playerWasInFunbocks(Player player) {
		return Main.currentGame != null && player != null && Main.currentGame.getPlayers().containsKey(player.getUniqueId().toString());
	}

	public static FBPlayer getFunbocksPlayer(Player player) {
		if(Main.currentGame != null && player != null && Main.currentGame.getPlayers().containsKey(player.getUniqueId().toString()))
			return Main.currentGame.getPlayers().get(player.getUniqueId().toString());
		else
			return null;
	}

	public static boolean noMoreEnemies() {
		return Main.currentGame != null && Main.currentGame.isStarted() && Main.currentGame.getLivingEntity().isEmpty();
	}

	public static boolean playerIsHost(Player p) {
		return Main.currentGame != null && Main.currentGame.getHost().getRawPlayer() == p;
	}

	public static int getCostPoints(int i) {
		switch(i){
		case 54:
			return 1;
		case 51:
			return 2;
		case 52:
			return 6;
		case 59:
			return 9;
		case 61:
			return 10;
		case 62:
			return 30;
		case 66:
			return 40;
		case 55:
			return 100;
			
		}
		return 0;
	}

	public static FBHost getFunbocksHost() {
		if(Main.currentGame != null && Main.currentGame.getHost() != null)
			return Main.currentGame.getHost();
		else
		return null;
	}

	public static boolean gameIsOn() {
		return Main.currentGame != null && Main.currentGame.isStarted();
	}

	public static ArrayList<ItemStack> getRandomRewards(
			ArrayList<ItemStack> list) {
		
		for(int i = 0; i < 3; i++)
			list.add(Core.nameItemStack(randomReward(Core.generateRandomNumber(9)+1), generateRandomName()));
		
		return list;
	}

	private static ItemStack randomReward(int x) {
		
		ItemStack is = new ItemStack(Material.AIR);
		
		
		
		switch(x){
		case 1:
			is.setType(Material.DIAMOND_AXE);
			is.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, Core.generateRandomNumber(5)+1);
			is.addUnsafeEnchantment(Enchantment.DURABILITY, Core.generateRandomNumber(9)+1);
			is.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, Core.generateRandomNumber(3)+1);
			is.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, Core.generateRandomNumber(9)+1);
			return is;
		case 2:
			is.setType(Material.DIAMOND_HOE);
			is.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, Core.generateRandomNumber(5)+3);
			is.addUnsafeEnchantment(Enchantment.DURABILITY, Core.generateRandomNumber(3)+1);
			is.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, Core.generateRandomNumber(4)+1);
			is.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, Core.generateRandomNumber(5)+1);
			return is;
		case 3:
			is.setType(Material.DIAMOND_CHESTPLATE);
			is.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Core.generateRandomNumber(5)+1);
			is.addUnsafeEnchantment(Enchantment.DURABILITY, Core.generateRandomNumber(3)+1);
			is.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, Core.generateRandomNumber(4)+1);
			is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, Core.generateRandomNumber(2)+1);
			return is;
		case 4:
			is.setType(Material.DIAMOND_SWORD);
			is.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, Core.generateRandomNumber(4)+1);
			is.addUnsafeEnchantment(Enchantment.DURABILITY, Core.generateRandomNumber(6)+1);
			is.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, Core.generateRandomNumber(9)+1);
			return is;
		case 5:
			is.setType(Material.DIAMOND_AXE);
			is.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, Core.generateRandomNumber(4)+1);
			is.addUnsafeEnchantment(Enchantment.DURABILITY, Core.generateRandomNumber(8)+1);
			is.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, Core.generateRandomNumber(4)+1);
			is.addUnsafeEnchantment(Enchantment.KNOCKBACK, Core.generateRandomNumber(9)+1);
			return is;
		case 6:
			is.setType(Material.BOW);
			is.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, Core.generateRandomNumber(4)+1);
			is.addUnsafeEnchantment(Enchantment.DURABILITY, Core.generateRandomNumber(9)+1);
			is.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, Core.generateRandomNumber(4)+1);
			is.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
			return is;
		case 7:
			is.setType(Material.DIAMOND_BOOTS);
			is.addUnsafeEnchantment(Enchantment.DURABILITY, Core.generateRandomNumber(5)+1);
			is.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Core.generateRandomNumber(9)+1);
			is.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, Core.generateRandomNumber(4)+1);
			return is;
		case 8:
			is.setType(Material.DIAMOND_PICKAXE);
			is.addUnsafeEnchantment(Enchantment.DURABILITY, Core.generateRandomNumber(5)+1);
			is.addUnsafeEnchantment(Enchantment.SILK_TOUCH, Core.generateRandomNumber(9)+1);
			is.addUnsafeEnchantment(Enchantment.DIG_SPEED, Core.generateRandomNumber(8)+1);
			return is;
		case 9:
			is.setType(Material.DIAMOND_CHESTPLATE);
			is.addUnsafeEnchantment(Enchantment.DURABILITY, Core.generateRandomNumber(6)+1);
			is.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Core.generateRandomNumber(8)+1);
			is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, Core.generateRandomNumber(5)+1);
			is.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, Core.generateRandomNumber(4)+1);
			return is;
		case 10:
			is.setType(Material.BOW);
			is.addUnsafeEnchantment(Enchantment.DURABILITY, Core.generateRandomNumber(6)+1);
			is.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, Core.generateRandomNumber(8)+1);
			is.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, Core.generateRandomNumber(7)+1);
			is.addUnsafeEnchantment(Enchantment.ARROW_FIRE, Core.generateRandomNumber(4)+1);
			return is;
		case 11:
			is.setType(Material.GOLDEN_APPLE);
			is.setDurability((short)1);
			is.setAmount(64);
			return is;
		}
		return null;
		
	}
	
	public static String generateRandomName() {
		
		String[] wordlist1 = {"Icy","Magical","Bloody","Darkened","Giant's","Rusty","Old","Ancient",
				"Blue","Black","White","Red","Purple","Golden","Silver","Aphotic","Orange","Yellow",
				"Green","Pink","White","Caliginous","Miniature","Dark","Holy","Sinful","Godly","Moldy",
				"Gloomy","Fairy","Ork","Goblin","Elf","Sunlit","Burnt","Glistening","Stunning","Demon",
				"Superior","Devil","Mother Nature's","Corruptive","Lovely","Meteor","Spiked","Gruesome",
				"Unholy","Diabolic","Vile","Sinister","Lucky","Angelic","Feline","Reptilian","Flaming",
				"Chilling","Tough","Rugged","Burning","Freezing","Possessed","Colorful","Florid","Gaudy",
				"Vibrant","Faded","Spirited","Dainty","Charming","Natural","Uncommon","Abnormal","Distinct",
				"Flaxen","Dusty","New","Wicked","Legendary","Alluring","Metalic","Fierce","Ferocious","Cruel",
				"Feral","Refined","Brutish","Bloodthirsty","Powerful","Moonlit","Glowing","Radiating","Celestial",
				"Crescent","Enlightened","Knight's","Ninja's","Saint's","Valkyrie's","Toy","Plastic","Wooden",
				"Witch's","Pegasus","Unicorn","Alicorn","Mermaid's","Ruby","Emerald","Opal","Sapphire","Garnet",
				"Amethyst","Peridot","Aquamarine","Diamond","Citrine","Pearl","Topaz","King's","Queen's","Prince's",
				"Princess's","Bishop's","Rook's","Turquoise","Mammoth","Mercury's","Aphrodite's","Mars's","Jupiter's",
				"Saturn's","Neptune's","Skeleton","Bone","Poison","Unknown","Battle","Mythril","Earth","Gale","Rune",
				"Regal","Zombie","Vampire","Alucard's","Dracula's","Orlok's","Pixie","Soul","Imp","Cross","Grim","Apollo's",
				"Spirit","Ivy","Nightmare","Dream","Requiem","Elk","Hunting","War","Modified","Custom","Cursed","Cheese-covered",
				"Hollow","Haunted","Chocolate-coated","Worn","Well used","Abandoned","Splintered","Duct-taped","Invisible","Clear"
				,"Mossy","Slimy","Ivory","Enchanted","Ghost Rider's","Ghastly","Ghost's","Thor's","Mythological","Mystic",
				"Death's","Sacred","Rubber","Putrid","Mighty","Graceful","Average","Normal","Killing","Peasant's","Heavy",
				"Light","Lightweight","Brave","Medieval","Lightning","Slimey","Gooey","Quartz","Acurate","Abysmal",
				"Feathered","Deathfire Grasp","Frozen","Broken"};

		
		String[] wordlist2 = null;
		
			String[] wl2 = {"Weapon", "Entity", "Destroying Device", "Doomsday Machine", "Your", "Malice", "Utrol"};
			wordlist2 = wl2;
						
						
		String[] wordlist3 = {"of Punishment","of Light","of Darkness","of Goodness","of Anger","of Revenge","of Happiness",
				"of Sorrow","of Melancholy","of Destruction","of Insanity","of Retribution","of Kindness","of Love",
				"of Sin","of Gluttony","of Wrath","of Envy","of Pride","of Chastity","of Temperance","of Charity","of Pride",
				"of Diligence","of Patience","of Acedia","of the East","of the West","of the North","of the South",
				"of Bravery","of Valour","of Freedom","of the Dragon","of Magic","of the Earth","of Corruption","of Fortune",
				"of Luck","of the Sea","of the Mountain","of the Sky","of Heaven","of Hell","of Mordor","of the Clouds",
				"of Fire","of the Ocean","of Gold","of Jade","of Silver","of Gaia","of Old","of the Forest","of the Plains",
				"of Enchantment","of Magnetism","of the Gods","of the Angels","of Lord","of Legend","of Metal","of Ferocity",
				"of Hatred","of Power","of Vengance","of the Harpie","of the Lion","of the Wolf","of the Crag","of the Stars",
				"of the Sun","of the Moon","of Day","of Night","of Enlightenment","of the Diety","of Evasion","of Accuracy",
				"of Resistance","of the Hydra","of the Ogre","of the Serpant","of the Kracken","of Cthulhu","of Steel","of Ice",
				"of Flowers","of the Phoenix","of Snowdrop","of Primrose","of the Lily","of the Valley","of the River","of Larkspur",
				"of Cosmos","of Narcissus","of Aries","of Taurus","of Gemini","of Leo","of Virgo","of Libra","of Scorpio",
				"of Sagittarius","of Capricorn","of Aquarius","of Pisces","of the Rat ","of the Ox","of the Tiger","of the Rabbit",
				"of the Snake","of the Horse","of the Goat","of the Monkey","of the Dog","of the Boar","of Healing","of Wind",
				"of Water","of Lightning","of Wealth","of the Apocalypse","of Dawn","of the Hunt","of the Chase","of Summer",
				"of Spring","of Winter","of Autumn","of Time","of Invisibility","of Hellfire","of Iron","of Mjölnir","of Beowulf",
				"of Death","of Truth","of Silence","of the Untold","of the Abyss","of Swiftness","of Harmony","Alacrity", 
				"of the IV Century", "of the IIV Century", "of the V Century", "of the II Century", "of the I Century",
				"of the III Century", "of the VI Century", "of the X Century", "of the IX Century", "of the XI Century",
				"of the VII Century", ""};				

		 

		int randomNumber1 = (int) (Math.random() * wordlist1.length);

		int randomNumber2 = (int) (Math.random() * wordlist2.length);
		
		int randomNumber3 = (int) (Math.random() * wordlist3.length);

		String name = wordlist1[randomNumber1] + " " + wordlist2[randomNumber2] + " " + wordlist3[randomNumber3];	
		
		System.out.println("[Weapon Name Generated] - " + name);
        
		return name;
	}
	
	//[FB Shop]
	// ItemID:Dura
	// Amount
	// Buying:Selling

	public static int signGetBuyingPrice(Sign sign) {
	
		if(sign.getLine(3).split(":").length > 1){
			
			return Integer.parseInt(sign.getLine(3).split(":")[0]);
			
		}else{
			return Integer.parseInt(sign.getLine(3));
		}
	}
	
		

	public static int signGetSellingPrice(Sign sign) {
		if(sign.getLine(3).split(":").length > 1){
			
			return Integer.parseInt(sign.getLine(3).split(":")[1]);
			
		}else{
			return 0;
		}
	}

	@SuppressWarnings("deprecation")
	public static ItemStack signGetItemStack(Sign sign) {
		
		ItemStack is = new ItemStack(Material.STONE, 1);
		
		if(sign.getLine(1).split(":").length > 1){
			is.setType(Material.getMaterial(Integer.parseInt(sign.getLine(1).split(":")[0])));
			is.setDurability((short) Integer.parseInt(sign.getLine(1).split(":")[1]));
		}else{
			is.setType(Material.getMaterial(Integer.parseInt(sign.getLine(1))));
		}
		is.setAmount(Integer.parseInt(sign.getLine(2)));
	
		return is;
		
	}

	public static boolean playerHasRoom(Player p, ItemStack item) {
	
		for(ItemStack i : p.getInventory())
			if(i == null || ((i.getType() == item.getType() && i.getAmount() < 64 && i.getMaxStackSize() > 1)))
				return true;
		
		return false;
	}

	
	public static void ClearPotionEffects(Player p){
		p.removePotionEffect(PotionEffectType.BLINDNESS);
		p.removePotionEffect(PotionEffectType.CONFUSION);
		p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		p.removePotionEffect(PotionEffectType.FAST_DIGGING);
		p.removePotionEffect(PotionEffectType.HARM);
		p.removePotionEffect(PotionEffectType.HEAL);
		p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
		p.removePotionEffect(PotionEffectType.HUNGER);
		p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
		p.removePotionEffect(PotionEffectType.JUMP);
		p.removePotionEffect(PotionEffectType.NIGHT_VISION);
		p.removePotionEffect(PotionEffectType.POISON);
		p.removePotionEffect(PotionEffectType.REGENERATION);
		p.removePotionEffect(PotionEffectType.SLOW);
		p.removePotionEffect(PotionEffectType.SPEED);
		p.removePotionEffect(PotionEffectType.WEAKNESS);
	}

	public static int[] sortList(HashMap<Integer, String> highestKills) {
		
		
		
		return null;
	}
}
