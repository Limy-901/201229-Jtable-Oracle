import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ActionHandler implements ActionListener, MouseListener, KeyListener {
	JTmini jt;
	String clicked_dno,clicked_dna,clicked_loc;
	String deptnoSql = "select * from DEPT where DEPTNO like ?";
	String dnameSql ="select * from DEPT where DNAME like ?";
	String locSql = "select * from DEPT where LOC like ?";
	
	ActionHandler(JTmini jt){
		this.jt = jt;
	}
	public void keyReleased(KeyEvent e) {
		String searchword = jt.search.getText().trim();
		if(searchword.length() > 0) {
    		switch(jt.searchMode) {
    	    case "DEPTNO": jt.update=jt.searchData(searchword,deptnoSql); break;
    	    case "DNAME": jt.update=jt.searchData(searchword,dnameSql); break;
    	    case "LOC": jt.update=jt.searchData(searchword,locSql); break;
    		}
		}else if(searchword.length() == 0) {
			jt.update=jt.selectView();
			jt.model.setDataVector(jt.update, jt.columnNames);
		}
	}
	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource();
    	if(obj==jt.t) {
    		jt.selectedRow = jt.t.getSelectedRow();
    		jt.pln(jt.selectedRow+"±×¸®°í~"+jt.selectedColumn);
	        clicked_dno = (String)(jt.rowdata.get(jt.selectedRow)).get(0);
	        clicked_dna = (String)(jt.rowdata.get(jt.selectedRow)).get(1);
	        clicked_loc = (String)(jt.rowdata.get(jt.selectedRow)).get(2);
	        jt.T_dno.setText(clicked_dno);
	        jt.T_dname.setText(clicked_dna);
	        jt.T_loc.setText(clicked_loc);
    	}	
	}
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
        if (obj.equals(jt.combo)) {
            String selected = jt.combo.getSelectedItem().toString();
            if (selected.equals("DEPTNO")) jt.searchMode="DEPTNO";
            else if (selected.equals("DNAME")) jt.searchMode="DNAME";
            else if (selected.equals("LOC")) jt.searchMode="LOC";   
        }
        else if (obj == jt.B_add)  jt.insertD();
        else if (obj == jt.B_update) jt.updateD();
        else if (obj == jt.B_delete) jt.deleteD();
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
}
