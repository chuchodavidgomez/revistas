/*
    Esta clase es la encargada de conectarse con la base de datos en MySql
*/
package ordena_revistas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author JesusDavid
 */
public class Conexion 
{
    //Información necesaria para poder conectarse con la base de datos
    private static Connection conn;
    private static String driver = "com.mysql.jdbc.Driver";
    //private static String user = "root";
    //private static String password = "";
    private static String user = "chuchito";
    private static String password = "root";
    private static String url = "jdbc:mysql://localhost/datosudea";    

    //Metodo encargado de conectarse con la base de datos
    public Conexion() 
    {
        conn = null;
        try
        {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            JOptionPane.showMessageDialog(null,"Se conecto con el servidor");
        }
        catch(ClassNotFoundException | SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Error al conectar con el servidor");
        }
        
    }
    
    public Connection getConection()
    {
        return conn;
    }
    
    public void desconectar()
    {
        conn = null;
        if(conn == null)
        {
            System.out.println("conexion terminada");
        }       
    }
    
    public void creartb_estadisticas_generales_columnas()
    {
        try 
        {
            String i = "CREATE TABLE IF NOT EXISTS estadisticas_generales_columnas (id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, columna varchar(100), total int(11), promedio float(11), valores float(11), separadores int(11)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_spanish_ci";
            System.out.println(i);
            Statement st = conn.createStatement();
            st.execute(i);
        } 
        catch (Exception e) 
        {
            System.out.println("Ocurrio este error "+e.getMessage());
        }
    }
    
    public void creartb_distribucion_columnas(String[] columnas)
    {
        try 
        {
            String i = "CREATE TABLE IF NOT EXISTS distribucion_columnas (name_file VARCHAR(50) NOT NULL PRIMARY KEY, ";
            for (int j = 0; j < columnas.length; j++) {
                if(columnas[j] != null){
                    i = i + columnas[j] + " VARCHAR(20),";                    
                }                    
            }    
            i = i.substring(0, i.length()-1)+") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_spanish_ci";
            System.out.println(i);
            Statement st = conn.createStatement();
            st.execute(i);
        } 
        catch (Exception e) 
        {
            System.out.println("Ocurrio este error "+e.getMessage());
        }
    }
    
    public void creartb_estadisticas_distribucion_columnas(String[] columnas)
    {
        try 
        {
            String i = "CREATE TABLE IF NOT EXISTS estadisticas_distribucion_columnas (name_file VARCHAR(50) NOT NULL PRIMARY KEY, ";
            for (int j = 0; j < columnas.length; j++) {
                if(columnas[j] != null){
                    i = i + columnas[j] + " FLOAT,";                    
                }                    
            }    
            i = i.substring(0, i.length()-1)+") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_spanish_ci";
            System.out.println(i);
            Statement st = conn.createStatement();
            st.execute(i);
        } 
        catch (Exception e) 
        {
            System.out.println("Ocurrio este error "+e.getMessage());
        }
    }
    
    public void creartb_estadisticas_celdas_diferentes(String[] columnas)
    {
        try 
        {
            String i = "CREATE TABLE IF NOT EXISTS estadisticas_celdas_diferentes (name_file VARCHAR(50) NOT NULL PRIMARY KEY, ";
            for (int j = 0; j < columnas.length; j++) {
                if(columnas[j] != null){
                    i = i + columnas[j] + " FLOAT,";                    
                }                    
            }    
            i = i.substring(0, i.length()-1)+") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_spanish_ci";
            System.out.println(i);
            Statement st = conn.createStatement();
            st.execute(i);
        } 
        catch (Exception e) 
        {
            System.out.println("Ocurrio este error "+e.getMessage());
        }
    }
    
    
    //too many columns
    public void creartb_final(String[] columnas) {
        try 
        {
            String i = "CREATE TABLE IF NOT EXISTS tabla_final (title_id VARCHAR(50) NOT NULL PRIMARY KEY, ";
            for (int j = 1; j < columnas.length; j++) {
                if(columnas[j] != null){
                    i = i + columnas[j] + " text(50),";                    
                }                    
            }    
            i = i.substring(0, i.length()-1)+") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_spanish_ci";
            System.out.println(i);
            Statement st = conn.createStatement();
            st.execute(i);
        } 
        catch (Exception e) 
        {
            System.out.println("Ocurrio este error "+e.getMessage());
        }
    }
    
    public void inserta_valores_columnas(String[] columnas)
    {       
        try 
        {   
            
            for (int j = 0; j < columnas.length; j++) {                
                if(columnas[j] != null){
                    String i = "INSERT INTO estadisticas_generales_columnas (columna,total) VALUES ('" + columnas[j] + "','0');";                       
                    System.out.println(i);
                    Statement st = conn.createStatement();
                    st.execute(i);
                }                      
            }    
            
        } 
        catch (Exception e) 
        {
            System.out.println("Ocurrio este error "+e.getMessage());
        }
    }
   
   
    //método para obtener la información de un alumno por semestre
    public void ejecuta_sql(String sql)
    {        
        try 
        {                   
            System.out.println(sql);
            Statement st = conn.createStatement();
            st.execute(sql); 
            
        } 
        catch (SQLException e) 
        {
            System.out.println("Ocurrio este error "+e.getMessage());
        }
    }
    
    //métodos para obtener la información de todos los columnas
    public String[] get_columnas()
    {
        String sql = "";        
        ResultSet columnas = null;
        String[] stats_columnas = null;
        try 
        {            
            
            sql = "SELECT * FROM estadisticas_generales_columnas";           
            System.out.println(sql);
            Statement st = conn.createStatement();                        
            columnas = st.executeQuery(sql);            
            stats_columnas = new String[obtenerCantFilas(columnas)];            
            int i = 0;                                       
            
            do{                            
                stats_columnas[i] = columnas.getString(2);
                i++;
            }while(columnas.next());
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stats_columnas;
    }
    
    public String[][] get_estadisticas_generales_columnas2()
    {
        String sql = "";        
        ResultSet columnas = null;
        String[][] stats_columnas = null;
        try 
        {            
            
            sql = "SELECT * FROM estadisticas_generales_columnas";           
            System.out.println(sql);
            Statement st = conn.createStatement();                        
            columnas = st.executeQuery(sql);            
            stats_columnas = new String[obtenerCantFilas(columnas)][6];            
            int i = 0;                                       
            
            do{
                for (int j = 0; j < 6; j++) {
                    stats_columnas[i][j] = columnas.getString(j+1);
                }
                i++;
            }while(columnas.next());
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stats_columnas;
    }
    
    public String[][] get_estadisticas_distribucion_columnas()
    {
        String sql = "";        
        ResultSet columnas = null;
        String[][] stats_columnas = null;
        try 
        {            
            
            sql = "SELECT * FROM estadisticas_distribucion_columnas";   
            System.out.println(sql);
            Statement st = conn.createStatement();                        
            columnas = st.executeQuery(sql);            
            stats_columnas = new String[obtenerCantFilas(columnas)][68];            
            int i = 0;                                       
            
            do{
                for (int j = 1; j < 68; j++) {
                    stats_columnas[i][j-1] = columnas.getString(j+1);
                }
                i++;
            }while(columnas.next());
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stats_columnas;
    }
    
    public String[][] get_distribucion_columnas()
    {
        String sql = "";        
        ResultSet columnas = null;
        String[][] stats_columnas = null;
        try 
        {            
            
            sql = "SELECT * FROM distribucion_columnas";   
            System.out.println(sql);
            Statement st = conn.createStatement();                        
            columnas = st.executeQuery(sql);            
            stats_columnas = new String[obtenerCantFilas(columnas)][68];            
            int i = 0;                                       
            
            do{
                for (int j = 1; j < 68; j++) {
                    stats_columnas[i][j-1] = columnas.getString(j+1);
                }
                i++;
            }while(columnas.next());
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stats_columnas;
    }
    
    public String[][] get_distribucion_caracteres()
    {
        String sql = "";        
        ResultSet columnas = null;
        String[][] stats_columnas = null;
        try 
        {            
            
            sql = "SELECT * FROM estadisticas_celdas_diferentes";   
            System.out.println(sql);
            Statement st = conn.createStatement();                        
            columnas = st.executeQuery(sql);            
            stats_columnas = new String[obtenerCantFilas(columnas)][68];            
            int i = 0;                                       
            
            do{
                for (int j = 1; j < 68; j++) {
                    stats_columnas[i][j-1] = columnas.getString(j+1);
                }
                i++;
            }while(columnas.next());
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stats_columnas;
    }
        
    public int obtenerCantFilas(ResultSet resultSet) throws SQLException
    {
        resultSet.last();
        int filas = resultSet.getRow();
        resultSet.first();
        return filas;
    }    
    
}
