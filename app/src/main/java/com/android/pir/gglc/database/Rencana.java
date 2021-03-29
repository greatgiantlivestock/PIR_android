package com.android.pir.gglc.database;

public class Rencana {

	//private variables
	int id_rencana_detail;
	String nama_customer;
	String alamat;
	int status;
	String tanggal_rencana;
	String indnr;
	public Rencana(){

	}
	// constructor
	public Rencana(int id_rencana_detail, String nama_customer, String alamat, int status, String tanggal_rencana,String indnr){
		this.id_rencana_detail=id_rencana_detail;
		this.nama_customer=nama_customer;
		this.alamat=alamat;
		this.status=status;
		this.tanggal_rencana=tanggal_rencana;
		this.indnr=indnr;
	}

	// constructor
	public Rencana(String nama_customer, String alamat, int status, String tanggal_rencana,String indnr){
		this.nama_customer=nama_customer;
		this.alamat=alamat;
		this.status=status;
		this.tanggal_rencana=tanggal_rencana;
		this.indnr=indnr;
	}
	// getting id_absen
	public int getId_rencana_detail(){
		return this.id_rencana_detail;
	}
	
	// setting id_absen
	public void setId_rencana_detail(int id_rencana_detail){
		this.id_rencana_detail= id_rencana_detail;
	}
	
	// getting nama karyawan
	public String getNama_customer(){
		return this.nama_customer;
	}
	
	// setting nama karyawan
	public void setNama_customer(String nama_customer){
		this.nama_customer= nama_customer;
	}

	// getting lokasi
	public String getAlamat(){
		return this.alamat;
	}

	// setting lokasi
	public void setAlamat(String alamat){
		this.alamat= alamat;
	}
	
	// getting waktu
	public int getStatus(){
		return this.status;
	}
	
	// setting waktu
	public void setStatus(int status){
		this.status= status;
	}

// getting tanggal rencana
	public String getTanggal_rencana(){
		return this.tanggal_rencana;
	}

	// setting tanggal rencana
	public void setTanggal_rencana(String tanggal_rencana){
		this.tanggal_rencana= tanggal_rencana;
	}

// getting tanggal rencana
	public String getIndnr(){
		return this.indnr;
	}

	// setting tanggal rencana
	public void setIndnr(String indnr){
		this.indnr= indnr;
	}

}
