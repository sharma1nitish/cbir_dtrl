package projcbir;

import java.applet.*;
import java.awt.*;
import java.io.*;

public class InputImage extends Applet {

	double input_mean_a;
	double input_mean_r;
	double input_mean_g;
	double input_mean_b;
	double input_var_a;
	double input_var_r;
	double input_var_g;
	double input_var_b;
	double input_skew_r;
	double input_skew_g;
	double input_skew_b;

	public InputImage() {

		Panel p = new Panel();

		Font f;

		String osname = System.getProperty("os.name", "");

		if (!osname.startsWith("Windows")) {

			f = new Font("Arial", Font.BOLD, 10);
		} else {

			f = new Font("Verdana", Font.BOLD, 12);
		}

		p.setFont(f);

		p.add(new Button("Open"));

		p.setBackground(new Color(255, 255, 255));

		add("North", p);

	}

	@SuppressWarnings("deprecation")
	public boolean action(Event evt, Object arg) {
		if (arg.equals("Open")) {

			System.out.println("OPEN CLICKED");

			Frame parent = new Frame();

			FileDialog fd = new FileDialog(parent, "Please choose a file:",
					FileDialog.LOAD);

			fd.show();

			String selectedItem = fd.getFile();

			if (selectedItem == null) {
				// no file selected
			} else {

				File inputimg = new File(fd.getDirectory() + File.separator
						+ fd.getFile());

				String inputimgpath = inputimg.getAbsolutePath().replace("\\",
						"/"); // read the file

				System.out.println("reading file " + inputimgpath);

				Display d = new Display();

				d.showImage(inputimgpath);

				QueryImage iiobj = new QueryImage();

				iiobj.retrieveImage(inputimg);

			}

		} else
			return false;

		return true;
	}

}
