package controllers;

import java.util.ArrayList;

import entities.JellyFish;
import entities.Player;
import entities.Projectile;

public class MasterProjectileController {

	public ArrayList<SingleSpiralProjectileSpawner> SpiralProjectileSpawners;
	public ArrayList<MultiSpiralProjectileSpawner> MultiProjectileSpawners;
	public ArrayList<SingleExplosionProjectileSpawner> SingleExplosionProjectileSpawners;
	public ArrayList<JellyFish> JellyFishProjectiles;
	
	public MasterProjectileController(){
		this.SpiralProjectileSpawners = new ArrayList<SingleSpiralProjectileSpawner>();
		this.MultiProjectileSpawners = new ArrayList<MultiSpiralProjectileSpawner>();
		this.SingleExplosionProjectileSpawners = new ArrayList<SingleExplosionProjectileSpawner>();
		this.JellyFishProjectiles = new ArrayList<JellyFish>();
	}
	
	
	// This method will aim the jelly fish at the player
	public void addJellyFishProjectile(float delay, int size, float travelTime, float timer, Player player, float distance, float direction){
		JellyFishProjectiles.add(new JellyFish(delay, size, travelTime, timer, player, distance, direction));
	}
	
	// This method will set the target x and y position for the jelly fish to travel to.
	public void addJellyFishProjectile(float x, float y, float delay, int size, float direction, float travelTime, float timer){
		JellyFishProjectiles.add(new JellyFish(x, y, delay, size, direction, travelTime, timer));
	}
	
	public void addMulitSpiralProjectileSpawner(float x, float y, float speed, float direction, float projectileSpeed, float startAngle, float angleDifference, float delay, int leaves, int projectileSize){
		MultiProjectileSpawners.add(new MultiSpiralProjectileSpawner(x, y, speed, direction, projectileSpeed, startAngle, angleDifference, delay, leaves, projectileSize));
	}
	
	public void addSingleSpiralProjectileSpawner(float x, float y, float speed, float direction, float startAngle, float angleDifference, float delay, int projectileSize, float projectileSpeed){
		SpiralProjectileSpawners.add(new SingleSpiralProjectileSpawner(x, y, speed, direction, startAngle, angleDifference, delay, projectileSize, projectileSpeed));
	}
	
	public void addExplosionProjectileSpawner(float x, float y, float delay, float startAngle, int numberOfProjectiles, int projectileSize){
		SingleExplosionProjectileSpawner explosionProjectileSpawner = new SingleExplosionProjectileSpawner(x, y, delay, startAngle, numberOfProjectiles, projectileSize);
		SingleExplosionProjectileSpawners.add(explosionProjectileSpawner);
	}
	
	
	public void update(){
		singleSpiralSpawnersUpdate();
		multiSpiralSpawnersUpdate();
		ExplosionProjectileSpawnersUpdate();

	}
	
	public void updateJellyFish(){
		JellFishProjectilesUpdate();
	}
	
	public void JellFishProjectilesUpdate(){
		for (JellyFish j : JellyFishProjectiles){
			j.update();
			j.render();
		}
	}
	
	public void ExplosionProjectileSpawnersUpdate(){
		for (SingleExplosionProjectileSpawner s : SingleExplosionProjectileSpawners){
			s.update();
		}
	}
	
	public void multiSpiralSpawnersUpdate(){
		for (MultiSpiralProjectileSpawner s : MultiProjectileSpawners){
			s.update();
		}
	}
	
	public void singleSpiralSpawnersUpdate(){
		for (SingleSpiralProjectileSpawner s : SpiralProjectileSpawners){
			s.update();
		}
		
		int i = 0;
		int size = SpiralProjectileSpawners.size();
		while (i < size){
			SingleSpiralProjectileSpawner spawner = SpiralProjectileSpawners.get(i);
			if (spawner.outOfBounds()){
				removeSingleSpiralProjectileSpawner(spawner);
				spawner = null;
				size -= 1;
			} else {
				i += 1;
			}
		}
	}
	
	public void removeSingleSpiralProjectileSpawner(SingleSpiralProjectileSpawner spawner){
		SpiralProjectileSpawners.remove(spawner);
	}
	
	public void removeMultiSpiralProjectileSpawner(MultiSpiralProjectileSpawner spawner){
		MultiProjectileSpawners.remove(spawner);
	}
	
	public void clearSpawners(){
		SpiralProjectileSpawners.clear();
		MultiProjectileSpawners.clear();
		SingleExplosionProjectileSpawners.clear();
		JellyFishProjectiles.clear();
	}
	
	public ArrayList<SingleSpiralProjectileSpawner> getSpiralProjectileSpawners(){
		return this.SpiralProjectileSpawners;
	}
	
	public ArrayList<MultiSpiralProjectileSpawner> getMultiProjectileSpawners(){
		return this.MultiProjectileSpawners;
	}
	
	public int getNumberOfMultProjectileSpawners(){
		return this.MultiProjectileSpawners.size();
	}
	
	public ArrayList<Projectile> getAllProjectiles(){
		ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
		for (MultiSpiralProjectileSpawner m : MultiProjectileSpawners){
			for (SingleSpiralProjectileSpawner s : m.SpiralProjectileSpawners){
				for (Projectile p : s.getProjectiles()){
					projectiles.add(p);
				}
			}
		}
		
		for (SingleExplosionProjectileSpawner s : SingleExplosionProjectileSpawners){
			for (Projectile p : s.getProjectiles()){
				projectiles.add(p);
			}
		}
		
		for (JellyFish j : JellyFishProjectiles){
			for (Projectile p : j.getProjectiles()){
				projectiles.add(p);
			}
		}
		
		return projectiles;
	}
}
