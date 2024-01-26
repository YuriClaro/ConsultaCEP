package program;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.AlfanumericoException;
import exceptions.ErroConsultaCep;
import exceptions.OitoCaracteresException;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitura = new Scanner(System.in);
        String cep = "";
        List<String> listas = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        System.out.println("\nBem-Vindo ao ConsultaCEP API no Java 17!"
                + "\nOs CEP devem ter:"
                + "\n- 8 numeros inteiros,"
                + "\n- Não podem conter espaços,"
                + "\n- Não podem conter letras,"
                + "\n- Não podem conter caractéres especiais.\n");

        while (!cep.equalsIgnoreCase("sair")) {
            System.out.print("--COMANDOS-- \n"
                    + "\"SAIR\": para sair da aplicação,\n"
                    + "\"LISTA\": para baixar uma lista dos CEP já consultados, ou\n"
                    + "Digite um novo CEP para busca: ");

            cep = leitura.nextLine();

            if (cep.equalsIgnoreCase("sair")) {
                System.out.println("Finalizando programa...");
                break;
            }

            if (cep.equalsIgnoreCase("lista")) {
                FileWriter escrita = new FileWriter("lista.json");
                escrita.write(gson.toJson(listas));
                System.out.println("Criando uma lista...");
                escrita.close();
                break;
            }

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
                } else if (response.statusCode() == 400) {
                    throw new ErroConsultaCep("Não foi possivel encontrar o CEP.");
                } else {
                    System.out.println(json);
                }
                listas.add(json);

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
