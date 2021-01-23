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
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import restore.DlgRestorePoli;

/**
 *
 * @author dosen
 */
public final class DlgKontrolBpjsTagihan extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabMode2;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private DlgKontrolBpjsCariDiagnosa poli=new DlgKontrolBpjsCariDiagnosa(null,false);
    private int i=0;
    private PreparedStatement stat,ps,ps2,ps3;
    private ResultSet rs,rs2,rs3;
    private String status="",kmr="",key="";
    private double stokbarang=0,ttltarif=0,ttltagihan=0,ttlselisih=0,y=0;
    private final DecimalFormat df5 = new DecimalFormat("###,###,###,###,###,###,###.##");  
    /** Creates new form DlgPenyakit
     * @param parent
     * @param modal */
    public DlgKontrolBpjsTagihan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(10,2);
        setSize(628,674);
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.","No.Rawat","No.MR","Nama Pasien","Kode Diagnosa","Nama Diagnosa","Tarif","Tagihan","Selisih","Status"
            
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbKamar.setModel(tabMode);
        
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbKamar.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbKamar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 10; i++) {
            TableColumn column = tbKamar.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(120);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(200);
            }else if(i==4){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==5){
                column.setPreferredWidth(200);
            }else if(i==6){
                column.setPreferredWidth(100);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setPreferredWidth(100);           
            }else if(i==9){
                column.setPreferredWidth(70);
            }
        }
        tbKamar.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabMode2=new DefaultTableModel(null,new Object[]{
            "No.","No.Rawat","No.MR","Nama Pasien","Kode.","Nama Diagnosa","Kamar","Tarif","Tagihan","Selisih","Status"
            
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbKamar2.setModel(tabMode2);              
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbKamar2.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbKamar2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 11; i++) {
            TableColumn column = tbKamar2.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(120);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(160);
            }else if(i==4){
                column.setPreferredWidth(40);
            }else if(i==5){
                column.setPreferredWidth(160);
            }else if(i==6){
                column.setPreferredWidth(180);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setPreferredWidth(100);
            }else if(i==9){
                column.setPreferredWidth(100);          
            }else if(i==10){
                column.setPreferredWidth(70);
            }
        }
        tbKamar2.setDefaultRenderer(Object.class, new WarnaTable());              
                
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
        internalFrame1 = new widget.InternalFrame();
        TabRawat = new javax.swing.JTabbedPane();
        Scroll = new widget.ScrollPane();
        tbKamar = new widget.Table();
        internalFrame9 = new widget.InternalFrame();
        internalFrame7 = new widget.InternalFrame();
        Scroll1 = new widget.ScrollPane();
        tbKamar2 = new widget.Table();
        panelCari = new widget.panelisi();
        R1 = new widget.RadioButton();
        R2 = new widget.RadioButton();
        DTPCari1 = new widget.Tanggal();
        jLabel22 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        R3 = new widget.RadioButton();
        DTPCari3 = new widget.Tanggal();
        jLabel25 = new widget.Label();
        DTPCari4 = new widget.Tanggal();
        jPanel1 = new javax.swing.JPanel();
        panelisi3 = new widget.panelisi();
        label9 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Kontrol BPJS - Tagihan Pasien ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbKamar.setAutoCreateRowSorter(true);
        tbKamar.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbKamar.setName("tbKamar"); // NOI18N
        tbKamar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbKamarMouseClicked(evt);
            }
        });
        tbKamar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbKamarKeyReleased(evt);
            }
        });
        Scroll.setViewportView(tbKamar);

        TabRawat.addTab("Ralan", Scroll);

        internalFrame9.setBorder(null);
        internalFrame9.setForeground(new java.awt.Color(50, 50, 50));
        internalFrame9.setName("internalFrame9"); // NOI18N
        internalFrame9.setLayout(new java.awt.BorderLayout(1, 1));

        internalFrame7.setBorder(null);
        internalFrame7.setName("internalFrame7"); // NOI18N
        internalFrame7.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);

        tbKamar2.setAutoCreateRowSorter(true);
        tbKamar2.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbKamar2.setName("tbKamar2"); // NOI18N
        tbKamar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbKamar2MouseClicked(evt);
            }
        });
        tbKamar2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbKamar2KeyReleased(evt);
            }
        });
        Scroll1.setViewportView(tbKamar2);

        internalFrame7.add(Scroll1, java.awt.BorderLayout.CENTER);

        panelCari.setName("panelCari"); // NOI18N
        panelCari.setPreferredSize(new java.awt.Dimension(44, 43));
        panelCari.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 9));

        R1.setBackground(new java.awt.Color(240, 250, 230));
        R1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R1);
        R1.setSelected(true);
        R1.setText("Belum Pulang");
        R1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R1.setName("R1"); // NOI18N
        R1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelCari.add(R1);

        R2.setBackground(new java.awt.Color(240, 250, 230));
        R2.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R2);
        R2.setText("Tgl.Masuk :");
        R2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R2.setName("R2"); // NOI18N
        R2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelCari.add(R2);

        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-02-2020" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(100, 23));
        DTPCari1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari1ItemStateChanged(evt);
            }
        });
        DTPCari1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari1KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari1);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("s.d");
        jLabel22.setName("jLabel22"); // NOI18N
        jLabel22.setPreferredSize(new java.awt.Dimension(25, 23));
        panelCari.add(jLabel22);

        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-02-2020" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(100, 23));
        DTPCari2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari2DTPCari1ItemStateChanged(evt);
            }
        });
        DTPCari2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari2KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari2);

        R3.setBackground(new java.awt.Color(240, 250, 230));
        R3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R3);
        R3.setText("Pulang :");
        R3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R3.setName("R3"); // NOI18N
        R3.setPreferredSize(new java.awt.Dimension(75, 23));
        panelCari.add(R3);

        DTPCari3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-02-2020" }));
        DTPCari3.setDisplayFormat("dd-MM-yyyy");
        DTPCari3.setName("DTPCari3"); // NOI18N
        DTPCari3.setOpaque(false);
        DTPCari3.setPreferredSize(new java.awt.Dimension(100, 23));
        DTPCari3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari3ItemStateChanged(evt);
            }
        });
        DTPCari3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari3KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari3);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("s.d");
        jLabel25.setName("jLabel25"); // NOI18N
        jLabel25.setPreferredSize(new java.awt.Dimension(25, 23));
        panelCari.add(jLabel25);

        DTPCari4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-02-2020" }));
        DTPCari4.setDisplayFormat("dd-MM-yyyy");
        DTPCari4.setName("DTPCari4"); // NOI18N
        DTPCari4.setOpaque(false);
        DTPCari4.setPreferredSize(new java.awt.Dimension(100, 23));
        DTPCari4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari4ItemStateChanged(evt);
            }
        });
        DTPCari4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari4KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari4);

        internalFrame7.add(panelCari, java.awt.BorderLayout.PAGE_END);

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

    private void tbKamarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbKamarMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbKamarMouseClicked

    private void TNoRw2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRw2KeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void tbKamarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKamarKeyReleased
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbKamarKeyReleased

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==0){
            tampil();
        }else if(TabRawat.getSelectedIndex()==1){                     
            tampilranap();         
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void tbKamar2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbKamar2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbKamar2MouseClicked

    private void tbKamar2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKamar2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbKamar2KeyReleased

    private void DTPCari1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari1ItemStateChanged
        R2.setSelected(true);

    }//GEN-LAST:event_DTPCari1ItemStateChanged

    private void DTPCari1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari1KeyPressed
        Valid.pindah(evt,TCari,DTPCari2);
    }//GEN-LAST:event_DTPCari1KeyPressed

    private void DTPCari2DTPCari1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari2DTPCari1ItemStateChanged
        R2.setSelected(true);

    }//GEN-LAST:event_DTPCari2DTPCari1ItemStateChanged

    private void DTPCari2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari2KeyPressed
        Valid.pindah(evt,DTPCari1,TCari);
    }//GEN-LAST:event_DTPCari2KeyPressed

    private void DTPCari3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari3ItemStateChanged
        R3.setSelected(true);
    }//GEN-LAST:event_DTPCari3ItemStateChanged

    private void DTPCari3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DTPCari3KeyPressed

    private void DTPCari4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari4ItemStateChanged
        R3.setSelected(true);
    }//GEN-LAST:event_DTPCari4ItemStateChanged

    private void DTPCari4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DTPCari4KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgKontrolBpjsTagihan dialog = new DlgKontrolBpjsTagihan(new javax.swing.JFrame(), true);
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
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.Tanggal DTPCari3;
    private widget.Tanggal DTPCari4;
    private widget.Label LCount;
    private widget.RadioButton R1;
    private widget.RadioButton R2;
    private widget.RadioButton R3;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.TextBox TCari;
    private widget.TextBox TNoRw2;
    private javax.swing.JTabbedPane TabRawat;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame7;
    private widget.InternalFrame internalFrame9;
    private widget.Label jLabel22;
    private widget.Label jLabel25;
    private javax.swing.JPanel jPanel1;
    private widget.Label label10;
    private widget.Label label11;
    private widget.Label label18;
    private widget.Label label9;
    private widget.panelisi panelCari;
    private widget.panelisi panelisi1;
    private widget.panelisi panelisi3;
    private widget.Table tbKamar;
    private widget.Table tbKamar2;
    // End of variables declaration//GEN-END:variables
//Object[] row={"P","No.Rawat","No.MR","Nama Pasien","Kode Diagnosa","Nama Diagnosa","Tarif","Percent(%)","Batas Tarif"};
    private void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            ps=koneksi.prepareStatement(" select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,c_tarif_bpjs.kd_penyakit, c_tarif_bpjs.nm_penyakit, c_tarif_bpjspasien.tarif, "+
                " c_tarif_bpjspasien.tagihan,c_tarif_bpjspasien.status "+
                " from pasien inner join reg_periksa inner join c_tarif_bpjs inner join c_tarif_bpjspasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                " and c_tarif_bpjspasien.no_rawat=reg_periksa.no_rawat and c_tarif_bpjs.kd_penyakit=c_tarif_bpjspasien.kd_penyakit where "+
                " reg_periksa.status_lanjut='Ralan' and reg_periksa.tgl_registrasi between ? and ? and c_tarif_bpjs.kd_penyakit like ? or "+
                " reg_periksa.status_lanjut='Ralan' and reg_periksa.tgl_registrasi between ? and ? and c_tarif_bpjs.nm_penyakit like ? or "+
                " reg_periksa.status_lanjut='Ralan' and reg_periksa.tgl_registrasi between ? and ? and pasien.nm_pasien like ? or "+
                " reg_periksa.status_lanjut='Ralan' and reg_periksa.tgl_registrasi between ? and ? and reg_periksa.no_rawat like ? or "+
                " reg_periksa.status_lanjut='Ralan' and reg_periksa.tgl_registrasi between ? and ? and reg_periksa.no_rkm_medis like ? ");      
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
                ttlselisih=0; ttltagihan=0; ttltarif=0; i=1;
                while(rs.next()){                   
                    tabMode.addRow(new Object[]{i,rs.getString(1),
                                   rs.getString(2),
                                   rs.getString(3),
                                   rs.getString(4),
                                   rs.getString(5),
                                   df5.format(rs.getDouble(6)),
                                   df5.format(rs.getDouble(7)),                                   
                                   df5.format(rs.getDouble(6)-rs.getDouble(7)),
                                   rs.getString(8)});
                    ttltarif=ttltarif+rs.getDouble(6);
                    ttltagihan=ttltagihan+rs.getDouble(7);
                    ttlselisih=ttlselisih+(rs.getDouble(6)-rs.getDouble(7));
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
            if(ttltarif>0){
                tabMode.addRow(new Object[]{
                    "","","","","","","","","",""                                                      
                });
                tabMode.addRow(new Object[]{
                    "","Total  :","","","","",df5.format(ttltarif),df5.format(ttltagihan),df5.format(ttlselisih),""                                                         
                });
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        LCount.setText(""+tabMode.getRowCount());
    }
    //"P","No.Rawat","No.MR","Nama Pasien","Kode Diagnosa","Nama Diagnosa","Kamar","Tarif","Percent(%)","Batas Tarif","Tagihan","Status"};
    private void tampilranap() {
        if(R1.isSelected()==true){
            kmr=" stts_pulang='-' ";            
        }else if(R2.isSelected()==true){
            kmr=" kamar_inap.tgl_masuk between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' ";            
        }else if(R3.isSelected()==true){
            kmr=" kamar_inap.tgl_keluar between '"+Valid.SetTgl(DTPCari3.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari4.getSelectedItem()+"")+"' ";                      
        }
        
        key=kmr+" ";
        if(!TCari.getText().equals("")){
            key= kmr+"and kamar_inap.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                 kmr+"and reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                 kmr+"and pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                 kmr+"and c_tarif_bpjs.kd_penyakit like '%"+TCari.getText().trim()+"%' or "+
                 kmr+"and c_tarif_bpjs.nm_penyakit like '%"+TCari.getText().trim()+"%' ";         
        }
        
        Valid.tabelKosong(tabMode2);
        try {
            ps2=koneksi.prepareStatement(" select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,c_tarif_bpjs.kd_penyakit, c_tarif_bpjs.nm_penyakit,concat(kamar_inap.kd_kamar,' ',bangsal.nm_bangsal) as kamar, c_tarif_bpjspasien.tarif, "+
                " c_tarif_bpjspasien.tagihan,c_tarif_bpjspasien.status "+
                " from kamar_inap inner join kamar inner join bangsal inner join pasien inner join reg_periksa inner join c_tarif_bpjs inner join c_tarif_bpjspasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                " and c_tarif_bpjspasien.no_rawat=reg_periksa.no_rawat and c_tarif_bpjs.kd_penyakit=c_tarif_bpjspasien.kd_penyakit and "+
                " kamar_inap.no_rawat=reg_periksa.no_rawat and kamar_inap.kd_kamar=kamar.kd_kamar and kamar.kd_bangsal=bangsal.kd_bangsal where "+
                " "+key+" order by bangsal.nm_bangsal,kamar_inap.tgl_masuk,kamar_inap.jam_masuk ");                
            try{               
                rs2=ps2.executeQuery();
                ttlselisih=0; ttltagihan=0; ttltarif=0; i=1;
                while(rs2.next()){                   
                    tabMode2.addRow(new Object[]{i,rs2.getString(1),
                                   rs2.getString(2),
                                   rs2.getString(3),
                                   rs2.getString(4),
                                   rs2.getString(5),
                                   rs2.getString(6),
                                   df5.format(rs2.getDouble(7)),                                   
                                   df5.format(rs2.getDouble(8)),
                                   df5.format(rs2.getDouble(7)-rs2.getDouble(8)),
                                   rs2.getString(9)});
                    ttltarif=ttltarif+rs2.getDouble(7);
                    ttltagihan=ttltagihan+rs2.getDouble(8);
                    ttlselisih=ttlselisih+(rs2.getDouble(7)-rs2.getDouble(8));
                    i++;
                }
                
            }catch(SQLException e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if( rs2 != null){
                    rs2.close();
                }
                
                if( ps2 != null){
                    ps2.close();
                }
            }
            if(ttltarif>0){
                tabMode2.addRow(new Object[]{
                    "","","","","","","","","","",""                                                            
                });
                tabMode2.addRow(new Object[]{
                    "","Total  :","","","","","",df5.format(ttltarif),df5.format(ttltagihan),df5.format(ttlselisih),""                                                         
                });
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        LCount.setText(""+tabMode2.getRowCount());
    }
       

    public void emptTeks() {       
        TCari.setText("");     
        TCari.requestFocus();        
        //Valid.autoNomer("poliklinik","U",4,Kd);
    }
//"P","No.Rawat","No.MR","Nama Pasien","Kode Diagnosa","Nama Diagnosa","Tarif","Percent(%)","Batas Tarif"};
    private void getData() {
        if(tbKamar.getSelectedRow()!= -1){           
            TNoRw2.setText(tbKamar.getValueAt(tbKamar.getSelectedRow(),1).toString());          
        }
    }
  
    public JButton getButton(){
        return BtnKeluar;
    }
    
    public void isCek(){
       
        BtnPrint.setEnabled(akses.getregistrasi());       
    }
    
   
    
    
}
