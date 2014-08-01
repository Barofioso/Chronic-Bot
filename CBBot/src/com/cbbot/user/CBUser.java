package com.cbbot.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cbbot.CBInfo;
import com.cbbot.channel.CBChannel;
import com.cbbot.channel.CBUserChannel;
import com.cbbot.group.CBGameGroup;
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
	private String clientAwayMessage;
	private int currentChannel;
	private boolean isAdmin;
	private boolean clientInDB = false;
	private ArrayList<CBUserChannel> privateChannels = new ArrayList<CBUserChannel>();
	private ArrayList<CBServerGroup> serverGroups = new ArrayList<CBServerGroup>();
	private CBGeburtsdatum geburtsdatum = null;
	private CBIp ipAdress;
	/**
	 * Erstellt direkt einen kompletten User mit allen Informationen,
	 * es werden die ServerGruppen und die Datenbank ID dazugeladen
	 * @param clientID - TS3 Flüchtige ID
	 */
	public CBUser(CBInfo info, int clientID){
		this.clientID = clientID;
		
		ClientInfo cInfo = info.getApi().getClientInfo(clientID);
		
		this.clientDatabaseID = cInfo.getDatabaseId();
		this.clientUI = cInfo.getUniqueIdentifier();
		this.clientCountry = cInfo.getCountry();
		this.clientNickname = cInfo.getNickname().trim();
		this.clientPlatform = cInfo.getPlatform();
		this.clientVersion = cInfo.getVersion();
		this.clientAwayMessage = cInfo.getAwayMessage();
		this.currentChannel = cInfo.getChannelId();
		
		//this.loadPrivateChannels();//Lade die privaten Channels
		this.loadDBID(info);
		this.loadServerGroups(info);
		this.setACGR(info);
		this.checkBDay(info);
		//Ist der User Admin?
		this.isAdmin = this.checkAdmin(info);
		
		//Game Channel implementierung
		this.addAllGameGroupMembers(info);
		
		//Aktualisiere den User
		this.updateUser(info);
	}
	
	public void addAllGameGroupMembers(CBInfo info){
		for(int i = 0; i < serverGroups.size(); i++){
			if(this.addGameGroupMember(info.getGameGroupByID(serverGroups.get(i).getGroupID()))){
				info.getLog().addLogEntry("[GameGruppe] - User " + this.getClientNickname() + " aktualisiert");
			}
			else{
				info.getLog().addLogEntry("[GameGruppe] - User " + this.getClientNickname() + " bereits dabei");
			}
		}		
	}
	/**
	 * Füge für jede Servergruppe, die eine Spielgruppe ist, diesen Member hinzu, sofern dieser nicht vorhanden ist!
	 * @param info Die Info klasse
	 * @return True falls der User hinzugefügt wurde
	 */
	private boolean addGameGroupMember(CBGameGroup gameGroup){
		if(gameGroup != null){
			if(gameGroup.getMemberByDatabaseID(clientDatabaseID) == null){
				gameGroup.getMembers().add(this);
				return true;
			}
		}
		return false;
	}
	
	private void checkBDay(CBInfo info) {
		info.getSql().open();
		ResultSet res = info.getSql().query("SELECT * FROM account WHERE a_ID = " + this.dbID + ";");
		try {
			while(res.next()){
				if(res.getDate("bDay") != null){
					String bDay = res.getDate("bDay").toString().replace("-", ".").trim();
					info.getSql().close();
					this.geburtsdatum = new CBGeburtsdatum(info, bDay, this);
					//lol
					info.getLog().addLogEntry("Geburtsdatum hinzugefügt: " + this.getGeburtsdatum().getDatum());
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		info.getSql().close();
		
	}
	/**
	 * Prüft ob der User ein Admin ist
	 * @param info 
	 * @return true wenn er die Servergruppe mit der entsprechenden Kategorie hat!
	 */
	private boolean checkAdmin(CBInfo info) {
		for(int i = 0; i < this.getServerGroups().size(); i++){
			for(int j = 0; j < this.getServerGroups().get(i).getKategorien().size(); j++){
				if(this.getServerGroups().get(i).getKategorien().get(j).getkID() == info.getAdminKat().getkID()){
					return true;
				}
			}
		}
		return false;
		
	}
	/**
	 * Ladet die ServerGruppen vom User
	 * @param info 
	 */
	private void loadServerGroups(CBInfo info) {
		TS3Api api = info.getApi();
		List<ServerGroup> serverGroups = api.getServerGroupsByClientId(this.clientDatabaseID);
		for(int i = 0; i < serverGroups.size(); i++){
			for(int j = 0; j < info.getServerGroups().size(); j++){
				if(serverGroups.get(i).getId() == info.getServerGroups().get(j).getGroupID()){
					this.serverGroups.add(info.getServerGroups().get(j));
					break;
				}
			}
		}
	}
	/**
	 * Ladet die ID von der MysqlDatenbank und setzt diese dem USER falls er vorhanden ist
	 * @param info 
	 */
	private void loadDBID(CBInfo info) {
		
		if(!this.clientInDB){
			info.getSql().open();
			ResultSet res = info.getSql().query("SELECT a_ID,clientDatabaseID FROM account WHERE clientDatabaseID = " + this.clientDatabaseID + ";");
			try {
				while(res.next()){
					if(res.getInt("clientDatabaseID") == this.clientDatabaseID){
						this.dbID = res.getInt("a_ID");
						this.clientInDB = true;
						info.getSql().close();
						break;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			info.getSql().close();
		}
		if(!this.clientInDB){
			this.createDB(info);
			this.loadDBID(info);
		}
		
	}
	
	/**
	 * Aktualisiert den User in der Datenbank
	 * @param info 
	 */
	public void updateUser(CBInfo info) {
		info.getSql().open();
		info.getSql().query("UPDATE account SET "
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
				+ "', clientAM = '"
				+ this.clientAwayMessage
				+ "' WHERE a_ID = "
				+ this.dbID
				+ ";");
		info.getSql().close();
		//CBIpAdress ip = new CBIpAdress(this.clientIP);
		//ip.saveToDB(this);
	}

	/**
	 * Erstellt einen neuen Eintrag in der Mysql Tabelle
	 * @param info 
	 */
	private void createDB(CBInfo info) {
		info.getSql().open();
		String query = "INSERT INTO account("
				+ "clientID, "
				+ "clientDatabaseID, "
				+ "clientUI, "
				+ "clientCountry, "
				+ "clientName, "
				+ "clientPlatform, "
				+ "clientVersion, "
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
				+ this.clientAwayMessage
				+ "');";
		info.getSql().query(query);
		
		info.getLog().addLogEntry("[Client Query] " + query);
		
		info.getSql().close();
		
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
	
	public void checkACGR(CBInfo info){
		for(int i = 0; i < this.serverGroups.size(); i++){
			if(!this.checkACGR(info ,this.serverGroups.get(i))){
				this.setACGR(info);
			}
		}
	}
	
	private void setACGR(CBInfo info) {
		
		for(int i = 0; i < this.serverGroups.size(); i++){
			if(!this.checkACGR(info, this.serverGroups.get(i))){
				info.getSql().open();
				String query = "INSERT INTO acgr(a_ID,g_ID) VALUES (" + this.dbID + "," + this.serverGroups.get(i).getGroupDBID() + ");";
				info.getSql().query(query);
				info.getSql().close();
				info.getLog().addLogEntry("[Der Account hat eine neue ServerGruppe erhalten] " + query);
			}
		}
		
	}
	private boolean checkACGR(CBInfo info, CBServerGroup serverGroup){
		info.getSql().open();
		ResultSet res = info.getSql().query("SELECT * FROM acgr WHERE a_ID = " + this.dbID + ";");
		try {
			while(res.next()){
				if(this.dbID == res.getInt("a_ID") && serverGroup.getGroupDBID() == res.getInt("g_ID")){
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
	/**
	 * Ladet alle Userchannel von diesem User
	 * Die Privaten Channel von diesem User werden gesetzt
	 * Dem UserChannel wird der Owner gesetzt falls dieser User der Owner/ersteller des Channels ist
	 * Dem UserChannel wird immer der ChannelAdmin gesetzt 
	 * @param info Die Info klasse mit allen wichtigen Informationen
	 */
	public void loadUserChannels(CBInfo info) {
		TS3Api api = info.getApi();
		
		//Für jeden UserChannel
		for(int i = 0; i < info.getUserChannels().size(); i++){
			//Hole die ChannelGruppenListe des Channels
			List<ChannelGroupClient> channelGroupClients = api.getChannelGroupClientsByChannelId(info.getUserChannels().get(i).getChannelDatabaseID());
			//Für jeden Client in der ChannelGruppe des Channels
			for(int j = 0; j < channelGroupClients.size(); j++){
				//Wenn die TS3DB ID vom Channel Gruppen Client gleich dieser TS3DBID ist
				if(channelGroupClients.get(j).getClientDatabaseId() == this.clientDatabaseID){
					//Wenn dieser User der Ersteller des Channels ist
					if(info.getUserChannels().get(i).checkOwner(info, this)){
						//Setze diesen User als Owner dem UserChannel
						info.getUserChannels().get(i).setOwner(this);
					}
					//Füge den ChannelMember diesen User hinzu
					info.getUserChannels().get(i).getChannelAdmins().add(this);
					//Füge der Person den Privaten Channel hinzu
					this.privateChannels.add(info.getUserChannels().get(i));
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
	/**
	 * @return the ipAdress
	 */
	public CBIp getIpAdress() {
		return ipAdress;
	}
	/**
	 * @param ipAdress the ipAdress to set
	 */
	public void setIpAdress(CBIp ipAdress) {
		this.ipAdress = ipAdress;
	}
	
}
