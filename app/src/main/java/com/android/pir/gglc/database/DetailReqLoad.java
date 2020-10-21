package com.android.pir.gglc.database;

public class DetailReqLoad {
	private int id_sales_order;
	private String nama_product;
	private String jumlah_order;
	private int id_product;
	private String batch;

//	public DetailReqLoad(int count, String nama_product, String kode_product, String harga_jual,
//						 String stock, String uomqtyl1, String uomqtyl2, String uomqtyl3, String uomqtyl4, int id_product) {
//
//	}

	// constructor
	public DetailReqLoad(String nama_product,String jumlah_order, int id_product, String batch) {
		this.nama_product = nama_product;
		this.jumlah_order = jumlah_order;
		this.id_product = id_product;
		this.batch = batch;

	}

	public int getId_sales_order() {
		return id_sales_order;
	}

	public void setId_sales_order(int id_sales_order) {
		this.id_sales_order = id_sales_order;
	}

	public String getNama_product() {
		return nama_product;
	}

	public void setNama_product(String nama_product) {
		this.nama_product = nama_product;
	}

	public String getJumlah_order() {
		return jumlah_order;
	}

	public void setJumlah_order(String jumlah_order) {
		this.jumlah_order = jumlah_order;
	}

	public int getId_product() {
		return id_product;
	}

	public void setId_product(int id_product) {
		this.id_product = id_product;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

}