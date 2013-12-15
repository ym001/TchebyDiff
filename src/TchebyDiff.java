import java.io.BufferedReader;
import java.awt.event.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.math.plot.*;
import javax.script.*;

public class TchebyDiff  {

	public static double[] coefMembre2,cl,x,ye,yrc,ys;
	public static double a,pas;
	public static int nbpoint,ordremembre1;
	public static String solution="";
	public static String donne="";
	public static Plot2DPanel plot = new Plot2DPanel();
	public static JTextArea text=new JTextArea("", 10, 10);
	 
	    //Lecture des données dans le fichier data.tch
	    public static void lectureFichier() {
			String fichier ="data.tch";
			//Lecture du fichier texte	
			try{
				//InputStream ips=new FileInputStream(fichier); 
				//InputStreamReader ipsr=new InputStreamReader(ips);
				//BufferedReader br=new BufferedReader(ipsr);
				
				 File f = new File (fichier);
				 FileReader fr = new FileReader (f);
				 BufferedReader br = new BufferedReader (fr);
				    
				String ligne;
				while ((ligne=br.readLine())!=null){
					donne=donne+ligne+System.getProperty("line.separator");
					String str[]=ligne.split(" ");
					if(str[0].equals("pas=")){pas=Double.parseDouble(str[1]);}
					if(str[0].equals("abcsisse_initiale=")){a=Double.parseDouble(str[1]);}
					if(str[0].equals("nombre_de_point=")){nbpoint=Integer.parseInt(str[1]);}
					if(str[0].equals("ordre_premier_membre=")){ordremembre1=Integer.parseInt(str[1]);}
					if(str[0].equals("solution=")){solution=str[1];}

					if(str[0].equals("condition_aux_limites=")){
						cl=new double[str.length-1];
						for (int i=0;i<str.length-1;i++){
							cl[i]=Double.parseDouble(str[i+1]);
						}
						}
					if(str[0].equals("coefficient_second_membre=")){
					      	coefMembre2=new double[str.length-1];
							for (int i=0;i<str.length-1;i++){
								coefMembre2[i]=Double.parseDouble(str[i+1]);
							}
							}					
				}
				br.close(); 
			}		
			catch (Exception e){
				System.out.println(e.toString());
			}
	    }
	    
	    public static void lectureText() {
			//Lecture du textarea.
			try{
				String ligneDuText[]=text.getText().split(System.getProperty("line.separator"));
				donne="";
				for (int j=0;j<ligneDuText.length;j++){
					String str[]=ligneDuText[j].split(" ");
					if(str[0].equals("pas=")){pas=Double.parseDouble(str[1]);}
					if(str[0].equals("abcsisse_initiale=")){a=Double.parseDouble(str[1]);}
					if(str[0].equals("nombre_de_point=")){nbpoint=Integer.parseInt(str[1]);}
					if(str[0].equals("ordre_premier_membre=")){ordremembre1=Integer.parseInt(str[1]);}
					if(str[0].equals("solution=")){solution=str[1];}

					if(str[0].equals("condition_aux_limites=")){
						cl=new double[str.length-1];
						for (int i=0;i<str.length-1;i++){
							cl[i]=Double.parseDouble(str[i+1]);
						}
						}
					if(str[0].equals("coefficient_second_membre=")){
					      	coefMembre2=new double[str.length-1];
							for (int i=0;i<str.length-1;i++){
								coefMembre2[i]=Double.parseDouble(str[i+1]);
							}
							}					
				}
			}		
			catch (Exception e){
				System.out.println(e.toString());
			}
	    }
		    
		public static void ecritureFichier(){
			String fichier="data.tch";
			try {
				FileWriter fw = new FileWriter (fichier);
				BufferedWriter bw = new BufferedWriter (fw);
				PrintWriter fichierSortie = new PrintWriter (bw); 
				fichierSortie.println (text.getText()); 
				fichierSortie.close();
				}
				catch (Exception e){
					System.out.println(e.toString());
				}		
		}
		
	    //Calcul de la valeur d'un polynome.
	    //Entrée : un tableau de double  coefpolynome représentant les coefficients du polynome;
	    //         un double x valeur de l'abscisse.
	    //Sortie : un double p valeur du polynome à l'abscisse x
	    public static double polynome(double[] coefpolynome,double x) {
	    	double p=0;
	    	for( int i = 0; i < coefpolynome.length; i++){
            	p=p+coefpolynome[i]*Math.pow(x,i);
	    	}
	    	return p;
	    }
	    
	    //Calcul de la valeur des abscisses de chaque point.
	    //Entrée : un entier nbpoint représentant le nombre de point;
	    //		   un double pas représentant le pas de calcul;
	    //         un double a représentant l'abscisse initiale du calcul des points.
	    //Sortie : un tableau de double x représentant la valeur des abscisses de chaque point.
	    public static double[] calculDesAbcsisses(int nbpoint , double pas, double a) {
	    	double[] x=new double[nbpoint]; 
	      	x[0]=a;
	    	for( int i = 1; i < nbpoint; i++){
            	x[i]=x[i-1]+pas;
	    	}
	    	return x;
	    }
	    
	    //Calcul des ordonnées de la slution analytique
	    //Entrée : un tableau x de double x valeur des abscisses;
	    //		   une chaine de caractère solutionAnalytique contenant l'expression de la solution analytique proposée.
	    //Sortie : un tableau de double y contenant les valeurs des ordonnées de la solution.
	    public static double[] calculSolutionAnalytique(double[] x,String solutionAnalytique)
	    		//throws Exception  
	    		//throws ScriptException
	    		{
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
	    		try{obj = engine.eval(sy) ;
	    		y[i]=Double.parseDouble(obj.toString());
	    		}
	    		catch (Exception e) {
	    		      e.printStackTrace();
	    		    }
            	
	    	}
	    	return y;
	    }
	    
	    //Calcul numérique d'une primitive par la méthode d'Euler.
	    //Entrée : un double h valeur du pas de calcul;
	    //		   un double ci constante d'intégration;
	    //		   un tableau de double f représentant la fonction.
	    //Sortie : un tableau de double g primitive de f
	    public static double[] integrationEuler(double h,double ci,double[] f) {
	    	double[] g=new double[f.length];
	    	g[0]=g[0]+ci;
	    	for( int i = 1; i < f.length; i++){
		    	g[i]=g[i-1]+h*f[i-1];
	    	}
	    	return g;
	    }

	    //Calcul de la solution numérique par la méthode d'Euler.
	    //Entrée : un double h représentant le pas ;
	    //		   un tableau de double x abscisse de la solution ;
	    //		   un double ci constante d'intégration;
	    //		   un tableau de double y ordonnée de la solution.
	    //Sortie : un tableau de double y ordonnée de la solution.
	    public static double[] euler(double h,double ci,double[] x,double[] y) {
	    	y[0]=ci;
	    	for( int i = 1; i < y.length; i++){
		    	y[i]=y[i-1]+h*polynome(coefMembre2,x[i-1]);
	    	}
	    	return y;
	    }
	    
	    //Calcul de la solution numérique par la méthode de Runge-Kutta.
	    //Entrée : un double h représentant le pas ;
	    //		   un double ci constante d'intégration;
	    //		   un tableau de double x abscisse de la solution ;
	    //		   un tableau de double y ordonnée de la solution.
	    //Sortie : un tableau de double y ordonnée de la solution.
	    public static double[] rungeKutta4(double h,double ci,double[] x,double[] y){
	    	double k1,k2,k3,k4;
	    	y[0]=ci;
	    	for( int i = 0; i < y.length-1; i++){
	    		k1 = h*polynome(coefMembre2,x[i]);
	    		k2 = h*polynome(coefMembre2,x[i]+h/2);
	    		k3 = k2;
	    		k4 = h*polynome(coefMembre2,x[i]+h);
	    		y[i+1] = y[i]+(k1+2*k2+2*k3+k4)/6;
	    	}
	    	return y;
	    } 
	    public static void calculGlobal() {
	    	//lectureFichier();
	    	lectureText();
    		x = calculDesAbcsisses(nbpoint , pas, a);
    		ye =new double[nbpoint];
    		yrc =new double[nbpoint];		    
    		ys = calculSolutionAnalytique(x,solution);

    		ye=euler(pas,cl[0],x,ye);
    		for(int i=1;i<ordremembre1;i++){
    			ye=integrationEuler(pas,cl[i],ye);
    		}
    		yrc=rungeKutta4(pas,cl[0],x,yrc);
    		for(int i=1;i<ordremembre1;i++){
    			yrc=integrationEuler(pas,cl[i],yrc);
    		}
    		plot.removeAllPlots ();
    		plot.addLinePlot("Solution analytique.", x, ys);
    		plot.addLinePlot("Solution avec la méthode d'Euler.", x, ye);
    		plot.addLinePlot("Solution avec la méthode de Runge-Kutta4.", x, yrc); 
	    }
		    public static void main(String[] args)
		    		{   
		    // define your data
		    lectureFichier();

		    double[] x = calculDesAbcsisses(nbpoint , pas, a);
		    double[] ye =new double[nbpoint];
		    double[] yrc =new double[nbpoint];
		    double[] ys ;
		    
		    ys = calculSolutionAnalytique(x,solution);

		    ye=euler(pas,cl[0],x,ye);
		    for(int i=1;i<ordremembre1;i++){
		    	ye=integrationEuler(pas,cl[i],ye);
		    }
		    yrc=rungeKutta4(pas,cl[0],x,yrc);
		    for(int i=1;i<ordremembre1;i++){
		    	yrc=integrationEuler(pas,cl[i],yrc);
		    }
		  
		    JFrame frame = new JFrame("Techbydiff (équation différentielle)");
			/*Reglage de la taille de la fenêtre*/
		    frame.setSize(700, 700);
		    /*Positionnement de la fenêtre*/
			frame.setLocation(10,10);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//On définit le layout à utiliser sur le content pane			
			GridBagLayout gbl= new GridBagLayout();
			GridBagConstraints gbc = new GridBagConstraints();
			frame.setLayout(gbl);
		    JButton bouton1 = new JButton("Résolution numérique de l'équation");
		    bouton1.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) 
	        		{calculGlobal();
		    	 } } ); 

		    JButton bouton2 = new JButton("Enregistrer la configuration");
		    bouton2.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) 
		    { ecritureFichier(); } } );
		    JButton bouton3 = new JButton("Quitter");
		    bouton3.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) 
		        { System.exit(0); } } );
		    text.setLineWrap(true);
		    text.setWrapStyleWord(true);
		    text.append(donne);
		    JScrollPane js=new JScrollPane(text);
		    js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

	        gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.ipadx = 0;
			gbc.ipady = 0;
			gbc.gridx = 0;
			gbc.gridy = 0;

			gbc.gridwidth = 3;
			gbc.gridheight = 6;
			gbc.fill = GridBagConstraints.HORIZONTAL;
		    
		    frame.getContentPane().add(js,gbc);

		    gbc.weightx = 0.01;
			gbc.weighty = 0.01;
			gbc.gridx = 0;
			gbc.gridy = 6;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.fill = GridBagConstraints.NONE;

		    
		    frame.getContentPane().add(bouton3,gbc);
		    gbc.ipady = 0;
		    gbc.gridx = 1;
			gbc.gridy = 6;
			
		    frame.getContentPane().add(bouton1,gbc);
		    gbc.ipady = 0;
		    gbc.gridx = 2;
			gbc.gridy = 6;
		
		    frame.getContentPane().add(bouton2,gbc);
		    
		    gbc.weightx = 1;
			gbc.weighty = 1;
		    gbc.gridx = 0;
			gbc.gridy = 7;
			gbc.gridwidth = 3;
			gbc.gridheight = 5;
			gbc.fill = GridBagConstraints.BOTH;
			
     		// Define the legend position
     		plot.addLegend("SOUTH");
    		// Add a line plot to the PlotPanel
    		plot.addLinePlot("Solution analytique.", x, ys);
    		plot.addLinePlot("Solution avec la méthode d'Euler.", x, ye);
    		plot.addLinePlot("Solution avec la méthode de Runge-Kutta4.", x, yrc); 
    		
    		gbc.weightx = 1;
 			gbc.weighty = 1;
 		    gbc.gridx = 0;
 			gbc.gridy = 7;
 			gbc.gridwidth = 3;
 			gbc.gridheight = 5;
 			gbc.fill = GridBagConstraints.BOTH;
 			
     		
 		    frame.getContentPane().add(plot,gbc);
		    frame.setVisible(true);
	    }
}
