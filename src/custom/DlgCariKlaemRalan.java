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

public class DlgCariKlaemRalan extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabMode2;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();   
    private DlgDepartemen dept=new DlgDepartemen(null,false);
    private DlgCariPerusahaan poli=new DlgCariPerusahaan(null,false);  
    private Jurnal jur=new Jurnal();
    private int i,nilai_detik,permintaanbaru=0;
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
    /** Creates new form DlgProgramStudi
     * @param parent
     * @param modal */
    public DlgCariKlaemRalan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Klaem","Tanggal","Jam","Perusahaan","Petugas",
            "Total Tagihan","Kode.Perusahaan","Kode.Petugas","Rek Piutang","Rek Bayar",
            "Jml.Piutang","Discount","Status Klaem","Jml.Terklaem","Jml.Piutang"
            
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbRadiologiRalan.setModel(tabMode);

        tbRadiologiRalan.setPreferredScrollableViewportSize(new Dimension(800,800));
        tbRadiologiRalan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 15; i++) {
            TableColumn column = tbRadiologiRalan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(120);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(200);
            }else if(i==4){
                column.setPreferredWidth(150);           
            }else if(i==5){
                column.setPreferredWidth(150);
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
            }else if(i==11){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==12){
                column.setPreferredWidth(70);
            }else if(i==13){
                column.setPreferredWidth(150);
            }else if(i==14){
                column.setPreferredWidth(150);
            }
        }
        tbRadiologiRalan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabMode2=new DefaultTableModel(null,new Object[]{
            "No.","No.Rawat","No.MR","Nama Pasien","Nama Dokter","Perusahaan",
            "Kons + Adm","Tindakan","Lab","Radiologi","Alat Radiologi","Obat",
            "JML.Kwitansi","Di.Bayar Pasien","Total Diajukan","Status","No.Klaem","Tgl.Klaim"   
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                    boolean a = false;
                    if (colIndex==0) {
                        a=true;
                    }
                    return a;
              }
              Class[] types = new Class[] {
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, 
                 java.lang.Double.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, 
                 java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Object.class,
                 java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRadiologiRalan2.setModel(tabMode2);

        tbRadiologiRalan2.setPreferredScrollableViewportSize(new Dimension(800,800));
        tbRadiologiRalan2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 18; i++) {
            TableColumn column = tbRadiologiRalan2.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(120);
            }else if(i==2){
                column.setPreferredWidth(50);
            }else if(i==3){
                column.setPreferredWidth(180);
            }else if(i==4){
                column.setPreferredWidth(130);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(70);
            }else if(i==7){
                column.setPreferredWidth(70);
            }else if(i==8){
                column.setPreferredWidth(70);           
            }else if(i==9){
                column.setPreferredWidth(70);
            }else if(i==10){
                column.setPreferredWidth(70);
            }else if(i==11){
                column.setPreferredWidth(70);
            }else if(i==12){
                column.setPreferredWidth(70);
            }else if(i==13){
                column.setPreferredWidth(80);
            }else if(i==14){
                column.setPreferredWidth(80);
            }else if(i==15){
                //column.setPreferredWidth(90);
                column.setMinWidth(0);
                column.setMaxWidth(0); 
            }else if(i==16){
                column.setPreferredWidth(90);
            }else if(i==17){
                column.setPreferredWidth(80);
                //column.setMinWidth(0);
                //column.setMaxWidth(0);           
            }
        }
        tbRadiologiRalan2.setDefaultRenderer(Object.class, new WarnaTable()); 
        
        
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        } 
        
        poli.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                //if(akses.getform().equals("DlgKlaemAsuransiDetail")){
                    if(poli.getTable().getSelectedRow()!= -1){                                          
                        Kd.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),0).toString());
                        Nm.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),1).toString()); 
                        //NmAkunPiutang.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),2).toString());
                        //AkunPiutang.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),3).toString());
                        Kd.requestFocus();  
                        //tampil();
                        //isHitungRalan();
                    }                
                //}
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
        MnHapusDataSalah = new javax.swing.JMenuItem();
        MnHapusJurnal = new javax.swing.JMenuItem();
        KdDept = new widget.TextBox();
        StatusPengeluaran = new widget.TextBox();
        Header = new widget.TextBox();
        terbilang = new widget.TextBox();
        Pengeluaran = new widget.TextBox();
        Pengeluaran2 = new widget.TextBox();
        RekBayarNm = new widget.TextBox();
        RekBayar = new widget.TextBox();
        RekPiutang = new widget.TextBox();
        RekPiutangNm = new widget.TextBox();
        Total = new widget.TextBox();
        BtnHapus = new widget.Button();
        JmlTerklaem2 = new widget.TextBox();
        TotalTerklaem2 = new widget.TextBox();
        SudahTerklaem = new widget.TextBox();
        Kd = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        jPanel2 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        label10 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel10 = new widget.Label();
        LCount = new widget.Label();
        jLabel12 = new widget.Label();
        cmbStatus = new widget.ComboBox();
        panelisi1 = new widget.panelisi();
        label11 = new widget.Label();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        Tgl2 = new widget.Tanggal();
        BtnAll = new widget.Button();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame2 = new widget.InternalFrame();
        scrollPane1 = new widget.ScrollPane();
        tbRadiologiRalan = new widget.Table();
        internalFrame3 = new widget.InternalFrame();
        scrollPane2 = new widget.ScrollPane();
        tbRadiologiRalan2 = new widget.Table();
        panelGlass10 = new widget.panelisi();
        jLabel4 = new widget.Label();
        TNoPermintaanRalan = new widget.TextBox();
        jSeparator6 = new javax.swing.JSeparator();
        label19 = new widget.Label();
        JmlTagihan2 = new widget.TextBox();
        label17 = new widget.Label();
        Discount = new widget.TextBox();
        label20 = new widget.Label();
        TotalTagihan2 = new widget.TextBox();
        jLabel13 = new widget.Label();
        jPanel3 = new javax.swing.JPanel();
        panelGlass11 = new widget.panelisi();
        label22 = new widget.Label();
        JmlKurang = new widget.TextBox();
        label21 = new widget.Label();
        JmlTerklaem = new widget.TextBox();
        label23 = new widget.Label();
        label24 = new widget.Label();
        TotalTerklaem = new widget.TextBox();
        jLabel14 = new widget.Label();
        panelGlass9 = new widget.panelisi();
        label14 = new widget.Label();
        Tanggal = new widget.Tanggal();
        jLabel11 = new widget.Label();
        nama_bayar = new widget.ComboBox();
        BtnSimpan = new widget.Button();
        label34 = new widget.Label();
        Nm = new widget.TextBox();
        BtnUnit = new widget.Button();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnHapusDataSalah.setBackground(new java.awt.Color(255, 255, 254));
        MnHapusDataSalah.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnHapusDataSalah.setForeground(new java.awt.Color(50, 50, 50));
        MnHapusDataSalah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnHapusDataSalah.setText("Hapus Data Salah");
        MnHapusDataSalah.setName("MnHapusDataSalah"); // NOI18N
        MnHapusDataSalah.setPreferredSize(new java.awt.Dimension(200, 28));
        MnHapusDataSalah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnHapusDataSalahActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnHapusDataSalah);

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

        KdDept.setHighlighter(null);
        KdDept.setName("KdDept"); // NOI18N
        KdDept.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdDeptKeyPressed(evt);
            }
        });

        StatusPengeluaran.setHighlighter(null);
        StatusPengeluaran.setName("StatusPengeluaran"); // NOI18N
        StatusPengeluaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusPengeluaranKeyPressed(evt);
            }
        });

        Header.setHighlighter(null);
        Header.setName("Header"); // NOI18N
        Header.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HeaderActionPerformed(evt);
            }
        });
        Header.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HeaderKeyPressed(evt);
            }
        });

        terbilang.setEditable(false);
        terbilang.setHighlighter(null);
        terbilang.setName("terbilang"); // NOI18N

        Pengeluaran.setText("0");
        Pengeluaran.setHighlighter(null);
        Pengeluaran.setName("Pengeluaran"); // NOI18N
        Pengeluaran.setPreferredSize(new java.awt.Dimension(150, 24));
        Pengeluaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PengeluaranKeyPressed(evt);
            }
        });

        Pengeluaran2.setText("0");
        Pengeluaran2.setHighlighter(null);
        Pengeluaran2.setName("Pengeluaran2"); // NOI18N
        Pengeluaran2.setPreferredSize(new java.awt.Dimension(150, 24));
        Pengeluaran2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Pengeluaran2KeyPressed(evt);
            }
        });

        RekBayarNm.setEditable(false);
        RekBayarNm.setHighlighter(null);
        RekBayarNm.setName("RekBayarNm"); // NOI18N
        RekBayarNm.setPreferredSize(new java.awt.Dimension(150, 24));

        RekBayar.setEditable(false);
        RekBayar.setHighlighter(null);
        RekBayar.setName("RekBayar"); // NOI18N
        RekBayar.setPreferredSize(new java.awt.Dimension(100, 24));

        RekPiutang.setEditable(false);
        RekPiutang.setHighlighter(null);
        RekPiutang.setName("RekPiutang"); // NOI18N
        RekPiutang.setPreferredSize(new java.awt.Dimension(100, 24));

        RekPiutangNm.setEditable(false);
        RekPiutangNm.setHighlighter(null);
        RekPiutangNm.setName("RekPiutangNm"); // NOI18N
        RekPiutangNm.setPreferredSize(new java.awt.Dimension(150, 24));

        Total.setText("0");
        Total.setHighlighter(null);
        Total.setName("Total"); // NOI18N
        Total.setPreferredSize(new java.awt.Dimension(150, 24));
        Total.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TotalKeyPressed(evt);
            }
        });

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

        JmlTerklaem2.setHighlighter(null);
        JmlTerklaem2.setName("JmlTerklaem2"); // NOI18N
        JmlTerklaem2.setPreferredSize(new java.awt.Dimension(150, 24));
        JmlTerklaem2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JmlTerklaem2KeyPressed(evt);
            }
        });

        TotalTerklaem2.setHighlighter(null);
        TotalTerklaem2.setName("TotalTerklaem2"); // NOI18N
        TotalTerklaem2.setPreferredSize(new java.awt.Dimension(150, 24));
        TotalTerklaem2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TotalTerklaem2KeyPressed(evt);
            }
        });

        SudahTerklaem.setHighlighter(null);
        SudahTerklaem.setName("SudahTerklaem"); // NOI18N
        SudahTerklaem.setPreferredSize(new java.awt.Dimension(150, 24));
        SudahTerklaem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SudahTerklaemKeyPressed(evt);
            }
        });

        Kd.setHighlighter(null);
        Kd.setName("Kd"); // NOI18N
        Kd.setPreferredSize(new java.awt.Dimension(80, 24));
        Kd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdKeyPressed(evt);
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

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Klaem Ralan ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 9));

        label10.setText("Key Word :");
        label10.setName("label10"); // NOI18N
        label10.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(label10);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(318, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass8.add(TCari);

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
        panelGlass8.add(BtnCari);

        jLabel10.setText("Record :");
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass8.add(jLabel10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(53, 23));
        panelGlass8.add(LCount);

        jLabel12.setText("Status Klaem :");
        jLabel12.setName("jLabel12"); // NOI18N
        jLabel12.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel12);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Proses", "Sudah", "Pending" }));
        cmbStatus.setName("cmbStatus"); // NOI18N
        cmbStatus.setPreferredSize(new java.awt.Dimension(100, 23));
        cmbStatus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbStatusItemStateChanged(evt);
            }
        });
        panelGlass8.add(cmbStatus);

        jPanel2.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelisi1.setName("panelisi1"); // NOI18N
        panelisi1.setPreferredSize(new java.awt.Dimension(100, 56));
        panelisi1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(60, 23));
        panelisi1.add(label11);

        Tgl1.setDisplayFormat("dd-MM-yyyy");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl1KeyPressed(evt);
            }
        });
        panelisi1.add(Tgl1);

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("s.d.");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(25, 23));
        panelisi1.add(label18);

        Tgl2.setDisplayFormat("dd-MM-yyyy");
        Tgl2.setName("Tgl2"); // NOI18N
        Tgl2.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl2KeyPressed(evt);
            }
        });
        panelisi1.add(Tgl2);

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
        panelisi1.add(BtnAll);

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
        panelisi1.add(BtnPrint);

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
        panelisi1.add(BtnKeluar);

        jPanel2.add(panelisi1, java.awt.BorderLayout.PAGE_END);

        internalFrame1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

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

        TabRawat.addTab("Data Klaem", internalFrame2);

        internalFrame3.setName("internalFrame3"); // NOI18N
        internalFrame3.setLayout(new java.awt.BorderLayout());

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        scrollPane2.setName("scrollPane2"); // NOI18N
        scrollPane2.setOpaque(true);

        tbRadiologiRalan2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbRadiologiRalan2.setComponentPopupMenu(jPopupMenu1);
        tbRadiologiRalan2.setName("tbRadiologiRalan2"); // NOI18N
        tbRadiologiRalan2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRadiologiRalan2MouseClicked(evt);
            }
        });
        tbRadiologiRalan2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbRadiologiRalan2KeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(tbRadiologiRalan2);

        internalFrame3.add(scrollPane2, java.awt.BorderLayout.CENTER);

        panelGlass10.setName("panelGlass10"); // NOI18N
        panelGlass10.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 9));

        jLabel4.setText("No.Permintaan :");
        jLabel4.setName("jLabel4"); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(85, 14));
        panelGlass10.add(jLabel4);

        TNoPermintaanRalan.setHighlighter(null);
        TNoPermintaanRalan.setName("TNoPermintaanRalan"); // NOI18N
        TNoPermintaanRalan.setPreferredSize(new java.awt.Dimension(150, 24));
        TNoPermintaanRalan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoPermintaanRalanKeyPressed(evt);
            }
        });
        panelGlass10.add(TNoPermintaanRalan);

        jSeparator6.setBackground(new java.awt.Color(220, 225, 215));
        jSeparator6.setForeground(new java.awt.Color(220, 225, 215));
        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator6.setName("jSeparator6"); // NOI18N
        jSeparator6.setOpaque(true);
        jSeparator6.setPreferredSize(new java.awt.Dimension(5, 23));
        panelGlass10.add(jSeparator6);

        label19.setText("Tttl.Tagihan :");
        label19.setName("label19"); // NOI18N
        label19.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass10.add(label19);

        JmlTagihan2.setHighlighter(null);
        JmlTagihan2.setName("JmlTagihan2"); // NOI18N
        JmlTagihan2.setPreferredSize(new java.awt.Dimension(150, 24));
        JmlTagihan2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JmlTagihan2KeyPressed(evt);
            }
        });
        panelGlass10.add(JmlTagihan2);

        label17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label17.setText("-  Disc. ");
        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass10.add(label17);

        Discount.setHighlighter(null);
        Discount.setName("Discount"); // NOI18N
        Discount.setPreferredSize(new java.awt.Dimension(150, 24));
        Discount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiscountKeyPressed(evt);
            }
        });
        panelGlass10.add(Discount);

        label20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label20.setText("=");
        label20.setName("label20"); // NOI18N
        label20.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass10.add(label20);

        TotalTagihan2.setHighlighter(null);
        TotalTagihan2.setName("TotalTagihan2"); // NOI18N
        TotalTagihan2.setPreferredSize(new java.awt.Dimension(150, 24));
        TotalTagihan2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TotalTagihan2KeyPressed(evt);
            }
        });
        panelGlass10.add(TotalTagihan2);

        jLabel13.setName("jLabel13"); // NOI18N
        jLabel13.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass10.add(jLabel13);

        internalFrame3.add(panelGlass10, java.awt.BorderLayout.PAGE_START);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass11.setName("panelGlass11"); // NOI18N
        panelGlass11.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 9));

        label22.setText("Piutang :");
        label22.setName("label22"); // NOI18N
        label22.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass11.add(label22);

        JmlKurang.setEditable(false);
        JmlKurang.setHighlighter(null);
        JmlKurang.setName("JmlKurang"); // NOI18N
        JmlKurang.setPreferredSize(new java.awt.Dimension(150, 24));
        JmlKurang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JmlKurangKeyPressed(evt);
            }
        });
        panelGlass11.add(JmlKurang);

        label21.setText("Ter-Klaem :");
        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass11.add(label21);

        JmlTerklaem.setHighlighter(null);
        JmlTerklaem.setName("JmlTerklaem"); // NOI18N
        JmlTerklaem.setPreferredSize(new java.awt.Dimension(150, 24));
        JmlTerklaem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JmlTerklaemKeyPressed(evt);
            }
        });
        panelGlass11.add(JmlTerklaem);

        label23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label23.setText("=");
        label23.setName("label23"); // NOI18N
        label23.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass11.add(label23);

        label24.setText("Sisa Piutang :");
        label24.setName("label24"); // NOI18N
        label24.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass11.add(label24);

        TotalTerklaem.setHighlighter(null);
        TotalTerklaem.setName("TotalTerklaem"); // NOI18N
        TotalTerklaem.setPreferredSize(new java.awt.Dimension(150, 24));
        TotalTerklaem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TotalTerklaemKeyPressed(evt);
            }
        });
        panelGlass11.add(TotalTerklaem);

        jLabel14.setName("jLabel14"); // NOI18N
        jLabel14.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass11.add(jLabel14);

        jPanel3.add(panelGlass11, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 9));

        label14.setText("Tanggal :");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(label14);

        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "13-12-2020" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        panelGlass9.add(Tanggal);

        jLabel11.setText("Akun Bayar :");
        jLabel11.setName("jLabel11"); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass9.add(jLabel11);

        nama_bayar.setName("nama_bayar"); // NOI18N
        nama_bayar.setPreferredSize(new java.awt.Dimension(265, 23));
        nama_bayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nama_bayarKeyPressed(evt);
            }
        });
        panelGlass9.add(nama_bayar);

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Klaem");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BtnSimpan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnSimpan);

        label34.setText("Perusahaan :");
        label34.setName("label34"); // NOI18N
        label34.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass9.add(label34);

        Nm.setHighlighter(null);
        Nm.setName("Nm"); // NOI18N
        Nm.setPreferredSize(new java.awt.Dimension(200, 24));
        Nm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NmKeyPressed(evt);
            }
        });
        panelGlass9.add(Nm);

        BtnUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnUnit.setMnemonic('4');
        BtnUnit.setToolTipText("ALt+4");
        BtnUnit.setName("BtnUnit"); // NOI18N
        BtnUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUnitActionPerformed(evt);
            }
        });
        panelGlass9.add(BtnUnit);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_END);

        internalFrame3.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        TabRawat.addTab("Detail Klaem", internalFrame3);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
/*
private void KdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKdKeyPressed
    Valid.pindah(evt,BtnCari,Nm);
}//GEN-LAST:event_TKdKeyPressed
*/

    private void Tgl1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl1KeyPressed
        Valid.pindah(evt,BtnKeluar,Tgl2);
    }//GEN-LAST:event_Tgl1KeyPressed

    private void Tgl2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl2KeyPressed
        Valid.pindah(evt, Tgl1,TCari);
    }//GEN-LAST:event_Tgl2KeyPressed

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
            tampil();
        }else if(TabRawat.getSelectedIndex()==1){
            tampil2();
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
            TCari.setText("");
            tampil();
        }else if(TabRawat.getSelectedIndex()==1){
            TNoPermintaanRalan.setText("");                       
            KdDept.setText("");
            Kd.setText(""); 
            Nm.setText(""); 
            TCari.setText("");
            tampil2();
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
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
        if(TabRawat.getSelectedIndex()==0){
            if(tabMode.getRowCount()==0){
                JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                TCari.requestFocus();
            }else if(tabMode.getRowCount()!=0){
                Sequel.AutoComitFalse();
                Sequel.queryu("delete from temporary3");
                int row=tabMode.getRowCount();
                for(int i=0;i<row;i++){
                    Sequel.menyimpan("temporary3","'0','"+
                        tabMode.getValueAt(i,0).toString()+"','"+
                        tabMode.getValueAt(i,1).toString()+"','"+
                        tabMode.getValueAt(i,2).toString()+"','"+
                        tabMode.getValueAt(i,3).toString()+"','"+
                        tabMode.getValueAt(i,4).toString()+"','"+
                        tabMode.getValueAt(i,5).toString()+"','"+
                        tabMode.getValueAt(i,6).toString()+"','"+
                        tabMode.getValueAt(i,7).toString()+"','"+
                        tabMode.getValueAt(i,8).toString()+"','','','','','','','','','','','','','','','','','','','','','','','','','','','',''","Posting Jurnal");
                }
                Map<String, Object> param = new HashMap<>();
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());
                param.put("logo",Sequel.cariGambar("select logo from setting")); 
                param.put("periode",Tgl1.getSelectedItem()+" s.d. "+Tgl2.getSelectedItem());
                Valid.MyReportqry("rptPengeluaranHarianData.jasper","report","::[ Data Pengeluaran Harian ]::",
                    "select no, temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14, temp15 from temporary3 order by no asc",param);
                this.setCursor(Cursor.getDefaultCursor());
            }
        }else if(TabRawat.getSelectedIndex()==1){
            if(tabMode2.getRowCount()==0){
                JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                TCari.requestFocus();
            }else if(tabMode2.getRowCount()!=0){
                Sequel.AutoComitFalse();
                Sequel.queryu("delete from temporary3");
                int row=tabMode2.getRowCount();
                for(int i=0;i<row;i++){
                    Sequel.menyimpan("temporary3","'0','"+
                        tabMode2.getValueAt(i,0).toString()+"','"+
                        tabMode2.getValueAt(i,1).toString()+"','"+
                        tabMode2.getValueAt(i,2).toString()+"','"+
                        tabMode2.getValueAt(i,3).toString()+"','"+
                        tabMode2.getValueAt(i,4).toString()+"','"+
                        tabMode2.getValueAt(i,5).toString()+"','"+
                        tabMode2.getValueAt(i,6).toString()+"','"+
                        tabMode2.getValueAt(i,7).toString()+"','"+
                        tabMode2.getValueAt(i,8).toString()+"','','','','','','','','','','','','','','','','','','','','','','','','','','','',''","Posting Jurnal");
                }
                Map<String, Object> param = new HashMap<>();
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());
                param.put("logo",Sequel.cariGambar("select logo from setting")); 
                param.put("periode",Tgl1.getSelectedItem()+" s.d. "+Tgl2.getSelectedItem());
                Valid.MyReportqry("rptPengeluaranHarianDataItem.jasper","report","::[ Item Pengeluaran Harian ]::",
                    "select no, temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14, temp15 from temporary3 order by no asc",param);
                this.setCursor(Cursor.getDefaultCursor());
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
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

private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
    if(TCari.getText().trim().equals("")){
         JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\nKlik data pada table untuk memilih...!!!!");
    }else{                               
            Sequel.queryu2("delete from bayar_piutang where catatan=?",1,new String[]{
                TCari.getText()
            });
            
            Sequel.mengedit("piutang_pasien","no_klaim='"+TCari.getText()+"'","status='Belum Lunas',no_klaim='-'"); 
            
            Sequel.queryu2("delete from c_klaim where no_klaim=?",1,new String[]{
                TCari.getText()
            });
            
            Sequel.queryu2("delete from c_klaim_detail where no_klaim=?",1,new String[]{
                TCari.getText()
            });

            Sequel.mengedittf("c_billing_klaim","no_klaim=? ","status_klaim='Belum',no_klaim='-'",1,new String[]{
                                    TCari.getText()
                                 });
            //======jurnal
            Sequel.queryu("delete from tampjurnal");                               
            Sequel.menyimpan("tampjurnal","'"+RekPiutang.getText()+"','"+RekPiutangNm.getText()+"','"+Total.getText()+"','0'","Rekening");         
            Sequel.menyimpan("tampjurnal","'"+RekBayar.getText()+"','"+RekBayarNm.getText()+"','0','"+Total.getText()+"'","Rekening"); 
            jur.simpanJurnal(TCari.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),"U","PEMBATALAN BAYAR PIUTANG KLAEM RALAN"+", OLEH "+akses.getkode());                   
            //======
            TCari.setText("");
            BtnCariActionPerformed(null);

            //tampil();            
    }
}//GEN-LAST:event_BtnHapusActionPerformed

private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari,BtnAll);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

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
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void tbRadiologiRalan2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRadiologiRalan2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbRadiologiRalan2MouseClicked

    private void tbRadiologiRalan2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRadiologiRalan2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbRadiologiRalan2KeyPressed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==0){
            //BtnAllActionPerformed(null);
            tampil();
        }else if(TabRawat.getSelectedIndex()==1){
            //BtnAllActionPerformed(null);
            if(jLabel13.getText().trim().equals("Pending")){
                jLabel11.setVisible(false);
                nama_bayar.setVisible(false);
            }else{
                jLabel11.setVisible(true);
                nama_bayar.setVisible(true);
            }
            tampil2();
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        aktif=true;
    }//GEN-LAST:event_formWindowActivated

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        aktif=false;
    }//GEN-LAST:event_formWindowDeactivated

    private void KdDeptKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDeptKeyPressed

    }//GEN-LAST:event_KdDeptKeyPressed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt, TNoPermintaanRalan, BtnSimpan);
    }//GEN-LAST:event_TanggalKeyPressed

    private void StatusPengeluaranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusPengeluaranKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusPengeluaranKeyPressed

    private void HeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HeaderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HeaderActionPerformed

    private void HeaderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HeaderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HeaderKeyPressed

    private void PengeluaranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PengeluaranKeyPressed
       
    }//GEN-LAST:event_PengeluaranKeyPressed

    private void Pengeluaran2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Pengeluaran2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Pengeluaran2KeyPressed

    private void TotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TotalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalKeyPressed

    private void TNoPermintaanRalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoPermintaanRalanKeyPressed
        //Valid.pindah(evt,TNoReg,DTPReg);
    }//GEN-LAST:event_TNoPermintaanRalanKeyPressed

    private void JmlTagihan2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JmlTagihan2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JmlTagihan2KeyPressed

    private void DiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiscountKeyPressed
        
    }//GEN-LAST:event_DiscountKeyPressed

    private void TotalTagihan2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TotalTagihan2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalTagihan2KeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoPermintaanRalan.getText().trim().equals("")){
            Valid.textKosong(TNoPermintaanRalan,"No.Klaem");
            BtnCariActionPerformed(null);
        }else if(jLabel13.getText().trim().equals("Sudah")){
            JOptionPane.showMessageDialog(null,"Maaf, Gagal menyimpan. No.Klaem sudah Lunas...Silahkan hapus jurnal terlebih dahulu ");
        }else if(jLabel13.getText().trim().equals("Proses")){
            if(Double.parseDouble(JmlTerklaem.getText())>Double.parseDouble(TotalTagihan2.getText())){
                JOptionPane.showMessageDialog(null,"Maaf, Jumlah Terklaem melebihi Jumlah Klaem...! ");
            }else if(Double.parseDouble(JmlTerklaem.getText())==Double.parseDouble(JmlKurang.getText())){
                    //============
                    int jawab=JOptionPane.showConfirmDialog(null, "Eeiiiiiits, udah bener belum data yang mau diklaem..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
                    if(jawab==JOptionPane.YES_OPTION){
                        try {                                             
                                akunbayar=Sequel.cariIsi("select kd_rek from akun_bayar where nama_bayar=?",nama_bayar.getSelectedItem().toString());
                                if(Sequel.mengedittf("c_klaim","no_klaim=?","status_klaim=?,rek_bayar=?,jml_terklaim=jml_terklaim+? ",4,new String[]{
                                                "Sudah",akunbayar,JmlTerklaem.getText(),TNoPermintaanRalan.getText()
                                })==true){
                                    //================                             
                                    for(i=0;i<tbRadiologiRalan2.getRowCount();i++){ 
                                        if(tbRadiologiRalan2.getValueAt(i,16).toString().equals(TNoPermintaanRalan.getText())){                                    
                                            Sequel.mengedittf("c_billing_klaim","no_klaim=? ","status_klaim='Sudah' ",1,new String[]{
                                                TNoPermintaanRalan.getText()
                                            });
                                            //==================================
                                            Sequel.mengedittf("c_klaim_detail","no_klaim=?","status_klaim='Sudah' ",1,new String[]{
                                                TNoPermintaanRalan.getText()
                                            });
                                            //==================================
                                            //if(Sequel.cariInteger("select count(catatan) from bayar_piutang where catatan=?",TNoPermintaanRalan.getText())>0){
                                                Sequel.mengedittf("piutang_pasien","no_rawat=?","status='Lunas',no_klaim=? ",2,new String[]{
                                                    TNoPermintaanRalan.getText(),tbRadiologiRalan2.getValueAt(i,1).toString()
                                                });
                                                //==================================
                                                pssimpanbayar=koneksi.prepareStatement("insert into bayar_piutang values(?,?,?,?,?,?)");
                                                try {
                                                    pssimpanbayar.setString(1,Valid.SetTgl(Tanggal.getSelectedItem()+""));
                                                    pssimpanbayar.setString(2,tbRadiologiRalan2.getValueAt(i,2).toString());
                                                    pssimpanbayar.setString(3,JmlTagihan2.getText());
                                                    pssimpanbayar.setString(4,TNoPermintaanRalan.getText());
                                                    pssimpanbayar.setString(5,tbRadiologiRalan2.getValueAt(i,1).toString());
                                                    pssimpanbayar.setString(6,akunbayar);        
                                                    pssimpanbayar.executeUpdate();

                                                } catch (Exception e) {
                                                    System.out.println("Notifikasi : "+e);
                                                } finally{
                                                    if(pssimpanbayar != null){
                                                        pssimpanbayar.close();
                                                    }
                                                }
                                            //}

                                        }

                                    } 
                                    //==================================
                                    if(Double.parseDouble(Discount.getText())==0){
                                        Sequel.queryu("delete from tampjurnal");  
                                        //===
                                        Sequel.menyimpan("tampjurnal","'"+RekPiutang.getText()+"','"+RekPiutangNm.getText()+"','0','"+JmlTerklaem.getText()+"'","Rekening"); 
                                        Sequel.menyimpan("tampjurnal","'"+akunbayar+"','"+nama_bayar.getSelectedItem()+"','"+JmlTerklaem.getText()+"','0'","Rekening"); 
                                        jur.simpanJurnal(TNoPermintaanRalan.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),"U","BAYAR PIUTANG KLAEM RALAN"+", OLEH "+akses.getkode());                   
                                    }else{
                                        Sequel.queryu("delete from tampjurnal");  
                                        //===
                                        //Sequel.menyimpan("tampjurnal","'"+RekPiutang.getText()+"','"+RekPiutangNm.getText()+"','0','"+JmlTerklaem.getText()+"'","Rekening"); 
                                        //Sequel.menyimpan("tampjurnal","'"+akunbayar+"','"+nama_bayar.getSelectedItem()+"','"+Double.toString(Double.parseDouble(JmlTerklaem.getText())-Double.parseDouble(Discount.getText()))+"','0'","Rekening"); 
                                        //Sequel.menyimpan("tampjurnal","'540102','POTONGAN BIAYA PASIEN RAWAT JALAN','"+Discount.getText()+"','0'","Rekening");
                                        Sequel.menyimpan("tampjurnal","'"+RekPiutang.getText()+"','"+RekPiutangNm.getText()+"','0','"+Double.toString(Double.parseDouble(JmlTerklaem.getText())+Double.parseDouble(Discount.getText()))+"'","Rekening"); 
                                        Sequel.menyimpan("tampjurnal","'"+akunbayar+"','"+nama_bayar.getSelectedItem()+"','"+JmlTerklaem.getText()+"','0'","Rekening"); 
                                        Sequel.menyimpan("tampjurnal","'540102','POTONGAN BIAYA PASIEN RAWAT JALAN','"+Discount.getText()+"','0'","Rekening");
                                        jur.simpanJurnal(TNoPermintaanRalan.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),"U","BAYAR PIUTANG KLAEM RALAN"+", OLEH "+akses.getkode());                   
                                    }
                                    //==================================   
                                    cmbStatus.setSelectedItem("Sudah");
                                    tampil2();
                                    emptTeks();
                                }

                        }catch (Exception ex) {
                            System.out.println("Notifikasi : "+ex);            
                            JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Data yang sama dimasukkan sebelumnya...!");
                        } 
                    }
                    //============
            }else if(Double.parseDouble(JmlTerklaem.getText())<Double.parseDouble(JmlKurang.getText())){
                    //============
                    int jawab=JOptionPane.showConfirmDialog(null, "Eeiiiiiits, udah bener belum data yang mau diklaem..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
                    if(jawab==JOptionPane.YES_OPTION){
                        try {                                             
                                akunbayar=Sequel.cariIsi("select kd_rek from akun_bayar where nama_bayar=?",nama_bayar.getSelectedItem().toString());
                                if(Sequel.mengedittf("c_klaim","no_klaim=?","status_klaim=?,rek_bayar=?,jml_terklaim=jml_terklaim+? ",4,new String[]{
                                                "Pending",akunbayar,JmlTerklaem.getText(),TNoPermintaanRalan.getText()
                                })==true){
                                    //================                             
                                    for(i=0;i<tbRadiologiRalan2.getRowCount();i++){ 
                                        if(tbRadiologiRalan2.getValueAt(i,16).toString().equals(TNoPermintaanRalan.getText())){                                    
                                            Sequel.mengedittf("c_billing_klaim","no_klaim=? ","status_klaim='Pending' ",1,new String[]{
                                                TNoPermintaanRalan.getText()
                                            });
                                            //==================================
                                            Sequel.mengedittf("c_klaim_detail","no_klaim=?","status_klaim='Pending' ",1,new String[]{
                                                TNoPermintaanRalan.getText()
                                            });
                                            //==================================
                                            //if(Sequel.cariInteger("select count(catatan) from bayar_piutang where catatan=?",TNoPermintaanRalan.getText())>0){
                                                Sequel.mengedittf("piutang_pasien","no_rawat=?","status='Lunas',no_klaim=? ",2,new String[]{
                                                    TNoPermintaanRalan.getText(),tbRadiologiRalan2.getValueAt(i,1).toString()
                                                });
                                                //==================================
                                                pssimpanbayar=koneksi.prepareStatement("insert into bayar_piutang values(?,?,?,?,?,?)");
                                                try {
                                                    pssimpanbayar.setString(1,Valid.SetTgl(Tanggal.getSelectedItem()+""));
                                                    pssimpanbayar.setString(2,tbRadiologiRalan2.getValueAt(i,2).toString());
                                                    pssimpanbayar.setString(3,JmlTagihan2.getText());
                                                    pssimpanbayar.setString(4,TNoPermintaanRalan.getText());
                                                    pssimpanbayar.setString(5,tbRadiologiRalan2.getValueAt(i,1).toString());
                                                    pssimpanbayar.setString(6,akunbayar);        
                                                    pssimpanbayar.executeUpdate();

                                                } catch (Exception e) {
                                                    System.out.println("Notifikasi : "+e);
                                                } finally{
                                                    if(pssimpanbayar != null){
                                                        pssimpanbayar.close();
                                                    }
                                                }
                                            //}

                                        }

                                    } 
                                    //==================================
                                    if(Double.parseDouble(Discount.getText())==0){
                                        Sequel.queryu("delete from tampjurnal");  
                                        //===
                                        Sequel.menyimpan("tampjurnal","'"+RekPiutang.getText()+"','"+RekPiutangNm.getText()+"','0','"+JmlTerklaem.getText()+"'","Rekening"); 
                                        Sequel.menyimpan("tampjurnal","'"+akunbayar+"','"+nama_bayar.getSelectedItem()+"','"+JmlTerklaem.getText()+"','0'","Rekening"); 
                                        jur.simpanJurnal(TNoPermintaanRalan.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),"U","BAYAR PIUTANG KLAEM RALAN"+", OLEH "+akses.getkode());                   
                                    }else{
                                        Sequel.queryu("delete from tampjurnal");  
                                        //===
                                        Sequel.menyimpan("tampjurnal","'"+RekPiutang.getText()+"','"+RekPiutangNm.getText()+"','0','"+Double.toString(Double.parseDouble(JmlTerklaem.getText())+Double.parseDouble(Discount.getText()))+"'","Rekening"); 
                                        Sequel.menyimpan("tampjurnal","'"+akunbayar+"','"+nama_bayar.getSelectedItem()+"','"+JmlTerklaem.getText()+"','0'","Rekening"); 
                                        Sequel.menyimpan("tampjurnal","'540102','POTONGAN BIAYA PASIEN RAWAT JALAN','"+Discount.getText()+"','0'","Rekening");
                                        jur.simpanJurnal(TNoPermintaanRalan.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),"U","BAYAR PIUTANG KLAEM RALAN"+", OLEH "+akses.getkode());                   
                                    }
                                    //==================================   
                                    cmbStatus.setSelectedItem("Pending");
                                    tampil2();
                                    emptTeks();
                                }

                        }catch (Exception ex) {
                            System.out.println("Notifikasi : "+ex);            
                            JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Data yang sama dimasukkan sebelumnya...!");
                        } 
                    }
                    //============
            }    
        
        }else if(jLabel13.getText().trim().equals("Pending")){
            if(Double.parseDouble(JmlTerklaem.getText())>Double.parseDouble(JmlKurang.getText())){
                JOptionPane.showMessageDialog(null,"Maaf, Jumlah Terklaem melebihi Jumlah Klaem...! ");
            }else if(Double.parseDouble(TotalTerklaem.getText())==0){
                    //============
                    int jawab=JOptionPane.showConfirmDialog(null, "Eeiiiiiits, udah bener belum data yang mau diklaem..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
                    if(jawab==JOptionPane.YES_OPTION){
                        try {                                             
                                akunbayar=Sequel.cariIsi("select kd_rek from akun_bayar where nama_bayar=?",nama_bayar.getSelectedItem().toString());
                                if(Sequel.mengedittf("c_klaim","no_klaim=?","status_klaim=?,rek_bayar=?,jml_terklaim=jml_terklaim+? ",4,new String[]{
                                                "Sudah",akunbayar,JmlTerklaem.getText(),TNoPermintaanRalan.getText()
                                })==true){
                                    //================                             
                                    for(i=0;i<tbRadiologiRalan2.getRowCount();i++){ 
                                        if(tbRadiologiRalan2.getValueAt(i,16).toString().equals(TNoPermintaanRalan.getText())){                                    
                                            Sequel.mengedittf("c_billing_klaim","no_klaim=? ","status_klaim='Sudah' ",1,new String[]{
                                                TNoPermintaanRalan.getText()
                                            });
                                            //==================================
                                            Sequel.mengedittf("c_klaim_detail","no_klaim=?","status_klaim='Sudah' ",1,new String[]{
                                                TNoPermintaanRalan.getText()
                                            });
                                            //==================================
                                            //if(Sequel.cariInteger("select count(catatan) from bayar_piutang where catatan=?",TNoPermintaanRalan.getText())>0){
                                                //Sequel.mengedittf("piutang_pasien","no_rawat=?","status='Lunas',no_klaim=? ",2,new String[]{
                                                    //TNoPermintaanRalan.getText(),tbRadiologiRalan2.getValueAt(i,1).toString()
                                                //});
                                                //==================================
                                                //pssimpanbayar=koneksi.prepareStatement("insert into bayar_piutang values(?,?,?,?,?,?)");
                                                //try {
                                                    //pssimpanbayar.setString(1,Valid.SetTgl(Tanggal.getSelectedItem()+""));
                                                    //pssimpanbayar.setString(2,tbRadiologiRalan2.getValueAt(i,2).toString());
                                                    //pssimpanbayar.setString(3,tbRadiologiRalan2.getValueAt(i,14).toString());
                                                    //pssimpanbayar.setString(4,TNoPermintaanRalan.getText());
                                                    //pssimpanbayar.setString(5,tbRadiologiRalan2.getValueAt(i,1).toString());
                                                    //pssimpanbayar.setString(6,akunbayar);        
                                                    //pssimpanbayar.executeUpdate();

                                                //} catch (Exception e) {
                                                    //System.out.println("Notifikasi : "+e);
                                                //} finally{
                                                    //if(pssimpanbayar != null){
                                                        //pssimpanbayar.close();
                                                    //}
                                                //}
                                            //}

                                        }

                                    } 
                                    //==================================
                                    Sequel.queryu("delete from tampjurnal");  
                                    //===
                                    Sequel.menyimpan("tampjurnal","'"+RekPiutang.getText()+"','"+RekPiutangNm.getText()+"','0','"+JmlTerklaem.getText()+"'","Rekening"); 
                                    Sequel.menyimpan("tampjurnal","'"+RekBayar.getText()+"','"+RekBayarNm.getText()+"','"+JmlTerklaem.getText()+"','0'","Rekening"); 
                                    jur.simpanJurnal(TNoPermintaanRalan.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),"U","BAYAR PIUTANG KLAEM RALAN"+", OLEH "+akses.getkode());                   
                                    //==================================

                                    cmbStatus.setSelectedItem("Sudah");
                                    tampil2();
                                    emptTeks();

                                }

                        }catch (Exception ex) {
                            System.out.println("Notifikasi : "+ex);            
                            JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Data yang sama dimasukkan sebelumnya...!");
                        } 
                    }
                    //============
            }else if(Double.parseDouble(TotalTerklaem.getText())>0){
                    //============
                    int jawab=JOptionPane.showConfirmDialog(null, "Eeiiiiiits, udah bener belum data yang mau diklaem..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
                    if(jawab==JOptionPane.YES_OPTION){
                        try {                                             
                                akunbayar=Sequel.cariIsi("select kd_rek from akun_bayar where nama_bayar=?",nama_bayar.getSelectedItem().toString());
                                if(Sequel.mengedittf("c_klaim","no_klaim=?","status_klaim=?,rek_bayar=?,jml_terklaim=jml_terklaim+? ",4,new String[]{
                                                "Pending",akunbayar,JmlTerklaem.getText(),TNoPermintaanRalan.getText()
                                })==true){
                                    //================                             
                                    for(i=0;i<tbRadiologiRalan2.getRowCount();i++){ 
                                        if(tbRadiologiRalan2.getValueAt(i,16).toString().equals(TNoPermintaanRalan.getText())){                                    
                                            Sequel.mengedittf("c_billing_klaim","no_klaim=? ","status_klaim='Pending' ",1,new String[]{
                                                TNoPermintaanRalan.getText()
                                            });
                                            //==================================
                                            Sequel.mengedittf("c_klaim_detail","no_klaim=?","status_klaim='Pending' ",1,new String[]{
                                                TNoPermintaanRalan.getText()
                                            });
                                            //==================================
                                            //if(Sequel.cariInteger("select count(catatan) from bayar_piutang where catatan=?",TNoPermintaanRalan.getText())>0){
                                                //Sequel.mengedittf("piutang_pasien","no_rawat=?","status='Lunas',no_klaim=? ",2,new String[]{
                                                    //TNoPermintaanRalan.getText(),tbRadiologiRalan2.getValueAt(i,1).toString()
                                                //});
                                                //==================================
                                                //pssimpanbayar=koneksi.prepareStatement("insert into bayar_piutang values(?,?,?,?,?,?)");
                                                //try {
                                                    //pssimpanbayar.setString(1,Valid.SetTgl(Tanggal.getSelectedItem()+""));
                                                    //pssimpanbayar.setString(2,tbRadiologiRalan2.getValueAt(i,2).toString());
                                                    //pssimpanbayar.setString(3,tbRadiologiRalan2.getValueAt(i,14).toString());
                                                    //pssimpanbayar.setString(4,TNoPermintaanRalan.getText());
                                                    //pssimpanbayar.setString(5,tbRadiologiRalan2.getValueAt(i,1).toString());
                                                    //pssimpanbayar.setString(6,akunbayar);        
                                                    //pssimpanbayar.executeUpdate();

                                                //} catch (Exception e) {
                                                    //System.out.println("Notifikasi : "+e);
                                                //} finally{
                                                    //if(pssimpanbayar != null){
                                                        //pssimpanbayar.close();
                                                    //}
                                                //}
                                            //}

                                        }

                                    } 
                                    //==================================
                                        Sequel.queryu("delete from tampjurnal");  
                                        //===
                                        Sequel.menyimpan("tampjurnal","'"+RekPiutang.getText()+"','"+RekPiutangNm.getText()+"','0','"+JmlTerklaem.getText()+"'","Rekening"); 
                                        Sequel.menyimpan("tampjurnal","'"+RekBayar.getText()+"','"+RekBayarNm.getText()+"','"+JmlTerklaem.getText()+"','0'","Rekening"); 
                                        jur.simpanJurnal(TNoPermintaanRalan.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),"U","BAYAR PIUTANG KLAEM RALAN"+", OLEH "+akses.getkode());                   
                                    //==================================
                                    cmbStatus.setSelectedItem("Pending");
                                    tampil2();
                                    emptTeks();

                                }

                        }catch (Exception ex) {
                            System.out.println("Notifikasi : "+ex);            
                            JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Data yang sama dimasukkan sebelumnya...!");
                        } 
                    }
                    //============
            }  
            
        }else{    
            JOptionPane.showMessageDialog(null,"Maaf, Gagal menyimpan. No.Klaem sudah terjurnal...Silahkan hapus jurnal terlebih dahulu ");
        }
    }//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSimpanKeyPressed

    private void nama_bayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nama_bayarKeyPressed
        Valid.pindah(evt,Tanggal,TCari);
    }//GEN-LAST:event_nama_bayarKeyPressed

    private void cmbStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbStatusItemStateChanged

    }//GEN-LAST:event_cmbStatusItemStateChanged

    private void JmlTerklaemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JmlTerklaemKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
        }
    }//GEN-LAST:event_JmlTerklaemKeyPressed

    private void JmlKurangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JmlKurangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JmlKurangKeyPressed

    private void JmlTerklaem2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JmlTerklaem2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JmlTerklaem2KeyPressed

    private void TotalTerklaemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TotalTerklaemKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalTerklaemKeyPressed

    private void TotalTerklaem2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TotalTerklaem2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalTerklaem2KeyPressed

    private void SudahTerklaemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SudahTerklaemKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SudahTerklaemKeyPressed

    private void MnHapusJurnalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnHapusJurnalActionPerformed
        if(TNoPermintaanRalan.getText().trim().equals("")){
            Valid.textKosong(TNoPermintaanRalan,"No.Klaem");
            BtnCariActionPerformed(null);
        }else{
            int jawab=JOptionPane.showConfirmDialog(null, "Eeiiiiiits, udah bener belum data yang mau diklaem..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                try {
                    //koneksi.setAutoCommit(false);
                    //ceksukses=false;
                    //akunbayar=Sequel.cariIsi("select kd_rek from akun_bayar where nama_bayar=?",nama_bayar.getSelectedItem().toString());
                    //if(Sequel.mengedittf("c_klaim","no_klaim=?","status_klaim=?,rek_bayar=? ",3,new String[]{
                        //"Proses","-",TNoPermintaanRalan.getText()
                        //})==true){
                //================
                //for(i=0;i<tbRadiologiRalan2.getRowCount();i++){
                    //if(tbRadiologiRalan2.getValueAt(i,16).toString().equals(TNoPermintaanRalan.getText())){
                        Sequel.mengedittf("c_klaim","no_klaim=?","status_klaim=?,rek_bayar=?,jml_terklaim=? ",4,new String[]{
                            "Proses","-","0",TNoPermintaanRalan.getText()
                        });

                        Sequel.mengedittf("c_billing_klaim","no_klaim=? ","status_klaim='Proses' ",1,new String[]{
                            TNoPermintaanRalan.getText()
                        });
                        //==================================

                        Sequel.mengedittf("c_klaim_detail","no_klaim=?","status_klaim='Proses' ",1,new String[]{
                            TNoPermintaanRalan.getText()
                        });
                        //==================================
                        Sequel.mengedittf("piutang_pasien","no_klaim=?","status='Belum Lunas' ",1,new String[]{
                            TNoPermintaanRalan.getText()
                        });
                        //==================================
                        Sequel.queryu2("delete from bayar_piutang where catatan=?",1,new String[]{
                            TNoPermintaanRalan.getText()
                        });
                        //==================================

                        //}

                    //}

                //==================================
                if(Double.parseDouble(Discount.getText())==0){
                    Sequel.queryu("delete from tampjurnal");
                    Sequel.menyimpan("tampjurnal","'"+RekPiutang.getText()+"','"+RekPiutangNm.getText()+"','"+SudahTerklaem.getText()+"','0'","Rekening");
                    Sequel.menyimpan("tampjurnal","'"+RekBayar.getText()+"'  ,'"+RekBayarNm.getText()+"'  ,'0','"+SudahTerklaem.getText()+"'","Rekening");
                    jur.simpanJurnal(TCari.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),"U","PEMBATALAN BAYAR PIUTANG KLAEM RALAN"+", OLEH "+akses.getkode());
                }else if(Double.parseDouble(Discount.getText())>0){
                    Sequel.queryu("delete from tampjurnal");
                    //===
                    //Sequel.menyimpan("tampjurnal","'"+RekPiutang.getText()+"','"+RekPiutangNm.getText()+"','"+SudahTerklaem.getText()+"','0'","Rekening");
                    //Sequel.menyimpan("tampjurnal","'"+RekBayar.getText()+"','"+RekBayarNm.getText()+"','0','"+Double.toString(Double.parseDouble(SudahTerklaem.getText())-Double.parseDouble(Discount.getText()))+"'","Rekening");
                    //Sequel.menyimpan("tampjurnal","'540102','POTONGAN BIAYA PASIEN RAWAT JALAN','0','"+Discount.getText()+"'","Rekening");
                    Sequel.menyimpan("tampjurnal","'"+RekPiutang.getText()+"','"+RekPiutangNm.getText()+"','"+Double.toString(Double.parseDouble(SudahTerklaem.getText())+Double.parseDouble(Discount.getText()))+"','0'","Rekening");
                    Sequel.menyimpan("tampjurnal","'"+RekBayar.getText()+"','"+RekBayarNm.getText()+"','0','"+SudahTerklaem.getText()+"'","Rekening");
                    Sequel.menyimpan("tampjurnal","'540102','POTONGAN BIAYA PASIEN RAWAT JALAN','0','"+Discount.getText()+"'","Rekening");
                    jur.simpanJurnal(TNoPermintaanRalan.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),"U","PEMBATALAN BAYAR PIUTANG KLAEM RALAN"+", OLEH "+akses.getkode());
                }
                //TNoPermintaan2.setText(TNoPermintaanRalan.getText());
                cmbStatus.setSelectedItem("Proses");
                tampil2();
                emptTeks();

                //}
            //koneksi.setAutoCommit(true);

        }catch (Exception ex) {
            System.out.println("Notifikasi : "+ex);
            JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Data yang sama dimasukkan sebelumnya...!");
        }
        }
        //=======
        }

    }//GEN-LAST:event_MnHapusJurnalActionPerformed

    private void MnHapusDataSalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnHapusDataSalahActionPerformed
        if(TNoPermintaanRalan.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\nKlik data pada Tab Data Klaem untuk memilih...!!!!");
        }else if(jLabel13.getText().trim().equals("Proses")){
            Sequel.mengedit("piutang_pasien","no_klaim='"+TNoPermintaanRalan.getText()+"'","no_klaim='-'");

            Sequel.queryu2("delete from c_klaim where no_klaim=?",1,new String[]{
                TCari.getText()
            });

            Sequel.queryu2("delete from c_klaim_detail where no_klaim=?",1,new String[]{
                TCari.getText()
            });

            Sequel.mengedittf("c_billing_klaim","no_klaim=? ","status_klaim='Belum',no_klaim='-'",1,new String[]{
                TNoPermintaanRalan.getText()
            });

            emptTeks();
            BtnCariActionPerformed(null);
        }else{
            JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. No.Klaem sudah terjurnal...Silahkan hapus jurnal terlebih dahulu ");
        }
    }//GEN-LAST:event_MnHapusDataSalahActionPerformed

    private void KdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdKeyPressed
        Valid.pindah(evt,TCari,Nm);
    }//GEN-LAST:event_KdKeyPressed

    private void NmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NmKeyPressed
        Valid.pindah(evt,Kd,TCari);
    }//GEN-LAST:event_NmKeyPressed

    private void BtnUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUnitActionPerformed
        //akses.setform("DlgKlaemAsuransiDetail");
        poli.isCek();
        poli.tampil();
        poli.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        poli.setLocationRelativeTo(internalFrame1);
        poli.setVisible(true);
    }//GEN-LAST:event_BtnUnitActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgCariKlaemRalan dialog = new DlgCariKlaemRalan(new javax.swing.JFrame(), true);
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
    private widget.Button BtnCari;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.Button BtnUnit;
    private widget.TextBox Discount;
    private widget.TextBox Header;
    private widget.TextBox JmlKurang;
    private widget.TextBox JmlTagihan2;
    private widget.TextBox JmlTerklaem;
    private widget.TextBox JmlTerklaem2;
    private widget.TextBox Kd;
    private widget.TextBox KdDept;
    private widget.Label LCount;
    private javax.swing.JMenuItem MnHapusDataSalah;
    private javax.swing.JMenuItem MnHapusJurnal;
    private widget.TextBox Nm;
    private widget.TextBox Pengeluaran;
    private widget.TextBox Pengeluaran2;
    private widget.TextBox RekBayar;
    private widget.TextBox RekBayarNm;
    private widget.TextBox RekPiutang;
    private widget.TextBox RekPiutangNm;
    private widget.TextBox StatusPengeluaran;
    private widget.TextBox SudahTerklaem;
    private widget.TextBox TCari;
    private widget.TextBox TNoPermintaanRalan;
    private javax.swing.JTabbedPane TabRawat;
    private widget.Tanggal Tanggal;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private widget.TextBox Total;
    private widget.TextBox TotalTagihan2;
    private widget.TextBox TotalTerklaem;
    private widget.TextBox TotalTerklaem2;
    private widget.ComboBox cmbStatus;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator6;
    private widget.Label label10;
    private widget.Label label11;
    private widget.Label label14;
    private widget.Label label17;
    private widget.Label label18;
    private widget.Label label19;
    private widget.Label label20;
    private widget.Label label21;
    private widget.Label label22;
    private widget.Label label23;
    private widget.Label label24;
    private widget.Label label34;
    private widget.ComboBox nama_bayar;
    private widget.panelisi panelGlass10;
    private widget.panelisi panelGlass11;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.panelisi panelisi1;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.Table tbRadiologiRalan;
    private widget.Table tbRadiologiRalan2;
    private widget.TextBox terbilang;
    // End of variables declaration//GEN-END:variables
//"No.Klaem","Tanggal","Jam","Perusahaan","Petugas","Total","Kode.Perusahaan","Kode.Petugas"
    private void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            ps=koneksi.prepareStatement(
                    "select c_klaim.no_klaim, c_klaim.tanggal, c_klaim.jam, c_group_perusahaan.nm_perusahaan, c_klaim.total_klaim, "+
                    "c_klaim.kd_perusahaan, c_klaim.petugas, c_klaim.rek_piutang, c_klaim.rek_bayar, c_klaim.jml_piutang, c_klaim.discount, c_klaim.status_klaim, c_klaim.jml_terklaim, "+
                    "c_klaim.total_klaim-c_klaim.jml_terklaim as piutang "+                  
                    "from c_klaim inner join c_group_perusahaan on "+
                    "c_group_perusahaan.kd_perusahaan=c_klaim.kd_perusahaan where "+
                    "c_klaim.status_rawat='Ralan' and c_klaim.tanggal between ? and ? and c_klaim.status_klaim like ? and c_klaim.no_klaim like ? or "+
                    "c_klaim.status_rawat='Ralan' and c_klaim.tanggal between ? and ? and c_klaim.status_klaim like ? and c_group_perusahaan.nm_perusahaan like ? order by c_klaim.no_klaim ");   
            try {
                ps.setString(1,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(3,"%"+cmbStatus.getSelectedItem().toString().replaceAll("Semua","")+"%");
                ps.setString(4,"%"+TCari.getText()+"%");
                ps.setString(5,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(6,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(7,"%"+cmbStatus.getSelectedItem().toString().replaceAll("Semua","")+"%");
                ps.setString(8,"%"+TCari.getText()+"%");
                y=0;               
                rs=ps.executeQuery();              
                while(rs.next()){
                    userpetugas="";
                    //userpetugas=rs.getString("petugas");
                    if(rs.getString("petugas").trim().equals("Admin Utama")){
                        userpetugas="Admin Utama";
                    }else{
                        userpetugas=Sequel.cariIsi("select petugas.nama from petugas where petugas.nip=?",rs.getString("petugas")); 
                    }
                    
                    
                    tabMode.addRow(new String[]{                   
                        rs.getString("no_klaim"),rs.getString("tanggal"),rs.getString("jam"),rs.getString("nm_perusahaan"),
                        userpetugas,rs.getString("total_klaim"),rs.getString("kd_perusahaan"),
                        rs.getString("petugas"),rs.getString("rek_piutang"),rs.getString("rek_bayar"),
                        rs.getString("jml_piutang"),rs.getString("discount"),rs.getString("status_klaim"),
                        rs.getString("jml_terklaim"),rs.getString("piutang")
                    });                  
                    
                    
                    y=y+rs.getDouble("total_klaim");
                    Total.setText(rs.getString("total_klaim"));
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
            //if(y>0){
                //tabMode.addRow(new Object[]{
                    //"","","","","","","",""                                                       
                //});
                //tabMode.addRow(new Object[]{
                    //"Total :","","","","",df5.format(y),"",""                                                  
                //});
            //}
            
            //LTotal.setText(df5.format(y));
            LCount.setText(""+tabMode.getRowCount());
            
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }        
    }
    
    //"No.Klaem","Tanggal","Jam","Perusahaan","Petugas","Total","Kode.Perusahaan","Kode.Petugas"
    private void tampil2() {
        Valid.tabelKosong(tabMode2);
        try {
            ps=koneksi.prepareStatement(" select c_klaim_detail.no_rawat,c_klaim_detail.no_rkm_medis,c_klaim_detail.nm_pasien,c_klaim_detail.nm_dokter,c_klaim_detail.perusahaan, "+ 
                " c_klaim_detail.konsultasi, c_klaim_detail.tindakan, c_klaim_detail.lab, c_klaim_detail.rad, c_klaim_detail.alat_radiologi, c_klaim_detail.obat, c_klaim_detail.jml_kwitansi, c_klaim_detail.jml_bayar, c_klaim_detail.jml_klaim, "+
                " c_klaim_detail.status_klaim, c_klaim_detail.no_klaim, c_klaim_detail.tanggal_klaem "+             
                " from c_klaim_detail inner join c_klaim on c_klaim_detail.no_klaim=c_klaim.no_klaim  where  "+
                " c_klaim.status_rawat='Ralan' and c_klaim.status_klaim like ? and c_klaim_detail.tanggal_klaem between ? and ? and c_klaim_detail.perusahaan like ? and c_klaim_detail.nm_pasien like ? or "+
                " c_klaim.status_rawat='Ralan' and c_klaim.status_klaim like ? and c_klaim_detail.tanggal_klaem between ? and ? and c_klaim_detail.perusahaan like ? and c_klaim_detail.no_rawat like ? or "+
                " c_klaim.status_rawat='Ralan' and c_klaim.status_klaim like ? and c_klaim_detail.tanggal_klaem between ? and ? and c_klaim_detail.perusahaan like ? and c_klaim_detail.no_klaim like ? or "+    
                " c_klaim.status_rawat='Ralan' and c_klaim.status_klaim like ? and c_klaim_detail.tanggal_klaem between ? and ? and c_klaim_detail.perusahaan like ? and c_klaim_detail.no_rkm_medis like ? ");      
            try{ 
                ps.setString(1,"%"+cmbStatus.getSelectedItem().toString().replaceAll("Semua","")+"%");
                ps.setString(2,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(3,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(4,"%"+Nm.getText().trim()+"%");
                ps.setString(5,"%"+TCari.getText().trim()+"%");
                ps.setString(6,"%"+cmbStatus.getSelectedItem().toString().replaceAll("Semua","")+"%");
                ps.setString(7,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(8,Valid.SetTgl(Tgl2.getSelectedItem()+""));  
                ps.setString(9,"%"+Nm.getText().trim()+"%");
                ps.setString(10,"%"+TCari.getText().trim()+"%");
                ps.setString(11,"%"+cmbStatus.getSelectedItem().toString().replaceAll("Semua","")+"%");
                ps.setString(12,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(13,Valid.SetTgl(Tgl2.getSelectedItem()+"")); 
                ps.setString(14,"%"+Nm.getText().trim()+"%");
                ps.setString(15,"%"+TCari.getText().trim()+"%");
                ps.setString(16,"%"+cmbStatus.getSelectedItem().toString().replaceAll("Semua","")+"%");
                ps.setString(17,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(18,Valid.SetTgl(Tgl2.getSelectedItem()+""));  
                ps.setString(19,"%"+Nm.getText().trim()+"%");
                ps.setString(20,"%"+TCari.getText().trim()+"%");
                               
                rs=ps.executeQuery();
                i=1;
                ADMdr=0;
                ADM=0;
                Obat=0;
                while(rs.next()){
                    //ADMdr=Sequel.cariIsiAngka("select c_group_perusahaan.obat from c_group_billing inner join c_group_perusahaan inner join penjab inner join reg_periksa on penjab.kd_perusahaan=c_group_perusahaan.kd_perusahaan and reg_periksa.no_rawat=c_group_billing.no_rawat where reg_periksa.no_rawat='"+rs.getString(1)+"' and reg_periksa.kd_pj='"+rs.getString(18)+"' ");
                    //ADM=ADM+rs.getDouble(9);
                    //Obat= Obat+ADM-((ADMdr*rs.getDouble(9))/100);
                    tabMode2.addRow(new Object[]{i,
                                   rs.getString(1),
                                   rs.getString(2),
                                   rs.getString(3),
                                   rs.getString(4),
                                   rs.getString(5),
                                   rs.getDouble(6),
                                   rs.getDouble(7),                                   
                                   rs.getDouble(8),
                                   rs.getDouble(9),
                                   rs.getString(10),
                                   rs.getDouble(11),
                                   rs.getDouble(12),
                                   rs.getDouble(13),
                                   rs.getDouble(14),
                                   rs.getString(15),
                                   rs.getString(16),
                                   rs.getString(17)});
                                   
                    
                    i++;
                }
            }catch(SQLException e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if( rs != null){
                    rs.close();
                }
                
                if( ps != null){
                    ps.close();
                }
            }
            
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    
     //"No.Klaem","Tanggal","Jam","Perusahaan","Petugas",
     //"Total Tagihan","Kode.Perusahaan","Kode.Petugas","Rek Piutang","Rek Bayar" ,
     //"Jml.Piutang","Discount"
    private void getData() {
        TNoPermintaanRalan.setText("");
        if(tbRadiologiRalan.getSelectedRow()!= -1){
            TNoPermintaanRalan.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),0).toString());
            TCari.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),0).toString()); 
            TotalTagihan2.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),5).toString());   
            RekPiutang.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),8).toString());
            RekBayar.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),9).toString()); 
            JmlTagihan2.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),10).toString());  
            Discount.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),11).toString()); 
            jLabel13.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),12).toString()); 
            SudahTerklaem.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),13).toString()); 
            JmlKurang.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),14).toString()); 
            //nominal();
            RekPiutangNm.setText(Sequel.cariIsi("select nm_rek from rekening where kd_rek=?",tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),8).toString()));
            RekBayarNm.setText(Sequel.cariIsi("select nm_rek from rekening where kd_rek=?",tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),9).toString()));
        }
    }
    
    public void isCek(){
        //MnHMnKlaemtEnabled(akses.getpermintaan_radiologi());      
        //BtnHapus.setEnabled(akses.getpermintaan_radiologi());
        //BtnPrint.setEnabled(akses.getpermintaan_radiologi());
        BtnPrint.setVisible(false);
    }
    
    public void setPasien(String pasien){
        TCari.setText(pasien);
    }     
    
    private void nominal(){
        satuan=0;
        satuan=satuan+Integer.parseInt(Pengeluaran.getText());
        terbilang.setText(konversiAngka.angka(satuan)+" Rupiah");
    }

    public void emptTeks() {
        TCari.setText("");
        TNoPermintaanRalan.setText("");
        JmlTagihan2.setText("");
        Discount.setText("");
        TotalTagihan2.setText("");
        jLabel13.setText("");
        JmlTerklaem.setText("");
        TotalTerklaem.setText("");
    }
    
    private void isjml(){
        TotalTerklaem.setText("0");
        //TotalTagihan2.setText("0");
        if((!TotalTagihan2.getText().equals(""))&&(!JmlKurang.getText().equals(""))&&(!JmlTerklaem.getText().equals(""))){
            TotalTerklaem.setText(Double.toString(Double.parseDouble(JmlKurang.getText())-Double.parseDouble(JmlTerklaem.getText())));
            TotalTerklaem2.setText(Double.toString(Double.parseDouble(JmlKurang.getText())-Double.parseDouble(JmlTerklaem.getText())));
            //TotalTerklaem2.setText(Double.toString(Double.parseDouble(TotalTagihan2.getText())-(Double.parseDouble(JmlTerklaem.getText())+Double.parseDouble(JmlKurang.getText()))));
        }
        
    }
    
    
 
}
