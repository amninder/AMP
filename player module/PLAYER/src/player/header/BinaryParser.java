/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player.header;

/**
 *
 * @author Amninder Singh
 */
public class BinaryParser {
     
    private final static int NUM_BYTES = 4;
    private final static int NUM_BITS = 8;
    
        public static boolean bitSet( byte b, int pos ) {
	boolean retval = false;

	if( (pos >= 0) && (pos < NUM_BITS) ) {
	    retval = ((b & (byte)(1 << pos)) != 0);
	}

	return retval;
    }
        
            public static boolean matchPattern( byte b, String pattern ) {
	boolean retval = true;

	for( int i = 0; (i < NUM_BITS) && (i < pattern.length()) 
		 && retval; i++ ) {
  
	    if( pattern.charAt(i) == '1' ) {
		retval = retval && bitSet( b, NUM_BITS - i - 1 );
	    }
	    else if( pattern.charAt(i) == '0' ) {
		retval = retval && !bitSet( b, NUM_BITS - i - 1 );
	    }
	}

	return retval;
    }
            
                public static int convertToDecimal( byte b, int start, int end ) {
	byte ret = 0;
	int bCount = 0;
	int s = start;
	int e = end;

	if( (start < 0) || (start >= NUM_BITS) ) {
	    s = 0;
	}

	if( (end < 0) || (end >= NUM_BITS) ) {
	    e = NUM_BITS - 1;
	}

	if( start > end ) {
	    s = end;
	    e = start;
	}

	for( int i = s; i <= e; i++ ) {
	    if( bitSet( b, i ) ) {
		ret = setBit( ret, bCount );
	    }
	    
	    bCount++;
	}

	return ret;
    }
                    public static int convertToInt( byte[] b ) {
	int retval = 0;
	int pos = 0;
	int start = b.length - 1;
      
	if( start >= NUM_BYTES ) {
	    start = NUM_BYTES - 1;
	}

	
	for( int i = start; i >= 0; i-- ) {
	    for( int j = 0; j < NUM_BITS; j++ ) {
		if( bitSet( b[i], j ) ) {
		    retval += Math.pow( 2, pos );
		}
		
		pos++;
	    }
	}

	return retval;
    }
                    
        public static byte[] convertToBytes( int num ) {
	byte[] b = { 0x00, 0x00, 0x00, 0x00 };
	int count = num;
	boolean done = false;

	for( int i = b.length - 1; (i >= 0) && !done; i-- ) {
	    for( int j = 0; (j < NUM_BITS) && !done; j++ ) {
		if( (count % 2) == 1 ) {
		    b[i] = setBit( b[i], j );
		}

		count = count / 2;
		done = (count == 0);
	    }
	}

	return b;
    }
  
        public static byte setBit( byte b, int location ) {
	byte ret = 0;

	if( (location >= 0) && (location < NUM_BITS) ) {
	    ret = (byte)(b | (byte)(1 << location));
	}

	return ret;
    }

}
