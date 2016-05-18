package com.dhemery.aggregator.helpers;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Dirs {
    private static void create(Path dir) {
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create directory " + dir);
        }
    }

    public static void createEmpty(Path dir) {
        delete(dir);
        create(dir);
    }

    public static Path createTemporary() {
        try {
            Path tmpDir = Files.createTempDirectory(null).toAbsolutePath();
            tmpDir.toFile().deleteOnExit();
            return tmpDir;
        } catch (IOException e) {
            throw new RuntimeException("Cannot create temporary directory", e);
        }
    }

    public static void delete(Path dir) {
        if (!Files.exists(dir)) return;
        if (!Files.isDirectory(dir))
            throw new RuntimeException("Refusing to delete non-directory " + dir);
        try {
            Files.walkFileTree(dir, deleteRecursively());
        } catch (IOException e) {
            throw new RuntimeException("Error deleting " + dir, e);
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
