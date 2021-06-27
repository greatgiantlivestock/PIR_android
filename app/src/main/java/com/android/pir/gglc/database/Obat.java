package com.android.pir.gglc.database;

public class Obat {

	//private variables
	int id_obat;
	String kode_obat;
	String nama_obat;
	String unit_obat;

	// Empty constructor
	public Obat(){

	}
	// constructor
	public Obat(int id_obat, String kode_obat,String nama_obat,String unit_obat){
		this.id_obat= id_obat;
		this.kode_obat= kode_obat;
		this.nama_obat= nama_obat;
		this.unit_obat= unit_obat;
	}

	// constructor
	public Obat(String kode_obat,String nama_obat, String unit_obat){
		this.kode_obat= kode_obat;
		this.nama_obat= nama_obat;
		this.unit_obat= unit_obat;
	}

	public int getId_obat(){
		return this.id_obat;
	}
	public void setId_obat(int id_obat){
		this.id_obat=id_obat;
	}

	public String getKode_obat(){
		return this.kode_obat;
	}
	public void setKode_obat(String kode_obat){
		this.kode_obat= kode_obat;
	}

	public String getNama_obat(){
		return this.nama_obat;
	}
	public void setNama_obat(String nama_obat){
		this.nama_obat= nama_obat;
	}

	public String getUnit_obat(){
		return this.unit_obat;
	}
	public void setUnit_obat(String unit_obat){
		this.unit_obat= unit_obat;
	}

}
