package com.fwumdesoft.fwumdephotos;

import java.awt.Color;
import java.awt.image.BufferedImage;

public final class FwumDePhotos
{
	private FwumDePhotos() {}
	public static BufferedImage sanitize(BufferedImage img)
	{
		BufferedImage sanitized;
		sanitized = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		Color pixel, target;
		for(int i = 0; i < img.getWidth(); i++)
		{
			for(int j = 0; j < img.getHeight(); j++)
			{
				pixel = new Color(img.getRGB(i, j));
				int r, g, b;
				r = sanitize(pixel.getRed());
				g = sanitize(pixel.getGreen());
				b = sanitize(pixel.getBlue());
				target = new Color(r, g, b);
				sanitized.setRGB(i, j, target.getRGB());
			}
		}
		return sanitized;
	}
	
	private static int sanitize(int i)
	{
		return i - (i % 2);
	}
	
	public static int getMaxCharacters(BufferedImage img)
	{
		return img.getWidth() * img.getHeight() * 3 / 16;
	}
	
	public static BufferedImage write(BufferedImage img, String text)
	{
		if(getMaxCharacters(img) < text.length())
			throw new IllegalArgumentException("Too many characters for the image. Maximum characters: " + getMaxCharacters(img));
		BufferedImage out = sanitize(img);
		text += "\0";
		boolean[] pixels = new boolean[text.length() * 16];
		for(int i = 0; i < text.length(); i++)
		{
			char current = text.charAt(i);
			for(int j = 15; j >= 0; j--)
			{
				int n = (int) Math.pow(2, j);
				pixels[i * 16 + (15 - j)] = current >= n;
				if(current >= n)
					current -= n;
			}
		}
		int x, y, r, g, b;
		Color pixel, edited;
		for(int i = 0; i < pixels.length; i += 3)
		{
			x = (i / 3) % out.getWidth();
			y = (i / 3) / out.getHeight();
			pixel = new Color(out.getRGB(x, y));
			r = pixel.getRed() + (pixels[i] ? 1 : 0);
			g = pixel.getGreen();
			if(i + 1 < pixels.length)
				g += (pixels[i + 1] ? 1 : 0);
			b = pixel.getBlue();
			if(i + 2 < pixels.length)
				b += (pixels[i + 2] ? 1 : 0);
			edited = new Color(r, g, b);
			out.setRGB(x, y, edited.getRGB());
		}
		return out;
	}
	
	public static String read(BufferedImage img)
	{
		BufferedImage orig = sanitize(img);
		String text = "";
		int x, y;
		Color original, encoded;
		for(int i = 0; i < img.getWidth() * img.getHeight(); i += 16)
		{
			int next = 0;
			boolean[] flags = new boolean[16];
			for(int j = 0; j < flags.length; j += 3)
			{
				x = ((i + j) / 3) % img.getWidth();
				y = ((i + j) / 3) / img.getHeight();
				original = new Color(orig.getRGB(x, y));
				encoded = new Color(img.getRGB(x, y));
				flags[j] = original.getRed() != encoded.getRed();
				if(j + 1 < flags.length)
					flags[j + 1] = original.getGreen() != encoded.getGreen();
				if(j + 2 < flags.length)
					flags[j + 2] = original.getBlue() != encoded.getBlue();
			}
			for(int j = 0; j < flags.length; j++)
			{
				next += flags[j] ? (int)Math.pow(2, 15 - j) : 0;
			}
			if((char)next != '\0')
				text += (char)next;
			else
				break;
		}
		return text.substring(0, text.length() - 1);
	}
}
