import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThermalConduction {
	public static void main(String[] args) {
		int length = 1;
		double dx = 0.01;
		double dt = 0.01;
		int l = (int)(length / dx);
		double kappa = 71.6;
		double density = 21.45;
		double cp = 25.86 * 195.084;
		double alpha = kappa / (density * cp);
		double gamma = alpha * dt / (dx * dx);
		System.out.print("gamma: ");
		System.out.println(gamma);
		double[][] hist;
		double[] u;
		LocalDateTime date = LocalDateTime.now();
		String dateStr = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(date);
		File directory = new File("./csv/");
		directory.mkdir();
		String filename = "./csv/output-" + dateStr + ".csv";
		hist = new double[2][l];
		u = new double[l];
		for(int i = 0; i < l; i++) {
			u[i] = 0;
		}
		u[0] = 773;
		hist[0] = u;
		int count = 0;
		int step = 500;
		System.out.print("step: ");
		System.out.println(step);
		boolean finish = false;
		while(true) {
			if(count % step == 0) {
				output(filename, hist[0]);
				if(finish) break;
			}
			double[] nu;
			nu = new double[l];
			nu[0] = 773;
			u = hist[0];
			for(int i = 1; i < l - 1; i++) {
				nu[i] = diff(gamma, u[i - 1], u[i], u[i + 1]);
			}
			nu[l - 1] = nu[l - 2];
			hist[1] = hist[0];
			hist[0] = nu;
			if(nu[l - 1] >= 343) finish = true;
			if(++count > 1000 * step) break;
		}
		// printArray(hist[0]);
		System.out.print("count: ");
		System.out.println(count);
	}
	public static double diff(double gamma, double u0, double u1, double u2) {
		return gamma * u2 + (1 - 2 * gamma) * u1 + gamma * u0;
	}
	public static void printArray(double[] arr) {
		for(int i = 0; i < arr.length; i++) {
			if(i != 0) System.out.print(", ");
			System.out.print(arr[i]);
		}
		System.out.println("");
	}
	public static void output(String filename, double[] arr) {
		File file = new File(filename);
		try {
			file.createNewFile();
		} catch(IOException err) {
			err.printStackTrace();
		}
		try {
			FileWriter writer = new FileWriter(filename, true);
			PrintWriter pw = new PrintWriter(new BufferedWriter(writer));
			for(int i = 0; i < arr.length; i++) {
				if(i != 0) pw.print(",");
				pw.print(arr[i]);
			}
			pw.println("");
			pw.close();
		} catch(IOException err) {
			err.printStackTrace();
		}
	}
}
