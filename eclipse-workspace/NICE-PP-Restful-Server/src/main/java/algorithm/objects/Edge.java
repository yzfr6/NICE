package algorithm.objects;

public class Edge {

	public int id;
	public Node source;
	public Node target;
	public int type;
	public Edge(int db_id, Node source, Node target, int type) {
		super();
		this.id = db_id;
		this.source = source;
		this.target = target;
		this.type = type;
	};
	
	
	
	
	
}
