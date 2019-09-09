// Programming Assignment 1
// Gilbert Velasquez 
// CS 3331

// The purpose of this code is to be used by a driving instructor who wants to determine where thier 3 cars are, on an oval training course. To solve this problem,
// I used the same approach as the last programming assignment. I created a velocity vs Time graph and broke down my changes in velocity into checkpints based on 
// Time and distance. In this programming assignment, we have to take into account more than one velocity. So, we make use of the following formulas: 
// T = d/v 
// v = u + at 
// v^2 = u^2 + 2ad
// The cars also start one minute apart so we need to take that into account as well. 


// Change Log: 	9/5/2019: Code Created
// 			   	9/6/2019: Code commented and polished to look more appealing. Both structure wise and output wise.

import java.text.DecimalFormat; // imported to create the desired two decimal outputs
import java.lang.Math; // imported to fix an output error that would show 2.99 instead of 3.00
class Car{

	public String name; // Name of car
	public double acceleration; // given acceleration of car 
	public double deceleration; // negative acceleration 
	double velocity1 = 20.00; // given velocity for the first segment 
	double velocity2 = 60.00; // given velocity for second segment 
	double velocity3 = 30.00; // given velocity for third segment 
	double distanceCP1; // distance after accelerating to 20 mph 
	double distanceCP2; // distance just before acclerating to 60 mph 
	double distanceDelta3; // distance travelled accelerating to 60 mph 
	double distanceCP3; // distance at beginning pf second segment 
	double distanceDelta4; // Distance between 4 and 5 to help us calculate CP4
	double distanceCP4; // distance just before declerating to 30 mph 
	double distanceCP5; // 2 miles beginning of 3 segment 
	double distanceDelta6; // distance travelled decelerating to 0 
	double distanceCP6; // distance just before declerating to 0 
	double timeStamp1; // time it takes to acclerate to  20 mph 
	double timeStamp2; // time at the end of the first segment 
	double timeStamp3; // time it takes just after accelerting to 60 mph 
	double timeStamp4; // time at which the driver declerates to 30 mph 
	double timeStamp5; // time at start of third segment 
	double timeStamp6;  // time when the driver decllerates to a stop 
	
	
	
	Car(String n){
		
		name = n;
		acceleration = 10.23;
		deceleration = -10.23;
		
		distanceCP1 = ((velocity1 * velocity1)/(2*acceleration))/3600; // must remeber to conver from miles per hour to miles per second 
		distanceCP2 = 1;
		distanceCP3 = ((((velocity2 * velocity2) - (velocity1 * velocity1))/(2* acceleration))/3600) + distanceCP2; // calculated using the change in velocity formulas 
		distanceDelta3 = (distanceCP3 - distanceCP2); // difference between  total travelled distance to this point minus the last point to get only the distance travlled when acclerating to 60 mph 
		distanceDelta4 = (((velocity3 * velocity3) - (velocity2 * velocity2))/(2*deceleration))/3600;
		distanceCP4 = 1 - (distanceDelta4 + distanceDelta3) + distanceCP3;
		distanceCP5 = distanceCP4 + distanceDelta4;
		distanceDelta6 = (velocity3*velocity3)/(2*acceleration)/3600;
		distanceCP6 = (1 - distanceDelta6) + distanceCP5; // segement 3 will just be 1 minus the distance travelled decelerating to 0, since this egement is a mile long 
		
		timeStamp1 = velocity1/acceleration;
		timeStamp2 = (((1 - distanceCP1)*3600)/velocity1) + timeStamp1; // divde by 3600 to get from mph to mps
		timeStamp3 = ((velocity2 - velocity1)/acceleration) + timeStamp2;
		timeStamp4 = (((1 -((distanceCP3 - distanceCP2) + distanceDelta4))*3600)/velocity2) + timeStamp3;
		timeStamp5 = ((velocity3 - velocity2)/acceleration) + timeStamp4;
		timeStamp6 = (((1 - distanceDelta6)*3600)/velocity3) + timeStamp5;
	}
	
	public static void main(String[] args){
		DecimalFormat formatter = new DecimalFormat("#0.00"); // used to format output to 2 decimals 
		Car CA = new Car("A"); // each instance of car, in this case 3 
		Car CB = new Car("B");
		Car CC = new Car("C");
		System.out.println(""); // spacing for output 
		System.out.printf("%-30s%-30s%-30s%-30s\n", "Time","Car A", "Car B", "Car C"); // output layouts 
		System.out.printf("%-30s%-10s%-20s%-10s%-20s%-10s%-10s\n"," ","Speed" ,"Location","Speed" ,"Location","Speed" ,"Location");
		
		for( double i = 0.00; i <= 480.00; i+= 30){ // for loop that will space our cars a minute apart, and will call the methods to calculate location and speed
			double j = (i-60); // car b start 
			double k = (i -120); // car c start 
			if(i <= 60){
				System.out.printf("%-30s%-10s%-20s%-10s%-20s%-10s%-10s\n",i,formatter.format(velocityAtTime(CA,i)),formatter.format(distanceAtTime(CA,i)), "0.00", "0.00", "0.00", "0.00");
			}
			if((i > 60) && (i <= 120)){
				System.out.printf("%-30s%-10s%-20s%-10s%-20s%-10s%-10s\n",i,formatter.format(velocityAtTime(CA,i)),formatter.format(distanceAtTime(CA,i)),formatter.format(velocityAtTime(CB,j)),formatter.format(distanceAtTime(CB,j)), "0.00", "0.00");
			}
			if(i>120){
				System.out.printf("%-30s%-10s%-20s%-10s%-20s%-10s%-10s\n",i,formatter.format(velocityAtTime(CA,i)),formatter.format(distanceAtTime(CA,i)),formatter.format(velocityAtTime(CB,j)),formatter.format(distanceAtTime(CB,j)),formatter.format(velocityAtTime(CC,k)) ,formatter.format(distanceAtTime(CC,k)));
			}
		}
	}
	
	// This method takes an instance of Car and a time and will compute the distance at the given time. it does this by using the checkpoint method. I will use time to place itself on our velocity vs time graph and then compute its current time based on the 
	// last checkpoints time and distance. It then takes into account the remaing distance need to reach our given time, and calcualtes that distance using the distance equation.  Each if statement is used to determine in between which check points our given time stands.
	// Once the car finishes it's lap i simply output 3 since it has completed the course. 
	public static double distanceAtTime(Car c, double time){
		double distance = 0.00;
		if(time < c.timeStamp1){
			distance = ((c.acceleration/3600)*time);
			return distance;
		}
		if((time >= c.timeStamp1) && (time < c.timeStamp2)){
			distance = c.distanceCP1 + ( c.velocity1* (time - c.timeStamp1))/3600;
			return distance;
		}
		if((time >= c.timeStamp2) && (time < c.timeStamp3)){
			distance = c.distanceCP2 + ((c.acceleration/3600) * (time - c.timeStamp2))/3600;
			return distance;
		}
		if((time >= c.timeStamp3) && (time < c.timeStamp4)){
			distance = c.distanceCP3 + ( c.velocity2 * (time - c.timeStamp3))/3600;
			return distance;
		}
		if((time >= c.timeStamp4) && (time < c.timeStamp5)){
			distance = c.distanceCP4 + (( c.acceleration/3600) * (time - c.timeStamp4))/3600;
			return distance;
		}
		if((time >= c.timeStamp5) && (time <c.timeStamp6)){
			distance = c.distanceCP5 + ( c.velocity3 * (time - c.timeStamp5))/3600;
			return distance;
		}
		if(time >= c.timeStamp6){
			distance = Math.round(c.distanceCP6 + ((c.acceleration/3600) * (time - c.timeStamp6))/3600); // had to round here since the output kept giving me 2.99 instead of 3 
				if(distance > 3.00){
					return 3;
				}
			return distance;
		}
	return distance;
	}
	
	// This method will given the instantenous velocity at a given time. Although since our time interval is quite big, we only ever see the car in 20,60, or 30 mph since it spends so little time accelerating to declerating to those speeds. 
	// Regardless we can use the fact that we have constant acceleration to simply plug time, accleration, and initial velocity into our kinematic equation to get an exact velocity! Again the if satements place the given time in between two checkpoints 
	// to determine at what speed the car is going. Once the car makes a full lap I decided to stop the car and therfore i output 0.
	public static double velocityAtTime(Car c, double time){
		double velocityAT = 0;
		if(time < c.timeStamp1){
			velocityAT = ( 0 + (c.acceleration* time));
			return velocityAT;
			}
			if((time >= c.timeStamp1) && (time < c.timeStamp2)){
				velocityAT = 20.0;
				return velocityAT;
			}
			if((time >= c.timeStamp2) && (time < c.timeStamp3)){
				velocityAT = ( 20.0 +(c.acceleration* time));
				return velocityAT;
			}
			if((time >= c.timeStamp3) && (time < c.timeStamp4)){
				velocityAT = 60.0;
				return velocityAT;
			}
			if((time >= c.timeStamp4) && (time < c.timeStamp5)){
				velocityAT = (60.0 - (c.acceleration* time));
				return velocityAT;
			}
			if((time >= c.timeStamp5) && (time <c.timeStamp6)){
				velocityAT = 30.0;
				return velocityAT;
			}
			if(time >= c.timeStamp6){
				velocityAT = (30.0 - (c.acceleration* time));
					if(velocityAT < 0){
						return 0;
					}
				return velocityAT;
			}
		return 0;
		}
}