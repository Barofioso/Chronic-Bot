package com.cbbot.message;

import com.cbbot.CBInfo;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class CBChannelMessage extends CBMessage{

	public CBChannelMessage(CBInfo info, TextMessageEvent e) {
		super(info, e);
	}

}
