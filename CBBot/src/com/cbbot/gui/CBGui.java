package com.cbbot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import com.cbbot.gui.event.CBConfigLoad;
import com.cbbot.gui.event.CBConfigSave;
import com.cbbot.gui.event.CBExit;
import com.cbbot.gui.event.CBMysqlTest;
import com.cbbot.gui.event.CBStartBot;
import com.cbbot.gui.event.CBStopBot;

public class CBGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1219835553415765439L;
	private JPanel contentPane;
	private JTextField inputSQLUsername;
	private JTextField inputSQLPasswort;
	private JTextField inputSQLDatabase;
	private JTextField inputSQLHost;
	private JTextField inputSQLPort;
	private JTextField inputQueryLoginName;
	private JTextField inputQueryLoginPasswort;
	private JTextPane textConsole;
	private JLabel lblVerbundeneUserNR;
	private JLabel lblAnzahlChannelsNR;
	private JCheckBox chckbxDebugToFile;
	private JCheckBox chckbxChannelEvent;
	private JCheckBox chckbxServerEvent;
	private JCheckBox chckbxTextEvent;
	private JLabel lblOffline;
	private JTextField textTs3IP;

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
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
	}*/

	/**
	 * Create the frame.
	 */
	public CBGui() {
		setTitle("Bot by Barofioso");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 324);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
		JMenuItem mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(new CBExit());
		mnDatei.add(mntmBeenden);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);
		tabbedPane.addTab("Console", null, scrollPane, null);
		tabbedPane.setEnabledAt(0, true);
		
		textConsole = new JTextPane();
		textConsole.setForeground(Color.BLACK);
		textConsole.setDropMode(DropMode.INSERT);
		textConsole.setEditable(false);
		scrollPane.setViewportView(textConsole);
		
		JPanel panelBotInfo = new JPanel();
		tabbedPane.addTab("Bot Info", null, panelBotInfo, null);
		SpringLayout sl_panelBotInfo = new SpringLayout();
		panelBotInfo.setLayout(sl_panelBotInfo);
		
		JLabel lblVerbundeneUser = new JLabel("Verbundene User:");
		sl_panelBotInfo.putConstraint(SpringLayout.NORTH, lblVerbundeneUser, 10, SpringLayout.NORTH, panelBotInfo);
		sl_panelBotInfo.putConstraint(SpringLayout.WEST, lblVerbundeneUser, 10, SpringLayout.WEST, panelBotInfo);
		panelBotInfo.add(lblVerbundeneUser);
		
		lblVerbundeneUserNR = new JLabel("0");
		sl_panelBotInfo.putConstraint(SpringLayout.NORTH, lblVerbundeneUserNR, 0, SpringLayout.NORTH, lblVerbundeneUser);
		sl_panelBotInfo.putConstraint(SpringLayout.WEST, lblVerbundeneUserNR, 6, SpringLayout.EAST, lblVerbundeneUser);
		panelBotInfo.add(lblVerbundeneUserNR);
		
		JLabel lblAnzahlChannels = new JLabel("Anzahl Channels:");
		sl_panelBotInfo.putConstraint(SpringLayout.NORTH, lblAnzahlChannels, 6, SpringLayout.SOUTH, lblVerbundeneUser);
		sl_panelBotInfo.putConstraint(SpringLayout.EAST, lblAnzahlChannels, 0, SpringLayout.EAST, lblVerbundeneUser);
		panelBotInfo.add(lblAnzahlChannels);
		
		lblAnzahlChannelsNR = new JLabel("0");
		sl_panelBotInfo.putConstraint(SpringLayout.WEST, lblAnzahlChannelsNR, 0, SpringLayout.WEST, lblVerbundeneUserNR);
		sl_panelBotInfo.putConstraint(SpringLayout.SOUTH, lblAnzahlChannelsNR, 0, SpringLayout.SOUTH, lblAnzahlChannels);
		panelBotInfo.add(lblAnzahlChannelsNR);
		
		JPanel sqlConfigPanel = new JPanel();
		tabbedPane.addTab("Bot Config", null, sqlConfigPanel, "Hier werden die Mysql einstellungen vorgenommen");
		sqlConfigPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		sqlConfigPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		SpringLayout sl_sqlConfigPanel = new SpringLayout();
		sqlConfigPanel.setLayout(sl_sqlConfigPanel);
		
		JLabel lblUsername = new JLabel("MySql Username:");
		lblUsername.setAlignmentY(Component.TOP_ALIGNMENT);
		sqlConfigPanel.add(lblUsername);
		
		JLabel lblPasswort = new JLabel("MySql Passwort:");
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, lblPasswort, 0, SpringLayout.EAST, lblUsername);
		lblPasswort.setAlignmentY(Component.TOP_ALIGNMENT);
		sqlConfigPanel.add(lblPasswort);
		
		JLabel lblDatabase = new JLabel("MySql Database:");
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, lblDatabase, 0, SpringLayout.EAST, lblUsername);
		lblDatabase.setAlignmentY(Component.TOP_ALIGNMENT);
		sqlConfigPanel.add(lblDatabase);
		
		JLabel lblHost = new JLabel("MySql Host:");
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, lblHost, 0, SpringLayout.EAST, lblUsername);
		sqlConfigPanel.add(lblHost);
		
		JLabel lblPort = new JLabel("MySql Port:");
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, lblPort, 0, SpringLayout.EAST, lblUsername);
		sqlConfigPanel.add(lblPort);
		
		inputSQLUsername = new JTextField();
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, inputSQLUsername, 10, SpringLayout.NORTH, sqlConfigPanel);
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, inputSQLUsername, -10, SpringLayout.EAST, sqlConfigPanel);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, lblUsername, 3, SpringLayout.NORTH, inputSQLUsername);
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, lblUsername, -6, SpringLayout.WEST, inputSQLUsername);
		inputSQLUsername.setAlignmentY(Component.TOP_ALIGNMENT);
		inputSQLUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
		sqlConfigPanel.add(inputSQLUsername);
		inputSQLUsername.setColumns(10);
		
		inputSQLPasswort = new JTextField();
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, inputSQLPasswort, 3, SpringLayout.SOUTH, inputSQLUsername);
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, inputSQLPasswort, -10, SpringLayout.EAST, sqlConfigPanel);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, lblPasswort, 3, SpringLayout.NORTH, inputSQLPasswort);
		inputSQLPasswort.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputSQLPasswort.setAlignmentY(Component.TOP_ALIGNMENT);
		sqlConfigPanel.add(inputSQLPasswort);
		inputSQLPasswort.setColumns(10);
		
		inputSQLDatabase = new JTextField();
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, inputSQLDatabase, -10, SpringLayout.EAST, sqlConfigPanel);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, lblDatabase, 3, SpringLayout.NORTH, inputSQLDatabase);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, inputSQLDatabase, 3, SpringLayout.SOUTH, inputSQLPasswort);
		inputSQLDatabase.setAlignmentY(Component.TOP_ALIGNMENT);
		inputSQLDatabase.setAlignmentX(Component.LEFT_ALIGNMENT);
		sqlConfigPanel.add(inputSQLDatabase);
		inputSQLDatabase.setColumns(10);
		
		inputSQLHost = new JTextField();
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, inputSQLHost, -10, SpringLayout.EAST, sqlConfigPanel);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, lblHost, 3, SpringLayout.NORTH, inputSQLHost);
		inputSQLHost.setAlignmentY(Component.TOP_ALIGNMENT);
		inputSQLHost.setAlignmentX(Component.LEFT_ALIGNMENT);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, inputSQLHost, 3, SpringLayout.SOUTH, inputSQLDatabase);
		sqlConfigPanel.add(inputSQLHost);
		inputSQLHost.setColumns(10);
		
		inputSQLPort = new JTextField();
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, inputSQLPort, -10, SpringLayout.EAST, sqlConfigPanel);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, lblPort, 3, SpringLayout.NORTH, inputSQLPort);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, inputSQLPort, 3, SpringLayout.SOUTH, inputSQLHost);
		sqlConfigPanel.add(inputSQLPort);
		inputSQLPort.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new CBConfigSave(this));
		sl_sqlConfigPanel.putConstraint(SpringLayout.SOUTH, btnSave, -10, SpringLayout.SOUTH, sqlConfigPanel);
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, btnSave, 0, SpringLayout.EAST, inputSQLUsername);
		sqlConfigPanel.add(btnSave);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new CBConfigLoad(this));
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, btnLoad, 0, SpringLayout.NORTH, btnSave);
		sqlConfigPanel.add(btnLoad);
		
		chckbxDebugToFile = new JCheckBox("Debug to File");
		sqlConfigPanel.add(chckbxDebugToFile);
		
		JLabel lblQueryLoginName = new JLabel("Query Login Name: ");
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, lblQueryLoginName, 0, SpringLayout.NORTH, lblUsername);
		sqlConfigPanel.add(lblQueryLoginName);
		
		JLabel lblQueryLoginPasswort = new JLabel("Query Login Passwort:");
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, lblQueryLoginName, 0, SpringLayout.EAST, lblQueryLoginPasswort);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, lblQueryLoginPasswort, 0, SpringLayout.NORTH, lblPasswort);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, lblQueryLoginPasswort, 10, SpringLayout.WEST, sqlConfigPanel);
		sqlConfigPanel.add(lblQueryLoginPasswort);
		
		inputQueryLoginName = new JTextField();
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, inputQueryLoginName, -3, SpringLayout.NORTH, lblUsername);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, inputQueryLoginName, 6, SpringLayout.EAST, lblQueryLoginName);
		sqlConfigPanel.add(inputQueryLoginName);
		inputQueryLoginName.setColumns(10);
		
		inputQueryLoginPasswort = new JTextField();
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, inputQueryLoginPasswort, -3, SpringLayout.NORTH, lblPasswort);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, inputQueryLoginPasswort, 6, SpringLayout.EAST, lblQueryLoginPasswort);
		sqlConfigPanel.add(inputQueryLoginPasswort);
		inputQueryLoginPasswort.setColumns(10);
		
		JButton btnMysqlTest = new JButton("MySql Test");
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, chckbxDebugToFile, 1, SpringLayout.NORTH, btnMysqlTest);
		btnMysqlTest.addActionListener(new CBMysqlTest(this));
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, btnMysqlTest, 6, SpringLayout.SOUTH, inputSQLPort);
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, btnMysqlTest, 0, SpringLayout.EAST, inputSQLUsername);
		btnMysqlTest.setPreferredSize(new Dimension(85, 24));
		sqlConfigPanel.add(btnMysqlTest);
		
		JSeparator separator_1 = new JSeparator();
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, separator_1, 41, SpringLayout.SOUTH, inputQueryLoginPasswort);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, separator_1, 10, SpringLayout.WEST, sqlConfigPanel);
		sl_sqlConfigPanel.putConstraint(SpringLayout.SOUTH, separator_1, 0, SpringLayout.SOUTH, lblHost);
		sqlConfigPanel.add(separator_1);
		
		JSeparator separator = new JSeparator();
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, separator_1, -12, SpringLayout.WEST, separator);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, separator, 0, SpringLayout.NORTH, sqlConfigPanel);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, separator, 6, SpringLayout.EAST, inputQueryLoginName);
		sl_sqlConfigPanel.putConstraint(SpringLayout.SOUTH, separator, 107, SpringLayout.SOUTH, lblPort);
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, separator, -6, SpringLayout.WEST, lblPasswort);
		separator.setOrientation(SwingConstants.VERTICAL);
		sqlConfigPanel.add(separator);
		
		JLabel lblConfig = new JLabel("Config:");
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, btnLoad, 6, SpringLayout.EAST, lblConfig);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, lblConfig, 4, SpringLayout.NORTH, btnSave);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, lblConfig, 0, SpringLayout.WEST, lblUsername);
		sqlConfigPanel.add(lblConfig);
		
		JButton btnStartbot = new JButton("Start Bot");
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, btnStartbot, 0, SpringLayout.WEST, lblQueryLoginPasswort);
		btnStartbot.addActionListener(new CBStartBot(this));
		sqlConfigPanel.add(btnStartbot);
		
		chckbxServerEvent = new JCheckBox("Server Event");
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, chckbxServerEvent, -4, SpringLayout.NORTH, lblPort);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, chckbxServerEvent, 10, SpringLayout.WEST, sqlConfigPanel);
		sqlConfigPanel.add(chckbxServerEvent);
		
		chckbxChannelEvent = new JCheckBox("Channel Event");
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, chckbxDebugToFile, 0, SpringLayout.WEST, chckbxChannelEvent);
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, chckbxChannelEvent, -4, SpringLayout.NORTH, lblPort);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, chckbxChannelEvent, 6, SpringLayout.EAST, chckbxServerEvent);
		sqlConfigPanel.add(chckbxChannelEvent);
		
		chckbxTextEvent = new JCheckBox("Text Event");
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, chckbxTextEvent, 5, SpringLayout.SOUTH, chckbxServerEvent);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, chckbxTextEvent, 0, SpringLayout.WEST, lblQueryLoginPasswort);
		sqlConfigPanel.add(chckbxTextEvent);
		
		JLabel lblBotIst = new JLabel("Bot ist: ");
		sl_sqlConfigPanel.putConstraint(SpringLayout.SOUTH, btnStartbot, -6, SpringLayout.NORTH, lblBotIst);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, lblBotIst, 10, SpringLayout.WEST, sqlConfigPanel);
		sl_sqlConfigPanel.putConstraint(SpringLayout.SOUTH, lblBotIst, 0, SpringLayout.SOUTH, btnSave);
		sqlConfigPanel.add(lblBotIst);
		
		lblOffline = new JLabel("offline");
		lblOffline.setForeground(Color.RED);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, lblOffline, 6, SpringLayout.EAST, lblBotIst);
		sl_sqlConfigPanel.putConstraint(SpringLayout.SOUTH, lblOffline, 0, SpringLayout.SOUTH, btnSave);
		sqlConfigPanel.add(lblOffline);
		
		JButton btnStopBot = new JButton("Stop Bot");
		btnStopBot.addActionListener(new CBStopBot(this));
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, btnStopBot, 0, SpringLayout.NORTH, btnStartbot);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, btnStopBot, 6, SpringLayout.EAST, btnStartbot);
		sqlConfigPanel.add(btnStopBot);
		
		JSeparator separator_2 = new JSeparator();
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, separator_2, 13, SpringLayout.SOUTH, chckbxDebugToFile);
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, separator_2, 10, SpringLayout.WEST, sqlConfigPanel);
		sl_sqlConfigPanel.putConstraint(SpringLayout.SOUTH, separator_2, -6, SpringLayout.NORTH, btnStartbot);
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, separator_2, -12, SpringLayout.WEST, separator);
		sqlConfigPanel.add(separator_2);
		
		textTs3IP = new JTextField();
		sl_sqlConfigPanel.putConstraint(SpringLayout.WEST, textTs3IP, 0, SpringLayout.WEST, inputQueryLoginName);
		sl_sqlConfigPanel.putConstraint(SpringLayout.SOUTH, textTs3IP, 0, SpringLayout.SOUTH, inputSQLDatabase);
		sqlConfigPanel.add(textTs3IP);
		textTs3IP.setColumns(10);
		
		JLabel lblTeamspeakIp = new JLabel("Teamspeak 3 IP:");
		sl_sqlConfigPanel.putConstraint(SpringLayout.NORTH, lblTeamspeakIp, 0, SpringLayout.NORTH, lblDatabase);
		sl_sqlConfigPanel.putConstraint(SpringLayout.EAST, lblTeamspeakIp, 0, SpringLayout.EAST, lblQueryLoginName);
		sqlConfigPanel.add(lblTeamspeakIp);
		sqlConfigPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{inputQueryLoginName, inputQueryLoginPasswort, inputSQLUsername, inputSQLPasswort, inputSQLDatabase, inputSQLHost, inputSQLPort, btnMysqlTest, chckbxServerEvent, chckbxChannelEvent, chckbxTextEvent, chckbxDebugToFile, btnSave, btnStartbot, btnStopBot, btnLoad, separator, lblUsername, lblPasswort, lblDatabase, lblHost, lblPort, lblQueryLoginName, lblQueryLoginPasswort, separator_1, lblConfig, lblBotIst, lblOffline, separator_2}));
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{scrollPane, textConsole, panelBotInfo, lblVerbundeneUser, lblVerbundeneUserNR, lblAnzahlChannels, lblAnzahlChannelsNR, sqlConfigPanel, lblUsername, lblPasswort, separator, lblDatabase, lblHost, lblPort, tabbedPane, inputSQLUsername, inputSQLPasswort, inputSQLDatabase, inputSQLHost, inputSQLPort, btnSave, btnLoad, chckbxDebugToFile, lblQueryLoginName, lblQueryLoginPasswort, inputQueryLoginName, inputQueryLoginPasswort, btnMysqlTest, separator_1, lblConfig, btnStartbot, chckbxServerEvent, chckbxChannelEvent, chckbxTextEvent, lblBotIst, lblOffline, btnStopBot, separator_2}));
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	
	public JTextPane getTextConsole() {
		return textConsole;
	}
	public JLabel getLblVerbundeneUserNR() {
		return lblVerbundeneUserNR;
	}
	public JLabel getLblAnzahlChannelsNR() {
		return lblAnzahlChannelsNR;
	}
	public JTextField getInputSQLDatabase() {
		return inputSQLDatabase;
	}
	public JTextField getInputSQLUsername() {
		return inputSQLUsername;
	}
	public JTextField getInputSQLPort() {
		return inputSQLPort;
	}
	public JTextField getInputSQLHost() {
		return inputSQLHost;
	}
	public JTextField getInputSQLPasswort() {
		return inputSQLPasswort;
	}
	public JTextField getInputQueryLoginPasswort() {
		return inputQueryLoginPasswort;
	}
	public JTextField getInputQueryLoginName() {
		return inputQueryLoginName;
	}
	public JCheckBox getChckbxDebugToFile() {
		return chckbxDebugToFile;
	}
	public JCheckBox getChckbxChannelEvent() {
		return chckbxChannelEvent;
	}
	public JCheckBox getChckbxServerEvent() {
		return chckbxServerEvent;
	}
	public JCheckBox getChckbxTextEvent() {
		return chckbxTextEvent;
	}
	public JLabel getLblOffline() {
		return lblOffline;
	}
	public JTextField getTextTs3IP() {
		return textTs3IP;
	}
}
