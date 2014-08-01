package com.cbbot.event;

import java.util.Timer;
import java.util.TimerTask;

import com.cbbot.CBInfo;
import com.cbbot.channel.CBChannel;

public class CBLobbyTimer extends TimerTask{
	
	private final CBInfo info;
	private CBLobbyEvent lobbyEvent;

	public CBLobbyTimer(CBInfo info, CBLobbyEvent cbLobbyEvent, int min){
		this.info = info;
		this.lobbyEvent = cbLobbyEvent;
		Timer timer = new Timer("Lobby Channel Timer");
		timer.schedule(this, min*60*1000, min*60*1000);
	}

	public void run() {
		//Aktualisiere alle Channel mit den Usern (Info ist user in Channel?)
		for(int i = 0; i < this.info.getUsers().size(); i++){
			this.info.getUsers().get(i).setCurrentChannel(this.info.getApi().getClientInfo(this.info.getUsers().get(i).getClientID()).getChannelId());
		}
		
		int channelWUser = 0;
		for(int i = 0; i < this.info.getLobbyChannel().getSubChannels().size(); i++){
			if(this.info.getLobbyChannel().getSubChannels().get(i).isUserInChannel(info) >= 1){
				channelWUser++;
			}
		}
		
		this.info.getLog().addLogEntry("Grösse Subchannel: " + this.info.getLobbyChannel().getSubChannels().size());
		this.info.getLog().addLogEntry("Besetzte Anzahl Channel: " + channelWUser);
		
		if(this.info.getLobbyChannel().getSubChannels().size() <= channelWUser || this.info.getLobbyChannel().getSubChannels().size() < 4){
			CBChannel channel = new CBChannel(this.info, this.lobbyEvent.createLobbyTalkChannel());
			this.info.getLobbyChannel().addSubChannel(channel);
			this.info.getLog().addLogEntry("Lobby Channel als Subchannel hinzugefügt: " + channel.getChannelName());
		}
		else if(this.info.getLobbyChannel().getSubChannels().size() > (channelWUser + 1) && this.info.getLobbyChannel().getSubChannels().size() > 4){
			if(!(this.info.getLobbyChannel().getSubChannels().get(this.info.getLobbyChannel().getSubChannels().size()-1).isUserInChannel(info) > 0)){
				this.lobbyEvent.removeLobbyChannel(this.info.getLobbyChannel().getSubChannels().get(this.info.getLobbyChannel().getSubChannels().size()-1));
			}
		}
	}
}
