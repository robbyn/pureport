package org.librebiz.pureport.run;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PageStore {
    private static final Logger LOG
            = Logger.getLogger(PageStore.class.getName());

    private File pageFile;
    private RandomAccessFile file;
    private final List<Long> pagePositions = new ArrayList<Long>();
    private boolean delete;

    public PageStore() {
       this(createTempFile());
       delete = true;
    }

    public PageStore(File file) {
        try {
            this.pageFile = file;
            this.file = new RandomAccessFile(pageFile, "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void close() {
        try {
            file.close();
            pagePositions.clear();
            if (delete) {
                pageFile.delete();
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error closing page store", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public void clear() {
        try {
            file.setLength(0);
            pagePositions.clear();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error clearing page store", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public int getPageCount() {
        return pagePositions.size();
    }

    public Page getPage(int index) {
        try {
            long offset = pagePositions.get(index);
            file.seek(offset);
            InputStream in = new BufferedInputStream(
                    new RandomInputStream(file));
            try {
                ObjectInputStream ois = new ObjectInputStream(in);
                Page page = (Page)ois.readObject();
                return page;
            } finally {
                in.close();
            }
        } catch (ClassNotFoundException e) {
            LOG.log(Level.SEVERE, "Error loading page", e);
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error loading page", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public void addPage(Page page) {
        try {
            long offset = file.length();
            file.seek(offset);
            OutputStream out = new BufferedOutputStream(
                    new RandomOutputStream(file));
            try {
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(page);
                oos.flush();
            } finally {
                out.close();
            }
            pagePositions.add(offset);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error adding page", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private static File createTempFile() {
        try {
            return File.createTempFile("fmt", ".pg");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error creating temp file", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private static class RandomInputStream extends InputStream {
        private final RandomAccessFile file;

        RandomInputStream(RandomAccessFile file) {
            this.file = file;
        }

        @Override
        public void close() {
        }

        @Override
        public int read() throws IOException {
            return file.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return file.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return file.read(b, off, len);
        }
    }

    private static class RandomOutputStream extends OutputStream {
        private final RandomAccessFile file;

        RandomOutputStream(RandomAccessFile file) {
            this.file = file;
        }

        @Override
        public void close() {
        }

        @Override
        public void write(int b) throws IOException {
            file.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            file.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            file.write(b, off, len);
        }
    }
}
