package com.cbbot.init;

import java.util.List;

import com.cbbot.CBInfo;
import com.cbbot.channel.CBChannel;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;

public class CBLoadChannels extends CBLoad{

	public CBLoadChannels(CBInfo info) {
		this.loadAll(info);
	}

	private void loadAll(CBInfo info) {
		TS3Api api = info.getApi();
		List<Channel> channels = api.getChannels();
		
		//Channels All
		for(int i = 0; i < channels.size(); i++){
			CBChannel tmpChannel = new CBChannel(info, channels.get(i).getId());
			info.getChannels().add(tmpChannel);
			info.getLog().addLogEntry("Channel Name: " + tmpChannel.getChannelName() + " ID: " + tmpChannel.getChannelDatabaseID());
		}
		//SubChannels +
		for(int i = 0; i < info.getChannels().size(); i++){
			info.getChannels().get(i).loadSubChannel();
		}
		for(int i = 0; i < info.getChannels().size(); i++){
			if(info.getChannels().get(i).getChannelName().contains("=====[ Lobby ]=====")){
				info.setLobbyChannel(info.getChannels().get(i));
			}
			if(info.getChannels().get(i).getChannelName().contains("=====[ Games ]=====")){
				info.setGameChannel(info.getChannels().get(i));
			}
			if(info.getChannels().get(i).getChannelName().contains("=====[ User ]=====")){
				info.setUserChannel(info.getChannels().get(i));
			}
			if(info.getChannels().get(i).getChannelName().contains("=====[ AFK ]=====")){
				info.setAfkChannel(info.getChannels().get(i));
			}
		}
	}
}
