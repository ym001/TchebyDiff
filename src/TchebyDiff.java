import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.math.plot.*;
import javax.script.*;


public class TchebyDiff {
	 private static Scanner clavier = new Scanner(System.in);
	 private static ArrayList<Double> coefAdbl= new ArrayList<Double>();;
	 private static ArrayList<Double> coefBdbl= new ArrayList<Double>();;
	 private static double[] coefMembre1,coefMembre2;
	 private static double[] y0;	 
	 private static double a,pas;
	 private static int nbpoint,ordremembre1;
	 private static String solution="";
	 
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
      	 // x=new double[nbpoint];
      	 // y=new double[nbpoint]; 
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
	     
	     
	     
     	 y0=new double[coefAdbl.size()-1]; 
	     System.out.println();
	     System.out.println("Entrez les valeurs des conditions initiales. ");
	     for(i = 0; i < coefAdbl.size()-1; i++){
	    	 System.out.print("x0 ("+i+") = ");
	         y0[i]= clavier.nextDouble();
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
	    	 System.out.print("y0["+i+"] = "+y0[i]+" ");
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
					String str[]=ligne.split(" ");
					if(str[0].equals("pas=")){pas=Double.parseDouble(str[1]);}
					if(str[0].equals("abcsisse_initiale=")){a=Double.parseDouble(str[1]);}
					if(str[0].equals("nombre_de_point=")){nbpoint=Integer.parseInt(str[1]);}
					if(str[0].equals("ordre_premier_membre=")){ordremembre1=Integer.parseInt(str[1]);}
					if(str[0].equals("solution=")){solution=str[1];}

					if(str[0].equals("condition_aux_limites=")){
						y0=new double[str.length];
						for (int i=1;i<str.length;i++){
							y0[i]=Double.parseDouble(str[i]);
						}
						}
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
	    	double[] x=new double[nbpoint]; 
	      	x[0]=a;
	    	for( int i = 1; i < nbpoint; i++){
            	x[i]=x[i-1]+pas;
	    	}
	    	return x;
	    }
	    
	    public static double[] calculSolutionAnalytique(double[] x,String solutionAnalytique)throws Exception  {
	    	double[] y=new double[x.length]; 

	    	 // create a script engine manager
		    ScriptEngineManager factory = new ScriptEngineManager();
		    // create a JavaScript engine
		    ScriptEngine engine = factory.getEngineByName("JavaScript");
		    // evaluate JavaScript code from String
		    Object obj ;
		    String sy;
	    	for( int i = 0; i < x.length; i++){
	    		sy=solutionAnalytique;
	    		sy = sy.replaceAll("x",String.valueOf(x[i])); 
	    		obj = engine.eval(sy) ;
            	y[i]=Double.parseDouble(obj.toString());
	    	}
	    	return y;
	    }
	   
	    public static double[] integrationEuler(double h,double gi,double[] f) {
	    	double[] g=new double[f.length]; 
	    	g[0]=gi;
	    	for( int i = 1; i < f.length; i++){
		    	g[i]=g[i-1]+h*f[i-1];
	    	}
	    	return g;
	    }
	    
	    public static double[] constanteIntegration(double c,double[] y) {
	    	for( int i = 0; i < y.length; i++){
		    	y[i]=y[i]+c;
	    	}
	    	return y;
	    }
	     
	   // 
	    public static double[] euler(double h,double yi,double[] x,double[] y) {
	    	y[0]=yi;
	    	for( int i = 1; i < y.length; i++){
		    	y[i]=y[i-1]+h*polynome(coefMembre2,x[i-1]);
	    	}
	    	return y;
	    }
	    
	    public static double[] rungeKutta(double h,double yi,double[] x,double[] y){
	    	y[0]=yi;
	    	double k0;
	    	for( int i = 1; i < y.length; i++){
		    	y[i]=y[i-1]+h*polynome(coefMembre2,x[i-1]+h/2);
	    	}
	    	return y;
	    } 
	    
		    public static void main(String[] args)throws Exception{   
			    
		    // define your data

		    lectureFichier();

		    double[] x =new double[nbpoint];
		    double[] y =new double[nbpoint];
		    double[] ye =new double[nbpoint];
		    double[] yrc =new double[nbpoint];
		    double[] xs = calculDesAbcsisses(nbpoint , pas, a);
		    double[] ys ;
		    ys = calculSolutionAnalytique(xs,solution);

		    x=xs;
		    y[0]=0;
		    for( int i = 1; i < y.length; i++){
		    	y[i]=polynome(coefMembre2,x[i]);
	    	}
		    //affichage(x,y);
		    ye=euler(pas,y0[1],x,ye);
		    ye=constanteIntegration(y0[1],ye);
		    for(int i=0;i<ordremembre1-1;i++){
		    	ye=integrationEuler(pas,y0[1],ye);
		    	ye=constanteIntegration(y0[1],ye);
		    }
		    yrc=rungeKutta(pas,y0[1],x,yrc);
		    yrc=constanteIntegration(y0[1],yrc);
		    for(int i=0;i<ordremembre1-1;i++){
		    	yrc=integrationEuler(pas,y0[1],yrc);
		    	yrc=constanteIntegration(y0[1],yrc);
		    }
		    
          // create your PlotPanel (you can use it as a JPanel)
		    Plot2DPanel plot = new Plot2DPanel();
	 
          // define the legend position
		    plot.addLegend("SOUTH");
	 
          // add a line plot to the PlotPanel
		    plot.addLinePlot("Solution analytique.", xs, ys);
		    plot.addLinePlot("Solution avec méthode d'Euler.", x, ye);
		    plot.addLinePlot("Solution avec méthode Runge-Kutta.", x, yrc);
	 
          // put the PlotPanel in a JFrame like a JPanel
		    JFrame frame = new JFrame("Techbyflow (équation différentielle) : d"+ordremembre1+"f/dx"+ordremembre1+" = "+solution);
		    frame.setSize(700, 700);
		    frame.setContentPane(plot);
		    frame.setVisible(true);
	 
	    }
}
