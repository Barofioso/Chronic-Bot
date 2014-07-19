package com.cbbot.message.cmd;

import com.cbbot.CBInfo;
import com.cbbot.message.CBMessage;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class CBCommandList extends CBMessage{

	public CBCommandList(CBInfo info, TextMessageEvent e) {
		super(info, e);
		this.sendClientCommandList();
	}

	private void sendClientCommandList() {		
		this.sendClientMessage(this.formatMessage("Command List", "Die Liste zeigt dir meine Befehle an. Alle Befehle die hier stehen kannst du ausführen\n"
				+ " - [B]!help[/B] --> [COLOR=#aa5500]Zeigt dir diese Übersicht an.[/COLOR]\n"
				+ " - [B]!about[/B] --> [COLOR=#aa5500]Informationen über den Bot.[/COLOR]\n" 
				+ " - [B]!myinfo[/B] --> [COLOR=#aa5500]Zeigt dir deine Informationen an.[/COLOR]\n" 
				+ " - [B]!admins[/B] --> [COLOR=#aa5500]Zurzeit noch nicht implementiert.[/COLOR]\n"
				+ " - [B]!mann[/B] oder [B]!frau[/B] --> [COLOR=#aa5500]Du bekommst damit ein Männlich oder Weiblich Zeichen.[/COLOR]\n"
				+ " - [B]!alter[/B] --> [COLOR=#aa5500]Du bekommst damit ein Zeichen für dein jetziges Alter. Bitte dein Geburtsdatum eingeben.[/COLOR]\n"));
				
	}

}
