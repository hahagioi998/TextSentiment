

import java.io.IOException;





public class TrainClassifierMain {
	public static void main(String[] args) throws IOException{
		///////////////////////////////////////////////////////////////////////
		/*
		////�ӹ���1������ѵ�������ӳ�ʼ��ѵ�����а�����ѡȡһ�������ı��������յ�ѵ������		
		InitTrainSet t=new InitTrainSet(); 
		
		
		//1����ѵ�����ļ���·��
		String trainFile="Data/InitTrainSet.txt";
		int Samples=400;//�û��趨��ѵ������������		
		
		//2 ��ԭʼ�ı�����ȡ��һ�������������γɵ�ѵ����������һ��String[](ts)��
		String[] ts=t.InitTrainSetMain(Samples);
		
		//3 ������String[](ts)�е�ѵ����д���ļ� trainFile
		InputOutput rw=new InputOutput();
		rw.writeOutput(ts, trainFile);
		*/
		///////////////////////////////////////////////////////////////////////
		////�ӹ���2��ѵ������Ԥ���������յ�ѵ���������дʵ�Ԥ�����γ�ԭʼ�ʼ��ϣ�
		String trainFile="10crossdata_eval/10/InitTrainSet.txt";
		InputOutput rw=new InputOutput();
		TextPreProcess p=new TextPreProcess(); 
		
		//1 ���ļ� trainFile����ѵ��������String[](its)��		
		String[] its=rw.readInput(trainFile);
		
		//2 ��ѵ����Ԥ����֮���γɵ�ѵ�����Ĵʼ��Ϸ���һ��String[]��docs����
		String[] docs=p.preProcessMain(its);
		
		//3 ������String[]�е�ѵ����д���ļ�������ļ����Բ��ò���,�������Կ���ȥ�����IO
		String trainFileSeg=trainFile.substring(0,trainFile.lastIndexOf("."))+"Segment.txt";		
		rw.writeOutput(docs, trainFileSeg);
		
		///////////////////////////////////////////////////////////////////////
		////�ӹ���3�����������ʵ䣨���Ƚ�ѵ�����Ĵʼ���ȥ�أ�֮��ʹ�ú��ʵ�����ѡ���㷨��һ��ȷ�������ʣ�
		//TermDictionary td=new TermDictionary(); 
		
		//3.1ʹ��ȫ��DF
		//DFTermSelect td=new DFTermSelect();
		
		//3.2ʹ�þֲ�DF
		DFTermSelect td=new DFTermSelect();
		
		//3.3ʹ��IG
		//IGTermSelect td=new IGTermSelect();
				
		//3.4ʹ��CHI
		//CHITermSelect td=new CHITermSelect();
		
		//1��ѵ�����Ĵʼ���ѡ��ʹ������ѡ�񷽷����������ʵ����һ��String[]��terms����
		String[] terms=td.TermDictionaryMain(docs);		
		
		//2�������ʵ�������ļ�,����ļ���ѵ���׶�����ĵ�һ��������ļ�
		String termDicFile=trainFile.substring(0,trainFile.lastIndexOf("/")+1)+"termDic.txt";
		rw.writeOutput(terms, termDicFile);
		
		//������ʾ
		String termEasyRepresentFile=trainFile.substring(0,trainFile.lastIndexOf("/")+1)+"termEasyRepresent.txt";
		TermEasyRepresent ter = new TermEasyRepresent();
		String[] termEasyRepresent = ter.TermEasyRepresentMain(docs, terms);
		rw.writeOutput(termEasyRepresent, termEasyRepresentFile);
		
		/////////////////////////////////////////////////////////////////////////////////
		//�ӹ���4��ѵ������������ʾ����ѵ�����Ĵʼ���String[]��docs�����������ʵ�String[]��terms��
		//�����ı���ʾ����һ��String[]��trDocs���У�
		TermRepresent tr=new TermRepresent();
		
		//2���������ʵ���ı�������������ʾ����һ��String[](trDocs)��
		String[] trDocs=tr.TermRepresentMain(docs,terms);
				
		//3��ѵ������������ʾд�뵽�ļ�������ļ���������svmҪ������ݸ�ʽ
		String trFile=trainFile.substring(0,trainFile.lastIndexOf("."))+"SegmentTR.txt";
		rw.writeOutput(trDocs, trFile);
		
		/////////////////////////////////////////////////////////////////////////////////
		//�ӹ���5����ѵ������������ʾ���й�һ������[0��1]��
		//�Ȼ���ѵ��������range�ļ���֮��ʹ��range�ļ���ѵ�������й�һ������
		
		//1����ѵ��������range�ļ�
		String rangeFile=trFile+".range";
		String argv[]={"-l","0","-s",rangeFile,trFile};
		SVMScale s = new SVMScale();
		s.run(argv);
		//2ʹ��range�ļ���ѵ�������й�һ������
		String scaleFile=trFile+".scale";
		String argv1[]={"-t",scaleFile,"-r",rangeFile,trFile};
		s.run(argv1);
		////////////////////////////////////////////////////////////////////////////////
		//�ӹ���6����������ģ��
		String modelFile=scaleFile+".model";
		String argv2[]={"-c","0.5","-t","0",scaleFile,modelFile};
		SVMTrain train = new SVMTrain();		
		train.run(argv2);
	}
}
