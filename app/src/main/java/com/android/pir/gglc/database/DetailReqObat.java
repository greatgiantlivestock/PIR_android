package com.android.pir.gglc.database;

public class DetailReqObat {
	private int id_obat;
	private String kode_obat;
	private String nama_obat;
	private int qty_obat;
	private String unit;

	// constructor
	public DetailReqObat(int id_obat, String kode_obat, String nama_obat, int qty_obat, String unit) {
		this.id_obat= id_obat;
		this.kode_obat = kode_obat;
		this.nama_obat = nama_obat;
		this.qty_obat = qty_obat;
		this.unit= unit;
	}

	public DetailReqObat(){

	}

	public int getId_obat() {
		return id_obat;
	}

	public void setId_obat(int id_obat) {
		this.id_obat= id_obat;
	}

	public String getKode_obat() {
		return kode_obat;
	}

	public void setKode_obat(String kode_obat) {
		this.kode_obat= kode_obat;
	}

	public String getNama_obat() {
		return nama_obat;
	}

	public void setNama_obat(String nama_obat) {
		this.nama_obat= nama_obat;
	}

	public int getQty_obat() {
		return qty_obat;
	}

	public void setQty_obat(int qty_obat) {
		this.qty_obat= qty_obat;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}