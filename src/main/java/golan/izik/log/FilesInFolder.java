package golan.izik.log;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

class FilesInFolder {
    private static final int MAX_FILES = 30;

    public static void main(String[] args) throws IOException {
        if (args[0]==null || args[0].length()==0) throw new IllegalArgumentException("Missing first parameter representing the path to scan.");
        Path rootFolder = Paths.get(args[0]);
        CassandraSinkVisitor visitor = new CassandraSinkVisitor();
        Files.walkFileTree(rootFolder, visitor);
        visitor.close();
    }

    private static class CassandraSinkVisitor implements FileVisitor<Path> {
        private final AtomicInteger maxFiles;
        private final Cluster cluster;
        private final Session session;

        CassandraSinkVisitor() {
            this.maxFiles = new AtomicInteger(MAX_FILES);
            this.cluster = Cluster.builder().addContactPoint("cassandra").build();
            this.session = cluster.connect("activity");
        }

        @Override
        public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
            if (maxFiles.getAndDecrement()==0) {
                return FileVisitResult.TERMINATE;
            }
            throw new RuntimeException("UNIMPLEMENTED - Need2Write2Cassandra");
        }

        @Override
        public FileVisitResult visitFileFailed(Path path, IOException e) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path path, IOException e) {
            return FileVisitResult.CONTINUE;
        }

        void close() {
            try {
                cluster.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
