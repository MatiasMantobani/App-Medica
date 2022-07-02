public class DniIncorrectoException extends Exception {
    public DniIncorrectoException() {
        System.out.println(Colores.amarillo() + "El dni solo puede contener numeros (NO LETRAS NI PUNTOS)" + Colores.blanco());
    }
}
