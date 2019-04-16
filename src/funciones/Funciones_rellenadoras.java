
package funciones;

import java.io.FileInputStream;
import java.util.Iterator;
import ordena_revistas.Conexion;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Funciones_rellenadoras {
    
    int contador_columnas = 0;
    Conexion con = new Conexion();
    int cont_repetido  = 0;
    int num_igual = 0;  
    
    public void llenatb_distribucion_columnas() {
        String[] columnas = con.get_columnas();
        String[] columnas_guardadas;
        for (int i = 100; i <= 181100; i = i + 100) {
            String rutaArchivo = "sources/UL " + i + ".xls";
            System.out.println("archivo: " + rutaArchivo);  
            try {
                HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(rutaArchivo));
                HSSFSheet sheet = wb.getSheetAt(0);                         
                Iterator<Row> rowIterator = sheet.iterator();
                Row row;
                int j = 0;
                String cadena = "INSERT INTO distribucion_columnas";
                String columna = "(name_file, ";
                String values = "('" + rutaArchivo + "', ";
                while (j != 1 && rowIterator.hasNext()) {
                    columnas_guardadas = new String[70];
                    row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell cell;                    
                    contador_columnas = 0;
                    while (cellIterator.hasNext()) {                                              
                        cell = cellIterator.next();                        
                        if (verifica_repe(cell.getStringCellValue().toLowerCase(),columnas)) {
                            if (contador_columnas == 0) {
                                columnas_guardadas[contador_columnas] = cell.getStringCellValue().toLowerCase();
                                contador_columnas++;
                                columna = columna + cell.getStringCellValue().toLowerCase().replaceAll(" ", "_").replaceAll("\\(", "ppp").replaceAll("\\)", "pip").replaceAll("\\&", "y").replaceAll("\\/", "slash") + ", ";
                                values = values + "'1', ";
                            } else {
                                if (!verifica_repe(cell.getStringCellValue().toLowerCase(), columnas_guardadas)) {
                                    columnas_guardadas[contador_columnas] = cell.getStringCellValue().toLowerCase();
                                    contador_columnas++;
                                    columna = columna + cell.getStringCellValue().toLowerCase().replaceAll(" ", "_").replaceAll("\\(", "ppp").replaceAll("\\)", "pip").replaceAll("\\&", "y").replaceAll("\\/", "slash") + ", ";
                                    values = values + "'1', ";
                                }
                            }
                        }
                    }
                    j++;
                }
                columna = columna.substring(0, columna.length() - 2) + ")";
                values = values.substring(0, values.length() - 2) + ")";
                cadena = cadena + columna + " VALUES " + values;     
                //System.out.println(cadena);
                con.ejecuta_sql(cadena);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
    
    public void llena_columna_total_estadisticas_generales() {
        String[][] distribucion_columnas = con.get_distribucion_columnas();
        String[][] estadisticas_generales_columnas = con.get_estadisticas_generales_columnas2();
        int suma;
        String sql;
        for (int i = 0; i < estadisticas_generales_columnas.length; i++) {
            sql = "UPDATE estadisticas_generales_columnas SET ";
            suma = 0;
            for (int j = 0; j < distribucion_columnas.length; j++) {
                if (distribucion_columnas[j][i] != null) {
                    suma += Integer.parseInt(distribucion_columnas[j][i]);
                }
            }
            System.out.println("columna es " + estadisticas_generales_columnas[i][1]);
            sql = sql + "total='" + suma + "' WHERE id='" + estadisticas_generales_columnas[i][0] + "'";
            //System.out.println(sql);
            con.ejecuta_sql(sql);
        }
    }
    
    public void llenatb_estadisticas_distribucion_columnas() {
        //String[][] columnas = con.get_estadisticas_generales_columnas2();
        String[][] columnas_guardadas = null;
        for (int i = 100; i <= 181100; i = i + 100) {            
            String rutaArchivo = "sources/UL " + i + ".xls";
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
                        //System.out.println("-> "+cell.getStringCellValue().toLowerCase());
                        if (x == 0) {
                            String column = cell.getStringCellValue().toLowerCase().replaceAll(" ", "_").replaceAll("\\(", "ppp").replaceAll("\\)", "pip").replaceAll("\\&", "y").replaceAll("\\/", "slash");
                            columnas_guardadas[x][y] = column;
                        } else {
                            if (!cell.getStringCellValue().toLowerCase().equals("null") & x != contFila) {
                                columnas_guardadas[x][y] = "1";
                            }
                        }
                        y++;
                    }
                    y = 0;
                    x++;
                }
                actualiza_tabla(columnas_guardadas,rutaArchivo); 
                //veri_column_repetida(columnas_guardadas, rutaArchivo);// no sirve con esta matrix
                //veri_celda(columnas_guardadas,rutaArchivo);// nor sirve con esta matrix
            } catch (Exception e) {
                //e.getMessage();
                System.out.println("ocurrio este error2 " + e);
            }            
        }
        //System.out.println("diferente --------------------->" + num_igual); 
        //System.out.println("El numero maximo de columna repetida es ----------------->"+ cont_repetido);
    }

    public void actualiza_tabla(String[][] columnas_guardadas, String rutaArchivo) {
        String[] guardados = new String[68];
        String cadena = "INSERT INTO estadisticas_distribucion_columnas";
        String columna = "(name_file, ";
        String values = "('" + rutaArchivo + "', ";
        int contador = 0;
        int cont_repe = 0;
        try {
            for (int j = 0; j < 68; j++) {
                if (columnas_guardadas[0][j] != null) {
                    if (!verifica_repe(columnas_guardadas[0][j], guardados)) {
                        guardados[cont_repe] = columnas_guardadas[0][j];
                        cont_repe++;
                        for (int k = 1; k < columnas_guardadas.length; k++) {
                            if (columnas_guardadas[k][j] != null) {
                                contador++;
                            }
                        }
                        columna = columna + columnas_guardadas[0][j] + ", ";
                        values = values + "'" + (float) contador / (columnas_guardadas.length - 1) + "', ";
                        //System.out.println(columnas_guardadas[0][j]+": "+contador+"/"+(columnas_guardadas.length-1) + " %: "+  (float)contador/(columnas_guardadas.length-1));
                        contador = 0;
                    } else {
                        //System.out.println("repetida ------------------------------>"+columnas_guardadas[0][j]);
                    }
                }
            }
            columna = columna.substring(0, columna.length() - 2) + ")";
            values = values.substring(0, values.length() - 2) + ")";
            cadena = cadena + columna + " VALUES " + values;
            //System.out.println(cadena);
             con.ejecuta_sql(cadena);
        } catch (Exception e) {
            //e.getMessage();
            System.out.println("ocurrio este error " + e);
        }
    }
    
    public void llena_columna_promedio_estadisticas_generales() {
        String[][] estadisticas = con.get_estadisticas_distribucion_columnas();
        String[][] columnas = con.get_estadisticas_generales_columnas2();
        float suma;
        String sql;
        for (int i = 0; i < columnas.length; i++) {
            sql = "UPDATE estadisticas_generales_columnas SET ";
            suma = 0;
            for (int j = 0; j < estadisticas.length; j++) {
                if (estadisticas[j][i] != null) {
                    suma += Float.parseFloat(estadisticas[j][i]);
                }
            }
            System.out.println("columna es " + columnas[i][1]);
            sql = sql + "promedio='" + suma / estadisticas.length + "' WHERE id='" + columnas[i][0] + "'";
            //System.out.println(sql);
            con.ejecuta_sql(sql);
        }
    }
    
    public boolean verifica_repe(String celda, String[] columnas_guardadas) {
        for (int i = 0; i < columnas_guardadas.length; i++) {
            if (celda.equals(columnas_guardadas[i])) {
                return true;
            }
        }
        return false;
    }
       
}
