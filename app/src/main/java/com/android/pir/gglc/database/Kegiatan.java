package com.android.pir.gglc.database;

public class Kegiatan {

	//private variables
	int id_kegiatan;
	String nama_kegiatan;
	int id_departemen;
	int id_wilayah;

	// Empty constructor
	public Kegiatan(){

	}
	// constructor
	public Kegiatan(int id_kegiatan, String nama_kegiatan, int id_departemen,  int id_wilayah){
		this.id_kegiatan= id_kegiatan;
		this.nama_kegiatan= nama_kegiatan;
		this.id_departemen = id_departemen;
		this.id_wilayah = id_wilayah;
	}

	// constructor
	public Kegiatan(String nama_kegiatan, int id_departemen, int id_wilayah){
		this.nama_kegiatan= nama_kegiatan;
		this.id_departemen = id_departemen;
		this.id_wilayah = id_wilayah;
	}

	public int getId_kegiatan(){
		return this.id_kegiatan;
	}
	public void setId_kegiatan(int id_kegiatan){
		this.id_kegiatan= id_kegiatan;
	}

	public String getNama_kegiatan(){
		return this.nama_kegiatan;
	}
	public void setNama_kegiatan(String nama_kegiatan){
		this.nama_kegiatan= nama_kegiatan;
	}

	public int getId_departemen(){
		return this.id_departemen;
	}
	public void setId_departemen(int id_departemen){
		this.id_departemen= id_departemen;
	}

	public int getId_wilayah(){
		return this.id_wilayah;
	}
	public void setId_wilayah(int id_wilayah){
		this.id_wilayah= id_wilayah;
	}
}
