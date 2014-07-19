package com.cbbot.message.cmd;

import com.cbbot.CBInfo;
import com.cbbot.user.CBGeburtsdatum;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class CBCommand {

	public CBCommand(CBInfo info, TextMessageEvent e) {
		this.analyzeMessage(info, e);
	}

	private void analyzeMessage(CBInfo info, TextMessageEvent e) {
		String[] splitMessage = e.getMessage().split(" ");
		
		if((splitMessage[0].equals("!help") || splitMessage[0].equals("!hilfe")) && splitMessage.length < 2){
			new CBCommandList(info,e);
		}
		else if((splitMessage[0].equals("!help") || splitMessage[0].equals("!hilfe")) && splitMessage[1].equalsIgnoreCase("chester")){
			new CBCAutor(info,e);
		}
		else if((splitMessage[0].equals("!help") || splitMessage[0].equals("!hilfe")) && splitMessage[1].equalsIgnoreCase("myinfo")){
			new CBHelpMessage(info, e, splitMessage[1].toString());
		}
		else if(splitMessage[0].equals("!about")){
			new CBCAutor(info,e);
		}
		else if(splitMessage[0].equalsIgnoreCase("!myinfo")){
			new CBCMyInfo(info, e);
		}
		else if(splitMessage[0].equalsIgnoreCase("!alter")){
			
			if(info.getUserByClientID(e.getInvokerId()).getGeburtsdatum() == null && splitMessage.length > 1){
				if((splitMessage[1].length() < 10 || splitMessage[1].length() > 10)){
					new CBHelpMessage(info, e, "wrongage");
				}
				else if(splitMessage[1].length() == 10){
					new CBAgeMessage(info, new CBGeburtsdatum(info, splitMessage[1].toString(), info.getUserByClientID(e.getInvokerId())), e);
				}
			}
			else {
				new CBHelpMessage(info, e, "wrongage");
			}
		}
		else if(splitMessage[0].equalsIgnoreCase("!mann") || splitMessage[0].equalsIgnoreCase("!frau")){
			new CBGenderMessage(info,e,splitMessage[0].toString());
		}
		else{
			new CBHelpMessage(info, e, "error");
		}
	}

}
