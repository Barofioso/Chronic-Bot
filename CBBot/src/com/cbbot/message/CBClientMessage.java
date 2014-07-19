package com.cbbot.message;

import com.cbbot.CBInfo;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class CBClientMessage extends CBMessage{

	public CBClientMessage(CBInfo info, TextMessageEvent e) {
		super(info, e);
	}

}
