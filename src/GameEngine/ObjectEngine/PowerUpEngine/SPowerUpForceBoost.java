package GameEngine.ObjectEngine.PowerUpEngine;

import GameEngine.ControlEngine.SPowerUpControlServer;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.EffectEngine.SEffectForceBoost;
import Main.SMain;

public class SPowerUpForceBoost extends SPowerUp {
	protected static int currentNumberOfPowerUps = 0;
	protected static int maxNumberOfPowerUps = 3;
	
	public SPowerUpForceBoost(SVector pos) {
		super(pos);
		this.type = SPowerUpFactory.PowerUpForceBoost;
		this.getBody().setTexture("res/object/powerup/powerupforceboost.png");
		this.setLookDir(new SVector(0, -1));
		if (SMain.IsServer()){
			((SPowerUpControlServer)this.getController()).setDuration(1100);
		}
	}

	@Override
	public boolean applyToEntity(SEntity entity) {
		SEffectForceBoost effectForceBoost = new SEffectForceBoost(entity);
		if (effectForceBoost.isActive()){
			SFH.Effects.createNewEffectAtServer(effectForceBoost);
			return true;
		}  else if (effectForceBoost.isApplied()){
			return true;
		} else{
			return false;
		}
	}
}