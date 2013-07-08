/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player.header;

/**
 *
 * @author Amninder Singh
 */
public class ID3v2FormatException extends Exception {
        public ID3v2FormatException() {
	super("ID3v2 tag is not formatted correctly.");
    }
    public ID3v2FormatException( String msg ) {
	super( msg );
    }
}
