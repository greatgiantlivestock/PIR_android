package com.android.pir.gglc.database;

public class History_canvassing {

	//private variables
	int id_canvassing;
	String nama_customer;
	String nomor_rencana;
	String alamat;
	String waktu_checkin;
	String waktu_checkout;

	// Empty constructor
	public History_canvassing(){

	}
	// constructor
	public History_canvassing(int id_canvassing, String nama_customer, String nomor_rencana, String alamat, String waktu_checkin, String waktu_checkout){
		this.id_canvassing=id_canvassing;
		this.nama_customer=nama_customer;
		this.nomor_rencana=nomor_rencana;
		this.alamat=alamat;
		this.waktu_checkin=waktu_checkin;
		this.waktu_checkout=waktu_checkout;
	}

	// constructor
	public History_canvassing(String nama_customer, String nomor_rencana, String alamat, String waktu_checkin, String waktu_checkout){
		this.nama_customer=nama_customer;
		this.nomor_rencana=nomor_rencana;
		this.alamat=alamat;
		this.waktu_checkin=waktu_checkin;
		this.waktu_checkout=waktu_checkout;
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
}
