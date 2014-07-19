package com.cbbot.message.cmd;

import com.cbbot.CBInfo;
import com.cbbot.message.CBMessage;
import com.cbbot.user.CBUser;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

public class CBCMyInfo extends CBMessage{
	
	public CBCMyInfo(CBInfo info, TextMessageEvent e) {
		super(info, e);
		this.sendMyInfoToClient(info, info.getUserByClientID(e.getInvokerId()));
	}

	private void sendMyInfoToClient(CBInfo info, CBUser user) {
		ClientInfo cInfo = info.getApi().getClientInfo(user.getClientID());
		
		String message = "[B]Hier erhälst du eine kleine Übersicht[/B]\n"
				+ "Du bist in " + user.getServerGroups().size() + " Servergruppen\n"
				+ "Du hast " + user.getPrivateChannels().size() + " Private Channel\n"
				+ "Du bist bereits seit [B]" + cInfo.getCreatedDate() + "[/B] dabei\n"
				+ "Du benutzt " + cInfo.getPlatform() + "\n"
				+ "Du bist zurzeit in diesem Channel: " + cInfo.getChannelId();

		this.sendClientMessage(this.formatMessage("Dein Account", message));
		
	}

}
