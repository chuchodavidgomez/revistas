
package ordena_revistas;

import java.io.IOException;
import funciones.Funciones_generadoras;
import funciones.Funciones_rellenadoras;
import funciones.Funciones_verificadora;

public class Ordena_revistas {

    public static void main(String[] args){
        Funciones_generadoras fg = new Funciones_generadoras();
        Funciones_rellenadoras fr = new Funciones_rellenadoras();
        Funciones_verificadora fv = new Funciones_verificadora();
        //fg.genera_tablas();//primer paso
        //fr.llenatb_distribucion_columnas();
        //fr.llena_columna_total_estadisticas_generales();
        //fr.llenatb_estadisticas_distribucion_columnas();
        //fr.llena_columna_promedio_estadisticas_generales();
        //fv.verifica_diferentes();
        Verifica_columna v_c = new Verifica_columna();
                       
       
    }
    
}
