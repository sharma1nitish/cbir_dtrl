package projcbir;

import java.awt.Component;
import java.awt.image.BufferedImage;

public class ColorMoment extends Component {
	double alpha1, alpha2, alpha3;
	double red1, red2, red3;
	double green1, green2, green3;
	double blue1, blue2, blue3;
	double mean_a, mean_r, mean_g, mean_b;
	double var_a, var_r, var_g, var_b;
	double skew_a, skew_r, skew_g, skew_b;

	public void marchThroughImage(BufferedImage image) {
		int i = 0, j = 0, pixelcount;
		int w = image.getWidth();
		int h = image.getHeight();

		System.out.println("w:");
		System.out.println("h: " + h);

		alpha1 = 0;
		alpha2 = 0;
		alpha3 = 0;
		red1 = 0;
		red2 = 0;
		red3 = 0;
		green1 = 0;
		green2 = 0;
		green3 = 0;
		blue1 = 0;
		blue2 = 0;
		blue3 = 0;
		mean_a = 0;
		mean_r = 0;
		mean_g = 0;
		mean_b = 0;
		var_a = 0;
		var_r = 0;
		var_g = 0;
		var_b = 0;
		skew_a = 0;
		skew_r = 0;
		skew_g = 0;
		skew_b = 0;

		for (i = 0; i < h; i++) {
			for (j = 0; j < w; j++) {

				System.out.print(i);
				System.out.print(j);

				int pixel = image.getRGB(j, i);

				System.out.println("pixel(" + j + "," + i + ") : " + pixel);
				System.out.println("shift 24: " + ((pixel >> 24) & 0xff));
				System.out.println("shift 16: " + ((pixel >> 16) & 0xff));
				System.out.println("shift 8: " + ((pixel >> 8) & 0xff));
				System.out.println("shift 0: " + ((pixel) & 0xff));

				alpha1 += ((pixel >> 24) & 0xff);

				alpha2 += Math.pow(((pixel >> 24) & 0xff), 2);

				alpha3 += Math.pow(((pixel >> 24) & 0xff), 3);

				red1 += ((pixel >> 16) & 0xff);

				red2 += Math.pow(((pixel >> 16) & 0xff), 2);

				red3 += Math.pow(((pixel >> 16) & 0xff), 3);

				green1 += ((pixel >> 8) & 0xff);

				green2 += Math.pow(((pixel >> 8) & 0xff), 2);

				green3 += Math.pow(((pixel >> 8) & 0xff), 3);

				blue1 += ((pixel) & 0xff);

				blue2 += Math.pow(((pixel) & 0xff), 2);

				blue3 += Math.pow(((pixel) & 0xff), 3);

			}

		}

		pixelcount = j * i;

		System.out.println("initREDmvs: " + mean_r + ", " + var_r + ", "
				+ skew_r);
		System.out.println("initGREENmvs: " + mean_g + ", " + var_g + ", "
				+ skew_g);
		System.out.println("initBLUEmvs: " + mean_b + ", " + var_b + ", "
				+ skew_b);

		mean_a = alpha1 / pixelcount;

		var_a = (alpha2 / pixelcount) - (mean_a * mean_a);

		skew_a = (((alpha3 / pixelcount) - (mean_a * mean_a * mean_a)) / Math
				.sqrt(var_a * var_a * var_a))
				- ((3 * mean_a) / Math.sqrt(var_a));

		mean_r = red1 / pixelcount;

		var_r = (red2 / pixelcount) - (mean_r * mean_r);

		skew_r = (((red3 / pixelcount) - (mean_r * mean_r * mean_r)) / Math
				.sqrt(var_r * var_r * var_r))
				- ((3 * mean_r) / Math.sqrt(var_r));

		mean_g = green1 / pixelcount;

		var_g = (green2 / pixelcount) - (mean_g * mean_g);

		skew_g = (((green3 / pixelcount) - (mean_g * mean_g * mean_g)) / Math
				.sqrt(var_g * var_g * var_g))
				- ((3 * mean_g) / Math.sqrt(var_g));

		mean_b = blue1 / pixelcount;

		var_b = (blue2 / pixelcount) - (mean_b * mean_b);

		skew_b = (((blue3 / pixelcount) - (mean_b * mean_b * mean_b)) / Math
				.sqrt(var_b * var_b * var_b))
				- ((3 * mean_b) / Math.sqrt(var_b));

		System.out.println("MEAN argb: " + mean_a + ", " + mean_r + ", "
				+ mean_g + ", " + mean_b);
		System.out.println("VARIANCE argb: " + var_a + ", " + var_r + ", "
				+ var_g + ", " + var_b);
		System.out.println("SKEWNESS argb: " + skew_r + ", " + skew_g + ", "
				+ skew_b);

	}

}