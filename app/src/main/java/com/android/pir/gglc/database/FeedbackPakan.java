package com.android.pir.gglc.database;

public class FeedbackPakan {

	//private variables
	int id_feedback;
	String id_rencana_detail;
	String feedback_pakan;
	String foto;

	// Empty constructor
	public FeedbackPakan(){

	}
	// constructor
	public FeedbackPakan(int id_feedback, String id_rencana_detail, String feedback_pakan, String foto){
		this.id_feedback= id_feedback;
		this.id_rencana_detail= id_rencana_detail;
		this.feedback_pakan= feedback_pakan;
		this.foto= foto;
	}

	// constructor
	public FeedbackPakan(String id_rencana_detail, String feedback_pakan, String foto){
		this.id_rencana_detail= id_rencana_detail;
		this.feedback_pakan= feedback_pakan;
		this.foto= foto;
	}

	public int getId_feedback(){
		return this.id_feedback;
	}
	public void setId_feedback(int id_feedback){
		this.id_feedback=id_feedback;
	}

	public String getId_rencana_detail(){
		return this.id_rencana_detail;
	}
	public void setId_rencana_detail(String id_rencana_detail){
		this.id_rencana_detail= id_rencana_detail;
	}

	public String getFeedback_pakan(){
		return this.feedback_pakan;
	}
	public void setFeedback_pakan(String feedback_pakan){
		this.feedback_pakan= feedback_pakan;
	}

	public String getFoto(){
		return this.foto;
	}
	public void setFoto(String foto){
		this.foto= foto;
	}

}
