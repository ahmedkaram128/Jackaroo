package engine.board;

import model.player.Marble;

public class BoardChange {
	public Marble marble;
	public int init; // initial index in arraylist
	public int from; // 4 track, i<4 homeZone, i>4 safezone, j index
	public int target; // final index in arraylist
	public int to; //  4 track, <4 homeZone, >4 safezone
	
	// [i,j] 
	public BoardChange(Marble marble, int init, int from, int target, int to){
		this.marble = marble; 
		this.init = init; 
		this.from = from; 
		this.target = target; 
		this.to = to; 
	}
}
