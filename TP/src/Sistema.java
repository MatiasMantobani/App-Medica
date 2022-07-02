import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Sistema {
    Scanner scan = new Scanner(System.in);

    // Atributos
    private ArrayList<Usuario> usuarios;    // Admins, Medicos y Pacientes
    private static LocalDate fechaDelDia = LocalDate.now();
    private Usuario usuarioLogueado;    // Se casteara al usuario que se loguie

    //Constructor
    public Sistema() {
        this.usuarios = new ArrayList<>();
        this.usuarioLogueado = null;
    }


    // Metodos
    public void menu() {

        // escondo los warning (soon to deprecated warnings)
        System.err.close();
        System.setErr(System.out);

        Scanner scan = new Scanner(System.in);
        int opcionMenu = 0;

        do {
            // Despersistimos medicos, pacientes y admins y los mandamos a la lista de usuarios
            this.usuarios = new ArrayList<Usuario>();
            ArrayList<Medico> medicosAux = Persistencia.deserializacion("medicos.json", Medico.class);
            ArrayList<Paciente> pacientesAux = Persistencia.deserializacionPacientes();
            ArrayList<Admin> adminsAux = Persistencia.deserializacion("admins.json", Admin.class);
            this.usuarios.addAll(medicosAux);
            this.usuarios.addAll(pacientesAux);
            this.usuarios.addAll(adminsAux);

            // Opciones menu principal
            System.out.println(Colores.violeta() + "Menu Principal" + Colores.blanco());
            System.out.println("Fecha del dia: " + fechaDelDia + "(Año/Mes/Dia)");
            System.out.println("[1] Log In");
            System.out.println("[2] Simular dias");
            System.out.println("[3] Resetear fecha");
            System.out.println("[4] Salir del programa");
            System.out.println("Ingrese una opcion:");

            // Validacion de opcion menu
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
                // CASE 1 : Se usa metodo login() para detectar si el usuario que entro es medico, paciente o admin
                case 1:
                    this.usuarioLogueado = this.login();    //login retorna el usuario con las credenciales ingresadas
                    if (this.usuarioLogueado != null) {
                        if (this.usuarioLogueado instanceof Paciente) { //se casteara el usuario a lo que corresponda (medico, paciente o admin)
                            this.menuPaciente();
                        } else if (this.usuarioLogueado instanceof Medico) {
                            this.menuMedico();
                        } else {
                            this.menuAdmin();
                        }
                    }
                    break;

                // CASE 2 : Para poder probar funcionamiento tenemos que poder simular que dia es (solo a futuro)
                case 2:
                    int flagDias = 0;
                    while (flagDias == 0) {
                        try {
                            System.out.println("Cuantos dias desea adelantarse?");
                            Sistema.agregarDias(scan.nextLong());
                            flagDias = 1;
                        } catch (InputMismatchException e) {
                            System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                            scan.nextLine();
                        }
                    }
                    break;

                // CASE 3 : Se deja de simular un dia elegido y se vuelve a la fecha actual
                case 3:
                    fechaDelDia = LocalDate.now();
                    System.out.println(Colores.amarillo() + "Resetenado la fecha del dia..." + Colores.blanco());
                    break;

                // CASE 4 : Se cierra el programa con opcionMenu != 4
                case 4:
                    System.out.println(Colores.rojo() + "Cerrando el programa..." + Colores.blanco());
                    break;

                // Validacion opcion elegida
                default:
                    System.out.println(Colores.rojo() + "Opcion incorrecta, ingrese otra" + Colores.blanco());
            }
        } while (opcionMenu != 4);
    }

    public void menuPaciente() {    // Si el usuario logueado es un paciente
        int opcionPaciente = 0;
        ((Paciente) usuarioLogueado).notificarPaciente();   // Lo primero que se hace es notificar al paciente (acciones que le corresponden)
        do {
            // Opciones menu paciente
            System.out.println(Colores.violeta() + "Menu Paciente" + Colores.blanco());
            System.out.println("[1] Realizar acciones del dia");
            System.out.println("[2] Modificar acciones del dia");
            System.out.println("[3] Mostrar acciones del dia");
            System.out.println("[4] Log Out");
            System.out.println("Ingrese una opcion:");

            // Validacion opcion elegida menu paciente
            int flagSwitch = 0;
            while (flagSwitch == 0) {
                try {
                    opcionPaciente = scan.nextInt();
                    flagSwitch = 1;
                } catch (InputMismatchException i) {
                    System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                    System.out.println("Ingrese una opcion:");
                    scan.nextLine();
                }
            }

            switch (opcionPaciente) {

                // CASE 1 : Elige que accion del dia hacer (si le toca, sino hay aviso)
                case 1:
                    try {
                        ((Paciente) usuarioLogueado).realizarAcciones();
                        persistirPacienteActual(((Paciente) usuarioLogueado));
                    } catch (NullPointerException e) {
                        System.out.println(Colores.amarillo() + "No te encuentras realizando un tratamiento" + Colores.blanco());
                    }
                    break;


                // CASE 2 : Elige que accion del dia editar (si tiene acciones del dia)
                case 2:
                    ((Paciente) usuarioLogueado).editarAccionesDelDia();
                    persistirPacienteActual(((Paciente) usuarioLogueado));
                    break;

                // CASE 3 : Por si no vio o quiere ver devuelta las notificaciones del dia
                case 3:
                    ((Paciente) usuarioLogueado).notificarPaciente();
                    break;

                // CASE 4 : Se cierra el menu paciente persistiendo los cambios que haya hecho
                case 4:
                    persistirPacienteActual(((Paciente) usuarioLogueado));
                    this.logout();
                    break;

                default:
                    System.out.println(Colores.rojo() + "Opcion incorrecta, ingrese otra" + Colores.blanco());
            }
        } while (opcionPaciente != 4);
    }

    public void menuMedico() {
        System.out.println(((Medico) usuarioLogueado).notificarMedico());   // 1ero se notifica al medico si tiene pacientes sin atender
        int opcionMedico = 0;
        do {
            // Opciones menu medico
            System.out.println(Colores.violeta() + "Menu Medico" + Colores.blanco());
            System.out.println("[1] Asignar tratamiento");
            System.out.println("[2] Ver las notificaciones del dia");
            System.out.println("[3] Ver historiales de los pacientes");
            System.out.println("[4] Ver historial del tratamiento actual de los pacientes");
            System.out.println("[5] Log Out");
            System.out.println("Ingrese una opcion:");

            //Validacion opcion elegida
            int flagSwitch = 0;
            while (flagSwitch == 0) {
                try {
                    opcionMedico = scan.nextInt();
                    flagSwitch = 1;
                } catch (InputMismatchException i) {
                    System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                    System.out.println("Ingrese una opcion:");
                    scan.nextLine();
                }
            }

            switch (opcionMedico) {
                case 1:
                    // CASE 1 : El medico elige a que paciente asignar un tratamiento con el dni
                    try {
                        ((Medico) usuarioLogueado).diagnosticarPacientes();
                    } catch (DniInexistenteException e) {
                    }
                    break;

                case 2:
                    // CASE 2 : Puede volver a ver las notificaciones si lo desea
                    System.out.println(((Medico) usuarioLogueado).notificarMedico());
                    break;

                case 3:
                    // Ver historial clinico (tratamientos terminados) de solo sus pacientes
                    try {
                        ((Medico) usuarioLogueado).verHistorialPaciente();
                    } catch (DniInexistenteException e) {
                    }
                    break;

                case 4:
                    // Ver historial del tratamiento actual del paciente (que hizo, que no, etc)
                    try {
                        ((Medico) usuarioLogueado).verHistorialTratamientoActual();
                    } catch (DniInexistenteException e) {
                    }
                    break;

                case 5:
                    this.logout();
                    break;

                default:
                    System.out.println(Colores.rojo() + "Opcion incorrecta, ingrese otra" + Colores.blanco());
            }
        } while (opcionMedico != 5);
    }

    public void menuAdmin() {
        int opcion = 0;
        do {
            // Opciones menu admin
            System.out.println(Colores.violeta() + "Menu Admin" + Colores.blanco());
            System.out.println("[1] Registrar un nuevo paciente");
            System.out.println("[2] Registrar un nuevo medico");
            System.out.println("[3] Agregar una nueva enfermedad");
            System.out.println("[4] Crear un nuevo tratamiento");
            System.out.println("[5] Editar un tratamiento");
            System.out.println("[6] Crear una nueva accion");
            System.out.println("[7] Log Out");
            System.out.println("Ingrese una opcion:");

            // Validacion opcion elegida
            int flagSwitch = 0;
            while (flagSwitch == 0) {
                try {
                    opcion = scan.nextInt();
                    flagSwitch = 1;
                } catch (InputMismatchException i) {
                    System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                    System.out.println("Ingrese una opcion:");
                    scan.nextLine();
                }
            }

            switch (opcion) {
                case 1: {
                    // Se crea paciente nuevo
                    ((Admin) usuarioLogueado).registrarPaciente();
                }
                break;

                case 2: {
                    // Se crea medico nuevo
                    ((Admin) usuarioLogueado).registrarMedico();
                }
                break;

                case 3: {
                    // Se crea enfermedad nueva
                    ((Admin) usuarioLogueado).agregarEnfermedad();
                }
                break;

                case 4: {
                    // Se crea tratamiento nuevo
                    ((Admin) usuarioLogueado).crearTratamiento();
                }
                break;

                case 5: {
                    // Se edita un tratamiento existente
                    ((Admin) usuarioLogueado).editarTratamiento(new Tratamiento());
                }
                break;

                case 6: {
                    // Se crea una accion nueva
                    ((Admin) usuarioLogueado).crearAccion();
                }
                break;

                case 7: {
                    this.logout();
                }
                break;

                default:
                    System.out.println(Colores.rojo() + "Opcion incorrecta, ingrese otra" + Colores.blanco());
            }
        } while (opcion != 7);
    }

    public Usuario login() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Log In");
        Usuario rta = null;     // Se retorna un Usuario (el que valide el mail y pass)
        Character opcion = 's';
        while (rta == null && opcion == 's') {  //Se pide que ingrese mail y pass

            System.out.println("Mail: ");
            String mail = scan.nextLine();

            System.out.println("Contraseña: ");
            String pass = scan.nextLine();

            // Se valida mail y contraseña con validarCredenciales(...)
            try {
                rta = validarCredenciales(mail, pass);  //Validar credenciales retorna un Usuario
                opcion = 'n';
            } catch (CredencialesIncorrectasException e) {
                System.out.println("Desea intentarlo de vuelta? (s/n):");
                opcion = scan.nextLine().charAt(0);
            }
        }
        return rta;
    }

    public static void agregarDias(long x) {
        // Solo se puede agregar dias, no restar (seria como ir para atras en el tiempo y njo se puede)
        if (x < 0) {
            System.out.println(Colores.amarillo() + "Solo se puede ir a fechas futuras" + Colores.blanco());
        } else {
            fechaDelDia = fechaDelDia.plusDays(x);
            System.out.println(Colores.amarillo() + "Cambiando la fecha del dia..." + Colores.blanco());
        }
    }

    public void persistirPacienteActual(Paciente logueado) {
        ArrayList<Paciente> pacientes = Persistencia.deserializacionPacientes();
        for (Paciente a : pacientes) {
            if (a.getId().equals(logueado.getId())) {
                pacientes.set(pacientes.indexOf(a), logueado);
            }
        }
        Persistencia.serializacionPacientes(pacientes);
    }

    public Usuario validarCredenciales(String mail, String pass) throws CredencialesIncorrectasException {
        for (Usuario u : this.usuarios) {
            if (u.getMail().equalsIgnoreCase(mail)) {
                if (u.getPassword().equals(pass)) {
                    return u;   // Retorna el usuario con las credenciales pedidas
                } else {
                    throw new CredencialesIncorrectasException(mail);
                }
            }
        }
        throw new CredencialesIncorrectasException();
    }

    public void logout() {
        // Se resetea el usuario logueado
        this.usuarioLogueado = null;
    }


    public static boolean comprobarCorrespodenAccion(LocalDate fecha, int cadaCuanto) {
        // Se fija si la accion corresponde hacerla el dia de hoy
        while (fecha.isBefore(fechaDelDia)) {
            fecha = fecha.plusDays(cadaCuanto);
        }
        if (fecha.isEqual(Sistema.getFechaDelDia())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean comprobarCorrespodiaAccionPasado(LocalDate inicioTratamiento, LocalDate fechaRegistro, int cadaCuanto) {
        // Comprueba si la accion debio haber sido realizada
        while (inicioTratamiento.isBefore(fechaRegistro)) {
            inicioTratamiento = inicioTratamiento.plusDays(cadaCuanto);
        }

        if (inicioTratamiento.isEqual(fechaRegistro)) {
            return true;
        } else {
            return false;
        }
    }

    public static LocalDate getFechaDelDia() {
        return fechaDelDia;
    }
}

