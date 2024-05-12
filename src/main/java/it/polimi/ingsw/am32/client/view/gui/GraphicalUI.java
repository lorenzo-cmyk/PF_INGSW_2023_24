package it.polimi.ingsw.am32.client.view.gui;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.NonObjCardFactory;
import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

import java.util.ArrayList;

public class GraphicalUI extends View {


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
    public void updateNewGameConfirm(int gameID, String recipientNickname) {

    }


    @Override
    public void askJoinGame() {
        //TODO
    }

    @Override
    public void updateNewPlayerJoin(ArrayList<String> players) {

    }

    @Override
    public void askReconnectGame() {
        //TODO
    }


    @Override
    public void chooseConnection() {

    }

    @Override
    public void updateMatchStatus(int matchStatus) {

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
    public void updateAfterPlacedCard(String playerNickname, NonObjCardFactory card, int x, int y, boolean isUp, ArrayList<int[]> availablePos, int[] resources, int points) {

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
