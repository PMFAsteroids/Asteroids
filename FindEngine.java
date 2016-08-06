package application;

import java.util.Random;

public class FindEngine
{
	public int [][] matrix= new int[8][12];
	public int [] two= new int [3];
	
	
	public FindEngine()
	{
		init();
	}

	private void init() 
	{
		Random rand= new Random();
		for (int i = 0; i < 8; i++) 
		{
			for (int j = 0; j <12; j++) 
			{
				matrix[i][j]=rand.nextInt(9)+1;
			}
		}
		
	}
	
	public int NumberEmpty()
	{
		int sum=0;
		for (int i = 0; i < 8; i++) 
		{
			for (int j = 0; j <12; j++) 
			{
				if(matrix[i][j]==0) sum++;
			}
		}
		return sum;
	}
	
	public int  DaLiPostoji(int x, int y)
	{
		for (int i = 0; i < 8; i++) 
		{
			for (int j = 0; j <6; j++) 
			{
				if(matrix[i][j*2]==x && matrix[i][j*2+1]==y) return 1;
			}
		}
		
		return 0;
		
	}
	
	public void setPicture()
	{
		int ind=0;
		int [] niz= new int[10];
		niz[0]=0;//1
		niz[1]=2;//3
		niz[2]=4;//5
		niz[3]=6;//7
		niz[4]=8;//9
		niz[5]=10;//11
		
		
		Random rand= new Random();
		int x=rand.nextInt(6);
		int y=rand.nextInt(8);
		
		while(true)
		{
			if((matrix[y][niz[x]]!=0) && (matrix[y][niz[x]+1]!=0) && DaLiPostoji(matrix[y][niz[x]], matrix[y][niz[x]+1])==1)
					{
						two[0]=matrix[y][niz[x]];
						two[1]=matrix[y][niz[x]+1];
						ind=1;
						break;
					}	
			
			else 
			{
				x=rand.nextInt(6);
				y=rand.nextInt(8);
			}
		}	
	}
	
}

