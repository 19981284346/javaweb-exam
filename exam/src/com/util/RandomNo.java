package com.util;

import java.util.Random;

public class RandomNo {
    
    public static void main (String args[]) {
    	
    	int[] lists = getRandomList(10,10);
    	for(int i=0;i<10;i++){
    		System.out.print(lists[i]+",");
    	}	
    }
    
   //����һ����Χ���������ظ����������  
   public static int[] getRandomList(int count,int b){
	   int[] list= new int[count];
	   
	   //��ʼ�����飬������һ�����������
	   for(int i=0;i<count;i++){
		   list[i]=i;
	   }
	   for(int i=0;i<count;i++){
		  int k =getRandom(0,count,b);
		  int j =getRandom(0,count,i*b);
		  int temp;
		  temp=list[k];
		  list[k]=list[j];
		  list[j]=temp;
	   }
	  
	   
	   return list;
   }
    
    
   //�õ�һ�������
    public static int getRandom(int start,int last,int i){
    	//iΪ���ӵı���
    	int rd;
    	do{
    		Random r =  new Random();
    		rd = r.nextInt(last);
    	}while(rd<start);
    	return rd;
    }
      
}


