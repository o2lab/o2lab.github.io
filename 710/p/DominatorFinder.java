import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.toolkits.graph.DirectedGraph;


public class DominatorFinder<N> {
	protected DirectedGraph<N> graph;
    protected BitSet fullSet;
    protected List<N> heads;
    protected Map<N,BitSet> nodeToFlowSet;
    protected Map<N,Integer> nodeToIndex;
    protected Map<Integer,N> indexToNode;
    protected int lastIndex = 0;

    public DominatorFinder(DirectedGraph<N> graph)
    {
        this.graph = graph;
        doAnalysis();
    }

    protected void doAnalysis()
    {
        heads = graph.getHeads();
        nodeToFlowSet = new HashMap<N, BitSet>();
        nodeToIndex = new HashMap<N, Integer>();
        indexToNode = new HashMap<Integer,N>();

        //build full set
        fullSet = new BitSet(graph.size());
        fullSet.flip(0, graph.size());//set all to true

        //set up domain for intersection: head nodes are only dominated by themselves,
        //other nodes are dominated by everything else
        for(Iterator<N> i = graph.iterator(); i.hasNext();){
            N o = i.next();
            if(heads.contains(o)){
                BitSet self = new BitSet();
                self.set(indexOf(o));
                nodeToFlowSet.put(o, self);
            }
            else{
                nodeToFlowSet.put(o, fullSet);
            }
        }

        boolean changed = true;
        do{
            changed = false;
            for(Iterator<N> i = graph.iterator(); i.hasNext();){
                N o = i.next();
                if(heads.contains(o)) continue;

                //initialize to the "neutral element" for the intersection
                //this clone() is fast on BitSets (opposed to on HashSets)
				BitSet predsIntersect = (BitSet) fullSet.clone();

                //intersect over all predecessors
                for(Iterator<N> j = graph.getPredsOf(o).iterator(); j.hasNext();){
                    BitSet predSet = nodeToFlowSet.get(j.next());
                    predsIntersect.and(predSet);
                }

                BitSet oldSet = nodeToFlowSet.get(o);
                //each node dominates itself
                predsIntersect.set(indexOf(o));
                if(!predsIntersect.equals(oldSet)){
                    nodeToFlowSet.put(o, predsIntersect);
                    changed = true;
                }
            }
        } while(changed);
    }

    protected int indexOf(N o) {
        Integer index = nodeToIndex.get(o);
        if(index==null) {
            index = lastIndex;
            nodeToIndex.put(o,index);
            indexToNode.put(index,o);
            lastIndex++;
        }
        return index;
    }

    public DirectedGraph<N> getGraph()
    {
        return graph;
    }

    public List<N> getDominators(Object node)
    {
        //reconstruct list of dominators from bitset
        List<N> result = new ArrayList<N>();
        BitSet bitSet = nodeToFlowSet.get(node);
        for(int i=0;i<bitSet.length();i++) {
            if(bitSet.get(i)) {
                result.add(indexToNode.get(i));
            }
        }
        return result;
    }
    }
