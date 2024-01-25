package program;

import exceptions.AlfanumericoException;
import exceptions.ErroConsultaCep;
import exceptions.OitoCaracteresException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitura = new Scanner(System.in);
        String cep = "";

        while (!cep.equalsIgnoreCase("sair")){
            System.out.print("Digite um CEP, para busca: ");
            cep = leitura.nextLine();

            String enderecoCepApi = "https://viacep.com.br/ws/" + cep + "/json/";

            try {
                ValidarCep(cep);
                System.out.println("Validando o CEP, aguarde...");

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(enderecoCepApi)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String json = (String) response.body();

                if (json.contains("\"erro\": true")) {
                    System.out.println("CEP não encontrado.");
                } else if (response.statusCode() == 400){
                    throw new ErroConsultaCep("Não foi possivel encontrar o CEP.");
                } else {
                    System.out.println(json);
                }

            } catch (OitoCaracteresException | AlfanumericoException | ErroConsultaCep e) {
                System.out.println(e.getMessage());
            }
        }

        leitura.close();
    }

    private static void ValidarCep(String cep) {
        if (cep.length() > 8) {
            throw new OitoCaracteresException("CEP inválido: Insira um CEP de 8 numeros inteiros");
        }
        if (!cep.matches("[0-9]+")) {
            throw new AlfanumericoException("CEP inválido: Insira um CEP de 8 numeros inteiros");
        }
    }

}
