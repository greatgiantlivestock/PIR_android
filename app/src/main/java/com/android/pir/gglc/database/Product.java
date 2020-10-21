package com.android.pir.gglc.database;

public class Product {

	//private variables
	int id_product;
	String nama_product;

	// Empty constructor
	public Product(){

	}
	// constructor
	public Product(int id_product, String nama_product){
		this.id_product= id_product;
		this.nama_product= nama_product;
	}

	// constructor
	public Product(String nama_product){
		this.nama_product= nama_product;
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

}
