# Message Types

We have chosen to represent the messages exchanged between the Client and the Server using JSON thanks to its simplicity and readability. Each message is represented as a JSON object with a "type" field that specifies the type of message. The other fields depend on the type of message. 
The messages are divided into three categories: Lobby Messages, Game Messages, and Chat Messages.

## Client to Server

### Lobby Messages

- NewGameMessage: sent by a Client who wants to create a new game.
```json
{
    "type": "NewGameMessage",
    "senderNickname": "playerName",
    "playerNum": 3
}
```

- AccessGameMessage: sent by a Client who wants to access an already existing game.
```json
{
    "type": "AccessGameMessage",
    "senderNickname": "playerName",
    "matchID": 10
}
```

### Game Messages

- RequestGameStatusMessage: sent by a Client to request the current game status.
```json
{
  "type": "RequestGameStatusMessage",
  "senderNickname": "playerName"
}
```

- PlaceCardMessage: sent by a Client to place a card on its field.
```json
{
  "type": "PlaceCardMessage",
  "senderNickname": "playerName",
  "cardID": 10,
  "row": 1,
  "column": 1,
  "isUp": true // Flag indicating which side the card was placed on
}
```

- DrawCardMessage: sent by a Client to notify the Server that a card had been chosen to be drawn.
```json
{
  "type": "DrawCardMessage",
  "senderNickname": "playerName",
  "deckType": 1,
  "cardID": 23 // Optional parameter used if face-up card is drawn
}
```

- RequestPlayerFieldMessage: sent by a Client to request the field of a player.
```json
{
  "type": "RequestPlayerFieldMessage",
  "senderNickname": "playerName",
  "playerNickname": "playerName"
}
```

- SelectedStarterCardSideMessage: sent by the Client to notify the server of the initial card placement side
```json
{
  "type": "SelectedStarterCardSideMessage",
  "senderNickname": "playerName",
  "isUp": true 
}
```

- SelectedSecretObjectiveCardMessage: sent by the Client to notify the server of the chosen secret objective card 
```json
{
  "type": "SelectedSecretObjectiveCardMessage",
  "senderNickname": "playerName",
  "cardId": 12 
}
```

### Chat Messages

- InboundChatMessage: sent by a Client to send a message in chat.
```json
{
  "type": "InboundChatMessage",
  "senderNickname": "playerName",
  "recipientNickname": "playerName", // Can be null if the message is for everyone
  "multicastFlag": true,
  "content": "Message"
}
```

### Service Messages

- PingMessage: sent by a Client to refresh the Server timeout 
```json
{
  "type": "PingMessage",
  "senderNickname": "playerName"
}
```

## Server to Client

### Lobby Messages

- NewGameConfirmationMessage: sent by the Server to confirm the creation of a new game.
```json
{
    "type": "NewGameConfirmationMessage",
    "recipientNickname": "playerName",
    "matchID": 10
}
```

- AccessGameConfirmMessage: sent by the Server to confirm the access to a new game.
```json
{
  "type": "AccessGameConfirmMessage",
  "recipientNickname": "playerName"
}
```

Some "reasons" could be: CodeNotFoundMessage (the inserted code does not correspond to a match), NonExistAvailableGameMessage (there are no active games on the server, the player is obliged to create a new match), or RoomFullMessage (the match lobby is full, so the player cannot connect).

- LobbyPlayerListMessage: sent by the Server to notify players of who is currently in the lobby
```json
{
  "type": "LobbyPlayerListMessage",
  "recipientNickname": "playerName",
  "playerList": ["playerName1", "playerName2", "playerName3"]
}
```

- GameStartedMessage: sent by the Server to announce the start of the game.
```json
{
  "type": "GameStartedMessage",
  "recipientNickname": "playerName"
}
```

### Game Messages

- PlayerGameStatusMessage : sent by the Server to notify the current game status.
```json
{
  "type": "PlayerGameStatusMessage",
  "recipientNickname": "playerName",
  "playerNicknames": ["player1", "player2"],
  "playerConnected": [true, false],
  "playerColours": [1, 4],
  "playerHand": [10, 11, 12],
  "playerSecretObjective": 9,
  "playerPoints": [10, 23, 10],
  "playerFields": [[[0, 0, 12, true], [1, 2, 4, false]], [[1, 2, 34, false]], [[0, 0, 12, false], [1, 1, 34, true]]], 
  "playerResources": [1, 1, 1, 1, 1, 1, 1],
  "gameCommonObjectives": [50, 51],
  "gameCurrentResourceCards": [20, 21],
  "gameCurrentGoldCards": [22, 23],
  "gameResourcesDeckSize": 30,
  "gameGoldDeckSize": 31,
  "matchStatus": "Status",
  "chatHistory": [
    {
      "senderNickname": "playerName",
      "recipientNickname": "playerName",
      "multicastFlag": true,
      "content": "Message"
    }
  ],
  "currentPlayer": "playerName",
  "newAvailableFieldSpaces": [[1,2],[3,4], [5,6]]
}
```

- PlayerTurnMessage: sent by the Server to notify the player that it's their turn. This message is sent also to the other players to notify them that the player is playing.
```json
{
  "type": "PlayerTurnMessage",
  "recipientNickname": "playerName",
  "playingNickname": "playerName" // Nickname of player whose turn it is
}
```

- PlaceCardConfirmationMessage: sent by the Server to confirm the placement of a card.
```json
{
  "type": "PlaceCardConfirmationMessage",
  "recipientNickname": "playerName",
  "playerResources": [1, 1, 1, 1, 1, 1, 1],
  "points": 5,
  "newAvailableFieldSpaces": [[1,2],[3,4],[5,6]]
}
```

- PlaceCardFailedMessage: sent by the Server to notify the player that the placement of a card failed.
```json
{
  "type": "PlaceCardFailedMessage",
  "recipientNickname": "playerName",
  "reason": "Reason"
}
```

- PointsUpdateMessage: sent by the Server to notify the player of the updated points.
```json
{
  "type": "PointsUpdateMessage",
  "recipientNickname": "playerName",
  "playerNickname": "playerName",
  "points": 5
}
```

- DrawCardConfirmationMessage: sent by the Server to confirm the drawing of a card.
```json
{
  "type": "DrawCardConfirmationMessage",
  "recipientNickname": "playerName",
  "playerHand": [14, 17, 21]
}
```

- DrawCardFailedMessage: sent by the Server to notify the player that the drawing of a card failed.
```json
{
  "type": "DrawCardFailedMessage",
  "recipientNickname": "playerName",
  "reason": "Reason"
}
```

- DeckSizeUpdateMessage: sent by the Server to notify the player of the updated deck size.
```json
{
  "type": "DeckSizeUpdateMessage",
  "recipientNickname": "playerName",
  "resourcesCardDeckSize": 30,
  "goldCardsDeckSize": 31,
  "currentResourceCards": [20, 21],
  "currentGoldCards": [22, 23]
}
```

- ResponsePlayerFieldMessage: sent by the Server to respond to a RequestPlayerFieldMessage.
```json
{
  "type": "ResponsePlayerFieldMessage",
  "recipientNickname": "playerName",
  "playerNickname": "playerName",
  "playerField": [[0, 0, 31, true], [1, 1, 30, true], [-1, -1, 29, false]],
  "playerResources": [1, 1, 1, 1, 1, 1, 1]
}
```

- NegativeResponsePlayerFieldMessage: sent by the Server to notify the player that the requested player field does not exist.
```json
{
  "type": "NegativeResponsePlayerFieldMessage",
  "recipientNickname": "playerName",
  "playerNickname": "playerName",
}
```

- PlayerDisconnectedMessage: sent by the Server to notify the other players that a player has disconnected.
```json
{
  "type": "PlayerDisconnectedMessage",
  "recipientNickname": "playerName",
  "disconnectedNickname": "playerName"
}
```

- PlayerReconnectedMessage: sent by the Server to notify the other players that a player has reconnected.
```json
{
  "type": "PlayerReconnectedMessage",
  "recipientNickname": "playerName",
  "reconnectedNickname": "playerName"
}
```

- MatchStatusMessage: sent by the Server to notify the players of the current match status.
```json
{
  "type": "MatchStatusMessage",
  "recipientNickname": "playerName",
  "matchStatus": 1
}
```

- MatchWinnersMessage: sent by the Server to notify the players of the winners of the match.
```json
{
  "type": "MatchWinnersMessage",
  "recipientNickname": "playerName",
  "winners": ["player1", "player2"]
}
```

- AssignedStarterCardMessage: sent by the Server to notify the Client of his assigned starting card
```json
{
  "type": "AssignedStarterCardMessage",
  "recipientNickname": "playerName",
  "cardId": 34 
}
```

- ConfirmStarterCardSideSelectionMessage: sent by the Server to confirm the reception of the selected starting card side. Contains the colour assigned to the player
```json
{
  "type": "ConfirmStarterCardSideSelectionMessage",
  "recipientNickname": "playerName",
  "playerColour": 1
}
```

- InvalidStarterCardSideSelectionMessage : sent by the Server to notify the Client that the selected side of the starting card is invalid
```json
{
  "type": "InvalidStarterCardSideSelectionMessage",
  "recipientNickname": "playerName",
  "reason": "Reason"
}
```

- AssignedSecretObjectiveCardMessage: sent by the Server to notify the Client of his assigned assortment of secret objective cards
```json
{
  "type": "AssignedSecretObjectiveCardMessage",
  "recipientNickname": "playerName",
  "assignedCards": [23, 26] 
}
```

- ConfirmSelectedSecretObjectiveCardMessage: sent by the Server to confirm the reception of the selected secret objective card
```json
{
  "type": "ConfirmSelectedSecretObjectiveCardMessage",
  "recipientNickname": "playerName"
}
```

- InvalidSelectedSecretObjectiveCardMessage: sent by the Server to notify the Client that the selected secret objective card is invalid
```json
{
  "type": "InvalidSelectedSecretObjectiveCardMessage",
  "recipientNickname": "playerName",
  "reason": "Reason"
}
```

### Chat Messages

- OutboundChatMessage: sent by the Server to all relevant clients to propagate a chat message.
```json
{
  "type": "OutboundChatMessage",
  "senderNickname": "playerName",
  "recipientNickname": "playerName", // Can be null if the message is for everyone
  "content": "Message"
}
```

- InvalidInboundChatMessage: sent by the Server to notify the Client that the chat message is invalid.
```json
{
  "type": "InvalidInboundChatMessage",
  "recipientNickname": "playerName",
  "reason": "Reason"
}
```

### Service Messages

- PongMessage: sent by the Server to confirm the reception of a PingMessage.
```json
{
  "type": "PongMessage",
  "recipientNickname": "playerName"
}
```

- ErrorMessages: sent by the Server to notify the Client of an error that can't be handled or checked by the GameController.
```json
{
  "type": "ErrorMessages",
  "recipientNickname": "playerName",
  "message": "Error"
}
```
