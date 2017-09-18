package com.android.canvasing.gglc.database;

public class Trx_Checkin {

	//private variables
	int id_checkin;
	String tanggal_checkin;
	String nomor_checkin;
	int id_user;
	int id_rencana_detail;
	String kode_customer;
	String lats;
	String longs;
	String foto;

	// Empty constructor
	public Trx_Checkin(){

	}
	// constructor
	public Trx_Checkin(int id_checkin, String tanggal_checkin,String nomor_checkin, int id_user, int id_rencana_detail, String kode_customer,
					   String lats, String longs, String foto){
		this.id_checkin = id_checkin;
		this.tanggal_checkin = tanggal_checkin;
		this.nomor_checkin = nomor_checkin;
		this.id_user = id_user;
		this.id_rencana_detail = id_rencana_detail;
		this.kode_customer = kode_customer;
		this.lats = lats;
		this.longs = longs;
		this.foto = foto;
	}

	// constructor
	public Trx_Checkin(String tanggal_checkin,String nomor_checkin, int id_user, int id_rencana_detail, String kode_customer,
					   String lats, String longs, String foto){
		this.tanggal_checkin = tanggal_checkin;
		this.nomor_checkin = nomor_checkin;
		this.id_user = id_user;
		this.id_rencana_detail = id_rencana_detail;
		this.kode_customer=kode_customer;
		this.lats=lats;
		this.longs=longs;
		this.foto=foto;
	}

	public int getId_checkin(){
		return this.id_checkin;
	}
	public void setId_checkin(int id_checkin){
		this.id_checkin= id_checkin;
	}

	public String getTanggal_checkin(){
		return this.tanggal_checkin;
	}
	public void setTanggal_checkin(String tanggal_checkin){
		this.tanggal_checkin= tanggal_checkin;
	}

	public String getNomor_checkin(){
		return this.nomor_checkin;
	}
	public void setNomor_checkin(String nomor_checkin){
		this.nomor_checkin= nomor_checkin;
	}

	public int getId_user(){
		return this.id_user;
	}
	public void setId_user(int id_user){
		this.id_user=id_user;
	}

	public int getId_rencana_detail(){
		return this.id_rencana_detail;
	}
	public void setId_rencana_detail(int id_rencana_detail){
		this.id_rencana_detail=id_rencana_detail;
	}

	public String getKode_customer(){
		return this.kode_customer;
	}
	public void setKode_customer(String kode_customer){
		this.kode_customer= kode_customer;
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

	public String getFoto(){
		return this.foto;
	}
	public void setFoto(String foto){
		this.foto= foto;
	}
}
