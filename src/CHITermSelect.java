import java.util.*;



public class CHITermSelect {
	//���ÿ���ͳ������������ѡ��
	
	int numOfClass;//ѵ�����а�������������������Ϊ3
	
	public CHITermSelect()	{ numOfClass=3;	}
	
	public CHITermSelect(int num)
	{ numOfClass=num;	}
	

	 public String[] TermDictionaryMain(String[] docs)
	 {
		 int numOfDocs=docs.length;//�������ĵ�����
		 LinkedList<String> allTerm=new LinkedList<String>();//��������ĵ��а��������е���
		 for(int i=0;i<numOfDocs;i++)//�����������е��ʵĵ��ʱ�
		 {
			 String d=docs[i];
			 
			 if (d.length()>2)
			 d=d.substring(2);
			 else
				 continue;
			 String[] terms=d.split("\\|");
			 LinkedList<String> termlist=new LinkedList<String>();//��ŵ�ǰ�ĵ��������ĵ���
			 for(int j=0;j<terms.length;j++)
			 {
				 if(!termlist.contains(terms[j]))
				 {
					 termlist.add(terms[j]);
				 }
			 }
			 for(int j=0;j<termlist.size();j++)
			 {
				 if(!allTerm.contains(termlist.get(j)))
				 {
					 allTerm.add(termlist.get(j));
				 }
			 }
			 
		 }
		 
		 
		 int numOfTerm=allTerm.size();
		 System.out.println("�ʵ��ܸ�����"+numOfTerm);
		 int[][]weights=new int[numOfTerm][numOfClass*2];//��¼ÿ��������ÿ�����г��ֵĴ���������Ŷ�ӦallTerm�ж�Ӧλ�õĵ���
		                                        //��0�б�ʾ��������0�г��ֵĴ�������1�б�ʾ��������1�г��ֵĴ�������2�б�ʾ��������2�г��֣���3�б�ʾ��������0�в����֣���4�б�ʾ��������1�в����֣���5�б�ʾ��������2�в����֣�
		 for(int i=0;i<numOfTerm;i++)
			 for(int j=0;j<numOfClass*2;j++)
				 weights[i][j]=0;
		 
		 for(int i=0;i<numOfDocs;i++)//ͳ��ÿ��������ÿ�����г��ֵĴ���
		 {
			 String d=docs[i];
			 int classNo=Integer.parseInt(d.substring(0, 1));
			 if (d.length()>2)
				 d=d.substring(2);
				 else
					 continue;
			 String[] terms=d.split("\\|");
			 LinkedList<String> termlist=new LinkedList<String>();//��ŵ�ǰ�ĵ��������ĵ���
			 for(int j=0;j<terms.length;j++)
			 {
				 if(!termlist.contains(terms[j]))
				 {
					 termlist.add(terms[j]);
				 }
			 }
			 for(int j=0;j<allTerm.size();j++)
				 weights[j][numOfClass+classNo]++;
			 
			 for(int j=0;j<termlist.size();j++)
			 {
				 int tempindex=allTerm.indexOf(termlist.get(j));
				 if(tempindex>=0)
				 {
					 weights[tempindex][numOfClass+classNo]--;
					 weights[tempindex][classNo]++;
				 }
			 }
		 }
		 //���㿨��ֵ
		 double[] finalweight=new double[numOfTerm];
		 for(int i=0;i<numOfTerm;i++)
		 {
			 finalweight[i]=0;
			 int[] tt=weights[i];
			 for (int j=0;j<numOfClass;j++)
			 {
				 double temp=(1.0*(tt[j]+tt[j+numOfClass])/docs.length)*((tt[0]+tt[1]+tt[2]));
				 finalweight[i]+=(tt[j]-temp)*(tt[j]-temp)/temp;
				 temp=(1.0*(tt[j]+tt[j+numOfClass])/docs.length)*((tt[3]+tt[4]+tt[5]));
				 finalweight[i]+=(tt[j+numOfClass]-temp)*(tt[j+numOfClass]-temp)/temp;
			 }
		 }
		
		 class MyType{
			 String data;
			 double weight;
			 MyType(String d, double w)
			 {
				 data=d;
				 weight=w;
			 }
		 }
		 Comparator<MyType> myComparator=new Comparator<MyType>(){
			 public int compare(MyType a,MyType b)
			 {
				 if(a.weight-b.weight<0)
					 return -1;
				 if(a.weight-b.weight>0)
				 return 1;
				 return 0;
			 }
		 };
		 //�Ե��ʰ�����ͳ������������
		 LinkedList<MyType> tmpList=new LinkedList<MyType>();
		 for(int i=0;i<numOfTerm;i++)
		 {
			 MyType my=new MyType(allTerm.get(i),finalweight[i]);
			 tmpList.add(my);
		 }
		 //Collections.sort(tmpList,myComparator);//�ú�������������
		 Collections.sort(tmpList,Collections.reverseOrder(myComparator));
		 
		 //����ȡ���е��ʵ�30%��Ϊ��������ѡȡ
		 
		 //int tmpNum=(numOfTerm*3/10);
		 int tmpNum=(int) (numOfTerm*0.3);
		 System.out.println("�ܴ�����"+tmpNum);
		 String[] result=new String[tmpNum];
		 for(int i=0;i<tmpNum;i++)
			 result[i]=tmpList.get(i).data;
		 return result;
	 }
	 
	

}
