import java.util.ArrayList;
import java.util.List;

public class TermEasyRepresent {
	//������������ѡ������������ȡ
	
	List<String> terms = new ArrayList<String>();//����ʹ�õ������ʵ�
	
	public String[] TermEasyRepresentMain(String[] segmentDocs, String[] termDic)
	{
		String[] result = new String[segmentDocs.length];
		//�����������ʼ��������ʵ�wordsIndex��
		for(int i = 0; i < termDic.length; i++)
		{
			terms.add(termDic[i]);
		}
		for(int i = 0; i < segmentDocs.length; i++)
		{
			String[] words = segmentDocs[i].split("([|])");
			String s = words[0];
			for(int j = 1; j < words.length; j++)
			{
				if(terms.contains(words[j]))
				{
					s += "|" + words[j];
				}
			}
			result[i] = s;
		}
		
		return result;
	}
}
