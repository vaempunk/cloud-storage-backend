package com.vaem.cloudstorage.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

import lombok.Data;

@Data
public class File implements Resource {
    
    private Path path;

    private byte[] content;

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public URL getURL() throws IOException {
        return new URL(path.toString());
    }

    @Override
    public URI getURI() throws IOException {
        return path.toUri();
    }

    @Override
    public java.io.File getFile() throws IOException {
        return new java.io.File(path.toString());
    }

    @Override
    public long contentLength() throws IOException {
        return content.length;
    }

    @Override
    public long lastModified() throws IOException {
        return 0;
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        throw new IOException("Can not create relative Resource");
    }

    @Override
    @Nullable
    public String getFilename() {
        return path.getFileName().toString();
    }

    @Override
    public String getDescription() {
        return "";
    }

}
