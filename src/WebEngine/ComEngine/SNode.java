package WebEngine.ComEngine;
import java.net.InetAddress;

import GameEngine.SId;
import GameEngine.SIdentifiable;
import GameEngine.SPlayer;
import GameEngine.SPlayer.PlayerState;

public class SNode extends SIdentifiable{
	//////////Communication
	private InetAddress IPAddress;
	//////////Interface

	public enum ConnectionState{
		Connected, NotConnected
	}
	private ConnectionState state = ConnectionState.NotConnected;
	private float ping = 1.0f;
	//////////Game - Client
	private SPlayer player;
	
	public SNode(InetAddress IPAddress, int port, String name, PlayerState playerState){
		super();
		this.IPAddress = IPAddress;
		this.player = new SPlayer(this, name, playerState);
	}
	
	public SNode(InetAddress IPAddress, int port, int id){
		this.Id = new SId(id);
		this.IPAddress = IPAddress;
	}

	public InetAddress getIPAddress() {
		return IPAddress;
	}
	
	public float getPing() {
		return ping;
	}

	public void setPing(float ping) {
		this.ping = ping;
	}
	
	public String getName() {
		return player.getName();
	}

	public void setName(String name) {
		this.player.setName(name);
	}

	public ConnectionState getState() {
		return state;
	}

	public void setState(ConnectionState state) {
		this.state = state;
	}

	public void setPlayer(SPlayer player) {
		this.player = player;
		player.inheritIdFrom(this);
	}
	public SPlayer getPlayer(){
		return player;
	}
}