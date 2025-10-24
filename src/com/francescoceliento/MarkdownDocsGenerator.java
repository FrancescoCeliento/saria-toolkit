package com.francescoceliento;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Genera documentazione Markdown analizzando i file .java
 * nella cartella 'src' senza dipendenze esterne.
 * Usa metodi di I/O compatibili con Java 8.
 */
public class MarkdownDocsGenerator {

    private static final String SOURCE_DIR = "src";
    private static final String OUTPUT_FILE = "DOCS.MD";
    private static final String ROOT_TITLE = "# DOCS - "+ Saria.getName() + " " + Saria.getVersion() + "\n";

    // Pattern per trovare il Javadoc che precede una dichiarazione di classe pubblica
    private static final Pattern CLASS_PATTERN = Pattern.compile(
            "/\\*\\*(.*?)\\*/\\s*public\\s+class\\s+(\\w+)",
            Pattern.DOTALL
    );
    
    // Pattern per trovare un metodo con il suo Javadoc precedente.
    // Cerca: un commento Javadoc /** ... */, seguito da una dichiarazione di metodo pubblica.
    private static final Pattern METHOD_PATTERN = Pattern.compile(
    	    "/\\*\\*((?:[^*]|\\*(?!/))*)\\*/\\s*" +          // Javadoc
    	    "(?:public|protected|private)\\s+" +             // Qualsiasi visibilità
    	    "(?:(?:static|final|abstract)\\s+)*" +           // Modificatori opzionali
    	    "[\\w<>\\[\\]]+\\s+" +                          // Tipo di ritorno
    	    "(\\w+)\\s*\\([^)]*\\)\\s*" +                   // Nome del metodo + parametri
    	    "(?:\\{?|;)",                                   // Apertura graffa o punto e virgola
    	    Pattern.DOTALL
    	);
    
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
    		//Lista delle cartelle da escludere
            "com/francescoceliento"
    );

    public static void main(String[] args) {
        Path projectRoot = Paths.get("").toAbsolutePath();
        Path sourcePath = projectRoot.resolve(SOURCE_DIR);
        Path outputPath = projectRoot.resolve(OUTPUT_FILE);

        if (!Files.exists(sourcePath)) {
            System.err.println("Errore: La cartella sorgente '" + SOURCE_DIR + "' non è stata trovata in " + projectRoot);
            return;
        }

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputPath))) {
            writer.write(ROOT_TITLE);
            System.out.println("Inizio generazione documentazione (versione Java 8 compatibile)...");

            // 1. Trova tutte le cartelle finali (i package)
            findPackageDirectories(sourcePath).forEach(packageDir -> {
                try {
                    String packageName = packageDir.getFileName().toString();
                    String packageTitle = "## " + packageName.toUpperCase() + "\n";
                    writer.write(packageTitle);
                    System.out.println("  Processing package: " + packageName.toUpperCase());

                    // 2. Trova tutti i file .java in questa cartella
                    Files.list(packageDir)
                            .filter(path -> path.toString().endsWith(".java"))
                            .sorted()
                            .forEach(javaFile -> processJavaFile(javaFile, writer));

                } catch (IOException e) {
                    System.err.println("Errore durante l'elaborazione del package " + packageDir + ": " + e.getMessage());
                }
            });

            System.out.println("\nDocumentazione generata con successo in: " + outputPath);

        } catch (IOException e) {
            System.err.println("Errore critico durante la scrittura del file DOCS.MD: " + e.getMessage());
        }
    }

	/**
	 * Trova tutte le cartelle che contengono direttamente file .java,
	 * escludendo quelle definite in EXCLUDED_PATHS.
	 */
    private static List<Path> findPackageDirectories(Path sourcePath) throws IOException {
        
        // Converti i percorsi di esclusione in Path completi per il confronto
        List<Path> absoluteExcludedPaths = EXCLUDED_PATHS.stream()
            .map(p -> sourcePath.resolve(p))
            .collect(Collectors.toList());

        // Cerca tutti i file .java
        List<Path> javaFiles = Files.walk(sourcePath)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .collect(Collectors.toList());

        // Estrai l'unica directory padre di ciascun file .java
        return javaFiles.stream()
                .map(Path::getParent)
                .distinct()
                .filter(dir -> !dir.equals(sourcePath)) // Esclude la cartella 'src' stessa
                // FILTRAGGIO AGGIUNTO: Esclude la directory padre se presente nella lista di esclusione
                .filter(dir -> {
                    // Controlla se il percorso della directory è *esattamente* uno dei percorsi da escludere.
                    boolean isExcluded = absoluteExcludedPaths.contains(dir);
                    
                    if (isExcluded) {
                        System.out.println("  --> Escluso il percorso: " + sourcePath.relativize(dir));
                    }
                    // Restituisce true se la directory NON è nella lista delle esclusioni
                    return !isExcluded;
                })
                .collect(Collectors.toList());
    }

    /**
     * Analizza un singolo file Java e estrae Javadoc di classe e metodi.
     */
    private static void processJavaFile(Path javaFile, PrintWriter writer) {
        String className = javaFile.getFileName().toString().replace(".java", "");
        String classTitle = "### " + className + "\n";
        writer.write(classTitle);
        System.out.println("    - Processing file: " + className);

        try {
            String content = new String(Files.readAllBytes(javaFile));
            
            // 1. ESTRAZIONE JAVADOC DELLA CLASSE
            // ... (logica invariata per la classe)
            Matcher classMatcher = CLASS_PATTERN.matcher(content);
            if (classMatcher.find()) {
                String rawClassJavadoc = classMatcher.group(1);
                String classDescription = extractDescriptionFromJavadoc(rawClassJavadoc);
                writer.write(classDescription + "\n\n");
            }

            // 2. ESTRAZIONE JAVADOC DEI METODI
            Matcher methodMatcher = METHOD_PATTERN.matcher(content);

            // *** IL CICLO while(find()) DEVE SCORRERE TUTTE LE OCCORRENZE ***
            while (methodMatcher.find()) {
                String rawJavadoc = methodMatcher.group(1); 
                String methodName = methodMatcher.group(2); 

                String description = extractDescriptionFromJavadoc(rawJavadoc);

                // Scrivi la sezione del metodo nel file Markdown
                writer.write("#### " + methodName + "\n");
                writer.write(description + "\n\n");
            }

        } catch (IOException e) {
            System.err.println("        Errore di lettura per il file " + javaFile.getFileName() + ": " + e.getMessage());
        }
    }

    /**
     * Estrae la descrizione principale ripulita dal blocco Javadoc grezzo.
     */
    private static String extractDescriptionFromJavadoc(String rawJavadoc) {
        String description = rawJavadoc.trim();

        // 1. Rimuove l'asterisco iniziale e lo spazio da ogni riga
        description = description.replaceAll("(?m)^\\s*\\*\\s?", "");
        
        // 2. Rimuove i tag Javadoc (@param, @return, ecc.) e tutto ciò che segue
        description = description.replaceAll("(?s)@\\w+.*", "");
        
        // 3. Pulisce spazi bianchi in eccesso e caratteri di nuova riga
        description = description.trim().replaceAll("\\s+", " ");
        
        // 4. Rimuove i tag HTML di base per un output Markdown più pulito (es. <p>, </p>)
        description = description.replaceAll("<[^>]*>", "");

        return description;
    }
}