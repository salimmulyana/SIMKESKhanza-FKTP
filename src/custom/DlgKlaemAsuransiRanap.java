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
import keuangan.Jurnal;
import restore.DlgRestorePoli;

/**
 *
 * @author dosen
 */
public final class DlgKlaemAsuransiRanap extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private DlgCariGroupPerusahaan poli=new DlgCariGroupPerusahaan(null,false);   
    private int i=0,jmlklaim=0,z=0;
    private boolean ceksukses=false;
    private PreparedStatement psnota,pssimpanbayar,stat,ps,ps2,ps3;
    private ResultSet rs,rs2,rs3;
    private String AlatRadiologi="",akunbayar="",status="",kmr="",key="",noklaem="",
            sqlpsnota="insert into c_klaim_detail values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private int jml=0;
    private double ttlkwitansi=0,ttlbayar=0,ttlpiutang=0,Discount2=0,JmlDiscount2=0,stokbarang=0,ttltarif=0,ttltagihan=0,ttlselisih=0,y=0,ttl=0,t=0,r=0;
    private final DecimalFormat df5 = new DecimalFormat("###,###,###,###,###,###,###.##");  
    private Jurnal jur=new Jurnal();
    /** Creates new form DlgPenyakit
     * @param parent
     * @param modal */
    public DlgKlaemAsuransiRanap(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(10,2);
        setSize(628,674);
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.MR","Nama Pasien","Nama Dokter","Perusahaan","Kons + Adm","Tindakan",
            "Lab","Radiologi","Alat Radiologi","Obat",
            "JML.Kwitansi","Di.Bayar Pasien","Total Diajukan","Status"     
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
                 java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, 
                 java.lang.Double.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, 
                 java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Object.class
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
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(50);
            }else if(i==3){
                column.setPreferredWidth(180);
            }else if(i==4){
                column.setPreferredWidth(180);
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
                column.setPreferredWidth(120);
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
                        NmAkunPiutang.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),2).toString());
                        AkunPiutang.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),3).toString());
                        Kd.requestFocus();  
                        tampil();
                        isHitungRalan();
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
        TNoPermintaan2 = new widget.TextBox();
        AkunPiutang = new widget.TextBox();
        NmAkunPiutang = new widget.TextBox();
        TotalTagihan = new widget.TextBox();
        JmlTagihan = new widget.TextBox();
        JmlKwitansi = new widget.TextBox();
        JmlBayar = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        jPanel2 = new javax.swing.JPanel();
        panelisi4 = new widget.panelisi();
        jLabel4 = new widget.Label();
        TNoPermintaanRalan = new widget.TextBox();
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
        Scroll = new widget.ScrollPane();
        tbRalan = new widget.Table();
        jPanel1 = new javax.swing.JPanel();
        panelisi3 = new widget.panelisi();
        label9 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        BtnSimpan = new widget.Button();
        panelisi5 = new widget.panelisi();
        label12 = new widget.Label();
        JmlPilih = new widget.TextBox();
        jSeparator6 = new javax.swing.JSeparator();
        label19 = new widget.Label();
        JmlTagihan2 = new widget.TextBox();
        label17 = new widget.Label();
        Discount = new widget.TextBox();
        label20 = new widget.Label();
        TotalTagihan2 = new widget.TextBox();
        panelisi1 = new widget.panelisi();
        label11 = new widget.Label();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        Tgl2 = new widget.Tanggal();
        label10 = new widget.Label();
        LCount = new widget.Label();
        BtnPrint = new widget.Button();
        BtnCari2 = new widget.Button();
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

        TNoPermintaan2.setHighlighter(null);
        TNoPermintaan2.setName("TNoPermintaan2"); // NOI18N
        TNoPermintaan2.setPreferredSize(new java.awt.Dimension(150, 24));
        TNoPermintaan2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoPermintaan2KeyPressed(evt);
            }
        });

        AkunPiutang.setHighlighter(null);
        AkunPiutang.setName("AkunPiutang"); // NOI18N
        AkunPiutang.setPreferredSize(new java.awt.Dimension(150, 24));
        AkunPiutang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AkunPiutangKeyPressed(evt);
            }
        });

        NmAkunPiutang.setHighlighter(null);
        NmAkunPiutang.setName("NmAkunPiutang"); // NOI18N
        NmAkunPiutang.setPreferredSize(new java.awt.Dimension(150, 24));
        NmAkunPiutang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NmAkunPiutangKeyPressed(evt);
            }
        });

        TotalTagihan.setHighlighter(null);
        TotalTagihan.setName("TotalTagihan"); // NOI18N
        TotalTagihan.setPreferredSize(new java.awt.Dimension(150, 24));
        TotalTagihan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TotalTagihanKeyPressed(evt);
            }
        });

        JmlTagihan.setHighlighter(null);
        JmlTagihan.setName("JmlTagihan"); // NOI18N
        JmlTagihan.setPreferredSize(new java.awt.Dimension(150, 24));
        JmlTagihan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JmlTagihanKeyPressed(evt);
            }
        });

        JmlKwitansi.setHighlighter(null);
        JmlKwitansi.setName("JmlKwitansi"); // NOI18N
        JmlKwitansi.setPreferredSize(new java.awt.Dimension(150, 24));
        JmlKwitansi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JmlKwitansiKeyPressed(evt);
            }
        });

        JmlBayar.setHighlighter(null);
        JmlBayar.setName("JmlBayar"); // NOI18N
        JmlBayar.setPreferredSize(new java.awt.Dimension(150, 24));
        JmlBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JmlBayarKeyPressed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Klaem Asuransi - Ranap ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
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

        TNoPermintaanRalan.setHighlighter(null);
        TNoPermintaanRalan.setName("TNoPermintaanRalan"); // NOI18N
        TNoPermintaanRalan.setPreferredSize(new java.awt.Dimension(150, 24));
        TNoPermintaanRalan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoPermintaanRalanKeyPressed(evt);
            }
        });
        panelisi4.add(TNoPermintaanRalan);

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

        jLabel15.setText("Tgl.Klaem :");
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(80, 14));
        panelisi4.add(jLabel15);

        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "05-12-2020" }));
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

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(816, 140));
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

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('2');
        BtnAll.setText("All");
        BtnAll.setToolTipText("Alt+2");
        BtnAll.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(80, 23));
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
        BtnSimpan.setText("Proses");
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

        panelisi5.setName("panelisi5"); // NOI18N
        panelisi5.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        label12.setText("Jml :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(50, 23));
        panelisi5.add(label12);

        JmlPilih.setHighlighter(null);
        JmlPilih.setName("JmlPilih"); // NOI18N
        JmlPilih.setPreferredSize(new java.awt.Dimension(60, 24));
        JmlPilih.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JmlPilihKeyPressed(evt);
            }
        });
        panelisi5.add(JmlPilih);

        jSeparator6.setBackground(new java.awt.Color(220, 225, 215));
        jSeparator6.setForeground(new java.awt.Color(220, 225, 215));
        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator6.setName("jSeparator6"); // NOI18N
        jSeparator6.setOpaque(true);
        jSeparator6.setPreferredSize(new java.awt.Dimension(5, 23));
        panelisi5.add(jSeparator6);

        label19.setText("Tttl.Tagihan :");
        label19.setName("label19"); // NOI18N
        label19.setPreferredSize(new java.awt.Dimension(80, 23));
        panelisi5.add(label19);

        JmlTagihan2.setHighlighter(null);
        JmlTagihan2.setName("JmlTagihan2"); // NOI18N
        JmlTagihan2.setPreferredSize(new java.awt.Dimension(150, 24));
        JmlTagihan2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JmlTagihan2KeyPressed(evt);
            }
        });
        panelisi5.add(JmlTagihan2);

        label17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label17.setText("-  Disc. ");
        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(60, 23));
        panelisi5.add(label17);

        Discount.setHighlighter(null);
        Discount.setName("Discount"); // NOI18N
        Discount.setPreferredSize(new java.awt.Dimension(150, 24));
        Discount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiscountKeyPressed(evt);
            }
        });
        panelisi5.add(Discount);

        label20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label20.setText("=");
        label20.setName("label20"); // NOI18N
        label20.setPreferredSize(new java.awt.Dimension(60, 23));
        panelisi5.add(label20);

        TotalTagihan2.setHighlighter(null);
        TotalTagihan2.setName("TotalTagihan2"); // NOI18N
        TotalTagihan2.setPreferredSize(new java.awt.Dimension(150, 24));
        TotalTagihan2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TotalTagihan2KeyPressed(evt);
            }
        });
        panelisi5.add(TotalTagihan2);

        jPanel1.add(panelisi5, java.awt.BorderLayout.CENTER);

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

        BtnCari2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnCari2.setMnemonic('E');
        BtnCari2.setText("Cari");
        BtnCari2.setToolTipText("Alt+E");
        BtnCari2.setName("BtnCari2"); // NOI18N
        BtnCari2.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnCari2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari2ActionPerformed(evt);
            }
        });
        BtnCari2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCari2KeyPressed(evt);
            }
        });
        panelisi1.add(BtnCari2);

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

        jPanel1.add(panelisi1, java.awt.BorderLayout.PAGE_END);

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
        tampil();
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
        emptTeks();
        tampil();
        isHitungRalan();
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
                isHitungRalan();
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

    private void TNoPermintaanRalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoPermintaanRalanKeyPressed
        //Valid.pindah(evt,TNoReg,DTPReg);
    }//GEN-LAST:event_TNoPermintaanRalanKeyPressed

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
        isHitungRalan();
    }//GEN-LAST:event_ppBersihkanRalanActionPerformed

    private void ppSemuaRalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppSemuaRalanActionPerformed
        for(i=0;i<tbRalan.getRowCount();i++){
            tbRalan.setValueAt(true,i,0);           
        }
        isHitungRalan();
    }//GEN-LAST:event_ppSemuaRalanActionPerformed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(Kd.getText().trim().equals("")||Nm.getText().trim().equals("")){
            Valid.textKosong(Kd,"Perusahaan");
            emptTeks();
            ppBersihkanRalanActionPerformed(evt);
        }else if(TotalTagihan.getText().trim().equals("0")){
            JOptionPane.showMessageDialog(null,"Maaf, Nilai Tagihan Tidak boleh Nol, Silahkan tekan enter di discount...!");
            emptTeks();
            ppBersihkanRalanActionPerformed(evt);
        }else{    
            int jawab=JOptionPane.showConfirmDialog(null, "Eeiiiiiits, udah bener belum data yang mau diklaem..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
            if(jawab==JOptionPane.YES_OPTION){
                ChkJln.setSelected(false);
                try {
                    //koneksi.setAutoCommit(false);                
                        ceksukses=false;                                                      
                        //akunbayar=Sequel.cariIsi("select kd_rek from akun_bayar where nama_bayar=?",nama_bayar.getSelectedItem().toString());
                        if(Sequel.menyimpantf2("c_klaim","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","No.Permintaan",15,new String[]{
                            TNoPermintaanRalan.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),
                            CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem(), 
                            Kd.getText(),akses.getkode(),JmlKwitansi.getText(),JmlBayar.getText(),JmlTagihan.getText(),Discount.getText(),TotalTagihan.getText(),
                            "Proses","Ranap",AkunPiutang.getText(),"-","0"
                            
                        })==true){
                            //================                             
                            for(i=0;i<tbRalan.getRowCount();i++){ 
                                if(tbRalan.getValueAt(i,0).toString().equals("true")){                                    
                                    Sequel.mengedittf("c_billing_klaim","no_rawat=? ","status_klaim='Proses',no_klaim=? ",2,new String[]{
                                        TNoPermintaanRalan.getText(),tbRalan.getValueAt(i,1).toString()
                                    });
                                    //==================================
                                    //Sequel.mengedittf("piutang_pasien","no_rawat=?","status='Lunas',no_klaim=? ",2,new String[]{
                                        //TNoPermintaanRalan.getText(),tbRalan.getValueAt(i,1).toString()
                                    //});
                                    Sequel.mengedittf("piutang_pasien","no_rawat=?","no_klaim=? ",2,new String[]{
                                        TNoPermintaanRalan.getText(),tbRalan.getValueAt(i,1).toString()
                                    });
                                    //==================================
                                    //pssimpanbayar=koneksi.prepareStatement("insert into bayar_piutang values(?,?,?,?,?,?)");
                                    //try {
                                        //pssimpanbayar.setString(1,Valid.SetTgl(Tanggal.getSelectedItem()+""));
                                        //pssimpanbayar.setString(2,tbRalan.getValueAt(i,2).toString());
                                        //pssimpanbayar.setString(3,tbRalan.getValueAt(i,14).toString());
                                        //pssimpanbayar.setString(4,TNoPermintaanRalan.getText());
                                        //pssimpanbayar.setString(5,tbRalan.getValueAt(i,1).toString());
                                        //pssimpanbayar.setString(6,akunbayar);        
                                        //pssimpanbayar.executeUpdate();

                                    //} catch (Exception e) {
                                        //System.out.println("Notifikasi : "+e);
                                    //} finally{
                                        //if(pssimpanbayar != null){
                                            //pssimpanbayar.close();
                                        //}
                                    //}
                                //==================================
                                    psnota=koneksi.prepareStatement(sqlpsnota);
                                    try {
                                        psnota.setString(1,tbRalan.getValueAt(i,1).toString());
                                        psnota.setString(2,tbRalan.getValueAt(i,2).toString());
                                        psnota.setString(3,tbRalan.getValueAt(i,3).toString());   
                                        psnota.setString(4,tbRalan.getValueAt(i,4).toString()); 
                                        psnota.setString(5,tbRalan.getValueAt(i,5).toString());
                                        psnota.setDouble(6,Valid.SetAngka(tbRalan.getValueAt(i,6).toString()));  
                                        psnota.setDouble(7,Valid.SetAngka(tbRalan.getValueAt(i,7).toString()));   
                                        psnota.setDouble(8,Valid.SetAngka(tbRalan.getValueAt(i,8).toString())); 
                                        psnota.setDouble(9,Valid.SetAngka(tbRalan.getValueAt(i,9).toString()));
                                        psnota.setString(10,tbRalan.getValueAt(i,10).toString());
                                        psnota.setDouble(11,Valid.SetAngka(tbRalan.getValueAt(i,11).toString()));
                                        psnota.setDouble(12,Valid.SetAngka(tbRalan.getValueAt(i,12).toString()));
                                        psnota.setDouble(13,Valid.SetAngka(tbRalan.getValueAt(i,13).toString()));
                                        psnota.setDouble(14,Valid.SetAngka(tbRalan.getValueAt(i,14).toString()));
                                        psnota.setString(15,"Proses");
                                        psnota.setString(16,TNoPermintaanRalan.getText());
                                        psnota.setString(17,Valid.SetTgl(Tanggal.getSelectedItem()+""));

                                        psnota.executeUpdate();
                                    } catch (Exception e) {
                                        System.out.println("Notifikasi : "+e);
                                    } finally{
                                        if(psnota != null){
                                            psnota.close();
                                        } 
                                    }
                                }
                                tbRalan.setValueAt(false,i,0);
                            } 
                            
                            //==================================
                                //Sequel.queryu("delete from tampjurnal");  
                                //===
                                //Sequel.menyimpan("tampjurnal","'"+AkunPiutang.getText()+"','"+NmAkunPiutang.getText()+"','0','"+TotalTagihan.getText()+"'","Rekening"); 
                                //Sequel.menyimpan("tampjurnal","'"+akunbayar+"','"+nama_bayar.getSelectedItem()+"','"+TotalTagihan.getText()+"','0'","Rekening"); 
                                //jur.simpanJurnal(TNoPermintaanRalan.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),"U","BAYAR PIUTANG KLAEM RALAN"+", OLEH "+akses.getkode());                   

                            TNoPermintaan2.setText(TNoPermintaanRalan.getText());
                            emptTeks();

                        }

                    ChkJln.setSelected(true);
                    isHitungRalan();
                    emptTeks();
                    tampil();
                    //koneksi.setAutoCommit(true);

                }catch (Exception ex) {
                    System.out.println("Notifikasi : "+ex);            
                    JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Data yang sama dimasukkan sebelumnya...!");
                } 
            }
        }
        
    }//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnCari2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari2ActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));  
        DlgCariKlaemRanap form=new DlgCariKlaemRanap(null,false);
        form.isCek();
        form.setPasien(TNoPermintaan2.getText());
        form.setSize(this.getWidth(),this.getHeight());
        form.setLocationRelativeTo(this);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnCari2ActionPerformed

    private void BtnCari2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari2KeyPressed
        
    }//GEN-LAST:event_BtnCari2KeyPressed

    private void TNoPermintaan2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoPermintaan2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoPermintaan2KeyPressed

    private void JmlPilihKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JmlPilihKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JmlPilihKeyPressed

    private void JmlTagihanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JmlTagihanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JmlTagihanKeyPressed

    private void TotalTagihanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TotalTagihanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalTagihanKeyPressed

    private void AkunPiutangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AkunPiutangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_AkunPiutangKeyPressed

    private void NmAkunPiutangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NmAkunPiutangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NmAkunPiutangKeyPressed

    private void TotalTagihan2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TotalTagihan2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalTagihan2KeyPressed

    private void DiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiscountKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
        }
    }//GEN-LAST:event_DiscountKeyPressed

    private void JmlTagihan2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JmlTagihan2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JmlTagihan2KeyPressed

    private void JmlBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JmlBayarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JmlBayarKeyPressed

    private void JmlKwitansiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JmlKwitansiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JmlKwitansiKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgKlaemAsuransiRanap dialog = new DlgKlaemAsuransiRanap(new javax.swing.JFrame(), true);
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
    private widget.TextBox AkunPiutang;
    private widget.Button BtnAll;
    private widget.Button BtnCari;
    private widget.Button BtnCari2;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.Button BtnUnit;
    private widget.CekBox ChkJln;
    private widget.ComboBox CmbDetik;
    private widget.ComboBox CmbJam;
    private widget.ComboBox CmbMenit;
    private widget.TextBox Discount;
    private widget.TextBox JmlBayar;
    private widget.TextBox JmlKwitansi;
    private widget.TextBox JmlPilih;
    private widget.TextBox JmlTagihan;
    private widget.TextBox JmlTagihan2;
    private widget.TextBox Kd;
    private widget.Label LCount;
    private widget.TextBox Nm;
    private widget.TextBox NmAkunPiutang;
    private javax.swing.JPopupMenu PopupRalan;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNoPermintaan2;
    private widget.TextBox TNoPermintaanRalan;
    private widget.TextBox TNoRw2;
    private widget.Tanggal Tanggal;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private widget.TextBox TotalTagihan;
    private widget.TextBox TotalTagihan2;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel15;
    private widget.Label jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator6;
    private widget.Label label10;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label17;
    private widget.Label label18;
    private widget.Label label19;
    private widget.Label label20;
    private widget.Label label34;
    private widget.Label label9;
    private widget.panelisi panelisi1;
    private widget.panelisi panelisi3;
    private widget.panelisi panelisi4;
    private widget.panelisi panelisi5;
    private javax.swing.JMenuItem ppBersihkanRalan;
    private javax.swing.JMenuItem ppSemuaRalan;
    private widget.Table tbRalan;
    // End of variables declaration//GEN-END:variables
            //"P","No.Rawat","No.MR","Nama Pasien","Nama Dokter","Perusahaan","Kons + Adm","Tindakan",
            //"Lab","Radiologi","Alat Radiologi","Obat",
            //"JML.Kwitansi","Di.Bayar Pasien","Total Diajukan","Status","No.Klaem","Tgl.Kwitansi"        
    public void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            ps=koneksi.prepareStatement(" select c_billing_klaim.no_rawat, c_billing_klaim.no_rkm_medis, c_billing_klaim.nm_pasien, "+ 
                " dokter.nm_dokter, penjab.png_jawab, (c_billing_klaim.adm+c_billing_klaim.konsultasi)as konsul, c_billing_klaim.tindakan_dokter, c_billing_klaim.lab,  "+
                " c_billing_klaim.radiologi, (c_billing_klaim.obat+c_billing_klaim.ppnobat)as tobat, c_billing_klaim.jml_kwitansi, c_billing_klaim.jml_bayar, c_billing_klaim.jml_klaim, "+
                " c_billing_klaim.status_klaim "+
                " from c_billing_klaim inner join reg_periksa inner join penjab inner join piutang_pasien inner join dokter on "+
                " reg_periksa.no_rawat=c_billing_klaim.no_rawat and penjab.kd_pj=reg_periksa.kd_pj and dokter.kd_dokter=reg_periksa.kd_dokter  and piutang_pasien.no_rawat=c_billing_klaim.no_rawat where "+
                " reg_periksa.kd_group!='UMUM' and c_billing_klaim.status_rawat='Ranap' and c_billing_klaim.status_klaim='Belum' and c_billing_klaim.tanggal between ? and ? and penjab.kd_perusahaan like ? and penjab.png_jawab like ? or "+
                " reg_periksa.kd_group!='UMUM' and c_billing_klaim.status_rawat='Ranap' and c_billing_klaim.status_klaim='Belum' and c_billing_klaim.tanggal between ? and ? and penjab.kd_perusahaan like ? and dokter.nm_dokter like ? or "+
                " reg_periksa.kd_group!='UMUM' and c_billing_klaim.status_rawat='Ranap' and c_billing_klaim.status_klaim='Belum' and c_billing_klaim.tanggal between ? and ? and penjab.kd_perusahaan like ? and c_billing_klaim.nm_pasien like ? or "+
                " reg_periksa.kd_group!='UMUM' and c_billing_klaim.status_rawat='Ranap' and c_billing_klaim.status_klaim='Belum' and c_billing_klaim.tanggal between ? and ? and penjab.kd_perusahaan like ? and c_billing_klaim.no_rawat like ? or "+
                " reg_periksa.kd_group!='UMUM' and c_billing_klaim.status_rawat='Ranap' and c_billing_klaim.status_klaim='Belum' and c_billing_klaim.tanggal between ? and ? and penjab.kd_perusahaan like ? and c_billing_klaim.no_rkm_medis like ? ");      
            try{
                ps.setString(1,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(3,"%"+Kd.getText().trim()+"%");
                ps.setString(4,"%"+TCari.getText().trim()+"%");
                ps.setString(5,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(6,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(7,"%"+Kd.getText().trim()+"%");
                ps.setString(8,"%"+TCari.getText().trim()+"%");
                ps.setString(9,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(10,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(11,"%"+Kd.getText().trim()+"%");
                ps.setString(12,"%"+TCari.getText().trim()+"%");
                ps.setString(13,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(14,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(15,"%"+Kd.getText().trim()+"%");
                ps.setString(16,"%"+TCari.getText().trim()+"%");
                ps.setString(17,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(18,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(19,"%"+Kd.getText().trim()+"%");
                ps.setString(20,"%"+TCari.getText().trim()+"%");
                rs=ps.executeQuery();
                i=1;
                while(rs.next()){
                    AlatRadiologi=Sequel.cariIsi("select group_concat(c_kategori_alat.nm_alat separator ', ') from c_kategori_alat inner join periksa_radiologi inner join jns_perawatan_radiologi on jns_perawatan_radiologi.kd_jenis_prw=periksa_radiologi.kd_jenis_prw and jns_perawatan_radiologi.kd_alat=c_kategori_alat.kd_alat where periksa_radiologi.no_rawat=? group by periksa_radiologi.no_rawat",rs.getString(1));
                    tabMode.addRow(new Object[]{false,
                                   rs.getString(1),
                                   rs.getString(2),
                                   rs.getString(3),
                                   rs.getString(4),
                                   rs.getString(5),
                                   0,
                                   0,
                                   0,
                                   0,
                                   "-",//=======
                                   0,
                                   rs.getDouble(11),                                 
                                   rs.getDouble(12),
                                   rs.getDouble(13),
                                   rs.getString(14)});
                                   
                    
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
  
    public void emptTeks() {
        Kd.setText("");
        Nm.setText("");
        Discount.setText("0");
        TotalTagihan.setText("0");
        TotalTagihan2.setText("0");
        TCari.setText(""); 
        TCari.requestFocus();
        autoNomor();
        //isHitungRalan();
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
        Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(no_klaim,4),signed)),0) from c_klaim where tanggal='"+Valid.SetTgl(Tanggal.getSelectedItem()+"")+"' ","ST"+Valid.SetTgl(Tanggal.getSelectedItem()+"").replaceAll("-",""),4,TNoPermintaanRalan);           
    }
    
    private void isHitungRalan() {
        ttl=0; 
        ttltagihan=0;
        ttlkwitansi=0;
        ttlbayar=0;
        z=tbRalan.getRowCount();
        for(i=0;i<z;i++){ 
            if(tbRalan.getValueAt(i,0).toString().equals("true")){
                ttl++; //y=0;              
                try {
                    t=Double.parseDouble(tbRalan.getValueAt(i,12).toString());
                    r=Double.parseDouble(tbRalan.getValueAt(i,13).toString());
                    y=Double.parseDouble(tbRalan.getValueAt(i,14).toString());
                } catch (Exception e) {
                    t=0; r=0; y=0;
                    
                }                                          
                //ttl=1; 
                ttlkwitansi=ttlkwitansi+t;
                ttlbayar=ttlbayar+r;
                ttltagihan=ttltagihan+y;
            }                           
        }
        
        JmlPilih.setText(Valid.SetAngka3(ttl));
        JmlKwitansi.setText(Double.toString(ttlkwitansi)); 
        JmlBayar.setText(Double.toString(ttlbayar)); 
        JmlTagihan.setText(Double.toString(ttltagihan)); 
        JmlTagihan2.setText(Valid.SetAngka3(ttltagihan)); 
        
    }
    
    private void isjml(){
        TotalTagihan.setText("0");
        TotalTagihan2.setText("0");
        if((!JmlTagihan.getText().equals(""))&&(!Discount.getText().equals(""))){
            TotalTagihan.setText(Double.toString(Double.parseDouble(JmlTagihan.getText())-Double.parseDouble(Discount.getText())));
            TotalTagihan2.setText(Valid.SetAngka3(Double.parseDouble(JmlTagihan.getText())-Double.parseDouble(Discount.getText())));
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
