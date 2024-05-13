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
    public void showPlayersField(String playerNickname) {

    }

    @Override
    public void showPoints(String playerNickname) {

    }

    @Override
    public void requestSelectSecretObjCard(ArrayList<Integer> cards) {

    }

    @Override
    public void showHand(ArrayList<Integer> hand) {

    }

    @Override
    public void showObjectiveCards(ArrayList<Integer> ObjCards) {

    }


    @Override
    public void showSecretObjCard(int ID) {

    }

    @Override
    public void showCard(int ID, boolean isUp) {

    }

    @Override
    public HashMap<Integer, ArrayList<String>> setImg() {
        return null;
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
    public void updateAfterDrawCard() {

    }

    @Override
    public void showInitialView() {

    }

    @Override
    public void showHelpInfo() {

    }

    @Override
    public void updatePlayerDate(ArrayList<String> players, ArrayList<Integer> colors, ArrayList<Integer> Hand, int SecretObjCard, int points, int colour, ArrayList<int[]> field, int[] resources, ArrayList<Integer> commonObjCards, ArrayList<Integer> currentResourceCards, ArrayList<Integer> currentGoldCards, int currentResourceDeckSize, int currentGoldDeckSize, int matchStatus) {

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
