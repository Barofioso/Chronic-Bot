package com.cbbot;

import java.util.ArrayList;

import com.cbbot.channel.CBChannel;
import com.cbbot.channel.CBUserChannel;
import com.cbbot.group.CBChannelGroup;
import com.cbbot.group.CBServerGroup;
import com.cbbot.gui.CBGui;
import com.cbbot.kategorie.CBKategorie;
import com.cbbot.log.CBLog;
import com.cbbot.user.CBUser;
import com.github.theholywaffle.teamspeak3.TS3Api;

public class CBInfo {

	private CBGui gui;
	private TS3Api api;
	private CBLog log;
	private CBMySql sql;
	private ArrayList<CBUser> users = new ArrayList<CBUser>();
	private ArrayList<CBKategorie> kategorien = new ArrayList<CBKategorie>();
	private CBKategorie adminKat;
	private CBKategorie normalKat;
	
	private ArrayList<CBServerGroup> serverGroups = new ArrayList<CBServerGroup>();
	private ArrayList<CBChannelGroup> channelGroups = new ArrayList<CBChannelGroup>();
	
	private ArrayList<CBChannel> channels = new ArrayList<CBChannel>();
	private ArrayList<CBUserChannel> userChannels = new ArrayList<CBUserChannel>();
	private CBChannel lobbyChannel;
	private CBChannel gameChannel;
	private CBChannel userChannel;
	private CBChannel afkChannel;
	private CBChannel defaultChannel;
	
	private CBServerGroup weiblich;
	private CBServerGroup mannlich;
	private CBServerGroup plus18;
	private CBServerGroup plus16;
	private CBServerGroup plus14;
	private CBServerGroup plus12;

	public CBInfo(CBGui gui, TS3Api api, CBLog log, CBMySql sql) {
		this.gui = gui;
		this.api = api;
		this.log = log;
		this.sql = sql;
		this.defaultChannel = new CBChannel(this, api.whoAmI().getChannelId());
	}
	
	public ArrayList<CBChannel> getChannels() {
		return channels;
	}

	public CBChannel getLobbyChannel() {
		return lobbyChannel;
	}

	public CBChannel getGameChannel() {
		return gameChannel;
	}

	public CBChannel getUserChannel() {
		return userChannel;
	}

	public CBChannel getAfkChannel() {
		return afkChannel;
	}

	public CBChannel getDefaultChannel() {
		return defaultChannel;
	}

	public void setChannels(ArrayList<CBChannel> channels) {
		this.channels = channels;
	}

	public void setLobbyChannel(CBChannel lobbyChannel) {
		this.lobbyChannel = lobbyChannel;
	}

	public void setGameChannel(CBChannel gameChannel) {
		this.gameChannel = gameChannel;
	}

	public void setUserChannel(CBChannel userChannel) {
		this.userChannel = userChannel;
	}

	public void setAfkChannel(CBChannel afkChannel) {
		this.afkChannel = afkChannel;
	}

	public void setDefaultChannel(CBChannel defaultChannel) {
		this.defaultChannel = defaultChannel;
	}
	
	public CBGui getGui() {
		return gui;
	}

	public ArrayList<CBUser> getUsers() {
		return users;
	}
	
	public CBUser getUserByClientID(int clientID){
		for(int i = 0; i < this.users.size(); i++){
			if(this.users.get(i).getClientID() == clientID){
				return this.users.get(i);
			}
		}
		return null;
	}
	
	public CBUser getUserByClientDatabaseID(int clientDatabaseID){
		for(int i = 0; i < this.users.size(); i++){
			if(this.users.get(i).getClientDatabaseID() == clientDatabaseID){
				return this.users.get(i);
			}
		}
		return null;
	}
	
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

	public void setGui(CBGui gui) {
		this.gui = gui;
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
	 * Sucht die Gruppe vom User
	 * @param user Der User der nach seinen Gruppen geprüft werden soll
	 * @param dbID Die Datenbank ID
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
	 * @param id Die Teamspeak 3 ID
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
	 * @return the api
	 */
	public TS3Api getApi() {
		return api;
	}

	/**
	 * @param api the api to set
	 */
	public void setApi(TS3Api api) {
		this.api = api;
	}

	/**
	 * @return the log
	 */
	public CBLog getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(CBLog log) {
		this.log = log;
	}

	/**
	 * @return the sql
	 */
	public CBMySql getSql() {
		return sql;
	}

	/**
	 * @param sql the sql to set
	 */
	public void setSql(CBMySql sql) {
		this.sql = sql;
	}
}
