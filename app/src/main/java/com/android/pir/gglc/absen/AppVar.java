package com.android.pir.gglc.absen;


import android.os.Environment;

import java.io.File;

public class AppVar {

    //URL to our login.php file, url bisa diganti sesuai dengan alamat server kita
    public static final String LOGIN_URL = "https://visit-pir.gg-livestock.com/login/login_member";
    public static final String LOGIN_URL1 = "https://visit-pir.gg-livestock.com/login/login_member1";
    public static final String HISTORY_URL = "https://visit-pir.gg-livestock.com/absen-android/absen/lihat_laporan_absen_mobile";
    public static final String GET_ABSEN = "https://visit-pir.gg-livestock.com/absen-android/absen/get/";
    public static final String POST_ABSEN = "https://visit-pir.gg-livestock.com/absen-android/absen/add";
    public static final String POST_CHECKIN = "https://visit-pir.gg-livestock.com/checkin/add";
    public static final String POST_CHECKIN_PROSPECT = "https://visit-pir.gg-livestock.com/checkin/add1";
    public static final String POST_CUSTOMER_TMP = "https://visit-pir.gg-livestock.com/checkin/addcustomer_tmp";
    public static final String POST_CHECKOUT = "https://visit-pir.gg-livestock.com/checkin/addcheckout";
    public static final String POST_UPDATE_PASSWORD= "https://visit-pir.gg-livestock.com/user/edit_password";
    public static final String SHARED_PREFERENCES_DISPLAY_FOTO_1 = "mahkota_display_foto_1";
    public static final String SHARED_PREFERENCES_DISPLAY_FOTO_2 = "mahkota_display_foto_2";
    public static final String SHARED_PREFERENCES_DISPLAY_FOTO_3 = "mahkota_display_foto_3";
    public static final String SHARED_PREFERENCES_CHILLER_FOTO_1 = "mahkota_chiller_foto_1";
    public static final String SHARED_PREFERENCES_KOMPETITOR_FOTO_1 = "mahkota_kompetitor_foto_1";
    public static final String SHARED_PREFERENCES_SPG_FOTO_1 = "mahkota_spg_foto_1";
    public static final String SHARED_PREFERENCES_CHECKIN = "mahkota_checkin";
    public static final String SHARED_PREFERENCES_CHECKIN_PROSPECT= "mahkota_checkin1";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_CHILLER = "insert_chiller_report.php";
    public static final String CONFIG_APP_URL_UPLOAD_STOCK_FISIK = "insert_stock_fisik.php";
    public static final String CONFIG_APP_URL_UPLOAD_PENJUALAN = "checkin/updateStock";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_DISPLAY = "insert_display_report.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_KOMPETITOR= "insert_kompetitor_report.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_SPG= "insert_spg_report.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_CHECKIN= "insert_checkin.php";
    public static final String CONFIG_APP_URL_UPLOAD_INSERT_CHECKIN_PROSPECT= "insert_checkin_prospect.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_MAC = "mac";

    //If server response is equal to this that means login is successful

    public static final String CONFIG_APP_URL_DOWNLOAD_CHECKPOINT = "absen-android/get_checkpoint.php";
    public static final String SHARED_PREFERENCES_TABLE_CHECKPOINT = "gglc_table_checkpoint";
    public static final String SHARED_PREFERENCES_TABLE_CHECKPOINT_SAME_DATA = "gglc_table_checkpoint_same_data";
    public static final String LOGIN_SUCCESS = "success";
    public static final String SHARED_PREFERENCES_NAME = "gglc";
    public static final String CONFIG_APP_URL_DOWNLOAD_CUSTOMER = "get_mst_customer.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_USER = "get_user.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_RENCANA_DETAIL = "get_rencana_detail.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_RENCANA_MASTER = "get_rencana_master.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_KEGIATAN = "get_kegiatan.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_JENIS_KENDARAAN = "get_jenis_kendaraan.php";
    public static final String SHARED_PREFERENCES_TABLE_MST_CUSTOMER = "gglc_table_customer";
    public static final String SHARED_PREFERENCES_TABLE_STOCK= "gglc_table_stock";
    public static final String SHARED_PREFERENCES_TABLE_MST_USER = "gglc_table_user";
    public static final String SHARED_PREFERENCES_TABLE_MST_KEGIATAN = "gglc_table_kegiatan";
    public static final String SHARED_PREFERENCES_TABLE_JENIS_KENDARAAN = "gglc_table_jenis_kendaraan";
    public static final String SHARED_PREFERENCES_TABLE_JENIS_KEGIATAN = "gglc_table_jenis_kegiatan";
    public static final String SHARED_PREFERENCES_TABLE_RENCANA_DETAIL = "gglc_table_rencana_detail";
    public static final String SHARED_PREFERENCES_TABLE_RENCANA_MASTER = "gglc_table_rencana_master";
    public static final String SHARED_PREFERENCES_TABLE_MST_CUSTOMER_SAME_DATA = "gglc_table_customer_same_data";
    public static final String SHARED_PREFERENCES_TABLE_STOCK_SAME_DATA = "gglc_table_stock_same_data";
    public static final String SHARED_PREFERENCES_TABLE_MST_USER_SAME_DATA = "gglc_table_user_same_data";
    public static final String SHARED_PREFERENCES_TABLE_MST_KEGIATAN_SAME_DATA = "gglc_table_kegiatan_same_data";
    public static final String SHARED_PREFERENCES_TABLE_JENIS_KENDARAAN_SAME_DATA = "gglc_table_jenis_kendaraan_same_data";
    public static final String SHARED_PREFERENCES_TABLE_RENCANA_DETAIL_SAME_DATA = "gglc_table_rencana_detail_same_data";
    public static final String SHARED_PREFERENCES_TABLE_RENCANA_MASTER_SAME_DATA = "gglc_table_rencana_master_same_data";
    public static final String SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER = "mahkota_table_customer_id_customer";
    public static final String SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING_ID_CANVASSING = "table_history_canvassing_id_canvssing";
    public static final String SHARED_PREFERENCES_TABLE_STOCK_VAN = "mahkota_table_stock_van";
    public static final String CONFIG_APP_FOLDER_CUSTOMER = "customer";
    public static final String CONFIG_APP_FOLDER_CUSTOMER_PROSPECT = "customer_checkin";
    public static final String CONFIG_APP_URL_PUBLIC = "https://visit-pir.gg-livestock.com/";
    public static final String SHARED_PREFERENCES_STAFF_ID_STAFF = "mahkota_staff_id_staff";
    public static final String SHARED_PREFERENCES_STAFF_LEVEL = "mahkota_staff_level";
    public static final String CONFIG_APP_URL_DOWNLOAD_PRODUCT = "get_product.php";
    public static final String SHARED_PREFERENCES_TABLE_PRODUCT = "ggl_table_product";
    public static final String SHARED_PREFERENCES_TABLE_PRODUCT_SAME_DATA = "ggl_table_product_same_data";
    public static final String SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_JADWAL = "mahkota_table_jadwal_detail_jadwal";
    public static final String SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_NAMA = "mahkota_table_jadwal_detail_nama_customer";
    public static final String SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_ALAMAT = "mahkota_table_jadwal_detail_alamat_customer";
    public static final String SHARED_PREFERENCES_TABLE_JADWAL_DETAIL_STATUS = "mahkota_table_jadwal_detail_status_kunjungan";
    public static final String CONFIG_APP_URL_DOWNLOAD_ABSEN_HISTORY = "get_absen_history.php";
    public static final String CONFIG_APP_URL_DOWNLOAD_CANVASSING_HISTORY = "get_canvassing_history.php";
    public static final String SHARED_PREFERENCES_TABLE_CUSTOMER = "mahkota_table_customer";
    public static final String SHARED_PREFERENCES_TABLE_CUSTOMER_SAME_DATA = "mahkota_table_customer_same_data";
    public static final String SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING = "table_history_canvassing";
    public static final String SHARED_PREFERENCES_TABLE_HISTORY_CANVASSING_SAME_DATA = "table_history_canvassing_same_data";
    public static final String CONFIG_APP_URL_UPLOAD_LOCATOR = "web_flg_mas/ws/update_locator_sales.php";
    public static final String CONFIG_APP_ERROR_MESSAGE_ADDRESS_FAILED = "Alamat Tidak Terdeteksi";

    public static final String APPLICATION_PACKAGE_NAME = "com.android.canvasing.mobile";

    public static String getFolderPath() {
        StringBuilder path_builder = new StringBuilder();
        path_builder.append(File.separatorChar);
        path_builder.append("Android");
        path_builder.append(File.separatorChar);
        path_builder.append("data");
        path_builder.append(File.separatorChar);
        path_builder.append(APPLICATION_PACKAGE_NAME);
        String temp_middle_path = path_builder.toString();

        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + temp_middle_path;

    }
}
