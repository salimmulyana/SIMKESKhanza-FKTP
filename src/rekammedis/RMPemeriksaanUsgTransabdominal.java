package rekammedis;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariPetugas;
import simrskhanza.DlgKamarInap;
import simrskhanza.DlgRujuk;

/**
 *
 * @author dosen
 */
public class RMPemeriksaanUsgTransabdominal extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0,gejala1=0,gejala2=0,gejala3=0,resiko1=0,resiko2a=0,resiko2b=0,resiko2c=0,resiko2d=0,resiko2e=0,gejala=0,resiko=0;
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    

    /** Creates new form DlgPemberianInfus
     * @param parent
     * @param modal */
    public RMPemeriksaanUsgTransabdominal(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        tabMode=new DefaultTableModel(null,new Object[]{
                "No.Rawat","No.R.M.","Nama Pasien","Alamat","No.KTP","No.Telp","Pekerjaan","Tgl.Lahir","Skrining","NIP","Nama Petugas",
                "Demam","Batuk","Sesak Napas","Timbul Gejala","Riwayat Sakit Sebelumnya","Riwayat Periksa Sebelumnya","Perjalanan LN",
                "Asal Daerah","Kedatangan","Kontak Positif","Kontak PDP","Faskes Positif","Perjalanan LN","Pasar Hewan","Kesimpulan",
                "Tindak Lanjut"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 27; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(150);
            }else if(i==3){
                column.setPreferredWidth(170);
            }else if(i==4){
                column.setPreferredWidth(100);
            }else if(i==5){
                column.setPreferredWidth(100);
            }else if(i==6){
                column.setPreferredWidth(100);
            }else if(i==7){
                column.setPreferredWidth(65);
            }else if(i==8){
                column.setPreferredWidth(65);
            }else if(i==9){
                column.setPreferredWidth(90);
            }else if(i==10){
                column.setPreferredWidth(150);
            }else if(i==11){
                column.setPreferredWidth(50);
            }else if(i==12){
                column.setPreferredWidth(50);
            }else if(i==13){
                column.setPreferredWidth(70);
            }else if(i==14){
                column.setPreferredWidth(75);
            }else if(i==15){
                column.setPreferredWidth(150);
            }else if(i==16){
                column.setPreferredWidth(150);
            }else if(i==17){
                column.setPreferredWidth(75);
            }else if(i==18){
                column.setPreferredWidth(120);
            }else if(i==19){
                column.setPreferredWidth(65);
            }else if(i==20){
                column.setPreferredWidth(75);
            }else if(i==21){
                column.setPreferredWidth(65);
            }else if(i==22){
                column.setPreferredWidth(75);
            }else if(i==23){
                column.setPreferredWidth(75);
            }else if(i==24){
                column.setPreferredWidth(73);
            }else if(i==25){
                column.setPreferredWidth(85);
            }else if(i==26){
                column.setPreferredWidth(80);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        //RiwayatSakitSebelumnya.setDocument(new batasInput((byte)50).getKata(RiwayatSakitSebelumnya));
        RiwayatPeriksaSebelumnya.setDocument(new batasInput((byte)50).getKata(RiwayatPeriksaSebelumnya));
        DiagnosaKehamilan.setDocument(new batasInput((byte)50).getKata(DiagnosaKehamilan));

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
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(petugas.getTable().getSelectedRow()!= -1){                   
                    kdptg.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                    nmptg.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                }  
                kdptg.requestFocus();
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
        MnCetakDeteksiDini = new javax.swing.JMenuItem();
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
        jLabel10 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass7 = new widget.panelisi();
        jLabel15 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel17 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        NoRM = new widget.TextBox();
        NamaPasien = new widget.TextBox();
        jLabel7 = new widget.Label();
        NoRawat = new widget.TextBox();
        A1 = new widget.ComboBox();
        jLabel9 = new widget.Label();
        Gs = new widget.TextBox();
        jLabel11 = new widget.Label();
        jLabel16 = new widget.Label();
        Alamat = new widget.TextBox();
        jLabel18 = new widget.Label();
        A2 = new widget.ComboBox();
        TglSkrining = new widget.Tanggal();
        jLabel23 = new widget.Label();
        DiagnosaKehamilan = new widget.TextBox();
        jLabel24 = new widget.Label();
        jLabel27 = new widget.Label();
        B1 = new widget.ComboBox();
        jLabel33 = new widget.Label();
        jLabel34 = new widget.Label();
        RiwayatPeriksaSebelumnya = new widget.TextBox();
        Rtl = new widget.ComboBox();
        jLabel49 = new widget.Label();
        JadwalKontrol = new widget.ComboBox();
        jLabel50 = new widget.Label();
        jLabel51 = new widget.Label();
        kdptg = new widget.TextBox();
        nmptg = new widget.TextBox();
        BtnPtg = new widget.Button();
        jLabel37 = new widget.Label();
        jLabel12 = new widget.Label();
        Gs1 = new widget.TextBox();
        jLabel19 = new widget.Label();
        jLabel20 = new widget.Label();
        Gs2 = new widget.TextBox();
        jLabel21 = new widget.Label();
        jLabel22 = new widget.Label();
        Gs3 = new widget.TextBox();
        jLabel31 = new widget.Label();
        jLabel36 = new widget.Label();
        Gs4 = new widget.TextBox();
        jLabel39 = new widget.Label();
        jLabel40 = new widget.Label();
        Gs5 = new widget.TextBox();
        jLabel42 = new widget.Label();
        jLabel46 = new widget.Label();
        Gs6 = new widget.TextBox();
        jLabel47 = new widget.Label();
        jLabel53 = new widget.Label();
        Gs7 = new widget.TextBox();
        jLabel54 = new widget.Label();
        jLabel55 = new widget.Label();
        Gs8 = new widget.TextBox();
        jLabel56 = new widget.Label();
        jLabel57 = new widget.Label();
        Gs9 = new widget.TextBox();
        jLabel58 = new widget.Label();
        jLabel13 = new widget.Label();
        Gs10 = new widget.TextBox();
        jLabel14 = new widget.Label();
        jLabel59 = new widget.Label();
        Gs11 = new widget.TextBox();
        jLabel60 = new widget.Label();
        jLabel61 = new widget.Label();
        Gs12 = new widget.TextBox();
        jLabel62 = new widget.Label();
        jLabel63 = new widget.Label();
        Gs13 = new widget.TextBox();
        jLabel64 = new widget.Label();
        jLabel65 = new widget.Label();
        Gs14 = new widget.TextBox();
        jLabel66 = new widget.Label();
        jLabel67 = new widget.Label();
        Gs15 = new widget.TextBox();
        jLabel68 = new widget.Label();
        jLabel69 = new widget.Label();
        Gs16 = new widget.TextBox();
        jLabel70 = new widget.Label();
        jLabel71 = new widget.Label();
        Gs17 = new widget.TextBox();
        jLabel72 = new widget.Label();
        jLabel73 = new widget.Label();
        Gs18 = new widget.TextBox();
        jLabel74 = new widget.Label();
        jLabel75 = new widget.Label();
        Gs19 = new widget.TextBox();
        jLabel76 = new widget.Label();
        jLabel77 = new widget.Label();
        Gs20 = new widget.TextBox();
        jLabel78 = new widget.Label();
        jLabel79 = new widget.Label();
        jLabel80 = new widget.Label();
        Gs21 = new widget.TextBox();
        jLabel81 = new widget.Label();
        jLabel82 = new widget.Label();
        Gs22 = new widget.TextBox();
        jLabel83 = new widget.Label();
        jLabel32 = new widget.Label();
        jLabel84 = new widget.Label();
        Gs23 = new widget.TextBox();
        jLabel85 = new widget.Label();
        jLabel86 = new widget.Label();
        Gs24 = new widget.TextBox();
        jLabel87 = new widget.Label();
        jLabel88 = new widget.Label();
        jLabel29 = new widget.Label();
        Gs25 = new widget.TextBox();
        jLabel30 = new widget.Label();
        jLabel89 = new widget.Label();
        jLabel90 = new widget.Label();
        Gs26 = new widget.TextBox();
        jLabel91 = new widget.Label();
        jLabel92 = new widget.Label();
        Gs27 = new widget.TextBox();
        jLabel93 = new widget.Label();
        Gs28 = new widget.TextBox();
        jLabel94 = new widget.Label();
        jLabel95 = new widget.Label();
        jLabel96 = new widget.Label();
        Gs29 = new widget.TextBox();
        jLabel97 = new widget.Label();
        jLabel35 = new widget.Label();
        jLabel38 = new widget.Label();
        jLabel28 = new widget.Label();
        jLabel25 = new widget.Label();
        jLabel26 = new widget.Label();
        TglSkrining1 = new widget.Tanggal();
        jLabel41 = new widget.Label();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnCetakDeteksiDini.setBackground(new java.awt.Color(255, 255, 254));
        MnCetakDeteksiDini.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCetakDeteksiDini.setForeground(new java.awt.Color(50, 50, 50));
        MnCetakDeteksiDini.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnCetakDeteksiDini.setText("Cetak Form Deteksi Dini");
        MnCetakDeteksiDini.setName("MnCetakDeteksiDini"); // NOI18N
        MnCetakDeteksiDini.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCetakDeteksiDiniActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnCetakDeteksiDini);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pemeriksaan USG Transabdominal ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbObat.setAutoCreateRowSorter(true);
        tbObat.setComponentPopupMenu(jPopupMenu1);
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
        panelGlass8.setPreferredSize(new java.awt.Dimension(55, 55));
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

        jLabel10.setText("Record :");
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass8.add(jLabel10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(95, 23));
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

        jPanel3.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        panelGlass7.setName("panelGlass7"); // NOI18N
        panelGlass7.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel15.setText("Tgl.Masuk :");
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(63, 23));
        panelGlass7.add(jLabel15);

        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-10-2020" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass7.add(DTPCari1);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("s.d");
        jLabel17.setName("jLabel17"); // NOI18N
        jLabel17.setPreferredSize(new java.awt.Dimension(24, 23));
        panelGlass7.add(jLabel17);

        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-10-2020" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass7.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass7.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(360, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass7.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('7');
        BtnCari.setToolTipText("Alt+7");
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
        panelGlass7.add(BtnCari);

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
        panelGlass7.add(BtnAll);

        jPanel3.add(panelGlass7, java.awt.BorderLayout.CENTER);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 306));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('M');
        ChkInput.setText(".: Input Data");
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

        scrollInput.setName("scrollInput"); // NOI18N

        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(830, 713));
        FormInput.setLayout(null);

        NoRM.setEditable(false);
        NoRM.setHighlighter(null);
        NoRM.setName("NoRM"); // NOI18N
        FormInput.add(NoRM);
        NoRM.setBounds(219, 10, 100, 23);

        NamaPasien.setEditable(false);
        NamaPasien.setHighlighter(null);
        NamaPasien.setName("NamaPasien"); // NOI18N
        FormInput.add(NamaPasien);
        NamaPasien.setBounds(321, 10, 497, 23);

        jLabel7.setText("No.Rawat :");
        jLabel7.setName("jLabel7"); // NOI18N
        FormInput.add(jLabel7);
        jLabel7.setBounds(0, 10, 72, 23);

        NoRawat.setEditable(false);
        NoRawat.setHighlighter(null);
        NoRawat.setName("NoRawat"); // NOI18N
        FormInput.add(NoRawat);
        NoRawat.setBounds(76, 10, 141, 23);

        A1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2-8 cukup", "<2 kurang", ">8 banyak", "S.solitus(N)", "S.inversus" }));
        A1.setName("A1"); // NOI18N
        A1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A1ItemStateChanged(evt);
            }
        });
        A1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A1KeyPressed(evt);
            }
        });
        FormInput.add(A1);
        A1.setBounds(75, 250, 120, 23);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("CRL: Crown Rumpth Length");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(940, 120, 150, 23);

        Gs.setEditable(false);
        Gs.setHighlighter(null);
        Gs.setName("Gs"); // NOI18N
        FormInput.add(Gs);
        Gs.setBounds(60, 100, 70, 23);

        jLabel11.setText("minggu");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(140, 100, 40, 23);

        jLabel16.setText("Alamat :");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 72, 23);

        Alamat.setEditable(false);
        Alamat.setHighlighter(null);
        Alamat.setName("Alamat"); // NOI18N
        FormInput.add(Alamat);
        Alamat.setBounds(80, 40, 557, 23);

        jLabel18.setText("Cardiac situs:");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(0, 250, 70, 23);

        A2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Abdormal", "Sulit dilihat" }));
        A2.setName("A2"); // NOI18N
        A2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A2ItemStateChanged(evt);
            }
        });
        A2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A2KeyPressed(evt);
            }
        });
        FormInput.add(A2);
        A2.setBounds(540, 250, 120, 23);

        TglSkrining.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-10-2020" }));
        TglSkrining.setDisplayFormat("dd-MM-yyyy");
        TglSkrining.setName("TglSkrining"); // NOI18N
        TglSkrining.setOpaque(false);
        TglSkrining.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TglSkriningActionPerformed(evt);
            }
        });
        TglSkrining.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglSkriningKeyPressed(evt);
            }
        });
        FormInput.add(TglSkrining);
        TglSkrining.setBounds(820, 40, 90, 23);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("Kelainan Mayor Yang Ditemukan");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(20, 350, 200, 23);

        DiagnosaKehamilan.setHighlighter(null);
        DiagnosaKehamilan.setName("DiagnosaKehamilan"); // NOI18N
        DiagnosaKehamilan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaKehamilanKeyPressed(evt);
            }
        });
        FormInput.add(DiagnosaKehamilan);
        DiagnosaKehamilan.setBounds(250, 380, 680, 23);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText(":");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(240, 350, 10, 23);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("Keterangan:");
        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(980, 70, 120, 23);

        B1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3 vessels", "2 vessels" }));
        B1.setName("B1"); // NOI18N
        B1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                B1ItemStateChanged(evt);
            }
        });
        B1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                B1KeyPressed(evt);
            }
        });
        FormInput.add(B1);
        B1.setBounds(430, 220, 100, 23);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setText("Diagnosa Kehamilan");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(20, 380, 200, 23);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText(":");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput.add(jLabel34);
        jLabel34.setBounds(240, 500, 10, 23);

        RiwayatPeriksaSebelumnya.setHighlighter(null);
        RiwayatPeriksaSebelumnya.setName("RiwayatPeriksaSebelumnya"); // NOI18N
        RiwayatPeriksaSebelumnya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RiwayatPeriksaSebelumnyaKeyPressed(evt);
            }
        });
        FormInput.add(RiwayatPeriksaSebelumnya);
        RiwayatPeriksaSebelumnya.setBounds(250, 350, 680, 23);

        Rtl.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kontrol rutin sesuai jadwal", "USG morfologi scan", "Skrinning pre-eclampsia", "Skrinning down syndrome", "Cek laboratorium ke 1", "Cek laboratorium ke 2", "Cek laboratorium ke 3", "Cek laboratorium ke 4", "Doppler velocimetri", "NST/CTG", "Biophysical profile janin" }));
        Rtl.setName("Rtl"); // NOI18N
        Rtl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RtlKeyPressed(evt);
            }
        });
        FormInput.add(Rtl);
        Rtl.setBounds(250, 420, 350, 23);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel49.setText("Rencana Selanjutnya");
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(20, 420, 110, 23);

        JadwalKontrol.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 bulan lagi", "2 minggu lagi", "1 minggu lagi" }));
        JadwalKontrol.setName("JadwalKontrol"); // NOI18N
        JadwalKontrol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JadwalKontrolKeyPressed(evt);
            }
        });
        FormInput.add(JadwalKontrol);
        JadwalKontrol.setBounds(250, 460, 110, 23);

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel50.setText("Jadwal kontrol berikutnya");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(20, 460, 160, 23);

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel51.setText("Petugas ");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput.add(jLabel51);
        jLabel51.setBounds(20, 500, 70, 23);

        kdptg.setEditable(false);
        kdptg.setHighlighter(null);
        kdptg.setName("kdptg"); // NOI18N
        kdptg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdptgKeyPressed(evt);
            }
        });
        FormInput.add(kdptg);
        kdptg.setBounds(260, 500, 110, 23);

        nmptg.setEditable(false);
        nmptg.setHighlighter(null);
        nmptg.setName("nmptg"); // NOI18N
        FormInput.add(nmptg);
        nmptg.setBounds(370, 500, 190, 23);

        BtnPtg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPtg.setMnemonic('X');
        BtnPtg.setToolTipText("Alt+X");
        BtnPtg.setName("BtnPtg"); // NOI18N
        BtnPtg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPtgActionPerformed(evt);
            }
        });
        BtnPtg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPtgKeyPressed(evt);
            }
        });
        FormInput.add(BtnPtg);
        BtnPtg.setBounds(570, 500, 28, 23);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel37.setText(":");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(240, 380, 10, 23);

        jLabel12.setText("CRL:");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(190, 100, 30, 23);

        Gs1.setEditable(false);
        Gs1.setHighlighter(null);
        Gs1.setName("Gs1"); // NOI18N
        FormInput.add(Gs1);
        Gs1.setBounds(230, 100, 70, 23);

        jLabel19.setText("minggu");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput.add(jLabel19);
        jLabel19.setBounds(310, 100, 40, 23);

        jLabel20.setText("NT:");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(360, 100, 30, 23);

        Gs2.setEditable(false);
        Gs2.setHighlighter(null);
        Gs2.setName("Gs2"); // NOI18N
        FormInput.add(Gs2);
        Gs2.setBounds(400, 100, 70, 23);

        jLabel21.setText("mm");
        jLabel21.setName("jLabel21"); // NOI18N
        FormInput.add(jLabel21);
        jLabel21.setBounds(480, 100, 20, 23);

        jLabel22.setText("YS:");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(520, 100, 30, 23);

        Gs3.setEditable(false);
        Gs3.setHighlighter(null);
        Gs3.setName("Gs3"); // NOI18N
        FormInput.add(Gs3);
        Gs3.setBounds(560, 100, 70, 23);

        jLabel31.setText("mm");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(640, 100, 20, 23);

        jLabel36.setText("BPD:");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(690, 100, 30, 23);

        Gs4.setEditable(false);
        Gs4.setHighlighter(null);
        Gs4.setName("Gs4"); // NOI18N
        FormInput.add(Gs4);
        Gs4.setBounds(730, 100, 70, 23);

        jLabel39.setText("Hari Pertama Menstruasi Terakhir:");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(640, 40, 170, 23);

        jLabel40.setText("TCD:");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput.add(jLabel40);
        jLabel40.setBounds(20, 130, 30, 23);

        Gs5.setEditable(false);
        Gs5.setHighlighter(null);
        Gs5.setName("Gs5"); // NOI18N
        FormInput.add(Gs5);
        Gs5.setBounds(60, 130, 70, 23);

        jLabel42.setText("minggu");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(140, 130, 40, 23);

        jLabel46.setText("CM:");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(190, 130, 30, 23);

        Gs6.setEditable(false);
        Gs6.setHighlighter(null);
        Gs6.setName("Gs6"); // NOI18N
        FormInput.add(Gs6);
        Gs6.setBounds(230, 130, 70, 23);

        jLabel47.setText("mm");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(310, 130, 40, 23);

        jLabel53.setText("NF:");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(360, 130, 30, 23);

        Gs7.setEditable(false);
        Gs7.setHighlighter(null);
        Gs7.setName("Gs7"); // NOI18N
        FormInput.add(Gs7);
        Gs7.setBounds(400, 130, 70, 23);

        jLabel54.setText("mm");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(480, 130, 20, 23);

        jLabel55.setText("HC:");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(520, 130, 30, 23);

        Gs8.setEditable(false);
        Gs8.setHighlighter(null);
        Gs8.setName("Gs8"); // NOI18N
        FormInput.add(Gs8);
        Gs8.setBounds(560, 130, 70, 23);

        jLabel56.setText("minggu");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(640, 130, 36, 23);

        jLabel57.setText("BOD:");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(690, 130, 30, 23);

        Gs9.setEditable(false);
        Gs9.setHighlighter(null);
        Gs9.setName("Gs9"); // NOI18N
        FormInput.add(Gs9);
        Gs9.setBounds(730, 130, 70, 23);

        jLabel58.setText("minggu");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(810, 130, 40, 23);

        jLabel13.setText("IOD:");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(20, 160, 30, 23);

        Gs10.setEditable(false);
        Gs10.setHighlighter(null);
        Gs10.setName("Gs10"); // NOI18N
        FormInput.add(Gs10);
        Gs10.setBounds(60, 160, 70, 23);

        jLabel14.setText("mm");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(140, 160, 40, 23);

        jLabel59.setText("HUM:");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(190, 160, 30, 23);

        Gs11.setEditable(false);
        Gs11.setHighlighter(null);
        Gs11.setName("Gs11"); // NOI18N
        FormInput.add(Gs11);
        Gs11.setBounds(230, 160, 70, 23);

        jLabel60.setText("minggu");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(310, 160, 40, 23);

        jLabel61.setText("RAD:");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(360, 160, 30, 23);

        Gs12.setEditable(false);
        Gs12.setHighlighter(null);
        Gs12.setName("Gs12"); // NOI18N
        FormInput.add(Gs12);
        Gs12.setBounds(400, 160, 70, 23);

        jLabel62.setText("minggu");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(480, 160, 40, 23);

        jLabel63.setText("AC:");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(520, 160, 30, 23);

        Gs13.setEditable(false);
        Gs13.setHighlighter(null);
        Gs13.setName("Gs13"); // NOI18N
        FormInput.add(Gs13);
        Gs13.setBounds(560, 160, 70, 23);

        jLabel64.setText("minggu");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(640, 160, 40, 23);

        jLabel65.setText("FL:");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(690, 160, 30, 23);

        Gs14.setEditable(false);
        Gs14.setHighlighter(null);
        Gs14.setName("Gs14"); // NOI18N
        FormInput.add(Gs14);
        Gs14.setBounds(730, 160, 70, 23);

        jLabel66.setText("minggu");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(810, 160, 40, 23);

        jLabel67.setText("TIB:");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(20, 190, 30, 23);

        Gs15.setEditable(false);
        Gs15.setHighlighter(null);
        Gs15.setName("Gs15"); // NOI18N
        FormInput.add(Gs15);
        Gs15.setBounds(60, 190, 70, 23);

        jLabel68.setText("minggu");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(140, 190, 40, 23);

        jLabel69.setText("FIB:");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput.add(jLabel69);
        jLabel69.setBounds(190, 190, 30, 23);

        Gs16.setEditable(false);
        Gs16.setHighlighter(null);
        Gs16.setName("Gs16"); // NOI18N
        FormInput.add(Gs16);
        Gs16.setBounds(230, 190, 70, 23);

        jLabel70.setText("Cord vessels:");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput.add(jLabel70);
        jLabel70.setBounds(350, 220, 70, 23);

        jLabel71.setText("FMF angle:");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(360, 190, 60, 23);

        Gs17.setEditable(false);
        Gs17.setHighlighter(null);
        Gs17.setName("Gs17"); // NOI18N
        Gs17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Gs17ActionPerformed(evt);
            }
        });
        FormInput.add(Gs17);
        Gs17.setBounds(430, 190, 70, 23);

        jLabel72.setText("o");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput.add(jLabel72);
        jLabel72.setBounds(510, 180, 6, 23);

        jLabel73.setText("Average:");
        jLabel73.setName("jLabel73"); // NOI18N
        FormInput.add(jLabel73);
        jLabel73.setBounds(520, 190, 30, 23);

        Gs18.setEditable(false);
        Gs18.setHighlighter(null);
        Gs18.setName("Gs18"); // NOI18N
        FormInput.add(Gs18);
        Gs18.setBounds(560, 190, 70, 23);

        jLabel74.setText("minggu");
        jLabel74.setName("jLabel74"); // NOI18N
        FormInput.add(jLabel74);
        jLabel74.setBounds(640, 190, 40, 23);

        jLabel75.setText("EFW:");
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput.add(jLabel75);
        jLabel75.setBounds(690, 190, 30, 23);

        Gs19.setEditable(false);
        Gs19.setHighlighter(null);
        Gs19.setName("Gs19"); // NOI18N
        FormInput.add(Gs19);
        Gs19.setBounds(730, 190, 70, 23);

        jLabel76.setText("gram");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput.add(jLabel76);
        jLabel76.setBounds(810, 190, 40, 23);

        jLabel77.setText("Plac Thick:");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput.add(jLabel77);
        jLabel77.setBounds(160, 220, 60, 23);

        Gs20.setEditable(false);
        Gs20.setHighlighter(null);
        Gs20.setName("Gs20"); // NOI18N
        Gs20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Gs20ActionPerformed(evt);
            }
        });
        FormInput.add(Gs20);
        Gs20.setBounds(230, 220, 70, 23);

        jLabel78.setText("minggu");
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput.add(jLabel78);
        jLabel78.setBounds(310, 190, 40, 23);

        jLabel79.setText("mm");
        jLabel79.setName("jLabel79"); // NOI18N
        FormInput.add(jLabel79);
        jLabel79.setBounds(310, 220, 30, 23);

        jLabel80.setText("FHR:");
        jLabel80.setName("jLabel80"); // NOI18N
        FormInput.add(jLabel80);
        jLabel80.setBounds(530, 220, 30, 23);

        Gs21.setEditable(false);
        Gs21.setHighlighter(null);
        Gs21.setName("Gs21"); // NOI18N
        FormInput.add(Gs21);
        Gs21.setBounds(570, 220, 70, 23);

        jLabel81.setText("x/menit");
        jLabel81.setName("jLabel81"); // NOI18N
        FormInput.add(jLabel81);
        jLabel81.setBounds(650, 220, 40, 23);

        jLabel82.setText("SDP");
        jLabel82.setName("jLabel82"); // NOI18N
        FormInput.add(jLabel82);
        jLabel82.setBounds(700, 220, 30, 23);

        Gs22.setEditable(false);
        Gs22.setHighlighter(null);
        Gs22.setName("Gs22"); // NOI18N
        FormInput.add(Gs22);
        Gs22.setBounds(740, 220, 70, 23);

        jLabel83.setText("cm");
        jLabel83.setName("jLabel83"); // NOI18N
        FormInput.add(jLabel83);
        jLabel83.setBounds(820, 220, 20, 23);

        jLabel32.setText("EDD USG:");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(0, 220, 50, 23);

        jLabel84.setText("Cor axis:");
        jLabel84.setName("jLabel84"); // NOI18N
        FormInput.add(jLabel84);
        jLabel84.setBounds(200, 250, 60, 23);

        Gs23.setEditable(false);
        Gs23.setHighlighter(null);
        Gs23.setName("Gs23"); // NOI18N
        Gs23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Gs23ActionPerformed(evt);
            }
        });
        FormInput.add(Gs23);
        Gs23.setBounds(270, 250, 70, 23);

        jLabel85.setText("o");
        jLabel85.setName("jLabel85"); // NOI18N
        FormInput.add(jLabel85);
        jLabel85.setBounds(340, 240, 10, 23);

        jLabel86.setText("CTAR");
        jLabel86.setName("jLabel86"); // NOI18N
        FormInput.add(jLabel86);
        jLabel86.setBounds(360, 250, 40, 23);

        Gs24.setEditable(false);
        Gs24.setHighlighter(null);
        Gs24.setName("Gs24"); // NOI18N
        Gs24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Gs24ActionPerformed(evt);
            }
        });
        FormInput.add(Gs24);
        Gs24.setBounds(410, 250, 70, 23);

        jLabel87.setText("%");
        jLabel87.setName("jLabel87"); // NOI18N
        FormInput.add(jLabel87);
        jLabel87.setBounds(480, 250, 20, 23);

        jLabel88.setText("4CV:");
        jLabel88.setName("jLabel88"); // NOI18N
        FormInput.add(jLabel88);
        jLabel88.setBounds(510, 250, 30, 23);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("AMNIOTIC FLUID INDEX");
        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput.add(jLabel29);
        jLabel29.setBounds(10, 280, 140, 23);

        Gs25.setEditable(false);
        Gs25.setHighlighter(null);
        Gs25.setName("Gs25"); // NOI18N
        FormInput.add(Gs25);
        Gs25.setBounds(60, 300, 70, 23);

        jLabel30.setText("Q1:");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(20, 300, 30, 23);

        jLabel89.setText("cm");
        jLabel89.setName("jLabel89"); // NOI18N
        FormInput.add(jLabel89);
        jLabel89.setBounds(140, 300, 20, 23);

        jLabel90.setText("Q2:");
        jLabel90.setName("jLabel90"); // NOI18N
        FormInput.add(jLabel90);
        jLabel90.setBounds(170, 300, 30, 23);

        Gs26.setEditable(false);
        Gs26.setHighlighter(null);
        Gs26.setName("Gs26"); // NOI18N
        FormInput.add(Gs26);
        Gs26.setBounds(210, 300, 70, 23);

        jLabel91.setText("cm");
        jLabel91.setName("jLabel91"); // NOI18N
        FormInput.add(jLabel91);
        jLabel91.setBounds(290, 300, 20, 23);

        jLabel92.setText("Q4:");
        jLabel92.setName("jLabel92"); // NOI18N
        FormInput.add(jLabel92);
        jLabel92.setBounds(480, 300, 30, 23);

        Gs27.setEditable(false);
        Gs27.setHighlighter(null);
        Gs27.setName("Gs27"); // NOI18N
        FormInput.add(Gs27);
        Gs27.setBounds(370, 300, 70, 23);

        jLabel93.setText("cm");
        jLabel93.setName("jLabel93"); // NOI18N
        FormInput.add(jLabel93);
        jLabel93.setBounds(450, 300, 20, 23);

        Gs28.setEditable(false);
        Gs28.setHighlighter(null);
        Gs28.setName("Gs28"); // NOI18N
        FormInput.add(Gs28);
        Gs28.setBounds(520, 300, 70, 23);

        jLabel94.setText("cm");
        jLabel94.setName("jLabel94"); // NOI18N
        FormInput.add(jLabel94);
        jLabel94.setBounds(600, 300, 20, 23);

        jLabel95.setText("Q3:");
        jLabel95.setName("jLabel95"); // NOI18N
        FormInput.add(jLabel95);
        jLabel95.setBounds(330, 300, 30, 23);

        jLabel96.setText("TOTAL AFI:");
        jLabel96.setName("jLabel96"); // NOI18N
        FormInput.add(jLabel96);
        jLabel96.setBounds(640, 300, 60, 23);

        Gs29.setEditable(false);
        Gs29.setHighlighter(null);
        Gs29.setName("Gs29"); // NOI18N
        FormInput.add(Gs29);
        Gs29.setBounds(710, 300, 70, 23);

        jLabel97.setText("cm");
        jLabel97.setName("jLabel97"); // NOI18N
        FormInput.add(jLabel97);
        jLabel97.setBounds(790, 300, 20, 23);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel35.setText(":");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(240, 420, 10, 23);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel38.setText(":");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(240, 460, 10, 23);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("BIOMETRI/ UKURAN ");
        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(10, 80, 120, 23);

        jLabel25.setText("GS:");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(20, 100, 30, 23);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("GS: Gestational Sack");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(940, 100, 150, 23);

        TglSkrining1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "31-10-2020" }));
        TglSkrining1.setDisplayFormat("dd-MM-yyyy");
        TglSkrining1.setName("TglSkrining1"); // NOI18N
        TglSkrining1.setOpaque(false);
        TglSkrining1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglSkrining1KeyPressed(evt);
            }
        });
        FormInput.add(TglSkrining1);
        TglSkrining1.setBounds(60, 220, 90, 23);

        jLabel41.setText("minggu");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(810, 100, 40, 23);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
       /* if(NoRM.getText().trim().equals("")||NamaPasien.getText().trim().equals("")){
            Valid.textKosong(NoRM,"Pasien");
        }else if(kdptg.getText().trim().equals("")||nmptg.getText().trim().equals("")){
            Valid.textKosong(BtnPtg,"Petugas");
        }else{
            if(Sequel.menyimpantf("deteksi_dini_corona","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","No.Rawat",19,new String[]{
                    NoRawat.getText(),Valid.SetTgl(TglSkrining.getSelectedItem()+""),kdptg.getText(), A1.getSelectedItem().toString(),A2.getSelectedItem().toString(), 
                    A3.getSelectedItem().toString(),(A3.getSelectedIndex()==0?"0000-00-00":Valid.SetTgl(TglGejala.getSelectedItem()+"")),(A3.getSelectedIndex()==0?"":RiwayatSakitSebelumnya.getText()),
                    (A3.getSelectedIndex()==0?"":RiwayatPeriksaSebelumnya.getText()),B1.getSelectedItem().toString(),(B1.getSelectedIndex()==0?"":DiagnosaKehamilan.getText()),
                    (B1.getSelectedIndex()==0?"0000-00-00":Valid.SetTgl(TglKedatangan.getSelectedItem()+"")),B2a.getSelectedItem().toString(),B2b.getSelectedItem().toString(),
                    B2c.getSelectedItem().toString(),B2d.getSelectedItem().toString(),B2e.getSelectedItem().toString(),Rtl.getSelectedItem().toString(),JadwalKontrol.getSelectedItem().toString()
                })==true){
                if(JadwalKontrol.getSelectedIndex()==0){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    DlgRujuk dlgrjk=new DlgRujuk(null,false);
                    dlgrjk.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                    dlgrjk.setLocationRelativeTo(internalFrame1);
                    dlgrjk.emptTeks();
                    dlgrjk.isCek();
                    dlgrjk.setNoRm(NoRawat.getText(),DTPCari1.getDate(),DTPCari2.getDate());
                    dlgrjk.tampil();
                    dlgrjk.setVisible(true);
                    this.setCursor(Cursor.getDefaultCursor());
                }else if(JadwalKontrol.getSelectedIndex()==0){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    akses.setstatus(true);
                    DlgKamarInap dlgki=new DlgKamarInap(null,false);
                    dlgki.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
                    dlgki.setLocationRelativeTo(internalFrame1);
                    dlgki.emptTeks();
                    dlgki.isCek();
                    dlgki.setNoRm(NoRawat.getText(),NoRM.getText(),NamaPasien.getText());   
                    dlgki.setVisible(true);
                    this.setCursor(Cursor.getDefaultCursor());
                }
                emptTeks();
                tampil();
            }
        } */
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
           //Valid.pindah(evt,Comorbid,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        ChkInput.setSelected(true);
        isForm(); 
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()> -1){ 
            Sequel.meghapus("deteksi_dini_corona","no_rawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            tampil();
        }else{
            JOptionPane.showMessageDialog(null,"Maaf silahkan pilih data terlebih dahulu..!!");
        }
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnPrint);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnPrint,TCari);}
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
            Valid.MyReportqry("rptDeteksiDiniCorona.jasper","report","::[ Deteksi Dini Pasien Corona ]::",
                    "select deteksi_dini_corona.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) asal,"+
                    "pasien.no_ktp,pasien.no_tlp,pasien.pekerjaan,pasien.tgl_lahir,deteksi_dini_corona.tanggal,deteksi_dini_corona.nip,petugas.nama,deteksi_dini_corona.gejala_demam,deteksi_dini_corona.gejala_batuk,"+
                    "deteksi_dini_corona.gejala_sesak,deteksi_dini_corona.gejala_tanggal_pertama,deteksi_dini_corona.gejala_riwayat_sakit,deteksi_dini_corona.gejala_riwayat_periksa,"+
                    "deteksi_dini_corona.faktor_riwayat_perjalanan,deteksi_dini_corona.faktor_asal_daerah,deteksi_dini_corona.faktor_tanggal_kedatangan,deteksi_dini_corona.faktor_paparan_kontakpositif,"+
                    "deteksi_dini_corona.faktor_paparan_kontakpdp,deteksi_dini_corona.faktor_paparan_faskespositif,deteksi_dini_corona.faktor_paparan_perjalananln,deteksi_dini_corona.faktor_paparan_pasarhewan,"+
                    "deteksi_dini_corona.kesimpulan,deteksi_dini_corona.tindak_lanjut from pasien inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
                    "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "+
                    "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "+
                    "inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join deteksi_dini_corona on deteksi_dini_corona.no_rawat=reg_periksa.no_rawat "+
                    "inner join petugas on deteksi_dini_corona.nip=petugas.nip "+
                    "where deteksi_dini_corona.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+
                    (TCari.getText().trim().equals("")?"":"and (deteksi_dini_corona.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                    "pasien.no_rkm_medis like '%"+TCari.getText().trim()+"%' or pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                    "pasien.no_tlp like '%"+TCari.getText().trim()+"%' or deteksi_dini_corona.kesimpulan like '%"+TCari.getText().trim()+"%' or "+
                    "deteksi_dini_corona.tindak_lanjut like '%"+TCari.getText().trim()+"%')")+" order by deteksi_dini_corona.tanggal",param);
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnCari, NamaPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

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

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
       /* if(NoRM.getText().trim().equals("")||NamaPasien.getText().trim().equals("")){
            Valid.textKosong(NoRM,"Pasien");
        }else if(kdptg.getText().trim().equals("")||nmptg.getText().trim().equals("")){
            Valid.textKosong(BtnPtg,"Petugas");
        }else{
            if(tbObat.getSelectedRow()> -1){ 
                if(Sequel.mengedittf("deteksi_dini_corona","no_rawat=?","no_rawat=?,tanggal=?,nip=?,gejala_demam=?,gejala_batuk=?,gejala_sesak=?,gejala_tanggal_pertama=?,gejala_riwayat_sakit=?,gejala_riwayat_periksa=?,faktor_riwayat_perjalanan=?,faktor_asal_daerah=?,faktor_tanggal_kedatangan=?,faktor_paparan_kontakpositif=?,faktor_paparan_kontakpdp=?,faktor_paparan_faskespositif=?,faktor_paparan_perjalananln=?,faktor_paparan_pasarhewan=?,kesimpulan=?,tindak_lanjut=?",20,new String[]{
                        NoRawat.getText(),Valid.SetTgl(TglSkrining.getSelectedItem()+""),kdptg.getText(), A1.getSelectedItem().toString(),A2.getSelectedItem().toString(), 
                        A3.getSelectedItem().toString(),(A3.getSelectedIndex()==0?"0000-00-00":Valid.SetTgl(TglGejala.getSelectedItem()+"")),(A3.getSelectedIndex()==0?"":RiwayatSakitSebelumnya.getText()),
                        (A3.getSelectedIndex()==0?"":RiwayatPeriksaSebelumnya.getText()),B1.getSelectedItem().toString(),(B1.getSelectedIndex()==0?"":DiagnosaKehamilan.getText()),
                        (B1.getSelectedIndex()==0?"0000-00-00":Valid.SetTgl(TglKedatangan.getSelectedItem()+"")),B2a.getSelectedItem().toString(),B2b.getSelectedItem().toString(),
                        B2c.getSelectedItem().toString(),B2d.getSelectedItem().toString(),B2e.getSelectedItem().toString(),Rtl.getSelectedItem().toString(),JadwalKontrol.getSelectedItem().toString(),
                        tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
                    })==true){
                    emptTeks();
                    tampil();
                }
            }else{
                JOptionPane.showMessageDialog(null,"Maaf silahkan pilih data terlebih dahulu..!!");
            }
        } */
    }//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnKeluar);
        }
    }//GEN-LAST:event_BtnEditKeyPressed

    private void A1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A1KeyPressed
        Valid.pindah(evt,TglSkrining,A2);
    }//GEN-LAST:event_A1KeyPressed

    private void A2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A2KeyPressed
       // Valid.pindah(evt,A1,A3);
    }//GEN-LAST:event_A2KeyPressed

    private void TglSkriningKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglSkriningKeyPressed
        Valid.pindah(evt,TCari,A1);
    }//GEN-LAST:event_TglSkriningKeyPressed

    private void DiagnosaKehamilanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaKehamilanKeyPressed
       // Valid.pindah(evt,B1,TglKedatangan);
    }//GEN-LAST:event_DiagnosaKehamilanKeyPressed

    private void B1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_B1KeyPressed
        Valid.pindah(evt,RiwayatPeriksaSebelumnya,DiagnosaKehamilan);
    }//GEN-LAST:event_B1KeyPressed

    private void RiwayatPeriksaSebelumnyaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RiwayatPeriksaSebelumnyaKeyPressed
      //  Valid.pindah(evt,RiwayatSakitSebelumnya,B1);
    }//GEN-LAST:event_RiwayatPeriksaSebelumnyaKeyPressed

    private void RtlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RtlKeyPressed
      //  Valid.pindah(evt,B2e,JadwalKontrol);
    }//GEN-LAST:event_RtlKeyPressed

    private void JadwalKontrolKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JadwalKontrolKeyPressed
        Valid.pindah(evt,Rtl,BtnSimpan);
    }//GEN-LAST:event_JadwalKontrolKeyPressed

    private void kdptgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdptgKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kdptgKeyPressed

    private void BtnPtgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPtgActionPerformed
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_BtnPtgActionPerformed

    private void BtnPtgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPtgKeyPressed
        Valid.pindah(evt,JadwalKontrol,BtnSimpan);
    }//GEN-LAST:event_BtnPtgKeyPressed

    private void A1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A1ItemStateChanged
        
    }//GEN-LAST:event_A1ItemStateChanged

    private void A2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A2ItemStateChanged
        
    }//GEN-LAST:event_A2ItemStateChanged

    private void B1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_B1ItemStateChanged
        
    }//GEN-LAST:event_B1ItemStateChanged

    private void MnCetakDeteksiDiniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCetakDeteksiDiniActionPerformed
        if(NamaPasien.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu pasien...!!!");
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            param.put("finger",Sequel.cariIsi("select sha1(sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",kdptg.getText()));
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReportqry("rptDeteksiCorona.jasper","report","::[ Form Deteksi Dini Corona ]::",
                "select deteksi_dini_corona.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) asal,pasien.umur,pasien.agama,"+
                "pasien.no_ktp,pasien.no_tlp,pasien.pekerjaan,date_format(pasien.tgl_lahir,'%d/%m/%Y')as tgl_lahir,date_format(deteksi_dini_corona.tanggal,'%d/%m/%Y')as tanggal,deteksi_dini_corona.nip,petugas.nama,deteksi_dini_corona.gejala_demam,deteksi_dini_corona.gejala_batuk,"+
                "deteksi_dini_corona.gejala_sesak,deteksi_dini_corona.gejala_tanggal_pertama,deteksi_dini_corona.gejala_riwayat_sakit,deteksi_dini_corona.gejala_riwayat_periksa,pasien.jk,pasien.stts_nikah,"+
                "deteksi_dini_corona.faktor_riwayat_perjalanan,deteksi_dini_corona.faktor_asal_daerah,deteksi_dini_corona.faktor_tanggal_kedatangan,deteksi_dini_corona.faktor_paparan_kontakpositif,"+
                "deteksi_dini_corona.faktor_paparan_kontakpdp,deteksi_dini_corona.faktor_paparan_faskespositif,deteksi_dini_corona.faktor_paparan_perjalananln,deteksi_dini_corona.faktor_paparan_pasarhewan,"+
                "deteksi_dini_corona.kesimpulan,deteksi_dini_corona.tindak_lanjut from pasien inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
                "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "+
                "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "+
                "inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                "inner join deteksi_dini_corona on deteksi_dini_corona.no_rawat=reg_periksa.no_rawat "+
                "inner join petugas on deteksi_dini_corona.nip=petugas.nip "+
                "where deteksi_dini_corona.no_rawat='"+NoRawat.getText()+"' ",param);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_MnCetakDeteksiDiniActionPerformed

    private void Gs17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Gs17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Gs17ActionPerformed

    private void Gs20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Gs20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Gs20ActionPerformed

    private void Gs23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Gs23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Gs23ActionPerformed

    private void Gs24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Gs24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Gs24ActionPerformed

    private void TglSkrining1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglSkrining1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TglSkrining1KeyPressed

    private void TglSkriningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TglSkriningActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TglSkriningActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMPemeriksaanUsgTransabdominal dialog = new RMPemeriksaanUsgTransabdominal(new javax.swing.JFrame(), true);
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
    private widget.ComboBox A1;
    private widget.ComboBox A2;
    private widget.TextBox Alamat;
    private widget.ComboBox B1;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnPtg;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextBox DiagnosaKehamilan;
    private widget.PanelBiasa FormInput;
    private widget.TextBox Gs;
    private widget.TextBox Gs1;
    private widget.TextBox Gs10;
    private widget.TextBox Gs11;
    private widget.TextBox Gs12;
    private widget.TextBox Gs13;
    private widget.TextBox Gs14;
    private widget.TextBox Gs15;
    private widget.TextBox Gs16;
    private widget.TextBox Gs17;
    private widget.TextBox Gs18;
    private widget.TextBox Gs19;
    private widget.TextBox Gs2;
    private widget.TextBox Gs20;
    private widget.TextBox Gs21;
    private widget.TextBox Gs22;
    private widget.TextBox Gs23;
    private widget.TextBox Gs24;
    private widget.TextBox Gs25;
    private widget.TextBox Gs26;
    private widget.TextBox Gs27;
    private widget.TextBox Gs28;
    private widget.TextBox Gs29;
    private widget.TextBox Gs3;
    private widget.TextBox Gs4;
    private widget.TextBox Gs5;
    private widget.TextBox Gs6;
    private widget.TextBox Gs7;
    private widget.TextBox Gs8;
    private widget.TextBox Gs9;
    private widget.ComboBox JadwalKontrol;
    private widget.Label LCount;
    private javax.swing.JMenuItem MnCetakDeteksiDini;
    private widget.TextBox NamaPasien;
    private widget.TextBox NoRM;
    private widget.TextBox NoRawat;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox RiwayatPeriksaSebelumnya;
    private widget.ComboBox Rtl;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.Tanggal TglSkrining;
    private widget.Tanggal TglSkrining1;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel49;
    private widget.Label jLabel50;
    private widget.Label jLabel51;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel57;
    private widget.Label jLabel58;
    private widget.Label jLabel59;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel61;
    private widget.Label jLabel62;
    private widget.Label jLabel63;
    private widget.Label jLabel64;
    private widget.Label jLabel65;
    private widget.Label jLabel66;
    private widget.Label jLabel67;
    private widget.Label jLabel68;
    private widget.Label jLabel69;
    private widget.Label jLabel7;
    private widget.Label jLabel70;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel73;
    private widget.Label jLabel74;
    private widget.Label jLabel75;
    private widget.Label jLabel76;
    private widget.Label jLabel77;
    private widget.Label jLabel78;
    private widget.Label jLabel79;
    private widget.Label jLabel80;
    private widget.Label jLabel81;
    private widget.Label jLabel82;
    private widget.Label jLabel83;
    private widget.Label jLabel84;
    private widget.Label jLabel85;
    private widget.Label jLabel86;
    private widget.Label jLabel87;
    private widget.Label jLabel88;
    private widget.Label jLabel89;
    private widget.Label jLabel9;
    private widget.Label jLabel90;
    private widget.Label jLabel91;
    private widget.Label jLabel92;
    private widget.Label jLabel93;
    private widget.Label jLabel94;
    private widget.Label jLabel95;
    private widget.Label jLabel96;
    private widget.Label jLabel97;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.TextBox kdptg;
    private widget.TextBox nmptg;
    private widget.panelisi panelGlass7;
    private widget.panelisi panelGlass8;
    private widget.ScrollPane scrollInput;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables

    public void tampil() {     
        Valid.tabelKosong(tabMode);
        try{    
            ps=koneksi.prepareStatement(
                    "select deteksi_dini_corona.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) asal,"+
                    "pasien.no_ktp,pasien.no_tlp,pasien.pekerjaan,pasien.tgl_lahir,deteksi_dini_corona.tanggal,deteksi_dini_corona.nip,petugas.nama,deteksi_dini_corona.gejala_demam,deteksi_dini_corona.gejala_batuk,"+
                    "deteksi_dini_corona.gejala_sesak,deteksi_dini_corona.gejala_tanggal_pertama,deteksi_dini_corona.gejala_riwayat_sakit,deteksi_dini_corona.gejala_riwayat_periksa,"+
                    "deteksi_dini_corona.faktor_riwayat_perjalanan,deteksi_dini_corona.faktor_asal_daerah,deteksi_dini_corona.faktor_tanggal_kedatangan,deteksi_dini_corona.faktor_paparan_kontakpositif,"+
                    "deteksi_dini_corona.faktor_paparan_kontakpdp,deteksi_dini_corona.faktor_paparan_faskespositif,deteksi_dini_corona.faktor_paparan_perjalananln,deteksi_dini_corona.faktor_paparan_pasarhewan,"+
                    "deteksi_dini_corona.kesimpulan,deteksi_dini_corona.tindak_lanjut from pasien inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
                    "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "+
                    "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "+
                    "inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join deteksi_dini_corona on deteksi_dini_corona.no_rawat=reg_periksa.no_rawat "+
                    "inner join petugas on deteksi_dini_corona.nip=petugas.nip "+
                    "where deteksi_dini_corona.tanggal between ? and ? "+
                    (TCari.getText().trim().equals("")?"":"and (deteksi_dini_corona.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or pasien.no_tlp like ? or "+
                    "deteksi_dini_corona.kesimpulan like ? or deteksi_dini_corona.tindak_lanjut like ?)")+"order by deteksi_dini_corona.tanggal");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().trim().equals("")){
                    ps.setString(3,"%"+TCari.getText().trim()+"%");
                    ps.setString(4,"%"+TCari.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText().trim()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,"%"+TCari.getText().trim()+"%");
                    ps.setString(8,"%"+TCari.getText().trim()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){    
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("asal"),rs.getString("no_ktp"),rs.getString("no_tlp"),
                        rs.getString("pekerjaan"),rs.getString("tgl_lahir"),rs.getString("tanggal"),rs.getString("nip"),rs.getString("nama"),rs.getString("gejala_demam"),
                        rs.getString("gejala_batuk"),rs.getString("gejala_sesak"),rs.getString("gejala_tanggal_pertama"),rs.getString("gejala_riwayat_sakit"),rs.getString("gejala_riwayat_periksa"),
                        rs.getString("faktor_riwayat_perjalanan"),rs.getString("faktor_asal_daerah"),rs.getString("faktor_tanggal_kedatangan"),rs.getString("faktor_paparan_kontakpositif"),
                        rs.getString("faktor_paparan_kontakpdp"),rs.getString("faktor_paparan_faskespositif"),rs.getString("faktor_paparan_perjalananln"),rs.getString("faktor_paparan_pasarhewan"),
                        rs.getString("kesimpulan"),rs.getString("tindak_lanjut")
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
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }

    public void emptTeks() {
        NoRM.setText("");
        NamaPasien.setText("");
        NoRawat.setText("");
        Gs.setText("");
        //NoHP.setText("");
        //Pekerjaan.setText("");
        //TglLahir.setText("");
        Alamat.setText("");
        TglSkrining.setDate(new Date());
        A1.setSelectedIndex(0);
        A2.setSelectedIndex(0);
        //A3.setSelectedIndex(0);
        //TglGejala.setDate(new Date());
        RiwayatPeriksaSebelumnya.setText("");
        //RiwayatSakitSebelumnya.setText("");
        B1.setSelectedIndex(0);
        DiagnosaKehamilan.setText("");
      //  TglKedatangan.setDate(new Date());
      //  B2a.setSelectedIndex(0);
       // B2b.setSelectedIndex(0);
      //  B2c.setSelectedIndex(0);
      //  B2d.setSelectedIndex(0);
       // B2e.setSelectedIndex(0);
        Rtl.setSelectedIndex(3);
        JadwalKontrol.setSelectedIndex(0);
       // HasilLaborat.setText("");
       // HasilRadiologi.setText("");
        ChkInput.setSelected(true);
        isForm(); 
        A1.requestFocus();
    }

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){      
            NoRawat.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            NoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            NamaPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            Alamat.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            Gs.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            
            kdptg.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            nmptg.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            A1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            A2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
           // A3.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            //RiwayatSakitSebelumnya.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            RiwayatPeriksaSebelumnya.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            B1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
            DiagnosaKehamilan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
            //B2a.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
            //B2b.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());
            //B2c.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
            //B2d.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString());
            //B2e.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),24).toString());
            Rtl.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),25).toString());
            JadwalKontrol.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),26).toString());
            //isLabRad(NoRawat.getText());
            Valid.SetTgl(TglSkrining,tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
           // Valid.SetTgl(TglGejala,tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
           // Valid.SetTgl(TglKedatangan,tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());
        }
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,this.getHeight()-122));
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
        BtnSimpan.setEnabled(akses.getusg_transabdominal());
        BtnHapus.setEnabled(akses.getusg_transabdominal());
        BtnEdit.setEnabled(akses.getusg_transabdominal());
        if(akses.getjml2()>=1){
            kdptg.setEditable(false);
            BtnPtg.setEnabled(false);
            kdptg.setText(akses.getkode());
            Sequel.cariIsi("select nama from petugas where nip=?", nmptg,kdptg.getText());
        }
    }
    
    public void setNoRm(String norawat,Date tgl2){
        NoRawat.setText(norawat);
        TCari.setText(norawat);
        try {
            ps=koneksi.prepareStatement(
                    "select pasien.no_rkm_medis,pasien.nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) asal,"+
                    "pasien.no_ktp,pasien.no_tlp,pasien.pekerjaan,pasien.tgl_lahir "+
                    "from pasien inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
                    "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "+
                    "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "+
                    "inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis where reg_periksa.no_rawat=?");
            try {            
                ps.setString(1,norawat);
                rs=ps.executeQuery();
                while(rs.next()){
                    NoRM.setText(rs.getString("no_rkm_medis"));
                    NamaPasien.setText(rs.getString("nm_pasien"));
                    Gs.setText(rs.getString("no_ktp"));
                   // NoHP.setText(rs.getString("no_tlp"));
                    //Pekerjaan.setText(rs.getString("pekerjaan"));
                    Alamat.setText(rs.getString("asal"));
                    //TglLahir.setText(rs.getString("tgl_lahir"));
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }finally{
                if(rs != null ){
                    rs.close();
                }
                
                if(ps != null ){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        DTPCari2.setDate(tgl2);  
        //isLabRad(norawat);
    }
    
    
    public JTable getTable(){
        return tbObat;
    }
}
    
