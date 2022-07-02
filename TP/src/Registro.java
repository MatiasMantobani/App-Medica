import java.util.Scanner;

public class Registro {
    // Atributos
    private String nombreAccion;
    private String rta;
    private Boolean seHizo;

    // Constructores
    public Registro(String nombre, String rta, Boolean seHizo) {
        this.nombreAccion = nombre;
        this.rta = rta;
        this.seHizo = seHizo;
    }

    // Metodos

    public String getNombreAccion() {
        return nombreAccion;
    }

    public void setNombreAccion(String nombreAccion) {
        this.nombreAccion = nombreAccion;
    }

    public String getRta() {
        return rta;
    }

    public void setRta(String rta) {
        this.rta = rta;
    }

    public Boolean getSeHizo() {
        return seHizo;
    }

    public void setSeHizo(Boolean seHizo) {
        this.seHizo = seHizo;
    }

    public String mostrarNombresRegistros() {
        return this.nombreAccion;
    }

    public void modificar() throws AccionFallidaException, EliminarRegistroException {
        Scanner scan = new Scanner(System.in);
        Character opcion;
        if (rta == null) {
            System.out.println("Usted marco la accion " + this.nombreAccion + " como" + (this.seHizo ? " realizada " : " sin realizar "));
        } else {
            System.out.println("Usted ingreso en la accion " + this.nombreAccion + " el resultado: " + this.rta);
        }
        System.out.println("Desea modificar la accion? (s/n): ");
        opcion = scan.nextLine().charAt(0);
        if (opcion != 's') {
            throw new AccionFallidaException();
        } else {
            if (rta == null) {
                throw new EliminarRegistroException(this.nombreAccion);
            } else {
                Character opcion2;
                System.out.println(Colores.amarillo() + "Si desea eliminar el registro ingrese 's', si quiere cambiar el resultado de la accion ingrese cualquier otra letra: " + Colores.blanco());
                opcion2 = scan.nextLine().charAt(0);
                if (opcion2 == 's') {
                    throw new EliminarRegistroException(this.nombreAccion);
                } else {
                    System.out.println("Ingrese el nuevo resultado: ");
                    String rta = scan.nextLine();
                    this.rta = rta;
                }
            }
        }
    }

    public String toStringParaHistorial() {
        String aux = "";
        aux += "La accion " + nombreAccion;
        if (this.seHizo) {
            aux += " se realizo";
            if (rta != null) {
                aux += " y su resultado fue:" + rta + ". \n";
            } else {
                aux += ". \n";
            }
        } else {
            aux += " no se realizo" + ". \n";
        }

        return aux;
    }

    @Override
    public String toString() {
        return "Registro{" +
                "nombreAccion='" + nombreAccion + '\'' +
                ", rta='" + rta + '\'' +
                ", seHizo=" + seHizo +
                '}';
    }
}
