package org.spigotmc.patcher;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import net.md_5.jbeat.Patcher;

public class Main
{

    public static void main(String[] args)
    {
        if ( !GraphicsEnvironment.isHeadless() && args.length == 0 )
        {
            UserInterface.main( args );
            return;
        }

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

        patchSafe( new PrintWriter( System.out ), new File( args[0] ), new File( args[1] ), new File( args[2] ) );
    }

    @SuppressWarnings("TooBroadCatch")
    public static void patchSafe(PrintWriter console, File originalFile, File patchFile, File outputFile)
    {
        try
        {
            patch( console, originalFile, patchFile, outputFile );
        } catch ( Exception ex )
        {
            console.println( "***** Unknown error occured during patch process:" );
            ex.printStackTrace( console );
        }
    }

    @SuppressWarnings("TooBroadCatch")
    public static void patch(PrintWriter console, File originalFile, File patchFile, File outputFile) throws IOException
    {
        if ( !originalFile.canRead() )
        {
            console.println( "Specified original file " + originalFile + " does not exist or cannot be read!" );
            return;
        }
        if ( !patchFile.canRead() )
        {
            console.println( "Specified patch file " + patchFile + " does not exist or cannot be read!!" );
            return;
        }
        if ( outputFile.exists() )
        {
            console.println( "Specified output file " + outputFile + " exists, please remove it before running this program!" );
            return;
        }
        if ( !outputFile.createNewFile() )
        {
            console.println( "Could not create specified output file " + outputFile + " please ensure that it is in a valid directory which can be written to." );
            return;
        }

        console.println( "***** Starting patching process, please wait." );
        console.println( "\tInput md5 Checksum: " + Files.hash( originalFile, Hashing.md5() ) );
        console.println( "\tPatch md5 Checksum: " + Files.hash( patchFile, Hashing.md5() ) );

        try
        {
            new Patcher( patchFile, originalFile, outputFile ).patch();
        } catch ( Exception ex )
        {
            console.println( "***** Exception occured whilst patching file!" );
            ex.printStackTrace( console );
            outputFile.delete();
            return;
        }

        console.println( "***** Your file has been patched and verified! We hope you enjoy using Spigot!" );
        console.println( "\tOutput md5 Checksum: " + Files.hash( outputFile, Hashing.md5() ) );
    }
}
