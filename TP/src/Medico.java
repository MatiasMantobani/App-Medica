import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Medico extends Usuario implements Tratamientos {

    private ArrayList<Integer> pacientesDelMedico = new ArrayList<>();  //ids paciente

    // Constructores
    public Medico() {
    }

    // sin lista de pacientes
    public Medico(String nombre, String apellido, String dni, String mail, String password) {
        super(nombre, apellido, dni, mail, password);
    }

    //Metodos
    public StringBuilder notificarMedico() {
        StringBuilder string1 = new StringBuilder();    //string 1 : pacientes que deben ser atendidos
        ArrayList<Paciente> listaPacientes = Persistencia.deserializacionPacientes();   // se levantan los pacientes
        string1.append("Pacientes que deben ser atendidos hoy: \n");
        for (Paciente pacientegeneral : listaPacientes) {   //en lista de pacientes levantada
            if (pacientegeneral.getDebeSerAtendido()) {     //si debe ser atendido
                for (Integer a : pacientesDelMedico) {
                    if (pacientegeneral.getId().equals(a) && pacientegeneral.getIdMedicoAsignado() != null && pacientegeneral.getIdMedicoAsignado().equals(this.getId())) {      // si es paciente del medico (id medico en paciente e id paciente en medico)
                        string1.append(pacientegeneral.getinfoPaciente());
                    }
                }
            }
        }
        if (string1.toString().equals("Pacientes que deben ser atendidos hoy: \n")) {
            string1.append(Colores.amarillo() + "No hay" + Colores.blanco());
        }
        string1.append("\n");
        StringBuilder string2 = new StringBuilder();    //string2 : pacientes que no registraron info ayer
        string2.append("Pacientes que no registraron toda la informacion ayer: \n");
        for (Paciente pacientegeneral : listaPacientes) {   // si esta en lista de pacientes
            if (pacientegeneral.tratamientoActual != null) {    //si tienen tratamiento
                for (Integer a : pacientesDelMedico) {
                    if (pacientegeneral.getId().equals(a) && pacientegeneral.getIdMedicoAsignado() != null && pacientegeneral.getIdMedicoAsignado().equals(this.getId())) {     //si es paciente de ese medico
                        if (!pacientegeneral.tratamientoActual.existeRegistroDiario(Sistema.getFechaDelDia().minusDays(1)) & pacientegeneral.tratamientoActual.getInicioDate().isBefore(Sistema.getFechaDelDia())) {    // Si no existe registro de ayer pero hay tratamiento asignado
                            string2.append(pacientegeneral.getinfoPaciente());  //el paciente no hizo nada
                        } else if (pacientegeneral.tratamientoActual.existeRegistroDiario(Sistema.getFechaDelDia().minusDays(1)) & pacientegeneral.tratamientoActual.getNumeroAccionesNotificables() != pacientegeneral.tratamientoActual.getNumeroAccionesRegistroDiario(Sistema.getFechaDelDia().minusDays(1))) {     //si hay registro de ayer y el numero de acciones realizadas es menor a las del tratamiento
                            string2.append(pacientegeneral.getinfoPaciente());  //el paciente no hizo algo
                        }
                    }
                }
            }
        }
        if (string2.toString().equals("Pacientes que no registraron toda la informacion ayer: \n")) {   //si todos los pacientes hicieron sus acciones
            string2.append(Colores.amarillo() + "No hay" + Colores.blanco());
        }
        return string1.append(string2); //se devuelven las 2 string
    }

    public ArrayList<Integer> obtenerIDsPacientes() {   //nice to have
        ArrayList<Integer> aux = new ArrayList();
        for (Integer a : pacientesDelMedico) {
            aux.add(a);
        }
        return pacientesDelMedico;
    }

    public void diagnosticarPacientes() throws DniInexistenteException {

        try {
            ArrayList<Paciente> listaPacientes = Persistencia.deserializacionPacientes();   // levantamos pacientes
            ArrayList<Tratamiento> listaTratamientosGenericos = Persistencia.deserializacionTratamientos(); // levantamos tratamientos genericos

            // muestro los pacientes que deben ser atendidos de vuelta
            int flagSinPacientes = 0;
            StringBuilder string = new StringBuilder();
            string.append("Pacientes que deben ser atendidos hoy: \n");
            for (Paciente pacientegeneral : listaPacientes) {
                if (pacientegeneral.getDebeSerAtendido()) {
                    for (Integer a : pacientesDelMedico) {
                        if (pacientegeneral.getId().equals(a) && pacientegeneral.getIdMedicoAsignado() != null && pacientegeneral.getIdMedicoAsignado().equals(this.getId())) {
                            string.append(pacientegeneral.getinfoPaciente());
                        }
                    }
                }
            }
            if (string.toString().equals("Pacientes que deben ser atendidos hoy: \n")) {    // por si no hay pacientes
                string.append(Colores.amarillo() + "No hay" + Colores.blanco());
                flagSinPacientes = 1;
            }

            System.out.println(string);

            // si hay pacientes para diagnosticar
            if (flagSinPacientes != 1) {
                Scanner scan = new Scanner(System.in);
                Paciente pacienteAux = null;
                int control = 0;
                while (control == 0) {

                    // elegimos paciente a diagnosticar por dni
                    System.out.println("Ingrese el dni del paciente que desea diagnosticar: ");
                    String dni = scan.nextLine();
                    for (Paciente a : listaPacientes) {
                        if (a.getDni().equals(dni)) {
                            if (a.getDebeSerAtendido()) {
                                pacienteAux = a;
                                control = 1;
                            }
                        }
                    }

                    if (pacienteAux == null) {  // validacion dni
                        System.out.println(Colores.amarillo() + "Dni invalido, ¿Quiere ingresar otro dni? (s/n)" + Colores.blanco());
                        if (scan.nextLine().charAt(0) != 's') {
                            throw new DniInexistenteException();
                        }
                    }
                }


                // menu para diagnosticar pacientes
                Tratamiento tratamientoAux = null;
                int x;
                int opcionMenu = 0;
                do {

                    System.out.println("[1] Elegir un tratamiento ya existente");
                    System.out.println("[2] Elegir un tratamiento ya existente y modificarlo");
                    System.out.println("[3] Crear un nuevo tratamiento");
                    System.out.println("[4] Salir");
                    System.out.println("Ingrese una opcion:");

                    //Validacion opcion menu
                    int flagSwitch = 0;
                    while (flagSwitch == 0) {
                        try {
                            opcionMenu = scan.nextInt();
                            flagSwitch = 1;
                        } catch (InputMismatchException i) {
                            System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                            System.out.println("Ingrese una opcion:");
                            scan.nextLine();
                        }
                    }

                    switch (opcionMenu) {
                        case 1:
                            // CASE 1 : Se elige un tratamiento ya existente para asignar a paciente
                            int flag1 = 0;
                            while (flag1 == 0) {
                                try {
                                    x = 1;
                                    for (Tratamiento a : listaTratamientosGenericos) {  //se muestran tratamientos
                                        System.out.println("[" + x + "] " + a.mostrarTratamientoString());
                                        x++;
                                    }
                                    System.out.println("Ingrese el numero del tratamiento que quiera asignarle al paciente: ");
                                    tratamientoAux = listaTratamientosGenericos.get(scan.nextInt() - 1).clonarTratamiento();
                                    pacienteAux.tratamientoActual = tratamientoAux;
                                    System.out.println(Colores.verde() + "Tratamiento generico asignado" + Colores.blanco());
                                    flag1 = 1;
                                } catch (IndexOutOfBoundsException e) { //validaciones
                                    System.out.println(Colores.rojo() + "Ingresaste una opcion incorrecta, intentalo nuevamente" + Colores.blanco());
                                } catch (InputMismatchException i) {
                                    System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                                    scan.nextLine();
                                }
                                opcionMenu = 0;
                            }

                            break;
                        case 2:
                            // CASE 2 : elegir tratamiento existente y modificarlo (no persiste)
                            int flag2 = 0;
                            while (flag2 == 0) {
                                try {
                                    x = 1;
                                    for (Tratamiento a : listaTratamientosGenericos) {  //se muestran tratamientos
                                        System.out.println("[" + x + "] " + a.mostrarTratamientoString());
                                        x++;
                                    }
                                    System.out.println("Ingrese el numero del tratamiento que desea modificar: ");
                                    tratamientoAux = listaTratamientosGenericos.get(scan.nextInt() - 1).clonarTratamiento();
                                    pacienteAux.tratamientoActual = tratamientoAux;
                                    pacienteAux.tratamientoActual = editarTratamiento(pacienteAux.tratamientoActual);   //se elige tratamiento y se manda a modificar con editarTratamiento() de Medico
                                    System.out.println(Colores.verde() + "Tratamiento modificado asignado" + Colores.blanco());
                                    flag2 = 1;
                                } catch (IndexOutOfBoundsException e) { //validaciones
                                    System.out.println(Colores.rojo() + "Ingresaste una opcion incorrecta, intentalo nuevamente" + Colores.blanco());
                                } catch (InputMismatchException i) {
                                    System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                                    scan.nextLine();
                                }
                            }
                            opcionMenu = 0;
                            break;

                        case 3:
                            // CASE 3 : Se crea un tratamiento nuevo para asignar
                            pacienteAux.tratamientoActual = crearTratamiento(); //se llama a crearTratamiento() de Medico (no persiste)
                            tratamientoAux = pacienteAux.tratamientoActual; //queda seteado el tratamiento aux para validaciones
                            System.out.println(Colores.verde() + "Tratamiento nuevo asignado" + Colores.blanco());
                            opcionMenu = 0;
                            break;

                        case 4:
                            System.out.println("Saliendo...");
                            opcionMenu = 0;
                            break;

                        default:
                            System.out.println(Colores.rojo() + "Opcion incorrecta, ingrese otra" + Colores.blanco());
                    }
                } while (opcionMenu != 0);

                if (tratamientoAux == null) {   //por si no se cargo nada a tratamiento aux (no hay genericos, salio antes, etc)
                    System.out.println(Colores.amarillo() + "No se cargo nada..." + Colores.blanco());

                } else {
                    // seteamos fecha de inicio y finde del tratamiento, y debeSerAtendido en false al tratamiento que estamos asignando
                    pacienteAux.tratamientoActual.setIncioDate(Sistema.getFechaDelDia());
                    pacienteAux.tratamientoActual.setFinDate(Sistema.getFechaDelDia().plusDays(pacienteAux.tratamientoActual.getDuracion()));
                    pacienteAux.setDebeSerAtendido(false);

                    // finalmente persistimos el archivo de pacientes, para que este sufra modificaciones
                    Persistencia.serializacionPacientes(listaPacientes);
                }
            } else {    //por si no hubo cambio de flag por ende no tenia pacientes
                System.out.println(Colores.amarillo() + "No tienes pacientes que atender en el dia de hoy" + Colores.blanco());
            }
        } catch (NullPointerException e) {  // por si nada sale bien
            System.out.println(Colores.amarillo() + "No tienes pacientes asignados por el momento" + Colores.blanco());
        }
    }

    public void verHistorialPaciente() throws DniInexistenteException {     //muestra historial medico (tratamientos terminados)
        Scanner scan = new Scanner(System.in);

        if (pacientesDelMedico.isEmpty()) { //si no tiene pacientes este medico
            System.out.println(Colores.amarillo() + "No hay pacientes asignados a este medico" + Colores.blanco());
        } else {    //si tiene pacientes
            ArrayList<Paciente> listaPacientes = Persistencia.deserializacionPacientes();   //levantamos pacientes
            for (Paciente p : listaPacientes) {
                for (int a : pacientesDelMedico) {  //para los pacientes del medico
                    if (p.getId().equals(a)) {
                        System.out.println(p.toStringInfoNoSensible()); //muestra nombre, apellido y DNI de los pacientes del medico
                    }
                }
            }

            // pregunta dni para ver historial
            Paciente pacienteAux = null;
            int control = 0;
            while (control == 0) {
                System.out.println("Ingrese el dni del paciente para ver su historial: ");

                String dni = scan.nextLine();
                pacienteAux = null;
                for (Paciente a : listaPacientes) {
                    if (a.getDni().equals(dni)) {
                        pacienteAux = a;
                        control = 1;
                    }
                }
                if (pacienteAux == null) {  //valida dni
                    System.out.println(Colores.amarillo() + "Dni invalido, ¿Quiere ingresar otro dni? s/n" + Colores.blanco());
                    if (scan.nextLine().charAt(0) != 's') {
                        throw new DniInexistenteException();
                    }
                }
            }

            //muestra el historial clinico
            try {
                if (pacienteAux.getHistorialClinico().isEmpty()) {  //si historial esta vacio
                    System.out.println(Colores.amarillo() + "No hay historial clinico para mostrar" + Colores.blanco());
                } else {
                    for (Tratamiento t : pacienteAux.getHistorialClinico()) {
                        t.mostrarTratamiento();
                    }
                }
            } catch (NullPointerException e) {  //manejo de excepcion nulo (no hay para mostrar)
                System.out.println(Colores.amarillo() + "No hay historial clinico para mostrar" + Colores.blanco());
            }
            System.out.println("Presione cualquier tecla para continuar");
            scan.nextLine();
        }
    }

    public void verHistorialTratamientoActual() throws DniInexistenteException {    //muestra un historial de tratamiento actual (el qeu esta haciendo y aun no termino)
        ArrayList<Paciente> listaPacientes = Persistencia.deserializacionPacientes();   //se levanta pacientes
        for (Paciente pacientegeneral : listaPacientes) {
            for (int a : pacientesDelMedico) {
                if (pacientegeneral.getId().equals(a)) {
                    System.out.println(pacientegeneral.toStringInfoNoSensible()); //muestra solo pacientes del medico
                }
            }
        }

        //pregunta dni y valida que sea de ese medico
        System.out.println("Ingrese el dni del paciente para ver el seguimiento de su tratamiento: ");
        Scanner scan = new Scanner(System.in);
        String dni = scan.nextLine();
        Paciente pacienteAux = new Paciente();
        for (Paciente a : listaPacientes) {
            if (a.getDni().equals(dni)) {
                pacienteAux = a;
            }
        }

        try {
            if (pacienteAux.getTratamientoActual() != null) {   //si tiene tratamiento
                if (!pacienteAux.tratamientoActual.listaRegistrosDiarios.isEmpty()) {   //si tiene registros
                    System.out.println(pacienteAux.tratamientoActual.toStringHistorialTratamientoActual());
                } else {
                    System.out.println(Colores.amarillo() + "El paciente que ingresaste no realizo ninguna accion del tratamiento por el momento!" + Colores.blanco());
                }
            } else {
                System.out.println(Colores.amarillo() + "El paciente que ingresaste no se encuentra realizando ningun tratamiento por el momento!" + Colores.blanco());
            }
        } catch (NullPointerException e) {  //atrapa excepcion nula desde Sistema y muestra dni invalido
        }
    }

    public void agregarPaciente(Integer id) {
        pacientesDelMedico.add(id);
    }   //agrega paciente a medico por id

    @Override
    public Tratamiento crearTratamiento() { //de interface Tratamientos
        int accionIndex;
        Scanner scan = new Scanner(System.in);
        ArrayList<Accion> listaAcciones = Persistencia.deserializacionAcciones();   //levanta acciones
        Tratamiento nuevoTratamiento = new Tratamiento();
        System.out.println("Ingrese la duracion del tratamiento:"); //pregunta duracion
        nuevoTratamiento.setDuracion(scan.nextInt());
        System.out.println("Ingrese el numero de acciones que tendra el tratamiento:"); //cant acciones
        int aux = scan.nextInt();
        int flag;

        for (int i = 0; i < aux; i++) { //se hara este proceso para cant de acciones elegidas
            flag = 0;
            while (flag == 0) {
                try {
                    int index = 1;
                    for (Accion a : listaAcciones) {
                        System.out.println("[" + index + "] " + a.mostrarAccion()); // muestra acciones existentes
                        index++;

                    }

                    System.out.println("Elija el numero de de la accion que desea para el tratamiento:");   //elige acciones
                    accionIndex = scan.nextInt() - 1;
                    if (listaAcciones.get(accionIndex) instanceof AccionBooleana) {
                        AccionBooleana accionAux = (AccionBooleana) listaAcciones.get(accionIndex).clonarAccion();  //clona accion Bool para no sobre-escribir acciones originales
                        System.out.println("Ingrese cada cuandos dias quiere que se realice la accion, encaso de ser todos los dias, ingrese 1:");
                        accionAux.setCadaCuanto(scan.nextInt());    //las acciones llevan atributo cada cuanto
                        nuevoTratamiento.listaAcciones.add(accionAux);  //se agrega accion clonada a tratamiento a crear
                    } else {    //de manera analoga para accion double
                        AccionDouble accionAux = (AccionDouble) listaAcciones.get(accionIndex).clonarAccion();
                        System.out.println("Ingrese cada cuandos dias quiere que se realice la accion, encaso de ser todos los dias, ingrese 1:");
                        accionAux.setCadaCuanto(scan.nextInt());
                        nuevoTratamiento.listaAcciones.add(accionAux);
                    }
                    flag = 1;
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(Colores.rojo() + "Ingresaste una opcion incorrecta, intentalo nuevamente" + Colores.blanco());
                } catch (InputMismatchException l) {
                    System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                    scan.nextLine();
                }
            }
        }
        return nuevoTratamiento;    // se retorna tratamiento nuevo
    }

    @Override
    public Tratamiento editarTratamiento(Tratamiento aux) { //de interfas Tratamientos
        Scanner scan = new Scanner(System.in);
        ArrayList<Accion> listaAcciones = Persistencia.deserializacionAcciones();   //levanta acciones
        int opcionMenu = 0;

        do {
            System.out.println("Editando el tratamiento para la enfermedad: " + aux.getEnfermedad().getNombre());
            System.out.println("[1] Agregar accion");
            System.out.println("[2] Quitar accion");
            System.out.println("[3] Modificar duracion");
            System.out.println("[4] Guardar tratamiendo");
            System.out.println("Ingrese una opcion:");

            //Validaciones de menu editar tratamiento
            int flagSwitch = 0;
            while (flagSwitch == 0) {
                try {
                    opcionMenu = scan.nextInt();
                    flagSwitch = 1;
                } catch (InputMismatchException i) {
                    System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                    System.out.println("Ingrese una opcion:");
                    scan.nextLine();
                }
            }
            int index;
            switch (opcionMenu) {
                case 1:
                    // CASE 1 : Agregar accion
                    int flag1 = 0;
                    int accionIndex;
                    while (flag1 == 0) {
                        try {
                            index = 1;
                            for (Accion a : listaAcciones) {
                                System.out.println("[" + index + "] " + a.mostrarAccion()); //muestra acciones genericas
                                index++;
                            }
                            System.out.println("Elija el numero de la accion que escoja para el tratamiento:"); //se elige la accione a editar
                            accionIndex = scan.nextInt();
                            if (listaAcciones.get(accionIndex - 1) instanceof AccionDouble) {   //si es double
                                AccionDouble accionAux = (AccionDouble) listaAcciones.get(accionIndex - 1).clonarAccion();  //la clona
                                System.out.println("Ingrese cada cuantos dias quiere que se realice la accion, encaso de ser todos los dias, ingrese 1:");
                                accionAux.setCadaCuanto(scan.nextInt());    //modifica cada cuanto
                                aux.listaAcciones.add(accionAux);   //la agrega
                            } else {    //analogo para accion booleana
                                AccionBooleana accionAux = (AccionBooleana) listaAcciones.get(accionIndex - 1).clonarAccion();
                                System.out.println("Ingrese cada cuandos dias quiere que se realice la accion, encaso de ser todos los dias, ingrese 1:");
                                accionAux.setCadaCuanto(scan.nextInt());
                                aux.listaAcciones.add(accionAux);
                            }
                            flag1 = 1;
                        } catch (IndexOutOfBoundsException e) { //manejo de excepciones
                            System.out.println(Colores.rojo() + "Ingresaste una opcion incorrecta, intentalo nuevamente" + Colores.blanco());
                        } catch (InputMismatchException i) {
                            System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                            scan.nextLine();
                        }
                    }
                    break;

                case 2:
                    // CASE 2 : quitar acciones
                    int flag2 = 0;
                    if (aux.listaAcciones.size() == 1) {    //no puede dejar un tratamiento sin acciones ("vacio")
                        System.out.println(Colores.amarillo() + "No se puede tener un tratamiento vacio" + Colores.blanco());
                    } else {
                        while (flag2 == 0) {
                            try {
                                index = 1;
                                for (Accion a : aux.listaAcciones) {
                                    System.out.println("[" + index + "] " + a.mostrarAccion()); //muestra acciones genericas
                                    index++;
                                }

                                System.out.println("Elija el numero de la accion que escoja para eliminar del tratamiento:");
                                accionIndex = scan.nextInt();
                                aux.listaAcciones.remove(accionIndex - 1);
                                flag2 = 1;
                            } catch (IndexOutOfBoundsException e) { //manejo de excepciones
                                System.out.println(Colores.rojo() + "Ingresaste una opcion incorrecta, intentalo nuevamente" + Colores.blanco());
                            } catch (InputMismatchException i) {
                                System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                                scan.nextLine();
                            }
                        }
                    }
                    break;

                case 3:
                    // CASE 3 : modificar duracion de tratamiento
                    System.out.println("Ingrese la duracion que desea para el tratamiento");
                    int x = scan.nextInt();
                    aux.setDuracion(x);
                    break;

                case 4:
                    // no persiste en archivo de tratamientos
                    System.out.println(Colores.verde() + "Guardando tratamieto..." + Colores.blanco());
                    break;

                default:
                    System.out.println(Colores.rojo() + "Opcion incorrecta, ingrese otra" + Colores.blanco());
            }
        } while (opcionMenu != 4);
        return aux; //retorna tratamiento modificado
    }

    @Override
    public String toString() {
        return super.toString() + "Medico{" + "pacientesDelMedico=" + pacientesDelMedico + '}';
    }
}
