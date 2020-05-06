**Hashing Techniques<br />**
we use Average hashing and Difference hashing methods to find hash values corresponding to images. <br />
Image similarity is found by comparing hash values by computing hamming distance. <br />

***Average hashing:* <br />***
step 1:Convert the image to grayscale.<br />
step 2:Resize to 8x8 image.<br />
step 3:Compute mean of pixels<br />
step 4:Calculate hash value<br />

***Difference hashing:<br />***
step 1:Convert the image to grayscale.<br />
step 2:Resize to 8x8 image.<br />
step 3:Compute difference between adjacent pixel values<br />
step 4:Calculate hash value<br />
