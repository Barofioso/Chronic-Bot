package com.cbbot.gui.event;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.cbbot.CBBot;
import com.cbbot.gui.CBGui;

public class CBStopBot implements ActionListener {

	private CBGui gui;

	public CBStopBot(CBGui cbGui) {
		this.gui = cbGui;
	}

	public void actionPerformed(ActionEvent e) {
		CBBot bot = this.gui.getBot();
		
		bot.getInfo().getQuery().exit();
		this.gui.setBot(null);
		this.gui.getLblOffline().setForeground(Color.RED);
		this.gui.getLblOffline().setText("Offline");
	}

}
