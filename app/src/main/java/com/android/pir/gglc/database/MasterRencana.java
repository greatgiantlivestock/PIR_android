package com.android.pir.gglc.database;

public class MasterRencana {

	//private variables
	int id;
	int id_rencana_header;
	String nomor_rencana;
	String tanggal_penetapan;
	String tanggal_rencana;
	int id_user_input_rencana;
	String keterangan;
	String aproved;

	// Empty constructor
	public MasterRencana(){

	}
	// constructor
	public MasterRencana(int id, int id_rencana_header, String nomor_rencana, String tanggal_penetapan, String tanggal_rencana,
						 int id_user_input_rencana, String keterangan, String aproved){
		this.id = id;
		this.id_rencana_header = id_rencana_header;
		this.nomor_rencana = nomor_rencana;
		this.tanggal_penetapan = tanggal_penetapan;
		this.tanggal_rencana = tanggal_rencana;
		this.id_user_input_rencana = id_user_input_rencana;
		this.keterangan = keterangan;
		this.aproved = aproved;
	}

	// constructor
	public MasterRencana(int id_rencana_header, String nomor_rencana, String tanggal_penetapan, String tanggal_rencana,
						 int id_user_input_rencana, String keterangan, String aproved){
		this.id_rencana_header = id_rencana_header;
		this.nomor_rencana = nomor_rencana;
		this.tanggal_penetapan = tanggal_penetapan;
		this.tanggal_rencana = tanggal_rencana;
		this.id_user_input_rencana = id_user_input_rencana;
		this.keterangan = keterangan;
		this.aproved = aproved;
	}

	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id= id;
	}

	public int getId_rencana_header(){
		return this.id_rencana_header;
	}
	public void setId_rencana_header(int id_rencana_header){
		this.id_rencana_header=id_rencana_header;
	}

	public String getNomor_rencana(){
		return this.nomor_rencana;
	}
	public void setNomor_rencana(String nomor_rencana){
		this.nomor_rencana=nomor_rencana;
	}

	public String getTanggal_penetapan(){
		return this.tanggal_penetapan;
	}
	public void setTanggal_penetapan(String tanggal_penetapan){
		this.tanggal_penetapan= tanggal_penetapan;
	}

	public String getTanggal_rencana(){
		return this.tanggal_rencana;
	}
	public void setTanggal_rencana(String tanggal_rencana){
		this.tanggal_rencana=tanggal_rencana;
	}

	public int getId_user_input_rencana(){
		return this.id_user_input_rencana;
	}
	public void setId_user_input_rencana(int id_user_input_rencana){
		this.id_user_input_rencana=id_user_input_rencana;
	}

	public String getKeterangan(){
		return this.keterangan;
	}
	public void setKeterangan(String keterangan){
		this.keterangan=keterangan;
	}

	public String getAproved(){
		return this.aproved;
	}
	public void setAproved(String aproved){
		this.aproved=aproved;
	}
}
