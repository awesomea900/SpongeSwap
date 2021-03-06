package controllers;

import java.util.ArrayList;

import engineTester.Clock;
import entities.Entity;
import entities.Projectile;

public class SingleSpiralProjectileSpawner {

	public float delay, angleDifference, x, y, spawnerSpeed, direction, timeSinceLastShot;
	public int incrementer;
	private ArrayList<Projectile> projectiles;
	private float startAngle;
	public int projectileSize;
	private float projectileSpeed;
	
	public SingleSpiralProjectileSpawner(float x, float y, float spawnerSpeed, float direction, float startAngle, float angleDifference, float delay, int projectileSize, float projectileSpeed){
		this.x = x;
		this.y = y;
		this.spawnerSpeed = spawnerSpeed;
		this.direction = direction;
		this.startAngle = startAngle;
		this.angleDifference = angleDifference;
		this.delay = delay;
		this.projectiles = new ArrayList<Projectile>();
		this.timeSinceLastShot = 0f;
		this.incrementer = 0;
		this.projectileSize = projectileSize;
		this.projectileSpeed = projectileSpeed;
	}
	
	public void update(){
		entitySpiralSpawner(angleDifference, delay, projectileSize, projectileSpeed);
		
		for (Projectile p : projectiles){
			p.update();
			p.render();
		}
	}
	
	public void entitySpiralSpawner(float angleDifference, float delay, int projectileSize, float projectileSpeed){		
		if (timeSinceLastShot > delay){
			shootProjectile(x, y, projectileSpeed, (float) (angleDifference * incrementer * Math.PI / 180) + startAngle, projectileSize);
			timeSinceLastShot = 0;
			incrementer++;
		}
	
		x += (float) (spawnerSpeed * Math.cos(direction));
		y += (float) (spawnerSpeed * Math.sin(direction));
		
		timeSinceLastShot += Clock.Delta();
	}
	
	// Angle is in radians
	public void shootProjectile(float x, float y, float projectileSpeed, float angle, int size){
		Projectile p = new Projectile(x, y, projectileSpeed, angle, size, (float) 0.0);
		projectiles.add(p);
	}
	
	public boolean outOfBounds(){
		if (x < 0 || x > 1280 || y < 0 || y > 960){
			return true;
		}
		return false;
	}
	
	public ArrayList<Projectile> getProjectiles(){
		return this.projectiles;
	}
	
}
