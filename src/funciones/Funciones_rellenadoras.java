package funciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import javax.swing.JOptionPane;
import ordena_revistas.Conexion;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Funciones_rellenadoras {

    int contador_columnas = 0;
    Conexion con = new Conexion();
    int cont_repetido = 0;
    int num_igual = 0;
    String[][] caracteres;

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
                        if (verifica_repe(cell.getStringCellValue().toLowerCase(), columnas)) {
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
                actualiza_tabla(columnas_guardadas, rutaArchivo);

            } catch (Exception e) {
                //e.getMessage();
                System.out.println("ocurrio este error2 " + e);
            }
        }
    }

    public BufferedReader getBuffered(String link) {
        BufferedReader br = null;
        try {
            File Arch = new File(link);
            if (!Arch.exists()) {
                System.out.println("No existe el archivo");
            } else {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(link), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }

    public void genera_matrix_caract() throws IOException {
        String ruta = "caracteres.txt";
        BufferedReader br = getBuffered(ruta);
        String[] linea = null;
        caracteres = new String[68][2];

        for (int i = 0; i < 68; i++) {
            linea = br.readLine().split(" ----> ");
            caracteres[i] = linea;
            caracteres[i][0] = caracteres[i][0].toLowerCase().toLowerCase().replaceAll(" ", "_").replaceAll("\\(", "ppp").replaceAll("\\)", "pip").replaceAll("\\&", "y").replaceAll("\\/", "slash");
        }
        caracteres[0][0] = "title_id";
    }

    public void llenatb_estadisticas_celdas_diferentes() throws IOException {
        String[][] columnas_guardadas = null;
        genera_matrix_caract();
        //imprime_matrix(caracteres);
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
                            //String column = cell.getStringCellValue().toLowerCase();
                            String column = cell.getStringCellValue().toLowerCase().toLowerCase().replaceAll(" ", "_").replaceAll("\\(", "ppp").replaceAll("\\)", "pip").replaceAll("\\&", "y").replaceAll("\\/", "slash");
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
                actualizatb_estadisticas_celdas_diferentes(columnas_guardadas, rutaArchivo);
            } catch (FileNotFoundException e) {
                System.out.println("ocurrio este error2 " + e);
            }
        }

    }

    public void actualizatb_estadisticas_celdas_diferentes(String[][] columnas_guardadas, String rutaArchivo) {
        //imprime_matrix(columnas_guardadas);
        String[] guardados = new String[68];
        String cadena = "INSERT INTO estadisticas_celdas_diferentes";
        String columna = "(name_file, ";
        String values = "('" + rutaArchivo + "', ";   
        int contador = 0;
        float promedio = 0;
        int cont_repe = 0;

        try {
            for (int j = 0; j < 68; j++) {
                if (columnas_guardadas[0][j] != null) {
                    if (!verifica_repe(columnas_guardadas[0][j], guardados)) {
                        guardados[cont_repe] = columnas_guardadas[0][j];
                        cont_repe++;
                            
                        for (int i = 0; i < caracteres.length; i++) {                                                                                    
                            if (caracteres[i][0].equals(columnas_guardadas[0][j])) {
                                for (int k = 1; k < columnas_guardadas.length; k++) {
                                    if (columnas_guardadas[k][j] != null) {
                                        for (int p = 0; p < caracteres[i][1].length(); p++) {
                                            for (int y = 0; y < columnas_guardadas[k][j].length(); y++) {
                                                if (caracteres[i][1].charAt(p) == columnas_guardadas[k][j].charAt(y)) {
                                                    contador++;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }                                
                                promedio = (float)contador/(columnas_guardadas.length-1);
                                columna = columna + columnas_guardadas[0][j] + ", ";
                                values = values + "'" + (float) promedio/(caracteres[i][1].length()) + "', ";
                                //System.out.println(columnas_guardadas[0][j]+": "+promedio+"/"+(caracteres[i][1].length()) + " %: "+  promedio/(caracteres[i][1].length()));
                                contador = 0;
                                break;
                            }
                        }
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

    //terminar
    public void llena_columna_valores_estadisticas_generales() {
        String[][] estadisticas = con.get_distribucion_caracteres();
        String[] columnas = con.get_columnas();
        float suma;
        String sql;
        System.out.println(columnas.length + " - "+estadisticas.length);
        for (int i = 0; i < columnas.length; i++) {
            sql = "UPDATE estadisticas_generales_columnas SET ";
            suma = 0;
            for (int j = 0; j < estadisticas.length; j++) {
                if (estadisticas[j][i] != null) {
                    suma += Float.parseFloat(estadisticas[j][i]);
                }
            }
            System.out.println("columna es " + columnas[i]);
            sql = sql + "valores='" + suma / estadisticas.length + "' WHERE columna='" + columnas[i] + "'";
            //System.out.println(sql);
            con.ejecuta_sql(sql);
        }
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

    public void imprime_matrix(String[][] columnas_guardadas) {
        String valor = "";
        for (int i = 0; i < columnas_guardadas.length; i++) {
            for (int j = 0; j < 2; j++) {
                if (columnas_guardadas[i][j] != null) {
                    valor = valor + columnas_guardadas[i][j] + "|";
                }
            }
            System.out.println(valor);
            valor = "";
        }
    }
}
