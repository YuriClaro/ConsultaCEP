package program;

import exceptions.ErroConsultaCep;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitura = new Scanner(System.in);
        System.out.print("Digite um CEP, para busca: ");
        String cep = leitura.nextLine();

        String enderecoCepApi = "https://viacep.com.br/ws/" + cep + "/json/";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(enderecoCepApi)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = (String)response.body();
            System.out.println(json);

            if (response.statusCode() == 400) {
                throw new ErroConsultaCep("CEP inválido, insira um cep de 8 digitos.");
            }
        } catch (ErroConsultaCep erroConsultaCep) {
            System.out.println(erroConsultaCep.getMessage());
        }

        leitura.close();
    }
}
