package com.cbbot.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.cbbot.CBInfo;

public class CBGeburtsdatum {
	
	private GregorianCalendar geburtsdatum;
	private CBInfo info;
	private CBUser user;
	
	
	public CBGeburtsdatum(CBInfo info, String bDay, CBUser user){
		this.info = info;
		this.user = user;
		this.setGeburtsdatum(bDay);
		
	}
	/**
	 * Wiedergibt das Alter in einer Dezimalzahl aus
	 * @return Das alter als INT wert
	 */
	public int getAge(){
		int dJahr = Calendar.getInstance().get(1) - this.getGeburtsdatumYear();
		
		if(Calendar.getInstance().get(2) < this.getGeburtsdatumMonth()){
			dJahr -= 1;
		}
		return dJahr;
	}
	
	/**
	 * Legt das Geburtsdatum fest
	 * @param datum - String 10.11.1991 || 1991.11.10
	 */
	public void setGeburtsdatum(String datum){
		int i = datum.indexOf('.');
		int day = Integer.parseInt(datum.substring(0,i));
		int month = Integer.parseInt(datum.substring(i+1,i+3));
		int year = Integer.parseInt(datum.substring(i+4, datum.length()));
		
		if(day > year){
			int tmp = year;
			year = day;
			day = tmp;
		}
		this.geburtsdatum = new GregorianCalendar(year,month-1,day);
		if(!this.checkDB(this.geburtsdatum)){
			this.updateDB();
		}
	}

	private void updateDB() {
		this.info.getSql().open();
		this.info.getSql().query("UPDATE account SET bDay = '" + this.formatSqlDate(this.geburtsdatum) + "' WHERE a_ID = " + this.user.getDbID() + ";");
		this.info.getSql().close();
	}

	private String formatSqlDate(GregorianCalendar bDay) {
		return "" + bDay.get(1) + "-" + bDay.get(2) + "-" + bDay.get(5);  
	}

	private boolean checkDB(GregorianCalendar geburtsdatum) {
		this.info.getSql().open();
		
		ResultSet res = this.info.getSql().query("SELECT * FROM account WHERE a_ID = " + user.getDbID() + ";");
		
		try {
			while(res.next()){
				if(res.getDate("bDay") != null){
					this.info.getSql().close();
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.info.getSql().close();
		return false;
	}

	public CBInfo getInfo() {
		return info;
	}
	public void setInfo(CBInfo info) {
		this.info = info;
	}
	public GregorianCalendar getGeburtsdatum() {
		return geburtsdatum;
	}
	public void setGeburtsdatum(GregorianCalendar geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}
	public int getGeburtsdatumYear(){
		return this.geburtsdatum.get(1);
	}
	public int getGeburtsdatumMonth(){
		return this.geburtsdatum.get(2);
	}
	public int getGeburtsdatumDay(){
		return this.geburtsdatum.get(5);
	}
	public String getDatum() {
		return this.getGeburtsdatumDay() + "." + this.getGeburtsdatumMonth() + "." + this.getGeburtsdatumYear();
	}
}
