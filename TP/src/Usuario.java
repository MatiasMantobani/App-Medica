import java.util.Objects;

public class Usuario {

    //Atributos
    private String nombre;
    private String apellido;
    private String dni;
    private String mail;
    private String password;
    private Integer id;

    //Constructores
    // Vacio
    public Usuario() {
    }

    // Completo
    public Usuario(String nombre, String apellido, String dni, String mail, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.mail = mail;
        this.password = password;
        this.id = this.hashCode();
    }

    //Metodos
    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public Integer getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(dni, usuario.dni) && Objects.equals(mail, usuario.mail); // usa mail y dni ya que estos son unicos para cada usuario
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni, mail);
    }

    public String toStringInfoNoSensible() {
        return "Nombre: " + nombre + " " + apellido + " DNI: " + dni + "\n";
    }

    //mostrar historial
    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}