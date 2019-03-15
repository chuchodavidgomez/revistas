
package ordena_revistas;

import java.io.IOException;

/**
 *
 * @author chucho
 */
public class Ordena_revistas {

    public static void main(String[] args){
        Verifica_columna v_c = new Verifica_columna();
        //v_c.readTxt();    
        //v_c.verifica_columnas();                
        //v_c.llena_vect_estadisticas();
        //v_c.llena_estadisticas_revistas(); 
        v_c.llena_promedios();
        //v_c.verifica_diferentes();
    }
    
}
