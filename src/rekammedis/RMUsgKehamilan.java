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
public class RMUsgKehamilan extends javax.swing.JDialog {
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
    public RMUsgKehamilan(java.awt.Frame parent, boolean modal) {
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
        Gs = new widget.TextBox();
        jLabel11 = new widget.Label();
        jLabel16 = new widget.Label();
        Alamat = new widget.TextBox();
        jLabel18 = new widget.Label();
        TglSkrining = new widget.Tanggal();
        jLabel34 = new widget.Label();
        jLabel51 = new widget.Label();
        kdptg = new widget.TextBox();
        nmptg = new widget.TextBox();
        BtnPtg = new widget.Button();
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
        jLabel46 = new widget.Label();
        Gs6 = new widget.TextBox();
        jLabel47 = new widget.Label();
        jLabel55 = new widget.Label();
        Gs8 = new widget.TextBox();
        jLabel56 = new widget.Label();
        jLabel13 = new widget.Label();
        Gs10 = new widget.TextBox();
        jLabel14 = new widget.Label();
        jLabel59 = new widget.Label();
        Gs11 = new widget.TextBox();
        jLabel60 = new widget.Label();
        jLabel63 = new widget.Label();
        Gs13 = new widget.TextBox();
        jLabel64 = new widget.Label();
        jLabel32 = new widget.Label();
        Gs28 = new widget.TextBox();
        Gs29 = new widget.TextBox();
        jLabel38 = new widget.Label();
        jLabel28 = new widget.Label();
        jLabel25 = new widget.Label();
        TglSkrining1 = new widget.Tanggal();
        jLabel41 = new widget.Label();
        jLabel43 = new widget.Label();
        Gs30 = new widget.TextBox();
        jLabel45 = new widget.Label();
        Gs31 = new widget.TextBox();
        jLabel52 = new widget.Label();
        Gs32 = new widget.TextBox();
        jLabel99 = new widget.Label();
        Gs33 = new widget.TextBox();
        jLabel101 = new widget.Label();
        Gs34 = new widget.TextBox();
        jLabel102 = new widget.Label();
        Gs35 = new widget.TextBox();
        jLabel26 = new widget.Label();
        A3 = new widget.ComboBox();
        jLabel27 = new widget.Label();
        A4 = new widget.ComboBox();
        jLabel44 = new widget.Label();
        A5 = new widget.ComboBox();
        jLabel48 = new widget.Label();
        A6 = new widget.ComboBox();
        jLabel98 = new widget.Label();
        jLabel100 = new widget.Label();
        A7 = new widget.ComboBox();
        jLabel103 = new widget.Label();
        A9 = new widget.ComboBox();
        jLabel105 = new widget.Label();
        jLabel106 = new widget.Label();
        jLabel107 = new widget.Label();
        jLabel108 = new widget.Label();
        A10 = new widget.ComboBox();
        jLabel104 = new widget.Label();
        A8 = new widget.ComboBox();
        jLabel109 = new widget.Label();
        A11 = new widget.ComboBox();
        Gs5 = new widget.TextBox();
        jLabel23 = new widget.Label();
        jLabel24 = new widget.Label();
        Gs7 = new widget.TextBox();
        jLabel29 = new widget.Label();
        jLabel30 = new widget.Label();
        Gs9 = new widget.TextBox();
        jLabel33 = new widget.Label();
        jLabel35 = new widget.Label();
        Gs12 = new widget.TextBox();
        jLabel37 = new widget.Label();
        jLabel40 = new widget.Label();
        Gs14 = new widget.TextBox();
        jLabel49 = new widget.Label();
        Gs15 = new widget.TextBox();
        jLabel50 = new widget.Label();
        jLabel57 = new widget.Label();
        Gs16 = new widget.TextBox();
        jLabel58 = new widget.Label();
        jLabel42 = new widget.Label();
        Gs17 = new widget.TextBox();
        jLabel53 = new widget.Label();
        jLabel61 = new widget.Label();
        Gs18 = new widget.TextBox();
        jLabel62 = new widget.Label();
        jLabel65 = new widget.Label();
        Gs19 = new widget.TextBox();
        jLabel66 = new widget.Label();
        jLabel54 = new widget.Label();
        jLabel67 = new widget.Label();
        Gs20 = new widget.TextBox();
        jLabel68 = new widget.Label();
        jLabel69 = new widget.Label();
        Gs21 = new widget.TextBox();
        jLabel70 = new widget.Label();
        jLabel71 = new widget.Label();
        Gs22 = new widget.TextBox();
        jLabel72 = new widget.Label();
        jLabel73 = new widget.Label();
        Gs23 = new widget.TextBox();
        jLabel74 = new widget.Label();
        jLabel75 = new widget.Label();
        Gs24 = new widget.TextBox();
        jLabel76 = new widget.Label();
        Gs25 = new widget.TextBox();
        jLabel77 = new widget.Label();
        jLabel78 = new widget.Label();
        Gs26 = new widget.TextBox();
        jLabel79 = new widget.Label();
        jLabel80 = new widget.Label();
        Gs27 = new widget.TextBox();
        jLabel81 = new widget.Label();
        jLabel82 = new widget.Label();
        Gs36 = new widget.TextBox();
        jLabel83 = new widget.Label();
        jLabel84 = new widget.Label();
        Gs37 = new widget.TextBox();
        jLabel85 = new widget.Label();
        jLabel86 = new widget.Label();
        jLabel87 = new widget.Label();
        Gs38 = new widget.TextBox();
        jLabel88 = new widget.Label();
        jLabel89 = new widget.Label();
        Gs39 = new widget.TextBox();
        jLabel90 = new widget.Label();
        jLabel91 = new widget.Label();
        Gs40 = new widget.TextBox();
        jLabel92 = new widget.Label();
        jLabel93 = new widget.Label();
        Gs41 = new widget.TextBox();
        jLabel94 = new widget.Label();
        jLabel95 = new widget.Label();
        Gs42 = new widget.TextBox();
        jLabel110 = new widget.Label();
        Gs44 = new widget.TextBox();
        jLabel111 = new widget.Label();
        jLabel112 = new widget.Label();
        Gs45 = new widget.TextBox();
        jLabel113 = new widget.Label();
        jLabel114 = new widget.Label();
        Gs46 = new widget.TextBox();
        jLabel115 = new widget.Label();
        jLabel116 = new widget.Label();
        Gs47 = new widget.TextBox();
        jLabel117 = new widget.Label();
        jLabel118 = new widget.Label();
        jLabel119 = new widget.Label();
        Gs43 = new widget.TextBox();
        Gs48 = new widget.TextBox();
        Gs49 = new widget.TextBox();
        Gs50 = new widget.TextBox();
        Gs51 = new widget.TextBox();
        Gs52 = new widget.TextBox();
        Gs53 = new widget.TextBox();
        Gs54 = new widget.TextBox();
        Gs55 = new widget.TextBox();
        Gs56 = new widget.TextBox();
        Gs57 = new widget.TextBox();
        Gs58 = new widget.TextBox();
        Gs59 = new widget.TextBox();
        Gs60 = new widget.TextBox();
        Gs61 = new widget.TextBox();
        Gs62 = new widget.TextBox();
        Gs63 = new widget.TextBox();
        jLabel96 = new widget.Label();
        jLabel97 = new widget.Label();
        Gs64 = new widget.TextBox();
        jLabel120 = new widget.Label();
        jLabel121 = new widget.Label();
        Gs65 = new widget.TextBox();
        jLabel122 = new widget.Label();
        jLabel123 = new widget.Label();
        Gs66 = new widget.TextBox();
        jLabel124 = new widget.Label();
        jLabel125 = new widget.Label();
        Gs67 = new widget.TextBox();
        jLabel126 = new widget.Label();
        Gs68 = new widget.TextBox();
        jLabel127 = new widget.Label();
        jLabel128 = new widget.Label();
        Gs69 = new widget.TextBox();
        jLabel129 = new widget.Label();
        jLabel130 = new widget.Label();
        Gs70 = new widget.TextBox();
        jLabel131 = new widget.Label();
        jLabel132 = new widget.Label();
        Gs71 = new widget.TextBox();
        jLabel133 = new widget.Label();
        jLabel134 = new widget.Label();
        Gs72 = new widget.TextBox();
        jLabel135 = new widget.Label();
        jLabel136 = new widget.Label();
        jLabel137 = new widget.Label();
        jLabel138 = new widget.Label();
        Gs73 = new widget.TextBox();
        jLabel139 = new widget.Label();
        jLabel140 = new widget.Label();
        Gs74 = new widget.TextBox();
        jLabel141 = new widget.Label();
        jLabel142 = new widget.Label();
        Gs75 = new widget.TextBox();
        jLabel143 = new widget.Label();
        jLabel144 = new widget.Label();
        Gs76 = new widget.TextBox();
        jLabel145 = new widget.Label();
        jLabel146 = new widget.Label();
        Gs77 = new widget.TextBox();
        jLabel147 = new widget.Label();
        Gs78 = new widget.TextBox();
        jLabel148 = new widget.Label();
        jLabel149 = new widget.Label();
        Gs79 = new widget.TextBox();
        jLabel150 = new widget.Label();
        jLabel151 = new widget.Label();
        Gs80 = new widget.TextBox();
        jLabel152 = new widget.Label();
        jLabel153 = new widget.Label();
        Gs81 = new widget.TextBox();
        jLabel154 = new widget.Label();
        jLabel155 = new widget.Label();
        Gs82 = new widget.TextBox();
        jLabel156 = new widget.Label();
        jLabel157 = new widget.Label();
        jLabel158 = new widget.Label();
        Gs83 = new widget.TextBox();
        jLabel159 = new widget.Label();
        jLabel160 = new widget.Label();
        Gs84 = new widget.TextBox();
        jLabel161 = new widget.Label();
        jLabel162 = new widget.Label();
        Gs85 = new widget.TextBox();
        jLabel163 = new widget.Label();
        jLabel164 = new widget.Label();
        Gs86 = new widget.TextBox();
        jLabel165 = new widget.Label();
        jLabel166 = new widget.Label();
        Gs87 = new widget.TextBox();
        jLabel167 = new widget.Label();
        Gs88 = new widget.TextBox();
        jLabel168 = new widget.Label();
        jLabel169 = new widget.Label();
        Gs89 = new widget.TextBox();
        jLabel170 = new widget.Label();
        jLabel171 = new widget.Label();
        Gs90 = new widget.TextBox();
        jLabel172 = new widget.Label();
        jLabel173 = new widget.Label();
        Gs91 = new widget.TextBox();
        jLabel174 = new widget.Label();
        jLabel175 = new widget.Label();
        Gs92 = new widget.TextBox();
        jLabel176 = new widget.Label();
        jLabel177 = new widget.Label();
        jLabel178 = new widget.Label();
        Gs93 = new widget.TextBox();
        jLabel179 = new widget.Label();
        jLabel180 = new widget.Label();
        Gs94 = new widget.TextBox();
        jLabel181 = new widget.Label();
        jLabel182 = new widget.Label();
        Gs95 = new widget.TextBox();
        jLabel183 = new widget.Label();
        jLabel184 = new widget.Label();
        Gs96 = new widget.TextBox();
        jLabel185 = new widget.Label();
        jLabel186 = new widget.Label();
        Gs97 = new widget.TextBox();
        jLabel187 = new widget.Label();
        Gs98 = new widget.TextBox();
        jLabel188 = new widget.Label();
        jLabel189 = new widget.Label();
        Gs99 = new widget.TextBox();
        jLabel190 = new widget.Label();
        jLabel191 = new widget.Label();
        Gs100 = new widget.TextBox();
        jLabel192 = new widget.Label();
        jLabel193 = new widget.Label();
        Gs101 = new widget.TextBox();
        jLabel194 = new widget.Label();
        jLabel195 = new widget.Label();
        jLabel196 = new widget.Label();
        Gs102 = new widget.TextBox();
        Gs103 = new widget.TextBox();
        Gs104 = new widget.TextBox();
        Gs105 = new widget.TextBox();
        Gs106 = new widget.TextBox();
        Gs107 = new widget.TextBox();
        Gs108 = new widget.TextBox();
        Gs109 = new widget.TextBox();
        Gs110 = new widget.TextBox();
        Gs111 = new widget.TextBox();
        Gs112 = new widget.TextBox();
        Gs113 = new widget.TextBox();
        Gs114 = new widget.TextBox();
        Gs115 = new widget.TextBox();
        Gs116 = new widget.TextBox();
        Gs117 = new widget.TextBox();
        Gs118 = new widget.TextBox();
        jLabel197 = new widget.Label();
        jLabel198 = new widget.Label();
        Gs119 = new widget.TextBox();
        jLabel199 = new widget.Label();
        jLabel200 = new widget.Label();
        Gs120 = new widget.TextBox();
        jLabel201 = new widget.Label();
        jLabel202 = new widget.Label();
        Gs121 = new widget.TextBox();
        jLabel203 = new widget.Label();
        jLabel204 = new widget.Label();
        Gs122 = new widget.TextBox();
        jLabel205 = new widget.Label();
        Gs123 = new widget.TextBox();
        jLabel206 = new widget.Label();
        jLabel207 = new widget.Label();
        Gs124 = new widget.TextBox();
        jLabel208 = new widget.Label();
        jLabel209 = new widget.Label();
        Gs125 = new widget.TextBox();
        jLabel210 = new widget.Label();
        jLabel211 = new widget.Label();
        Gs126 = new widget.TextBox();
        jLabel212 = new widget.Label();
        jLabel213 = new widget.Label();
        Gs127 = new widget.TextBox();
        jLabel214 = new widget.Label();
        jLabel215 = new widget.Label();
        jLabel216 = new widget.Label();
        jLabel217 = new widget.Label();
        Gs128 = new widget.TextBox();
        jLabel218 = new widget.Label();
        jLabel219 = new widget.Label();
        Gs129 = new widget.TextBox();
        jLabel220 = new widget.Label();
        jLabel221 = new widget.Label();
        Gs130 = new widget.TextBox();
        jLabel222 = new widget.Label();
        jLabel223 = new widget.Label();
        Gs131 = new widget.TextBox();
        jLabel224 = new widget.Label();
        jLabel225 = new widget.Label();
        Gs132 = new widget.TextBox();
        jLabel226 = new widget.Label();
        Gs133 = new widget.TextBox();
        jLabel227 = new widget.Label();
        jLabel228 = new widget.Label();
        Gs134 = new widget.TextBox();
        jLabel229 = new widget.Label();
        jLabel230 = new widget.Label();
        Gs135 = new widget.TextBox();
        jLabel231 = new widget.Label();
        jLabel232 = new widget.Label();
        Gs136 = new widget.TextBox();
        jLabel233 = new widget.Label();
        jLabel234 = new widget.Label();
        Gs137 = new widget.TextBox();
        jLabel235 = new widget.Label();
        jLabel236 = new widget.Label();
        jLabel237 = new widget.Label();
        Gs138 = new widget.TextBox();
        jLabel238 = new widget.Label();
        jLabel239 = new widget.Label();
        Gs139 = new widget.TextBox();
        jLabel240 = new widget.Label();
        jLabel241 = new widget.Label();
        Gs140 = new widget.TextBox();
        jLabel242 = new widget.Label();
        jLabel243 = new widget.Label();
        Gs141 = new widget.TextBox();
        jLabel244 = new widget.Label();
        jLabel245 = new widget.Label();
        Gs142 = new widget.TextBox();
        jLabel246 = new widget.Label();
        Gs143 = new widget.TextBox();
        jLabel247 = new widget.Label();
        jLabel248 = new widget.Label();
        Gs144 = new widget.TextBox();
        jLabel249 = new widget.Label();
        jLabel250 = new widget.Label();
        Gs145 = new widget.TextBox();
        jLabel251 = new widget.Label();
        jLabel252 = new widget.Label();
        Gs146 = new widget.TextBox();
        jLabel253 = new widget.Label();
        jLabel254 = new widget.Label();
        Gs147 = new widget.TextBox();
        jLabel255 = new widget.Label();
        jLabel256 = new widget.Label();
        jLabel257 = new widget.Label();
        Gs148 = new widget.TextBox();
        jLabel258 = new widget.Label();
        jLabel259 = new widget.Label();
        Gs149 = new widget.TextBox();
        jLabel260 = new widget.Label();
        jLabel261 = new widget.Label();
        Gs150 = new widget.TextBox();
        jLabel262 = new widget.Label();
        jLabel263 = new widget.Label();
        Gs151 = new widget.TextBox();
        jLabel264 = new widget.Label();
        jLabel265 = new widget.Label();
        Gs152 = new widget.TextBox();
        jLabel266 = new widget.Label();
        Gs153 = new widget.TextBox();
        jLabel267 = new widget.Label();
        jLabel268 = new widget.Label();
        Gs154 = new widget.TextBox();
        jLabel269 = new widget.Label();
        jLabel270 = new widget.Label();
        Gs155 = new widget.TextBox();
        jLabel271 = new widget.Label();
        jLabel272 = new widget.Label();
        Gs156 = new widget.TextBox();
        jLabel273 = new widget.Label();
        jLabel274 = new widget.Label();
        jLabel275 = new widget.Label();
        Gs157 = new widget.TextBox();
        Gs158 = new widget.TextBox();
        Gs159 = new widget.TextBox();
        Gs160 = new widget.TextBox();
        Gs161 = new widget.TextBox();
        Gs162 = new widget.TextBox();
        Gs163 = new widget.TextBox();
        Gs164 = new widget.TextBox();
        Gs165 = new widget.TextBox();
        Gs166 = new widget.TextBox();
        Gs167 = new widget.TextBox();
        Gs168 = new widget.TextBox();
        Gs169 = new widget.TextBox();
        Gs170 = new widget.TextBox();
        Gs171 = new widget.TextBox();
        Gs172 = new widget.TextBox();
        jLabel276 = new widget.Label();
        jLabel277 = new widget.Label();
        jLabel278 = new widget.Label();
        jLabel279 = new widget.Label();
        jLabel280 = new widget.Label();
        A12 = new widget.ComboBox();
        jLabel281 = new widget.Label();
        A13 = new widget.ComboBox();
        jLabel282 = new widget.Label();
        A14 = new widget.ComboBox();
        jLabel283 = new widget.Label();
        A15 = new widget.ComboBox();
        jLabel284 = new widget.Label();
        jLabel285 = new widget.Label();
        A16 = new widget.ComboBox();
        jLabel286 = new widget.Label();
        A17 = new widget.ComboBox();
        jLabel287 = new widget.Label();
        jLabel288 = new widget.Label();
        A18 = new widget.ComboBox();
        jLabel289 = new widget.Label();
        jLabel290 = new widget.Label();
        A19 = new widget.ComboBox();
        jLabel291 = new widget.Label();
        A20 = new widget.ComboBox();
        jLabel292 = new widget.Label();
        A21 = new widget.ComboBox();
        jLabel293 = new widget.Label();
        A22 = new widget.ComboBox();
        jLabel295 = new widget.Label();
        A24 = new widget.ComboBox();
        jLabel294 = new widget.Label();
        A23 = new widget.ComboBox();
        jLabel296 = new widget.Label();
        A25 = new widget.ComboBox();
        jLabel297 = new widget.Label();
        Gs173 = new widget.TextBox();
        jLabel298 = new widget.Label();
        jLabel299 = new widget.Label();
        A26 = new widget.ComboBox();
        jLabel300 = new widget.Label();
        A27 = new widget.ComboBox();
        jLabel301 = new widget.Label();
        A28 = new widget.ComboBox();
        jLabel302 = new widget.Label();
        A29 = new widget.ComboBox();
        jLabel303 = new widget.Label();
        A30 = new widget.ComboBox();
        jLabel304 = new widget.Label();
        A31 = new widget.ComboBox();
        jLabel305 = new widget.Label();
        A32 = new widget.ComboBox();
        jLabel306 = new widget.Label();
        Gs174 = new widget.TextBox();
        jLabel307 = new widget.Label();
        A33 = new widget.ComboBox();
        jLabel308 = new widget.Label();
        jLabel309 = new widget.Label();
        A34 = new widget.ComboBox();
        A35 = new widget.ComboBox();
        jLabel310 = new widget.Label();
        jLabel311 = new widget.Label();
        A36 = new widget.ComboBox();
        A37 = new widget.ComboBox();
        jLabel312 = new widget.Label();
        jLabel313 = new widget.Label();
        A38 = new widget.ComboBox();
        A39 = new widget.ComboBox();
        jLabel314 = new widget.Label();
        jLabel315 = new widget.Label();
        A40 = new widget.ComboBox();
        A41 = new widget.ComboBox();
        jLabel316 = new widget.Label();
        jLabel317 = new widget.Label();
        A42 = new widget.ComboBox();
        A43 = new widget.ComboBox();
        jLabel318 = new widget.Label();
        jLabel319 = new widget.Label();
        A44 = new widget.ComboBox();
        A45 = new widget.ComboBox();
        jLabel320 = new widget.Label();
        jLabel321 = new widget.Label();
        A46 = new widget.ComboBox();
        A47 = new widget.ComboBox();
        jLabel322 = new widget.Label();
        jLabel323 = new widget.Label();
        A48 = new widget.ComboBox();
        A49 = new widget.ComboBox();
        jLabel324 = new widget.Label();
        jLabel325 = new widget.Label();
        A50 = new widget.ComboBox();
        A51 = new widget.ComboBox();
        jLabel326 = new widget.Label();
        jLabel327 = new widget.Label();
        A52 = new widget.ComboBox();
        A53 = new widget.ComboBox();
        jLabel328 = new widget.Label();
        A54 = new widget.ComboBox();
        jLabel329 = new widget.Label();
        A55 = new widget.ComboBox();
        jLabel330 = new widget.Label();
        A56 = new widget.ComboBox();
        jLabel331 = new widget.Label();
        A57 = new widget.ComboBox();
        jLabel332 = new widget.Label();
        A58 = new widget.ComboBox();
        jLabel333 = new widget.Label();
        A59 = new widget.ComboBox();
        jLabel334 = new widget.Label();
        A60 = new widget.ComboBox();
        jLabel335 = new widget.Label();
        A61 = new widget.ComboBox();
        jLabel336 = new widget.Label();
        A62 = new widget.ComboBox();
        jLabel337 = new widget.Label();
        A63 = new widget.ComboBox();
        jLabel338 = new widget.Label();
        A64 = new widget.ComboBox();
        jLabel339 = new widget.Label();
        A65 = new widget.ComboBox();
        jLabel340 = new widget.Label();
        A66 = new widget.ComboBox();
        jLabel341 = new widget.Label();
        A67 = new widget.ComboBox();
        jLabel342 = new widget.Label();
        A68 = new widget.ComboBox();
        jLabel343 = new widget.Label();
        A69 = new widget.ComboBox();

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

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ USG Kehamilan ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
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

        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "27-11-2020" }));
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

        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "27-11-2020" }));
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

        A1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Satu", "Dua", "Tiga" }));
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
        A1.setBounds(110, 140, 80, 23);

        Gs.setEditable(false);
        Gs.setHighlighter(null);
        Gs.setName("Gs"); // NOI18N
        FormInput.add(Gs);
        Gs.setBounds(200, 1380, 120, 23);

        jLabel11.setText("cm");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(170, 360, 20, 23);

        jLabel16.setText("Alamat :");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 72, 23);

        Alamat.setEditable(false);
        Alamat.setHighlighter(null);
        Alamat.setName("Alamat"); // NOI18N
        FormInput.add(Alamat);
        Alamat.setBounds(80, 40, 557, 23);

        jLabel18.setText("Jml Gestasional sac:");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(10, 140, 100, 23);

        TglSkrining.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "27-11-2020" }));
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
        TglSkrining.setBounds(710, 40, 90, 23);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText(":");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput.add(jLabel34);
        jLabel34.setBounds(500, 220, 10, 23);

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel51.setText("Petugas ");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput.add(jLabel51);
        jLabel51.setBounds(230, 70, 70, 23);

        kdptg.setEditable(false);
        kdptg.setHighlighter(null);
        kdptg.setName("kdptg"); // NOI18N
        kdptg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdptgKeyPressed(evt);
            }
        });
        FormInput.add(kdptg);
        kdptg.setBounds(320, 70, 110, 23);

        nmptg.setEditable(false);
        nmptg.setHighlighter(null);
        nmptg.setName("nmptg"); // NOI18N
        FormInput.add(nmptg);
        nmptg.setBounds(430, 70, 190, 23);

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
        BtnPtg.setBounds(620, 70, 28, 23);

        jLabel12.setText("CRL:");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(50, 420, 30, 23);

        Gs1.setEditable(false);
        Gs1.setHighlighter(null);
        Gs1.setName("Gs1"); // NOI18N
        FormInput.add(Gs1);
        Gs1.setBounds(90, 420, 70, 23);

        jLabel19.setText("cm");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput.add(jLabel19);
        jLabel19.setBounds(170, 420, 20, 23);

        jLabel20.setText("Cereb:");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(40, 510, 40, 23);

        Gs2.setEditable(false);
        Gs2.setHighlighter(null);
        Gs2.setName("Gs2"); // NOI18N
        FormInput.add(Gs2);
        Gs2.setBounds(90, 510, 70, 23);

        jLabel21.setText("mm");
        jLabel21.setName("jLabel21"); // NOI18N
        FormInput.add(jLabel21);
        jLabel21.setBounds(170, 510, 20, 23);

        jLabel22.setText("YS:");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(50, 390, 30, 23);

        Gs3.setEditable(false);
        Gs3.setHighlighter(null);
        Gs3.setName("Gs3"); // NOI18N
        FormInput.add(Gs3);
        Gs3.setBounds(90, 390, 70, 23);

        jLabel31.setText("cm");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(170, 390, 20, 23);

        jLabel36.setText("BPD:");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(50, 450, 30, 23);

        Gs4.setEditable(false);
        Gs4.setHighlighter(null);
        Gs4.setName("Gs4"); // NOI18N
        FormInput.add(Gs4);
        Gs4.setBounds(90, 450, 70, 23);

        jLabel39.setText("HPHT:");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(650, 40, 50, 23);

        jLabel46.setText("Ulna:");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(50, 630, 30, 23);

        Gs6.setEditable(false);
        Gs6.setHighlighter(null);
        Gs6.setName("Gs6"); // NOI18N
        FormInput.add(Gs6);
        Gs6.setBounds(90, 630, 70, 23);

        jLabel47.setText("cm");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(170, 630, 20, 23);

        jLabel55.setText("HC:");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(50, 540, 30, 23);

        Gs8.setEditable(false);
        Gs8.setHighlighter(null);
        Gs8.setName("Gs8"); // NOI18N
        FormInput.add(Gs8);
        Gs8.setBounds(90, 540, 70, 23);

        jLabel56.setText("cm");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(170, 540, 20, 23);

        jLabel13.setText("OFD:");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(50, 480, 30, 23);

        Gs10.setEditable(false);
        Gs10.setHighlighter(null);
        Gs10.setName("Gs10"); // NOI18N
        FormInput.add(Gs10);
        Gs10.setBounds(90, 480, 70, 23);

        jLabel14.setText("cm");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(170, 480, 20, 23);

        jLabel59.setText("HUM:");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(50, 600, 30, 23);

        Gs11.setEditable(false);
        Gs11.setHighlighter(null);
        Gs11.setName("Gs11"); // NOI18N
        FormInput.add(Gs11);
        Gs11.setBounds(90, 600, 70, 23);

        jLabel60.setText("cm");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(170, 600, 20, 23);

        jLabel63.setText("AC:");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(50, 570, 30, 23);

        Gs13.setEditable(false);
        Gs13.setHighlighter(null);
        Gs13.setName("Gs13"); // NOI18N
        FormInput.add(Gs13);
        Gs13.setBounds(90, 570, 70, 23);

        jLabel64.setText("cm");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(170, 570, 20, 23);

        jLabel32.setText("EDD USG:");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(740, 300, 50, 23);

        Gs28.setEditable(false);
        Gs28.setHighlighter(null);
        Gs28.setName("Gs28"); // NOI18N
        FormInput.add(Gs28);
        Gs28.setBounds(860, 190, 220, 23);

        Gs29.setEditable(false);
        Gs29.setHighlighter(null);
        Gs29.setName("Gs29"); // NOI18N
        Gs29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Gs29ActionPerformed(evt);
            }
        });
        FormInput.add(Gs29);
        Gs29.setBounds(400, 190, 190, 23);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(260, 700, 10, 23);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("GESTASIONAL SAC (GS)");
        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(10, 170, 140, 23);

        jLabel25.setText("GS:");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(50, 360, 30, 23);

        TglSkrining1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "27-11-2020" }));
        TglSkrining1.setDisplayFormat("dd-MM-yyyy");
        TglSkrining1.setName("TglSkrining1"); // NOI18N
        TglSkrining1.setOpaque(false);
        TglSkrining1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglSkrining1KeyPressed(evt);
            }
        });
        FormInput.add(TglSkrining1);
        TglSkrining1.setBounds(800, 300, 90, 23);

        jLabel41.setText("cm");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(170, 450, 20, 23);

        jLabel43.setText("Gravida:");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(20, 110, 50, 23);

        Gs30.setEditable(false);
        Gs30.setHighlighter(null);
        Gs30.setName("Gs30"); // NOI18N
        FormInput.add(Gs30);
        Gs30.setBounds(80, 110, 70, 23);

        jLabel45.setText("Para:");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(160, 110, 30, 23);

        Gs31.setEditable(false);
        Gs31.setHighlighter(null);
        Gs31.setName("Gs31"); // NOI18N
        FormInput.add(Gs31);
        Gs31.setBounds(200, 110, 70, 23);

        jLabel52.setText("Premature:");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(270, 110, 70, 23);

        Gs32.setEditable(false);
        Gs32.setHighlighter(null);
        Gs32.setName("Gs32"); // NOI18N
        FormInput.add(Gs32);
        Gs32.setBounds(350, 110, 70, 23);

        jLabel99.setText("Immature:");
        jLabel99.setName("jLabel99"); // NOI18N
        FormInput.add(jLabel99);
        jLabel99.setBounds(420, 110, 70, 23);

        Gs33.setEditable(false);
        Gs33.setHighlighter(null);
        Gs33.setName("Gs33"); // NOI18N
        FormInput.add(Gs33);
        Gs33.setBounds(500, 110, 70, 23);

        jLabel101.setText("Abortus:");
        jLabel101.setName("jLabel101"); // NOI18N
        FormInput.add(jLabel101);
        jLabel101.setBounds(580, 110, 60, 23);

        Gs34.setEditable(false);
        Gs34.setHighlighter(null);
        Gs34.setName("Gs34"); // NOI18N
        FormInput.add(Gs34);
        Gs34.setBounds(650, 110, 70, 23);

        jLabel102.setText("Anak hidup:");
        jLabel102.setName("jLabel102"); // NOI18N
        FormInput.add(jLabel102);
        jLabel102.setBounds(730, 110, 60, 23);

        Gs35.setEditable(false);
        Gs35.setHighlighter(null);
        Gs35.setName("Gs35"); // NOI18N
        FormInput.add(Gs35);
        Gs35.setBounds(800, 110, 70, 23);

        jLabel26.setText("Jml Janin:");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(200, 140, 60, 23);

        A3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Satu", "Dua", "Tiga" }));
        A3.setName("A3"); // NOI18N
        A3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A3ItemStateChanged(evt);
            }
        });
        A3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A3KeyPressed(evt);
            }
        });
        FormInput.add(A3);
        A3.setBounds(260, 140, 80, 23);

        jLabel27.setText("Penampakan GS:");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(10, 190, 90, 23);

        A4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tampak GS", "Belum tampak GS", "Tidak tampak GS" }));
        A4.setName("A4"); // NOI18N
        A4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A4ItemStateChanged(evt);
            }
        });
        A4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A4KeyPressed(evt);
            }
        });
        FormInput.add(A4);
        A4.setBounds(100, 190, 120, 23);

        jLabel44.setText("Lokasi GS:");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(230, 190, 50, 23);

        A5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Intrauterine", "Ectopic", "Extrauterine", "Uterine scar pregnancy", "Keterangan tambahan" }));
        A5.setName("A5"); // NOI18N
        A5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A5ItemStateChanged(evt);
            }
        });
        A5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A5KeyPressed(evt);
            }
        });
        FormInput.add(A5);
        A5.setBounds(280, 190, 120, 23);

        jLabel48.setText("Kelainan pada GS:");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(600, 190, 100, 23);

        A6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak tampak kelainan", "Dinding irregular", "Subchoroionic bleeding", "Keterangan tambahan" }));
        A6.setName("A6"); // NOI18N
        A6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A6ItemStateChanged(evt);
            }
        });
        A6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A6KeyPressed(evt);
            }
        });
        FormInput.add(A6);
        A6.setBounds(700, 190, 160, 23);

        jLabel98.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel98.setText("III");
        jLabel98.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel98.setName("jLabel98"); // NOI18N
        FormInput.add(jLabel98);
        jLabel98.setBounds(750, 330, 20, 23);

        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel100.setText("KATEGORI KEHAMILAN");
        jLabel100.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel100.setName("jLabel100"); // NOI18N
        FormInput.add(jLabel100);
        jLabel100.setBounds(10, 80, 140, 23);

        A7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak tampak kelainan", "Ukuran YS 2-8 mm", "Small yolk cac <= 2mm", "Calcified yolk sac", "Large yolk sac >= 8 mm", "Floating yolk sac", "Solid echodense yolk sac" }));
        A7.setName("A7"); // NOI18N
        A7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A7ItemStateChanged(evt);
            }
        });
        A7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A7KeyPressed(evt);
            }
        });
        FormInput.add(A7);
        A7.setBounds(380, 250, 160, 23);

        jLabel103.setText("Kelainan pada YS:");
        jLabel103.setName("jLabel103"); // NOI18N
        FormInput.add(jLabel103);
        jLabel103.setBounds(280, 250, 100, 23);

        A9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tampak YS", "Tak tampak YS", "Tidak ada YS" }));
        A9.setName("A9"); // NOI18N
        A9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A9ItemStateChanged(evt);
            }
        });
        A9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A9ActionPerformed(evt);
            }
        });
        A9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A9KeyPressed(evt);
            }
        });
        FormInput.add(A9);
        A9.setBounds(140, 250, 130, 23);

        jLabel105.setText("Penampakan YS dalam GS:");
        jLabel105.setName("jLabel105"); // NOI18N
        FormInput.add(jLabel105);
        jLabel105.setBounds(10, 250, 130, 23);

        jLabel106.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel106.setText("YOLK SAC (YS)");
        jLabel106.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel106.setName("jLabel106"); // NOI18N
        FormInput.add(jLabel106);
        jLabel106.setBounds(10, 230, 140, 23);

        jLabel107.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel107.setText("JANIN");
        jLabel107.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel107.setName("jLabel107"); // NOI18N
        FormInput.add(jLabel107);
        jLabel107.setBounds(10, 280, 140, 23);

        jLabel108.setText("Penampakan janin dalam GS:");
        jLabel108.setName("jLabel108"); // NOI18N
        FormInput.add(jLabel108);
        jLabel108.setBounds(0, 300, 150, 23);

        A10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Belum tampak janin", "Tidak tampak janin", "Tampak janin" }));
        A10.setName("A10"); // NOI18N
        A10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A10ItemStateChanged(evt);
            }
        });
        A10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A10ActionPerformed(evt);
            }
        });
        A10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A10KeyPressed(evt);
            }
        });
        FormInput.add(A10);
        A10.setBounds(150, 300, 130, 23);

        jLabel104.setText("Detak jantung janin:");
        jLabel104.setName("jLabel104"); // NOI18N
        FormInput.add(jLabel104);
        jLabel104.setBounds(280, 300, 100, 23);

        A8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak ada" }));
        A8.setName("A8"); // NOI18N
        A8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A8ItemStateChanged(evt);
            }
        });
        A8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A8KeyPressed(evt);
            }
        });
        FormInput.add(A8);
        A8.setBounds(380, 300, 90, 23);

        jLabel109.setText(" Gerakan Janin:");
        jLabel109.setName("jLabel109"); // NOI18N
        FormInput.add(jLabel109);
        jLabel109.setBounds(490, 300, 100, 23);

        A11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak ada" }));
        A11.setName("A11"); // NOI18N
        A11.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A11ItemStateChanged(evt);
            }
        });
        A11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A11KeyPressed(evt);
            }
        });
        FormInput.add(A11);
        A11.setBounds(590, 300, 90, 23);

        Gs5.setEditable(false);
        Gs5.setHighlighter(null);
        Gs5.setName("Gs5"); // NOI18N
        FormInput.add(Gs5);
        Gs5.setBounds(90, 660, 70, 23);

        jLabel23.setText("cm");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(170, 660, 20, 23);

        jLabel24.setText("Radius:");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(40, 720, 40, 23);

        Gs7.setEditable(false);
        Gs7.setHighlighter(null);
        Gs7.setName("Gs7"); // NOI18N
        FormInput.add(Gs7);
        Gs7.setBounds(90, 720, 70, 23);

        jLabel29.setText("cm");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput.add(jLabel29);
        jLabel29.setBounds(170, 720, 20, 23);

        jLabel30.setText("AFI-Q1:");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(40, 810, 40, 23);

        Gs9.setEditable(false);
        Gs9.setHighlighter(null);
        Gs9.setName("Gs9"); // NOI18N
        FormInput.add(Gs9);
        Gs9.setBounds(90, 810, 70, 23);

        jLabel33.setText("mm");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(170, 810, 20, 23);

        jLabel35.setText("Fibula:");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(40, 690, 40, 23);

        Gs12.setEditable(false);
        Gs12.setHighlighter(null);
        Gs12.setName("Gs12"); // NOI18N
        FormInput.add(Gs12);
        Gs12.setBounds(90, 690, 70, 23);

        jLabel37.setText("cm");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(170, 690, 20, 23);

        jLabel40.setText("FL:");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput.add(jLabel40);
        jLabel40.setBounds(50, 750, 30, 23);

        Gs14.setEditable(false);
        Gs14.setHighlighter(null);
        Gs14.setName("Gs14"); // NOI18N
        FormInput.add(Gs14);
        Gs14.setBounds(90, 750, 70, 23);

        jLabel49.setText("TOTAL AFI:");
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(20, 930, 60, 23);

        Gs15.setEditable(false);
        Gs15.setHighlighter(null);
        Gs15.setName("Gs15"); // NOI18N
        FormInput.add(Gs15);
        Gs15.setBounds(90, 930, 70, 23);

        jLabel50.setText("cm");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(170, 930, 20, 23);

        jLabel57.setText("AFI-Q2:");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(40, 840, 39, 23);

        Gs16.setEditable(false);
        Gs16.setHighlighter(null);
        Gs16.setName("Gs16"); // NOI18N
        FormInput.add(Gs16);
        Gs16.setBounds(90, 840, 70, 23);

        jLabel58.setText("cm");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(170, 840, 20, 23);

        jLabel42.setText("NT:");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(50, 780, 30, 23);

        Gs17.setEditable(false);
        Gs17.setHighlighter(null);
        Gs17.setName("Gs17"); // NOI18N
        FormInput.add(Gs17);
        Gs17.setBounds(90, 780, 70, 23);

        jLabel53.setText("cm");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(170, 780, 20, 23);

        jLabel61.setText("AFI-Q4:");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(40, 900, 40, 23);

        Gs18.setEditable(false);
        Gs18.setHighlighter(null);
        Gs18.setName("Gs18"); // NOI18N
        FormInput.add(Gs18);
        Gs18.setBounds(90, 900, 70, 23);

        jLabel62.setText("cm");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(170, 900, 20, 23);

        jLabel65.setText("AFI-Q3:");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(30, 870, 50, 23);

        Gs19.setEditable(false);
        Gs19.setHighlighter(null);
        Gs19.setName("Gs19"); // NOI18N
        FormInput.add(Gs19);
        Gs19.setBounds(90, 870, 70, 23);

        jLabel66.setText("cm");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(170, 870, 20, 23);

        jLabel54.setText("Tibia:");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(50, 660, 30, 23);

        jLabel67.setText("cm");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(170, 750, 20, 23);

        Gs20.setEditable(false);
        Gs20.setHighlighter(null);
        Gs20.setName("Gs20"); // NOI18N
        FormInput.add(Gs20);
        Gs20.setBounds(90, 960, 70, 23);

        jLabel68.setText("cm");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(170, 960, 20, 23);

        jLabel69.setText("hrtD ap:");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput.add(jLabel69);
        jLabel69.setBounds(20, 1020, 60, 23);

        Gs21.setEditable(false);
        Gs21.setHighlighter(null);
        Gs21.setName("Gs21"); // NOI18N
        FormInput.add(Gs21);
        Gs21.setBounds(90, 1020, 70, 23);

        jLabel70.setText("cm");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput.add(jLabel70);
        jLabel70.setBounds(170, 1020, 20, 23);

        jLabel71.setText("IT:");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(40, 1110, 40, 23);

        Gs22.setEditable(false);
        Gs22.setHighlighter(null);
        Gs22.setName("Gs22"); // NOI18N
        FormInput.add(Gs22);
        Gs22.setBounds(90, 1110, 70, 23);

        jLabel72.setText("mm");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput.add(jLabel72);
        jLabel72.setBounds(170, 1110, 20, 23);

        jLabel73.setText("ThD trans::");
        jLabel73.setName("jLabel73"); // NOI18N
        FormInput.add(jLabel73);
        jLabel73.setBounds(20, 990, 60, 23);

        Gs23.setEditable(false);
        Gs23.setHighlighter(null);
        Gs23.setName("Gs23"); // NOI18N
        FormInput.add(Gs23);
        Gs23.setBounds(90, 990, 70, 23);

        jLabel74.setText("cm");
        jLabel74.setName("jLabel74"); // NOI18N
        FormInput.add(jLabel74);
        jLabel74.setBounds(170, 990, 20, 23);

        jLabel75.setText("hrtD trans:");
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput.add(jLabel75);
        jLabel75.setBounds(20, 1050, 60, 23);

        Gs24.setEditable(false);
        Gs24.setHighlighter(null);
        Gs24.setName("Gs24"); // NOI18N
        FormInput.add(Gs24);
        Gs24.setBounds(90, 1050, 70, 23);

        jLabel76.setText("NF:");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput.add(jLabel76);
        jLabel76.setBounds(50, 1230, 30, 23);

        Gs25.setEditable(false);
        Gs25.setHighlighter(null);
        Gs25.setName("Gs25"); // NOI18N
        FormInput.add(Gs25);
        Gs25.setBounds(90, 1230, 70, 23);

        jLabel77.setText("cm");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput.add(jLabel77);
        jLabel77.setBounds(170, 1230, 20, 23);

        jLabel78.setText("NB:");
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput.add(jLabel78);
        jLabel78.setBounds(50, 1140, 30, 23);

        Gs26.setEditable(false);
        Gs26.setHighlighter(null);
        Gs26.setName("Gs26"); // NOI18N
        FormInput.add(Gs26);
        Gs26.setBounds(90, 1140, 70, 23);

        jLabel79.setText("cm");
        jLabel79.setName("jLabel79"); // NOI18N
        FormInput.add(jLabel79);
        jLabel79.setBounds(170, 1140, 20, 23);

        jLabel80.setText("CTAR:");
        jLabel80.setName("jLabel80"); // NOI18N
        FormInput.add(jLabel80);
        jLabel80.setBounds(30, 1080, 50, 23);

        Gs27.setEditable(false);
        Gs27.setHighlighter(null);
        Gs27.setName("Gs27"); // NOI18N
        FormInput.add(Gs27);
        Gs27.setBounds(90, 1080, 70, 23);

        jLabel81.setText("cm");
        jLabel81.setName("jLabel81"); // NOI18N
        FormInput.add(jLabel81);
        jLabel81.setBounds(170, 1080, 20, 23);

        jLabel82.setText("IOD:");
        jLabel82.setName("jLabel82"); // NOI18N
        FormInput.add(jLabel82);
        jLabel82.setBounds(50, 1200, 30, 23);

        Gs36.setEditable(false);
        Gs36.setHighlighter(null);
        Gs36.setName("Gs36"); // NOI18N
        FormInput.add(Gs36);
        Gs36.setBounds(90, 1200, 70, 23);

        jLabel83.setText("cm");
        jLabel83.setName("jLabel83"); // NOI18N
        FormInput.add(jLabel83);
        jLabel83.setBounds(170, 1200, 20, 23);

        jLabel84.setText("CM:");
        jLabel84.setName("jLabel84"); // NOI18N
        FormInput.add(jLabel84);
        jLabel84.setBounds(50, 1170, 30, 23);

        Gs37.setEditable(false);
        Gs37.setHighlighter(null);
        Gs37.setName("Gs37"); // NOI18N
        FormInput.add(Gs37);
        Gs37.setBounds(90, 1170, 70, 23);

        jLabel85.setText("cm");
        jLabel85.setName("jLabel85"); // NOI18N
        FormInput.add(jLabel85);
        jLabel85.setBounds(170, 1170, 20, 23);

        jLabel86.setText("ThD ap:");
        jLabel86.setName("jLabel86"); // NOI18N
        FormInput.add(jLabel86);
        jLabel86.setBounds(30, 960, 50, 23);

        jLabel87.setText("cm");
        jLabel87.setName("jLabel87"); // NOI18N
        FormInput.add(jLabel87);
        jLabel87.setBounds(170, 1050, 20, 23);

        Gs38.setEditable(false);
        Gs38.setHighlighter(null);
        Gs38.setName("Gs38"); // NOI18N
        FormInput.add(Gs38);
        Gs38.setBounds(90, 1260, 70, 23);

        jLabel88.setText("cm");
        jLabel88.setName("jLabel88"); // NOI18N
        FormInput.add(jLabel88);
        jLabel88.setBounds(170, 1260, 20, 23);

        jLabel89.setText("FMF angle:");
        jLabel89.setName("jLabel89"); // NOI18N
        FormInput.add(jLabel89);
        jLabel89.setBounds(20, 1320, 60, 23);

        Gs39.setEditable(false);
        Gs39.setHighlighter(null);
        Gs39.setName("Gs39"); // NOI18N
        FormInput.add(Gs39);
        Gs39.setBounds(90, 1320, 70, 23);

        jLabel90.setText("cm");
        jLabel90.setName("jLabel90"); // NOI18N
        FormInput.add(jLabel90);
        jLabel90.setBounds(170, 1320, 20, 23);

        jLabel91.setText("MVP:");
        jLabel91.setName("jLabel91"); // NOI18N
        FormInput.add(jLabel91);
        jLabel91.setBounds(40, 1410, 40, 23);

        Gs40.setEditable(false);
        Gs40.setHighlighter(null);
        Gs40.setName("Gs40"); // NOI18N
        FormInput.add(Gs40);
        Gs40.setBounds(90, 1410, 70, 23);

        jLabel92.setText("mm");
        jLabel92.setName("jLabel92"); // NOI18N
        FormInput.add(jLabel92);
        jLabel92.setBounds(170, 1410, 20, 23);

        jLabel93.setText("Lat Vent:");
        jLabel93.setName("jLabel93"); // NOI18N
        FormInput.add(jLabel93);
        jLabel93.setBounds(20, 1290, 60, 23);

        Gs41.setEditable(false);
        Gs41.setHighlighter(null);
        Gs41.setName("Gs41"); // NOI18N
        FormInput.add(Gs41);
        Gs41.setBounds(90, 1290, 70, 23);

        jLabel94.setText("cm");
        jLabel94.setName("jLabel94"); // NOI18N
        FormInput.add(jLabel94);
        jLabel94.setBounds(170, 1290, 20, 23);

        jLabel95.setText("BOD:");
        jLabel95.setName("jLabel95"); // NOI18N
        FormInput.add(jLabel95);
        jLabel95.setBounds(50, 1350, 30, 23);

        Gs42.setEditable(false);
        Gs42.setHighlighter(null);
        Gs42.setName("Gs42"); // NOI18N
        FormInput.add(Gs42);
        Gs42.setBounds(90, 1350, 70, 23);

        jLabel110.setText("Cardiac axis:");
        jLabel110.setName("jLabel110"); // NOI18N
        FormInput.add(jLabel110);
        jLabel110.setBounds(0, 1440, 80, 23);

        Gs44.setEditable(false);
        Gs44.setHighlighter(null);
        Gs44.setName("Gs44"); // NOI18N
        FormInput.add(Gs44);
        Gs44.setBounds(90, 1440, 70, 23);

        jLabel111.setText("cm");
        jLabel111.setName("jLabel111"); // NOI18N
        FormInput.add(jLabel111);
        jLabel111.setBounds(170, 1440, 20, 23);

        jLabel112.setText("Clav:");
        jLabel112.setName("jLabel112"); // NOI18N
        FormInput.add(jLabel112);
        jLabel112.setBounds(50, 1380, 30, 23);

        Gs45.setEditable(false);
        Gs45.setHighlighter(null);
        Gs45.setName("Gs45"); // NOI18N
        FormInput.add(Gs45);
        Gs45.setBounds(90, 1380, 70, 23);

        jLabel113.setText("cm");
        jLabel113.setName("jLabel113"); // NOI18N
        FormInput.add(jLabel113);
        jLabel113.setBounds(170, 1380, 20, 23);

        jLabel114.setText("Cervical length:");
        jLabel114.setName("jLabel114"); // NOI18N
        FormInput.add(jLabel114);
        jLabel114.setBounds(0, 1500, 80, 23);

        Gs46.setEditable(false);
        Gs46.setHighlighter(null);
        Gs46.setName("Gs46"); // NOI18N
        FormInput.add(Gs46);
        Gs46.setBounds(90, 1500, 70, 23);

        jLabel115.setText("cm");
        jLabel115.setName("jLabel115"); // NOI18N
        FormInput.add(jLabel115);
        jLabel115.setBounds(170, 1500, 20, 23);

        jLabel116.setText("Placenta thick:");
        jLabel116.setName("jLabel116"); // NOI18N
        FormInput.add(jLabel116);
        jLabel116.setBounds(0, 1470, 80, 23);

        Gs47.setEditable(false);
        Gs47.setHighlighter(null);
        Gs47.setName("Gs47"); // NOI18N
        FormInput.add(Gs47);
        Gs47.setBounds(90, 1470, 70, 23);

        jLabel117.setText("cm");
        jLabel117.setName("jLabel117"); // NOI18N
        FormInput.add(jLabel117);
        jLabel117.setBounds(170, 1470, 20, 23);

        jLabel118.setText("CSP:");
        jLabel118.setName("jLabel118"); // NOI18N
        FormInput.add(jLabel118);
        jLabel118.setBounds(50, 1260, 30, 23);

        jLabel119.setText("cm");
        jLabel119.setName("jLabel119"); // NOI18N
        FormInput.add(jLabel119);
        jLabel119.setBounds(170, 1350, 20, 23);

        Gs43.setEditable(false);
        Gs43.setHighlighter(null);
        Gs43.setName("Gs43"); // NOI18N
        FormInput.add(Gs43);
        Gs43.setBounds(90, 360, 70, 23);

        Gs48.setEditable(false);
        Gs48.setHighlighter(null);
        Gs48.setName("Gs48"); // NOI18N
        FormInput.add(Gs48);
        Gs48.setBounds(200, 360, 120, 23);

        Gs49.setEditable(false);
        Gs49.setHighlighter(null);
        Gs49.setName("Gs49"); // NOI18N
        FormInput.add(Gs49);
        Gs49.setBounds(200, 420, 120, 23);

        Gs50.setEditable(false);
        Gs50.setHighlighter(null);
        Gs50.setName("Gs50"); // NOI18N
        FormInput.add(Gs50);
        Gs50.setBounds(200, 450, 120, 23);

        Gs51.setEditable(false);
        Gs51.setHighlighter(null);
        Gs51.setName("Gs51"); // NOI18N
        FormInput.add(Gs51);
        Gs51.setBounds(200, 480, 120, 23);

        Gs52.setEditable(false);
        Gs52.setHighlighter(null);
        Gs52.setName("Gs52"); // NOI18N
        FormInput.add(Gs52);
        Gs52.setBounds(200, 510, 120, 23);

        Gs53.setEditable(false);
        Gs53.setHighlighter(null);
        Gs53.setName("Gs53"); // NOI18N
        FormInput.add(Gs53);
        Gs53.setBounds(200, 540, 120, 23);

        Gs54.setEditable(false);
        Gs54.setHighlighter(null);
        Gs54.setName("Gs54"); // NOI18N
        FormInput.add(Gs54);
        Gs54.setBounds(200, 570, 120, 23);

        Gs55.setEditable(false);
        Gs55.setHighlighter(null);
        Gs55.setName("Gs55"); // NOI18N
        FormInput.add(Gs55);
        Gs55.setBounds(200, 600, 120, 23);

        Gs56.setEditable(false);
        Gs56.setHighlighter(null);
        Gs56.setName("Gs56"); // NOI18N
        FormInput.add(Gs56);
        Gs56.setBounds(200, 780, 120, 23);

        Gs57.setEditable(false);
        Gs57.setHighlighter(null);
        Gs57.setName("Gs57"); // NOI18N
        FormInput.add(Gs57);
        Gs57.setBounds(200, 750, 120, 23);

        Gs58.setEditable(false);
        Gs58.setHighlighter(null);
        Gs58.setName("Gs58"); // NOI18N
        FormInput.add(Gs58);
        Gs58.setBounds(200, 720, 120, 23);

        Gs59.setEditable(false);
        Gs59.setHighlighter(null);
        Gs59.setName("Gs59"); // NOI18N
        FormInput.add(Gs59);
        Gs59.setBounds(200, 690, 120, 23);

        Gs60.setEditable(false);
        Gs60.setHighlighter(null);
        Gs60.setName("Gs60"); // NOI18N
        FormInput.add(Gs60);
        Gs60.setBounds(200, 660, 120, 23);

        Gs61.setEditable(false);
        Gs61.setHighlighter(null);
        Gs61.setName("Gs61"); // NOI18N
        FormInput.add(Gs61);
        Gs61.setBounds(200, 630, 120, 23);

        Gs62.setEditable(false);
        Gs62.setHighlighter(null);
        Gs62.setName("Gs62"); // NOI18N
        FormInput.add(Gs62);
        Gs62.setBounds(200, 1350, 120, 23);

        Gs63.setEditable(false);
        Gs63.setHighlighter(null);
        Gs63.setName("Gs63"); // NOI18N
        FormInput.add(Gs63);
        Gs63.setBounds(480, 1380, 120, 23);

        jLabel96.setText("cm");
        jLabel96.setName("jLabel96"); // NOI18N
        FormInput.add(jLabel96);
        jLabel96.setBounds(450, 360, 20, 23);

        jLabel97.setText("CRL:");
        jLabel97.setName("jLabel97"); // NOI18N
        FormInput.add(jLabel97);
        jLabel97.setBounds(330, 420, 30, 23);

        Gs64.setEditable(false);
        Gs64.setHighlighter(null);
        Gs64.setName("Gs64"); // NOI18N
        FormInput.add(Gs64);
        Gs64.setBounds(370, 420, 70, 23);

        jLabel120.setText("cm");
        jLabel120.setName("jLabel120"); // NOI18N
        FormInput.add(jLabel120);
        jLabel120.setBounds(450, 420, 20, 23);

        jLabel121.setText("Cereb:");
        jLabel121.setName("jLabel121"); // NOI18N
        FormInput.add(jLabel121);
        jLabel121.setBounds(320, 510, 40, 23);

        Gs65.setEditable(false);
        Gs65.setHighlighter(null);
        Gs65.setName("Gs65"); // NOI18N
        FormInput.add(Gs65);
        Gs65.setBounds(370, 510, 70, 23);

        jLabel122.setText("mm");
        jLabel122.setName("jLabel122"); // NOI18N
        FormInput.add(jLabel122);
        jLabel122.setBounds(450, 510, 20, 23);

        jLabel123.setText("YS:");
        jLabel123.setName("jLabel123"); // NOI18N
        FormInput.add(jLabel123);
        jLabel123.setBounds(330, 390, 30, 23);

        Gs66.setEditable(false);
        Gs66.setHighlighter(null);
        Gs66.setName("Gs66"); // NOI18N
        FormInput.add(Gs66);
        Gs66.setBounds(370, 390, 70, 23);

        jLabel124.setText("cm");
        jLabel124.setName("jLabel124"); // NOI18N
        FormInput.add(jLabel124);
        jLabel124.setBounds(450, 390, 20, 23);

        jLabel125.setText("BPD:");
        jLabel125.setName("jLabel125"); // NOI18N
        FormInput.add(jLabel125);
        jLabel125.setBounds(330, 450, 30, 23);

        Gs67.setEditable(false);
        Gs67.setHighlighter(null);
        Gs67.setName("Gs67"); // NOI18N
        FormInput.add(Gs67);
        Gs67.setBounds(370, 450, 70, 23);

        jLabel126.setText("Ulna:");
        jLabel126.setName("jLabel126"); // NOI18N
        FormInput.add(jLabel126);
        jLabel126.setBounds(330, 630, 30, 23);

        Gs68.setEditable(false);
        Gs68.setHighlighter(null);
        Gs68.setName("Gs68"); // NOI18N
        FormInput.add(Gs68);
        Gs68.setBounds(370, 630, 70, 23);

        jLabel127.setText("cm");
        jLabel127.setName("jLabel127"); // NOI18N
        FormInput.add(jLabel127);
        jLabel127.setBounds(450, 630, 20, 23);

        jLabel128.setText("HC:");
        jLabel128.setName("jLabel128"); // NOI18N
        FormInput.add(jLabel128);
        jLabel128.setBounds(330, 540, 30, 23);

        Gs69.setEditable(false);
        Gs69.setHighlighter(null);
        Gs69.setName("Gs69"); // NOI18N
        FormInput.add(Gs69);
        Gs69.setBounds(370, 540, 70, 23);

        jLabel129.setText("cm");
        jLabel129.setName("jLabel129"); // NOI18N
        FormInput.add(jLabel129);
        jLabel129.setBounds(450, 540, 20, 23);

        jLabel130.setText("OFD:");
        jLabel130.setName("jLabel130"); // NOI18N
        FormInput.add(jLabel130);
        jLabel130.setBounds(330, 480, 30, 23);

        Gs70.setEditable(false);
        Gs70.setHighlighter(null);
        Gs70.setName("Gs70"); // NOI18N
        FormInput.add(Gs70);
        Gs70.setBounds(370, 480, 70, 23);

        jLabel131.setText("cm");
        jLabel131.setName("jLabel131"); // NOI18N
        FormInput.add(jLabel131);
        jLabel131.setBounds(450, 480, 20, 23);

        jLabel132.setText("HUM:");
        jLabel132.setName("jLabel132"); // NOI18N
        FormInput.add(jLabel132);
        jLabel132.setBounds(330, 600, 30, 23);

        Gs71.setEditable(false);
        Gs71.setHighlighter(null);
        Gs71.setName("Gs71"); // NOI18N
        FormInput.add(Gs71);
        Gs71.setBounds(370, 600, 70, 23);

        jLabel133.setText("cm");
        jLabel133.setName("jLabel133"); // NOI18N
        FormInput.add(jLabel133);
        jLabel133.setBounds(450, 600, 20, 23);

        jLabel134.setText("AC:");
        jLabel134.setName("jLabel134"); // NOI18N
        FormInput.add(jLabel134);
        jLabel134.setBounds(330, 570, 30, 23);

        Gs72.setEditable(false);
        Gs72.setHighlighter(null);
        Gs72.setName("Gs72"); // NOI18N
        FormInput.add(Gs72);
        Gs72.setBounds(370, 570, 70, 23);

        jLabel135.setText("cm");
        jLabel135.setName("jLabel135"); // NOI18N
        FormInput.add(jLabel135);
        jLabel135.setBounds(450, 570, 20, 23);

        jLabel136.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel136.setName("jLabel136"); // NOI18N
        FormInput.add(jLabel136);
        jLabel136.setBounds(540, 700, 10, 23);

        jLabel137.setText("GS:");
        jLabel137.setName("jLabel137"); // NOI18N
        FormInput.add(jLabel137);
        jLabel137.setBounds(330, 360, 30, 23);

        jLabel138.setText("cm");
        jLabel138.setName("jLabel138"); // NOI18N
        FormInput.add(jLabel138);
        jLabel138.setBounds(450, 450, 20, 23);

        Gs73.setEditable(false);
        Gs73.setHighlighter(null);
        Gs73.setName("Gs73"); // NOI18N
        FormInput.add(Gs73);
        Gs73.setBounds(370, 660, 70, 23);

        jLabel139.setText("cm");
        jLabel139.setName("jLabel139"); // NOI18N
        FormInput.add(jLabel139);
        jLabel139.setBounds(450, 660, 20, 23);

        jLabel140.setText("Radius:");
        jLabel140.setName("jLabel140"); // NOI18N
        FormInput.add(jLabel140);
        jLabel140.setBounds(320, 720, 40, 23);

        Gs74.setEditable(false);
        Gs74.setHighlighter(null);
        Gs74.setName("Gs74"); // NOI18N
        FormInput.add(Gs74);
        Gs74.setBounds(370, 720, 70, 23);

        jLabel141.setText("cm");
        jLabel141.setName("jLabel141"); // NOI18N
        FormInput.add(jLabel141);
        jLabel141.setBounds(450, 720, 20, 23);

        jLabel142.setText("AFI-Q1:");
        jLabel142.setName("jLabel142"); // NOI18N
        FormInput.add(jLabel142);
        jLabel142.setBounds(320, 810, 40, 23);

        Gs75.setEditable(false);
        Gs75.setHighlighter(null);
        Gs75.setName("Gs75"); // NOI18N
        FormInput.add(Gs75);
        Gs75.setBounds(370, 810, 70, 23);

        jLabel143.setText("mm");
        jLabel143.setName("jLabel143"); // NOI18N
        FormInput.add(jLabel143);
        jLabel143.setBounds(450, 810, 20, 23);

        jLabel144.setText("Fibula:");
        jLabel144.setName("jLabel144"); // NOI18N
        FormInput.add(jLabel144);
        jLabel144.setBounds(320, 690, 40, 23);

        Gs76.setEditable(false);
        Gs76.setHighlighter(null);
        Gs76.setName("Gs76"); // NOI18N
        FormInput.add(Gs76);
        Gs76.setBounds(370, 690, 70, 23);

        jLabel145.setText("cm");
        jLabel145.setName("jLabel145"); // NOI18N
        FormInput.add(jLabel145);
        jLabel145.setBounds(450, 690, 20, 23);

        jLabel146.setText("FL:");
        jLabel146.setName("jLabel146"); // NOI18N
        FormInput.add(jLabel146);
        jLabel146.setBounds(330, 750, 30, 23);

        Gs77.setEditable(false);
        Gs77.setHighlighter(null);
        Gs77.setName("Gs77"); // NOI18N
        FormInput.add(Gs77);
        Gs77.setBounds(370, 750, 70, 23);

        jLabel147.setText("TOTAL AFI:");
        jLabel147.setName("jLabel147"); // NOI18N
        FormInput.add(jLabel147);
        jLabel147.setBounds(300, 930, 60, 23);

        Gs78.setEditable(false);
        Gs78.setHighlighter(null);
        Gs78.setName("Gs78"); // NOI18N
        FormInput.add(Gs78);
        Gs78.setBounds(370, 930, 70, 23);

        jLabel148.setText("cm");
        jLabel148.setName("jLabel148"); // NOI18N
        FormInput.add(jLabel148);
        jLabel148.setBounds(450, 930, 20, 23);

        jLabel149.setText("AFI-Q2:");
        jLabel149.setName("jLabel149"); // NOI18N
        FormInput.add(jLabel149);
        jLabel149.setBounds(320, 840, 39, 23);

        Gs79.setEditable(false);
        Gs79.setHighlighter(null);
        Gs79.setName("Gs79"); // NOI18N
        FormInput.add(Gs79);
        Gs79.setBounds(370, 840, 70, 23);

        jLabel150.setText("cm");
        jLabel150.setName("jLabel150"); // NOI18N
        FormInput.add(jLabel150);
        jLabel150.setBounds(450, 840, 20, 23);

        jLabel151.setText("NT:");
        jLabel151.setName("jLabel151"); // NOI18N
        FormInput.add(jLabel151);
        jLabel151.setBounds(330, 780, 30, 23);

        Gs80.setEditable(false);
        Gs80.setHighlighter(null);
        Gs80.setName("Gs80"); // NOI18N
        FormInput.add(Gs80);
        Gs80.setBounds(370, 780, 70, 23);

        jLabel152.setText("cm");
        jLabel152.setName("jLabel152"); // NOI18N
        FormInput.add(jLabel152);
        jLabel152.setBounds(450, 780, 20, 23);

        jLabel153.setText("AFI-Q4:");
        jLabel153.setName("jLabel153"); // NOI18N
        FormInput.add(jLabel153);
        jLabel153.setBounds(320, 900, 40, 23);

        Gs81.setEditable(false);
        Gs81.setHighlighter(null);
        Gs81.setName("Gs81"); // NOI18N
        FormInput.add(Gs81);
        Gs81.setBounds(370, 900, 70, 23);

        jLabel154.setText("cm");
        jLabel154.setName("jLabel154"); // NOI18N
        FormInput.add(jLabel154);
        jLabel154.setBounds(450, 900, 20, 23);

        jLabel155.setText("AFI-Q3:");
        jLabel155.setName("jLabel155"); // NOI18N
        FormInput.add(jLabel155);
        jLabel155.setBounds(310, 870, 50, 23);

        Gs82.setEditable(false);
        Gs82.setHighlighter(null);
        Gs82.setName("Gs82"); // NOI18N
        FormInput.add(Gs82);
        Gs82.setBounds(370, 870, 70, 23);

        jLabel156.setText("cm");
        jLabel156.setName("jLabel156"); // NOI18N
        FormInput.add(jLabel156);
        jLabel156.setBounds(450, 870, 20, 23);

        jLabel157.setText("Tibia:");
        jLabel157.setName("jLabel157"); // NOI18N
        FormInput.add(jLabel157);
        jLabel157.setBounds(330, 660, 30, 23);

        jLabel158.setText("cm");
        jLabel158.setName("jLabel158"); // NOI18N
        FormInput.add(jLabel158);
        jLabel158.setBounds(450, 750, 20, 23);

        Gs83.setEditable(false);
        Gs83.setHighlighter(null);
        Gs83.setName("Gs83"); // NOI18N
        FormInput.add(Gs83);
        Gs83.setBounds(370, 960, 70, 23);

        jLabel159.setText("cm");
        jLabel159.setName("jLabel159"); // NOI18N
        FormInput.add(jLabel159);
        jLabel159.setBounds(450, 960, 20, 23);

        jLabel160.setText("hrtD ap:");
        jLabel160.setName("jLabel160"); // NOI18N
        FormInput.add(jLabel160);
        jLabel160.setBounds(300, 1020, 60, 23);

        Gs84.setEditable(false);
        Gs84.setHighlighter(null);
        Gs84.setName("Gs84"); // NOI18N
        FormInput.add(Gs84);
        Gs84.setBounds(370, 1020, 70, 23);

        jLabel161.setText("cm");
        jLabel161.setName("jLabel161"); // NOI18N
        FormInput.add(jLabel161);
        jLabel161.setBounds(450, 1020, 20, 23);

        jLabel162.setText("IT:");
        jLabel162.setName("jLabel162"); // NOI18N
        FormInput.add(jLabel162);
        jLabel162.setBounds(320, 1110, 40, 23);

        Gs85.setEditable(false);
        Gs85.setHighlighter(null);
        Gs85.setName("Gs85"); // NOI18N
        FormInput.add(Gs85);
        Gs85.setBounds(370, 1110, 70, 23);

        jLabel163.setText("mm");
        jLabel163.setName("jLabel163"); // NOI18N
        FormInput.add(jLabel163);
        jLabel163.setBounds(450, 1110, 20, 23);

        jLabel164.setText("ThD trans::");
        jLabel164.setName("jLabel164"); // NOI18N
        FormInput.add(jLabel164);
        jLabel164.setBounds(300, 990, 60, 23);

        Gs86.setEditable(false);
        Gs86.setHighlighter(null);
        Gs86.setName("Gs86"); // NOI18N
        FormInput.add(Gs86);
        Gs86.setBounds(370, 990, 70, 23);

        jLabel165.setText("cm");
        jLabel165.setName("jLabel165"); // NOI18N
        FormInput.add(jLabel165);
        jLabel165.setBounds(450, 990, 20, 23);

        jLabel166.setText("hrtD trans:");
        jLabel166.setName("jLabel166"); // NOI18N
        FormInput.add(jLabel166);
        jLabel166.setBounds(300, 1050, 60, 23);

        Gs87.setEditable(false);
        Gs87.setHighlighter(null);
        Gs87.setName("Gs87"); // NOI18N
        FormInput.add(Gs87);
        Gs87.setBounds(370, 1050, 70, 23);

        jLabel167.setText("NF:");
        jLabel167.setName("jLabel167"); // NOI18N
        FormInput.add(jLabel167);
        jLabel167.setBounds(330, 1230, 30, 23);

        Gs88.setEditable(false);
        Gs88.setHighlighter(null);
        Gs88.setName("Gs88"); // NOI18N
        FormInput.add(Gs88);
        Gs88.setBounds(370, 1230, 70, 23);

        jLabel168.setText("cm");
        jLabel168.setName("jLabel168"); // NOI18N
        FormInput.add(jLabel168);
        jLabel168.setBounds(450, 1230, 20, 23);

        jLabel169.setText("NB:");
        jLabel169.setName("jLabel169"); // NOI18N
        FormInput.add(jLabel169);
        jLabel169.setBounds(330, 1140, 30, 23);

        Gs89.setEditable(false);
        Gs89.setHighlighter(null);
        Gs89.setName("Gs89"); // NOI18N
        FormInput.add(Gs89);
        Gs89.setBounds(370, 1140, 70, 23);

        jLabel170.setText("cm");
        jLabel170.setName("jLabel170"); // NOI18N
        FormInput.add(jLabel170);
        jLabel170.setBounds(450, 1140, 20, 23);

        jLabel171.setText("CTAR:");
        jLabel171.setName("jLabel171"); // NOI18N
        FormInput.add(jLabel171);
        jLabel171.setBounds(310, 1080, 50, 23);

        Gs90.setEditable(false);
        Gs90.setHighlighter(null);
        Gs90.setName("Gs90"); // NOI18N
        FormInput.add(Gs90);
        Gs90.setBounds(370, 1080, 70, 23);

        jLabel172.setText("cm");
        jLabel172.setName("jLabel172"); // NOI18N
        FormInput.add(jLabel172);
        jLabel172.setBounds(450, 1080, 20, 23);

        jLabel173.setText("IOD:");
        jLabel173.setName("jLabel173"); // NOI18N
        FormInput.add(jLabel173);
        jLabel173.setBounds(330, 1200, 30, 23);

        Gs91.setEditable(false);
        Gs91.setHighlighter(null);
        Gs91.setName("Gs91"); // NOI18N
        FormInput.add(Gs91);
        Gs91.setBounds(370, 1200, 70, 23);

        jLabel174.setText("cm");
        jLabel174.setName("jLabel174"); // NOI18N
        FormInput.add(jLabel174);
        jLabel174.setBounds(450, 1200, 20, 23);

        jLabel175.setText("CM:");
        jLabel175.setName("jLabel175"); // NOI18N
        FormInput.add(jLabel175);
        jLabel175.setBounds(330, 1170, 30, 23);

        Gs92.setEditable(false);
        Gs92.setHighlighter(null);
        Gs92.setName("Gs92"); // NOI18N
        FormInput.add(Gs92);
        Gs92.setBounds(370, 1170, 70, 23);

        jLabel176.setText("cm");
        jLabel176.setName("jLabel176"); // NOI18N
        FormInput.add(jLabel176);
        jLabel176.setBounds(450, 1170, 20, 23);

        jLabel177.setText("ThD ap:");
        jLabel177.setName("jLabel177"); // NOI18N
        FormInput.add(jLabel177);
        jLabel177.setBounds(310, 960, 50, 23);

        jLabel178.setText("cm");
        jLabel178.setName("jLabel178"); // NOI18N
        FormInput.add(jLabel178);
        jLabel178.setBounds(450, 1050, 20, 23);

        Gs93.setEditable(false);
        Gs93.setHighlighter(null);
        Gs93.setName("Gs93"); // NOI18N
        FormInput.add(Gs93);
        Gs93.setBounds(370, 1260, 70, 23);

        jLabel179.setText("cm");
        jLabel179.setName("jLabel179"); // NOI18N
        FormInput.add(jLabel179);
        jLabel179.setBounds(450, 1260, 20, 23);

        jLabel180.setText("FMF angle:");
        jLabel180.setName("jLabel180"); // NOI18N
        FormInput.add(jLabel180);
        jLabel180.setBounds(300, 1320, 60, 23);

        Gs94.setEditable(false);
        Gs94.setHighlighter(null);
        Gs94.setName("Gs94"); // NOI18N
        FormInput.add(Gs94);
        Gs94.setBounds(370, 1320, 70, 23);

        jLabel181.setText("cm");
        jLabel181.setName("jLabel181"); // NOI18N
        FormInput.add(jLabel181);
        jLabel181.setBounds(450, 1320, 20, 23);

        jLabel182.setText("MVP:");
        jLabel182.setName("jLabel182"); // NOI18N
        FormInput.add(jLabel182);
        jLabel182.setBounds(320, 1410, 40, 23);

        Gs95.setEditable(false);
        Gs95.setHighlighter(null);
        Gs95.setName("Gs95"); // NOI18N
        FormInput.add(Gs95);
        Gs95.setBounds(370, 1410, 70, 23);

        jLabel183.setText("mm");
        jLabel183.setName("jLabel183"); // NOI18N
        FormInput.add(jLabel183);
        jLabel183.setBounds(450, 1410, 20, 23);

        jLabel184.setText("Lat Vent:");
        jLabel184.setName("jLabel184"); // NOI18N
        FormInput.add(jLabel184);
        jLabel184.setBounds(300, 1290, 60, 23);

        Gs96.setEditable(false);
        Gs96.setHighlighter(null);
        Gs96.setName("Gs96"); // NOI18N
        FormInput.add(Gs96);
        Gs96.setBounds(370, 1290, 70, 23);

        jLabel185.setText("cm");
        jLabel185.setName("jLabel185"); // NOI18N
        FormInput.add(jLabel185);
        jLabel185.setBounds(450, 1290, 20, 23);

        jLabel186.setText("BOD:");
        jLabel186.setName("jLabel186"); // NOI18N
        FormInput.add(jLabel186);
        jLabel186.setBounds(330, 1350, 30, 23);

        Gs97.setEditable(false);
        Gs97.setHighlighter(null);
        Gs97.setName("Gs97"); // NOI18N
        FormInput.add(Gs97);
        Gs97.setBounds(370, 1350, 70, 23);

        jLabel187.setText("Cardiac axis:");
        jLabel187.setName("jLabel187"); // NOI18N
        FormInput.add(jLabel187);
        jLabel187.setBounds(280, 1440, 80, 23);

        Gs98.setEditable(false);
        Gs98.setHighlighter(null);
        Gs98.setName("Gs98"); // NOI18N
        FormInput.add(Gs98);
        Gs98.setBounds(370, 1440, 70, 23);

        jLabel188.setText("cm");
        jLabel188.setName("jLabel188"); // NOI18N
        FormInput.add(jLabel188);
        jLabel188.setBounds(450, 1440, 20, 23);

        jLabel189.setText("Clav:");
        jLabel189.setName("jLabel189"); // NOI18N
        FormInput.add(jLabel189);
        jLabel189.setBounds(330, 1380, 30, 23);

        Gs99.setEditable(false);
        Gs99.setHighlighter(null);
        Gs99.setName("Gs99"); // NOI18N
        FormInput.add(Gs99);
        Gs99.setBounds(370, 1380, 70, 23);

        jLabel190.setText("cm");
        jLabel190.setName("jLabel190"); // NOI18N
        FormInput.add(jLabel190);
        jLabel190.setBounds(450, 1380, 20, 23);

        jLabel191.setText("Cervical length:");
        jLabel191.setName("jLabel191"); // NOI18N
        FormInput.add(jLabel191);
        jLabel191.setBounds(280, 1500, 80, 23);

        Gs100.setEditable(false);
        Gs100.setHighlighter(null);
        Gs100.setName("Gs100"); // NOI18N
        FormInput.add(Gs100);
        Gs100.setBounds(370, 1500, 70, 23);

        jLabel192.setText("cm");
        jLabel192.setName("jLabel192"); // NOI18N
        FormInput.add(jLabel192);
        jLabel192.setBounds(450, 1500, 20, 23);

        jLabel193.setText("Placenta thick:");
        jLabel193.setName("jLabel193"); // NOI18N
        FormInput.add(jLabel193);
        jLabel193.setBounds(280, 1470, 80, 23);

        Gs101.setEditable(false);
        Gs101.setHighlighter(null);
        Gs101.setName("Gs101"); // NOI18N
        FormInput.add(Gs101);
        Gs101.setBounds(370, 1470, 70, 23);

        jLabel194.setText("cm");
        jLabel194.setName("jLabel194"); // NOI18N
        FormInput.add(jLabel194);
        jLabel194.setBounds(450, 1470, 20, 23);

        jLabel195.setText("CSP:");
        jLabel195.setName("jLabel195"); // NOI18N
        FormInput.add(jLabel195);
        jLabel195.setBounds(330, 1260, 30, 23);

        jLabel196.setText("cm");
        jLabel196.setName("jLabel196"); // NOI18N
        FormInput.add(jLabel196);
        jLabel196.setBounds(450, 1350, 20, 23);

        Gs102.setEditable(false);
        Gs102.setHighlighter(null);
        Gs102.setName("Gs102"); // NOI18N
        FormInput.add(Gs102);
        Gs102.setBounds(370, 360, 70, 23);

        Gs103.setEditable(false);
        Gs103.setHighlighter(null);
        Gs103.setName("Gs103"); // NOI18N
        FormInput.add(Gs103);
        Gs103.setBounds(480, 360, 120, 23);

        Gs104.setEditable(false);
        Gs104.setHighlighter(null);
        Gs104.setName("Gs104"); // NOI18N
        FormInput.add(Gs104);
        Gs104.setBounds(480, 420, 120, 23);

        Gs105.setEditable(false);
        Gs105.setHighlighter(null);
        Gs105.setName("Gs105"); // NOI18N
        FormInput.add(Gs105);
        Gs105.setBounds(480, 450, 120, 23);

        Gs106.setEditable(false);
        Gs106.setHighlighter(null);
        Gs106.setName("Gs106"); // NOI18N
        FormInput.add(Gs106);
        Gs106.setBounds(480, 480, 120, 23);

        Gs107.setEditable(false);
        Gs107.setHighlighter(null);
        Gs107.setName("Gs107"); // NOI18N
        FormInput.add(Gs107);
        Gs107.setBounds(480, 510, 120, 23);

        Gs108.setEditable(false);
        Gs108.setHighlighter(null);
        Gs108.setName("Gs108"); // NOI18N
        FormInput.add(Gs108);
        Gs108.setBounds(480, 540, 120, 23);

        Gs109.setEditable(false);
        Gs109.setHighlighter(null);
        Gs109.setName("Gs109"); // NOI18N
        FormInput.add(Gs109);
        Gs109.setBounds(480, 570, 120, 23);

        Gs110.setEditable(false);
        Gs110.setHighlighter(null);
        Gs110.setName("Gs110"); // NOI18N
        FormInput.add(Gs110);
        Gs110.setBounds(480, 600, 120, 23);

        Gs111.setEditable(false);
        Gs111.setHighlighter(null);
        Gs111.setName("Gs111"); // NOI18N
        FormInput.add(Gs111);
        Gs111.setBounds(480, 780, 120, 23);

        Gs112.setEditable(false);
        Gs112.setHighlighter(null);
        Gs112.setName("Gs112"); // NOI18N
        FormInput.add(Gs112);
        Gs112.setBounds(480, 750, 120, 23);

        Gs113.setEditable(false);
        Gs113.setHighlighter(null);
        Gs113.setName("Gs113"); // NOI18N
        FormInput.add(Gs113);
        Gs113.setBounds(480, 720, 120, 23);

        Gs114.setEditable(false);
        Gs114.setHighlighter(null);
        Gs114.setName("Gs114"); // NOI18N
        FormInput.add(Gs114);
        Gs114.setBounds(480, 690, 120, 23);

        Gs115.setEditable(false);
        Gs115.setHighlighter(null);
        Gs115.setName("Gs115"); // NOI18N
        FormInput.add(Gs115);
        Gs115.setBounds(480, 660, 120, 23);

        Gs116.setEditable(false);
        Gs116.setHighlighter(null);
        Gs116.setName("Gs116"); // NOI18N
        FormInput.add(Gs116);
        Gs116.setBounds(480, 630, 120, 23);

        Gs117.setEditable(false);
        Gs117.setHighlighter(null);
        Gs117.setName("Gs117"); // NOI18N
        FormInput.add(Gs117);
        Gs117.setBounds(480, 1350, 120, 23);

        Gs118.setEditable(false);
        Gs118.setHighlighter(null);
        Gs118.setName("Gs118"); // NOI18N
        FormInput.add(Gs118);
        Gs118.setBounds(780, 1380, 120, 23);

        jLabel197.setText("cm");
        jLabel197.setName("jLabel197"); // NOI18N
        FormInput.add(jLabel197);
        jLabel197.setBounds(750, 360, 20, 23);

        jLabel198.setText("CRL:");
        jLabel198.setName("jLabel198"); // NOI18N
        FormInput.add(jLabel198);
        jLabel198.setBounds(630, 420, 30, 23);

        Gs119.setEditable(false);
        Gs119.setHighlighter(null);
        Gs119.setName("Gs119"); // NOI18N
        FormInput.add(Gs119);
        Gs119.setBounds(670, 420, 70, 23);

        jLabel199.setText("cm");
        jLabel199.setName("jLabel199"); // NOI18N
        FormInput.add(jLabel199);
        jLabel199.setBounds(750, 420, 20, 23);

        jLabel200.setText("Cereb:");
        jLabel200.setName("jLabel200"); // NOI18N
        FormInput.add(jLabel200);
        jLabel200.setBounds(620, 510, 40, 23);

        Gs120.setEditable(false);
        Gs120.setHighlighter(null);
        Gs120.setName("Gs120"); // NOI18N
        FormInput.add(Gs120);
        Gs120.setBounds(670, 510, 70, 23);

        jLabel201.setText("mm");
        jLabel201.setName("jLabel201"); // NOI18N
        FormInput.add(jLabel201);
        jLabel201.setBounds(750, 510, 20, 23);

        jLabel202.setText("YS:");
        jLabel202.setName("jLabel202"); // NOI18N
        FormInput.add(jLabel202);
        jLabel202.setBounds(630, 390, 30, 23);

        Gs121.setEditable(false);
        Gs121.setHighlighter(null);
        Gs121.setName("Gs121"); // NOI18N
        FormInput.add(Gs121);
        Gs121.setBounds(670, 390, 70, 23);

        jLabel203.setText("cm");
        jLabel203.setName("jLabel203"); // NOI18N
        FormInput.add(jLabel203);
        jLabel203.setBounds(750, 390, 20, 23);

        jLabel204.setText("BPD:");
        jLabel204.setName("jLabel204"); // NOI18N
        FormInput.add(jLabel204);
        jLabel204.setBounds(630, 450, 30, 23);

        Gs122.setEditable(false);
        Gs122.setHighlighter(null);
        Gs122.setName("Gs122"); // NOI18N
        FormInput.add(Gs122);
        Gs122.setBounds(670, 450, 70, 23);

        jLabel205.setText("Ulna:");
        jLabel205.setName("jLabel205"); // NOI18N
        FormInput.add(jLabel205);
        jLabel205.setBounds(630, 630, 30, 23);

        Gs123.setEditable(false);
        Gs123.setHighlighter(null);
        Gs123.setName("Gs123"); // NOI18N
        FormInput.add(Gs123);
        Gs123.setBounds(670, 630, 70, 23);

        jLabel206.setText("cm");
        jLabel206.setName("jLabel206"); // NOI18N
        FormInput.add(jLabel206);
        jLabel206.setBounds(750, 630, 20, 23);

        jLabel207.setText("HC:");
        jLabel207.setName("jLabel207"); // NOI18N
        FormInput.add(jLabel207);
        jLabel207.setBounds(630, 540, 30, 23);

        Gs124.setEditable(false);
        Gs124.setHighlighter(null);
        Gs124.setName("Gs124"); // NOI18N
        FormInput.add(Gs124);
        Gs124.setBounds(670, 540, 70, 23);

        jLabel208.setText("cm");
        jLabel208.setName("jLabel208"); // NOI18N
        FormInput.add(jLabel208);
        jLabel208.setBounds(750, 540, 20, 23);

        jLabel209.setText("OFD:");
        jLabel209.setName("jLabel209"); // NOI18N
        FormInput.add(jLabel209);
        jLabel209.setBounds(630, 480, 30, 23);

        Gs125.setEditable(false);
        Gs125.setHighlighter(null);
        Gs125.setName("Gs125"); // NOI18N
        FormInput.add(Gs125);
        Gs125.setBounds(670, 480, 70, 23);

        jLabel210.setText("cm");
        jLabel210.setName("jLabel210"); // NOI18N
        FormInput.add(jLabel210);
        jLabel210.setBounds(750, 480, 20, 23);

        jLabel211.setText("HUM:");
        jLabel211.setName("jLabel211"); // NOI18N
        FormInput.add(jLabel211);
        jLabel211.setBounds(630, 600, 30, 23);

        Gs126.setEditable(false);
        Gs126.setHighlighter(null);
        Gs126.setName("Gs126"); // NOI18N
        FormInput.add(Gs126);
        Gs126.setBounds(670, 600, 70, 23);

        jLabel212.setText("cm");
        jLabel212.setName("jLabel212"); // NOI18N
        FormInput.add(jLabel212);
        jLabel212.setBounds(750, 600, 20, 23);

        jLabel213.setText("AC:");
        jLabel213.setName("jLabel213"); // NOI18N
        FormInput.add(jLabel213);
        jLabel213.setBounds(630, 570, 30, 23);

        Gs127.setEditable(false);
        Gs127.setHighlighter(null);
        Gs127.setName("Gs127"); // NOI18N
        FormInput.add(Gs127);
        Gs127.setBounds(670, 570, 70, 23);

        jLabel214.setText("cm");
        jLabel214.setName("jLabel214"); // NOI18N
        FormInput.add(jLabel214);
        jLabel214.setBounds(750, 570, 20, 23);

        jLabel215.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel215.setName("jLabel215"); // NOI18N
        FormInput.add(jLabel215);
        jLabel215.setBounds(840, 700, 10, 23);

        jLabel216.setText("GS:");
        jLabel216.setName("jLabel216"); // NOI18N
        FormInput.add(jLabel216);
        jLabel216.setBounds(630, 360, 30, 23);

        jLabel217.setText("cm");
        jLabel217.setName("jLabel217"); // NOI18N
        FormInput.add(jLabel217);
        jLabel217.setBounds(750, 450, 20, 23);

        Gs128.setEditable(false);
        Gs128.setHighlighter(null);
        Gs128.setName("Gs128"); // NOI18N
        FormInput.add(Gs128);
        Gs128.setBounds(670, 660, 70, 23);

        jLabel218.setText("cm");
        jLabel218.setName("jLabel218"); // NOI18N
        FormInput.add(jLabel218);
        jLabel218.setBounds(750, 660, 20, 23);

        jLabel219.setText("Radius:");
        jLabel219.setName("jLabel219"); // NOI18N
        FormInput.add(jLabel219);
        jLabel219.setBounds(620, 720, 40, 23);

        Gs129.setEditable(false);
        Gs129.setHighlighter(null);
        Gs129.setName("Gs129"); // NOI18N
        FormInput.add(Gs129);
        Gs129.setBounds(670, 720, 70, 23);

        jLabel220.setText("cm");
        jLabel220.setName("jLabel220"); // NOI18N
        FormInput.add(jLabel220);
        jLabel220.setBounds(750, 720, 20, 23);

        jLabel221.setText("AFI-Q1:");
        jLabel221.setName("jLabel221"); // NOI18N
        FormInput.add(jLabel221);
        jLabel221.setBounds(620, 810, 40, 23);

        Gs130.setEditable(false);
        Gs130.setHighlighter(null);
        Gs130.setName("Gs130"); // NOI18N
        FormInput.add(Gs130);
        Gs130.setBounds(670, 810, 70, 23);

        jLabel222.setText("mm");
        jLabel222.setName("jLabel222"); // NOI18N
        FormInput.add(jLabel222);
        jLabel222.setBounds(750, 810, 20, 23);

        jLabel223.setText("Fibula:");
        jLabel223.setName("jLabel223"); // NOI18N
        FormInput.add(jLabel223);
        jLabel223.setBounds(620, 690, 40, 23);

        Gs131.setEditable(false);
        Gs131.setHighlighter(null);
        Gs131.setName("Gs131"); // NOI18N
        FormInput.add(Gs131);
        Gs131.setBounds(670, 690, 70, 23);

        jLabel224.setText("cm");
        jLabel224.setName("jLabel224"); // NOI18N
        FormInput.add(jLabel224);
        jLabel224.setBounds(750, 690, 20, 23);

        jLabel225.setText("FL:");
        jLabel225.setName("jLabel225"); // NOI18N
        FormInput.add(jLabel225);
        jLabel225.setBounds(630, 750, 30, 23);

        Gs132.setEditable(false);
        Gs132.setHighlighter(null);
        Gs132.setName("Gs132"); // NOI18N
        FormInput.add(Gs132);
        Gs132.setBounds(670, 750, 70, 23);

        jLabel226.setText("TOTAL AFI:");
        jLabel226.setName("jLabel226"); // NOI18N
        FormInput.add(jLabel226);
        jLabel226.setBounds(600, 930, 60, 23);

        Gs133.setEditable(false);
        Gs133.setHighlighter(null);
        Gs133.setName("Gs133"); // NOI18N
        FormInput.add(Gs133);
        Gs133.setBounds(670, 930, 70, 23);

        jLabel227.setText("cm");
        jLabel227.setName("jLabel227"); // NOI18N
        FormInput.add(jLabel227);
        jLabel227.setBounds(750, 930, 20, 23);

        jLabel228.setText("AFI-Q2:");
        jLabel228.setName("jLabel228"); // NOI18N
        FormInput.add(jLabel228);
        jLabel228.setBounds(620, 840, 39, 23);

        Gs134.setEditable(false);
        Gs134.setHighlighter(null);
        Gs134.setName("Gs134"); // NOI18N
        FormInput.add(Gs134);
        Gs134.setBounds(670, 840, 70, 23);

        jLabel229.setText("cm");
        jLabel229.setName("jLabel229"); // NOI18N
        FormInput.add(jLabel229);
        jLabel229.setBounds(750, 840, 20, 23);

        jLabel230.setText("NT:");
        jLabel230.setName("jLabel230"); // NOI18N
        FormInput.add(jLabel230);
        jLabel230.setBounds(630, 780, 30, 23);

        Gs135.setEditable(false);
        Gs135.setHighlighter(null);
        Gs135.setName("Gs135"); // NOI18N
        FormInput.add(Gs135);
        Gs135.setBounds(670, 780, 70, 23);

        jLabel231.setText("cm");
        jLabel231.setName("jLabel231"); // NOI18N
        FormInput.add(jLabel231);
        jLabel231.setBounds(750, 780, 20, 23);

        jLabel232.setText("AFI-Q4:");
        jLabel232.setName("jLabel232"); // NOI18N
        FormInput.add(jLabel232);
        jLabel232.setBounds(620, 900, 40, 23);

        Gs136.setEditable(false);
        Gs136.setHighlighter(null);
        Gs136.setName("Gs136"); // NOI18N
        FormInput.add(Gs136);
        Gs136.setBounds(670, 900, 70, 23);

        jLabel233.setText("cm");
        jLabel233.setName("jLabel233"); // NOI18N
        FormInput.add(jLabel233);
        jLabel233.setBounds(750, 900, 20, 23);

        jLabel234.setText("AFI-Q3:");
        jLabel234.setName("jLabel234"); // NOI18N
        FormInput.add(jLabel234);
        jLabel234.setBounds(610, 870, 50, 23);

        Gs137.setEditable(false);
        Gs137.setHighlighter(null);
        Gs137.setName("Gs137"); // NOI18N
        FormInput.add(Gs137);
        Gs137.setBounds(670, 870, 70, 23);

        jLabel235.setText("cm");
        jLabel235.setName("jLabel235"); // NOI18N
        FormInput.add(jLabel235);
        jLabel235.setBounds(750, 870, 20, 23);

        jLabel236.setText("Tibia:");
        jLabel236.setName("jLabel236"); // NOI18N
        FormInput.add(jLabel236);
        jLabel236.setBounds(630, 660, 30, 23);

        jLabel237.setText("cm");
        jLabel237.setName("jLabel237"); // NOI18N
        FormInput.add(jLabel237);
        jLabel237.setBounds(750, 750, 20, 23);

        Gs138.setEditable(false);
        Gs138.setHighlighter(null);
        Gs138.setName("Gs138"); // NOI18N
        FormInput.add(Gs138);
        Gs138.setBounds(670, 960, 70, 23);

        jLabel238.setText("cm");
        jLabel238.setName("jLabel238"); // NOI18N
        FormInput.add(jLabel238);
        jLabel238.setBounds(750, 960, 20, 23);

        jLabel239.setText("hrtD ap:");
        jLabel239.setName("jLabel239"); // NOI18N
        FormInput.add(jLabel239);
        jLabel239.setBounds(600, 1020, 60, 23);

        Gs139.setEditable(false);
        Gs139.setHighlighter(null);
        Gs139.setName("Gs139"); // NOI18N
        FormInput.add(Gs139);
        Gs139.setBounds(670, 1020, 70, 23);

        jLabel240.setText("cm");
        jLabel240.setName("jLabel240"); // NOI18N
        FormInput.add(jLabel240);
        jLabel240.setBounds(750, 1020, 20, 23);

        jLabel241.setText("IT:");
        jLabel241.setName("jLabel241"); // NOI18N
        FormInput.add(jLabel241);
        jLabel241.setBounds(620, 1110, 40, 23);

        Gs140.setEditable(false);
        Gs140.setHighlighter(null);
        Gs140.setName("Gs140"); // NOI18N
        FormInput.add(Gs140);
        Gs140.setBounds(670, 1110, 70, 23);

        jLabel242.setText("mm");
        jLabel242.setName("jLabel242"); // NOI18N
        FormInput.add(jLabel242);
        jLabel242.setBounds(750, 1110, 20, 23);

        jLabel243.setText("ThD trans::");
        jLabel243.setName("jLabel243"); // NOI18N
        FormInput.add(jLabel243);
        jLabel243.setBounds(600, 990, 60, 23);

        Gs141.setEditable(false);
        Gs141.setHighlighter(null);
        Gs141.setName("Gs141"); // NOI18N
        FormInput.add(Gs141);
        Gs141.setBounds(670, 990, 70, 23);

        jLabel244.setText("cm");
        jLabel244.setName("jLabel244"); // NOI18N
        FormInput.add(jLabel244);
        jLabel244.setBounds(750, 990, 20, 23);

        jLabel245.setText("hrtD trans:");
        jLabel245.setName("jLabel245"); // NOI18N
        FormInput.add(jLabel245);
        jLabel245.setBounds(600, 1050, 60, 23);

        Gs142.setEditable(false);
        Gs142.setHighlighter(null);
        Gs142.setName("Gs142"); // NOI18N
        FormInput.add(Gs142);
        Gs142.setBounds(670, 1050, 70, 23);

        jLabel246.setText("NF:");
        jLabel246.setName("jLabel246"); // NOI18N
        FormInput.add(jLabel246);
        jLabel246.setBounds(630, 1230, 30, 23);

        Gs143.setEditable(false);
        Gs143.setHighlighter(null);
        Gs143.setName("Gs143"); // NOI18N
        FormInput.add(Gs143);
        Gs143.setBounds(670, 1230, 70, 23);

        jLabel247.setText("cm");
        jLabel247.setName("jLabel247"); // NOI18N
        FormInput.add(jLabel247);
        jLabel247.setBounds(750, 1230, 20, 23);

        jLabel248.setText("NB:");
        jLabel248.setName("jLabel248"); // NOI18N
        FormInput.add(jLabel248);
        jLabel248.setBounds(630, 1140, 30, 23);

        Gs144.setEditable(false);
        Gs144.setHighlighter(null);
        Gs144.setName("Gs144"); // NOI18N
        FormInput.add(Gs144);
        Gs144.setBounds(670, 1140, 70, 23);

        jLabel249.setText("cm");
        jLabel249.setName("jLabel249"); // NOI18N
        FormInput.add(jLabel249);
        jLabel249.setBounds(750, 1140, 20, 23);

        jLabel250.setText("CTAR:");
        jLabel250.setName("jLabel250"); // NOI18N
        FormInput.add(jLabel250);
        jLabel250.setBounds(610, 1080, 50, 23);

        Gs145.setEditable(false);
        Gs145.setHighlighter(null);
        Gs145.setName("Gs145"); // NOI18N
        FormInput.add(Gs145);
        Gs145.setBounds(670, 1080, 70, 23);

        jLabel251.setText("cm");
        jLabel251.setName("jLabel251"); // NOI18N
        FormInput.add(jLabel251);
        jLabel251.setBounds(750, 1080, 20, 23);

        jLabel252.setText("IOD:");
        jLabel252.setName("jLabel252"); // NOI18N
        FormInput.add(jLabel252);
        jLabel252.setBounds(630, 1200, 30, 23);

        Gs146.setEditable(false);
        Gs146.setHighlighter(null);
        Gs146.setName("Gs146"); // NOI18N
        FormInput.add(Gs146);
        Gs146.setBounds(670, 1200, 70, 23);

        jLabel253.setText("cm");
        jLabel253.setName("jLabel253"); // NOI18N
        FormInput.add(jLabel253);
        jLabel253.setBounds(750, 1200, 20, 23);

        jLabel254.setText("CM:");
        jLabel254.setName("jLabel254"); // NOI18N
        FormInput.add(jLabel254);
        jLabel254.setBounds(630, 1170, 30, 23);

        Gs147.setEditable(false);
        Gs147.setHighlighter(null);
        Gs147.setName("Gs147"); // NOI18N
        FormInput.add(Gs147);
        Gs147.setBounds(670, 1170, 70, 23);

        jLabel255.setText("cm");
        jLabel255.setName("jLabel255"); // NOI18N
        FormInput.add(jLabel255);
        jLabel255.setBounds(750, 1170, 20, 23);

        jLabel256.setText("ThD ap:");
        jLabel256.setName("jLabel256"); // NOI18N
        FormInput.add(jLabel256);
        jLabel256.setBounds(610, 960, 50, 23);

        jLabel257.setText("cm");
        jLabel257.setName("jLabel257"); // NOI18N
        FormInput.add(jLabel257);
        jLabel257.setBounds(750, 1050, 20, 23);

        Gs148.setEditable(false);
        Gs148.setHighlighter(null);
        Gs148.setName("Gs148"); // NOI18N
        FormInput.add(Gs148);
        Gs148.setBounds(670, 1260, 70, 23);

        jLabel258.setText("cm");
        jLabel258.setName("jLabel258"); // NOI18N
        FormInput.add(jLabel258);
        jLabel258.setBounds(750, 1260, 20, 23);

        jLabel259.setText("FMF angle:");
        jLabel259.setName("jLabel259"); // NOI18N
        FormInput.add(jLabel259);
        jLabel259.setBounds(600, 1320, 60, 23);

        Gs149.setEditable(false);
        Gs149.setHighlighter(null);
        Gs149.setName("Gs149"); // NOI18N
        FormInput.add(Gs149);
        Gs149.setBounds(670, 1320, 70, 23);

        jLabel260.setText("cm");
        jLabel260.setName("jLabel260"); // NOI18N
        FormInput.add(jLabel260);
        jLabel260.setBounds(750, 1320, 20, 23);

        jLabel261.setText("MVP:");
        jLabel261.setName("jLabel261"); // NOI18N
        FormInput.add(jLabel261);
        jLabel261.setBounds(620, 1410, 40, 23);

        Gs150.setEditable(false);
        Gs150.setHighlighter(null);
        Gs150.setName("Gs150"); // NOI18N
        FormInput.add(Gs150);
        Gs150.setBounds(670, 1410, 70, 23);

        jLabel262.setText("mm");
        jLabel262.setName("jLabel262"); // NOI18N
        FormInput.add(jLabel262);
        jLabel262.setBounds(750, 1410, 20, 23);

        jLabel263.setText("Lat Vent:");
        jLabel263.setName("jLabel263"); // NOI18N
        FormInput.add(jLabel263);
        jLabel263.setBounds(600, 1290, 60, 23);

        Gs151.setEditable(false);
        Gs151.setHighlighter(null);
        Gs151.setName("Gs151"); // NOI18N
        FormInput.add(Gs151);
        Gs151.setBounds(670, 1290, 70, 23);

        jLabel264.setText("cm");
        jLabel264.setName("jLabel264"); // NOI18N
        FormInput.add(jLabel264);
        jLabel264.setBounds(750, 1290, 20, 23);

        jLabel265.setText("BOD:");
        jLabel265.setName("jLabel265"); // NOI18N
        FormInput.add(jLabel265);
        jLabel265.setBounds(630, 1350, 30, 23);

        Gs152.setEditable(false);
        Gs152.setHighlighter(null);
        Gs152.setName("Gs152"); // NOI18N
        FormInput.add(Gs152);
        Gs152.setBounds(670, 1350, 70, 23);

        jLabel266.setText("Cardiac axis:");
        jLabel266.setName("jLabel266"); // NOI18N
        FormInput.add(jLabel266);
        jLabel266.setBounds(580, 1440, 80, 23);

        Gs153.setEditable(false);
        Gs153.setHighlighter(null);
        Gs153.setName("Gs153"); // NOI18N
        FormInput.add(Gs153);
        Gs153.setBounds(670, 1440, 70, 23);

        jLabel267.setText("cm");
        jLabel267.setName("jLabel267"); // NOI18N
        FormInput.add(jLabel267);
        jLabel267.setBounds(750, 1440, 20, 23);

        jLabel268.setText("Clav:");
        jLabel268.setName("jLabel268"); // NOI18N
        FormInput.add(jLabel268);
        jLabel268.setBounds(630, 1380, 30, 23);

        Gs154.setEditable(false);
        Gs154.setHighlighter(null);
        Gs154.setName("Gs154"); // NOI18N
        FormInput.add(Gs154);
        Gs154.setBounds(670, 1380, 70, 23);

        jLabel269.setText("cm");
        jLabel269.setName("jLabel269"); // NOI18N
        FormInput.add(jLabel269);
        jLabel269.setBounds(750, 1380, 20, 23);

        jLabel270.setText("Cervical length:");
        jLabel270.setName("jLabel270"); // NOI18N
        FormInput.add(jLabel270);
        jLabel270.setBounds(580, 1500, 80, 23);

        Gs155.setEditable(false);
        Gs155.setHighlighter(null);
        Gs155.setName("Gs155"); // NOI18N
        FormInput.add(Gs155);
        Gs155.setBounds(670, 1500, 70, 23);

        jLabel271.setText("cm");
        jLabel271.setName("jLabel271"); // NOI18N
        FormInput.add(jLabel271);
        jLabel271.setBounds(750, 1500, 20, 23);

        jLabel272.setText("Placenta thick:");
        jLabel272.setName("jLabel272"); // NOI18N
        FormInput.add(jLabel272);
        jLabel272.setBounds(580, 1470, 80, 23);

        Gs156.setEditable(false);
        Gs156.setHighlighter(null);
        Gs156.setName("Gs156"); // NOI18N
        FormInput.add(Gs156);
        Gs156.setBounds(670, 1470, 70, 23);

        jLabel273.setText("cm");
        jLabel273.setName("jLabel273"); // NOI18N
        FormInput.add(jLabel273);
        jLabel273.setBounds(750, 1470, 20, 23);

        jLabel274.setText("CSP:");
        jLabel274.setName("jLabel274"); // NOI18N
        FormInput.add(jLabel274);
        jLabel274.setBounds(630, 1260, 30, 23);

        jLabel275.setText("cm");
        jLabel275.setName("jLabel275"); // NOI18N
        FormInput.add(jLabel275);
        jLabel275.setBounds(750, 1350, 20, 23);

        Gs157.setEditable(false);
        Gs157.setHighlighter(null);
        Gs157.setName("Gs157"); // NOI18N
        FormInput.add(Gs157);
        Gs157.setBounds(670, 360, 70, 23);

        Gs158.setEditable(false);
        Gs158.setHighlighter(null);
        Gs158.setName("Gs158"); // NOI18N
        FormInput.add(Gs158);
        Gs158.setBounds(780, 360, 120, 23);

        Gs159.setEditable(false);
        Gs159.setHighlighter(null);
        Gs159.setName("Gs159"); // NOI18N
        FormInput.add(Gs159);
        Gs159.setBounds(780, 420, 120, 23);

        Gs160.setEditable(false);
        Gs160.setHighlighter(null);
        Gs160.setName("Gs160"); // NOI18N
        FormInput.add(Gs160);
        Gs160.setBounds(780, 450, 120, 23);

        Gs161.setEditable(false);
        Gs161.setHighlighter(null);
        Gs161.setName("Gs161"); // NOI18N
        FormInput.add(Gs161);
        Gs161.setBounds(780, 480, 120, 23);

        Gs162.setEditable(false);
        Gs162.setHighlighter(null);
        Gs162.setName("Gs162"); // NOI18N
        FormInput.add(Gs162);
        Gs162.setBounds(780, 510, 120, 23);

        Gs163.setEditable(false);
        Gs163.setHighlighter(null);
        Gs163.setName("Gs163"); // NOI18N
        FormInput.add(Gs163);
        Gs163.setBounds(780, 540, 120, 23);

        Gs164.setEditable(false);
        Gs164.setHighlighter(null);
        Gs164.setName("Gs164"); // NOI18N
        FormInput.add(Gs164);
        Gs164.setBounds(780, 570, 120, 23);

        Gs165.setEditable(false);
        Gs165.setHighlighter(null);
        Gs165.setName("Gs165"); // NOI18N
        FormInput.add(Gs165);
        Gs165.setBounds(780, 600, 120, 23);

        Gs166.setEditable(false);
        Gs166.setHighlighter(null);
        Gs166.setName("Gs166"); // NOI18N
        FormInput.add(Gs166);
        Gs166.setBounds(780, 780, 120, 23);

        Gs167.setEditable(false);
        Gs167.setHighlighter(null);
        Gs167.setName("Gs167"); // NOI18N
        FormInput.add(Gs167);
        Gs167.setBounds(780, 750, 120, 23);

        Gs168.setEditable(false);
        Gs168.setHighlighter(null);
        Gs168.setName("Gs168"); // NOI18N
        FormInput.add(Gs168);
        Gs168.setBounds(780, 720, 120, 23);

        Gs169.setEditable(false);
        Gs169.setHighlighter(null);
        Gs169.setName("Gs169"); // NOI18N
        FormInput.add(Gs169);
        Gs169.setBounds(780, 690, 120, 23);

        Gs170.setEditable(false);
        Gs170.setHighlighter(null);
        Gs170.setName("Gs170"); // NOI18N
        FormInput.add(Gs170);
        Gs170.setBounds(780, 660, 120, 23);

        Gs171.setEditable(false);
        Gs171.setHighlighter(null);
        Gs171.setName("Gs171"); // NOI18N
        FormInput.add(Gs171);
        Gs171.setBounds(780, 630, 120, 23);

        Gs172.setEditable(false);
        Gs172.setHighlighter(null);
        Gs172.setName("Gs172"); // NOI18N
        FormInput.add(Gs172);
        Gs172.setBounds(780, 1350, 120, 23);

        jLabel276.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel276.setText("PLACENTA");
        jLabel276.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel276.setName("jLabel276"); // NOI18N
        FormInput.add(jLabel276);
        jLabel276.setBounds(10, 1540, 120, 23);

        jLabel277.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel277.setText("I");
        jLabel277.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel277.setName("jLabel277"); // NOI18N
        FormInput.add(jLabel277);
        jLabel277.setBounds(160, 330, 20, 23);

        jLabel278.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel278.setText("II");
        jLabel278.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel278.setName("jLabel278"); // NOI18N
        FormInput.add(jLabel278);
        jLabel278.setBounds(450, 330, 20, 23);

        jLabel279.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel279.setText("BIOMETRI JANIN ");
        jLabel279.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel279.setName("jLabel279"); // NOI18N
        FormInput.add(jLabel279);
        jLabel279.setBounds(10, 330, 120, 23);

        jLabel280.setText("Letak placenta:");
        jLabel280.setName("jLabel280"); // NOI18N
        FormInput.add(jLabel280);
        jLabel280.setBounds(10, 1560, 80, 23);

        A12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Fundus", "Corpus", "SBR", "Anterior", "Posterior", "Lateral", "Kanan", "Kiri", "Mencapai tepi OUI", "Menutupi OUI sebagian", "Menutupi OUI totalis", "Berada dari tepi OUI" }));
        A12.setName("A12"); // NOI18N
        A12.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A12ItemStateChanged(evt);
            }
        });
        A12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A12KeyPressed(evt);
            }
        });
        FormInput.add(A12);
        A12.setBounds(100, 1560, 140, 23);

        jLabel281.setText("Grade placenta:");
        jLabel281.setName("jLabel281"); // NOI18N
        FormInput.add(jLabel281);
        jLabel281.setBounds(250, 1560, 80, 23);

        A13.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Grade 0", "Grade 1", "Grade 2", "Grade 3" }));
        A13.setName("A13"); // NOI18N
        A13.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A13ItemStateChanged(evt);
            }
        });
        A13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A13KeyPressed(evt);
            }
        });
        FormInput.add(A13);
        A13.setBounds(340, 1560, 80, 23);

        jLabel282.setText("Celar zone:");
        jLabel282.setName("jLabel282"); // NOI18N
        FormInput.add(jLabel282);
        jLabel282.setBounds(430, 1560, 80, 23);

        A14.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jelas", "Tak jelas sebagian", "Tak jelas", "Terdapat bridging vessels", "Terdapat lacunae" }));
        A14.setName("A14"); // NOI18N
        A14.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A14ItemStateChanged(evt);
            }
        });
        A14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A14KeyPressed(evt);
            }
        });
        FormInput.add(A14);
        A14.setBounds(520, 1560, 100, 23);

        jLabel283.setText("Kategori placenta:");
        jLabel283.setName("jLabel283"); // NOI18N
        FormInput.add(jLabel283);
        jLabel283.setBounds(640, 1560, 100, 23);

        A15.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Placenta normal", "Placenta letak rendah", "Placenta previa totalis", "Placenta previa marginalis", "Curiga placenta akreta" }));
        A15.setName("A15"); // NOI18N
        A15.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A15ItemStateChanged(evt);
            }
        });
        A15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A15KeyPressed(evt);
            }
        });
        FormInput.add(A15);
        A15.setBounds(750, 1560, 140, 23);

        jLabel284.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel284.setText("UMBILLICAL CORD");
        jLabel284.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel284.setName("jLabel284"); // NOI18N
        FormInput.add(jLabel284);
        jLabel284.setBounds(10, 1600, 120, 23);

        jLabel285.setText("Jumlah pembuluh darah tali pusat:");
        jLabel285.setName("jLabel285"); // NOI18N
        FormInput.add(jLabel285);
        jLabel285.setBounds(10, 1620, 180, 23);

        A16.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2 arteri 1 vena", "1 arteri 1 vena" }));
        A16.setName("A16"); // NOI18N
        A16.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A16ItemStateChanged(evt);
            }
        });
        A16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A16ActionPerformed(evt);
            }
        });
        A16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A16KeyPressed(evt);
            }
        });
        FormInput.add(A16);
        A16.setBounds(200, 1620, 140, 23);

        jLabel286.setText("Lilitan tali pusat di leher:");
        jLabel286.setName("jLabel286"); // NOI18N
        FormInput.add(jLabel286);
        jLabel286.setBounds(400, 1620, 140, 23);

        A17.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak ada", "Terdapat lilitan tali pusat" }));
        A17.setName("A17"); // NOI18N
        A17.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A17ItemStateChanged(evt);
            }
        });
        A17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A17ActionPerformed(evt);
            }
        });
        A17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A17KeyPressed(evt);
            }
        });
        FormInput.add(A17);
        A17.setBounds(550, 1620, 140, 23);

        jLabel287.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel287.setText("KETUBAN");
        jLabel287.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel287.setName("jLabel287"); // NOI18N
        FormInput.add(jLabel287);
        jLabel287.setBounds(10, 1650, 120, 23);

        jLabel288.setText("Jumlah cairan ketuban secara kualitatif:");
        jLabel288.setName("jLabel288"); // NOI18N
        FormInput.add(jLabel288);
        jLabel288.setBounds(10, 1670, 210, 23);

        A18.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cukup", "Banyak", "Kurang", "Minimal", "Habis" }));
        A18.setName("A18"); // NOI18N
        A18.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A18ItemStateChanged(evt);
            }
        });
        A18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A18ActionPerformed(evt);
            }
        });
        A18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A18KeyPressed(evt);
            }
        });
        FormInput.add(A18);
        A18.setBounds(230, 1670, 140, 23);

        jLabel289.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel289.setText("ANATOMI JANIN");
        jLabel289.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel289.setName("jLabel289"); // NOI18N
        FormInput.add(jLabel289);
        jLabel289.setBounds(10, 1710, 120, 23);

        jLabel290.setText("Tulang kepala/tengkorak:");
        jLabel290.setName("jLabel290"); // NOI18N
        FormInput.add(jLabel290);
        jLabel290.setBounds(10, 1730, 130, 23);

        A19.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak ada" }));
        A19.setName("A19"); // NOI18N
        A19.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A19ItemStateChanged(evt);
            }
        });
        A19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A19ActionPerformed(evt);
            }
        });
        A19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A19KeyPressed(evt);
            }
        });
        FormInput.add(A19);
        A19.setBounds(150, 1730, 70, 23);

        jLabel291.setText("Bentuk tulang kepala:");
        jLabel291.setName("jLabel291"); // NOI18N
        FormInput.add(jLabel291);
        jLabel291.setBounds(230, 1730, 130, 23);

        A20.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Lemon shape", "Clover", "Dolicocephali", "Brachicephali", "Mengingocele" }));
        A20.setName("A20"); // NOI18N
        A20.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A20ItemStateChanged(evt);
            }
        });
        A20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A20ActionPerformed(evt);
            }
        });
        A20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A20KeyPressed(evt);
            }
        });
        FormInput.add(A20);
        A20.setBounds(370, 1730, 120, 23);

        jLabel292.setText("Nuchal translucency:");
        jLabel292.setName("jLabel292"); // NOI18N
        FormInput.add(jLabel292);
        jLabel292.setBounds(500, 1730, 130, 23);

        A21.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Menebal >= 3 mm", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A21.setName("A21"); // NOI18N
        A21.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A21ItemStateChanged(evt);
            }
        });
        A21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A21ActionPerformed(evt);
            }
        });
        A21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A21KeyPressed(evt);
            }
        });
        FormInput.add(A21);
        A21.setBounds(640, 1730, 220, 23);

        jLabel293.setText("Midline falx cerebri:");
        jLabel293.setName("jLabel293"); // NOI18N
        FormInput.add(jLabel293);
        jLabel293.setBounds(10, 1760, 130, 23);

        A22.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A22.setName("A22"); // NOI18N
        A22.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A22ItemStateChanged(evt);
            }
        });
        A22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A22ActionPerformed(evt);
            }
        });
        A22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A22KeyPressed(evt);
            }
        });
        FormInput.add(A22);
        A22.setBounds(150, 1760, 220, 23);

        jLabel295.setText("Chroid plexus:");
        jLabel295.setName("jLabel295"); // NOI18N
        FormInput.add(jLabel295);
        jLabel295.setBounds(500, 1760, 130, 23);

        A24.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Menebal >= 3 mm", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A24.setName("A24"); // NOI18N
        A24.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A24ItemStateChanged(evt);
            }
        });
        A24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A24ActionPerformed(evt);
            }
        });
        A24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A24KeyPressed(evt);
            }
        });
        FormInput.add(A24);
        A24.setBounds(640, 1760, 220, 23);

        jLabel294.setText("Ventrikel:");
        jLabel294.setName("jLabel294"); // NOI18N
        FormInput.add(jLabel294);
        jLabel294.setBounds(10, 1790, 130, 23);

        A23.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A23.setName("A23"); // NOI18N
        A23.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A23ItemStateChanged(evt);
            }
        });
        A23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A23ActionPerformed(evt);
            }
        });
        A23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A23KeyPressed(evt);
            }
        });
        FormInput.add(A23);
        A23.setBounds(150, 1790, 220, 23);

        jLabel296.setText("Orientasi ankle kaki kiri:");
        jLabel296.setName("jLabel296"); // NOI18N
        FormInput.add(jLabel296);
        jLabel296.setBounds(10, 2360, 130, 23);

        A25.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Menebal >= 3 mm", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A25.setName("A25"); // NOI18N
        A25.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A25ItemStateChanged(evt);
            }
        });
        A25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A25ActionPerformed(evt);
            }
        });
        A25.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A25KeyPressed(evt);
            }
        });
        FormInput.add(A25);
        A25.setBounds(150, 2360, 220, 23);

        jLabel297.setText("Keterangan tambahan:");
        jLabel297.setName("jLabel297"); // NOI18N
        FormInput.add(jLabel297);
        jLabel297.setBounds(20, 2400, 130, 23);

        Gs173.setEditable(false);
        Gs173.setHighlighter(null);
        Gs173.setName("Gs173"); // NOI18N
        FormInput.add(Gs173);
        Gs173.setBounds(160, 2400, 230, 23);

        jLabel298.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel298.setText("ORGAN KANDUNGAN IBU HAMIL");
        jLabel298.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel298.setName("jLabel298"); // NOI18N
        FormInput.add(jLabel298);
        jLabel298.setBounds(20, 2460, 190, 23);

        jLabel299.setText("Nyeri Perut Ibu hamil:");
        jLabel299.setName("jLabel299"); // NOI18N
        FormInput.add(jLabel299);
        jLabel299.setBounds(20, 2480, 130, 23);

        A26.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lower Abdominal Pain", "Nyeri tekan supra pubic", "Acute Abdomen", "Nyeri tekan regio lumbalis D", "Nyeri tekan mc-burney", "Acute abdomen", " " }));
        A26.setName("A26"); // NOI18N
        A26.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A26ItemStateChanged(evt);
            }
        });
        A26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A26ActionPerformed(evt);
            }
        });
        A26.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A26KeyPressed(evt);
            }
        });
        FormInput.add(A26);
        A26.setBounds(160, 2480, 180, 23);

        jLabel300.setText("Vesica Urinaria:");
        jLabel300.setName("jLabel300"); // NOI18N
        FormInput.add(jLabel300);
        jLabel300.setBounds(310, 2480, 130, 23);

        A27.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Terisi", "Kosong", "Trabeculae", "Penebalan dinding", "Diverticle", "Batu" }));
        A27.setName("A27"); // NOI18N
        A27.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A27ItemStateChanged(evt);
            }
        });
        A27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A27ActionPerformed(evt);
            }
        });
        A27.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A27KeyPressed(evt);
            }
        });
        FormInput.add(A27);
        A27.setBounds(450, 2480, 120, 23);

        jLabel301.setText("Uterus:");
        jLabel301.setName("jLabel301"); // NOI18N
        FormInput.add(jLabel301);
        jLabel301.setBounds(510, 2480, 130, 23);

        A28.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dalam batas normal", "Antefleksi", "Retrofleksi", "Anteversi", "Regroversi", "Ditemukan myoma uteri" }));
        A28.setName("A28"); // NOI18N
        A28.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A28ItemStateChanged(evt);
            }
        });
        A28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A28ActionPerformed(evt);
            }
        });
        A28.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A28KeyPressed(evt);
            }
        });
        FormInput.add(A28);
        A28.setBounds(650, 2480, 220, 23);

        jLabel302.setText("Ovarium Kanan:");
        jLabel302.setName("jLabel302"); // NOI18N
        FormInput.add(jLabel302);
        jLabel302.setBounds(20, 2510, 130, 23);

        A29.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dalam batas normal", "Ditemukan kista ovarium" }));
        A29.setName("A29"); // NOI18N
        A29.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A29ItemStateChanged(evt);
            }
        });
        A29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A29ActionPerformed(evt);
            }
        });
        A29.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A29KeyPressed(evt);
            }
        });
        FormInput.add(A29);
        A29.setBounds(160, 2510, 220, 23);

        jLabel303.setText("Ovarium kiri:");
        jLabel303.setName("jLabel303"); // NOI18N
        FormInput.add(jLabel303);
        jLabel303.setBounds(510, 2510, 130, 23);

        A30.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dalam batas normal", "Ditemukan kista ovarium" }));
        A30.setName("A30"); // NOI18N
        A30.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A30ItemStateChanged(evt);
            }
        });
        A30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A30ActionPerformed(evt);
            }
        });
        A30.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A30KeyPressed(evt);
            }
        });
        FormInput.add(A30);
        A30.setBounds(650, 2510, 220, 23);

        jLabel304.setText("Cavum douglas:");
        jLabel304.setName("jLabel304"); // NOI18N
        FormInput.add(jLabel304);
        jLabel304.setBounds(20, 2540, 130, 23);

        A31.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dalam batas normal", "Ditemukan cairan bebas banyak", "Ditemukan cairan bebas sedikit" }));
        A31.setName("A31"); // NOI18N
        A31.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A31ItemStateChanged(evt);
            }
        });
        A31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A31ActionPerformed(evt);
            }
        });
        A31.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A31KeyPressed(evt);
            }
        });
        FormInput.add(A31);
        A31.setBounds(160, 2540, 220, 23);

        jLabel305.setText("Cervix:");
        jLabel305.setName("jLabel305"); // NOI18N
        FormInput.add(jLabel305);
        jLabel305.setBounds(510, 2540, 130, 23);

        A32.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dalam batas normal", "Shortening canal <2,5 cm", "Funeling", "Hourglass apprearance" }));
        A32.setName("A32"); // NOI18N
        A32.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A32ItemStateChanged(evt);
            }
        });
        A32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A32ActionPerformed(evt);
            }
        });
        A32.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A32KeyPressed(evt);
            }
        });
        FormInput.add(A32);
        A32.setBounds(650, 2540, 220, 23);

        jLabel306.setText("Keterangan tambahan:");
        jLabel306.setName("jLabel306"); // NOI18N
        FormInput.add(jLabel306);
        jLabel306.setBounds(20, 2570, 130, 23);

        Gs174.setEditable(false);
        Gs174.setHighlighter(null);
        Gs174.setName("Gs174"); // NOI18N
        FormInput.add(Gs174);
        Gs174.setBounds(160, 2570, 230, 23);

        jLabel307.setText("Cavum septum Pellucidum:");
        jLabel307.setName("jLabel307"); // NOI18N
        FormInput.add(jLabel307);
        jLabel307.setBounds(500, 1790, 130, 23);

        A33.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A33.setName("A33"); // NOI18N
        A33.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A33ItemStateChanged(evt);
            }
        });
        A33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A33ActionPerformed(evt);
            }
        });
        A33.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A33KeyPressed(evt);
            }
        });
        FormInput.add(A33);
        A33.setBounds(640, 1790, 220, 23);

        jLabel308.setText("Cisterna magna:");
        jLabel308.setName("jLabel308"); // NOI18N
        FormInput.add(jLabel308);
        jLabel308.setBounds(10, 1820, 130, 23);

        jLabel309.setText("Nuchal fold:");
        jLabel309.setName("jLabel309"); // NOI18N
        FormInput.add(jLabel309);
        jLabel309.setBounds(10, 1850, 130, 23);

        A34.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A34.setName("A34"); // NOI18N
        A34.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A34ItemStateChanged(evt);
            }
        });
        A34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A34ActionPerformed(evt);
            }
        });
        A34.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A34KeyPressed(evt);
            }
        });
        FormInput.add(A34);
        A34.setBounds(150, 1850, 220, 23);

        A35.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A35.setName("A35"); // NOI18N
        A35.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A35ItemStateChanged(evt);
            }
        });
        A35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A35ActionPerformed(evt);
            }
        });
        A35.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A35KeyPressed(evt);
            }
        });
        FormInput.add(A35);
        A35.setBounds(150, 1820, 220, 23);

        jLabel310.setText("Vermis cerebelli:");
        jLabel310.setName("jLabel310"); // NOI18N
        FormInput.add(jLabel310);
        jLabel310.setBounds(500, 1820, 130, 23);

        jLabel311.setText("Palatum:");
        jLabel311.setName("jLabel311"); // NOI18N
        FormInput.add(jLabel311);
        jLabel311.setBounds(500, 1850, 130, 23);

        A36.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A36.setName("A36"); // NOI18N
        A36.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A36ItemStateChanged(evt);
            }
        });
        A36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A36ActionPerformed(evt);
            }
        });
        A36.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A36KeyPressed(evt);
            }
        });
        FormInput.add(A36);
        A36.setBounds(640, 1850, 220, 23);

        A37.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Menebal >= 3 mm", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A37.setName("A37"); // NOI18N
        A37.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A37ItemStateChanged(evt);
            }
        });
        A37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A37ActionPerformed(evt);
            }
        });
        A37.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A37KeyPressed(evt);
            }
        });
        FormInput.add(A37);
        A37.setBounds(640, 1820, 220, 23);

        jLabel312.setText("Orbita:");
        jLabel312.setName("jLabel312"); // NOI18N
        FormInput.add(jLabel312);
        jLabel312.setBounds(10, 1880, 130, 23);

        jLabel313.setText("Face profile::");
        jLabel313.setName("jLabel313"); // NOI18N
        FormInput.add(jLabel313);
        jLabel313.setBounds(10, 1910, 130, 23);

        A38.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A38.setName("A38"); // NOI18N
        A38.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A38ItemStateChanged(evt);
            }
        });
        A38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A38ActionPerformed(evt);
            }
        });
        A38.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A38KeyPressed(evt);
            }
        });
        FormInput.add(A38);
        A38.setBounds(150, 1910, 220, 23);

        A39.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A39.setName("A39"); // NOI18N
        A39.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A39ItemStateChanged(evt);
            }
        });
        A39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A39ActionPerformed(evt);
            }
        });
        A39.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A39KeyPressed(evt);
            }
        });
        FormInput.add(A39);
        A39.setBounds(150, 1880, 220, 23);

        jLabel314.setText("Lensa mata:");
        jLabel314.setName("jLabel314"); // NOI18N
        FormInput.add(jLabel314);
        jLabel314.setBounds(500, 1880, 130, 23);

        jLabel315.setText("Nasal bone:");
        jLabel315.setName("jLabel315"); // NOI18N
        FormInput.add(jLabel315);
        jLabel315.setBounds(500, 1910, 130, 23);

        A40.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A40.setName("A40"); // NOI18N
        A40.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A40ItemStateChanged(evt);
            }
        });
        A40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A40ActionPerformed(evt);
            }
        });
        A40.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A40KeyPressed(evt);
            }
        });
        FormInput.add(A40);
        A40.setBounds(640, 1910, 220, 23);

        A41.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Menebal >= 3 mm", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A41.setName("A41"); // NOI18N
        A41.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A41ItemStateChanged(evt);
            }
        });
        A41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A41ActionPerformed(evt);
            }
        });
        A41.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A41KeyPressed(evt);
            }
        });
        FormInput.add(A41);
        A41.setBounds(640, 1880, 220, 23);

        jLabel316.setText("Retronasal triangle:");
        jLabel316.setName("jLabel316"); // NOI18N
        FormInput.add(jLabel316);
        jLabel316.setBounds(10, 1940, 130, 23);

        jLabel317.setText("Bibir bawah:");
        jLabel317.setName("jLabel317"); // NOI18N
        FormInput.add(jLabel317);
        jLabel317.setBounds(10, 1970, 130, 23);

        A42.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A42.setName("A42"); // NOI18N
        A42.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A42ItemStateChanged(evt);
            }
        });
        A42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A42ActionPerformed(evt);
            }
        });
        A42.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A42KeyPressed(evt);
            }
        });
        FormInput.add(A42);
        A42.setBounds(150, 1970, 220, 23);

        A43.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A43.setName("A43"); // NOI18N
        A43.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A43ItemStateChanged(evt);
            }
        });
        A43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A43ActionPerformed(evt);
            }
        });
        A43.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A43KeyPressed(evt);
            }
        });
        FormInput.add(A43);
        A43.setBounds(150, 1940, 220, 23);

        jLabel318.setText("Bibir atas:");
        jLabel318.setName("jLabel318"); // NOI18N
        FormInput.add(jLabel318);
        jLabel318.setBounds(500, 1940, 130, 23);

        jLabel319.setText("Cavum septum Pellucidum:");
        jLabel319.setName("jLabel319"); // NOI18N
        FormInput.add(jLabel319);
        jLabel319.setBounds(500, 1970, 130, 23);

        A44.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A44.setName("A44"); // NOI18N
        A44.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A44ItemStateChanged(evt);
            }
        });
        A44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A44ActionPerformed(evt);
            }
        });
        A44.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A44KeyPressed(evt);
            }
        });
        FormInput.add(A44);
        A44.setBounds(640, 1970, 220, 23);

        A45.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Menebal >= 3 mm", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A45.setName("A45"); // NOI18N
        A45.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A45ItemStateChanged(evt);
            }
        });
        A45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A45ActionPerformed(evt);
            }
        });
        A45.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A45KeyPressed(evt);
            }
        });
        FormInput.add(A45);
        A45.setBounds(640, 1940, 220, 23);

        jLabel320.setText("Mandibula:");
        jLabel320.setName("jLabel320"); // NOI18N
        FormInput.add(jLabel320);
        jLabel320.setBounds(10, 2000, 130, 23);

        jLabel321.setText("Simetrical Lung fields:");
        jLabel321.setName("jLabel321"); // NOI18N
        FormInput.add(jLabel321);
        jLabel321.setBounds(10, 2030, 130, 23);

        A46.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A46.setName("A46"); // NOI18N
        A46.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A46ItemStateChanged(evt);
            }
        });
        A46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A46ActionPerformed(evt);
            }
        });
        A46.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A46KeyPressed(evt);
            }
        });
        FormInput.add(A46);
        A46.setBounds(150, 2030, 220, 23);

        A47.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A47.setName("A47"); // NOI18N
        A47.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A47ItemStateChanged(evt);
            }
        });
        A47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A47ActionPerformed(evt);
            }
        });
        A47.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A47KeyPressed(evt);
            }
        });
        FormInput.add(A47);
        A47.setBounds(150, 2000, 220, 23);

        jLabel322.setText("Leher:");
        jLabel322.setName("jLabel322"); // NOI18N
        FormInput.add(jLabel322);
        jLabel322.setBounds(500, 2000, 130, 23);

        jLabel323.setText("ICEF:");
        jLabel323.setName("jLabel323"); // NOI18N
        FormInput.add(jLabel323);
        jLabel323.setBounds(500, 2030, 130, 23);

        A48.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak ada", "Tidak dapat diamat dengan baik karena kendala teknis" }));
        A48.setName("A48"); // NOI18N
        A48.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A48ItemStateChanged(evt);
            }
        });
        A48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A48ActionPerformed(evt);
            }
        });
        A48.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A48KeyPressed(evt);
            }
        });
        FormInput.add(A48);
        A48.setBounds(640, 2030, 220, 23);

        A49.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Menebal >= 3 mm", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A49.setName("A49"); // NOI18N
        A49.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A49ItemStateChanged(evt);
            }
        });
        A49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A49ActionPerformed(evt);
            }
        });
        A49.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A49KeyPressed(evt);
            }
        });
        FormInput.add(A49);
        A49.setBounds(640, 2000, 220, 23);

        jLabel324.setText("Cardiac regular activities:");
        jLabel324.setName("jLabel324"); // NOI18N
        FormInput.add(jLabel324);
        jLabel324.setBounds(10, 2060, 130, 23);

        jLabel325.setText("4CV:");
        jLabel325.setName("jLabel325"); // NOI18N
        FormInput.add(jLabel325);
        jLabel325.setBounds(10, 2090, 130, 23);

        A50.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A50.setName("A50"); // NOI18N
        A50.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A50ItemStateChanged(evt);
            }
        });
        A50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A50ActionPerformed(evt);
            }
        });
        A50.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A50KeyPressed(evt);
            }
        });
        FormInput.add(A50);
        A50.setBounds(150, 2090, 220, 23);

        A51.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A51.setName("A51"); // NOI18N
        A51.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A51ItemStateChanged(evt);
            }
        });
        A51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A51ActionPerformed(evt);
            }
        });
        A51.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A51KeyPressed(evt);
            }
        });
        FormInput.add(A51);
        A51.setBounds(150, 2060, 220, 23);

        jLabel326.setText("Cardiac situs:");
        jLabel326.setName("jLabel326"); // NOI18N
        FormInput.add(jLabel326);
        jLabel326.setBounds(500, 2060, 130, 23);

        jLabel327.setText("LVOT:");
        jLabel327.setName("jLabel327"); // NOI18N
        FormInput.add(jLabel327);
        jLabel327.setBounds(500, 2090, 130, 23);

        A52.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A52.setName("A52"); // NOI18N
        A52.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A52ItemStateChanged(evt);
            }
        });
        A52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A52ActionPerformed(evt);
            }
        });
        A52.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A52KeyPressed(evt);
            }
        });
        FormInput.add(A52);
        A52.setBounds(640, 2090, 220, 23);

        A53.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Situs solitus", "Situs inversus" }));
        A53.setName("A53"); // NOI18N
        A53.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A53ItemStateChanged(evt);
            }
        });
        A53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A53ActionPerformed(evt);
            }
        });
        A53.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A53KeyPressed(evt);
            }
        });
        FormInput.add(A53);
        A53.setBounds(640, 2060, 220, 23);

        jLabel328.setText("RVOT:");
        jLabel328.setName("jLabel328"); // NOI18N
        FormInput.add(jLabel328);
        jLabel328.setBounds(10, 2120, 130, 23);

        A54.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A54.setName("A54"); // NOI18N
        A54.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A54ItemStateChanged(evt);
            }
        });
        A54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A54ActionPerformed(evt);
            }
        });
        A54.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A54KeyPressed(evt);
            }
        });
        FormInput.add(A54);
        A54.setBounds(150, 2120, 220, 23);

        jLabel329.setText("3VV:");
        jLabel329.setName("jLabel329"); // NOI18N
        FormInput.add(jLabel329);
        jLabel329.setBounds(500, 2120, 130, 23);

        A55.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A55.setName("A55"); // NOI18N
        A55.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A55ItemStateChanged(evt);
            }
        });
        A55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A55ActionPerformed(evt);
            }
        });
        A55.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A55KeyPressed(evt);
            }
        });
        FormInput.add(A55);
        A55.setBounds(640, 2120, 220, 23);

        jLabel330.setText("Diafragma:");
        jLabel330.setName("jLabel330"); // NOI18N
        FormInput.add(jLabel330);
        jLabel330.setBounds(10, 2150, 130, 23);

        A56.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A56.setName("A56"); // NOI18N
        A56.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A56ItemStateChanged(evt);
            }
        });
        A56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A56ActionPerformed(evt);
            }
        });
        A56.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A56KeyPressed(evt);
            }
        });
        FormInput.add(A56);
        A56.setBounds(150, 2150, 220, 23);

        jLabel331.setText("Lambung:");
        jLabel331.setName("jLabel331"); // NOI18N
        FormInput.add(jLabel331);
        jLabel331.setBounds(500, 2150, 130, 23);

        A57.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A57.setName("A57"); // NOI18N
        A57.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A57ItemStateChanged(evt);
            }
        });
        A57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A57ActionPerformed(evt);
            }
        });
        A57.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A57KeyPressed(evt);
            }
        });
        FormInput.add(A57);
        A57.setBounds(640, 2150, 220, 23);

        jLabel332.setText("Liver/hati:");
        jLabel332.setName("jLabel332"); // NOI18N
        FormInput.add(jLabel332);
        jLabel332.setBounds(10, 2180, 130, 23);

        A58.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A58.setName("A58"); // NOI18N
        A58.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A58ItemStateChanged(evt);
            }
        });
        A58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A58ActionPerformed(evt);
            }
        });
        A58.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A58KeyPressed(evt);
            }
        });
        FormInput.add(A58);
        A58.setBounds(150, 2180, 220, 23);

        jLabel333.setText("Ginjal:");
        jLabel333.setName("jLabel333"); // NOI18N
        FormInput.add(jLabel333);
        jLabel333.setBounds(500, 2180, 130, 23);

        A59.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A59.setName("A59"); // NOI18N
        A59.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A59ItemStateChanged(evt);
            }
        });
        A59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A59ActionPerformed(evt);
            }
        });
        A59.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A59KeyPressed(evt);
            }
        });
        FormInput.add(A59);
        A59.setBounds(640, 2180, 220, 23);

        jLabel334.setText("Dinding perut:");
        jLabel334.setName("jLabel334"); // NOI18N
        FormInput.add(jLabel334);
        jLabel334.setBounds(10, 2210, 130, 23);

        A60.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A60.setName("A60"); // NOI18N
        A60.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A60ItemStateChanged(evt);
            }
        });
        A60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A60ActionPerformed(evt);
            }
        });
        A60.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A60KeyPressed(evt);
            }
        });
        FormInput.add(A60);
        A60.setBounds(150, 2210, 220, 23);

        jLabel335.setText("Vesica urinaria:");
        jLabel335.setName("jLabel335"); // NOI18N
        FormInput.add(jLabel335);
        jLabel335.setBounds(500, 2210, 130, 23);

        A61.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A61.setName("A61"); // NOI18N
        A61.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A61ItemStateChanged(evt);
            }
        });
        A61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A61ActionPerformed(evt);
            }
        });
        A61.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A61KeyPressed(evt);
            }
        });
        FormInput.add(A61);
        A61.setBounds(640, 2210, 220, 23);

        jLabel336.setText("Cervical spine:");
        jLabel336.setName("jLabel336"); // NOI18N
        FormInput.add(jLabel336);
        jLabel336.setBounds(10, 2240, 130, 23);

        A62.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A62.setName("A62"); // NOI18N
        A62.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A62ItemStateChanged(evt);
            }
        });
        A62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A62ActionPerformed(evt);
            }
        });
        A62.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A62KeyPressed(evt);
            }
        });
        FormInput.add(A62);
        A62.setBounds(150, 2240, 220, 23);

        jLabel337.setText("Thoracal spine");
        jLabel337.setName("jLabel337"); // NOI18N
        FormInput.add(jLabel337);
        jLabel337.setBounds(500, 2240, 130, 23);

        A63.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A63.setName("A63"); // NOI18N
        A63.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A63ItemStateChanged(evt);
            }
        });
        A63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A63ActionPerformed(evt);
            }
        });
        A63.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A63KeyPressed(evt);
            }
        });
        FormInput.add(A63);
        A63.setBounds(640, 2240, 220, 23);

        jLabel338.setText("Lumbar spine:");
        jLabel338.setName("jLabel338"); // NOI18N
        FormInput.add(jLabel338);
        jLabel338.setBounds(10, 2270, 130, 23);

        A64.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A64.setName("A64"); // NOI18N
        A64.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A64ItemStateChanged(evt);
            }
        });
        A64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A64ActionPerformed(evt);
            }
        });
        A64.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A64KeyPressed(evt);
            }
        });
        FormInput.add(A64);
        A64.setBounds(150, 2270, 220, 23);

        jLabel339.setText("Sacral spine:");
        jLabel339.setName("jLabel339"); // NOI18N
        FormInput.add(jLabel339);
        jLabel339.setBounds(500, 2270, 130, 23);

        A65.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A65.setName("A65"); // NOI18N
        A65.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A65ItemStateChanged(evt);
            }
        });
        A65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A65ActionPerformed(evt);
            }
        });
        A65.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A65KeyPressed(evt);
            }
        });
        FormInput.add(A65);
        A65.setBounds(640, 2270, 220, 23);

        jLabel340.setText("Skin line punggung:");
        jLabel340.setName("jLabel340"); // NOI18N
        FormInput.add(jLabel340);
        jLabel340.setBounds(10, 2300, 130, 23);

        A66.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Kesan abnormal", "Tidak diamati dengan baik karena kendala teknis" }));
        A66.setName("A66"); // NOI18N
        A66.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A66ItemStateChanged(evt);
            }
        });
        A66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A66ActionPerformed(evt);
            }
        });
        A66.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A66KeyPressed(evt);
            }
        });
        FormInput.add(A66);
        A66.setBounds(150, 2300, 220, 23);

        jLabel341.setText("Upper extermities:");
        jLabel341.setName("jLabel341"); // NOI18N
        FormInput.add(jLabel341);
        jLabel341.setBounds(500, 2300, 130, 23);

        A67.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada 2", "Yang tampak 1", "Cuma ada 1", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A67.setName("A67"); // NOI18N
        A67.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A67ItemStateChanged(evt);
            }
        });
        A67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A67ActionPerformed(evt);
            }
        });
        A67.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A67KeyPressed(evt);
            }
        });
        FormInput.add(A67);
        A67.setBounds(640, 2300, 220, 23);

        jLabel342.setText("Lower extremities:");
        jLabel342.setName("jLabel342"); // NOI18N
        FormInput.add(jLabel342);
        jLabel342.setBounds(10, 2330, 130, 23);

        A68.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada 2", "Yang tampak 1", "Cuma ada 1", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A68.setName("A68"); // NOI18N
        A68.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A68ItemStateChanged(evt);
            }
        });
        A68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A68ActionPerformed(evt);
            }
        });
        A68.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A68KeyPressed(evt);
            }
        });
        FormInput.add(A68);
        A68.setBounds(150, 2330, 220, 23);

        jLabel343.setText("Orientasi ankle kaki kanan:");
        jLabel343.setName("jLabel343"); // NOI18N
        FormInput.add(jLabel343);
        jLabel343.setBounds(450, 2330, 180, 23);

        A69.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kesan normal", "Menebal >= 3 mm", "Tidak dapat diamati dengan baik karena kendala teknis" }));
        A69.setName("A69"); // NOI18N
        A69.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                A69ItemStateChanged(evt);
            }
        });
        A69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A69ActionPerformed(evt);
            }
        });
        A69.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A69KeyPressed(evt);
            }
        });
        FormInput.add(A69);
        A69.setBounds(640, 2330, 220, 23);

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

    private void A11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A11KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A11KeyPressed

    private void A11ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A11ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A11ItemStateChanged

    private void A8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A8KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A8KeyPressed

    private void A8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A8ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A8ItemStateChanged

    private void A10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A10KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A10KeyPressed

    private void A10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A10ActionPerformed

    private void A10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A10ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A10ItemStateChanged

    private void A9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A9KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A9KeyPressed

    private void A9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A9ActionPerformed

    private void A9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A9ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A9ItemStateChanged

    private void A7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A7KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A7KeyPressed

    private void A7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A7ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A7ItemStateChanged

    private void A6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A6KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A6KeyPressed

    private void A6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A6ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A6ItemStateChanged

    private void A5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A5KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A5KeyPressed

    private void A5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A5ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A5ItemStateChanged

    private void A4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A4KeyPressed

    private void A4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A4ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A4ItemStateChanged

    private void A3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A3KeyPressed

    private void A3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A3ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A3ItemStateChanged

    private void TglSkrining1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglSkrining1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TglSkrining1KeyPressed

    private void Gs29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Gs29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Gs29ActionPerformed

    private void BtnPtgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPtgKeyPressed
        Valid.pindah(evt,JadwalKontrol,BtnSimpan);
    }//GEN-LAST:event_BtnPtgKeyPressed

    private void BtnPtgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPtgActionPerformed
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_BtnPtgActionPerformed

    private void kdptgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdptgKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kdptgKeyPressed

    private void TglSkriningKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglSkriningKeyPressed
        Valid.pindah(evt,TCari,A1);
    }//GEN-LAST:event_TglSkriningKeyPressed

    private void TglSkriningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TglSkriningActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TglSkriningActionPerformed

    private void A1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A1KeyPressed
        Valid.pindah(evt,TglSkrining,A2);
    }//GEN-LAST:event_A1KeyPressed

    private void A1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A1ItemStateChanged

    }//GEN-LAST:event_A1ItemStateChanged

    private void A12ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A12ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A12ItemStateChanged

    private void A12KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A12KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A12KeyPressed

    private void A13ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A13ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A13ItemStateChanged

    private void A13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A13KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A13KeyPressed

    private void A14ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A14ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A14ItemStateChanged

    private void A14KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A14KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A14KeyPressed

    private void A15ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A15ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A15ItemStateChanged

    private void A15KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A15KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A15KeyPressed

    private void A16ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A16ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A16ItemStateChanged

    private void A16KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A16KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A16KeyPressed

    private void A16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A16ActionPerformed

    private void A17ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A17ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A17ItemStateChanged

    private void A17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A17ActionPerformed

    private void A17KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A17KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A17KeyPressed

    private void A18ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A18ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A18ItemStateChanged

    private void A18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A18ActionPerformed

    private void A18KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A18KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A18KeyPressed

    private void A19ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A19ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A19ItemStateChanged

    private void A19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A19ActionPerformed

    private void A19KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A19KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A19KeyPressed

    private void A20ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A20ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A20ItemStateChanged

    private void A20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A20ActionPerformed

    private void A20KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A20KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A20KeyPressed

    private void A21ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A21ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A21ItemStateChanged

    private void A21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A21ActionPerformed

    private void A21KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A21KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A21KeyPressed

    private void A22ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A22ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A22ItemStateChanged

    private void A22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A22ActionPerformed

    private void A22KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A22KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A22KeyPressed

    private void A24ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A24ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A24ItemStateChanged

    private void A24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A24ActionPerformed

    private void A24KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A24KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A24KeyPressed

    private void A23ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A23ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A23ItemStateChanged

    private void A23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A23ActionPerformed

    private void A23KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A23KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A23KeyPressed

    private void A25ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A25ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A25ItemStateChanged

    private void A25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A25ActionPerformed

    private void A25KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A25KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A25KeyPressed

    private void A26ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A26ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A26ItemStateChanged

    private void A26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A26ActionPerformed

    private void A26KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A26KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A26KeyPressed

    private void A27ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A27ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A27ItemStateChanged

    private void A27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A27ActionPerformed

    private void A27KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A27KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A27KeyPressed

    private void A28ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A28ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A28ItemStateChanged

    private void A28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A28ActionPerformed

    private void A28KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A28KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A28KeyPressed

    private void A29ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A29ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A29ItemStateChanged

    private void A29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A29ActionPerformed

    private void A29KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A29KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A29KeyPressed

    private void A30ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A30ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A30ItemStateChanged

    private void A30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A30ActionPerformed

    private void A30KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A30KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A30KeyPressed

    private void A31ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A31ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A31ItemStateChanged

    private void A31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A31ActionPerformed

    private void A31KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A31KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A31KeyPressed

    private void A32ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A32ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A32ItemStateChanged

    private void A32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A32ActionPerformed

    private void A32KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A32KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A32KeyPressed

    private void A33ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A33ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A33ItemStateChanged

    private void A33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A33ActionPerformed

    private void A33KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A33KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A33KeyPressed

    private void A34ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A34ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A34ItemStateChanged

    private void A34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A34ActionPerformed

    private void A34KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A34KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A34KeyPressed

    private void A35ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A35ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A35ItemStateChanged

    private void A35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A35ActionPerformed

    private void A35KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A35KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A35KeyPressed

    private void A36ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A36ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A36ItemStateChanged

    private void A36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A36ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A36ActionPerformed

    private void A36KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A36KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A36KeyPressed

    private void A37ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A37ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A37ItemStateChanged

    private void A37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A37ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A37ActionPerformed

    private void A37KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A37KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A37KeyPressed

    private void A38ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A38ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A38ItemStateChanged

    private void A38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A38ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A38ActionPerformed

    private void A38KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A38KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A38KeyPressed

    private void A39ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A39ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A39ItemStateChanged

    private void A39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A39ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A39ActionPerformed

    private void A39KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A39KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A39KeyPressed

    private void A40ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A40ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A40ItemStateChanged

    private void A40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A40ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A40ActionPerformed

    private void A40KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A40KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A40KeyPressed

    private void A41ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A41ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A41ItemStateChanged

    private void A41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A41ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A41ActionPerformed

    private void A41KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A41KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A41KeyPressed

    private void A42ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A42ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A42ItemStateChanged

    private void A42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A42ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A42ActionPerformed

    private void A42KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A42KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A42KeyPressed

    private void A43ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A43ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A43ItemStateChanged

    private void A43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A43ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A43ActionPerformed

    private void A43KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A43KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A43KeyPressed

    private void A44ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A44ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A44ItemStateChanged

    private void A44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A44ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A44ActionPerformed

    private void A44KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A44KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A44KeyPressed

    private void A45ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A45ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A45ItemStateChanged

    private void A45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A45ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A45ActionPerformed

    private void A45KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A45KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A45KeyPressed

    private void A46ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A46ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A46ItemStateChanged

    private void A46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A46ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A46ActionPerformed

    private void A46KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A46KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A46KeyPressed

    private void A47ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A47ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A47ItemStateChanged

    private void A47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A47ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A47ActionPerformed

    private void A47KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A47KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A47KeyPressed

    private void A48ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A48ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A48ItemStateChanged

    private void A48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A48ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A48ActionPerformed

    private void A48KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A48KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A48KeyPressed

    private void A49ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A49ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A49ItemStateChanged

    private void A49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A49ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A49ActionPerformed

    private void A49KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A49KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A49KeyPressed

    private void A50ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A50ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A50ItemStateChanged

    private void A50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A50ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A50ActionPerformed

    private void A50KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A50KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A50KeyPressed

    private void A51ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A51ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A51ItemStateChanged

    private void A51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A51ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A51ActionPerformed

    private void A51KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A51KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A51KeyPressed

    private void A52ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A52ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A52ItemStateChanged

    private void A52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A52ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A52ActionPerformed

    private void A52KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A52KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A52KeyPressed

    private void A53ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A53ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A53ItemStateChanged

    private void A53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A53ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A53ActionPerformed

    private void A53KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A53KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A53KeyPressed

    private void A54ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A54ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A54ItemStateChanged

    private void A54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A54ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A54ActionPerformed

    private void A54KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A54KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A54KeyPressed

    private void A55ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A55ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A55ItemStateChanged

    private void A55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A55ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A55ActionPerformed

    private void A55KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A55KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A55KeyPressed

    private void A56ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A56ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A56ItemStateChanged

    private void A56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A56ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A56ActionPerformed

    private void A56KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A56KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A56KeyPressed

    private void A57ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A57ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A57ItemStateChanged

    private void A57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A57ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A57ActionPerformed

    private void A57KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A57KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A57KeyPressed

    private void A58ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A58ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A58ItemStateChanged

    private void A58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A58ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A58ActionPerformed

    private void A58KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A58KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A58KeyPressed

    private void A59ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A59ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A59ItemStateChanged

    private void A59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A59ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A59ActionPerformed

    private void A59KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A59KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A59KeyPressed

    private void A60ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A60ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A60ItemStateChanged

    private void A60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A60ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A60ActionPerformed

    private void A60KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A60KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A60KeyPressed

    private void A61ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A61ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A61ItemStateChanged

    private void A61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A61ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A61ActionPerformed

    private void A61KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A61KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A61KeyPressed

    private void A62ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A62ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A62ItemStateChanged

    private void A62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A62ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A62ActionPerformed

    private void A62KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A62KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A62KeyPressed

    private void A63ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A63ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A63ItemStateChanged

    private void A63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A63ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A63ActionPerformed

    private void A63KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A63KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A63KeyPressed

    private void A64ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A64ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A64ItemStateChanged

    private void A64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A64ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A64ActionPerformed

    private void A64KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A64KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A64KeyPressed

    private void A65ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A65ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A65ItemStateChanged

    private void A65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A65ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A65ActionPerformed

    private void A65KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A65KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A65KeyPressed

    private void A66ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A66ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A66ItemStateChanged

    private void A66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A66ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A66ActionPerformed

    private void A66KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A66KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A66KeyPressed

    private void A67ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A67ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A67ItemStateChanged

    private void A67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A67ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A67ActionPerformed

    private void A67KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A67KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A67KeyPressed

    private void A68ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A68ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A68ItemStateChanged

    private void A68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A68ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A68ActionPerformed

    private void A68KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A68KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A68KeyPressed

    private void A69ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_A69ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_A69ItemStateChanged

    private void A69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A69ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A69ActionPerformed

    private void A69KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A69KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_A69KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMUsgKehamilan dialog = new RMUsgKehamilan(new javax.swing.JFrame(), true);
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
    private widget.ComboBox A10;
    private widget.ComboBox A11;
    private widget.ComboBox A12;
    private widget.ComboBox A13;
    private widget.ComboBox A14;
    private widget.ComboBox A15;
    private widget.ComboBox A16;
    private widget.ComboBox A17;
    private widget.ComboBox A18;
    private widget.ComboBox A19;
    private widget.ComboBox A20;
    private widget.ComboBox A21;
    private widget.ComboBox A22;
    private widget.ComboBox A23;
    private widget.ComboBox A24;
    private widget.ComboBox A25;
    private widget.ComboBox A26;
    private widget.ComboBox A27;
    private widget.ComboBox A28;
    private widget.ComboBox A29;
    private widget.ComboBox A3;
    private widget.ComboBox A30;
    private widget.ComboBox A31;
    private widget.ComboBox A32;
    private widget.ComboBox A33;
    private widget.ComboBox A34;
    private widget.ComboBox A35;
    private widget.ComboBox A36;
    private widget.ComboBox A37;
    private widget.ComboBox A38;
    private widget.ComboBox A39;
    private widget.ComboBox A4;
    private widget.ComboBox A40;
    private widget.ComboBox A41;
    private widget.ComboBox A42;
    private widget.ComboBox A43;
    private widget.ComboBox A44;
    private widget.ComboBox A45;
    private widget.ComboBox A46;
    private widget.ComboBox A47;
    private widget.ComboBox A48;
    private widget.ComboBox A49;
    private widget.ComboBox A5;
    private widget.ComboBox A50;
    private widget.ComboBox A51;
    private widget.ComboBox A52;
    private widget.ComboBox A53;
    private widget.ComboBox A54;
    private widget.ComboBox A55;
    private widget.ComboBox A56;
    private widget.ComboBox A57;
    private widget.ComboBox A58;
    private widget.ComboBox A59;
    private widget.ComboBox A6;
    private widget.ComboBox A60;
    private widget.ComboBox A61;
    private widget.ComboBox A62;
    private widget.ComboBox A63;
    private widget.ComboBox A64;
    private widget.ComboBox A65;
    private widget.ComboBox A66;
    private widget.ComboBox A67;
    private widget.ComboBox A68;
    private widget.ComboBox A69;
    private widget.ComboBox A7;
    private widget.ComboBox A8;
    private widget.ComboBox A9;
    private widget.TextBox Alamat;
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
    private widget.PanelBiasa FormInput;
    private widget.TextBox Gs;
    private widget.TextBox Gs1;
    private widget.TextBox Gs10;
    private widget.TextBox Gs100;
    private widget.TextBox Gs101;
    private widget.TextBox Gs102;
    private widget.TextBox Gs103;
    private widget.TextBox Gs104;
    private widget.TextBox Gs105;
    private widget.TextBox Gs106;
    private widget.TextBox Gs107;
    private widget.TextBox Gs108;
    private widget.TextBox Gs109;
    private widget.TextBox Gs11;
    private widget.TextBox Gs110;
    private widget.TextBox Gs111;
    private widget.TextBox Gs112;
    private widget.TextBox Gs113;
    private widget.TextBox Gs114;
    private widget.TextBox Gs115;
    private widget.TextBox Gs116;
    private widget.TextBox Gs117;
    private widget.TextBox Gs118;
    private widget.TextBox Gs119;
    private widget.TextBox Gs12;
    private widget.TextBox Gs120;
    private widget.TextBox Gs121;
    private widget.TextBox Gs122;
    private widget.TextBox Gs123;
    private widget.TextBox Gs124;
    private widget.TextBox Gs125;
    private widget.TextBox Gs126;
    private widget.TextBox Gs127;
    private widget.TextBox Gs128;
    private widget.TextBox Gs129;
    private widget.TextBox Gs13;
    private widget.TextBox Gs130;
    private widget.TextBox Gs131;
    private widget.TextBox Gs132;
    private widget.TextBox Gs133;
    private widget.TextBox Gs134;
    private widget.TextBox Gs135;
    private widget.TextBox Gs136;
    private widget.TextBox Gs137;
    private widget.TextBox Gs138;
    private widget.TextBox Gs139;
    private widget.TextBox Gs14;
    private widget.TextBox Gs140;
    private widget.TextBox Gs141;
    private widget.TextBox Gs142;
    private widget.TextBox Gs143;
    private widget.TextBox Gs144;
    private widget.TextBox Gs145;
    private widget.TextBox Gs146;
    private widget.TextBox Gs147;
    private widget.TextBox Gs148;
    private widget.TextBox Gs149;
    private widget.TextBox Gs15;
    private widget.TextBox Gs150;
    private widget.TextBox Gs151;
    private widget.TextBox Gs152;
    private widget.TextBox Gs153;
    private widget.TextBox Gs154;
    private widget.TextBox Gs155;
    private widget.TextBox Gs156;
    private widget.TextBox Gs157;
    private widget.TextBox Gs158;
    private widget.TextBox Gs159;
    private widget.TextBox Gs16;
    private widget.TextBox Gs160;
    private widget.TextBox Gs161;
    private widget.TextBox Gs162;
    private widget.TextBox Gs163;
    private widget.TextBox Gs164;
    private widget.TextBox Gs165;
    private widget.TextBox Gs166;
    private widget.TextBox Gs167;
    private widget.TextBox Gs168;
    private widget.TextBox Gs169;
    private widget.TextBox Gs17;
    private widget.TextBox Gs170;
    private widget.TextBox Gs171;
    private widget.TextBox Gs172;
    private widget.TextBox Gs173;
    private widget.TextBox Gs174;
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
    private widget.TextBox Gs30;
    private widget.TextBox Gs31;
    private widget.TextBox Gs32;
    private widget.TextBox Gs33;
    private widget.TextBox Gs34;
    private widget.TextBox Gs35;
    private widget.TextBox Gs36;
    private widget.TextBox Gs37;
    private widget.TextBox Gs38;
    private widget.TextBox Gs39;
    private widget.TextBox Gs4;
    private widget.TextBox Gs40;
    private widget.TextBox Gs41;
    private widget.TextBox Gs42;
    private widget.TextBox Gs43;
    private widget.TextBox Gs44;
    private widget.TextBox Gs45;
    private widget.TextBox Gs46;
    private widget.TextBox Gs47;
    private widget.TextBox Gs48;
    private widget.TextBox Gs49;
    private widget.TextBox Gs5;
    private widget.TextBox Gs50;
    private widget.TextBox Gs51;
    private widget.TextBox Gs52;
    private widget.TextBox Gs53;
    private widget.TextBox Gs54;
    private widget.TextBox Gs55;
    private widget.TextBox Gs56;
    private widget.TextBox Gs57;
    private widget.TextBox Gs58;
    private widget.TextBox Gs59;
    private widget.TextBox Gs6;
    private widget.TextBox Gs60;
    private widget.TextBox Gs61;
    private widget.TextBox Gs62;
    private widget.TextBox Gs63;
    private widget.TextBox Gs64;
    private widget.TextBox Gs65;
    private widget.TextBox Gs66;
    private widget.TextBox Gs67;
    private widget.TextBox Gs68;
    private widget.TextBox Gs69;
    private widget.TextBox Gs7;
    private widget.TextBox Gs70;
    private widget.TextBox Gs71;
    private widget.TextBox Gs72;
    private widget.TextBox Gs73;
    private widget.TextBox Gs74;
    private widget.TextBox Gs75;
    private widget.TextBox Gs76;
    private widget.TextBox Gs77;
    private widget.TextBox Gs78;
    private widget.TextBox Gs79;
    private widget.TextBox Gs8;
    private widget.TextBox Gs80;
    private widget.TextBox Gs81;
    private widget.TextBox Gs82;
    private widget.TextBox Gs83;
    private widget.TextBox Gs84;
    private widget.TextBox Gs85;
    private widget.TextBox Gs86;
    private widget.TextBox Gs87;
    private widget.TextBox Gs88;
    private widget.TextBox Gs89;
    private widget.TextBox Gs9;
    private widget.TextBox Gs90;
    private widget.TextBox Gs91;
    private widget.TextBox Gs92;
    private widget.TextBox Gs93;
    private widget.TextBox Gs94;
    private widget.TextBox Gs95;
    private widget.TextBox Gs96;
    private widget.TextBox Gs97;
    private widget.TextBox Gs98;
    private widget.TextBox Gs99;
    private widget.Label LCount;
    private javax.swing.JMenuItem MnCetakDeteksiDini;
    private widget.TextBox NamaPasien;
    private widget.TextBox NoRM;
    private widget.TextBox NoRawat;
    private javax.swing.JPanel PanelInput;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.Tanggal TglSkrining;
    private widget.Tanggal TglSkrining1;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel100;
    private widget.Label jLabel101;
    private widget.Label jLabel102;
    private widget.Label jLabel103;
    private widget.Label jLabel104;
    private widget.Label jLabel105;
    private widget.Label jLabel106;
    private widget.Label jLabel107;
    private widget.Label jLabel108;
    private widget.Label jLabel109;
    private widget.Label jLabel11;
    private widget.Label jLabel110;
    private widget.Label jLabel111;
    private widget.Label jLabel112;
    private widget.Label jLabel113;
    private widget.Label jLabel114;
    private widget.Label jLabel115;
    private widget.Label jLabel116;
    private widget.Label jLabel117;
    private widget.Label jLabel118;
    private widget.Label jLabel119;
    private widget.Label jLabel12;
    private widget.Label jLabel120;
    private widget.Label jLabel121;
    private widget.Label jLabel122;
    private widget.Label jLabel123;
    private widget.Label jLabel124;
    private widget.Label jLabel125;
    private widget.Label jLabel126;
    private widget.Label jLabel127;
    private widget.Label jLabel128;
    private widget.Label jLabel129;
    private widget.Label jLabel13;
    private widget.Label jLabel130;
    private widget.Label jLabel131;
    private widget.Label jLabel132;
    private widget.Label jLabel133;
    private widget.Label jLabel134;
    private widget.Label jLabel135;
    private widget.Label jLabel136;
    private widget.Label jLabel137;
    private widget.Label jLabel138;
    private widget.Label jLabel139;
    private widget.Label jLabel14;
    private widget.Label jLabel140;
    private widget.Label jLabel141;
    private widget.Label jLabel142;
    private widget.Label jLabel143;
    private widget.Label jLabel144;
    private widget.Label jLabel145;
    private widget.Label jLabel146;
    private widget.Label jLabel147;
    private widget.Label jLabel148;
    private widget.Label jLabel149;
    private widget.Label jLabel15;
    private widget.Label jLabel150;
    private widget.Label jLabel151;
    private widget.Label jLabel152;
    private widget.Label jLabel153;
    private widget.Label jLabel154;
    private widget.Label jLabel155;
    private widget.Label jLabel156;
    private widget.Label jLabel157;
    private widget.Label jLabel158;
    private widget.Label jLabel159;
    private widget.Label jLabel16;
    private widget.Label jLabel160;
    private widget.Label jLabel161;
    private widget.Label jLabel162;
    private widget.Label jLabel163;
    private widget.Label jLabel164;
    private widget.Label jLabel165;
    private widget.Label jLabel166;
    private widget.Label jLabel167;
    private widget.Label jLabel168;
    private widget.Label jLabel169;
    private widget.Label jLabel17;
    private widget.Label jLabel170;
    private widget.Label jLabel171;
    private widget.Label jLabel172;
    private widget.Label jLabel173;
    private widget.Label jLabel174;
    private widget.Label jLabel175;
    private widget.Label jLabel176;
    private widget.Label jLabel177;
    private widget.Label jLabel178;
    private widget.Label jLabel179;
    private widget.Label jLabel18;
    private widget.Label jLabel180;
    private widget.Label jLabel181;
    private widget.Label jLabel182;
    private widget.Label jLabel183;
    private widget.Label jLabel184;
    private widget.Label jLabel185;
    private widget.Label jLabel186;
    private widget.Label jLabel187;
    private widget.Label jLabel188;
    private widget.Label jLabel189;
    private widget.Label jLabel19;
    private widget.Label jLabel190;
    private widget.Label jLabel191;
    private widget.Label jLabel192;
    private widget.Label jLabel193;
    private widget.Label jLabel194;
    private widget.Label jLabel195;
    private widget.Label jLabel196;
    private widget.Label jLabel197;
    private widget.Label jLabel198;
    private widget.Label jLabel199;
    private widget.Label jLabel20;
    private widget.Label jLabel200;
    private widget.Label jLabel201;
    private widget.Label jLabel202;
    private widget.Label jLabel203;
    private widget.Label jLabel204;
    private widget.Label jLabel205;
    private widget.Label jLabel206;
    private widget.Label jLabel207;
    private widget.Label jLabel208;
    private widget.Label jLabel209;
    private widget.Label jLabel21;
    private widget.Label jLabel210;
    private widget.Label jLabel211;
    private widget.Label jLabel212;
    private widget.Label jLabel213;
    private widget.Label jLabel214;
    private widget.Label jLabel215;
    private widget.Label jLabel216;
    private widget.Label jLabel217;
    private widget.Label jLabel218;
    private widget.Label jLabel219;
    private widget.Label jLabel22;
    private widget.Label jLabel220;
    private widget.Label jLabel221;
    private widget.Label jLabel222;
    private widget.Label jLabel223;
    private widget.Label jLabel224;
    private widget.Label jLabel225;
    private widget.Label jLabel226;
    private widget.Label jLabel227;
    private widget.Label jLabel228;
    private widget.Label jLabel229;
    private widget.Label jLabel23;
    private widget.Label jLabel230;
    private widget.Label jLabel231;
    private widget.Label jLabel232;
    private widget.Label jLabel233;
    private widget.Label jLabel234;
    private widget.Label jLabel235;
    private widget.Label jLabel236;
    private widget.Label jLabel237;
    private widget.Label jLabel238;
    private widget.Label jLabel239;
    private widget.Label jLabel24;
    private widget.Label jLabel240;
    private widget.Label jLabel241;
    private widget.Label jLabel242;
    private widget.Label jLabel243;
    private widget.Label jLabel244;
    private widget.Label jLabel245;
    private widget.Label jLabel246;
    private widget.Label jLabel247;
    private widget.Label jLabel248;
    private widget.Label jLabel249;
    private widget.Label jLabel25;
    private widget.Label jLabel250;
    private widget.Label jLabel251;
    private widget.Label jLabel252;
    private widget.Label jLabel253;
    private widget.Label jLabel254;
    private widget.Label jLabel255;
    private widget.Label jLabel256;
    private widget.Label jLabel257;
    private widget.Label jLabel258;
    private widget.Label jLabel259;
    private widget.Label jLabel26;
    private widget.Label jLabel260;
    private widget.Label jLabel261;
    private widget.Label jLabel262;
    private widget.Label jLabel263;
    private widget.Label jLabel264;
    private widget.Label jLabel265;
    private widget.Label jLabel266;
    private widget.Label jLabel267;
    private widget.Label jLabel268;
    private widget.Label jLabel269;
    private widget.Label jLabel27;
    private widget.Label jLabel270;
    private widget.Label jLabel271;
    private widget.Label jLabel272;
    private widget.Label jLabel273;
    private widget.Label jLabel274;
    private widget.Label jLabel275;
    private widget.Label jLabel276;
    private widget.Label jLabel277;
    private widget.Label jLabel278;
    private widget.Label jLabel279;
    private widget.Label jLabel28;
    private widget.Label jLabel280;
    private widget.Label jLabel281;
    private widget.Label jLabel282;
    private widget.Label jLabel283;
    private widget.Label jLabel284;
    private widget.Label jLabel285;
    private widget.Label jLabel286;
    private widget.Label jLabel287;
    private widget.Label jLabel288;
    private widget.Label jLabel289;
    private widget.Label jLabel29;
    private widget.Label jLabel290;
    private widget.Label jLabel291;
    private widget.Label jLabel292;
    private widget.Label jLabel293;
    private widget.Label jLabel294;
    private widget.Label jLabel295;
    private widget.Label jLabel296;
    private widget.Label jLabel297;
    private widget.Label jLabel298;
    private widget.Label jLabel299;
    private widget.Label jLabel30;
    private widget.Label jLabel300;
    private widget.Label jLabel301;
    private widget.Label jLabel302;
    private widget.Label jLabel303;
    private widget.Label jLabel304;
    private widget.Label jLabel305;
    private widget.Label jLabel306;
    private widget.Label jLabel307;
    private widget.Label jLabel308;
    private widget.Label jLabel309;
    private widget.Label jLabel31;
    private widget.Label jLabel310;
    private widget.Label jLabel311;
    private widget.Label jLabel312;
    private widget.Label jLabel313;
    private widget.Label jLabel314;
    private widget.Label jLabel315;
    private widget.Label jLabel316;
    private widget.Label jLabel317;
    private widget.Label jLabel318;
    private widget.Label jLabel319;
    private widget.Label jLabel32;
    private widget.Label jLabel320;
    private widget.Label jLabel321;
    private widget.Label jLabel322;
    private widget.Label jLabel323;
    private widget.Label jLabel324;
    private widget.Label jLabel325;
    private widget.Label jLabel326;
    private widget.Label jLabel327;
    private widget.Label jLabel328;
    private widget.Label jLabel329;
    private widget.Label jLabel33;
    private widget.Label jLabel330;
    private widget.Label jLabel331;
    private widget.Label jLabel332;
    private widget.Label jLabel333;
    private widget.Label jLabel334;
    private widget.Label jLabel335;
    private widget.Label jLabel336;
    private widget.Label jLabel337;
    private widget.Label jLabel338;
    private widget.Label jLabel339;
    private widget.Label jLabel34;
    private widget.Label jLabel340;
    private widget.Label jLabel341;
    private widget.Label jLabel342;
    private widget.Label jLabel343;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel49;
    private widget.Label jLabel50;
    private widget.Label jLabel51;
    private widget.Label jLabel52;
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
    private widget.Label jLabel90;
    private widget.Label jLabel91;
    private widget.Label jLabel92;
    private widget.Label jLabel93;
    private widget.Label jLabel94;
    private widget.Label jLabel95;
    private widget.Label jLabel96;
    private widget.Label jLabel97;
    private widget.Label jLabel98;
    private widget.Label jLabel99;
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
    
