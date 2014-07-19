package com.cbbot.channel;

import java.util.HashMap;

import com.cbbot.CBInfo;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
/**
 * 
 * Chronic Bot Default Channel Klasse erstellt neue Channel
 * Der ChannelName  sowie prefix und suffix können gesetzt werden.
 * Prefix ist davor, Suffix nach dem ChannelNamen
 *
 * @author D. Lehmann
 * @copyright D. Lehmann
 *
 * @version 1.0
 *
 */
public class CBDChannel {
	
	private char firstChannelPrefix 		= '╔';
	private char middleChannelPrefix 		= '╠';
	private char lastChannelPrefix 			= '╚';
	private String channelName;
	private int type; // 1 = separizor, 2 = center, 3 = default, 4 = normal
	private String prefix = ""; //prefix vor dem Channel
	private String suffix = ""; //suffix nach dem Channel
	private CBInfo info;
	
	public CBDChannel(CBInfo info, String prefix, String suffix){
		this.info = info;
		this.prefix = prefix;
		this.suffix = suffix;
	}
	
	/**
	 * Erstellt anhand der aktuellen Nr einen Separator Channel mit der Standard order 0
	 * @param nr
	 * @return den gerade erstellten Channel als ChannelDatabaseID
	 */
	public int createSeparatorChannel(int nr){
		return this.createSeparatorChannel(nr, "0");
		
	}
	/**
	 * Erstellt anhand der aktuellen Nr und der order einen Separator Channel.
	 * Der order muss ein int in Form eines String sein! - Teamspeak 3 definition ...
	 * @param nr
	 * @param order Sortierung
	 * @return den gerade erstellten Channel als ChannelDatabaseID
	 */
	public int createSeparatorChannel(int nr, String order){
		HashMap<ChannelProperty,String> prop = new HashMap<>();
		prop.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "1");
		prop.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, "0");
		prop.put(ChannelProperty.CHANNEL_FLAG_MAXFAMILYCLIENTS_UNLIMITED, "0");
		prop.put(ChannelProperty.CHANNEL_ORDER, order);
		
		return this.info.getApi().createChannel("[*spacer" + nr + "]" + this.prefix +
				this.channelName + this.suffix, prop);
		
	}
	/**
	 * Erstellt anhand der aktuellen Nr einen zentrierten Channel mit der Standard order 0
	 * @param nr
	 * @return den gerade erstellten Channel als ChannelDatabaseID
	 */
	public int createCenterChannel(int nr){
		return this.createCenterChannel(nr, "0");
	}
	/**
	 * Erstellt anhand der aktuellen Nr einen zentrierten Channel mit der order
	 * @param nr
	 * @param order Sortierung
	 * @return den gerade erstellten Channel als ChannelDatabaseID
	 */
	public int createCenterChannel(int nr, String order){
		HashMap<ChannelProperty,String> prop = new HashMap<>();
		prop.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "1");
		prop.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, "0");
		prop.put(ChannelProperty.CHANNEL_FLAG_MAXFAMILYCLIENTS_UNLIMITED, "0");
		prop.put(ChannelProperty.CHANNEL_ORDER, order);
		
		return this.info.getApi().createChannel("[cspacer" + nr + "]" + this.prefix
				+ this.channelName + this.suffix, prop);
	}
	/**
	 * Erstellt anhand der aktuellen Nr einen Standart Channel
	 * @param nr
	 * @return den gerade erstellten Channel als ChannelDatabaseID
	 */
	public int createDefaultChannel(int nr){
		HashMap<ChannelProperty,String> prop = new HashMap<>();
		prop.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "1");
		prop.put(ChannelProperty.CHANNEL_FLAG_DEFAULT, "1");
		prop.put(ChannelProperty.CHANNEL_FORCED_SILENCE, "99");
		
		return this.info.getApi().createChannel(this.prefix + " " + this.channelName + " " + this.suffix, prop);
	}
	/**
	 * Erstellt einen normalen Channel mit der standart parentID 0 und order 0
	 * @return den gerade erstellten Channel als ChannelDatabaseID
	 */
	public int createNormalChannel(){
		return this.createNormalChannel("0");
	}
	/**
	 * Erstellt einen normalen Channel mit der order 0 und der parentID
	 * @param parentID Ã¼bergeordneter Channel
	 * @return den gerade erstellten Channel als ChannelDatabaseID
	 */
	public int createNormalChannel(String parentID){
		return this.createNormalChannel("0", parentID);
	}
	/**
	 * Erstellt einen normalen Channel mit der order und der parentID
	 * @param order sortierung
	 * @param parentID Ã¼bergeordneter Channel
	 * @return den gerade erstellten Channel als ChannelDatabaseID
	 */
	public int createNormalChannel(String order, String parentID){
		HashMap<ChannelProperty,String> prop = new HashMap<>();
		prop.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "1");
		prop.put(ChannelProperty.CHANNEL_ORDER, order);
		prop.put(ChannelProperty.CPID, parentID);
		
		return this.info.getApi().createChannel(this.prefix + this.channelName + this.suffix, prop);
	}
	
	public int createSTNormalChannel(String order, String parentID){
		HashMap<ChannelProperty,String> prop = new HashMap<>();
		prop.put(ChannelProperty.CHANNEL_FLAG_SEMI_PERMANENT, "1");
		prop.put(ChannelProperty.CHANNEL_ORDER, order);
		prop.put(ChannelProperty.CPID, parentID);
		
		return this.info.getApi().createChannel(this.prefix + this.channelName + this.suffix, prop);
	}

	public String getChannelName() {
		return channelName;
	}

	public int getType() {
		return type;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setPrefix(char c) {
		this.prefix = c +"";
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public char getFirstChannelPrefix() {
		return firstChannelPrefix;
	}

	public char getMiddleChannelPrefix() {
		return middleChannelPrefix;
	}

	public char getLastChannelPrefix() {
		return lastChannelPrefix;
	}
}
