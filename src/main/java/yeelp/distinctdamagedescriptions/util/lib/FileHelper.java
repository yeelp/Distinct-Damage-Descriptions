package yeelp.distinctdamagedescriptions.util.lib;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;

import java.io.*;

public final class FileHelper {
	private static final String NEW_LINE = System.lineSeparator();

	public static boolean copyFile(File src, File dest, boolean overwrite) throws IOException {
		try(BufferedReader input = new BufferedReader(new FileReader(src))) {
			return copyFile(input, dest, overwrite);
		}
	}

	public static boolean copyFile(InputStream stream, File dest, boolean overwrite) throws IOException {
		try(BufferedReader input = new BufferedReader(new InputStreamReader(stream))) {
			return copyFile(input, dest, overwrite);
		}
	}

	public static boolean copyFile(BufferedReader reader, File dest, boolean overwrite) throws IOException {
		if(dest.exists()) {
			if(overwrite) {
                //noinspection ResultOfMethodCallIgnored
                dest.delete();
			}
			else {
				return false;
			}
		}
		else {
            //noinspection ResultOfMethodCallIgnored
            dest.createNewFile();
		}

		try(FileWriter output = new FileWriter(dest)) {
			reader.lines().forEach((s) -> {
				try {
					output.write(s + NEW_LINE);
				}
				catch(IOException e) {
					DistinctDamageDescriptions.logStackTrace(e, DistinctDamageDescriptions::err);
				}
			});
		}
		return true;
	}
}
