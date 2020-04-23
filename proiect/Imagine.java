package proiect;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marius
 */
public class Imagine{
    
     String sPath;              //path to initial image
     BufferedImage img = null;  //image to modify
     Image displayedImage;      //image to display
     File fileToSave;           //save path
     boolean saveCheck=false;   //open save as dialog for new path
   
     
     
     
     
     String getsPath(){
         return sPath;
     }
     BufferedImage getImage(){
     return img;
     }
     
     Image getDisplayedImage(){
         return displayedImage;
     }
     
     File getFileOutput(){
         return fileToSave;
     }
     
     boolean getSaveCheck(){
         return saveCheck;
     }    
     
     void setPath(String path){
         this.sPath=path;
     }
     
     void setImage(BufferedImage image){
         this.img=image;
         
     }
     
     
     void setDisplayImage(Image disp){
         this.displayedImage=disp;
         
     }
     
     void setFileOutput(File out){
         this.fileToSave=out;
         
     }
             
     void setSaveCheck(boolean sv){
         this.saveCheck=sv;
     }
     
     
     
    
    
}
