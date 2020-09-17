/*
  Dilarang keras menggandakan/mengcopy/menyebarkan/membajak/mendecompile 
  Software ini dalam bentuk apapun tanpa seijin pembuat software
  (Khanza.Soft Media). Bagi yang sengaja membajak softaware ini ta
  npa ijin, kami sumpahi sial 1000 turunan, miskin sampai 500 turu
  nan. Selalu mendapat kecelakaan sampai 400 turunan. Anak pertama
  nya cacat tidak punya kaki sampai 300 turunan. Susah cari jodoh
  sampai umur 50 tahun sampai 200 turunan. Ya Alloh maafkan kami 
  karena telah berdoa buruk, semua ini kami lakukan karena kami ti
  dak pernah rela karya kami dibajak tanpa ijin.
 */

package simrskhanza;

import surat.SuratKontrol;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;
import inventory.DlgPemberianObat;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import inventory.DlgCariObat;
import inventory.DlgCopyResep;
import inventory.DlgPeresepanDokter;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import keuangan.DlgJnsPerawatanRalan;
import laporan.DlgBerkasRawat;
import rekammedis.RMDataResumePasien;
import permintaan.DlgPermintaanLaboratorium;
import permintaan.DlgPermintaanRadiologi;
import rekammedis.DataTriaseIGD;

/**
 *
 * @author dosen
 */
public final class DlgRawatJalan extends javax.swing.JDialog {
    private final DefaultTableModel tabModeDr,tabModePr,tabModeDrPr,
            tabModePemeriksaan,tabModeObstetri,tabModeGinekologi,
            TabModeTindakan,TabModeTindakan2,TabModeTindakan3,TabModeCatatan;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private DlgPasien pasien=new DlgPasien(null,false);
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    public  DlgCariPetugas petugas=new DlgCariPetugas(null,false);       
    private PreparedStatement ps,ps2,ps3,ps4,ps5,ps6,pstindakan,psset_tarif;
    private ResultSet rs,rstindakan,rsset_tarif;
    private int i=0,jmlparsial=0,jml=0,index=0;
    private String aktifkanparsial="no",kode_poli="",kd_pj="",poli_ralan="No",cara_bayar_ralan="No";
    private final Properties prop = new Properties();
    private boolean[] pilih; 
    private String[] kode,nama,kategori;
    private double[] totaltnd,bagianrs,bhp,jmdokter,jmperawat,kso,menejemen;

    /** Creates new form DlgPerawatan
     * @param parent
     * @param modal */
    public DlgRawatJalan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(8,1);
        setSize(885,674);
        tabModeDr=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Perawatan/Tindakan","Kode Dokter",
            "Dokter Yg Menangani","Tgl.Rawat","Jam Rawat","Biaya","Kode","Tarif Dokter",
            "KSO"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRawatDr.setModel(tabModeDr);
        //tampilDr();

        tbRawatDr.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRawatDr.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 13; i++) {
            TableColumn column = tbRawatDr.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(180);
            }else if(i==4){
                column.setPreferredWidth(180);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(180);
            }else if(i==7){
                column.setPreferredWidth(80);
            }else if(i==8){
                column.setPreferredWidth(75);
            }else if(i==9){
                column.setPreferredWidth(90);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==11){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==12){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbRawatDr.setDefaultRenderer(Object.class, new WarnaTable());

        tabModePr=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Perawatan/Tindakan","NIP",
            "Petugas Yg Menangani","Tgl.Rawat","Jam Rawat","Biaya","Kode","Tarif Perawat",
            "KSO"}){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRawatPr.setModel(tabModePr);
        tbRawatPr.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRawatPr.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 13; i++) {
            TableColumn column = tbRawatPr.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(180);
            }else if(i==4){
                column.setPreferredWidth(180);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(180);
            }else if(i==7){
                column.setPreferredWidth(80);
            }else if(i==8){
                column.setPreferredWidth(75);
            }else if(i==9){
                column.setPreferredWidth(90);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==11){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==12){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbRawatPr.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeDrPr=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Perawatan/Tindakan","Kode Dokter",
            "Dokter Yg Menangani","NIP","Petugas Yg Menangani","Tgl.Rawat","Jam Rawat",
            "Biaya","Kode","Tarif Dokter","Tarif Petugas","KSO"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRawatDrPr.setModel(tabModeDrPr);
        tbRawatDrPr.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRawatDrPr.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 16; i++) {
            TableColumn column = tbRawatDrPr.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(180);
            }else if(i==4){
                column.setPreferredWidth(180);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(180);
            }else if(i==7){
                column.setPreferredWidth(90);
            }else if(i==8){
                column.setPreferredWidth(180);
            }else if(i==9){
                column.setPreferredWidth(80);
            }else if(i==10){
                column.setPreferredWidth(75);
            }else if(i==11){
                column.setPreferredWidth(90);
            }else if(i==12){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==13){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==14){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==15){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbRawatDrPr.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModePemeriksaan=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Tgl.Rawat","Jam Rawat","Suhu(C)","Tensi","Nadi(/menit)",
            "Respirasi(/menit)","Tinggi(Cm)","Berat(Kg)","GCS(E,V,M)","Keluhan Utama","Keluhan Tambahan",
            "Riwayat Penyakit Sekarang","Kepala","Abnomal Kepala","Leher","Abnormal Leher","Thorax","Abnormal Thorax",
            "Abdomen","Abnormal Abdomen","Genital","Abnormal Genital","Rektum","Abnormal Rektum","Extremitas","Abnormal Extremitas",
            "Pemeriksaan","Alergi", "Imun Ke","Kesadaran","Keadaan Umum","Tindak Lanjut","Penilaian"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                 java.lang.Object.class
                 
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbPemeriksaan.setModel(tabModePemeriksaan);
        tbPemeriksaan.setPreferredScrollableViewportSize(new Dimension(500,30));
        tbPemeriksaan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 36; i++) {
            TableColumn column = tbPemeriksaan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(180);
            }else if(i==4){
                column.setPreferredWidth(180);
            }else if(i==5){
                column.setPreferredWidth(180);
            }else if(i==6){
                column.setPreferredWidth(180);
            }else if(i==7){
                column.setPreferredWidth(180);
            }else if(i==8){
                column.setPreferredWidth(180);
            }else if(i==9){
                column.setPreferredWidth(180);
            }else if(i==10){
                column.setPreferredWidth(180);
            }else if(i==11){
                column.setPreferredWidth(180);
            }else if(i==12){
                column.setPreferredWidth(180);
            }else if(i==13){
                column.setPreferredWidth(180);
            }else if(i==14){
                column.setPreferredWidth(180);
            }else if(i==15){
                column.setPreferredWidth(180);
            }else if(i==16){
                column.setPreferredWidth(180);
            }else if(i==17){
                column.setPreferredWidth(180);
            }else if(i==18){
                column.setPreferredWidth(180);
            }else if(i==19){
                column.setPreferredWidth(180);
            }else if(i==20){
                column.setPreferredWidth(180);
            }else if(i==21){
                column.setPreferredWidth(180);
            }else if(i==22){
                column.setPreferredWidth(180);
            }else if(i==23){
                column.setPreferredWidth(180);
            }else if(i==24){
                column.setPreferredWidth(180);
            }else if(i==25){
                column.setPreferredWidth(180);
            }else if(i==26){
                column.setPreferredWidth(180);
            }else if(i==27){
                column.setPreferredWidth(180);
            }else if(i==28){
                column.setPreferredWidth(180);
            }else if(i==29){
                column.setPreferredWidth(180);
            }else if(i==30){
                column.setPreferredWidth(180);
            }else if(i==31){
                column.setPreferredWidth(180);
            }else if(i==32){
                column.setPreferredWidth(180);
            }else if(i==33){
                column.setPreferredWidth(180);
            }else if(i==34){
                column.setPreferredWidth(180);
            }else if(i==35){
                column.setPreferredWidth(180);
            }else if(i==36){
                column.setPreferredWidth(180);
            }
        }
        tbPemeriksaan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeObstetri=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M","Nama Pasien","Tgl.Rawat","Jam Rawat",
            "Tinggi Fundus","Janin", "Letak","Panggul","Denyut","Kontraksi",
            "Kualitas Mnt", "Kualitas Detik","Fluksus","Albus","Vulva",
            "Portio","Dalam","Tebal","Arah","Pembukaan","Penurunan",
            "Denominator","Ketuban","Feto"}) {
             @Override public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
            }
            Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Object.class,java.lang.Object.class,
            };
            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        };
        
        tbPemeriksaanObstetri.setModel(tabModeObstetri);
        tbPemeriksaanObstetri.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbPemeriksaanObstetri.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for (i = 0; i < 26; i++) {
            TableColumn column = tbPemeriksaanObstetri.getColumnModel().getColumn(i);
            if(i==0) {
                column.setPreferredWidth(20);
            }else if(i==1) {
                column.setPreferredWidth(105);
            }else if(i==2) {
                column.setPreferredWidth(70);
            }else if(i==3) {
                column.setPreferredWidth(180);
            }else if(i==4) {
                column.setPreferredWidth(80);
            }else if(i==5) {
                column.setPreferredWidth(70);
            }else if(i==6) {
                column.setPreferredWidth(80);
            }else if(i==7) {
                column.setPreferredWidth(60);
            }else if(i==8) {
                column.setPreferredWidth(60);
            }else if(i==9) {
                column.setPreferredWidth(60);
            }else if(i==10) {
                column.setPreferredWidth(60);
            }else if(i==11) {
                column.setPreferredWidth(60);
            }else if(i==12) {
                column.setPreferredWidth(70);
            }else if(i==13) {
                column.setPreferredWidth(80);
            }else if(i==14) {
                column.setPreferredWidth(50);
            }else if(i==15) {
                column.setPreferredWidth(40);
            }else if(i==16) {
                column.setPreferredWidth(170);
            }else if(i==17) {
                column.setPreferredWidth(170);
            }else if(i==18) {
                column.setPreferredWidth(60);
            }else if(i==19) {
                column.setPreferredWidth(50);
            }else if(i==20) {
                column.setPreferredWidth(60);
            }else if(i==21) {
                column.setPreferredWidth(170);
            }else if(i==22) {
                column.setPreferredWidth(170);
            }else if(i==23) {
                column.setPreferredWidth(170);
            }else if(i==24) {
                column.setPreferredWidth(50);
            }else if(i==25) {
                column.setPreferredWidth(70);
            }
        }
        tbPemeriksaanObstetri.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeGinekologi=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M","Nama Pasien","Tgl.Rawat","Jam Rawat",
            "Inpeksi","Inspeksi Vulva/Uretra/Vagina", "Inspekulo","Fluxus",
            "Fluor Albus", "Inspekulo Vulva/Vagina", "Inspekulo Portio", "Inspekulo Sondage",
            "Pemeriksaan Dalam Portio", "Pemeriksaan Dalam Bentuk","Pemeriksaan Dalam Cavum Uteri","Mobilitas",
            "Ukuran Cavum Uteri","Nyeri Tekan","Pemeriksaan Dalam Adnexa Kanan","Pemeriksaan Dalam Adnexa Kiri",
            "Pemeriksaan Dalam Cavum Douglas"}) {
             @Override public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
            }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class,java.lang.Object.class
                 
             };
            @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
                }
            };
        
        tbPemeriksaanGinekologi.setModel(tabModeGinekologi);
        tbPemeriksaanGinekologi.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbPemeriksaanGinekologi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for (i = 0; i < 23; i++) {
            TableColumn column = tbPemeriksaanGinekologi.getColumnModel().getColumn(i);
            if(i==0) {
                column.setPreferredWidth(20);
            }else if(i==1) {
                column.setPreferredWidth(105);
            }else if(i==2) {
                column.setPreferredWidth(70);
            }else if(i==3) {
                column.setPreferredWidth(180);
            }else if(i==4) {
                column.setPreferredWidth(80);
            }else if(i==5) {
                column.setPreferredWidth(70);
            }else if(i==6) {
                column.setPreferredWidth(200);
            }else if(i==7) {
                column.setPreferredWidth(200);
            }else if(i==8) {
                column.setPreferredWidth(200);
            }else if(i==9) {
                column.setPreferredWidth(42);
            }else if(i==10) {
                column.setPreferredWidth(62);
            }else if(i==11) {
                column.setPreferredWidth(200);
            }else if(i==12) {
                column.setPreferredWidth(200);
            }else if(i==13) {
                column.setPreferredWidth(200);
            }else if(i==14) {
                column.setPreferredWidth(200);
            }else if(i==15) {
                column.setPreferredWidth(200);
            }else if(i==16) {
                column.setPreferredWidth(200);
            }else if(i==17) {
                column.setPreferredWidth(50);
            }else if(i==18) {
                column.setPreferredWidth(200);
            }else if(i==19) {
                column.setPreferredWidth(67);
            }else if(i==20) {
                column.setPreferredWidth(200);
            }else if(i==21) {
                column.setPreferredWidth(200);
            }else if(i==22) {
                column.setPreferredWidth(200);
            }  
        }
        tbPemeriksaanGinekologi.setDefaultRenderer(Object.class, new WarnaTable());
        
        TabModeTindakan=new DefaultTableModel(null,new Object[]{"P","Kode","Nama Perawatan","Kategori Perawatan","Tarif/Biaya","Bagian RS","BHP","JM Dokter","JM Perawat","KSO","Menejemen"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,  
                java.lang.Object.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class
             };
             /*Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };*/
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbTindakan.setModel(TabModeTindakan);
        tbTindakan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbTindakan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 11; i++) {
            TableColumn column = tbTindakan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(90);
            }else if(i==2){
                column.setPreferredWidth(420);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==6){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==7){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==8){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==9){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else{
                column.setPreferredWidth(90);
            }
        }
        tbTindakan.setDefaultRenderer(Object.class, new WarnaTable());

        TabModeTindakan2=new DefaultTableModel(null,new Object[]{"P","Kode","Nama Perawatan","Kategori Perawatan","Tarif/Biaya","Bagian RS","BHP","JM Dokter","JM Perawat","KSO","Menejemen"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,  
                java.lang.Object.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class
             };
             /*Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };*/
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbTindakan2.setModel(TabModeTindakan2);
        tbTindakan2.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbTindakan2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 11; i++) {
            TableColumn column = tbTindakan2.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(90);
            }else if(i==2){
                column.setPreferredWidth(420);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==6){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==7){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==8){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==9){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else{
                column.setPreferredWidth(90);
            }
        }
        tbTindakan2.setDefaultRenderer(Object.class, new WarnaTable());
        
        TabModeTindakan3=new DefaultTableModel(null,new Object[]{"P","Kode","Nama Perawatan","Kategori Perawatan","Tarif/Biaya","Bagian RS","BHP","JM Dokter","JM Perawat","KSO","Menejemen"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,  
                java.lang.Object.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class
             };
             /*Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };*/
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbTindakan3.setModel(TabModeTindakan3);
        tbTindakan3.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbTindakan3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 11; i++) {
            TableColumn column = tbTindakan3.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(90);
            }else if(i==2){
                column.setPreferredWidth(420);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==6){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==7){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==8){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==9){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else{
                column.setPreferredWidth(90);
            }
        }
        tbTindakan3.setDefaultRenderer(Object.class, new WarnaTable());
        
        TabModeCatatan=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Tanggal","Jam","Kode Dokter","Nama Dokter","Catatan"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,java.lang.Object.class,
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbCatatan.setModel(TabModeCatatan);
        tbCatatan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbCatatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 9; i++) {
            TableColumn column = tbCatatan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(180);
            }else if(i==4){
                column.setPreferredWidth(80);
            }else if(i==5){
                column.setPreferredWidth(75);
            }else if(i==6){
                column.setPreferredWidth(80);
            }else if(i==7){
                column.setPreferredWidth(150);
            }else if(i==8){
                column.setPreferredWidth(700);
            }
        }
        tbCatatan.setDefaultRenderer(Object.class, new WarnaTable());
        
        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        kdptg.setDocument(new batasInput((byte)20).getKata(kdptg));
        kdptg2.setDocument(new batasInput((byte)20).getKata(kdptg2));
        KdDok.setDocument(new batasInput((byte)20).getKata(KdDok));
        KdDok2.setDocument(new batasInput((byte)20).getKata(KdDok2));
        TSuhu.setDocument(new batasInput((byte)5).getKata(TSuhu));
        TTensi.setDocument(new batasInput((byte)7).getKata(TTensi));
        TKeluhan.setDocument(new batasInput((int)400).getKata(TKeluhan));
        TPemeriksaan.setDocument(new batasInput((int)400).getKata(TPemeriksaan));
        TPenilaian.setDocument(new batasInput((int)400).getKata(TPenilaian));     
        TAlergi.setDocument(new batasInput((int)50).getKata(TAlergi));      
        TKeluhanUtama.setDocument(new batasInput((int)100).getKata(TKeluhanUtama));     
        TKeluhanTambahan.setDocument(new batasInput((int)100).getKata(TKeluhanTambahan));     
        TCari.setDocument(new batasInput((int)100).getKata(TCari));       
        TGCS.setDocument(new batasInput((byte)10).getKata(TGCS));
        TTinggi.setDocument(new batasInput((byte)5).getKata(TTinggi));
        TBerat.setDocument(new batasInput((byte)5).getKata(TBerat));
        TindakLanjut.setDocument(new batasInput((int)400).getKata(TindakLanjut));
        TNadi.setDocument(new batasInput((byte)3).getOnlyAngka(TNadi));
        TRespirasi.setDocument(new batasInput((byte)3).getOnlyAngka(TRespirasi));
        TTinggi_uteri.setDocument(new batasInput((byte)5).getKata(TTinggi_uteri));
        TLetak.setDocument(new batasInput((byte)50).getKata(TLetak));
        TDenyut.setDocument(new batasInput((byte)5).getKata(TDenyut));
        TKualitas_dtk.setDocument(new batasInput((byte)5).getKata(TKualitas_dtk));
        TKualitas_mnt.setDocument(new batasInput((byte)5).getKata(TKualitas_mnt));
        TVulva.setDocument(new batasInput((byte)50).getKata(TVulva));
        TPortio.setDocument(new batasInput((byte)50).getKata(TPortio));
        TTebal.setDocument(new batasInput((byte)5).getKata(TTebal));
        TPembukaan.setDocument(new batasInput((byte)50).getKata(TPembukaan));
        TPenurunan.setDocument(new batasInput((byte)50).getKata(TPenurunan));
        TDenominator.setDocument(new batasInput((byte)50).getKata(TDenominator));
        TInspeksi.setDocument(new batasInput((byte)50).getKata(TInspeksi));
        TInspeksiVulva.setDocument(new batasInput((byte)50).getKata(TInspeksiVulva));
        TInspekuloGine.setDocument(new batasInput((byte)50).getKata(TInspekuloGine));
        TVulvaInspekulo.setDocument(new batasInput((byte)50).getKata(TVulvaInspekulo));
        TPortioInspekulo.setDocument(new batasInput((byte)50).getKata(TPortioInspekulo));
        TSondage.setDocument(new batasInput((byte)50).getKata(TSondage));
        TPortioDalam.setDocument(new batasInput((byte)50).getKata(TPortioDalam));
        TBentuk.setDocument(new batasInput((byte)50).getKata(TBentuk));
        TCavumUteri.setDocument(new batasInput((byte)50).getKata(TCavumUteri));
        TUkuran.setDocument(new batasInput((byte)50).getKata(TUkuran));
        TAdnexaKanan.setDocument(new batasInput((byte)50).getKata(TAdnexaKanan));
        TAdnexaKiri.setDocument(new batasInput((byte)50).getKata(TAdnexaKiri));
        TCavumDouglas.setDocument(new batasInput((byte)50).getKata(TCavumDouglas));
        Catatan.setDocument(new batasInput((int)700).getKata(Catatan));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        TampilkanData();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        TampilkanData();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        TampilkanData();
                    }
                }
            });
        }  
        
        pasien.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(akses.getform().equals("DlgRawatJalan")){
                    if(pasien.getTable().getSelectedRow()!= -1){                   
                        TCariPasien.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),1).toString());
                    } 
                    if(pasien.getTable2().getSelectedRow()!= -1){                   
                        TCariPasien.setText(pasien.getTable2().getValueAt(pasien.getTable2().getSelectedRow(),1).toString());
                    } 
                    if(pasien.getTable3().getSelectedRow()!= -1){                   
                        TCariPasien.setText(pasien.getTable3().getValueAt(pasien.getTable3().getSelectedRow(),1).toString());
                    } 
                    TCariPasien.requestFocus();
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        pasien.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(akses.getform().equals("DlgRawatJalan")){
                    if(e.getKeyCode()==KeyEvent.VK_SPACE){
                        pasien.dispose();
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        pasien.getTable2().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(akses.getform().equals("DlgRawatJalan")){
                    if(e.getKeyCode()==KeyEvent.VK_SPACE){
                        pasien.dispose();
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        pasien.getTable3().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(akses.getform().equals("DlgRawatJalan")){
                    if(e.getKeyCode()==KeyEvent.VK_SPACE){
                        pasien.dispose();
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(akses.getform().equals("DlgRawatJalan")){
                    if(dokter.getTable().getSelectedRow()!= -1){
                        if(TabRawat.getSelectedIndex()==0){
                            KdDok.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                            TDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                            KdDok.requestFocus();
                        }else if(TabRawat.getSelectedIndex()==2){
                            KdDok2.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                            TDokter2.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                            KdDok2.requestFocus();
                        }else if(TabRawat.getSelectedIndex()==8){
                            KdDok3.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                            TDokter3.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                            KdDok3.requestFocus();
                        }                         
                    }                      
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(akses.getform().equals("DlgRawatJalan")){
                    if(petugas.getTable().getSelectedRow()!= -1){   
                        if(TabRawat.getSelectedIndex()==1){
                            kdptg.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                            TPerawat.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                            kdptg.requestFocus();
                        }else if(TabRawat.getSelectedIndex()==2){
                            kdptg2.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                            TPerawat2.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                            kdptg2.requestFocus();
                        }                            
                    }            
                    
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        panelDiagnosa1.TabRawat.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LCount.setText(panelDiagnosa1.getRecord()+"");
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        panelDiagnosa1.tbDiagnosaPasien.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {
                if(panelDiagnosa1.tbDiagnosaPasien.getSelectedRow()!= -1){
                    TNoRw.setText(panelDiagnosa1.tbDiagnosaPasien.getValueAt(panelDiagnosa1.tbDiagnosaPasien.getSelectedRow(),2).toString());
                    TNoRM.setText(panelDiagnosa1.tbDiagnosaPasien.getValueAt(panelDiagnosa1.tbDiagnosaPasien.getSelectedRow(),3).toString());
                    TPasien.setText(panelDiagnosa1.tbDiagnosaPasien.getValueAt(panelDiagnosa1.tbDiagnosaPasien.getSelectedRow(),4).toString());
                } 
            }
        });
        
        panelDiagnosa1.tbDiagnosaPasien.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(panelDiagnosa1.tbDiagnosaPasien.getSelectedRow()!= -1){
                    TNoRw.setText(panelDiagnosa1.tbDiagnosaPasien.getValueAt(panelDiagnosa1.tbDiagnosaPasien.getSelectedRow(),2).toString());
                    TNoRM.setText(panelDiagnosa1.tbDiagnosaPasien.getValueAt(panelDiagnosa1.tbDiagnosaPasien.getSelectedRow(),3).toString());
                    TPasien.setText(panelDiagnosa1.tbDiagnosaPasien.getValueAt(panelDiagnosa1.tbDiagnosaPasien.getSelectedRow(),4).toString());
                }                
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        panelDiagnosa1.tbTindakanPasien.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(panelDiagnosa1.tbTindakanPasien.getSelectedRow()!= -1){
                    TNoRw.setText(panelDiagnosa1.tbTindakanPasien.getValueAt(panelDiagnosa1.tbTindakanPasien.getSelectedRow(),2).toString());
                    TNoRM.setText(panelDiagnosa1.tbTindakanPasien.getValueAt(panelDiagnosa1.tbTindakanPasien.getSelectedRow(),3).toString());
                    TPasien.setText(panelDiagnosa1.tbTindakanPasien.getValueAt(panelDiagnosa1.tbTindakanPasien.getSelectedRow(),4).toString());
                }                
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        panelDiagnosa1.tbTindakanPasien.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {
                if(panelDiagnosa1.tbTindakanPasien.getSelectedRow()!= -1){
                    TNoRw.setText(panelDiagnosa1.tbTindakanPasien.getValueAt(panelDiagnosa1.tbTindakanPasien.getSelectedRow(),2).toString());
                    TNoRM.setText(panelDiagnosa1.tbTindakanPasien.getValueAt(panelDiagnosa1.tbTindakanPasien.getSelectedRow(),3).toString());
                    TPasien.setText(panelDiagnosa1.tbTindakanPasien.getValueAt(panelDiagnosa1.tbTindakanPasien.getSelectedRow(),4).toString());
                } 
            }
        });
        
        ChkInput.setSelected(false);
        isForm(); 
        ChkInput1.setSelected(false);
        isForm2(); 
        ChkInput2.setSelected(false);
        isForm3(); 
        ChkInput3.setSelected(false);
        isForm4();
        ChkAccor.setSelected(false);
        isMenu(); 
        jam();
        
        try {
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            aktifkanparsial=prop.getProperty("AKTIFKANBILLINGPARSIAL");
        } catch (Exception ex) {            
            aktifkanparsial="no";
        }
        
        try {
            psset_tarif=koneksi.prepareStatement("select * from set_tarif");
            try {
                rsset_tarif=psset_tarif.executeQuery();
                if(rsset_tarif.next()){
                    poli_ralan=rsset_tarif.getString("poli_ralan");
                    cara_bayar_ralan=rsset_tarif.getString("cara_bayar_ralan");
                }else{
                    poli_ralan="Yes";
                    cara_bayar_ralan="Yes";
                }  
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rsset_tarif != null){
                    rsset_tarif.close();
                }
                if(psset_tarif != null){
                    psset_tarif.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        } 
    }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
            isPsien();
            kd_pj=Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",TNoRw.getText());
            kode_poli=Sequel.cariIsi("select kd_poli from reg_periksa where no_rawat=?",TNoRw.getText());
        }else{         
            if(TabRawat.getSelectedIndex()==0){
                Valid.pindah(evt,DTPTgl,KdDok);
            }else if(TabRawat.getSelectedIndex()==1){
                Valid.pindah(evt,DTPTgl,kdptg);
            }else if(TabRawat.getSelectedIndex()==2){
                Valid.pindah(evt,DTPTgl,KdDok2);
            }else if(TabRawat.getSelectedIndex()==3){
                Valid.pindah(evt,DTPTgl,TKeluhan);
            }else if(TabRawat.getSelectedIndex()==4){
                Valid.pindah(evt,DTPTgl,TTinggi_uteri);
            }else if(TabRawat.getSelectedIndex()==5){
                Valid.pindah(evt,DTPTgl,TInspeksi);
            }else if(TabRawat.getSelectedIndex()==8){
                Valid.pindah(evt,DTPTgl,KdDok3);
            }
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"No.Rawat");
        }else{
            switch (TabRawat.getSelectedIndex()) {
                case 0:
                    if(KdDok.getText().trim().equals("")||TDokter.getText().trim().equals("")){
                        Valid.textKosong(KdDok,"Dokter");
                    }else{                        
                        try {
                            jmlparsial=0;
                            if(aktifkanparsial.equals("yes")){
                                jmlparsial=Sequel.cariInteger("select count(kd_pj) from set_input_parsial where kd_pj=?",Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",TNoRw.getText()));
                            }
                            if(jmlparsial>0){    
                                SimpanPenangananDokter();
                            }else{
                                if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                                    JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                    TCari.requestFocus();
                                }else{
                                    SimpanPenangananDokter();
                                }
                            } 
                        } catch (Exception e) {
                        }                      
                    } 
                    break;
                case 1:
                    if(kdptg.getText().trim().equals("")||TPerawat.getText().trim().equals("")){
                        Valid.textKosong(kdptg,"Petugas");
                    }else{
                        try {
                            jmlparsial=0;
                            if(aktifkanparsial.equals("yes")){
                                jmlparsial=Sequel.cariInteger("select count(kd_pj) from set_input_parsial where kd_pj=?",Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",TNoRw.getText()));
                            }
                            if(jmlparsial>0){ 
                                SimpanPenangananPetugas();
                            }else{
                                if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                                    JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                    TCari.requestFocus();
                                }else{
                                    SimpanPenangananPetugas();
                                }
                            } 
                        } catch (Exception e) {
                        } 
                    }  
                    break;
                case 2:
                    if(KdDok2.getText().trim().equals("")||TDokter2.getText().trim().equals("")){
                        Valid.textKosong(KdDok2,"Dokter");
                    }else if(kdptg2.getText().trim().equals("")||TPerawat2.getText().trim().equals("")){
                        Valid.textKosong(kdptg2,"Petugas");
                    }else{      
                        try {
                            jmlparsial=0;
                            if(aktifkanparsial.equals("yes")){
                                jmlparsial=Sequel.cariInteger("select count(kd_pj) from set_input_parsial where kd_pj=?",Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",TNoRw.getText()));
                            }
                            if(jmlparsial>0){ 
                                SimpanPenangananDokterPetugas();
                            }else {
                                if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                                    JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                    TCari.requestFocus();
                                }else{
                                    SimpanPenangananDokterPetugas();
                                }
                            } 
                        } catch (Exception e) {
                        }                            
                    }  
                    break;
                case 3:
                    if((!TKeluhan.getText().trim().equals(""))||(!TPemeriksaan.getText().trim().equals(""))||
                            (!TKeluhanUtama.getText().trim().equals(""))||(!TKeluhanTambahan.getText().trim().equals(""))||
                            (!TSuhu.getText().trim().equals(""))||(!TTensi.getText().trim().equals(""))||
                            (!TAlergi.getText().trim().equals(""))||(!TTinggi.getText().trim().equals(""))||
                            (!TBerat.getText().trim().equals(""))||(!TRespirasi.getText().trim().equals(""))||
                            (!TNadi.getText().trim().equals(""))||(!TGCS.getText().trim().equals(""))||
                            (!AbKepala.getText().trim().equals(""))||(!AbLeher.getText().trim().equals(""))||
                            (!AbThorax.getText().trim().equals(""))||(!AbAbdomen.getText().trim().equals(""))||
                            (!AbGenital.getText().trim().equals(""))||(!AbRektum.getText().trim().equals(""))||
                            (!AbExtremitas.getText().trim().equals(""))||(!TindakLanjut.getText().trim().equals(""))||
                            (!TPenilaian.getText().trim().equals(""))){
                        if(Sequel.menyimpantf("pemeriksaan_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",34,new String[]{
                            TNoRw.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                            TSuhu.getText(),TTensi.getText(),TNadi.getText(),TRespirasi.getText(),TTinggi.getText(),
                            TBerat.getText(),TGCS.getText(),TKeluhanUtama.getText(),TKeluhanTambahan.getText(),TKeluhan.getText(),
                            CmbKepala.getSelectedItem().toString(),AbKepala.getText(),CmbLeher.getSelectedItem().toString(),AbLeher.getText(),CmbThorax.getSelectedItem().toString(),
                            AbThorax.getText(),CmbAbdomen.getSelectedItem().toString(),AbAbdomen.getText(),CmbGenital.getSelectedItem().toString(),AbGenital.getText(),CmbRektum.getSelectedItem().toString(),AbRektum.getText(),CmbEktremitas.getSelectedItem().toString(),
                            AbExtremitas.getText(),TPemeriksaan.getText(),TAlergi.getText(),cmbImun.getSelectedItem().toString(),kesadaran.getSelectedItem().toString(),keadaanumum.getSelectedItem().toString(),
                            TindakLanjut.getText(),TPenilaian.getText()})==true){
                                TSuhu.setText("");TTensi.setText("");TNadi.setText("");TRespirasi.setText("");
                                TTinggi.setText("");TBerat.setText("");TGCS.setText("");TKeluhanUtama.setText("");TKeluhanTambahan.setText("");TKeluhan.setText("");
                                CmbKepala.setSelectedIndex(0);AbKepala.setText("");CmbLeher.setSelectedIndex(0);AbLeher.setText("");CmbThorax.setSelectedIndex(0);AbThorax.setText("");
                                CmbAbdomen.setSelectedIndex(0);AbAbdomen.setText("");CmbEktremitas.setSelectedIndex(0);AbExtremitas.setText("");
                                TPemeriksaan.setText("");TAlergi.setText("");cmbImun.setSelectedIndex(0);kesadaran.setSelectedIndex(0);keadaanumum.setSelectedIndex(0);
                                TindakLanjut.setText("");TPenilaian.setText("");
                                tampilPemeriksaan();
                        }
                    }
                    break;
                case 3:
                    if((!TKeluhan.getText().trim().equals(""))||(!TPemeriksaan.getText().trim().equals(""))||
                            (!TKeluhanUtama.getText().trim().equals(""))||(!TKeluhanTambahan.getText().trim().equals(""))||
                            (!TSuhu.getText().trim().equals(""))||(!TTensi.getText().trim().equals(""))||
                            (!TAlergi.getText().trim().equals(""))||(!TTinggi.getText().trim().equals(""))||
                            (!TBerat.getText().trim().equals(""))||(!TRespirasi.getText().trim().equals(""))||
                            (!TNadi.getText().trim().equals(""))||(!TGCS.getText().trim().equals(""))||
                            (!AbKepala.getText().trim().equals(""))||(!AbLeher.getText().trim().equals(""))||
                            (!AbThorax.getText().trim().equals(""))||(!AbAbdomen.getText().trim().equals(""))||
                            (!AbGenital.getText().trim().equals(""))||(!AbRektum.getText().trim().equals(""))||
                            (!AbExtremitas.getText().trim().equals(""))||(!TindakLanjut.getText().trim().equals(""))||
                            (!TPenilaian.getText().trim().equals(""))){
                        if(Sequel.menyimpantf("pemeriksaan_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",34,new String[]{
                            TNoRw.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                            TSuhu.getText(),TTensi.getText(),TNadi.getText(),TRespirasi.getText(),TTinggi.getText(),
                            TBerat.getText(),TGCS.getText(),TKeluhanUtama.getText(),TKeluhanTambahan.getText(),TKeluhan.getText(),
                            CmbKepala.getSelectedItem().toString(),AbKepala.getText(),CmbLeher.getSelectedItem().toString(),AbLeher.getText(),CmbThorax.getSelectedItem().toString(),
                            AbThorax.getText(),CmbAbdomen.getSelectedItem().toString(),AbAbdomen.getText(),CmbGenital.getSelectedItem().toString(),AbGenital.getText(),CmbRektum.getSelectedItem().toString(),AbRektum.getText(),CmbEktremitas.getSelectedItem().toString(),
                            AbExtremitas.getText(),TPemeriksaan.getText(),TAlergi.getText(),cmbImun.getSelectedItem().toString(),kesadaran.getSelectedItem().toString(),keadaanumum.getSelectedItem().toString(),
                            TindakLanjut.getText(),TPenilaian.getText()})==true){
                                TSuhu.setText("");TTensi.setText("");TNadi.setText("");TRespirasi.setText("");
                                TTinggi.setText("");TBerat.setText("");TGCS.setText("");TKeluhanUtama.setText("");TKeluhanTambahan.setText("");TKeluhan.setText("");
                                CmbKepala.setSelectedIndex(0);AbKepala.setText("");CmbLeher.setSelectedIndex(0);AbLeher.setText("");CmbThorax.setSelectedIndex(0);AbThorax.setText("");
                                CmbAbdomen.setSelectedIndex(0);AbAbdomen.setText("");CmbEktremitas.setSelectedIndex(0);AbExtremitas.setText("");
                                TPemeriksaan.setText("");TAlergi.setText("");cmbImun.setSelectedIndex(0);kesadaran.setSelectedIndex(0);keadaanumum.setSelectedIndex(0);
                                TindakLanjut.setText("");TPenilaian.setText("");
                                tampilPemeriksaan();
                        }
                    }
                    break;
                case 4:
                    if((!TTinggi_uteri.getText().trim().equals(""))||(!TLetak.getText().trim().equals(""))||
                            (!TDenyut.getText().trim().equals(""))||(!TKualitas_mnt.getText().trim().equals(""))||
                            (!TKualitas_dtk.getText().trim().equals(""))||(!TVulva.getText().trim().equals(""))||
                            (!TPortio.getText().trim().equals(""))||(!TTebal.getText().trim().equals(""))||
                            (!TPembukaan.getText().trim().equals(""))||(!TPenurunan.getText().trim().equals(""))||
                            (!TDenominator.getText().trim().equals(""))){
                        if(Sequel.menyimpantf("pemeriksaan_obstetri_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",23,new String[]{
                            TNoRw.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                            TTinggi_uteri.getText(),cmbJanin.getSelectedItem().toString(),TLetak.getText(),cmbPanggul.getSelectedItem().toString(),TDenyut.getText(),
                            cmbKontraksi.getSelectedItem().toString(),TKualitas_mnt.getText(),TKualitas_dtk.getText(),cmbFluksus.getSelectedItem().toString(),
                            cmbAlbus.getSelectedItem().toString(),TVulva.getText(),TPortio.getText(),cmbDalam.getSelectedItem().toString(),TTebal.getText(),
                            cmbArah.getSelectedItem().toString(),TPembukaan.getText(),TPenurunan.getText(),TDenominator.getText(),cmbKetuban.getSelectedItem().toString(),
                                cmbFeto.getSelectedItem().toString()})==true){
                                TTinggi_uteri.setText("");cmbJanin.setSelectedIndex(0);TLetak.setText("");cmbPanggul.setSelectedIndex(0);TDenyut.setText("");
                                cmbKontraksi.setSelectedIndex(0);TKualitas_mnt.setText("");TKualitas_dtk.setText("");cmbFluksus.setSelectedIndex(0);
                                cmbAlbus.setSelectedIndex(0);TVulva.setText("");TPortio.setText("");cmbDalam.setSelectedIndex(0);TTebal.setText("");
                                cmbArah.setSelectedIndex(0);TPembukaan.setText("");TPenurunan.setText("");TDenominator.setText("");cmbKetuban.setSelectedIndex(0);
                                cmbFeto.getSelectedItem().toString();
                                tampilPemeriksaanObstetri();
                        }
                    }  
                    break;
                case 5:
                    if((!TTinggi_uteri.getText().trim().equals(""))||(!TLetak.getText().trim().equals(""))||
                            (!TDenyut.getText().trim().equals(""))||(!TKualitas_mnt.getText().trim().equals(""))||
                            (!TKualitas_dtk.getText().trim().equals(""))||(!TVulva.getText().trim().equals(""))||
                            (!TPortio.getText().trim().equals(""))||(!TTebal.getText().trim().equals(""))||
                            (!TPembukaan.getText().trim().equals(""))||(!TPenurunan.getText().trim().equals(""))||
                            (!TDenominator.getText().trim().equals(""))){
                        if(Sequel.menyimpantf("pemeriksaan_obstetri_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",23,new String[]{
                            TNoRw.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                            TTinggi_uteri.getText(),cmbJanin.getSelectedItem().toString(),TLetak.getText(),cmbPanggul.getSelectedItem().toString(),TDenyut.getText(),
                            cmbKontraksi.getSelectedItem().toString(),TKualitas_mnt.getText(),TKualitas_dtk.getText(),cmbFluksus.getSelectedItem().toString(),
                            cmbAlbus.getSelectedItem().toString(),TVulva.getText(),TPortio.getText(),cmbDalam.getSelectedItem().toString(),TTebal.getText(),
                            cmbArah.getSelectedItem().toString(),TPembukaan.getText(),TPenurunan.getText(),TDenominator.getText(),cmbKetuban.getSelectedItem().toString(),
                                cmbFeto.getSelectedItem().toString()})==true){
                                TTinggi_uteri.setText("");cmbJanin.setSelectedIndex(0);TLetak.setText("");cmbPanggul.setSelectedIndex(0);TDenyut.setText("");
                                cmbKontraksi.setSelectedIndex(0);TKualitas_mnt.setText("");TKualitas_dtk.setText("");cmbFluksus.setSelectedIndex(0);
                                cmbAlbus.setSelectedIndex(0);TVulva.setText("");TPortio.setText("");cmbDalam.setSelectedIndex(0);TTebal.setText("");
                                cmbArah.setSelectedIndex(0);TPembukaan.setText("");TPenurunan.setText("");TDenominator.setText("");cmbKetuban.setSelectedIndex(0);
                                cmbFeto.getSelectedItem().toString();
                                tampilPemeriksaanObstetri();
                        }
                    }  
                    break;
                case 6:
                    if ((!TInspeksi.getText().trim().equals(""))||(!TInspeksiVulva.getText().trim().equals(""))||
                            (!TInspekuloGine.getText().trim().equals(""))||(!TUkuran.getText().trim().equals(""))||
                            (!TPortioInspekulo.getText().trim().equals(""))||(!TSondage.getText().trim().equals(""))||
                            (!TPortioDalam.getText().trim().equals(""))||(!TBentuk.getText().trim().equals(""))||
                            (!TCavumUteri.getText().trim().equals(""))||(!TUkuran.getText().trim().equals(""))||
                            (!TAdnexaKanan.getText().trim().equals(""))||(!TAdnexaKiri.getText().trim().equals(""))||
                            (!TCavumDouglas.getText().trim().equals(""))) {
                        if(Sequel.menyimpantf("pemeriksaan_ginekologi_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",20, new String[] {
                            TNoRw.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                            TInspeksi.getText(),TInspeksiVulva.getText(),TInspekuloGine.getText(),
                            cmbFluxusGine.getSelectedItem().toString(),cmbFluorGine.getSelectedItem().toString(), TVulvaInspekulo.getText(),
                            TPortioInspekulo.getText(), TSondage.getText(), TPortioDalam.getText(),
                            TBentuk.getText(), TCavumUteri.getText(), cmbMobilitas.getSelectedItem().toString(),
                            TUkuran.getText(), cmbNyeriTekan.getSelectedItem().toString(),
                            TAdnexaKanan.getText(), TAdnexaKiri.getText(), TCavumDouglas.getText()})==true){
                                TInspeksi.setText("");TInspeksiVulva.setText("");TInspekuloGine.setText("");
                                cmbFluxusGine.setSelectedIndex(0);cmbFluorGine.setSelectedIndex(0); TVulvaInspekulo.setText("");
                                TPortioInspekulo.setText(""); TSondage.setText(""); TPortioDalam.setText("");
                                TBentuk.setText(""); TCavumUteri.setText(""); cmbMobilitas.setSelectedIndex(0);
                                TUkuran.setText(""); cmbNyeriTekan.setSelectedIndex(0);
                                TAdnexaKanan.setText(""); TAdnexaKiri.setText(""); TCavumDouglas.getText();
                                tampilPemeriksaanGinekologi();
                        }
                    }
                    break;
                case 7:
                    if(akses.getdiagnosa_pasien()==true){
                        panelDiagnosa1.setRM(TNoRw.getText(),TNoRM.getText(),Valid.SetTgl(DTPCari1.getSelectedItem()+""),Valid.SetTgl(DTPCari2.getSelectedItem()+""),"Ralan",TCari.getText().trim());
                        panelDiagnosa1.simpan();
                    }
                    break;
                case 8:
                    if((!KdDok3.getText().trim().equals(""))&&(!TDokter3.getText().trim().equals(""))&&(!Catatan.getText().trim().equals(""))){
                        if(Sequel.menyimpantf("catatan_perawatan","?,?,?,?,?","Data",5,new String[]{
                            Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                            TNoRw.getText(),KdDok3.getText(),Catatan.getText()
                        })==true){
                            Catatan.setText("");
                            tampilCatatan();
                        }
                    }
                    break;
                default:
                    break;
            }            
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            if(TabRawat.getSelectedIndex()==0){
                Valid.pindah(evt,BtnSeekDokter,BtnBatal);
            }else if(TabRawat.getSelectedIndex()==1){
                Valid.pindah(evt,BtnSeekPetugas,BtnBatal);
            }else if(TabRawat.getSelectedIndex()==2){
                Valid.pindah(evt,BtnSeekPetugas2,BtnBatal);
            }else if(TabRawat.getSelectedIndex()==3){
                Valid.pindah(evt,TindakLanjut,BtnBatal);
            }else if(TabRawat.getSelectedIndex()==4){
                Valid.pindah(evt,cmbFeto,BtnBatal);
            }else if(TabRawat.getSelectedIndex()==5){
                Valid.pindah(evt,TCavumDouglas,BtnBatal);
            }else if(TabRawat.getSelectedIndex()==8){
                Valid.pindah(evt,Catatan,BtnBatal);
            }
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        ChkInput.setSelected(true);
        ChkInput1.setSelected(true);
        ChkInput2.setSelected(true);
        ChkInput3.setSelected(true);
        isForm(); 
        isForm2();
        isForm3();
        isForm4();
        TSuhu.setText("");    
        TTensi.setText("");
        TKeluhan.setText("");
        TPemeriksaan.setText("");
        TPenilaian.setText("");
        TKeluhanTambahan.setText("");
        TBerat.setText("");
        TTinggi.setText("");
        TNadi.setText("");
        TRespirasi.setText("");
        TGCS.setText("");
        TindakLanjut.setText("");
        TTinggi_uteri.setText("");
        TLetak.setText("");
        TDenyut.setText("");
        TVulva.setText("");
        TPortio.setText("");
        TTebal.setText("");
        TPembukaan.setText("");
        TPenurunan.setText("");
        TDenominator.setText("");
        TKualitas_mnt.setText("");
        TKualitas_dtk.setText("");
        TInspeksi.setText("");
        TInspeksiVulva.setText("");
        TInspekuloGine.setText("");
        TVulvaInspekulo.setText("");
        TPortioInspekulo.setText("");
        TSondage.setText("");
        TPortioDalam.setText("");
        TBentuk.setText("");
        TCavumUteri.setText("");
        TUkuran.setText("");
        TAdnexaKanan.setText("");
        TAdnexaKiri.setText("");
        TCavumDouglas.setText("");
        Catatan.setText("");
        TNoRw.requestFocus();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnBatalActionPerformed(null);
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if(tabModeDr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TNoRw.requestFocus();
                }else{
                    
                    for(i=0;i<tbRawatDr.getRowCount();i++){
                        if(tbRawatDr.getValueAt(i,0).toString().equals("true")){                            
                            if(Sequel.cariRegistrasi(tbRawatDr.getValueAt(i,1).toString())>0){
                                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                TCari.requestFocus();
                            }else{
                                Sequel.queryu("delete from rawat_jl_dr where no_rawat='"+tbRawatDr.getValueAt(i,1).toString()+
                                        "' and kd_jenis_prw='"+tbRawatDr.getValueAt(i,10)+
                                        "' and kd_dokter='"+tbRawatDr.getValueAt(i,5).toString()+
                                        "' and tgl_perawatan='"+tbRawatDr.getValueAt(i,7).toString()+
                                        "' and jam_rawat='"+tbRawatDr.getValueAt(i,8).toString()+"'");
                            }
                        }                            
                    }
                    
                    tampilDr();
                }   break;
            case 1:
                if(tabModePr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TNoRw.requestFocus();
                }else{
                    
                    for(i=0;i<tbRawatPr.getRowCount();i++){
                        if(tbRawatPr.getValueAt(i,0).toString().equals("true")){
                            if(Sequel.cariRegistrasi(tbRawatPr.getValueAt(i,1).toString())>0){
                                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                TCari.requestFocus();
                            }else{
                                Sequel.queryu("delete from rawat_jl_pr where no_rawat='"+tbRawatPr.getValueAt(i,1).toString()+
                                        "' and kd_jenis_prw='"+tbRawatPr.getValueAt(i,10)+
                                        "' and nip='"+tbRawatPr.getValueAt(i,5).toString()+
                                        "' and tgl_perawatan='"+tbRawatPr.getValueAt(i,7).toString()+
                                        "' and jam_rawat='"+tbRawatPr.getValueAt(i,8).toString()+"' ");
                            }
                        }
                    }
                    
                    tampilPr();
                }   break;
            case 2:
                if(tabModeDrPr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TNoRw.requestFocus();
                }else{
                    
                    for(i=0;i<tbRawatDrPr.getRowCount();i++){
                        if(tbRawatDrPr.getValueAt(i,0).toString().equals("true")){                            
                            if(Sequel.cariRegistrasi(tbRawatDrPr.getValueAt(i,1).toString())>0){
                                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                TCari.requestFocus();
                            }else{
                                Sequel.queryu("delete from rawat_jl_drpr where no_rawat='"+tbRawatDrPr.getValueAt(i,1).toString()+
                                        "' and kd_jenis_prw='"+tbRawatDrPr.getValueAt(i,12)+
                                        "' and kd_dokter='"+tbRawatDrPr.getValueAt(i,5).toString()+
                                        "' and nip='"+tbRawatDrPr.getValueAt(i,7).toString()+
                                        "' and tgl_perawatan='"+tbRawatDrPr.getValueAt(i,9).toString()+
                                        "' and jam_rawat='"+tbRawatDrPr.getValueAt(i,10).toString()+"' ");
                            }
                        }                            
                    }
                    
                    tampilDrPr();
                }   break;
            case 3:
                if(tabModePemeriksaan.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TNoRw.requestFocus();
                }else{
                    for(i=0;i<tbPemeriksaan.getRowCount();i++){
                        if(tbPemeriksaan.getValueAt(i,0).toString().equals("true")){
                            Sequel.queryu("delete from pemeriksaan_ralan where no_rawat='"+tbPemeriksaan.getValueAt(i,1).toString()+
                                    "' and tgl_perawatan='"+tbPemeriksaan.getValueAt(i,4).toString()+
                                    "' and jam_rawat='"+tbPemeriksaan.getValueAt(i,5).toString()+"' ");
                        }
                    }
                    tampilPemeriksaan();
                }   break;
            case 4:
                if(tabModeObstetri.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TNoRw.requestFocus();
                }else{
                    for(i=0;i<tbPemeriksaanObstetri.getRowCount();i++){
                        if(tbPemeriksaanObstetri.getValueAt(i,0).toString().equals("true")){
                            Sequel.queryu("delete from pemeriksaan_obstetri_ralan where no_rawat='"+tbPemeriksaanObstetri.getValueAt(i,1).toString()+
                                    "' and tgl_perawatan='"+tbPemeriksaanObstetri.getValueAt(i,4).toString()+
                                    "' and jam_rawat='"+tbPemeriksaanObstetri.getValueAt(i,5).toString()+"' ");
                        }
                    }
                    tampilPemeriksaanObstetri();
                }   break;
            case 5:
                if(tabModeGinekologi.getRowCount()==0){
                    JOptionPane.showMessageDialog(null, "Maaf, data sudah habis...!!!");
                    TNoRw.requestFocus();                    
                }else {
                    for(i=0;i<tbPemeriksaanGinekologi.getRowCount();i++){
                        if(tbPemeriksaanGinekologi.getValueAt(i,0).toString().equals("true")){
                            Sequel.queryu("delete from pemeriksaan_ginekologi_ralan where no_rawat='"+tbPemeriksaanGinekologi.getValueAt(i,1).toString()+
                                    "' and tgl_perawatan='"+tbPemeriksaanGinekologi.getValueAt(i,4).toString()+
                                    "' and jam_rawat='"+tbPemeriksaanGinekologi.getValueAt(i,5).toString()+"' ");
                        }
                    }
                    tampilPemeriksaanGinekologi();
                }   break;
            case 7:
                panelDiagnosa1.setRM(TNoRw.getText(),TNoRM.getText(),Valid.SetTgl(DTPCari1.getSelectedItem()+""),Valid.SetTgl(DTPCari2.getSelectedItem()+""),"Ralan",TCari.getText().trim());
                panelDiagnosa1.hapus();
                LCount.setText(panelDiagnosa1.getRecord()+"");
                break;
            case 8:
                if(TabModeCatatan.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TNoRw.requestFocus();
                }else{
                    for(i=0;i<tbCatatan.getRowCount();i++){
                        if(tbCatatan.getValueAt(i,0).toString().equals("true")){
                            Sequel.queryu("delete from catatan_perawatan where no_rawat='"+tbCatatan.getValueAt(i,1).toString()+
                                    "' and tanggal='"+tbCatatan.getValueAt(i,4).toString()+
                                    "' and jam='"+tbCatatan.getValueAt(i,5).toString()+
                                    "' and kd_dokter='"+tbCatatan.getValueAt(i,6).toString()+"' ");
                        }
                    }
                    tampilCatatan();
                }   break;
            default:
                break;
        }

        BtnBatalActionPerformed(evt);
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnPrint);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(! TCari.getText().trim().equals("")){
            BtnCariActionPerformed(evt);
        }
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if(tabModeDr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                }else if(tabModeDr.getRowCount()!=0){
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    param.put("logo",Sequel.cariGambar("select logo from setting"));
                    String pas=" and reg_periksa.no_rkm_medis like '%"+TCariPasien.getText()+"%' ";
                    
                    String tgl=" rawat_jl_dr.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+pas;
                    Valid.MyReportqry("rptJalanDr.jasper","report","::[ Data Rawat Jalan Yang Ditangani Dokter ]::",
                            "select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                                    "jns_perawatan.nm_perawatan,rawat_jl_dr.kd_dokter,dokter.nm_dokter,"+
                                    "rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat,rawat_jl_dr.biaya_rawat " +
                                    "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                                    "dokter inner join rawat_jl_dr "+
                                    "on rawat_jl_dr.no_rawat=reg_periksa.no_rawat "+
                                    "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                                    "and rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                    "and rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                                    "where "+tgl+" and rawat_jl_dr.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and jns_perawatan.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and rawat_jl_dr.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and tgl_perawatan like '%"+TCari.getText().trim()+"%' "+
                                            " order by rawat_jl_dr.no_rawat desc",param);
                    
                }   break;
            case 1:
                if(tabModePr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                }else if(tabModePr.getRowCount()!=0){
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    param.put("logo",Sequel.cariGambar("select logo from setting"));
                    String pas=" and reg_periksa.no_rkm_medis like '%"+TCariPasien.getText()+"%' ";
                    
                    String tgl=" rawat_jl_pr.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+pas;
                    Valid.MyReportqry("rptJalanPr.jasper","report","::[ Data Rawat Jalan Yang Ditangani Perawat ]::",
                            "select rawat_jl_pr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                                    "jns_perawatan.nm_perawatan,rawat_jl_pr.nip,petugas.nama,"+
                                    "rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat,rawat_jl_pr.biaya_rawat " +
                                    "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                                    "petugas inner join rawat_jl_pr "+
                                    "on rawat_jl_pr.no_rawat=reg_periksa.no_rawat "+
                                    "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                                    "and rawat_jl_pr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                    "and rawat_jl_pr.nip=petugas.nip where  "+
                                    tgl+"and rawat_jl_pr.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and jns_perawatan.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and rawat_jl_pr.nip like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and rawat_jl_pr.tgl_perawatan like '%"+TCari.getText().trim()+"%'  "+
                                            "order by rawat_jl_pr.no_rawat desc",param);
                }   break;
            case 2:
                if(tabModeDrPr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                }else if(tabModeDrPr.getRowCount()!=0){
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    param.put("logo",Sequel.cariGambar("select logo from setting"));
                    String pas=" and reg_periksa.no_rkm_medis like '%"+TCariPasien.getText()+"%' ";
                    
                    String tgl=" rawat_jl_drpr.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+pas;
                    Valid.MyReportqry("rptJalanDrPr.jasper","report","::[ Data Rawat Jalan Yang Ditangani Dokter ]::",
                            "select rawat_jl_drpr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                                    "jns_perawatan.nm_perawatan,rawat_jl_drpr.kd_dokter,dokter.nm_dokter,rawat_jl_drpr.nip,petugas.nama,"+
                                    "rawat_jl_drpr.tgl_perawatan,rawat_jl_drpr.jam_rawat,rawat_jl_drpr.biaya_rawat " +
                                    "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                                    "dokter inner join rawat_jl_drpr inner join "+
                                    "petugas on rawat_jl_drpr.no_rawat=reg_periksa.no_rawat "+
                                    "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                                    "and rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                    "and rawat_jl_drpr.kd_dokter=dokter.kd_dokter "+
                                    "and rawat_jl_drpr.nip=petugas.nip "+
                                    "where "+tgl+" and rawat_jl_drpr.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and jns_perawatan.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and rawat_jl_drpr.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and rawat_jl_drpr.nip like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and tgl_perawatan like '%"+TCari.getText().trim()+"%' "+
                                            " order by rawat_jl_drpr.no_rawat desc",param);
                }   break;
            case 3:
                if(tabModePemeriksaan.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                }else if(tabModePemeriksaan.getRowCount()!=0){
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    param.put("logo",Sequel.cariGambar("select logo from setting"));
                    String pas=" and reg_periksa.no_rkm_medis like '%"+TCariPasien.getText()+"%' ";
                    
                    String tgl=" pemeriksaan_ralan.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+pas;
                    Valid.MyReportqry("rptJalanPemeriksaan.jasper","report","::[ Data Pemeriksaan Rawat Jalan ]::",
                            "select pemeriksaan_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                                    "pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.suhu_tubuh,pemeriksaan_ralan.tensi, " +
                                    "pemeriksaan_ralan.nadi,pemeriksaan_ralan.respirasi,pemeriksaan_ralan.tinggi, " +
                                    "pemeriksaan_ralan.berat,pemeriksaan_ralan.gcs,pemeriksaan_ralan.keluhan, " +
                                    "pemeriksaan_ralan.kepala,pemeriksaan_ralan.leher,pemeriksaan_ralan.thorax,pemeriksaan_ralan.abdomen,pemeriksaan_ralan.ekstremitas, "+
                                    "pemeriksaan_ralan.pemeriksaan,pemeriksaan_ralan.alergi,pemeriksaan_ralan.imun_ke,"+
                                    "pemeriksaan_ralan.rtl,pemeriksaan_ralan.penilaian from pasien inner join reg_periksa inner join pemeriksaan_ralan "+
                                    "on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis where  "+
                                    tgl+"and pemeriksaan_ralan.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and pemeriksaan_ralan.alergi like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and pemeriksaan_ralan.keluhan like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and pemeriksaan_ralan.penilaian like '%"+TCari.getText().trim()+"%' or "+
                                    tgl+"and pemeriksaan_ralan.pemeriksaan like '%"+TCari.getText().trim()+"%' "+
                                            "order by pemeriksaan_ralan.no_rawat desc",param);
                }   break;
            case 4:
                if(tabModeObstetri.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                }else if(tabModeObstetri.getRowCount()!=0){
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    param.put("logo",Sequel.cariGambar("select logo from setting"));
                    String pas=" and reg_periksa.no_rkm_medis like '%"+TCariPasien.getText()+"%' ";
                    
                    String tgl=" pemeriksaan_obstetri_ralan.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+pas;
                    Valid.MyReportqry("rptJalanObstetri.jasper","report","::[ Data Pemeriksaan Obstetri Rawat Jalan ]::",
                            "select pemeriksaan_obstetri_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                            "pemeriksaan_obstetri_ralan.tgl_perawatan,pemeriksaan_obstetri_ralan.jam_rawat,pemeriksaan_obstetri_ralan.tinggi_uteri,pemeriksaan_obstetri_ralan.janin,pemeriksaan_obstetri_ralan.letak, " +
                            "pemeriksaan_obstetri_ralan.panggul,pemeriksaan_obstetri_ralan.denyut,pemeriksaan_obstetri_ralan.kontraksi, " +
                            "pemeriksaan_obstetri_ralan.kualitas_mnt,pemeriksaan_obstetri_ralan.kualitas_dtk,pemeriksaan_obstetri_ralan.fluksus,pemeriksaan_obstetri_ralan.albus, " +
                            "pemeriksaan_obstetri_ralan.vulva,pemeriksaan_obstetri_ralan.portio,pemeriksaan_obstetri_ralan.dalam, pemeriksaan_obstetri_ralan.tebal, pemeriksaan_obstetri_ralan.arah, pemeriksaan_obstetri_ralan.pembukaan," +
                            "pemeriksaan_obstetri_ralan.penurunan, pemeriksaan_obstetri_ralan.denominator, pemeriksaan_obstetri_ralan.ketuban, pemeriksaan_obstetri_ralan.feto " +
                            "from pasien inner join reg_periksa inner join pemeriksaan_obstetri_ralan "+
                            "on pemeriksaan_obstetri_ralan.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis where  "+
                            tgl+"and pemeriksaan_obstetri_ralan.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                            tgl+"and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or  "+
                            tgl+"and pemeriksaan_obstetri_ralan.tinggi_uteri like '%"+TCari.getText().trim()+"%' or "+
                            tgl+"and pemeriksaan_obstetri_ralan.janin like '%"+TCari.getText().trim()+"%' or "+
                            tgl+"and pemeriksaan_obstetri_ralan.letak like '%"+TCari.getText().trim()+"%' "+
                            "order by pemeriksaan_obstetri_ralan.no_rawat desc",param);
                }   break;
            case 5:
                if(tabModeGinekologi.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                }else if(tabModeGinekologi.getRowCount()!=0){
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    param.put("logo",Sequel.cariGambar("select logo from setting"));
                    String pas=" and reg_periksa.no_rkm_medis like '%"+TCariPasien.getText()+"%' ";
                    
                    String tgl=" pemeriksaan_ginekologi_ralan.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+pas;
                    Valid.MyReportqry("rptJalanGinekologi.jasper","report","::[ Data Pemeriksaan Ginekologi Rawat Jalan ]::",
                            "select pemeriksaan_ginekologi_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                            "pemeriksaan_ginekologi_ralan.tgl_perawatan,pemeriksaan_ginekologi_ralan.jam_rawat,pemeriksaan_ginekologi_ralan.inspeksi,pemeriksaan_ginekologi_ralan.inspeksi_vulva,pemeriksaan_ginekologi_ralan.inspekulo_gine, " +
                            "pemeriksaan_ginekologi_ralan.fluxus_gine,pemeriksaan_ginekologi_ralan.fluor_gine,pemeriksaan_ginekologi_ralan.vulva_inspekulo, " +
                            "pemeriksaan_ginekologi_ralan.portio_inspekulo,pemeriksaan_ginekologi_ralan.sondage,pemeriksaan_ginekologi_ralan.portio_dalam,pemeriksaan_ginekologi_ralan.bentuk, " +
                            "pemeriksaan_ginekologi_ralan.cavum_uteri,pemeriksaan_ginekologi_ralan.mobilitas,pemeriksaan_ginekologi_ralan.ukuran, pemeriksaan_ginekologi_ralan.nyeri_tekan, pemeriksaan_ginekologi_ralan.adnexa_kanan, pemeriksaan_ginekologi_ralan.adnexa_kiri," +
                            "pemeriksaan_ginekologi_ralan.cavum_douglas " +
                            "from pasien inner join reg_periksa inner join pemeriksaan_ginekologi_ralan "+
                            "on pemeriksaan_ginekologi_ralan.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis where  "+
                            tgl+"and pemeriksaan_ginekologi_ralan.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                            tgl+"and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                            tgl+"and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or  "+
                            tgl+"and pemeriksaan_ginekologi_ralan.inspeksi like '%"+TCari.getText().trim()+"%' or "+
                            tgl+"and pemeriksaan_ginekologi_ralan.inspeksi_vulva like '%"+TCari.getText().trim()+"%' or "+
                            tgl+"and pemeriksaan_ginekologi_ralan.inspekulo_gine like '%"+TCari.getText().trim()+"%' "+
                            "order by pemeriksaan_ginekologi_ralan.no_rawat desc",param);
                }   break;
            case 6:
                if(akses.getresume_pasien()==true){
                    panelResume1.laporan();
                }   
                break;
            case 7:
                if(akses.getdiagnosa_pasien()==true){
                    panelDiagnosa1.cetak();
                } 
                break;
            case 8:
                if(TabModeCatatan.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                }else if(TabModeCatatan.getRowCount()!=0){
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    param.put("logo",Sequel.cariGambar("select logo from setting"));
                    String pas=" and reg_periksa.no_rkm_medis like '%"+TCariPasien.getText()+"%' ";
                    
                    String tgl=" catatan_perawatan.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+pas;
                    Valid.MyReportqry("rptCatatanDokter.jasper","report","::[ Data Catatan Dokter ]::",
                            "select catatan_perawatan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                            "catatan_perawatan.tanggal,catatan_perawatan.jam,catatan_perawatan.kd_dokter,dokter.nm_dokter,"+
                            "catatan_perawatan.catatan from pasien inner join reg_periksa inner join catatan_perawatan inner join dokter "+
                            "on catatan_perawatan.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                            "and catatan_perawatan.kd_dokter=dokter.kd_dokter where  "+
                            tgl+" and catatan_perawatan.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                            tgl+" and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                            tgl+" and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or  "+
                            tgl+" and catatan_perawatan.catatan like '%"+TCari.getText().trim()+"%' or "+
                            tgl+" and catatan_perawatan.kd_dokter like '%"+TCari.getText().trim()+"%' or "+
                            tgl+" and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' "+
                            "order by catatan_perawatan.no_rawat desc",param);
                }   break;    
            default:
                break;
        }

        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnAll);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        petugas.dispose();
        dokter.dispose();
        pasien.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnPrint,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        TCariPasien.setText("");
        TampilkanData();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnPrint, BtnKeluar);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            TampilkanData();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            switch (TabRawat.getSelectedIndex()) {
                case 0:
                    if(TabRawatTindakanDokter.getSelectedIndex()==0){
                        tbTindakan.requestFocus();
                    }else if(TabRawatTindakanDokter.getSelectedIndex()==1){
                        tbRawatDr.requestFocus();
                    }
                    break;
                case 1:
                    if(TabRawatTindakanPetugas.getSelectedIndex()==0){
                        tbTindakan2.requestFocus();
                    }else if(TabRawatTindakanPetugas.getSelectedIndex()==1){
                        tbRawatPr.requestFocus();
                    }
                    break;
                case 2:
                    if(TabRawatTindakanDokterPetugas.getSelectedIndex()==0){
                        tbTindakan3.requestFocus();
                    }else if(TabRawatTindakanDokterPetugas.getSelectedIndex()==1){
                        tbRawatDrPr.requestFocus();
                    }
                    break;
                case 3:
                    tbPemeriksaan.requestFocus();
                    break;
                case 4:
                    tbPemeriksaanObstetri.requestFocus();
                    break;
                case 5:
                    tbPemeriksaanGinekologi.requestFocus();
                    break;
                default:
                    break;
            }
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        TampilkanData();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void tbRawatDrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRawatDrMouseClicked
        if(tabModeDr.getRowCount()!=0){
            try {
                getDataDr();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbRawatDrMouseClicked

    private void tbRawatPrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRawatPrMouseClicked
        if(tabModePr.getRowCount()!=0){
            try {
                getDataPr();
            } catch (java.lang.NullPointerException e) {
            }
            
        }
}//GEN-LAST:event_tbRawatPrMouseClicked

private void KdDokKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDokKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",TDokter,KdDok.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnSeekDokterActionPerformed(null);
        }else{            
            Valid.pindah(evt,TNoRw,BtnSeekDokter);
        }
}//GEN-LAST:event_KdDokKeyPressed

private void BtnSeekDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekDokterActionPerformed
        akses.setform("DlgRawatJalan");
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
}//GEN-LAST:event_BtnSeekDokterActionPerformed

private void kdptgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdptgKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nama from petugas where nip=?",TPerawat,kdptg.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnSeekPetugasActionPerformed(null);
        }else{
            Valid.pindah(evt,TNoRw,BtnSeekPetugas);
        }
}//GEN-LAST:event_kdptgKeyPressed

private void BtnSeekPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekPetugasActionPerformed
        akses.setform("DlgRawatJalan");
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
}//GEN-LAST:event_BtnSeekPetugasActionPerformed

private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"No.Rawat");
        }else{
            switch (TabRawat.getSelectedIndex()) {
                case 3:
                    if((!TKeluhan.getText().trim().equals(""))||(!TPemeriksaan.getText().trim().equals(""))||
                            (!TSuhu.getText().trim().equals(""))||(!TTensi.getText().trim().equals(""))||
                            (!TAlergi.getText().trim().equals(""))||(!TTinggi.getText().trim().equals(""))||
                            (!TBerat.getText().trim().equals(""))||(!TRespirasi.getText().trim().equals(""))||
                            (!TNadi.getText().trim().equals(""))||(!TGCS.getText().trim().equals(""))||
                            (!TKeluhanUtama.getText().trim().equals(""))||(!TKeluhanTambahan.getText().trim().equals(""))||
                            (!TindakLanjut.getText().trim().equals(""))||(!TPenilaian.getText().trim().equals(""))){
                        if(tbPemeriksaan.getSelectedRow()>-1){
                            if(Sequel.mengedittf("pemeriksaan_ralan","no_rawat='"+tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),1)+
                                "' and tgl_perawatan='"+tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),4)+
                                "' and jam_rawat='"+tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),5)+"'",
                                "no_rawat='"+TNoRw.getText()+"',suhu_tubuh='"+TSuhu.getText()+"',tensi='"+TTensi.getText()+"',"+
                                "keluhan='"+TKeluhan.getText()+"',pemeriksaan='"+TPemeriksaan.getText()+"',"+
                                "nadi='"+TNadi.getText()+"',respirasi='"+TRespirasi.getText()+"',"+
                                "tinggi='"+TTinggi.getText()+"',berat='"+TBerat.getText()+"',"+
                                "gcs='"+TGCS.getText()+"',keluhanutama='"+TKeluhanUtama.getText()+"',keluhantambahan='"+TKeluhanTambahan.getText()+"',alergi='"+TAlergi.getText()+"',imun_ke='"+cmbImun.getSelectedItem()+"',"+
                                "kepala='"+CmbKepala.getSelectedItem()+"',abkepala='"+AbKepala.getText()+"',leher='"+CmbLeher.getSelectedItem()+"',ableher='"+AbLeher.getText()+"', "+
                                "Thorax='"+CmbThorax.getSelectedItem()+"',abthorax='"+AbThorax.getText()+"',abdomen='"+CmbAbdomen.getSelectedItem()+"',ababdomen='"+AbAbdomen.getText()+"', "+
                                "genital='"+CmbGenital.getSelectedItem()+"',abgenital='"+AbGenital.getText()+"',rektum='"+CmbRektum.getSelectedItem()+"',abrektum='"+AbRektum.getText()+"',ekstremitas='"+CmbEktremitas.getSelectedItem()+"',abekstremitas='"+AbExtremitas.getText()+"', "+
                                "kesadaran='"+kesadaran.getSelectedItem()+"',keadaanumum='"+keadaanumum.getSelectedItem()+"',tgl_perawatan='"+Valid.SetTgl(DTPTgl.getSelectedItem()+"")+"',"+
                                "jam_rawat='"+cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem()+"',"+
                                "rtl='"+TindakLanjut.getText()+"',penilaian='"+TPenilaian.getText()+"'")==true){
                                    TSuhu.setText("");TTensi.setText("");TNadi.setText("");TRespirasi.setText("");
                                    TTinggi.setText("");TBerat.setText("");TGCS.setText("");TKeluhanUtama.setText("");TKeluhanTambahan.setText("");TKeluhan.setText("");
                                    CmbKepala.setSelectedIndex(0);AbKepala.setText("");CmbLeher.setSelectedIndex(0);AbLeher.setText("");CmbThorax.setSelectedIndex(0);AbThorax.setText("");
                                    CmbAbdomen.setSelectedIndex(0);AbAbdomen.setText("");CmbEktremitas.setSelectedIndex(0);AbExtremitas.setText("");
                                    TPemeriksaan.setText("");TAlergi.setText("");cmbImun.setSelectedIndex(0);kesadaran.setSelectedIndex(0);keadaanumum.setSelectedIndex(0);
                                    TindakLanjut.setText("");TPenilaian.setText("");
                                    tampilPemeriksaan();
                            }                            
                        }else{
                            JOptionPane.showMessageDialog(rootPane,"Silahkan pilih data yang mau diganti..!!");
                            TCari.requestFocus();
                        }
                    }   break;
                case 4:
                    if((!TTinggi_uteri.getText().trim().equals(""))||(!TLetak.getText().trim().equals(""))||
                            (!TDenyut.getText().trim().equals(""))||(!TKualitas_mnt.getText().trim().equals(""))||
                            (!TKualitas_dtk.getText().trim().equals(""))||(!TVulva.getText().trim().equals(""))||
                            (!TPortio.getText().trim().equals(""))||(!TTebal.getText().trim().equals(""))||
                            (!TPembukaan.getText().trim().equals(""))||(!TPenurunan.getText().trim().equals(""))||
                            (!TDenominator.getText().trim().equals(""))){
                        if(tbPemeriksaanObstetri.getSelectedRow()>-1){
                            if(Sequel.mengedittf("pemeriksaan_obstetri_ralan","no_rawat='"+tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),1)+
                                "' and tgl_perawatan='"+tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),4)+
                                "' and jam_rawat='"+tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),5)+"'",
                                "no_rawat='"+TNoRw.getText()+"', tgl_perawatan='"+Valid.SetTgl(DTPTgl.getSelectedItem()+"")+"', "+
                                "jam_rawat='"+cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem()+"', "+
                                "tinggi_uteri='"+TTinggi_uteri.getText()+"', janin='"+cmbJanin.getSelectedItem()+"', letak='"+TLetak.getText()+"', "+
                                "panggul='"+cmbPanggul.getSelectedItem()+"', denyut='"+TDenyut.getText()+"', kontraksi='"+cmbKontraksi.getSelectedItem()+"', "+
                                "kualitas_mnt='"+TKualitas_mnt.getText()+"', kualitas_dtk='"+TKualitas_dtk.getText()+"', "+
                                "fluksus='"+cmbFluksus.getSelectedItem()+"', albus='"+cmbAlbus.getSelectedItem()+"', vulva='"+TVulva.getText()+"',"+
                                "portio='"+TPortio.getText()+"', dalam='"+cmbDalam.getSelectedItem()+"', tebal='"+TTebal.getText()+"', "+
                                "arah='"+cmbArah.getSelectedItem()+"', pembukaan='"+TPembukaan.getText()+"', penurunan='"+TPenurunan.getText()+"', "+
                                "denominator='"+TDenominator.getText()+"', ketuban='"+cmbKetuban.getSelectedItem()+"', feto='"+cmbFeto.getSelectedItem()+"'")==true){
                                    TTinggi_uteri.setText("");cmbJanin.setSelectedIndex(0);TLetak.setText("");cmbPanggul.setSelectedIndex(0);TDenyut.setText("");
                                    cmbKontraksi.setSelectedIndex(0);TKualitas_mnt.setText("");TKualitas_dtk.setText("");cmbFluksus.setSelectedIndex(0);
                                    cmbAlbus.setSelectedIndex(0);TVulva.setText("");TPortio.setText("");cmbDalam.setSelectedIndex(0);TTebal.setText("");
                                    cmbArah.setSelectedIndex(0);TPembukaan.setText("");TPenurunan.setText("");TDenominator.setText("");cmbKetuban.setSelectedIndex(0);
                                    cmbFeto.getSelectedItem().toString();
                                    tampilPemeriksaanObstetri();
                            }
                        }else{
                            JOptionPane.showMessageDialog(rootPane,"Silahkan pilih data yang mau diganti..!!");
                            TCari.requestFocus();
                        }
                    }   break;
                case 5:
                   if((!TInspeksi.getText().trim().equals(""))||(!TInspeksiVulva.getText().trim().equals(""))||
                            (!TInspekuloGine.getText().trim().equals(""))||(!TVulvaInspekulo.getText().trim().equals(""))||
                            (!TPortioInspekulo.getText().trim().equals(""))||(!TSondage.getText().trim().equals(""))||
                            (!TPortioDalam.getText().trim().equals(""))||(!TBentuk.getText().trim().equals(""))||
                            (!TCavumUteri.getText().trim().equals(""))||(!TUkuran.getText().trim().equals(""))||
                            (!TAdnexaKanan.getText().trim().equals(""))||(!TAdnexaKiri.getText().trim().equals(""))||
                            (!TCavumDouglas.getText().trim().equals(""))){
                        if(tbPemeriksaanGinekologi.getSelectedRow()>-1){
                            if(Sequel.mengedittf("pemeriksaan_ginekologi_ralan","no_rawat='"+tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),1)+
                                "' and tgl_perawatan='"+tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),4)+
                                "' and jam_rawat='"+tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),5)+"'",
                                "no_rawat='"+TNoRw.getText()+"', tgl_perawatan='"+Valid.SetTgl(DTPTgl.getSelectedItem()+"")+"', "+
                                "jam_rawat='"+cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem()+"', "+
                                "inspeksi='"+TInspeksi.getText()+"', inspeksi_vulva='"+TInspeksiVulva.getText()+"', inspekulo_gine='"+TInspekuloGine.getText()+"', "+
                                "fluxus_gine='"+cmbFluxusGine.getSelectedItem()+"', fluor_gine='"+cmbFluorGine.getSelectedItem()+"', "+
                                "vulva_inspekulo='"+TVulvaInspekulo.getText()+"', portio_inspekulo='"+TPortioInspekulo.getText()+"', sondage='"+TSondage.getText()+"', "+
                                "portio_dalam='"+TPortioDalam.getText()+"', bentuk='"+TBentuk.getText()+"', cavum_uteri='"+TCavumUteri.getText()+"', "+
                                "mobilitas='"+cmbMobilitas.getSelectedItem()+"', ukuran='"+TUkuran.getText()+"', nyeri_tekan='"+cmbNyeriTekan.getSelectedItem()+"',"+
                                "adnexa_kanan='"+TAdnexaKanan.getText()+"', adnexa_kiri='"+TAdnexaKiri.getText()+"', cavum_douglas='"+TCavumDouglas.getText()+"'")==true){
                                    TInspeksi.setText("");TInspeksiVulva.setText("");TInspekuloGine.setText("");
                                    cmbFluxusGine.setSelectedIndex(0);cmbFluorGine.setSelectedIndex(0); TVulvaInspekulo.setText("");
                                    TPortioInspekulo.setText(""); TSondage.setText(""); TPortioDalam.setText("");
                                    TBentuk.setText(""); TCavumUteri.setText(""); cmbMobilitas.setSelectedIndex(0);
                                    TUkuran.setText(""); cmbNyeriTekan.setSelectedIndex(0);
                                    TAdnexaKanan.setText(""); TAdnexaKiri.setText(""); TCavumDouglas.getText();
                                    tampilPemeriksaanGinekologi();
                            }                            
                        }else{
                            JOptionPane.showMessageDialog(rootPane,"Silahkan pilih data yang mau diganti..!!");
                            TCari.requestFocus();
                        }
                    }   break; 
                case 8:
                    if(!Catatan.getText().trim().equals("")){
                        if(tbCatatan.getSelectedRow()>-1){
                            if(Sequel.mengedittf("catatan_perawatan","no_rawat='"+tbCatatan.getValueAt(tbCatatan.getSelectedRow(),1)+
                                "' and tanggal='"+tbCatatan.getValueAt(tbCatatan.getSelectedRow(),4)+
                                "' and jam='"+tbCatatan.getValueAt(tbCatatan.getSelectedRow(),5)+
                                "' and kd_dokter='"+tbCatatan.getValueAt(tbCatatan.getSelectedRow(),6)+"'",
                                "no_rawat='"+TNoRw.getText()+"',catatan='"+Catatan.getText()+"',"+
                                "kd_dokter='"+KdDok3.getText()+"',tanggal='"+Valid.SetTgl(DTPTgl.getSelectedItem()+"")+"',"+
                                "jam='"+cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem()+"'")==true){
                                    Catatan.setText("");
                                    tampilCatatan();
                            }                            
                        }else{
                            JOptionPane.showMessageDialog(rootPane,"Silahkan pilih data yang mau diganti..!!");
                            TCari.requestFocus();
                        }
                    }   break;
                default:                
                    break;
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void tbRawatDrPrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRawatDrPrMouseClicked
        if(tabModeDrPr.getRowCount()!=0){
            try {
                getDataDrPr();
            } catch (java.lang.NullPointerException e) {
            }            
        }
    }//GEN-LAST:event_tbRawatDrPrMouseClicked

    private void kdptg2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdptg2KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nama from petugas where nip=?",TPerawat2,kdptg2.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnSeekPetugas2ActionPerformed(null);
        }else{
            Valid.pindah(evt,KdDok2,BtnSeekPetugas2);
        }    
    }//GEN-LAST:event_kdptg2KeyPressed

    private void BtnSeekPetugas2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekPetugas2ActionPerformed
        akses.setform("DlgRawatJalan");
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_BtnSeekPetugas2ActionPerformed

    private void KdDok2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDok2KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",TDokter2,KdDok2.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnSeekDokter2ActionPerformed(null);
        }else{            
            Valid.pindah(evt,TNoRw,kdptg2);
        }
    }//GEN-LAST:event_KdDok2KeyPressed

    private void BtnSeekDokter2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekDokter2ActionPerformed
        akses.setform("DlgRawatJalan");
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnSeekDokter2ActionPerformed

    private void DTPTglKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPTglKeyPressed
        Valid.pindah(evt,BtnSeekDokter,cmbJam);
    }//GEN-LAST:event_DTPTglKeyPressed

    private void cmbJamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJamKeyPressed
        Valid.pindah(evt,DTPTgl,cmbMnt);
    }//GEN-LAST:event_cmbJamKeyPressed

    private void cmbMntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMntKeyPressed
        Valid.pindah(evt,cmbJam,cmbDtk);
    }//GEN-LAST:event_cmbMntKeyPressed

    private void cmbDtkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDtkKeyPressed
        Valid.pindah(evt,cmbMnt,TCari);
    }//GEN-LAST:event_cmbDtkKeyPressed

    private void ChkJlnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkJlnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkJlnActionPerformed

    private void btnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienActionPerformed
        akses.setform("DlgRawatJalan");
        pasien.emptTeks();
        pasien.isCek();
        pasien.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        pasien.setLocationRelativeTo(internalFrame1);
        pasien.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_btnPasienActionPerformed

    private void btnPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPasienKeyPressed
        Valid.pindah(evt,TCariPasien,DTPCari1);
    }//GEN-LAST:event_btnPasienKeyPressed

    private void cmbImunKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbImunKeyPressed
        Valid.pindah(evt,TGCS,TKeluhanTambahan); 
    }//GEN-LAST:event_cmbImunKeyPressed

    private void tbPemeriksaanObstetriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPemeriksaanObstetriMouseClicked
        // TODO add your handling code here:
        if(tabModeObstetri.getRowCount()!=0) {
            try {
                getDataPemeriksaanObstetri();

            } catch (java.lang.NullPointerException e) {

            }
        }
    }//GEN-LAST:event_tbPemeriksaanObstetriMouseClicked

    private void ChkInput1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput1ActionPerformed
        // TODO add your handling code here:
        isForm2();
    }//GEN-LAST:event_ChkInput1ActionPerformed

    private void TTinggi_uteriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TTinggi_uteriKeyPressed
        Valid.pindah(evt,TNoRw,cmbJanin);
    }//GEN-LAST:event_TTinggi_uteriKeyPressed

    private void TLetakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TLetakKeyPressed
        Valid.pindah(evt,cmbJanin,cmbPanggul);
    }//GEN-LAST:event_TLetakKeyPressed

    private void TKualitas_dtkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKualitas_dtkKeyPressed
        Valid.pindah(evt,TKualitas_mnt,cmbFluksus);
    }//GEN-LAST:event_TKualitas_dtkKeyPressed

    private void cmbPanggulKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPanggulKeyPressed
        Valid.pindah(evt,TLetak,TDenyut);
    }//GEN-LAST:event_cmbPanggulKeyPressed

    private void TTebalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TTebalKeyPressed
        Valid.pindah(evt,cmbDalam,cmbArah);
    }//GEN-LAST:event_TTebalKeyPressed

    private void TDenyutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TDenyutKeyPressed
        Valid.pindah(evt,cmbPanggul,cmbKontraksi);
    }//GEN-LAST:event_TDenyutKeyPressed

    private void TDenominatorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TDenominatorKeyPressed
        Valid.pindah(evt,TPenurunan,cmbFeto);
    }//GEN-LAST:event_TDenominatorKeyPressed

    private void TKualitas_mntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKualitas_mntKeyPressed
        Valid.pindah(evt,cmbKontraksi,TKualitas_dtk);
    }//GEN-LAST:event_TKualitas_mntKeyPressed

    private void cmbFetoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbFetoKeyPressed
        Valid.pindah(evt,TDenominator,BtnSimpan);
    }//GEN-LAST:event_cmbFetoKeyPressed

    private void cmbJaninKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJaninKeyPressed
        Valid.pindah(evt,TTinggi_uteri,TLetak);
    }//GEN-LAST:event_cmbJaninKeyPressed

    private void cmbKetubanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbKetubanKeyPressed
        Valid.pindah(evt,cmbAlbus,TVulva);
    }//GEN-LAST:event_cmbKetubanKeyPressed

    private void TPortioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPortioKeyPressed
        Valid.pindah(evt,TVulva,cmbDalam);
    }//GEN-LAST:event_TPortioKeyPressed

    private void TVulvaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TVulvaKeyPressed
        Valid.pindah(evt,cmbKetuban,TPortio);
    }//GEN-LAST:event_TVulvaKeyPressed

    private void cmbKontraksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbKontraksiKeyPressed
        Valid.pindah(evt,TDenyut,TKualitas_mnt);
    }//GEN-LAST:event_cmbKontraksiKeyPressed

    private void cmbAlbusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbAlbusKeyPressed
        Valid.pindah(evt,cmbFluksus,cmbKetuban);
    }//GEN-LAST:event_cmbAlbusKeyPressed

    private void cmbFluksusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbFluksusKeyPressed
        Valid.pindah(evt,TKualitas_dtk,cmbAlbus);
    }//GEN-LAST:event_cmbFluksusKeyPressed

    private void cmbDalamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDalamKeyPressed
        Valid.pindah(evt,TPortio,TTebal);
    }//GEN-LAST:event_cmbDalamKeyPressed

    private void TPembukaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPembukaanKeyPressed
        Valid.pindah(evt,cmbArah,TPenurunan);
    }//GEN-LAST:event_TPembukaanKeyPressed

    private void TPenurunanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPenurunanKeyPressed
        Valid.pindah(evt,TPembukaan,TDenominator);
    }//GEN-LAST:event_TPenurunanKeyPressed

    private void cmbArahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbArahKeyPressed
        Valid.pindah(evt,TTebal,TPembukaan);
    }//GEN-LAST:event_cmbArahKeyPressed

    private void tbPemeriksaanGinekologiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPemeriksaanGinekologiMouseClicked
        // TODO add your handling code here:
        if(tabModeGinekologi.getRowCount()!=0) {
            try {
                getDataPemeriksaanGinekologi();

            } catch (java.lang.NullPointerException e) {

            }
        }
    }//GEN-LAST:event_tbPemeriksaanGinekologiMouseClicked

    private void Scroll5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Scroll5KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Scroll5KeyPressed

    private void ChkInput2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput2ActionPerformed
        isForm3();
    }//GEN-LAST:event_ChkInput2ActionPerformed

    private void TInspeksiVulvaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TInspeksiVulvaKeyPressed
        Valid.pindah(evt,TInspeksi,TInspekuloGine);
    }//GEN-LAST:event_TInspeksiVulvaKeyPressed

    private void TAdnexaKananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TAdnexaKananKeyPressed
        Valid.pindah(evt,cmbNyeriTekan,TAdnexaKiri);
    }//GEN-LAST:event_TAdnexaKananKeyPressed

    private void cmbMobilitasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMobilitasKeyPressed
        Valid.pindah(evt,TCavumUteri,TUkuran);
    }//GEN-LAST:event_cmbMobilitasKeyPressed

    private void TInspekuloGineKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TInspekuloGineKeyPressed
        Valid.pindah(evt,TInspeksiVulva,cmbFluxusGine);
    }//GEN-LAST:event_TInspekuloGineKeyPressed

    private void TPortioInspekuloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPortioInspekuloKeyPressed
        Valid.pindah(evt,TVulvaInspekulo,TSondage);
    }//GEN-LAST:event_TPortioInspekuloKeyPressed

    private void TCavumUteriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCavumUteriKeyPressed
        Valid.pindah(evt,TBentuk,cmbMobilitas);
    }//GEN-LAST:event_TCavumUteriKeyPressed

    private void cmbFluorGineKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbFluorGineKeyPressed
        Valid.pindah(evt,cmbFluxusGine,TVulvaInspekulo);
    }//GEN-LAST:event_cmbFluorGineKeyPressed

    private void TInspeksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TInspeksiKeyPressed
        Valid.pindah(evt,TNoRw,TInspeksiVulva);
    }//GEN-LAST:event_TInspeksiKeyPressed

    private void cmbFluxusGineKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbFluxusGineKeyPressed
        Valid.pindah(evt,TInspekuloGine,cmbFluorGine);
    }//GEN-LAST:event_cmbFluxusGineKeyPressed

    private void TVulvaInspekuloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TVulvaInspekuloKeyPressed
        Valid.pindah(evt,cmbFluorGine,TPortioInspekulo);
    }//GEN-LAST:event_TVulvaInspekuloKeyPressed

    private void TPortioDalamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPortioDalamKeyPressed
        Valid.pindah(evt,TSondage,TBentuk);
    }//GEN-LAST:event_TPortioDalamKeyPressed

    private void TBentukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBentukKeyPressed
        Valid.pindah(evt,TPortioDalam,TCavumUteri);
    }//GEN-LAST:event_TBentukKeyPressed

    private void cmbNyeriTekanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbNyeriTekanKeyPressed
        Valid.pindah(evt,TUkuran,TAdnexaKanan);
    }//GEN-LAST:event_cmbNyeriTekanKeyPressed

    private void TSondageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TSondageKeyPressed
        Valid.pindah(evt,TPortioInspekulo,TPortioDalam);
    }//GEN-LAST:event_TSondageKeyPressed

    private void TAdnexaKiriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TAdnexaKiriKeyPressed
        Valid.pindah(evt,TAdnexaKanan,TCavumDouglas);
    }//GEN-LAST:event_TAdnexaKiriKeyPressed

    private void TCavumDouglasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCavumDouglasKeyPressed
        Valid.pindah(evt,TAdnexaKiri,BtnSimpan);
    }//GEN-LAST:event_TCavumDouglasKeyPressed

    private void TUkuranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TUkuranKeyPressed
        Valid.pindah(evt,cmbMobilitas,cmbNyeriTekan);
    }//GEN-LAST:event_TUkuranKeyPressed

    private void TKeluhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKeluhanKeyPressed
//        Valid.pindah(evt,DTPTgl,TPemeriksaan);
    }//GEN-LAST:event_TKeluhanKeyPressed

    private void tbRawatDrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRawatDrKeyReleased
        if(tabModeDr.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getDataDr();
                } catch (java.lang.NullPointerException e) {
                }
            }
            
        }
    }//GEN-LAST:event_tbRawatDrKeyReleased

    private void tbRawatPrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRawatPrKeyReleased
        if(tabModePr.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getDataPr();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbRawatPrKeyReleased

    private void tbRawatDrPrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRawatDrPrKeyReleased
        if(tabModeDrPr.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getDataDrPr();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbRawatDrPrKeyReleased

    private void tbPemeriksaanObstetriKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPemeriksaanObstetriKeyReleased
        // TODO add your handling code here:
        if(tabModeObstetri.getRowCount()!=0) {
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)) {
                try {
                    getDataPemeriksaanObstetri();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbPemeriksaanObstetriKeyReleased

    private void tbPemeriksaanGinekologiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPemeriksaanGinekologiKeyReleased
        // TODO add your handling code here:
        if(tabModeGinekologi.getRowCount()!=0) {
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)) {
                try {
                    getDataPemeriksaanGinekologi();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbPemeriksaanGinekologiKeyReleased

    private void tbTindakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbTindakanKeyPressed
        if(tbTindakan.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    i=tbTindakan.getSelectedColumn();
                    if(i==1){
                        if(tbTindakan.getSelectedRow()>-1){
                            tbTindakan.setValueAt(true,tbTindakan.getSelectedRow(),0);
                        }
                        TCari.setText("");
                        TCari.requestFocus();
                    }
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                TCari.setText("");
                TCari.requestFocus();
            }
        }
    }//GEN-LAST:event_tbTindakanKeyPressed

    private void tbTindakan2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbTindakan2KeyPressed
        if(tbTindakan2.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    i=tbTindakan2.getSelectedColumn();
                    if(i==1){
                        if(tbTindakan2.getSelectedRow()>-1){
                            tbTindakan2.setValueAt(true,tbTindakan2.getSelectedRow(),0);
                        }
                        TCari.setText("");
                        TCari.requestFocus();
                    }
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                TCari.setText("");
                TCari.requestFocus();
            }
        }
    }//GEN-LAST:event_tbTindakan2KeyPressed

    private void tbTindakan3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbTindakan3KeyPressed
        if(tbTindakan3.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    i=tbTindakan3.getSelectedColumn();
                    if(i==1){
                        if(tbTindakan3.getSelectedRow()>-1){
                            tbTindakan3.setValueAt(true,tbTindakan3.getSelectedRow(),0);
                        }
                        TCari.setText("");
                        TCari.requestFocus();
                    }
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                TCari.setText("");
                TCari.requestFocus();
            }
        }
    }//GEN-LAST:event_tbTindakan3KeyPressed

    private void BtnTambahTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahTindakanActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DlgJnsPerawatanRalan perawatan=new DlgJnsPerawatanRalan(null,false);
        perawatan.emptTeks();
        perawatan.isCek();
        perawatan.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
        perawatan.setLocationRelativeTo(internalFrame1);
        perawatan.setAlwaysOnTop(false);
        perawatan.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahTindakanActionPerformed

    private void BtnResepObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnResepObatActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{   
            if(Sequel.cariInteger("select count(no_rawat) from kamar_inap where no_rawat=?",TNoRw.getText())>0){
                JOptionPane.showMessageDialog(null,"Maaf, Pasien sudah masuk Kamar Inap. Gunakan billing Ranap..!!!");
            }else {
                jmlparsial=0;
                if(aktifkanparsial.equals("yes")){
                    jmlparsial=Sequel.cariInteger("select count(kd_pj) from set_input_parsial where kd_pj=?",kd_pj);
                }
                if(jmlparsial>0){
                    inputResep();
                }else{
                    if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                        JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi ..!!");
                    }else{ 
                        inputResep();
                    }
                }                     
            }            
        }
    }//GEN-LAST:event_BtnResepObatActionPerformed

    private void BtnResepObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnResepObatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnResepObatKeyPressed

    private void BtnObatBhpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnObatBhpActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{ 
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            DlgPemberianObat dlgrwinap=new DlgPemberianObat(null,false);
            dlgrwinap.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
            dlgrwinap.setLocationRelativeTo(internalFrame1);
            dlgrwinap.isCek();
            dlgrwinap.setNoRm2(TNoRw.getText(),DTPCari1.getDate(),DTPCari2.getDate(),"ralan");
            dlgrwinap.tampilPO();
            dlgrwinap.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnObatBhpActionPerformed

    private void BtnObatBhpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnObatBhpKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnObatBhpKeyPressed

    private void BtnBerkasDigitalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBerkasDigitalActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{ 
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            DlgBerkasRawat berkas=new DlgBerkasRawat(null,true);
            berkas.setJudul("::[ Berkas Digital Perawatan ]::","berkasrawat/pages");
            try {
                berkas.loadURL("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+prop.getProperty("PORTWEB")+"/"+prop.getProperty("HYBRIDWEB")+"/"+"berkasrawat/login2.php?act=login&usere=admin&passwordte=akusayangsamakamu&no_rawat="+TNoRw.getText());
            } catch (Exception ex) {
                System.out.println("Notifikasi : "+ex);
            }

            berkas.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
            berkas.setLocationRelativeTo(internalFrame1);
            berkas.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }            
    }//GEN-LAST:event_BtnBerkasDigitalActionPerformed

    private void BtnBerkasDigitalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBerkasDigitalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnBerkasDigitalKeyPressed

    private void BtnPermintaanLabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPermintaanLabActionPerformed
        if(TPasien.getText().trim().equals("")||TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{            
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            DlgPermintaanLaboratorium dlgro=new DlgPermintaanLaboratorium(null,false);
            dlgro.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
            dlgro.setLocationRelativeTo(internalFrame1);
            dlgro.emptTeks();
            dlgro.isCek();
            dlgro.setNoRm(TNoRw.getText(),"Ralan");
            dlgro.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());            
        }
    }//GEN-LAST:event_BtnPermintaanLabActionPerformed

    private void BtnPermintaanLabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPermintaanLabKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPermintaanLabKeyPressed

    private void BtnPermintaanRadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPermintaanRadActionPerformed
        if(TPasien.getText().trim().equals("")||TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{            
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            DlgPermintaanRadiologi dlgro=new DlgPermintaanRadiologi(null,false);
            dlgro.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
            dlgro.setLocationRelativeTo(internalFrame1);
            dlgro.emptTeks();
            dlgro.isCek();
            dlgro.setNoRm(TNoRw.getText(),"Ralan");
            dlgro.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());            
        }
    }//GEN-LAST:event_BtnPermintaanRadActionPerformed

    private void BtnPermintaanRadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPermintaanRadKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPermintaanRadKeyPressed

    private void BtnInputObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInputObatActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{   
            if(Sequel.cariInteger("select count(no_rawat) from kamar_inap where no_rawat=?",TNoRw.getText())>0){
                JOptionPane.showMessageDialog(null,"Maaf, Pasien sudah masuk Kamar Inap. Gunakan billing Ranap..!!!");
            }else {
                jmlparsial=0;
                if(aktifkanparsial.equals("yes")){
                    jmlparsial=Sequel.cariInteger("select count(kd_pj) from set_input_parsial where kd_pj=?",kd_pj);
                }
                if(jmlparsial>0){
                    inputObat();
                }else{
                    if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                        JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi ..!!");
                    }else{ 
                        inputObat();
                    }
                }                    
            }            
        }
    }//GEN-LAST:event_BtnInputObatActionPerformed

    private void BtnInputObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnInputObatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnInputObatKeyPressed

    private void BtnSKDPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSKDPActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{ 
            if(Sequel.cariInteger("select count(no_rawat) from kamar_inap where no_rawat=?",TNoRw.getText())>0){
                JOptionPane.showMessageDialog(null,"Maaf, Pasien sudah masuk Kamar Inap. Gunakan billing Ranap..!!!");
            }else {
                SuratKontrol form=new SuratKontrol(null,false);
                form.isCek();
                form.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                form.setLocationRelativeTo(internalFrame1);      
                form.emptTeks();      
                form.setNoRm(TNoRM.getText(),TPasien.getText(), kode_poli,Sequel.cariIsi("select nm_poli from poliklinik where kd_poli=?",kode_poli),KdDok.getText(),TDokter.getText());
                form.setVisible(true);
            }                
        }
    }//GEN-LAST:event_BtnSKDPActionPerformed

    private void BtnCopyResepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCopyResepActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{ 
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            DlgCopyResep daftar=new DlgCopyResep(null,false);
            daftar.isCek();
            daftar.setRM(TNoRw.getText(),TNoRM.getText(),KdDok.getText(),kd_pj,"ralan");
            daftar.tampil();
            daftar.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
            daftar.setLocationRelativeTo(internalFrame1);
            daftar.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        } 
    }//GEN-LAST:event_BtnCopyResepActionPerformed

    private void BtnCopyResepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCopyResepKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCopyResepKeyPressed

    private void ChkAccorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkAccorActionPerformed
        isMenu();
    }//GEN-LAST:event_ChkAccorActionPerformed

    private void BtnKamarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKamarActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{   
            if(Sequel.cariInteger("select count(no_rawat) from kamar_inap where no_rawat=?",TNoRw.getText())>0){
                JOptionPane.showMessageDialog(null,"Maaf, Pasien sudah masuk Kamar Inap. Gunakan billing Ranap..!!!");
            }else {
                if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                    JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi ..!!");
                }else{ 
                    inputKamar();
                }                     
            }            
        }
    }//GEN-LAST:event_BtnKamarActionPerformed

    private void BtnRujukInternalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRujukInternalActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{   
            if(Sequel.cariInteger("select count(no_rawat) from kamar_inap where no_rawat=?",TNoRw.getText())>0){
                JOptionPane.showMessageDialog(null,"Maaf, Pasien sudah masuk Kamar Inap. Gunakan billing Ranap..!!!");
            }else {
                if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                    JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi ..!!");
                }else{ 
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    DlgRujukanPoliInternal dlgrjk=new DlgRujukanPoliInternal(null,false);
                    dlgrjk.setLocationRelativeTo(internalFrame1);
                    dlgrjk.isCek();
                    dlgrjk.setNoRm(TNoRw.getText(),TNoRM.getText(),TPasien.getText(),this.getWidth()+20,this.getHeight()+20);
                    dlgrjk.setVisible(true);
                    this.setCursor(Cursor.getDefaultCursor());
                }                     
            }            
        }
    }//GEN-LAST:event_BtnRujukInternalActionPerformed

    private void BtnRujukKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRujukKeluarActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{   
            if(Sequel.cariInteger("select count(no_rawat) from kamar_inap where no_rawat=?",TNoRw.getText())>0){
                JOptionPane.showMessageDialog(null,"Maaf, Pasien sudah masuk Kamar Inap. Gunakan billing Ranap..!!!");
            }else {
                if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                    JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi ..!!");
                }else{ 
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    DlgRujuk dlgrjk=new DlgRujuk(null,false);
                    dlgrjk.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                    dlgrjk.setLocationRelativeTo(internalFrame1);
                    dlgrjk.emptTeks();
                    dlgrjk.isCek();
                    dlgrjk.setNoRm(TNoRw.getText(),DTPCari1.getDate(),DTPCari2.getDate());
                    dlgrjk.tampil();
                    dlgrjk.setVisible(true);
                    this.setCursor(Cursor.getDefaultCursor());
                }                     
            }            
        }
    }//GEN-LAST:event_BtnRujukKeluarActionPerformed

    private void BtnCatatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCatatanActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{   
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            DlgCatatan catatan=new DlgCatatan(null,true);
            catatan.setNoRm(TNoRM.getText());
            catatan.setSize(720,330);
            catatan.setLocationRelativeTo(internalFrame1);
            catatan.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnCatatanActionPerformed

    private void ChkInput3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput3ActionPerformed
        isForm4();
    }//GEN-LAST:event_ChkInput3ActionPerformed

    private void CatatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CatatanKeyPressed
        Valid.pindah(evt,KdDok3,BtnSimpan);
    }//GEN-LAST:event_CatatanKeyPressed

    private void tbCatatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCatatanMouseClicked
        if(TabModeCatatan.getRowCount()!=0){
            try {
                getDataCatatan();
            } catch (java.lang.NullPointerException e) {
            }

        }
    }//GEN-LAST:event_tbCatatanMouseClicked

    private void tbCatatanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbCatatanKeyReleased
        if(TabModeCatatan.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getDataCatatan();
                } catch (java.lang.NullPointerException e) {
                }
            }

        }
    }//GEN-LAST:event_tbCatatanKeyReleased

    private void KdDok3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDok3KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",TDokter3,KdDok3.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnSeekDokter3ActionPerformed(null);
        }else{            
            Valid.pindah(evt,TNoRw,BtnSeekDokter3);
        }
    }//GEN-LAST:event_KdDok3KeyPressed

    private void BtnSeekDokter3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekDokter3ActionPerformed
        akses.setform("DlgRawatJalan");
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnSeekDokter3ActionPerformed

    private void TPemeriksaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPemeriksaanKeyPressed
       // Valid.pindah(evt,TKeluhan,TSuhu);
    }//GEN-LAST:event_TPemeriksaanKeyPressed

    private void TSuhuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TSuhuKeyPressed
        Valid.pindah(evt,TPemeriksaan,TTinggi);
    }//GEN-LAST:event_TSuhuKeyPressed

    private void TTensiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TTensiKeyPressed
        Valid.pindah(evt,TBerat,TRespirasi);
    }//GEN-LAST:event_TTensiKeyPressed

    private void TTinggiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TTinggiKeyPressed
        Valid.pindah(evt,TSuhu,TBerat);
    }//GEN-LAST:event_TTinggiKeyPressed

    private void TRespirasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TRespirasiKeyPressed
        Valid.pindah(evt,TTensi,TNadi);
    }//GEN-LAST:event_TRespirasiKeyPressed

    private void TBeratKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBeratKeyPressed
        Valid.pindah(evt,TTinggi,TTensi);
    }//GEN-LAST:event_TBeratKeyPressed

    private void TNadiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TNadiActionPerformed

    }//GEN-LAST:event_TNadiActionPerformed

    private void TNadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNadiKeyPressed
        Valid.pindah(evt,TRespirasi,TGCS);
    }//GEN-LAST:event_TNadiKeyPressed

    private void TGCSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TGCSKeyPressed
        Valid.pindah(evt,TNadi,cmbImun);
    }//GEN-LAST:event_TGCSKeyPressed

    private void TKeluhanTambahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKeluhanTambahanKeyPressed
        Valid.pindah(evt,cmbImun,TPenilaian);
    }//GEN-LAST:event_TKeluhanTambahanKeyPressed

    private void TPenilaianKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPenilaianKeyPressed
//        Valid.pindah(evt,TKeluhanTambahan,TindakLanjut);
    }//GEN-LAST:event_TPenilaianKeyPressed

    private void TindakLanjutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TindakLanjutKeyPressed
//        Valid.pindah(evt,TPenilaian,BtnSimpan);
    }//GEN-LAST:event_TindakLanjutKeyPressed

    private void BtnTriaseIGDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTriaseIGDActionPerformed
        if(TPasien.getText().trim().equals("")||TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{  
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            DataTriaseIGD form=new DataTriaseIGD(null,false);
            form.isCek();
            form.setNoRm(TNoRw.getText(),TNoRM.getText(),TPasien.getText());
            form.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            form.setLocationRelativeTo(internalFrame1);
            form.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnTriaseIGDActionPerformed

    private void BtnResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnResumeActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu dengan menklik data pada table...!!!");
            TCari.requestFocus();
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            RMDataResumePasien resume=new RMDataResumePasien(null,false);
            resume.isCek();
            resume.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            resume.setLocationRelativeTo(internalFrame1);
            resume.setNoRm(TNoRw.getText(),DTPCari2.getDate());
            resume.tampil();
            resume.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnResumeActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void tbPemeriksaanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPemeriksaanKeyReleased
        if(tabModePemeriksaan.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getDataPemeriksaan();
                } catch (java.lang.NullPointerException e) {
                }
            }

        }
    }//GEN-LAST:event_tbPemeriksaanKeyReleased

    private void tbPemeriksaanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPemeriksaanMouseClicked
        if(tabModePemeriksaan.getRowCount()!=0){
            try {
                getDataPemeriksaan();
            } catch (java.lang.NullPointerException e) {
            }

        }
    }//GEN-LAST:event_tbPemeriksaanMouseClicked

    private void TKeluhanTambahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TKeluhanTambahanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TKeluhanTambahanActionPerformed

    private void TAlergiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TAlergiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TAlergiActionPerformed

    private void TAlergiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TAlergiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TAlergiKeyPressed

    private void TKeluhanUtamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TKeluhanUtamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TKeluhanUtamaActionPerformed

    private void TKeluhanUtamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKeluhanUtamaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TKeluhanUtamaKeyPressed

    private void AbKepalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AbKepalaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AbKepalaActionPerformed

    private void AbLeherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AbLeherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AbLeherActionPerformed

    private void AbGenitalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AbGenitalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AbGenitalActionPerformed

    private void tbPemeriksaanUSGMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPemeriksaanUSGMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbPemeriksaanUSGMouseClicked

    private void tbPemeriksaanUSGKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPemeriksaanUSGKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbPemeriksaanUSGKeyReleased

    private void ChkInput4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkInput4ActionPerformed

    private void TBpdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBpdKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TBpdKeyPressed

    private void TFlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TFlKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFlKeyPressed

    private void TAcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TAcKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TAcKeyPressed

    private void cmbGradeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbGradeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGradeKeyPressed

    private void TCrlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCrlKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCrlKeyPressed

    private void THcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_THcKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_THcKeyPressed

    private void cmbCairanKetubanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCairanKetubanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCairanKetubanKeyPressed

    private void cmbKehamilanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbKehamilanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbKehamilanKeyPressed

    private void cmbPlasentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPlasentaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPlasentaKeyPressed

    private void TGsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TGsKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TGsKeyPressed

    private void TUsiaHamilKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TUsiaHamilKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TUsiaHamilKeyPressed

    private void cmbGerakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbGerakanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGerakanKeyPressed

    private void cmbAnatomiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbAnatomiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAnatomiKeyPressed

    private void cmbLetakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbLetakKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbLetakKeyPressed

    private void cmbJkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJkKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbJkKeyPressed

    private void TTbjKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TTbjKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TTbjKeyPressed

    private void cmbOuiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbOuiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbOuiKeyPressed

    private void TDenyutJantungKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TDenyutJantungKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TDenyutJantungKeyPressed

    private void cmbCairanKetubanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCairanKetubanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCairanKetubanActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgRawatJalan dialog = new DlgRawatJalan(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void tampilDr() {
        Valid.tabelKosong(tabModeDr);
        try{
            ps=koneksi.prepareStatement("select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                   "concat(rawat_jl_dr.kd_jenis_prw,' ',jns_perawatan.nm_perawatan),rawat_jl_dr.kd_dokter,dokter.nm_dokter,"+
                   "rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat,rawat_jl_dr.biaya_rawat,rawat_jl_dr.kd_jenis_prw, " +
                   "rawat_jl_dr.tarif_tindakandr,rawat_jl_dr.kso "+
                   "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                   "dokter inner join rawat_jl_dr "+
                   "on rawat_jl_dr.no_rawat=reg_periksa.no_rawat "+
                   "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                   "and rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                   "and rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                   "where rawat_jl_dr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and rawat_jl_dr.no_rawat like ? or "+
                    "rawat_jl_dr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and reg_periksa.no_rkm_medis like ? or "+
                    "rawat_jl_dr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pasien.nm_pasien like ? or "+
                    "rawat_jl_dr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and jns_perawatan.nm_perawatan like ? or "+
                    "rawat_jl_dr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and rawat_jl_dr.kd_dokter like ? or "+
                    "rawat_jl_dr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and dokter.nm_dokter like ? or "+
                    "rawat_jl_dr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and tgl_perawatan like ? "+
                   " order by rawat_jl_dr.no_rawat desc");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(3,"%"+TCariPasien.getText()+"%");
                ps.setString(4,"%"+TCari.getText().trim()+"%");
                ps.setString(5,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(6,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(7,"%"+TCariPasien.getText()+"%");
                ps.setString(8,"%"+TCari.getText().trim()+"%");
                ps.setString(9,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(10,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(11,"%"+TCariPasien.getText()+"%");
                ps.setString(12,"%"+TCari.getText().trim()+"%");
                ps.setString(13,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(14,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(15,"%"+TCariPasien.getText()+"%");
                ps.setString(16,"%"+TCari.getText().trim()+"%");
                ps.setString(17,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(18,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(19,"%"+TCariPasien.getText()+"%");
                ps.setString(20,"%"+TCari.getText().trim()+"%");
                ps.setString(21,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(22,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(23,"%"+TCariPasien.getText()+"%");
                ps.setString(24,"%"+TCari.getText().trim()+"%");
                ps.setString(25,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(26,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(27,"%"+TCariPasien.getText()+"%");
                ps.setString(28,"%"+TCari.getText().trim()+"%");
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeDr.addRow(new Object[]{
                        false,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                        rs.getString(8),rs.getDouble(9),rs.getString("kd_jenis_prw"),rs.getString("tarif_tindakandr"),rs.getString("kso")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }           
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModeDr.getRowCount());
    }
    
    private void getDataDr() {
        if(tbRawatDr.getSelectedRow()!= -1){
            TNoRw.setText(tbRawatDr.getValueAt(tbRawatDr.getSelectedRow(),1).toString());
            TNoRM.setText(tbRawatDr.getValueAt(tbRawatDr.getSelectedRow(),2).toString());
            TPasien.setText(tbRawatDr.getValueAt(tbRawatDr.getSelectedRow(),3).toString());
            KdDok.setText(tbRawatDr.getValueAt(tbRawatDr.getSelectedRow(),5).toString());
            TDokter.setText(tbRawatDr.getValueAt(tbRawatDr.getSelectedRow(),6).toString());
            cmbJam.setSelectedItem(tbRawatDr.getValueAt(tbRawatDr.getSelectedRow(),8).toString().substring(0,2));
            cmbMnt.setSelectedItem(tbRawatDr.getValueAt(tbRawatDr.getSelectedRow(),8).toString().substring(3,5));
            cmbDtk.setSelectedItem(tbRawatDr.getValueAt(tbRawatDr.getSelectedRow(),8).toString().substring(6,8));
            Valid.SetTgl(DTPTgl,tbRawatDr.getValueAt(tbRawatDr.getSelectedRow(),7).toString());
        }
    }

    private void tampilPr() {
        Valid.tabelKosong(tabModePr);
        try{  
            ps2=koneksi.prepareStatement("select rawat_jl_pr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                   "concat(rawat_jl_pr.kd_jenis_prw,' ',jns_perawatan.nm_perawatan),rawat_jl_pr.nip,petugas.nama,"+
                   "rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat,rawat_jl_pr.biaya_rawat,rawat_jl_pr.kd_jenis_prw, " +
                   "rawat_jl_pr.tarif_tindakanpr,rawat_jl_pr.kso from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                   "petugas inner join rawat_jl_pr "+
                   "on rawat_jl_pr.no_rawat=reg_periksa.no_rawat "+
                   "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                   "and rawat_jl_pr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                   "and rawat_jl_pr.nip=petugas.nip where  "+
                    "rawat_jl_pr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and rawat_jl_pr.no_rawat like ? or "+
                    "rawat_jl_pr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and reg_periksa.no_rkm_medis like ? or "+
                    "rawat_jl_pr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pasien.nm_pasien like ? or "+
                    "rawat_jl_pr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and jns_perawatan.nm_perawatan like ? or "+
                    "rawat_jl_pr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and rawat_jl_pr.nip like ? or "+
                    "rawat_jl_pr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and petugas.nama like ? or "+
                    "rawat_jl_pr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and rawat_jl_pr.tgl_perawatan like ? "+
                   "order by rawat_jl_pr.no_rawat desc"); 
            try{
                ps2.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps2.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps2.setString(3,"%"+TCariPasien.getText()+"%");
                ps2.setString(4,"%"+TCari.getText().trim()+"%");
                ps2.setString(5,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps2.setString(6,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps2.setString(7,"%"+TCariPasien.getText()+"%");
                ps2.setString(8,"%"+TCari.getText().trim()+"%");
                ps2.setString(9,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps2.setString(10,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps2.setString(11,"%"+TCariPasien.getText()+"%");
                ps2.setString(12,"%"+TCari.getText().trim()+"%");
                ps2.setString(13,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps2.setString(14,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps2.setString(15,"%"+TCariPasien.getText()+"%");
                ps2.setString(16,"%"+TCari.getText().trim()+"%");
                ps2.setString(17,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps2.setString(18,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps2.setString(19,"%"+TCariPasien.getText()+"%");
                ps2.setString(20,"%"+TCari.getText().trim()+"%");
                ps2.setString(21,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps2.setString(22,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps2.setString(23,"%"+TCariPasien.getText()+"%");
                ps2.setString(24,"%"+TCari.getText().trim()+"%");
                ps2.setString(25,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps2.setString(26,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps2.setString(27,"%"+TCariPasien.getText()+"%");
                ps2.setString(28,"%"+TCari.getText().trim()+"%");
                rs=ps2.executeQuery();
                while(rs.next()){
                    tabModePr.addRow(new Object[]{false,rs.getString(1),
                                   rs.getString(2),
                                   rs.getString(3),
                                   rs.getString(4),
                                   rs.getString(5),
                                   rs.getString(6),
                                   rs.getString(7),
                                   rs.getString(8),
                                   rs.getDouble(9),
                                   rs.getString("kd_jenis_prw"),
                                   rs.getString("tarif_tindakanpr"),
                                   rs.getString("kso")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps2!=null){
                    ps2.close();
                }
            }                  
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModePr.getRowCount());
    }

    private void getDataPr() {
        if(tbRawatPr.getSelectedRow()!= -1){
            TNoRw.setText(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),1).toString());
            TNoRM.setText(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),2).toString());
            TPasien.setText(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),3).toString());   
            kdptg.setText(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),5).toString());
            TPerawat.setText(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),6).toString());
            cmbJam.setSelectedItem(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),8).toString().substring(0,2));
            cmbMnt.setSelectedItem(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),8).toString().substring(3,5));
            cmbDtk.setSelectedItem(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),8).toString().substring(6,8));
            Valid.SetTgl(DTPTgl,tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),7).toString());
        }
    }
    
    private void tampilDrPr() {
        Valid.tabelKosong(tabModeDrPr);
        try{
            ps3=koneksi.prepareStatement("select rawat_jl_drpr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                   "concat(rawat_jl_drpr.kd_jenis_prw,' ',jns_perawatan.nm_perawatan),rawat_jl_drpr.kd_dokter,dokter.nm_dokter,"+
                   "rawat_jl_drpr.nip,petugas.nama,rawat_jl_drpr.tgl_perawatan,rawat_jl_drpr.jam_rawat,rawat_jl_drpr.biaya_rawat,rawat_jl_drpr.kd_jenis_prw, " +
                   "rawat_jl_drpr.tarif_tindakandr,rawat_jl_drpr.tarif_tindakanpr,rawat_jl_drpr.kso  "+
                   "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                   "dokter inner join rawat_jl_drpr inner join petugas on rawat_jl_drpr.no_rawat=reg_periksa.no_rawat "+
                   "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis and rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                   "and rawat_jl_drpr.kd_dokter=dokter.kd_dokter and rawat_jl_drpr.nip=petugas.nip "+
                   "where rawat_jl_drpr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and rawat_jl_drpr.no_rawat like ? or "+
                    "rawat_jl_drpr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and reg_periksa.no_rkm_medis like ? or "+
                    "rawat_jl_drpr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pasien.nm_pasien like ? or "+
                    "rawat_jl_drpr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and jns_perawatan.nm_perawatan like ? or "+
                    "rawat_jl_drpr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and rawat_jl_drpr.kd_dokter like ? or "+
                    "rawat_jl_drpr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and dokter.nm_dokter like ? or "+
                    "rawat_jl_drpr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and rawat_jl_drpr.nip like ? or "+
                    "rawat_jl_drpr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and petugas.nama like ? or "+
                    "rawat_jl_drpr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and tgl_perawatan like ?  "+
                   " order by rawat_jl_drpr.no_rawat desc");
            try{
                ps3.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps3.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps3.setString(3,"%"+TCariPasien.getText()+"%");
                ps3.setString(4,"%"+TCari.getText().trim()+"%");
                ps3.setString(5,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps3.setString(6,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps3.setString(7,"%"+TCariPasien.getText()+"%");
                ps3.setString(8,"%"+TCari.getText().trim()+"%");
                ps3.setString(9,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps3.setString(10,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps3.setString(11,"%"+TCariPasien.getText()+"%");
                ps3.setString(12,"%"+TCari.getText().trim()+"%");
                ps3.setString(13,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps3.setString(14,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps3.setString(15,"%"+TCariPasien.getText()+"%");
                ps3.setString(16,"%"+TCari.getText().trim()+"%");
                ps3.setString(17,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps3.setString(18,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps3.setString(19,"%"+TCariPasien.getText()+"%");
                ps3.setString(20,"%"+TCari.getText().trim()+"%");
                ps3.setString(21,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps3.setString(22,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps3.setString(23,"%"+TCariPasien.getText()+"%");
                ps3.setString(24,"%"+TCari.getText().trim()+"%");
                ps3.setString(25,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps3.setString(26,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps3.setString(27,"%"+TCariPasien.getText()+"%");
                ps3.setString(28,"%"+TCari.getText().trim()+"%");
                ps3.setString(29,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps3.setString(30,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps3.setString(31,"%"+TCariPasien.getText()+"%");
                ps3.setString(32,"%"+TCari.getText().trim()+"%");
                ps3.setString(33,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps3.setString(34,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps3.setString(35,"%"+TCariPasien.getText()+"%");
                ps3.setString(36,"%"+TCari.getText().trim()+"%");
                rs=ps3.executeQuery();
                while(rs.next()){
                    tabModeDrPr.addRow(new Object[]{
                        false,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                        rs.getString(8),rs.getString(9),rs.getString(10),rs.getDouble(11),rs.getString("kd_jenis_prw"),
                        rs.getString("tarif_tindakandr"),rs.getString("tarif_tindakanpr"),rs.getString("kso")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps3!=null){
                    ps3.close();
                }
            }              
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModeDrPr.getRowCount());
    }
    
    private void getDataDrPr() {
        if(tbRawatDrPr.getSelectedRow()!= -1){
            TNoRw.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),1).toString());
            TNoRM.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),2).toString());
            TPasien.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),3).toString());
            KdDok2.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),5).toString());
            TDokter2.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),6).toString());
            kdptg2.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),7).toString());
            TPerawat2.setText(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),8).toString());
            cmbJam.setSelectedItem(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),10).toString().substring(0,2));
            cmbMnt.setSelectedItem(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),10).toString().substring(3,5));
            cmbDtk.setSelectedItem(tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),10).toString().substring(6,8));
            Valid.SetTgl(DTPTgl,tbRawatDrPr.getValueAt(tbRawatDrPr.getSelectedRow(),9).toString());
        }
    }
    
    private void isRawat(){
        Sequel.cariIsi("select no_rkm_medis from reg_periksa where no_rawat=? ",TNoRM,TNoRw.getText());
        TCariPasien.setText(TNoRM.getText());
    }

    private void isPsien(){
        Sequel.cariIsi("select nm_pasien from pasien where no_rkm_medis=? ",TPasien,TNoRM.getText());
    }
    
    public void setNoRm(String norwt,Date tgl1,Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText("");
        DTPCari1.setDate(tgl1);
        DTPCari2.setDate(tgl2);
        isRawat();
        isPsien();  
        KdDok.setText(Sequel.cariIsi("select kd_dokter from reg_periksa where no_rawat=?",norwt));
        Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",TDokter,KdDok.getText());
        KdDok2.setText(KdDok.getText());
        KdDok3.setText(KdDok.getText());
        TDokter2.setText(TDokter.getText()); 
        TDokter3.setText(TDokter.getText()); 
        ChkInput.setSelected(true);
        isForm();
        ChkInput1.setSelected(true);
        isForm2();
        ChkInput2.setSelected(true);
        isForm3(); 
        ChkInput3.setSelected(true);
        isForm4();
        TabRawatMouseClicked(null);
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,250));
            panelGlass12.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            panelGlass12.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    private void isForm4(){
        if(ChkInput3.isSelected()==true){
            ChkInput3.setVisible(false);
            PanelInput3.setPreferredSize(new Dimension(WIDTH,140));
            panelGlass15.setVisible(true);      
            ChkInput3.setVisible(true);
        }else if(ChkInput3.isSelected()==false){           
            ChkInput3.setVisible(false);            
            PanelInput3.setPreferredSize(new Dimension(WIDTH,20));
            panelGlass15.setVisible(false);      
            ChkInput3.setVisible(true);
        }
    }
    
    private void isMenu(){
        if(ChkAccor.isSelected()==true){
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(135,HEIGHT));
            FormMenu.setVisible(true);      
            ChkAccor.setVisible(true);
        }else if(ChkAccor.isSelected()==false){           
            ChkAccor.setVisible(false);            
            PanelAccor.setPreferredSize(new Dimension(15,HEIGHT));
            FormMenu.setVisible(false);      
            ChkAccor.setVisible(true);
        }
    }
    
    public void isCek(){
        if(akses.getjml2()>=1){
            kdptg.setText(akses.getkode());
            Sequel.cariIsi("select nama from petugas where nip=?", TPerawat,kdptg.getText());
        }else{
            kdptg.setText("");
            TPerawat.setText("");
        }
        BtnSimpan.setEnabled(akses.gettindakan_ralan());
        BtnHapus.setEnabled(akses.gettindakan_ralan());
        BtnEdit.setEnabled(akses.gettindakan_ralan());
        BtnPrint.setEnabled(akses.gettindakan_ralan());
        BtnResepObat.setEnabled(akses.getresep_dokter());
        BtnCopyResep.setEnabled(akses.getresep_dokter());
        BtnObatBhp.setEnabled(akses.getberi_obat());  
        BtnInputObat.setEnabled(akses.getberi_obat());   
        BtnPermintaanLab.setEnabled(akses.getpermintaan_lab());     
        BtnBerkasDigital.setEnabled(akses.getberkas_digital_perawatan());    
        BtnPermintaanRad.setEnabled(akses.getpermintaan_radiologi());  
        BtnTambahTindakan.setEnabled(akses.gettarif_ralan());    
        BtnKamar.setEnabled(akses.getkamar_inap());   
        BtnRujukInternal.setEnabled(akses.getrujukan_poli_internal());
        BtnRujukKeluar.setEnabled(akses.getrujukan_keluar());
        BtnSKDP.setEnabled(akses.getskdp_bpjs());     
        BtnCatatan.setEnabled(akses.getcatatan_pasien());
        BtnTriaseIGD.setEnabled(akses.getdata_triase_igd());   
        BtnResume.setEnabled(akses.getdata_resume_pasien());      
        TCari.setPreferredSize(new Dimension(207,23));
    }

    private void tampilPemeriksaan() {
        Valid.tabelKosong(tabModePemeriksaan);
        try{  
            ps4=koneksi.prepareStatement("select pemeriksaan_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                    "pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.suhu_tubuh,pemeriksaan_ralan.tensi, " +
                    "pemeriksaan_ralan.nadi,pemeriksaan_ralan.respirasi,pemeriksaan_ralan.tinggi, " +
                    "pemeriksaan_ralan.berat,pemeriksaan_ralan.gcs,pemeriksaan_ralan.keluhanutama,pemeriksaan_ralan.keluhantambahan, "+                     
                    "pemeriksaan_ralan.keluhan,pemeriksaan_ralan.kepala,pemeriksaan_ralan.abkepala,pemeriksaan_ralan.leher,pemeriksaan_ralan.ableher, "+
                    "pemeriksaan_ralan.thorax,pemeriksaan_ralan.abthorax,pemeriksaan_ralan.abdomen,pemeriksaan_ralan.ababdomen,pemeriksaan_ralan.genital,pemeriksaan_ralan.abgenital, "+
                    "pemeriksaan_ralan.rektum,pemeriksaan_ralan.abrektum,pemeriksaan_ralan.ekstremitas, "+  
                    "pemeriksaan_ralan.abekstremitas,pemeriksaan_ralan.pemeriksaan,pemeriksaan_ralan.alergi,pemeriksaan_ralan.imun_ke,pemeriksaan_ralan.kesadaran,pemeriksaan_ralan.keadaanumum, "+
                    "pemeriksaan_ralan.rtl,pemeriksaan_ralan.penilaian from pasien inner join reg_periksa inner join pemeriksaan_ralan "+
                    "on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis where  "+
                    "pemeriksaan_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_ralan.no_rawat like ? or "+
                    "pemeriksaan_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and reg_periksa.no_rkm_medis like ? or "+
                    "pemeriksaan_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pasien.nm_pasien like ? or  "+
                    "pemeriksaan_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_ralan.alergi like ? or "+
                    "pemeriksaan_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_ralan.keluhan like ? or "+
                    "pemeriksaan_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_ralan.penilaian like ? or "+
                    "pemeriksaan_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_ralan.pemeriksaan like ? "+
                   "order by pemeriksaan_ralan.no_rawat desc"); 
            try{
                ps4.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(3,"%"+TCariPasien.getText()+"%");
                ps4.setString(4,"%"+TCari.getText().trim()+"%");
                ps4.setString(5,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(6,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(7,"%"+TCariPasien.getText()+"%");
                ps4.setString(8,"%"+TCari.getText().trim()+"%");
                ps4.setString(9,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(10,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(11,"%"+TCariPasien.getText()+"%");
                ps4.setString(12,"%"+TCari.getText().trim()+"%");
                ps4.setString(13,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(14,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(15,"%"+TCariPasien.getText()+"%");
                ps4.setString(16,"%"+TCari.getText().trim()+"%");
                ps4.setString(17,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(18,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(19,"%"+TCariPasien.getText()+"%");
                ps4.setString(20,"%"+TCari.getText().trim()+"%");
                ps4.setString(21,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(22,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(23,"%"+TCariPasien.getText()+"%");
                ps4.setString(24,"%"+TCari.getText().trim()+"%");
                ps4.setString(25,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(26,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(27,"%"+TCariPasien.getText()+"%");
                ps4.setString(28,"%"+TCari.getText().trim()+"%");
                rs=ps4.executeQuery();
                while(rs.next()){
                    tabModePemeriksaan.addRow(new Object[]{
                        false,rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                        rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
                        rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),
                        rs.getString(16),rs.getString(17),rs.getString(18),rs.getString(19),
                        rs.getString(20),rs.getString(21),rs.getString(22),rs.getString(23),
                        rs.getString(24),rs.getString(25),rs.getString(26),rs.getString(27),
                        rs.getString(28),rs.getString(29),rs.getString(30),rs.getString(31),rs.getString(32),
                        rs.getString(33),rs.getString(34),rs.getString(35),rs.getString(36),
                        
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps4!=null){
                    ps4.close();
                }
            }                  
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModePemeriksaan.getRowCount());
    }
    
    private void tampilCatatan() {
        Valid.tabelKosong(TabModeCatatan);
        try{  
            ps4=koneksi.prepareStatement("select catatan_perawatan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                    "catatan_perawatan.tanggal,catatan_perawatan.jam,catatan_perawatan.kd_dokter,dokter.nm_dokter,"+
                    "catatan_perawatan.catatan from pasien inner join reg_periksa inner join catatan_perawatan inner join dokter "+
                    "on catatan_perawatan.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "and catatan_perawatan.kd_dokter=dokter.kd_dokter where  "+
                    "catatan_perawatan.tanggal between ? and ? and reg_periksa.no_rkm_medis like ? and catatan_perawatan.no_rawat like ? or "+
                    "catatan_perawatan.tanggal between ? and ? and reg_periksa.no_rkm_medis like ? and reg_periksa.no_rkm_medis like ? or "+
                    "catatan_perawatan.tanggal between ? and ? and reg_periksa.no_rkm_medis like ? and pasien.nm_pasien like ? or  "+
                    "catatan_perawatan.tanggal between ? and ? and reg_periksa.no_rkm_medis like ? and catatan_perawatan.catatan like ? or "+
                    "catatan_perawatan.tanggal between ? and ? and reg_periksa.no_rkm_medis like ? and catatan_perawatan.kd_dokter like ? or "+
                    "catatan_perawatan.tanggal between ? and ? and reg_periksa.no_rkm_medis like ? and dokter.nm_dokter like ? "+
                   "order by catatan_perawatan.no_rawat desc"); 
            try{
                ps4.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(3,"%"+TCariPasien.getText()+"%");
                ps4.setString(4,"%"+TCari.getText().trim()+"%");
                ps4.setString(5,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(6,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(7,"%"+TCariPasien.getText()+"%");
                ps4.setString(8,"%"+TCari.getText().trim()+"%");
                ps4.setString(9,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(10,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(11,"%"+TCariPasien.getText()+"%");
                ps4.setString(12,"%"+TCari.getText().trim()+"%");
                ps4.setString(13,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(14,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(15,"%"+TCariPasien.getText()+"%");
                ps4.setString(16,"%"+TCari.getText().trim()+"%");
                ps4.setString(17,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(18,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(19,"%"+TCariPasien.getText()+"%");
                ps4.setString(20,"%"+TCari.getText().trim()+"%");
                ps4.setString(21,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps4.setString(22,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps4.setString(23,"%"+TCariPasien.getText()+"%");
                ps4.setString(24,"%"+TCari.getText().trim()+"%");
                rs=ps4.executeQuery();
                while(rs.next()){
                    TabModeCatatan.addRow(new Object[]{false,rs.getString(1),
                                   rs.getString(2),
                                   rs.getString(3),
                                   rs.getString(4),
                                   rs.getString(5),
                                   rs.getString(6),
                                   rs.getString(7),
                                   rs.getString(8)
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi Catatan : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps4!=null){
                    ps4.close();
                }
            }                  
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+TabModeCatatan.getRowCount());
    }

    private void getDataPemeriksaan() {
        if(tbPemeriksaan.getSelectedRow()!= -1){
            TNoRw.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),1).toString());
            TNoRM.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),2).toString());
            TPasien.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),3).toString());             
            TSuhu.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),6).toString()); 
            TTensi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),7).toString()); 
            TNadi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),8).toString()); 
            TRespirasi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),9).toString()); 
            TTinggi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),10).toString()); 
            TBerat.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),11).toString());  
            TGCS.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),12).toString()); 
            TKeluhanUtama.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),13).toString()); 
            TKeluhanTambahan.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),14).toString()); 
            TKeluhan.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),15).toString());
            CmbKepala.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),16).toString());
            AbKepala.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),17).toString()); 
            CmbLeher.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),18).toString());
            AbLeher.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),19).toString()); 
            CmbThorax.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),20).toString());
            AbThorax.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),21).toString()); 
            CmbAbdomen.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),22).toString());
            AbAbdomen.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),23).toString()); 
            CmbGenital.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),24).toString());
            AbGenital.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),25).toString()); 
            CmbRektum.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),26).toString());
            AbRektum.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),27).toString()); 
            CmbEktremitas.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),28).toString());
            AbExtremitas.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),29).toString()); 
            TPemeriksaan.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),30).toString()); 
            TAlergi.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),31).toString()); 
            cmbImun.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),32).toString());
            kesadaran.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),33).toString());
            keadaanumum.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),34).toString());
            TindakLanjut.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),35).toString()); 
            TPenilaian.setText(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),36).toString()); 
            cmbJam.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),5).toString().substring(0,2));
            cmbMnt.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),5).toString().substring(3,5));
            cmbDtk.setSelectedItem(tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),5).toString().substring(6,8));
            Valid.SetTgl(DTPTgl,tbPemeriksaan.getValueAt(tbPemeriksaan.getSelectedRow(),4).toString());
        }
    }
    
    private void getDataCatatan() {
        if(tbCatatan.getSelectedRow()!= -1){
            TNoRw.setText(tbCatatan.getValueAt(tbCatatan.getSelectedRow(),1).toString());
            TNoRM.setText(tbCatatan.getValueAt(tbCatatan.getSelectedRow(),2).toString());
            TPasien.setText(tbCatatan.getValueAt(tbCatatan.getSelectedRow(),3).toString()); 
            KdDok3.setText(tbCatatan.getValueAt(tbCatatan.getSelectedRow(),6).toString());  
            TDokter3.setText(tbCatatan.getValueAt(tbCatatan.getSelectedRow(),7).toString());
            Catatan.setText(tbCatatan.getValueAt(tbCatatan.getSelectedRow(),8).toString());             
            cmbJam.setSelectedItem(tbCatatan.getValueAt(tbCatatan.getSelectedRow(),5).toString().substring(0,2));
            cmbMnt.setSelectedItem(tbCatatan.getValueAt(tbCatatan.getSelectedRow(),5).toString().substring(3,5));
            cmbDtk.setSelectedItem(tbCatatan.getValueAt(tbCatatan.getSelectedRow(),5).toString().substring(6,8));
            Valid.SetTgl(DTPTgl,tbCatatan.getValueAt(tbCatatan.getSelectedRow(),4).toString());
        }
    }
    
    private void tampilPemeriksaanObstetri() {
        Valid.tabelKosong(tabModeObstetri);
        try{
            ps5=koneksi.prepareStatement("select pemeriksaan_obstetri_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                    "pemeriksaan_obstetri_ralan.tgl_perawatan,pemeriksaan_obstetri_ralan.jam_rawat,pemeriksaan_obstetri_ralan.tinggi_uteri,pemeriksaan_obstetri_ralan.janin,pemeriksaan_obstetri_ralan.letak, " +
                    "pemeriksaan_obstetri_ralan.panggul,pemeriksaan_obstetri_ralan.denyut,pemeriksaan_obstetri_ralan.kontraksi, " +
                    "pemeriksaan_obstetri_ralan.kualitas_mnt,pemeriksaan_obstetri_ralan.kualitas_dtk,pemeriksaan_obstetri_ralan.fluksus,pemeriksaan_obstetri_ralan.albus, " +
                    "pemeriksaan_obstetri_ralan.vulva,pemeriksaan_obstetri_ralan.portio,pemeriksaan_obstetri_ralan.dalam, pemeriksaan_obstetri_ralan.tebal, pemeriksaan_obstetri_ralan.arah, pemeriksaan_obstetri_ralan.pembukaan," +
                    "pemeriksaan_obstetri_ralan.penurunan, pemeriksaan_obstetri_ralan.denominator, pemeriksaan_obstetri_ralan.ketuban, pemeriksaan_obstetri_ralan.feto " +
                    "from pasien inner join reg_periksa inner join pemeriksaan_obstetri_ralan "+
                    "on pemeriksaan_obstetri_ralan.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis where  "+
                    "pemeriksaan_obstetri_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_obstetri_ralan.no_rawat like ? or "+
                    "pemeriksaan_obstetri_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and reg_periksa.no_rkm_medis like ? or "+
                    "pemeriksaan_obstetri_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pasien.nm_pasien like ? or  "+
                    "pemeriksaan_obstetri_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_obstetri_ralan.tinggi_uteri like ? or "+
                    "pemeriksaan_obstetri_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_obstetri_ralan.janin like ? or "+
                    "pemeriksaan_obstetri_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_obstetri_ralan.letak like ? "+
                    "order by pemeriksaan_obstetri_ralan.no_rawat desc");
            try {
                ps5.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps5.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps5.setString(3,"%"+TCariPasien.getText()+"%");
                ps5.setString(4,"%"+TCari.getText().trim()+"%");
                ps5.setString(5,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps5.setString(6,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps5.setString(7,"%"+TCariPasien.getText()+"%");
                ps5.setString(8,"%"+TCari.getText().trim()+"%");
                ps5.setString(9,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps5.setString(10,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps5.setString(11,"%"+TCariPasien.getText()+"%");
                ps5.setString(12,"%"+TCari.getText().trim()+"%");
                ps5.setString(13,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps5.setString(14,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps5.setString(15,"%"+TCariPasien.getText()+"%");
                ps5.setString(16,"%"+TCari.getText().trim()+"%");
                ps5.setString(17,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps5.setString(18,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps5.setString(19,"%"+TCariPasien.getText()+"%");
                ps5.setString(20,"%"+TCari.getText().trim()+"%");
                ps5.setString(21,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps5.setString(22,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps5.setString(23,"%"+TCariPasien.getText()+"%");
                ps5.setString(24,"%"+TCari.getText().trim()+"%");
                rs=ps5.executeQuery();
                while(rs.next()) {
                    tabModeObstetri.addRow(new Object[] {
                        false, rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("tinggi_uteri"),
                        rs.getString("janin"),rs.getString("letak"),rs.getString("panggul"),
                        rs.getString("denyut"),rs.getString("kontraksi"),rs.getString("kualitas_mnt"),
                        rs.getString("kualitas_dtk"),rs.getString("fluksus"),rs.getString("albus"),
                        rs.getString("vulva"),rs.getString("portio"),rs.getString("dalam"),
                        rs.getString("tebal"),rs.getString("arah"),rs.getString("pembukaan"),
                        rs.getString("penurunan"),rs.getString("denominator"),rs.getString("ketuban"),
                        rs.getString("feto")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);    
            } finally {
                if(rs!=null) {
                    rs.close();
                }
                if(ps5!=null) {
                    ps5.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi :" +e);
        }
        LCount.setText(""+tabModeObstetri.getRowCount());
    }     
        
    private void getDataPemeriksaanObstetri() {
        if(tbPemeriksaanObstetri.getSelectedRow()!= -1) {
            TNoRw.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),1).toString());
            TNoRM.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),2).toString());
            TPasien.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),3).toString());
            Valid.SetTgl(DTPTgl,tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),4).toString());
            cmbJam.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),5).toString().substring(0,2));
            cmbMnt.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),5).toString().substring(3,5));
            cmbDtk.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),5).toString().substring(6,8));
            TTinggi_uteri.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),6).toString());
            cmbJanin.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),7).toString());
            TLetak.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),8).toString());
            cmbPanggul.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),9).toString());
            TDenyut.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),10).toString());
            cmbKontraksi.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),11).toString());
            TKualitas_mnt.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),12).toString());
            TKualitas_dtk.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),13).toString());
            cmbFluksus.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),14).toString());
            cmbAlbus.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),15).toString());
            TVulva.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),16).toString());
            TPortio.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),17).toString());
            cmbDalam.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),18).toString());
            TTebal.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),19).toString());
            cmbArah.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),20).toString());
            TPembukaan.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),21).toString());
            TPenurunan.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),22).toString());
            TDenominator.setText(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),23).toString());
            cmbKetuban.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),24).toString());
            cmbFeto.setSelectedItem(tbPemeriksaanObstetri.getValueAt(tbPemeriksaanObstetri.getSelectedRow(),25).toString());
        }
    }
    
    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            @Override
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                // Membuat Date
                //Date dt = new Date();
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if(ChkJln.isSelected()==true){
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                }else if(ChkJln.isSelected()==false){
                    nilai_jam =cmbJam.getSelectedIndex();
                    nilai_menit =cmbMnt.getSelectedIndex();
                    nilai_detik =cmbDtk.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                cmbJam.setSelectedItem(jam);
                cmbMnt.setSelectedItem(menit);
                cmbDtk.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }
    
    public void setNoRm(String norwt,Date tgl1,Date tgl2,String kodedokter, String namadokter) {
        TNoRw.setText(norwt);
        DTPCari1.setDate(tgl1);
        DTPCari2.setDate(tgl2);
        isRawat();
        isPsien();  
        ChkInput.setSelected(true);
        isForm(); 
        ChkInput1.setSelected(true);
        isForm2(); 
        ChkInput2.setSelected(true);
        isForm3(); 
        ChkInput3.setSelected(true);
        isForm4();
        KdDok.setText(kodedokter);
        KdDok2.setText(kodedokter);
        KdDok3.setText(kodedokter);
        TDokter.setText(namadokter);
        TDokter2.setText(namadokter); 
        TDokter3.setText(namadokter); 
    }
    
    public void SetPoli(String KodePoli){
        this.kode_poli=KodePoli;
    }
    
    public void SetPj(String KodePj){
        this.kd_pj=KodePj;
    }
    
    private void isForm2(){
        if(ChkInput1.isSelected()==true){
            ChkInput1.setVisible(false);
            PanelInput1.setPreferredSize(new Dimension(WIDTH,156));
            panelGlass13.setVisible(true);      
            ChkInput1.setVisible(true);
        }else if(ChkInput1.isSelected()==false){           
            ChkInput1.setVisible(false);            
            PanelInput1.setPreferredSize(new Dimension(WIDTH,20));
            panelGlass13.setVisible(false);      
            ChkInput1.setVisible(true);
        }
    }
    
    private void tampilPemeriksaanGinekologi() {
        Valid.tabelKosong(tabModeGinekologi);
        try{
            ps6=koneksi.prepareStatement("select pemeriksaan_ginekologi_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                    "pemeriksaan_ginekologi_ralan.tgl_perawatan,pemeriksaan_ginekologi_ralan.jam_rawat,pemeriksaan_ginekologi_ralan.inspeksi,pemeriksaan_ginekologi_ralan.inspeksi_vulva,pemeriksaan_ginekologi_ralan.inspekulo_gine, " +
                    "pemeriksaan_ginekologi_ralan.fluxus_gine,pemeriksaan_ginekologi_ralan.fluor_gine,pemeriksaan_ginekologi_ralan.vulva_inspekulo, " +
                    "pemeriksaan_ginekologi_ralan.portio_inspekulo,pemeriksaan_ginekologi_ralan.sondage,pemeriksaan_ginekologi_ralan.portio_dalam,pemeriksaan_ginekologi_ralan.bentuk, " +
                    "pemeriksaan_ginekologi_ralan.cavum_uteri,pemeriksaan_ginekologi_ralan.mobilitas,pemeriksaan_ginekologi_ralan.ukuran, pemeriksaan_ginekologi_ralan.nyeri_tekan, pemeriksaan_ginekologi_ralan.adnexa_kanan, pemeriksaan_ginekologi_ralan.adnexa_kiri," +
                    "pemeriksaan_ginekologi_ralan.cavum_douglas " +
                    "from pasien inner join reg_periksa inner join pemeriksaan_ginekologi_ralan "+
                    "on pemeriksaan_ginekologi_ralan.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis where  "+
                    "pemeriksaan_ginekologi_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_ginekologi_ralan.no_rawat like ? or "+
                    "pemeriksaan_ginekologi_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and reg_periksa.no_rkm_medis like ? or "+
                    "pemeriksaan_ginekologi_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pasien.nm_pasien like ? or  "+
                    "pemeriksaan_ginekologi_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_ginekologi_ralan.inspeksi like ? or "+
                    "pemeriksaan_ginekologi_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_ginekologi_ralan.inspeksi_vulva like ? or "+
                    "pemeriksaan_ginekologi_ralan.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? and pemeriksaan_ginekologi_ralan.inspekulo_gine like ? "+
                    "order by pemeriksaan_ginekologi_ralan.no_rawat desc");
            try {
                ps6.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps6.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps6.setString(3,"%"+TCariPasien.getText()+"%");
                ps6.setString(4,"%"+TCari.getText().trim()+"%");
                ps6.setString(5,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps6.setString(6,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps6.setString(7,"%"+TCariPasien.getText()+"%");
                ps6.setString(8,"%"+TCari.getText().trim()+"%");
                ps6.setString(9,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps6.setString(10,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps6.setString(11,"%"+TCariPasien.getText()+"%");
                ps6.setString(12,"%"+TCari.getText().trim()+"%");
                ps6.setString(13,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps6.setString(14,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps6.setString(15,"%"+TCariPasien.getText()+"%");
                ps6.setString(16,"%"+TCari.getText().trim()+"%");
                ps6.setString(17,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps6.setString(18,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps6.setString(19,"%"+TCariPasien.getText()+"%");
                ps6.setString(20,"%"+TCari.getText().trim()+"%");
                ps6.setString(21,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps6.setString(22,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps6.setString(23,"%"+TCariPasien.getText()+"%");
                ps6.setString(24,"%"+TCari.getText().trim()+"%");
                
                rs=ps6.executeQuery();
                while(rs.next()) {
                    tabModeGinekologi.addRow(new Object[] {
                        false, rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("inspeksi"),
                        rs.getString("inspeksi_vulva"),rs.getString("inspekulo_gine"),rs.getString("fluxus_gine"),
                        rs.getString("fluor_gine"),rs.getString("vulva_inspekulo"),rs.getString("portio_inspekulo"),
                        rs.getString("sondage"),rs.getString("portio_dalam"),rs.getString("bentuk"),
                        rs.getString("cavum_uteri"),rs.getString("mobilitas"),rs.getString("ukuran"),
                        rs.getString("nyeri_tekan"),rs.getString("adnexa_kanan"),rs.getString("adnexa_kiri"),
                        rs.getString("cavum_douglas")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);    
            } finally {
                if(rs!=null) {
                    rs.close();
                }
                if(ps5!=null) {
                    ps5.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi :" +e);
        }
        LCount.setText(""+tabModeGinekologi.getRowCount());
    }
    
    private void getDataPemeriksaanGinekologi() {
         if(tbPemeriksaanGinekologi.getSelectedRow()!= -1) {
            TNoRw.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),1).toString());
            TNoRM.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),2).toString());
            TPasien.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),3).toString());
            Valid.SetTgl(DTPTgl,tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),4).toString());
            cmbJam.setSelectedItem(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),5).toString().substring(0,2));
            cmbMnt.setSelectedItem(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),5).toString().substring(3,5));
            cmbDtk.setSelectedItem(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),5).toString().substring(6,8));
            TInspeksi.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),6).toString());
            TInspeksiVulva.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),7).toString());
            TInspekuloGine.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),8).toString());
            cmbFluxusGine.setSelectedItem(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),9).toString());
            cmbFluorGine.setSelectedItem(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),10).toString());
            TVulvaInspekulo.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),11).toString());
            TPortioInspekulo.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),12).toString());
            TSondage.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),13).toString());
            TPortioDalam.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),14).toString());
            TBentuk.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),15).toString());
            TCavumUteri.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),16).toString());
            cmbMobilitas.setSelectedItem(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),17).toString());
            TUkuran.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),18).toString());
            cmbNyeriTekan.setSelectedItem(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),19).toString());
            TAdnexaKanan.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),20).toString());
            TAdnexaKiri.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),21).toString());
            TCavumDouglas.setText(tbPemeriksaanGinekologi.getValueAt(tbPemeriksaanGinekologi.getSelectedRow(),22).toString());
        }
    }
    
    private void isForm3(){
        if(ChkInput2.isSelected()==true){
            ChkInput2.setVisible(false);
            PanelInput2.setPreferredSize(new Dimension(WIDTH,246));
            panelGlass14.setVisible(true);      
            ChkInput2.setVisible(true);
        }else if(ChkInput2.isSelected()==false){           
            ChkInput2.setVisible(false);            
            PanelInput2.setPreferredSize(new Dimension(WIDTH,20));
            panelGlass14.setVisible(false);      
            ChkInput2.setVisible(true);
        }
    }
    
    private void tampilTindakan() {
        try{     
            jml=0;
            for(i=0;i<TabModeTindakan.getRowCount();i++){
                if(TabModeTindakan.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            nama=null;
            nama=new String[jml];
            kategori=null;
            kategori=new String[jml];
            totaltnd=null;
            totaltnd=new double[jml];  
            bagianrs=null;
            bagianrs=new double[jml];
            bhp=null;
            bhp=new double[jml];
            jmdokter=null;
            jmdokter=new double[jml];
            jmperawat=null;
            jmperawat=new double[jml];
            kso=null;
            kso=new double[jml];
            menejemen=null;
            menejemen=new double[jml];

            index=0;        
            for(i=0;i<TabModeTindakan.getRowCount();i++){
                if(TabModeTindakan.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=TabModeTindakan.getValueAt(i,1).toString();
                    nama[index]=TabModeTindakan.getValueAt(i,2).toString();
                    kategori[index]=TabModeTindakan.getValueAt(i,3).toString();
                    totaltnd[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,4).toString());
                    bagianrs[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,5).toString());
                    bhp[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,6).toString());
                    jmdokter[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,7).toString());
                    jmperawat[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,8).toString());  
                    kso[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,9).toString());
                    menejemen[index]=Double.parseDouble(TabModeTindakan.getValueAt(i,10).toString());  
                    index++;
                }
            }       

            Valid.tabelKosong(TabModeTindakan);

            for(i=0;i<jml;i++){
                TabModeTindakan.addRow(new Object[] {
                    pilih[i],kode[i],nama[i],kategori[i],totaltnd[i],bagianrs[i],bhp[i],jmdokter[i],jmperawat[i],kso[i],menejemen[i]
                });
            }
            
            if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");        
            }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");     
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdr>0 and jns_perawatan.status='1' and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }
            
            try {
                if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,kode_poli.trim());
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(4,kd_pj.trim());
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(7,kd_pj.trim());
                    pstindakan.setString(8,kode_poli.trim());
                    pstindakan.setString(9,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kd_pj.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kd_pj.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,kode_poli.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kode_poli.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }
                
                while(rstindakan.next()){
                    TabModeTindakan.addRow(new Object[] {
                        false,rstindakan.getString(1),rstindakan.getString(2),rstindakan.getString(3),
                        rstindakan.getDouble("total_byrdr"),rstindakan.getDouble("material"),
                        rstindakan.getDouble("bhp"),rstindakan.getDouble("tarif_tindakandr"),
                        rstindakan.getDouble("tarif_tindakanpr"),rstindakan.getDouble("kso"),
                        rstindakan.getDouble("menejemen")
                    });    
                }                   
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rstindakan != null){
                    rstindakan.close();
                }
                if(pstindakan != null){
                    pstindakan.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+TabModeTindakan.getRowCount());
    }
    
    private void tampilTindakan2() {
        try{     
            jml=0;
            for(i=0;i<TabModeTindakan2.getRowCount();i++){
                if(TabModeTindakan2.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            nama=null;
            nama=new String[jml];
            kategori=null;
            kategori=new String[jml];
            totaltnd=null;
            totaltnd=new double[jml];  
            bagianrs=null;
            bagianrs=new double[jml];
            bhp=null;
            bhp=new double[jml];
            jmdokter=null;
            jmdokter=new double[jml];
            jmperawat=null;
            jmperawat=new double[jml];
            kso=null;
            kso=new double[jml];
            menejemen=null;
            menejemen=new double[jml];

            index=0;        
            for(i=0;i<TabModeTindakan2.getRowCount();i++){
                if(TabModeTindakan2.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=TabModeTindakan2.getValueAt(i,1).toString();
                    nama[index]=TabModeTindakan2.getValueAt(i,2).toString();
                    kategori[index]=TabModeTindakan2.getValueAt(i,3).toString();
                    totaltnd[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,4).toString());
                    bagianrs[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,5).toString());
                    bhp[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,6).toString());
                    jmdokter[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,7).toString());
                    jmperawat[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,8).toString());  
                    kso[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,9).toString());
                    menejemen[index]=Double.parseDouble(TabModeTindakan2.getValueAt(i,10).toString());  
                    index++;
                }
            }       

            Valid.tabelKosong(TabModeTindakan2);

            for(i=0;i<jml;i++){
                TabModeTindakan2.addRow(new Object[] {
                    pilih[i],kode[i],nama[i],kategori[i],totaltnd[i],bagianrs[i],bhp[i],jmdokter[i],jmperawat[i],kso[i],menejemen[i]
                });
            }
            
            if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");        
            }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");     
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrpr>0 and jns_perawatan.status='1' and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }
            
            try {
                if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,kode_poli.trim());
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(4,kd_pj.trim());
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(7,kd_pj.trim());
                    pstindakan.setString(8,kode_poli.trim());
                    pstindakan.setString(9,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kd_pj.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kd_pj.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,kode_poli.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kode_poli.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }
                
                while(rstindakan.next()){
                    TabModeTindakan2.addRow(new Object[] {
                        false,rstindakan.getString(1),rstindakan.getString(2),rstindakan.getString(3),
                        rstindakan.getDouble("total_byrpr"),rstindakan.getDouble("material"),
                        rstindakan.getDouble("bhp"),rstindakan.getDouble("tarif_tindakandr"),
                        rstindakan.getDouble("tarif_tindakanpr"),rstindakan.getDouble("kso"),
                        rstindakan.getDouble("menejemen")
                    });        
                }                      
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rstindakan != null){
                    rstindakan.close();
                }
                if(pstindakan != null){
                    pstindakan.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+TabModeTindakan2.getRowCount());
    }
    
    private void tampilTindakan3() {
        try{     
            jml=0;
            for(i=0;i<TabModeTindakan3.getRowCount();i++){
                if(TabModeTindakan3.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            nama=null;
            nama=new String[jml];
            kategori=null;
            kategori=new String[jml];
            totaltnd=null;
            totaltnd=new double[jml];  
            bagianrs=null;
            bagianrs=new double[jml];
            bhp=null;
            bhp=new double[jml];
            jmdokter=null;
            jmdokter=new double[jml];
            jmperawat=null;
            jmperawat=new double[jml];
            kso=null;
            kso=new double[jml];
            menejemen=null;
            menejemen=new double[jml];

            index=0;        
            for(i=0;i<TabModeTindakan3.getRowCount();i++){
                if(TabModeTindakan3.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=TabModeTindakan3.getValueAt(i,1).toString();
                    nama[index]=TabModeTindakan3.getValueAt(i,2).toString();
                    kategori[index]=TabModeTindakan3.getValueAt(i,3).toString();
                    totaltnd[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,4).toString());
                    bagianrs[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,5).toString());
                    bhp[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,6).toString());
                    jmdokter[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,7).toString());
                    jmperawat[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,8).toString());  
                    kso[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,9).toString());
                    menejemen[index]=Double.parseDouble(TabModeTindakan3.getValueAt(i,10).toString());  
                    index++;
                }
            }       

            Valid.tabelKosong(TabModeTindakan3);

            for(i=0;i<jml;i++){
                TabModeTindakan3.addRow(new Object[] {
                    pilih[i],kode[i],nama[i],kategori[i],totaltnd[i],bagianrs[i],bhp[i],jmdokter[i],jmperawat[i],kso[i],menejemen[i]
                });
            }
            
            if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_pj=? or jns_perawatan.kd_pj='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");        
            }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and (jns_perawatan.kd_poli=? or jns_perawatan.kd_poli='-') and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan ");     
            }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                pstindakan=koneksi.prepareStatement("select jns_perawatan.kd_jenis_prw,jns_perawatan.nm_perawatan,kategori_perawatan.nm_kategori,"+
                   "jns_perawatan.total_byrdr,jns_perawatan.total_byrpr,jns_perawatan.total_byrdrpr,jns_perawatan.bhp,jns_perawatan.material,"+
                   "jns_perawatan.tarif_tindakandr,jns_perawatan.tarif_tindakanpr,jns_perawatan.kso,jns_perawatan.menejemen from jns_perawatan inner join kategori_perawatan "+
                   "on jns_perawatan.kd_kategori=kategori_perawatan.kd_kategori  "+
                   "where jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and jns_perawatan.kd_jenis_prw like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and jns_perawatan.nm_perawatan like ? or "+
                    "jns_perawatan.total_byrdrpr>0 and jns_perawatan.status='1' and kategori_perawatan.nm_kategori like ? order by jns_perawatan.nm_perawatan "); 
            }
            
            try {
                if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,kode_poli.trim());
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(4,kd_pj.trim());
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(7,kd_pj.trim());
                    pstindakan.setString(8,kode_poli.trim());
                    pstindakan.setString(9,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kd_pj.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kd_pj.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,kode_poli.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kode_poli.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }
                
                while(rstindakan.next()){
                    TabModeTindakan3.addRow(new Object[] {
                        false,rstindakan.getString(1),rstindakan.getString(2),rstindakan.getString(3),
                        rstindakan.getDouble("total_byrdrpr"),rstindakan.getDouble("material"),
                        rstindakan.getDouble("bhp"),rstindakan.getDouble("tarif_tindakandr"),
                        rstindakan.getDouble("tarif_tindakanpr"),rstindakan.getDouble("kso"),
                        rstindakan.getDouble("menejemen")
                    });    
                }   
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rstindakan != null){
                    rstindakan.close();
                }
                if(pstindakan != null){
                    pstindakan.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+TabModeTindakan3.getRowCount());
    }
    
    private void TampilkanData(){
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                tampilkanPenangananDokter();
                break;
            case 1:
                tampilkanPenangananPetugas();
                break;
            case 2:
                tampilkanPenangananDokterPetugas();
                break;
            case 3:
                tampilPemeriksaan();
                break;
            case 4:
                tampilPemeriksaanObstetri();
                break;
            case 5:
                tampilPemeriksaanGinekologi();
                break;
            case 6:
                if(akses.getresume_pasien()==true){
                    panelResume1.setRM(TNoRM.getText(),Valid.SetTgl(DTPCari1.getSelectedItem()+""), Valid.SetTgl(DTPCari2.getSelectedItem()+""),false);
                    panelResume1.pilihTab();
                }  
                LCount.setText("0");
                break;
            case 7:
                if(akses.getdiagnosa_pasien()==true){
                    panelDiagnosa1.setRM(TNoRw.getText(),TNoRM.getText(),Valid.SetTgl(DTPCari1.getSelectedItem()+""), Valid.SetTgl(DTPCari2.getSelectedItem()+""),"Ralan",TCari.getText().trim());
                    panelDiagnosa1.pilihTab();
                    LCount.setText(panelDiagnosa1.getRecord()+"");
                }  
                break;
            case 8:
                if(akses.getcatatan_perawatan()==true){
                    tampilCatatan();
                }  
                break;
            default:
                break;
        }
    }

    private void tampilkanPenangananDokter() {
        if(TabRawatTindakanDokter.getSelectedIndex()==0){
            tampilTindakan();
        }else if(TabRawatTindakanDokter.getSelectedIndex()==1){
            tampilDr();
        }
    }
    
    private void SimpanPenangananDokter(){        
        try {
            koneksi.setAutoCommit(false);
            for(i=0;i<tbTindakan.getRowCount();i++){ 
                if(tbTindakan.getValueAt(i,0).toString().equals("true")){  
                    if(Sequel.menyimpantf("rawat_jl_dr","?,?,?,?,?,?,?,?,?,?,?,'Belum'","Tindakan",11,new String[]{
                        TNoRw.getText(),tbTindakan.getValueAt(i,1).toString(),KdDok.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),
                        cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),tbTindakan.getValueAt(i,5).toString(),
                        tbTindakan.getValueAt(i,6).toString(),tbTindakan.getValueAt(i,7).toString(),tbTindakan.getValueAt(i,9).toString(),
                        tbTindakan.getValueAt(i,10).toString(),tbTindakan.getValueAt(i,4).toString()
                    })==true){
                        tbTindakan.setValueAt(false,i,0);
                    }
                }                           
            }
            koneksi.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }
    
    private void SimpanPenangananPetugas(){
        try {
            koneksi.setAutoCommit(false);
            for(i=0;i<tbTindakan2.getRowCount();i++){ 
                if(tbTindakan2.getValueAt(i,0).toString().equals("true")){  
                    if(Sequel.menyimpantf("rawat_jl_pr","?,?,?,?,?,?,?,?,?,?,?,'Belum'","Tindakan",11,new String[]{
                        TNoRw.getText(),tbTindakan2.getValueAt(i,1).toString(),kdptg.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),
                        cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),tbTindakan2.getValueAt(i,5).toString(),
                        tbTindakan2.getValueAt(i,6).toString(),tbTindakan2.getValueAt(i,8).toString(),tbTindakan2.getValueAt(i,9).toString(),
                        tbTindakan2.getValueAt(i,10).toString(),tbTindakan2.getValueAt(i,4).toString()
                    })==true){
                        tbTindakan2.setValueAt(false,i,0);
                    }
                }                           
            }
            koneksi.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }
    
    private void SimpanPenangananDokterPetugas(){        
        try {
            koneksi.setAutoCommit(false);
            for(i=0;i<tbTindakan3.getRowCount();i++){ 
                if(tbTindakan3.getValueAt(i,0).toString().equals("true")){  
                    if(Sequel.menyimpantf("rawat_jl_drpr","?,?,?,?,?,?,?,?,?,?,?,?,?,'Belum'","Tindakan",13,new String[]{
                        TNoRw.getText(),tbTindakan3.getValueAt(i,1).toString(),KdDok2.getText(),kdptg2.getText(),
                        Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                        tbTindakan3.getValueAt(i,5).toString(),tbTindakan3.getValueAt(i,6).toString(),tbTindakan3.getValueAt(i,7).toString(),
                        tbTindakan3.getValueAt(i,8).toString(),tbTindakan3.getValueAt(i,9).toString(),tbTindakan3.getValueAt(i,10).toString(),
                        tbTindakan3.getValueAt(i,4).toString()
                    })==true){
                        tbTindakan3.setValueAt(false,i,0);
                    }
                }                           
            }
            koneksi.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }

    private void tampilkanPenangananPetugas() {
        if(TabRawatTindakanPetugas.getSelectedIndex()==0){
            tampilTindakan2();
        }else if(TabRawatTindakanPetugas.getSelectedIndex()==1){
            tampilPr();
        }
    }

    private void tampilkanPenangananDokterPetugas() {
        if(TabRawatTindakanDokterPetugas.getSelectedIndex()==0){
            tampilTindakan3();
        }else if(TabRawatTindakanDokterPetugas.getSelectedIndex()==1){
            tampilDrPr();
        }
    }

    private void inputObat() {
        DlgCariObat dlgobt=new DlgCariObat(null,false);
        dlgobt.setNoRm(TNoRw.getText(),TNoRM.getText(),TPasien.getText(),Valid.SetTgl(DTPTgl.getSelectedItem()+""),cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem());
        dlgobt.isCek();
        dlgobt.setDokter(KdDok.getText(),TDokter.getText());
        dlgobt.tampilobat();
        dlgobt.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
        dlgobt.setLocationRelativeTo(internalFrame1);
        dlgobt.setVisible(true);
    }

    private void inputResep() {
        DlgPeresepanDokter resep=new DlgPeresepanDokter(null,false);
        resep.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
        resep.setLocationRelativeTo(internalFrame1);
        resep.setNoRm(TNoRw.getText(),DTPTgl.getDate(),cmbJam.getSelectedItem().toString(),cmbMnt.getSelectedItem().toString(),
                cmbDtk.getSelectedItem().toString(),KdDok.getText(),TDokter.getText(),"ralan");
        resep.isCek();
        resep.tampilobat();
        resep.setVisible(true);
    }

    private void inputKamar() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        akses.setstatus(true);
        DlgKamarInap dlgki=new DlgKamarInap(null,false);
        dlgki.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
        dlgki.setLocationRelativeTo(internalFrame1);
        dlgki.emptTeks();
        dlgki.isCek();
        dlgki.setNoRm(TNoRw.getText(),TNoRM.getText(),TPasien.getText());  
        dlgki.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }

}
