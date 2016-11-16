package GameEngine;

import GameEngine.EntityEngine.SEntity;
import WebEngine.ComEngine.SNode;

public class SPlayer extends SIdentifiable{
	private SNode clientNode;
	private SEntity entity;
	private String name;
	private PlayerState playerState;
	
	public enum PlayerState{
		local, lan
	}
	
	//TODO implement scores
	private int kills;
	private int deaths;
	
	// clientNode is just for the local for everybody at server side
	public SPlayer(SNode clientNode, String name, PlayerState playerState){
		this.setClientNode(clientNode);
		this.playerState = playerState;
		this.name = name;
	}
	public SPlayer(String name, PlayerState playerState){
		this.playerState = playerState;
		this.name = name;
	}
	public SNode getClientNode() {
		return clientNode;
	}
	public void setClientNode(SNode clientNode) {
		this.clientNode = clientNode;
		this.shareId(clientNode);
		clientNode.setPlayer(this);
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public SEntity getEntity(){
		return entity;
	}
	public void setEntity(SEntity entity){
		this.entity = entity;
		this.entity.shareId(this);
	}
}
