import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.math.plot.*;

public class TchebyDiff {
	 private static Scanner clavier = new Scanner(System.in);
//	 private static ArrayList<Double> coefAdbl= new ArrayList<Double>();;
//	 private static ArrayList<Double> coefBdbl= new ArrayList<Double>();;
	 private static double[] coefMembre1,coefMembre2;
	 private static double[] x,y,x0;
	 
	 private static double a,pas;
	 private static int nbpoint;
	 
	 public static void donnee() {
	      System.out.println("Début du programme.");
		  System.out.println("Résolution numérique d'une équation différentielle à coefficient constant le second membre étant de forme polynomiale. ");
	      System.out.println();
      	  System.out.print("Entrez le pas de calcul : ");
      	  pas = clavier.nextDouble();
      	System.out.print("le nombre de point : ");
    	  nbpoint = clavier.nextInt();
    	  System.out.print("l'abscisse initiale: ");
    	  a = clavier.nextDouble();
      	  x=new double[nbpoint];
      	  y=new double[nbpoint]; 
	      System.out.println();
      	  System.out.println("Entrez les valeurs des coefficients du premier membre. ");
	      int i=0;
	      boolean lecturea=true;
	     do {
	            System.out.print("a"+i+"=");
	            try {
	            	double x = clavier.nextDouble();
	            	coefAdbl.add(x);
	            	} catch (InputMismatchException ime) {
	            	clavier.nextLine();
	            	System.out.println("Fin de lecture des coefficients du premier membre.");
	            	lecturea=false;
	            	}
	            i++;
	        } while (lecturea);
	     
	     
	     
     	 x0=new double[coefAdbl.size()-1]; 
	     System.out.println();
	     System.out.println("Entrez les valeurs des conditions initiales. ");
	     for(i = 0; i < coefAdbl.size()-1; i++){
	    	 System.out.print("x0 ("+i+") = ");
	         x0[i]= clavier.nextDouble();
	     }
	      System.out.println();
          System.out.println("Entrez les valeurs des coefficients du polynome du second membre. ");
          i=0;
	    	lecturea=true;
          do {
	            System.out.print("b"+i+"=");
	            try {
	            	double x = clavier.nextDouble();
	            	coefBdbl.add(x);
	            	} catch (InputMismatchException ime) {
		            clavier.nextLine();
		          	System.out.println();
	            	System.out.println("Fin de lecture des coefficients du second membre.");
	            	lecturea=false;
	            	}
	            i++;
	        } while (lecturea);
        	System.out.println();

          	System.out.print("Nous allons résoudre l'équation suivante : ");
          
      	System.out.print(coefAdbl.get(coefAdbl.size()-1)+"df"+i+" = ");
          for( i = 0; i < coefBdbl.size()-1; i++){
          	System.out.print(coefBdbl.get(i)+"x^"+i+" + ");
          }
      	System.out.println(coefBdbl.get(coefBdbl.size()-1)+"x^"+i);
      	System.out.print("Avec les conditions initiales : ");
      	for(i = 0; i < coefAdbl.size()-1; i++){
	    	 System.out.print("x0["+i+"] = "+x0[i]+" ");
	     }
      	System.out.println();
      	System.out.println();
      	}
	 
	 
	    public static void lectureFichier() {
	    	//String chaine="";
			String fichier ="ressources/data.tch";
			
			//lecture du fichier texte	
			try{
				InputStream ips=new FileInputStream(fichier); 
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String ligne;
				while ((ligne=br.readLine())!=null){
					
					//System.out.println(ligne);
					String str[]=ligne.split(" ");
					if(str[0].equals("pas=")){pas=Double.parseDouble(str[1]);}
					if(str[0].equals("abcsisse_initiale=")){a=Double.parseDouble(str[1]);}
					if(str[0].equals("nombre_de_point=")){nbpoint=Integer.parseInt(str[1]);}
					if(str[0].equals("coefficient_premier_membre=")){
				      	coefMembre1=new double[str.length];
						for (int i=1;i<str.length;i++){
							coefMembre1[i]=Double.parseDouble(str[i]);
						}
						}
					if(str[0].equals("coefficient_second_membre=")){
					      	coefMembre2=new double[str.length];
							for (int i=1;i<str.length;i++){
								coefMembre2[i]=Double.parseDouble(str[i]);
							}
							}
					
					
				}
				br.close(); 
			}		
			catch (Exception e){
				System.out.println(e.toString());
			}
	    }
		    public static void affichage(double[] x,double[] y) {
	      	System.out.println("Solution proposée :");
	      	System.out.println();
	    	for( int i = 0; i < x.length; i++){
		      	System.out.println("x = "+x[i]+" ; y = "+y[i]);

	    	}
	      	System.out.println();
	      	System.out.println("Fin du programme.");

	    }
	    
	    
	    
	    
	    
	    
	    public static double polynome(double[] coefpolynome,double x) {
	    	double p=0;
	    	for( int i = 0; i < coefpolynome.length; i++){
            	p=p+coefpolynome[i]*Math.pow(x,i);
	    	}
	    	return p;
	    }
	    
	    public static double[] calculDesAbcsisses(int nbpoint , double pas, double a) {
	    	double[] x;
	      	x=new double[nbpoint]; 
	      	x[0]=a;
	    	for( int i = 1; i < nbpoint; i++){
            	x[i]=x[i-1]+pas;
	    	}
	    	return x;
	    }
	    
	    public static double[] calculDesOrdonnés(double[] x) {
	    	double[] y=new double[x.length]; 
	    	for( int i = 0; i < x.length; i++){
            	y[i]=Math.pow(x[i],3)/6;
	    	}
	    	return y;
	    }
	    
	   
	   
	    
	    
	    
	    public static double[] euler(double[] x,double[] coefPoly) {
	    	double[] y=new double[x.length]; 
	    	y[0]=polynome(coefPoly,x[0]);
	    	for( int i = 1; i < x.length-1; i++){
		    	y[i]=y[i-1]+pas*polynome(coefPoly,x[i-1]);
	    	}
	    	return y;
	    }
	    
	    public static void rungeKutta2(){
	    	x[0]=x0[0];
	    	y[0]=x0[1];
	    	double k1,k2;
	    	for( int i = 0; i < nbpoint-1; i++){
	    	//	k1=pas*polynome(coefBdbl,x[i]);
	    	//	k2=pas*(polynome(coefBdbl,x[i]+pas/2)+k1/2);
	    		
		    	x[i+1]=x[i]+pas;
		    //	y[i+1]=y[i]+k2;

	    	}
	    } 

	    
	    
	    
	    
		    public static void main(String[] args) {
		    	
		    	
          //donnee();
       // define your data
		    lectureFichier();
		    double[] xs = calculDesAbcsisses(nbpoint , pas, a);
		    double[] ys = calculDesOrdonnés(xs);
		    y=euler(xs,coefMembre2);
		    affichage(xs,y);          
          
          
	 
          // create your PlotPanel (you can use it as a JPanel)
		    Plot2DPanel plot = new Plot2DPanel();
	 
          // define the legend position
		    plot.addLegend("SOUTH");
	 
          // add a line plot to the PlotPanel
		    plot.addLinePlot("Solution analytique de l'équation différentielle", xs, ys);
		    plot.addLinePlot("Solution numérique de l'équation différentielle", xs, y);
	 
          // put the PlotPanel in a JFrame like a JPanel
		    JFrame frame = new JFrame("Techbyflow : équation différentielle.");
		    frame.setSize(700, 700);
		    frame.setContentPane(plot);
		    frame.setVisible(true);
          
          
          
	 
	    }
}
