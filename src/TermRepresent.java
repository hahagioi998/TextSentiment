
import java.util.*;

//��ģ��Ĺ����Ǹ��������ʵ䣬��ԭʼ�ı�������������ʾ��ʹ��TF*IDF�㷨
public class TermRepresent {
	
	private static String[] docs;//������ı���
	private static String[] terms;//����������ʵ�
	private int numDocs;//�ܼ�¼����
	private int numTerms;//�������ʸ���
	private int[][] termFreq;
	private float[][] termWeight;
	private int[] maxTermFreq;//��¼������¼�г��ִ�������������Ƶ��
	private int[] docFreq;
	private Dictionary wordsIndex = new Hashtable();//����ʹ�õ������ʵ�
	private String[] trDocs;//�ı�����������ʾ
	
	//���������ʵ��ԭʼ�ı�������������ʾ������String���͵�����
	public String[] TermRepresentMain(String[] allDocs, String[] termDic)
	{
		docs = allDocs;
		terms = termDic;
		numDocs = allDocs.length;//��¼������
		numTerms = termDic.length;//��������ά��
		maxTermFreq = new int[numDocs];
		docFreq = new int[numTerms];
		termFreq = new int[numTerms][];
		termWeight = new float[numTerms][];
		
		//�����������ʼ��������ʵ�wordsIndex��
		for(int i = 0; i < terms.length; i++)
		{
			termWeight[i] = new float[numDocs];
			termFreq[i] = new int[numDocs];
			AddElement(wordsIndex, terms[i], i);
		}
		
		//����TF
		GenerateTermFrequency();
		//����Weight
		GenerateTermWeight();
		
//		//����άȨֵ����WeightתΪKNN��Ҫ�ĸ�ʽ
//		System.out.println(termWeight.length + ", " + termWeight[0].length);
//		String[] result = new String[docs.length];
//		for(int i = 0; i < termWeight[0].length; i++)
//		{
//			result[i] = docs[i].substring(0, 1);
//			for(int j = 0; j < termWeight.length; j++)
//			{
//				result[i] += " " + termWeight[j][i];
//			}
//		}
		
		//����άȨֵ����WeightתΪSVM��Ҫ�ĸ�ʽ
		System.out.println("termDim:" + termWeight.length + ", recordLength:" + termWeight[0].length);
		String[] result = new String[docs.length];
		for(int i = 0; i < termWeight[0].length; i++)
		{
			result[i] = docs[i].substring(0, 1);
			for(int j = 0; j < termWeight.length; j++)
			{
				if(termWeight[j][i] != 0)
				{
					result[i] += " " + j + ":" + termWeight[j][i];
				}
			}
		}
		
		return result;
	}
	
	//���������ֵ������Ԫ�صķ���
	private Object AddElement(Dictionary<Object, Object> collection, Object key, Object newValue)
	{
		Object element = collection.get(key);
		collection.put(key, newValue);
		return element;
	}
	
	//����TF���������ڸ�����¼�г��ֵĴ�����
	private void GenerateTermFrequency()
	{
		for(int i = 0;i < numDocs; i++)
		{
			String curDoc = docs[i];
			Dictionary freq = GetWordFrequency(curDoc);
			Enumeration enums = freq.keys();
			while(enums.hasMoreElements())
			{
				String word = (String)enums.nextElement();
				int wordFreq = (Integer)freq.get(word);
				int termIndex = GetTermIndex(word);
				if(termIndex == -1)//�������ʲ��������ʵ���
				{
					continue;
				}
//				System.out.println(word);
				termFreq[termIndex][i] = wordFreq;
				docFreq[termIndex]++;
				
				if(wordFreq > maxTermFreq[i])
				{
					maxTermFreq[i] = wordFreq;
				}
			}
//			System.out.println();
//			for(int j = 0; j < numTerms; j++)
//			{
//				System.out.print(termFreq[j][i]);
//			}
//			System.out.println();
		}
//		for(int i = 0; i < numDocs; i++)
//		{
//			System.out.println(maxTermFreq[i]);
//		}
	}
	private Dictionary GetWordFrequency(String input)//�������¼���������ʵ�Ƶ��
	{
		String convertedInput = input.toLowerCase();
		String[] words = convertedInput.split("([|])");
		Arrays.sort(words);
		
		String[] distinctWords = GetDistinctWords(words);//ȥ��������¼�ظ���������
		
		Dictionary result = new Hashtable();
		for(int i = 0; i < distinctWords.length; i++)
		{
			Object temp;
			temp = CountWords(distinctWords[i], words);
			result.put(distinctWords[i], temp);
		}
		return result;
	}
	private static String[] GetDistinctWords(String[] input)//ȥ��������¼�ظ���������
	{
		if(input == null)
		{
			return new String[0];
		}
		else
		{
			List<String> list = new ArrayList<String>();
			for(int i = 0; i < input.length; i++)
			{
				if(!list.contains(input[i]))
				{
					list.add(input[i]);
				}
			}
			String[] v = new String[list.size()];
			return (String[]) list.toArray(v);
		}
	}
	private int CountWords(String word, String[] words)//����wordԪ����words�����г��ֵĴ���
	{
		int itemIdx = Arrays.binarySearch(words, word);//���word��words�����У�����������
		if(itemIdx > 0)
		{
			while(itemIdx > 0 && words[itemIdx].equals(word))
			{
				itemIdx--;
			}
		}
		
		int count = 0;
		while(itemIdx < words.length && itemIdx >= 0)
		{
			if(words[itemIdx].equals(word))
			{
				count++;
			}
			itemIdx++;
			if(itemIdx < words.length)
			{
				if(!words[itemIdx].equals(word))
				{
					break;
				}
			}
		}
		return count;
	}
	private int GetTermIndex(String term)//����������term�������ʵ��е������������ڴʵ��ڷ���-1
	{
		Object index = wordsIndex.get(term);
		if(index == null)
		{
			return -1;
		}
		else
		{
			return (Integer)index;
		}
	}
	
	//����Weight
	private void GenerateTermWeight()
	{
		for(int i = 0; i < numTerms; i++)
		{
			for(int j = 0; j < numDocs; j++)
			{
				termWeight[i][j] = ComputeTermWeight(i, j);
			}
		}
	}
	private float ComputeTermWeight(int term, int doc)
	{
		float tf = GetTermFrequency(term, doc);
		float idf = GetInverseDocumentFrequence(term);
		//System.out.println("tf:" + tf + ", idf:" + idf);
		return tf * idf;
	}
	private float GetTermFrequency(int term, int doc)
	{
		int freq = termFreq[term][doc];
		int maxfreq = maxTermFreq[doc] + 1;//��һΪ�˷�ֹ��0����
		return ((float)freq / (float)(maxfreq));
	}
	private float GetInverseDocumentFrequence(int term)//�ĵ�Ƶ�ʵĵ���
	{
		int df = docFreq[term];
		if(df == 0)
		{
			return 0;
		}
		else
		{
			return Log((float)(numDocs) / (float)df);
		}
	}
	private float Log(float num)
	{
		return (float) Math.log(num);
	}
}
