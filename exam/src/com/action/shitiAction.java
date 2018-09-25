package com.action;

import java.security.Policy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.struts2.ServletActionContext;

import com.dao.TAdminDAO;
import com.dao.TShitiDAO;
import com.dao.TTimuDAO;
import com.dao.TKechengDAO;
import com.dao.TTimuShitiDAO;
import com.model.TAdmin;
import com.model.TShiti;
import com.model.TShiti;
import com.model.TTimu;
import com.model.TTimuShiti;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.util.Pagination;
import com.util.RandomNo;

public class shitiAction extends ActionSupport
{
	private int shitiId;
	private String shitiName;
	private String shitiJieshao;
	private int kechengId;
	
	private String message;
	private String path;
	private Integer shitiNandu;	
	private Integer danxuanCount;	
	private Integer duoxuanCount;		
	private Integer jiandaCount;		
	
	private TShitiDAO shitiDAO;
	private TTimuDAO timuDAO;	
	private TKechengDAO kechengDAO;	
	private TTimuShitiDAO timuShitiDAO;	
	
	public String shitiAdd()
	{
		TShiti shiti=new TShiti();
		shiti.setShitiName(shitiName);
		shiti.setShitiNandu(shitiNandu);
		shiti.setShitiJieshao(shitiJieshao);
		shiti.setKechengId(kechengId);
		shiti.setShitiShijian(new Date().toLocaleString());
		shiti.setDel("no");
		shitiDAO.save(shiti);
		this.setMessage("�����ɹ�");
		this.setPath("shitiMana.action");
		return "succeed";
	}
	
	public String shitiDel()
	{
		TShiti shiti=shitiDAO.findById(shitiId);
		shiti.setDel("yes");
		shitiDAO.attachDirty(shiti);
		this.setMessage("ɾ���ɹ�");
		this.setPath("shitiMana.action");
		return "succeed";
	}
	
	public String shitiAutoPre()
	{
		TShiti shiti=shitiDAO.findById(shitiId);
		Map request=(Map)ServletActionContext.getContext().get("request");
		request.put("shiti", shiti);
		return ActionSupport.SUCCESS;
	}
	
	//�Զ���� 
	public String shitiAuto()
	{
		TShiti shiti=shitiDAO.findById(shitiId);
		
		String sql1="from TTimuShiti where  shitiId="+shitiId;
		List uList=timuDAO.getHibernateTemplate().find(sql1);
		for(int j=0;j<uList.size();j++)
		{
			TTimuShiti timuS=(TTimuShiti)uList.get(j);
			timuShitiDAO.delete(timuS);		
		}
//		String sql1="from TTimu where del='no' and kechengId="+kechengId+" and timuLeixing='danxuan' order by timuId";
//		List danList=timuDAO.getHibernateTemplate().find(sql1);
//		
//		String sql2="from TTimu where del='no' and kechengId="+kechengId+" and timuLeixing='duoxuan' order by timuId";
//		List duoList=timuDAO.getHibernateTemplate().find(sql2);
//		
//		String sql3="from TTimu where del='no' and kechengId="+kechengId+" and timuLeixing='jianda' order by timuId";
//		List jiandaList=timuDAO.getHibernateTemplate().find(sql3);
		
		int iCount1=0;
		int iCount2=0;
		int iCount3=0;
		if(danxuanCount>0){
		    iCount1 =timuFind(danxuanCount,"danxuan",shiti);}
		if(danxuanCount>0){
			iCount2 =timuFind(danxuanCount,"duoxuan",shiti);}
		if(danxuanCount>0){
			iCount3 =timuFind(danxuanCount,"jianda",shiti);}
		
		shiti.setShitiShijian(new Date().toLocaleString());
		shitiDAO.attachDirty(shiti);
		
		
		this.setMessage("����������ѡ��������["+iCount1+"]��ѡ��������["+iCount2+"]�����������["+iCount3+"]");
		this.setPath("timuByShiti.action?shitiId="+shitiId);
		return "succeed";
	}
	//private ArrayList selList;
	
	
	/**
	 * Ⱦɫ��ĳ�ʼ����������������������õ�Ⱦɫ��ĳ��ȣ����ݳ�ʼ��Ⱥ�Ĵ�С������Ⱦɫ�壻
	 * Ⱦɫ������Ŀ������һ�����У����ڽ�������Ĳ���
	 * @return
	 */
	public List<List<TShiti>> ChromosomeInti(Policy p,TTimuShiti gm){
	
		 
//System.out.print("������"+gm.getId());
		//�õ�֪ʶ�������
		String kpStr = "";
		int[] knowledgePointIds = null;//֪ʶ��Ids
		if(kpStr!=null&&!kpStr.trim().equals("")){
			String[] kpStrings = kpStr.split(",");
			knowledgePointIds = new int[kpStrings.length];
			for(int i=0;i<kpStrings.length;i++){
				knowledgePointIds[i] = Integer.parseInt(kpStrings[i]);
			}
		}
		
		//�õ����͵ľ�������
		String typeStr = "";
		int[] typeIds = null;//����Id�б�
		int[] sourse=null;//���͵ķ���
		int[] counts=null;//ÿ�����͵�����
		if(typeStr!=null&&!typeStr.trim().equals("")){
			String[] typeStrings = typeStr.split(",");
			int typeLength = typeStrings.length;
			typeIds = new int[typeLength/3];
			sourse = new int[typeLength/3];
			counts =new int[typeLength/3];
			for(int i=0,j=0;i<typeLength;i+=3,j++){
				typeIds[j] = Integer.parseInt(typeStrings[i]);
				sourse[j] = Integer.parseInt(typeStrings[i+1]);
				counts[j] = Integer.parseInt(typeStrings[i+2]);
			}
		}					
		/*
		 * Ⱦɫ��ĳ�ʼ��
		 */
		
		//intiLib(knowledgePointIds,typeIds); //��ʼ��Ⱥ
		List<List<TShiti>> chromosomeList = new ArrayList<List<TShiti>>();//Ⱦɫ����б�	
		for(int i=0;i<chromosomeList.size()*2;i++){//����ϵͳ�涨��Ⱦɫ�����������������ѭ��
			List<TShiti> tqList = new ArrayList<TShiti>();//һ��Ⱦɫ��
		
			for(int j=0;j<tqList.size();j++){//��������ȥȡ����
				List<TShiti> tqTypeList = null ;//�õ���һ�����͵���������
				int[] random = RandomNo.getRandomList(tqTypeList.size(),i);//����һ����0��tqTypeList.size()-1��
//System.out.println("������======��"+counts[j]+"("+counts.length+")");																		//������飬����ȡǰ�漸�����뵽Ⱦɫ����
				for(int k=0;k<counts[j];k++){//���ݸ���������Ⱦɫ������ռ����������
					TShiti tq = (TShiti) chromosomeList.get(random[k]);
					tqList.add(tq);
				}	
				
			}
			chromosomeList.add(tqList);//��һ��Ⱦɫ����뵽Ⱦɫ���б�
			
		}
		return chromosomeList;
	}
	/**
	 * ��Ⱦɫ�����Ӧֵ
	 */
	public int getAdaptive(List<TShiti> qsList,Policy p){
		
		double errorD = 0.0;//�Ѷ����
		double errorDt = 0.0;//���ֶ����
		double errorKp = 0.0;//֪ʶ�����
		double errorLateTime = 0.0;//�������ʱ�����
		double errorExposure = 0.0;//�ع�����
		double errorTime = 0.0;//�ܵĿ���ʱ�����
		
		double sumD = 0.0;//������ѶȺ�
		double sumDt = 0.0;//���ֶȵĺ�
		double sumTime = 0.0;//����ʱ���
		double sumEx = 0.0;//�ܵ��ع��
		long sumLateTime = 0;//�ܵ��������ʱ��
	
		
		int length = qsList.size();//Ⱦɫ�峤��
		for(int i=0;i<length;i++){
			TShiti t = qsList.get(i);
			
			sumD += t.getKechengId();
			
		}
		
		
//���Բ���
//System.out.println("���Բ���");
//System.out.println("�Ѷ����"+errorD);
//System.out.println("���ֶ����"+errorDt);
//System.out.println("�ܵ��ع��"+sumEx);		
//System.out.println("����ʱ�����"+errorTime);	
		double thisAvgEx =0.0;
		if(sumEx!=0){
			thisAvgEx = sumEx/length;
		}else{
			thisAvgEx = 0;
		}
		if(thisAvgEx>0){
			errorExposure = thisAvgEx - 0;//��ƽ���ع�ȴ����
		}else{
			errorExposure = 0.0;//���С��ƽ���ع�ȣ���ô��Ⱦɫ����ع�����Ϊ0
		}	
//System.out.println("�ع�����"+errorExposure);		
		
		double avgS = sumLateTime/(double)1/(double)length;
		if(avgS>0){
			errorLateTime = avgS - 0;
		}else
		{
			errorLateTime = 0.0;
		}
		
//System.out.println("ƽ���������ʱ��������"+errorLateTime);

	//����֪ʶ�����
		String chapterStr  = null;
		int[] chapterIds = null;//�½�Ids
		int[] ratio = null;
		if(chapterStr!=null&&!chapterStr.trim().equals("")){
			String[] kpStrings = chapterStr.split(",");
			chapterIds = new int[kpStrings.length/2];
			ratio = new int[kpStrings.length/2];
			for(int i=0,j=0;i<kpStrings.length;j++,i+=2){
				chapterIds[j] = Integer.parseInt(kpStrings[i]);
				ratio[j] = Integer.parseInt(kpStrings[i+1]);
			}

		}
		
		double factor = 0.0;
		for(int i=0;i<qsList.size();i++){//�����ܵĸ�������
			TShiti qs = qsList.get(i);
			
		}
		
		
		for(int i=0;i<chapterIds.length;i++){
			double fa=0.0;
			
			double ra = fa/factor;//����ĸ����������ܵ����⸳�����ӵı���
			double errorX = Math.abs(ra-ratio[i]/(double)100);//ĳһ�µ����
			errorKp += errorX;//�����
		}

	
	//������Ӧֵ�ü��� ���ǰ���Ӧֵ�Ŵ�ʮ�򱶣�Ȼ��ȥ����С�����֣���ת��Ϊ����
	int fitenessAll = (int)((errorD+errorDt+errorKp+errorLateTime+errorExposure+errorTime)*100000);
//System.out.println("�ܵ���Ӧֵ"+fitenessAll);
	
	return fitenessAll;
	}

	
/**
 * ����Ⱦɫ�����Ӧֵ����Ⱦɫ��������
 */	
	public void sortChr(List<List<TShiti>>  chromosomeList){
		//Policy p = policyManager.getPolicyById(7);
		int[] fitnesses = new int[chromosomeList.size()];//�Ȱ���Ӧֵ�浽�����У������Ͳ����������ʹ�����Ǽ�����Ӧֵ��
		for(int i=0;i<chromosomeList.size();i++){
			fitnesses[i] = 1; 
		}
		//���ǲ��ÿ�������
		sort(fitnesses,chromosomeList,0,fitnesses.length-1);
	}
	
	private void sort(int[] number,List<List<TShiti>>  chromosomeList,int left,int right){
		if(left<right)
	    {
	      
	      int s=number[left];
	      int a = left;
	      int i=left;
	      int j=right+1;
	      while(true)
	      {
	        //������ i �Ĕ��������ҷ��ң�ֱ���ҵ���� s �Ĕ�
	        while(i+1<number.length &&number[++i]<s);
	        //������ j �Ĕ����ҷ������ң�ֱ���ҵ�С� s �Ĕ�
	        while(j>0&&number[--j]>s);
	        if(i>=j) break; //��� i >= j���t�x�_ޒȦ
	        swap(number,chromosomeList,i,j);//��� i < j���t���Q����i�cj��̎��ֵ

	      }
	      //���������j����
	      number[left]=number[j];
	      number[j]=s;
	      List<TShiti> tqLista = chromosomeList.get(a);
		  List<TShiti> tqListj = chromosomeList.get(j);
		  chromosomeList.set(a, tqListj);
		  chromosomeList.set(j, tqLista);
	      
	      sort(number,chromosomeList,left,j-1);//����߽��еݹ�

		  sort(number,chromosomeList,j+1,right);//���ұ߽��еݹ�

	   }
	  }
	
	private static void swap(int[] number,List<List<TShiti>>  chromosomeList,int i, int j) {
	    int t;
	    t=number[i];
	    number[i]=number[j];
	    number[j]=t;
//System.out.print("��"+i+"����"+j+"����");
	    List<TShiti> tqListi = chromosomeList.get(i);
	    List<TShiti> tqListj = chromosomeList.get(j);
	    chromosomeList.set(i, tqListj);
	    chromosomeList.set(j, tqListi);
	 }
	
	
	/**
	 * ���溯��
	 */
	public void crossDo(List<List<TShiti>>  chromosomeList){
		Random ra = new Random();
		int length = chromosomeList.size();
		for(int i=0;i<chromosomeList.size();i++){
			if(1<ra.nextInt(100)/(double)100){
				crossGe(chromosomeList,length);
			}
		}
	}
	/**
	 * �����ʵ��
	 * @param chromosomeList
	 * @param size
	 */
	private void crossGe(List<List<TShiti>>  chromosomeList,int size){
		
		Random ra = new Random();
		List<TShiti> tqList1 = chromosomeList.get(ra.nextInt(size));
		List<TShiti> tqList2 = chromosomeList.get(ra.nextInt(size));
		
		for(int i=0;i<tqList1.size();i++){
			if(ra.nextInt(100)/(double)100<0.3){//�ٷ�֮30�����ݽ��н���
				TShiti tq1 = tqList1.get(i);
				TShiti tq2 = tqList2.get(i);
				
				int state = 0;
				for(int j=0;j<tqList1.size();j++){
					if(tq1.getDel()==tqList2.get(j).getDel()){
						state=1;
						break;
					}
					if(tq2.getDel()==tqList1.get(j).getDel()){
						state=1;
						break;
					}
				}
				if(state==0){
					tqList1.set(i, tq2);
					tqList2.set(i, tq1);
				}else{
					i--;//���ȡ�������ⲻ���ϣ����Ǽ���ȥ��ʼ�����ȥȡ
				}
				
			}
		}
		
	}

	/**
	 * ���캯��
	 */
	public void mutationDo(List<List<TShiti>>  chromosomeList){
		Random ra = new Random();
		int length = chromosomeList.size();
		}
	
	/**
	 *�����ʵ��
	 * @param chromosomeList
	 * @param size
	 */
	private void mutationGe(List<List<TShiti>>  chromosomeList,int size){
		Random ra = new Random();
		int length = chromosomeList.size();
		List<TShiti> tqList1 = chromosomeList.get(ra.nextInt(size));//���ѡ��һ��Ⱦɫ����б���
		for(int i=0;i<tqList1.size();i++){
			
			if(ra.nextInt(100)/(double)100<0.2){//�����ѡ�е�Ⱦɫ���20%�����ݽ��б���
				TShiti tq1 = tqList1.get(i);
				TShiti tq2 = null;
				Map tyList = null;
				//���ѭ��ˮ�����������ͣ�ȥ��ʼ�������ȥȡ���ϸ����͵�����
				for(int j=0;j<tyList.size();j++){
					Type type = (Type)tyList.get(j);
					
						Map tqList2 = null;
						tq2 = (TShiti) tqList2.get(ra.nextInt(tqList2.size()));//�õ��滻������
					}	
				}
				//�жϸ������д�Ⱦɫ�����Ƿ���ڣ�������ڣ���ô�Ͳ���Ҫ�����⣬��ȥ��ʼ�����Ѱ���µ�����
				int state = 0;
				TShiti tq2 = null;
				for(int j=0;j<tqList1.size();j++){
					TTimuShiti tq1 = null;
					if(tq1.getId()==tq2.getKechengId()){
						state=1;
						break;
					}
				}
				
				if(state==0){
					tqList1.set(i, tq2);
				}else{
					i--;//���ȡ�������ⲻ���ϣ����Ǽ���ȥ��ʼ�����ȥȡ
				}
				
				
			}
		}

	
	private int timuFind(int selCount,String pLeixing,TShiti shiti)
	{
		int okCount=0;
		 ArrayList selList=new ArrayList();
		String strSel="";
		String sql1="from TTimu where del='no' and kechengId="+shiti.getKechengId()
		+" and timuLeixing='"+pLeixing+"' and timuNandu="+shiti.getShitiNandu()+" order by timuId";
		List danList=timuDAO.getHibernateTemplate().find(sql1);	
		int iList=danList.size();
		if(iList>0)
		{
			if(iList<selCount)
			{
				for(int j=0;j<danList.size();j++)
				{
					TTimu timu=(TTimu)danList.get(j);
				    selList.add(timu.getTimuId());
				    okCount++;
				}
			}
			else
			{
		        java.util.Random rd =new java.util.Random();
				while(okCount<selCount)
				{
					int xId = rd.nextInt(iList) ;
					TTimu timu=(TTimu)danList.get(xId);
					if(!strSel.contains(timu.getTimuId().toString()))
					{
						strSel+=timu.getTimuId()+",";
						selList.add(timu.getTimuId());
						okCount++;
					}
				}
			}
		}
		
		for(int j=0;j<selList.size();j++)
		{
			TTimuShiti timuS= new TTimuShiti();
			timuS.setShitiId(shiti.getShitiId());
			timuS.setTimuId(Integer.parseInt(selList.get(j).toString()));

			timuShitiDAO.save(timuS);
			
		}
		
		return okCount;
	}
	public String shitiEditPre()
	{
		TShiti shiti=shitiDAO.findById(shitiId);
		Map request=(Map)ServletActionContext.getContext().get("request");
    	String sql="from TKecheng where del='no'";
		List kechengList=kechengDAO.getHibernateTemplate().find(sql);
		request.put("kechengList", kechengList);
		request.put("shiti", shiti);
		return ActionSupport.SUCCESS;
	}
	
	public String shitiEdit()
	{
		TShiti shiti=shitiDAO.findById(shitiId);
		shiti.setShitiName(shitiName);
		shiti.setShitiNandu(shitiNandu);
		shiti.setShitiJieshao(shitiJieshao);
		shiti.setKechengId(kechengId);
		shiti.setShitiShijian(new Date().toLocaleString());
		shitiDAO.attachDirty(shiti);
		this.setMessage("���³ɹ�");
		this.setPath("shitiMana.action");
		return "succeed";
	}
	
	public String shitiMana()
	{
		String sql="from TShiti where del='no'";
		List shitiList=shitiDAO.getHibernateTemplate().find(sql);
		
		for(int i=0;i<shitiList.size();i++)
		{
			TShiti shiti=(TShiti)shitiList.get(i);
			int kechengId=shiti.getKechengId();

			shiti.setKechengName(kechengDAO.findById(kechengId).getKechengName());

		}
		
		Map request=(Map)ServletActionContext.getContext().get("request");
		request.put("shitiList", shitiList);
		return ActionSupport.SUCCESS;
	}
	
	public String shitiAll()
	{
		String sql="from TShiti where del='no'";
		List shitiList=shitiDAO.getHibernateTemplate().find(sql);
		Map request=(Map)ServletActionContext.getContext().get("request");
		request.put("shitiList", shitiList);
		return ActionSupport.SUCCESS;
	}
	
	public String shitiAll1()
	{
		String sql="from TShiti where del='no'";
		List shitiList=shitiDAO.getHibernateTemplate().find(sql);
		Map request=(Map)ServletActionContext.getContext().get("request");
		request.put("shitiList", shitiList);
		return ActionSupport.SUCCESS;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public TShitiDAO getShitiDAO()
	{
		return shitiDAO;
	}

	public void setShitiDAO(TShitiDAO shitiDAO)
	{
		this.shitiDAO = shitiDAO;
	}

	public int getShitiId()
	{
		return shitiId;
	}

	public void setShitiId(int shitiId)
	{
		this.shitiId = shitiId;
	}

	public String getShitiJieshao()
	{
		return shitiJieshao;
	}

	public void setShitiJieshao(String shitiJieshao)
	{
		this.shitiJieshao = shitiJieshao;
	}

	public String getShitiName()
	{
		return shitiName;
	}

	public void setShitiName(String shitiName)
	{
		this.shitiName = shitiName;
	}

	public Integer getShitiNandu() {
		return shitiNandu;
	}

	
	public Integer getDanxuanCount() {
		return danxuanCount;
	}

	public void setDanxuanCount(Integer danxuanCount) {
		this.danxuanCount = danxuanCount;
	}

	public Integer getDuoxuanCount() {
		return duoxuanCount;
	}

	public void setDuoxuanCount(Integer duoxuanCount) {
		this.duoxuanCount = duoxuanCount;
	}

	public Integer getJiandaCount() {
		return jiandaCount;
	}

	public void setJiandaCount(Integer jiandaCount) {
		this.jiandaCount = jiandaCount;
	}

	public TTimuDAO getTimuDAO() {
		return timuDAO;
	}

	public void setTimuDAO(TTimuDAO timuDAO) {
		this.timuDAO = timuDAO;
	}

	public TKechengDAO getKechengDAO() {
		return kechengDAO;
	}

	public void setKechengDAO(TKechengDAO kechengDAO) {
		this.kechengDAO = kechengDAO;
	}

	public TTimuShitiDAO getTimuShitiDAO() {
		return timuShitiDAO;
	}

	public void setTimuShitiDAO(TTimuShitiDAO timuShitiDAO) {
		this.timuShitiDAO = timuShitiDAO;
	}

	public int getKechengId() {
		return kechengId;
	}

	public void setKechengId(int kechengId) {
		this.kechengId = kechengId;
	}

	public void setShitiNandu(Integer shitiNandu) {
		this.shitiNandu = shitiNandu;
	}
	private int pageNow = 1; // ��ʼ��Ϊ1,Ĭ�ϴӵ�һҳ��ʼ��ʾ
	private int pageSize = 10; // ÿҳ��ʾ3����¼
	private int pageTotle = 1;// ��ҳ��
	
	public int getPageNow() {
		return pageNow;
	}
	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageTotle() {
		return pageTotle;
	}
	public void setPageTotle(int pageTotle) {
		this.pageTotle = pageTotle;
	}	
}
