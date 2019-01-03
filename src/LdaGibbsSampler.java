
import java.text.DecimalFormat;
import java.text.NumberFormat;

// Gibbs sample �����㷨
public class LdaGibbsSampler {

    //�ĵ�
    int[][] documents;

    //�ʱ��С
    int V;

    //������Ŀ
    int K;

    //�ĵ�-�������
    double alpha = 2.0;

    //����-�������
    double beta = 0.5;

    //ÿ�����������z[i][j] = �ĵ�i�ĵ�j�������������
    int z[][];

    //������nw[i][j] = ����i��������j�Ĵ���
    int[][] nw;

    //������nd[i][j] = �ĵ�i��������j�Ĵ���ĸ���
    int[][] nd;

    //������nwsum[j] = ��������j����ĸ���
    int[] nwsum;

    //������ndsum[i] = �ĵ�i��ȫ�����������
    int[] ndsum;

    //theta���ۼ���
    double[][] thetasum;

    //phi���ۻ���
    double[][] phisum;

    //��������
    int numstats;

    //��ø���һ��ͳ����
    private static int THIN_INTERVAL = 20;

    //����ǰ�ĵ�������
    private static int BURN_IN = 100;

    //����������
    private static int ITERATIONS = 1000;

    //���ģ�͵ĸ�����ȡ�������n�������Ĳ�����ƽ������ʹ��ģ����������
    private static int SAMPLE_LAG = 10;

    private static int dispcol = 0;

    
    //�����ݳ�ʼ��������
    //documents���ĵ�����
    //V:vocabulary.size �ʱ��С
    public LdaGibbsSampler(int[][] documents, int V) 
    {

        this.documents = documents;
        this.V = V;
    }

    //�����ʼ��״̬��K������
    public void initialState(int K) 
    {
        int M = documents.length;

        //��ʼ��������
        nw = new int[V][K];
        nd = new int[M][K];
        nwsum = new int[K];
        ndsum = new int[M];
        
        z = new int[M][];//z_i:=1��K֮���ֵ����ʾ�������ĳ�ʼ״̬
        for (int m = 0; m < M; m++) 
        {
            int N = documents[m].length;
            z[m] = new int[N];
            for (int n = 0; n < N; n++) 
            {
                int topic = (int) (Math.random() * K);
                z[m][n] = topic;
                nw[documents[m][n]][topic]++;
                nd[m][topic]++;
                nwsum[topic]++;
            }
            ndsum[m] = N;
        }
    }

    public void gibbs(int K) 
    {
        gibbs(K, 2.0, 0.5);
    }

    //����
    public void gibbs(int K, double alpha, double beta) 
    {
        this.K = K;
        this.alpha = alpha;
        this.beta = beta;

       //�����ڴ�
        if (SAMPLE_LAG > 0) 
        {
            thetasum = new double[documents.length][K];
            phisum = new double[K][V];
            numstats = 0;
        }
        
        //��ʼ��������
        initialState(K);

        System.out.println("Sampling " + ITERATIONS
                + " iterations with burn-in of " + BURN_IN + " (B/S="
                + THIN_INTERVAL + ").");

        for (int i = 0; i < ITERATIONS; i++) 
        {
            for (int m = 0; m < z.length; m++) 
            {
                for (int n = 0; n < z[m].length; n++) 
                {
                    int topic = sampleFullConditional(m, n);
                    z[m][n] = topic;
                }
            }

            if ((i < BURN_IN) && (i % THIN_INTERVAL == 0)) 
            {
                System.out.print("B");
                dispcol++;
            }
            // display progress
            if ((i > BURN_IN) && (i % THIN_INTERVAL == 0)) 
            {
                System.out.print("S");
                dispcol++;
            }
            if ((i > BURN_IN) && (SAMPLE_LAG > 0) && (i % SAMPLE_LAG == 0)) 
            {
                updateParams();
                System.out.print("|");
                if (i % THIN_INTERVAL != 0)
                    dispcol++;
            }
            if (dispcol >= 100) 
            {
                System.out.println();
                dispcol = 0;
            }
        }
        System.out.println();
    }

    //����������ʽ�����ĵ�m�е�n��������������ȫ�����ֲ���������п��ܵ�����
    private int sampleFullConditional(int m, int n) 
    {

        //�Ƚ�����ʴӼ�������Ĩ��
        int topic = z[m][n];
        nw[documents[m][n]][topic]--;
        nd[m][topic]--;
        nwsum[topic]--;
        ndsum[m]--;

        //ͨ������ʽ������������ʽ�ֲ�
        double[] p = new double[K];
        for (int k = 0; k < K; k++) 
        {
            p[k] = (nw[documents[m][n]][k] + beta) / (nwsum[k] + V * beta) * (nd[m][k] + alpha) / (ndsum[m] + K * alpha);
        }
        // �ۼӶ���ʽ�ֲ��Ĳ���
        for (int k = 1; k < p.length; k++) 
        {
            p[k] += p[k - 1];
        }
        // p[]����
        double u = Math.random() * p[K - 1];
        for (topic = 0; topic < p.length; topic++) 
        {
            if (u < p[topic])
                break;
        }

        //�����¹��Ƶĸô�����������
        nw[documents[m][n]][topic]++;
        nd[m][topic]++;
        nwsum[topic]++;
        ndsum[m]++;

        return topic;
    }

    //���²���
    private void updateParams() 
    {
        for (int m = 0; m < documents.length; m++) 
        {
            for (int k = 0; k < K; k++) 
            {
                thetasum[m][k] += (nd[m][k] + alpha) / (ndsum[m] + K * alpha);
            }
        }
        for (int k = 0; k < K; k++) 
        {
            for (int w = 0; w < V; w++) 
            {
                phisum[k][w] += (nw[w][k] + beta) / (nwsum[k] + V * beta);
            }
        }
        numstats++;
    }

    //��ȡ�ĵ�-�������
    public double[][] getTheta() 
    {
        double[][] theta = new double[documents.length][K];

        if (SAMPLE_LAG > 0) 
        {
            for (int m = 0; m < documents.length; m++) 
            {
                for (int k = 0; k < K; k++) 
                {
                    theta[m][k] = thetasum[m][k] / numstats;
                }
            }

        } 
        else 
        {
            for (int m = 0; m < documents.length; m++) 
            {
                for (int k = 0; k < K; k++) 
                {
                    theta[m][k] = (nd[m][k] + alpha) / (ndsum[m] + K * alpha);
                }
            }
        }

        return theta;
    }

    //��ȡ����-�������
    public double[][] getPhi() 
    {
        double[][] phi = new double[K][V];
        if (SAMPLE_LAG > 0) 
        {
            for (int k = 0; k < K; k++) 
            {
                for (int w = 0; w < V; w++) 
                {
                    phi[k][w] = phisum[k][w] / numstats;
                }
            }
        } 
        else 
        {
            for (int k = 0; k < K; k++) 
            {
                for (int w = 0; w < V; w++) 
                {
                    phi[k][w] = (nw[w][k] + beta) / (nwsum[k] + V * beta);
                }
            }
        }
        return phi;
    }

    public static void hist(double[] data, int fmax) 
    {

        double[] hist = new double[data.length];
        double hmax = 0;
        for (int i = 0; i < data.length; i++) 
        {
            hmax = Math.max(data[i], hmax);
        }
        double shrink = fmax / hmax;
        for (int i = 0; i < data.length; i++) 
        {
            hist[i] = shrink * data[i];
        }

        NumberFormat nf = new DecimalFormat("00");
        String scale = "";
        for (int i = 1; i < fmax / 10 + 1; i++) 
        {
            scale += "    .    " + i % 10;
        }

        System.out.println("x" + nf.format(hmax / fmax) + "\t0" + scale);
        for (int i = 0; i < hist.length; i++) 
        {
            System.out.print(i + "\t|");
            for (int j = 0; j < Math.round(hist[i]); j++) 
            {
                if ((j + 1) % 10 == 0)
                    System.out.print("]");
                else
                    System.out.print("|");
            }
            System.out.println();
        }
    }

    //���ò�����
    public void configure(int iterations, int burnIn, int thinInterval, int sampleLag) 
    {
        ITERATIONS = iterations;
        BURN_IN = burnIn;
        THIN_INTERVAL = thinInterval;
        SAMPLE_LAG = sampleLag;
    }

    public static double[] inference(double alpha, double beta, double[][] phi, int[] doc) 
    {
        int K = phi.length;
        int V = phi[0].length;
        //��ʼ��������
        int[][] nw = new int[V][K];
        int[] nd = new int[K];
        int[] nwsum = new int[K];
        int ndsum = 0;

        int N = doc.length;
        int[] z = new int[N];   
        for (int n = 0; n < N; n++) 
        {
            int topic = (int) (Math.random() * K);
            z[n] = topic;
            nw[doc[n]][topic]++;
            nd[topic]++;
            nwsum[topic]++;
        }
        ndsum = N;
        for (int i = 0; i < ITERATIONS; i++) 
        {
            for (int n = 0; n < z.length; n++) 
            {
                int topic = z[n];
                nw[doc[n]][topic]--;
                nd[topic]--;
                nwsum[topic]--;
                ndsum--;
                double[] p = new double[K];
                for (int k = 0; k < K; k++) 
                {
                    p[k] = phi[k][doc[n]] * (nd[k] + alpha) / (ndsum + K * alpha);
                }
                for (int k = 1; k < p.length; k++) 
                {
                    p[k] += p[k - 1];
                }
                double u = Math.random() * p[K - 1];
                for (topic = 0; topic < p.length; topic++) 
                {
                    if (u < p[topic])
                        break;
                }
                if (topic == K) 
                {
                    throw new RuntimeException("the param K or topic is set too small");
                }
                nw[doc[n]][topic]++;
                nd[topic]++;
                nwsum[topic]++;
                ndsum++;
                z[n] = topic;
            }
        }

        double[] theta = new double[K];

        for (int k = 0; k < K; k++) 
        {
            theta[k] = (nd[k] + alpha) / (ndsum + K * alpha);
        }
        return theta;
    }

    public static double[] inference(double[][] phi, int[] doc) 
    {
        return inference(2.0, 0.5, phi, doc);
    }

    //�������
    public static void main(String[] args) 
    {
    	//�ĵ��Ĵ���id����
        int[][] documents = {
                {1, 4, 3, 2, 3, 1, 4, 3, 2, 3, 1, 4, 3, 2, 3, 6},
                {2, 2, 4, 2, 4, 2, 2, 2, 2, 4, 2, 2},
                {1, 6, 5, 6, 0, 1, 6, 5, 6, 0, 1, 6, 5, 6, 0, 0},
                {5, 6, 6, 2, 3, 3, 6, 5, 6, 2, 2, 6, 5, 6, 6, 6, 0},
                {2, 2, 4, 4, 4, 4, 1, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1, 0},
                {5, 4, 2, 3, 4, 5, 6, 6, 5, 4, 3, 2}};
        //�ʱ��С
        int V = 7;                                    
        int M = documents.length;
        //������Ŀ
        int K = 2;             
        double alpha = 2;
        double beta = .5;

        System.out.println("Latent Dirichlet Allocation using Gibbs Sampling.");

        LdaGibbsSampler lda = new LdaGibbsSampler(documents, V);
        lda.configure(10000, 2000, 100, 10);
        lda.gibbs(K, alpha, beta);

        double[][] theta = lda.getTheta();
        double[][] phi = lda.getPhi();

        System.out.println();
        System.out.println();
        System.out.println("Document--Topic Associations, Theta[d][k] (alpha="
                + alpha + ")");
        System.out.print("d\\k\t");
        for (int m = 0; m < theta[0].length; m++) 
        {
            System.out.print("   " + m % 10 + "    ");
        }
        System.out.println();
        for (int m = 0; m < theta.length; m++) 
        {
            System.out.print(m + "\t");
            for (int k = 0; k < theta[m].length; k++) 
            {
                System.out.print(shadeDouble(theta[m][k], 1) + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Topic--Term Associations, Phi[k][w] (beta=" + beta + ")");

        System.out.print("k\\w\t");
        for (int w = 0; w < phi[0].length; w++) 
        {
            System.out.print("   " + w % 10 + "    ");
        }
        System.out.println();
        for (int k = 0; k < phi.length; k++) 
        {
            System.out.print(k + "\t");
            for (int w = 0; w < phi[k].length; w++) 
            {
                System.out.print(shadeDouble(phi[k][w], 1) + " ");
            }
            System.out.println();
        }
        int[] aNewDocument = {2, 2, 4, 2, 4, 2, 2, 2, 2, 4, 2, 2};
        double[] newTheta = inference(alpha, beta, phi, aNewDocument);
        for (int k = 0; k < newTheta.length; k++) 
        {
            System.out.print(shadeDouble(newTheta[k], 1) + " ");
        }
        System.out.println();
    }

    static String[] shades = {"     ", ".    ", ":    ", ":.   ", "::   ",
            "::.  ", ":::  ", ":::. ", ":::: ", "::::.", ":::::"};

    static NumberFormat lnf = new DecimalFormat("00E0");

    public static String shadeDouble(double d, double max) 
    {
        int a = (int) Math.floor(d * 10 / max + 0.5);
        if (a > 10 || a < 0) 
        {
            String x = lnf.format(d);
            a = 5 - x.length();
            for (int i = 0; i < a; i++) 
            {
                x += " ";
            }
            return "<" + x + ">";
        }
        return "[" + shades[a] + "]";
    }
}