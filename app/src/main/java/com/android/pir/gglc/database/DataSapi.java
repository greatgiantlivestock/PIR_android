package com.android.pir.gglc.database;

public class DataSapi {

	//private variables
	int id_data_sapi;
	String indnr;
	String lifnr;
	String beastid;
	String vistgid;

	// Empty constructor
	public DataSapi(){

	}

	// constructor
	public DataSapi(int id_data_sapi, String indnr, String lifnr, String beastid, String vistgid){
		this.id_data_sapi = id_data_sapi;
		this.indnr = indnr;
		this.lifnr = lifnr;
		this.beastid = beastid;
		this.vistgid = vistgid;
	}

	// constructor
	public DataSapi(String indnr, String lifnr, String beastid, String vistgid){
		this.indnr = indnr;
		this.lifnr = lifnr;
		this.beastid = beastid;
		this.vistgid = vistgid;
	}

	public int getId_data_sapi(){
		return this.id_data_sapi;
	}
	public void setId_data_sapi(int id_data_sapi){
		this.id_data_sapi = id_data_sapi;
	}

	public String getIndnr(){
		return this.indnr;
	}
	public void setIndnr(String indnr){
		this.indnr=indnr;
	}

	public String getLifnr(){
		return this.lifnr;
	}
	public void setLifnr(String lifnr){
		this.lifnr=lifnr;
	}

	public String getBeastid(){
		return this.beastid;
	}
	public void setBeastid(String beastid){
		this.beastid=beastid;
	}

	public String getVistgid(){
		return this.vistgid;
	}
	public void setVistgid(String vistgid){
		this.vistgid=vistgid;
	}
}
