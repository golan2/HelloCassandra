package golan.izik.log;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class FilesInFolder {


    public static void main(String[] args) throws IOException {

        Path rootFolder = Paths.get("C:\\Users\\golaniz\\Desktop\\DuplicateFiles");
        CassandraSinkVisitor visitor = new CassandraSinkVisitor(30);
        Files.walkFileTree(rootFolder, visitor);
        visitor.close();

    }

    private static class CassandraSinkVisitor implements FileVisitor<Path> {

        private final AtomicInteger maxFiles;
        private final Cluster cluster;
        private final Session session;

        CassandraSinkVisitor(int maxFiles) {
            this.maxFiles = new AtomicInteger(maxFiles);
            this.cluster = Cluster.builder().addContactPoint(CassandraConstants.HOST).build();
            this.session = cluster.connect(CassandraConstants.KEYSPACE);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
            if (maxFiles.getAndDecrement()==0) {
                return FileVisitResult.TERMINATE;
            }

            long eventtimestamp = System.currentTimeMillis();



            throw new RuntimeException("UNIMPLEMENTED - Need2Write2Cassandra");
//            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
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
