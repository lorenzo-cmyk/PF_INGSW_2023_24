package it.polimi.ingsw.am32.network.exceptions;

/**
 * A simple Exception that extends {@code Throwable} and add nothing else. <br>
 * Used to notify the caller that the message given to the Node (Server or Client) could not be sent.
 *
 * @author Matteo
 */
public class UploadFailureException extends Throwable{}