
package clases;

public class Pelicula {
    //Atributos
    private String titulo;
    private Director director;
    private int duracion;
    //Constructor
    public Pelicula(String titulo, Director director, int duracion) {
        this.titulo = titulo;
        this.director = director;
        this.duracion = duracion;
    }
    //Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return "Titulo: " + titulo + ", Director: " + director + ", Duracion: " + duracion + " minutos.";
    }
}
