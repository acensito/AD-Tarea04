package inicio;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import clases.Director;
import clases.Pelicula;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.Db4oIOException;
import com.db4o.query.Constraint;
import com.db4o.query.Query;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;

public class InicioE01 {

    /**
     * Método main, que iniciará el programa e introducirá valores en la BD
     * @param args 
     */
    public static void main(String[] args) {
        //Conectamos con la base de datos
        ObjectContainer db;
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "peliculas.db4o");
        
        try {
            //Llamamos al metodo que introducirá valores en la BD
            //almacenarPeliculas(db);
            consultarPeliculasDuracion(db);
        } catch (Exception e) {
            //En caso de errores que muestre mensaje por consola
            System.err.print("ERROR: " +e.getMessage());
        } finally {
            //Cerramos las operaciones con la BD
            db.close();
        }
    }
    
    public static void almacenarPeliculas(ObjectContainer db){
        //Creamos tres directores
        Director d1 = new Director("Steven Spielberg", "USA");
        Director d2 = new Director("Paul Verhoeven", "Holanda");
        Director d3 = new Director("Duncan Jones", "Reino Unido");
        
        //Creamos seis peliculas
        Pelicula p1 = new Pelicula("Ready Player One", d1, 140);
        Pelicula p2 = new Pelicula("Robocop", d2, 103);
        Pelicula p3 = new Pelicula("Warcraft: El Origen", d3, 123);
        Pelicula p4 = new Pelicula("Encuentros en la tercera fase", d1, 135);
        Pelicula p5 = new Pelicula("Desafio Total", d2, 109);
        Pelicula p6 = new Pelicula("Moon", d3, 93);
        
        //Almacenamos las peliculas
        db.store(p1);
        db.store(p2);
        db.store(p3);
        db.store(p4);
        db.store(p5);
        db.store(p6);
    }
    
    //Método para mostrar objetos recuperados de la Base de Objetos
    public static void mostrarConsulta(ObjectSet resul) {
        //Mensaje indicando el total de objetos recuperados
        System.out.println("Recuperados " + resul.size() + " Objetos");
        //Bucle que muestra objeto tras objeto
        while (resul.hasNext()) {
            System.out.println(resul.next());
        }
    }
    
    public static void consultarPeliculas(ObjectContainer db) {
        try {
            //Creamos una plantilla básica de búsqueda
            Pelicula p = new Pelicula(null, null, 0);
            //Consulta las canciones con patrones indicados
            ObjectSet resul = db.queryByExample(p);
            mostrarConsulta(resul);//método que muestra los objetos recuperados de BDOO
        } catch (DatabaseClosedException | Db4oIOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }   
    }
    
    public static void consultarPeliculasDirector(ObjectContainer db) {     
        try {
            //Solicitamos el nombre del director que queremos filtrar
            String nombre = JOptionPane.showInputDialog("Introduzca el nombre de un director:");
            //Creamos una plantilla de busqueda de objeto director
            Director d = new Director(nombre, null);
            //Incluimos a la plantilla de pelicula la plantilla de director
            Pelicula p = new Pelicula(null, d, 0);
            //Consulta las canciones con patrones indicados
            ObjectSet resul = db.queryByExample(p);
            mostrarConsulta(resul);//método que muestra los objetos recuperados de BDOO
        } catch (DatabaseClosedException | Db4oIOException | HeadlessException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
    
    public static void consultarPeliculasDuracion(ObjectContainer db) {
        try {
            //Solicitamos el valor minimo
            int min = Integer.parseInt(JOptionPane.showInputDialog("Introduzca la duración mínima:"));
            int max = Integer.parseInt(JOptionPane.showInputDialog("Introduzca la duración máxima:"));
            //Crearemos en esta ocasión una consulta SODA
            Query query = db.query();
            //Definimos la clase a la que le vamos a realizar una restricción
            query.constrain(Pelicula.class);
            //Se declara la primera restricción, valor tope
            Constraint constra1 = query.descend("duracion").constrain(max).smaller();
            //Se enlazan las dos reestricciones a aplicar
            query.descend("duracion").constrain(min).greater().and(constra1);
            ObjectSet result = query.execute();
            mostrarConsulta(result);
            
        } catch (DatabaseClosedException | Db4oIOException | HeadlessException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

}
