package it.polimi.ingsw.am32.network.exceptions;

/**
 * A simple Exception that extends {@code Throwable} and add nothing else. <br>
 * Used to notify the {@link it.polimi.ingsw.am32.network.ClientAcceptor.SKClientAcceptor} that the instantiation
 * of a {@link it.polimi.ingsw.am32.network.ClientNode.SKClientNode} was unsuccessful.
 *
 * @author Matteo
 */
public class UninitializedException extends Throwable {}