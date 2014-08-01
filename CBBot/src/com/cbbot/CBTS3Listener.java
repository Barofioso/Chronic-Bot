package com.cbbot;

import java.util.ArrayList;

import com.cbbot.channel.CBChannel;
import com.cbbot.channel.CBUserChannel;
import com.cbbot.event.CBChannelEvent;
import com.cbbot.message.cmd.CBCommand;
import com.cbbot.message.cmd.CBWelcomeMessage;
import com.cbbot.user.CBIp;
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
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerQueryInfo;
/**
 * 
 * Die Listener Klasse
 * Die Events werden hier ausgelöst
 * Implementiert von der TS3 API TS3Listener
 * Der Start für alle Informationen
 *
 * @author D. Lehmann
 * @copyright D. Lehmann
 *
 * @version 1.0
 *
 */
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
		
		user.setIpAdress(new CBIp(info.getApi().getClientInfo(e.getClientId()).getIp(), info, user));
		user.loadUserChannels(info);
		
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
		CBUser user = info.getUserByClientID(e.getClientId());
		if(user != null){
			info.getLog().addLogEntry("[Client before Move] - ChannelID: " + user.getCurrentChannel() + " - ClientNickname: " + user.getClientNickname());
			
			user.setCurrentChannel(this.info.getApi().getClientInfo(e.getClientId()).getChannelId());
			
			this.info.getLog().addLogEntry("[Client after Move] - ChannelID: " + user.getCurrentChannel() + " - ClientNickname: " + user.getClientNickname());
		}
	}

	public void onChannelCreate(ChannelCreateEvent e) {
		//Channel wird erstellt
		CBChannel channel = new CBChannel(info, e.getChannelId());
		//Den ersteller abfragen
		CBUser creator = info.getUserByClientID(e.getInvokerId());
		
		info.getLog().addLogEntry("ChannelID:\t " + e.getChannelId());
		info.getLog().addLogEntry("CreatorID:\t " + e.getInvokerId());
		info.getLog().addLogEntry("Creator Name:\t " + e.getInvokerName());
		
		/*
		 * Wenn der User kein Admin ist, ist es ein Userchannel. Speichere diesen Channel dem User auf seinen Account
		 */
		if(!creator.isAdmin()){
			boolean haveChannel = false;
			//Der User hat einen Channel mit AdminRechten
			ArrayList<CBUserChannel> tmpUChannels = info.getUserChannelsByClientDatabaseID(creator);
			for(CBUserChannel uc : tmpUChannels){
				if(uc.checkOwner(info, creator)){
					haveChannel = true;
				}
			}
			//Wenn der User keinen Channel hat
			if(!haveChannel){
				CBUserChannel userChannel = new CBUserChannel(info, e.getChannelId(), creator);
				CBChannelEvent cEvent = new CBChannelEvent(info, "UserChannel Created");
				
				//Setze den UserChannel als Permanent
				if(cEvent.setChannelPermanent(channel)){
					//Bewege den Channel in den User Bereich
					if(cEvent.moveUserChannel(channel)){
						//Lade den Channel neu
						channel.reloadChannel(info);
						userChannel.reloadChannel(info);
						userChannel.updateUserChannel(info);
						userChannel.updateDB(info);
					}
				}
				else{
					//Channel ist bereits permanent!
				}
				info.getUserChannels().add(userChannel);
				creator.addPrivateChannel(userChannel);
			}
			else{
				//User hat bereits einen Channel¨
				//nachricht du hast einen Channel was auch immer usw.
				info.getApi().moveClient(e.getInvokerId(), info.getDefaultChannel().getChannelDatabaseID());
			}
			
		}
		else{
			ServerQueryInfo sqi = info.getApi().whoAmI();//Der Bot
			if(e.getInvokerUniqueId().trim().equalsIgnoreCase(sqi.getUniqueIdentifier().trim())){
				//Wen den Bot den channel erstellt hat
			}
			else{
				//Irgend ein anderer Admin hat den Channel erstellt
			}
		}
		
		//Füge der Channel liste den Channel hinzu
		if(channel.getChannelID() != 0){
			this.info.getChannels().add(channel);
		}
		
		//Lade alle subchannels beim Channel neu
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
		int channelParentID = 0, pos = 0;
		CBUser user = info.getUserByClientID(e.getInvokerId());
		//Subchannels aktualisieren
		
		//Channel aus der Liste löschen
		for(int i = 0; i < this.info.getChannels().size(); i++){
			if(this.info.getChannels().get(i).getChannelDatabaseID() == e.getChannelId()){
				channelParentID = this.info.getChannels().get(i).getChannelParentID();
				
				this.info.getLog().addLogEntry("Channel Gelöscht: " + this.info.getChannels().get(i).getChannelName());
				pos = i;
				break;
			}
		}
		if(channelParentID != 0){
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
		this.info.getChannels().remove(pos);
		//UserChannel aus der Liste entfernen
		ArrayList<CBUserChannel> uc = info.getUserChannels();
		for(int i = 0; i < uc.size(); i++){
			if(uc.get(i).getChannelDatabaseID() == info.getUserChannelByDatabaseID(e.getChannelId()).getChannelDatabaseID()){
				//Lösche den Ownereintrag aus der Datenbank, damit sich der User wieder einen UserChannel erstellen kann!
				uc.get(i).removeOwner(info, uc.get(i));
				//Lösche den Privatenchannel aus der liste vom User
				user.removePrivateChannel(uc.get(i).getChannelDatabaseID());
				//entferne den Channel aus der Info UserChannel liste
				uc.remove(i);
				break;
			}
		}
	}

	public void onChannelMoved(ChannelMovedEvent e) {
		info.getLog().addLogEntry("[Channel moved event] - Channel ID" + e.getChannelId() + " " + e.getChannelOrder());
	}

	public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
		
	}

}
