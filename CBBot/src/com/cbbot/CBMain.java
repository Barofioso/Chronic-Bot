package com.cbbot;

import java.awt.EventQueue;

import com.cbbot.gui.CBGui;

public class CBMain {
	
	public static void main(String[] args) {
		System.setProperty("sun.jnu.encoding", "UTF-8");
		System.setProperty("file.encoding", "UTF-8");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CBGui frame = new CBGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
