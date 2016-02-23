package com.COSC603.FireDanger;

//import java.math.BigDecimal;

//import java.io.*; 
//import java.util.*;
import java.lang.Math;
// TODO: Auto-generated Javadoc
/**
 * The Class FireDanger.
 */
public class FireDanger {
	public static int MLength = 4;
	public static int MCLength = 3;
	public static int MDLength = 6;
	double precipThreshold =0.1; //.10 inches of 24hr, rainfall or less is considered a rainless day

	
	//define arrays
	public double A[] = new double [MLength];
	public double B[] = new double [MLength];
	public double C[] = new double [MCLength];
	public double D[] = new double [MDLength];
	
	/** The dry. */
	//Declaring input variables:
	private 	double dry;// dry bulb temperature
	
	/** The wet. */
	private 	double  wet;// wet bulb temperature
	
	private double diff; //store difference dry -wet
	/** The isnow. */
	private int isnow; //>0 is there is snow on the ground
	
	/** The precip. */
	private 	double  precip; //rain measure
	
	/** The wind. */
	public static  	double  wind; //current wind speed in miles per hour
	
	/** The buo. */
	private  double  buo; //last value of the buildup index
	
	/** The iherb. */
	private  	double  iherb; //current herb state of the district 1= cured;2= transition; 3 = green
		
		//returned values:
		
		/** The df. */
		private 	double df; //drying factor
		
		/** The ffm. */
		private 	double ffm; //fine fuel moisture
		
		/** The adfm. */
		private   	double adfm; //adjusted (10 day lag) fuel moisture
		
		/** The grass. */
		private  	double  grass; //grass spread index
		
		/** The timber. */
		private 	double  timber; //timber spread index
		
		/** The fload. */
		private  	double  fload; //fire load rating (max-hour base)
		
		/** The buo2. */
		public static  	double  buo2;// build up index
		
		/**
		 * Checks if is isnow.
		 *
		 * @return true, if is isnow
		 */
		public int isIsnow() {
			return isnow;
		}
		
		/**
		 * Sets the isnow.
		 *
		 * @param isnow the new isnow
		 */
		public void setIsnow(int isnow) {
			this.isnow = isnow;
		}	
		
	
		  
		  

		
		
		
		
	/**
	 * Calculate fire danger.
	 */
	public  void calculateFireDanger() {
		// TODO Auto-generated method stub
		
		
		System.out.println("inside calculateFireDanger");
		 //if snow is positive, set all spread indexes to zero, estimates rain and adjust bui
		if (isnow > 0)
		{
			//set grass to zero, set timber to zero
			this.grass =0;
			this.timber =0;
			
			
			//if there is precipitation grater than 0.1 inch
			//BigDecimal bdPrecip = new BigDecimal(precip);
			//BigDecimal bdthreshold = new BigDecimal(0.1);
			

			if((precip -precipThreshold ) >0) 
				//precipitation exceeded 0.1 inches and we reduced the buildup index 
			{
				this.buo = calculateBuildUpIndex();
				if (buo < 0)
				{
					buo = 0;
				}
				else
				   finalizeDanger();
				
				
			}
			else finalizeDanger();
				
		}
		
		else // there is no snow on the ground and we will compute the spread indexes and fire load
		
		{
		//if snow is negative or zero, 
			//calculates fine fuel moisture
			this.ffm = calculateFineFuelMoisture();
			// finalizeDanger();
			
			//calculates drying factor
			this.df = calculateDryngFactor();
			 //finalizeDanger();
			
			
			//Adjust Fine Fuel Moisture for Herb Stage
			this.ffm= adjustFFMHerb();
			
			
			
		}
	}
	
	private double adjustFFMHerb() {
		// TODO Auto-generated method stub
		double ffm2 = ffm;
		//if Fuel Fine Moisture is negative, we set it to one
		if ((ffm2- 1.0) < 0)
		{
			
			ffm2 = 1.0;
		}
		
		//add the 5% ffm for each herb stage > 1
		if (iherb>1)
			ffm2 = ffm2 + (iherb -1)* 0.05;
		return ffm2;
	}

	private double calculateDryngFactor() {
		// TODO Auto-generated method stub
		
		int i = 0;
		double df2=0;
		
		while ((( ffm - D[i]) <= 0 ) &&(i < D.length))
			
		{
			i++;
			
		}
		
		if (( ffm - D[i]) > 0 &&(i < D.length) )
		{
			//if (i >= 0)
			//{	
				df2 = i;
			//}
			//else
				//df= 0;
			
			
		}
		
		if (i == D.length)
		{
			df2 = MDLength+1;
		}
		
		return df2;
	}

	private double calculateFineFuelMoisture() {
		// TODO Auto-generated method stub
		//diff = dry- wet ;
		double ffm2;
			int i =0;
			//for (i = 0 ; i < C.length; i++)
			//{
				while (((diff - C[i])>0) && (i< C.length))
				{
					i++;
				}
				
				ffm2 =  B[i]* Math.exp(A[i]*diff);
				
			//}
				return ffm2;
	}

	private double calculateBuildUpIndex() {
		// TODO Auto-generated method stub
		double newBuo;
		
		newBuo = -50.0 * Math.log(1.0 - (Math.exp(- buo/50.0)))* Math.exp(-1.175*(precip - 0.1));
		return newBuo;
	}

	private void finalizeDanger() {
		// TODO Auto-generated method stub
		System.out.println("IsSnow :" + isnow);
		System.out.println("Build up Index (BUO):" + buo);
		System.out.println("Precipitation (PRECIP):" + precip);
		
		System.out.println("Grass :" + grass);
		System.out.println("Timber :" + timber);
		
		System.out.println("Wet :" + wet);
		System.out.println("Dry :" + dry);
		System.out.println("Fine Fuel Factor (FFM) :" + ffm);
		System.out.println("Drying Factor (DF) :" + df);
	}

	/**
	 * Gets the df.
	 *
	 * @return the df
	 */
	public double getDf() {
		return df;
	}
	
	/**
	 * Sets the df.
	 *
	 * @param df the new df
	 */
	public void setDf(double df) {
		this.df = df;
	}
	
	/**
	 * Gets the ffm.
	 *
	 * @return the ffm
	 */
	public double getFfm() {
		return ffm;
	}
	
	/**
	 * Sets the ffm.
	 *
	 * @param ffm the new ffm
	 */
	public void setFfm(double ffm) {
		this.ffm = ffm;
	}
	
	/**
	 * Gets the adfm.
	 *
	 * @return the adfm
	 */
	public double getAdfm() {
		return adfm;
	}
	
	/**
	 * Sets the adfm.
	 *
	 * @param adfm the new adfm
	 */
	public void setAdfm(double adfm) {
		this.adfm = adfm;
	}

	public double getFload() {
		return fload;
	}

	public void setFload(double fload) {
		this.fload = fload;
	}

	public double getGrass() {
		return grass;
	}

	public void setGrass(double grass) {
		this.grass = grass;
	}

	public double getTimber() {
		return timber;
	}

	public void setTimber(double timber) {
		this.timber = timber;
	}

	public double getPrecip() {
		return precip;
	}

	public void setPrecip(double precip) {
		this.precip = precip;
	}

	public void setBuo( double buo) {
		// TODO Auto-generated method stub
		this.buo = buo;
		
	}
	public double getBuo()
	{
		
		return buo;
	}

	public double getDry() {
		return dry;
	}

	

	public double getWet() {
		return wet;
	}

	public double getDiff()
	{
		return diff;
		
	}

	public void setDry(double dry) {
		// TODO Auto-generated method stub
		this.dry = dry;
	}
	

	public void setWet(double wet) {
		// TODO Auto-generated method stub
		this.wet = wet;
	}

	public void setDiff(double diff) {
		// TODO Auto-generated method stub
		this.diff = diff;
		
	}

	public double getIherb() {
		return iherb;
	}

	public void setIherb(double iherb) {
		this.iherb = iherb;
		
		//adding a comment here
		
		//ther comment added
	}
	
	
	
	
	

}
