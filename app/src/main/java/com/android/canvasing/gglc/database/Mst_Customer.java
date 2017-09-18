package com.android.canvasing.gglc.database;

public class Mst_Customer {

	//private variables
	int id_customer;
	String kode_customer;
	String nama_customer;
	String alamat;
	String no_hp;
	String lats;
	String longs;
	int id_wilayah;

	// Empty constructor
	public Mst_Customer(){

	}
	// constructor
	public Mst_Customer(int id_customer, String kode_customer, String nama_customer, String alamat, String no_hp,
						String lats, String longs, int id_wilayah){
		this.id_customer= id_customer;
		this.kode_customer= kode_customer;
		this.nama_customer = nama_customer;
		this.alamat= alamat;
		this.no_hp = no_hp;
		this.lats = lats;
		this.longs = longs;
		this.id_wilayah = id_wilayah;
	}

	// constructor
	public Mst_Customer(String kode_customer, String nama_customer, String alamat, String no_hp,
						String lats, String longs, int id_wilayah){
		this.kode_customer = kode_customer;
		this.nama_customer = nama_customer;
		this.alamat = alamat;
		this.no_hp=no_hp;
		this.lats=lats;
		this.longs=longs;
		this.id_wilayah=id_wilayah;
	}

	public int getId_customer(){
		return this.id_customer;
	}
	public void setId_customer(int id_customer){
		this.id_customer= id_customer;
	}

	public String getKode_customer(){
		return this.kode_customer;
	}
	public void setKode_customer(String kode_customer){
		this.kode_customer= kode_customer;
	}

	public String getNama_customer(){
		return this.nama_customer;
	}
	public void setNama_customer(String nama_customer){
		this.nama_customer= nama_customer;
	}

	public String getAlamat(){
		return this.alamat;
	}
	public void setAlamat(String alamat){
		this.alamat= alamat;
	}

	public String getNo_hp(){
		return this.no_hp;
	}
	public void setNo_hp(String no_hp){
		this.no_hp= no_hp;
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

	public int getId_wilayah(){
		return this.id_wilayah;
	}
	public void setId_wilayah(int id_wilayah){
		this.id_wilayah=id_wilayah;
	}
}
