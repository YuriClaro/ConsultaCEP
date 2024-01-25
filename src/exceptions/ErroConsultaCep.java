package exceptions;

public class ErroConsultaCep extends RuntimeException{
    private String message;

    public ErroConsultaCep(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
