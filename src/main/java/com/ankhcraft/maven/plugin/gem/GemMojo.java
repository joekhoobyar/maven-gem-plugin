/**
 * Copyright 2009. Joe Khoobyar.  All Rights Reserved.
 */
package com.ankhcraft.maven.plugin.gem;

import java.io.File;

import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.tar.TarArchiver;

/**
 * Mojo for creating a Ruby GEM archive from project sources.
 * 
 * @goal gem
 * @requiresDependencyResolution runtime
 *
 * @author Joe Khoobyar
 */
public class GemMojo
    extends AbstractGemMojo
{

	/**
	 * @param tgz
	 * @throws ArchiverException
	 */
	protected void configureDataArchive (TarArchiver tgz) throws ArchiverException {
		tgz.addDirectory (new File (libSourceDirectory), "lib/", libIncludes, libExcludes);
		tgz.addDirectory (new File (binSourceDirectory), "bin/", binIncludes, binExcludes);
		tgz.addDirectory (new File (testSourceDirectory), "test/", testIncludes, testExcludes);
		for (Object srcRoot : extraBaseDirectories)
		    tgz.addDirectory (new File ((String) srcRoot));
	}

}
