package com.fwumdesoft.fwumdephotos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CommandLine
{
	public static void main(String[] args) throws IOException
	{
		BufferedImage image = ImageIO.read(new File("before.png"));
		image = FwumDePhotos.write(image, "An aweseome new program!");
		System.out.println(FwumDePhotos.read(image));
	}
}
