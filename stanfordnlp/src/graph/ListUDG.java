package graph;

import java.io.IOException;

import java.util.*;
import java.io.StringReader;

public class ListUDG {
	
	    // 邻接表中表对应的链表的顶点
	    private class ENode {
	        int ivex;       // 该边所指向的顶点的位置
	        String relation;
	        ENode nextEdge; // 指向下一条弧的指针
	    }
	    // 邻接表中表的顶点
	    private class VNode {
	        String data;          // 顶点信息
	        ENode firstEdge;    // 指向第一条依附该顶点的弧
	    };

	    private VNode[] mVexs;  // 顶点数组
	    private int vlen,elen;

	    /*创建图
	     *     vexs  -- 顶点数组
	     *     edges -- 边数组
	     */
	    public ListUDG(String[] vexs, String[][] edges,String[] Relation) {
	        
	        // 初始化"顶点数"和"边数"
	        vlen = vexs.length;
	        elen = edges.length;
	        // 初始化"顶点"
	        mVexs = new VNode[vlen];
	        for (int i = 0; i < vlen; i++) {
	            mVexs[i] = new VNode();
	            mVexs[i].data = vexs[i];
	            mVexs[i].firstEdge = null;
	        }

	        // 初始化"边"
	        for (int i = 0; i < elen; i++) {
	            // 读取边的起始顶点和结束顶点
	            int p1 = getPosition(edges[i][0]);
	            int p2 = getPosition(edges[i][1]);
	         // 初始化node1
	            ENode node1 = new ENode();
	            node1.relation = Relation[i];
	            node1.ivex = p2;
	            // 将node1链接到"p1所在链表的末尾"
	            if(mVexs[p1].firstEdge == null)
	              mVexs[p1].firstEdge = node1;
	            else
	                linkLast(mVexs[p1].firstEdge, node1);
	            // 初始化node2
	            ENode node2 = new ENode();
	            node2.relation = Relation[i];
	            node2.ivex = p1;
	            // 将node2链接到"p2所在链表的末尾"
	            if(mVexs[p2].firstEdge == null)
	              mVexs[p2].firstEdge = node2;
	            else
	                linkLast(mVexs[p2].firstEdge, node2);
	        }
	    }

	    /*
	     * 将node节点链接到list的最后
	     */
	    private void linkLast(ENode list, ENode node) {
	        ENode p = list;
	        while(p.nextEdge!=null)
	            p = p.nextEdge;
	        p.nextEdge = node;
	    }

	    /*
	     * 返回ch位置
	     */
	    private int getPosition(String edges) {
	        for(int i=0; i<vlen; i++)
	            if(mVexs[i].data.equals(edges))
	                return i;
	        return -1;
	    }

	    /*
	     * 打印矩阵队列图
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
	        //采用已有的"图"
	        pG = new ListUDG(vexs, edges,relationship);
	        pG.print();   //打印图
	    }
}
