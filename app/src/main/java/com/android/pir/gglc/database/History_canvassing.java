package com.android.pir.gglc.database;

public class History_canvassing {

	//private variables
	int id_canvassing;
	String nama_customer;
	String nomor_rencana;
	String alamat;
	String waktu_checkin;
	String waktu_checkout;
	String lats;
	String longs;
	String foto;
	String indnr;
	String id_rencana_detail;

	// Empty constructor
	public History_canvassing(){

	}
	// constructor
	public History_canvassing(int id_canvassing, String nama_customer, String nomor_rencana, String alamat, String waktu_checkin, String waktu_checkout, String lats, String longs, String foto, String indnr, String id_rencana_detail){
		this.id_canvassing=id_canvassing;
		this.nama_customer=nama_customer;
		this.nomor_rencana=nomor_rencana;
		this.alamat=alamat;
		this.waktu_checkin=waktu_checkin;
		this.waktu_checkout=waktu_checkout;
		this.lats=lats;
		this.longs=longs;
		this.foto=foto;
		this.indnr=indnr;
		this.id_rencana_detail=id_rencana_detail;
	}

	// constructor
	public History_canvassing(String nama_customer, String nomor_rencana, String alamat, String waktu_checkin, String waktu_checkout, String lats, String longs, String foto, String indnr, String id_rencana_detail){
		this.nama_customer=nama_customer;
		this.nomor_rencana=nomor_rencana;
		this.alamat=alamat;
		this.waktu_checkin=waktu_checkin;
		this.waktu_checkout=waktu_checkout;
		this.lats=lats;
		this.longs=longs;
		this.foto=foto;
		this.indnr=indnr;
		this.id_rencana_detail=id_rencana_detail;
	}
	// getting id_absen
	public int getId_canvassing(){
		return this.id_canvassing;
	}
	
	// setting id_absen
	public void setId_canvassing(int id_canvassing){
		this.id_canvassing= id_canvassing;
	}
	
	// getting nama karyawan
	public String getNama_customer(){
		return this.nama_customer;
	}
	
	// setting nama karyawan
	public void setNama_customer(String nama_customer){
		this.nama_customer= nama_customer;
	}
	
	// getting waktu
	public String getNomor_rencana(){
		return this.nomor_rencana;
	}
	
	// setting waktu
	public void setNomor_rencana(String nomor_rencana){
		this.nomor_rencana= nomor_rencana;
	}


	// getting lokasi
	public String getAlamat(){
		return this.alamat;
	}

	// setting lokasi
	public void setAlamat(String alamat){
		this.alamat= alamat;
	}

	// getting lokasi
	public String getWaktu_checkin(){
		return this.waktu_checkin;
	}

	// setting lokasi
	public void setWaktu_checkin(String waktu_checkin){
		this.waktu_checkin= waktu_checkin;
	}

	// getting lokasi
	public String getWaktu_checkout(){
		return this.waktu_checkout;
	}

	// setting lokasi
	public void setWaktu_checkout(String waktu_checkout){
		this.waktu_checkout= waktu_checkout;
	}

	// getting lokasi
	public String getLats(){
		return this.lats;
	}

	// setting lokasi
	public void setLats(String lats){
		this.lats= lats;
	}

	// getting lokasi
	public String getLongs(){
		return this.longs;
	}

	// setting lokasi
	public void setLongs(String longs){
		this.longs= longs;
	}

	// getting lokasi
	public String getFoto(){
		return this.foto;
	}

	// setting lokasi
	public void setFoto(String foto){
		this.foto= foto;
	}

	// getting lokasi
	public String getIndnr(){
		return this.indnr;
	}

	// setting lokasi
	public void setIndnr(String indnr){
		this.indnr= indnr;
	}

	// getting lokasi
	public String getId_rencana_detail(){
		return this.id_rencana_detail;
	}

	// setting lokasi
	public void setId_rencana_detail(String id_rencana_detail){
		this.id_rencana_detail= id_rencana_detail;
	}
}
