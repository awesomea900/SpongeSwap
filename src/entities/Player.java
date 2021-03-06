package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import audio.AudioMaster;
import audio.Source;
import data.PlayerStats;
import engineTester.Clock;
import renderEngine.DisplayManager;
import shapes.Shapes;

public class Player extends Entity {

	public final int DOWN = 0;
	public final int LEFT = 1;
	public final int UP = 2;
	public final int RIGHT = 3;

	public final int PLAYMODE = 0;
	public final int MENUMODE = 1;
	public final int GAMEOVER = 2;
	public final int DISABLE = 3;
	
	public int speed, size, gravityDirection;

	public Texture texture;
	// bounds the player to an area of movement
	public float xMinBound, xMaxBound, yMinBound, yMaxBound;
	
	// handle movement
	public Vector3f color;
	
	// Blue movement
	public float gravity;				// blue movement
	public boolean inAir, falling;		// blue movement
	
	public int mode;
	
	
	public PlayerStats stats;
	
	
	private boolean hide;
	
	
	// items to handle death animation
	private int extention;
	public float timeDelay;
	private ArrayList<PlayerBrokenBit> playerBrokenBits;
	private boolean phase1;
	
	// Audio handlers
	private int SoulShatterBuffer;
	private int SoulSplitBuffer;
	private int SlamBuffer;
	private Source source;
	
	private boolean playSlam;
	
	public Player(int x, int y, int size) {
		super(x, y);
		this.size = size;
		this.speed = 2;
		this.texture = Shapes.LoadTexture("res/heart.png", "PNG");
		resetBounds();
		this.color = new Vector3f(0, 0, 1);
		this.gravity = 0;
		this.inAir = false;
		this.falling = false;
		this.gravityDirection = DOWN;
		this.mode = MENUMODE;
		this.stats = new PlayerStats();
		this.hide = false;
		
		
		//Death handler
		this.timeDelay = 0;
		this.extention = 0;
		this.playerBrokenBits = new ArrayList<PlayerBrokenBit>();
		this.phase1 = false;
		
		// Audio
		this.SoulShatterBuffer = AudioMaster.loadSound("audio/SOUL shatter.wav");
		this.SoulSplitBuffer = AudioMaster.loadSound("audio/SOUL split.wav");
		this.SlamBuffer = AudioMaster.loadSound("audio/Slam.wav");
		this.source = new Source();
		this.playSlam = false;
	}
	
	public void init(){
		this.timeDelay = 0;
	}

	public void render() {
		if (hide){
			return;
		}
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(color.x, color.y, color.z);

		Shapes.DrawQuadTexRot(texture, super.getX()-extention/2, super.getY(), size + extention, size, gravityDirection * 90);
	}

	
	public void update() {

		// Clamp the player within the bounds of the window
		super.setX(clamp(super.getX(), xMinBound, xMaxBound));
		super.setY(clamp(super.getY(), yMinBound, yMaxBound));

		if (this.mode == PLAYMODE) {
			playermovement();
		} else if (this.mode == MENUMODE){
			// handled by the menu class, (in the menu class it sets the position of the player)
		} else if (this.mode == GAMEOVER){
			gameOverSequence();
			// run the death animation
			// Heart -> broken heart -> 5 random pieces
		} else if (this.mode == DISABLE){
			// Player is not responsive to keyboard events
			if (this.getColor().equals(new Vector3f(0,0,1))){
				applyGravity(gravityDirection);
			}
		}

	}
	
	public void gameOverSequence(){
		if (timeDelay > 40 && hide == false){
			int i = 0;
			while (i < 6){
				Random rn = new Random();
				int bit = rn.nextInt(3) + 1;
				playerBrokenBits.add(new PlayerBrokenBit(super.getX()+this.size/2, super.getY()+this.size/2, "playerBrokenBit"+bit, (float) (Math.random()*4-2), (float) (Math.random()*-4+1), 1f, this.color));
				i += 1;
			}
			
			this.hide = true;
			
			// Play the SOUL shatter sfx
			this.source.play(this.SoulShatterBuffer);
			
		} else if (timeDelay > 20 && phase1 == false){
			setTexture("heart_broken");
			this.extention = 8;
			this.source.play(this.SoulSplitBuffer);
			this.phase1 = true;
			//System.out.println("Insert death animation here");
		}

		for (PlayerBrokenBit b : playerBrokenBits){
			b.update();
			b.render();
		}
		
		timeDelay += Clock.Delta();
	}

	public void playermovement(){
		if (this.getColor().equals(new Vector3f(1, 0, 0))) {			// When player color is red
			redMovement();
		} else if (this.getColor().equals(new Vector3f(0, 0, 1))) {		// When player color is blue
			blueMovement(gravityDirection);
			
			applyGravity(gravityDirection);								// Apply gravity

			// Allows for changing gravity direction
			/*
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				setGravityDirection(UP);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				setGravityDirection(DOWN);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				setGravityDirection(LEFT);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				setGravityDirection(RIGHT);
			}
			*/
		}
	}
	
	public void applyGravity(int gravityDirection) {
		if (gravityDirection == DOWN) {
			if (super.getY() <= yMaxBound) {
				super.setY(super.getY() + gravity);
				gravity += 0.02;
				inAir = true;
				if (super.getY() >= yMaxBound) {
					gravity = 0;
					inAir = false;
					falling = false;
					if (playSlam){
						playSlam = false;
						source.play(SlamBuffer);
					}
				}
			}
		} else if (gravityDirection == LEFT) {
			if (super.getX() >= xMinBound) {
				super.setX(super.getX() - gravity);
				gravity += 0.02;
				inAir = true;
				if (super.getX() <= xMinBound) {
					gravity = 0;
					inAir = false;
					falling = false;
					if (playSlam){
						playSlam = false;
						source.play(SlamBuffer);
					}
				}
			}
		} else if (gravityDirection == UP) {
			if (super.getY() >= yMinBound) {
				super.setY(super.getY() - gravity);
				gravity += 0.02;
				inAir = true;
				if (super.getY() <= yMinBound) {
					gravity = 0;
					inAir = false;
					falling = false;
					if (playSlam){
						playSlam = false;
						source.play(SlamBuffer);
					}
				}
			}
		} else if (gravityDirection == RIGHT) {
			if (super.getX() <= xMaxBound) {
				super.setX(super.getX() + gravity);
				gravity += 0.02;
				inAir = true;
				if (super.getX() >= xMaxBound) {
					gravity = 0;
					inAir = false;
					falling = false;
					if (playSlam){
						playSlam = false;
						source.play(SlamBuffer);
					}
				}
			}
		}
	}

	public void redMovement() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveUp();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveDown();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			moveLeft();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			moveRight();
		}
	}

	public void blueMovement(int gravityDirection) {
		if (gravityDirection == DOWN) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W) && falling == false) {
				super.setY(super.getY() - speed);
				inAir = true;
				falling = false;
			} else if (falling == false && inAir == true) {
				falling = true;
				if (gravity > speed) {
					gravity = gravity - speed;
				} else {
					gravity = 0;
				}
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				moveLeft();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				moveRight();
			}
		} else if (gravityDirection == LEFT) {
			if (Keyboard.isKeyDown(Keyboard.KEY_D) && falling == false) {
				super.setX(super.getX() + speed);
				inAir = true;
				falling = false;
			} else if (falling == false && inAir == true) {
				falling = true;
				if (gravity > speed) {
					gravity = gravity - speed;
				} else {
					gravity = 0;
				}
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				moveUp();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				moveDown();
			}
		} else if (gravityDirection == UP) {
			if (Keyboard.isKeyDown(Keyboard.KEY_S) && falling == false) {
				super.setY(super.getY() + speed);
				inAir = true;
				falling = false;
			} else if (falling == false && inAir == true) {
				falling = true;
				if (gravity > speed) {
					gravity = gravity - speed;
				} else {
					gravity = 0;
				}
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				moveLeft();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				moveRight();
			}
		} else if (gravityDirection == RIGHT) {
			if (Keyboard.isKeyDown(Keyboard.KEY_A) && falling == false) {
				super.setX(super.getX() - speed);
				inAir = true;
				falling = false;
			} else if (falling == false && inAir == true) {
				falling = true;
				if (gravity > speed) {
					gravity = gravity - speed;
				} else {
					gravity = 0;
				}
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				moveUp();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				moveDown();
			}
		}
	}

	public float clamp(float variable, float min, float max) {
		if (variable < min)
			return min;
		else if (variable > max)
			return max;
		else
			return variable;
	}

	public void playSlamSFXOnNextChange(){
		this.playSlam = true;
	}
	
	public void setGravityDirection(int gravity_direction) {
		if (gravity_direction >= 0 && gravity_direction <= 3) {
			this.gravityDirection = gravity_direction;
		} else {
			System.out.println("gravity direction needs to be a int between 0 and 3 inclusive");
		}
	}
	
	public void resetBounds(){
		this.xMinBound = 0;
		this.xMaxBound = DisplayManager.getWidth() - size;
		this.yMinBound = 0;
		this.yMaxBound = DisplayManager.getHeight() - size;
	}
	
	public void reset(){
		this.timeDelay = 0;
		this.extention = 0;
		this.phase1 = false;
		this.gravityDirection = DOWN;
		this.setTexture("heart");
		this.playerBrokenBits.clear();
	}

	public void moveLeft() {
		if (super.getX() > xMinBound) {
			super.setX(super.getX() - speed);
		}
	}

	public void moveRight() {
		if (super.getX() < xMaxBound) {
			super.setX(super.getX() + speed);
		}
	}

	public void moveUp() {
		if (super.getY() > yMinBound) {
			super.setY(super.getY() - speed);
		}
	}

	public void moveDown() {
		if (super.getY() < yMaxBound) {
			super.setY(super.getY() + speed);
		}
	}

	public void setTexture(String texture){
		this.texture = Shapes.LoadTexture("res/" + texture + ".png", "PNG");
	}
	
	public float getxMinBound() {
		return xMinBound;
	}

	public void setxMinBound(float xMinBound) {
		this.xMinBound = xMinBound;
	}

	public float getxMaxBound() {
		return xMaxBound;
	}

	public void setxMaxBound(float xMaxBound) {
		this.xMaxBound = xMaxBound;
	}

	public float getyMinBound() {
		return yMinBound;
	}

	public void setyMinBound(float yMinBound) {
		this.yMinBound = yMinBound;
	}

	public float getyMaxBound() {
		return yMaxBound;
	}

	public void setyMaxBound(float yMaxBound) {
		this.yMaxBound = yMaxBound;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public int getGravityDirection() {
		return gravityDirection;
	}

	public void setPlayMode() {
		this.mode = PLAYMODE;
	}

	public void setMenuMode() {
		this.mode = MENUMODE;
	}
	
	public void disablePlayerMovement(){
		this.mode = DISABLE;
	}
	
	public void decrementHealth(){
		this.stats.decrementHealth();
	}
	
	public int getHealth(){
		return this.stats.getHealth();
	}
	
	public void setHealth(int health){
		this.stats.setHealth(health);
	}

	public void setGameOverMode() {
		this.mode = GAMEOVER;
	}
	
	public boolean getHideStatus(){
		return this.hide;
	}
	
	public void setHide(boolean value){
		this.hide = value;
	}
	
	public void hide(){
		this.hide = true;
	}
	
	public void show(){
		this.hide = false;
	}
	
	public PlayerStats getStats(){
		return this.stats;
	}
	
}
