/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

/**
 *
 * @author user
 */
public class Konversi {
    public String angka(int satuan){ 
      String[] huruf ={"","Satu","Dua","Tiga","Empat","Lima","Enam","Tujuh","Delapan","Sembilan","Sepuluh","Sebelas"}; 
      String hasil=""; 
      if(satuan<12) 
       hasil=hasil+huruf[satuan]; 
      else if(satuan<20) 
       hasil=hasil+angka(satuan-10)+" Belas"; 
      else if(satuan<100) 
       hasil=hasil+angka(satuan/10)+" Puluh "+angka(satuan%10); 
      else if(satuan<200) 
       hasil=hasil+"Seratus "+angka(satuan-100); 
      else if(satuan<1000) 
       hasil=hasil+angka(satuan/100)+" Ratus "+angka(satuan%100); 
      else if(satuan<2000) 
       hasil=hasil+"Seribu "+angka(satuan-1000); 
      else if(satuan<1000000) 
       hasil=hasil+angka(satuan/1000)+" Ribu "+angka(satuan%1000); 
      else if(satuan<1000000000) 
       hasil=hasil+angka(satuan/1000000)+" Juta "+angka(satuan%1000000);
      else if(satuan<1000000000) 
       hasil=hasil+angka(satuan/1000000000)+" Milyar "+angka(satuan%1000000000); 
      else if(satuan>=1000000000) 
       hasil="Angka terlalu besar, harus kurang dari 1 milyar!"; 
      return hasil; 
    } 
    //public Konversi(){
        //super();
    //}
    
}
