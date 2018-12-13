import java.io.*;
import java.util.Scanner;
import java.util.regex.*;

public class BlockCipher {
	public static void main(String[] args) {
		// http://www.cis.twcu.ac.jp/~asakawa/java_intro/files.html
		java.util.Scanner stdin = new Scanner(System.in);
		System.out.println("File name you want to encrypt (or decrypt) ?");
		String fileName = stdin.nextLine();
		if(!(new File(fileName)).exists()) {
			System.out.println("No such file");
			stdin.close();
			return;
		}
		System.out.println("Key string?");
		String keyStr = String.valueOf(System.console().readPassword());
		System.out.println("Retype key (input empty to skip)");
		String retypeKeyStr = String.valueOf(System.console().readPassword());
		stdin.close();
		if(retypeKeyStr.length() > 0 && keyStr != retypeKeyStr) {
			System.out.println("Key mismatch");
			return;
		}
		byte[] key = keyStr.getBytes();
		Pattern regex_filename = Pattern.compile(".encrypted$");
		try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(fileName));
			byte[] buffer = readAll(inputStream);
			inputStream.close();
			// System.out.println(key.length);
			String outFileName;
			if(regex_filename.matcher(fileName).find()) {
				outFileName = "_" + fileName.replaceAll(".encrypted$", "");
			} else {
				outFileName = fileName + ".encrypted";
			}
			PrintStream outputStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(outFileName)));
			writeBytes(encrypt(buffer, key), outputStream);
			outputStream.close();
		} catch(IOException err) {
			System.out.println(err);
		}
	}
	public static byte[] readAll(InputStream inputStream) throws IOException {
		// http://d.hatena.ne.jp/muupan/20120304/1330834324
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		while(true) {
			int len = inputStream.read(buffer);
			if(len < 0) {
				break;
			}
			bout.write(buffer, 0, len);
		}
		return bout.toByteArray();
	}
	public static void writeBytes(byte[] buffer, PrintStream output) {
		// http://www.geocities.jp/inu_poti/java/file/FileOutputStream.html
		for(int i = 0; i < buffer.length; i++) {
			output.write(buffer[i]);
		}
	}
	public static byte[] encrypt(byte[] buffer, byte[] key) {
		byte[] ret = new byte[buffer.length];
		for(int i = 0; buffer.length > key.length * i; i++) {
			for(int j = 0; j < key.length; j++) {
				int index = key.length * i + j;
				if(index >= buffer.length)
					break;
				ret[index] = (byte)(buffer[index] ^ key[j]);
			}
		}
		return ret;
	}
}
