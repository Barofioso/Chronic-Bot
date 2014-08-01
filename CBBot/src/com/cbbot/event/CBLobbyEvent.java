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
		
		CBDChannel dChannel = new CBDChannel(info, null, null);
		CBChannel lChannel = info.getLobbyChannel();
		
		dChannel.setPrefix(dChannel.getLastChannelPrefix());
		dChannel.setChannelName(" Talk ");
		
		int lastLobbyChannel = lChannel.getSubChannels().size();
		
		dChannel.setSuffix((lastLobbyChannel + 1) + "");
		
		int channelID = dChannel.createSTNormalChannel(info, lChannel.getSubChannels().get(lastLobbyChannel-1).getChannelDatabaseID() + "", lChannel.getChannelDatabaseID() + "");
		lChannel.getSubChannels().get(lastLobbyChannel-1).setChannelName(dChannel.getMiddleChannelPrefix() + dChannel.getChannelName() + lastLobbyChannel);
		this.updateChannel(lChannel.getSubChannels().get(lastLobbyChannel-1));
		
		info.getLog().addLogEntry("LobbyChannel ID: " + channelID);
		
		return channelID;
	}
	
	public void removeLobbyChannel(CBChannel c){
		CBChannel lChannel = info.getLobbyChannel();
		
		for(int i = 0; i < lChannel.getSubChannels().size(); i++){
			if(lChannel.getSubChannels().get(i).getChannelDatabaseID() == c.getChannelDatabaseID()){
				lChannel.getSubChannels().remove(i);
				break;
			}
		}
		
		if(info.getApi().deleteChannel(c.getChannelDatabaseID())){
			info.getLog().addLogEntry("Lobby Channel gelöscht: " + c.getChannelName());
		}
		else{
			info.getLog().addLogEntry("Lobby Channel NICHT gelöscht: " + c.getChannelName());
		}
		
		CBDChannel dChannel = new CBDChannel(info, null, null);
		lChannel.getSubChannels().get(lChannel.getSubChannels().size()-1).setChannelName(dChannel.getLastChannelPrefix() + " Talk " + (lChannel.getSubChannels().size()) + "");
		this.updateChannel(lChannel.getSubChannels().get(lChannel.getSubChannels().size()-1));
	}
}
