package org.spigotmc.patcher;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import java.io.File;
import net.md_5.jbeat.Patcher;

public class Main
{

    public static void main(String[] args) throws Exception
    {
        if ( args.length != 3 )
        {
            System.out.println( "Welcome to the Spigot patch applicator." );
            System.out.println( "In order to use this tool you will need to specify three command line arguments as follows:" );
            System.out.println( "\tjava -jar SpigotPatcher.jar original.jar patch.bps output.jar" );
            System.out.println( "This will apply the specified patch to the original jar and save it to the output jar" );
            System.out.println( "Please ensure that you save your original jar for later use." );
            System.out.println( "If you have any queries, please direct them to http://www.spigotmc.org/" );
            return;
        }

        File originalFile = new File( args[0] );
        File patchFile = new File( args[1] );
        File outputFile = new File( args[2] );

        if ( !originalFile.canRead() )
        {
            System.err.println( "Specified original file " + originalFile + " does not exist or cannot be read!" );
            return;
        }
        if ( !patchFile.canRead() )
        {
            System.err.println( "Specified patch file " + patchFile + " does not exist or cannot be read!!" );
            return;
        }
        if ( outputFile.exists() )
        {
            System.err.println( "Specified output file " + outputFile + " exists, please remove it before running this program!" );
            return;
        }
        if ( !outputFile.createNewFile() )
        {
            System.out.println( "Could not create specified output file " + outputFile + " please ensure that it is in a valid directory which can be written to." );
            return;
        }

        System.out.println( "***** Starting patching process, please wait." );
        System.out.println( "\tInput md5 Checksum: " + Files.hash( originalFile, Hashing.md5() ) );
        System.out.println( "\tPatch md5 Checksum: " + Files.hash( patchFile, Hashing.md5() ) );

        try
        {
            new Patcher( patchFile, originalFile, outputFile ).patch();
        } catch ( Exception ex )
        {
            System.err.println( "***** Exception occured whilst patching file!" );
            ex.printStackTrace();
            outputFile.delete();
            return;
        }

        System.out.println( "***** Your file has been patched and verified! We hope you enjoy using Spigot!" );
        System.out.println( "\tOutput md5 Checksum: " + Files.hash( outputFile, Hashing.md5() ) );
    }
}
