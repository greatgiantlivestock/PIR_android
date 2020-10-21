package com.android.pir.gglc.database;

public class CheckDataStockFisik {

	//private variables
	int id_rencana_detail;
	int id_product;
	int qty;

	// Empty constructor
	public CheckDataStockFisik(){

	}
	// constructor
	public CheckDataStockFisik(int id_rencana_detail, int id_product, int qty){
		this.id_rencana_detail= id_rencana_detail;
		this.id_product = id_product;
		this.qty = qty;
	}

	// constructor
	public CheckDataStockFisik(int id_product, int qty){
		this.id_product = id_product;
		this.qty = qty;
	}

	public int getId_rencana_detail(){
		return this.id_rencana_detail;
	}
	public void setId_rencana_detail(int id_rencana_detail){
		this.id_rencana_detail=id_rencana_detail;
	}

	public int getId_product(){
		return this.id_product;
	}
	public void setNama_jenis(int id_product){
		this.id_product= id_product;
	}
	public int getQty(){
		return this.qty;
	}
	public void setQty(int qty){
		this.qty= qty;
	}

}
