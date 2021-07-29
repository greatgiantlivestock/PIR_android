package com.android.pir.gglc.database;

public class PengobatanJoin {

	//private variables
	int id_pengobatan;
	int id_rencana_detail;
	String kode_obat;
	int qty;
	String foto_pengobatan;
	String tanggal;
	String satuan;

	// Empty constructor
	public PengobatanJoin(){

	}
	// constructor
	public PengobatanJoin(int id_pengobatan, int id_rencana_detail, String kode_obat, int qty, String foto_pengobatan, String tanggal, String satuan){
		this.id_pengobatan= id_pengobatan;
		this.id_rencana_detail= id_rencana_detail;
		this.kode_obat= kode_obat;
		this.qty= qty;
		this.foto_pengobatan= foto_pengobatan;
		this.tanggal= tanggal;
		this.satuan= satuan;
	}

	// constructor
	public PengobatanJoin(int id_rencana_detail, String kode_obat, int qty, String foto_pengobatan, String tanggal, String satuan){
		this.id_rencana_detail= id_rencana_detail;
		this.kode_obat= kode_obat;
		this.qty= qty;
		this.foto_pengobatan= foto_pengobatan;
		this.tanggal= tanggal;
		this.satuan= satuan;
	}

	public int getId_pengobatan(){
		return this.id_pengobatan;
	}
	public void setId_pengobatan(int id_pengobatan){
		this.id_pengobatan=id_pengobatan;
	}

	public int getId_rencana_detail(){
		return this.id_rencana_detail;
	}
	public void setId_rencana_detail(int id_rencana_detail){
		this.id_rencana_detail= id_rencana_detail;
	}

	public String getKode_obat(){
		return this.kode_obat;
	}
	public void setKode_obat(String kode_obat){
		this.kode_obat= kode_obat;
	}

	public int getQty(){
		return this.qty;
	}
	public void setQty(int qty){
		this.qty= qty;
	}

	public String getFoto_pengobatan(){
		return this.foto_pengobatan;
	}
	public void setFoto_pengobatan(String foto_pengobatan){
		this.foto_pengobatan= foto_pengobatan;
	}

	public String getTanggal(){
		return this.tanggal;
	}
	public void setTanggal(String tanggal){
		this.tanggal= tanggal;
	}


	public String getSatuan(){
		return this.satuan;
	}
	public void setSatuan(String satuan){
		this.satuan= satuan;
	}

}
