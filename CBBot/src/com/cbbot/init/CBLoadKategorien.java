package com.cbbot.init;

import com.cbbot.CBInfo;
import com.cbbot.kategorie.CBKategorie;

public class CBLoadKategorien extends CBLoad{

	public CBLoadKategorien(CBInfo info) {
		this.loadAll(info);
	}

	private void loadAll(CBInfo info) {
		info.setAdminKat(new CBKategorie(info, CBKategorie.admin));
		info.setNormalKat(new CBKategorie(info, CBKategorie.normal));
		info.setGameKat(new CBKategorie(info, CBKategorie.game));
		info.setClanKat(new CBKategorie(info, CBKategorie.clan));
	}
}
