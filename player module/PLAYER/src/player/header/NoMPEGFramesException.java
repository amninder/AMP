/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player.header;

/**
 *
 * @author Amninder Singh
 */
public class NoMPEGFramesException extends Exception {
 public NoMPEGFramesException() {
	super("The file specified is not a valid MPEG.");
    }
 public NoMPEGFramesException( String msg ) {
	super( msg );
 }
}
