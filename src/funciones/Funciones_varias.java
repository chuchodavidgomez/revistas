
package funciones;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public void prueba_automatizacion(){
        //emulamos un navegador web
        final WebClient webClient = new WebClient();

        try {
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            //Pagina donde se hara la consulta
            System.out.println("-----------"+webClient.getOptions().toString());
            HtmlPage page1 = webClient.getPage("http://ulrichsweb.serialssolutions.com/search/1786591");
            
            //nombre del formulario
            final HtmlForm form = page1.getForms().get(0);
            //el valor "f" no es arbitrario es el nombre del formulario web de google
            //DomElement domElement = page1.getElementById("cb_list");
            //domElement.click();
            //nombre de la caja de texto
            //final HtmlTextInput textField = form.getInputByName("q");
            //el valor "q" no es arbitrario es el nombre de la caja de texto del formulario web de google

            //nombre del boton del formulario
            //final HtmlSubmitInput button = form.getInputByName("btnK");
            //el valor "btnG" no es arbitrario es el nombre del boton del formulario web de google
                        
            //llenamos la caja de texto
            //textField.setValueAttribute("usandojava");

            //Creamos la pagina que nos devolverá el resultado
            HtmlPage pageResultado;
            
            //hacemos clic en el boton del formulario y asignamos el resultado a la pagina pageResultado
            //pageResultado = button.click();
            
            //imprimimos el resultado
            System.out.println(page1.asText());
            
            //cerramos el navegador emulado, para liberar todo esto de la memoria
            webClient.close();
        } catch (IOException ex) {
            Logger.getLogger(Funciones_varias.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(Funciones_varias.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
