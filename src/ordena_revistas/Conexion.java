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
    private static String user = "root";
    private static String password = "";
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
    
    public void crearBd(String[] columnas)
    {
        try 
        {
            String i = "CREATE TABLE IF NOT EXISTS columnas_revistas (name_file VARCHAR(50) NOT NULL PRIMARY KEY, ";
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
    
    public void inserta_columnas(String[] columnas)
    {
        System.out.println("entre aqui");
        try 
        {   
            
            for (int j = 0; j < columnas.length; j++) {                
                if(columnas[j] != null){
                    String i = "INSERT INTO columnas_estadisticas (columna,total) VALUES ('" + columnas[j] + "','0');";                       
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
    public String[][] getColumnas()
    {
        String sql = "";        
        ResultSet columnas = null;
        String[][] stats_columnas = null;
        try 
        {            
            
            sql = "SELECT * FROM columnas_estadisticas";           
            System.out.println(sql);
            Statement st = conn.createStatement();                        
            columnas = st.executeQuery(sql);            
            stats_columnas = new String[obtenerCantFilas(columnas)][3];            
            int i = 0;                                       
            
            do{
                for (int j = 0; j < 2; j++) {
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
    
    //método para actualizar la información de un estudiante
    /*
    public void actulizarAlumno(Alumno alumno)
    {
        try 
        {        
            String i = "UPDATE estudiante SET "                    
                    +"direccion_normalizada='"+alumno.getDireccion_normalizada()
                    +"', confiabilidad='"+alumno.getConfiabilidad()
                    +"' WHERE cedula='"+alumno.getCedula()+"' AND semestre='"+alumno.getSemestre()+"'";
            System.out.println(i);
            Statement st = conn.createStatement();
            st.execute(i);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
        
    public int obtenerCantFilas(ResultSet resultSet) throws SQLException
    {
        resultSet.last();
        int filas = resultSet.getRow();
        resultSet.first();
        return filas;
    }
    
}
