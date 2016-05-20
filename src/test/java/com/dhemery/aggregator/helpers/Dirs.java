package com.dhemery.aggregator.helpers;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class Dirs {
    private static void create(Path dir) {
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create directory " + dir);
        }
    }

    static void createEmpty(Path dir) {
        delete(dir);
        create(dir);
    }

    static Path createTemporary() {
        try {
            Path tmpDir = Files.createTempDirectory(null).toAbsolutePath();
            tmpDir.toFile().deleteOnExit();
            return tmpDir;
        } catch (IOException e) {
            throw new RuntimeException("Cannot create temporary directory", e);
        }
    }

    private static void delete(Path path) {
        if (!Files.exists(path)) return;
        if (!Files.isDirectory(path))
            throw new RuntimeException("Refusing to delete non-directory " + path);
        try {
            Files.walkFileTree(path, deleteRecursively());
        } catch (IOException e) {
            throw new RuntimeException("Error deleting " + path, e);
        }
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
