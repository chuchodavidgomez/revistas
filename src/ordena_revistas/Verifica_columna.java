//archivos que no existen sources/UL 29400.xls sources/UL 8600.xls sources/UL 130200.pdf
package ordena_revistas;

//import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
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

    //String valores[] = new String[70];
    int cont = 0;
    Conexion con = new Conexion();
    int cont_repetido  = 0;
    int num_igual = 0;    


    

    //guardar los repetidos y luegos procesar de nuevo todos y verificar los repetidos.
    public void veri_column_repetida(String[][] columnas_guardadas, String rutaArchivo) {
        String[] guardados = new String[68];
        String[] repetidos = new String[68];
        int cont_repe = 0;
        int cont_repe2 = 0;
        int contador = 0;
        
        for (int j = 0; j < 68; j++) {
            if (columnas_guardadas[0][j] != null) {
                if (!verifica_repe(columnas_guardadas[0][j], guardados)) {
                    guardados[cont_repe] = columnas_guardadas[0][j];
                    cont_repe++;                        
                } else {
                    repetidos[cont_repe2] = columnas_guardadas[0][j];
                    cont_repe2++;
                }
            }
        }
            
        if (cont_repe2 > 0) {
            int conta = 0;
            int[] posiciones = new int[2];            
            for (int i = 0; i < cont_repe2; i++) {
                for (int j = 0; j < 68; j++) {
                    if (columnas_guardadas[0][j] != null) {
                        if (columnas_guardadas[0][j].equals(repetidos[i])) {
                            posiciones[conta] = j;
                            conta++;                            
                        }
                    }
                }
                if(conta>cont_repetido){
                    cont_repetido = conta;
                }
            }
            //System.out.println("iguales " + columnas_guardadas[columnas_guardadas.length-1][0]);
            
            for (int k = 1; k < columnas_guardadas.length; k++) {
                if (columnas_guardadas[k][posiciones[0]] != null && columnas_guardadas[k][posiciones[1]] != null) {
                    if(columnas_guardadas[k][posiciones[0]].equals(columnas_guardadas[k][posiciones[1]])){
                        contador++;
                    }        
                }else{
                    if (columnas_guardadas[k][posiciones[0]] == null && columnas_guardadas[k][posiciones[1]] == null) {
                        contador++;
                    }
                }    
            } 
            
            int num_igual2 = 0;
            if(contador != columnas_guardadas.length-1){
                
                num_igual2++;
                System.out.println("iguales " + contador+" de "+(columnas_guardadas.length-1)); 
            }
            if(num_igual2>num_igual){
                num_igual = num_igual2;
            }
            
        }

    }
    
    public void veri_celda(String[][] columnas_guardadas, String rutaArchivo){
        imprime_matrix(columnas_guardadas);
        String[][] guardados = new String[2][68];
        String cadena = "INSERT INTO estadisticas_revistas ";
        String columna = "(name_file, ";
        String values = "('" + rutaArchivo + "', ";
        int contador = 0;
        int cont_repe = 0;
        System.out.println(columnas_guardadas[1][1]);
        try {
            for (int j = 0; j < 68; j++) {
                if (columnas_guardadas[0][j] != null) {
                    if (!verifica_repe(columnas_guardadas[0][j], guardados[0])) {
                        guardados[0][cont_repe] = columnas_guardadas[0][j];
                        cont_repe++;
                        for (int k = 1; k < columnas_guardadas.length; k++) {
                            if (columnas_guardadas[k][j] != null) {
                                for (int i = 0; i < columnas_guardadas[k][j].length(); i++) {
                                    if(guardados[1][cont_repe] == null){
                                        System.out.println(columnas_guardadas[k][j]);
                                        guardados[1][cont_repe] = columnas_guardadas[k][j].charAt(0)+"";
                                    }else{
                                        for (int p = 0; p < guardados[1][cont_repe].length(); p++) {
                                            if(columnas_guardadas[k][j].charAt(i) != guardados[1][cont_repe].charAt(p)){
                                                guardados[1][cont_repe] = guardados[1][cont_repe] + ", " + columnas_guardadas[k][j].charAt(i);
                                            }
                                        }
                                    }                                        
                                }
                            }
                        }
                        columna = columna + columnas_guardadas[0][j] + ", ";
                        values = values + "'" + (float) contador / (columnas_guardadas.length - 1) + "', ";                        
                        contador = 0;
                    } else {
                        //System.out.println("repetida ------------------------------>"+columnas_guardadas[0][j]);
                    }
                }
            }
            columna = columna.substring(0, columna.length() - 2) + ")";
            values = values.substring(0, values.length() - 2) + ")";
            cadena = cadena + columna + " VALUES " + values;
            System.out.println(guardados[1][0]);
            //System.out.println(cadena);
            //con.ejecuta_sql(cadena);
        } catch (Exception e) {
            //e.getMessage();
            System.out.println("ocurrio este error " + e);
        }
    }
    
    public void imprime_matrix(String[][] columnas_guardadas){
        String valor = "";
        for (int i = 0; i < columnas_guardadas.length; i++) {
            for (int j = 0; j < 68; j++) {
                if(columnas_guardadas[i][j] != null){
                    valor = valor + columnas_guardadas[i][j] + "|";
                }
            }
            System.out.println(valor);
            valor = "";
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
