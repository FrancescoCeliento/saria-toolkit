# DOCS - Saria Toolkit 0.0.7-alpha
## NETWORK
### FileDownloader
#### download
Scarica un file da un URL e lo salva in una directory locale, mantenendo il nome del file originale.

#### download
Scarica un file da un URL e lo salva in una directory locale, rinominandolo con il nome specificato.

### RssReader
#### setUrl
Configura l'URL del feed RSS da leggere

#### read
Legge e parsifica il feed RSS dall'URL configurato

### UrlFetcher
This class deals with retrieving data from URL.

#### readUrl
Calls a target URL and receives the body response in String format

#### getJsonResponse
Calls a target URL and receives the body response in JSON format

## API
### GitHubAPI
Class for managing GitHub API

#### getLastTag
Reads the latest tag released from a repository

### MastodonAPI
Class for managing Mastodon API

### TelegramAPI
Class for managing Telegram API

## CONVERTER
### DateConverter
Manages date conversion.

#### dateToString
Function to convert java.util.Date to String.

#### stringToDateWithThrow
Function to convert String to java.util.Date

#### stringToDate
Function to convert String to java.util.Date with internal exception handling

## SYSTEM
### ClassUtils


#### getClassName


### Explorer
#### fileExist
Verifica l'esistenza di un file che soddisfi i criteri specificati.

### FilePermissionManager
Classe per la gestione completa dei permessi, della proprietà e degli attributi speciali di un file utilizzando l'API Java NIO

#### getFilePath
Ottiene il percorso del file attualmente gestito

#### setFilePath
Imposta il percorso del file da gestire

#### setPermission
Imposta o rimuove un permesso specifico per un Principal

#### getPermission
Verifica se un permesso specifico è abilitato per un Principal

#### setChmod
Imposta i permessi con notazione numerica (chmod 3 o 4 cifre). Gestisce anche i permessi speciali (Setuid: 4, Setgid: 2, Sticky: 1)

#### getChmod
Ottiene i permessi attuali in notazione numerica (chmod 4 cifre)

#### setChmod
Imposta i permessi con notazione simbolica (es. "g+w", "u-x,a+r"). Supporta solo gli operatori '+' (aggiungi) e '-' (rimuovi).

#### setPermission
Imposta i permessi utilizzando la notazione estesa (rwxr-xr-x)

#### getPermission
Ottiene i permessi attuali in notazione estesa (rwxr-xr-x)

#### getOwner
Legge il nome del proprietario del file (chown)

#### setOwner
Cambia il proprietario del file (chown)

#### getGroup
Legge il nome del gruppo proprietario del file (chgrp)

#### setGroup
Cambia il gruppo proprietario del file (chgrp)

#### isSetuidEnabled
Verifica l'abilitazione del permesso speciale Setuid

#### setSetuidEnabled
Imposta il permesso speciale Setuid

#### isSetgidEnabled
Verifica l'abilitazione del permesso speciale Setgid

#### setSetgidEnabled
Imposta l'abilitazione del permesso speciale Setgid

#### isStickyBitEnabled
Verifica l'abilitazione del permesso speciale Sticky Bit

#### setStickyBitEnabled
Imposta l'abilitazione del permesso speciale Sticky Bit

### SystemViewer
#### getOperatingSystemType
Determina e restituisce il tipo di sistema operativo in uso

### TimerSystem
Manages timers

#### sleep
Executes Thread.sleep with internal exception handling.

#### waitForSeconds
Sleep cycle for the desired seconds.

#### waitForMinutes
Sleep cycle for the desired minutes.

## TEXT
### CSVUtility
Manages CSV.

#### makeRow
Create a CSV row from a set of fields.

## VALIDATOR
### JsonValidator
Manages JSON format validations.

#### isValid
Checks that the JSON is correct.

### YouTubeValidator
## IO
### JsonIO
Class for reading data from JSON.

#### readKeys
Returns a list of values that match the key.

#### readKey
Returns the value that match the key.

### KeyboardInput
Simplify keyboard input usage

#### waitEnter
Pause the application until the ENTER button is pressed

#### getInputStringFromConsole
Pause the application until the ENTER button is pressed

#### getInputIntFromConsole
Read input Integer from console

#### getInputDoubleFromConsole
Read input Double from console

#### getInputDateFromConsole
Read input Date from console

