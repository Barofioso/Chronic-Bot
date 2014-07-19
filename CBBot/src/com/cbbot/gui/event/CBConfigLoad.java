package com.cbbot.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.cbbot.gui.CBGui;

public class CBConfigLoad implements ActionListener {

	private CBGui gui;

	public CBConfigLoad(CBGui cbGui) {
		this.gui = cbGui;
	}

	public void actionPerformed(ActionEvent e) {
		
		File f = new File("config/cbbot.ini");
		
		if(f.exists()){
			FileReader fr;
			try {
				fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String line;
				
				while((line = br.readLine()) != null){
					
					int i = line.indexOf('=');
					String tmp = null;
					
					if(i > 0){
						tmp = line.substring(i).replace("=", "");
					}
					
					if(line.contains("queryLogin") || line.contains("queryTeamspeak3")){
						if(line.contains("Name")){
							this.gui.getInputQueryLoginName().setText(tmp);
						}
						if(line.contains("Pass")){
							this.gui.getInputQueryLoginPasswort().setText(tmp);
						}
						if(line.contains("IP")){
							this.gui.getTextTs3IP().setText(tmp);
						}
					}
					if(line.contains("sql")){
						if(line.contains("Username")){
							this.gui.getInputSQLUsername().setText(tmp);
						}
						if(line.contains("Passwort")){
							this.gui.getInputSQLPasswort().setText(tmp);
						}
						if(line.contains("Database")){
							this.gui.getInputSQLDatabase().setText(tmp);
						}
						if(line.contains("Host")){
							this.gui.getInputSQLHost().setText(tmp);
						}
						if(line.contains("Port")){
							this.gui.getInputSQLPort().setText(tmp);
						}
					}
					if(line.contains("bot")){
						if(line.contains("Debug")){
							if(tmp.contains("true")) {
								this.gui.getChckbxDebugToFile().setSelected(true);
							}
							else {
								this.gui.getChckbxDebugToFile().setSelected(false);
							}
						}
						if(line.contains("Server")){
							if(tmp.contains("true")){
								this.gui.getChckbxServerEvent().setSelected(true);
							}
							else {
								this.gui.getChckbxServerEvent().setSelected(false);
							}
							
						}
						if(line.contains("Channel")){
							if(tmp.contains("true")) {
								this.gui.getChckbxChannelEvent().setSelected(true);
							}
							else {
								this.gui.getChckbxChannelEvent().setSelected(false);
							}
							
						}
						if(line.contains("Text")){
							if(tmp.contains("true")) {
								this.gui.getChckbxTextEvent().setSelected(true);
							}
							else {
								this.gui.getChckbxTextEvent().setSelected(false);
							}
						}
					}
				}
				br.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Die Config Datei existiert noch nicht, bitte zuerst Daten Speichern!");
		}
	}
}
