import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RegistroDiario {
    // Atributos
    private LocalDate fecha;
    protected ArrayList<Registro> listaRegistros;

    // Constructores
    public RegistroDiario() {
        this.fecha = Sistema.getFechaDelDia();
        this.listaRegistros = new ArrayList<Registro>();
    }

    // Metodos

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Registro> getListaRegistros() {
        return listaRegistros;
    }

    public void setListaRegistros(ArrayList<Registro> listaRegistros) {
        this.listaRegistros = listaRegistros;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void agregarRegistro(Registro registro) {
        listaRegistros.add(registro);
    }

    public void modificarRegistro() {
        Scanner scan = new Scanner(System.in);
        int flag = 0;

        while (flag == 0) {
            try {
                int i = 1;
                for (Registro r : listaRegistros) {
                    System.out.println("[" + i + "] " + r.mostrarNombresRegistros());
                    i++;
                }
                System.out.println("Ingrese el numero del Registro que desea modificar: ");
                int aux = scan.nextInt();
                try {
                    listaRegistros.get(aux - 1).modificar();
                } catch (EliminarRegistroException e) {
                    // porque no pide la accion de vuelta despues de borrarlo del registro diario?
                    listaRegistros.remove(aux - 1);
                }
                flag = 1;
            } catch (IndexOutOfBoundsException e) {
                System.out.println(Colores.rojo() + "Ingresaste una opcion incorrecta, intentalo nuevamente" + Colores.blanco());
            } catch (InputMismatchException i) {
                System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                scan.nextLine();
            } catch (AccionFallidaException f) {
                flag = 1;
            }
        }
    }

    public String mostrarRegistrosParaToString() {
        String rta = "";
        for (Registro a : listaRegistros) {
            rta += a.toStringParaHistorial();
        }
        return rta;
    }

    @Override
    public String toString() {
        return "Registro de la fecha: " +
                fecha + "\n" + mostrarRegistrosParaToString()
                ;
    }
}

