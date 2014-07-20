package com.cbbot;

import java.util.logging.Level;

import com.cbbot.event.CBLobbyEvent;
import com.cbbot.gui.CBGui;
import com.cbbot.init.CBLoadChannels;
import com.cbbot.init.CBLoadGroups;
import com.cbbot.init.CBLoadKategorien;
import com.cbbot.init.CBLoadUserChannel;
import com.cbbot.init.CBLoadUsers;
import com.cbbot.log.CBLog;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;

public class CBBot {

	private CBInfo info;
	
	
	public CBBot(CBGui gui, boolean debugToFile, boolean serverEvent, boolean channelEvent, boolean TextEvent){
		
		CBLog log = new CBLog(gui);
		
		CBMySql sql = new CBMySql();
		
		int sqlPort = Integer.parseInt(gui.getInputSQLPort().getText());
		
		sql.setIp(gui.getInputSQLHost().getText());
		sql.setPort(sqlPort);
		sql.setUser(gui.getInputSQLUsername().getText());
		sql.setPass(gui.getInputSQLPasswort().getText());
		sql.setDatabase(gui.getInputSQLDatabase().getText());
		
		final TS3Config config = new TS3Config();
		config.setHost(gui.getTextTs3IP().getText());
		config.setDebugLevel(Level.ALL);
		config.setDebugToFile(debugToFile);
		config.setFloodRate(FloodRate.UNLIMITED);
		config.setLoginCredentials(gui.getInputQueryLoginName().getText(), gui.getInputQueryLoginPasswort().getText());
		
		final TS3Query query = new TS3Query(config);
		query.connect();
		
		TS3Api api = query.getApi();
		api.selectVirtualServerById(1);
		api.setNickname("Chester"); //Station 99 - PutPutBot
		//this.api.sendChannelMessage("PutPutBot is online!");
		
		this.info = new CBInfo(gui, api, log, sql);
		
		new CBLoadKategorien(this.info);
		new CBLoadChannels(this.info);
		new CBLoadGroups(this.info);
		new CBLoadUsers(this.info);
		new CBLoadUserChannel(this.info);
		
		api.moveClient(this.info.getRegelnChannel().getChannelDatabaseID());
		
		//this.api.registerAllEvents();
		api.registerEvent(TS3EventType.CHANNEL,0);
		//this.api.registerEvent(TS3EventType.SERVER);
		api.registerEvent(TS3EventType.TEXT_CHANNEL);
		api.registerEvent(TS3EventType.TEXT_PRIVATE);
		api.registerEvent(TS3EventType.TEXT_SERVER);
		
		api.addTS3Listeners(new CBTS3Listener(this.info));
		
		new CBLobbyEvent(this.info, 5);
	}
	/**
	 * @return the info
	 */
	public CBInfo getInfo() {
		return info;
	}

}
