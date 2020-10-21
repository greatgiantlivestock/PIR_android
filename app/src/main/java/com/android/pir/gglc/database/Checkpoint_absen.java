package com.android.pir.gglc.database;

public class Checkpoint_absen {

	//private variables
	int id_checkpoint;
	String nama_checkpoint;
	String lats;
	String longs;

	// Empty constructor
	public Checkpoint_absen(){

	}
	// constructor
	public Checkpoint_absen(int id_checkpoint, String nama_checkpoint, String lats, String longs){
		this.id_checkpoint = id_checkpoint;
		this.nama_checkpoint = nama_checkpoint;
		this.lats = lats;
		this.longs = longs;
	}

	// constructor
	public Checkpoint_absen(String nama_checkpoint, String lats, String longs){
		this.nama_checkpoint = nama_checkpoint;
		this.lats = lats;
		this.longs = longs;
	}
	// getting id_checkpoint
	public int getId_checkpoint(){
		return this.id_checkpoint;
	}
	
	// setting id_checkpoint
	public void setId_checkpoint(int id_checkpoint){
		this.id_checkpoint= id_checkpoint;
	}
	
	// getting nama checkpoint
	public String getNama_checkpoint(){
		return this.nama_checkpoint;
	}
	
	// setting nama checkpoint
	public void setNama_checkpoint(String nama_checkpoint){
		this.nama_checkpoint= nama_checkpoint;
	}
	
	// getting lats
	public String getLats(){
		return this.lats;
	}
	
	// setting lats
	public void setLats(String lats){
		this.lats= lats;
	}

	// getting longs
	public String getLongs(){
		return this.longs;
	}

	// setting longs
	public void setLongs(String longs){
		this.longs= longs;
	}
}
