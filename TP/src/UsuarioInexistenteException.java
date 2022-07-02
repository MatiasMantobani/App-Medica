public class UsuarioInexistenteException extends Exception {
    public UsuarioInexistenteException() {
        System.out.println(Colores.amarillo() + "No existe el usuario, creando uno nuevo..." + Colores.blanco());
    }
}
