
package clases;

public class Director {
    private String nombre;
    private  String nacionalidad;
    //Constructor de la clase
    public Director(String nombre, String nacionalidad) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
    }
    //Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
    //Clase toString
    @Override
    public String toString() {
        return nombre + ", " + nacionalidad;
    }
}
