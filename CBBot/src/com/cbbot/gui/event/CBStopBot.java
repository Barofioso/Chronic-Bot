package com.cbbot.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.cbbot.gui.CBGui;

public class CBStopBot implements ActionListener {

	private CBGui gui;

	public CBStopBot(CBGui cbGui) {
		this.gui = cbGui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
