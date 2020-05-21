import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.InputStream;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class ImagePHash {

	private int size = 32;
	private int smallerSize = 8;
    private HashMap<String, BufferedImage> images = new HashMap<>();

	
	public ImagePHash() {
		initCoefficients();
        CompareImages();
	}
	
	public ImagePHash(int size, int smallerSize) {
		this.size = size;
		this.smallerSize = smallerSize;
		
		initCoefficients();
	}

    public void CompareImages() {

		loadImages();

		// // Compare each picture to each other
		 images.forEach((imageName, image) -> {
			images.forEach((imageName2, image2) -> {
		formatOutput(imageName, imageName2, compareTwoImages(image, image2));
			});
		 });
	}

	
	private void loadImages() {
		// Load images
		try {

		File file = new File("C:uday/oppo/images");//Provide Dataset Path folder
		File[] files = file.listFiles();
		for(File f: files){
			images.put(f.getName(), ImageIO.read(new File(f.getPath())));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Print header
		System.out.println("|   Image 1   |   Image 2   | Similar  |");
	}

    public boolean compareTwoImages(BufferedImage image1, BufferedImage image2) {

		//Generate the hash for each image
		String hash1 = getHash(image1);
		String hash2 = getHash(image2);

		//Compute a similarity score
		int similarityScore = distance(hash1,hash2);
        System.out.println("similarityScore: "+similarityScore);

		return similarityScore <4;// on this condition alg returns true for similarity
	}


	public int distance(String s1, String s2) {
		System.out.println("hash1: "+s1);
		System.out.println("hash2: "+s2);
		int counter = 0;
		for (int k = 0; k < s1.length();k++) {
			if(s1.charAt(k) != s2.charAt(k)) {
				counter++;
			}
		}
		return counter;
	}

    private void formatOutput(String image1, String image2, boolean similar) {
		String val;
		String format = "| %-11s | %-11s | %-11s |%n";
		if(similar){
			val="similar";
		}
		else
		val="not similar";
		System.out.printf(format, image1, image2, val);
		System.out.printf("--------------------------------------\n");
	}
	
	// Returns a 'binary string' (like. 001010111011100010) which is easy to do a hamming distance on. 
	public String getHash(BufferedImage img){
		
		// 1. Reduce size. 
		img = resize(img, size, size);
		
		// 2. Reduce color. 

		img = grayscale(img);
		
		double[][] vals = new double[size][size];
		
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				vals[x][y] = getBlue(img, x, y);
			}
		}
		
		//3. Compute the DCT. 

		long start = System.currentTimeMillis();
		double[][] dctVals = applyDCT(vals);
		// System.out.println("DCT: " + (System.currentTimeMillis() - start));
		
		// 4. Reduce the DCT. 

		//5. Compute the average value. 

		double total = 0;
		
		for (int x = 0; x < smallerSize; x++) {
			for (int y = 0; y < smallerSize; y++) {
				total += dctVals[x][y];
			}
		}
		total -= dctVals[0][0];
		
		double avg = total / (double) ((smallerSize * smallerSize) - 1);
	
		// 6. Further reduce the DCT. 

		String hash = "";
		
		for (int x = 0; x < smallerSize; x++) {
			for (int y = 0; y < smallerSize; y++) {
				if (x != 0 && y != 0) {
					hash += (dctVals[x][y] > avg?"1":"0");
				}
			}
		}
		
		return hash;
	}
	
	private BufferedImage resize(BufferedImage image, int width,	int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}
	
	private ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

	private BufferedImage grayscale(BufferedImage img) {
        colorConvert.filter(img, img);
        return img;
    }
	
	private static int getBlue(BufferedImage img, int x, int y) {
		return (img.getRGB(x, y)) & 0xff;
	}
	
	private double[] c;
	private void initCoefficients() {
		c = new double[size];
		
        for (int i=1;i<size;i++) {
            c[i]=1;
        }
        c[0]=1/Math.sqrt(2.0);
    }
	
	private double[][] applyDCT(double[][] f) {
		int N = size;
		
        double[][] F = new double[N][N];
        for (int u=0;u<N;u++) {
          for (int v=0;v<N;v++) {
            double sum = 0.0;
            for (int i=0;i<N;i++) {
              for (int j=0;j<N;j++) {
                sum+=Math.cos(((2*i+1)/(2.0*N))*u*Math.PI)*Math.cos(((2*j+1)/(2.0*N))*v*Math.PI)*(f[i][j]);
              }
            }
            sum*=((c[u]*c[v])/4.0);
            F[u][v] = sum;
          }
        }
        return F;
    }



	public static void main(String[] args) {
		new ImagePHash();
	}
}