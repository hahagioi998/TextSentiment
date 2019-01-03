
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

//�ı�Ԥ����ʹ��IKAnalyzer��������ķִʡ�ȥͣ�ô�
public class TextPreProcess {
	
	//�ú���������InputDocs�����طִʺ�Ľ��
	public String[] preProcessMain(String[] InputDocs) throws IOException
	{
		Set<String> stopWordSet = new HashSet<String>();
		String stopWord = null;
		//����ͣ�ô��ļ�
		try
		{
			BufferedReader StopWordFileBr = new BufferedReader(new InputStreamReader(new FileInputStream("data/stopWordSet.txt")));
			while((stopWord = StopWordFileBr.readLine()) != null)
			{
				stopWordSet.add(stopWord);
			}
			StopWordFileBr.close();
			//System.out.println(stopWordSet);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		String[] OutputDocs = new String[InputDocs.length];
		String row = "";
		String t = null;
		int count = 0;
		for(int i = 0; i < InputDocs.length; i++)
		{
			row += InputDocs[i].substring(0, 1);//�±�0���ַ������м�¼�����
			t = InputDocs[i].substring(2);//��ȡ���±�2������ַ���
			
			//�ڶ������������Ƿ�ʹ�����ʳ��з�
			IKSegmentation ikSeg = new IKSegmentation(new StringReader(t), true);
			Lexeme l = null;//����
			while((l = ikSeg.next()) != null)
			{
				if(l.getLexemeType() == Lexeme.TYPE_CJK_NORMAL)
				{
					//ȥ��ͣ�ô�
					if(stopWordSet.contains(l.getLexemeText()))
					{
						continue;
					}
					row += '|' + l.getLexemeText();
				}
			}
			if(row.length() == 1)//û��������ĵ��ʣ�ֻ��һ����ǩ
			{
				row = "";
				continue;
			}
			OutputDocs[count++] = row;
			row = "";
		}
		String[] OutputDocs2 = new String[count];
		System.arraycopy(OutputDocs, 0, OutputDocs2, 0, count);
//		System.out.println(OutputDocs2.length);
//		System.out.println(InputDocs.length);
		
		return OutputDocs2;
	}
}
