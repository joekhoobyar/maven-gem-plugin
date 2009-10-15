package com.ankhcraft.maven.artifact.repository.layout;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;

/**
 * @author jkhoobyar
 */
public class GemRepositoryLayout
    implements ArtifactRepositoryLayout
{
    private static final String PATH_SEPARATOR = "/";

    public String pathOf( Artifact artifact )
    {
        ArtifactHandler artifactHandler = artifact.getArtifactHandler();

        StringBuffer path = new StringBuffer();

        path.append( artifactHandler.getDirectory() ).append( PATH_SEPARATOR );
        path.append( artifact.getArtifactId() ).append( '-' ).append( artifact.getVersion() );

        if ( artifact.hasClassifier() )
        {
            path.append( '-' ).append( artifact.getClassifier() );
        }

        if ( artifactHandler.getExtension() != null && artifactHandler.getExtension().length() > 0 )
        {
            path.append( '.' ).append( artifactHandler.getExtension() );
        }

        return path.toString();
    }

    public String pathOfLocalRepositoryMetadata( ArtifactMetadata metadata, ArtifactRepository repository )
    {
        return "quick/" + metadata.getLocalFilename (repository);
    }

    public String pathOfRemoteRepositoryMetadata( ArtifactMetadata metadata )
    {
        return "quick/" + metadata.getRemoteFilename ();
    }

}
