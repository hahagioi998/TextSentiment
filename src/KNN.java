
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

//KNN�㷨������
public class KNN {
	//�������ȼ����еıȽϺ���������Խ�����ȼ�Խ��
	private Comparator<KNNNode> comparator = new Comparator<KNNNode>() {
		public int compare(KNNNode o1, KNNNode o2) {
			if (o1.getDistance() >= o2.getDistance()) {
				return 1;
			} else {
				return 0;
			}
		}
	};
	//��ȡK����ͬ�������
	// k ������ĸ���
	// max ��������ķ�Χ
	// ���� ���ɵ����������
	public List<Integer> getRandKNum(int k, int max) {
		List<Integer> rand = new ArrayList<Integer>(k);
		for (int i = 0; i < k; i++) {
			int temp = (int) (Math.random() * max);
			if (!rand.contains(temp)) {
				rand.add(temp);
			} else {
				i--;
			}
		}
		return rand;
	}
	//�������Ԫ����ѵ��Ԫ��֮ǰ�ľ���
	// d1 ����Ԫ��
	// d2 ѵ��Ԫ��
	//���� ����ֵ
	public double calDistance(List<Double> d1, List<Double> d2) {
		/*
		//����������ϡ�������ľ���
		double distance = 0.00;
		int maxlength;
		int minlength;
		if (d2.size()>d1.size()) 
		{
			maxlength=d2.size();
			minlength=d1.size();
		}
		else
		{
			maxlength=d1.size();
			minlength=d2.size();
		}
		
		for (int i = 1; i <minlength; i++) {
			distance += (d1.get(i) - d2.get(i)) * (d1.get(i) - d2.get(i));
		}
		for (int i = minlength; i <maxlength; i++) {
			if(d1.size()==minlength)
			distance += (0 - d2.get(i)) * (0 - d2.get(i));
			else
				distance += (d1.get(i) - 0) * (d1.get(i) - 0);	
		}
		return distance;
		*/
		//�������������ľ���
		double distance = 0.00;
		for (int i = 1; i <d1.size(); i++) {
			distance += (d1.get(i) - d2.get(i)) * (d1.get(i) - d2.get(i));
		}
		return distance;
		
	}
	//ִ��KNN�㷨����ȡ����Ԫ������
	// datas ѵ�����ݼ�
	// testData ����Ԫ��
	// k �趨��Kֵ
	//���� ����Ԫ������
	public String knn(List<List<Double>> datas, List<Double> testData, int k) {
		PriorityQueue<KNNNode> pq = new PriorityQueue<KNNNode>(k, comparator);
		List<Integer> randNum = getRandKNum(k, datas.size());
		//System.out.println("training "+datas.size());
		for (int i = 0; i < k; i++) {
			int index = randNum.get(i);
			List<Double> currData = datas.get(index);
			String c = currData.get(0).toString();
			KNNNode node = new KNNNode(index, calDistance(testData, currData), c);
			pq.add(node);
		}
		for (int i = 0; i < datas.size(); i++) {
			List<Double> t = datas.get(i);
			double distance = calDistance(testData, t);
			KNNNode top = pq.peek();
			if (top.getDistance() > distance) {
				pq.remove();
				pq.add(new KNNNode(i, distance, t.get(0).toString()));
			}
		}
		
		return getMostClass(pq);
	}
	//��ȡ���õ���k�������Ԫ��Ķ�����
        // pq �洢k���������Ԫ������ȼ�����
	//���� �����������/
	private String getMostClass(PriorityQueue<KNNNode> pq) {
		Map<String, Integer> classCount = new HashMap<String, Integer>();
		for (int i = 0; i < pq.size(); i++) {
			KNNNode node = pq.remove();
			String c = node.getC();
			if (classCount.containsKey(c)) {
				classCount.put(c, classCount.get(c) + 1);
			} else {
				classCount.put(c, 1);
			}
		}
		int maxIndex = -1;
		int maxCount = 0;
		Object[] classes = classCount.keySet().toArray();
		for (int i = 0; i < classes.length; i++) {
			if (classCount.get(classes[i]) > maxCount) {
				maxIndex = i;
				maxCount = classCount.get(classes[i]);
			}
		}
		return classes[maxIndex].toString();
	}
}