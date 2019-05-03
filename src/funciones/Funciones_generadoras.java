package funciones;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        llena_columna_separadores_estadisticas_generales();
        imprime_matrix(separadores_guardados_globales);
    }

    public void veri_separador(String[][] columnas_guardadas, String rutaArchivo) {
        String[][] separadores_guardados_locales = new String[2][68];
        //en el las funciones generadoras se debe generar los caracteres generales, pero primero organizar el codigo de tal forma que tenga un metodo que crea la matriz del excel ccoco
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
    
    public void genera_archivo(){
        String[][] estadisticas_generales_columnas = con.get_estadisticas_generales_columnas2();
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
            for (int j = 0; j < 68; j++) {
                if (columnas_guardadas[i][j] != null) {
                    valor = valor + columnas_guardadas[i][j] + "|";
                }
            }
            System.out.println(valor);
            valor = "";
        }
    }
}
