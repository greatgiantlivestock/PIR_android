package com.android.pir.gglc.database;

public class Absen {

	//private variables
	int id_absen;
	String nama_karyawan;
	String waktu;
	String lokasi;

	// Empty constructor
	public Absen(){

	}
	// constructor
	public Absen(int id_absen, String nama_karyawan, String waktu, String lokasi){
		this.id_absen = id_absen;
		this.nama_karyawan = nama_karyawan;
		this.waktu = waktu;
		this.lokasi = lokasi;
	}

	// constructor
	public Absen(String nama_karyawan, String waktu, String lokasi){
		this.nama_karyawan = nama_karyawan;
		this.waktu = waktu;
		this.lokasi = lokasi;
	}
	// getting id_absen
	public int getId_absen(){
		return this.id_absen;
	}
	
	// setting id_absen
	public void setId_absen(int id_absen){
		this.id_absen= id_absen;
	}
	
	// getting nama karyawan
	public String getNama_karyawan(){
		return this.nama_karyawan;
	}
	
	// setting nama karyawan
	public void setNama_karyawan(String nama_karyawan){
		this.nama_karyawan= nama_karyawan;
	}
	
	// getting waktu
	public String getWaktu(){
		return this.waktu;
	}
	
	// setting waktu
	public void setWaktu(String waktu){
		this.waktu= waktu;
	}


	// getting lokasi
	public String getLokasi(){
		return this.lokasi;
	}

	// setting lokasi
	public void setLokasi(String lokasi){
		this.lokasi= lokasi;
	}
}
