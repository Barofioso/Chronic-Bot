package com.cbbot.log;

import com.cbbot.gui.CBGui;

public class CBLog {
	
	private CBGui gui;

	public CBLog(CBGui gui){
		this.gui = gui;
	}
	
	public void addLogEntry(String message){
		String currentText = this.gui.getTextConsole().getText();
		
		if(currentText.length() > 100000){
			this.gui.getTextConsole().setText(message);
		}
		else{
			if(currentText.length() == 0){
				this.gui.getTextConsole().setText(message);
			}
			else{
				this.gui.getTextConsole().setText(currentText + "\n" + message);
			}
		}
	}
}
