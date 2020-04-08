import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;


public class SeamCarver {

    // a class representing a coordinate
    private class coordinate {
        private int x, y;
        public coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
        int getX() {
            return x;
        }
        int getY() {
            return y;
        }
    }

    private double[][] pixelEnergy;
    private double[][] seamVert;
    private double[][] seamHor;
    private coordinate[][] pathVert;   // vertical path
    private coordinate[][] pathHor;    // horizontal path
    private int height, width;
    private Picture curPicture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        set(picture);
    }
    
    // calculating energy and paths 
    private void set(Picture picture) {
        curPicture = new Picture(picture);
        width = picture.width();
        height = picture.height();
        pixelEnergy = new double[width][height];
        seamVert = new double [width][height];
        seamHor = new double [width][height];
        pathVert = new coordinate [width][height];
        pathHor = new coordinate [width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == 0 || (i == (width - 1)) || j == 0 || (j == (height - 1))) {
                    pixelEnergy[i][j] = 1000;
                }
                else {
                    pixelEnergy[i][j] = getEnergy(i, j);
                }
            }
        }
        for (int i = 0; i < width; i++) {
            seamVert[i][0] = pixelEnergy[i][0];
        }
        for (int i = 0; i < height; i++) {
            seamHor[0][i] = pixelEnergy[0][i];
        }
        routine();  // do for horizontal
        // do for vertical
        if (width > 1) {
            for (int j = 1; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    if (i == 0) {
                        if (seamVert[i][j - 1] <= seamVert[i + 1][j - 1]) {
                            seamVert[i][j] = seamVert[i][j - 1] + pixelEnergy[i][j];
                            pathVert[i][j] = new coordinate(i, j - 1);
                        }
                        else {
                            seamVert[i][j] = seamVert[i + 1][j - 1] + pixelEnergy[i][j];
                            pathVert[i][j] = new coordinate(i + 1, j - 1);
                        }
                    }
                    else if (i == width - 1) {
                        if (seamVert[i][j - 1] <= seamVert[i - 1][j - 1]) {
                            seamVert[i][j] = seamVert[i][j - 1] + pixelEnergy[i][j];
                            pathVert[i][j] = new coordinate(i, j - 1);
                        }
                        else {
                            seamVert[i][j] = seamVert[i - 1][j - 1] + pixelEnergy[i][j];
                            pathVert[i][j] = new coordinate(i - 1, j - 1);
                        }
                    }
                    else if ((seamVert[i - 1][j - 1] <= seamVert[i][j - 1]) && (seamVert[i - 1][j - 1] <= seamVert[i + 1][j - 1])) {
                        seamVert[i][j] = seamVert[i - 1][j - 1] + pixelEnergy[i][j];
                        pathVert[i][j] = new coordinate(i - 1, j - 1);
                    }
                    else if ((seamVert[i][j - 1] <= seamVert[i - 1][j - 1]) && (seamVert[i][j - 1] <= seamVert[i + 1][j - 1])) {
                        seamVert[i][j] = seamVert[i][j - 1] + pixelEnergy[i][j];
                        pathVert[i][j] = new coordinate(i, j - 1);
                    }
                    else {
                        seamVert[i][j] = seamVert[i + 1][j - 1] + pixelEnergy[i][j];
                        pathVert[i][j] = new coordinate(i + 1, j - 1);
                    }
                }
            }
        }
    }
 
    private void routine() {  // do for horizontal
        if (height > 1) {
            for (int j = 1; j < width; j++) {
                for (int i = 0; i < height; i++) {
                    if (i == 0) {
                        if (seamHor[j - 1][i] <= seamHor[j - 1][i + 1]) {
                            seamHor[j][i] = seamHor[j - 1][i] + pixelEnergy[j][i];
                            pathHor[j][i] = new coordinate(j - 1, i);
                        }
                        else {
                            seamHor[j][i] = seamHor[j - 1][i + 1] + pixelEnergy[j][i];
                            pathHor[j][i] = new coordinate(j - 1, i + 1);
                        }
                    }
                    else if (i == height - 1) {
                        if (seamHor[j - 1][i] <= seamVert[j - 1][i - 1]) {
                            seamHor[j][i] = seamHor[j - 1][i] + pixelEnergy[j][i];
                            pathHor[j][i] = new coordinate(j - 1, i);
                        }
                        else {
                            seamHor[j][i] = seamHor[j - 1][i - 1] + pixelEnergy[j][i];
                            pathHor[j][i] = new coordinate(j - 1, i - 1);
                        }
                    }
                    else if ((seamHor[j - 1][i - 1] <= seamHor[j - 1][i]) && (seamHor[j - 1][i - 1] <= seamHor[j - 1][i + 1])) {
                        seamHor[j][i] = seamHor[j - 1][i - 1] + pixelEnergy[j][i];
                        pathHor[j][i] = new coordinate(j - 1, i - 1);
                    }
                    else if ((seamHor[j - 1][i] <= seamHor[j - 1][i - 1]) && (seamHor[j - 1][i] <= seamHor[j - 1][i + 1])) {
                        seamHor[j][i] = seamHor[j - 1][i] + pixelEnergy[j][i];
                        pathHor[j][i] = new coordinate(j - 1, i);
                    }
                    else {
                        seamHor[j][i] = seamHor[j - 1][i + 1] + pixelEnergy[j][i];
                        pathHor[j][i] = new coordinate(j - 1, i + 1);
                    }
                }
            }
    	}
    }
    
    // printing an array for debugging
    private void print(double[][] array) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                StdOut.print((int)array[j][i]+ " ");
            }
            StdOut.println();
        }
    }

    // printing an array of coordinates for debugging
    private void print(coordinate[][] array) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (array[j][i] == null) {
                    StdOut.print("null : ");
                }
                else
                    StdOut.print(array[j][i].x + " " +  array[j][i].y + " : ");
            }
            StdOut.println();
        }
    }

    // calculate the difference of energy
    private double delta(int x1, int y1, int x2, int y2) { 
        int rgb1 = curPicture.get(x1, y1).getRGB();
        int rgb2 = curPicture.get(x2, y2).getRGB();

        int red1 = (rgb1 >> 16) & 0xFF;
        int red2 = (rgb2 >> 16) & 0xFF;
        int red = red2 - red1;

        int green1 = (rgb1 >> 8) & 0xFF;
        int green2 = (rgb2 >> 8) & 0xFF;
        int green = green2 - green1;

        int blue1 = (rgb1) & 0xFF;
        int blue2 = (rgb2) & 0xFF;
        int blue = blue2 - blue1;

        return red * red + green * green + blue * blue;
    }

    private double getEnergy(int col, int row) {
        return Math.sqrt(delta(col - 1, row, col + 1, row) + delta(col, row - 1, col, row + 1));
    }

    // current picture
    public Picture picture() {
        return new Picture(curPicture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        return pixelEnergy[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] pathArr = new int[width];
        if (height == 1) {
            for (int j = 0; j < width; j++) {
                pathArr[j] = 0;
            }
            return pathArr;
        }
        double min = seamHor[width - 1][0];
        coordinate start = null;
        start = pathHor[width - 1][0];
        pathArr[width - 1] = 0;
        for (int j = 1; j < height; j++) {
            if (seamHor[width - 1][j] < min) {
                min = seamHor[width - 1][j];
                start = pathHor[width - 1][j];
                pathArr[width - 1] = j;
            }
        }
        int i = 1;
        while (start != null) {
            // StdOut.println(seam[start.getX()][start.getY()]);
            pathArr[width - 1 - i] = start.getY();
            start = pathHor[start.getX()][start.getY()];
            i++;
        }
        return pathArr;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] pathArr = new int[height];
        if (width == 1) {
            for (int j = 0; j < height; j++) {
                pathArr[j] = 0;
            }
            return pathArr;
        }
        coordinate start = null;
        double min = seamVert[0][height - 1];
        start = pathVert[0][height - 1];
        pathArr[height - 1] = 0;
        for (int j = 1; j < width; j++) {
            if (seamVert[j][height - 1] < min) {
                min = seamVert[j][height - 1];
                start = pathVert[j][height - 1];
                pathArr[height - 1] = j;
            }
        }
        int i = 1;
        while (start != null) {
           // StdOut.println(seam[start.getX()][start.getY()]);
            pathArr[height - 1 - i] = start.getX();
            start = pathVert[start.getX()][start.getY()];
            i++;
        }
        return pathArr;
    }


    // remove horizontal seam from current picture
       public void removeHorizontalSeam(int[] seam) {
           Picture newPicture = new Picture(width, height - 1);
           for (int i = 0; i < width; i++) {
               int jNew = 0;
               for (int j = 0; j < height; j++) {
                   if (j != seam[i]) {
                       newPicture.set(i, jNew, curPicture.get(i, j));
                       jNew++;
                   }
               }
           }
           set(newPicture);
       }

    // remove vertical seam from current picture
      public void removeVerticalSeam(int[] seam) {
          Picture newPicture = new Picture(width - 1, height);
          for (int i = 0; i < height; i++) {
              int jNew = 0;
              for (int j = 0; j < width; j++) {
                  if (j != seam[i]) {
                      newPicture.set(jNew, i, curPicture.get(j, i));
                      jNew++;
                  }
              }
          }
          set(newPicture);
      }

    //  unit testing 
      public static void main(String[] args) {
        Picture picture = new Picture("chameleon.png");
        SeamCarver carver = new SeamCarver(picture);
        int i = 1;
        // carver.curPicture.show();
        while (i > 0) {
            int seam[] = carver.findVerticalSeam();
            carver.removeVerticalSeam(seam);
            i--;
        }
        //carver.curPicture.show();
    }
}
