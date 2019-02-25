//archivos que no existen sources/UL 29400.xls sources/UL 8600.xls sources/UL 130200.pdf
package ordena_revistas;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Verifica_columna {
    String valores[] = new String[500];
    int cont = 0;
    Conexion con = new Conexion(); 
    
    public BufferedReader getBuffered(String link){
        BufferedReader br = null;
        try {
            File Arch=new File(link);
            if(!Arch.exists()){
               System.out.println("No existe el archivo -------------------> "+link);
            }else{
               br = new BufferedReader(new InputStreamReader(new FileInputStream(link),"UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }
    
    public void readTxt(){                        
        int cont2 = 1;
        for (int i = 100; i <= 181100; i=i+100) {   
            String rutaArchivo = "sources/UL "+i+".xls";
            System.out.println("archivo: "+rutaArchivo);
            //getBuffered(rutaArchivo);
            try {
                HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(rutaArchivo));
                HSSFSheet sheet = wb.getSheetAt(0);
                // leer archivo excel

                //obtener todas las filas de la hoja excel            
                Iterator<Row> rowIterator = sheet.iterator();

                Row row;
                // se recorre cada fila hasta el final
                int j = 0;
                while (j!=1&&rowIterator.hasNext()) {
                    row = rowIterator.next();
                    cont2++;
                    //se obtiene las celdas por fila
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell cell;
                    //se recorre cada celda
                    while (cellIterator.hasNext()) {
                            // se obtiene la celda en específico y se la imprime
                            cell = cellIterator.next();
                            //System.out.print(cell.getStringCellValue()+" | ");
                            actualizar_vec(cell.getStringCellValue());

                    }                    
                    j++;
                }
            } catch (Exception e) {
                    e.getMessage();
            }
            
        }
        imprime();
        System.out.println("contador: "+ cont2);
        System.out.println("contador de columnas: "+ cont);
    }
    
    public void actualizar_vec(String celda){
        boolean esta = false;
        for (int i = 0; i < valores.length; i++) {
            if(celda.equals(valores[i])){
                esta = true;
            }                
            
        }
        if(!esta){
            valores[cont] = celda;
            cont++;
        }
    }
    
    public void imprime(){
        for (int i = 0; i < valores.length; i++) {
            if(valores[i] != null){
                String valore = valores[i];
                System.out.println(valore);
            }                
        }
    }
}
