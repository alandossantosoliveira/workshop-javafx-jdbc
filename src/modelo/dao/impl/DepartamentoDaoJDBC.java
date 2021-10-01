package modelo.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import modelo.dao.DepartamentoDao;
import modelo.entidades.Departamento;

public class DepartamentoDaoJDBC implements DepartamentoDao{
	
	private Connection conn;
	
	public DepartamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Departamento obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("insert into departamento (nome) values (?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Departamento obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("update departamento "
					+ "set nome = ? "
					+ "where id = ?");
			
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"delete from departamento "
					+ "where id = ?");
			st.setInt(1, id);
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected == 0) {
				throw new DbException("Id not exist!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Departamento findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT *from departamento " +
					"WHERE Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Departamento dep = instantiateDepartamento(rs);
				return dep;
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<Departamento> findAll() {
		
		List<Departamento> list = new ArrayList<>();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT *from departamento");
			
			rs = st.executeQuery();
			
			while(rs.next()) {
				Departamento dep = instantiateDepartamento(rs);
				list.add(dep);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	private Departamento instantiateDepartamento(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("id"));
		dep.setNome(rs.getString("nome"));
		return dep;
	}

}
