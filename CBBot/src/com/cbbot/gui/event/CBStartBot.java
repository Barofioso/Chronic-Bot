package com.cbbot.gui.event;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.cbbot.CBBot;
import com.cbbot.gui.CBGui;

public class CBStartBot implements ActionListener {

	private CBGui gui;

	public CBStartBot(CBGui cbGui) {
		this.gui = cbGui;
	}

	public void actionPerformed(ActionEvent e) {
		new CBBot(this.gui, this.gui.getChckbxDebugToFile().isSelected(),this.gui.getChckbxServerEvent().isSelected(),this.gui.getChckbxChannelEvent().isSelected(),this.gui.getChckbxTextEvent().isSelected());
		this.gui.getLblOffline().setForeground(Color.GREEN);
		this.gui.getLblOffline().setText("Online");
	}
}
