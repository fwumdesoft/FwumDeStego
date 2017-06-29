#  BasicStego
A steganography tool for images that has little to no visible effect. <br>
##  What is steganography?
Steganography is the process of hiding data in plain sight. <br>
An analog example would be invisible ink. <br>
This tool subtly tweaks the pixels of an image in a way that is almost invisible to the human eye, but still encodes the messages.
##  How does this work?
First, the image is "sanitized." All rgb values are set to the highest even number less than or equal to themselves. <br>
For example, a pixel with (r = 2, b = 5, g = 7) become (r = 2, b = 4, g = 6)
###  Writing
The write method takes a string and an image.
A string is taken character by character. Each character is broken into a sixteen-character bit string. <br>
Then the program writes the bit strings to the image by increasing one of the rgb values if the string has a 'true.'
For example, mapping the bit string [010] onto (r = 2, b = 4, g = 6) makes it (r = 2, b = 5, g = 6)
At the end, a null character ('\0') is added to act as a delimiter.
###  Reading
The read method takes an image. <br>
A copy of the image is made and sanitized for analysis. Wherever the images don't match, an array of flags is marked. <br>
Once the images have been analyzed, the array is checked at 16 character intervals. <br>
It is converted from a bit string to an int, and then a character. <br>
If the character is not a null character ('\0') then it is added to the end of a string. <br>
If it is the null character, the reading is complete and the string is returned.
