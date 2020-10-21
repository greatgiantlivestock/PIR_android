package com.android.pir.gglc.database;

public class DetailRencana {

	//private variables
	int id_rencana_detail;
	int id_rencana_header;
	int id_kegiatan;
	int id_customer;
	int id_karyawan;
	int status_rencana;
	String nomor_rencana_detail;

	// Empty constructor
	public DetailRencana(){

	}
	// constructor
	public DetailRencana(int id_rencana_detail, int id_rencana_header, int id_kegiatan, int id_customer, int id_karyawan,
						 int status_rencana, String nomor_rencana_detail){
		this.id_rencana_detail = id_rencana_detail;
		this.id_rencana_header = id_rencana_header;
		this.id_kegiatan = id_kegiatan;
		this.id_customer = id_customer;
		this.id_karyawan = id_karyawan;
		this.status_rencana = status_rencana;
		this.nomor_rencana_detail = nomor_rencana_detail;
	}

	// constructor
	public DetailRencana(int id_rencana_header, int id_kegiatan, int id_customer, int id_karyawan,
						 int status_rencana,String nomor_rencana_detail){
		this.id_rencana_header = id_rencana_header;
		this.id_kegiatan = id_kegiatan;
		this.id_customer = id_customer;
		this.id_karyawan = id_karyawan;
		this.status_rencana = status_rencana;
		this.nomor_rencana_detail = nomor_rencana_detail;
	}

	public int getId_rencana_detail(){
		return this.id_rencana_detail;
	}
	public void setId_rencana_detail(int id_rencana_detail){
		this.id_rencana_detail= id_rencana_detail;
	}

	public int getId_rencana_header(){
		return this.id_rencana_header;
	}
	public void setId_rencana_header(int id_rencana_header){
		this.id_rencana_header=id_rencana_header;
	}

	public int getId_kegiatan(){
		return this.id_kegiatan;
	}
	public void setId_kegiatan(int id_kegiatan){
		this.id_kegiatan=id_kegiatan;
	}

	public int getId_customer(){
		return this.id_customer;
	}
	public void setId_customer(int id_customer){
		this.id_customer= id_customer;
	}

	public int getId_karyawan(){
		return this.id_karyawan;
	}
	public void setId_karyawan(int id_karyawan){
		this.id_karyawan=id_karyawan;
	}

	public int getStatus_rencana(){
		return this.status_rencana;
	}
	public void setStatus_rencana(int status_rencana){
		this.status_rencana=status_rencana;
	}

	public String getNomor_rencana_detail(){
		return this.nomor_rencana_detail;
	}
	public void setNomor_rencana_detail(String nomor_rencana_detail){
		this.nomor_rencana_detail=nomor_rencana_detail;
	}
}
