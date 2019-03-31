package nlp;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import graph.ListUDG;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

import java.util.*;
import java.util.Map.Entry;

public class NlpTool {
    @SuppressWarnings("deprecation")
	public static void main(String[] args) {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization,
        // NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        //props.setProperty("annotators", "tokenize, pos, lemma, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text = "Which diseases can be cured by drug that is named Aspirin?";// Add your text here!
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and
        // has values with custom types
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        

        List<String> words = new ArrayList<>();
        List<String> posTags = new ArrayList<>();
        SemanticGraph graph = new SemanticGraph();//������ϵ
        String parser_relation = new String();
        //List<String> nerTags = new ArrayList<>();
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(TextAnnotation.class);
                words.add(word);
                // this is the POS tag of the token
                String pos = token.get(PartOfSpeechAnnotation.class);
                posTags.add(pos);
                // this is the NER label of the token
                //String ne = token.get(NamedEntityTagAnnotation.class);
                //nerTags.add(ne);
            } 
            graph = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);  
            parser_relation = graph.toString(SemanticGraph.OutputFormat.LIST);  
        }
        //System.out.print(graph.toString());
        System.out.println(words.toString());
        System.out.println(posTags.toString());
        //System.out.println(nerTags.toString());   
        List<String> Vexs = new ArrayList<>();//ͼ�ڵ�
        List<String> verb = new ArrayList<>();//��������
        String[] pos=posTags.toArray(new String[posTags.size()]);
        String[] parser = parser_relation.split("\n");
        System.out.println(Arrays.toString(parser));
        int i = 0;
        for(String s:pos){        	
        	if(s.equals("WDT")||s.equals("NN")||s.equals("NNP") || s.equals("NNS"))//�ڵ�
        	{
        		Vexs.add(words.get(i)); 
        	}
        	else if(s.equals("VB")||s.equals("VBD") || s.equals("VBN")|| s.equals("VBG")||
        			s.equals("VBP")|| s.equals("VBZ"))
        	{
        		verb.add(words.get(i));
        	}
        	i++;
        }
        
        String[] vexs = Vexs.toArray(new String[Vexs.size()]);
        String[][] edge = new String[10][2];
        ArrayList<String[]> edges =  new ArrayList<String[]>();
        List<String> Relationship = new ArrayList<>();
        
        Map<String,String> vs = new HashMap<String,String>();//���ϴʾ��ж�
        for (int j = 0; j < words.size(); j++) {
            vs.put(words.get(j), j+"");
         }
        
        ListUDG pG;
        //�������е�"ͼ"       
        int j = 0,m = 0;
        String repeat = new String();//���ظ��ӱ�
        for(String s:verb){ //�Զ���Ϊ��׼Ѱ�������Ľڵ�
        	int vexs_number = 0;
        	Map<String,String> vs1 = new HashMap<String,String>();
        	for(String p:parser) {//��parser���Ҵ����ʵĹ�ϵ
        		if(((p.indexOf("det") != -1)||(p.indexOf("whdt") != -1))&&(repeat.indexOf(p) == -1)) {
        			int l = p.indexOf("(");
        			int g1 = p.indexOf("-");
        			int r = p.indexOf(",");
        			int g2 = p.lastIndexOf("-");
        			edge[m][0] = p.substring(l+1,g1);
        			edge[m][1] = p.substring(r+2,g2);
        			edges.add(edge[m]); m++;
        			Relationship.add("whdt"); j++;
        			repeat += p;
        			continue;
        		 }	
        	     if(p.indexOf(s) != -1){//�ҵ�
        	        	int basic =  Integer.valueOf(vs.get(s)).intValue();
        	        	for(int k = 0;k < Vexs.size();k ++)  {//�ж��Ƿ�Ϊ�ڵ�
        	        		if(p.indexOf(vexs[k]) != -1) {
        	        			int distance = Math.abs(Integer.valueOf(vs.get(vexs[k])).intValue() - basic);//��������
        	        			vs1.put(vexs[k], distance+"");
        	        		}
        	        	}
        	     }
        	  }
        	List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(vs1.entrySet());//���ݾ��������
        	Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
            //��������
        		public int compare(Entry<String, String> o1,
        				Entry<String, String> o2) {
        			return o1.getValue().compareTo(o2.getValue());
        		}
            
        	});
        	for(Map.Entry<String,String> mapping:list){ //ȡ�������С��ǰ�����ڵ�
        		edge[m][vexs_number++] = mapping.getKey(); 
        		if(vexs_number == 2) {
        			edges.add(edge[m]); m++;
    	        	Relationship.add(s); j++; 
        			vexs_number = 0;
        			break;
        		}
        	 }
        	}   		
        System.out.print(repeat);
        String[] relationship = Relationship.toArray(new String[j]);
        String[][] Edges = (String [][])edges.toArray(new String[0][0]);
        //System.out.println(Arrays.toString(vexs));
        //System.out.println(Arrays.deepToString(Edges));
        //System.out.println(Arrays.toString(relationship));        
        //��VBΪ��׼Ѱ�ҹ�ϵ������ͼ�ṹ
        pG = new ListUDG(vexs, Edges,relationship);
        pG.print();   //��ӡͼ
    }

}
