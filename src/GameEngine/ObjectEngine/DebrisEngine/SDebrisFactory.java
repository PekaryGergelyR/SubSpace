package GameEngine.ObjectEngine.DebrisEngine;

import java.util.ArrayList;
import java.util.Random;

import GameEngine.SId;
import GameEngine.Specifications;
import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.GeomEngine.SVector;
import GameEngine.InteracterEngine.SDebrisInteracter;
import GameEngine.ObjectEngine.SFactory;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SDebrisFactory extends SFactory<SDebris>{
	public static final byte Asteroid = 1;
	public static final byte Mine = 10;
	protected static int currentNumberOfAsteroids = 0;
	protected static int currentNumberOfMines = 0;
	protected static int maxNumberOfAsteroid = Specifications.maxNumberOfAsteroids;
	protected static int maxNumberOfMines = Specifications.maxNumberOfMines;
	
	public SDebrisFactory(){
		super("Debris factory", (byte)50);
	}
	
	
	public void createNewDebrisAtClient(SVector pos, SVector moveDir, float scale, int id, byte debrisType){
		if (debrisType == Asteroid){
			SAsteroid asteroid = new SAsteroid(pos, moveDir, scale);
			asteroid.setId(new SId(id));
			addObject(asteroid);
		} else if (debrisType == Mine){
			SMine mine = new SMine(pos, moveDir, scale);
			mine.setId(new SId(id));
			addObject(mine);
		}
	}
	
	public void tryToCreateNewDebrisAtServer(byte debrisType){
		if (debrisType == Asteroid){
			if (currentNumberOfAsteroids >= maxNumberOfAsteroid)
				return;
			Random random = new Random();
			int section = random.nextInt(4);
			float rate = random.nextFloat()*100f;
			float speed = 50 * rate*rate / 10000.0f + 5;
			float scale = 4 * (1 - rate*rate / 10000.0f) + 0.8f;
			
			SVector pos = new SVector();
			SVector moveDir = new SVector();
			if (section==0){
				pos = new SVector(random.nextFloat()*200-100-5000, 
						  random.nextFloat()*10200-5100);
				moveDir = new SVector(random.nextFloat() , (random.nextFloat()*2 - 1)).setLength(speed);
			}else if (section == 1){
				pos = new SVector(random.nextFloat()*10200-5100, 
						  random.nextFloat()*200-100+5000);
				moveDir = new SVector((random.nextFloat()*2 - 1), -random.nextFloat()).setLength(speed);
			}else if (section == 2){
				pos = new SVector(random.nextFloat()*200-100+5000, 
						  random.nextFloat()*10200-5100);
				moveDir = new SVector(-random.nextFloat(), (random.nextFloat()*2 - 1)).setLength(speed);
			}else {
				pos = new SVector(random.nextFloat()*10200-5100, 
						  random.nextFloat()*200-100-5000);
				moveDir = new SVector((random.nextFloat()*2 - 1), random.nextFloat()).setLength(speed);
			}
			
			SAsteroid asteroid = new SAsteroid(pos, moveDir, scale);
			addObject(asteroid);
			SM message = SMPatterns.getObjectCreateMessage(asteroid);
			SMain.getCommunicationHandler().SendMessage(message);
			currentNumberOfAsteroids++;
		} else if (debrisType == Mine){
			if (currentNumberOfMines >= maxNumberOfMines)
				return;
			
			Random random = new Random();
			int section = random.nextInt(4);
			float rate = random.nextFloat()*100f;
			float speed = 50 * rate*rate / 10000.0f + 5;
			float scale = 0.15f;
			
			SVector pos = new SVector();
			SVector moveDir = new SVector();
			if (section==0){
				pos = new SVector(random.nextFloat()*200-100-5000, 
						  random.nextFloat()*10200-5100);
				moveDir = new SVector(random.nextFloat() , (random.nextFloat()*2 - 1)).setLength(speed);
			}else if (section == 1){
				pos = new SVector(random.nextFloat()*10200-5100, 
						  random.nextFloat()*200-100+5000);
				moveDir = new SVector((random.nextFloat()*2 - 1), -random.nextFloat()).setLength(speed);
			}else if (section == 2){
				pos = new SVector(random.nextFloat()*200-100+5000, 
						  random.nextFloat()*10200-5100);
				moveDir = new SVector(-random.nextFloat(), (random.nextFloat()*2 - 1)).setLength(speed);
			}else {
				pos = new SVector(random.nextFloat()*10200-5100, 
						  random.nextFloat()*200-100-5000);
				moveDir = new SVector((random.nextFloat()*2 - 1), random.nextFloat()).setLength(speed);
			}
			
			SMine mine = new SMine(pos, moveDir, scale);
			addObject(mine);
			SM message = SMPatterns.getObjectCreateMessage(mine);
			SMain.getCommunicationHandler().SendMessage(message);
			currentNumberOfMines++;
		}
	}
	public void deletedDebris(byte debrisType){
		if (debrisType == Asteroid){
			currentNumberOfAsteroids--;
		} else if (debrisType == Mine){
			currentNumberOfMines--;
		}
	}
	
	public void collisionCheckInFactory(){
		// Asteroid checks
		// Hack around types:
		// we build an array list which has a better performance for this
		boolean[] update = new boolean[objects.size()];
		
		ArrayList<SDebris> temps = new ArrayList<SDebris>(objects.size());
		for (SDebris element : objects){
			temps.add(element); 
		}
		for (int i=0; i<temps.size()-1; i++){
			SDebris currentObject = temps.get(i);
			if (currentObject instanceof SDebris && 
					currentObject.getObjectState().equals(ObjectState.Active)){
				for (int j=i+1;j<temps.size(); j++){
					SDebris contra = temps.get(j);
					if (contra instanceof SDebris &&
						contra.getObjectState().equals(ObjectState.Active) &&
						!contra.equals(currentObject)){
						SDebrisInteracter interaction = new SDebrisInteracter(currentObject, contra);
						if (interaction.IsHappened()){
							update[i] = true;
							update[j] = true;
						}
					}
				}
			}
		}
		for (int i=0; i< update.length; i++){
			if (update[i]){
				SDebris debris = temps.get(i);
				debris.getController().setSendCounter(0);
				SM message = SMPatterns.getObjectUpdateMessage(debris);
				SMain.getCommunicationHandler().SendMessage(message);
			}
		}
	}
}
