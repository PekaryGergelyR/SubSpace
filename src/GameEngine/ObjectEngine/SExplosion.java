package GameEngine.ObjectEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import GameEngine.SPlayer;
import GameEngine.SResLoader;
import GameEngine.BaseEngine.SObject;
import GameEngine.GeomEngine.SVector;
import Main.SMain;
import RenderingEngine.SRenderObject;

public class SExplosion extends SObject{
	
	protected int currentLife = 1;
	protected int maxLife = 64;
	protected float size = 0.7f;
	
	protected boolean firstTime = true;
	
	protected String audioString;
	
	public SExplosion(SVector pos){
		super();
		this.pos = new SVector(pos);
		this.getBody().setTexture("res/object/explosion/explosionv2.png");
		this.getBody().setScale(1.0f);
		this.getBody().setDrawScale(size);
		Random random = new Random();
		this.setLookDir(new SVector(random.nextFloat()-0.5f, random.nextFloat()-0.5f));
		this.audioString = "res/audio/small_blast.wav";
	}

	@Override
	public void update() {
		if (firstTime && !SMain.IsServer()){
			SPlayer localPlayer = SMain.getGameInstance().getLocalPlayer();
			if (localPlayer.getEntity().getObjectState().equals(ObjectState.Active))
			{
				SVector playerPos = localPlayer.getEntity().getPos();
				float dist = playerPos.d(pos);
				float maxDistance = 4000;
				if (dist < maxDistance){
					float maxVolume = 0.2f;
					float volume = (maxDistance-dist)*maxVolume/maxDistance;
					playSoundEffect(1.0f, volume);
				}
			}
			firstTime = false;
		}
		//TODO remove this commented out
		//this.getBody().setScale(this.getBody().getScale()+growing);
		currentLife++;
		if(currentLife>=maxLife){
			this.setObjectState(ObjectState.WaitingDelete);
		}
	}

	@Override
	public List<SRenderObject> getDrawables() {
		List<SRenderObject> list = new ArrayList<SRenderObject>();
		int Life = currentLife;
		SVector leftBottom = new SVector(((Life-1)%8)*0.125f, ((Life-1)/8+1)*0.125f);
		SVector rightUpper = new SVector(((Life-1)%8+1)*0.125f, ((Life-1)/8)*0.125f);
		
		list.add(new SRenderObject(body.getTexture(), pos, lookDir.getAngle(), body.getCurrentDrawScale(), 1.0f, body.getColor(), leftBottom, rightUpper, 0.0101f));
		return list;
	}
	
	protected void playSoundEffect(float speed, float volume){
		SResLoader.getAudio(audioString).playAsSoundEffect(speed, volume, false);
	}
}
