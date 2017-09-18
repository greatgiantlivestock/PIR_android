package com.android.canvasing.gglc.database;

public class TmpCustomer {

	//private variables
	int id_customer_tmp;
	String kode_customer;
	String nama_customer;
	String no_hp;
	String alamat;
	String nama_usaha;

	// Empty constructor
	public TmpCustomer(){

	}
	// constructor
	public TmpCustomer(int id_customer_tmp, String kode_customer, String nama_customer,String no_hp, String alamat, String nama_usaha){
		this.id_customer_tmp= id_customer_tmp;
		this.kode_customer= kode_customer;
		this.nama_customer = nama_customer;
		this.no_hp = no_hp;
		this.alamat= alamat;
		this.nama_usaha= nama_usaha;
	}

	// constructor
	public TmpCustomer(String kode_customer, String nama_customer, String no_hp, String alamat, String nama_usaha){
		this.kode_customer = kode_customer;
		this.nama_customer = nama_customer;
		this.no_hp=no_hp;
		this.alamat = alamat;
		this.nama_usaha = nama_usaha;
	}

	public int getId_customer_tmp(){
		return this.id_customer_tmp;
	}
	public void setId_customer_tmp(int id_customer_tmp){
		this.id_customer_tmp= id_customer_tmp;
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

	public String getNo_hp(){
		return this.no_hp;
	}
	public void setNo_hp(String no_hp){
		this.no_hp= no_hp;
	}

	public String getAlamat(){
		return this.alamat;
	}
	public void setAlamat(String alamat){
		this.alamat= alamat;
	}

	public String getNama_usaha(){
		return this.nama_usaha;
	}
	public void setNama_usaha(String nama_usaha){
		this.nama_usaha= nama_usaha;
	}
}
