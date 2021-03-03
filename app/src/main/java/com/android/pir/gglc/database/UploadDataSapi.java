package com.android.pir.gglc.database;

public class UploadDataSapi {

	//private variables
	int id_upload;
	String id_rencana_detail;
	String eartag;
	String foto;
	String keterangan;
	String assessment;
	String tanggal;

	// Empty constructor
	public UploadDataSapi(){

	}

	// constructor
	public UploadDataSapi(int id_upload, String id_rencana_detail, String eartag, String foto, String keterangan,
						  String asssessment, String tanggal){
		this.id_upload = id_upload;
		this.id_rencana_detail= id_rencana_detail;
		this.eartag= eartag;
		this.foto = foto;
		this.keterangan= keterangan;
		this.assessment = asssessment;
		this.tanggal = tanggal;
	}

	// constructor
	public UploadDataSapi(String id_rencana_detail, String eartag, String foto, String keterangan,
						  String asssessment, String tanggal){
		this.id_rencana_detail= id_rencana_detail;
		this.eartag= eartag;
		this.foto = foto;
		this.keterangan= keterangan;
		this.assessment = asssessment;
		this.tanggal = tanggal;
	}

	public int getId_upload(){
		return this.id_upload;
	}
	public void setId_upload(int id_upload){
		this.id_upload= id_upload;
	}

	public String getId_rencana_detail(){
		return this.id_rencana_detail;
	}
	public void setId_rencana_detail(String id_rencana_detail){
		this.id_rencana_detail=id_rencana_detail;
	}

	public String getEartag(){
		return this.eartag;
	}
	public void setEartag(String eartag){
		this.eartag=eartag;
	}

	public String getFoto(){
		return this.foto;
	}
	public void setFoto(String foto){
		this.foto=foto;
	}

	public String getKeterangan(){
		return this.keterangan;
	}
	public void setKeterangan(String keterangan){
		this.keterangan=keterangan;
	}
	public String getAssessment(){
		return this.assessment;
	}
	public void setAssessment(String assessment){
		this.assessment=assessment;
	}
	public String getTanggal(){
		return this.tanggal;
	}
	public void setTanggal(String tanggal){
		this.tanggal=tanggal;
	}
}
