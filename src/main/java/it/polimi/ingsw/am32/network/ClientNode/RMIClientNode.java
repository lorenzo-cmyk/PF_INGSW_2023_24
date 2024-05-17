package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

public class RMIClientNode implements ClientNodeInterface, RMIClientNodeInt {
    private final View view;
    public RMIClientNode(View view) {
        this.view = view;
    }
    @Override
    public void uploadToServer(CtoSMessage message) throws UploadFailureException {
        //TODO
    }

    @Override
    public void receiveFromServer() {
        //TODO
    }

    @Override
    public void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException  {
        //TODO
    }

    @Override
    public void uploadStoC(StoCMessage message) {
        //TODO
    }

    public void run() {
        // TODO
    }

    @Override
    public void pingTimeOverdue() {

    }
}
