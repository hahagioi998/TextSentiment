import java.io.IOException;

public class LdaPredict {
	public static void main(String[] args) throws IOException{
		String dir="10crossdata_eval/1/";
		//���ı���ʾд���ļ�
		String trFileTest=dir+"Test/testSet.TR";
		
		//4scale����
		//2ʹ��range�ļ���ѵ�������й�һ������
		String scaleFileTest=trFileTest+".scale";
		String rangeFileTest=dir+"InitTrainSetSegmentTR.txt.range";
		String argvTest[]={"-t",scaleFileTest,"-r",rangeFileTest,trFileTest};
		SVMScale sTest = new SVMScale();
		sTest.run(argvTest);
		
		
		//5 �ı�����
		//���������ֱ��Ǿ���Scale����Ĵ�����������ļ�����ŷ�����ģ�͵��ļ����洢���������ļ�
		//String scaleFile="10crossdatairis_libsvm_scale/3/testSet.txt";
		//String modelFile="10crossdatairis_libsvm_scale/3/InitTrainSet.txt.model";
		
		String modelFileTest=dir+"InitTrainSetSegmentTR.txt.scale.model";
		String predictFileTest=scaleFileTest+".predict";
		String argv1Test[]={scaleFileTest,modelFileTest,predictFileTest};
		
		//String modelFile="Data/InitTrainSetSegmentTR.txt.model";
		//String predictFile=trFile+".predict";
		//String argv1[]={trFile,modelFile,predictFile};
		SVMPredict predict = new SVMPredict();		
		predict.run(argv1Test);
	}
}
