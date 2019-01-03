
import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

public class TestMain {
	public static void main(String[] args) throws IOException
	{
		
//		//�����������
		InputOutput rw = new InputOutput();
		String[] temp = rw.readInput("data/target.txt");
		TextPreProcess tp = new TextPreProcess();
		String[] temp2 = tp.preProcessMain(temp);//�ı�Ԥ�����
//		for(int i = 0; i < temp2.length; i++)
//		{
//			System.out.println(temp2[i]);
//		}
		DFTermSelect dfTermSelect = new DFTermSelect();
		String[] temp3 = dfTermSelect.TermDictionaryMain(temp2);
		TermRepresent termRepresent = new TermRepresent();
		String[] temp4 = termRepresent.TermRepresentMain(temp2, temp3);
		rw.writeOutput(temp4, "data/weight.txt");
		
		rw.writeOutput(temp2, "data/divided.txt");
		
		//���Էִ���IKAnalyzer
//		StringReader row = new StringReader("�л����񹲺͹���һ������Ĺ���");
		
//		IKSegmentation ikSeg = new IKSegmentation(row, true);
//		Lexeme t;//����
//		while((t = ikSeg.next()) != null)
//		{
//			System.out.println(t);
//		}
		
//		IKSegmentation ikSeg2 = new IKSegmentation(row, false);
//		Lexeme t2;
//		while((t2 = ikSeg2.next()) != null)
//		{
//			System.out.println(t2);
//		}
		
		
	}
}
