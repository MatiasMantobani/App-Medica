public class EliminarRegistroException extends Exception {
    public EliminarRegistroException(String accion) {
        System.out.println(Colores.amarillo() + "El registro de la accion " + accion + " fue eliminado" + Colores.blanco());
    }
}
