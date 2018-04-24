package Clases;

/**
 * Created by danie on 21/04/2018.
 */

public class Juego {

    public Juego()
    {

    }

    public Juego(String tipo, String titulo, int icono, int codigoLeccion) {
        Tipo = tipo;
        Titulo = titulo;
        Icono = icono;
        CodigoLeccion = codigoLeccion;
    }

    public String Tipo;
    public String Titulo;
    public int Icono;
    public int CodigoLeccion;
}
