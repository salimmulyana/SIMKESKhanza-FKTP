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

public class DlgBillingTracer extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabMode2;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();   
    private DlgDepartemen dept=new DlgDepartemen(null,false);
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
    public DlgBillingTracer(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.Nota","Tanggal","Jam","No.RM","Nama Pasien","Total Bayar",
            "Total Hutang","Total Tagihan","Kasir","Aksi"
            
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbRadiologiRalan.setModel(tabMode);

        tbRadiologiRalan.setPreferredScrollableViewportSize(new Dimension(800,800));
        tbRadiologiRalan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 11; i++) {
            TableColumn column = tbRadiologiRalan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(120);
            }else if(i==1){
                column.setPreferredWidth(120);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(50);           
            }else if(i==5){
                column.setPreferredWidth(100);
            }else if(i==6){
                column.setPreferredWidth(100);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setPreferredWidth(100);
            }else if(i==9){
                column.setPreferredWidth(200);
            }else if(i==10){
                column.setPreferredWidth(70);
            }
        }
        tbRadiologiRalan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabMode2=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.Nota","Tanggal","Jam","No.RM","Nama Pasien","Deposit","Total Bayar",
            "Total Hutang","Total Tagihan","Kasir","Aksi"
            
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbRadiologiRalan2.setModel(tabMode2);

        tbRadiologiRalan2.setPreferredScrollableViewportSize(new Dimension(800,800));
        tbRadiologiRalan2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 12; i++) {
            TableColumn column = tbRadiologiRalan2.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(120);
            }else if(i==1){
                column.setPreferredWidth(120);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(50);           
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
                column.setPreferredWidth(200);
            }else if(i==11){
                column.setPreferredWidth(70);
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
        
                           
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TNoRw = new widget.TextBox();
        BtnHapus = new widget.Button();
        internalFrame1 = new widget.InternalFrame();
        jPanel2 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        label10 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel10 = new widget.Label();
        LCount = new widget.Label();
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

        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.setPreferredSize(new java.awt.Dimension(318, 23));
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
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

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Billing Tracer ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
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

        TabRawat.addTab("Billing Ralan", internalFrame2);

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

        TabRawat.addTab("Billing Ranap", internalFrame3);

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
    if(TabRawat.getSelectedIndex()==0){
        if(TNoRw.getText().trim().equals("")){
             JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\nKlik data pada Tab Data Klaem untuk memilih...!!!!");
        }else{
                Sequel.queryu2("delete from billing_trace where no_rawat=?",1,new String[]{
                    TNoRw.getText()
                });
                      
                emptTeks();
                BtnCariActionPerformed(null);
        }
    }else if(TabRawat.getSelectedIndex()==1){
        if(TNoRw.getText().trim().equals("")){
             JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\nKlik data pada Tab Data Klaem untuk memilih...!!!!");
        }else{
                Sequel.queryu2("delete from billing_trace where no_rawat=?",1,new String[]{
                    TNoRw.getText()
                });
                      
                emptTeks();
                BtnCariActionPerformed(null);
        }
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
        if(tabMode2.getRowCount()!=0){
        try {
            getData2();
        } catch (java.lang.NullPointerException e) {
        }
    }
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
            tampil2();
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        aktif=true;
    }//GEN-LAST:event_formWindowActivated

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        aktif=false;
    }//GEN-LAST:event_formWindowDeactivated

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRwKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgBillingTracer dialog = new DlgBillingTracer(new javax.swing.JFrame(), true);
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
    private widget.Label LCount;
    private widget.TextBox TCari;
    private widget.TextBox TNoRw;
    private javax.swing.JTabbedPane TabRawat;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.Label jLabel10;
    private javax.swing.JPanel jPanel2;
    private widget.Label label10;
    private widget.Label label11;
    private widget.Label label18;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelisi1;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.Table tbRadiologiRalan;
    private widget.Table tbRadiologiRalan2;
    // End of variables declaration//GEN-END:variables
//"No.Rawat","No.Nota","Tanggal","Jam","No.RM","Nama Pasien","Total Bayar",
            //"Total Hutang","Total Tagihan","Kasir"
    private void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            ps=koneksi.prepareStatement(
                    "select billing_trace.no_rawat,billing_trace.no_nota, billing_trace.tanggal, billing_trace.jam, reg_periksa.no_rkm_medis, pasien.nm_pasien, "+
                    "billing_trace.besar_bayar, billing_trace.besar_hutang, billing_trace.total, billing_trace.kasir, billing_trace.aksi "+
                    "from billing_trace inner join reg_periksa inner join pasien on "+
                    "billing_trace.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis where "+
                    "billing_trace.status='Ralan' and billing_trace.tanggal between ? and ? and billing_trace.no_rawat like ? or "+
                    "billing_trace.status='Ralan' and billing_trace.tanggal between ? and ? and pasien.nm_pasien like ? or "+
                    "billing_trace.status='Ralan' and billing_trace.tanggal between ? and ? and reg_periksa.no_rkm_medis like ? or "+
                    "billing_trace.status='Ralan' and billing_trace.tanggal between ? and ? and billing_trace.no_nota like ? ");   
            try {
                ps.setString(1,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(3,"%"+TCari.getText()+"%");
                ps.setString(4,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(5,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(6,"%"+TCari.getText()+"%");
                ps.setString(7,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(8,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(9,"%"+TCari.getText()+"%");
                ps.setString(10,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(11,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(12,"%"+TCari.getText()+"%");
                y=0;               
                rs=ps.executeQuery();              
                while(rs.next()){
                    tabMode.addRow(new String[]{                   
                        rs.getString("no_rawat"),rs.getString("no_nota"),rs.getString("tanggal"),rs.getString("jam"),
                        rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("besar_bayar"),rs.getString("besar_hutang"),rs.getString("total"),
                        rs.getString("kasir"),rs.getString("aksi")
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
            LCount.setText(""+tabMode.getRowCount());
            
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }        
    }
    
    //"No.Klaem","Tanggal","Jam","Perusahaan","Petugas","Total","Kode.Perusahaan","Kode.Petugas"
    private void tampil2() {
        Valid.tabelKosong(tabMode2);
        try {
            ps=koneksi.prepareStatement(
                    "select billing_trace.no_rawat,billing_trace.no_nota, billing_trace.tanggal, billing_trace.jam, reg_periksa.no_rkm_medis, pasien.nm_pasien, "+
                    "billing_trace.deposit, billing_trace.besar_bayar, billing_trace.besar_hutang, billing_trace.total, billing_trace.kasir,billing_trace.aksi "+
                    "from billing_trace inner join reg_periksa inner join pasien on "+
                    "billing_trace.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis where "+
                    "billing_trace.status='Ranap' and billing_trace.tanggal between ? and ? and billing_trace.no_rawat like ? or "+
                    "billing_trace.status='Ranap' and billing_trace.tanggal between ? and ? and pasien.nm_pasien like ? or "+
                    "billing_trace.status='Ranap' and billing_trace.tanggal between ? and ? and reg_periksa.no_rkm_medis like ? or "+
                    "billing_trace.status='Ranap' and billing_trace.tanggal between ? and ? and billing_trace.no_nota like ? ");   
            try {
                ps.setString(1,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(3,"%"+TCari.getText()+"%");
                ps.setString(4,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(5,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(6,"%"+TCari.getText()+"%");
                ps.setString(7,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(8,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(9,"%"+TCari.getText()+"%");
                ps.setString(10,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                ps.setString(11,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                ps.setString(12,"%"+TCari.getText()+"%");
                y=0;               
                rs=ps.executeQuery();              
                while(rs.next()){
                    tabMode2.addRow(new String[]{                   
                        rs.getString("no_rawat"),rs.getString("no_nota"),rs.getString("tanggal"),rs.getString("jam"),
                        rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("deposit"),rs.getString("besar_bayar"),rs.getString("besar_hutang"),rs.getString("total"),
                        rs.getString("kasir"),rs.getString("aksi")
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
            LCount.setText(""+tabMode2.getRowCount());
            
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }        
    }
    
     //"No.Klaem","Tanggal","Jam","Perusahaan","Petugas",
     //"Total Tagihan","Kode.Perusahaan","Kode.Petugas","Rek Piutang","Rek Bayar" ,
     //"Jml.Piutang","Discount"
    private void getData() {
        if(tbRadiologiRalan.getSelectedRow()!= -1){
            TNoRw.setText(tbRadiologiRalan.getValueAt(tbRadiologiRalan.getSelectedRow(),0).toString());
        }
    }
    
    private void getData2() {
        if(tbRadiologiRalan2.getSelectedRow()!= -1){
            TNoRw.setText(tbRadiologiRalan2.getValueAt(tbRadiologiRalan2.getSelectedRow(),0).toString());
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
        TNoRw.setText("");
    }
    
    
    
    
 
}
