public class DniInexistenteException extends Exception {
    public DniInexistenteException() {
        System.out.println(Colores.amarillo() + "EL dni que ingresaste no pertenece a ningun usuario registrado" + Colores.blanco());
    }

}
