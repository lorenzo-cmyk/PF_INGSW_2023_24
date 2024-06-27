package it.polimi.ingsw.am32.network.exceptions;

/**
 * A simple Exception that extends {@code Throwable} and add nothing else. <br>
 * Used to notify the caller that the Node (Server or Client) on which the method was invoked, is not usable at the
 * moment.
 *
 * @author Matteo
 */
public class NodeClosedException extends Throwable {}