package com.cbbot.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.cbbot.CBInfo;

public class CBGeburtsdatum {
	
	private GregorianCalendar geburtsdatum;
	
	
	public CBGeburtsdatum(CBInfo info, String bDay, CBUser user){
		this.setGeburtsdatum(info,user,bDay);
		
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
	 * @param user 
	 * @param info 
	 * @param datum - String 10.11.1991 || 1991.11.10
	 */
	public void setGeburtsdatum(CBInfo info, CBUser user, String datum){
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
		if(!this.checkDB(info, user, this.geburtsdatum)){
			this.updateDB(info, user);
		}
	}

	private void updateDB(CBInfo info, CBUser user) {
		info.getSql().open();
		info.getSql().query("UPDATE account SET bDay = '" + this.formatSqlDate(this.geburtsdatum) + "' WHERE a_ID = " + user.getDbID() + ";");
		info.getSql().close();
	}

	private String formatSqlDate(GregorianCalendar bDay) {
		return "" + bDay.get(1) + "-" + bDay.get(2) + "-" + bDay.get(5);  
	}

	private boolean checkDB(CBInfo info, CBUser user, GregorianCalendar geburtsdatum) {
		info.getSql().open();
		
		ResultSet res = info.getSql().query("SELECT * FROM account WHERE a_ID = " + user.getDbID() + ";");
		
		try {
			while(res.next()){
				if(res.getDate("bDay") != null){
					info.getSql().close();
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		info.getSql().close();
		return false;
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
