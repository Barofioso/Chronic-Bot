package com.cbbot.message.cmd;

import com.cbbot.CBInfo;
import com.cbbot.user.CBGeburtsdatum;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
/**
 * 
 * Diese Klasse Prüft den Command und erstellt dann das richtige Command Objekt
 *
 * @author D. Lehmann
 * @copyright D. Lehmann
 *
 * @version 0.0
 *
 */
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
			
			if(info.getUserByClientID(e.getInvokerId()).getGeburtsdatum() == null){
				if(splitMessage.length > 1){
					if(!(splitMessage[1].length() >= 8 && splitMessage[1].length() <= 10)){
						//Falsche datumseingabe!
						new CBHelpMessage(info, e, "wrongage"); //10.11.1991 - 9 | 0 - 9 = 10
					}
					else {
						//Neues Geburtsdatum
						new CBAgeMessage(info, new CBGeburtsdatum(info, splitMessage[1].toString(), info.getUserByClientID(e.getInvokerId())), e);
					}
				}
				else{
					//Send Tutorial
					new CBAgeMessage(info, info.getUserByClientID(e.getInvokerId()));
				}
			}
			else {
				//Bereits vorhanden
				new CBHelpMessage(info, e, "hasage");
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
