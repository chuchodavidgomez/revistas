
package funciones;

import java.io.FileInputStream;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import ordena_revistas.*;

public class Funciones_generadoras {
    String[] valores_columnas = new String[70];
    Conexion con = new Conexion();
    int contador_columnas = 0;
    
    public void genera_tablas() {
        int cont2 = 1;
        for (int i = 100; i <= 181100; i = i + 100) {
            String rutaArchivo = "sources/UL " + i + ".xls";
            System.out.println("archivo: " + rutaArchivo);
            
            try {
                HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(rutaArchivo));
                HSSFSheet sheet = wb.getSheetAt(0);//lee la hoja           
                Iterator<Row> rowIterator = sheet.iterator();
                Row row;

                int j = 0;
                while (j != 1 && rowIterator.hasNext()) {
                    row = rowIterator.next();//lee la fila
                    cont2++;                    
                    Iterator<Cell> cellIterator = row.cellIterator();//se obtiene las celdas por fila
                    Cell cell;
                    while (cellIterator.hasNext()) {                        
                        cell = cellIterator.next();// se obtiene la celda                       
                        actualizar_vec(cell.getStringCellValue().toLowerCase());
                    }
                    j++;
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
        con.creartb_estadisticas_generales_columnas();
        con.inserta_valores_columnas(valores_columnas);
        for (int i = 0; i < valores_columnas.length; i++) {
            if (valores_columnas[i] != null) {
                valores_columnas[i] = valores_columnas[i].replaceAll(" ", "_").replaceAll("\\(", "ppp").replaceAll("\\)", "pip").replaceAll("\\&", "y").replaceAll("\\/", "slash");                
            }
        }
        System.out.println("contador: " + cont2);
        System.out.println("Contador de columnas: " + contador_columnas);
        con.creartb_distribucion_columnas(valores_columnas);
        con.creartb_estadisticas_distribucion_columnas(valores_columnas);
        con.creartb_estadisticas_celdas_diferentes(valores_columnas);
    }
    
    public void actualizar_vec(String celda) {
        boolean esta = false;
        for (int i = 0; i < valores_columnas.length; i++) {
            if (celda.equals(valores_columnas[i])) {
                esta = true;
            }

        }
        if (!esta) {
            valores_columnas[contador_columnas] = celda;            
            contador_columnas++;
        }
    }
}
