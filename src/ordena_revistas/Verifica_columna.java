
package ordena_revistas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Verifica_columna {
    
    public BufferedReader getBuffered(String link){
        BufferedReader br = null;
        try {
            File Arch=new File(link);
            if(!Arch.exists()){
               System.out.println("No existe el archivo");
            }else{
               br = new BufferedReader(new InputStreamReader(new FileInputStream(link),"UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }
    
    public void readTxt(){
        try {            
            //ruta de tu archivo
            String ruta = "sources/UL 200.xls";
            BufferedReader br = getBuffered(ruta);
            //leemos la primera linea
            String linea =  br.readLine();
            //creamos la matriz vacia
            int contador = 0;
            while(linea != null){
                String[] values = linea.split(";");                
                System.out.println("Insertados: "+linea);
                contador++;
                linea = br.readLine();
            }            
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } 
    }
    
    
    
}
