package com.cbbot.event;

import com.cbbot.CBInfo;
import com.cbbot.channel.CBChannel;
import com.cbbot.channel.CBDChannel;

public class CBLobbyEvent extends CBChannelEvent{


	public CBLobbyEvent(CBInfo info, int min){
		super(info, "Lobby");
		new CBLobbyTimer(info, this, min);
	}
	
	public int createLobbyTalkChannel(){
		CBDChannel dChannel = new CBDChannel(this.getInfo(), null, null);
		
		dChannel.setPrefix(dChannel.getLastChannelPrefix());
		dChannel.setChannelName(" Talk ");
		
		int lastLobbyChannel = this.getInfo().getLobbyChannel().getSubChannels().size();
		
		dChannel.setSuffix((lastLobbyChannel + 1) + "");
		
		int channelID = dChannel.createSTNormalChannel(this.getInfo().getLobbyChannel().getSubChannels().get(lastLobbyChannel-1).getChannelDatabaseID() + "", this.getInfo().getLobbyChannel().getChannelDatabaseID() + "");
		this.getInfo().getLobbyChannel().getSubChannels().get(lastLobbyChannel-1).setChannelName(dChannel.getMiddleChannelPrefix() + dChannel.getChannelName() + lastLobbyChannel);
		this.updateChannel(this.getInfo().getLobbyChannel().getSubChannels().get(lastLobbyChannel-1));
		
		this.getInfo().getLog().addLogEntry("LobbyChannel ID: " + channelID);
		
		return channelID;
	}
	
	public void removeLobbyChannel(CBChannel c){
		
		for(int i = 0; i < this.getInfo().getLobbyChannel().getSubChannels().size(); i++){
			if(this.getInfo().getLobbyChannel().getSubChannels().get(i).getChannelDatabaseID() == c.getChannelDatabaseID()){
				this.getInfo().getLobbyChannel().getSubChannels().remove(i);
			}
		}
		this.getInfo().getLog().addLogEntry("Lobby Channel gelöscht: " + c.getChannelName());
		this.getInfo().getApi().deleteChannel(c.getChannelDatabaseID());
		
		CBDChannel dChannel = new CBDChannel(this.getInfo(), null, null);
		this.getInfo().getLobbyChannel().getSubChannels().get(this.getInfo().getLobbyChannel().getSubChannels().size()-1).setChannelName(dChannel.getLastChannelPrefix() + " Talk " + (this.getInfo().getLobbyChannel().getSubChannels().size()) + "");
		this.updateChannel(this.getInfo().getLobbyChannel().getSubChannels().get(this.getInfo().getLobbyChannel().getSubChannels().size()-1));
	}
}
