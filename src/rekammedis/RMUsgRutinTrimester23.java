/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgRujuk.java
 *
 * Created on 31 Mei 10, 20:19:56
 */

package rekammedis;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import laporan.DlgCariPenyakit;


/**
 *
 * @author perpustakaan
 */
public final class RMUsgRutinTrimester23 extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0;    
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private DlgCariPenyakit penyakit=new DlgCariPenyakit(null,false);
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMUsgRutinTrimester23(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.R.M.","Nama Pasien","Umur","JK","Tanggal","Lama","Akses","Dialist","Transfusi","Penarikan Cairan",
            "QB","QD","Ureum","Hb","HbsAg","Creatinin","HIV","HCV","Lain-Lain","Kode Dokter","Dokter","ICD 10","Diagnosa"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 24; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(160);
            }else if(i==3){
                column.setPreferredWidth(35);
            }else if(i==4){
                column.setPreferredWidth(20);
            }else if(i==5){
                column.setPreferredWidth(120);
            }else if(i==6){
                column.setPreferredWidth(35);
            }else if(i==7){
                column.setPreferredWidth(110);
            }else if(i==8){
                column.setPreferredWidth(100);
            }else if(i==9){
                column.setPreferredWidth(60);
            }else if(i==10){
                column.setPreferredWidth(100);
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
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==19){
                column.setPreferredWidth(150);
            }else if(i==20){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==21){
                column.setPreferredWidth(150);
            }else if(i==22){
                column.setPreferredWidth(45);
            }else if(i==23){
                column.setPreferredWidth(180);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        kddok.setDocument(new batasInput((byte)20).getKata(kddok));
        TLama.setDocument(new batasInput((int)10).getKata(TLama));
        TAkses.setDocument(new batasInput((int)50).getKata(TAkses));
        TDialist.setDocument(new batasInput((int)50).getKata(TDialist));
        TTransfusi.setDocument(new batasInput((int)10).getKata(TTransfusi));
        TPenarikan.setDocument(new batasInput((int)10).getKata(TPenarikan));
        TQB.setDocument(new batasInput((int)10).getKata(TQB));
        TQD.setDocument(new batasInput((int)10).getKata(TQD));
        TUreum.setDocument(new batasInput((int)20).getKata(TUreum));
        THb.setDocument(new batasInput((int)20).getKata(THb));
        THbsag.setDocument(new batasInput((int)20).getKata(THbsag));
        TCreatinin.setDocument(new batasInput((int)20).getKata(TCreatinin));
        THIV.setDocument(new batasInput((int)20).getKata(THIV));
        THCV.setDocument(new batasInput((int)20).getKata(THCV));
        TLain.setDocument(new batasInput((int)50).getKata(TLain));
        kdDiagnosa.setDocument(new batasInput((int)10).getKata(kdDiagnosa));
        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
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
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){                   
                    kddok.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    namadokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                }  
                kddok.requestFocus();
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
        
        penyakit.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if( penyakit.getTable().getSelectedRow()!= -1){                   
                    kdDiagnosa.setText(penyakit.getTable().getValueAt(penyakit.getTable().getSelectedRow(),0).toString());
                    NmDiagnosa.setText(penyakit.getTable().getValueAt(penyakit.getTable().getSelectedRow(),1).toString());
                }  
                kdDiagnosa.requestFocus();
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
        
        ChkInput.setSelected(false);
        isForm();
        
        kddok.setText(Sequel.cariIsi("select kd_dokterhemodialisa from set_pjlab"));
        namadokter.setText(Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",kddok.getText()));
        
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

        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        Tanggal = new widget.Tanggal();
        TNoRM = new widget.TextBox();
        jLabel16 = new widget.Label();
        Jam = new widget.ComboBox();
        Menit = new widget.ComboBox();
        Detik = new widget.ComboBox();
        ChkKejadian = new widget.CekBox();
        jLabel18 = new widget.Label();
        kddok = new widget.TextBox();
        namadokter = new widget.TextBox();
        btnDokter = new widget.Button();
        jLabel17 = new widget.Label();
        Tanggal1 = new widget.Tanggal();
        jLabel5 = new widget.Label();
        TNoRw1 = new widget.TextBox();
        Tanggal2 = new widget.Tanggal();
        jLabel20 = new widget.Label();
        jLabel9 = new widget.Label();
        TNoRw3 = new widget.TextBox();
        TNoRw4 = new widget.TextBox();
        jLabel11 = new widget.Label();
        TNoRw5 = new widget.TextBox();
        jLabel14 = new widget.Label();
        TNoRw7 = new widget.TextBox();
        jLabel25 = new widget.Label();
        TNoRw10 = new widget.TextBox();
        jLabel26 = new widget.Label();
        Tanggal3 = new widget.Tanggal();
        jLabel27 = new widget.Label();
        TNoRw11 = new widget.TextBox();
        jLabel28 = new widget.Label();
        jLabel29 = new widget.Label();
        jLabel31 = new widget.Label();
        TNoRw12 = new widget.TextBox();
        jLabel32 = new widget.Label();
        jLabel33 = new widget.Label();
        comboBox4 = new widget.ComboBox();
        comboBox5 = new widget.ComboBox();
        jLabel34 = new widget.Label();
        comboBox6 = new widget.ComboBox();
        comboBox7 = new widget.ComboBox();
        jLabel35 = new widget.Label();
        jLabel36 = new widget.Label();
        TNoRw13 = new widget.TextBox();
        jLabel37 = new widget.Label();
        jLabel38 = new widget.Label();
        TNoRw14 = new widget.TextBox();
        jLabel39 = new widget.Label();
        TNoRw15 = new widget.TextBox();
        jLabel40 = new widget.Label();
        TNoRw16 = new widget.TextBox();
        jLabel41 = new widget.Label();
        TNoRw17 = new widget.TextBox();
        jLabel42 = new widget.Label();
        jLabel43 = new widget.Label();
        TNoRw19 = new widget.TextBox();
        jLabel44 = new widget.Label();
        TNoRw20 = new widget.TextBox();
        jLabel45 = new widget.Label();
        TNoRw21 = new widget.TextBox();
        jLabel46 = new widget.Label();
        TNoRw22 = new widget.TextBox();
        TNoRw23 = new widget.TextBox();
        jLabel47 = new widget.Label();
        comboBox8 = new widget.ComboBox();
        ChkInput = new widget.CekBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data USG Rutin Trimester 2 dan 3 ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
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
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

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
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

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
        panelGlass8.add(BtnPrint);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(LCount);

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
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "07-02-2021" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "07-02-2021" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(310, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('3');
        BtnCari.setToolTipText("Alt+3");
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
        panelGlass9.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
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
        panelGlass9.add(BtnAll);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 245));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 225));
        FormInput.setLayout(null);

        jLabel4.setText("No.Rawat :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 10, 75, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(79, 10, 141, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(336, 10, 480, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "07-02-2021" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        FormInput.add(Tanggal);
        Tanggal.setBounds(100, 250, 90, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(222, 10, 112, 23);

        jLabel16.setText("Tanggal Periksa :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 250, 90, 23);

        Jam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam.setName("Jam"); // NOI18N
        Jam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamKeyPressed(evt);
            }
        });
        FormInput.add(Jam);
        Jam.setBounds(200, 250, 62, 23);

        Menit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit.setName("Menit"); // NOI18N
        Menit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenitKeyPressed(evt);
            }
        });
        FormInput.add(Menit);
        Menit.setBounds(260, 250, 62, 23);

        Detik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik.setName("Detik"); // NOI18N
        Detik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetikKeyPressed(evt);
            }
        });
        FormInput.add(Detik);
        Detik.setBounds(330, 250, 62, 23);

        ChkKejadian.setBorder(null);
        ChkKejadian.setSelected(true);
        ChkKejadian.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkKejadian.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setName("ChkKejadian"); // NOI18N
        FormInput.add(ChkKejadian);
        ChkKejadian.setBounds(390, 250, 23, 23);

        jLabel18.setText("Dokter P.J. :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(420, 250, 70, 23);

        kddok.setEditable(false);
        kddok.setHighlighter(null);
        kddok.setName("kddok"); // NOI18N
        kddok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kddokKeyPressed(evt);
            }
        });
        FormInput.add(kddok);
        kddok.setBounds(500, 250, 94, 23);

        namadokter.setEditable(false);
        namadokter.setName("namadokter"); // NOI18N
        FormInput.add(namadokter);
        namadokter.setBounds(600, 250, 185, 23);

        btnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokter.setMnemonic('2');
        btnDokter.setToolTipText("ALt+2");
        btnDokter.setName("btnDokter"); // NOI18N
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
        FormInput.add(btnDokter);
        btnDokter.setBounds(790, 250, 28, 23);

        jLabel17.setText("Tanggal Lahir :");
        jLabel17.setName("jLabel17"); // NOI18N
        jLabel17.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel17);
        jLabel17.setBounds(0, 40, 73, 23);

        Tanggal1.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "07-02-2021" }));
        Tanggal1.setDisplayFormat("dd-MM-yyyy");
        Tanggal1.setName("Tanggal1"); // NOI18N
        Tanggal1.setOpaque(false);
        Tanggal1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tanggal1KeyPressed(evt);
            }
        });
        FormInput.add(Tanggal1);
        Tanggal1.setBounds(80, 40, 90, 23);

        jLabel5.setText("Umur:");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(180, 40, 30, 23);

        TNoRw1.setHighlighter(null);
        TNoRw1.setName("TNoRw1"); // NOI18N
        TNoRw1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw1KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw1);
        TNoRw1.setBounds(220, 40, 141, 23);

        Tanggal2.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "07-02-2021" }));
        Tanggal2.setDisplayFormat("dd-MM-yyyy");
        Tanggal2.setName("Tanggal2"); // NOI18N
        Tanggal2.setOpaque(false);
        Tanggal2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tanggal2KeyPressed(evt);
            }
        });
        FormInput.add(Tanggal2);
        Tanggal2.setBounds(410, 40, 90, 23);

        jLabel20.setText("HPHT:");
        jLabel20.setName("jLabel20"); // NOI18N
        jLabel20.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel20);
        jLabel20.setBounds(370, 40, 30, 23);

        jLabel9.setText("Diagnosa:");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(0, 220, 60, 23);

        TNoRw3.setHighlighter(null);
        TNoRw3.setName("TNoRw3"); // NOI18N
        TNoRw3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw3KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw3);
        TNoRw3.setBounds(70, 220, 380, 23);

        TNoRw4.setHighlighter(null);
        TNoRw4.setName("TNoRw4"); // NOI18N
        TNoRw4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw4KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw4);
        TNoRw4.setBounds(110, 70, 240, 23);

        jLabel11.setText("Keluhan:");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(360, 70, 50, 23);

        TNoRw5.setHighlighter(null);
        TNoRw5.setName("TNoRw5"); // NOI18N
        TNoRw5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw5KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw5);
        TNoRw5.setBounds(410, 70, 410, 23);

        jLabel14.setText("Jumlah Janin:");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(0, 100, 80, 23);

        TNoRw7.setHighlighter(null);
        TNoRw7.setName("TNoRw7"); // NOI18N
        TNoRw7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw7KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw7);
        TNoRw7.setBounds(90, 100, 70, 23);

        jLabel25.setText("Usia Kehamilan Menurut USG:");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(0, 190, 150, 23);

        TNoRw10.setHighlighter(null);
        TNoRw10.setName("TNoRw10"); // NOI18N
        TNoRw10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw10KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw10);
        TNoRw10.setBounds(160, 190, 141, 23);

        jLabel26.setText("HPL:");
        jLabel26.setName("jLabel26"); // NOI18N
        jLabel26.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel26);
        jLabel26.setBounds(300, 190, 30, 23);

        Tanggal3.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "07-02-2021" }));
        Tanggal3.setDisplayFormat("dd-MM-yyyy");
        Tanggal3.setName("Tanggal3"); // NOI18N
        Tanggal3.setOpaque(false);
        Tanggal3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tanggal3KeyPressed(evt);
            }
        });
        FormInput.add(Tanggal3);
        Tanggal3.setBounds(340, 190, 90, 23);

        jLabel27.setText("gram");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(580, 190, 30, 23);

        TNoRw11.setHighlighter(null);
        TNoRw11.setName("TNoRw11"); // NOI18N
        TNoRw11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw11KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw11);
        TNoRw11.setBounds(510, 190, 70, 23);

        jLabel28.setText("TBJ:");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(440, 190, 70, 23);

        jLabel29.setText("Indikasi Pemeriksaan:");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput.add(jLabel29);
        jLabel29.setBounds(0, 70, 107, 23);

        jLabel31.setText("Rencana:");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(450, 220, 60, 23);

        TNoRw12.setHighlighter(null);
        TNoRw12.setName("TNoRw12"); // NOI18N
        TNoRw12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw12KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw12);
        TNoRw12.setBounds(520, 220, 300, 23);

        jLabel32.setText("Letak Janin:");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(170, 100, 60, 23);

        jLabel33.setText("AFI 1:");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(140, 160, 30, 23);

        comboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Memanjang" }));
        comboBox4.setName("comboBox4"); // NOI18N
        FormInput.add(comboBox4);
        comboBox4.setBounds(240, 100, 90, 23);

        comboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kepala" }));
        comboBox5.setName("comboBox5"); // NOI18N
        comboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox5ActionPerformed(evt);
            }
        });
        FormInput.add(comboBox5);
        comboBox5.setBounds(410, 100, 110, 23);

        jLabel34.setText("Implatansi Plasenta:");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput.add(jLabel34);
        jLabel34.setBounds(210, 130, 110, 23);

        comboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Fundus ", "Corpus", "Segmen bawah uterus" }));
        comboBox6.setName("comboBox6"); // NOI18N
        comboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox6ActionPerformed(evt);
            }
        });
        FormInput.add(comboBox6);
        comboBox6.setBounds(330, 130, 110, 23);

        comboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3" }));
        comboBox7.setName("comboBox7"); // NOI18N
        comboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox7ActionPerformed(evt);
            }
        });
        FormInput.add(comboBox7);
        comboBox7.setBounds(550, 130, 40, 23);

        jLabel35.setText("Derajat Maturasi: ");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(450, 130, 90, 23);

        jLabel36.setText("Amnion Fluid Index (AFI):");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(0, 160, 130, 23);

        TNoRw13.setHighlighter(null);
        TNoRw13.setName("TNoRw13"); // NOI18N
        TNoRw13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw13KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw13);
        TNoRw13.setBounds(180, 160, 50, 23);

        jLabel37.setText("Presentasi:");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(340, 100, 60, 23);

        jLabel38.setText("AFI 2:");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(240, 160, 30, 23);

        TNoRw14.setHighlighter(null);
        TNoRw14.setName("TNoRw14"); // NOI18N
        TNoRw14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw14KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw14);
        TNoRw14.setBounds(280, 160, 50, 23);

        jLabel39.setText("AFI 3:");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(340, 160, 30, 23);

        TNoRw15.setHighlighter(null);
        TNoRw15.setName("TNoRw15"); // NOI18N
        TNoRw15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw15KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw15);
        TNoRw15.setBounds(380, 160, 50, 23);

        jLabel40.setText("AFI 4:");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput.add(jLabel40);
        jLabel40.setBounds(440, 160, 30, 23);

        TNoRw16.setHighlighter(null);
        TNoRw16.setName("TNoRw16"); // NOI18N
        TNoRw16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw16KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw16);
        TNoRw16.setBounds(480, 160, 50, 23);

        jLabel41.setText("TOTAL AFI:");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(540, 160, 60, 23);

        TNoRw17.setHighlighter(null);
        TNoRw17.setName("TNoRw17"); // NOI18N
        TNoRw17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw17KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw17);
        TNoRw17.setBounds(610, 160, 50, 23);

        jLabel42.setText("Air Ketuban:");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(670, 160, 80, 23);

        jLabel43.setText("BPD:");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(530, 100, 30, 23);

        TNoRw19.setHighlighter(null);
        TNoRw19.setName("TNoRw19"); // NOI18N
        TNoRw19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw19KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw19);
        TNoRw19.setBounds(570, 100, 50, 23);

        jLabel44.setText("HC:");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(650, 100, 30, 23);

        TNoRw20.setHighlighter(null);
        TNoRw20.setName("TNoRw20"); // NOI18N
        TNoRw20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw20KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw20);
        TNoRw20.setBounds(690, 100, 50, 23);

        jLabel45.setText("AC:");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(750, 100, 30, 23);

        TNoRw21.setHighlighter(null);
        TNoRw21.setName("TNoRw21"); // NOI18N
        TNoRw21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw21KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw21);
        TNoRw21.setBounds(790, 100, 50, 23);

        jLabel46.setText("FL:");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(10, 130, 30, 23);

        TNoRw22.setHighlighter(null);
        TNoRw22.setName("TNoRw22"); // NOI18N
        TNoRw22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw22KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw22);
        TNoRw22.setBounds(50, 130, 50, 23);

        TNoRw23.setHighlighter(null);
        TNoRw23.setName("TNoRw23"); // NOI18N
        TNoRw23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw23KeyPressed(evt);
            }
        });
        FormInput.add(TNoRw23);
        TNoRw23.setBounds(150, 130, 50, 23);

        jLabel47.setText("DJJ:");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(110, 130, 30, 23);

        comboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cukup", "Kurang", "Lebih" }));
        comboBox8.setName("comboBox8"); // NOI18N
        FormInput.add(comboBox8);
        comboBox8.setBounds(760, 160, 60, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
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

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
            isPsien();
        }else{            
            Valid.pindah(evt,TCari,Tanggal);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
}//GEN-LAST:event_TPasienKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(kddok.getText().trim().equals("")||namadokter.getText().trim().equals("")){
            Valid.textKosong(kddok,"Dokter P.J");
        }else if(TLama.getText().trim().equals("")){
            Valid.textKosong(TLama,"Lama Instruksi");
        }else if(TAkses.getText().trim().equals("")){
            Valid.textKosong(TAkses,"Akses Instruksi");
        }else if(TDialist.getText().trim().equals("")){
            Valid.textKosong(TDialist,"Dialist");
        }else if(TTransfusi.getText().trim().equals("")){
            Valid.textKosong(TTransfusi,"Transfusi");
        }else if(TPenarikan.getText().trim().equals("")){
            Valid.textKosong(TPenarikan,"Penarikan Cairan");
        }else if(TQB.getText().trim().equals("")){
            Valid.textKosong(TQB,"QB");
        }else if(TQD.getText().trim().equals("")){
            Valid.textKosong(TQD,"QD");
        }else if(TUreum.getText().trim().equals("")){
            Valid.textKosong(TUreum,"Ureum");
        }else if(THb.getText().trim().equals("")){
            Valid.textKosong(THb,"Hb");
        }else if(THbsag.getText().trim().equals("")){
            Valid.textKosong(THbsag,"Hbasg");
        }else if(THb.getText().trim().equals("")){
            Valid.textKosong(THb,"Hb");
        }else if(TCreatinin.getText().trim().equals("")){
            Valid.textKosong(TCreatinin,"Creatinin");
        }else if(THIV.getText().trim().equals("")){
            Valid.textKosong(THIV,"HIV");
        }else if(THCV.getText().trim().equals("")){
            Valid.textKosong(THCV,"HCV");
        }else if(TLain.getText().trim().equals("")){
            Valid.textKosong(TLain,"Lain-Lain");
        }else if(kdDiagnosa.getText().trim().equals("")||NmDiagnosa.getText().trim().equals("")){
            Valid.textKosong(kdDiagnosa,"Diagnosa Pasien");
        }else{
            if(Sequel.menyimpantf("hemodialisa","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",18,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                kddok.getText(),TLama.getText(),TAkses.getText(),TDialist.getText(),TTransfusi.getText(),TPenarikan.getText(),TQB.getText(),TQD.getText(),TUreum.getText(),THb.getText(),
                THbsag.getText(),TCreatinin.getText(),THIV.getText(),THCV.getText(),TLain.getText(),kdDiagnosa.getText()
            })==true){
                tampil();
                emptTeks();
            }   
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,btnDiagnosa,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
        ChkInput.setSelected(true);
        isForm(); 
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()!= -1){
            if(Sequel.queryu2tf("delete from hemodialisa where tanggal=? and no_rawat=?",2,new String[]{
                tbObat.getValueAt(tbObat.getSelectedRow(),5).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
            })==true){
                tampil();
                emptTeks();
            }else{
                JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
            }
        }            
            
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(kddok.getText().trim().equals("")||namadokter.getText().trim().equals("")){
            Valid.textKosong(kddok,"Dokter P.J");
        }else if(TLama.getText().trim().equals("")){
            Valid.textKosong(TLama,"Lama Instruksi");
        }else if(TAkses.getText().trim().equals("")){
            Valid.textKosong(TAkses,"Akses Instruksi");
        }else if(TDialist.getText().trim().equals("")){
            Valid.textKosong(TDialist,"Dialist");
        }else if(TTransfusi.getText().trim().equals("")){
            Valid.textKosong(TTransfusi,"Transfusi");
        }else if(TPenarikan.getText().trim().equals("")){
            Valid.textKosong(TPenarikan,"Penarikan Cairan");
        }else if(TQB.getText().trim().equals("")){
            Valid.textKosong(TQB,"QB");
        }else if(TQD.getText().trim().equals("")){
            Valid.textKosong(TQD,"QD");
        }else if(TUreum.getText().trim().equals("")){
            Valid.textKosong(TUreum,"Ureum");
        }else if(THb.getText().trim().equals("")){
            Valid.textKosong(THb,"Hb");
        }else if(THbsag.getText().trim().equals("")){
            Valid.textKosong(THbsag,"Hbasg");
        }else if(THb.getText().trim().equals("")){
            Valid.textKosong(THb,"Hb");
        }else if(TCreatinin.getText().trim().equals("")){
            Valid.textKosong(TCreatinin,"Creatinin");
        }else if(THIV.getText().trim().equals("")){
            Valid.textKosong(THIV,"HIV");
        }else if(THCV.getText().trim().equals("")){
            Valid.textKosong(THCV,"HCV");
        }else if(TLain.getText().trim().equals("")){
            Valid.textKosong(TLain,"Lain-Lain");
        }else if(kdDiagnosa.getText().trim().equals("")||NmDiagnosa.getText().trim().equals("")){
            Valid.textKosong(kdDiagnosa,"Diagnosa Pasien");
        }else{        
            Sequel.mengedit("hemodialisa","tanggal=? and no_rawat=?","no_rawat=?,tanggal=?,kd_dokter=?,lama=?,akses=?,dialist=?,transfusi=?,penarikan=?,qb=?,qd=?,ureum=?,hb=?,hbsag=?,creatinin=?,hiv=?,hcv=?,lain=?,kd_penyakit=?",20,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),kddok.getText(),TLama.getText(),TAkses.getText(),
                TDialist.getText(),TTransfusi.getText(),TPenarikan.getText(),TQB.getText(),TQD.getText(),TUreum.getText(),THb.getText(),
                THbsag.getText(),TCreatinin.getText(),THIV.getText(),THCV.getText(),TLain.getText(),kdDiagnosa.getText(),
                tbObat.getValueAt(tbObat.getSelectedRow(),5).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
            });
            if(tabMode.getRowCount()!=0){tampil();}
            emptTeks();
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dokter.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            Map<String, Object> param = new HashMap<>(); 
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select logo from setting")); 
            if(TCari.getText().equals("")){ 
                Valid.MyReportqry("rptDataHemodialisa.jasper","report","::[ Data Hemodialis ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,hemodialisa.tanggal,hemodialisa.lama,hemodialisa.akses,hemodialisa.dialist,hemodialisa.transfusi,hemodialisa.penarikan, "+
                    "hemodialisa.qb,hemodialisa.qd,hemodialisa.ureum,hemodialisa.hb,hemodialisa.hbsag,creatinin,hemodialisa.hiv,hemodialisa.hcv,hemodialisa.lain, "+
                    "hemodialisa.kd_dokter,dokter.nm_dokter,hemodialisa.kd_penyakit,penyakit.nm_penyakit "+
                    "from hemodialisa inner join reg_periksa on hemodialisa.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter on hemodialisa.kd_dokter=dokter.kd_dokter "+
                    "inner join penyakit on hemodialisa.kd_penyakit=penyakit.kd_penyakit where "+
                    "hemodialisa.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59' order by hemodialisa.tanggal ",param);
            }else{
                Valid.MyReportqry("rptDataHemodialisa.jasper","report","::[ Data Hemodialis ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,hemodialisa.tanggal,hemodialisa.lama,hemodialisa.akses,hemodialisa.dialist,hemodialisa.transfusi,hemodialisa.penarikan, "+
                    "hemodialisa.qb,hemodialisa.qd,hemodialisa.ureum,hemodialisa.hb,hemodialisa.hbsag,creatinin,hemodialisa.hiv,hemodialisa.hcv,hemodialisa.lain, "+
                    "hemodialisa.kd_dokter,dokter.nm_dokter,hemodialisa.kd_penyakit,penyakit.nm_penyakit "+
                    "from hemodialisa inner join reg_periksa on hemodialisa.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter on hemodialisa.kd_dokter=dokter.kd_dokter "+
                    "inner join penyakit on hemodialisa.kd_penyakit=penyakit.kd_penyakit where "+
                    "hemodialisa.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59' and reg_periksa.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                    "hemodialisa.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59' and pasien.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                    "hemodialisa.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59' and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                    "hemodialisa.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59' and hemodialisa.akses like '%"+TCari.getText().trim()+"%' or "+
                    "hemodialisa.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59' and hemodialisa.dialist like '%"+TCari.getText().trim()+"%' or "+
                    "hemodialisa.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59' and hemodialisa.lain like '%"+TCari.getText().trim()+"%' or "+
                    "hemodialisa.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59' and dokter.nm_dokter like '%"+TCari.getText().trim()+"%' "+
                    "order by hemodialisa.tanggal ",param);
            }  
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
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
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt,TCari,Jam);
}//GEN-LAST:event_TanggalKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
}//GEN-LAST:event_TNoRMKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void JamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamKeyPressed
        Valid.pindah(evt,Tanggal,Menit);
    }//GEN-LAST:event_JamKeyPressed

    private void MenitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenitKeyPressed
        Valid.pindah(evt,Jam,Detik);
    }//GEN-LAST:event_MenitKeyPressed

    private void DetikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetikKeyPressed
        Valid.pindah(evt,Menit,btnDokter);
    }//GEN-LAST:event_DetikKeyPressed

    private void kddokKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kddokKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",namadokter,kddok.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            Detik.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            TLama.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            btnDokterActionPerformed(null);
        }
    }//GEN-LAST:event_kddokKeyPressed

    private void btnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokterActionPerformed
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_btnDokterActionPerformed

    private void btnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDokterKeyPressed
        Valid.pindah(evt,Detik,TLama);
    }//GEN-LAST:event_btnDokterKeyPressed

    private void Tanggal1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tanggal1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Tanggal1KeyPressed

    private void TNoRw1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw1KeyPressed

    private void Tanggal2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tanggal2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Tanggal2KeyPressed

    private void TNoRw3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw3KeyPressed

    private void TNoRw4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw4KeyPressed

    private void TNoRw5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw5KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw5KeyPressed

    private void TNoRw7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw7KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw7KeyPressed

    private void TNoRw10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw10KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw10KeyPressed

    private void Tanggal3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tanggal3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Tanggal3KeyPressed

    private void TNoRw11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw11KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw11KeyPressed

    private void TNoRw12KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw12KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw12KeyPressed

    private void comboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBox5ActionPerformed

    private void comboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBox6ActionPerformed

    private void comboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBox7ActionPerformed

    private void TNoRw13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw13KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw13KeyPressed

    private void TNoRw14KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw14KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw14KeyPressed

    private void TNoRw15KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw15KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw15KeyPressed

    private void TNoRw16KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw16KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw16KeyPressed

    private void TNoRw17KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw17KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw17KeyPressed

    private void TNoRw19KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw19KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw19KeyPressed

    private void TNoRw20KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw20KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw20KeyPressed

    private void TNoRw21KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw21KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw21KeyPressed

    private void TNoRw22KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw22KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw22KeyPressed

    private void TNoRw23KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw23KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw23KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMUsgRutinTrimester23 dialog = new RMUsgRutinTrimester23(new javax.swing.JFrame(), true);
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
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkKejadian;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.ComboBox Detik;
    private widget.PanelBiasa FormInput;
    private widget.ComboBox Jam;
    private widget.Label LCount;
    private widget.ComboBox Menit;
    private javax.swing.JPanel PanelInput;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TNoRw1;
    private widget.TextBox TNoRw10;
    private widget.TextBox TNoRw11;
    private widget.TextBox TNoRw12;
    private widget.TextBox TNoRw13;
    private widget.TextBox TNoRw14;
    private widget.TextBox TNoRw15;
    private widget.TextBox TNoRw16;
    private widget.TextBox TNoRw17;
    private widget.TextBox TNoRw19;
    private widget.TextBox TNoRw20;
    private widget.TextBox TNoRw21;
    private widget.TextBox TNoRw22;
    private widget.TextBox TNoRw23;
    private widget.TextBox TNoRw3;
    private widget.TextBox TNoRw4;
    private widget.TextBox TNoRw5;
    private widget.TextBox TNoRw7;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.Tanggal Tanggal1;
    private widget.Tanggal Tanggal2;
    private widget.Tanggal Tanggal3;
    private widget.Button btnDokter;
    private widget.ComboBox comboBox4;
    private widget.ComboBox comboBox5;
    private widget.ComboBox comboBox6;
    private widget.ComboBox comboBox7;
    private widget.ComboBox comboBox8;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel11;
    private widget.Label jLabel14;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel4;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel3;
    private widget.TextBox kddok;
    private widget.TextBox namadokter;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().toString().trim().equals("")){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,hemodialisa.tanggal,hemodialisa.lama,hemodialisa.akses,hemodialisa.dialist,hemodialisa.transfusi,hemodialisa.penarikan, "+
                    "hemodialisa.qb,hemodialisa.qd,hemodialisa.ureum,hemodialisa.hb,hemodialisa.hbsag,creatinin,hemodialisa.hiv,hemodialisa.hcv,hemodialisa.lain, "+
                    "hemodialisa.kd_dokter,dokter.nm_dokter,hemodialisa.kd_penyakit,penyakit.nm_penyakit "+
                    "from hemodialisa inner join reg_periksa on hemodialisa.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter on hemodialisa.kd_dokter=dokter.kd_dokter "+
                    "inner join penyakit on hemodialisa.kd_penyakit=penyakit.kd_penyakit where "+
                    "hemodialisa.tanggal between ? and ? order by hemodialisa.tanggal ");
            }else{
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,hemodialisa.tanggal,hemodialisa.lama,hemodialisa.akses,hemodialisa.dialist,hemodialisa.transfusi,hemodialisa.penarikan, "+
                    "hemodialisa.qb,hemodialisa.qd,hemodialisa.ureum,hemodialisa.hb,hemodialisa.hbsag,creatinin,hemodialisa.hiv,hemodialisa.hcv,hemodialisa.lain, "+
                    "hemodialisa.kd_dokter,dokter.nm_dokter,hemodialisa.kd_penyakit,penyakit.nm_penyakit "+
                    "from hemodialisa inner join reg_periksa on hemodialisa.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter on hemodialisa.kd_dokter=dokter.kd_dokter "+
                    "inner join penyakit on hemodialisa.kd_penyakit=penyakit.kd_penyakit where "+
                    "hemodialisa.tanggal between ? and ? and reg_periksa.no_rawat like ? or "+
                    "hemodialisa.tanggal between ? and ? and pasien.no_rkm_medis like ? or "+
                    "hemodialisa.tanggal between ? and ? and pasien.nm_pasien like ? or "+
                    "hemodialisa.tanggal between ? and ? and hemodialisa.akses like ? or "+
                    "hemodialisa.tanggal between ? and ? and hemodialisa.dialist like ? or "+
                    "hemodialisa.tanggal between ? and ? and hemodialisa.lain like ? or "+
                    "hemodialisa.tanggal between ? and ? and dokter.nm_dokter like ? "+
                    "order by hemodialisa.tanggal ");
            }
                
            try {
                if(TCari.getText().toString().trim().equals("")){
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                }else{
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(5,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(8,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(11,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(12,"%"+TCari.getText()+"%");
                    ps.setString(13,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(14,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(15,"%"+TCari.getText()+"%");
                    ps.setString(16,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(17,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(18,"%"+TCari.getText()+"%");
                    ps.setString(19,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(20,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(21,"%"+TCari.getText()+"%");
                }
                    
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("umurdaftar")+" "+rs.getString("sttsumur"),rs.getString("jk"),
                        rs.getString("tanggal"),rs.getString("lama"),rs.getString("akses"),
                        rs.getString("dialist"),rs.getString("transfusi"),rs.getString("penarikan"),rs.getString("qb"),rs.getString("qd"),
                        rs.getString("ureum"),rs.getString("hb"),rs.getString("hbsag"),rs.getString("creatinin"),
                        rs.getString("hiv"),rs.getString("hcv"),rs.getString("lain"),rs.getString("kd_dokter"),
                        rs.getString("nm_dokter"),rs.getString("kd_penyakit"),rs.getString("nm_penyakit")
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
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }
        int b=tabMode.getRowCount();
        LCount.setText(""+b);
    }

    public void emptTeks() {
        TLama.setText("");
        TAkses.setText("Femoral / Cimino");
        TDialist.setText("Bicarbonat");
        TTransfusi.setText("0");
        TPenarikan.setText("0");
        TQB.setText("0");
        TQD.setText("0");
        TUreum.setText("");
        THb.setText("");
        THbsag.setText("");
        TCreatinin.setText("");
        THIV.setText("");
        THCV.setText("");
        TLain.setText("");
        kdDiagnosa.setText("");
        NmDiagnosa.setText("");
        Tanggal.setDate(new Date());
        Tanggal.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            Valid.SetTgl(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());  
            Jam.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString().substring(11,13));
            Menit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString().substring(14,15));
            Detik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString().substring(17,19));
            TLama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            TAkses.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());  
            TDialist.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());  
            TTransfusi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            TPenarikan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            TQB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            TQD.setText(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            TUreum.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            THb.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString()); 
            THbsag.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            TCreatinin.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            THIV.setText(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
            THCV.setText(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
            TLain.setText(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());                 
            kddok.setText(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
            namadokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());
            kdDiagnosa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
            NmDiagnosa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
        }
    }

    private void isRawat() {
         Sequel.cariIsi("select no_rkm_medis from reg_periksa where no_rawat='"+TNoRw.getText()+"' ",TNoRM);
    }

    private void isPsien() {
        Sequel.cariIsi("select nm_pasien from pasien where no_rkm_medis='"+TNoRM.getText()+"' ",TPasien);
    }
    
    public void setNoRm(String norwt) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        isRawat();
        isPsien();              
        ChkInput.setSelected(true);
        isForm();
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,245));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.gethemodialisa());
        BtnHapus.setEnabled(akses.gethemodialisa());
        BtnEdit.setEnabled(akses.gethemodialisa());
        BtnPrint.setEnabled(akses.gethemodialisa()); 
    }

    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if(ChkKejadian.isSelected()==true){
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                }else if(ChkKejadian.isSelected()==false){
                    nilai_jam =Jam.getSelectedIndex();
                    nilai_menit =Menit.getSelectedIndex();
                    nilai_detik =Detik.getSelectedIndex();
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
                Jam.setSelectedItem(jam);
                Menit.setSelectedItem(menit);
                Detik.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }
    
}
