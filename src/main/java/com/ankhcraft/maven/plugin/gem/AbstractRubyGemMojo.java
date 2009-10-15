/**
 * Copyright 2009. Joe Khoobyar.  All Rights Reserved.
 */
package com.ankhcraft.maven.plugin.gem;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.DirectoryScanner;

public abstract class AbstractRubyGemMojo
	extends AbstractMojo
{

  /**
   * The Maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * @component
   */
  private MavenProjectHelper projectHelper;

	/**
	 * Directory containing the generated GEM.
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	protected File outputDirectory;

	/**
	 * Name of the generated GEM.
	 * @parameter alias="gemFinalName" expression="${gem.finalName}" default-value="${project.build.finalName}"
	 * @required
	 */
	protected String finalName;

	/**
	 * Name of the GEM, without the version.
	 * @parameter alias="gemBaseName" expression="${gem.baseName}" default-value="${project.artifactId}"
	 * @required
	 */
	protected String baseName;

	/**
	 * GEM version.
	 * @parameter alias="gemVersion" expression="${gem.version}" default-value="${project.version}"
	 * @required
	 */
	protected String version;

	/**
	 * Whether creating the archive should be forced.
	 * @parameter expression="${gem.forceCreation}" default-value="false"
	 */
	protected boolean forceCreation;

	/**
	 * List of files to include in the lib sub-directory.
	 * @parameter
	 */
	protected String[] libIncludes = { "**/**" };

	/**
	 * List of files to exclude from the lib sub-directory.
	 * @parameter
	 */
	protected String[] libExcludes = {};

	/**
	 * List of files to include in the bin sub-directory.
	 * @parameter
	 */
	protected String[] binIncludes = { "**/**" };

	/**
	 * List of files to exclude from the bin sub-directory.
	 * @parameter
	 */
	protected String[] binExcludes = {};

	/**
	 * List of files to include in the test sub-directory.
	 * @parameter
	 */
	protected String[] testIncludes = { "**/**" };

	/**
	 * List of files to exclude from the test sub-directory.
	 * @parameter
	 */
	protected String[] testExcludes = {};

	/**
	 * List of files to include in the base directory.
	 * @parameter
	 */
	protected String[] extraIncludes = { "**/**" };

	/**
	 * List of files to exclude from the test sub-directory.
	 * @parameter
	 */
	protected String[] extraExcludes = {};

	/**
	 * Directory containing the ruby source to be output under ./lib
	 * @parameter expression="${gem.libsourceDirectory}" default-value="${project.basedir}/src/main/ruby"
	 * @required
	 */
	protected String libSourceDirectory;

	/**
	 * Directory containing the ruby source to be output under ./bin
	 * @parameter expression="${gem.binSourceDirectory}" default-value="${project.build.scriptSourceDirectory}"
	 * @required
	 */
	protected String binSourceDirectory;

	/**
	 * Optional directory containing the ruby source to be output under ./test
	 * @parameter expression="${gem.testSourceDirectory}" default-value="${project.basedir}/src/test/ruby"
	 */
	protected String testSourceDirectory;

	/** Extra directories to be output under ./
   *  @parameter
   */
	protected String extraBaseDirectories[] = {};

	/** Executable files (defaults to all files under ./bin)
   *  @parameter
   */
	protected String executables[];

	/** Default executable
   *  @parameter
   */
	protected String defaultExecutable;

	protected static File getGemFile(File basedir, String finalName, String classifier, String extension) {
	    if ( classifier == null )
	        classifier = "";
	    else if ((classifier = classifier.trim ()).length () > 0 && classifier.charAt (0) != '-')
	        classifier = "-" + classifier;
	    if (extension == null || (extension = extension.trim ()).length () == 0)
	    	extension = "gem";
	    return new File( basedir, finalName + classifier + "." + extension );
	}

  protected String[] scanDirectory (String base, String includes[], String excludes[]) 
    throws MojoExecutionException 
  {
    File dir = new File (base);
    if ( ! dir.isDirectory ())
      throw new MojoExecutionException (dir.getAbsolutePath () + " isn't a directory.");
    DirectoryScanner scanner = new DirectoryScanner ();
    scanner.setIncludes (includes);
    scanner.setExcludes (excludes);
    scanner.addDefaultExcludes ();
    scanner.setBasedir (dir);
    scanner.scan ();
    return scanner.getIncludedFiles ();
  }

	protected final MavenProject getProject() { return project; }

	protected final MavenProjectHelper getProjectHelper() { return projectHelper; }
}
