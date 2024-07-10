package br.com.danielschiavo.filestorage.dto.response;

public record FileInfoResponse(
		String fileName,
		StatusFileInfoResponse status,
		String message,
		byte[] content) {

	public static FileInfoResponse success(String fileName, String message, byte[] content) {
		return new FileInfoResponse(fileName, StatusFileInfoResponse.SUCCESS, message, content);
	}

	public static FileInfoResponse error(String fileName, String message, byte[] content) {
		return new FileInfoResponse(fileName, StatusFileInfoResponse.ERROR, message, content);
	}
}
