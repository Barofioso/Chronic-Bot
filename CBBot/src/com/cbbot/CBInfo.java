package com.cbbot;

import java.util.ArrayList;

import com.cbbot.channel.CBChannel;
import com.cbbot.channel.CBUserChannel;
import com.cbbot.group.CBChannelGroup;
import com.cbbot.group.CBGameGroup;
import com.cbbot.group.CBServerGroup;
import com.cbbot.gui.CBGui;
import com.cbbot.init.CBLoadChannels;
import com.cbbot.init.CBLoadGroups;
import com.cbbot.init.CBLoadKategorien;
import com.cbbot.init.CBLoadUsers;
import com.cbbot.kategorie.CBKategorie;
import com.cbbot.log.CBLog;
import com.cbbot.user.CBUser;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Query;
/**
 * 
 * Diese Klasse speichert alle Informationen
 *
 * @author D. Lehmann
 * @copyright D. Lehmann
 *
 * @version 1.0
 *
 */
public final class CBInfo {

	private final CBGui gui;
	private TS3Api api;
	private final CBLog log;
	private final CBMySql sql;
	private final TS3Query query;
	
	//Alle Clients
	private ArrayList<CBUser> users = new ArrayList<CBUser>();
	//Alle Kategorien
	private ArrayList<CBKategorie> kategorien = new ArrayList<CBKategorie>();
	
	//Gruppen Kategorien
	private CBKategorie adminKat;
	private CBKategorie normalKat;
	
	//Weitere wichtige Kategorien
	private CBKategorie gameKat;
	private CBKategorie clanKat;
	
	//Gruppen
	private ArrayList<CBServerGroup> serverGroups = new ArrayList<CBServerGroup>();
	private ArrayList<CBChannelGroup> channelGroups = new ArrayList<CBChannelGroup>();
	private ArrayList<CBGameGroup> gameGroups = new ArrayList<CBGameGroup>();
	
	//Alle Channel
	private ArrayList<CBChannel> channels = new ArrayList<CBChannel>();
	//User Channel
	private ArrayList<CBUserChannel> userChannels = new ArrayList<CBUserChannel>();
	
	//Standart Channel
	private CBChannel regelnChannel;
	private CBChannel lobbyChannel;
	private CBChannel gameChannel;
	private CBChannel userChannel;
	private CBChannel afkChannel;
	private final CBChannel defaultChannel;
	
	//Gender
	private CBServerGroup weiblich;
	private CBServerGroup mannlich;
	//Alter
	private CBServerGroup plus18;
	private CBServerGroup plus16;
	private CBServerGroup plus14;
	private CBServerGroup plus12;

	public CBInfo(CBGui gui, TS3Api api, CBLog log, CBMySql sql, TS3Query query) {
		this.gui = gui;
		this.api = api;
		this.log = log;
		this.sql = sql;
		this.query = query;
		this.defaultChannel = new CBChannel(this, api.whoAmI().getChannelId());
		
		new CBLoadKategorien(this);
		new CBLoadChannels(this);
		new CBLoadGroups(this);
		new CBLoadUsers(this);
	}
	/**
	 * Gibt alle Userchannel aus die ein User besitzt
	 * @param clientID die ClientID - TS3 Flüchtige ID
	 * @return die UserChannel wo der User channelAdmin besitzt
	 */
	public ArrayList<CBUserChannel> getUserChannelsByClientID(int clientID){
		ArrayList<CBUserChannel> tmpUChannel = new ArrayList<CBUserChannel>();
		for(int i = 0; i < userChannels.size(); i++){
			if(userChannels.get(i).getChannelMemberByClientID(clientID) != null){
				tmpUChannel.add(userChannels.get(i));
			}
		}
		return tmpUChannel;
	}
	/**
	 * Gibt alle Userchannel aus die ein User besitzt
	 * @param user der User
	 * @return die Userchannel wo der User channelAdmin besitzt
	 */
	public ArrayList<CBUserChannel> getUserChannelsByClientID(CBUser user){
		ArrayList<CBUserChannel> tmpUChannel = new ArrayList<CBUserChannel>();
		for(int i = 0; i < userChannels.size(); i++){
			if(userChannels.get(i).getChannelMemberByClientID(user) != null){
				tmpUChannel.add(userChannels.get(i));
			}
		}
		return tmpUChannel;
	}
	/**
	 * Gibt alle Userchannel aus die ein User besitzt
	 * @param clientDatabaseID die ClientDatabaseID - TS3 Flüchtige ID
	 * @return die UserChannel wo der User channelAdmin besitzt
	 */
	public ArrayList<CBUserChannel> getUserChannelsByClientDatabseID(int clientDatabaseID){
		ArrayList<CBUserChannel> tmpUChannel = new ArrayList<CBUserChannel>();
		for(int i = 0; i < userChannels.size(); i++){
			if(userChannels.get(i).getChannelMemberByClientDatabaseID(clientDatabaseID) != null){
				tmpUChannel.add(userChannels.get(i));
			}
		}
		return tmpUChannel;
	}
	/**
	 * Gibt alle Userchannel aus die ein User besitzt
	 * @param user der User
	 * @return die Userchannel, wo der User channelAdmin ist
	 */
	public ArrayList<CBUserChannel> getUserChannelsByClientDatabaseID(CBUser user){
		ArrayList<CBUserChannel> tmpUChannel = new ArrayList<CBUserChannel>();
		for(int i = 0; i < userChannels.size(); i++){
			if(userChannels.get(i).getChannelMemberByClientDatabaseID(user) != null){
				tmpUChannel.add(userChannels.get(i));
			}
		}
		return tmpUChannel;
	}
	/**
	 * @return Alle Channels
	 */
	public ArrayList<CBChannel> getChannels() {
		return channels;
	}
	/**
	 * @return Den LobbyChannel
	 */
	public CBChannel getLobbyChannel() {
		return lobbyChannel;
	}
	/**
	 * @return Den GameChannel
	 */
	public CBChannel getGameChannel() {
		return gameChannel;
	}
	/**
	 * @return den UserChannel
	 */
	public CBChannel getUserChannel() {
		return userChannel;
	}
	/**
	 * @return den AFK Channel
	 */
	public CBChannel getAfkChannel() {
		return afkChannel;
	}
	/**
	 * @return den StandartChannel
	 */
	public CBChannel getDefaultChannel() {
		return defaultChannel;
	}
	/**
	 * @param channels setzt die Channelliste
	 */
	public void setChannels(ArrayList<CBChannel> channels) {
		this.channels = channels;
	}
	/**
	 * @param lobbyChannel setzt den LobbyChannel 
	 */
	public void setLobbyChannel(CBChannel lobbyChannel) {
		this.lobbyChannel = lobbyChannel;
	}
	/**
	 * @param gameChannel setzt den GameChannel 
	 */
	public void setGameChannel(CBChannel gameChannel) {
		this.gameChannel = gameChannel;
	}
	/**
	 * @param userChannel setzt den UserChannel 
	 */
	public void setUserChannel(CBChannel userChannel) {
		this.userChannel = userChannel;
	}
	/**
	 * @param afkChannel setzt den AFK Channel 
	 */
	public void setAfkChannel(CBChannel afkChannel) {
		this.afkChannel = afkChannel;
	}
	/**
	 * @return Die Grafische benutzeroberfläche
	 */
	public CBGui getGui() {
		return gui;
	}
	/**
	 * @return Die UserListe
	 */
	public ArrayList<CBUser> getUsers() {
		return users;
	}
	/**
	 * Sucht den User
	 * @param clientID Die User Client ID
	 * @return Falls vorhanden, den User, ansonsten null
	 */
	public CBUser getUserByClientID(int clientID){
		for(int i = 0; i < this.users.size(); i++){
			if(this.users.get(i).getClientID() == clientID){
				return this.users.get(i);
			}
		}
		return null;
	}
	/**
	 * Sucht den User
	 * @param clientDatabaseID Die TS3 User DB ID
	 * @return Falls vorhanden, den User, ansonsten null
	 */
	public CBUser getUserByClientDatabaseID(int clientDatabaseID){
		for(int i = 0; i < this.users.size(); i++){
			if(this.users.get(i).getClientDatabaseID() == clientDatabaseID){
				return this.users.get(i);
			}
		}
		return null;
	}
	/**
	 * Sucht den User
	 * @param dbID Die MySql DB ID vom User
	 * @return Falls vorhanden, den User, ansonsten null
	 */
	public CBUser getUserByDBID(int dbID){
		for(int i = 0; i < this.users.size(); i++){
			if(this.users.get(i).getDbID() == dbID){
				return this.users.get(i);
			}
		}
		return null;
	}

	public ArrayList<CBKategorie> getKategorien() {
		return kategorien;
	}

	public void setUsers(ArrayList<CBUser> users) {
		this.users = users;
	}

	public void setKategorien(ArrayList<CBKategorie> kategorien) {
		this.kategorien = kategorien;
	}

	public ArrayList<CBServerGroup> getServerGroups() {
		return serverGroups;
	}

	public void setServerGroups(ArrayList<CBServerGroup> serverGroups) {
		this.serverGroups = serverGroups;
	}

	public ArrayList<CBChannelGroup> getChannelGroups() {
		return channelGroups;
	}

	public void setChannelGroups(ArrayList<CBChannelGroup> channelGroups) {
		this.channelGroups = channelGroups;
	}

	public CBKategorie getAdminKat() {
		return adminKat;
	}

	public void setAdminKat(CBKategorie adminKat) {
		this.adminKat = adminKat;
	}

	public CBKategorie getNormalKat() {
		return normalKat;
	}

	public void setNormalKat(CBKategorie normalKat) {
		this.normalKat = normalKat;
	}

	public ArrayList<CBUserChannel> getUserChannels() {
		return userChannels;
	}

	public void setUserChannels(ArrayList<CBUserChannel> userChannels) {
		this.userChannels = userChannels;
	}

	public CBServerGroup getWeiblich() {
		return weiblich;
	}

	public CBServerGroup getMannlich() {
		return mannlich;
	}

	public CBServerGroup getPlus18() {
		return plus18;
	}

	public CBServerGroup getPlus16() {
		return plus16;
	}

	public CBServerGroup getPlus14() {
		return plus14;
	}

	public CBServerGroup getPlus12() {
		return plus12;
	}

	public void setWeiblich(CBServerGroup weiblich) {
		this.weiblich = weiblich;
	}

	public void setMannlich(CBServerGroup mannlich) {
		this.mannlich = mannlich;
	}

	public void setPlus18(CBServerGroup plus18) {
		this.plus18 = plus18;
	}

	public void setPlus16(CBServerGroup plus16) {
		this.plus16 = plus16;
	}

	public void setPlus14(CBServerGroup plus14) {
		this.plus14 = plus14;
	}

	public void setPlus12(CBServerGroup plus12) {
		this.plus12 = plus12;
	}
	/**
	 * Sucht den UserChannel anhand der TS3 ID
	 * @param channelDatabaseID der Channel der gesucht werden soll
	 * @return Falls vorhanden, den Channel, ansonsten null
	 */
	public CBUserChannel getUserChannelByDatabaseID(int channelDatabaseID){
		for(int i = 0; i < this.userChannels.size(); i++){
			if(this.userChannels.get(i).getChannelDatabaseID() == channelDatabaseID){
				return this.userChannels.get(i);
			}
		}
		return null;
	}
	/**
	 * Sucht den UserChannel anhand der TS3 ID
	 * @param channel Der UserChannel der gesucht werden soll
	 * @return Falls vorhanden, den UserChannel, ansonsten null
	 */
	public CBUserChannel getUserChannelByDatabaseID(CBUserChannel channel){
		int channelDatabaseID = channel.getChannelDatabaseID();
		for(int i = 0; i < this.userChannels.size(); i++){
			if(this.userChannels.get(i).getChannelDatabaseID() == channelDatabaseID){
				return this.userChannels.get(i);
			}
		}
		return null;
	}
	/**
	 * Sucht den UserChannel anhand der MySql DB ID
	 * @param channelID der Channel der gesucht werden soll
	 * @return Falls vorhanden, den Channel, ansonsten null
	 */
	public CBUserChannel getUserChannelByID(int channelID){
		for(int i = 0; i < this.userChannels.size(); i++){
			if(this.userChannels.get(i).getChannelID() == channelID){
				return this.userChannels.get(i);
			}
		}
		return null;
	}
	/**
	 * Sucht den UserChannel anhand der MySql DB ID
	 * @param userChannel Der Userchannel der Gesucht wird
	 * @return Falls vorhanden, den Channel, ansonsten null
	 */
	public CBUserChannel getUserChannelByID(CBUserChannel userChannel){
		int channelID = userChannel.getChannelID();
		for(int i = 0; i < this.userChannels.size(); i++){
			if(this.userChannels.get(i).getChannelID() == channelID){
				return this.userChannels.get(i);
			}
		}
		return null;
	}
	/**
	 * Sucht den Channel
	 * @param channelID der Channel der gesucht werden soll
	 * @return Falls vorhanden, den Channel, ansonsten null
	 */
	public CBChannel getChannelByID(int channelID){
		for(int i = 0; i < this.channels.size(); i++){
			if(this.channels.get(i).getChannelID() == channelID){
				return this.channels.get(i);
			}
		}
		return null;
	}
	/**
	 * Sucht den Channel
	 * @param channel der Channel der Gesucht werden soll
	 * @return Falls vorhanden, den Channel, ansonsten null
	 */
	public CBChannel getChannelByID(CBChannel channel){
		int channelID = channel.getChannelID();
		for(int i = 0; i < this.channels.size(); i++){
			if(this.channels.get(i).getChannelID() == channelID){
				return this.channels.get(i);
			}
		}
		return null;
	}
	/**
	 * Entfernt einen Channel
	 * @param channelID der Channel der entfernt werden soll
	 * @return true falls der gesuchte Channel entfernt wurde
	 */
	public boolean removeChannelByID(int channelID){
		for(int i = 0; i < this.channels.size(); i++){
			if(this.channels.get(i).getChannelID() == channelID){
				this.channels.remove(i);
				return true;
			}
		}
		return false;
	}
	/**
	 * Entfernt einen Channel
	 * @param channel der Channel der entfernt werden soll
	 * @return true falls der gesuchte Channel entfernt wurde
	 */
	public boolean removeChannelByID(CBChannel channel){
		int channelID = channel.getChannelID();
		for(int i = 0; i < this.channels.size(); i++){
			if(this.channels.get(i).getChannelID() == channelID){
				this.channels.remove(i);
				return true;
			}
		}
		return false;
	}
	/**
	 * Sucht die Gruppe vom User
	 * @param user Der User der nach seinen Gruppen geprüft werden soll
	 * @param dbID Die Datenbank Gruppen ID
	 * @return Falls vorhanden, die gesuchte Gruppe, ansonsten null
	 */
	public CBServerGroup getUserGroupByDBID(CBUser user, int dbID){
		for(int i = 0; i < user.getServerGroups().size(); i++){
			if(user.getServerGroups().get(i).getGroupDBID() == dbID){
				return user.getServerGroups().get(i);
			}
		}
		return null;
	}
	/**
	 * Sucht die Gruppe vom User 
	 * @param user Der User der nach seinen Gruppen geprüft werden soll
	 * @param id Die Teamspeak 3 Gruppen ID
	 * @return Falls vorhanden, die gesuchte Gruppe, ansonsten null
	 */
	public CBServerGroup getUserGroupByID(CBUser user, int id){
		for(int i = 0; i < user.getServerGroups().size(); i++){
			if(user.getServerGroups().get(i).getGroupID() == id){
				return user.getServerGroups().get(i);
			}
		}
		return null;
	}
	/**
	 * Sucht die Gruppe vom User
	 * @param user Der User der nach seinen Gruppen geprüft werden soll
	 * @param serverGroupID Die serverGruppe
	 * @return Falls vorhanden, die gesuchte Gruppe, ansonsten null
	 */
	public CBServerGroup getUserGroupByID(CBUser user, CBServerGroup serverGroupID){
		for(int i = 0; i < user.getServerGroups().size(); i++){
			if(user.getServerGroups().get(i).getGroupID() == serverGroupID.getGroupID()){
				return user.getServerGroups().get(i);
			}
		}
		return null;
	}
	/**
	 * Sucht die Gruppe vom User
	 * @param user Der User der nach seinen Gruppen geprüft werden soll
	 * @param serverGroupDBID Die ServerGruppe
	 * @return Falls vorhanden, die gesuchte Gruppe, ansonsten null
	 */
	public CBServerGroup getUserGroupByDBID(CBUser user, CBServerGroup serverGroupDBID){
		for(int i = 0; i < user.getServerGroups().size(); i++){
			if(user.getServerGroups().get(i).getGroupDBID() == serverGroupDBID.getGroupDBID()){
				return user.getServerGroups().get(i);
			}
		}
		return null;
	}
	public CBGameGroup getGameGroupByID(int groupID){
		for(int i = 0; i < gameGroups.size(); i++){
			if(gameGroups.get(i).getGroupID() == groupID){
				return gameGroups.get(i);
			}
		}
		return null;
	}
	public CBGameGroup getGameGroupByID(CBGameGroup gameGroup){
		int groupID = gameGroup.getGroupID();
		for(int i = 0; i < gameGroups.size(); i++){
			if(gameGroups.get(i).getGroupID() == groupID){
				return gameGroups.get(i);
			}
		}
		return null;
	}
	/**
	 * @return the api
	 */
	public TS3Api getApi() {
		return api;
	}
	/**
	 * @return the log
	 */
	public CBLog getLog() {
		return log;
	}
	/**
	 * @return the sql
	 */
	public CBMySql getSql() {
		return sql;
	}
	/**
	 * @return the regelnChannel
	 */
	public CBChannel getRegelnChannel() {
		return regelnChannel;
	}
	/**
	 * @param regelnChannel the regelnChannel to set
	 */
	public void setRegelnChannel(CBChannel regelnChannel) {
		this.regelnChannel = regelnChannel;
	}

	/**
	 * @return the query
	 */
	public TS3Query getQuery() {
		return query;
	}
	/**
	 * @return the gameKat
	 */
	public CBKategorie getGameKat() {
		return gameKat;
	}
	/**
	 * @param gameKat the gameKat to set
	 */
	public void setGameKat(CBKategorie gameKat) {
		this.gameKat = gameKat;
	}
	/**
	 * @return the gameGroups
	 */
	public ArrayList<CBGameGroup> getGameGroups() {
		return gameGroups;
	}
	/**
	 * @param gameGroups the gameGroups to set
	 */
	public void setGameGroups(ArrayList<CBGameGroup> gameGroups) {
		this.gameGroups = gameGroups;
	}
	/**
	 * @return the clanKat
	 */
	public CBKategorie getClanKat() {
		return clanKat;
	}
	/**
	 * @param clanKat the clanKat to set
	 */
	public void setClanKat(CBKategorie clanKat) {
		this.clanKat = clanKat;
	}
}
