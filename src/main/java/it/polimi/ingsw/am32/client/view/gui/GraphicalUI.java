package it.polimi.ingsw.am32.client.view.gui;

import it.polimi.ingsw.am32.Client;
import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.UI;
import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ClientNodeInterface;

public class GraphicalUI extends UI implements View {


    public GraphicalUI() {
    //TODO
    }

    @Override
    public void updateView(StoCMessage message) {
        //TODO
    }

    @Override
    public void notifyAskListener(CtoSMessage message) {
        //TODO
    }

    @Override
    public void notifyAskListenerLobby(CtoSLobbyMessage message) {
        //TODO
    }


    @Override
    public void launch() {
        //TODO
    }


    @Override
    public void InitializeViewElement() {
        //TODO
    }

    @Override
    public void showWelcome() {
        //TODO
    }


    @Override
    public void askSelectGameMode() {
        //TODO
    }

    @Override
    public void askNickname() {
        //TODO
    }

    @Override
    public void askCreateGame() {
        //TODO
    }


    @Override
    public void askJoinGame() {
        //TODO
    }

    @Override
    public void askReconnectGame() {
        //TODO
    }


    @Override
    public void chooseConnection() {

    }

    @Override
    public void showInitialView() {

    }

    @Override
    public void showHelpInfo() {

    }

    @Override
    public void requestSelectStarterCardSide() {

    }

    @Override
    public void requestSelectSecretObjCard() {

    }

    @Override
    public void requestPlaceCard() {

    }

    @Override
    public void handleEvent(Event event) {
        //TODO
    }

    @Override
    public void handleChoiceEvent(Event event, int Choice) {
        //TODO
    }
}
