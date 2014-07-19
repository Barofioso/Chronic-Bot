package com.cbbot.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.cbbot.CBMySql;
import com.cbbot.gui.CBGui;

public class CBMysqlTest implements ActionListener {

	private CBGui gui;

	public CBMysqlTest(CBGui cbGui) {
		this.gui = cbGui;
	}

	public void actionPerformed(ActionEvent e) {
		CBMySql sql = new CBMySql();
		
		int port = 0; 
		
		if(this.gui.getInputSQLPort().getText().isEmpty()){
			port = 3306;
		}
		else{
			port = Integer.parseInt(this.gui.getInputSQLPort().getText()); 
		}
		sql.setUser(this.gui.getInputSQLUsername().getText());
		sql.setPass(this.gui.getInputSQLPasswort().getText());
		sql.setDatabase(this.gui.getInputSQLDatabase().getText());
		sql.setIp(this.gui.getInputSQLHost().getText());
		sql.setPort(port);
		
		if(sql.open()){
			sql.close();
			JOptionPane.showMessageDialog(null, "Die MySql Verbindung war erfolgreich!");
		}
		else{
			JOptionPane.showMessageDialog(null, "Die MySql Verbindung war nicht erfolgreich!");
		}

	}

}
