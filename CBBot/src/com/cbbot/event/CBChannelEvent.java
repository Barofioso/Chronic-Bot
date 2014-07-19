package com.cbbot.event;

import java.util.HashMap;

import com.cbbot.CBInfo;
import com.cbbot.channel.CBChannel;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;

public class CBChannelEvent {

	private CBInfo info;
	private String name;

	public CBChannelEvent(CBInfo info, String name) {
		this.info = info;
		this.name = name;
		this.info.getLog().addLogEntry("Channel Event: " + this.name);
	}
	
	public void updateChannel(CBChannel c){
		HashMap<ChannelProperty, String> ch = new HashMap<ChannelProperty, String>();
		ch.put(ChannelProperty.CHANNEL_NAME, c.getChannelName());
		this.info.getApi().editChannel(c.getChannelDatabaseID(), ch);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public CBInfo getInfo(){
		return this.info;
	}

}
