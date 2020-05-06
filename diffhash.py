#!/usr/bin/env python

################################################################################
# DifferenceHash                                                               #
################################################################################
# A program to calculate a hash of an image based on visual characteristics.   #
# Author: David J. Oftedal.                                                    #
#                                                                              #
# Depends on Python Imaging Library: http://www.pythonware.com/products/pil/   #
#                                                                              #
# Thanks to Dr. Neal Krawetz:                                                  #
# http://www.hackerfactor.com/blog/index.php?/archives/432-Looks-Like-It.html  #
# for the algorithm which formed the inspiration for this algorithm.           #
################################################################################

from sys import argv
from sys import exit
from PIL import Image
from PIL import ImageStat

def DifferenceHash(theImage):

	# Convert the image to 8-bit grayscale.
	theImage = theImage.convert("L") # 8-bit grayscale

	# Squeeze it down to an 8x8 image.
	theImage = theImage.resize((8,8), Image.ANTIALIAS)

	# Go through the image pixel by pixel.
	# Return 1-bits when a pixel is equal to or brighter than the previous
	# pixel, and 0-bits when it's below.

	# Use the 64th pixel as the 0th pixel.
	previousPixel = theImage.getpixel((0, 7))

	differenceHash = 0
	for row in range(0, 8, 2):

		# Go left to right on odd rows.
		for col in range(8):
			differenceHash <<= 1
			pixel = theImage.getpixel((col, row))
			differenceHash |= 1 * (pixel >= previousPixel)
			previousPixel = pixel

		row += 1

		# Go right to left on even rows.
		for col in range(7, -1, -1):
			differenceHash <<= 1
			pixel = theImage.getpixel((col, row))
			differenceHash |= 1 * (pixel >= previousPixel)
			previousPixel = pixel

	return differenceHash

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
		hash1 = DifferenceHash(image1)
		print ("\nhash value:" + '%(hash)016x' %{"hash": hash1} + "\t" + argv[1])

	if len(argv) == 3:
		image2 = loadImage(argv[2])
		hash2 = DifferenceHash(image2)
		print ("hash value:"+'%(hash)016x' %{"hash": hash2} + "\t" + argv[2] + "\n")

		# XOR hash1 with hash2 and count the number of 1 bits to assess similarity.
		print (argv[1] + " and " + argv[2] + " are " + str(((64 - bin(hash1 ^ hash2).count("1"))*100.0)/64.0) + "% similar.")

	if len(argv) < 2 or len(argv) > 3:
		print ("\nTo get the hash of an image: python " + argv[0] + " <image name>")
		print ("To compare two images: python " + argv[0] + " <image 1> <image 2>\n")
		exit(1)

	