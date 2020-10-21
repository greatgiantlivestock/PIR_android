package com.android.pir.gglc.database;

public class Jenis_kendaraan {

	//private variables
	int id_jenis_kendaraan;
	String nama_jenis;

	// Empty constructor
	public Jenis_kendaraan(){

	}
	// constructor
	public Jenis_kendaraan(int id_jenis_kendaraan, String nama_jenis){
		this.id_jenis_kendaraan= id_jenis_kendaraan;
		this.nama_jenis = nama_jenis;
	}

	// constructor
	public Jenis_kendaraan(String nama_jenis){
		this.nama_jenis = nama_jenis;
	}

	public int getId_jenis_kendaraan(){
		return this.id_jenis_kendaraan;
	}
	public void setId_jenis_kendaraan(int id_jenis_kendaraan){
		this.id_jenis_kendaraan=id_jenis_kendaraan;
	}

	public String getNama_jenis(){
		return this.nama_jenis;
	}
	public void setNama_jenis(String nama_jenis){
		this.nama_jenis= nama_jenis;
	}

}
