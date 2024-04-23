package it.polimi.ingsw.am32.network;



public interface RMIClientAccextorInt {
    GameTuple createGame(RMIClientNodeInt node, int id, String creatorName);
    RMIServerNodeInt accessGame(RMIClientNodeInt node, int id, String playerName);
    RMIServerNodeInt ReconFromDeath(RMIClientNodeInt node, int id, String playerName);
}
