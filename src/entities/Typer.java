package entities;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import UI.Text;
import engineTester.Clock;

public class Typer extends Entity{

	public String text, currentText;
	private TrueTypeFont font;
	public float timeSinceLastLetter;
	public float delay;
	public int i;
	public boolean hide;
	public boolean renderStar;
	
	public int fontsize;
	
	
	public Typer(float x, float y, int fontsize, String text) {
		super(x, y);
		this.text = text;
		this.currentText = "";
		this.delay = 1.0f;
		this.i = 0;
		this.hide = false;
		this.renderStar = true;
		
		Font awtFont = new Font(Font.SERIF, Font.PLAIN, fontsize); //name, style (PLAIN, BOLD, or ITALIC), size
		this.font = new TrueTypeFont(awtFont, false); //base Font, anti-aliasing true/false
		
		this.fontsize = fontsize;
	}

	
	public void render() {
		if (hide)
			return;
		if (renderStar){
			font.drawString(super.getX()-30, super.getY()+10, "*", Color.white);		// magic numbers for alignment
		}
		font.drawString(super.getX(), super.getY(), this.currentText, Color.white);
	}

	
	public void update() {
		if (hide)
			return;
		if (timeSinceLastLetter > delay){
			if (i >= text.length())
				return;
			currentText += (text.charAt(i));
			i += 1;
			timeSinceLastLetter = 0;
		}
		
		timeSinceLastLetter += Clock.Delta();
	}
	
	public String getText(){
		return this.text;
	}
	
	public String getCurrentText(){
		return this.currentText;
	}

	public void setCurrentText(String text){
		this.text = text;
		this.currentText = text;
		this.i = text.length();
	}
	
	public void setText(String text){
		this.text = text;
		this.currentText = "";
		this.i = 0;
	}
	
	public void inverseHide(){
		this.hide = !this.hide;
	}
	
	public void hide(){
		this.hide = true;
	}
	
	public void show(){
		this.hide = false;
	}
	
	public void setRenderStar(boolean value){
		this.renderStar = value;
	}
	
	public boolean finishTyping(){
		return this.getCurrentText() == this.getText();
	}
	
	public int getfontsize(){
		return this.fontsize;
	}
	
	public void setfontsize(int fontsize){
		this.fontsize = fontsize;
		Font awtFont = new Font(Font.SERIF, Font.PLAIN, fontsize); 	//name, style (PLAIN, BOLD, or ITALIC), size
		this.font = new TrueTypeFont(awtFont, false); 				//base Font, anti-aliasing true/false
	}
}
