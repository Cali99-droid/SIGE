package com.tesla.colegio.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtil {

	public static void main(String[] args) throws Exception {
		try {
			/*
			 * File file = new File(
			 * "D:\\proyectos\\plantillas\\cleanuitemplate.com.dashboard.beta.har"
			 * ); String base = "D:/proyectos/cleanuitemplate"; String
			 * urlBase="http://cleanuitemplate.com";
			 */
			// File file = new
			// File("D:\\proyectos\\plantillas\\byrushan.form.har");
			// File file = new
			// File("D:\\proyectos\\plantillas\\byrushan.form.components.har");
			File file = new File("D:\\proyectos\\plantillas\\byrushan.form.validation.har");
			String base = "D:/proyectos/byrushan";
			String urlBase = "http://byrushan.com";

			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			String linea;
			String url;
			String carpetas;
			String archivo;
			int i = 0;
			while ((line = bufferedReader.readLine()) != null) {
				linea = line.trim();
				if (linea.indexOf("\"url\"") == 0) {
					url = linea.substring(7).replace("\"", "");
					url = url.replace(",", "");
					// //System.out.println(url);
					if (url.indexOf("?") > -1)
						url = url.substring(0, url.indexOf("?"));
					if (url.indexOf(urlBase) == 0) {
						//System.out.println(url);
						carpetas = url.substring(urlBase.length());
						archivo = carpetas.substring(carpetas.lastIndexOf("/") + 1);
						carpetas = carpetas.substring(0, carpetas.lastIndexOf("/"));

						//System.out.println(carpetas);
						//System.out.println(archivo);

						createDir(base, carpetas);
						Path targetFile = Paths.get(base + carpetas + "/" + archivo);
						URL website = new URL(url);
						try (InputStream in = website.openStream()) {
							if (!(new File(base + carpetas + "/" + archivo)).exists())
								Files.copy(in, targetFile, StandardCopyOption.REPLACE_EXISTING);
						}
					}
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveFile(String pageFullName, String cadena) {
		try {

			FileWriter fw = new FileWriter(pageFullName);

			fw.write(cadena);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void createDirectory(String directoryFullName) {
		File f = new File(directoryFullName);
		boolean exists = f.exists();
		if (!exists) {
			new File(directoryFullName).mkdirs();
		}
	}

	public static void copyFolder(File source, File destination) {
		if (source.isDirectory()) {
			if (!destination.exists()) {
				destination.mkdirs();
			}

			String files[] = source.list();

			for (String file : files) {
				File srcFile = new File(source, file);
				File destFile = new File(destination, file);

				copyFolder(srcFile, destFile);
			}
		} else {
			InputStream in = null;
			OutputStream out = null;

			try {
				in = new FileInputStream(source);
				out = new FileOutputStream(destination);

				byte[] buffer = new byte[1024];

				int length;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
			} catch (Exception e) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static void createDir(String base, String carpetas) {
		File source = new File(base + carpetas);
		if (!source.exists()) {
			source.mkdirs();
		} else {
			System.err.println("CARPETA YA EXISTE");
		}
		/*
		 * String carpetasArr[] = carpetas.split("/"); for (String carpeta :
		 * carpetasArr) { File source = new File(base + "/" + carpeta); if
		 * (!source.exists()){ source.mkdirs(); }else{
		 * System.err.println("CARPETA YA EXISTE"); } }
		 */

	}

	/**
	 * PAra lina
	 * @param pathFile
	 * @return
	 * @throws IOException
	 */
	public static byte[] filePathToByte(String pathFile) throws IOException {
		File file = new File(pathFile);
		byte[] bytesArray = new byte[(int) file.length()];

		FileInputStream fis = new FileInputStream(file);
		fis.read(bytesArray); // read file into bytes[]
		fis.close();

		return bytesArray;
	}
}
