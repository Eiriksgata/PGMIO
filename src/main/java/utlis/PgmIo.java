package utlis;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for reading and writing PGM images. Methods use integers to represent unsigned bytes.
 * <p>
 * Does not fully conform to the PGM specification because currently there is no support for:
 * <ul>
 * <li>More than one image per file</li>
 * <li>Images with more than 256 shades of gray</li>
 * <li>Comments within the raster</li>
 * </ul>
 * github https://gist.github.com/armanbilge/3276d80030d1caa2ed7c#file-pgmio-java-L2
 *
 * @author Arman Bilge
 */
public final class PgmIo {

    private static final String MAGIC = "P5";
    /**
     * Character indicating a comment.
     */
    private static final char COMMENT = '#';
    /**
     * The maximum gray value.
     */
    private static final int MAXVAL = 255;

    /**
     * Reads a grayscale image from a file in PGM format.
     *
     * @param file the PGM file read from
     * @return two-dimensional byte array representation of the image
     * @throws IOException
     */
    public static int[][] read(final File file) throws IOException {
        final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
        try {
            if (!next(stream).equals(MAGIC))
                throw new IOException("File " + file + " is not a binary PGM image.");
            final int col = Integer.parseInt(next(stream));
            final int row = Integer.parseInt(next(stream));
            final int max = Integer.parseInt(next(stream));
            if (max < 0 || max > MAXVAL)
                throw new IOException("The image's maximum gray value must be in range [0, " + MAXVAL + "].");
            final int[][] image = new int[row][col];
            for (int i = 0; i < row; ++i) {
                for (int j = 0; j < col; ++j) {
                    final int p = stream.read();
                    if (p == -1)
                        throw new IOException("Reached end-of-file prematurely.");
                    else if (p < 0 || p > max)
                        throw new IOException("Pixel value " + p + " outside of range [0, " + max + "].");
                    image[i][j] = p;
                }
            }
            return image;
        } finally {
            stream.close();
        }
    }

    /**
     * Finds the next whitespace-delimited string in a stream, ignoring any comments.
     *
     * @param stream the stream read from
     * @return the next whitespace-delimited string
     * @throws IOException
     */
    private static String next(final InputStream stream) throws IOException {
        final List<Byte> bytes = new ArrayList<Byte>();
        while (true) {
            final int b = stream.read();

            if (b != -1) {

                final char c = (char) b;
                if (c == COMMENT) {
                    int d;
                    do {
                        d = stream.read();
                    } while (d != -1 && d != '\n' && d != '\r');
                } else if (!Character.isWhitespace(c)) {
                    bytes.add((byte) b);
                } else if (bytes.size() > 0) {
                    break;
                }

            } else {
                break;
            }

        }
        final byte[] bytesArray = new byte[bytes.size()];
        for (int i = 0; i < bytesArray.length; ++i)
            bytesArray[i] = bytes.get(i);
        return new String(bytesArray);
    }

    /**
     * Writes a grayscale image to a file in PGM format.
     *
     * @param image a two-dimensional byte array representation of the image
     * @param file  the file to write to
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public static void write(final int[][] image, final File file) throws IOException {
        write(image, file, MAXVAL);
    }


    public static void write(BufferedImage image, final File file) throws IOException {
        int[][] rgbArray = new int[image.getHeight()][image.getWidth()];
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                int p = image.getRGB(i, j);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int avg = (r + g + b) / 3;
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                //newImg.setRGB(i, j, p);
                //image.setRGB(i, j, p);
                rgbArray[j][i] = avg;
            }
        }
        write(rgbArray, file);
    }

    public static void write(File imageFile, final File file) throws IOException {
        try {
            BufferedImage bf = ImageIO.read(imageFile);
            write(bf, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a grayscale image to a file in PGM format.
     *
     * @param image  a two-dimensional byte array representation of the image
     * @param file   the file to write to
     * @param maxval the maximum gray value
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public static void write(final int[][] image, final File file, final int maxval) throws IOException {
        if (maxval > MAXVAL)
            throw new IllegalArgumentException("The maximum gray value cannot exceed " + MAXVAL + ".");
        final BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
        try {
            stream.write(MAGIC.getBytes());
            stream.write("\n".getBytes());
            stream.write(Integer.toString(image[0].length).getBytes());
            stream.write(" ".getBytes());
            stream.write(Integer.toString(image.length).getBytes());
            stream.write("\n".getBytes());
            stream.write(Integer.toString(maxval).getBytes());
            stream.write("\n".getBytes());

            for (int i = 0; i < image.length; i++) {
                for (int j = 0; j < image[0].length; j++) {
                    final int p = image[i][j];
                    if (p < 0 || p > maxval)
                        throw new IOException("Pixel value " + p + " outside of range [0, " + maxval + "].");
                    stream.write(image[i][j]);
                }
            }

            //you can delete println
            System.out.println("PGM create successful, file name :" + file.getName() + "\nwidth:" + image[0].length + ",height:" + image.length);

        } finally {
            stream.close();
        }
    }

}