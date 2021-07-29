package com.android.pir.gglc.absen;

import android.os.Environment;

import java.io.File;

public class AppVar {
    //URL to our login.php file, url bisa diganti sesuai dengan alamat server kita
    public static final String LOGIN_URL = "https://dev-pir-visit.gg-livestock.com/login/login_member";
    public static final String LOGIN_URL1 = "https://dev-pir-visit.gg-livestock.com/login/login_member1";
    public static final String POST_CHECKIN = "https://dev-pir-visit.gg-livestock.com/checkin/add";
    public static final String POST_CHECKIN_PROSPECT = "https://dev-pir-visit.gg-livestock.com/checkin/add1";
    public static final String POST_NEW_CHECKIN = "https://dev-pir-visit.gg-livestock.com/checkin/addNewCheckin";
    public static final String POST_CUSTOMER_TMP = "https://dev-pir-visit.gg-livestock.com/checkin/addcustomer_tmp";
    public static final String POST_CHECKOUT = "https://dev-pir-visit.gg-livestock.com/checkin/addcheckout";
    public static final String POST_UPDATE_PASSWORD= "https://dev-pir-visit.gg-livestock.com/user/edit_password";
    public static final String SHARED_PREFERENCES_DISPLAY_FOTO_1 = "mahkota_display_foto_1";
    public static final String SHARED_PREFERENCES_DISPLAY_FOTO_2 = "mahkota_display_foto_2";
    public static final String SHARED_PREFERENCES_CHILLER_FOTO_1 = "mahkota_chiller_foto_1";
    public static final String SHARED_PREFERENCES_EARTAG = "pir_eartag";
    public static final String SHARED_PREFERENCES_KETERANGAN_CHECKOUT = "pir_keterangan_checkout";
    public static final String SHARED_PREFERENCES_EARTAG1 = "pir_eartag1";
    public static final String SHARED_PREFERENCES_EARTAG2 = "pir_eartag2";
    public static final String SHARED_PREFERENCES_EARTAG3 = "pir_eartag3";
    public static final String SHARED_PREFERENCES_KETERANGAN = "pir_keterangan";
    public static final String SHARED_PREFERENCES_KETERANGAN_PAKAN = "pir_keterangan_pakan";
    public static final String SHARED_PREFERENCES_KETERANGAN1 = "pir_keterangan1";
    public static final String SHARED_PREFERENCES_KETERANGAN2 = "pir_keterangan2";
    public static final String SHARED_PREFERENCES_KETERANGAN3 = "pir_keterangan3";
    public static final String SHARED_PREFERENCES_PIR_FOTO_OBAT = "pir_foto_obat";
    public static final String SHARED_PREFERENCES_COUNT_SAVED_OBAT = "count_saved_obat";
    public static final String SHARED_PREFERENCES_PIR_FOTO_PAKAN = "pir_foto_pakan";
    public static final String SHARED_PREFERENCES_PIR_FOTO_1 = "pir_foto_1";
    public static final String SHARED_PREFERENCES_PIR_FOTO_2 = "pir_foto_2";
    public static final String SHARED_PREFERENCES_PIR_FOTO_3 = "pir_foto_3";
    public static final String SHARED_PREFERENCES_PIR_FOTO_4 = "pir_foto_4";
    public static final String SHARED_PREFERENCES_ASSESSMENT = "pir_assessment";
    public static final String SHARED_PREFERENCES_CHECKIN = "mahkota_checkin";
    public static final String SHARED_PREFERENCES_CHECKIN_PROSPECT= "mahkota_checkin1";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_CHILLER = "insert_chiller_report.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_CHECKIN_NEW = "insert_checkin_new.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_CHECKOUT = "insert_checkout_new.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_DATA_SAPI = "insert_data_sapi.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_PAKAN = "insert_pakan_feedback.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_DATA_SAPI_ALL = "insert_data_sapi_new.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_DATA_PENGOBATAN = "insert_data_pengobatan.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_DATA_PAKAN = "insert_pakan_feedback.php";
    public static final String CONFIG_APP_URL_UPLOAD_STOCK_FISIK = "insert_stock_fisik.php";
    public static final String CONFIG_APP_URL_UPLOAD_PENJUALAN = "checkin/updateStock";
    public static final String CONFIG_APP_URL_UPLOAD_RENCANA = "Rencana/create_header";
    public static final String CONFIG_APP_URL_UPLOAD_RENCANA_URGENT = "Rencana/create_header_urgent";
    public static final String CONFIG_APP_URL_UPLOAD_RENCANA_DETAIL = "Rencana/create_detail";
    public static final String CONFIG_APP_URL_UPLOAD_RENCANA_DETAIL_URGENT = "Rencana/create_detail_urgent";
    public static final String CONFIG_APP_URL_UPLOAD_RENCANA_DETAIL_URGENT_PROSPECT = "Rencana/create_detail_urgent_prospect";
    public static final String CONFIG_APP_URL_UPLOAD_RENCANA_EDIT = "Rencana/create_header_edit";
    public static final String CONFIG_APP_URL_UPLOAD_RENCANA_DETAIL_EDIT = "Rencana/create_detail_edit";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_DISPLAY = "insert_display_report.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_CHECKIN= "insert_checkin.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_CHECKIN_PROSPECT= "insert_checkin_prospect.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_MAC = "mac";

    //If server response is equal to this that means login is successful

    public static final String LOGIN_SUCCESS = "success";
    public static final String SHARED_PREFERENCES_NAME = "gglc";
    public static final String CONFIG_APP_URL_DOWNLOAD_CUSTOMER = "get_mst_customer.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_CUSTOMER_NEW = "get_customer_new.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_USER = "get_user.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_RENCANA_DETAIL = "get_rencana_detail.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_RENCANA_DETAIL_APROVED = "get_rencana_detail_aproved.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_DATA_SAPI = "get_data_sapi.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_DATA_OBAT = "get_obat.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_CHECKIN = "get_checkin.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_RENCANA_DETAIL_PARAM = "get_rencana_detail_param.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_RENCANA_MASTER = "get_rencana_master.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_RENCANA_MASTER_ONLY = "get_rencana_master_only.php";
    public static final String SHARED_PREFERENCES_TABLE_MST_CUSTOMER = "gglc_table_customer";
    public static final String SHARED_PREFERENCES_TABLE_PAKAN = "gglc_table_pakan";
    public static final String SHARED_PREFERENCES_TABLE_STOCK= "gglc_table_stock";
    public static final String SHARED_PREFERENCES_TABLE_MST_USER = "gglc_table_user";
    public static final String SHARED_PREFERENCES_TABLE_RENCANA_DETAIL = "gglc_table_rencana_detail";
    public static final String SHARED_PREFERENCES_KETERANGAN_PETANI = "keterangan_petani";
    public static final String SHARED_PREFERENCES_TABLE_DATA_SAPI = "gglc_table_data_sapi";
    public static final String SHARED_PREFERENCES_TABLE_DATA_OBAT = "gglc_table_data_obat";
    public static final String SHARED_PREFERENCES_TABLE_DATA_SAPI_SAME_DATA = "gglc_table_data_sapi_same_data";
    public static final String SHARED_PREFERENCES_TABLE_DATA_OBAT_SAME_DATA = "gglc_table_data_obat_same_data";
    public static final String SHARED_PREFERENCES_TABLE_CHECKIN = "gglc_table_checkin";
    public static final String SHARED_PREFERENCES_TABLE_RENCANA_MASTER = "gglc_table_rencana_master";
    public static final String SHARED_PREFERENCES_TABLE_MST_CUSTOMER_SAME_DATA = "gglc_table_customer_same_data";
    public static final String SHARED_PREFERENCES_TABLE_PAKAN_SAME_DATA = "gglc_table_pakan_same_data";
    public static final String SHARED_PREFERENCES_TABLE_STOCK_SAME_DATA = "gglc_table_stock_same_data";
    public static final String SHARED_PREFERENCES_TABLE_MST_USER_SAME_DATA = "gglc_table_user_same_data";
    public static final String SHARED_PREFERENCES_TABLE_RENCANA_DETAIL_SAME_DATA = "gglc_table_rencana_detail_same_data";
    public static final String SHARED_PREFERENCES_TABLE_CHECKIN_SAME_DATA = "gglc_table_checkin_same_data";
    public static final String SHARED_PREFERENCES_TABLE_RENCANA_MASTER_SAME_DATA = "gglc_table_rencana_master_same_data";
    public static final String SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING_ID_CANVASSING = "table_history_canvassing_id_canvssing";
    public static final String SHARED_PREFERENCES_TABLE_PRODUCT_SAME_DATA = "ggl_table_product_same_data";
    public static final String CONFIG_APP_URL_DOWNLOAD_PRODUCT = "get_product.php";
    public static final String SHARED_PREFERENCES_TABLE_STOCK_VAN = "mahkota_table_stock_van";
    public static final String CONFIG_APP_FOLDER_CUSTOMER = "customer";
    public static final String CONFIG_APP_FOLDER_CUSTOMER_PROSPECT = "customer_checkin";
    public static final String CONFIG_APP_URL_PUBLIC = "https://dev-pir-visit.gg-livestock.com/";
    public static final String SHARED_PREFERENCES_STAFF_ID_STAFF = "mahkota_staff_id_staff";
    public static final String SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL = "mahkota_table_jadwal_detail_jadwal";
    public static final String SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_NAMA = "mahkota_table_jadwal_detail_nama_customer";
    public static final String SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_ALAMAT = "mahkota_table_jadwal_detail_alamat_customer";
    public static final String SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_STATUS = "mahkota_table_jadwal_detail_status_kunjungan";
    public static final String SHARED_PREFERENCES_TABLE_INDEX_NUMBER = "index_number_rencana";
    public static final String CONFIG_APP_URL_DOWNLOAD_CANVASSING_HISTORY = "get_canvassing_history.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_PAKAN = "get_pakan.php";
    public static final String SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING = "table_history_canvassing";
    public static final String SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING_SAME_DATA = "table_history_canvassing_same_data";
    public static final String SHARED_PREFERENCES_LATS = "lats_checkin";
    public static final String SHARED_PREFERENCES_LONGS = "longs_checkin";

    public static final String APPLICATION_PACKAGE_NAME = "com.android.canvasing.mobile";

    public static String getFolderPath() {
        StringBuilder path_builder = new StringBuilder();
        path_builder.append(File.separatorChar);
        path_builder.append("Visit");
        path_builder.append(File.separatorChar);
        path_builder.append(APPLICATION_PACKAGE_NAME);
        String temp_middle_path = path_builder.toString();

        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + temp_middle_path;
    }
    public static String getFolderPatha() {
        StringBuilder path_builder = new StringBuilder();
        path_builder.append(File.separatorChar);
        path_builder.append("visit_pir");
        path_builder.append(File.separatorChar);
        path_builder.append(APPLICATION_PACKAGE_NAME);
        String temp_middle_path = path_builder.toString();

        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + temp_middle_path;
    }
}
