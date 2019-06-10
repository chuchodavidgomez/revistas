package funciones;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import ordena_revistas.*;

public class Funciones_generadoras {

    String[] valores_columnas = new String[70];
    Conexion con = new Conexion();
    Funciones_varias fv = new Funciones_varias();
    String[][] separadores_guardados_globales = new String[2][68];
    int contador_columnas = 0;
    String archivo[][] = null;
    
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

    //terminar el metodoso de separador
    public void genera_matriz() throws IOException {
        String[][] columnas_guardadas = null;
        String[] columnas = con.get_columnas();
        separadores_guardados_globales[0] = columnas;

        for (int i = 100; i <= 181100; i = i + 100) {
            String rutaArchivo = "sources/UL " + i + ".xls";
            columnas_guardadas = fv.genera_matrix(rutaArchivo);
            if (columnas_guardadas != null) {
                veri_separador(columnas_guardadas, rutaArchivo);
            }
        }
        //llena_columna_separadores_estadisticas_generales();
        imprime_matrix(separadores_guardados_globales);
    }
    
    public int almacena_archivo(String[][] columnas_guardadas, int puntero) {        
        String[] columnas_guar = new String[68];        
        String[] element_separados = null;
        int cont_repe = 0;
        
        for (int j = 0; j < 1069; j++) {//recorre todas las columnas del archivo            
            for (int i = 0; i < 68; i++) {          
                //System.out.println(".");
                if(columnas_guardadas[0][i] != null){
                    //System.out.println("..");
                    //primero verificar que la columna de archivo corresponda                     
                    if (archivo[0][j].equals(columnas_guardadas[0][i])) {
                        //System.out.println("...");
                        if (!verifica_repe(columnas_guardadas[0][i], columnas_guar) ) {
                            //System.out.println("....");
                            columnas_guar[cont_repe] = columnas_guardadas[0][i];
                            //verificar como evitar la anulacion
                            for (int k = puntero; k < columnas_guardadas.length+(puntero-1); k++) {
                                //System.out.println(".....");
                                if (columnas_guardadas[k-(puntero-1)][i] != null) {
                                    element_separados = columnas_guardadas[k-(puntero-1)][i].split("\\|");
                                    if (element_separados.length > 1) {
                                        //System.out.println(".......");
                                        for (int l = 0; l < element_separados.length; l++) {
                                            archivo[k][j+l] = element_separados[l];  
                                            //System.out.print("a");
                                        }   
                                        
                                    }else{
                                        //System.out.println("......");
                                        archivo[k][j] = columnas_guardadas[k-(puntero-1)][i];                                                   
                                    }
                                }
                                
                            }                                 
                            cont_repe++;
                            break;
                        }                  
                    }
                }
            }            
        }                
        return columnas_guardadas.length;
    }

    public void veri_separador(String[][] columnas_guardadas, String rutaArchivo) {
        String[][] separadores_guardados_locales = new String[2][68];        
        String[] element_separados = null;
        int cont_repe = 0;
        int max_separador = 0;
        for (int j = 0; j < 68; j++) {
            if (columnas_guardadas[0][j] != null) {
                if (!verifica_repe(columnas_guardadas[0][j], separadores_guardados_locales[0])) {
                    separadores_guardados_locales[0][cont_repe] = columnas_guardadas[0][j];
                    for (int k = 1; k < columnas_guardadas.length; k++) {
                        //System.out.println(".");                        
                        if (columnas_guardadas[k][j] != null) {
                            element_separados = columnas_guardadas[k][j].split("\\|");
                            if (element_separados.length > 1) {
                                if (element_separados.length > max_separador) {
                                    max_separador = element_separados.length;
                                }/*
                                if (max_separador == 362) {
                                    System.out.println("se encuentra en ----> " + rutaArchivo + " columna " + columnas_guardadas[0][j] + " num " + k);
                                }*/
                            }
                        }
                    }

                    separadores_guardados_locales[1][cont_repe] = max_separador + "";
                    max_separador = 0;
                    cont_repe++;
                } else {
                    //System.out.println("repetida ------------------------------>"+columnas_guardadas[0][j]);
                }
            }
        }
        //imprime_matrix(separadores_guardados_locales);
        actualiza_separadores_globales(separadores_guardados_locales);

    }

    public void actualiza_separadores_globales(String[][] separadores_guardados_locales) {
        for (int i = 0; i < 68; i++) {//se lee la matrix de locales
            if (separadores_guardados_locales[0][i] != null) {//hay una columna en los caract locales
                for (int j = 0; j < 68; j++) {//se lee la matrix de globales
                    if (separadores_guardados_locales[0][i].equals(separadores_guardados_globales[0][j])) {//columnas iguales
                        if (separadores_guardados_locales[1][i] != null) {//se verifica que que los locales tengan datos
                            if (separadores_guardados_globales[1][j] != null) {//se verifica que el global tenga datos                                                                                                
                                if (Integer.parseInt(separadores_guardados_locales[1][i]) > Integer.parseInt(separadores_guardados_globales[1][j])) {
                                    separadores_guardados_globales[1][j] = separadores_guardados_locales[1][i];
                                }
                            } else {
                                separadores_guardados_globales[1][j] = separadores_guardados_locales[1][i];
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void llena_columna_separadores_estadisticas_generales() {
        String sql;
        for (int i = 0; i < separadores_guardados_globales[0].length; i++) {
            sql = "UPDATE estadisticas_generales_columnas SET ";         
            sql = sql + "separadores='" + separadores_guardados_globales[1][i] + "' WHERE columna='" + separadores_guardados_globales[0][i] + "'";
            //System.out.println(sql);
            con.ejecuta_sql(sql);
        }
    }
    
    public void genera_archivo() throws IOException{
        String[][] estadisticas_generales_columnas = con.get_estadisticas_generales_columnas2();
        int columnas = 0;
        int cont = 0;
        int cont_columna = 0;
        int cont_replicas = 0;
        //cuenta la cantidad de columnas
        for (int i = 0; i < estadisticas_generales_columnas.length; i++) {
            if(Integer.parseInt(estadisticas_generales_columnas[i][5]) == 0){
                columnas = columnas + 1;
            }else{
                columnas = columnas + Integer.parseInt(estadisticas_generales_columnas[i][5]);
            }
            
        }
        archivo = new String[181101][columnas];        
        System.out.println(archivo[0].length);
        
        while(cont != archivo[0].length){
            if(Integer.parseInt(estadisticas_generales_columnas[cont_columna][5]) == 0){
                cont_replicas = 1;
            }else{
                cont_replicas = Integer.parseInt(estadisticas_generales_columnas[cont_columna][5]);
            }
            
            for (int i = 0; i < cont_replicas; i++) {
                if(i == 0){
                    archivo[0][cont] = estadisticas_generales_columnas[cont_columna][1];
                }else{
                    archivo[0][cont] = estadisticas_generales_columnas[cont_columna][1] + " " + i;
                }
                
                cont++;
            }
            cont_columna++;
        }
        
        String[][] columnas_guardadas = null;
        int puntero = 1;
        for (int i = 100; i <= 181100; i = i + 100) {
            String rutaArchivo = "sources/UL " + i + ".xls";
            columnas_guardadas = fv.genera_matrix(rutaArchivo);
            if (columnas_guardadas != null) {
                puntero = almacena_archivo(columnas_guardadas, puntero);
            }
        }
        genera_archivocsv();
        //imprime_matrix(archivo);        
    }
    
    public void genera_archivocsv(){
        BufferedWriter out = null;
        try {
            String ruta = "datos.csv";
            File fichero = new File(ruta);
            BufferedWriter bw;            
            
            if (fichero.exists()) {                
                fichero.delete();                
            }
            
            bw = new BufferedWriter(new FileWriter(fichero));
            bw.close();
            
            out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(ruta, true), // true to append
                        StandardCharsets.UTF_8 // Set encoding
                )
            );

            for (int j = 0; j < archivo.length; j++) {
                for (int i = 0; i < archivo[0].length; i++) {
                    if (archivo[j][i] != null) {                        
                        out.write(archivo[j][i].replaceAll(";", ""));
                    }   
                    if(archivo[0].length-1 != i){
                        out.write(";");
                    }
                }                
                out.flush();
                out.newLine();                
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
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

    public void imprime_matrix(String[][] columnas_guardadas) {
        String valor = "";
        for (int i = 0; i < columnas_guardadas.length; i++) {
            for (int j = 0; j < columnas_guardadas[0].length; j++) {
                if (columnas_guardadas[i][j] != null) {
                    valor = valor + columnas_guardadas[i][j] + "|";
                }
            }
            System.out.println(valor);
            valor = "";
        }
    }
}
