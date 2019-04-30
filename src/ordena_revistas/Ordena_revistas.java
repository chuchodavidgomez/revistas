
package ordena_revistas;

import java.io.IOException;
import funciones.Funciones_generadoras;
import funciones.Funciones_rellenadoras;
import funciones.Funciones_verificadora;
import javax.swing.JOptionPane;

public class Ordena_revistas {

    public static void main(String[] args) throws IOException{
        String opt = JOptionPane.showInputDialog("Ingrese la opción:\n"
                + "1.Genera tablas\n"
                + "2.LLenar columnas\n"
                + "3.Verificar palabras en la dirección\n"
                + "4.Genera csv");
        switch(opt){
            case "1":
                Funciones_generadoras fg = new Funciones_generadoras();
                fg.genera_tablas();
                break;
            case "2":
                Funciones_rellenadoras fr = new Funciones_rellenadoras();
                //fr.llenatb_distribucion_columnas();
                //fr.llena_columna_total_estadisticas_generales();
                //fr.llenatb_estadisticas_distribucion_columnas();
                //fr.llena_columna_promedio_estadisticas_generales()
                //fr.llenatb_estadisticas_celdas_diferentes();
                fr.llena_columna_valores_estadisticas_generales();
                break;
            case "3":
                Funciones_verificadora fv = new Funciones_verificadora();
                //fv.verifica_diferentes();
                //fv.verifica_columna_repetida();
                break;
            case "4":
                //gd.writeTxt();
                break;
            default:
                break;
        }   
    }
    
}
