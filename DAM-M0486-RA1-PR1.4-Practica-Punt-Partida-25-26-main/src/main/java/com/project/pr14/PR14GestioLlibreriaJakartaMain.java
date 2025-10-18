package com.project.pr14;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.project.objectes.Llibre;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;


/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de llibres.
 */
public class PR14GestioLlibreriaJakartaMain {

    private final File dataFile;

    /**
     * Constructor de la classe PR14GestioLlibreriaJSONPMain.
     *
     * @param dataFile Fitxer on es troben els llibres.
     */
    public PR14GestioLlibreriaJakartaMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        File dataFile = new File(System.getProperty("user.dir"), "data/pr14" + File.separator + "llibres_input.json");
        PR14GestioLlibreriaJakartaMain app = new PR14GestioLlibreriaJakartaMain(dataFile);
        app.processarFitxer();
    }

    /**
     * Processa el fitxer JSON per carregar, modificar, afegir, esborrar i guardar les dades dels llibres.
     */
    public void processarFitxer() {
        List<Llibre> llibres = carregarLlibres();
        if (llibres != null) {
            modificarAnyPublicacio(llibres, 1, 1995);
            afegirNouLlibre(llibres, new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022));
            esborrarLlibre(llibres, 2);
            guardarLlibres(llibres);
        }
    }

    /**
     * Carrega els llibres des del fitxer JSON.
     *
     * @return Llista de llibres o null si hi ha hagut un error en la lectura.
     */
    public List<Llibre> carregarLlibres() {
        List<Llibre> llibres = new ArrayList<>();
        try (JsonReader jsonReader = Json.createReader(new FileReader(dataFile))) {
            JsonArray jsonArray = jsonReader.readArray();
            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                int id = jsonObject.getInt("id");
                String titol = jsonObject.getString("titol");
                String autor = jsonObject.getString("autor");
                int any = jsonObject.getInt("any");
                llibres.add(new Llibre(id, titol, autor, any));
            }
        } catch (IOException e) {
            System.out.println("Archivo no encontrado " + e.getMessage());
            return null;
        }
        return llibres;
    }

    /**
     * Modifica l'any de publicació d'un llibre amb un id específic.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a modificar.
     * @param nouAny Nou any de publicació.
     */
    public void modificarAnyPublicacio(List<Llibre> llibres, int id, int nouAny) {
      if (llibres == null || llibres.isEmpty()) return;
        boolean trobat = false;
        for (Llibre llibre : llibres) {
            if (llibre.getId() == id) {
                llibre.setAny(nouAny);
                trobat = true;
                break;
            }
        }
        if (!trobat) {
            System.out.println("Libro con id inexitente: " + id);
        } 
    }

    /**
     * Afegeix un nou llibre a la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param nouLlibre Nou llibre a afegir.
     */
    public void afegirNouLlibre(List<Llibre> llibres, Llibre nouLlibre) {
        if (llibres == null || nouLlibre == null) return;

        for (Llibre llibre : llibres) {
            if (llibre.getId() == nouLlibre.getId()) {
                System.out.println("Libro esitente con el mismo id: " + nouLlibre.getId());
                return;
            }
        }
        llibres.add(nouLlibre);
    }

    /**
     * Esborra un llibre amb un id específic de la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a esborrar.
     */
    public void esborrarLlibre(List<Llibre> llibres, int id) {
        if (llibres == null || llibres.isEmpty()) return;

        Iterator<Llibre> iterator = llibres.iterator();
        boolean trobat = false;
        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
                iterator.remove();
                trobat = true;
                break;
            }
        }
        if (!trobat) {
            System.out.println("Libro con id " + id + " inexitente.");
        }
    }

    /**
     * Guarda la llista de llibres en un fitxer nou.
     *
     * @param llibres Llista de llibres a guardar.
     */
    public void guardarLlibres(List<Llibre> llibres) {
        if (llibres == null || llibres.isEmpty()) {
            System.out.println("Lista de libros vacia, no se puede puede guardar nada.");
            return;
        }

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Llibre llibre : llibres) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                .add("id", llibre.getId())
                .add("titol", llibre.getTitol())
                .add("autor", llibre.getAutor())
                .add("any", llibre.getAny());
            arrayBuilder.add(objectBuilder);
        }

        File outputFile = new File(System.getProperty("user.dir"), "data/pr14" + File.separator + "llibres_output_jakarta.json");
        try (JsonWriter jsonWriter = Json.createWriter(new FileWriter(outputFile))) {
            jsonWriter.writeArray(arrayBuilder.build());
            System.out.println("Libro guardado exitosamente!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }    }
}