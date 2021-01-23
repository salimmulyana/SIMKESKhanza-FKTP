/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgPenyakit.java
 *
 * Created on May 23, 2010, 12:57:16 AM
 */

package custom;

import simrskhanza.*;
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import restore.DlgRestorePoli;

/**
 *
 * @author dosen
 */
public final class DlgJunalPelayanan extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabMode2;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private DlgCariGroupPerusahaan poli=new DlgCariGroupPerusahaan(null,false);   
    private int i=0,jmlklaim=0,z=0;
    private boolean ceksukses=false;
    private PreparedStatement stat,ps,ps2,ps3;
    private ResultSet rs,rs2,rs3;
    private String status="",kmr="",key="";
    private double stokbarang=0,ttltarif=0,ttltagihan=0,ttlselisih=0,y=0;
    private double DokterUmum=0,DokterUmumDr=0,DokterUmumDrPr=0,DokterSpesialis=0,DokterSpesialisDr=0,DokterSpesialisDrPr=0,
                   Obat=0,Lab=0,Lab1=0,Lab2=0,Rad=0,ADM=0,ADMdr=0,ADMpr=0,ADMdrpr=0,Lain=0,Tagihan=0,UangMuka=0;
    private final DecimalFormat df5 = new DecimalFormat("###,###,###,###,###,###,###.##");  
    /** Creates new form DlgPenyakit
     * @param parent
     * @param modal */
    public DlgJunalPelayanan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(10,2);
        setSize(628,674);
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.Nota","Nama Pasien","Perusahaan","No.Kartu",
            "Tgl.Kwitansi","DU","DS","Obat","LAB/RAD","ADM",
            "Lain-Lain","JML.Kwitansi","Di.Bayar Pasien","Total Diajukan"           
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                    boolean a = false;
                    if (colIndex==0) {
                        a=true;
                    }
                    return a;
              }
              Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, 
                 java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, 
                 java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRalan.setModel(tabMode);
        
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbRalan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRalan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 16; i++) {
            TableColumn column = tbRalan.getColumnModel().getColumn(i);
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
                column.setPreferredWidth(90);
            }
        }
        tbRalan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabMode2=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.MR","Nama Pasien","Perusahaan","No.Kartu","Tgl.Kwitansi",
            "Registrasi","Tindakan","Obt+Emb+Tsl","Retur Obat","Resep Pulang","Laborat",
            "Radiologi","Potongan","Tambahan","Kamar","Operasi","Harian","JML.Kwitansi","Deposit","Total.Diajukan"
            
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                    boolean a = false;
                    if (colIndex==0) {
                        a=true;
                    }
                    return a;
              }
              Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, 
                 java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, 
                 java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                 java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                 java.lang.Double.class, java.lang.Double.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRanap.setModel(tabMode2);              
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbRanap.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRanap.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 22; i++) {
            TableColumn column = tbRanap.getColumnModel().getColumn(i);
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
                column.setPreferredWidth(70);
            }else if(i==14){
                column.setPreferredWidth(70);
            }else if(i==15){
                column.setPreferredWidth(70);
            }else if(i==16){
                column.setPreferredWidth(70);
            }else if(i==17){
                column.setPreferredWidth(70);
            }else if(i==18){
                column.setPreferredWidth(70);
            }else if(i==19){
                column.setPreferredWidth(70);
            }else if(i==20){
                column.setPreferredWidth(70);
            }else if(i==21){
                column.setPreferredWidth(70);
            }
        }
        tbRanap.setDefaultRenderer(Object.class, new WarnaTable());              
                
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
                        Kd.requestFocus();                      
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
        
        
        jam();
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TNoRw2 = new widget.TextBox();
        buttonGroup1 = new javax.swing.ButtonGroup();
        PopupRalan = new javax.swing.JPopupMenu();
        ppBersihkanRalan = new javax.swing.JMenuItem();
        ppSemuaRalan = new javax.swing.JMenuItem();
        PopupRanap = new javax.swing.JPopupMenu();
        ppBersihkanRanap = new javax.swing.JMenuItem();
        ppSemuaRanap = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        jPanel2 = new javax.swing.JPanel();
        panelisi4 = new widget.panelisi();
        jLabel4 = new widget.Label();
        TNoPermintaan = new widget.TextBox();
        label34 = new widget.Label();
        Kd = new widget.TextBox();
        Nm = new widget.TextBox();
        BtnUnit = new widget.Button();
        jLabel15 = new widget.Label();
        Tanggal = new widget.Tanggal();
        CmbJam = new widget.ComboBox();
        CmbMenit = new widget.ComboBox();
        CmbDetik = new widget.ComboBox();
        ChkJln = new widget.CekBox();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame8 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbRalan = new widget.Table();
        internalFrame9 = new widget.InternalFrame();
        internalFrame7 = new widget.InternalFrame();
        Scroll1 = new widget.ScrollPane();
        tbRanap = new widget.Table();
        jPanel1 = new javax.swing.JPanel();
        panelisi3 = new widget.panelisi();
        label9 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        BtnSimpan = new widget.Button();
        panelisi1 = new widget.panelisi();
        label11 = new widget.Label();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        Tgl2 = new widget.Tanggal();
        label10 = new widget.Label();
        LCount = new widget.Label();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();

        TNoRw2.setHighlighter(null);
        TNoRw2.setName("TNoRw2"); // NOI18N
        TNoRw2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw2KeyPressed(evt);
            }
        });

        PopupRalan.setBackground(new java.awt.Color(255, 255, 254));
        PopupRalan.setName("PopupRalan"); // NOI18N

        ppBersihkanRalan.setBackground(new java.awt.Color(255, 255, 254));
        ppBersihkanRalan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkanRalan.setForeground(new java.awt.Color(50, 50, 50));
        ppBersihkanRalan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBersihkanRalan.setText("Bersihkan Pilihan");
        ppBersihkanRalan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkanRalan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkanRalan.setName("ppBersihkanRalan"); // NOI18N
        ppBersihkanRalan.setPreferredSize(new java.awt.Dimension(160, 25));
        ppBersihkanRalan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBersihkanRalanActionPerformed(evt);
            }
        });
        PopupRalan.add(ppBersihkanRalan);

        ppSemuaRalan.setBackground(new java.awt.Color(255, 255, 254));
        ppSemuaRalan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppSemuaRalan.setForeground(new java.awt.Color(50, 50, 50));
        ppSemuaRalan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppSemuaRalan.setText("Pilih Semua");
        ppSemuaRalan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppSemuaRalan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppSemuaRalan.setName("ppSemuaRalan"); // NOI18N
        ppSemuaRalan.setPreferredSize(new java.awt.Dimension(160, 25));
        ppSemuaRalan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppSemuaRalanActionPerformed(evt);
            }
        });
        PopupRalan.add(ppSemuaRalan);

        PopupRanap.setBackground(new java.awt.Color(255, 255, 254));
        PopupRanap.setName("PopupRanap"); // NOI18N

        ppBersihkanRanap.setBackground(new java.awt.Color(255, 255, 254));
        ppBersihkanRanap.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkanRanap.setForeground(new java.awt.Color(50, 50, 50));
        ppBersihkanRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBersihkanRanap.setText("Bersihkan Pilihan");
        ppBersihkanRanap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkanRanap.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkanRanap.setName("ppBersihkanRanap"); // NOI18N
        ppBersihkanRanap.setPreferredSize(new java.awt.Dimension(160, 25));
        ppBersihkanRanap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBersihkanRanapActionPerformed(evt);
            }
        });
        PopupRanap.add(ppBersihkanRanap);

        ppSemuaRanap.setBackground(new java.awt.Color(255, 255, 254));
        ppSemuaRanap.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppSemuaRanap.setForeground(new java.awt.Color(50, 50, 50));
        ppSemuaRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppSemuaRanap.setText("Pilih Semua");
        ppSemuaRanap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppSemuaRanap.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppSemuaRanap.setName("ppSemuaRanap"); // NOI18N
        ppSemuaRanap.setPreferredSize(new java.awt.Dimension(160, 25));
        ppSemuaRanap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppSemuaRanapActionPerformed(evt);
            }
        });
        PopupRanap.add(ppSemuaRanap);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Klaem Asuransi - Rincian Tagihan ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(816, 45));
        jPanel2.setLayout(new java.awt.BorderLayout(1, 1));

        panelisi4.setName("panelisi4"); // NOI18N
        panelisi4.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        jLabel4.setText("No.Permintaan :");
        jLabel4.setName("jLabel4"); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(85, 14));
        panelisi4.add(jLabel4);

        TNoPermintaan.setHighlighter(null);
        TNoPermintaan.setName("TNoPermintaan"); // NOI18N
        TNoPermintaan.setPreferredSize(new java.awt.Dimension(150, 24));
        TNoPermintaan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoPermintaanKeyPressed(evt);
            }
        });
        panelisi4.add(TNoPermintaan);

        label34.setText("Perusahaan :");
        label34.setName("label34"); // NOI18N
        label34.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi4.add(label34);

        Kd.setHighlighter(null);
        Kd.setName("Kd"); // NOI18N
        Kd.setPreferredSize(new java.awt.Dimension(80, 24));
        Kd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdKeyPressed(evt);
            }
        });
        panelisi4.add(Kd);

        Nm.setHighlighter(null);
        Nm.setName("Nm"); // NOI18N
        Nm.setPreferredSize(new java.awt.Dimension(200, 24));
        Nm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NmKeyPressed(evt);
            }
        });
        panelisi4.add(Nm);

        BtnUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnUnit.setMnemonic('4');
        BtnUnit.setToolTipText("ALt+4");
        BtnUnit.setName("BtnUnit"); // NOI18N
        BtnUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUnitActionPerformed(evt);
            }
        });
        panelisi4.add(BtnUnit);

        jLabel15.setText("Tgl.Periksa :");
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(80, 14));
        panelisi4.add(jLabel15);

        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "08-03-2020" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        panelisi4.add(Tanggal);

        CmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        CmbJam.setName("CmbJam"); // NOI18N
        panelisi4.add(CmbJam);

        CmbMenit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbMenit.setName("CmbMenit"); // NOI18N
        panelisi4.add(CmbMenit);

        CmbDetik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbDetik.setName("CmbDetik"); // NOI18N
        panelisi4.add(CmbDetik);

        ChkJln.setBorder(null);
        ChkJln.setSelected(true);
        ChkJln.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkJln.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkJln.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkJln.setName("ChkJln"); // NOI18N
        ChkJln.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkJlnActionPerformed(evt);
            }
        });
        panelisi4.add(ChkJln);

        jPanel2.add(panelisi4, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame8.setBorder(null);
        internalFrame8.setName("internalFrame8"); // NOI18N
        internalFrame8.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbRalan.setAutoCreateRowSorter(true);
        tbRalan.setComponentPopupMenu(PopupRalan);
        tbRalan.setName("tbRalan"); // NOI18N
        tbRalan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRalanMouseClicked(evt);
            }
        });
        tbRalan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbRalanKeyReleased(evt);
            }
        });
        Scroll.setViewportView(tbRalan);

        internalFrame8.add(Scroll, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Ralan", internalFrame8);

        internalFrame9.setBorder(null);
        internalFrame9.setForeground(new java.awt.Color(50, 50, 50));
        internalFrame9.setName("internalFrame9"); // NOI18N
        internalFrame9.setLayout(new java.awt.BorderLayout(1, 1));

        internalFrame7.setBorder(null);
        internalFrame7.setName("internalFrame7"); // NOI18N
        internalFrame7.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);

        tbRanap.setAutoCreateRowSorter(true);
        tbRanap.setComponentPopupMenu(PopupRanap);
        tbRanap.setName("tbRanap"); // NOI18N
        tbRanap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRanapMouseClicked(evt);
            }
        });
        tbRanap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbRanapKeyReleased(evt);
            }
        });
        Scroll1.setViewportView(tbRanap);

        internalFrame7.add(Scroll1, java.awt.BorderLayout.CENTER);

        internalFrame9.add(internalFrame7, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Ranap", internalFrame9);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(816, 100));
        jPanel1.setLayout(new java.awt.BorderLayout(1, 1));

        panelisi3.setName("panelisi3"); // NOI18N
        panelisi3.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        label9.setText("Key Word :");
        label9.setName("label9"); // NOI18N
        label9.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi3.add(label9);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(350, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelisi3.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('1');
        BtnCari.setToolTipText("Alt+1");
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
        panelisi3.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('2');
        BtnAll.setToolTipText("Alt+2");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
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
        panelisi3.add(BtnAll);

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
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
        panelisi3.add(BtnSimpan);

        jPanel1.add(panelisi3, java.awt.BorderLayout.PAGE_START);

        panelisi1.setName("panelisi1"); // NOI18N
        panelisi1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(50, 23));
        panelisi1.add(label11);

        Tgl1.setDisplayFormat("dd-MM-yyyy");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelisi1.add(Tgl1);

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("s.d.");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(25, 23));
        panelisi1.add(label18);

        Tgl2.setDisplayFormat("dd-MM-yyyy");
        Tgl2.setName("Tgl2"); // NOI18N
        Tgl2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelisi1.add(Tgl2);

        label10.setText("Record :");
        label10.setName("label10"); // NOI18N
        label10.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi1.add(label10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(60, 23));
        panelisi1.add(LCount);

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

        jPanel1.add(panelisi1, java.awt.BorderLayout.CENTER);

        internalFrame1.add(jPanel1, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        BtnCariActionPerformed(evt);
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnKeluar.requestFocus();
        }else if(tabMode.getRowCount()!=0){     
                    Map<String, Object> param = new HashMap<>();  
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());   
                    param.put("logo",Sequel.cariGambar("select logo from setting"));        
            Valid.MyReportqry("rptPoli.jasper","report","::[ Data Unit ]::","select kd_poli, nm_poli, registrasi, registrasilama "+
                " from poliklinik where status='1' and kd_poli like '%"+TCari.getText().trim()+"%' or "+
                " status='1' and nm_poli like '%"+TCari.getText().trim()+"%' order by kd_poli",param);
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnKeluar, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

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
        TabRawatMouseClicked(null);
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        TabRawatMouseClicked(null);
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCari, TCari);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbRalanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRalanMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbRalanMouseClicked

    private void TNoRw2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw2KeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void tbRalanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRalanKeyReleased
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbRalanKeyReleased

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==0){
            tampil();
        }else if(TabRawat.getSelectedIndex()==1){                     
            tampilranap();         
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void tbRanapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRanapMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbRanapMouseClicked

    private void tbRanapKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRanapKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbRanapKeyReleased

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

    private void TNoPermintaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoPermintaanKeyPressed
        //Valid.pindah(evt,TNoReg,DTPReg);
    }//GEN-LAST:event_TNoPermintaanKeyPressed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt, TCari, TCari);
    }//GEN-LAST:event_TanggalKeyPressed

    private void ChkJlnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkJlnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkJlnActionPerformed

    private void ppBersihkanRalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkanRalanActionPerformed
        for(i=0;i<tbRalan.getRowCount();i++){
            tbRalan.setValueAt(false,i,0);
        }
    }//GEN-LAST:event_ppBersihkanRalanActionPerformed

    private void ppSemuaRalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppSemuaRalanActionPerformed
        for(i=0;i<tbRalan.getRowCount();i++){
            tbRalan.setValueAt(true,i,0);
        }
    }//GEN-LAST:event_ppSemuaRalanActionPerformed

    private void ppBersihkanRanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkanRanapActionPerformed
        for(i=0;i<tbRanap.getRowCount();i++){
            tbRanap.setValueAt(false,i,0);
        }
    }//GEN-LAST:event_ppBersihkanRanapActionPerformed

    private void ppSemuaRanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppSemuaRanapActionPerformed
        for(i=0;i<tbRanap.getRowCount();i++){
            tbRanap.setValueAt(true,i,0);
        }
    }//GEN-LAST:event_ppSemuaRanapActionPerformed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        int jawab=JOptionPane.showConfirmDialog(null, "Eeiiiiiits, udah bener belum data yang mau diklaem..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if(jawab==JOptionPane.YES_OPTION){
            ChkJln.setSelected(false);
            try {
                //koneksi.setAutoCommit(false);                
                    ceksukses=false; 
                    ttltarif=0; 
                    z=tbRalan.getRowCount();
                    for(i=0;i<z;i++){ 
                        if(tbRalan.getValueAt(i,0).toString().equals("true")){
                            y=0;             
                            try {                
                                y=Double.parseDouble(tbRalan.getValueAt(i,15).toString());  
                            } catch (Exception e) {
                                y=0; 
                            }
                            ttltarif=ttltarif+y;                                                             
                        }                           
                    }  
                    //"P","No.Rawat","No.MR","Nama Pasien","Perusahaan","No.Kartu",
                    //"Tgl.Kwitansi","DU","DS","Obat","LAB/RAD","ADM",
                    //"Lain-Lain","JML.Kwitansi","Di.Bayar Pasien","Total Diajukan"  
                    if(Sequel.menyimpantf2("c_klaim","?,?,?,?,?,?,?,?","No.Permintaan",8,new String[]{
                        TNoPermintaan.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),
                        CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem(), 
                        Kd.getText(),akses.getkode(),Double.toString(ttltarif),"Ralan","Proses"   
                    })==true){
                        for(i=0;i<tbRalan.getRowCount();i++){ 
                            if(tbRalan.getValueAt(i,0).toString().equals("true")){
                                Sequel.menyimpantf2("c_klaim_ralan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Tindakan dokter",18,new String[]{
                                    TNoPermintaan.getText(),tbRalan.getValueAt(i,1).toString(),tbRalan.getValueAt(i,2).toString(),tbRalan.getValueAt(i,3).toString(),
                                    tbRalan.getValueAt(i,4).toString(),tbRalan.getValueAt(i,5).toString(),tbRalan.getValueAt(i,6).toString(),
                                    Valid.SetTgl(Tanggal.getSelectedItem()+""),tbRalan.getValueAt(i,7).toString(),tbRalan.getValueAt(i,8).toString(),
                                    tbRalan.getValueAt(i,9).toString(),tbRalan.getValueAt(i,10).toString(),tbRalan.getValueAt(i,11).toString(),tbRalan.getValueAt(i,12).toString(),
                                    tbRalan.getValueAt(i,13).toString(),tbRalan.getValueAt(i,14).toString(),
                                    tbRalan.getValueAt(i,15).toString(),"Proses",
                                });
                                
                                Sequel.mengedittf("c_group_billing","no_rawat=? and status_klaim='Belum'","status_klaim='Proses'",1,new String[]{
                                    tbRalan.getValueAt(i,1).toString()
                                });
                            }
                            tbRalan.setValueAt(false,i,0);
                        } 
                        
                        emptTeks();
                    }
                   
                ChkJln.setSelected(true);
                //koneksi.setAutoCommit(true);
            
            }catch (Exception ex) {
                System.out.println("Notifikasi : "+ex);            
                JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Data yang sama dimasukkan sebelumnya...!");
            } 
        }
    }//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSimpanKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgJunalPelayanan dialog = new DlgJunalPelayanan(new javax.swing.JFrame(), true);
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
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.Button BtnUnit;
    private widget.CekBox ChkJln;
    private widget.ComboBox CmbDetik;
    private widget.ComboBox CmbJam;
    private widget.ComboBox CmbMenit;
    private widget.TextBox Kd;
    private widget.Label LCount;
    private widget.TextBox Nm;
    private javax.swing.JPopupMenu PopupRalan;
    private javax.swing.JPopupMenu PopupRanap;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.TextBox TCari;
    private widget.TextBox TNoPermintaan;
    private widget.TextBox TNoRw2;
    private javax.swing.JTabbedPane TabRawat;
    private widget.Tanggal Tanggal;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame7;
    private widget.InternalFrame internalFrame8;
    private widget.InternalFrame internalFrame9;
    private widget.Label jLabel15;
    private widget.Label jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private widget.Label label10;
    private widget.Label label11;
    private widget.Label label18;
    private widget.Label label34;
    private widget.Label label9;
    private widget.panelisi panelisi1;
    private widget.panelisi panelisi3;
    private widget.panelisi panelisi4;
    private javax.swing.JMenuItem ppBersihkanRalan;
    private javax.swing.JMenuItem ppBersihkanRanap;
    private javax.swing.JMenuItem ppSemuaRalan;
    private javax.swing.JMenuItem ppSemuaRanap;
    private widget.Table tbRalan;
    private widget.Table tbRanap;
    // End of variables declaration//GEN-END:variables
//"No.","No.Rawat","No.MR","Nama Pasien","Perusahaan","No.Kartu","Tgl.Kwitansi","DU","DS","Obat","LAB/RAD","ADM",
//"Lain-Lain","JML.Kwitansi","Di.Bayar Pasien","Total Diajukan"
    public void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            ps=koneksi.prepareStatement(" select no_rawat,no_nota,tanggal,jam, "+
                " Tindakan_Ralan,Beban_Jasa_Medik_Dokter_Tindakan_Ralan, Beban_Jasa_Medik_Paramedis_Tindakan_Ralan, "+ 
                " Beban_Jasa_Medik_Paramedis_Tindakan_Ralan, Utang_Jasa_Medik_Paramedis_Tindakan_Ralan,  "+
                " Beban_KSO_Tindakan_Ralan, Utang_KSO_Tindakan_Ralan, "+   
                " Laborat_Ralan, Beban_Jasa_Medik_Dokter_Laborat_Ralan, Utang_Jasa_Medik_Dokter_Laborat_Ralan,  "+   
                " Beban_Jasa_Medik_Petugas_Laborat_Ralan, Utang_Jasa_Medik_Petugas_Laborat_Ralan,  "+   
                " Beban_Kso_Laborat_Ralan, Utang_Kso_Laborat_Ralan,  "+   
                " HPP_Persediaan_Laborat_Rawat_Jalan, Persediaan_BHP_Laborat_Rawat_Jalan, "+   
                " Radiologi_Ralan, Beban_Jasa_Medik_Dokter_Radiologi_Ralan, Utang_Jasa_Medik_Dokter_Radiologi_Ralan, "+   
                " Beban_Jasa_Medik_Petugas_Radiologi_Ralan, Utang_Jasa_Medik_Petugas_Radiologi_Ralan, "+   
                " Beban_Kso_Radiologi_Ralan, Utang_Kso_Radiologi_Ralan, "+   
                " HPP_Persediaan_Radiologi_Rawat_Jalan, Persediaan_BHP_Radiologi_Rawat_Jalan, "+   
                " Obat_Ralan, HPP_Obat_Rawat_Jalan, Persediaan_Obat_Rawat_Jalan, "+   
                " Registrasi_Ralan, Operasi_Ralan, "+ 
                " Beban_Jasa_Medik_Dokter_Operasi_Ralan, Utang_Jasa_Medik_Dokter_Operasi_Ralan, "+   
                " HPP_Obat_Operasi_Ralan, Persediaan_Obat_Kamar_Operasi_Ralan, "+   
                " Tambahan_Ralan, Potongan_Ralan, "+  
                " no_jurnal from jurnal_nota_jalan where no_jurnal<>'-' and tanggal between ? and ?  ");      
            try{
                ps.setString(1,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(Tgl2.getSelectedItem()+""));               
                rs=ps.executeQuery();
                i=1;
                while(rs.next()){
                    tabMode.addRow(new Object[]{false,
                                   rs.getString(1),
                                   rs.getString(2),
                                   rs.getString(3),
                                   rs.getString(4),
                                   rs.getString(5),
                                   rs.getString(6),
                                   rs.getDouble(7),                                   
                                   rs.getDouble(8),
                                   rs.getDouble(9),
                                   rs.getDouble(10),
                                   rs.getDouble(11),
                                   rs.getDouble(12),
                                   rs.getDouble(13),
                                   rs.getDouble(14),
                                   rs.getDouble(15)});
                                   
                    
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
            //if(DokterUmum>0){
                //tabMode.addRow(new Object[]{
                    //"","","","","","","","","","","","","","","",""                                                         
                //});
                
            //}
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        LCount.setText(""+tabMode.getRowCount());
    }
   
    private void tampilranap() {
        Valid.tabelKosong(tabMode2);
        try {
            ps=koneksi.prepareStatement(" select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,penjab.png_jawab, pasien.no_peserta, c_group_billing.tanggal,c_group_billing.registrasi, "+ 
                " (c_group_billing.tindakan_ralandr+c_group_billing.tindakan_ralanpr+c_group_billing.tindakan_ralandrpr+ "+
                " c_group_billing.tindakan_ranapdr+c_group_billing.tindakan_ranappr+c_group_billing.tindakan_ranapdrpr)as tindakan, "+
                " (c_group_billing.obat_ralan+c_group_billing.obat_ranap)as obat,c_group_billing.retur_obat,c_group_billing.resep_pulang,c_group_billing.laborat_ralan+c_group_billing.laborat_ralan as laborat, "+
                " (c_group_billing.radiologi_ralan+c_group_billing.radiologi_ranap)as radiologi,c_group_billing.potongan,c_group_billing.tambahan,c_group_billing.kamar,c_group_billing.operasi, "+
                " c_group_billing.harian,c_group_billing.deposit,c_group_billing.jumlah_tagihan,c_group_billing.jumlah_bayar,c_group_billing.piutang "+
                " from pasien inner join reg_periksa inner join penjab inner join piutang_pasien inner join c_group_billing inner join poliklinik on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                " and penjab.kd_pj=reg_periksa.kd_pj and piutang_pasien.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rawat=c_group_billing.no_rawat and reg_periksa.kd_poli=poliklinik.kd_poli where "+
                " reg_periksa.status_lanjut='Ranap' and c_group_billing.tanggal between ? and ? and penjab.png_jawab like ? or "+
                " reg_periksa.status_lanjut='Ranap' and c_group_billing.tanggal between ? and ? and pasien.no_peserta like ? or "+
                " reg_periksa.status_lanjut='Ranap' and c_group_billing.tanggal between ? and ? and pasien.nm_pasien like ? or "+
                " reg_periksa.status_lanjut='Ranap' and c_group_billing.tanggal between ? and ? and reg_periksa.no_rawat like ? or "+
                " reg_periksa.status_lanjut='Ranap' and c_group_billing.tanggal between ? and ? and reg_periksa.no_rkm_medis like ? ");      
            try{
                ps.setString(1,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(3,"%"+TCari.getText().trim()+"%");
                ps.setString(4,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(5,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(6,"%"+TCari.getText().trim()+"%");
                ps.setString(7,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(8,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(9,"%"+TCari.getText().trim()+"%");
                ps.setString(10,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(11,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(12,"%"+TCari.getText().trim()+"%");
                ps.setString(13,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(14,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(15,"%"+TCari.getText().trim()+"%");
                rs=ps.executeQuery();
                DokterUmum=0; DokterUmumDr=0; DokterUmumDrPr=0; 
                DokterSpesialis=0; DokterSpesialisDr=0; DokterSpesialisDrPr=0;
                ADMdr=0; ADMpr=0; ADMdrpr=0; Lab1=0; Lab2=0;
                Obat=0; Lab=0; Rad=0; ADM=0; Lain=0; Tagihan=0; UangMuka=0;
                i=1;
                while(rs.next()){
                    ADMdr=Sequel.cariIsiAngka("select sum(rawat_inap_dr.biaya_rawat)as biaya from rawat_inap_dr inner join jns_perawatan on rawat_inap_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw where jns_perawatan.ket='ADM' and no_rawat='"+rs.getString(1)+"' ");
                    ADMpr=Sequel.cariIsiAngka("select sum(rawat_inap_pr.biaya_rawat)as biaya from rawat_inap_pr inner join jns_perawatan on rawat_inap_pr.kd_jenis_prw=jns_perawatan.kd_jenis_prw where jns_perawatan.ket='ADM' and no_rawat='"+rs.getString(1)+"' ");
                    ADMdrpr=Sequel.cariIsiAngka("select sum(rawat_inap_drpr.biaya_rawat)as biaya from rawat_inap_drpr inner join jns_perawatan on rawat_inap_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw where jns_perawatan.ket='ADM' and no_rawat='"+rs.getString(1)+"' ");
                    DokterUmumDr=Sequel.cariIsiAngka("select sum(rawat_inap_dr.biaya_rawat)as biaya from rawat_inap_dr inner join jns_perawatan on rawat_inap_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw where jns_perawatan.ket='DU' and no_rawat='"+rs.getString(1)+"' ");
                    DokterUmumDrPr=Sequel.cariIsiAngka("select sum(rawat_inap_drpr.biaya_rawat)as biaya from rawat_inap_drpr inner join jns_perawatan on rawat_inap_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw where jns_perawatan.ket='DU' and no_rawat='"+rs.getString(1)+"' ");
                    DokterSpesialisDr=Sequel.cariIsiAngka("select sum(rawat_inap_dr.biaya_rawat)as biaya from rawat_inap_dr inner join jns_perawatan on rawat_inap_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw where jns_perawatan.ket='DS' and no_rawat='"+rs.getString(1)+"' ");
                    DokterSpesialisDrPr=Sequel.cariIsiAngka("select sum(rawat_inap_drpr.biaya_rawat)as biaya from rawat_inap_drpr inner join jns_perawatan on rawat_inap_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw where jns_perawatan.ket='DS' and no_rawat='"+rs.getString(1)+"' ");
                    Rad=Sequel.cariIsiAngka("select sum(periksa_radiologi.biaya)as biaya from periksa_radiologi where no_rawat='"+rs.getString(1)+"' ");
                    Lab1=Sequel.cariIsiAngka("select sum(periksa_lab.biaya)as biaya from periksa_lab where no_rawat='"+rs.getString(1)+"' ");
                    Lab2=Sequel.cariIsiAngka("select sum(detail_periksa_lab.biaya_item)as biaya from detail_periksa_lab where no_rawat='"+rs.getString(1)+"' ");
                    Obat=Sequel.cariIsiAngka("select sum(detail_pemberian_obat.total)as biaya from detail_pemberian_obat where no_rawat='"+rs.getString(1)+"' ");
                    Tagihan=Sequel.cariIsiAngka("select tagihan_sadewa.jumlah_tagihan from tagihan_sadewa where no_nota='"+rs.getString(1)+"' ");
                    UangMuka=Sequel.cariIsiAngka("select tagihan_sadewa.jumlah_bayar from tagihan_sadewa where no_nota='"+rs.getString(1)+"' ");
                    DokterUmum=DokterUmum+DokterUmumDr+DokterUmumDrPr;
                    DokterSpesialis=DokterSpesialis+DokterSpesialisDr+DokterSpesialisDrPr; 
                    ADM=ADM+ADMdr+ADMpr+ADMdrpr;
                    Lab=Lab+Lab1+Lab2;
                    tabMode2.addRow(new Object[]{false,rs.getString(1),
                                   rs.getString(2),
                                   rs.getString(3),
                                   rs.getString(4),
                                   rs.getString(5),
                                   rs.getString(6),
                                   rs.getDouble(7),                                   
                                   rs.getDouble(8),
                                   rs.getDouble(9),
                                   rs.getDouble(10),
                                   rs.getDouble(11),
                                   rs.getDouble(12),
                                   rs.getDouble(13),
                                   rs.getDouble(14),
                                   rs.getDouble(15),
                                   rs.getDouble(16),
                                   rs.getDouble(17),                                   
                                   rs.getDouble(18),
                                   rs.getDouble(19),
                                   rs.getDouble(20),
                                   rs.getDouble(21)});
                                   
                    
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
            //if(DokterUmum>0){
                //tabMode2.addRow(new Object[]{
                    //"","","","","","","","","","","","","","","",""                                                         
                //});
                
            //}
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        LCount.setText(""+tabMode2.getRowCount());
    }
       

    public void emptTeks() {       
        TCari.setText("");     
        TCari.requestFocus();
        autoNomor();
        //Valid.autoNomer("poliklinik","U",4,Kd);
    }
//"P","No.Rawat","No.MR","Nama Pasien","Kode Diagnosa","Nama Diagnosa","Tarif","Percent(%)","Batas Tarif"};
    private void getData() {
        if(tbRalan.getSelectedRow()!= -1){           
            TNoRw2.setText(tbRalan.getValueAt(tbRalan.getSelectedRow(),1).toString());          
        }
    }
  
    public JButton getButton(){
        return BtnKeluar;
    }
    
    public void isCek(){
       
        BtnPrint.setEnabled(akses.getregistrasi());       
    }
    
    private void autoNomor() {
        Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(noorder,4),signed)),0) from permintaan_radiologi where tgl_permintaan='"+Valid.SetTgl(Tanggal.getSelectedItem()+"")+"' ","ST"+Valid.SetTgl(Tanggal.getSelectedItem()+"").replaceAll("-",""),4,TNoPermintaan);           
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
                    nilai_jam =CmbJam.getSelectedIndex();
                    nilai_menit =CmbMenit.getSelectedIndex();
                    nilai_detik =CmbDetik.getSelectedIndex();
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
                CmbJam.setSelectedItem(jam);
                CmbMenit.setSelectedItem(menit);
                CmbDetik.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }
    
    
}
