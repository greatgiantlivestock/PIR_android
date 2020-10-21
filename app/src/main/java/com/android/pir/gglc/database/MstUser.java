package com.android.pir.gglc.database;

public class MstUser {

	//private variables
	int id_user;
	String nama;
	String username;
	String password;
	int id_departemen;
	int id_wilayah;
	int id_karyawan;
	String no_hp;
	int id_role;

	// Empty constructor
	public MstUser(){

	}
	// constructor
	public MstUser(int id_user, String nama, String username, String password, int id_departemen, int id_wilayah , int id_karyawan,
				   String no_hp, int id_role){
		this.id_user= id_user;
		this.nama = nama;
		this.username = username;
		this.password = password;
		this.id_departemen = id_departemen;
		this.id_wilayah = id_wilayah;
		this.id_karyawan = id_karyawan;
		this.no_hp= no_hp;
		this.id_role= id_role;
	}

	// constructor
	public MstUser(String nama, String username, String password, int id_departemen, int id_wilayah, int id_karyawan, String no_hp, int id_role){
		this.nama = nama;
		this.username = username;
		this.password = password;
		this.id_departemen = id_departemen;
		this.id_wilayah = id_wilayah;
		this.id_karyawan = id_karyawan;
		this.no_hp= no_hp;
		this.id_role= id_role;
	}

	public int getId_user(){
		return this.id_user;
	}
	public void setId_user(int id_user){
		this.id_user=id_user;
	}

	public String getNama(){
		return this.nama;
	}
	public void setNama(String nama){
		this.nama= nama;
	}

	public String getUsername(){
		return this.username;
	}
	public void setUsername(String username){
		this.username= username;
	}

	public String getPassword(){
		return this.password;
	}
	public void setPassword(String password){
		this.password= password;
	}

	public int getId_departemen(){
		return this.id_departemen;
	}
	public void setId_departemen(int id_departemen){
		this.id_departemen= id_departemen;
	}

	public int getId_wilayah(){
		return this.id_wilayah;
	}
	public void setId_wilayah(int id_wilayah){
		this.id_wilayah= id_wilayah;
	}

	public int getId_karyawan(){
		return this.id_karyawan;
	}
	public void setId_karyawan(int id_karyawan){
		this.id_karyawan= id_karyawan;
	}

	public String getNo_hp(){
		return this.no_hp;
	}
	public void setNo_hp(String no_hp){
		this.no_hp= no_hp;
	}

	public int getId_role(){
		return this.id_role;
	}
	public void setId_role(int id_role){
		this.id_role= id_role;
	}
}
