package aog.minigame.funbocks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import aog.minigame.funbocks.instance.FBData;

public class Serial {
	
	public static void saveDataFile(){
	  	 
	      try
	      	{
	         FileOutputStream fileOut =
	         new FileOutputStream(Main.dir + "funbocks.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(Main.data);
	         out.close();
	         fileOut.close();
	         Main.log("Succesfully saved 'Data' file.");
	      }catch(IOException i){
	    	  Main.log("An error occured while trying to serialize the object 'Data'.");
	          i.printStackTrace();
	      	}	      
		
	}
	
	public static FBData loadDataFile(){
		
		FBData data = null;
		
	      try
	      {
	    	  if(!new File(Main.dir + "funbocks.ser").exists())
	    		  return new FBData();
	    	  
	         FileInputStream fileIn = new FileInputStream(Main.dir + "funbocks.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         data = (FBData) in.readObject();
	         in.close();
	         fileIn.close();
	         Main.log("Succesfully loaded 'Data' file.");
	         return data;
	      }catch(IOException i){
	         i.printStackTrace();
	         return null;
	      }catch(ClassNotFoundException c){
	    	 Main.log("Failed to load 'Data'.");
	    	 c.printStackTrace();
	         return null;
	      }
		
	}

	public static boolean fileExists(String name) {
		File f = new File(Main.dir, name + ".ser");
		if(f.exists())
			return true;
		else
			return false;
	}

}
