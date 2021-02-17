package yeelp.distinctdamagedescriptions.util.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class FileHelper
{
	private static final String NEW_LINE = System.lineSeparator();
	public static boolean copyFile(File src, File dest, boolean overwrite) throws IOException
	{
		try(BufferedReader input = new BufferedReader(new FileReader(src)))
		{
			return copyFile(input, dest, overwrite);
		}
	}
	
	public static boolean copyFile(InputStream stream, File dest, boolean overwrite) throws IOException
	{
		try(BufferedReader input = new BufferedReader(new InputStreamReader(stream)))
		{
			return copyFile(input, dest, overwrite);
		}
	}
	
	public static boolean copyFile(BufferedReader reader, File dest, boolean overwrite) throws IOException
	{
		if(dest.exists())
		{
			if(overwrite)
			{
				dest.delete();
			}
			else
			{
				return false;
			}
		}
		else
		{
			dest.createNewFile();
		}
		
		try(FileWriter output = new FileWriter(dest))
		{
			reader.lines().forEach((s) -> {
				try
				{
					output.write(s + NEW_LINE);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			});
		}
		return true;
	}
}
