package com.francescoceliento.system;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Classe per la gestione completa dei permessi, della proprietà e degli attributi speciali
 * di un file utilizzando l'API Java NIO.2 (PosixFilePermissions).
 * * NOTA: Le funzionalità POSIX (permessi rwx, chown, chgrp) sono supportate
 * solo su sistemi operativi Unix-like (Linux, macOS).
 * Compatibile con versioni Java 8 e successive.
 */
public class FilePermissionManager {

    private Path filePath;

    // Rappresenta i tipi di permessi standard.
    public enum Permission {
        READ, WRITE, EXECUTABLE
    }

	// Rappresenta i principali destinatari dei permessi.
    public enum Principal {
        USER, GROUP, OTHER, ALL
    }

    // Mappa per tradurre i nostri enum in PosixFilePermission standard
    // Inizializzazione con blocco statico per compatibilità con Java < 9 (dove Map.of non esiste)
    private static final Map<Principal, Map<Permission, PosixFilePermission>> PERMISSION_MAP;
    
    static {
        Map<Principal, Map<Permission, PosixFilePermission>> tempMap = new HashMap<>();

        // USER permissions
        Map<Permission, PosixFilePermission> userPerms = new HashMap<>();
        userPerms.put(Permission.READ, PosixFilePermission.OWNER_READ);
        userPerms.put(Permission.WRITE, PosixFilePermission.OWNER_WRITE);
        userPerms.put(Permission.EXECUTABLE, PosixFilePermission.OWNER_EXECUTE);
        tempMap.put(Principal.USER, Collections.unmodifiableMap(userPerms));

        // GROUP permissions
        Map<Permission, PosixFilePermission> groupPerms = new HashMap<>();
        groupPerms.put(Permission.READ, PosixFilePermission.GROUP_READ);
        groupPerms.put(Permission.WRITE, PosixFilePermission.GROUP_WRITE);
        groupPerms.put(Permission.EXECUTABLE, PosixFilePermission.GROUP_EXECUTE);
        tempMap.put(Principal.GROUP, Collections.unmodifiableMap(groupPerms));

        // OTHER permissions
        Map<Permission, PosixFilePermission> otherPerms = new HashMap<>();
        otherPerms.put(Permission.READ, PosixFilePermission.OTHERS_READ);
        otherPerms.put(Permission.WRITE, PosixFilePermission.OTHERS_WRITE);
        otherPerms.put(Permission.EXECUTABLE, PosixFilePermission.OTHERS_EXECUTE);
        tempMap.put(Principal.OTHER, Collections.unmodifiableMap(otherPerms));

        PERMISSION_MAP = Collections.unmodifiableMap(tempMap);
    }

    /**
     * Costruttore che accetta un oggetto File.
     * @param file Il file da gestire.
     */
    public FilePermissionManager(File file) {
        this.filePath = file.toPath();
    }
    
    /**
     * Costruttore che accetta un oggetto Path.
     * @param path Il percorso del file da gestire.
     */
    public FilePermissionManager(Path path) {
        this.filePath = path;
    }
    
    /**
     * Costruttore che accetta un percorso Stringa.
     * @param path Il percorso del file da gestire.
     */
    public FilePermissionManager(String path) {
        this.filePath = Paths.get(path);
    }

    /**
     * Ottiene il percorso del file attualmente gestito.
     * @return Il Path del file.
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * Imposta il percorso del file da gestire.
     * @param filePath Il Path del nuovo file.
     */
    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }
    
    /**
     * Verifica se il filesystem supporta le viste attributi POSIX.
     * @throws UnsupportedOperationException se i permessi POSIX non sono supportati.
     */
    private void checkPosixSupport() {
        if (!filePath.getFileSystem().supportedFileAttributeViews().contains("posix")) {
            throw new UnsupportedOperationException("Il filesystem non supporta i permessi POSIX per il file: " + filePath);
        }
    }

    /**
     * Ottiene l'attuale set di permessi POSIX del file.
     * @return Set<PosixFilePermission> dei permessi attuali.
     * @throws IOException Se si verifica un errore I/O.
     */
    private Set<PosixFilePermission> getCurrentPermissions() throws IOException {
        checkPosixSupport();
        return Files.getPosixFilePermissions(filePath);
    }

    /**
     * Imposta un nuovo set di permessi POSIX sul file.
     * @param perms Il set di permessi da impostare.
     * @throws IOException Se si verifica un errore I/O.
     */
    private void setCurrentPermissions(Set<PosixFilePermission> perms) throws IOException {
        checkPosixSupport();
        Files.setPosixFilePermissions(filePath, perms);
    }

    /**
     * Ottiene la PosixFilePermission corrispondente ai nostri enum.
     */
    private PosixFilePermission getPosixPerm(Principal principal, Permission permission) {
        return PERMISSION_MAP.get(principal).get(permission);
    }
    
    /**
     * Imposta o rimuove un permesso specifico per un Principal.
     * @param principal USER, GROUP, OTHER o ALL.
     * @param permission READ, WRITE o EXECUTABLE.
     * @param enabled true per abilitare, false per disabilitare.
     * @throws IOException Se si verifica un errore I/O.
     */
    public void setPermission(Principal principal, Permission permission, boolean enabled) throws IOException {
        Set<PosixFilePermission> currentPerms = getCurrentPermissions();

        if (principal == Principal.ALL) {
            for (Principal p : new Principal[]{Principal.USER, Principal.GROUP, Principal.OTHER}) {
                PosixFilePermission posixPerm = getPosixPerm(p, permission);
                if (enabled) {
                    currentPerms.add(posixPerm);
                } else {
                    currentPerms.remove(posixPerm);
                }
            }
        } else {
            PosixFilePermission posixPerm = getPosixPerm(principal, permission);
            if (enabled) {
                currentPerms.add(posixPerm);
            } else {
                currentPerms.remove(posixPerm);
            }
        }
        setCurrentPermissions(currentPerms);
    }

    /**
     * Verifica se un permesso specifico è abilitato per un Principal.
     * @param principal USER, GROUP, OTHER o ALL.
     * @param permission READ, WRITE o EXECUTABLE.
     * @return true se il permesso è abilitato.
     * @throws IOException Se si verifica un errore I/O.
     */
    public boolean getPermission(Principal principal, Permission permission) throws IOException {
        if (principal == Principal.ALL) {
            // ALL è vero solo se è vero per USER, GROUP e OTHER.
            return getPermission(Principal.USER, permission) &&
                   getPermission(Principal.GROUP, permission) &&
                   getPermission(Principal.OTHER, permission);
        }

        Set<PosixFilePermission> currentPerms = getCurrentPermissions();
        PosixFilePermission posixPerm = getPosixPerm(principal, permission);
        return currentPerms.contains(posixPerm);
    }
    
    /**
     * Converte i permessi POSIX (9 bit) in formato ottale (3 cifre).
     */
    private int permissionsToOctal(Set<PosixFilePermission> perms) {
        int octal = 0;
        // User (400, 200, 100)
        if (perms.contains(PosixFilePermission.OWNER_READ)) octal += 400;
        if (perms.contains(PosixFilePermission.OWNER_WRITE)) octal += 200;
        if (perms.contains(PosixFilePermission.OWNER_EXECUTE)) octal += 100;
        // Group (40, 20, 10)
        if (perms.contains(PosixFilePermission.GROUP_READ)) octal += 40;
        if (perms.contains(PosixFilePermission.GROUP_WRITE)) octal += 20;
        if (perms.contains(PosixFilePermission.GROUP_EXECUTE)) octal += 10;
        // Others (4, 2, 1)
        if (perms.contains(PosixFilePermission.OTHERS_READ)) octal += 4;
        if (perms.contains(PosixFilePermission.OTHERS_WRITE)) octal += 2;
        if (perms.contains(PosixFilePermission.OTHERS_EXECUTE)) octal += 1;
        return octal;
    }

    /**
     * Converte un valore ottale (3 cifre) in un Set di permessi POSIX (9 bit).
     */
    private Set<PosixFilePermission> octalToPermissions(int octal) {
        Set<PosixFilePermission> perms = EnumSet.noneOf(PosixFilePermission.class);
        int owner = (octal / 100) % 10;
        int group = (octal / 10) % 10;
        int other = octal % 10;

        if ((owner & 4) != 0) perms.add(PosixFilePermission.OWNER_READ);
        if ((owner & 2) != 0) perms.add(PosixFilePermission.OWNER_WRITE);
        if ((owner & 1) != 0) perms.add(PosixFilePermission.OWNER_EXECUTE);

        if ((group & 4) != 0) perms.add(PosixFilePermission.GROUP_READ);
        if ((group & 2) != 0) perms.add(PosixFilePermission.GROUP_WRITE);
        if ((group & 1) != 0) perms.add(PosixFilePermission.GROUP_EXECUTE);

        if ((other & 4) != 0) perms.add(PosixFilePermission.OTHERS_READ);
        if ((other & 2) != 0) perms.add(PosixFilePermission.OTHERS_WRITE);
        if ((other & 1) != 0) perms.add(PosixFilePermission.OTHERS_EXECUTE);

        return perms;
    }
    
    // Metodi di supporto per leggere/scrivere gli attributi speciali
    private boolean getSpecialAttribute(String attributeName) throws IOException {
        checkPosixSupport();
        try {
            return (Boolean) Files.getAttribute(filePath, attributeName);
        } catch (Exception e) {
            // Non tutti i filesystem supportano tutti gli attributi speciali.
            return false;
        }
    }

    private void setSpecialAttribute(String attributeName, boolean enabled) throws IOException {
        checkPosixSupport();
        try {
            Files.setAttribute(filePath, attributeName, enabled);
        } catch (Exception e) {
            System.err.println("Avviso: Impossibile impostare l'attributo speciale " + attributeName + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Imposta i permessi con notazione numerica (chmod 3 o 4 cifre).
     * Gestisce anche i permessi speciali (Setuid: 4, Setgid: 2, Sticky: 1).
     * @param chmod Il valore numerico (es. 755 o 4755).
     * @throws IOException Se si verifica un errore I/O.
     */
    public void setChmod(int chmod) throws IOException {
        if (chmod < 0 || chmod > 7777) {
            throw new IllegalArgumentException("Il valore Chmod deve essere tra 0 e 7777.");
        }
        
        int specialBits = chmod / 1000;
        int standardBits = chmod % 1000;

        // 1. Imposta i permessi standard (rwx per u/g/o)
        Set<PosixFilePermission> perms = octalToPermissions(standardBits);
        setCurrentPermissions(perms);

        // 2. Imposta i permessi speciali
        setSpecialAttribute("posix:suid", (specialBits & 4) != 0); // 4000
        setSpecialAttribute("posix:sgid", (specialBits & 2) != 0); // 2000
        setSpecialAttribute("posix:sticky", (specialBits & 1) != 0); // 1000
    }

    /**
     * Ottiene i permessi attuali in notazione numerica (chmod 4 cifre).
     * @return Il valore chmod a 4 cifre.
     * @throws IOException Se si verifica un errore I/O.
     */
    public int getChmod() throws IOException {
        int octal = permissionsToOctal(getCurrentPermissions());
        int specialBits = 0;
        
        // Calcola i bit speciali
        if (isSetuidEnabled()) specialBits += 4000;
        if (isSetgidEnabled()) specialBits += 2000;
        if (isStickyBitEnabled()) specialBits += 1000;
        
        // Combina i bit speciali con l'ottale standard (es. 4000 + 755 = 4755)
        return specialBits + octal;
    }

    /**
     * Imposta i permessi con notazione simbolica (es. "g+w", "u-x,a+r").
     * Supporta solo gli operatori '+' (aggiungi) e '-' (rimuovi).
     * @param symbolicChmod La stringa dei permessi simbolici.
     * @throws IOException Se si verifica un errore I/O.
     */
    public void setChmod(String symbolicChmod) throws IOException {
        String[] operations = symbolicChmod.replaceAll("\\s+", "").toLowerCase().split(",");
        
        for (String operation : operations) {
            if (operation.isEmpty()) continue;
            
            // Trova l'indice dell'operatore (+ o -)
            int operatorIndex = operation.indexOf('+');
            if (operatorIndex == -1) operatorIndex = operation.indexOf('-');
            if (operatorIndex == -1) {
                throw new IllegalArgumentException("Operatore simbolico non trovato (+ o -): " + operation);
            }

            String principalString = operation.substring(0, operatorIndex);
            char operatorChar = operation.charAt(operatorIndex);
            String permissionString = operation.substring(operatorIndex + 1);
            
            boolean enable = (operatorChar == '+');
            
            for (char pChar : principalString.toCharArray()) {
                Principal principal;
                switch (pChar) {
                    case 'u': principal = Principal.USER; break;
                    case 'g': principal = Principal.GROUP; break;
                    case 'o': principal = Principal.OTHER; break;
                    case 'a': principal = Principal.ALL; break;
                    default: throw new IllegalArgumentException("Principal simbolico non valido: " + pChar);
                }

                for (char permChar : permissionString.toCharArray()) {
                    Permission permission;
                    switch (permChar) {
                        case 'r': permission = Permission.READ; break;
                        case 'w': permission = Permission.WRITE; break;
                        case 'x': permission = Permission.EXECUTABLE; break;
                        default: throw new IllegalArgumentException("Permesso simbolico non valido: " + permChar);
                    }
                    
                    // Utilizza il metodo setPermission a flag per applicare la modifica
                    setPermission(principal, permission, enable);
                }
            }
        }
    }

    /**
     * Imposta i permessi utilizzando la notazione estesa (rwxr-xr-x).
     * @param extendedPermission La stringa dei permessi (9 caratteri).
     * @throws IOException Se si verifica un errore I/O.
     */
    public void setPermission(String extendedPermission) throws IOException {
        try {
            Set<PosixFilePermission> perms = PosixFilePermissions.fromString(extendedPermission);
            setCurrentPermissions(perms);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato esteso dei permessi non valido (deve essere 9 caratteri rwxrwxrwx).", e);
        }
    }

    /**
     * Ottiene i permessi attuali in notazione estesa (rwxr-xr-x).
     * @return La stringa dei permessi estesi.
     * @throws IOException Se si verifica un errore I/O.
     */
    public String getPermission() throws IOException {
        return PosixFilePermissions.toString(getCurrentPermissions());
    }
    
    /**
     * Legge il nome del proprietario del file (chown).
     * @return Il nome del proprietario.
     * @throws IOException Se si verifica un errore I/O.
     */
    public String getOwner() throws IOException {
        checkPosixSupport();
        return Files.getOwner(filePath).getName();
    }

    /**
     * Cambia il proprietario del file (chown).
     * @param ownerName Il nome del nuovo proprietario.
     * @throws IOException Se si verifica un errore I/O o l'utente non viene trovato.
     */
    public void setOwner(String ownerName) throws IOException {
        checkPosixSupport();
        UserPrincipalLookupService lookup = filePath.getFileSystem().getUserPrincipalLookupService();
        UserPrincipal owner = lookup.lookupPrincipalByName(ownerName);
        Files.setOwner(filePath, owner);
    }

    /**
     * Legge il nome del gruppo proprietario del file (chgrp).
     * @return Il nome del gruppo.
     * @throws IOException Se si verifica un errore I/O.
     */
    public String getGroup() throws IOException {
        checkPosixSupport();
        PosixFileAttributes attrs = Files.readAttributes(filePath, PosixFileAttributes.class);
        return attrs.group().getName();
    }

    /**
     * Cambia il gruppo proprietario del file (chgrp).
     * @param groupName Il nome del nuovo gruppo.
     * @throws IOException Se si verifica un errore I/O o il gruppo non viene trovato.
     */
    public void setGroup(String groupName) throws IOException {
        checkPosixSupport();
        UserPrincipalLookupService lookup = filePath.getFileSystem().getUserPrincipalLookupService();
        GroupPrincipal group = lookup.lookupPrincipalByGroupName(groupName);
        Files.setAttribute(filePath, "posix:group", group);
    }
    
    /**
     * Verifica l'abilitazione del permesso speciale Setuid
     * @author @francescoceliento@github.com
     *
     * @return boolean
     * @throws IOException
     */
    public boolean isSetuidEnabled() throws IOException { return getSpecialAttribute("posix:suid"); }
    /**
     * Imposta il permesso speciale Setuid
     * @author @francescoceliento@github.com
     *
     * @param enabled
     * @throws IOException
     */
    public void setSetuidEnabled(boolean enabled) throws IOException { setSpecialAttribute("posix:suid", enabled); }

    /**
     * Verifica l'abilitazione del permesso speciale Setgid
     * @author @francescoceliento@github.com
     *
     * @return boolean
     * @throws IOException
     */
    public boolean isSetgidEnabled() throws IOException { return getSpecialAttribute("posix:sgid"); }
    /**
     * Imposta l'abilitazione del permesso speciale Setgid
     * @author @francescoceliento@github.com
     *
     * @param enabled
     * @throws IOException
     */
    public void setSetgidEnabled(boolean enabled) throws IOException { setSpecialAttribute("posix:sgid", enabled); }

    /**
     * Verifica l'abilitazione del permesso speciale Sticky Bit
     * @author @francescoceliento@github.com
     *
     * @return boolean
     * @throws IOException
     */
    public boolean isStickyBitEnabled() throws IOException { return getSpecialAttribute("posix:sticky"); }
    /**
     * Imposta l'abilitazione del permesso speciale Sticky Bit
     * @author @francescoceliento@github.com
     *
     * @param enabled
     * @throws IOException
     */
    public void setStickyBitEnabled(boolean enabled) throws IOException { setSpecialAttribute("posix:sticky", enabled); }
    
    
    
    
}


/**
Mi serve una classe Java chiamata FilePermissionManager che mi permetta di leggere e modificare i permessi
di qualsiasi file. Deve avere un attributo file configurabile da costruttore o da get e set
e deve permettermi di gestire i permessi con 3 diverse modalità, un enum con i valori READ, WRITE, EXECUTABLE
un enum con i valori USER, GROUP, OTHER, ALL (cioé valido per USER, GROUP E OTHER contemporaneamente)

1. flag con metodi come questi
.setPermission(USER, READ, true);      -- permesso di lettura abilitato per l'utente
.setPermission(ALL, EXECUTABLE, true); -- permesso di esecuzione abilitato per tutti
.setPermission(GROUP, WRITE, false);   -- permesso di scrittura disabilitato per il gruppo
.getPermission(GROUP, READ);           -- verifica permesso di lettura per il gruppo
ecc...

2. chmod numerico
.setChmod(755);  -- imposta il chmod 755
.getChmod();     -- verifica il chmod attuale con ritorno int

3. chmod simbolico
.setChmod("g+w") -- imposta il chmod g+w

4. esteso
.setPermission("rwxr--r--");
.getPermission(); --riceve una string

mi servono anche i metodi per
- leggere e cambiare il proprietario del file chown
- leggere e cambiare il gruppo proprietario del file chgrp
- leggere e definire i permessi di default umask
- verificare, abilitare e disabilitare i permessi speciali Setuid, Setgid, Sticky Bit

La classe non deve avere bisogno di librerie esterne, ma essere autonoma
*/