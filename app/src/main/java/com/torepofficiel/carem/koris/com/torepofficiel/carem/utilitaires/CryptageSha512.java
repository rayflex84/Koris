package com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by macbook on 2/09/17.
 */

public class CryptageSha512 {

    private static MessageDigest digester;

    static {
        try {
            digester = MessageDigest.getInstance( "SHA-512" );
        } catch ( NoSuchAlgorithmException e ) {
            e.printStackTrace();
        }
    }

    public static String crypt( String str ) {
        if ( str == null || str.length() == 0 ) {
            throw new IllegalArgumentException( "String to encript cannot be null or zero length" );
        }

        digester.update( str.getBytes() );
        byte[] hash = digester.digest();
        StringBuffer hexString = new StringBuffer();
        for ( int i = 0; i < hash.length; i++ ) {
            if ( ( 0xff & hash[i] ) < 0x10 ) {
                hexString.append( "0" + Integer.toHexString( ( 0xFF & hash[i] ) ) );
            } else {
                hexString.append( Integer.toHexString( 0xFF & hash[i] ) );
            }
        }
        return hexString.toString();
    }

}
