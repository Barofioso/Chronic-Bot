package com.cbbot.message.cmd;

import com.cbbot.CBInfo;
import com.cbbot.message.CBMessage;
import com.cbbot.user.CBUser;
import com.github.theholywaffle.teamspeak3.api.wrapper.VirtualServerInfo;

public class CBWelcomeMessage extends CBMessage{
	
	public CBWelcomeMessage(CBInfo info, CBUser user) {
		super(info, user);
	}
	
	public void sendClientWelcomeMessage(CBInfo info){
		//int clientID = user.getClientID();
		String clientName = this.getUser().getClientNickname();
		//String country = this.getUser().getClientCountry();
		//String clientIp = this.getUser().getClientIP();
		
		int userTC = info.getApi().getClientInfo(this.getUser().getClientID()).getTotalConnections();
		VirtualServerInfo vsi = info.getApi().getServerInfo();
		float serverTC = vsi.getTotalClientConnections();
		
		float tmpE = ((100/serverTC)*userTC);
		String prozent = "";
		int count = 0; 
		while(count < 1){
			count = (int)(tmpE * 100);
			prozent += 0;
		}
		prozent += "."+count;
		
		
		String message = "\n" +
				"[B]Sei gegrüsst " + clientName + "\n" +
				"Ich bin Chester und wünsche dir einen angenehmen Aufenthalt.[/B] \n\n" +
				"Du möchtest mehr über mich wissen? - Dann schreib mir: [b][COLOR=#000000]!hilfe[/COLOR] [COLOR=#aa5500]chester[/COLOR][/b] \n" +
				"==========[COLOR=#ff0000]==========[/COLOR][COLOR=#dcdc27]==========[/COLOR] \n" +
				"==========[COLOR=#ff0000]==========[/COLOR][COLOR=#dcdc27]==========[/COLOR] \n" +
				"==========[COLOR=#ff0000]==========[/COLOR][COLOR=#dcdc27]==========[/COLOR] \n" +
				"[COLOR=#c75d00][I]Vergiss nicht die [B]Regeln[/B] zu lesen![/I][/COLOR] \n" +
				"Du warst bereits [B]"+ userTC + "[/B] mal auf " + vsi.getName() + ". \n" +
				"Dies sind [B]" + prozent + "%[/B] von [B]" + (int)serverTC + "[/B] totalen Verbindungen. \n" +
				"Es sind zurzeit [B]" + vsi.getClientsOnline() + "[/B] Clients Online \n" +
				"Deine letzte Verbindung war am  " + info.getApi().getClientInfo(this.getUser().getClientID()).getLastConnectedDate().toString() + "\n";
		
		info.getApi().sendPrivateMessage(this.getUser().getClientID(), message);
		info.getLog().addLogEntry("[Nachricht gesendet] " + "Avatar vom User: " + info.getApi().getClientInfo(this.getUser().getClientID()).getAvatar());
	}
}
