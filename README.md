# Mail code reader extension (Chrome, Yandex)

Simple browser extension with server for reading verification codes from e-mail and inserting them into input fields

**Stack**:
Extension: Plasmo, React, antd, TS
Server: Kotlin, Ktor

**Tested**: Chrome

**Build**: node v20.11.1, yarn v1.22.21, jdk 17, gradle 8.4

## Build

### Extension:

```
cd client
yarn install
yarn build
```

Result folder: build/chrome-mv3-prod

### Server

Result folder: build/chrome-mv3-prod

```
cd server
./gradlew build
```

Result folder: build/libs

## Installation

### Server

1. Download jar from "Releases" sections or build it
2. Run it with your jre

#### OR

```
docker run -d -p8080:8080 anarchyghost/mail-code-reader-extension:0.0.1
```

### Extension

1. Download bundled extension from "Releases" sections or build it
2. Follow your browser instructions

- [Chrome](https://developer.chrome.com/docs/extensions/get-started/tutorial/hello-world#load-unpacked)

## Usage

1. Start your server
2. Open extension screen
3. Enable work via IMAP in your email account settings
4. Configure your accounts and server URL (with /getCode endpoint) in JSON format.
5. Right-click on input field -> Mail code reader -> Your account name.
6. Wait until the code from the last account message is inserted

## Configuration Example

```
{
  "url": "http://localhost:8080/getCode",
  "accounts": [
    {
      "name": "Personal",
      "host": "imap.example.com",
      "port": 993,
      "username": "example@example.com",
      "password": "example",
      "useSsl": true,
      "titleRegex": ".* one time password .*",
      "codeRegex": "\\d{4}",
      "folder": "INBOX"
    },
    {
      "name": "Personal 2",
      "host": "imap.example.com",
      "port": 993,
      "username": "example@example.com",
      "password": "example",
      "useSsl": true,
      "titleRegex": ".* one time password .*",
      "codeRegex": "\\d{4}",
      "folder": "PRIORITY"
      "searchInBody": true
    }
  ]
}
```

## Account configuration fields

* name - the name that will be displayed in the context menu
* host - imap server host
* port - imap server port
* username - your username
* password - your password
* useSsl - boolean, is it worth using ssl when connecting to an imap server
* titleRegex - regex for searching for messages with a code by title
* codeRegex - code regex
* folder - the directory where the codes will be searched
* searchInBody - boolean, should search code in message body or only in title, optional, by default - false
* searchCount - the number of recent messages that will be read to search for the code among them, optional, by
  default - 10

## TODO

1. Cross-browser compatibility
2. More settings
3. UI for settings

----

[LICENSE](LICENSE)
