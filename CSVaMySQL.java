package csvamysql;

import com.csvreader.CsvReader;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xcheko51x
 */
public class CSVaMySQL {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        
        usuarios = importarCSV();
        
        insertarMySQL(usuarios);
    }
    
    public static List<Usuario> importarCSV() {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        
        try {
            CsvReader leerUsuarios = new CsvReader("Usuarios.csv");
            leerUsuarios.readHeaders();
            
            while(leerUsuarios.readRecord()) {
                String nombre = leerUsuarios.get(0);
                String telefono = leerUsuarios.get(1);
                String email = leerUsuarios.get(2);
                String usuario = leerUsuarios.get(3);
                String contrasena = leerUsuarios.get(4);
                
                usuarios.add(new Usuario(nombre, telefono, email, usuario, contrasena));
            }
            
            leerUsuarios.close();
            
            System.out.println("LISTA DE USUARIOS DEL CSV\n");
            for(Usuario user : usuarios) {
                System.out.println(
                        user.getNombre()+", "+
                        user.getTelefono()+", "+
                        user.getEmail()+", "+
                        user.getUsuario()+", "+
                        user.getContrasena()
                );
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return usuarios;
        
    }
    
    public static void insertarMySQL(List<Usuario> usuarios) {
        System.out.println("\nSE VAN A INSERTA: "+usuarios.size()+" REGISTROS\n");
        
        ConexionMySQL sql = new ConexionMySQL();
        Connection con = sql.conectarMySQL();
        
        String query = "INSERT INTO usuarios(nombre, telefono, email, usuario, contrasena) VALUES(?,?,?,?,?)";
        
        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(query);
            
            for(int i = 0 ; i < usuarios.size() ; i++) {
                ps.setString(1, usuarios.get(i).getNombre());
                ps.setString(2, usuarios.get(i).getTelefono());
                ps.setString(3, usuarios.get(i).getEmail());
                ps.setString(4, usuarios.get(i).getUsuario());
                ps.setString(5, usuarios.get(i).getContrasena());
                
                ps.executeUpdate();
                
                System.out.println("Se inserto el elemento: "+(i+1)+"/"+usuarios.size());
            }
            
            ps.close();
            con.close();
        } catch(SQLException e) {
            
        }
    }
}
