package com.cbbot.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cbbot.CBInfo;
import com.cbbot.channel.CBChannel;
import com.cbbot.channel.CBUserChannel;
import com.cbbot.group.CBServerGroup;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelGroupClient;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;

public class CBUser {
	
	private int dbID;//Meine Datenbank
	private int clientID;//TS3 Datenbank Variabel
	private int clientDatabaseID;//TS3 Datenbank FIX
	private String clientUI;
	private String clientCountry;
	private String clientNickname;
	private String clientPlatform;
	private String clientVersion;
	private String clientIP;
	private String clientAwayMessage;
	private int currentChannel;
	private boolean isAdmin;
	private boolean clientInDB = false;
	private ArrayList<CBUserChannel> privateChannels = new ArrayList<CBUserChannel>();
	private ArrayList<CBServerGroup> serverGroups = new ArrayList<CBServerGroup>();
	private CBInfo info;
	private CBGeburtsdatum geburtsdatum = null;
	/**
	 * Erstellt direkt einen kompletten User mit allen Informationen,
	 * es werden die ServerGruppen und die Datenbank ID dazugeladen
	 * @param clientID - TS3 Flüchtige ID
	 */
	public CBUser(CBInfo info, int clientID){
		this.info = info;
		this.clientID = clientID;
		
		ClientInfo cInfo = this.info.getApi().getClientInfo(clientID);
		
		this.clientDatabaseID = cInfo.getDatabaseId();
		this.clientUI = cInfo.getUniqueIdentifier();
		this.clientCountry = cInfo.getCountry();
		this.clientNickname = cInfo.getNickname().trim();
		this.clientPlatform = cInfo.getPlatform();
		this.clientVersion = cInfo.getVersion();
		this.clientIP = cInfo.getIp();
		this.clientAwayMessage = cInfo.getAwayMessage();
		this.currentChannel = cInfo.getChannelId();
		
		//this.loadPrivateChannels();//Lade die privaten Channels
		this.loadDBID();
		this.loadServerGroups();
		this.setACGR();
		this.checkBDay();
		//Ist der User Admin?
		this.isAdmin = this.checkAdmin();
		this.updateUser();
	}
	private void checkBDay() {
		this.info.getSql().open();
		ResultSet res = this.info.getSql().query("SELECT * FROM account WHERE a_ID = " + this.dbID + ";");
		try {
			while(res.next()){
				if(res.getDate("bDay") != null){
					String bDay = res.getDate("bDay").toString().replace("-", ".").trim();
					this.geburtsdatum = new CBGeburtsdatum(this.info, bDay, this);
					//lol
					this.info.getLog().addLogEntry("Geburtsdatum hinzugefügt: " + this.getGeburtsdatum().getDatum());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * Prüft ob der User ein Admin ist
	 * @return true wenn er die Servergruppe mit der entsprechenden Kategorie hat!
	 */
	private boolean checkAdmin() {
		for(int i = 0; i < this.getServerGroups().size(); i++){
			for(int j = 0; j < this.getServerGroups().get(i).getKategorie().size(); j++){
				if(this.getServerGroups().get(i).getKategorie().get(j).getkID() == this.info.getAdminKat().getkID()){
					return true;
				}
			}
		}
		return false;
		
	}
	/**
	 * Ladet die ServerGruppen vom User
	 */
	private void loadServerGroups() {
		TS3Api api = this.info.getApi();
		List<ServerGroup> serverGroups = api.getServerGroupsByClientId(this.clientDatabaseID);
		for(int i = 0; i < serverGroups.size(); i++){
			for(int j = 0; j < this.info.getServerGroups().size(); j++){
				if(serverGroups.get(i).getId() == this.info.getServerGroups().get(j).getGroupID()){
					this.serverGroups.add(this.info.getServerGroups().get(j));
					break;
				}
			}
		}
	}
	/**
	 * Ladet die ID von der MysqlDatenbank und setzt diese dem USER falls er vorhanden ist
	 */
	private void loadDBID() {
		
		if(!this.clientInDB){
			this.info.getSql().open();
			ResultSet res = this.info.getSql().query("SELECT a_ID,clientDatabaseID FROM account WHERE clientDatabaseID = " + this.clientDatabaseID + ";");
			try {
				while(res.next()){
					if(res.getInt("clientDatabaseID") == this.clientDatabaseID){
						this.dbID = res.getInt("a_ID");
						this.clientInDB = true;
						break;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.info.getSql().close();
		}
		if(!this.clientInDB){
			this.createDB();
			this.loadDBID();
		}
		
	}
	
	/**
	 * Aktualisiert den User in der Datenbank
	 */
	public void updateUser() {
		this.info.getSql().open();
		this.info.getSql().query("UPDATE account SET "
				+ "clientID = "
				+ this.clientID
				+ ", clientCountry = '"
				+ this.clientCountry
				+ "', clientName = '"
				+ this.clientNickname.replace("'", "").replace("´", "").replace("`", "").trim()
				+ "', clientPlatform = '"
				+ this.clientPlatform
				+ "', clientVersion = '"
				+ this.clientVersion
				+ "', clientIP = '"
				+ this.clientIP
				+ "', clientAM = '"
				+ this.clientAwayMessage
				+ "' WHERE a_ID = "
				+ this.dbID
				+ ";");
		this.info.getSql().close();
		//CBIpAdress ip = new CBIpAdress(this.clientIP);
		//ip.saveToDB(this);
	}

	/**
	 * Erstellt einen neuen Eintrag in der Mysql Tabelle
	 */
	private void createDB() {
		this.info.getSql().open();
		String query = "INSERT INTO account("
				+ "clientID, "
				+ "clientDatabaseID, "
				+ "clientUI, "
				+ "clientCountry, "
				+ "clientName, "
				+ "clientPlatform, "
				+ "clientVersion, "
				+ "clientIP, "
				+ "clientAM "
				+ ") VALUES "
				+ "(" 
				+ this.clientID
				+ ", " 
				+ this.clientDatabaseID
				+ ", '"
				+ this.clientUI
				+ "', '"
				+ this.clientCountry
				+ "', '"
				+ this.clientNickname.replace("'", "").replace("´", "").replace("`", "").trim()
				+ "', '"
				+ this.clientPlatform
				+ "', '"
				+ this.clientVersion
				+ "', '"
				+ this.clientIP
				+ "', '"
				+ this.clientAwayMessage
				+ "');";
		this.info.getSql().query(query);
		
		this.info.getLog().addLogEntry("[Client Query] " + query);
		
		this.info.getSql().close();
		
	}
	
	public int getClientID() {
		return clientID;
	}

	public int getClientDatabaseID() {
		return clientDatabaseID;
	}

	public String getClientUI() {
		return clientUI;
	}

	public String getClientCountry() {
		return clientCountry;
	}

	public String getClientNickname() {
		return clientNickname;
	}

	public String getClientPlatform() {
		return clientPlatform;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public String getClientIP() {
		return clientIP;
	}

	public String getClientAwayMessage() {
		return clientAwayMessage;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public void setClientDatabaseID(int clientDatabaseID) {
		this.clientDatabaseID = clientDatabaseID;
	}

	public void setClientUI(String clientUI) {
		this.clientUI = clientUI;
	}

	public void setClientCountry(String clientCountry) {
		this.clientCountry = clientCountry;
	}

	public void setClientNickname(String clientNickname) {
		this.clientNickname = clientNickname;
	}

	public void setClientPlatform(String clientPlatform) {
		this.clientPlatform = clientPlatform;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public void setClientAwayMessage(String clientAwayMessage) {
		this.clientAwayMessage = clientAwayMessage;
	}

	public int getDbID() {
		return dbID;
	}

	public void setDbID(int dbID) {
		this.dbID = dbID;
	}

	public int getCurrentChannel() {
		return currentChannel;
	}

	public void setCurrentChannel(int currentChannel) {
		this.currentChannel = currentChannel;
	}
	
	public ArrayList<CBUserChannel> getPrivateChannels() {
		return privateChannels;
	}

	public void setPrivateChannels(ArrayList<CBUserChannel> privateChannels) {
		this.privateChannels = privateChannels;
	}
	
	public void addPrivateChannel(CBUserChannel channel){
		this.privateChannels.add(channel);
	}
	/**
	 * Entferne einen Privaten Channel
	 * @param channelDatabaseID  Die Teamspeak 3 channel ID
	 */
	public void removePrivateChannel(int channelDatabaseID){
		for(int i = 0; i < this.privateChannels.size(); i++){
			if(channelDatabaseID == this.privateChannels.get(i).getChannelDatabaseID()){
				this.privateChannels.remove(i);
			}
		}
	}
	/**
	 * Entferne einen Privaten Channel
	 * @param channel Die Klasse CBChannel
	 */
	public void removePrivateChannel(CBChannel channel){
		for(int i = 0; i < this.privateChannels.size(); i++){
			if(channel.getChannelDatabaseID() == this.privateChannels.get(i).getChannelDatabaseID()){
				this.privateChannels.remove(i);
			}
		}
	}
	public ArrayList<CBServerGroup> getServerGroups() {
		return serverGroups;
	}
	public void setServerGroups(ArrayList<CBServerGroup> serverGroups) {
		this.serverGroups = serverGroups;
	}
	public void addServerGroup(CBServerGroup serverGroup){
		this.serverGroups.add(serverGroup);
	}
	public void removeServerGroup(CBServerGroup serverGroup){
		for(int i = 0; i < this.serverGroups.size(); i++){
			if(this.serverGroups.get(i).getGroupID() == serverGroup.getGroupID()){
				this.serverGroups.remove(i);
			}
		}
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public void checkACGR(){
		for(int i = 0; i < this.serverGroups.size(); i++){
			if(!this.checkACGR(this.serverGroups.get(i))){
				this.setACGR();
			}
		}
	}
	
	private void setACGR() {
		
		for(int i = 0; i < this.serverGroups.size(); i++){
			if(!this.checkACGR(this.serverGroups.get(i))){
				this.info.getSql().open();
				String query = "INSERT INTO acgr(a_ID,g_ID) VALUES (" + this.dbID + "," + this.serverGroups.get(i).getGroupDBID() + ");";
				this.info.getSql().query(query);
				this.info.getSql().close();
				this.info.getLog().addLogEntry("[Der Account hat eine neue ServerGruppe erhalten] " + query);
			}
		}
		
	}
	private boolean checkACGR(CBServerGroup serverGroup){
		this.info.getSql().open();
		ResultSet res = this.info.getSql().query("SELECT * FROM acgr WHERE a_ID = " + this.dbID + ";");
		try {
			while(res.next()){
				if(this.dbID == res.getInt("a_ID") && serverGroup.getGroupDBID() == res.getInt("g_ID")){
					this.info.getSql().close();
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.info.getSql().close();
		return false;
	}
	public void loadPrivateChannels() {
		TS3Api api = this.info.getApi();
		 
		for(int i = 0; i < info.getUserChannels().size(); i++){
			List<ChannelGroupClient> channelGroupClients = api.getChannelGroupClientsByChannelId(this.info.getUserChannels().get(i).getChannelDatabaseID());
			for(int j = 0; j < channelGroupClients.size(); j++){
				if(channelGroupClients.get(j).getClientDatabaseId() == this.clientDatabaseID){
					this.privateChannels.add(this.info.getUserChannels().get(i));
				}
			}
		}
	}
	public CBGeburtsdatum getGeburtsdatum() {
		return geburtsdatum;
	}
	public void setGeburtsdatum(CBGeburtsdatum geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}
	
}
