package com.cbbot.message.cmd;

import com.cbbot.CBInfo;
import com.cbbot.message.CBMessage;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class CBCAutor extends CBMessage{

	public CBCAutor(CBInfo info, TextMessageEvent e) {
		super(info, e);
		
		String message = "Ich bin ein Bot, der von Barofioso erstellt wurde. Ich helfe bei vielen Dingen und kann auch dir vielleicht helfen.\n"
				+ "Falls du Fragen oder sonstiges hast, schreib mir eine [B]Private[/B] Nachricht: [B]!hilfe[/B]\n"
				+ "Du bekommt dann eine kleine Übersicht was ich bereits kann.\n"
				+ "\n"
				+ "[B]Bedenke bitte, dass ich immer noch in der Testphase am laufen bin und vielleicht etwas nicht ganz so wie gewüscht ausführe.[/B]\n"
				+ "\n"
				+ "Version: [B]Alpha[/B]\n"
				+ "Author: [B]Barofioso[/B]\n"
				+ "Baro hat bestimmt ein offenes Ohr für neue Möglichkeiten. Wobei eine Liste ihm mehr bringen würde.\n"
				+ "Baro sucht eine Person, die eine Homepage erstellen kann, bzw. Administrieren tut. Alles weitere sagt er, ich weiss von nichts. Der wills mir nicht verraten 8)";
		
		this.sendClientMessage(this.formatMessage("Über Chester", message));
	}

}
