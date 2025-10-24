# DOCS - Saria Toolkit 0.0.7-alpha
## NETWORK
### FileDownloader
#### String download(String source, String localDir)
Scarica un file da un URL e lo salva in una directory locale, mantenendo il nome del file originale.

#### String download(String source, String localDir, String name)
Scarica un file da un URL e lo salva in una directory locale, rinominandolo con il nome specificato.

### RssReader
#### void setUrl(String urlString)
Configura l'URL del feed RSS da leggere

#### List<RssItem> read()
Legge e parsifica il feed RSS dall'URL configurato

### UrlFetcher
This class deals with retrieving data from URL.

#### String readUrl(String targetUrl)
Calls a target URL and receives the body response in String format

#### String getJsonResponse(String targetUrl)
Calls a target URL and receives the body response in JSON format

## API
### GitHubAPI
Class for managing GitHub API

#### String getLastTag(String author, String repository)
Reads the latest tag released from a repository

### MastodonAPI
Class for managing Mastodon API

### TelegramAPI
Class for managing Telegram API

## CONVERTER
### DateConverter
Manages date conversion.

#### String dateToString(Date date, String pattern)
Function to convert java.util.Date to String.

#### Date stringToDateWithThrow(String dateString, String pattern)
Function to convert String to java.util.Date

#### Date stringToDate(String dateString, String pattern)
Function to convert String to java.util.Date with internal exception handling

## SYSTEM
### ClassUtils


#### String getClassName()


### Explorer
#### boolean fileExist(String dir, String search, String ext, TypeSearch typeSearch, Include include, boolean subFolder)
Verifica l'esistenza di un file che soddisfi i criteri specificati.

### FilePermissionManager
Classe per la gestione completa dei permessi, della proprietà e degli attributi speciali di un file utilizzando l'API Java NIO

#### Path getFilePath()
Ottiene il percorso del file attualmente gestito

#### void setFilePath(Path filePath)
Imposta il percorso del file da gestire

#### void setPermission(Principal principal, Permission permission, boolean enabled)
Imposta o rimuove un permesso specifico per un Principal

#### boolean getPermission(Principal principal, Permission permission)
Verifica se un permesso specifico è abilitato per un Principal

#### void setChmod(int chmod)
Imposta i permessi con notazione numerica (chmod 3 o 4 cifre). Gestisce anche i permessi speciali (Setuid: 4, Setgid: 2, Sticky: 1)

#### int getChmod()
Ottiene i permessi attuali in notazione numerica (chmod 4 cifre)

#### void setChmod(String symbolicChmod)
Imposta i permessi con notazione simbolica (es. "g+w", "u-x,a+r"). Supporta solo gli operatori '+' (aggiungi) e '-' (rimuovi).

#### void setPermission(String extendedPermission)
Imposta i permessi utilizzando la notazione estesa (rwxr-xr-x)

#### String getPermission()
Ottiene i permessi attuali in notazione estesa (rwxr-xr-x)

#### String getOwner()
Legge il nome del proprietario del file (chown)

#### void setOwner(String ownerName)
Cambia il proprietario del file (chown)

#### String getGroup()
Legge il nome del gruppo proprietario del file (chgrp)

#### void setGroup(String groupName)
Cambia il gruppo proprietario del file (chgrp)

#### boolean isSetuidEnabled()
Verifica l'abilitazione del permesso speciale Setuid

#### void setSetuidEnabled(boolean enabled)
Imposta il permesso speciale Setuid

#### boolean isSetgidEnabled()
Verifica l'abilitazione del permesso speciale Setgid

#### void setSetgidEnabled(boolean enabled)
Imposta l'abilitazione del permesso speciale Setgid

#### boolean isStickyBitEnabled()
Verifica l'abilitazione del permesso speciale Sticky Bit

#### void setStickyBitEnabled(boolean enabled)
Imposta l'abilitazione del permesso speciale Sticky Bit

### SystemViewer
#### OperatingSystemType getOperatingSystemType()
Determina e restituisce il tipo di sistema operativo in uso

### TimerSystem
Manages timers

#### void sleep(int millisecond)
Executes Thread.sleep with internal exception handling.

#### void waitForSeconds(int seconds)
Sleep cycle for the desired seconds.

#### void waitForMinutes(int minutes)
Sleep cycle for the desired minutes.

## TEXT
### CSVUtility
Manages CSV.

#### String makeRow(String separator, String...fields)
Create a CSV row from a set of fields.

## VALIDATOR
### JsonValidator
Manages JSON format validations.

#### boolean isValid(String json)
Checks that the JSON is correct.

### YouTubeValidator
## IO
### JsonIO
Class for reading data from JSON.

#### List<String> readKeys(String json, String key)
Returns a list of values that match the key.

#### String readKey(String json, String key)
Returns the value that match the key.

### KeyboardInput
Simplify keyboard input usage

#### void waitEnter()
Pause the application until the ENTER button is pressed

#### String getInputStringFromConsole()
Pause the application until the ENTER button is pressed

#### Integer getInputIntFromConsole()
Read input Integer from console

#### Double getInputDoubleFromConsole()
Read input Double from console

#### Date getInputDateFromConsole(String pattern)
Read input Date from console

