package com.cbbot.log;

import com.cbbot.gui.CBGui;

public class CBLog {
	
	private CBGui gui;

	public CBLog(CBGui gui){
		this.gui = gui;
	}
	
	public void addLogEntry(String message){
		String currentText = this.gui.getTextConsole().getText();
		
		if(currentText.length() == 0){
			this.gui.getTextConsole().setText(message);
		}
		else{
			this.gui.getTextConsole().setText(currentText + "\n" + message);
			//Nach dem Eintrag neu laden damit der Cursor auch zuunterst ist!
			currentText = gui.getTextConsole().getText();
			//Setze den Cursor auf die unterste stelle, autoscroll ;D
			this.gui.getTextConsole().setCaretPosition(currentText.length());
		}
	}
}
