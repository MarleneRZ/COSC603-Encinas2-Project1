/*Copyright (c) 2016 - $(year), $(author), All rights reserved
 * 
 *<p>
 **This is a reengineering work from Fortran 77 to Java to calculate the National Fire Danger ratings and Fire Load Index..
 *this program is developed according to the Java Programming Style Guidelines and
 *Maintainable Java code recommendation discussed in class COSC 603 - Software Testing  and Maintenance 
 *
 */
package com.COSC603.FireDanger;

//import parameters

//import java.io.*; 
//import java.util.*;
import java.lang.Math;
// TODO: Auto-generated Javadoc
/**
 * The Class FireDanger.
 * 
 *@author   Marlene R. Encinas
 *@version  1.0
 *@since    February 26 2016
 *<p>
 *Contains all input parameters, output indexes and their corresponding methods to calculate their values
 */
public class FireDanger {
	
	private FireDangerProduct fireDangerProduct = new FireDangerProduct();

	/** The M length.Define size for Array A and B */
	public static int MLength = 4;
	
	/** The MC length.Define size of Array C */
	public static int MCLength = 3;
	
	/** The MD length.Define size of Array D */
	public static int MDLength = 6;
	
	/** used to measure the arrays for factors used in Formula to calculate Spread Index Grass and Timber. */
	public static int SILength = 2;
	
	/** The precip threshold. .10 inches of 24hr, rainfall or less is considered a rainless day*/
	double precipThreshold =0.1; //.10 inches of 24hr, rainfall or less is considered a rainless day

	
	/** The array A.Its values  are used in computing the Fine Fuel Moisture as factor A */
	//define arrays
	public double A[] = new double [MLength];
	
	/** The B.Its values  are used in computing the Fine Fuel Moisture as factor B*/
	public double B[] = new double [MLength];
	
	/** The array C.Identifies a range when computing the Fine Fuel Moisture as factor C */
	public double C[] = new double [MCLength];
	
	/** The array D.Identifies a range when computing the Fine Fuel Moisture as factor D */
	public double D[] = new double [MDLength];
	
	/** Arrays to store the factors used in Formula to calculate Spread Index Grass and Timber. */
	public double SpreadA[] = new double[SILength];
	
	/** Arrays to store the factors used in Formula to calculate Spread Index Grass and Timber. */
	public double SpreadB[] = new double[SILength];
	
	
	/** The dry. dry bulb temperature*/
	//Declaring input variables:
	private 	double dry;// dry bulb temperature
	
	/** The wet.wet bulb temperature */
	private 	double  wet;// wet bulb temperature
	
	/** The diff. store difference dry -wet */
	private double diff; //store difference dry -wet
	
	/** The isnow. 1 is there is snow on the ground , 0 is no snow*/
	private int isnow; //>0 is there is snow on the ground
	
	/** The precip.rain measure */
	private 	double  precip; //rain measure
	
	/** The wind. current wind speed in miles per hour*/
	private double wind; //current wind speed in miles per hour
	
	/** The buo. last value of the buildup index*/
	private  double  buo; //last value of the buildup index
	
	/** The iherb.current herb state of the district 1= cured;2= transition; 3 = green */
	private  	double  iherb; //current herb state of the district 1= cured;2= transition; 3 = green
		
		//returned values:
		
		/** The df. drying factor*/
		private 	double df; //drying factor
		
		/** The ffm. fine fuel moisture*/
		private 	double ffm; //fine fuel moisture
		
		/** The adfm.adjusted (10 day lag) fuel moisture */
		private   	double adfm; //adjusted (10 day lag) fuel moisture
		
		/** The grass. grass spread index*/
		private  	double  grass; //grass spread index
		
		/** The timber. timber spread index */
		private 	double  timber; //timber spread index
		
		/** The fload. fire load rating (max-hour base)*/
		private  	double  fload; //fire load rating (max-hour base)
		
		///** The buo2. */
	//	public static  	double  buo2;// build up index
		
		/**
		 * Checks if is isnow.
		 *
		 * @return 1, if is isnow
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
		
		
		//System.out.println("inside calculateFireDanger");
		 
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
			
			
			//if Rain, adjust BuildUp Index (buo) for precipitation
			if((precip -precipThreshold ) >0) 
				
			{
				//precipitation exceeded 0.1 inches and we reduced the buildup index by am amount 
				//equal to the rain fall
				this.buo = calculateBuildUpIndex();
				if (buo < 0)
				{
					buo = 0;
				}
			
				
				
			}
			//else 
				
				//if no rain, increase buo by today's drying factor to obtain the current buildup index
				buo = buo + df;
				
			//Calculate the Adjusted Fuel Moisture	ADFM
				
				this.adfm = calculateAdjustedFuelMoisture();
				
			//
				
				//if Fine Fuel Moisture is greater than 30 percent
				//ffm =29.0; <- this is just for a test
				if (adfm <30)
				{
					
					int i=0;//counter to grab a Spread Index element
					
					if ( wind < 14)
					{
						this.timber = fireDangerProduct.calculateTimber(i, SpreadA, wind, SpreadB, adfm);
						this.grass = fireDangerProduct.calculateGrass(i, SpreadA, wind, SpreadB, ffm);
						
						if (timber <=1)
						
						{
							timber = 1;
							if (grass < 1)
							{
								grass = 1;
								
							}
														
						}
													
												
							
						
					} 
					else
					{
						//wind > 14
						i++;
						this.timber = fireDangerProduct.calculateTimber(i, SpreadA, wind, SpreadB, adfm);
						this.grass = fireDangerProduct.calculateGrass(i, SpreadA, wind, SpreadB, ffm);
						
						if (grass > 99.0)
							
						{
							grass = 99.0;
							
							if (timber> 99.00)
							{
								timber = 99.0;
								
							}
						}
						
						
							
							
						
					
					}
					
				
					if (timber <= 0) //this is line 28
							{
								finalizeDanger();
							}
							else
							{
								//line 29
								if (buo <= 0)
								{
									finalizeDanger();
								}
								else 
								{
									this.fload =calculateFireLoad();
									
									//ensure fload is grater than 0, otherwise set it to 0
									if (fload <=0)
									{
										fload = 0.0;
										
									}
									else
									{
										fload = Math.pow(10.0, fload);
										
									}
									finalizeDanger();
									
								}
							}
					
				}
					
				else 
				if (ffm >= 30)
				{
					//Set grass and Timer Spread Index to 1
					grass = 1;
					timber =1;
					finalizeDanger();
					
					
				}
				else //if ffm < 30
				{
					timber =1;
					
					//test to see if Wind speed is greater than 14 mph
					
					int i=0;//counter to grab a Spread Index element
					
					if ( wind < 14)
					{
						this.grass = fireDangerProduct.calculateGrass(i, SpreadA, wind, SpreadB, ffm);
						
						if (timber <=1)
						
						{
							timber = 1;
							if (grass < 1)
							{
								grass = 1;
								
							}
														
						}

					} 
					else
					{
						//wind > 14
						i++;
						this.grass = fireDangerProduct.calculateGrass(i, SpreadA, wind, SpreadB, ffm);
						
						if (grass > 99.0)
							
						{
							grass = 99.0;
							
							if (timber> 99.00)
							{
								timber = 99.0;
								
							}
						}
						
					
					}
					if (timber <= 0)
							{
								finalizeDanger();
							}
							else
							{
								//line 29
								if (buo <= 0)
								{
									finalizeDanger();
								}
								else 
								{
									this.fload =calculateFireLoad();
									
									
									//ensure fload is grater than 0, otherwise set it to 0
									if (fload <=0)
									{
										fload = 0.0;
										
									}
									else
									{
										fload = Math.pow(10.0, fload);
										
									}
									finalizeDanger();
									
								}
							}
					
					
				}
			
		}
	}
	
	/**
	 * Calculate fire load.
	 * Uses timber and buo
	 *
	 * @return the double fire load fload
	 */
	private double calculateFireLoad() {
		// TODO Auto-generated method stub
		
		
		return 1.75* Math.log10(timber) + 0.32*Math.log10(buo) - 1.640;
	}

	/**
	 * Calculate adjusted fuel moisture.
	 *
	 * @return the double adjusted fuel moisture
	 */
	private double calculateAdjustedFuelMoisture() {
		// TODO Auto-generated method stub
		
		double adfm2 = 0;
		adfm2 =  0.9*ffm + 0.5 + 9.5* Math.exp(-buo/50);
		return adfm2;
	}

	/**
	 * Adjust ffm herb.
	 *
	 * @return the double Fuel Moisture adjusted based on herb stage
	 */
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
			ffm2 = ffm2 + (iherb -1)* 5.0;
		return ffm2;
	}

	/**
	 * Calculate dryng factor.
	 *
	 * @return the double drying factor based on Fuel Moisture and Array D
	 */
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

	/**
	 * Calculate fine fuel moisture.
	 *
	 * @return the double Fine fuel Moisture based on dry- wet  and array C.
	 */
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

	/**
	 * Calculate build up index.
	 *
	 * @return the double new BuildIup Index based on current BUI and precipitation
	 */
	private double calculateBuildUpIndex() {
		// TODO Auto-generated method stub
		double newBuo;
		
		newBuo = -50.0 * Math.log(1.0 - (Math.exp(- buo/50.0)))* Math.exp(-1.175*(precip - 0.1));
		return newBuo;
	}

	/**
	 * Finalize danger. Print all input and output values on screen.
	 */
	private void finalizeDanger() {
		// TODO Auto-generated method stub
		System.out.println("IsSnow :" + isnow);
		//System.out.println("Last Value of Build up Index (BUO):" + buo);
		System.out.println("Precipitation (PRECIP):" + precip);
		System.out.println("Current herb state of the district:" +  iherb);
	
		
		System.out.println("Grass :" + grass);
		System.out.println("Timber :" + timber);
		
		System.out.println("Wet :" + wet);
		System.out.println("Dry :" + dry);
		System.out.println("Wind :" + wind);
		System.out.println("Fine Fuel Factor (FFM) :" + ffm);
		System.out.println("Drying Factor (DF) :" + df);
		System.out.println("Difference betwen precipiation and threshold(0.1) : " + (precip -precipThreshold) );
		System.out.println("New BuildUp Index(BUI) based on precipitation (BUO) : " +buo);
		System.out.println("Adjusted Fuel Moisture(ADFM) : " + adfm);
		System.out.println("Fire Load Rating (FLOAD) : " + fload);
		
		
	}

	/**
	 * Gets the df.
	 *
	 * @return the Drying Factor 
	 */
	public double getDf() {
		return df;
	}
	
	/**
	 * Sets the df.
	 *
	 * @param df the new Drying Factor 
	 */
	public void setDf(double df) {
		this.df = df;
	}
	
	/**
	 * Gets the Fine Fuel Factor.
	 *
	 * @return the Fine Fuel Moisture
	 */
	public double getFfm() {
		return ffm;
	}
	
	/**
	 * Sets the Fine Fuel Factor.
	 *
	 * @param ffm the new Fine Fuel Moisture
	 */
	public void setFfm(double ffm) {
		this.ffm = ffm;
	}
	
	/**
	 * Gets the Adjusted Fine Fuel Moisture.
	 *
	 * @return the adfm Adjusted Fine Fuel Moisture
	 */
	public double getAdfm() {
		return adfm;
	}
	
	/**
	 * Sets the Adjusted Fine Fuel Moisture.
	 *
	 * @param adfm the new adfm Adjusted Fine Fuel Moisture
	 */
	public void setAdfm(double adfm) {
		this.adfm = adfm;
	}

	/**
	 * Gets the fload Fire Load Rating.
	 *
	 * @return the fload Fire Load Rating
	 */
	public double getFload() {
		return fload;
	}

	/**
	 * Sets the fload Fire Load Rating.
	 *
	 * @param fload the new fload Fire Load Rating
	 */
	public void setFload(double fload) {
		this.fload = fload;
	}

	/**
	 * Gets the grass.
	 *
	 * @return the grass
	 */
	public double getGrass() {
		return grass;
	}

	/**
	 * Sets the grass.
	 *
	 * @param grass the new grass
	 */
	public void setGrass(double grass) {
		this.grass = grass;
	}

	/**
	 * Gets the timber.
	 *
	 * @return the timber
	 */
	public double getTimber() {
		return timber;
	}

	/**
	 * Sets the timber.
	 *
	 * @param timber the new timber
	 */
	public void setTimber(double timber) {
		this.timber = timber;
	}

	/**
	 * Gets the precipitation.
	 *
	 * @return the precip precipitation
	 */
	public double getPrecip() {
		return precip;
	}

	/**
	 * Sets the precipitation.
	 *
	 * @param precip the new precipitation
	 */
	public void setPrecip(double precip) {
		this.precip = precip;
	}

	/**
	 * Sets the buo  BuildUp Index.
	 *
	 * @param buo the new buo  BuildUp Index
	 */
	public void setBuo( double buo) {
		// TODO Auto-generated method stub
		this.buo = buo;
		
	}
	
	/**
	 * Gets the buo  BuildUp Index.
	 *
	 * @return the buo  BuildUp Index
	 */
	public double getBuo()
	{
		
		return buo;
	}

	/**
	 * Gets the dry.
	 *
	 * @return the dry
	 */
	public double getDry() {
		return dry;
	}

	

	/**
	 * Gets the wet.
	 *
	 * @return the wet
	 */
	public double getWet() {
		return wet;
	}

	/**
	 * Gets the diff difference between dry -wet.
	 *
	 * @return the diff difference between dry -wet
	 */
	public double getDiff()
	{
		return diff;
		
	}

	/**
	 * Sets the dry.
	 *
	 * @param dry the new dry
	 */
	public void setDry(double dry) {
		// TODO Auto-generated method stub
		this.dry = dry;
	}
	

	/**
	 * Sets the wet.
	 *
	 * @param wet the new wet
	 */
	public void setWet(double wet) {
		// TODO Auto-generated method stub
		this.wet = wet;
	}

	/**
	 * Sets the diff difference between dry -wet.
	 *
	 * @param diff the new diff difference between dry -wet
	 */
	public void setDiff(double diff) {
		// TODO Auto-generated method stub
		this.diff = diff;
		
	}

	/**
	 * Gets the iherb herb stage.
	 *
	 * @return the iherb herb stage
	 */
	public double getIherb() {
		return iherb;
	}

	/**
	 * Sets the iherb herb stage.
	 *
	 * @param iherb the new iherb herb stage
	 */
	public void setIherb(double iherb) {
		this.iherb = iherb;
		
	
	}
	
	/**
	 * Sets the wind.
	 *
	 * @param wind the new wind
	 */
	public void setWind(double wind) {
		// TODO Auto-generated method stub
		this.wind = wind;
		
	}

	/**
	 * Gets the wind.
	 *
	 * @return the wind
	 */
	public double getWind() {
		return wind;
	}
	
	
	

}
