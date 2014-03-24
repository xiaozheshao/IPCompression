import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class RTPath {

	public ArrayList<ArrayList<Integer>> pathList = new ArrayList<ArrayList<Integer>>();

	public RTPath(String fileName, Map<Integer, AS> asList) throws IOException {
		File file = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String in = null;
		int index = 0;
		while ((in = br.readLine()) != null 
				&& index < 1200
				) {

			String[] temp = in.trim().split(" ");
			ArrayList<Integer> out = new ArrayList<Integer>();
			for (int i = 0; i < temp.length; i++) {
				Integer inte = null;
				try {
					inte = Integer.valueOf(temp[i]);
				} catch (NumberFormatException e) {
					System.out.println(index + ":" + in);
					e.printStackTrace();
					continue;
				}
				out.add(inte);
				if (!asList.containsKey(inte)) {
					asList.put(inte, new AS(inte));

				}

			}
			// System.out.println("path:");
			// for (int i = 0; i < out.length ; i++){
			// System.out.print(out[i] + " ");
			// }
			pathList.add(out);
			index++;
		}
		br.close();
	}
}
