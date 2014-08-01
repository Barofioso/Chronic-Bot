package com.cbbot.channel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cbbot.CBInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelInfo;
/**
 * 
 * Diese Klasse erstellt einen Channel anhand der Teamspeak 3 Informationen
 * Speichert alle Informationen direkt in der MySql Datenbank
 *
 * @author D. Lehmann
 * @copyright D. Lehmann
 *
 * @version 1.0
 *
 */
public class CBChannel {

	private int channelID; //DB MySql
	private int channelDatabaseID; //Ts3 DB
	private String channelName;
	private String channelDescription;
	private String channelPassword;
	private String channelTopic;
	private int channelOrderID; 
	private int channelParentID;
	private boolean isUserChannel;
	private ArrayList<CBChannel> subChannels = new ArrayList<CBChannel>();
	
	/**
	 * Erstellt einen neuen Channel der kein UserChannel ist
	 * @param bot Die Instanz
	 * @param channelDatabaseID Die Channel TS3 ID
	 */
	public CBChannel(CBInfo info, int channelDatabaseID) {
		this.newCBChannel(info, channelDatabaseID, false);//Unmöglich zu wissen, da die User Channel nach allen Channeln geladen werden! Deshalb false
	}
	/**
	 * Erstellt einen neuen Channel der kein UserChannel ist
	 * @param info Die Info klasse mit allen Informationen
	 * @param channelDatabaseID Die Channel TS3 ID
	 * @param isUserChannel Wenn true, dann werden auch die temporären Channel in der Datenbank gespeichert bzw. von der Datenbank geladen!
	 */
	public CBChannel(CBInfo info, int channelDatabaseID, boolean isUserChannel){
		this.newCBChannel(info, channelDatabaseID, isUserChannel);
	}
	
	/**
	 * Ladet alle Channel Informationen neu
	 */
	public void reloadChannel(CBInfo info){
		ChannelInfo ci = info.getApi().getChannelInfo(this.channelDatabaseID);
		this.channelOrderID = ci.getOrder();
		this.channelParentID = ci.getParentChannelId();
		this.channelDescription = ci.getDescription();
		this.channelName = ci.getName();
		this.channelPassword = ci.getPassword();
		this.channelTopic = ci.getTopic();
	}
	
	private void newCBChannel(CBInfo info, int channelDatabaseID, boolean isUserChannel){
		this.channelDatabaseID = channelDatabaseID;
		ChannelInfo ci = info.getApi().getChannelInfo(channelDatabaseID);
		this.channelDescription = ci.getDescription();
		this.channelName = ci.getName();
		this.channelOrderID = ci.getOrder();
		this.channelParentID = ci.getParentChannelId();
		this.channelPassword = ci.getPassword();
		this.channelTopic = ci.getTopic();
		
		//if(this.bot.getInfo().getUserChannel().getChannelDatabaseID() == this.channelParentID) this.isUserChannel = true; 
		this.isUserChannel = isUserChannel; //Unmöglich zu wissen, da die User Channel nach allen Channeln geladen werden! Deshalb false
		
		if(ci.isPermanent()){
			this.loadChannelID(info);
		}
		if(this.isUserChannel){
			this.loadChannelID(info);
		}
	}

	private void loadChannelID(CBInfo info) {
		boolean channelInDB = false;
		info.getSql().open();
		ResultSet res = info.getSql().query("SELECT channel_ID, channelDatabaseID FROM channel;");
		try {
			while(res.next()){
				if(res.getInt("channelDatabaseID") == this.channelDatabaseID){
					this.channelID = res.getInt("channel_ID");
					res.afterLast();
					info.getSql().close();
					channelInDB = true;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		info.getSql().close();
		//Wenn channel nicht vorhanden ist!
		if(!channelInDB){
			this.createDB(info);
			this.loadChannelID(info);
		}
	}
	
	private void createDB(CBInfo info) {
		info.getSql().open();
		info.getSql().query("INSERT INTO channel("
				+ "channelname,channelDatabaseID, channelDescription, channelPassword, channelTopic, channelOrderID, channelParentID)"
				+ " VALUES ('"
				+ this.channelName.replace("'", "").trim() + "',"
				+ this.channelDatabaseID + ",'"
				+ this.channelDescription.replace("'", "").trim() + "','"
				+ this.channelPassword.trim() + "','"
				+ this.channelTopic.replace("'", "").trim() + "',"
				+ this.channelOrderID + ","
				+ this.channelParentID + ");");
		info.getSql().close();
		
	}
	/**
	 * Aktualisiert diesen Channel in der Datenbank
	 */
	public void updateDB(CBInfo info) {
		info.getSql().open();
		info.getSql().query("UPDATE channel SET "
				+ "channelname = '" + this.channelName.replace("'", "").trim() + "',"
				+ "channelDescription = '" + this.channelDescription.replace("'", "").trim() + "',"
				+ "channelPassword = '" + this.channelPassword + "',"
				+ "channelTopic = '" + this.channelTopic + "',"
				+ "channelOrderID = " + this.channelOrderID + ", "
				+ "channelParentID = " + this.channelParentID + " "
				+ "WHERE channelDatabaseID = " + this.channelDatabaseID + ";");
		info.getSql().close();
	}

	public String getChannelDescription() {
		return channelDescription;
	}

	public String getChannelPassword() {
		return channelPassword;
	}

	public String getChannelTopic() {
		return channelTopic;
	}

	public void setChannelDescription(String channelDescription) {
		this.channelDescription = channelDescription;
	}

	public void setChannelPassword(String channelPassword) {
		this.channelPassword = channelPassword;
	}

	public void setChannelTopic(String channelTopic) {
		this.channelTopic = channelTopic;
	}
	/**
	 * Die MySql channelID
	 * @return channelID von der MySql Datenbank
	 */
	public int getChannelID() {
		return channelID;
	}
	/**
	 * Die Teamspeak 3 Channel ID
	 * @return ChannelID vom Teamspeak 3
	 */
	public int getChannelDatabaseID() {
		return channelDatabaseID;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelID(int channelID) {
		this.channelID = channelID;
	}

	public void setChannelDatabaseID(int channelDatabaseID) {
		this.channelDatabaseID = channelDatabaseID;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getChannelOrderID() {
		return channelOrderID;
	}

	public void setChannelOrderID(int channelOrderID) {
		this.channelOrderID = channelOrderID;
	}

	public int getChannelParentID() {
		return channelParentID;
	}

	public void setChannelParentID(int channelParentID) {
		this.channelParentID = channelParentID;
	}

	public ArrayList<CBChannel> getSubChannels() {
		return subChannels;
	}

	public void setSubChannels(ArrayList<CBChannel> subChannels) {
		this.subChannels = subChannels;
	}
	
	public void addSubChannel(CBChannel channel){
		this.subChannels.add(channel);
	}
	
	public boolean subChannelIsEmpty(){
		return this.subChannels.isEmpty();
	}
	/**
	 * Ladet von diesem Channel all seine Subchannels
	 */
	public void loadSubChannel(CBInfo info){
		for(int i = 0; i < info.getChannels().size(); i++){
			if(this.channelDatabaseID == info.getChannels().get(i).getChannelParentID()){
				this.subChannels.add(info.getChannels().get(i));
			}
		}
	}
	/**
	 * Zählt die User in diesem Channel
	 * @return Die Anzahl User die sich in diesem Channel aufhalten
	 */
	public int isUserInChannel(CBInfo info){
		int counter = 0;
		for(int i = 0; i < info.getUsers().size(); i++){
			if(info.getUsers().get(i).getCurrentChannel() == this.channelDatabaseID){
				counter++;
			}
		}
		return counter;
	}

	public boolean isUserChannel() {
		return isUserChannel;
	}

	public void setUserChannel(boolean isUserChannel) {
		this.isUserChannel = isUserChannel;
	}

}