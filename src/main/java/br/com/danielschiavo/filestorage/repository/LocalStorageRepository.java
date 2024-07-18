package br.com.danielschiavo.filestorage.repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.Optional;

import br.com.danielschiavo.filestorage.LocalStorageUtil;
import br.com.danielschiavo.filestorage.exception.FileNotFoundException;
import br.com.danielschiavo.filestorage.exception.FileStorageException;
import org.springframework.stereotype.Service;


@Service
public class LocalStorageRepository {

	public void delete(Path path, String fileName) {
		showCurrentDirectory();

		try {
			boolean wasDeleted = Files.deleteIfExists(path.resolve(fileName));
			if (!wasDeleted)
				throw new FileNotFoundException("The file does not exist, so it could not be deleted");
		} catch (IOException e) {
			throw new FileStorageException("Unable to delete image", Map.of(fileName, e.getMessage()));
		}
	}

	public void save(Path path, String fileName, byte[] bytes) {
		showCurrentDirectory();

		try {
			Files.write(path.resolve(fileName), bytes, StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			throw new FileStorageException("Unable to save image to disk", Map.of(fileName, e.getMessage()));
		}
	}

	public byte[] get(Path path, String fileName) {
		showCurrentDirectory();

		try {
			boolean exists;
			if (fileName == null) {
				exists = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
			} else {
				exists = Files.exists(path.resolve(fileName), LinkOption.NOFOLLOW_LINKS);
			}

			if (!exists)
				throw new FileNotFoundException("The specified file does not exist");

			byte[] allBytes = Files.readAllBytes(path.resolve(fileName));
			return LocalStorageUtil.encodeToBase64(allBytes);
		} catch (IOException e) {
			throw new FileStorageException("Unable to retrieve the image", Map.of(fileName, e.getMessage()));
		}
	}

	public Optional<byte[]> verifyIfFileExists(Path path, String fileName, String startsWith, String endsWith) {
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + startsWith + fileName + endsWith);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path entry : stream) {
				if (matcher.matches(entry.getFileName())) {
					return Optional.of(get(entry, null));
				}
			}
			return Optional.empty();
		} catch (IOException e) {
			throw new FileStorageException("Failed to retrieve image from disk.", e);
		}
	}

	public void showCurrentDirectory() {
		String currentDirectory = System.getProperty("user.dir");
		System.out.println("The current directory is: " + currentDirectory);
	}

}