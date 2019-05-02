
package funciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Funciones_varias {
    
    public BufferedReader getBuffered(String link) {
        BufferedReader br = null;
        try {
            File Arch = new File(link);
            if (!Arch.exists()) {
                System.out.println("No existe el archivo -------------------> " + link);
            } else {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(link), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }    
    
    public String [][] genera_matrix(String rutaArchivo) throws FileNotFoundException, IOException{
 
        String[][] columnas_guardadas = null;       
        System.out.println("archivo: " + rutaArchivo);
        int contFila = 0;

        try {
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(rutaArchivo));
            HSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                rowIterator.next();
                contFila++;
            }

            contFila = contFila - 1;
            columnas_guardadas = new String[contFila][68];
            rowIterator = sheet.iterator();
            Row row;

            int x = 0;
            int y = 0;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell cell;
                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();
                    if (x == 0) {
                        String column = cell.getStringCellValue().toLowerCase();
                        //String column = cell.getStringCellValue().toLowerCase().replaceAll(" ", "_").replaceAll("\\(", "ppp").replaceAll("\\)", "ppp").replaceAll("\\&", "y").replaceAll("\\/", "slash");
                        columnas_guardadas[x][y] = column;
                    } else {
                        if (!cell.getStringCellValue().toLowerCase().equals("null") & x != contFila) {
                            columnas_guardadas[x][y] = cell.getStringCellValue().toLowerCase();
                        }
                    }
                    y++;
                }
                y = 0;
                x++;
            }            

        } catch (Exception e) {
            System.out.println("ocurrio este error2 " + e);
        }
            
        return columnas_guardadas;
    }
}
