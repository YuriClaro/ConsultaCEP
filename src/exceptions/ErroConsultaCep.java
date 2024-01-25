package exceptions;

public class ErroConsultaCep extends RuntimeException {
    public ErroConsultaCep(String mensagem) {
        super(mensagem);
    }
}
