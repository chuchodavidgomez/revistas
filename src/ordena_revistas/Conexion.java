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
            String i = "CREATE TABLE IF NOT EXISTS columnas_revistas (Title_Id INT NOT NULL PRIMARY KEY, ";
            for (int j = 1; j < columnas.length; j++) {
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
    
    /*
    //método para insertar los alumnos en la base de datos
    public void insertAlumno(Alumno alumno)
    {
        try 
        {
            String i = "INSERT INTO estudiante VALUES "+
                    "('"+alumno.getCedula()+                                        
                    "','"+alumno.getSemestre()+
                    "','"+alumno.getDireccion()+                    
                    "','"+alumno.getMunicipio()+
                    "','"+alumno.getDepartamento()+  
                    "','"+
                    "','"+
                    "','"+
                    "','"+
                    "','"+
                    "')";
            System.out.println(i);
            Statement st = conn.createStatement();
            st.execute(i);
        } 
        catch (Exception e) 
        {
            System.out.println("Ocurrio este error "+e.getMessage());
        }
    }*/
  
    //método para obtener la información de un alumno por semestre
    public String getAlumno(String cedula, String semestre)
    {
        String sql = "";
        ResultSet alumnos = null;
        String alumno = "";
        try 
        {            
            sql = "SELECT * FROM estudiante WHERE cedula='"+cedula+"' AND semestre='" +semestre+ "'";           
            System.out.println(sql);
            Statement st = conn.createStatement();
            alumnos = st.executeQuery(sql); 
            if(alumnos.next())
            {
                alumno = alumnos.getString("cedula");
                
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alumno;
    }
    
    //métodos para obtener la información de todos los alumnos
    public String[][] getAlumnos()
    {
        String sql = "";        
        ResultSet alumnos = null;
        String[][] datosAlumnos = null;
        try 
        {            
            
            sql = "SELECT * FROM estudiante";           
            System.out.println(sql);
            Statement st = conn.createStatement();                        
            alumnos = st.executeQuery(sql);
            datosAlumnos = new String[obtenerCantFilas(alumnos)][10];            
            int i = 0;
            while(alumnos.next())
            {
                datosAlumnos[i][0] = alumnos.getString("cedula");
                datosAlumnos[i][1] = alumnos.getString("semestre");
                datosAlumnos[i][2] = alumnos.getString("direccion");                                                
                datosAlumnos[i][3] = alumnos.getString("municipio");            
                datosAlumnos[i][4] = alumnos.getString("departamento");
                datosAlumnos[i][5] = alumnos.getString("direccion_normalizada");                
                datosAlumnos[i][6] = alumnos.getString("latitud");
                datosAlumnos[i][7] = alumnos.getString("longitud");
                datosAlumnos[i][8] = alumnos.getString("barrio");
                datosAlumnos[i][9] = alumnos.getString("confiabilidad");
                i++;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return datosAlumnos;
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
