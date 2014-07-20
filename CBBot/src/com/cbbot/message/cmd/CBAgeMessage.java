package com.cbbot.message.cmd;

import com.cbbot.CBInfo;
import com.cbbot.message.CBMessage;
import com.cbbot.user.CBGeburtsdatum;
import com.cbbot.user.CBUser;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class CBAgeMessage extends CBMessage{

	public CBAgeMessage(CBInfo info, CBUser user) {
		super(info, user);
		this.sendTutorial();
	}

	public CBAgeMessage(CBInfo info, CBGeburtsdatum bDay, TextMessageEvent e) {
		super(info,e);
		this.addClientToAgeGroup(info, bDay, info.getUserByClientID(e.getInvokerId()));
	}

	private void sendTutorial() {
		String message ="Bitte gebe dein Alter in form deines Geburtstages an:\n"
				+ " Beispiel: [b]!alter 10.11.1991[/b]\n";
		this.sendClientMessage(this.formatMessage("Alter", message));
		
	}
	
	private void addClientToAgeGroup(CBInfo info, CBGeburtsdatum bDay, CBUser user){
		
		int dJahr = bDay.getAge();
		
		if(info.getUserGroupByID(user, info.getPlus18().getGroupID()) == null &&
				info.getUserGroupByID(user, info.getPlus16().getGroupID()) == null && 
				info.getUserGroupByID(user, info.getPlus14().getGroupID()) == null && 
				info.getUserGroupByID(user, info.getPlus12().getGroupID()) == null){
			
			if(dJahr >= 18){
				info.getApi().addClientToServerGroup(info.getPlus18().getGroupID(), user.getClientDatabaseID());
				user.addServerGroup(info.getPlus18());
				user.checkACGR(info);
			}
			else if(dJahr <= 17 && dJahr >= 16){
				info.getApi().addClientToServerGroup(info.getPlus16().getGroupID(), user.getClientDatabaseID());
				user.addServerGroup(info.getPlus16());
				user.checkACGR(info);
			}
			else if(dJahr <= 15 && dJahr >= 14){
				info.getApi().addClientToServerGroup(info.getPlus14().getGroupID(), user.getClientDatabaseID());
				user.addServerGroup(info.getPlus14());
				user.checkACGR(info);
			}
			else{// dJahr < 12!
				info.getApi().addClientToServerGroup(info.getPlus12().getGroupID(), user.getClientDatabaseID());
				user.addServerGroup(info.getPlus12());
				user.checkACGR(info);
			}
		}
		else{
			//Wenn der User bereits ein Alter hat
			//bDay.checkAge(user);
			this.sendClientMessage(this.formatMessage("Alter", "Hey, du versuchst etwas, was ich nicht darf. Wende dich an einen Admin bitte!"));
		}
	}
}
