public class CredencialesIncorrectasException extends Exception {
    public CredencialesIncorrectasException() {
        System.out.println(Colores.amarillo() + "Credenciales incorrectas, vuelva a intentarlo..." + Colores.blanco());
    }

    public CredencialesIncorrectasException(String mail) {
        System.out.println(Colores.rojo() + "Contraseña Incorrecta" + Colores.blanco());
    }

}
