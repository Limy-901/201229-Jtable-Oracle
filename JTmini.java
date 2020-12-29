import java.sql.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

class JTmini extends JFrame {
    Connection con;
    PreparedStatement DBsmt,searchpsmt;
    ResultSet rs;
    Statement stmt;
    
    String url = "jdbc:oracle:thin:@127.0.0.1:1521:JAVA";
    String usr = "scott";
    String pwd = "tiger";
    String searchMode;
    int selectedRow, selectedColumn;
    
    JButton B_add,B_update,B_delete;
    JComboBox < String > combo;
    JTextField search,T_dno,T_dname,T_loc;
    JTable t;
    
    Vector < Vector > rowdata;
    Vector < String > columnNames;
    Vector < Vector > update;
    DefaultTableModel model = new DefaultTableModel();
    
    JTmini() {
        columnNames = new Vector<String>();
        rowdata = new Vector<Vector>();
        update = selectView();
        columnNames.add("DEPTNO");columnNames.add("DNAME");columnNames.add("LOC");
        model.setDataVector(update, columnNames);
        init();
    }
    void init() {
    	searchMode="DEPTNO";
        Container cp = getContentPane();
        JPanel jp_TABLE = new JPanel();
        JPanel jp_NORTH = new JPanel();
        JPanel jp_SOUTHT = new JPanel();
        JPanel jp_SOUTHB = new JPanel();
        JPanel jp_SOUTH = new JPanel();
        combo = new JComboBox<String>(columnNames);
        B_add = new JButton("추가");
        B_update = new JButton("수정");
        B_delete = new JButton("삭제");
        search = new JTextField();
        T_dno = new JTextField();
        T_dname = new JTextField();
        T_loc = new JTextField();
        t = new JTable(model);
        JScrollPane sp = new JScrollPane(t);
        jp_TABLE.setLayout(new GridLayout());
        jp_TABLE.add(sp);
        jp_NORTH.setLayout(new GridLayout());
        jp_SOUTH.setLayout(new BorderLayout());
        jp_SOUTHB.setLayout(new GridLayout());
        jp_SOUTHT.setLayout(new GridLayout());
        jp_SOUTHT.setPreferredSize(new Dimension(300, 30));
        cp.add(jp_TABLE);
        cp.add(jp_SOUTH, BorderLayout.SOUTH);
        cp.add(jp_NORTH, BorderLayout.NORTH);
        jp_SOUTHB.add(B_add);
        jp_SOUTHB.add(B_update);
        jp_SOUTHB.add(B_delete);
        jp_SOUTHT.add(T_dno);
        jp_SOUTHT.add(T_dname);
        jp_SOUTHT.add(T_loc);
        jp_NORTH.add(combo);
        jp_NORTH.add(search);
        jp_SOUTH.add(jp_SOUTHT, BorderLayout.NORTH);
        jp_SOUTH.add(jp_SOUTHB, BorderLayout.SOUTH);
        ActionListener al = (ActionListener) new ActionHandler(this);
        MouseListener ml = (MouseListener) new ActionHandler(this);
        KeyListener kl = (KeyListener) new ActionHandler(this);
        t.addMouseListener(ml);
        search.addKeyListener(kl);
        B_add.addActionListener(al);
        B_update.addActionListener(al);
        B_delete.addActionListener(al);
        combo.addActionListener(al);
        setUI();
    }
    void setUI() {
        setTitle("JTABLE TEST1");
        setSize(300, 400);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    Vector selectView() { 
    	String allSql = "select * from DEPT order by DEPTNO";
    	String deptno,dname,loc;
        rowdata.clear();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(url, usr, pwd);
            stmt = con.createStatement();
            rs = stmt.executeQuery(allSql);
            int count = 0;
            while (rs.next()) {
                Vector insert = new Vector<String>();
                deptno = rs.getString(1);
                dname = rs.getString(2);
                loc = rs.getString(3);
                insert.add(deptno);
                insert.add(dname);
                insert.add(loc);
                rowdata.add(insert);
            }
        } catch (ClassNotFoundException cnfe) {
        } catch (SQLException se) {        	
        } finally {
        	try {
        		rs.close();
        		stmt.close();
        	}catch(SQLException se) {}
        }
        return rowdata;
    }
	Vector searchData(String searchword, String sql) {
    	String deptno,dname,loc;
    	rowdata.clear();
    	try {
    		searchpsmt = con.prepareStatement(sql);
    		searchpsmt.setString(1, "%"+searchword+"%");
    		rs = searchpsmt.executeQuery();
    		while(rs.next()) {
    			Vector insert = new Vector<String>();
    			deptno = rs.getString(1);
                dname = rs.getString(2);
                loc = rs.getString(3);
                insert.add(deptno);
                insert.add(dname);
                insert.add(loc);
                rowdata.add(insert);
    		}
    	} catch (SQLException se) {        	
        } finally {
        	try {
        		rs.close();
        		searchpsmt.close();
        	}catch(SQLException se) {}
        }
    	model.setDataVector(update, columnNames);
		return rowdata;
	}
    void insertD() {
    	String addSql = "insert into DEPT values(?,?,?)";
    	String deptno = T_dno.getText();
        String dname = T_dname.getText();
        String loc = T_loc.getText();
        int dnum = Integer.parseInt(deptno);
        try {
        	DBsmt = con.prepareStatement(addSql);
        	DBsmt.setInt(1, dnum);
        	DBsmt.setString(2, dname);
        	DBsmt.setString(3, loc);
        	DBsmt.executeUpdate();
        }catch(SQLException se) {
        }finally {
    		try {
    			DBsmt.close();
    		}catch(SQLException se) { }
        }
        update = selectView();
        model.setDataVector(update, columnNames);
        T_dno.setText("");
        T_dname.setText("");
        T_loc.setText("");
    }
    void updateD() {
    	String updateSql = "update DEPT set DEPTNO=?,DNAME=?,LOC=? where DEPTNO=?";
    	String deptno = T_dno.getText();
        String dname = T_dname.getText();
        String loc = T_loc.getText();
        int dnum = Integer.parseInt(deptno);
    	try {
    		DBsmt = con.prepareStatement(updateSql);
    		DBsmt.setInt(1, dnum);
    		DBsmt.setString(2, dname);
    		DBsmt.setString(3, loc);
    		DBsmt.setInt(4, dnum);
    		DBsmt.executeUpdate();
    	}catch(SQLException se) {
    	}finally {
    		try {
    			DBsmt.close();
    		}catch(SQLException se) { }
    	}
    	update = selectView();
        model.setDataVector(update, columnNames);
        T_dno.setText("");
        T_dname.setText("");
        T_loc.setText("");
    }
    void deleteD() {
    	String deleteSql = "delete from DEPT where DEPTNO=?";
        String deptno = T_dno.getText();
        int dnum = Integer.parseInt(deptno);
        try {
        	DBsmt = con.prepareStatement(deleteSql);
        	DBsmt.setInt(1, dnum);
        	DBsmt.executeUpdate();
        }catch(SQLException se) {
        }finally {
    		try {
    			DBsmt.close();
    		}catch(SQLException se) { }
        }
        update = selectView();
        model.setDataVector(update, columnNames);
        T_dno.setText("");
        T_dname.setText("");
        T_loc.setText("");
    }
    void pln(String str) {
        System.out.println(str);
    }
    public static void main(String[] args) {
        JTmini j = new JTmini();
    }
}