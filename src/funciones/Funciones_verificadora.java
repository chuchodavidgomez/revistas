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
import javax.swing.JOptionPane;
import ordena_revistas.Conexion;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Funciones_verificadora {

    Conexion con = new Conexion();
    int cont_columnas_repe = 0;
    int num_diferentes_global = 0;
    String[][] caract_guardados_globales = new String[2][68];

    public void verifica_diferentes() {
        String[][] promedios = con.get_estadisticas_distribucion_columnas();
        String[][] estadisticas1 = con.get_distribucion_columnas();
        boolean entro = false;
        for (int i = 70100; i <= 181100; i = i + 100) {
            String rutaArchivo = "sources/UL " + i + ".xls";
            for (int j = 0; j < estadisticas1.length; j++) {
                if (rutaArchivo.equals(estadisticas1[j][0])) {
                    entro = true;
                }
            }
            if (!entro) {
                System.out.println("1. No existe este archivo: " + rutaArchivo);
            }
            entro = false;
            for (int j = 0; j < promedios.length; j++) {
                if (rutaArchivo.equals(promedios[j][0])) {
                    entro = true;
                }
            }
            if (!entro) {
                System.out.println("2. No existe este archivo: " + rutaArchivo);
            }
            entro = false;
        }
    }

    public void verifica_columna_repetida() throws FileNotFoundException, IOException {
        //String[][] columnas = con.get_estadisticas_generales_columnas();
        String[] columnas = con.get_columnas();
        caract_guardados_globales[0] = columnas;
        imprime_matrix(caract_guardados_globales);
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
                //imprime_matrix(columnas_guardadas);
                //veri_column_repetida(columnas_guardadas, rutaArchivo);// no sirve con esta matrix
                String opt = JOptionPane.showInputDialog("desea revisar celdad:\n");
                veri_celda(columnas_guardadas, rutaArchivo);// nor sirve con esta matrix

            } catch (FileNotFoundException e) {
                System.out.println("ocurrio este error2 " + e);
            }
        }
        //imprime_matrix(caract_guardados_globales);
        guarda_valores_celdes(caract_guardados_globales);
        System.out.println("archivos con valores diferentes en las columnas repetidas --------------------->" + num_diferentes_global);
        System.out.println("El numero maximo de columna repetida es ----------------->" + cont_columnas_repe / 2);
    }

    public void veri_column_repetida(String[][] columnas_guardadas, String rutaArchivo) {
        String[] guardados = new String[68];
        String[] repetidos = new String[68];
        int cont_columnas = 0;
        int cont_columnas_repe = 0;
        int contador = 0;

        //verifica cuantas columnas estan repetidas
        for (int j = 0; j < 68; j++) {
            if (columnas_guardadas[0][j] != null) {
                if (!verifica_repe(columnas_guardadas[0][j], guardados)) {
                    guardados[cont_columnas] = columnas_guardadas[0][j];
                    cont_columnas++;
                } else {
                    repetidos[cont_columnas_repe] = columnas_guardadas[0][j];
                    cont_columnas_repe++;
                }
            }
        }

        //guarda las posiciones de los repetidos
        if (cont_columnas_repe > 0) {
            int conta = 0;
            int[] posiciones = new int[2];
            for (int i = 0; i < cont_columnas_repe; i++) {
                for (int j = 0; j < 68; j++) {
                    if (columnas_guardadas[0][j] != null) {
                        if (columnas_guardadas[0][j].equals(repetidos[i])) {
                            posiciones[conta] = j;
                            conta++;
                        }
                    }
                }
                if (conta > this.cont_columnas_repe) {
                    this.cont_columnas_repe = conta;
                }
            }
            //System.out.println("ultimo guardado " + columnas_guardadas[columnas_guardadas.length-1][0]);
            //comparo las columnas iguales
            for (int k = 1; k < columnas_guardadas.length; k++) {
                if (columnas_guardadas[k][posiciones[0]] != null && columnas_guardadas[k][posiciones[1]] != null) {
                    if (columnas_guardadas[k][posiciones[0]].equals(columnas_guardadas[k][posiciones[1]])) {
                        contador++;
                    } else {
                        System.out.println("archivo: " + rutaArchivo);
                        System.out.println("id: " + columnas_guardadas[k][posiciones[0]]);
                        System.out.println("<------------son diferentes------------------>");
                    }
                } else {
                    if (columnas_guardadas[k][posiciones[0]] == null && columnas_guardadas[k][posiciones[1]] == null) {
                        contador++;
                    } else {
                        System.out.println("archivo: " + rutaArchivo);
                        System.out.println("id: " + columnas_guardadas[k][0]);
                        System.out.println("<------------alguno no es nulo------------------>");
                    }
                }
            }

            if (contador != columnas_guardadas.length - 1) {

                num_diferentes_global++;

            }
            //System.out.println("iguales " + contador+" de "+(columnas_guardadas.length-1)); 
        }

    }

    public void guarda_valores_celdes(String[][] caract_guardados) {
        BufferedWriter out = null;
        try {
            String ruta = "caracteres.txt";
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

            for (int j = 0; j < 68; j++) {
                if (caract_guardados[0][j] != null) {
                    if (caract_guardados[1][j] != null) {
                        out.write(caract_guardados[0][j] + " ----> " + caract_guardados[1][j]);
                    } else {
                        out.write(caract_guardados[0][j] + " ----> sin caract");
                    }
                    out.flush();
                    out.newLine();
                }
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void veri_celda(String[][] columnas_guardadas, String rutaArchivo) {
        String[][] caract_guardados_locales = new String[2][68];
        //en el las funciones generadoras se debe generar los caracteres generales, pero primero organizar el codigo de tal forma que tenga un metodo que crea la matriz del excel ccoco

        int cont_repe = 0;

        for (int j = 0; j < 68; j++) {
            if (columnas_guardadas[0][j] != null) {
                if (!verifica_repe(columnas_guardadas[0][j], caract_guardados_locales[0])) {
                    caract_guardados_locales[0][cont_repe] = columnas_guardadas[0][j];
                    for (int k = 1; k < columnas_guardadas.length; k++) {
                        //System.out.println(".");
                        if (columnas_guardadas[k][j] != null) {
                            String[] element_guardados = null;
                            for (int i = 0; i < columnas_guardadas[k][j].length(); i++) {
                                //System.out.println("..");
                                if (caract_guardados_locales[1][cont_repe] == null) {
                                    //System.out.println(columnas_guardadas[k][j]);
                                    caract_guardados_locales[1][cont_repe] = columnas_guardadas[k][j].charAt(0) + "";

                                } else {
                                    element_guardados = caract_guardados_locales[1][cont_repe].split("");
                                    if (!verifica_repe(columnas_guardadas[k][j].charAt(i) + "", element_guardados)) {
                                        //System.out.println(caract_guardados_locales[1][cont_repe]);
                                        caract_guardados_locales[1][cont_repe] = caract_guardados_locales[1][cont_repe] + columnas_guardadas[k][j].charAt(i);
                                    }
                                }
                            }
                        }
                    }
                    cont_repe++;
                    //columna = columna + columnas_guardadas[0][j] + ", ";
                    //values = values + "'" + (float) contador / (columnas_guardadas.length - 1) + "', ";                        
                } else {
                    //System.out.println("repetida ------------------------------>"+columnas_guardadas[0][j]);
                }
            }
        }
        //imprime_matrix(caract_guardados_locales);
        actualiza_caract_globales(caract_guardados_locales);

    }

    public void actualiza_caract_globales(String[][] caract_guardados_locales) {
        String[] element_guardados = null;
        for (int i = 0; i < 68; i++) {//se lee la matrix de locales
            if (caract_guardados_locales[0][i] != null) {//hay una columna en los caract locales
                for (int j = 0; j < 68; j++) {//se lee la matrix de globales
                    if (caract_guardados_locales[0][i].equals(caract_guardados_globales[0][j])) {//columnas iguales
                        if (caract_guardados_locales[1][i] != null) {//se verifica que que los locales tengan datos
                            if (caract_guardados_globales[1][j] != null) {//se verifica que el global tenga datos
                                element_guardados = caract_guardados_globales[1][j].split("");
                                for (int k = 0; k < caract_guardados_locales[1][i].length(); k++) {
                                    if (!verifica_repe(caract_guardados_locales[1][i].charAt(k) + "", caract_guardados_globales[1][j].split(""))) {
                                        caract_guardados_globales[1][j] = caract_guardados_globales[1][j] + caract_guardados_locales[1][i].charAt(k);
                                    }
                                }
                            } else {
                                
                                caract_guardados_globales[1][j] = caract_guardados_locales[1][i];
                            }
                        }
                    }
                }
            }
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
