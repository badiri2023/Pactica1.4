package com.project.pr14;


import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.objectes.Llibre;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de llibres.
 */
public class PR14GestioLlibreriaJacksonMain {

    private final File dataFile;

    /**
     * Constructor de la classe PR14GestioLlibreriaMain.
     *
     * @param dataFile Fitxer on es troben els llibres.
     */
    public PR14GestioLlibreriaJacksonMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        File dataFile = new File(System.getProperty("user.dir"), "data/pr14" + File.separator + "llibres_input.json");
        PR14GestioLlibreriaJacksonMain app = new PR14GestioLlibreriaJacksonMain(dataFile);
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
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Llibre> llibres = mapper.readValue(dataFile, new TypeReference<List<Llibre>>() {});

            // Validació manual
            Iterator<Llibre> iterator = llibres.iterator();
            while (iterator.hasNext()) {
                Llibre llibre = iterator.next();
                if (llibre.getId() <= 0 || llibre.getTitol() == null || llibre.getAutor() == null || llibre.getAny() <= 0) {
                    System.out.println("Llibre invàlid detectat i eliminat: " + llibre);
                    iterator.remove();
                }
            }

            return llibres;
        } catch (IOException e) {
            System.out.println("Error llegint el fitxer: " + e.getMessage());
            return null;
        }
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
            System.out.println("ID inexistente: " + id);
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
                System.out.println("Libre ya existente con id: " + nouLlibre.getId());
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
            System.out.println("Libro con id: " + id + " inexistente!");
        }  
    }

    /**
     * Guarda la llista de llibres en un fitxer nou.
     *
     * @param llibres Llista de llibres a guardar.
     */
    public void guardarLlibres(List<Llibre> llibres) {
        if (llibres == null || llibres.isEmpty()) {
            System.out.println("No hi ha llibres per guardar.");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        File outputFile = new File(dataFile.getParent(), "llibres_output_jackson.json");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, llibres);
            System.out.println("Archivo guardado exitosamente ");
        } catch (IOException e) {
            System.out.println("Error de guardado: "+e.getMessage());
        } 
    }
}
