package com.cbbot;

import com.cbbot.channel.CBChannel;
import com.cbbot.channel.CBUserChannel;
import com.cbbot.message.cmd.CBCommand;
import com.cbbot.message.cmd.CBWelcomeMessage;
import com.cbbot.user.CBUser;
import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class CBTS3Listener implements TS3Listener {

	private final CBInfo info;

	public CBTS3Listener(CBInfo info) {
		this.info = info;
	}

	public void onTextMessage(TextMessageEvent e) {
		
		if(e.getMessage().charAt(0) == '!'){
			new CBCommand(this.info, e);
		}
		
		/*if(e.getTargetMode().equals(TextMessageTargetMode.CHANNEL)){
			new CBChannelMessage(this.info, e);
		}
		if(e.getTargetMode().equals(TextMessageTargetMode.CLIENT)){
			new CBClientMessage(this.info, e);
		}
		if(e.getTargetMode().equals(TextMessageTargetMode.SERVER)){
			new CBServerMessage(this.info, e);
		}*/
	}

	public void onClientJoin(ClientJoinEvent e) {
		CBUser user = new CBUser(this.info, e.getClientId());
		new CBWelcomeMessage(this.info, user).sendClientWelcomeMessage(this.info);
		
		/*if(this.bot.getInfo().getUserGroupByID(user, this.bot.getInfo().getMannlich().getGroupID()) == null &&
			this.bot.getInfo().getUserGroupByID(user, this.bot.getInfo().getWeiblich().getGroupID()) == null){
			new CBGenderMessage(this.bot, user);
		}*/
		/*if(this.bot.getInfo().getUserGroupByID(user, this.bot.getInfo().getPlus18().getGroupID()) == null){
			if(this.bot.getInfo().getUserGroupByID(user, this.bot.getInfo().getPlus16().getGroupID()) == null){
				if(this.bot.getInfo().getUserGroupByID(user, this.bot.getInfo().getPlus14().getGroupID()) == null){
					if(this.bot.getInfo().getUserGroupByID(user, this.bot.getInfo().getPlus12().getGroupID()) == null){
						new CBAgeMessage(this.bot, user);
					}
				}
			}
		}*/
		user.loadPrivateChannels(this.info);
		boolean isInList = false;
		for(int i = 0; i < this.info.getUsers().size(); i++){
			if(this.info.getUsers().get(i).getClientDatabaseID() == user.getClientDatabaseID()){
				this.info.getUsers().set(i, user);
				isInList = true;
				break;
			}
		}
		if(!isInList){
			this.info.getUsers().add(user);
		}
		if(user.isAdmin()){
			//new CBWelcomeMessage(this.info, user).sendClientWelcomeMessage(this.info);
		}

	}

	public void onClientLeave(ClientLeaveEvent e) {
		int clientID = e.getClientId();
		for(int i = 0; i < this.info.getUsers().size(); i++){
			if(clientID == this.info.getUsers().get(i).getClientID()){
				this.info.getUsers().remove(i);
			}
		}

	}

	public void onServerEdit(ServerEditedEvent e) {
		

	}

	public void onChannelEdit(ChannelEditedEvent e) {
		

	}

	public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {
		

	}

	public void onClientMoved(ClientMovedEvent e) {
		for(int i = 0; i < this.info.getUsers().size(); i++){
			if(this.info.getUsers().get(i).getClientID() == e.getClientId()){
				this.info.getLog().addLogEntry("[Client before Move] - ChannelID: " + this.info.getUsers().get(i).getCurrentChannel() + " - ClientNickname: " + this.info.getUsers().get(i).getClientNickname());
				this.info.getUsers().get(i).setCurrentChannel(this.info.getApi().getClientInfo(e.getClientId()).getChannelId());
				this.info.getLog().addLogEntry("[Client after Move] - ChannelID: " + this.info.getUsers().get(i).getCurrentChannel() + " - ClientNickname: " + this.info.getUsers().get(i).getClientNickname());
				break;
			}
		}
	}

	public void onChannelCreate(ChannelCreateEvent e) {
		CBChannel channel = new CBChannel(this.info, e.getChannelId());
		
		this.info.getLog().addLogEntry("ChannelID: " + e.getChannelId());
		this.info.getLog().addLogEntry("InvokerID: " + e.getInvokerId());
		this.info.getLog().addLogEntry("InvokerName: " + e.getInvokerName());
		
		if(!this.info.getUserByClientID(e.getInvokerId()).isAdmin()){
			CBUserChannel userChannel = new CBUserChannel(this.info, e.getChannelId(), this.info.getUserByClientID(e.getInvokerId()));
			this.info.getUserByClientID(e.getInvokerId()).addPrivateChannel(userChannel);
		}
		
		this.info.getChannels().add(channel);
		
		if(channel.getChannelParentID() != 0){
			for(int i = 0; i < this.info.getChannels().size(); i++){
				if(this.info.getChannels().get(i).getChannelDatabaseID() == channel.getChannelParentID()){
					this.info.getChannels().get(i).getSubChannels().clear();
					this.info.getChannels().get(i).loadSubChannel(info);
					break;
				}
			}
		}
	}

	public void onChannelDeleted(ChannelDeletedEvent e) {
		this.info.getLog().addLogEntry("[Channel Delete Event] - ChannelID: " + e.getChannelId());
		int channelParentID = 0;
		for(int i = 0; i < this.info.getChannels().size(); i++){
			if(this.info.getChannels().get(i).getChannelID() == e.getChannelId()){
				channelParentID = this.info.getChannels().get(i).getChannelParentID();
				
				this.info.getLog().addLogEntry("Channel Gelöscht: " + this.info.getChannels().get(i).getChannelName());
				this.info.getChannels().remove(i);
				break;
			}
		}
		for(int i = 0; i < this.info.getChannels().size(); i++){
			if(this.info.getChannels().get(i).getChannelDatabaseID() == channelParentID){
				for(int j = 0; j < this.info.getChannels().get(i).getSubChannels().size(); j++){
					if(this.info.getChannels().get(i).getSubChannels().get(j).getChannelDatabaseID() == e.getChannelId()){
						this.info.getChannels().get(i).getSubChannels().remove(j);
						break;
					}
				}
			}
		}
	}

	public void onChannelMoved(ChannelMovedEvent e) {
		
		
	}

	@Override
	public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
		// TODO Auto-generated method stub
		
	}

}
