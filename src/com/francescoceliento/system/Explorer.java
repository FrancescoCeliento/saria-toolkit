package com.francescoceliento.system;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Explorer {
	
	// 1. Enum per la modalità di ricerca (case-sensitive o case-insensitive)
	public enum TypeSearch {
	    CASESENSITIVE,
	    CASEINSENSITIVE
	}

	// 2. Enum per la logica di inclusione (esattamente, contiene, esclude)
	public enum Include {
	    EXACTLY,    // Il nome del file deve corrispondere esattamente a 'search'
	    CONTAINS,   // Il nome del file deve contenere 'search'
	    EXCLUDE     // Il nome del file NON deve contenere 'search'
	}
	
	/**
     * Verifica l'esistenza di un file che soddisfi i criteri specificati.
     *
     * @param dir        La cartella di partenza per la ricerca.
     * @param search     Il testo da cercare nel nome del file.
     * @param ext        L'estensione del file (es. "txt", "pdf", o "*"). Deve essere gestita in lowercase.
     * @param typeSearch Enum che definisce se la ricerca è case-sensitive o case-insensitive.
     * @param include    Enum che definisce se cercare corrispondenza esatta, se contenere, o se escludere la stringa.
     * @param subFolder  Se true, la ricerca è ricorsiva in tutte le sottocartelle.
     * @return           true se viene trovato almeno un file che soddisfa i criteri, altrimenti false.
     */
    public static boolean fileExist(String dir, String search, String ext, TypeSearch typeSearch, Include include, boolean subFolder) {
        // 1. Validazione di base
        if (dir == null || dir.trim().isEmpty()) {
            return false;
        }

        final Path startPath = Paths.get(dir);
        if (!Files.isDirectory(startPath)) {
            System.err.println("Errore: Il percorso specificato non è una directory valida: " + dir);
            return false;
        }

        // 2. Preparazione della Chiave di Ricerca (Gestione Case)
        final String searchKey;
        if (typeSearch == TypeSearch.CASEINSENSITIVE) {
            searchKey = search.toLowerCase();
        } else {
            searchKey = search;
        }

        // 3. Preparazione dell'Estensione (Gestione Wildcard e Lowercase)
        final String normalizedExt;
        final boolean searchAnyExt = (ext == null || ext.trim().isEmpty() || ext.trim().equals("*"));
        
        if (searchAnyExt) {
            normalizedExt = "";
        } else {
            String trimmedExt = ext.trim().toLowerCase();
            // Assicura che l'estensione inizi con un punto per un confronto coerente
            normalizedExt = trimmedExt.startsWith(".") ? trimmedExt : "." + trimmedExt;
        }

        // 4. Impostazione della Profondità (Ricorsività)
        final int maxDepth = subFolder ? Integer.MAX_VALUE : 1;

        // 5. Esecuzione della Ricerca
        try (Stream<Path> stream = Files.walk(startPath, maxDepth)) {
            return stream
                .filter(Files::isRegularFile) // Filtra solo i file regolari
                .anyMatch(path -> {
                    String fileName = path.getFileName().toString();
                    
                    // Prepara il nome del file per il confronto in base al TypeSearch
                    final String fileToCompare;
                    if (typeSearch == TypeSearch.CASEINSENSITIVE) {
                        fileToCompare = fileName.toLowerCase();
                    } else {
                        fileToCompare = fileName;
                    }
                    
                    // --- 5a. Verifica dell'Estensione ---
                    if (!searchAnyExt) {
                        // Se non è il wildcard '*', l'estensione deve corrispondere (gestita in lowercase)
                        if (!fileToCompare.toLowerCase().endsWith(normalizedExt)) {
                            return false;
                        }
                    }

                    // --- 5b. Verifica del Nome in base al parametro Include ---
                    boolean match = false;
                    
                    switch (include) {
                        case EXACTLY:
                            // La logica EXACTLY deve confrontare searchKey + normalizedExt (se ext non è '*')
                            String expectedName = searchKey + (searchAnyExt ? "" : normalizedExt);
                            match = fileToCompare.equals(expectedName);
                            break;
                            
                        case CONTAINS:
                            match = fileToCompare.contains(searchKey);
                            break;
                            
                        case EXCLUDE:
                            // Troviamo un file se NON contiene la chiave di ricerca
                            // Quindi, il match è true se la chiave NON è contenuta
                            match = !fileToCompare.contains(searchKey);
                            break;
                    }
                    
                    return match;
                });

        } catch (IOException e) {
            System.err.println("Si è verificato un errore durante l'attraversamento delle directory: " + e.getMessage());
            return false;
        }
    }
	
}
