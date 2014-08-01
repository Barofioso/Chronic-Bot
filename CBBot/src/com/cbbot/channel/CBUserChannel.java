package com.cbbot.channel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cbbot.CBInfo;
import com.cbbot.CBMySql;
import com.cbbot.user.CBUser;

public class CBUserChannel extends CBChannel{
	
	/**
	 * Der channel ersteller
	 */
	private CBUser creator;
	/**
	 * Die weiteren Channel admins im Channel
	 */
	private ArrayList<CBUser> channelAdmins = new ArrayList<CBUser>();
	
	/**
	 * Userchannel der bereits erstellt wurde aber kein channelAdmin online ist
	 * Dient zum Laden aller Channel nur am anfang.
	 * @param info Die Info klasse
	 * @param channelDatabaseID Die TS3Channel ID
	 */
	public CBUserChannel(CBInfo info, int channelDatabaseID){
		super(info, channelDatabaseID, true);
		this.creator = null;
	}
	/**
	 * Erstellt einen neuen UserChannel.
	 * Setzt den ersteller.
	 * Fügt der Channel Admin Liste den Ersteller hinzu.
	 * Erstellt einen eintrag in der Datenbank falls dieser nicht vorhanden ist.
	 * Aktualisiert den Ersteller in der Datenbank.
	 * @param info Die Info klasse
	 * @param channelDatabaseID Die Channel TS3 ID
	 * @param owner Der User der den Channel erstellt hat
	 */
	public CBUserChannel(CBInfo info, int channelDatabaseID, CBUser creator){
		super(info, channelDatabaseID, true);
		this.creator = creator;
		this.channelAdmins.add(creator);
		this.createDB(info);
		this.updateOwner(info);
		this.updateUserChannel(info);
	}
	/**
	 * Aktualisiert den Besitzer des Channels
	 * @param info die Info Klasse
	 */
	private void updateOwner(CBInfo info) {
		info.getSql().open();
		int owner = 0;
		if(this.creator != null){
			owner = 1;
		}
		info.getSql().query("UPDATE chac SET isOwner = " + owner + " WHERE a_ID = " + this.creator.getDbID() + ";");
		info.getSql().close();
	}
	/**
	 * Prüft nach einem Eintrag in der Datenbank
	 * @param user Der User der geprüft werden soll
	 * @return true falls dieser User in der Datenbank existiert
	 */
	public boolean checkDB(CBInfo info, CBUser user){
		info.getSql().open();
		ResultSet res = info.getSql().query("SELECT * FROM chac WHERE a_ID = " + user.getDbID() + ";");
		try {
			while(res.next()){
				if(res.getInt("channel_ID") == this.getChannelID()){
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		info.getSql().close();
		return false;
	}
	/**
	 * Erstellt für jeden channelOwner einen eintrag in der Datenbank, sofern dieser nicht existiert!
	 */
	public void createDB(CBInfo info){
		for(int i = 0; i < this.channelAdmins.size(); i++){
			if(!this.checkDB(info, this.channelAdmins.get(i))){
				info.getSql().open();				
				info.getSql().query("INSERT INTO chac(a_ID,channel_ID) VALUES ("
						+ this.channelAdmins.get(i).getDbID() + ", "
						+ this.getChannelID() + ");");
			}
		}
		info.getSql().close();
	}
	/**
	 * Aktualisiert den Channel, setzt die isUserChannel variable direkt dem channel hinzu
	 * @param info die Info Klasse
	 */
	public void updateUserChannel(CBInfo info) {
		this.updateDB(info);
		CBMySql sql = info.getSql();
		sql.open();
		sql.query("UPDATE channel SET isUserChannel = " + this.isUserChannel() + " WHERE channelDatabaseID = " + this.getChannelDatabaseID() + ";");
		sql.close();
	}
	public ArrayList<CBUser> getChannelAdmins() {
		return channelAdmins;
	}

	public void setChannelMember(ArrayList<CBUser> channelMember) {
		this.channelAdmins = channelMember;
	}
	
	public CBUser getChannelMemberByClientID(int clientID){
		for(int i = 0; i < this.channelAdmins.size(); i++){
			if(this.channelAdmins.get(i).getClientID() == clientID){
				return channelAdmins.get(i);
			}
		}
		return null;
	}
	
	public CBUser getChannelMemberByClientDatabaseID(int clientDatabaseID){
		for(int i = 0; i < this.channelAdmins.size(); i++){
			if(this.channelAdmins.get(i).getClientDatabaseID() == clientDatabaseID){
				return channelAdmins.get(i);
			}
		}
		return null;
	}
	
	public CBUser getChannelMemberByClientDatabaseID(CBUser user){
		int clientDatabaseID = user.getClientDatabaseID();
		for(int i = 0; i < this.channelAdmins.size(); i++){
			if(this.channelAdmins.get(i).getClientDatabaseID() == clientDatabaseID){
				return channelAdmins.get(i);
			}
		}
		return null;
	}
	
	public CBUser getChannelMemberByClientID(CBUser user){
		int clientID = user.getClientID();
		for(int i = 0; i < this.channelAdmins.size(); i++){
			if(this.channelAdmins.get(i).getClientID() == clientID){
				return channelAdmins.get(i);
			}
		}
		return null;
	}

	public CBUser getOwner() {
		return creator;
	}

	public void setOwner(CBUser owner) {
		this.creator = owner;
	}
	/**
	 * Hat der Channel überhaupt einen Owner?
	 * @return true wenn creator != null
	 */
	public boolean haveOwner() {
		if(this.creator != null){
			return true;
		}
		return false;
	}
	/**
	 * Prüft in der channelAdminListe ob der übergebene User
	 * der Ersteller/Besitzer von diesem Channel ist
	 * @param info die Info klasse
	 * @param user der zu prüfende User
	 * @return true falls der User der Besitzer dieses Channels ist
	 */
	public boolean checkOwner(CBInfo info, CBUser user) {
		info.getSql().open();
		ResultSet res = info.getSql().query("SELECT * FROM chac WHERE channel_ID = " + this.getChannelID() + ";");
		try {
			while(res.next()){
				if(res.getInt("a_ID") == user.getDbID() && res.getInt("isOwner") == 1){
					info.getSql().close();
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		info.getSql().close();
		return false;
	}
	public void removeOwner(CBInfo info, CBUserChannel channel) {
		CBMySql sql = info.getSql();
		sql.open();
		sql.query("UPDATE chac SET isOwner = 0 WHERE channel_ID = " + channel.getChannelID() + ";");
		sql.close();
		
	}

}
