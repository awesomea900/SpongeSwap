package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import shapes.Shapes;

public class SpongeBob extends Entity {

	public Texture faceTexture;
	public Texture bodyTexture;
	
	public float faceX, faceY, faceYIdleDisplacement;
	public int faceW, faceH;
	
	public float bodyX, bodyY;
	public int bodyW, bodyH;
	
	public float faceXOffset, bodyYOffset;
	
	public float xSpeed;
	
	public int counter;
	
	private final float MAXDISPLACEMENT = 5;
	private final float IDLEMOVEMENTSPEED = 0;
	private boolean upMovement = true;
	
	public ArrayList<Texture> animationDown;
	
	public SpongeBob(float x, float y) {
		super(x, y);
		
		int size = 4;
		
		this.faceXOffset = 11 * size;
		this.bodyYOffset = 27 * size;
		this.faceX = super.getX() + faceXOffset;
		this.faceY = super.getY();
		
		this.faceW = 49*size;
		this.faceH = 44*size;
		
		this.bodyX = super.getX();
		this.bodyY = super.getY() + bodyYOffset;
		
		this.bodyW = 72 * size;
		this.bodyH = 48 * size;
	
		this.faceTexture = Shapes.LoadTexture("res/SpongeBob/face_netural.png", "PNG");
		this.bodyTexture = Shapes.LoadTexture("res/SpongeBob/torso.png", "PNG");
		
		this.xSpeed = 4;
		this.counter = 0;
		
		//Idle movement
		this.faceYIdleDisplacement = 0;
		
		loadAnimationDownTextures();
	}

	public void render() {
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		Shapes.DrawQuadTex(faceTexture, faceX, faceY + faceYIdleDisplacement, faceW, faceH);
		Shapes.DrawQuadTex(bodyTexture, bodyX, bodyY, bodyW, bodyH);
	}

	public void update() {
		if (counter > 0){
			super.setX(super.getX() + xSpeed);
			counter -= 1;
		}
		
		if (counter == 0 && xSpeed < 0){
			xSpeed = 0;
			counter = 120;
		} else if (counter == 0 && xSpeed == 0){
			this.faceTexture = Shapes.LoadTexture("res/SpongeBob/face_netural.png", "PNG");
			xSpeed = 4;
			counter = 60;
		}
		
		
		this.faceX = super.getX() + faceXOffset;
		this.faceY = super.getY();
		
		this.bodyX = super.getX();
		this.bodyY = super.getY() + bodyYOffset;
		
		if (upMovement){
			this.faceYIdleDisplacement += IDLEMOVEMENTSPEED;
			if (faceYIdleDisplacement >= MAXDISPLACEMENT){
				upMovement = false;
			}
		} else {
			this.faceYIdleDisplacement -= IDLEMOVEMENTSPEED;
			if (faceYIdleDisplacement < 0){
				upMovement = true;
			}
		}
		
	}
	
	public void dodge(){
		this.faceTexture = Shapes.LoadTexture("res/SpongeBob/face_wink_right.png", "PNG");
		this.xSpeed = -4;
		this.counter = 60;
	}
	
	public void loadAnimationDownTextures(){
		this.animationDown = new ArrayList<Texture>();
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down1.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down2.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down3.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down4.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down5.png", "PNG"));
	}

}
