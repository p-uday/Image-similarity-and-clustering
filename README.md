**Hashing Techniques<br />**
we use Average hashing and Difference hashing methods to find hash values corresponding to images. <br />
Image similarity is found by computing hamming distance between hash values. <br />

***Average hashing:<br />***
step 1:Convert the image to grayscale.<br />
step 2:Resize to 8x8 image.<br />
step 3:Compute mean of pixels<br />
step 4:Calculate hash value<br />

***Difference hashing:<br />***
step 1:Convert the image to grayscale.<br />
step 2:Resize to 8x8 image.<br />
step 3:Compute difference between adjacent pixel values<br />
step 4:Calculate hash value<br /><br/>

***Perceptive hashing:<br />***
step 1:Convert the image to grayscale.<br />
step 2:Resize to 8x8 image.<br />
step 3:Compute DCT<br />
step 4:Reduce DCT<br />
step 5:Compute Average value<br />
step 6:Further Reduce DCT<br />
step 7:Calculate hash value<br /><br/>


**Python:<br/>**
avghash.py<br />
diffhash.py <br /><br />

**Java:<br />**
ImagePhash.java<br />
diffhash.java<br />
avghash.java<br />

