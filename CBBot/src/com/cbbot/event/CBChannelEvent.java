package com.cbbot.event;

import java.util.HashMap;

import com.cbbot.CBInfo;
import com.cbbot.channel.CBChannel;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;

public class CBChannelEvent {

	protected final CBInfo info;

	public CBChannelEvent(CBInfo info, String name) {
		this.info = info;
		this.info.getLog().addLogEntry("Channel Event: " + name);
	}
	/**
	 * Aktualisiert den Channelnamen
	 * @param c Der channel
	 * @return true wenn der Channel erfolgreich umbenannt wurde
	 */
	public boolean updateChannel(CBChannel c){
		HashMap<ChannelProperty, String> ch = new HashMap<ChannelProperty, String>();
		ch.put(ChannelProperty.CHANNEL_NAME, c.getChannelName());
		return info.getApi().editChannel(c.getChannelDatabaseID(), ch);
	}
	/**
	 * Verschiebt einen Channel, der von einem User erstellt wurde,
	 * in den Userbereich an die unterste Stelle.
	 * @param c Der Channel
	 * @return true falls der Channel erfolgreich verschoben wurde
	 */
	public boolean moveUserChannel(CBChannel c){
		return info.getApi().moveChannel(c.getChannelDatabaseID(), info.getUserChannel().getChannelDatabaseID(), info.getUserChannels().get(info.getUserChannels().size()-1).getChannelDatabaseID());
	}
	/**
	 * Setzt einen Channel permanent
	 * @param c Der Channel
	 * @return true falls der Channel erfolgreicht als Permanent gesetzt wurde
	 */
	public boolean setChannelPermanent(CBChannel c){
		HashMap<ChannelProperty, String> ch = new HashMap<ChannelProperty, String>();
		ch.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "1");
		return info.getApi().editChannel(c.getChannelDatabaseID(), ch);
	}

}
