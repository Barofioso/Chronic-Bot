package com.cbbot.group;

import com.cbbot.CBInfo;
import com.cbbot.kategorie.CBKategorie;
import com.github.theholywaffle.teamspeak3.api.PermissionGroupDatabaseType;

public class CBChannelGroup extends CBGroup{
	
	public CBChannelGroup(CBInfo info, CBKategorie kategorie){
		super(info);
	}

	/**
	 * This Method copies the standardChannelGroup and rename it
	 * 
	 * @param name	The Name for your copied ChannelGroup
	 * @return		ID from the copied Channel
	 */
	public int createChannelGroup(String name){
		return  this.getInfo().getApi().copyChannelGroup(166, name, PermissionGroupDatabaseType.REGULAR);
	}

}
