/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class konversi3 {
        static String[] huruf={"","Satu","Dua","Tiga","Empat","Lima","Enam","Tujuh","Delapan","Sembilan","Sepuluh","Sebelas"};
            public static void main(String[] args) {
                Scanner sc = new Scanner(System.in);
                System.out.print("Masukan Bilangan yang akan disebut: ");
                System.out.println(new konversi3().angkaToTerbilang(sc.nextLong()));
            }   
        public static String angkaToTerbilang(Long angka){
            if(angka < 12)
                return huruf[angka.intValue()];
            if(angka >=12 && angka <= 19)
               return huruf[angka.intValue() % 10] + " Belas";
            if(angka >= 20 && angka <= 99)
               return angkaToTerbilang(angka / 10) + " Puluh " + huruf[angka.intValue() % 10];
            if(angka >= 100 && angka <= 199)
               return "Seratus " + angkaToTerbilang(angka % 100);
            if(angka >= 200 && angka <= 999)
               return angkaToTerbilang(angka / 100) + " Ratus " + angkaToTerbilang(angka % 100);
            if(angka >= 1000 && angka <= 1999)
               return "Seribu " + angkaToTerbilang(angka % 1000);
            if(angka >= 2000 && angka <= 999999)
               return angkaToTerbilang(angka / 1000) + " Ribu " + angkaToTerbilang(angka % 1000);
            if(angka >= 1000000 && angka <= 999999999)
               return angkaToTerbilang(angka / 1000000) + " Juta " + angkaToTerbilang(angka % 1000000);
            if(angka >= 1000000000 && angka <= 999999999999L)
               return angkaToTerbilang(angka / 1000000000) + " Milyar " + angkaToTerbilang(angka % 1000000000);
            if(angka >= 1000000000000L && angka <= 999999999999999L)
               return angkaToTerbilang(angka / 1000000000000L) + " Triliun " + angkaToTerbilang(angka % 1000000000000L);
            if(angka >= 1000000000000000L && angka <= 999999999999999999L)
              return angkaToTerbilang(angka / 1000000000000000L) + " Quadrilyun " + angkaToTerbilang(angka % 1000000000000000L);
            return "";
        }


    
}
