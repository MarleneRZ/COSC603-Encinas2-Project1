/*Copyright (c) 2016 - $(year), $(author), All rights reserved
 * 
 *<p>

 *This is a reengineering work from Fortran 77 to Java to calculate the National Fire Danger ratings and Fire Load Index..
 *this program is developed according to the Java Programming Style Guidelines and
 *Maintainable Java code recommendation discussed in class COSC 603 - Software Testing  and Maintenance 
 *
 */
package com.COSC603.FireDanger;


// TODO: Auto-generated Javadoc

/**
 * The Class FireIndexes.
 * 
 * 
 *@author   Marlene R. Encinas
 *@version  1.0
 *@since    February 26 2016
 *<p>
 *Contains the main method. This class uses FireDander class to calculate the fire  danger indexes and fire load index.
 *<BR> Input parameters for calculation are:<BR>
 * dry : dry bulb temperature <BR>
 * wet : wet bulb temperature <BR>
 * isnow : if snow in the ground <BR>
 * wind :  current wind speed (mph)<BR>
 * buo: last values of the buildup index<BR>
 * iherb: current herb state of the district 1= cured;2= transition; 3 = green
		<BR>
 * Output parameters are:<BR>
 * df : drying factor,<BR>
 * ffm: fine fuel moisture<BR>
 * adfm: adjusted (10 day lag) fuel moisture<BR>
 * grass: grass spread index<BR>
 * timber: timber spread index<BR>
 * fload: fire load rating (max-hour base)<BR>
 * buo: New value of the buildup index <BR>
		
 */
 
public class FireIndexes {
	

	/**
	 * The main method.
	 * Initialize all input variables.
	 * Creates an instance of  FireDaner class to calculate
	 * the National Fire Danger ratings and Fire Load Index
	 * and printout all input and output parameters.
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println("Hello From FireDanger! main");
		
		 FireDanger fireDanger = new FireDanger();
		
		 // Initializing data used in computing the danger ratings
		 //Parameter Array A
		 fireDanger.A[0]= -0.185900;
		 fireDanger.A[1]= -0.85900;
		 fireDanger.A[2]= -0.059660;
		 fireDanger.A[3]= -0.77373;
		 
		 //Parameter Array B
		 fireDanger.B[0]= 30.0;
		 fireDanger.B[1]= 19.2;
		 fireDanger.B[2]= 13.8;
		 fireDanger.B[3]= 22.5;
		 
		 //Parameter Array C
		 fireDanger.C[0]= 4.5;
		 fireDanger.C[1]= 12.5;
		 fireDanger.C[2]= 27.5;
		 
		 //Parameter Array D
		 fireDanger.D[0]= 16.0;
		 fireDanger.D[1]= 10.0;
		 fireDanger.D[2]= 7.0;
		 fireDanger.D[3]= 5.0;
		 fireDanger.D[4]= 4.0;
		 fireDanger.D[5]= 3.0;
		 
				  
		 //Initializing Spread Index arrays
		  
		 fireDanger.SpreadA[0] = 0.01312;
		 fireDanger.SpreadA[1] = 0.009184;
		 fireDanger.SpreadB[0] = 6.0;
		 fireDanger.SpreadB[1] = 14.4;
		  
		 //initializing variables
		  fireDanger.setIsnow(0);
		  fireDanger.setFfm(99.0);//ffm=99.0;
		  fireDanger.setAdfm(99.0);// = 99.0;
		  fireDanger.setDf(0.0); //= 0;
		  fireDanger.setFload(0.0);// = 0;
		  fireDanger.setPrecip(1.0);
		 
		  //BUO can go between 0 and more than 100
		  //buo>0 and < 25 is Normal
		  //buo >=25 < 40 is Critical
		  //buo > 40 is extreme
		   fireDanger.setBuo(35);
		   
		   fireDanger.setWet(0.10); //wet =10 %
		   fireDanger.setDry(0.80); //dry  =12%
		   double wet;
		   double dry;
		   
		   wet = fireDanger.getWet();
		   dry = fireDanger.getDry();
		   fireDanger.setDiff(dry - wet);
		   fireDanger.setWind(14);
		   fireDanger.setIherb(2.0); //	current herb state of the district 1= cured;2= transition; 3 = green
		 
		   //Calculate Indexes and display them
			fireDanger.calculateFireDanger();
	}
}
