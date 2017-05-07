package test;

import java.io.BufferedReader;
import java.io.FileReader;

public class Test {

	public static void main(String[] args) {
		try {
			FileReader fr = new FileReader("d:\\test.vcf");
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
//				String[] ss = line.split("\n");
				if(line.startsWith("FN")){
					String name = line.substring(line.indexOf(":")+1);
					line = br.readLine();
					String tel = line.substring(line.indexOf(":")+1);
//					byte[] namebs = name.getBytes("GBK");
					System.out.println("姓名："+name+"\t电话："+tel);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
