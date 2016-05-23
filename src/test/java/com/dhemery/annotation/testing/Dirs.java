package com.dhemery.annotation.testing;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class Dirs {
    private static void create(Path dir) throws IOException {
        Files.createDirectories(dir);
    }

    static void createEmpty(Path dir) throws IOException {
        delete(dir);
        create(dir);
    }

    static Path createTemporary() throws IOException {
        Path tmpDir = Files.createTempDirectory(null).toAbsolutePath();
        tmpDir.toFile().deleteOnExit();
        return tmpDir;
    }

    private static void delete(Path path) throws IOException {
        if (!Files.exists(path)) return;
        if (!Files.isDirectory(path))
            throw new RuntimeException("Refusing to delete non-directory " + path);
        Files.walkFileTree(path, deleteRecursively());
    }

    private static SimpleFileVisitor<Path> deleteRecursively() {
        return new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        };
    }
}
