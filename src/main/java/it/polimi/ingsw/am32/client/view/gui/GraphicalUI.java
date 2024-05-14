package it.polimi.ingsw.am32.client.view.gui;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.NonObjCardFactory;
import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphicalUI extends View {


    public GraphicalUI() {
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
    public void showPlayersField(String playerNickname) {

    }

    @Override
    public void showPoints(String playerNickname) {

    }

    @Override
    public void requestSelectSecretObjCard(ArrayList<Integer> secrets, ArrayList<Integer> common, ArrayList<Integer> hand) {

    }

    @Override
    public void showHand(ArrayList<Integer> hand) {

    }

    @Override
    public void showObjectiveCards(ArrayList<Integer> ObjCards) {

    }


    @Override
    public void showCard(int ID, boolean isUp) {

    }

    @Override
    public HashMap<Integer, ArrayList<String>> setImg() {
        return null;
    }

    @Override
    public void updatePlacedCardConfirm(String playerNickname, int placedCard, int[] placedCardCoordinates, boolean placedSide, int playerPoints, int[] playerResources, ArrayList<int[]> newAvailableFieldSpaces) {

    }

    @Override
    public void showMatchWinners(ArrayList<String> players, ArrayList<Integer> points, ArrayList<Integer> secrets, ArrayList<Integer> pointsGainedFromSecrets, ArrayList<String> winners) {

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
    public void setUpPlayersData() {

    }

    @Override
    public void updateMatchStatus(int matchStatus) {

    }

    @Override
    public void requestSelectStarterCardSide(int ID) {

    }

    @Override
    public void updateConfirmStarterCard(int colour, int cardID, boolean isUp, ArrayList<int[]> availablePos, int[] resources) {

    }

    @Override
    public void requestDrawCard() {

    }

    @Override
    public void updateAfterDrawCard(ArrayList<Integer> hand) {

    }

    @Override
    public void updateDeck(int resourceDeckSize, int goldDeckSize, int[] currentResourceCards, int[] currentGoldCards) {

    }

    @Override
    public void showDeck() {

    }

    @Override
    public void showHelpInfo() {

    }

    @Override
    public void updateConfirmSelectedSecretCard(int chosenSecretObjectiveCard) {

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
