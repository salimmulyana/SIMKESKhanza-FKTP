package custom;
import keuangan.*;
import permintaan.*;
import fungsi.BackgroundMusic;
import fungsi.Konversi;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
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
import kepegawaian.DlgCariDokter;
import simrskhanza.DlgCariBangsal;
import simrskhanza.DlgCariPoli;
import simrskhanza.DlgPeriksaRadiologi;
import custom.DlgDepartemen;
import java.sql.SQLException;
import simrskhanza.DlgPenanggungJawab;

public class DlgJasmedDokter extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabMode3,tabMode4;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();   
    private DlgDepartemen dept=new DlgDepartemen(null,false);
    private Jurnal jur=new Jurnal();
    private int jml=0,i,nilai_detik,permintaanbaru=0;
    private double stokbarang=0,ttl=0,y=0,ttl2=0;
    private final DecimalFormat df5 = new DecimalFormat("###,###,###,###,###,###,###.##");  
    private PreparedStatement ps,ps2,pssimpanbayar;
    private final Properties prop = new Properties();
    private BackgroundMusic music;
    private Konversi konversiAngka=new Konversi();
    private String input="";
    private int satuan=0; 
    private ResultSet rs,rs2;
    private Date now;
    private boolean aktif=false;
    private String userpetugas="",akunbayar="",nmakunbayar="",nmakunpiutang="",alarm="",formalarm="",nol_detik,detik,tglsampel="",tglhasil="",norm="",kamar="",namakamar="",diagnosa="";
    private double DokterUmum=0,DokterUmumDr=0,DokterUmumDrPr=0,DokterSpesialis=0,DokterSpesialisDr=0,DokterSpesialisDrPr=0,
                   Obat=0,Lab=0,Lab1=0,Lab2=0,Rad=0,ADM=0,ADMdr=0,ADMpr=0,ADMdrpr=0,Lain=0,Tagihan=0,UangMuka=0;
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private DlgPenanggungJawab carabayar=new DlgPenanggungJawab(null,false);
    private int a=0;
    private double jm=0,totaljm=0;
    private double jmralan=0,jmranap=0,
            jmoperasi1=0,jmoperasi2=0,jmoperasi3=0,jmoperasi_dranak=0,jmoperasi_drpjanak=0,jmoperasi_dranastesi=0,jmoperasi_drumum=0;
    private double jmralandr=0,jmralandrpr=0,ttlralan=0,
                   jmranapdr=0,jmranapdrpr=0,ttlranap=0,
                   jmlab=0,jmdetaillab=0,ttllab=0,
                   jmrad=0,ttlrad=0,ttloperasi=0;
    private double ttlralan2=0,ttlranap2=0,ttllab2=0,ttlrad2=0,ttloperasi2=0;
    private PreparedStatement psralandokter,psralandokterdrpr,psranapdokter,psranapdokterdrpr,psbiayaoperator1,psbiayaoperator2,psbiayaoperator3,psbiayadokter_anak,
            psbiayadokter_anestesi,psperiksa_lab,psdetaillab,psperiksa_lab2,psdetaillab2,psperiksa_radiologi,psperiksa_radiologi2,psbiaya_dokter_pjanak,
            psbiaya_dokter_umum;
    private ResultSet rsralandokter,rsralandokterdrpr,rsranapdokter,rsranapdokterdrpr,rsbiayaoperator1,rsbiayaoperator2,rsbiayaoperator3,rsbiayadokter_anak,
            rsbiayadokter_anestesi,rsperiksa_lab,rsdetaillab,rsperiksa_radiologi,rsbiaya_dokter_pjanak,rsbiaya_dokter_umum;
    public DlgGroupPenjab ktg=new DlgGroupPenjab(null,false);
    /** Creates new form DlgProgramStudi
     * @param parent
     * @param modal */
    public DlgJasmedDokter(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        //tabMode=new DefaultTableModel(null,new Object[]{
            //"Kd.Dokter","Nama Dokter","JM.Ralan","JM.Ranap","JM.Lab","JM.Rad","JM.Operasi","Total JM"
            
            //}){
              //@Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        //};
        Object[] rowRwJlDr={
            "P","Kd.Dokter","Nama Dokter","J.M.Ralan","J.M.Ranap","J.M.Lab","J.M.Rad","J.M.Operasi","Total J.M"
        };
        tabMode=new DefaultTableModel(null,rowRwJlDr){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if ((colIndex==10)||(colIndex==0)) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class,java.lang.Object.class,java.lang.Object.class,
                java.lang.Double.class,java.lang.Double.class,java.lang.Double.class,
                java.lang.Double.class,java.lang.Double.class,java.lang.Double.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRadiologiRalan.setModel(tabMode);

        tbRadiologiRalan.setPreferredScrollableViewportSize(new Dimension(800,800));
        tbRadiologiRalan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 9; i++) {
            TableColumn column = tbRadiologiRalan.getColumnModel().getColumn(i);
            if(i==0){
                //column.setPreferredWidth(22);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==1){
                column.setPreferredWidth(90);
            }else if(i==2){
                column.setPreferredWidth(200);
            }else if(i==3){
                column.setPreferredWidth(100);
            }else if(i==4){
                column.setPreferredWidth(100);           
            }else if(i==5){
                column.setPreferredWidth(100);
            }else if(i==6){
                column.setPreferredWidth(100);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setPreferredWidth(100);
            }
        }
        tbRadiologiRalan.setDefaultRenderer(Object.class, new WarnaTable());
        
        
        Object[] rowRwJlDr3={
            "P","No.Bukti","Tanggal","Tgl.1","Tgl.2","J.M.Ralan","J.M.Ranap",
            "J.M.Lab","J.M.Rad","J.M.Operasi","Total J.M","Petugas","Rek.Bayar","Rek.Hutang"
        };
        tabMode3=new DefaultTableModel(null,rowRwJlDr3){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if ((colIndex==10)||(colIndex==0)) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class,java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Double.class,
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                java.lang.Double.class, java.lang.Object.class,java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRadiologiRalan3.setModel(tabMode3);

        tbRadiologiRalan3.setPreferredScrollableViewportSize(new Dimension(800,800));
        tbRadiologiRalan3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 14; i++) {
            TableColumn column = tbRadiologiRalan3.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(22);
                //column.setMinWidth(0);
                //column.setMaxWidth(0);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(100);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(70);
            }else if(i==5){
                column.setPreferredWidth(100);
            }else if(i==6){
                column.setPreferredWidth(100);
            }else if(i==7){
                column.setPreferredWidth(100);           
            }else if(i==8){
                column.setPreferredWidth(100);
            }else if(i==9){
                column.setPreferredWidth(100);
            }else if(i==10){
                column.setPreferredWidth(100);
            }else if(i==11){
                column.setPreferredWidth(200);
            }else if(i==12){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==13){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbRadiologiRalan3.setDefaultRenderer(Object.class, new WarnaTable());
        
        Object[] rowRwJlDr4={
            "P","No.Bukti","Tanggal","Kd.Dokter","Nama Dokter","Tgl.1","Tgl.2","J.M.Ralan","J.M.Ranap",
            "J.M.Lab","J.M.Rad","J.M.Operasi","Total J.M","Petugas"
        };
        tabMode4=new DefaultTableModel(null,rowRwJlDr4){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if ((colIndex==10)||(colIndex==0)) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class,java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class,
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                java.lang.Double.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRadiologiRalan4.setModel(tabMode4);

        tbRadiologiRalan4.setPreferredScrollableViewportSize(new Dimension(800,800));
        tbRadiologiRalan4.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 14; i++) {
            TableColumn column = tbRadiologiRalan4.getColumnModel().getColumn(i);
            if(i==0){
                //column.setPreferredWidth(22);
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(100);
            }else if(i==3){
                column.setPreferredWidth(80);
            }else if(i==4){
                column.setPreferredWidth(200);           
            }else if(i==5){
                column.setPreferredWidth(70);
            }else if(i==6){
                column.setPreferredWidth(70);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setPreferredWidth(100);
            }else if(i==9){
                column.setPreferredWidth(100);           
            }else if(i==10){
                column.setPreferredWidth(100);
            }else if(i==11){
                column.setPreferredWidth(100);
            }else if(i==12){
                column.setPreferredWidth(100);
            }else if(i==13){
                column.setPreferredWidth(200);
            }
        }
        tbRadiologiRalan4.setDefaultRenderer(Object.class, new WarnaTable());
        
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        prosesCari();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        prosesCari();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        prosesCari();
                    }
                }
            });
        }
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){
                    kddokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    nmdokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                } 
                //prosesCari();
                kddokter.requestFocus();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {dokter.emptTeks();}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        carabayar.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(carabayar.getTable().getSelectedRow()!= -1){
                    KdCaraBayar.setText(carabayar.getTable().getValueAt(carabayar.getTable().getSelectedRow(),1).toString());
                    NmCaraBayar.setText(carabayar.getTable().getValueAt(carabayar.getTable().getSelectedRow(),2).toString());
                } 
                //prosesCari();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {carabayar.onCari();}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });   
        
        carabayar.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    carabayar.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        }); 
        
        isForm();
        Valid.LoadTahun(TahunCariBayar);
        Valid.loadCombo(nama_bayar,"nama_bayar","akun_bayar");
                           
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnHapusJurnal = new javax.swing.JMenuItem();
        KdCaraBayar = new widget.TextBox();
        kddokter = new widget.TextBox();
        KdGroup = new widget.TextBox();
        label35 = new widget.Label();
        NmGroup = new widget.TextBox();
        btnAlat = new widget.Button();
        jumlahralan = new widget.TextBox();
        jumlahranap = new widget.TextBox();
        jumlahlab = new widget.TextBox();
        jumlahrad = new widget.TextBox();
        jumlahoperasi = new widget.TextBox();
        rekbayar = new widget.TextBox();
        rekhutang = new widget.TextBox();
        nobukti = new widget.TextBox();
        RekBayarNm = new widget.TextBox();
        RekPiutangNm = new widget.TextBox();
        RekPiutangNm2 = new widget.TextBox();
        RekPiutangNm1 = new widget.TextBox();
        BtnPrint = new widget.Button();
        internalFrame1 = new widget.InternalFrame();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame2 = new widget.InternalFrame();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        FormInput = new widget.panelisi();
        label12 = new widget.Label();
        Tgl3 = new widget.Tanggal();
        label19 = new widget.Label();
        Tgl4 = new widget.Tanggal();
        label39 = new widget.Label();
        no_bukti = new widget.TextBox();
        label32 = new widget.Label();
        tgl_bayar = new widget.Tanggal();
        label37 = new widget.Label();
        keterangan = new widget.TextBox();
        scrollPane1 = new widget.ScrollPane();
        tbRadiologiRalan = new widget.Table();
        jPanel2 = new javax.swing.JPanel();
        panelGlass14 = new widget.panelisi();
        label27 = new widget.Label();
        JmlKurang2 = new widget.TextBox();
        jLabel12 = new widget.Label();
        nama_bayar = new widget.ComboBox();
        BtnBayar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        label17 = new widget.Label();
        nmdokter = new widget.TextBox();
        btnDokter = new widget.Button();
        label20 = new widget.Label();
        NmCaraBayar = new widget.TextBox();
        BtnCaraBayarRalanDokter = new widget.Button();
        BtnCari1 = new widget.Button();
        BtnAll1 = new widget.Button();
        label21 = new widget.Label();
        BtnKeluar1 = new widget.Button();
        internalFrame3 = new widget.InternalFrame();
        jPanel4 = new javax.swing.JPanel();
        panelGlass11 = new widget.panelisi();
        label34 = new widget.Label();
        BulanCariBayar = new widget.Tanggal();
        label36 = new widget.Label();
        TahunCariBayar = new widget.Tanggal();
        label24 = new widget.Label();
        JmlKurang = new widget.TextBox();
        panelGlass12 = new widget.panelisi();
        label10 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        jLabel10 = new widget.Label();
        LCount = new widget.Label();
        BtnHapus = new widget.Button();
        BtnKeluar = new widget.Button();
        internalFrame5 = new widget.InternalFrame();
        TabRawatLaborat = new javax.swing.JTabbedPane();
        scrollPane3 = new widget.ScrollPane();
        tbRadiologiRalan3 = new widget.Table();
        scrollPane4 = new widget.ScrollPane();
        tbRadiologiRalan4 = new widget.Table();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnHapusJurnal.setBackground(new java.awt.Color(255, 255, 254));
        MnHapusJurnal.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnHapusJurnal.setForeground(new java.awt.Color(50, 50, 50));
        MnHapusJurnal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnHapusJurnal.setText("Hapus Jurnal");
        MnHapusJurnal.setName("MnHapusJurnal"); // NOI18N
        MnHapusJurnal.setPreferredSize(new java.awt.Dimension(200, 28));
        MnHapusJurnal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnHapusJurnalActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnHapusJurnal);

        KdCaraBayar.setEditable(false);
        KdCaraBayar.setName("KdCaraBayar"); // NOI18N
        KdCaraBayar.setPreferredSize(new java.awt.Dimension(50, 23));

        kddokter.setName("kddokter"); // NOI18N
        kddokter.setPreferredSize(new java.awt.Dimension(70, 23));

        KdGroup.setToolTipText("Tekan ENTER untuk lanjut ke field berikutnya, tekan PAGE UP untuk ke field sebelumnya, Tekan UP untuk menampilkan data Kategori Perawatan");
        KdGroup.setHighlighter(null);
        KdGroup.setName("KdGroup"); // NOI18N
        KdGroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdGroupKeyPressed(evt);
            }
        });

        label35.setText("Group Cara Bayar :");
        label35.setName("label35"); // NOI18N
        label35.setPreferredSize(new java.awt.Dimension(120, 23));

        NmGroup.setEditable(false);
        NmGroup.setHighlighter(null);
        NmGroup.setName("NmGroup"); // NOI18N
        NmGroup.setPreferredSize(new java.awt.Dimension(150, 24));
        NmGroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NmGroupKeyPressed(evt);
            }
        });

        btnAlat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAlat.setMnemonic('1');
        btnAlat.setToolTipText("Alt+1");
        btnAlat.setName("btnAlat"); // NOI18N
        btnAlat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlatActionPerformed(evt);
            }
        });
        btnAlat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAlatKeyPressed(evt);
            }
        });

        jumlahralan.setEditable(false);
        jumlahralan.setHighlighter(null);
        jumlahralan.setName("jumlahralan"); // NOI18N
        jumlahralan.setPreferredSize(new java.awt.Dimension(150, 24));
        jumlahralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jumlahralanKeyPressed(evt);
            }
        });

        jumlahranap.setEditable(false);
        jumlahranap.setHighlighter(null);
        jumlahranap.setName("jumlahranap"); // NOI18N
        jumlahranap.setPreferredSize(new java.awt.Dimension(150, 24));
        jumlahranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jumlahranapKeyPressed(evt);
            }
        });

        jumlahlab.setEditable(false);
        jumlahlab.setHighlighter(null);
        jumlahlab.setName("jumlahlab"); // NOI18N
        jumlahlab.setPreferredSize(new java.awt.Dimension(150, 24));
        jumlahlab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jumlahlabKeyPressed(evt);
            }
        });

        jumlahrad.setEditable(false);
        jumlahrad.setHighlighter(null);
        jumlahrad.setName("jumlahrad"); // NOI18N
        jumlahrad.setPreferredSize(new java.awt.Dimension(150, 24));
        jumlahrad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jumlahradKeyPressed(evt);
            }
        });

        jumlahoperasi.setEditable(false);
        jumlahoperasi.setHighlighter(null);
        jumlahoperasi.setName("jumlahoperasi"); // NOI18N
        jumlahoperasi.setPreferredSize(new java.awt.Dimension(150, 24));
        jumlahoperasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jumlahoperasiKeyPressed(evt);
            }
        });

        rekbayar.setEditable(false);
        rekbayar.setHighlighter(null);
        rekbayar.setName("rekbayar"); // NOI18N
        rekbayar.setPreferredSize(new java.awt.Dimension(150, 24));
        rekbayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rekbayarKeyPressed(evt);
            }
        });

        rekhutang.setEditable(false);
        rekhutang.setHighlighter(null);
        rekhutang.setName("rekhutang"); // NOI18N
        rekhutang.setPreferredSize(new java.awt.Dimension(150, 24));
        rekhutang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rekhutangKeyPressed(evt);
            }
        });

        nobukti.setEditable(false);
        nobukti.setHighlighter(null);
        nobukti.setName("nobukti"); // NOI18N
        nobukti.setPreferredSize(new java.awt.Dimension(150, 24));
        nobukti.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nobuktiKeyPressed(evt);
            }
        });

        RekBayarNm.setEditable(false);
        RekBayarNm.setHighlighter(null);
        RekBayarNm.setName("RekBayarNm"); // NOI18N
        RekBayarNm.setPreferredSize(new java.awt.Dimension(150, 24));
        RekBayarNm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RekBayarNmKeyPressed(evt);
            }
        });

        RekPiutangNm.setEditable(false);
        RekPiutangNm.setHighlighter(null);
        RekPiutangNm.setName("RekPiutangNm"); // NOI18N
        RekPiutangNm.setPreferredSize(new java.awt.Dimension(150, 24));
        RekPiutangNm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RekPiutangNmKeyPressed(evt);
            }
        });

        RekPiutangNm2.setEditable(false);
        RekPiutangNm2.setHighlighter(null);
        RekPiutangNm2.setName("RekPiutangNm2"); // NOI18N
        RekPiutangNm2.setPreferredSize(new java.awt.Dimension(150, 24));
        RekPiutangNm2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RekPiutangNm2KeyPressed(evt);
            }
        });

        RekPiutangNm1.setEditable(false);
        RekPiutangNm1.setHighlighter(null);
        RekPiutangNm1.setName("RekPiutangNm1"); // NOI18N
        RekPiutangNm1.setPreferredSize(new java.awt.Dimension(150, 24));
        RekPiutangNm1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RekPiutangNm1KeyPressed(evt);
            }
        });

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Jasa Medis Dokter ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout());

        PanelInput.setBackground(new java.awt.Color(255, 255, 255));
        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 126));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('M');
        ChkInput.setText(".: Filter Data");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 44));
        FormInput.setLayout(null);

        label12.setText("Tgl.Nota :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(75, 23));
        FormInput.add(label12);
        label12.setBounds(6, 10, 75, 23);

        Tgl3.setDisplayFormat("dd-MM-yyyy");
        Tgl3.setName("Tgl3"); // NOI18N
        Tgl3.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl3KeyPressed(evt);
            }
        });
        FormInput.add(Tgl3);
        Tgl3.setBounds(86, 10, 90, 23);

        label19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label19.setText("s.d.");
        label19.setName("label19"); // NOI18N
        label19.setPreferredSize(new java.awt.Dimension(30, 23));
        FormInput.add(label19);
        label19.setBounds(181, 10, 30, 23);

        Tgl4.setDisplayFormat("dd-MM-yyyy");
        Tgl4.setName("Tgl4"); // NOI18N
        Tgl4.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl4KeyPressed(evt);
            }
        });
        FormInput.add(Tgl4);
        Tgl4.setBounds(216, 10, 90, 23);

        label39.setText("No.Bukti :");
        label39.setName("label39"); // NOI18N
        label39.setPreferredSize(new java.awt.Dimension(35, 23));
        FormInput.add(label39);
        label39.setBounds(10, 40, 75, 23);

        no_bukti.setHighlighter(null);
        no_bukti.setName("no_bukti"); // NOI18N
        no_bukti.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                no_buktiKeyPressed(evt);
            }
        });
        FormInput.add(no_bukti);
        no_bukti.setBounds(90, 40, 220, 23);

        label32.setText("Tanggal :");
        label32.setName("label32"); // NOI18N
        label32.setPreferredSize(new java.awt.Dimension(35, 23));
        FormInput.add(label32);
        label32.setBounds(320, 40, 70, 23);

        tgl_bayar.setDisplayFormat("dd-MM-yyyy");
        tgl_bayar.setName("tgl_bayar"); // NOI18N
        tgl_bayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tgl_bayarKeyPressed(evt);
            }
        });
        FormInput.add(tgl_bayar);
        tgl_bayar.setBounds(400, 40, 90, 23);

        label37.setText("Keterangan :");
        label37.setName("label37"); // NOI18N
        label37.setPreferredSize(new java.awt.Dimension(35, 23));
        FormInput.add(label37);
        label37.setBounds(0, 70, 85, 23);

        keterangan.setHighlighter(null);
        keterangan.setName("keterangan"); // NOI18N
        keterangan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                keteranganKeyPressed(evt);
            }
        });
        FormInput.add(keterangan);
        keterangan.setBounds(90, 70, 400, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        internalFrame2.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        scrollPane1.setName("scrollPane1"); // NOI18N
        scrollPane1.setOpaque(true);

        tbRadiologiRalan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbRadiologiRalan.setName("tbRadiologiRalan"); // NOI18N
        tbRadiologiRalan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRadiologiRalanMouseClicked(evt);
            }
        });
        tbRadiologiRalan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbRadiologiRalanKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(tbRadiologiRalan);

        internalFrame2.add(scrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 101));
        jPanel2.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass14.setName("panelGlass14"); // NOI18N
        panelGlass14.setPreferredSize(new java.awt.Dimension(44, 51));
        panelGlass14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 9));

        label27.setText("Total :");
        label27.setName("label27"); // NOI18N
        label27.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass14.add(label27);

        JmlKurang2.setEditable(false);
        JmlKurang2.setHighlighter(null);
        JmlKurang2.setName("JmlKurang2"); // NOI18N
        JmlKurang2.setPreferredSize(new java.awt.Dimension(180, 24));
        JmlKurang2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JmlKurang2KeyPressed(evt);
            }
        });
        panelGlass14.add(JmlKurang2);

        jLabel12.setText("Akun Bayar :");
        jLabel12.setName("jLabel12"); // NOI18N
        jLabel12.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass14.add(jLabel12);

        nama_bayar.setName("nama_bayar"); // NOI18N
        nama_bayar.setPreferredSize(new java.awt.Dimension(220, 23));
        nama_bayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nama_bayarKeyPressed(evt);
            }
        });
        panelGlass14.add(nama_bayar);

        BtnBayar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnBayar.setMnemonic('S');
        BtnBayar.setText("Bayar");
        BtnBayar.setToolTipText("Alt+S");
        BtnBayar.setName("BtnBayar"); // NOI18N
        BtnBayar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBayarActionPerformed(evt);
            }
        });
        BtnBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBayarKeyPressed(evt);
            }
        });
        panelGlass14.add(BtnBayar);

        jPanel2.add(panelGlass14, java.awt.BorderLayout.PAGE_START);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 51));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 9));

        label17.setText("Dokter :");
        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(label17);

        nmdokter.setEditable(false);
        nmdokter.setName("nmdokter"); // NOI18N
        nmdokter.setPreferredSize(new java.awt.Dimension(230, 23));
        panelGlass9.add(nmdokter);

        btnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokter.setMnemonic('3');
        btnDokter.setToolTipText("Alt+3");
        btnDokter.setName("btnDokter"); // NOI18N
        btnDokter.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokterActionPerformed(evt);
            }
        });
        btnDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDokterKeyPressed(evt);
            }
        });
        panelGlass9.add(btnDokter);

        label20.setText("Cara Bayar :");
        label20.setName("label20"); // NOI18N
        label20.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass9.add(label20);

        NmCaraBayar.setEditable(false);
        NmCaraBayar.setName("NmCaraBayar"); // NOI18N
        NmCaraBayar.setPreferredSize(new java.awt.Dimension(150, 23));
        panelGlass9.add(NmCaraBayar);

        BtnCaraBayarRalanDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnCaraBayarRalanDokter.setMnemonic('3');
        BtnCaraBayarRalanDokter.setToolTipText("Alt+3");
        BtnCaraBayarRalanDokter.setName("BtnCaraBayarRalanDokter"); // NOI18N
        BtnCaraBayarRalanDokter.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCaraBayarRalanDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCaraBayarRalanDokterActionPerformed(evt);
            }
        });
        panelGlass9.add(BtnCaraBayarRalanDokter);

        BtnCari1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari1.setMnemonic('5');
        BtnCari1.setToolTipText("Alt+5");
        BtnCari1.setName("BtnCari1"); // NOI18N
        BtnCari1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari1ActionPerformed(evt);
            }
        });
        BtnCari1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCari1KeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari1);

        BtnAll1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll1.setMnemonic('M');
        BtnAll1.setText("Semua");
        BtnAll1.setToolTipText("Alt+M");
        BtnAll1.setName("BtnAll1"); // NOI18N
        BtnAll1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAll1ActionPerformed(evt);
            }
        });
        BtnAll1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAll1KeyPressed(evt);
            }
        });
        panelGlass9.add(BtnAll1);

        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass9.add(label21);

        BtnKeluar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar1.setMnemonic('K');
        BtnKeluar1.setText("Keluar");
        BtnKeluar1.setToolTipText("Alt+K");
        BtnKeluar1.setName("BtnKeluar1"); // NOI18N
        BtnKeluar1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluar1ActionPerformed(evt);
            }
        });
        BtnKeluar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluar1KeyPressed(evt);
            }
        });
        panelGlass9.add(BtnKeluar1);

        jPanel2.add(panelGlass9, java.awt.BorderLayout.CENTER);

        internalFrame2.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        TabRawat.addTab("Data Jasmed", internalFrame2);

        internalFrame3.setName("internalFrame3"); // NOI18N
        internalFrame3.setLayout(new java.awt.BorderLayout());

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(100, 100));
        jPanel4.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass11.setName("panelGlass11"); // NOI18N
        panelGlass11.setPreferredSize(new java.awt.Dimension(44, 51));
        panelGlass11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 9));

        label34.setText("Bulan :");
        label34.setName("label34"); // NOI18N
        label34.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass11.add(label34);

        BulanCariBayar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "December" }));
        BulanCariBayar.setDisplayFormat("MMMM");
        BulanCariBayar.setName("BulanCariBayar"); // NOI18N
        BulanCariBayar.setPreferredSize(new java.awt.Dimension(90, 23));
        BulanCariBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BulanCariBayarKeyPressed(evt);
            }
        });
        panelGlass11.add(BulanCariBayar);

        label36.setText("Tahun :");
        label36.setName("label36"); // NOI18N
        label36.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass11.add(label36);

        TahunCariBayar.setDisplayFormat("yyyy");
        TahunCariBayar.setName("TahunCariBayar"); // NOI18N
        TahunCariBayar.setPreferredSize(new java.awt.Dimension(90, 23));
        TahunCariBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TahunCariBayarKeyPressed(evt);
            }
        });
        panelGlass11.add(TahunCariBayar);

        label24.setText("Total :");
        label24.setName("label24"); // NOI18N
        label24.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass11.add(label24);

        JmlKurang.setEditable(false);
        JmlKurang.setHighlighter(null);
        JmlKurang.setName("JmlKurang"); // NOI18N
        JmlKurang.setPreferredSize(new java.awt.Dimension(180, 24));
        JmlKurang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JmlKurangKeyPressed(evt);
            }
        });
        panelGlass11.add(JmlKurang);

        jPanel4.add(panelGlass11, java.awt.BorderLayout.PAGE_START);

        panelGlass12.setName("panelGlass12"); // NOI18N
        panelGlass12.setPreferredSize(new java.awt.Dimension(44, 51));
        panelGlass12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 9));

        label10.setText("Key Word :");
        label10.setName("label10"); // NOI18N
        label10.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass12.add(label10);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(318, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass12.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('5');
        BtnCari.setToolTipText("Alt+5");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass12.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass12.add(BtnAll);

        jLabel10.setText("Record :");
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass12.add(jLabel10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(53, 23));
        panelGlass12.add(LCount);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass12.add(BtnHapus);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass12.add(BtnKeluar);

        jPanel4.add(panelGlass12, java.awt.BorderLayout.CENTER);

        internalFrame3.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        internalFrame5.setName("internalFrame5"); // NOI18N
        internalFrame5.setLayout(new java.awt.BorderLayout());

        TabRawatLaborat.setBackground(new java.awt.Color(255, 255, 253));
        TabRawatLaborat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawatLaborat.setName("TabRawatLaborat"); // NOI18N
        TabRawatLaborat.setPreferredSize(new java.awt.Dimension(540, 500));
        TabRawatLaborat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatLaboratMouseClicked(evt);
            }
        });

        scrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        scrollPane3.setName("scrollPane3"); // NOI18N
        scrollPane3.setOpaque(true);

        tbRadiologiRalan3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbRadiologiRalan3.setName("tbRadiologiRalan3"); // NOI18N
        tbRadiologiRalan3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRadiologiRalan3MouseClicked(evt);
            }
        });
        tbRadiologiRalan3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbRadiologiRalan3KeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(tbRadiologiRalan3);

        TabRawatLaborat.addTab("Bayar", scrollPane3);

        scrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        scrollPane4.setName("scrollPane4"); // NOI18N
        scrollPane4.setOpaque(true);

        tbRadiologiRalan4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbRadiologiRalan4.setName("tbRadiologiRalan4"); // NOI18N
        tbRadiologiRalan4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRadiologiRalan4MouseClicked(evt);
            }
        });
        tbRadiologiRalan4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbRadiologiRalan4KeyPressed(evt);
            }
        });
        scrollPane4.setViewportView(tbRadiologiRalan4);

        TabRawatLaborat.addTab("Detail Bayar", scrollPane4);

        internalFrame5.add(TabRawatLaborat, java.awt.BorderLayout.PAGE_START);

        internalFrame3.add(internalFrame5, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Bayar Jasmed", internalFrame3);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
/*
private void KdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKdKeyPressed
    Valid.pindah(evt,BtnCari,Nm);
}//GEN-LAST:event_TKdKeyPressed
*/

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        if(TabRawat.getSelectedIndex()==0){
            prosesCari();
        }else if(TabRawat.getSelectedIndex()==1){
            if(TabRawatLaborat.getSelectedIndex()==0){
                tampilbayar();
            }else if(TabRawatLaborat.getSelectedIndex()==1){
                tampildetailbayar();
            } 
        }
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        if(TabRawat.getSelectedIndex()==0){
            emptTeks();
            prosesCari();
        }else if(TabRawat.getSelectedIndex()==1){
            if(TabRawatLaborat.getSelectedIndex()==0){
                tampilbayar();
            }else if(TabRawatLaborat.getSelectedIndex()==1){
                TCari.setText("");
                tampildetailbayar();
            } 
        }
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnKeluar);
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
       
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt,BtnAll,BtnAll);
        }
    }//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){          
            dispose();
        }else{Valid.pindah(evt,BtnPrint,BtnHapus);}
    }//GEN-LAST:event_BtnKeluarKeyPressed

private void tbRadiologiRalanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRadiologiRalanMouseClicked
    if(tabMode.getRowCount()!=0){
        try {
            getData();
        } catch (java.lang.NullPointerException e) {
        }
    }
}//GEN-LAST:event_tbRadiologiRalanMouseClicked

private void tbRadiologiRalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRadiologiRalanKeyPressed
   if(tabMode.getRowCount()!=0){
        if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }
}//GEN-LAST:event_tbRadiologiRalanKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //tampil2();
        emptTeks();
        prosesCari();
    }//GEN-LAST:event_formWindowOpened

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==0){
            prosesCari();
            RekPiutangNm1.setText(tgl_bayar.getSelectedItem().toString().substring(6,10)+"-"+tgl_bayar.getSelectedItem().toString().substring(3,5));
            RekPiutangNm2.setText(Sequel.cariIsi("select date_format(tanggal,'%Y-%m') from c_bayardokter" ));
        }else if(TabRawat.getSelectedIndex()==1){
            TabRawatLaborat.setSelectedIndex(0);
            if(TabRawatLaborat.getSelectedIndex()==0){
                tampilbayar();
            }else if(TabRawatLaborat.getSelectedIndex()==1){
                tampildetailbayar();
            } 
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        aktif=true;
    }//GEN-LAST:event_formWindowActivated

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        aktif=false;
    }//GEN-LAST:event_formWindowDeactivated

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void Tgl3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl3KeyPressed
        Valid.pindah(evt, BtnKeluar,Tgl3);
    }//GEN-LAST:event_Tgl3KeyPressed

    private void Tgl4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl4KeyPressed
        Valid.pindah(evt, Tgl3,kddokter);
    }//GEN-LAST:event_Tgl4KeyPressed

    private void btnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokterActionPerformed
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setAlwaysOnTop(false);
        dokter.setVisible(true);
    }//GEN-LAST:event_btnDokterActionPerformed

    private void btnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDokterKeyPressed
        //Valid.pindah(evt,DTPCari2,TCari);
    }//GEN-LAST:event_btnDokterKeyPressed

    private void BtnCaraBayarRalanDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCaraBayarRalanDokterActionPerformed
        carabayar.isCek();
        carabayar.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        carabayar.setLocationRelativeTo(internalFrame1);
        carabayar.setAlwaysOnTop(false);
        carabayar.setVisible(true);
    }//GEN-LAST:event_BtnCaraBayarRalanDokterActionPerformed

    private void KdGroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdGroupKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdGroupKeyPressed

    private void NmGroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NmGroupKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NmGroupKeyPressed

    private void btnAlatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlatActionPerformed
        ktg.isCek();
        ktg.onCari();
        ktg.emptTeks();
        ktg.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        ktg.setLocationRelativeTo(internalFrame1);
        ktg.setVisible(true);
    }//GEN-LAST:event_btnAlatActionPerformed

    private void btnAlatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAlatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlatKeyPressed

    private void BulanCariBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BulanCariBayarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BulanCariBayarKeyPressed

    private void TahunCariBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TahunCariBayarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TahunCariBayarKeyPressed

    private void no_buktiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_no_buktiKeyPressed
        Valid.pindah(evt,TCari,tgl_bayar);
    }//GEN-LAST:event_no_buktiKeyPressed

    private void tgl_bayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tgl_bayarKeyPressed
        Valid.pindah(evt,no_bukti,BtnKeluar);
    }//GEN-LAST:event_tgl_bayarKeyPressed

    private void keteranganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keteranganKeyPressed
        Valid.pindah(evt,no_bukti,BtnKeluar);
    }//GEN-LAST:event_keteranganKeyPressed

    private void JmlKurangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JmlKurangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JmlKurangKeyPressed

    private void tbRadiologiRalan3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRadiologiRalan3MouseClicked
    if(tabMode3.getRowCount()!=0){
        try {
            getData2();
        } catch (java.lang.NullPointerException e) {
        }
    }
    }//GEN-LAST:event_tbRadiologiRalan3MouseClicked

    private void tbRadiologiRalan3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRadiologiRalan3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbRadiologiRalan3KeyPressed

    private void tbRadiologiRalan4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRadiologiRalan4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbRadiologiRalan4MouseClicked

    private void tbRadiologiRalan4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRadiologiRalan4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbRadiologiRalan4KeyPressed

    private void TabRawatLaboratMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatLaboratMouseClicked
        if(TabRawat.getSelectedIndex()==1){
            if(TabRawatLaborat.getSelectedIndex()==0){
                tampilbayar();
            }else if(TabRawatLaborat.getSelectedIndex()==1){
                tampildetailbayar();
            } 
        }
    }//GEN-LAST:event_TabRawatLaboratMouseClicked

    private void JmlKurang2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JmlKurang2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JmlKurang2KeyPressed

    private void nama_bayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nama_bayarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_nama_bayarKeyPressed

    private void BtnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBayarActionPerformed
        //if(TabRawat.getSelectedIndex()==0){
            if(no_bukti.getText().trim().equals("")){
                Valid.textKosong(no_bukti,"No.Bukti");
            }else if(keterangan.getText().trim().equals("")){
                Valid.textKosong(keterangan,"Keterangan");
            }else if(!kddokter.getText().trim().equals("")||!nmdokter.getText().trim().equals("")){
                JOptionPane.showMessageDialog(null,"Maaf, silahkan tampilkan semua data terlebih dahulu (Klik SEMUA)...!!!!");
            }else if(!KdCaraBayar.getText().trim().equals("")||!NmCaraBayar.getText().trim().equals("")){
                JOptionPane.showMessageDialog(null,"Maaf, silahkan tampilkan semua data terlebih dahulu (Klik SEMUA)...!!!!");
            }else if(Sequel.cariInteger("select count(no_bukti) from c_bayardokter where date_format(tanggal,'%Y-%m')=?",tgl_bayar.getSelectedItem().toString().substring(6,10)+"-"+tgl_bayar.getSelectedItem().toString().substring(3,5))>0){
                JOptionPane.showMessageDialog(null,"Maaf, sudah dilakukan pembayaran Jasmed Dokter pada bulan dan tahun yang akan disimpan...!!!!");
            }else{
                int reply = JOptionPane.showConfirmDialog(rootPane,"Eeiiiiiits, udah bener belum data yang mau disimpan..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    //ChkJln.setSelected(false);
                    try {                    
                        //koneksi.setAutoCommit(false);
                        akunbayar=Sequel.cariIsi("select kd_rek from akun_bayar where nama_bayar=?",nama_bayar.getSelectedItem().toString());
                        if(Sequel.menyimpantf2("c_bayardokter","?,?,?,?,?,?,?,?,?,?,?,?,?,?","No.Permintaan",14,new String[]{
                                no_bukti.getText(),Valid.SetTgl(tgl_bayar.getSelectedItem()+""),keterangan.getText(),
                                Valid.SetTgl(Tgl3.getSelectedItem()+""),Valid.SetTgl(Tgl4.getSelectedItem()+""),
                                jumlahralan.getText(),jumlahranap.getText(),jumlahlab.getText(),
                                jumlahrad.getText(),jumlahoperasi.getText(),JmlKurang2.getText(),
                                akses.getkode(),akunbayar,Sequel.cariIsi("select cashbon from set_akun_custom")
                        })==true){
                            for(i=0;i<tbRadiologiRalan.getRowCount();i++){ 
                                //if(tbRadiologiRalan.getValueAt(i,0).toString().equals("true")){
                                    Sequel.menyimpan2("c_detail_bayardokter","?,?,?,?,?,?,?,?,?,?,?,?,?","No.Permintaan",13,new String[]{
                                        no_bukti.getText(),Valid.SetTgl(tgl_bayar.getSelectedItem()+""),keterangan.getText(),
                                        Valid.SetTgl(Tgl3.getSelectedItem()+""),Valid.SetTgl(Tgl4.getSelectedItem()+""),
                                        tbRadiologiRalan.getValueAt(i,1).toString(),tbRadiologiRalan.getValueAt(i,3).toString(),
                                        tbRadiologiRalan.getValueAt(i,4).toString(),tbRadiologiRalan.getValueAt(i,5).toString(),
                                        tbRadiologiRalan.getValueAt(i,6).toString(),tbRadiologiRalan.getValueAt(i,7).toString(),
                                        tbRadiologiRalan.getValueAt(i,8).toString(),akses.getkode()
                                    });
                                //}                        
                            }

                            //Sequel.queryu2("update billing_verif set status_bayar='Bayar' where no_bukti='"+tbRadiologiRalan2.getValueAt(i,1).toString()+"'  ");     
                            Sequel.queryu2("update billing_verif set status_bayar='Bayar',no_bukti='"+no_bukti.getText()+"' where status_bayar='Kunci' and tanggal between '"+Valid.SetTgl(Tgl3.getSelectedItem()+"")+"' and '"+Valid.SetTgl(Tgl4.getSelectedItem()+"")+"'  ");
                            
                            Sequel.queryu("delete from tampjurnal");  
                            //===
                            Sequel.menyimpan("tampjurnal","'"+akunbayar+"','"+nama_bayar.getSelectedItem()+"','0','"+JmlKurang2.getText()+"'","Rekening"); 
                            Sequel.menyimpan("tampjurnal","'211120','UTANG JASA MEDIK DOKTER TINDAKAN ','"+JmlKurang2.getText()+"','0'","Rekening"); 
                            jur.simpanJurnal(no_bukti.getText(),Valid.SetTgl(tgl_bayar.getSelectedItem()+""),"U","BAYAR UTANG JASA MEDIK DOKTER TINDAKAN"+", OLEH "+akses.getkode());                   
                            
                            
                            
                            isReset();
                            emptTeks();
                            prosesCari();
                        }
                        //koneksi.setAutoCommit(true);                    
                        JOptionPane.showMessageDialog(null,"Proses simpan selesai...!");
                    } catch (Exception e) {
                        System.out.println(e);
                    }    
                    //ChkJln.setSelected(true);            
                }
            }
        //}
    }//GEN-LAST:event_BtnBayarActionPerformed

    private void BtnBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBayarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnBayarKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(TabRawatLaborat.getSelectedIndex()==0){    
            for(i=0;i<tbRadiologiRalan3.getRowCount();i++){ 
                if(tbRadiologiRalan3.getValueAt(i,0).toString().equals("true")){
                    Sequel.meghapus("c_bayardokter","no_bukti",tbRadiologiRalan3.getValueAt(i,1).toString());
                }
            }
            Sequel.queryu2("update billing_verif set status_bayar='Kunci',no_bukti='-' where no_bukti='"+nobukti.getText()+"'  ");
            
            Sequel.queryu("delete from tampjurnal");     
            Sequel.menyimpan("tampjurnal","'"+rekbayar.getText()+"'  ,'"+RekBayarNm.getText()+"'  ,'"+JmlKurang.getText()+"','0'","Rekening");
            Sequel.menyimpan("tampjurnal","'"+rekhutang.getText()+"','"+RekPiutangNm.getText()+"','0','"+JmlKurang.getText()+"'","Rekening");         
            jur.simpanJurnal(nobukti.getText(),Valid.SetTgl(tgl_bayar.getSelectedItem()+""),"U","PEMBATALAN BAYAR UTANG JASA MEDIK DOKTER TINDAKAN"+", OLEH "+akses.getkode());                   
            
            
            emptTeks();
            tampilbayar();
            
        }else{
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan Klik Tab Bayar untuk menghapus...!!!!");
            TCari.requestFocus();
        }
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnHapusKeyPressed

    private void jumlahralanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahralanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jumlahralanKeyPressed

    private void jumlahranapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahranapKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jumlahranapKeyPressed

    private void jumlahlabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahlabKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jumlahlabKeyPressed

    private void jumlahradKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahradKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jumlahradKeyPressed

    private void jumlahoperasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahoperasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jumlahoperasiKeyPressed

    private void rekbayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rekbayarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_rekbayarKeyPressed

    private void rekhutangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rekhutangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_rekhutangKeyPressed

    private void nobuktiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nobuktiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_nobuktiKeyPressed

    private void RekBayarNmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RekBayarNmKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RekBayarNmKeyPressed

    private void RekPiutangNmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RekPiutangNmKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RekPiutangNmKeyPressed

    private void RekPiutangNm1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RekPiutangNm1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RekPiutangNm1KeyPressed

    private void RekPiutangNm2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RekPiutangNm2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RekPiutangNm2KeyPressed

    private void MnHapusJurnalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnHapusJurnalActionPerformed
       
    }//GEN-LAST:event_MnHapusJurnalActionPerformed

    private void BtnKeluar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar1ActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluar1ActionPerformed

    private void BtnKeluar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluar1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKeluar1KeyPressed

    private void BtnAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAll1ActionPerformed
        if(TabRawat.getSelectedIndex()==0){
            KdCaraBayar.setText("");
            NmCaraBayar.setText("");
            kddokter.setText("");
            nmdokter.setText("");
            prosesCari();
        }else if(TabRawat.getSelectedIndex()==1){
            if(TabRawatLaborat.getSelectedIndex()==0){
                tampilbayar();
            }else if(TabRawatLaborat.getSelectedIndex()==1){
                TCari.setText("");
                tampildetailbayar();
            } 
        }
    }//GEN-LAST:event_BtnAll1ActionPerformed

    private void BtnAll1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAll1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAll1KeyPressed

    private void BtnCari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari1ActionPerformed
        if(TabRawat.getSelectedIndex()==0){
            prosesCari();
        }
    }//GEN-LAST:event_BtnCari1ActionPerformed

    private void BtnCari1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCari1KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgJasmedDokter dialog = new DlgJasmedDokter(new javax.swing.JFrame(), true);
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
    private widget.Button BtnAll;
    private widget.Button BtnAll1;
    private widget.Button BtnBayar;
    private widget.Button BtnCaraBayarRalanDokter;
    private widget.Button BtnCari;
    private widget.Button BtnCari1;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnKeluar1;
    private widget.Button BtnPrint;
    private widget.Tanggal BulanCariBayar;
    private widget.CekBox ChkInput;
    private widget.panelisi FormInput;
    private widget.TextBox JmlKurang;
    private widget.TextBox JmlKurang2;
    private widget.TextBox KdCaraBayar;
    private widget.TextBox KdGroup;
    private widget.Label LCount;
    private javax.swing.JMenuItem MnHapusJurnal;
    private widget.TextBox NmCaraBayar;
    private widget.TextBox NmGroup;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox RekBayarNm;
    private widget.TextBox RekPiutangNm;
    private widget.TextBox RekPiutangNm1;
    private widget.TextBox RekPiutangNm2;
    private widget.TextBox TCari;
    private javax.swing.JTabbedPane TabRawat;
    private javax.swing.JTabbedPane TabRawatLaborat;
    private widget.Tanggal TahunCariBayar;
    private widget.Tanggal Tgl3;
    private widget.Tanggal Tgl4;
    private widget.Button btnAlat;
    private widget.Button btnDokter;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.InternalFrame internalFrame5;
    private widget.Label jLabel10;
    private widget.Label jLabel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.TextBox jumlahlab;
    private widget.TextBox jumlahoperasi;
    private widget.TextBox jumlahrad;
    private widget.TextBox jumlahralan;
    private widget.TextBox jumlahranap;
    private widget.TextBox kddokter;
    private widget.TextBox keterangan;
    private widget.Label label10;
    private widget.Label label12;
    private widget.Label label17;
    private widget.Label label19;
    private widget.Label label20;
    private widget.Label label21;
    private widget.Label label24;
    private widget.Label label27;
    private widget.Label label32;
    private widget.Label label34;
    private widget.Label label35;
    private widget.Label label36;
    private widget.Label label37;
    private widget.Label label39;
    private widget.ComboBox nama_bayar;
    private widget.TextBox nmdokter;
    private widget.TextBox no_bukti;
    private widget.TextBox nobukti;
    private widget.panelisi panelGlass11;
    private widget.panelisi panelGlass12;
    private widget.panelisi panelGlass14;
    private widget.panelisi panelGlass9;
    private widget.TextBox rekbayar;
    private widget.TextBox rekhutang;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane4;
    private widget.Table tbRadiologiRalan;
    private widget.Table tbRadiologiRalan3;
    private widget.Table tbRadiologiRalan4;
    private widget.Tanggal tgl_bayar;
    // End of variables declaration//GEN-END:variables
//"P","No.Bukti","Tanggal","Kd.Dokter","Nama Dokter","Tgl.1","Tgl.2","J.M.Ralan","J.M.Ranap",
//"J.M.Lab","J.M.Rad","J.M.Operasi","Total J.M","Petugas"
    private void prosesCari() {
       Valid.tabelKosong(tabMode); 
           try{  
                ps=koneksi.prepareStatement("select kd_dokter,nm_dokter from dokter where kd_dokter!='-' and status='1' and concat(kd_dokter,nm_dokter) like ? order by nm_dokter");
                try {
                     ps.setString(1,"%"+kddokter.getText()+nmdokter.getText()+"%");
                     rs=ps.executeQuery();
                     i=1;
                     totaljm=0;
                     //==
                        ttlralan2=0;
                        ttlranap2=0;
                        ttllab2=0;
                        ttlrad2=0;
                        ttloperasi2=0;
                     while(rs.next()){
                        //tabMode.addRow(new Object[]{""+i+".",rs.getString("nm_dokter"),"","",""}); 
                        jm=0;
                        a=0;
                        jmralandr=0; jmralandrpr=0; ttlralan=0;
                        jmranapdr=0; jmranapdrpr=0; ttlranap=0;
                        jmlab=0; jmdetaillab=0; ttllab=0;
                        jmrad=0; ttlrad=0;
                        ttloperasi=0;
                        //rawat jalan dokter   
                        //if(chkRalan.isSelected()==true){
                             psralandokter=koneksi.prepareStatement(
                                 "select jns_perawatan.nm_perawatan,rawat_jl_dr.tarif_tindakandr,"+
                                 "count(rawat_jl_dr.kd_jenis_prw) as jml,"+
                                 "sum(rawat_jl_dr.tarif_tindakandr) as total,rawat_jl_dr.kd_jenis_prw "+
                                 "from reg_periksa inner join jns_perawatan inner join rawat_jl_dr "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and rawat_jl_dr.no_rawat=reg_periksa.no_rawat "+
                                 "and rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw and rawat_jl_dr.no_rawat=billing_verif.no_rawat "+
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and rawat_jl_dr.kd_dokter=? "+
                                 "and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_dr.tarif_tindakandr>0 group by rawat_jl_dr.kd_jenis_prw order by jns_perawatan.nm_perawatan");   
                             psralandokterdrpr=koneksi.prepareStatement(
                                 "select jns_perawatan.nm_perawatan,rawat_jl_drpr.tarif_tindakandr,"+
                                 "count(rawat_jl_drpr.kd_jenis_prw) as jml,"+
                                 "sum(rawat_jl_drpr.tarif_tindakandr) as total,rawat_jl_drpr.kd_jenis_prw "+
                                 "from reg_periksa inner join jns_perawatan inner join rawat_jl_drpr "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and rawat_jl_drpr.no_rawat=reg_periksa.no_rawat "+
                                 "and rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw and rawat_jl_drpr.no_rawat=billing_verif.no_rawat "+
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and rawat_jl_drpr.kd_dokter=? "+
                                 "and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_jl_drpr.tarif_tindakandr>0 group by rawat_jl_drpr.kd_jenis_prw order by jns_perawatan.nm_perawatan");   
                             try {
                                 psralandokter.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psralandokter.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psralandokter.setString(3,rs.getString("kd_dokter"));
                                 psralandokter.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsralandokter=psralandokter.executeQuery();

                                 psralandokterdrpr.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psralandokterdrpr.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psralandokterdrpr.setString(3,rs.getString("kd_dokter"));
                                 psralandokterdrpr.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsralandokterdrpr=psralandokterdrpr.executeQuery();

                                 //if(rsralandokter.next()||rsralandokterdrpr.next()){
                                     //a++;
                                     //tabMode.addRow(new Object[]{"","",a+". Rawat Jalan ","","",""});   
                                 //}

                                 //rsralandokter.beforeFirst();
                                 while(rsralandokter.next()){
                                     //tabMode.addRow(new Object[]{
                                         //"","","     "+rsralandokter.getString("nm_perawatan")+" ("+rsralandokter.getString("kd_jenis_prw")+")",
                                         //rsralandokter.getString("jml"),Valid.SetAngka(rsralandokter.getDouble("total"))
                                     //});     
                                     jm=jm+rsralandokter.getDouble("total");
                                     jmralandr=jmralandr+rsralandokter.getDouble("total");
                                 }

                                 //rsralandokterdrpr.beforeFirst();               
                                 while(rsralandokterdrpr.next()){
                                     //tabMode.addRow(new Object[]{
                                         //"","","     "+rsralandokterdrpr.getString("nm_perawatan")+" ("+rsralandokterdrpr.getString("kd_jenis_prw")+")",
                                         //rsralandokterdrpr.getString("jml"),Valid.SetAngka(rsralandokterdrpr.getDouble("total"))
                                     //});     
                                     jm=jm+rsralandokterdrpr.getDouble("total");
                                     jmralandrpr=jmralandrpr+rsralandokter.getDouble("total");
                                 }
                                 
                                 ttlralan=ttlralan+jmralandr+jmralandrpr;
                                 ttlralan2=ttlralan2+jmralandr+jmralandrpr;
                                 
                             } catch (Exception e) {
                                 System.out.println("Notifikasi Ralan : "+e);
                             } finally{
                                 if(rsralandokter!=null){
                                     rsralandokter.close();
                                 }
                                 if(psralandokter!=null){
                                     psralandokter.close();
                                 }
                                 if(rsralandokterdrpr!=null){
                                     rsralandokterdrpr.close();
                                 }
                                 if(psralandokterdrpr!=null){
                                     psralandokterdrpr.close();
                                 }
                             }
                        //}                    

                        //rawat inap dokter   
                        //if(chkRanap.isSelected()==true){
                             psranapdokter=koneksi.prepareStatement(
                                 "select jns_perawatan_inap.nm_perawatan,rawat_inap_dr.tarif_tindakandr,"+
                                 "count(rawat_inap_dr.kd_jenis_prw) as jml, " +
                                 "sum(rawat_inap_dr.tarif_tindakandr) as total,rawat_inap_dr.kd_jenis_prw "+
                                 "from jns_perawatan_inap inner join rawat_inap_dr inner join reg_periksa "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and rawat_inap_dr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw and rawat_inap_dr.no_rawat=reg_periksa.no_rawat "+
                                 "and rawat_inap_dr.no_rawat=billing_verif.no_rawat "+        
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and rawat_inap_dr.kd_dokter=? "+
                                 "and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_dr.tarif_tindakandr>0 group by jns_perawatan_inap.kd_jenis_prw order by jns_perawatan_inap.nm_perawatan  ");
                             psranapdokterdrpr=koneksi.prepareStatement(
                                 "select jns_perawatan_inap.nm_perawatan,rawat_inap_drpr.tarif_tindakandr,"+
                                 "count(rawat_inap_drpr.kd_jenis_prw) as jml, " +
                                 "sum(rawat_inap_drpr.tarif_tindakandr) as total,rawat_inap_drpr.kd_jenis_prw "+
                                 "from jns_perawatan_inap inner join rawat_inap_drpr inner join reg_periksa "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and rawat_inap_drpr.kd_jenis_prw=jns_perawatan_inap.kd_jenis_prw and rawat_inap_drpr.no_rawat=reg_periksa.no_rawat "+
                                 "and rawat_inap_drpr.no_rawat=billing_verif.no_rawat "+   
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and rawat_inap_drpr.kd_dokter=? "+
                                 "and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and rawat_inap_drpr.tarif_tindakandr>0 group by jns_perawatan_inap.kd_jenis_prw order by jns_perawatan_inap.nm_perawatan  ");
                             try {
                                 psranapdokter.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psranapdokter.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psranapdokter.setString(3,rs.getString("kd_dokter"));
                                 psranapdokter.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsranapdokter=psranapdokter.executeQuery();

                                 psranapdokterdrpr.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psranapdokterdrpr.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psranapdokterdrpr.setString(3,rs.getString("kd_dokter"));
                                 psranapdokterdrpr.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsranapdokterdrpr=psranapdokterdrpr.executeQuery();
                                 //if((rsranapdokterdrpr.next())||(rsranapdokter.next())){
                                     //a++;
                                     //tabMode.addRow(new Object[]{"","",a+". Rawat Inap","","",""});  
                                 //}
                                 //rsranapdokter.beforeFirst();
                                 while(rsranapdokter.next()){
                                     //tabMode.addRow(new Object[]{
                                         //"","","     "+rsranapdokter.getString("nm_perawatan")+" ("+rsranapdokter.getString("kd_jenis_prw")+")",
                                         //rsranapdokter.getString("jml"),Valid.SetAngka(rsranapdokter.getDouble("total"))
                                     //});     
                                     jm=jm+rsranapdokter.getDouble("total");
                                     jmranapdr=jmranapdr+rsranapdokter.getDouble("total");
                                 }
                                 //rsranapdokterdrpr.beforeFirst();
                                 while(rsranapdokterdrpr.next()){
                                     //tabMode.addRow(new Object[]{
                                         //"","","     "+rsranapdokterdrpr.getString("nm_perawatan")+" ("+rsranapdokterdrpr.getString("kd_jenis_prw")+")",
                                         //rsranapdokterdrpr.getString("jml"),Valid.SetAngka(rsranapdokterdrpr.getDouble("total"))
                                     //});     
                                     jm=jm+rsranapdokterdrpr.getDouble("total");
                                     jmranapdrpr=jmranapdrpr+rsranapdokterdrpr.getDouble("total");
                                 }
                                 
                                 ttlranap=ttlranap+jmranapdr+jmranapdrpr;
                                 ttlranap2=ttlranap2+jmranapdr+jmranapdrpr;
                                 
                             } catch (Exception e) {
                                 System.out.println("Notifikasi Ranap : "+e);
                             } finally{
                                 if(rsranapdokter!=null){
                                     rsranapdokter.close();
                                 }
                                 if(psranapdokter!=null){
                                     psranapdokter.close();
                                 }
                                 if(rsranapdokterdrpr!=null){
                                     rsranapdokterdrpr.close();
                                 }
                                 if(psranapdokterdrpr!=null){
                                     psranapdokterdrpr.close();
                                 }
                             }
                        //} 
                        
                        //if(chkLaborat.isSelected()==true){
                             psperiksa_lab=koneksi.prepareStatement(
                                 "select jns_perawatan_lab.nm_perawatan,periksa_lab.tarif_tindakan_dokter,"+
                                 "periksa_lab.kd_jenis_prw,count(periksa_lab.kd_jenis_prw) as jml, "+
                                 "sum(periksa_lab.tarif_tindakan_dokter) as total,jns_perawatan_lab.kd_jenis_prw "+
                                 " from periksa_lab inner join jns_perawatan_lab inner join reg_periksa "+
                                 " inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.no_rawat=reg_periksa.no_rawat and periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                                 " and periksa_lab.no_rawat=billing_verif.no_rawat "+
                                 " where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and periksa_lab.kd_dokter=? "+
                                 " and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_lab.tarif_tindakan_dokter>0 group by periksa_lab.kd_jenis_prw order by jns_perawatan_lab.nm_perawatan  "); 
                             try {
                                 psperiksa_lab.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psperiksa_lab.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psperiksa_lab.setString(3,rs.getString("kd_dokter"));
                                 psperiksa_lab.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsperiksa_lab=psperiksa_lab.executeQuery();
                                 //if(rsperiksa_lab.next()){
                                     //a++;
                                     //tabMode.addRow(new Object[]{"","",a+". Pemeriksaan Lab ","","",""});   
                                 //}

                                 //rsperiksa_lab.beforeFirst();
                                 
                                 while(rsperiksa_lab.next()){    
                                     //tabMode.addRow(new Object[]{
                                         //"","","     "+rsperiksa_lab.getString("nm_perawatan")+" ("+rsperiksa_lab.getString("kd_jenis_prw")+")",
                                         //rsperiksa_lab.getString("jml"),Valid.SetAngka(rsperiksa_lab.getDouble("total"))
                                     //});      
                                     jm=jm+rsperiksa_lab.getDouble("total");
                                     ttllab=ttllab+rsperiksa_lab.getDouble("total");
                                     ttllab2=ttllab2+rsperiksa_lab.getDouble("total");
                                 }
                             } catch (Exception e) {
                                 System.out.println("Notifikasi Periksa Lab : "+e);
                             } finally{
                                 if(rsperiksa_lab!=null){
                                     rsperiksa_lab.close();
                                 }
                                 if(psperiksa_lab!=null){
                                     psperiksa_lab.close();
                                 }
                             }

                             psdetaillab=koneksi.prepareStatement(
                                 "select sum(detail_periksa_lab.bagian_dokter) as total,"+
                                 "template_laboratorium.Pemeriksaan,count(detail_periksa_lab.id_template) as jml, "+
                                 "periksa_lab.kd_jenis_prw "+
                                 "from detail_periksa_lab inner join periksa_lab "+
                                 "inner join reg_periksa inner join pasien inner join template_laboratorium "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and periksa_lab.no_rawat=detail_periksa_lab.no_rawat "+
                                 "and periksa_lab.kd_jenis_prw=detail_periksa_lab.kd_jenis_prw "+
                                 "and periksa_lab.tgl_periksa=detail_periksa_lab.tgl_periksa "+
                                 "and periksa_lab.jam=detail_periksa_lab.jam "+
                                 "and periksa_lab.no_rawat=reg_periksa.no_rawat "+
                                 "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                                 "and detail_periksa_lab.id_template=template_laboratorium.id_template "+
                                 "and periksa_lab.no_rawat=billing_verif.no_rawat "+
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? "+
                                 "and periksa_lab.kd_dokter=? and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? "+
                                 "and detail_periksa_lab.bagian_dokter>0 group by detail_periksa_lab.id_template");
                             try {
                                 psdetaillab.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psdetaillab.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psdetaillab.setString(3,rs.getString("kd_dokter"));
                                 psdetaillab.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsdetaillab=psdetaillab.executeQuery();
                                 //rsdetaillab.last();
                                 //if(rsdetaillab.getRow()>0){
                                     //a++;
                                     //tabMode.addRow(new Object[]{"","",a+". Detail Pemeriksaan Lab ","","",""});
                                 //} 
                                 //rsdetaillab.beforeFirst();
                                 while(rsdetaillab.next()){
                                     //tabMode.addRow(new Object[]{
                                         //"","","     "+rsdetaillab.getString("Pemeriksaan")+" ("+rsdetaillab.getString("kd_jenis_prw")+")",
                                         //rsdetaillab.getString("jml"),Valid.SetAngka(rsdetaillab.getDouble("total"))
                                     //});    
                                     jm=jm+rsdetaillab.getDouble("total");
                                     ttllab=ttllab+rsdetaillab.getDouble("total");
                                     ttllab2=ttllab2+rsdetaillab.getDouble("total");
                                 }
                             } catch (Exception e) {
                                 System.out.println("Notifikasi Detail Lab : "+e);
                             } finally{
                                 if(rsdetaillab!=null){
                                     rsdetaillab.close();
                                 }
                                 if(psdetaillab!=null){
                                     psdetaillab.close();
                                 }
                             }
                        //}

                        //if(chkRadiologi.isSelected()==true){
                            //periksa radiologi
                             psperiksa_radiologi=koneksi.prepareStatement(
                                 "select jns_perawatan_radiologi.nm_perawatan,periksa_radiologi.tarif_tindakan_dokter,"+
                                 "periksa_radiologi.kd_jenis_prw,count(periksa_radiologi.kd_jenis_prw) as jml, "+
                                 "sum(periksa_radiologi.tarif_tindakan_dokter) as total "+
                                 " from periksa_radiologi inner join jns_perawatan_radiologi inner join reg_periksa"+
                                 " inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw and periksa_radiologi.no_rawat=reg_periksa.no_rawat "+
                                 " and periksa_radiologi.no_rawat=billing_verif.no_rawat "+
                                 " where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and periksa_radiologi.kd_dokter=? "+
                                 " and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and periksa_radiologi.tarif_tindakan_dokter>0 group by periksa_radiologi.kd_jenis_prw order by jns_perawatan_radiologi.nm_perawatan  "); 
                             try {
                                 psperiksa_radiologi.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psperiksa_radiologi.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psperiksa_radiologi.setString(3,rs.getString("kd_dokter"));
                                 psperiksa_radiologi.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsperiksa_radiologi=psperiksa_radiologi.executeQuery();
                                 //if(rsperiksa_radiologi.next()){
                                     //a++;
                                     //tabMode.addRow(new Object[]{"","",a+". Pemeriksaan Radiologi","","",""});   
                                 //}

                                 //rsperiksa_radiologi.beforeFirst();
                                 while(rsperiksa_radiologi.next()){
                                     //tabMode.addRow(new Object[]{
                                         //"","","     "+rsperiksa_radiologi.getString("nm_perawatan")+" ("+rsperiksa_radiologi.getString("kd_jenis_prw")+")",
                                         //rsperiksa_radiologi.getString("jml"),Valid.SetAngka(rsperiksa_radiologi.getDouble("total"))
                                     //});             
                                     jm=jm+rsperiksa_radiologi.getDouble("total");
                                     ttlrad=ttlrad+rsperiksa_radiologi.getDouble("total");
                                     ttlrad2=ttlrad2+rsperiksa_radiologi.getDouble("total");
                                 }
                             } catch (Exception e) {
                                 System.out.println("Notifikasi Radiologi : "+e);
                             } finally{
                                 if(rsperiksa_radiologi!=null){
                                     rsperiksa_radiologi.close();
                                 }
                                 if(psperiksa_radiologi!=null){
                                     psperiksa_radiologi.close();
                                 }
                             }

                             
                        //}    

                        //if(chkOperasi.isSelected()==true){
                             psbiayaoperator1=koneksi.prepareStatement(
                                 "select paket_operasi.nm_perawatan,operasi.biayaoperator1,"+
                                 "count(operasi.kode_paket) as jml, " +
                                 "sum(operasi.biayaoperator1) as total,operasi.kode_paket "+
                                 "from paket_operasi inner join operasi inner join reg_periksa "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and operasi.kode_paket=paket_operasi.kode_paket and operasi.no_rawat=reg_periksa.no_rawat "+
                                 "and operasi.no_rawat=billing_verif.no_rawat "+
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and operasi.operator1=? "+
                                 "and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.biayaoperator1>0 group by operasi.kode_paket order by paket_operasi.nm_perawatan ");
                             psbiayaoperator2=koneksi.prepareStatement("select paket_operasi.nm_perawatan,operasi.biayaoperator2,"+
                                 "count(operasi.kode_paket) as jml, " +
                                 "sum(operasi.biayaoperator2) as total,operasi.kode_paket "+
                                 "from paket_operasi inner join operasi inner join reg_periksa "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and operasi.kode_paket=paket_operasi.kode_paket and operasi.no_rawat=reg_periksa.no_rawat "+
                                 "and operasi.no_rawat=billing_verif.no_rawat "+
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and operasi.operator2=? "+
                                 "and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.biayaoperator2>0 group by operasi.kode_paket order by paket_operasi.nm_perawatan ");
                             psbiayaoperator3=koneksi.prepareStatement("select paket_operasi.nm_perawatan,operasi.biayaoperator3,"+
                                 "count(operasi.kode_paket) as jml, " +
                                 "sum(operasi.biayaoperator3) as total,operasi.kode_paket "+
                                 "from paket_operasi inner join operasi inner join reg_periksa "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and operasi.kode_paket=paket_operasi.kode_paket and operasi.no_rawat=reg_periksa.no_rawat "+
                                 "and operasi.no_rawat=billing_verif.no_rawat "+
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and operasi.operator3=? "+
                                 "and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.biayaoperator3>0 group by operasi.kode_paket order by paket_operasi.nm_perawatan ");
                             psbiayadokter_anak=koneksi.prepareStatement("select paket_operasi.nm_perawatan,operasi.biayadokter_anak,"+
                                 "count(operasi.kode_paket) as jml, " +
                                 "sum(operasi.biayadokter_anak) as total,operasi.kode_paket "+
                                 "from paket_operasi inner join operasi inner join reg_periksa "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and operasi.kode_paket=paket_operasi.kode_paket and operasi.no_rawat=reg_periksa.no_rawat "+
                                 "and operasi.no_rawat=billing_verif.no_rawat "+
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and operasi.dokter_anak=? "+
                                 "and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.biayadokter_anak>0 group by operasi.kode_paket order by paket_operasi.nm_perawatan ");
                             psbiaya_dokter_umum=koneksi.prepareStatement("select paket_operasi.nm_perawatan,operasi.biaya_dokter_umum,"+
                                 "count(operasi.kode_paket) as jml, " +
                                 "sum(operasi.biaya_dokter_umum) as total,operasi.kode_paket "+
                                 "from paket_operasi inner join operasi inner join reg_periksa "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and operasi.kode_paket=paket_operasi.kode_paket and operasi.no_rawat=reg_periksa.no_rawat "+
                                 "and operasi.no_rawat=billing_verif.no_rawat "+
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and operasi.dokter_umum=? "+
                                 "and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.biaya_dokter_umum>0 group by operasi.kode_paket order by paket_operasi.nm_perawatan ");
                             psbiaya_dokter_pjanak=koneksi.prepareStatement("select paket_operasi.nm_perawatan,operasi.biaya_dokter_pjanak,"+
                                 "count(operasi.kode_paket) as jml, " +
                                 "sum(operasi.biaya_dokter_pjanak) as total,operasi.kode_paket "+
                                 "from paket_operasi inner join operasi inner join reg_periksa "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and operasi.kode_paket=paket_operasi.kode_paket and operasi.no_rawat=reg_periksa.no_rawat "+
                                 "and operasi.no_rawat=billing_verif.no_rawat "+
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and operasi.dokter_pjanak=? "+
                                 "and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.biaya_dokter_pjanak>0 group by operasi.kode_paket order by paket_operasi.nm_perawatan ");
                             psbiayadokter_anestesi=koneksi.prepareStatement("select paket_operasi.nm_perawatan,operasi.biayadokter_anestesi,"+
                                 "count(operasi.kode_paket) as jml, " +
                                 "sum(operasi.biayadokter_anestesi) as total,operasi.kode_paket "+
                                 "from paket_operasi inner join operasi inner join reg_periksa "+
                                 "inner join penjab inner join billing_verif on reg_periksa.kd_pj=penjab.kd_pj and operasi.kode_paket=paket_operasi.kode_paket and operasi.no_rawat=reg_periksa.no_rawat "+
                                 "and operasi.no_rawat=billing_verif.no_rawat "+
                                 "where billing_verif.status_bayar='Kunci' and billing_verif.tanggal between ? and ? and operasi.dokter_anestesi=? "+
                                 "and concat(reg_periksa.kd_pj,penjab.png_jawab) like ? and operasi.biayadokter_anestesi>0 group by operasi.kode_paket order by paket_operasi.nm_perawatan ");
                             try {
                                 psbiayaoperator1.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psbiayaoperator1.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psbiayaoperator1.setString(3,rs.getString("kd_dokter"));
                                 psbiayaoperator1.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsbiayaoperator1=psbiayaoperator1.executeQuery();

                                 psbiayaoperator2.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psbiayaoperator2.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psbiayaoperator2.setString(3,rs.getString("kd_dokter"));
                                 psbiayaoperator2.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsbiayaoperator2=psbiayaoperator2.executeQuery();

                                 psbiayaoperator3.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psbiayaoperator3.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psbiayaoperator3.setString(3,rs.getString("kd_dokter"));
                                 psbiayaoperator3.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsbiayaoperator3=psbiayaoperator3.executeQuery(); 

                                 psbiayadokter_anak.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psbiayadokter_anak.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psbiayadokter_anak.setString(3,rs.getString("kd_dokter"));
                                 psbiayadokter_anak.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsbiayadokter_anak=psbiayadokter_anak.executeQuery();   

                                 psbiaya_dokter_pjanak.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psbiaya_dokter_pjanak.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psbiaya_dokter_pjanak.setString(3,rs.getString("kd_dokter"));
                                 psbiaya_dokter_pjanak.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsbiaya_dokter_pjanak=psbiaya_dokter_pjanak.executeQuery();  

                                 psbiaya_dokter_umum.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psbiaya_dokter_umum.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psbiaya_dokter_umum.setString(3,rs.getString("kd_dokter"));
                                 psbiaya_dokter_umum.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsbiaya_dokter_umum=psbiaya_dokter_umum.executeQuery();  

                                 psbiayadokter_anestesi.setString(1,Valid.SetTgl(Tgl3.getSelectedItem()+""));
                                 psbiayadokter_anestesi.setString(2,Valid.SetTgl(Tgl4.getSelectedItem()+""));
                                 psbiayadokter_anestesi.setString(3,rs.getString("kd_dokter"));
                                 psbiayadokter_anestesi.setString(4,"%"+KdCaraBayar.getText()+NmCaraBayar.getText()+"%");
                                 rsbiayadokter_anestesi=psbiayadokter_anestesi.executeQuery();

                                 //if((rsbiayaoperator1.next())||(rsbiayaoperator2.next())||(rsbiayaoperator3.next())||(rsbiayadokter_anak.next())||(rsbiaya_dokter_pjanak.next())||(rsbiaya_dokter_umum.next())||(rsbiayadokter_anestesi.next())){
                                      //a++;
                                      //tabMode.addRow(new Object[]{"","",a+". Operasi/VK","","",""});   
                                 //}

                                 //dokter operasi               
                                 //rsbiayaoperator1.beforeFirst();
                                 while(rsbiayaoperator1.next()){
                                     //tabMode.addRow(new Object[]{
                                         //"","","     "+rsbiayaoperator1.getString("nm_perawatan")+" ("+rsbiayaoperator1.getString("kode_paket")+")(Operator 1)",
                                         //rsbiayaoperator1.getString("jml"),Valid.SetAngka(rsbiayaoperator1.getDouble("total"))
                                     //});        
                                     jm=jm+rsbiayaoperator1.getDouble("total");
                                     ttloperasi=ttloperasi+rsbiayaoperator1.getDouble("total");
                                     ttloperasi2=ttloperasi2+rsbiayaoperator1.getDouble("total");
                                 }

                                 //dokter anasthesi               
                                 //rsbiayaoperator2.beforeFirst();
                                 while(rsbiayaoperator2.next()){
                                     //tabMode.addRow(new Object[]{"","","     "+rsbiayaoperator2.getString("nm_perawatan")+" ("+rsbiayaoperator2.getString("kode_paket")+")(Operator 2)",
                                         //rsbiayaoperator2.getString("jml"),Valid.SetAngka(rsbiayaoperator2.getDouble("total"))
                                     //});      
                                     jm=jm+rsbiayaoperator2.getDouble("total");
                                     ttloperasi=ttloperasi+rsbiayaoperator2.getDouble("total");
                                     ttloperasi2=ttloperasi2+rsbiayaoperator2.getDouble("total");
                                 }
                                 //rsbiayaoperator2.close();

                                 //dokter anasthesi               
                                 //rsbiayaoperator3.beforeFirst();
                                 while(rsbiayaoperator3.next()){
                                     //tabMode.addRow(new Object[]{"","","     "+rsbiayaoperator3.getString("nm_perawatan")+" ("+rsbiayaoperator3.getString("kode_paket")+")(Operator 3)",
                                         //rsbiayaoperator3.getString("jml"),Valid.SetAngka(rsbiayaoperator3.getDouble("total"))
                                     //});           
                                     jm=jm+rsbiayaoperator3.getDouble("total");
                                     ttloperasi=ttloperasi+rsbiayaoperator3.getDouble("total");
                                     ttloperasi2=ttloperasi2+rsbiayaoperator3.getDouble("total");
                                 }
                                 //rsbiayaoperator3.close();

                                 //dokter anasthesi               
                                 //rsbiayadokter_anak.beforeFirst();
                                 while(rsbiayadokter_anak.next()){
                                     //tabMode.addRow(new Object[]{"","","     "+rsbiayadokter_anak.getString("nm_perawatan")+" ("+rsbiayadokter_anak.getString("kode_paket")+")(dr Anak)",
                                         //rsbiayadokter_anak.getString("jml"),Valid.SetAngka(rsbiayadokter_anak.getDouble("total"))
                                     //});       
                                     jm=jm+rsbiayadokter_anak.getDouble("total");
                                     ttloperasi=ttloperasi+rsbiayadokter_anak.getDouble("total");
                                     ttloperasi2=ttloperasi2+rsbiayadokter_anak.getDouble("total");
                                 }
                                 //rsbiayadokter_anak.close();

                                 //dokter anasthesi               
                                 //rsbiayadokter_anestesi.beforeFirst();
                                 while(rsbiayadokter_anestesi.next()){
                                     //tabMode.addRow(new Object[]{
                                         //"","","     "+rsbiayadokter_anestesi.getString("nm_perawatan")+" ("+rsbiayadokter_anestesi.getString("kode_paket")+")(dr Anestesi)",
                                         //rsbiayadokter_anestesi.getString("jml"),Valid.SetAngka(rsbiayadokter_anestesi.getDouble("total"))
                                     //});       
                                     jm=jm+rsbiayadokter_anestesi.getDouble("total");
                                     ttloperasi=ttloperasi+rsbiayadokter_anestesi.getDouble("total");
                                     ttloperasi2=ttloperasi2+rsbiayadokter_anestesi.getDouble("total");
                                 }

                                 //dokter pj anak              
                                 rsbiaya_dokter_pjanak.beforeFirst();
                                 while(rsbiaya_dokter_pjanak.next()){
                                     //tabMode.addRow(new Object[]{
                                         //"","","     "+rsbiaya_dokter_pjanak.getString("nm_perawatan")+" ("+rsbiaya_dokter_pjanak.getString("kode_paket")+")(dr Pj Anak)",
                                         //rsbiaya_dokter_pjanak.getString("jml"),Valid.SetAngka(rsbiaya_dokter_pjanak.getDouble("total"))
                                     //});       
                                     jm=jm+rsbiaya_dokter_pjanak.getDouble("total");
                                     ttloperasi=ttloperasi+rsbiaya_dokter_pjanak.getDouble("total");
                                     ttloperasi2=ttloperasi2+rsbiaya_dokter_pjanak.getDouble("total");
                                 }
                                 //rsbiaya_dokter_pjanak.close();

                                 //dokter umum              
                                 rsbiaya_dokter_umum.beforeFirst();
                                 while(rsbiaya_dokter_umum.next()){
                                     //tabMode.addRow(new Object[]{"","","     "+rsbiaya_dokter_umum.getString("nm_perawatan")+" ("+rsbiaya_dokter_umum.getString("kode_paket")+")(dr Umum)",
                                         //rsbiaya_dokter_umum.getString("jml"),Valid.SetAngka(rsbiaya_dokter_umum.getDouble("total"))
                                     //});       
                                     jm=jm+rsbiaya_dokter_umum.getDouble("total");
                                     ttloperasi=ttloperasi+rsbiaya_dokter_umum.getDouble("total");
                                     ttloperasi2=ttloperasi2+rsbiaya_dokter_umum.getDouble("total");
                                 }
                                 //rsbiaya_dokter_umum.close();
                             } catch (Exception e) {
                                 System.out.println("Notifikasi Operasi : "+e);
                             } finally{
                                 if(rsbiayaoperator1!=null){
                                     rsbiayaoperator1.close();
                                 }
                                 if(psbiayaoperator1!=null){
                                     psbiayaoperator1.close();
                                 }
                                 if(rsbiayaoperator2!=null){
                                     rsbiayaoperator2.close();
                                 }
                                 if(psbiayaoperator2!=null){
                                     psbiayaoperator2.close();
                                 }
                                 if(rsbiayaoperator3!=null){
                                     rsbiayaoperator3.close();
                                 }
                                 if(psbiayaoperator3!=null){
                                     psbiayaoperator3.close();
                                 }                            
                                 if(rsbiayadokter_anak!=null){
                                     rsbiayadokter_anak.close();
                                 }
                                 if(psbiayadokter_anak!=null){
                                     psbiayadokter_anak.close();
                                 }                            
                                 if(rsbiaya_dokter_pjanak!=null){
                                     rsbiaya_dokter_pjanak.close();
                                 }
                                 if(psbiaya_dokter_pjanak!=null){
                                     psbiaya_dokter_pjanak.close();
                                 }
                                 if(rsbiaya_dokter_umum!=null){
                                     rsbiaya_dokter_umum.close();
                                 }
                                 if(psbiaya_dokter_umum!=null){
                                     psbiaya_dokter_umum.close();
                                 }
                                 if(rsbiayadokter_anestesi!=null){
                                     rsbiayadokter_anestesi.close();
                                 }
                                 if(psbiayadokter_anestesi!=null){
                                     psbiayadokter_anestesi.close();
                                 }
                             }   
                        //}

                        

                        totaljm=totaljm+jm;
                        //if(jm>0){
                            //tabMode.addRow(new Object[]{"","","Total : ","",Valid.SetAngka(jm)});
                        //} 
                       // "Kd.Dokter","Nama Dokter","JM.Ralan","JM.Ranap","JM.Lab","JM.Rad","JM.Operasi","Total JM"
                        //tabMode.addRow(new Object[]{""+i+".",rs.getString("nm_dokter"),"","",""});   
                        if(ttlralan+ttlranap+ttllab+ttlrad+ttloperasi>0){
                            tabMode.addRow(new Object[]{
                                false,rs.getString("kd_dokter"),rs.getString("nm_dokter"),ttlralan,ttlranap,ttllab,ttlrad,ttloperasi,(ttlralan+ttlranap+ttllab+ttlrad+ttloperasi)
                            });
                        }
                        i++;
                     }
                     
                } catch (Exception e) {
                    System.out.println("Notifikasi Perujuk Radiologi : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                }           

                //if(totaljm>0){
                    //tabMode.addRow(new Object[]{">> ","Total Jasa Medis :","","",Valid.SetAngka(totaljm)});
                //}  
                 jumlahralan.setText(Double.toString(ttlralan2));
                 jumlahranap.setText(Double.toString(ttlranap2));
                 jumlahlab.setText(Double.toString(ttllab2));
                 jumlahrad.setText(Double.toString(ttlrad2));
                 jumlahoperasi.setText(Double.toString(ttloperasi2));
                 JmlKurang2.setText(Double.toString(ttlralan2+ttlranap2+ttllab2+ttlrad2+ttloperasi2));
             }catch(SQLException e){
                System.out.println("Catatan  "+e);
             } 
           
    }
    
    private void tampilbayar() {
        Valid.tabelKosong(tabMode3);
        try {
            ps=koneksi.prepareStatement(
                    "select c_bayardokter.no_bukti,date_format(c_bayardokter.tanggal,'%M-%Y')as tanggal, c_bayardokter.tanggal_1,c_bayardokter.tanggal_2, c_bayardokter.jm_ralan, c_bayardokter.jm_ranap, "+
                    "c_bayardokter.jm_lab, c_bayardokter.jm_rad, c_bayardokter.jm_operasi, c_bayardokter.total_utang,petugas.nama, "+
                    "c_bayardokter.rek_bayar, c_bayardokter.rek_hutang "+       
                    "from c_bayardokter inner join petugas on "+
                    "petugas.nip=c_bayardokter.petugas where "+
                    "date_format(c_bayardokter.tanggal,'%M')=? and date_format(c_bayardokter.tanggal,'%Y')=? "); 
            try {
                ps.setString(1,BulanCariBayar.getSelectedItem()+"");
                ps.setString(2,TahunCariBayar.getSelectedItem()+"");
                //ps.setString(3,"%"+TCari.getText());
                y=0;               
                rs=ps.executeQuery();              
                while(rs.next()){
                    tabMode3.addRow(new Object[]{                   
                        false,rs.getString("no_bukti"),rs.getString("tanggal"),
                        rs.getString("tanggal_1"),rs.getString("tanggal_2"),
                        rs.getDouble("jm_ralan"),rs.getDouble("jm_ranap"),rs.getDouble("jm_lab"),
                        rs.getDouble("jm_rad"),rs.getDouble("jm_operasi"),rs.getDouble("total_utang"),
                        rs.getString("nama"),rs.getString("rek_bayar"),rs.getString("rek_hutang")
                    });   
                    
                   JmlKurang.setText(rs.getString("total_utang"));
                   
                }  
                
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            LCount.setText(""+tabMode3.getRowCount());
            
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }        
    }
    
    private void tampildetailbayar() {
        Valid.tabelKosong(tabMode4);
        try {
            ps=koneksi.prepareStatement(
                    "select c_detail_bayardokter.no_bukti,date_format(c_detail_bayardokter.tanggal,'%M-%Y')as tanggal, c_detail_bayardokter.kd_dokter, dokter.nm_dokter, c_detail_bayardokter.tanggal_1,c_detail_bayardokter.tanggal_2, c_detail_bayardokter.jm_ralan, c_detail_bayardokter.jm_ranap, "+
                    "c_detail_bayardokter.jm_lab, c_detail_bayardokter.jm_rad, c_detail_bayardokter.jm_operasi, c_detail_bayardokter.total_utang,petugas.nama "+
                    "from c_detail_bayardokter inner join dokter inner join petugas inner join c_bayardokter on "+
                    "c_detail_bayardokter.kd_dokter=dokter.kd_dokter and petugas.nip=c_detail_bayardokter.petugas and c_bayardokter.no_bukti=c_detail_bayardokter.no_bukti where "+
                    "date_format(c_detail_bayardokter.tanggal,'%M')=? and date_format(c_detail_bayardokter.tanggal,'%Y')=? and dokter.nm_dokter like ? "); 
            try {
                ps.setString(1,BulanCariBayar.getSelectedItem()+"");
                ps.setString(2,TahunCariBayar.getSelectedItem()+"");
                ps.setString(3,"%"+TCari.getText()+"%");
                y=0;               
                rs=ps.executeQuery();              
                while(rs.next()){
                    tabMode4.addRow(new Object[]{                   
                        false,rs.getString("no_bukti"),rs.getString("tanggal"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),
                        rs.getString("tanggal_1"),rs.getString("tanggal_2"),
                        rs.getDouble("jm_ralan"),rs.getDouble("jm_ranap"),rs.getDouble("jm_lab"),
                        rs.getDouble("jm_rad"),rs.getDouble("jm_operasi"),rs.getDouble("total_utang"),
                        rs.getString("nama")
                    });                  
                    
                }  
                
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            LCount.setText(""+tabMode4.getRowCount());
            
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }        
    }
     //"No.Klaem","Tanggal","Jam","Perusahaan","Petugas",
     //"Total Tagihan","Kode.Perusahaan","Kode.Petugas","Rek Piutang","Rek Bayar" ,
     //"Jml.Piutang","Discount"
    private void getData() {
        if(tbRadiologiRalan.getSelectedRow()!= -1){
            //cmbStatus.setSelectedItem(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),0).toString());
            //Tahun.setSelectedItem(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),1).toString());
            //kddokter.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),2).toString());
            //nmdokter.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),3).toString());
            Valid.SetTgl(Tgl3,tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),4).toString());
            Valid.SetTgl(Tgl4,tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),5).toString());
        }
    }
    
    private void getData2() {
        if(tbRadiologiRalan3.getSelectedRow()!= -1){
            nobukti.setText(tbRadiologiRalan3.getValueAt(tbRadiologiRalan3.getSelectedRow(),1).toString());
            rekbayar.setText(tbRadiologiRalan3.getValueAt(tbRadiologiRalan3.getSelectedRow(),12).toString());
            rekhutang.setText(tbRadiologiRalan3.getValueAt(tbRadiologiRalan3.getSelectedRow(),13).toString());
            
            RekBayarNm.setText(Sequel.cariIsi("select nm_rek from rekening where kd_rek=?",tbRadiologiRalan3.getValueAt(tbRadiologiRalan3.getSelectedRow(),12).toString()));
            RekPiutangNm.setText(Sequel.cariIsi("select nm_rek from rekening where kd_rek=?",tbRadiologiRalan3.getValueAt(tbRadiologiRalan3.getSelectedRow(),13).toString()));
            
        }
    }
    
    public void isCek(){
        //MnHMnKlaemtEnabled(akses.getpermintaan_radiologi());      
        //BtnHapus.setEnabled(akses.getpermintaan_radiologi());
        //BtnPrint.setEnabled(akses.getpermintaan_radiologi());
    }
    
    public void setPasien(String pasien){
        TCari.setText(pasien);
    }   

    public void emptTeks() {
        TCari.setText("");
        //no_bukti.setText("");
        keterangan.setText("");
        KdCaraBayar.setText("");
        NmCaraBayar.setText("");
        kddokter.setText("");
        nmdokter.setText("");
        jumlahralan.setText("0");
        jumlahranap.setText("0");
        jumlahlab.setText("0");
        jumlahrad.setText("0");
        jumlahoperasi.setText("0");
        JmlKurang2.setText("0");
        JmlKurang.setText("0");
        autoNomor();
    }
    
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,125));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    private void autoNomor() {
        //Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(no_bukti,4),signed)),0) from c_detail_bayardokter where tanggal like '"+Valid.SetTgl(tgl_bayar.getSelectedItem()+"")+"' ","JM"+tgl_bayar.getSelectedItem().toString().substring(6,10)+tgl_bayar.getSelectedItem().toString().substring(3,5),4,no_bukti);           
        Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(no_bukti,6),signed)),0) from c_detail_bayardokter","JM",6,no_bukti);
    }
    
    public void isReset(){
        jml=tbRadiologiRalan.getRowCount();
        for(i=0;i<jml;i++){ 
            tbRadiologiRalan.setValueAt(false,i,0);
        }
        Valid.tabelKosong(tabMode);
        prosesCari();
    }
    
}
