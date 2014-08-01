package com.cbbot.event;

import com.cbbot.CBInfo;

public class CBGameEvent extends CBChannelEvent{

	public CBGameEvent(CBInfo info, int min) {
		super(info, "Game");
		new CBGameTimer(info, this, min);
	}

	public void checkGameChannel(CBInfo info, String gameName) {
		info.getG
		
	}
}
