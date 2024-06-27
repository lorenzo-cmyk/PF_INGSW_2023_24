# Deployment

## Server

It is required to put the correct server IPv4 address in the Config.json file in order to let RMI work properly.

Here is an example of the Config.json file:

```json
{
  "socketPort": 30000,
  "rmiPort": 30001,
  "pingTimePeriod": 5000,
  "socketReadTimeout": 100,
  "maxPingCount": 3,
  "serverIp": "192.168.178.20",
  "endGameDueToDisconnectionTimeout": 120000
}
```

## Client

If the client is running on a Windows machine it is required to enable UTF-8 encoding in the terminal to let the client display the special characters correctly for TUI.

To enable UTF-8 encoding in the terminal, run the following command:

```powershell
PS C:\> $OutputEncoding = [System.Console]::OutputEncoding = [System.Console]::InputEncoding = [System.Text.Encoding]::UTF8
PS C:\> $PSDefaultParameterValues['*:Encoding'] = 'utf8'
PS C:\> java -jar .\IS24-AM32-0.1-CLIENT.jar
```
