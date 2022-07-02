import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public abstract class Persistencia {
    // Metodos genericos
    // lectura
    public static <T> ArrayList<T> deserializacion(String archivo, Class<T> type) {
        ArrayList<T> rta = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(archivo));
            rta = gson.fromJson(reader, TypeToken.getParameterized(ArrayList.class, type).getType());
            reader.close();
        } catch (IOException e) {
            System.out.println(Colores.rojo() + e + Colores.blanco());
        }
        return rta;
    }

    // escritura
    public static <T> void serializacion(ArrayList<T> list, String archivo) {
        try {
            Writer writer = new FileWriter(archivo);
            new Gson().toJson(list, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println(Colores.rojo() + e + Colores.blanco());
        }
    }

    // Clase Pacientes
    // lectura
    public static ArrayList<Paciente> deserializacionPacientes() {

        GsonBuilder gsonBilder = new GsonBuilder();
        gsonBilder.registerTypeAdapter(Accion.class, new AbstractAccionAdapter());
        Gson gson = gsonBilder.create();
        ArrayList<Paciente> listaPacientes = new ArrayList<>();

        File file = new File("pacientes.json");
        try {
            Type listType = new TypeToken<ArrayList<Paciente>>() {
            }.getType();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            listaPacientes = gson.fromJson(bufferedReader, listType);
            return listaPacientes;
        } catch (IOException e) {
            System.out.println(Colores.rojo() + "No se pudo leer/escribir el archivo: " + e.getMessage() + Colores.blanco());
            return null;
        }
    }

    // escritura
    public static <T> void serializacionPacientes(ArrayList<Paciente> arrayPaciente) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Accion.class, new AbstractAccionAdapter());
        Gson gson = gsonBuilder.create();

        File file = new File("pacientes.json");
        try {
            Type listType = new TypeToken<ArrayList<Paciente>>() {
            }.getType();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            gson.toJson(arrayPaciente, listType, bufferedWriter);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println(Colores.rojo() + "No se pudo leer/escribir el archivo: " + e.getMessage() + Colores.blanco());
        }
    }

    // Clase Accion
    // lectura
    public static ArrayList<Accion> deserializacionAcciones() {

        GsonBuilder gsonBilder = new GsonBuilder();
        gsonBilder.registerTypeAdapter(Accion.class, new AbstractAccionAdapter());
        Gson gson = gsonBilder.create();
        ArrayList<Accion> listaAcciones = new ArrayList<>();

        File file = new File("acciones.json");
        try {
            Type listType = new TypeToken<ArrayList<Accion>>() {
            }.getType();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            listaAcciones = gson.fromJson(bufferedReader, listType);
            return listaAcciones;
        } catch (IOException e) {
            System.out.println(Colores.rojo() + "No se pudo leer/escribir el archivo: " + e.getMessage() + Colores.blanco());
            return null;
        }
    }

    // escritura
    public static <T> void serializacionAcciones(ArrayList<Accion> arrayAcciones) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Accion.class, new AbstractAccionAdapter());
        Gson gson = gsonBuilder.create();

        File file = new File("acciones.json");
        try {
            Type listType = new TypeToken<ArrayList<Accion>>() {
            }.getType();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            gson.toJson(arrayAcciones, listType, bufferedWriter);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println(Colores.rojo() + "No se pudo leer/escribir el archivo: " + e.getMessage() + Colores.blanco());
        }
    }

    // Clase Accion
    // lectura
    public static ArrayList<Tratamiento> deserializacionTratamientos() {
        GsonBuilder gsonBilder = new GsonBuilder();
        gsonBilder.registerTypeAdapter(Accion.class, new AbstractAccionAdapter());
        Gson gson = gsonBilder.create();
        ArrayList<Tratamiento> listaTratamientos = new ArrayList<>();

        File file = new File("tratamientos.json");
        try {
            Type listType = new TypeToken<ArrayList<Tratamiento>>() {
            }.getType();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            listaTratamientos = gson.fromJson(bufferedReader, listType);
            return listaTratamientos;
        } catch (IOException e) {
            System.out.println(Colores.rojo() + "No se pudo leer/escribir el archivo: " + e.getMessage() + Colores.blanco());
            return null;
        }
    }

    // escritura
    public static <T> void serializacionTratamientos(ArrayList<Tratamiento> arrayTratamientos) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Accion.class, new AbstractAccionAdapter());
        Gson gson = gsonBuilder.create();

        File file = new File("tratamientos.json");
        try {
            Type listType = new TypeToken<ArrayList<Tratamiento>>() {
            }.getType();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            gson.toJson(arrayTratamientos, listType, bufferedWriter);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println(Colores.rojo() + "No se pudo leer/escribir el archivo: " + e.getMessage() + Colores.blanco());
        }
    }
}