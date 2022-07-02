import java.time.LocalDate;

public abstract class Accion {
    // Atributos
    protected LocalDate ultimaNoti = null;
    protected String textoPregunta;
    protected Integer cadaCuanto;

    protected String nombre;

    // Constructores
    public Accion(Integer cadaCuanto, String nombre, String textoPregunta) {
        this.textoPregunta = textoPregunta;
        this.cadaCuanto = cadaCuanto;
        this.nombre = nombre;
    }

    // Metodos

    public LocalDate getUltimaNoti() {
        return ultimaNoti;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public void setTextoPregunta(String textoPregunta) {
        this.textoPregunta = textoPregunta;
    }

    public Integer getCadaCuanto() {
        return cadaCuanto;
    }

    public void setUltimaNoti(LocalDate ultimaNoti) {
        this.ultimaNoti = ultimaNoti;
    }

    public void setCadaCuanto(Integer cadaCuanto) {
        this.cadaCuanto = cadaCuanto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public abstract Accion clonarAccion();

    public abstract Registro accionar() throws AccionFallidaException;

    public String getNombre() {
        return nombre;
    }

    public String mostrarAccion() {
        return "Accion : " + this.nombre + " , cada cuanto : " + this.cadaCuanto;
    }

    @Override
    public String toString() {
        return "Accion{" +
                "ultimaNoti=" + ultimaNoti +
                ", textoPregunta='" + textoPregunta + '\'' +
                ", cadaCuanto=" + cadaCuanto +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
