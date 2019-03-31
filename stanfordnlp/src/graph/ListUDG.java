package graph;

import java.io.IOException;

import java.util.*;
import java.io.StringReader;

public class ListUDG {
	
	    // �ڽӱ��б��Ӧ������Ķ���
	    private class ENode {
	        int ivex;       // �ñ���ָ��Ķ����λ��
	        String relation;
	        ENode nextEdge; // ָ����һ������ָ��
	    }
	    // �ڽӱ��б�Ķ���
	    private class VNode {
	        String data;          // ������Ϣ
	        ENode firstEdge;    // ָ���һ�������ö���Ļ�
	    };

	    private VNode[] mVexs;  // ��������
	    private int vlen,elen;

	    /*����ͼ
	     *     vexs  -- ��������
	     *     edges -- ������
	     */
	    public ListUDG(String[] vexs, String[][] edges,String[] Relation) {
	        
	        // ��ʼ��"������"��"����"
	        vlen = vexs.length;
	        elen = edges.length;
	        // ��ʼ��"����"
	        mVexs = new VNode[vlen];
	        for (int i = 0; i < vlen; i++) {
	            mVexs[i] = new VNode();
	            mVexs[i].data = vexs[i];
	            mVexs[i].firstEdge = null;
	        }

	        // ��ʼ��"��"
	        for (int i = 0; i < elen; i++) {
	            // ��ȡ�ߵ���ʼ����ͽ�������
	            int p1 = getPosition(edges[i][0]);
	            int p2 = getPosition(edges[i][1]);
	         // ��ʼ��node1
	            ENode node1 = new ENode();
	            node1.relation = Relation[i];
	            node1.ivex = p2;
	            // ��node1���ӵ�"p1���������ĩβ"
	            if(mVexs[p1].firstEdge == null)
	              mVexs[p1].firstEdge = node1;
	            else
	                linkLast(mVexs[p1].firstEdge, node1);
	            // ��ʼ��node2
	            ENode node2 = new ENode();
	            node2.relation = Relation[i];
	            node2.ivex = p1;
	            // ��node2���ӵ�"p2���������ĩβ"
	            if(mVexs[p2].firstEdge == null)
	              mVexs[p2].firstEdge = node2;
	            else
	                linkLast(mVexs[p2].firstEdge, node2);
	        }
	    }

	    /*
	     * ��node�ڵ����ӵ�list�����
	     */
	    private void linkLast(ENode list, ENode node) {
	        ENode p = list;
	        while(p.nextEdge!=null)
	            p = p.nextEdge;
	        p.nextEdge = node;
	    }

	    /*
	     * ����chλ��
	     */
	    private int getPosition(String edges) {
	        for(int i=0; i<vlen; i++)
	            if(mVexs[i].data.equals(edges))
	                return i;
	        return -1;
	    }

	    /*
	     * ��ӡ�������ͼ
	     */
	    public void print() {
	        System.out.printf("List Graph:\n");
	        for (int i = 0; i < mVexs.length; i++) {
	            System.out.printf("%d(%s): ", i, mVexs[i].data);
	            ENode node = mVexs[i].firstEdge;
	            while (node != null) {
	                System.out.printf(node.ivex+"("+mVexs[node.ivex].data+"-"+mVexs[node.ivex].firstEdge.relation+") ");
	                node = node.nextEdge;
	            }
	            System.out.printf("\n");
	        }
	    }

	    public static void main(String[] args) {
	        String[] vexs = {"What", "diseases","Aspirin"};
	        String[][] edges = new String[][]{
	            {"What", "diseases"}, 
	            {"diseases", "Aspirin"}
	            };
	        String[] relationship = {"WHDT","cure"};
	        ListUDG pG;
	        //�������е�"ͼ"
	        pG = new ListUDG(vexs, edges,relationship);
	        pG.print();   //��ӡͼ
	    }
}
