package com.android.pir.gglc.database;

public class Pakan {
	int id;
	int indnr;
	String kode_pakan;
	String desc_pakan;
	String std;
	int budget;
	int terkirim;
	int sisa;
	int nofanim;
	String dof;
	String satuan;
	String tanggal_kirim;
	int qty_terima;
	String create_date;
	String pakan_type;

	// Empty constructor
	public Pakan(){

	}
	// constructor
	public Pakan(int id,int indnr,String kode_pakan,String desc_pakan,String std,int budget,int terkirim,int sisa,int nofanim,String dof,String satuan,String tanggal_kirim,int qty_terima,String create_date, String pakan_type){
		this.id= id;
		this.indnr=indnr;
		this.kode_pakan=kode_pakan;
		this.desc_pakan=desc_pakan;
		this.std=std;
		this.budget=budget;
		this.terkirim=terkirim;
		this.sisa=sisa;
		this.nofanim=nofanim;
		this.dof=dof;
		this.satuan=satuan;
		this.tanggal_kirim=tanggal_kirim;
		this.qty_terima=qty_terima;
		this.create_date=create_date;
		this.pakan_type=pakan_type;
	}

	// constructor
	public Pakan(int indnr,String kode_pakan,String desc_pakan,String std,int budget,int terkirim,int sisa,int nofanim,String dof,String satuan,String tanggal_kirim,int qty_terima,String create_date, String pakan_type){
		this.indnr=indnr;
		this.kode_pakan=kode_pakan;
		this.desc_pakan=desc_pakan;
		this.std=std;
		this.budget=budget;
		this.terkirim=terkirim;
		this.sisa=sisa;
		this.nofanim=nofanim;
		this.dof=dof;
		this.satuan=satuan;
		this.tanggal_kirim=tanggal_kirim;
		this.qty_terima=qty_terima;
		this.create_date=create_date;
		this.pakan_type=pakan_type;
	}

	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id=id;
	}
	public int getIndnr(){
		return this.indnr;
	}
	public void setIndnr(int indnr){
		this.indnr=indnr;
	}
	public String getKode_pakan(){
		return this.kode_pakan;
	}
	public void setKode_pakan(String kode_pakan){
		this.kode_pakan= kode_pakan;
	}
	public String getDesc_pakan(){
		return this.desc_pakan;
	}
	public void setDesc_pakan(String desc_pakan){
		this.desc_pakan= desc_pakan;
	}
	public String getStd(){
		return this.std;
	}
	public void setStd(String std){
		this.std= std;
	}
	public int getBudget(){
		return this.budget;
	}
	public void setBudget(int budget){
		this.budget=budget ;
	}
	public int getTerkirim(){
		return this.terkirim;
	}
	public void setTerkirim(int terkirim){
		this.terkirim= terkirim;
	}
	public int getSisa(){
		return this.sisa;
	}
	public void setSisa(int sisa){
		this.sisa= sisa;
	}
	public int getNofanim(){
		return this.nofanim;
	}
	public void setNofanim(int nofanim){
		this.nofanim= nofanim;
	}
	public String getDof(){
		return this.dof;
	}
	public void setDof(String dof){
		this.dof= dof;
	}
	public String getSatuan(){
		return this.satuan;
	}
	public void setSatuan(String satuan){
		this.satuan= satuan;
	}
	public String getTanggal_kirim(){
		return this.tanggal_kirim;
	}
	public void setTanggal_kirim(String tanggal_kirim){
		this.tanggal_kirim= tanggal_kirim;
	}
	public int getQty_terima(){
		return this.qty_terima;
	}
	public void setQty_terima(int qty_terima){
		this.qty_terima= qty_terima;
	}
	public String getCreate_date(){
		return this.create_date;
	}
	public void setCreate_date(String create_date){
		this.create_date= create_date;
	}
	public String getPakan_type(){
		return this.pakan_type;
	}
	public void setPakan_type(String pakan_type){
		this.pakan_type= pakan_type;
	}
}
