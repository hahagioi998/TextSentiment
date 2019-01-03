
import java.io.IOException;


public class TextCategorizationMain  {
	public static void main(String[] args) throws IOException{
	
	//1����������ı����ļ�
	String dir="10crossdata_eval/10/";
	String CorpusFile=dir+"Test/testSet.txt";
	InputOutput rw=new InputOutput();
	String[] inputCorpus=rw.readInput(CorpusFile);
	
	
	//InitTestSet its=new InitTestSet();
	//String[] newCorpus=its.InitTestSetMain(inputCorpus,200,1500,1700,1999);
	
	//2Ԥ����
	TextPreProcess p=new TextPreProcess();
	String[] docs=p.preProcessMain(inputCorpus);
	String trainFileSeg=CorpusFile.substring(0,CorpusFile.lastIndexOf("."))+"Segment.txt";		
	rw.writeOutput(docs, trainFileSeg);
	System.out.println("Ԥ�������");
	//3�ı���ʾ
	//���������ʵ��ļ�
	String termDicFile=dir+"termDic.txt";
	String[] terms=rw.readInput(termDicFile);
	
	//������ʾ
	String termEasyRepresentFile=CorpusFile.substring(0,CorpusFile.lastIndexOf("."))+"termEasyRepresent.txt";
	TermEasyRepresent ter = new TermEasyRepresent();
	String[] termEasyRepresent = ter.TermEasyRepresentMain(docs, terms);
	rw.writeOutput(termEasyRepresent, termEasyRepresentFile);
	
	//���������ʵ�����ı���ʾ
	TermRepresent tr=new TermRepresent();	
	String[] trDocs=tr.TermRepresentMain(docs,terms);
	
	//���ı���ʾд���ļ�
	String trFile=dir+"Test/testSet.TR";
	rw.writeOutput(trDocs, trFile);
	
	
	//4scale����
	//2ʹ��range�ļ���ѵ�������й�һ������
	String scaleFile=trFile+".scale";
	String rangeFile=dir+"InitTrainSetSegmentTR.txt.range";
	String argv[]={"-t",scaleFile,"-r",rangeFile,trFile};
	SVMScale s = new SVMScale();
	s.run(argv);
	
	
	//5 �ı�����
	//���������ֱ��Ǿ���Scale����Ĵ�����������ļ�����ŷ�����ģ�͵��ļ����洢���������ļ�
	//String scaleFile="10crossdatairis_libsvm_scale/3/testSet.txt";
	//String modelFile="10crossdatairis_libsvm_scale/3/InitTrainSet.txt.model";
	
	String modelFile=dir+"InitTrainSetSegmentTR.txt.scale.model";
	String predictFile=scaleFile+".predict";
	String argv1[]={scaleFile,modelFile,predictFile};
	
	//String modelFile="Data/InitTrainSetSegmentTR.txt.model";
	//String predictFile=trFile+".predict";
	//String argv1[]={trFile,modelFile,predictFile};
	SVMPredict predict = new SVMPredict();		
	predict.run(argv1);
	
	
}
	
}
