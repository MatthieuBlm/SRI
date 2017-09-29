package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static void main(String[] args) {
		BufferedReader br = null;
		FileReader fr = null;
		List<String> theme = new ArrayList<>();
		List<String> name = new ArrayList<>();
		String file = "";

		try {

			fr = new FileReader(args[0]);
			br = new BufferedReader(fr);

			String sCurrentLine;
			String [] tmp = null;
			
			while ((sCurrentLine = br.readLine()) != null) {
				file += sCurrentLine + "\n";
				tmp = sCurrentLine.split(";");
				
				if(!name.contains(tmp[1]))
					name.add(tmp[1]);
				
				if(!theme.contains(tmp[2]))
					theme.add(tmp[2]);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		System.out.println(theme.size() + " themes.");
		System.out.println(name.size() + " noms.");
		
		int [][] matrice = new int[theme.size()][name.size()];
		String [] splitedFile = file.split("\n");
		
		for (int i = 0; i < matrice.length; i++) {				// Theme
			for (int j = 0; j < matrice[0].length; j++) {			// Name
				for (int x = 0; x < splitedFile.length; x++) {			// Parours tous le fichier
					String tmp = splitedFile[x];
					if(tmp.contains(theme.get(i)) && tmp.contains(name.get(j)))
						matrice[i][j]++;
				}
			}
		}
		
		printMatrice(matrice);
		
		// ### Save name ###
		String nameStr = "";
		for (String string : name) {
			nameStr += string + ";";
		}
		printInFile("name", nameStr);

		// ### Save theme ###
		String themeStr = "";
		for (String string : theme) {
			themeStr += string + ";";
		}
		printInFile("themes", themeStr);
		
		// ### Matrice ###
		String matriceStr = "";
		for (int i = 0; i < matrice.length; i++) {
			for (int j = 0; j < matrice[0].length; j++) {
				matriceStr += matrice[i][j] + ";";
			}
			matriceStr += "\n";
		}
		printInFile("matrice", matriceStr);
		
		// ###############
		// ##    TP2R   ##
		// ###############
		printMatrice(getMatriceBin(matrice));
		
	}
	
	public static int[][] getMatriceBin(int[][] matrice){
		int [][] res = new int[matrice.length][matrice[0].length];
		
		for (int i = 0; i < matrice.length; i++) {
			for (int j = 0; j < matrice[0].length; j++) {
				if(matrice[i][j] > 0)
					res[i][j] = 1;
				else
					res[i][j] = 0;
			}
		}
		
		return res;
	}
	
	/**
	 * Calcule la disance entre les thème à partir de la matrice binaire
	 * @param matrice La matrice des goûts
	 * @return La matrice distance
	 */
	public static double[][] getMatriceDistance(int[][] matriceBin){
		double [][] res = new double[matriceBin.length][matriceBin.length];		// Matrice carée de taille theme.size

		// Pour chaque utilisateur
		for (int j = 0; j < matriceBin[0].length; j++) {
			// Pour chaque thème t1
			for (int t1 = 0; t1 < matriceBin.length; t1++) {
				// Pour chaque thème t2
				for (int t2 = 0; t2 < matriceBin.length; t2++) {
					res [t1][t2] = getDistance(matriceBin, t1, t2);
				}
			}
		}
		
		return res;
	}
	
	/**
	 * Calcule le ratio distance entre deux thème
	 * @param matriceBin
	 * @param t1
	 * @param t2
	 * @return La distance entre les deux thème
	 */
	public static double getDistance(int[][]matriceBin, int t1, int t2){
		int t1Nt2 = 0;		// Intersection
		int t1Ut2 = 0;		// Union
		
		// Pour chaque utilisateur
		for (int i = 0; i < matriceBin[0].length; i++) {
			// On compte combien de fois t1 et t2 sont appréciés en même temps
			if(matriceBin[t1][i] == 1 && matriceBin[t2][i] == 1)
				t1Nt2++;
			if(matriceBin[t1][i] == 1 || matriceBin[t2][i] == 1)
				t1Ut2++;
		}
		
		return (t1Ut2 != 0 ? 1 - (t1Nt2 / t1Ut2) : 0);
	}
	
	public static void printMatrice(int[][] matrice){
		for (int i = 0; i < matrice.length; i++) {
			for (int j = 0; j < matrice[0].length; j++) {
				System.out.print(matrice[i][j] + " ");
			}
			System.out.println("");
		}
	}
	
	public static void printMatrice(double[][] matrice){
		for (int i = 0; i < matrice.length; i++) {
			for (int j = 0; j < matrice[0].length; j++) {
				System.out.print(matrice[i][j] + " ");
			}
			System.out.println("");
		}
	}
	
	public static void printInFile(String path, String content){
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(content);

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}
	
}
