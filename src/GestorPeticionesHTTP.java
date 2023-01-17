import javax.swing.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class GestorPeticionesHTTP
{
    public int almacenarPagina (String esquema, String servidor, String recurso, String path) throws Exception {

        recurso = URLEncoder.encode(recurso, StandardCharsets.UTF_8);
        String direccion = esquema + servidor + recurso;
        HttpClient httpClient = HttpClient.
                                 newBuilder()
                                 .version(HttpClient.Version.HTTP_1_1)
                                 .followRedirects(HttpClient.Redirect.NORMAL)
                                 .build();

        HttpRequest request = HttpRequest.newBuilder()
                                         .GET()
                                         .uri(URI.create(direccion))
                                         .headers("Content-Type","text/plain")
                                         .setHeader("User-Agent","Mozilla/5.0")
                                         .build();

        HttpResponse<Path> response = httpClient.send(request,HttpResponse.BodyHandlers.ofFile(Path.of(path)));
        return response.statusCode();

    }

    public static void main(String[] args) {
        String tituloPelicula = JOptionPane.showInputDialog("Introduzca el nombre de la película a buscar");
        String esquema = "http://";
        String servidor = "www.omdbapi.com/?apikey=cb43d735&t=";
        String recurso = tituloPelicula.replace(" ","+");


        GestorPeticionesHTTP gestorPeticionesHTTP = new GestorPeticionesHTTP();
        try {
            int codigoEstado = gestorPeticionesHTTP.almacenarPagina(esquema,servidor,recurso,"respuestas/" + recurso + ".json");

            if (codigoEstado == HttpURLConnection.HTTP_OK){
                System.out.println("Descarga finalizada");
            }else {
                System.out.println("Error " + codigoEstado);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
