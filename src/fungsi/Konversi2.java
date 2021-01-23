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
public class Konversi2 {
 
    String[] nomina={"","Satu","Dua","Tiga","Empat","Lima","Enam",
                         "Tujuh","Delapan","Sembilan","Sepuluh","Sebelas"};
 
    public String bilangx(double angka){
        if(angka<12)
        {
          return nomina[(int)angka];
        }
        
        if(angka>=12 && angka <=19)
        {
            return nomina[(int)angka%10] +" Belas ";
        }
        
        if(angka>=20 && angka <=99)
        {
            return nomina[(int)angka/10] +" Puluh "+nomina[(int)angka%10];
        }
        
        if(angka>=100 && angka <=199)
        {
            return "Seratus "+ bilangx(angka%100);
        }
        
        if(angka>=200 && angka <=999)
        {
            return nomina[(int)angka/100]+" Ratus "+bilangx(angka%100);
        }
        
        if(angka>=1000 && angka <=1999)
        {
            return "Seribu "+ bilangx(angka%1000);
        }
        
        if(angka >= 2000 && angka <=999999)
        {
            return bilangx((int)angka/1000)+" Ribu "+ bilangx(angka%1000);
        }
        
        if(angka >= 1000000 && angka <=999999999)
        {
            return bilangx((int)angka/1000000)+" Juta "+ bilangx(angka%1000000);
        }
        
        return "";
    }
    
    public static void main(String[] args) {
       
       System.out.println( new Konversi2().bilangx(1000009));
       
    }

    
}
