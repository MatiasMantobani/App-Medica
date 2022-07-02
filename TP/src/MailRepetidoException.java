public class MailRepetidoException extends Exception {
    public MailRepetidoException() {
        System.out.println(Colores.amarillo() + "Ese mail ya se encuentra en uso, intenta con otra direccion de email" + Colores.blanco());
    }

    public MailRepetidoException(String mail) {
        System.out.println(Colores.amarillo() + "El mail debe ser de dominio '@hotmail.com', '@outlook.com' o '@gmail.com'" + Colores.blanco());
    }
}