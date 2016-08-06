package application;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

public class MemoryEngine {

	//koja slika se gde nalazi-RESENO
	public int[][] slike=new int[6][6];
	
	//0-nije otvorena, 1-otvorena trenutno, 2-pogodjena
	public int[][] otvoreno=new int[6][6];
	
	//slike
	ImageIcon[] niz=new ImageIcon[36];
			
	//lista za generisanje-RESENO
	ArrayList<Integer> list = new ArrayList<>();

	public MemoryEngine()
	{
		initialize();		
	}
	
	public void initialize()
	{
		/*za novu igru incijalizacija;
		 * ni jedno polje nije otvoreno
		 */
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				otvoreno[i][j]=0;
			}
		}
		
		//ponovo pravimo listu
		
		for (int i = 1; i < niz.length; i++) {
			list.add(i);
			list.add(i);
		}
		
		//smestiti slike na polja
		int br=0;
		int p=35;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				
				br=new Random().nextInt(p);
				//System.out.println(br);
				p--;
				if(p==0 || p<0)
				{
					p=1;
					
				}
				slike[i][j]=list.get(br);
				list.remove(br);
			}
		}
		
	}

}

