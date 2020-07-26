package yeelp.distinctdamagedescriptions.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class FileHelper
{
	public static boolean copyFile(File src, File dest, boolean overwrite) throws IOException
	{
		try(InputStream input = new FileInputStream(src))
		{
			return copyFile(input, dest, overwrite);
		}
	}
	
	public static boolean copyFile(InputStream stream, File dest, boolean overwrite) throws IOException
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
		
		try(FileOutputStream output = new FileOutputStream(dest))
		{
			int readBytes;
			byte[] buf = new byte[4096];
			while((readBytes = stream.read(buf)) > 0)
			{
				output.write(buf);
			}
		}
		return true;
	}
}
