#!/usr/bin/env python

################################################################################
# AverageHash                                                                  #
################################################################################
# A program to calculate a hash of an image based on visual characteristics.   #
# Author: David J. Oftedal.                                                    #
#                                                                              #
# Depends on Python Imaging Library: http://www.pythonware.com/products/pil/   #
#                                                                              #
# Thanks to Dr. Neal Krawetz:                                                  #
# http://www.hackerfactor.com/blog/index.php?/archives/432-Looks-Like-It.html  #
# for the algorithm.                                                           #
################################################################################

from sys import argv
from sys import exit
from PIL import Image
from PIL import ImageStat

def AverageHash(theImage):

	# Convert the image to 8-bit grayscale.
	theImage = theImage.convert("L") # 8-bit grayscale

	# Squeeze it down to an 8x8 image.
	theImage = theImage.resize((8,8), Image.ANTIALIAS)

	# Calculate the average value.
	averageValue = ImageStat.Stat(theImage).mean[0]

	# Go through the image pixel by pixel.
	# Return 1-bits when the tone is equal to or above the average,
	# and 0-bits when it's below the average.
	averageHash = 0
	for row in range(8):
		for col in range(8):
			averageHash <<= 1
			averageHash |= 1 * ( theImage.getpixel((col, row)) >= averageValue)

	return averageHash

def loadImage(filename):

	try:
		theImage = Image.open(filename)
		theImage.load()
		return theImage
	except:
		print ("\nCouldn't open the image " + filename + ".\n")
		exit(1)

if __name__ == '__main__':

	if len(argv) == 2 or len(argv) == 3:
		image1 = loadImage(argv[1])
		hash1 = AverageHash(image1)
		print ("\nhash value: " + '%(hash)016x' %{"hash": hash1} + "\t" + argv[1])

	if len(argv) == 3:
		image2 = loadImage(argv[2])
		hash2 = AverageHash(image2)
		print("hash value: " '%(hash)016x' %{"hash": hash2} + "\t" + argv[2] + "\n")

		# XOR hash1 with hash2 and count the number of 1 bits to assess similarity.
		print (argv[1] + " and " + argv[2] + " are " + str(((64 - bin(hash1 ^ hash2).count("1"))*100.0)/64.0) + "% similar.")

	if len(argv) < 2 or len(argv) > 3:
		print ("\nTo get the hash of an image: python " + argv[0] + " <image name>")
		print ("To compare two images: python " + argv[0] + " <image 1> <image 2>\n")
		exit(1)		