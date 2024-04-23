# Message Types

## Client to Server

### Lobby Messages

- NewGameMessage: sent by a Client who wants to create a new game.
```json
{
    "type": "NewGameMessage",
    "senderNickname": "playerName"
}
```

- AccessGameMessage: sent by a Client who wants to access an already existing game.
```json
{
    "type": "AccessGameMessage",
    "matchID": 10,
    "senderNickname": "playerName"
}
```

- DestroyGameMessage : sent by Client to terminate a game and kick everyone out of the lobby
```json
{
  "type": "DestroyGameMessage",
  "senderNickname": "playerName"
}
```

- StartGameMessage: sent by a Client to start the game.
```json
{
  "type": "StartGameMessage",
  "senderNickname": "playerName"
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
  "type": "PlayCardMessage",
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
  "type": "PlayCardMessage",
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

- NewGameConfirmation: sent by the Server to confirm the creation of a new game.
```json
{
    "type": "NewGameConfirmation",
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

- AccessGameFailedMessage: sent by the Server to refuse the join of a Player.
```json
{
  "type": "AccessGameFailedMessage",
  "recipientNickname": "playerName",
  "reason": "Reason"
}
```

- LobbyPlayerListMessage: sent by the Server to notify players of who is currently in the lobby
```json
{
  "type": "LobbyPlayerListMessage",
  "recipientNickname": "playerName",
  "playerList": ["playerName1", "playerName2", "playerName3"]
}
```

- DestroyGameConfirmationMessage: sent by the Server to confirm the destruction of a game.
```json
{
  "type": "DestroyGameConfirmationMessage",
  "recipientNickname": "playerName"
}
```

- StartGameConfirmationMessage: sent by the Server to confirm the start of the game.
```json
{
  "type": "StartGameConfirmation",
  "recipientNickname": "playerName"
}
```

### Game Messages

- ResponseGameStatusMessage: sent by the Server to notify the current game status.
```json
{
  "type": "ResponseGameStatusMessage",
  "recipientNickname": "playerName",
  "playerNicknames": ["player1", "player2"],
  "playerColours": ["Red", "Blue"],
  "playerHand": [10, 11, 12],
  "playerSecretObjective": 9,
  "playerPoints": 5,
  "playerColour": "Red",
  "playerField": [[0, 0, 31, true], [1, 1, 30, true], [-1, -1, 29, false]],
  "playerResources": [1, 1, 1, 1, 1, 1, 1],
  "gameCommonObjectives": [50, 51],
  "gameCurrentResourceCards": [20, 21],
  "gameCurrentGoldCards": [22, 23],
  "gameResourcesDeckSize": 30,
  "gameGoldDeckSize": 31,
  "matchStatus": "Status",
  "playerChatHistory": [
    {
      "senderNickname": "playerName",
      "recipientNickname": "playerName",
      "multicastFlag": true,
      "content": "Message"
    }
  ]
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
  "points": 5
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
  "cardID": 14
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
  "matchStatus": "Status"
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

### Chat Messages

- OutboundChatMessage: sent by the Server to all relevant clients to propagate a chat message.
```json
{
  "type": "OutboundChatMessage",
  "senderNickname": "playerName",
  "recipientNickname": "playerName", // Can be null if the message is for everyone
  "multicastFlag": true, // If true, message is to be sent to all players
  "content": "Message"
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