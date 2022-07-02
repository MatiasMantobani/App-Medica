import java.util.Scanner;

public class AccionBooleana extends Accion {

    // Constructores
    public AccionBooleana(int cadaCuanto, String nombre, String textoPregunta) {
        super(cadaCuanto, nombre, textoPregunta);
    }

    // Metodos
    public void mostrarAccionBooleana() {
        System.out.println(super.mostrarAccion());
    }

    @Override
    public AccionBooleana clonarAccion() {
        AccionBooleana aux = new AccionBooleana(this.cadaCuanto, this.nombre, this.textoPregunta);
        return aux;
    }

    @Override
    public Registro accionar() throws AccionFallidaException {  // pregunta si quiere realizar la accion bool y crea y retorna el registro para guardar que la hizo
        Scanner scan = new Scanner(System.in);
        Character opcion;
        System.out.println("Quiere realizar la accion " + super.getNombre() + ": (s/n)");
        opcion = scan.nextLine().charAt(0);
        if (opcion != 's') {
            throw new AccionFallidaException();
        } else {

            Registro registro = new Registro(super.getNombre(), null, true);
            return registro;
        }
    }

    @Override
    public String toString() {
        return "AccionBooleana{" + super.toString() +
                '}';
    }
}
