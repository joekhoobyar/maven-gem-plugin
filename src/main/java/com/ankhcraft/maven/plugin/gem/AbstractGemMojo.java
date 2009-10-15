/**
 * Copyright 2009. Joe Khoobyar.  All Rights Reserved.
 */
package com.ankhcraft.maven.plugin.gem;

import java.io.File;

import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.gzip.GZipArchiver;
import org.codehaus.plexus.archiver.tar.TarArchiver;
import org.codehaus.plexus.archiver.tar.TarArchiver.TarCompressionMethod;

/**
 * Base class for creating a Ruby GEM archive.
 */
public abstract class AbstractGemMojo
    extends AbstractRubyGemMojo
{
	/**
   * Whether this is the main artifact of the current project.
   * 
   * @parameter default-value="true"
   */
  private boolean primaryArtifact;

  /**
   * The artifact handler manager.
   * 
   * @component
   */
  private ArtifactHandlerManager artifactHandlerManager;

  /**
   * Generates the GEM.
   */
  public void execute()
      throws MojoExecutionException
  {
      File gemFile = createArchive ();

      String classifier = getClassifier();
      if (classifier != null || ! primaryArtifact)
          getProjectHelper ().attachArtifact (getProject(), "gem", classifier, gemFile);
      else
          getProject ().getArtifact ().setFile (gemFile);
  }

	/** Generates the GEM. */
	protected File createArchive() throws MojoExecutionException {
	    try {
	    	TarArchiver tgz = new TarArchiver ();
	    	TarCompressionMethod tcomp = new TarCompressionMethod ();
	    	tcomp.setValue ("gzip");
	    	tgz.setCompression (tcomp);
	    	File gemDataFile = new File (outputDirectory, "data.tar.gz");
	        tgz.setDestFile (gemDataFile);
	        tgz.setForced (forceCreation);
	        configureDataArchive (tgz);
	        tgz.setIncludeEmptyDirs (false);
	        tgz.createArchive ();
	        
	        GZipArchiver gzip = new GZipArchiver ();
	        gzip.setDestFile (new File (outputDirectory, "metadata.gz"));
	        configureMetadataArchive (gzip);
	        gzip.createArchive ();
	        
	        File gemFile = getGemFile (outputDirectory, finalName, getClassifier (), "gem");
	    	tgz = new TarArchiver ();
	    	tcomp.setValue ("none");
	    	tgz.setCompression (tcomp);
	        tgz.setDestFile (gemFile);
	        tgz.setForced (forceCreation);
	        tgz.addFile (gemDataFile, "data.tar.gz");
	        tgz.addFile (new File (outputDirectory, "metadata.gz"), "metadata.gz");
	        tgz.createArchive ();
	
	        return gemFile;
	    }
	    catch ( Exception e )
	    {
	        // TODO: improve error handling
	        throw new MojoExecutionException( "Error assembling GEM", e );
	    }
	}

	/** Implement this to configure the data portion of the gem archive. */
    protected abstract void configureDataArchive (TarArchiver tgz) throws ArchiverException;

	/** Overload this to produce a gem with another classifier, for example a test-gem. */
    protected String getClassifier () { return null; }

	/**
	 * @param gzip 
	 * @throws ArchiverException
	 */
	protected void configureMetadataArchive(GZipArchiver gzip) throws ArchiverException {
		gzip.addFile (getGemFile (outputDirectory, finalName, getClassifier (), "gemspec"), "data.gz");
	}
}
