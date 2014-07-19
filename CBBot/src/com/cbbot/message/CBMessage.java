package com.cbbot.message;

import com.cbbot.CBInfo;
import com.cbbot.user.CBUser;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class CBMessage {

	private CBInfo info;
	private TextMessageEvent event;
	private CBUser user;
	
	public CBMessage(CBInfo info, TextMessageEvent e) {
		this.event = e;
		this.info = info;
	}

	public CBMessage(CBInfo info, CBUser user) {
		this.user = user;
		this.info = info;
	}
	/**
	 * Sendet dem User eine Private Nachricht
	 * @param message Die Nachricht die übermittelt werden soll
	 * @return true wenn die Nachricht erfolgreicht abgesendet wurde
	 */
	public boolean sendClientMessage(String message){
		if(this.event != null){
			return this.info.getApi().sendPrivateMessage(this.event.getInvokerId(), message);
		}
		else{
			return this.info.getApi().sendPrivateMessage(this.user.getClientID(), message);
		}
		
	}
	/**
	 * Sendet eine Nachricht dem Channel wo sich der Bot befindet
	 * @param message Die Nachricht die übermittelt werden soll
	 * @return true wenn die Nachricht erfolgreicht abgesendet wurde
	 */
	public boolean sendChannelMessage(String message){
		return this.info.getApi().sendChannelMessage(message);
	}
	/**
	 * Sendet eine Nachricht an den gewünschten Channel
	 * @param channelID Der Channel der die Nachricht erhalten soll
	 * @param message Die Nachricht die übermittelt werden soll
	 * @return true wenn die Nachricht erfolgreich abgesendet wurde
	 */
	public boolean sendChannelMessage(int channelID, String message){
		return this.info.getApi().sendChannelMessage(channelID, message);
	}

	public TextMessageEvent getEvent() {
		return event;
	}

	public void setEvent(TextMessageEvent event) {
		this.event = event;
	}

	public CBUser getUser() {
		return user;
	}

	public void setUser(CBUser user) {
		this.user = user;
	}
	/**
	 * Formatiert die Nachricht nach dem manuell eingestellten Template
	 * @param t Der Titel
	 * @param m Die Nachricht
	 * @return Die formatierte Message
	 */
	public String formatMessage(String t, String m){
		String title = this.formatTitle('=', "[ " + t + " ]");
		String message = "";
		
		message += this.newLine();
		message += this.line('=', title.length());
		message += this.newLine();
		message += title;
		message += this.newLine();
		message += this.line('=', title.length());
		message += this.newLine();
		message += this.newLine();
		message += m;
		message += this.newLine();
		message += this.newLine();
		message += this.line('=', title.length());
		
		return message;
	}
	private String formatTitle(char c,String title){
		String tmp = "";
		int baseLength = 50;
		for(int i = 0; i < baseLength; i++){
			tmp += c;
			if(i == baseLength/2){
				tmp += title;
			}
		}
		return tmp;
		
		
	}
	private String newLine(){
		return "\n";
	}
	private String line(char c, int l){
		String tmp = "";
		for(int i = 0; i < l; i++){
			tmp += c;
		}
		return tmp;
	}
}
