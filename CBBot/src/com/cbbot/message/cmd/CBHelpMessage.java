package com.cbbot.message.cmd;

import com.cbbot.CBInfo;
import com.cbbot.message.CBMessage;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class CBHelpMessage extends CBMessage{

	public CBHelpMessage(CBInfo info, TextMessageEvent e, String param) {
		super(info, e);
		
		if(param.contains("myinfo")){
			this.sendMyInfoHelp();
		}
		if(param.contains("error")){
			this.sendErrorHelp();
		}
		if(param.contains("wrongage")){
			this.sendWrongAgeHelp();
		}
	}

	private void sendErrorHelp() {
		this.sendClientMessage(this.formatMessage("Hilfe", "Bitte was? Ich kenne diesen Befehl nicht, versuchs noch einmal"));
	}
	
	private void sendMyInfoHelp() {
		this.sendClientMessage(this.formatMessage("myInfo Hilfe", "Dies ist deine Übersicht. Hier kannst du das Wichtigste über dich nachlesen"));		
	}
	
	private void sendWrongAgeHelp() {
		this.sendClientMessage(this.formatMessage("Alter", "Du hast das Geburtsdatum falsch eingegeben. \n"
				+ "\n"
				+ "[B]Versuche es nocheinmal[/B]\n"
				+ "\n"
				+ "Mögliche eingaben: [B]25.12.1999[/B] oder [B]1999.12.25[/B]"));
	}

}
