package com.fwumdesoft.fwumdestego;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.fwumdesoft.api.io.Resource;

public class CommandLine
{
	public static void main(String[] args)
	{
		if(args.length == 0 || args[0].equals("help"))
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(new File("help")));
				String line;
				while((line = reader.readLine()) != null)
					System.out.println(line);
				reader.close();
			}
			catch(IOException e)
			{
				System.out.println("Unexpected filesystem error occured.");
			}
			return;
		}
		if(args[0].equals("encode"))
			encode(args);
		else if(args[0].equals("decode"))
			decode(args);
		else 
			System.out.println("Command unknown. See help for more details.");
	}
	
	private static void encode(String[] args)
	{
		if(args.length != 3)
		{
			System.out.println("Invalid number of parameters for command. See help for more details.");
			return;
		}
		String imagepath = "", text = "", contents;
		BufferedImage image = null;
		imagepath = args[1];
		try
		{
			image = ImageIO.read(new File(imagepath));
		} catch(IOException e)
		{
			System.out.println("Unexpected error occured while reading image. Check to make sure the path is correct.");
			return;
		}
		text = args[2];
		try
		{
			contents = Resource.readAllText(new File(text));
		} catch(IOException e) 
		{
			System.out.println("Unexpected error occured while reading input file. Check to make sure the path is correct.");
			return;
		}
		try
		{
			image = FStego.write(image, contents);
		} catch(IllegalArgumentException e)
		{
			System.out.println("The image is too small for the contents of the input file.");
			return;
		}
		try
		{
			ImageIO.write(image, ".png", new File("output.png"));
			System.out.println("output.png has been created.");
		} catch (IOException e)
		{
			System.out.println("Unexpected error occured while writing to output file.");
		}
	}
		
	private static void decode(String[] args)
	{
		if(args.length != 3)
		{
			System.out.println("Invalid number of parameters for command. See help for more details.");
		}
		BufferedImage image;
		String imagepath = args[1], text;
		try
		{
			image = ImageIO.read(new File(imagepath));
		} catch (IOException e)
		{
			System.out.println("Unexpected error occured while reading image. Check to make sure the path is correct.");
			return;
		}
		text = FStego.read(image);
		String outpath = args[2];
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outpath)));
			writer.write(text);
			writer.close();
		}
		catch (IOException e)
		{
			System.out.println("Unexpected error occured while writing to output file.");
		}
		System.out.println(text);
	}
}
