package com.francescoceliento.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class FileDownloader {

    private static final int BUFFER_SIZE = 4096;

    /**
     * Scarica un file da un URL e lo salva in una directory locale,
     * mantenendo il nome del file originale.
     *
     * @param source L'URL del file da scaricare (es. "https://example.com/file.txt").
     * @param localDir La directory locale dove salvare il file (es. "/path/alla/directory").
     * @return Il percorso assoluto del file scaricato localmente.
     * @throws IOException Se si verifica un errore di I/O (connessione, lettura/scrittura, ecc.).
     * @throws IllegalArgumentException Se l'URL o la directory locale non sono validi.
     */
    public static String download(String source, String localDir) throws IOException, IllegalArgumentException {
        // 1. Validazione e determinazione del nome del file
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("L'URL sorgente non può essere vuoto.");
        }
        if (localDir == null || localDir.trim().isEmpty()) {
            throw new IllegalArgumentException("La directory locale non può essere vuota.");
        }

        URL url = new URL(source);
        String fileName = getFileNameFromUrl(url);

        if (fileName.isEmpty()) {
             // Tenta un nome predefinito se l'URL è privo di nome file (es. finisce con '/')
            fileName = "downloaded_file";
            System.out.println("Attenzione: Impossibile determinare il nome del file dall'URL. Usando il nome predefinito: " + fileName);
        }

        return downloadAndRename(source, localDir, fileName);
    }

    // Metodo helper per estrarre il nome del file dall'URL
    private static String getFileNameFromUrl(URL url) {
        String path = url.getPath();
        // Trova l'ultima occorrenza di '/' per estrarre il nome
        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex >= 0) {
            String fileName = path.substring(lastSlashIndex + 1);
            // Rimuove eventuali parametri query (dopo '?')
            int queryIndex = fileName.indexOf('?');
            if (queryIndex > 0) {
                fileName = fileName.substring(0, queryIndex);
            }
            return fileName.isEmpty() ? "" : fileName;
        }
        return "";
    }
// ----------------------------------------------------------------------------------------------------------------------
    /**
     * Scarica un file da un URL e lo salva in una directory locale,
     * rinominandolo con il nome specificato.
     *
     * @param source L'URL del file da scaricare (es. "https://example.com/file.txt").
     * @param localDir La directory locale dove salvare il file (es. "/path/alla/directory").
     * @param name Il nome da assegnare al file scaricato (es. "nuovo_nome.pdf").
     * @return Il percorso assoluto del file scaricato localmente.
     * @throws IOException Se si verifica un errore di I/O (connessione, lettura/scrittura, ecc.).
     * @throws IllegalArgumentException Se l'URL, la directory locale o il nome del file non sono validi.
     */
    public static String download(String source, String localDir, String name) throws IOException, IllegalArgumentException {
        // 1. Validazione
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del file non può essere vuoto.");
        }

        return downloadAndRename(source, localDir, name);
    }

    // Metodo privato comune per eseguire l'effettivo download
    private static String downloadAndRename(String source, String localDir, String finalName) throws IOException {
        // 1. Preparazione dei percorsi e stream
        File localDirectory = new File(localDir);
        if (!localDirectory.exists()) {
            // Tenta di creare la directory se non esiste
            if (!localDirectory.mkdirs()) {
                throw new IOException("Impossibile creare la directory locale: " + localDir);
            }
        } else if (!localDirectory.isDirectory()) {
            throw new IOException("Il percorso locale esiste ma non è una directory: " + localDir);
        }

        File destinationFile = new File(localDirectory, finalName);
        URL url = new URL(source);

        // 2. Download effettivo
        try (InputStream is = url.openStream();
             OutputStream os = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            // Legge dal server e scrive sul file locale
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            return destinationFile.getAbsolutePath();

        } catch (IOException e) {
            // Pulizia: se il download fallisce, elimina il file parziale
            if (destinationFile.exists() && destinationFile.length() > 0) {
                destinationFile.delete();
            }
            throw new IOException("Errore durante il download del file da " + source + ": " + e.getMessage(), e);
        }
    }

}
