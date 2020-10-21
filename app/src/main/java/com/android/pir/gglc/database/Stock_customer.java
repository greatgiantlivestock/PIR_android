package com.android.pir.gglc.database;

public class Stock_customer {

	//private variables
	int id_stock;
	int id_customer;
	int id_product;
	String nama_product;
	String satuan;
	int qty;
	String tanggal_update;

	// Empty constructor
	public Stock_customer(){

	}
	// constructor
	public Stock_customer(int id_stock, int id_customer, int id_product, String nama_product, String satuan, int qty, String tanggal_update){
		this.id_stock= id_stock;
		this.id_customer= id_customer;
		this.id_product= id_product;
		this.nama_product = nama_product;
		this.satuan = satuan;
		this.qty = qty;
		this.tanggal_update= tanggal_update;
	}

	// constructor
	public Stock_customer(int id_customer, int id_product, String nama_product, String satuan, int qty, String tanggal_update){
		this.id_customer= id_customer;
		this.id_product= id_product;
		this.nama_product = nama_product;
		this.satuan = satuan;
		this.qty= qty;
		this.tanggal_update= tanggal_update;
	}

	public int getId_stock(){
		return this.id_stock;
	}
	public void setId_stock(int id_stock){
		this.id_stock=id_stock;
	}
	public int getId_customer(){
		return this.id_customer;
	}
	public void setId_customer(int id_customer){
		this.id_customer=id_customer;
	}
	public int getId_product(){
		return this.id_product;
	}
	public void setId_product(int id_product){
		this.id_product=id_product;
	}

	public String getNama_product(){
		return this.nama_product;
	}
	public void setNama_product(String nama_product){
		this.nama_product= nama_product;
	}
	public String getSatuan(){
		return this.satuan;
	}
	public void setSatuan(String satuan){
		this.satuan= satuan;
	}
	public int getQty(){
		return this.qty;
	}
	public void setQty(int qty){
		this.qty= qty;
	}
	public String getTanggal_update(){
		return this.tanggal_update;
	}
	public void setTanggal_update(String tanggal_update){
		this.tanggal_update= tanggal_update;
	}

}
