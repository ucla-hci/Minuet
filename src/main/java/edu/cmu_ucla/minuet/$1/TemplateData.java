/* -------------------------------------------------------------------------
 *
 *	$1 Java
 *
 * 	This is a Java port of the $1 Gesture Recognizer by
 *	Jacob O. Wobbrock, Andrew D. Wilson, Yang Li.
 * 
 *	"The $1 Unistroke Recognizer is a 2-D single-stroke recognizer designed for 
 *	rapid prototyping of gesture-based user interfaces."
 *	 
 *	http://depts.washington.edu/aimgroup/proj/dollar/
 *
 *	Copyright (C) 2009, Alex Olwal, www.olwal.com
 *
 *	$1 Java free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	$1 Java is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with $1 Java.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  -------------------------------------------------------------------------
 */

package edu.cmu_ucla.minuet.$1;

public class TemplateData
{	public static double one[] ={18.81, 19.94, 66.56, 2.5, 2.37, -142.81, -207.94, -29.5, 19.69, 233.56, 238.12, -22.94, -31.5, -253.88, -241.25, 60.56, 106.81, 259.75, 217.87, -121.06, -23.75, -99.13, -5.81, 1.81, -1.75, 5.44};

	public static double two[] ={-0.62, 4.06, 25.56, 55.44, 2.87, -123.0, -208.81, -83.37, -27.87, 214.88, 240.69, 0.94, -13.81, -248.62, -205.25, 36.69, -66.37, 249.62, 274.88, -5.0, 35.19, -172.37, -63.63, 11.88, -20.62, 22.19, 12.31, 1.25
			};

	public static double three[] ={-1.0, 1.87, 29.12, 19.87, 49.06, -72.31, -197.0, -71.81, -71.19, 238.06, 229.38, -62.25, -5.94, -197.25, -210.31, 23.31, -32.25, 221.94, 202.19, -19.25, 16.62, -150.81, -56.31, -10.75, -12.44, 15.06, -0.88, 5.69};

	public static double four[] ={1.69, 3.06, 0.0, 12.31, 15.13, 28.87, 25.44, -69.62, -153.44, -113.31, -139.81, 208.81, 255.56, 74.94, 64.94, -201.12, -160.75, -68.25, -129.94, 205.37, 221.75, 50.12, 148.25, -226.69, -54.25, -40.69, -17.44, 20.44, 1.37, 21.06};


	public static double rightSwap[] ={1.31, 1.25, 0.44, 2.94, 27.87, 18.81, -34.75, 38.19, 32.81, -411.44, -127.19, -192.31, 126.81, 315.87, -50.25, 270.94, 55.19, -5.31, 5.19, -12.5, 0.81, -18.0};
	public static double leftSwap[] ={-20.88, -5.94, 5.75, -25.62, -71.87, -19.19, 187.19, 615.31, 33.88, -43.19, -120.62, -350.69, -37.63, -171.56, 15.94, 20.81, 17.75, 2.87};
	public static double rightSwap1[]={0.19, 6.62, -2.44, 3.0, -1.94, 40.88, -353.5, -115.31, -63.5, -445.5, 154.94, 97.12, 363.0, 416.25, 69.31, 114.0, -12.44, -33.0, 9.5, -22.19, 9.06, -11.69};
	public static double leftSwap1[]={2.44, -4.81, -3.56, -4.75, -6.44, -34.56, 97.37, 143.06, 267.0, 449.5, 15.38, -176.31, -158.94, -329.94, -75.87, -46.63, 14.0, 4.37, -7.25, 6.81, 7.44, -2.0};


	void copyReversed(int dst[], int src[])
	{		
		for (int i = 0; i < src.length/2; i++)
		{
			dst[ i*2 ] = src[src.length - i*2 - 2];  
			dst[ i*2 + 1 ] = src[src.length - i*2 + 1 - 2];  
	
		}
	}
	


}