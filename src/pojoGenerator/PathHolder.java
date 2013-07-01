package pojoGenerator;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;


public class PathHolder {

   static String MAIN_FOLDER="tool";
	
   static String SCHEMA_FILE = MAIN_FOLDER+File.separator+"schemaFile"+File.separator;
        
   static String CHECKOUT_PATH="temp";
   static String SOURCE_PATH=CHECKOUT_PATH + File.separator + "FinanceJSONFiles";
   static String POJO_TARGET_PATH=MAIN_FOLDER+File.separator+"pojo";
   
   
   static String SVN_PATH="https://subversion.assembla.com/svn/wethejumpingspiders/trunk/server/Finance-AppEngine/trunk/FinanceJSONFiles/";
   static String USERNAME;
   static String PASSWORD;
   static String PACKAGENAME="com.wethejumpingspiders.finance.pojo";

   static String JAR_FILE = MAIN_FOLDER+File.separator+"kmbmodel.jar";
   static Boolean DELETE_FILES=true;
   static String SVN_CO_CMD;
   
   
   static final String CONFIG_FILE = "config.properties";
   
   public static void loadConfig(){
	   Properties p = new Properties();
	   try {
		p.load(new FileReader(CONFIG_FILE));
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("Error in reading config file");
		System.exit(1);
	}
	   
	   SVN_PATH = p.getProperty("SVN_PATH");
	   USERNAME= p.getProperty("USERNAME");
	   PASSWORD=p.getProperty("PASSWORD");
	   PACKAGENAME=p.getProperty("PACKAGENAME");
	   JAR_FILE= p.getProperty("JAR_FILE");
	   if(USERNAME==null || PASSWORD==null || USERNAME.length()==0 || PASSWORD.length()==0){
		   System.out.println("Username or Password not specified in the config file");
	   }
	   
	  SVN_CO_CMD = "svn co " + PathHolder.SVN_PATH + " --username "
				+ PathHolder.USERNAME + " --password " + PathHolder.PASSWORD;
	   
   }

}
