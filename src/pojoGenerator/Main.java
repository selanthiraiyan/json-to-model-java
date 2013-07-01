package pojoGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;


public class Main {

	public static void main(String[] args) throws Exception {

		PathHolder.loadConfig();
		deleteFolders();
		createParentStructure();
		checkOutFiles();

		File f = new File(PathHolder.SOURCE_PATH);
		generatePojoClasses(f);

		File pojoPath = new File(PathHolder.POJO_TARGET_PATH);
		removeUnwantedFilesAndStrings(pojoPath);
	}

	private static void removeUnwantedFilesAndStrings(File f) {		
		for (File file : f.listFiles()) {
			if (file.isDirectory()) {
				removeUnwantedFilesAndStrings(file);
			}
			else {
				if (file.getName().endsWith(".java")) {
					String[] nameOfFilesToBeDeleted = {"Request.java", "Request_.java", "Echo.java", "Response.java", "Response_.java"};					
					for (int i = 0; i < nameOfFilesToBeDeleted.length; i++) {
						if (file.getName().equalsIgnoreCase(nameOfFilesToBeDeleted[i])) {
							System.out.println("Deleting file at " + file.getPath());
							deleteDir(file);
							return;
							//Commit test
						}
					}


				}
			}
		}
	}

	private static void generatePojoClasses(File f) {		
		for (File file : f.listFiles()) {
			if (file.isDirectory()) {
				generatePojoClasses(file);
			}
			else {
				if (file.getName().endsWith(".json")) {
					System.out.println(file.getName() + " is a .json file.");

					File servlet = file.getParentFile().getParentFile();
					File base = servlet.getParentFile();

					String basePackage = PathHolder.PACKAGENAME + "." + base.getName() + "." + servlet.getName();

					if (file.getName().startsWith("request")) {
						createPojo(file.getAbsolutePath(), PathHolder.POJO_TARGET_PATH, basePackage + ".Request");
					}
					else if (file.getName().startsWith("response")) {
						createPojo(file.getAbsolutePath(), PathHolder.POJO_TARGET_PATH, basePackage + ".Response");
					}
				}
			}
		}
	}

	private static void createPojo(String fromJSONFileNamed, String targetFolder, String packageName) {

		System.out.println("\nTrying to generate POJO from " + fromJSONFileNamed
				+ " \nto: " + targetFolder + "\nusingPackageName " + packageName);

		File buildFile = new File("build.xml");
		Project p = new Project();
		p.setUserProperty("ant.file", buildFile.getAbsolutePath());
		p.setUserProperty("targetPackage", packageName);
		p.setUserProperty("targetDirectory", targetFolder);
		p.setUserProperty("source", fromJSONFileNamed);
		p.init();
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		p.addReference("ant.projectHelper", helper);
		helper.parse(p, buildFile);
		p.executeTarget(p.getDefaultTarget());
	}

	public static void checkOutFiles() throws Exception {

		File f = new File(PathHolder.CHECKOUT_PATH);
		Process p = Runtime.getRuntime().exec(PathHolder.SVN_CO_CMD, null, f);
		readFromProcess(p);
	}

	public static void deleteFolders() {
		String[] dirCreater = { PathHolder.SCHEMA_FILE, PathHolder.POJO_TARGET_PATH};
		for (int i = 0; i < dirCreater.length; i++) {
			deleteDir(new File(dirCreater[i]));
		}
	}

	public static void createParentStructure() {
		String[] path = {
				PathHolder.CHECKOUT_PATH, PathHolder.POJO_TARGET_PATH};
		File mainFolder = new File(PathHolder.MAIN_FOLDER);
		if (mainFolder.exists()) {
			deleteDir(mainFolder);
		}
		mainFolder.mkdir();
		for (int i = 0; i < path.length; i++) {
			File file = new File(path[i]);

			file.mkdirs();
		}
	}

	public static void deleteDir(File dir) {
		if (dir.isDirectory()) {
			File[] parent = dir.listFiles();
			for (File file : parent) {

				if (file.isDirectory()) {
					deleteDir(file);
				}
				file.delete();
			}
		}
	}

	private static void readFromProcess(Process p) throws IOException {
		BufferedReader bi = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String lineString = null;
		while ((lineString = bi.readLine()) != null) {
			System.out.println(lineString);
		}
		bi.close();

	}


}
