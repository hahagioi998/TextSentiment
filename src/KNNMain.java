import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class KNNMain {
	//�������ļ��ж�ȡ����
	// datas �洢���ݵļ��϶���
	// path �����ļ���·��
	public void read(List<List<Double>> datas, String path){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			String data = br.readLine();
			List<Double> l = null;
			while (data != null) {
				String t[] = data.split(" ");
				l = new ArrayList<Double>();
				for (int i = 0; i < t.length; i++) {
					l.add(Double.parseDouble(t[i]));
				}
				datas.add(l);
				data = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//����ִ�����
	public static void main(String[] args) throws IOException{
		KNNMain t = new KNNMain();
		InputOutput rw=new InputOutput();
		
		//************************ѵ������Ԥ����ѵ��������DataĿ¼��
	////�ӹ���2��ѵ������Ԥ������ѵ���������дʵ�Ԥ�����γ�ԭʼ�ʼ��ϣ�
		String dir="10crossdata_eval_KNN/2/";
		String trainFile=dir+"InitTrainSet.txt";
		String CorpusFile=dir+"test/testSet.txt";//�������ı���
		
		//InputOutput rw=new InputOutput();
		TextPreProcess p=new TextPreProcess(); 
		
		//1 ���ļ� trainFile����ѵ��������String[](its)��		
		String[] its=rw.readInput(trainFile);
		
		//2 ��ѵ����Ԥ����֮���γɵ�ѵ�����Ĵʼ��Ϸ���һ��String[]��docs����
		String[] docs=p.preProcessMain(its);
		
		//3 ������String[]�е�ѵ����д���ļ�
		String trainFileSeg=trainFile.substring(0,trainFile.lastIndexOf("."))+"Segment.txt";		
		rw.writeOutput(docs, trainFileSeg);
		System.out.println("Ԥ�������");
		///////////////////////////////////////////////////////////////////////
		////�ӹ���3�����������ʵ䣨ʹ�ú��ʵ�����ѡ���㷨ȷ�������ʣ�		
		
		//3.0ʹ�þֲ�DF
		DFTermSelect td=new DFTermSelect();
		
		//3.1ʹ��IG		
		//IGTermSelect td=new IGTermSelect();
		
		//3.2ʹ��CHI
		//CHITermSelect td=new CHITermSelect();
		
		//��ѵ�����Ĵʼ���ѡ��ʹ������ѡ�񷽷����������ʵ����һ��String[]��terms����
		String[] terms=td.TermDictionaryMain(docs);		
		
		//�������ʵ�������ļ�,����ļ���ѵ���׶�����ĵ�һ��������ļ�
		String termDicFile=trainFile.substring(0,trainFile.lastIndexOf("/")+1)+"termDic.txt";
		rw.writeOutput(terms, termDicFile);
		
/////////////////////////////////////////////////////////////////////////////////
		//�ӹ���4��ѵ������������ʾ����ѵ�����Ĵʼ���String[]��docs�����������ʵ�String[]��terms��
		//�����ı���ʾ����һ��String[]��trDocs���У�
		TermRepresent tr=new TermRepresent();
		
		//2���������ʵ���ı�������������ʾ����һ��String[](trDocs)��
		String[] trDocs=tr.TermRepresentMain(docs,terms);
				
		//3��ѵ������������ʾд�뵽�ļ�������ļ���������svmҪ������ݸ�ʽ
		String trFile=trainFile.substring(0,trainFile.lastIndexOf("."))+"SegmentTR.txt";
		rw.writeOutput(trDocs, trFile);	
		
		//***************�������ı�����Ԥ��������Data/testĿ¼��
		//1����������ı����ļ�
		//String CorpusFile="Data/Test/testSet.txt";
		//InputOutput rw=new InputOutput();
		String[] inputCorpus=rw.readInput(CorpusFile);
		
		
		//InitTestSet its=new InitTestSet();
		//String[] newCorpus=its.InitTestSetMain(inputCorpus,200,1500,1700,1999);
		
		//2Ԥ����
		//PreProcess p=new PreProcess();
		String[] docs1=p.preProcessMain(inputCorpus);
		String testFileSeg=CorpusFile.substring(0,CorpusFile.lastIndexOf("."))+"Segment.txt";		
		rw.writeOutput(docs1, testFileSeg);
		System.out.println("Ԥ�������");
		//3�ı���ʾ
		//���������ʵ��ļ�
		//String termDicFile="Data/termDic.txt";
		//String[] terms1=rw.readInput(termDicFile);
		
		//���������ʵ�����ı���ʾ
		//TermRepresent tr=new TermRepresent();	
		String[] trDocs1=tr.TermRepresentMain(docs1,terms);
		
		//���ı���ʾд���ļ�
		String trFile1=CorpusFile.substring(0,CorpusFile.lastIndexOf("."))+"TR.txt";
		//String trFile1="Data/Test/testSet.TR";
		rw.writeOutput(trDocs1, trFile1);	
		 
		 //**********************����׼�����
		try {
			List<List<Double>> datas = new ArrayList<List<Double>>();
			List<List<Double>> testDatas = new ArrayList<List<Double>>();
			t.read(datas, trFile);			
			t.read(testDatas, trFile1);
			//t.read(datas, trainFile);			
			//t.read(testDatas, CorpusFile);
			KNN knn = new KNN();
			int correct=0;
			int error=0;
			String[] result=new String[testDatas.size()];
			for (int i = 0; i < testDatas.size(); i++) {
				List<Double> test = testDatas.get(i);
				//System.out.print("����Ԫ��: ");
				for (int j = 1; j < test.size(); j++) {
					//System.out.print(test.get(j) + " ");
				}
				System.out.print("���Ϊ: ");				
				//System.out.println(Math.round(Float.parseFloat((knn.knn(datas, test, 3)))));
				String label=knn.knn(datas, test, 3);//k = 3,�ɵ���				
				System.out.println(label);
				result[i]=label;
				//ͳ����ȷ��
				if(test.get(0).equals(Double.parseDouble(label)))
				{
					correct+=1;
				}
			}
			double total=testDatas.size();
			double rate=correct/total;
			System.out.println("��ȷ�ʣ�"+rate+"("+correct+"/"+testDatas.size()+")");
			//�����д���ļ�
			
			//String resultFile=trFile1.substring(0,trFile1.lastIndexOf("."))+"result.txt";
			String resultFile=CorpusFile.substring(0,CorpusFile.lastIndexOf("."))+"result.txt";
			System.out.println(resultFile);
			rw.writeOutput(result, resultFile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
