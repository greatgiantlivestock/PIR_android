package com.android.pir.gglc.database;

public class Trx_Checkout {

	//private variables
	int id_checkout;
	int id_rencana_detail;
	String tanggal_checkout;
	int id_user;
	String realisasi_kegiatan;
	String lats;
	String longs;

	// Empty constructor
	public Trx_Checkout(){

	}
	// constructor
	public Trx_Checkout(int id_checkout, int id_rencana_detail, String tanggal_checkout, int id_user, String lats, String longs){
		this.id_checkout = id_checkout;
		this.id_rencana_detail = id_rencana_detail;
		this.tanggal_checkout = tanggal_checkout;
		this.id_user= id_user;
		this.realisasi_kegiatan= realisasi_kegiatan;
		this.lats = lats;
		this.longs = longs;
	}

	// constructor
	public Trx_Checkout(int id_rencana_detail, String tanggal_checkout, int id_user, String lats, String longs){
		this.id_rencana_detail = id_rencana_detail;
		this.tanggal_checkout = tanggal_checkout;
		this.id_user = id_user;
		this.realisasi_kegiatan = realisasi_kegiatan;
		this.lats=lats;
		this.longs=longs;
	}

	public int getId_checkout(){
		return this.id_checkout;
	}
	public void setId_checkout(int id_checkout){
		this.id_checkout= id_checkout;
	}

	public int getId_rencana_detail(){
		return this.id_rencana_detail;
	}
	public void setId_rencana_detail(int id_rencana_detail){
		this.id_rencana_detail= id_rencana_detail;
	}

	public String getTanggal_checkout(){
		return this.tanggal_checkout;
	}
	public void setTanggal_checkout(String tanggal_checkout){
		this.tanggal_checkout= tanggal_checkout;
	}

	public int getId_user(){
		return this.id_user;
	}
	public void setId_user(int id_user){
		this.id_user=id_user;
	}

	public String getRealisasi_kegiatan(){
		return this.realisasi_kegiatan;
	}
	public void setRealisasi_kegiatan(String realisasi_kegiatan){
		this.realisasi_kegiatan= realisasi_kegiatan;
	}

	public String getLats(){
		return this.lats;
	}
	public void setLats(String lats){
		this.lats= lats;
	}

	public String getLongs(){
		return this.longs;
	}
	public void setLongs(String longs){
		this.longs= longs;
	}
}
