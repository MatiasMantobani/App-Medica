import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin extends Usuario implements Tratamientos {

    //Constructores
    public Admin(String nombre, String apellido, String dni, String mail, String password) {
        super(nombre, apellido, dni, mail, password);
    }

    //Metodos
    public void registrarPaciente() {
        Scanner scan = new Scanner(System.in);

        Paciente rta;
        // levantar archivo usuarios
        ArrayList<Paciente> pacientes = Persistencia.deserializacionPacientes();
        ArrayList<Medico> medicos = Persistencia.deserializacion("medicos.json", Medico.class);
        ArrayList<Usuario> aux = new ArrayList<Usuario>();
        aux.addAll(pacientes);
        aux.addAll(medicos);

        if (medicos.isEmpty()) {
            System.out.println(Colores.amarillo() + "No puede crear pacientes sin medicos" + Colores.blanco());
        } else {
            Character opcionDni = 'n';
            String dni = "";
            int flagDni;
            System.out.println("Registrando un paciente:");
            while (opcionDni != 's') {
                flagDni = 0;
                while (flagDni == 0) {
                    try {
                        System.out.println("Ingrese el dni del paciente: ");
                        dni = scan.nextLine();
                        dni = validarDni(dni);
                        System.out.println("Ingreso este dni: " + dni + " quiere continuar con el? (s/n)");
                        opcionDni = scan.nextLine().charAt(0);
                        flagDni = 1;
                    } catch (DniIncorrectoException w) {
                    }
                }
            }
            //se fija que exista el usuario
            try {
                Usuario verificar = this.buscarUsuario(aux, dni);
                if (verificar instanceof Paciente) {
                    // si existe le cambio el atributo debeSerAtendido a true
                    rta = (Paciente) verificar;
                    // verifico que no tenga un tratamiento en curso
                    if (rta.getTratamientoActual() == null) {
                        System.out.println(Colores.amarillo() + "El paciente ya existe, asignarle un medico..." + Colores.blanco());
                        // asignar medico
                        Integer id = asignarMedico(rta.getApellido(), rta.getNombre());
                        rta.setIdMedicoAsignado(id);
                        rta.setDebeSerAtendido(true);

                        for (Medico a : medicos) {
                            if (id.equals(a.getId())) {
                                a.agregarPaciente(rta.getId());
                            }
                        }
                    } else {
                        System.out.println(Colores.amarillo() + "El paciente ya se encuentra con un tratamiento vigente, debe terminar el mismo para generar una nueva visita" + Colores.blanco());
                    }
                } else {
                    System.out.println(Colores.amarillo() + "El dni que ingresaste pertenece a un medico ya registrado" + Colores.blanco());
                }
            } catch (UsuarioInexistenteException e) {
                // si no lo creo y lo agrego
                int flag = 0;
                while (flag == 0) {
                    try {
                        System.out.println("Nombre: ");
                        String nombre = scan.nextLine();
                        System.out.println("Apellido: ");
                        String apellido = scan.nextLine();

                        int flag2 = 0;
                        String mail = "";
                        while (flag2 == 0) {
                            try {
                                System.out.println("Mail: ");
                                mail = this.validarMail(aux, scan.nextLine());
                                flag2 = 1;
                            } catch (MailRepetidoException w) {
                            }
                        }
                        System.out.println("Password: ");
                        String password = scan.nextLine();
                        //asignar medico
                        Integer id = asignarMedico(apellido, nombre);
                        flag = 1;
                        rta = new Paciente(nombre, apellido, dni, mail, password, id);
                        for (Medico a : medicos) {
                            if (id.equals(a.getId())) {
                                a.agregarPaciente(rta.getId());
                            }
                        }
                        pacientes.add(rta);
                        System.out.println(Colores.verde() + "Creaste el paciente con exito!" + Colores.blanco());
                    } catch (InputMismatchException f) {
                        System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                    }
                }
            }
            //persistir
            Persistencia.serializacionPacientes(pacientes);
            Persistencia.serializacion(medicos, "medicos.json");
        }
    }

    public void registrarMedico() {
        Scanner scan = new Scanner(System.in);
        Medico rta;
        // levantar archivo usuarios
        ArrayList<Medico> medicos = Persistencia.deserializacion("medicos.json", Medico.class);
        ArrayList<Paciente> pacientes = Persistencia.deserializacionPacientes();
        ArrayList<Usuario> aux = new ArrayList<Usuario>();
        aux.addAll(medicos);
        aux.addAll(pacientes);
        String dni = "";
        Character opcionDni = 'n';
        int flagDni;
        System.out.println("Registrando un Medico:");
        while (opcionDni != 's') {
            flagDni = 0;
            while (flagDni == 0) {
                try {
                    System.out.println("Ingrese el dni del medico: ");
                    dni = scan.nextLine();
                    dni = validarDni(dni);
                    System.out.println("Ingreso este dni: " + dni + " quiere continuar con el? (s/n)");
                    opcionDni = scan.nextLine().charAt(0);
                    flagDni = 1;
                } catch (DniIncorrectoException w) {
                }
            }
        }
        //fijarse que exista el usuario
        try {
            if (this.buscarUsuario(aux, dni) instanceof Paciente) {
                System.out.println(Colores.amarillo() + "El dni que ingresaste pertenece a un paciente ya registrado" + Colores.blanco());
            } else {
                System.out.println(Colores.amarillo() + "El Medico ya existe, no hace falta registrarlo de vuelta" + Colores.blanco());
            }
        } catch (UsuarioInexistenteException e) {
            // si no lo creo y lo agrego
            int flag = 0;
            while (flag == 0) {
                try {
                    System.out.println("Nombre: ");
                    String nombre = scan.nextLine();
                    System.out.println("Apellido: ");
                    String apellido = scan.nextLine();

                    int flag2 = 0;
                    String mail = "";
                    while (flag2 == 0) {
                        try {
                            System.out.println("Mail: ");
                            mail = this.validarMail(aux, scan.nextLine());
                            flag2 = 1;
                        } catch (MailRepetidoException w) {
                        }
                    }

                    System.out.println("Password: ");
                    String password = scan.nextLine();
                    flag = 1;
                    rta = new Medico(nombre, apellido, dni, mail, password);
                    medicos.add(rta);
                    System.out.println(Colores.verde() + "Creaste el medico con exito!" + Colores.blanco());
                } catch (InputMismatchException f) {
                    System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                }
            }
        }
        //persistir
        Persistencia.serializacion(medicos, "medicos.json");
    }


    private Usuario buscarUsuario(ArrayList<Usuario> listado, String dni) throws UsuarioInexistenteException {
        for (Usuario u : listado) {
            if (u.getDni().equals(dni)) {
                return u;
            }
        }
        throw new UsuarioInexistenteException();
    }

    private String validarMail(ArrayList<Usuario> listado, String mail) throws MailRepetidoException {
        for (Usuario u : listado) {
            if (u.getMail().equals(mail)) {
                throw new MailRepetidoException();
            }
        }
        if (mail.contains("@hotmail.com") || mail.contains("@outlook.com") || mail.contains("@gmail.com")) {
            return mail;
        }
        throw new MailRepetidoException(mail);
    }

    public String validarDni(String dni) throws DniIncorrectoException {
        if (dni.matches("[0-9]+")) {
            return dni;
        }
        throw new DniIncorrectoException();
    }

    private Integer asignarMedico(String apellido, String nombre) {
        ArrayList<Medico> medicos = Persistencia.deserializacion("medicos.json", Medico.class);
        Scanner scan = new Scanner(System.in);
        int rta = 0;
        int flag = 0;
        while (flag == 0) {
            try {
                int i = 1;
                System.out.println("Elegir un medico para " + nombre + " " + apellido);
                for (Medico m : medicos) {
                    System.out.println("[" + i + "]" + " Dr. " + m.getNombre() + " " + m.getApellido());
                    i++;
                }
                System.out.println("Ingrese el nro del medico que desea asignar");
                rta = medicos.get(scan.nextInt() - 1).getId();
                flag = 1;
            } catch (IndexOutOfBoundsException e) {
                System.out.println(Colores.rojo() + "Ingresaste una opcion incorrecta, intentalo nuevamente" + Colores.blanco());
            } catch (InputMismatchException i) {
                System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                scan.nextLine();
            }
        }
        return rta;
    }

    public void agregarEnfermedad() {
        ArrayList<Enfermedad> enfermedades = Persistencia.deserializacion("enfermedades.json", Enfermedad.class);
        mostrarEnfermedadesArchivo();

        System.out.println("Ingrese nueva enfermedad : ");
        Scanner scan = new Scanner(System.in);
        String nombreValido = nombreRepetidoEnfermedad(scan.nextLine());

        enfermedades.add(new Enfermedad(nombreValido));
        Persistencia.serializacion(enfermedades, "enfermedades.json");
    }

    @Override
    public Tratamiento crearTratamiento() { //falta ver si el tratamiento ya existe

        Scanner scan = new Scanner(System.in);
        ArrayList<Enfermedad> enfermedades = Persistencia.deserializacion("enfermedades.json", Enfermedad.class);
        ArrayList<Accion> listaAcciones = Persistencia.deserializacionAcciones();


        // nombre enfermedad para tratamiento (2 opciones : la crea o la elige)
        Enfermedad enfermedad = null;
        int opcion = 0;
        do {
            System.out.println("[1] Elegir enfermedad existente");
            System.out.println("[2] Crear nueva enfermedad");
            System.out.println("Ingrese una opcion:");

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

                    int flag = 0;
                    while (flag == 0) {
                        try {
                            mostrarEnfermedadesArchivo();
                            System.out.println("Ingrese el numero de la enfermedad: ");
                            int indice = scan.nextInt();
                            enfermedad = enfermedades.get(indice - 1);  // falta validar indice
                            System.out.println("Se eligio la enfermedad : " + enfermedad.getNombre());
                            opcion = 0;
                            flag = 1;
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println(Colores.rojo() + "Ingresaste una opcion incorrecta, intentalo nuevamente" + Colores.blanco());
                        } catch (InputMismatchException i) {
                            System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                            scan.nextLine();
                        }
                    }


                    break;
                }
                case 2:
                    System.out.println("Creando una nueva enfermedad, ingrese el nombre: ");
                    scan.nextLine();
                    String nombreValido = nombreRepetidoEnfermedad(scan.nextLine());
                    Enfermedad nuevaEnfermedad = new Enfermedad(nombreValido);
                    enfermedades.add(nuevaEnfermedad);
                    System.out.println(Colores.verde() + "Se agrego la enfermedad " + nuevaEnfermedad.getNombre() + " al archivo de enfermedades" + Colores.blanco());
                    Persistencia.serializacion(enfermedades, "enfermedades.json");
                    enfermedad = nuevaEnfermedad;
                    opcion = 0;
                    break;
            }
        } while (opcion != 0);

        Tratamiento nuevoTratamiento = new Tratamiento();

        System.out.println("Ingrese la duracion que desea que tenga el tratamiento: ");
        nuevoTratamiento.setDuracion(scan.nextInt());
        nuevoTratamiento.setEnfermedad(enfermedad);


        System.out.println("Ingrese el numero de acciones que tendra el tratamiento: ");
        int aux = scan.nextInt();
        int flag;
        int accionIndex;

        for (int i = 0; i < aux; i++) {
            flag = 0;
            while (flag == 0) {
                try {
                    int index = 1;
                    for (Accion a : listaAcciones) {
                        System.out.println("[" + index + "] " + a.mostrarAccion()); // mostrar mejor el a
                        index++;

                    }

                    System.out.println("Elija el numero de de la accion que desea para el tratamiento: ");
                    accionIndex = scan.nextInt() - 1;
                    if (listaAcciones.get(accionIndex) instanceof AccionBooleana) {
                        AccionBooleana accionAux = (AccionBooleana) listaAcciones.get(accionIndex).clonarAccion();
                        System.out.println("Ingrese cada cuandos dias quiere que se realice la accion, encaso de ser todos los dias, ingrese 1:");
                        accionAux.setCadaCuanto(scan.nextInt());
                        nuevoTratamiento.listaAcciones.add(accionAux);
                    } else {
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


        ArrayList<Tratamiento> listaTratamientos = Persistencia.deserializacionTratamientos();
        listaTratamientos.add(nuevoTratamiento);
        Persistencia.serializacionTratamientos(listaTratamientos);

        System.out.println(Colores.verde() + "El tratamiento fue creado con exito." + Colores.blanco());

        return nuevoTratamiento;

    }

    @Override
    public Tratamiento editarTratamiento(Tratamiento aux) {
        Scanner scan = new Scanner(System.in);
        // levantar archivo Tratamientos
        ArrayList<Tratamiento> listaTratamientosGenericos = Persistencia.deserializacionTratamientos();

        // seleccionar el tratamiento a editar
        int flag = 0;
        while (flag == 0) {
            try {
                int x = 1;
                for (Tratamiento a : listaTratamientosGenericos) {
                    System.out.println("[" + x + "] " + a.mostrarTratamientoString());
                    x++;
                }
                System.out.println("Ingrese el numero del tratamiento que desea modificar: ");
                int y = scan.nextInt() - 1;
                aux = listaTratamientosGenericos.get(y).clonarTratamiento();

                //editar tratamiento
                Medico medicoAux = new Medico();
                medicoAux.editarTratamiento(aux);

                listaTratamientosGenericos.remove(y);
                listaTratamientosGenericos.add(aux);

                //persistir
                Persistencia.serializacionTratamientos(listaTratamientosGenericos);
                flag = 1;
            } catch (IndexOutOfBoundsException e) {
                System.out.println(Colores.rojo() + "Ingresaste una opcion incorrecta, intentalo nuevamente" + Colores.blanco());
            } catch (InputMismatchException i) {
                System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                scan.nextLine();
            }
        }
        return aux;
    }

    public void crearAccion() {
        Scanner scan = new Scanner(System.in);
        ArrayList<Accion> acciones = Persistencia.deserializacionAcciones();
        int opcion = 0;

        System.out.println("Creando una nueva accion...");

        System.out.println("Ingrese el nombre de la accion: ");
        String nombre = scan.nextLine();
        if (!verificarAccionRepetida(acciones, nombre)) {
            System.out.println("Ingrese el texto de pregunta para la accion: ");
            String pregunta = scan.nextLine();
            int flagCadaCuanto = 0;
            int cadaCuanto = 0;
            while (flagCadaCuanto == 0) {
                try {
                    System.out.println("Ingrese cada cuantos dias debe realizar la accion: ");
                    cadaCuanto = scan.nextInt();
                    flagCadaCuanto = 1;
                } catch (InputMismatchException e) {
                    System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                    scan.nextLine();
                }
            }
            while (opcion != 3) {
                System.out.println("Que tipo de accion es?");
                System.out.println("[1] Booleana");
                System.out.println("[2] Double");
                System.out.println("[3] Cancelar");
                System.out.println("Ingrese una opcion:");

                int flagSwitch = 0;
                while (flagSwitch == 0) {
                    try {
                        opcion = scan.nextInt();
                        flagSwitch = 1;
                    } catch (InputMismatchException e) {
                        System.out.println(Colores.rojo() + "Ingresaste un tipo de dato incorrecto, intentalo nuevamente" + Colores.blanco());
                        System.out.println("Ingrese una opcion:");
                        scan.nextLine();
                    }
                }

                switch (opcion) {
                    case 1:
                        AccionBooleana accionBooleana = new AccionBooleana(cadaCuanto, nombre, pregunta);
                        acciones.add(accionBooleana);
                        Persistencia.serializacionAcciones(acciones);
                        System.out.println(Colores.verde() + "La accion fue creada con exito!" + Colores.blanco());
                        opcion = 3;
                        break;

                    case 2:
                        AccionDouble accionDouble = new AccionDouble(cadaCuanto, nombre, pregunta);
                        acciones.add(accionDouble);
                        Persistencia.serializacionAcciones(acciones);
                        System.out.println(Colores.verde() + "La accion fue creada con exito!" + Colores.blanco());
                        opcion = 3;
                        break;

                    case 3:
                        System.out.println(Colores.amarillo() + "Se cancelo la creacion de la accion" + Colores.blanco());
                        break;

                    default:
                        System.out.println(Colores.rojo() + "Opcion incorrecta, ingrese otra" + Colores.blanco());
                }
            }
        } else {
            System.out.println(Colores.amarillo() + "Esa accion ya existe, volviendo al menu de admin..." + Colores.blanco());
        }
    }

    public boolean verificarAccionRepetida(ArrayList<Accion> acciones, String nombre) {
        for (Accion a : acciones) {
            if (a.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    public String nombreRepetidoEnfermedad(String nuevoNombre) {
        ArrayList<Enfermedad> enfermedades = Persistencia.deserializacion("enfermedades.json", Enfermedad.class);
        Scanner scan = new Scanner(System.in);
        for (Enfermedad e : enfermedades) {
            while (nuevoNombre.equalsIgnoreCase(e.getNombre())) {
                System.out.println(Colores.amarillo() + "Enfermedad ya cargada, ingrese otra" + Colores.blanco());
                nuevoNombre = scan.nextLine();
            }
        }
        return nuevoNombre;
    }

    public void mostrarEnfermedadesArchivo() {
        Integer contador = 1;
        ArrayList<Enfermedad> enfermedades = Persistencia.deserializacion("enfermedades.json", Enfermedad.class);
        System.out.println("Enfermedades persistidas : ");
        for (Enfermedad e : enfermedades) {
            System.out.print("[" + contador + "] ");
            System.out.println(e.mostrarEnfermedad());
            contador++;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
