package inicio;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import clases.Director;
import clases.Pelicula;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
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
            //Rellenamos la BD
            almacenarPeliculas(db);
            //Llamamos al menu
            menu(db);
        } catch (Exception e) {
            //En caso de errores que muestre mensaje por consola
            System.err.print("ERROR: " +e.getMessage());
        }
    }
    
    public static void menu(ObjectContainer db) {
        //Lanzamos panel input
        String menu = JOptionPane.showInputDialog(
                   "Introduzca un valor a ejecutar:\n"
                + "***********************************\n"
                + "1 - Consulta todas las peliculas \n"
                + "2 - Consulta todas las peliculas de un director \n"
                + "3 - Consulta las peliculas por duracion \n"
                + "4 - Eliminar una pelicula por titulo \n"
                + "5 - Modifica la duracion de una pelicula \n"
                + "0 - Todas las anteriores");
        //En el caso que no se cancele 
        if (menu != null) {
            //Dependiendo del valor introducido, llamamos al método correspondiente
            switch(menu) {
                case "1":
                    consultarPeliculas(db);
                    menu(db);
                    break;
                case "2":
                    //Solicitamos el nombre del director que queremos filtrar
                    String nombre = JOptionPane.showInputDialog("Introduzca el nombre de un director:");
                    consultarPeliculasDirector(db, nombre);
                    menu(db);
                    break;
                case "3":
                    //Solicitamos el valor minimo y máximo
                    int min = Integer.parseInt(JOptionPane.showInputDialog("Introduzca la duración mínima:"));
                    int max = Integer.parseInt(JOptionPane.showInputDialog("Introduzca la duración máxima:"));
                    consultarPeliculasDuracion(db, min, max);
                    menu(db);
                    break;
                case "4":
                    String tit = JOptionPane.showInputDialog("Introduzca el nombre la pelicula a eliminar:");
                    eliminarPelicula(db, tit);
                    menu(db);
                    break;
                case "5":
                    String titulo = JOptionPane.showInputDialog("Introduzca el nombre la pelicula a modificar:");
                    int dur = Integer.parseInt(JOptionPane.showInputDialog("Introduzca la duración:"));
                    modificaDuracion(db, titulo, dur);
                    menu(db);
                    break;
                case "0":
                    consultarPeliculas(db);
                    consultarPeliculasDirector(db, "Guillermo del Toro");
                    consultarPeliculasDuracion(db, 90, 110);
                    eliminarPelicula(db, "Desafio Total");
                    modificaDuracion(db, "Robocop", 120);
                    menu(db);
                    break;
                default:
                    //Si se introduce cualquier otra cosa, se llama al menu nuevamente
                    menu(db);
                    break;
            }
        } else {
            //Cerramos las operaciones con la BD
            db.close();
            System.err.println("Salida del programa");
        }
    }
    
    public static void almacenarPeliculas(ObjectContainer db){
        //Creamos tres directores
        Director d1 = new Director("Steven Spielberg", "USA");
        Director d2 = new Director("Paul Verhoeven", "Holanda");
        Director d3 = new Director("Duncan Jones", "Reino Unido");
        Director d4 = new Director("Guillermo del Toro", "Mexico");
        
        //Creamos seis peliculas
        Pelicula p1 = new Pelicula("Ready Player One", d1, 140);
        Pelicula p2 = new Pelicula("Robocop", d2, 103);
        Pelicula p3 = new Pelicula("Warcraft: El Origen", d3, 123);
        Pelicula p4 = new Pelicula("Encuentros en la tercera fase", d1, 135);
        Pelicula p5 = new Pelicula("Desafio Total", d2, 109);
        Pelicula p6 = new Pelicula("Moon", d3, 93);
        Pelicula p7 = new Pelicula("Pacific Rim", d4, 0);
        
        //Almacenamos las peliculas
        db.store(p1);
        db.store(p2);
        db.store(p3);
        db.store(p4);
        db.store(p5);
        db.store(p6);
        db.store(p7);
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
    
    public static void consultarPeliculasDirector(ObjectContainer db, String nombre) {     
        try {
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
    
    public static void consultarPeliculasDuracion(ObjectContainer db, int min, int max) {
        try {
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
    
    public static void eliminarPelicula(ObjectContainer db, String tit) {
        //Reconfiguramos para poder proceder a eliminar en cascada
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().objectClass(Pelicula.class).cascadeOnDelete(true);
        //Declaramos un objeto de tipo query (consulta)
        Query query = db.query();
        //Establecemos la clase a la que se le aplicará la reestricción
        query.constrain(Pelicula.class);
        //Establecemos la restricción de búsqueda al titulo pasado por parámetro
        query.descend("titulo").constrain(tit);
        //Ejecuta la consulta con restricción de búsquedda
        ObjectSet resul = query.execute();
        //Bucle que recupera los objetos que coincidan y los va eliminando de la BD
        while (resul.hasNext()) {
            Pelicula p = (Pelicula) resul.next();
            //Mensaje feedback
            System.out.println("Eliminando: " + p);
            //Elimina el objeto de la base de datos
            db.delete(p);
        }
    }

    public static void modificaDuracion(ObjectContainer db, String tit, int dur) {
        //Creamos un patrón de consulta con el titulo que nos interesa
        ObjectSet res = db.queryByExample(new Pelicula(tit, null, 0));
        //Obtenemos la pelicula que nos interesa, ojo, solo modificará la primera
        //pelicula que encuentre con este patrón
        Pelicula p = (Pelicula) res.next();
        //Le asignamos la nueva duración
        p.setDuracion(dur);
        //Almacenamos la pelicula
        db.store(p);
    }
}
