package projcbir;

import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import javax.imageio.ImageIO;
import org.apache.commons.io.*;

public class QueryImage {

	ColorMoment colimg = new ColorMoment();
	String[] imgpath = new String[10];

	public Connection getConnection() {

		Connection connection = null;

		try {

			Class.forName("com.mysql.jdbc.Driver");

			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/nitschema", "root", "nitish");

		} catch (Exception e) {
			System.out.println("Error Occured While Getting the Connection: - "
					+ e);
		}
		return connection;
	}

	public void insertImage() {

		Connection connection = null;

		PreparedStatement statement = null;

		try {
			connection = getConnection();

			Iterator<?> iter = FileUtils.iterateFiles(new File(
					"C:\\Users\\Ni\\Desktop\\JPEG_Dataset"),
					new String[] { "jpg" }, true);

			Statement check = connection.createStatement();

			ResultSet rs = check
					.executeQuery("SELECT trn_imgs.sr_no FROM trn_imgs"); // To
																			// prevent
																			// overwriting
																			// of
																			// previously
																			// entered
																			// data
			rs.last();
			int i = rs.getInt("sr_no");

			while (iter.hasNext()) {

				i++;

				File file = (File) iter.next();

				BufferedImage image = ImageIO.read(file);

				colimg.marchThroughImage(image);

				statement = connection
						.prepareStatement("INSERT INTO trn_imgs(sr_no, img_title, img_path, mean_alpha, mean_red, mean_green, mean_blue, var_alpha, var_red, var_green, var_blue, skew_red, skew_green, skew_blue) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

				statement.setInt(1, i);
				statement.setString(2, file.getName());
				statement.setString(3, file.getAbsolutePath());
				statement.setDouble(4, colimg.mean_a);
				statement.setDouble(5, colimg.mean_r);
				statement.setDouble(6, colimg.mean_g);
				statement.setDouble(7, colimg.mean_b);
				statement.setDouble(8, colimg.var_a);
				statement.setDouble(9, colimg.var_r);
				statement.setDouble(10, colimg.var_g);
				statement.setDouble(11, colimg.var_b);
				statement.setDouble(12, colimg.skew_r);
				statement.setDouble(13, colimg.skew_g);
				statement.setDouble(14, colimg.skew_b);

				statement.executeUpdate();

				System.out.println(" " + i + ". Name: " + file.getName());
			}
		} catch (Exception e) {
			System.out.println("Exception: - " + e);
		}

		finally {
			try {

				connection.close();

				statement.close();
			} catch (SQLException e) {
				System.out.println("SQLException Finally: - " + e);
			}
		}
	}

	public void retrieveImage(File file) {

		int i = 0;

		HashMap<Integer, Double> ed_hash = new HashMap<Integer, Double>();

		Connection connection = null;

		Statement statement = null;

		try {
			connection = getConnection();

			statement = connection.createStatement();

			BufferedImage image = ImageIO.read(file);

			ColorMoment colimg = new ColorMoment();

			colimg.marchThroughImage(image);

			double q_mean_r = (colimg.mean_r);
			double q_mean_g = (colimg.mean_g);
			double q_mean_b = (colimg.mean_b);
			double q_var_r = (colimg.var_r);
			double q_var_g = (colimg.var_g);
			double q_var_b = (colimg.var_b);
			double q_skew_r = (colimg.skew_r);
			double q_skew_g = (colimg.skew_g);
			double q_skew_b = (colimg.skew_b);

			ResultSet rs = statement
					.executeQuery("SELECT trn_imgs.mean_red,trn_imgs.mean_green,trn_imgs.mean_blue,trn_imgs.var_red,trn_imgs.var_green,trn_imgs.var_blue,trn_imgs.skew_red,trn_imgs.skew_green,trn_imgs.skew_blue FROM trn_imgs ");

			System.out.println("BEFORE");

			while (rs.next()) {

				double ret_mean_r = rs.getDouble("mean_red");
				double ret_mean_g = rs.getDouble("mean_green");
				double ret_mean_b = rs.getDouble("mean_blue");
				double ret_var_r = rs.getDouble("var_red");
				double ret_var_g = rs.getDouble("var_green");
				double ret_var_b = rs.getDouble("var_blue");
				double ret_skew_r = rs.getDouble("skew_red");
				double ret_skew_g = rs.getDouble("skew_green");
				double ret_skew_b = rs.getDouble("skew_blue");

				i++;

				double mean_sq_dist = ((q_mean_r - ret_mean_r) * (q_mean_r - ret_mean_r))
						+ ((q_mean_g - ret_mean_g) * (q_mean_g - ret_mean_g))
						+ ((q_mean_b - ret_mean_b) * (q_mean_b - ret_mean_b));
				double var_sq_dist = ((q_var_r - ret_var_r) * (q_var_r - ret_var_r))
						+ ((q_var_g - ret_var_g) * (q_var_g - ret_var_g))
						+ ((q_var_b - ret_var_b) * (q_var_b - ret_var_b));
				double skew_sq_dist = ((q_skew_r - ret_skew_r) * (q_skew_r - ret_skew_r))
						+ ((q_skew_g - ret_skew_g) * (q_skew_g - ret_skew_g))
						+ ((q_skew_b - ret_skew_b) * (q_skew_b - ret_skew_b));

				ed_hash.put(i,
						Math.sqrt(mean_sq_dist + var_sq_dist + skew_sq_dist)); // Calculating
																				// the
																				// Euclidean
																				// Distance
																				// for
																				// Image
																				// Comparison

				System.out.println("entered");

			}

			ValueComparator bvc = new ValueComparator(ed_hash);
			TreeMap<Integer, Double> sorted_map = new TreeMap<Integer, Double>(
					bvc);
			sorted_map.putAll(ed_hash);

			System.out.println("sorted : " + sorted_map);

			for (int j = 1; j <= 10; j++) {

				rs = statement
						.executeQuery("select trn_imgs.sr_no, trn_imgs.img_path from trn_imgs where sr_no = "
								+ sorted_map.keySet().toArray()[j]);

				System.out.println("loop " + j + " : "
						+ sorted_map.keySet().toArray()[j]);

				while (rs.next()) {
					imgpath[j - 1] = rs.getString("img_path");

					System.out.println(j - 1 + " " + rs.getInt("sr_no") + " "
							+ rs.getString("img_path"));

					System.out.println("endloop");

				}
			}

			System.out.println("imgpath_size: " + imgpath.length);

			System.out.println("imgpath_elements: " + Arrays.toString(imgpath));

			Display disp = new Display();

			for (i = 0; i < 10; i++) {

				disp.showImage(imgpath[i].replace("\\", "/"));

			}

		} catch (Exception e) {
			System.out.println("Exception: - " + e);
		}

		finally {
			try {

				connection.close();

				statement.close();
			} catch (SQLException e) {
				System.out.println("SQLException Finally: - " + e);
			}
		}

	}

	public static void main(String[] args) throws SQLException {

		QueryImage img = new QueryImage();

		img.insertImage();

	}
}

class ValueComparator implements Comparator<Integer> {

	Map<Integer, Double> base;

	public ValueComparator(Map<Integer, Double> base) {
		this.base = base;
	}

	// This comparator imposes orderings that are inconsistent with equals.
	public int compare(Integer a, Integer b) {
		if (base.get(a) <= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}