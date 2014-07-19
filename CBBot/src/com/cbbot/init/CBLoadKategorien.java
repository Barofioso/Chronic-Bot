package com.cbbot.init;

import com.cbbot.CBInfo;
import com.cbbot.kategorie.CBKategorie;

public class CBLoadKategorien extends CBLoad{

	public CBLoadKategorien(CBInfo info) {
		this.loadAll(info);
	}

	private void loadAll(CBInfo info) {
		info.setAdminKat(new CBKategorie(info, "Admin"));
		info.setNormalKat(new CBKategorie(info, "Normal"));
	}

}
