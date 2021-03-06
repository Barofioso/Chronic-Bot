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
		if(param.contains("hasage")){
			this.sendHasAgeHelp();
		}
	}

	private void sendHasAgeHelp() {
		this.sendClientMessage(this.formatMessage("Alter", "Du hast bereits ein Geburtsdatum eingegeben"));
	}

	private void sendErrorHelp() {
		this.sendClientMessage(this.formatMessage("Hilfe", "Bitte was? Ich kenne diesen Befehl nicht, versuchs noch einmal"));
	}
	
	private void sendMyInfoHelp() {
		this.sendClientMessage(this.formatMessage("myInfo Hilfe", "Dies ist deine �bersicht. Hier kannst du das Wichtigste �ber dich nachlesen"));		
	}
	
	private void sendWrongAgeHelp() {
		this.sendClientMessage(this.formatMessage("Alter", "Du hast das Geburtsdatum falsch eingegeben. \n"
				+ "\n"
				+ "[B]Versuche es nocheinmal[/B]\n"
				+ "\n"
				+ "M�gliche eingaben: [B]25.12.1999[/B] oder [B]1999.12.25[/B]"));
	}

}
