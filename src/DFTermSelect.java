import java.util.List;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

//�����ĵ�Ƶ��DF������ѡ�񷽷�
public class DFTermSelect {
	public String[] TermDictionaryMain(String[] initTerms)
	{
		String label = initTerms[0].substring(0, 1);//label��ʾ������¼�����0��1��2
		List<String> allUniques = new ArrayList<String>();
		ArrayList<Integer> perLabelTermsNum =  new ArrayList<Integer>();//��������������ʸ���
		int startTermsNum = 0;//��¼ԭʼ�����ʸ����������ظ���
		int i = 0;
		while(i < initTerms.length)
		{
			//�Ѹ�������������ʲ��ظ��Ĵ���һ��List
			List<String> uniques = new ArrayList<String>();
			
			while(label.compareTo(initTerms[i].substring(0, 1)) == 0)
			{
				String[] terms = initTerms[i].split("([|])");//terms�������¼������������
				startTermsNum += terms.length;
				for(int j = 1; j < terms.length; j++)
				{
					if(!uniques.contains(terms[j]))
					{
						uniques.add(terms[j]);
					}
				}
				if(i < initTerms.length - 1)
				{
					i++;
				}
				else
				{
					break;
				}
			}
			//һ��uniques�б�����һ�����������ʣ��������ظ�
			allUniques.addAll(uniques);//allUniques�в�ͬ���������ʿ����ظ�
			perLabelTermsNum.add(uniques.size());
			if(i < initTerms.length - 1)
			{
				label = initTerms[i].substring(0, 1);
			}
			else
			{
				break;
			}
		}
		System.out.println("all labels:" + perLabelTermsNum.size());
		System.out.println(perLabelTermsNum);
		System.out.println("TermsNum:" + startTermsNum);//ȥ��ǰ�����ʸ���
		System.out.println("UniqueTermsNum:" + allUniques.size());//ȥ�غ������ʸ���
//		for(int n = 0; n < allUniques.size(); n++)
//		{
//			System.out.println(allUniques.get(n));
//		}
		
		int m = 0;//��ǰ���
		int start = 0;//����ʵĿ�ʼλ��
		int k = 0;//��ǰ��¼������
		int[] docFreq = new int[allUniques.size()];//��һ��������������С�����飬���ڴ�����дʵ��ĵ�Ƶ��
		int termIndex;
		while(m < perLabelTermsNum.size())//ѭ�����е����
		{
			int termsNum = perLabelTermsNum.get(m);//ȡ��m����������ܸ���
			//ͳ�Ƹ����еĴ����������е�DF(LocalDF)
			Dictionary<Object, Object> wordsIndex = new Hashtable<Object, Object>();//�洢�����ʵ����ݽṹ
			for(int n = start; n < start + termsNum; n++)
			{
				AddElement(wordsIndex, allUniques.get(n), n);
			}
			System.out.println("dictionary" + m + ":" + wordsIndex.size());
			start += termsNum;
			
			//ɨ���������м�¼
			while(m == Integer.parseInt(initTerms[k].substring(0, 1)))
			{
				String curDoc = initTerms[k];
				String[] terms = curDoc.split("([|])");
				
//				List<String> termsUniques = new ArrayList<String>();
//				for(int j = 1; j < terms.length; j++)
//				{
//					if(!termsUniques.contains(terms[j]))
//					{
//						termsUniques.add(terms[j]);
//					}
//				}
//				for(int j = 0; j < termsUniques.size(); j++)//ֻ���ǳ�����񣬲����Ƕ�γ���
//				{
//					Object index = wordsIndex.get(termsUniques.get(j));
//					if(index != null)
//					{
//						termIndex = (Integer)index;
//						docFreq[termIndex]++;
//					}
//				}
				for(int j = 1; j < terms.length; j++)//����һ����¼�г����ظ��������ʵ����
				{
					Object index = wordsIndex.get(terms[j]);//���������ڴʵ��е�����
					if(index != null)
					{
						termIndex = (Integer)index;
						docFreq[termIndex]++;
					}
				}
				
				if(k < initTerms.length - 1)
				{
					k++;
				}
				else
				{
					break;
				}
			}
			//��һ�����
			m++;
		}	
//		for(int j = 0; j < allUniques.size(); j++)
//		{
//			System.out.println(allUniques.get(j)+ ":" + docFreq[j]);
//		}
		
		//ȡ������ֵ�������������γ��µ������ʵ�
		List<String> DFterms = new ArrayList<String>();
		int threshold = 2;//DF��ֵ���ɵ�
		int firstNums = perLabelTermsNum.get(0);
		//��һ�����г�����ֵ�Ĵ�ֱ�Ӽ���DF�ʵ�
		for(int j = 0; j < firstNums; j++)
		{
			if(docFreq[j] > threshold)
			{
				DFterms.add(allUniques.get(j));
			}
		}
		//ʣ�������г�����ֵ�Ĵʲ��ظ�����DF�ʵ�
		for(int j = firstNums; j < allUniques.size(); j++)
		{
			if(docFreq[j] > threshold)
			{
				if(!DFterms.contains(allUniques.get(j)))
				{
					DFterms.add(allUniques.get(j));
				}
			}
		}
		System.out.println("DFthreshold:" + threshold);
		System.out.println("DFterms:" + DFterms.size());
		
//		int count = 0;
//		for(int j = 0; j < allUniques.size(); j++)
//		{
//			if(docFreq[j] > threshold)
//			{
//				System.out.println(allUniques.get(j)+ ":" + docFreq[j]);
//				count++;
//			}
//		}
//		System.out.println(count);
						
		String[] DFtermsDic = new String[DFterms.size()];
		return (String[]) DFterms.toArray(DFtermsDic);
	}
	
	//���������ֵ������Ԫ�صķ���
	private Object AddElement(Dictionary<Object, Object> collection, Object key, Object newValue)
	{
		Object element = collection.get(key);
		collection.put(key, newValue);
		return element;
	}
}
