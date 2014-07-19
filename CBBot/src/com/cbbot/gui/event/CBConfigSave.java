package com.cbbot.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cbbot.gui.CBGui;

public class CBConfigSave implements ActionListener {

	private CBGui gui;

	public CBConfigSave(CBGui cbGui) {
		this.gui = cbGui;
	}

	public void actionPerformed(ActionEvent e) {
		this.checkFile();
	}
	
	private void checkFile(){
		File f = new File("config/cbbot.ini");
		
		if(!f.exists()){
			try {
				f.createNewFile();
				this.checkFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else{
			
			FileWriter fw;
			BufferedWriter out;
			try {
				fw = new FileWriter(f);
				out = new BufferedWriter(fw);
				out.write("====================");
				out.newLine();
				out.write("Query Config ");
				out.newLine();
				out.write("====================");
				out.newLine();
				out.write("queryLoginName=" + this.gui.getInputQueryLoginName().getText().trim());
				out.newLine();
				out.write("queryLoginPass=" + this.gui.getInputQueryLoginPasswort().getText().trim());
				out.newLine();
				out.write("queryTeamspeak3IP=" + this.gui.getTextTs3IP().getText().trim());
				out.newLine();
				out.write("====================");
				out.newLine();
				out.write("SQL Config");
				out.newLine();
				out.write("====================");
				out.newLine();
				out.write("sqlUsername=" + this.gui.getInputSQLUsername().getText().trim());
				out.newLine();
				out.write("sqlPasswort=" + this.gui.getInputSQLPasswort().getText().trim());
				out.newLine();
				out.write("sqlDatabase=" + this.gui.getInputSQLDatabase().getText().trim());
				out.newLine();
				out.write("sqlHost=" + this.gui.getInputSQLHost().getText().trim());
				out.newLine();
				out.write("sqlPort=" + this.gui.getInputSQLPort().getText().trim());
				out.newLine();
				out.write("====================");
				out.newLine();
				out.write("Bot Config");
				out.newLine();
				out.write("====================");
				out.newLine();
				out.write("botDebugToFile=" + this.gui.getChckbxDebugToFile().isSelected());
				out.newLine();
				out.write("botServerEvent=" + this.gui.getChckbxServerEvent().isSelected());
				out.newLine();
				out.write("botChannelEvent=" + this.gui.getChckbxChannelEvent().isSelected());
				out.newLine();
				out.write("botTextEvent=" + this.gui.getChckbxTextEvent().isSelected());
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
