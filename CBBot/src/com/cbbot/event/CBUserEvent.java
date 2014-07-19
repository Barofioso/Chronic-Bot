package com.cbbot.event;

import com.cbbot.CBInfo;

public class CBUserEvent extends CBChannelEvent{

	public CBUserEvent(CBInfo info) {
		super(info, "User");
	}

}
