public class AccionFallidaException extends Exception {
    public AccionFallidaException() {
        System.out.println(Colores.amarillo() + "La accion no se llevo a cabo" + Colores.blanco());
    }
}
