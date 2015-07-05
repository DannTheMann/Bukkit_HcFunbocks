package aog.minigame.funbocks.instance;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;

import aog.minigame.funbocks.Main;

public class FBBoss {
	
	private String name;
	private int amount;
	private EntityType enemy;
	private boolean lightningCreeper;
	private boolean spawnOnPlayer;
	private SkeletonType skeletonType;
	private ArrayList<EntityType> stackedEntites;
	
	public void setSpawnPlayer(boolean flag){
		spawnOnPlayer = flag;
	}
	
	/**
	 * Used to create a standard boss, with the amount.
	 * @param name
	 * @param spawnAmount
	 * @param bossType
	 */
	public FBBoss(String name, int spawnAmount, EntityType bossType, boolean spawn){
		this.setName(name);
		this.setAmount(spawnAmount);
		this.setEnemy(bossType);
		this.spawnOnPlayer = spawn;
	}

	/**
	 * Used to create skeleton boss.
	 * @param name
	 * @param spawnAmount
	 * @param bossType
	 * @param skeleType
	 */
	public FBBoss(String name, int spawnAmount, EntityType bossType, SkeletonType skeleType, boolean spawn){
		this.setName(name);
		this.setAmount(spawnAmount);
		this.setEnemy(bossType);
		this.setSkeletonType(skeleType);
		this.spawnOnPlayer = spawn;
	}
	
	/**
	 * 
	 * @param name
	 * @param spawnAmount
	 * @param bossType
	 * @param lightningCreeper
	 */
	public FBBoss(String name, int spawnAmount, EntityType bossType, boolean lightningCreeper, boolean spawn){
		this.setName(name);
		this.setAmount(spawnAmount);
		this.setEnemy(bossType);
		this.setLightningCreeper(lightningCreeper);
		this.spawnOnPlayer = spawn;
	}
	
	public FBBoss(String string, int spawnAmount, EntityType bossType, ArrayList<EntityType> stackedEntites, boolean spawn) {
		this.setName(string);
		this.setAmount(spawnAmount);
		this.setEnemy(bossType);
		this.setStackedEntites(stackedEntites);
		this.spawnOnPlayer = spawn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public EntityType getEnemy() {
		return enemy;
	}

	public void setEnemy(EntityType enemy) {
		this.enemy = enemy;
	}

	public SkeletonType getSkeletonType() {
		return skeletonType;
	}

	public void setSkeletonType(SkeletonType skeletonType) {
		this.skeletonType = skeletonType;
	}

	public boolean isLightningCreeper() {
		return lightningCreeper;
	}

	public void setLightningCreeper(boolean lightningCreeper) {
		this.lightningCreeper = lightningCreeper;
	}

	public ArrayList<EntityType> getStackedEntites() {
		return stackedEntites;
	}

	public void setStackedEntites(ArrayList<EntityType> stackedEntites) {
		this.stackedEntites = stackedEntites;
	}

	public ArrayList<LivingEntity> spawn() {
		
		ArrayList<LivingEntity> lel = new ArrayList<LivingEntity>();
		
		for(int i = 0; i < getAmount(); i ++){
			
			LivingEntity le = null;
				
				if(spawnOnPlayer){
					Location l = getRandomPlayerLocation();
					le = (LivingEntity) l.getWorld().spawnEntity(l, getEnemy());
				}else{
					le = (LivingEntity) Main.currentGame.getMap().getArena().getBukkitLocation().getWorld().
						spawnEntity(Main.currentGame.getMap().getArena().getBukkitLocation(), getEnemy());
				}
				le.setCustomName(getName());
				le.setCustomNameVisible(true);
				
				if(getStackedEntites() != null){
					
					LivingEntity le2 = le;
					LivingEntity pass = null;
					
					for(EntityType et : getStackedEntites()){
						
						pass = (LivingEntity) Main.currentGame.getMap().getArena().getBukkitLocation().getWorld().
								spawnEntity(Main.currentGame.getMap().getArena().getBukkitLocation(), et);
						
						le2.setPassenger(pass);
						
						le2 = pass;
						
						Main.currentGame.getLivingEntity().add(le2);
						
					}
					
				}
				
				if(le instanceof Creeper){
					
					Creeper c = (Creeper) le;
					
					c.setPowered(true);
					
				}
				
				if(getSkeletonType() != null){
					
					Skeleton s = (Skeleton) le;
					
					s.setSkeletonType(getSkeletonType());
					
				}
				
				if(le != null){
					Main.currentGame.getLivingEntity().add(le);
				}
				
				lel.add(le);
		}
		
		return lel;
		
	}

	private Location getRandomPlayerLocation() {
		
		int r = new Random().nextInt(Main.currentGame.getPlayers().size());
		
		int i = 0;
		
		try{
		
			for (FBPlayer fp : Main.currentGame.getPlayers().values()) {

				if (i == r) {
					return fp.getRawUser().getLocation();
				}

				i++;
			}
		
		}catch(Exception e){
			
		}
		
		return Main.currentGame.getMap().getArena().getBukkitLocation();
	}

	public boolean isSpawnOnPlayer() {
		return spawnOnPlayer;
	}

	public void setSpawnOnPlayer(boolean spawnOnPlayer) {
		this.spawnOnPlayer = spawnOnPlayer;
	}
}
