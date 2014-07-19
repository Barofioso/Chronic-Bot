package com.cbbot.channel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cbbot.CBInfo;
import com.cbbot.user.CBUser;

public class CBUserChannel extends CBChannel{
	
	private boolean haveChannelOwner;
	private CBUser owner;
	private ArrayList<CBUser> channelMembers = new ArrayList<CBUser>();
	
	public CBUserChannel(CBInfo info, int channelDatabaseID){
		super(info, channelDatabaseID, true);
		this.checkDB(info);
	}
	/**
	 * Wenn der Client einen neuen Channel aufmacht, soll dieser auch gespeichert werden!
	 * @param bot
	 * @param channelDatabaseID Die Channel TS3 ID
	 * @param owner Der User der den Channel erstellt hat
	 */
	public CBUserChannel(CBInfo info, int channelDatabaseID, CBUser owner){
		super(info, channelDatabaseID,true);
		this.owner = owner;
		this.haveChannelOwner = true;
		this.channelMembers.add(owner);
		this.createDB(info);
		this.updateOwner(info);
	}
	
	private void updateOwner(CBInfo info) {
		info.getSql().open();
		int owner = 0;
		if(this.owner != null){
			owner = 1;
		}
		info.getSql().query("UPDATE chac SET isOwner = " + owner + " WHERE a_ID = " + this.owner.getDbID() + ";");
		info.getSql().close();
	}
	private void checkDB(CBInfo info) {
		info.getSql().open();
		ResultSet res = info.getSql().query("SELECT * FROM chac WHERE channel_ID = " + this.getChannelID() + ";");
		try {
			while(res.next()){
				if(res.getInt("isOwner") == 1){
					this.haveChannelOwner = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	 * Erstellt für jeden channelMember einen eintrag in der Datenbank, sofern dieser nicht existiert!
	 */
	public void createDB(CBInfo info){
		for(int i = 0; i < this.channelMembers.size(); i++){
			if(!this.checkDB(info, this.channelMembers.get(i))){
				info.getSql().open();				
				info.getSql().query("INSERT INTO chac(a_ID,channel_ID) VALUES ("
						+ this.channelMembers.get(i).getDbID() + ", "
						+ this.getChannelID() + ");");
			}
		}
		info.getSql().close();
	}
	public ArrayList<CBUser> getChannelMember() {
		return channelMembers;
	}

	public void setChannelMember(ArrayList<CBUser> channelMember) {
		this.channelMembers = channelMember;
	}

	public CBUser getOwner() {
		return owner;
	}

	public void setOwner(CBUser owner) {
		this.owner = owner;
	}

	public boolean isHaveChannelOwner() {
		return haveChannelOwner;
	}

	public void setHaveChannelOwner(boolean haveChannelOwner) {
		this.haveChannelOwner = haveChannelOwner;
	}

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

}
