
package funciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
    
}
