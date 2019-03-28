
package funciones;

import ordena_revistas.Conexion;

public class Funciones_verificadora {
    Conexion con = new Conexion();
    public void verifica_diferentes() {
        
        String[][] promedios = con.get_estadisticas_distribucion_columnas();
        String[][] estadisticas1 = con.get_distribucion_columnas();
        boolean entro = false;
        for (int i = 100; i <= 181100; i = i + 100) {
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
}
