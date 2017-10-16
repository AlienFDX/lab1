package lab1gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class lab1 {
	public static int[][] adj1;
	public static String str1;
	public static String[] vexs1;
	public static String[] words1;
	public static String[][] edges1;
	public static String[][] edg1;

	// ����������ת��Ϊ���飨ɾ���ظ�Ԫ�أ�
	public static String[] tranVexs(String[] words) {
		String[] tmp = new String[words.length];
		int j = 0;
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (iscontain(tmp, word) == false) {
				tmp[j] = word;
				j++;
			}
		}
		String[] vexs = new String[j];
		for (int i = 0; i < j; i++) {
			vexs[i] = tmp[i];
		}
		return vexs;
	}

	public static boolean iscontain(String[] words, String word) {
		return Arrays.asList(words).contains(word);
	}// �ж������Ƿ����ĳ��
	
	//���ַ��������ɱ߾���
	public static String[][] transMa(String str) {
		String[] words = str.split("\\s+"); // ���ݿո�ת��Ϊ����words

		int wordscount = words.length;// 13
		String[][] edges = new String[wordscount - 1][2];
		for (int i = 0; i < wordscount - 1; i++) {
			edges[i][0] = words[i];
			edges[i][1] = words[i + 1];
		}
		return edges;
	}

	// ɾ���ظ��ߣ�����ߵ�Ȩֵ��
	public static String[][] edgweight(String[][] edges) {
		String[] tmp = new String[edges.length];
		for (int i = 0; i < edges.length; i++) {
			tmp[i] = edges[i][0] + " " + edges[i][1];
		}
		String[] tmp1 = tranVexs(tmp);
		int edgweight[] = weight(tmp, tmp1);

		String[][] tmp2 = new String[tmp1.length][edges[0].length + 1];
		for (int i = 0; i < tmp1.length; i++) {
			String[] mmp = tmp1[i].split(" ");
			tmp2[i][0] = mmp[0];
			tmp2[i][1] = mmp[1];
			tmp2[i][2] = Integer.toString(edgweight[i]);
		}
		return tmp2;
	}
	//�����б���Ԫ�ص�Ȩֵ��
	public static int[] weight(String[] strlist, String[] vlist) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		int[] vweight = new int[vlist.length];
		for (String word : strlist) {
			if (map.containsKey(word)) {// ���map���Ѿ������õ��ʣ��������+1
				int x = map.get(word);
				x++;
				map.put(word, x);
			} else { // ���map��û�ð����õ��ʣ�����õ��ʵ�һ�γ��֣��������map������������Ϊ1
				map.put(word, 1);
			}
		}
		for (int i = 0; i < vlist.length; i++) {
			vweight[i] = map.get(vlist[i]);
		}
		return vweight;
	}

	// ����ʼĩ��ѯ�ŽӴ�
	public static String queryBridgeWords(String before, String behind) {

		boolean isbefore = iscontain(vexs1, before);
		boolean isbehind = iscontain(vexs1, behind);
		String str = "";
		if (isbefore == false && isbehind == false) {
			str = "No ��" + before + "�� and ��" + behind + " ��in the graph!";
		} else if (isbefore == false) {
			str = "No ��" + before + "�� in the graph!";
		} else if (isbehind == false) {
			str = "No ��" + behind + "�� in the graph!";
		} else {
			String[] br = brilist(before, behind);
			if (br.length == 0) {
				str = str + "No bridge words from ��" + before + "�� to ��" + behind + "��!";
			} else if (br.length == 1) {
				str = str + "The bridge words from�� " + before + "�� to ��" + behind + "�� are:" + br[0] + ".";
			} else if (br.length == 2) {
				str = str + "The bridge words from�� " + before + " ��to ��" + behind + "�� are:" + br[0] + "," + br[1]
						+ ".";
			} else {
				str = str + "The bridge words from�� " + before + " ��to ��" + behind + "�� are:";
				for (int i = 0; i < br.length - 2; i++) {
					str = str + br[i] + ",";
				}
				str = str + br[br.length - 2] + " and ";
				str = str + br[br.length - 1] + ".";
			}
		}
		return str;
	}

	// ����ʼĩ�������ŽӴ����飨����ŽӴʣ�
	public static String[] brilist(String before, String behind) {
		int a = Arrays.binarySearch(vexs1, before);
		int b = Arrays.binarySearch(vexs1, behind);
		int j = 0;
		String[] tmp = new String[vexs1.length];
		String[] sp = new String[0];
		if (a < 0 || b < 0) {
			return sp;
		} else {
			for (int i = 0; i < vexs1.length; i++) {
				if (adj1[a][i] != Integer.MAX_VALUE) {
					if (adj1[i][b] != Integer.MAX_VALUE) {
						tmp[j] = vexs1[i];
						j++;
					}
				}
			}
			String[] bri = new String[j];
			for (int i = 0; i < j; i++) {
				bri[i] = tmp[i];
			}
			return bri;
		}
	}

	// �����ŽӴʾ���;��������¾��ӡ�
	public static String generateNewText(String sentence) {
		sentence = sentence.replaceAll("[^a-zA-Z]+", " ");
		sentence = sentence.toLowerCase();
		String[] words = sentence.split("\\s+");
		String[] temnew = new String[20];
		String newsen = "";
		temnew[0] = words[0];
		int j = 1;
		for (int i = 0; i < words.length - 1; i++) {
			String st1 = words[i];
			String st2 = words[i + 1];
			String[] mmp = brilist(st1, st2);
			if (mmp.length != 0) {
				for (int k = 0; k < mmp.length; k++) {
					temnew[j] = mmp[k];
					j++;
				}
			}
			temnew[j] = words[i + 1];
			j++;
		}
		for (int i = 0; i < j; i++) {
			newsen += temnew[i];
			newsen += " ";
		}
		return newsen;
	}

	// չʾͼƬ
	public static void showgraph(String[][] edg) {
		GraphViz gv = new GraphViz();
		String str = "";
		for (int i = 0; i < edg.length; i++) {
			str = str + edg[i][0];
			str = str + "->";
			str = str + edg[i][1];
			str = str + "[label=\"";
			str = str + edg[i][2];
			str = str + "\"]";
			str = str + "\n";
		}
		gv.addln(gv.start_graph());
		gv.add(str);
		gv.addln(gv.end_graph());
		String type = "jpg";
		File out = new File("graph" + "." + type);
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	// �ڽӾ���
	public static int[][] Adjacency(String[][] edg, String[] vexs) {
		int adj[][] = new int[vexs.length][vexs.length];
		for (int i = 0; i < vexs.length; i++) {
			for (int j = 0; j < vexs.length; j++) {
				adj[i][j] = Integer.MAX_VALUE;
			}
		}
		int a, b;
		for (int i = 0; i < edg.length; i++) {
			a = Arrays.binarySearch(vexs, edg[i][0]);
			b = Arrays.binarySearch(vexs, edg[i][1]);
			adj[a][b] = Integer.parseInt(edg[i][2]);
		}
		return adj;
	}

	// �����ڽӾ����ѯ����֮�����·��
	public static String calcShortestPath(int point, int point2) {
		int n = adj1.length;
		int i, j, k;
		int[] path = new int[n];
		int[] temp = new int[n];
		int[] weight = new int[n];
		for (i = 0; i < n; i++) {
			weight[i] = adj1[point][i];
		}
		for (i = 0; i < n; i++) {
			if (weight[i] < Integer.MAX_VALUE) {
				path[i] = point;
			}
		}
		for (i = 0; i < n; i++) {
			temp[i] = 0;
		}
		temp[point] = 1;
		weight[point] = 0;
		for (i = 0; i < n; i++) {
			int min = Integer.MAX_VALUE;
			k = point;
			for (j = 0; j < n; j++) {
				if (temp[j] == 0 && weight[j] < min) {
					min = weight[j];
					k = j;
				}
			}
			temp[k] = 1; // ������kѡ��
			for (j = 0; j < n; j++) { // �Զ���kΪ�м�㣬���¼���Ȩֵ
				if (temp[j] == 0 && adj1[k][j] != Integer.MAX_VALUE && weight[k] + adj1[k][j] < weight[j]) {
					weight[j] = weight[k] + adj1[k][j];
					path[j] = k;
				}
			}
		}
		i = point2;
		String[] str = new String[n];
		j = 0;
		String tmp = "";
		if (i == point) {
			tmp = tmp + "ɵ�ư�����������һ���ĵ�";
		} else if (temp[i] == 0) {
			tmp = tmp + vexs1[point] + " and " + vexs1[i] + " no path";
		} else {
			k = i;
			while (k != point) {
				str[j] = vexs1[k];
				j++;
				k = path[k];
			}
			str[j] = vexs1[k];
			tmp = tmp + vexs1[point] + "��" + vexs1[i] + "�����·��Ϊ��";
			while (j != 0) {
				tmp = tmp + str[j] + "->";
				j--;
			}
			tmp = tmp + str[0] + "!";
		}
		return tmp;
	}

	// �����ڽӾ����ѯһ�����ʵ��������е���֮������·����
	public static String calcShortestPath(int point) {
		int n = adj1.length;
		int i, j, k;
		int[] path = new int[n];
		int[] temp = new int[n];
		int[] weight = new int[n];
		for (i = 0; i < n; i++) {
			weight[i] = adj1[point][i];
		}
		for (i = 0; i < n; i++) {
			if (weight[i] < Integer.MAX_VALUE) {
				path[i] = point;
			}
		}
		for (i = 0; i < n; i++) {
			temp[i] = 0;
		}
		temp[point] = 1;
		weight[point] = 0;
		for (i = 0; i < n; i++) {
			int min = Integer.MAX_VALUE;
			k = point;
			for (j = 0; j < n; j++) {
				if (temp[j] == 0 && weight[j] < min) {
					min = weight[j];
					k = j;
				}
			}
			temp[k] = 1; // ������kѡ��
			for (j = 0; j < n; j++) { // �Զ���kΪ�м�㣬���¼���Ȩֵ
				if (temp[j] == 0 && adj1[k][j] != Integer.MAX_VALUE && weight[k] + adj1[k][j] < weight[j]) {
					weight[j] = weight[k] + adj1[k][j];
					path[j] = k;
				}
			}
		}
		String tmp = "";
		for (i = 0; i < n; i++) {
			String[] str = new String[n];
			j = 0;
			if (i == point) {
				continue;
			} else if (temp[i] == 0) {
				tmp = tmp + vexs1[point] + "��" + vexs1[i] + " û��·����\r\n";
			} else {
				k = i;
				while (k != point) {
					str[j] = vexs1[k];
					j++;
					k = path[k];
				}
				str[j] = vexs1[k];
				tmp = tmp + vexs1[point] + "��" + vexs1[i] + "�����·��Ϊ��";
				while (j != 0) {
					tmp = tmp + str[j] + "->";
					j--;
				}
				tmp = tmp + str[0] + "!\r\n";
			}
		}
		return tmp;
	}

	// ������ߡ�
	public static String randomWalk(int point) {
		int m = vexs1.length;
		int[][] p = new int[m][m];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++)
				p[i][j] = -1;
		String result = new String();
		int a = point;
		result += vexs1[a];

		while (ran(a, m) != -1) {
			int b = ran(a, m);
			if (p[a][b] == 1)
				break;
			else {
				result += (" " + vexs1[b]);
				p[a][b] = 1;
				a = b;
			}
		}
		return result;

	}

	protected static int ran(int x, int m) {
		int[] l = new int[m];
		int j = 0;
		for (int i = 0; i < m; i++) {
			if (adj1[x][i] < Integer.MAX_VALUE) {
				l[j] = i;
				j++;
			}
		}
		if (j > 1)
			return l[(int) (Math.random() * j)];
		else if (j == 1)
			return l[0];
		else
			return -1;
	}
	//���ַ���д���ļ�
	public static void WriteFile(String filename, String filec) {
		try {
			File file = new File(filename);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println(filec);// ���ļ���д���ַ���
			ps.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String OpenGui() {
		JFileChooser fd = new JFileChooser();
		fd.showOpenDialog(fd);
		File f = fd.getSelectedFile();
		String str = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));// ����һ��BufferedReader������ȡ�ļ�
			String s = "";
			while ((s = br.readLine()) != null) {// ʹ��readLine������һ�ζ�һ��
				str += s;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String str0 = str;
		str0 = str0.replaceAll("[^a-zA-Z]+", " ");
		str0 = str0.toLowerCase();

		return str0;
	}

	public static void ShowGui() {
		showgraph(edg1);
		JFrame pic = new JFrame("����ͼ");
		File picture = new File("graph.jpg");
		BufferedImage sourceImg;
		try {
			sourceImg = ImageIO.read(new FileInputStream(picture));
			int width = sourceImg.getWidth();
			int height = sourceImg.getHeight();
			if (height < 960) {
				pic.setSize(width + 20, height + 40);
			} else {
				pic.setSize(width + 20, 960);
			}
			JPanel panel = new JPanel();
			JLabel label = new JLabel();
			// pic.setExtendedState(JFrame.MAXIMIZED_BOTH);//JFrame���
			pic.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// ��JFrame�Ĺ�
			ImageIcon img = new ImageIcon("./graph.jpg");// ����ͼƬ����
			label.setIcon(img);
			panel.add(label);
			JScrollPane scr = new JScrollPane(panel);
			scr.setVisible(true);
			pic.getContentPane().add(scr);
			pic.setVisible(true);
			pic.setAlwaysOnTop(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print("ͼƬ�޷���ȡ");
		}
	}

	public static void BrigGui() {
		JFrame gui = new JFrame();
		gui.setTitle("���ŽӴ�");
		gui.setBounds(0, 0, 1120, 700);
		gui.setLocation(320, 220);
		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gui.setResizable(false);
		ImageIcon BG = new ImageIcon("Backgroud.jpg");
		JLabel bgLabel = new JLabel(BG);
		bgLabel.setBounds(0, 0, 1120, 700);
		JPanel bgPanel = (JPanel) gui.getContentPane();
		bgPanel.setOpaque(false);
		gui.getLayeredPane().setLayout(null);
		gui.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));
		JTextArea word1text = new JTextArea();
		word1text.setBounds(50, 250, 150, 40);
		word1text.setFont(new java.awt.Font("����", 1, 20));
		JTextArea word2text = new JTextArea();
		word2text.setBounds(50, 350, 150, 40);
		word2text.setFont(new java.awt.Font("����", 1, 20));
		JLabel label1 = new JLabel("�����뵥��һ��");
		label1.setFont(new java.awt.Font("����", 1, 20));
		label1.setBounds(50, 200, 150, 40);
		JLabel label2 = new JLabel("�����뵥�ʶ���");
		label2.setFont(new java.awt.Font("����", 1, 20));
		label2.setBounds(50, 300, 150, 40);
		JLabel label4 = new JLabel();
		label4.setFont(new java.awt.Font("����", 1, 20));
		label4.setBounds(50, 500, 1000, 50);
		JButton function = new JButton("��ѯ");
		function.setBounds(50, 400, 150, 40);
		function.setFont(new java.awt.Font("����", 1, 20));

		function.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String word1 = word1text.getText();
				String word2 = word2text.getText();
				word1 = word1.toLowerCase();
				word2 = word2.toLowerCase();
				word1 = word1.replaceAll("[^a-zA-Z]+", " ");
				word2 = word2.replaceAll("[^a-zA-Z]+", " ");
				word1 = word1.replaceAll(" ", "");
				word2 = word2.replaceAll(" ", "");
				String result = queryBridgeWords(word1, word2);
				label4.setText("���:" + result);
				label4.setOpaque(true);
				label4.setBackground(Color.white);
			}
		});
		gui.add(label1);
		gui.add(label2);
		gui.add(label4);
		gui.add(function);
		gui.add(word1text);
		gui.add(word2text);
		gui.add(new JLabel());
		gui.setVisible(true);
	}

	public static void CreaGui() {

		JFrame gui = new JFrame();
		gui.setTitle("�����¾�");
		gui.setBounds(0, 0, 1120, 700);
		gui.setLocation(320, 220);
		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gui.setResizable(false);
		ImageIcon BG = new ImageIcon("Backgroud.jpg");
		JLabel bgLabel = new JLabel(BG);
		bgLabel.setBounds(0, 0, 1120, 700);
		JPanel bgPanel = (JPanel) gui.getContentPane();
		bgPanel.setOpaque(false);
		gui.getLayeredPane().setLayout(null);
		gui.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));
		JTextArea sentext = new JTextArea();
		sentext.setBounds(50, 250, 600, 40);
		sentext.setFont(new java.awt.Font("����", 1, 20));
		JLabel label1 = new JLabel("������һ�����ӣ�");
		label1.setFont(new java.awt.Font("����", 1, 20));
		label1.setBounds(50, 200, 300, 40);
		JTextArea text = new JTextArea();
		text.setBounds(50, 350, 1000, 240);
		text.setFont(new java.awt.Font("����", 1, 20));
		text.setEditable(false);
		
		JButton function = new JButton("����");
		function.setBounds(50, 300, 150, 40);
		function.setFont(new java.awt.Font("����", 1, 20));

		function.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sen = sentext.getText();
				sen = sen.toLowerCase();
				String result = generateNewText(sen);
				text.setText("���:" + result);
				text.setLineWrap(true);
			}
		});
		gui.add(sentext);
		gui.add(function);
		gui.add(label1);
		gui.add(text);
		gui.add(new JLabel());
		gui.setVisible(true);
	}

	public static void PathGui() {

		JFrame gui = new JFrame();
		gui.setTitle("���·��");
		gui.setBounds(0, 0, 1120, 700);
		gui.setLocation(320, 220);
		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gui.setResizable(false);
		ImageIcon BG = new ImageIcon("Backgroud.jpg");
		JLabel bgLabel = new JLabel(BG);
		bgLabel.setBounds(0, 0, 1120, 700);
		JPanel bgPanel = (JPanel) gui.getContentPane();
		bgPanel.setOpaque(false);
		gui.getLayeredPane().setLayout(null);
		gui.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));

		JTextArea wordtxt = new JTextArea();
		wordtxt.setBounds(50, 250, 600, 40);
		wordtxt.setFont(new java.awt.Font("����", 1, 20));
		JLabel label1 = new JLabel("������һ�������������ʣ�");
		label1.setFont(new java.awt.Font("����", 1, 20));
		label1.setBounds(50, 200, 300, 40);
		JButton function = new JButton("��ѯ");
		function.setBounds(50, 300, 150, 40);
		function.setFont(new java.awt.Font("����", 1, 20));
		JTextArea text = new JTextArea();
		text.setBounds(50, 350, 1000, 240);
		text.setFont(new java.awt.Font("����", 1, 20));
		text.setEditable(false);
		function.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sen = wordtxt.getText();
				sen = sen.toLowerCase();
				String result = "";
				String wor = wordtxt.getText();
				String[] aha = wor.split("\\s+");
				if (aha.length == 1) {
					int point = Arrays.binarySearch(vexs1, aha[0]);
					if (point >= 0) {
						result = calcShortestPath(point);
					} else {
						result = "û���������";
					}
				} else if (aha.length == 2) {
					int point1 = Arrays.binarySearch(vexs1, aha[0]);
					int point2 = Arrays.binarySearch(vexs1, aha[1]);
					if (point1 < 0 && point2 < 0) {
						result = "û��" + aha[0] + "��" + aha[1] + "!";
					} else if (point1 < 0) {
						result = "û��" + aha[0] + "!";
					} else if (point2 < 0) {
						result = "û��" + aha[1] + "!";
					} else {
						result = calcShortestPath(point1, point2);
					}

				} else {
					result = "��������";
				}
				text.setText(result);
				text.setLineWrap(true);

			}
		});

		gui.add(wordtxt);
		gui.add(function);
		gui.add(label1);
		gui.add(text);

		gui.add(new JLabel());
		gui.setVisible(true);
	}

	public static void RandGui() {
		JFrame gui = new JFrame();
		gui.setTitle("�������");
		gui.setBounds(0, 0, 1120, 700);
		gui.setLocation(320, 220);
		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gui.setResizable(false);
		ImageIcon BG = new ImageIcon("Backgroud.jpg");
		JLabel bgLabel = new JLabel(BG);
		bgLabel.setBounds(0, 0, 1120, 700);
		JPanel bgPanel = (JPanel) gui.getContentPane();
		bgPanel.setOpaque(false);
		gui.getLayeredPane().setLayout(null);
		gui.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));
		JTextArea text = new JTextArea();
		text.setBounds(50, 350, 1000, 240);
		text.setFont(new java.awt.Font("����", 1, 20));
		text.setEditable(false);
		text.setLineWrap(true);
		JTextArea wordtxt = new JTextArea();
		wordtxt.setBounds(50, 250, 600, 40);
		wordtxt.setFont(new java.awt.Font("����", 1, 20));
		JLabel label1 = new JLabel("������һ�����ʣ�");
		label1.setFont(new java.awt.Font("����", 1, 20));
		label1.setBounds(50, 200, 300, 40);
		JButton function = new JButton("��ʼ����");
		function.setBounds(50, 300, 150, 40);
		function.setFont(new java.awt.Font("����", 1, 20));
		function.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String start = wordtxt.getText();
				String result = "";
				int point = Arrays.binarySearch(vexs1, start);
				if (point < 0) {
					result = "û��������ʡ�";
					text.setText(result);
				} else {
					int m = vexs1.length;
					int[][] p = new int[m][m];
					for (int i = 0; i < m; i++)
						for (int j = 0; j < m; j++)
							p[i][j] = -1;
					result = new String();
					int a = point;
					result += vexs1[a];
					while (ran(a, m) != -1) {
						int b = ran(a, m);
						if (p[a][b] == 1)
							break;
						else {
							result += (" " + vexs1[b]);
							p[a][b] = 1;
							a = b;
						}
					}

				}
				text.setText(result);
				WriteFile("mmp.txt", result);
			}
		});

		gui.add(text);
		gui.add(function);
		gui.add(wordtxt);
		gui.add(label1);
		gui.add(new JLabel());
		gui.setVisible(true);
	}

	public static void main(String[] args) {
		JFrame gui = new JFrame();
		gui.setTitle("����ѡ��");
		gui.setBounds(0, 0, 1120, 700);
		gui.setLocation(300, 200);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setResizable(false);
		ImageIcon BG = new ImageIcon("Backgroud.jpg");
		JLabel bgLabel = new JLabel(BG);
		bgLabel.setBounds(0, 0, 1120, 700);
		JPanel bgPanel = (JPanel) gui.getContentPane();
		bgPanel.setOpaque(false);
		gui.getLayeredPane().setLayout(null);
		gui.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));
		JButton functionOpen = new JButton("���ļ�");
		functionOpen.setBounds(50, 200, 150, 40);
		functionOpen.setFont(new java.awt.Font("����", 1, 20));
		gui.add(functionOpen);
		JButton functionShow = new JButton("չʾͼƬ");
		functionShow.setBounds(50, 250, 150, 40);
		functionShow.setFont(new java.awt.Font("����", 1, 20));
		gui.add(functionShow);
		JButton functionBrig = new JButton("���ŽӴ�");
		functionBrig.setBounds(50, 300, 150, 40);
		functionBrig.setFont(new java.awt.Font("����", 1, 20));
		gui.add(functionBrig);
		JButton functionCrea = new JButton("�����¾�");
		functionCrea.setBounds(50, 350, 150, 40);
		functionCrea.setFont(new java.awt.Font("����", 1, 20));
		gui.add(functionCrea);
		JButton functionPath = new JButton("���·��");
		functionPath.setBounds(50, 400, 150, 40);
		functionPath.setFont(new java.awt.Font("����", 1, 20));
		gui.add(functionPath);
		JButton functionRand = new JButton("�������");
		functionRand.setBounds(50, 450, 150, 40);
		functionRand.setFont(new java.awt.Font("����", 1, 20));
		gui.add(functionRand);
		JButton functionExit = new JButton("�˳�����");
		functionExit.setBounds(50, 500, 150, 40);
		functionExit.setFont(new java.awt.Font("����", 1, 20));
		gui.add(functionExit);

		functionOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				str1 = OpenGui();
				words1 = str1.split("\\s+");
				Arrays.sort(words1);
				vexs1 = tranVexs(words1);
				edges1 = transMa(str1);
				edg1 = edgweight(edges1);
				adj1 = Adjacency(edg1, vexs1);
			}
		});
		functionShow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowGui();
			}
		});
		functionBrig.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BrigGui();
			}
		});
		functionCrea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CreaGui();
			}
		});
		functionPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PathGui();
			}
		});
		functionRand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RandGui();
			}
		});
		functionExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		gui.add(new JLabel());
		gui.setVisible(true);
	}
}
